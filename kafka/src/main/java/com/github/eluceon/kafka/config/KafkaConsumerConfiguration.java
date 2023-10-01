package com.github.eluceon.kafka.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
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

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class KafkaConsumerConfiguration {
    private final KafkaConsumerProperties properties;

    @Bean
    public ConcurrentMessageListenerContainer<String, String> messageListenerContainer (
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

        configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, properties.getBootstrapServers());
        configs.put(ConsumerConfig.GROUP_ID_CONFIG, properties.getGroupId());
        configs.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        configs.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, properties.getSessionTimeoutMs());
        configs.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, properties.getAutoOffsetReset());
        configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        configs.values().removeIf(Objects::isNull);
        return new DefaultKafkaConsumerFactory<>(configs);
    }
}
