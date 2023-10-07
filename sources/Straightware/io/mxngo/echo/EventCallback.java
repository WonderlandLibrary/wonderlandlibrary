package io.mxngo.echo;


import java.util.Arrays;
import java.util.function.Consumer;

/**
 * The {@code EventCallback} class represents a callback function for a specific event type, along with an optional
 * array of filters that must pass for the callback to be executed.
 *
 * @param <T> the type of event this callback can handle
 */
public interface EventCallback<T> {
    void invokeEvent(T event);
}
