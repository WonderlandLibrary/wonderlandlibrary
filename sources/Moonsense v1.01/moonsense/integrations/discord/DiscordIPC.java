// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.integrations.discord;

import org.json.JSONObject;
import com.jagrosh.discordipc.entities.RichPresence;
import com.jagrosh.discordipc.entities.pipe.PipeStatus;
import moonsense.MoonsenseClient;
import com.jagrosh.discordipc.exceptions.NoDiscordClientException;
import com.jagrosh.discordipc.entities.DiscordBuild;
import java.time.OffsetDateTime;
import se.michaelthelin.spotify.model_objects.specification.AlbumSimplified;
import se.michaelthelin.spotify.model_objects.miscellaneous.CurrentlyPlaying;
import com.jagrosh.discordipc.IPCClient;
import com.jagrosh.discordipc.IPCListener;

public class DiscordIPC implements IPCListener
{
    public static final DiscordIPC INSTANCE;
    private IPCClient client;
    private CurrentlyPlaying currentlyPlaying;
    private AlbumSimplified currentlyPlayingAlbum;
    private OffsetDateTime timestamp;
    
    static {
        INSTANCE = new DiscordIPC();
    }
    
    public void init() {
        (this.client = new IPCClient(971237541384687616L)).setListener(this);
        try {
            this.client.connect(new DiscordBuild[0]);
        }
        catch (NoDiscordClientException e) {
            e.printStackTrace();
        }
        catch (Exception e2) {
            MoonsenseClient.error("UNKOWN ERROR", new Object[0]);
            e2.printStackTrace();
        }
        MoonsenseClient.info("IPC {} -> {}", this.client.getStatus(), this.client.getDiscordBuild());
    }
    
    public void shutdown() {
        if (this.client != null && this.client.getStatus() == PipeStatus.CONNECTED) {
            this.client.close();
            MoonsenseClient.info("Discord IPC closed!", new Object[0]);
        }
    }
    
    @Override
    public void onReady(final IPCClient client) {
        final RichPresence.Builder builder = new RichPresence.Builder().setState("Minecraft 1.8.9").setDetails("In Game").setLargeImage("logo", "Moonsense Client 1.8.9").setStartTimestamp(this.timestamp);
        client.sendRichPresence(builder.build());
    }
    
    @Override
    public void onClose(final IPCClient client, final JSONObject json) {
    }
    
    @Override
    public void onDisconnect(final IPCClient client, final Throwable t) {
    }
}
