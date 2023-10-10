package ravenweave.client.event.ext;

public interface IEventDirection {

    EventDirection getDirection();

    default boolean isIncoming() {
        return getDirection() == EventDirection.INCOMING;
    }

    default boolean isOutgoing() {
        return getDirection() == EventDirection.OUTGOING;
    }

}
