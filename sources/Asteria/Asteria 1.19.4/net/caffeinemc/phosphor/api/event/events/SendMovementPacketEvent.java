package net.caffeinemc.phosphor.api.event.events;

@SuppressWarnings("all")
public class SendMovementPacketEvent {

    public static class Pre extends SendMovementPacketEvent {
        private static final Pre INSTANCE = new Pre();

        public static Pre get() {
            return INSTANCE;
        }
    }

    public static class Post extends SendMovementPacketEvent {
        private static final Post INSTANCE = new Post();

        public static Post get() {
            return INSTANCE;
        }
    }
	
}
