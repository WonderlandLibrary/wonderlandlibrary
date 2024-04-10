package info.sigmaclient.sigma;

import com.jagrosh.discordipc.IPCClient;
import com.jagrosh.discordipc.IPCListener;
import com.jagrosh.discordipc.entities.RichPresence;
import com.jagrosh.discordipc.exceptions.NoDiscordClientException;
import info.sigmaclient.sigma.premium.PremiumManager;
import info.sigmaclient.sigma.sigma5.SelfDestructManager;
import org.apache.logging.log4j.Logger;

import java.time.OffsetDateTime;
import java.util.Date;

import static info.sigmaclient.sigma.SigmaNG.getClientVersion;

public class DiscordRPC {
    public static OffsetDateTime time;
    private static final Logger LOGGER = org.apache.logging.log4j.LogManager.getLogger();
    public static void load() {
        IPCClient client = new IPCClient(1134419237365100684L);
        time = OffsetDateTime.now();
        client.setListener(new IPCListener() {
            @Override
            public void onReady(IPCClient client) {
                update(client);
                new Thread(() -> {
                    while (true) {
                        if(SelfDestructManager.destruct){
                            client.close();
                            return;
                        }
                        update(client);
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException ignored) {
                        }
                    }
                });
            }
        });
        try {
            client.connect();
        } catch (NoDiscordClientException e) {
            LOGGER.warn("No discord client");
        } catch (Exception e){
            LOGGER.warn("No discord client! #2");
        }
    }
    static int state = 0;
    private static void update(IPCClient client) {
        state ++;
        String rr = "SigmaNG 1.16.4 b" + SigmaNG.getClientVersion();
        RichPresence.Builder builder = new RichPresence.Builder();
        builder.setDetails(rr)
                .setStartTimestamp(time)
                .setLargeImage("sb");
        builder.setState("Dev FallenAngeler Editing");
        if(PremiumManager.isPremium){
            builder.setSmallImage("sb", "Premium");
        }
        client.sendRichPresence(builder.build());
    }

    static void stop() {
    }
}
