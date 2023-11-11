package me.nyan.flush.ui.discord;

import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;
import me.nyan.flush.Flush;
import net.minecraft.util.Util;

import java.util.concurrent.atomic.AtomicBoolean;

public class DiscordRP {
    private final AtomicBoolean running = new AtomicBoolean();
    private final long created = System.currentTimeMillis();

    private String nextFirstLine, nextSecondLine;
    private String currentFirstLine, currentSecondLine;

    private void discordInit() {
        DiscordRPC.discordInitialize("982948593663094844", new DiscordEventHandlers.Builder().build(), true);
        running.set(true);
    }

    public void start() {
        if (Util.getOSType() == Util.EnumOS.OSX) {
            return;
        }

        discordInit();

        if (nextFirstLine != null && nextSecondLine != null) {
            update(nextFirstLine, nextSecondLine);
            nextFirstLine = nextSecondLine = null;
        }

        final Thread thread = new Thread("Discord RPC Callback") {
            public void run() {
                while (running.get()) {
                    DiscordRPC.discordRunCallbacks();
                }
            }
        };
        thread.setDaemon(true);
        thread.start();
    }

    public void shutdown() {
        if (Util.getOSType() == Util.EnumOS.OSX) return;

        running.set(false);
        DiscordRPC.discordShutdown();

        nextFirstLine = currentFirstLine;
        nextSecondLine = currentSecondLine;

        currentFirstLine = null;
        currentSecondLine = null;
    }

    public void update(String firstLine, String secondLine) {
        if (Util.getOSType() == Util.EnumOS.OSX) return;

        if (!running.get()) {
            this.nextFirstLine = firstLine;
            this.nextSecondLine = secondLine;
            return;
        }

        currentFirstLine = firstLine;
        currentSecondLine = secondLine;

        DiscordRichPresence.Builder builder = new DiscordRichPresence.Builder(secondLine);
        builder.setBigImage("flush_1024x1024", Flush.NAME + " " + Flush.VERSION);
        builder.setDetails(firstLine);
        builder.setStartTimestamps(created);

        DiscordRPC.discordUpdatePresence(builder.build());
    }

    public boolean isRunning() {
        return running.get();
    }
}
