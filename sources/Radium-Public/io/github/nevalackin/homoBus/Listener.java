package io.github.nevalackin.homoBus;

@FunctionalInterface
public interface Listener<Event> {
    void call(Event event);
}