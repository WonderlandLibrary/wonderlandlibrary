package net.caffeinemc.phosphor.api.event.events;

@SuppressWarnings("all")
public class GameJoinedEvent {

    private static final GameJoinedEvent INSTANCE = new GameJoinedEvent();

    public static GameJoinedEvent get() {
        return INSTANCE;
    }

}
