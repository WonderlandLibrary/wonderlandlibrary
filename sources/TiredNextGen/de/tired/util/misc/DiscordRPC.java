package de.tired.util.misc;

import de.tired.base.interfaces.IHook;
import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRichPresence;


public class DiscordRPC implements IHook {

    public static DiscordRPC instance;

    public DiscordRPC() {
        instance = this;
    }

    public boolean state = true;
    public long time = 0;

    public void start() {

        this.time = System.currentTimeMillis();

        DiscordEventHandlers handlers = new DiscordEventHandlers.Builder().setReadyEventHandler(discordUser -> {
            System.out.println("start");
            setStage(new String[]{"Loading", ""});
        }).build();
        net.arikia.dev.drpc.DiscordRPC.discordInitialize("710896112734830634", handlers, true);
        new Thread("RPC Callback") {
            @Override
            public void run() {
                net.arikia.dev.drpc.DiscordRPC.discordRunCallbacks();
                super.run();
            }
        }.start();
    }

    public void disState() {
        state = false;
        time = 0;
    }


    public void setStage(String[] strings) {
        assert strings[0] != null && strings[1] != null;

        DiscordRichPresence.Builder richPresence = new DiscordRichPresence.Builder(strings[1]);
        richPresence.setBigImage("large", "");
        richPresence.setDetails(strings[0]);
        richPresence.setStartTimestamps(time);
        DiscordRichPresence richPresence3 = new DiscordRichPresence();
        richPresence3.largeImageText = "jude";
        richPresence3.state = ("Best free minecraft cheat client! -> Tired-Client.de");

        net.arikia.dev.drpc.DiscordRPC.discordUpdatePresence(richPresence.build());

    }


}