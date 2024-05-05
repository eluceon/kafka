package com.github.eluceon.kafka.config;

import com.github.eluceon.kafka.config.properties.KafkaConsumerProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.config.SslConfigs;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.support.converter.JsonMessageConverter;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class KafkaConsumerConfiguration {
    private final KafkaConsumerProperties properties;

    @Bean
    public ConcurrentMessageListenerContainer<String, String> messageListenerContainer(
            ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory,
            MessageListener<?, ?> messageListener
    ) {
        final ConcurrentMessageListenerContainer<String, String> container =
                kafkaListenerContainerFactory.createContainer(properties.getTopics().toArray(String[]::new));
        container.setupMessageListener(messageListener);
        container.setConcurrency(properties.getConsumerConcurrency());
        container.start();
        return container;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory(
            ConsumerFactory<String, String> consumerFactory
    ) {
        final ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        factory.setMessageConverter(new JsonMessageConverter());
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        return factory;
    }

    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        final Map<String, Object> configs = new HashMap<>();
        KafkaConsumerProperties.Ssl ssl = properties.getSsl();

        configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, properties.getBootstrapServers());
        configs.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, properties.getSecurity().getProtocol());
        if (ssl.getEnabled()) {
            configs.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, ssl.getTruststoreLocation());
            configs.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, ssl.getTruststorePassword());
            setConfigs(configs, SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG, ssl.getKeystoreLocation());
            setConfigs(configs, SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG, ssl.getKeystorePassword());
            setConfigs(configs, SslConfigs.SSL_KEY_PASSWORD_CONFIG, ssl.getKeyPassword());
            setConfigs(configs, SslConfigs.SSL_KEYSTORE_TYPE_CONFIG, ssl.getKeystoreType());
            setConfigs(configs, SslConfigs.SSL_TRUSTSTORE_TYPE_CONFIG, ssl.getTruststoreType());
        }
        configs.put(ConsumerConfig.CLIENT_ID_CONFIG, properties.getClientId());
        configs.put(ConsumerConfig.GROUP_ID_CONFIG, properties.getGroupId());
        configs.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        configs.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, properties.getSessionTimeoutMs());
        configs.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, properties.getAutoOffsetReset());
        configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        configs.values().removeIf(Objects::isNull);
        return new DefaultKafkaConsumerFactory<>(configs);
    }

    private static void setConfigs(Map<String, Object> configs, String key, String value) {
        if (StringUtils.hasText(value)) {
            configs.put(key, value);
        }
    }
}
