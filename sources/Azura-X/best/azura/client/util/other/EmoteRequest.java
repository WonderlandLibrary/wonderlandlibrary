package best.azura.client.util.other;

public class EmoteRequest {

    private final String username, emote;
    private long emoteInitTime;

    public EmoteRequest(String username, String emote) {
        this.username = username;
        this.emote = emote;
    }

    public void startEmote() {
        emoteInitTime = System.currentTimeMillis();
    }

    public String getUsername() {
        return username;
    }

    public String getEmote() {
        return emote;
    }

    public long getEmoteInitTime() {
        return emoteInitTime;
    }
}
