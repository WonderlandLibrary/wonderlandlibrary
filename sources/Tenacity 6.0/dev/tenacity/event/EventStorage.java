package dev.tenacity.event;

public class EventStorage<T> {

    private final Object owner;
    private final IEventListener<T> callback;

    public EventStorage(Object owner, IEventListener<T> callback) {
        this.owner = owner;
        this.callback = callback;
    }

    public Object getOwner() {
        return owner;
    }

    public IEventListener<T> getCallback() {
        return callback;
    }

}
