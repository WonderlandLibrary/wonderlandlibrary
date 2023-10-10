package ravenweave.client.event.ext;

public interface IEventTiming {
    EventTiming getTiming();

    default boolean isPre() {
        return getTiming() == EventTiming.PRE;
    }

    default boolean isPost() {
        return getTiming() == EventTiming.POST;
    }

}
