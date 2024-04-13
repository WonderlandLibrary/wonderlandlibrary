package dev.tenacity.event;

public interface IEventListener<Event> {

    void invoke(final Event event);

}
