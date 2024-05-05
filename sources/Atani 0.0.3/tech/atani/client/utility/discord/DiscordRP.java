package tech.atani.client.utility.discord;

import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;
import tech.atani.client.loader.Modification;
import tech.atani.client.utility.interfaces.Methods;

public class DiscordRP implements Methods {

    public static void startup() {
        DiscordEventHandlers handlers = new DiscordEventHandlers.Builder().setReadyEventHandler((user) -> {
            System.out.println("Welcome " + user.username + "!");
        }).build();
        DiscordRPC.discordInitialize("1141582938127998988", handlers, true);
    }

    public static void create() {
        final DiscordRichPresence rich = new DiscordRichPresence.Builder("Logging in...")
                .setBigImage("icon", getText())
                .build();
        DiscordRPC.discordUpdatePresence(rich);
    }


    public static void update(final String details, final String state) {
        new Thread(() -> {
            final DiscordRichPresence rich = new DiscordRichPresence.Builder(state)
                    .setBigImage("icon", getText())
                    .setDetails(details)
                    .setStartTimestamps(Modification.INSTANCE.initTime)
                    .build();
            DiscordRPC.discordUpdatePresence(rich);
        }).start();
    }

    public static void update(final String state) {
        new Thread(() -> {
            final DiscordRichPresence rich = new DiscordRichPresence.Builder(state)
                    .setBigImage("icon", getText())
                    .setStartTimestamps(Modification.INSTANCE.initTime)
                    .build();
            DiscordRPC.discordUpdatePresence(rich);
        }).start();
    }

    public static void end() {
        DiscordRPC.discordShutdown();
    }

    public final static String getText() {
        return CLIENT_NAME + " " + CLIENT_VERSION + ((BETA_SWITCH || DEVELOPMENT_SWITCH) ? BETA_SWITCH ? " (Beta)" : "(Developement)" : "");
    }

}
