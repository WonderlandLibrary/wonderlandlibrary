//package me.offeex.bloomware.client.module.extension;
//
//import me.offeex.bloomware.Bloomware;
//import me.offeex.bloomware.client.module.Module;
//import me.offeex.bloomware.client.setting.settings.SettingBool;
//import net.arikia.dev.drpc.DiscordEventHandlers;
//import net.arikia.dev.drpc.DiscordRPC;
//import net.arikia.dev.drpc.DiscordRichPresence;
//
//@Module.Register(name = "RichPresence", description = "Shows your in-game activity in Discord rich presence", category = Module.Category.EXTENSION)
//public class RichPresence extends Module {
//    private final SettingBool timestamp = new SettingBool.Builder("Timestamp").value(true).setup(this);
//
//    private long startTimestamp;
//
//    @Override
//    protected void onEnable() {
//        DiscordEventHandlers handlers = new DiscordEventHandlers.Builder().setReadyEventHandler((user) -> {}).build();
//        DiscordRPC.discordInitialize("845695870452367370", handlers, true);
//        if (timestamp.getValue()) startTimestamp = System.currentTimeMillis() / 1000L;
//        new Thread(() -> {
//            while (!Thread.currentThread().isInterrupted()) {
//                String state = "IGN: " + Bloomware.INSTANCE.getMc().getSession().getUsername();
//                String details = Bloomware.INSTANCE.getMc().getCurrentServerEntry() != null ? "Playing on " + Bloomware.INSTANCE.getMc().getCurrentServerEntry().address : Bloomware.INSTANCE.getMc().isInSingleplayer() ? "On main menu" : "Playing on singleplayer";
//                DiscordRichPresence presence = new DiscordRichPresence.Builder(state).setDetails(details).setBigImage("logo", "Bloomware Client v" + Bloomware.version).setStartTimestamps(startTimestamp).build();
//                DiscordRPC.discordUpdatePresence(presence);
//                try {
//                    Thread.sleep(2000L);
//                } catch (InterruptedException e) {
//                    DiscordRPC.discordShutdown();
//                    break;
//                }
//            }
//        }).start();
//    }
//
//    @Override
//    protected void onDisable() {
//        DiscordRPC.discordShutdown();
//    }
//}
