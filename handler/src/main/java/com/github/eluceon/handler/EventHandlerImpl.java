package com.github.eluceon.handler;

import com.github.eluceon.handler.dto.EventDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EventHandlerImpl implements EventHandler<EventDto> {
    @Override
    public void handle(EventDto event) {
        log.info("handle event, code = [{}]", event.getCode());
    }
}
