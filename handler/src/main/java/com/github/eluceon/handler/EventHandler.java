package com.github.eluceon.handler;

/**
 * Интерфейс обработки событий
 *
 * @param <T> тип события
 */
public interface EventHandler<T> {
    /**
     * Обработать пришедшее событие
     *
     * @param event событие для обработки
     */
    void handle(T event);
}
