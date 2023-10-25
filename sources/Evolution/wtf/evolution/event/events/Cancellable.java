package wtf.evolution.event.events;

public interface Cancellable {

    boolean isCancelled();

    void cancel();

}
