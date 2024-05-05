package net.caffeinemc.phosphor.api.event.events;

@SuppressWarnings("all")
public class HandleInputEvent {

    public static class Pre extends HandleInputEvent {
        private static final Pre INSTANCE = new Pre();

        public static Pre get() {
            return INSTANCE;
        }
    }

    public static class Post extends HandleInputEvent {
        private static final Post INSTANCE = new Post();

        public static Post get() {
            return INSTANCE;
        }
    }
	
}
