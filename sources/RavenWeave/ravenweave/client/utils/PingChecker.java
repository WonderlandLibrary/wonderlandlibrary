package ravenweave.client.utils;

import net.weavemc.loader.api.event.ChatReceivedEvent;
import net.weavemc.loader.api.event.SubscribeEvent;
import ravenweave.client.clickgui.raven.Terminal;

public class PingChecker {
    private static boolean e;
    private static long s;

    @SubscribeEvent
    public void onChatMessageReceived(ChatReceivedEvent event) {
        if (e && Utils.Player.isPlayerInGame()) {
            if (Utils.Java.str(event.getMessage().getUnformattedText()).startsWith("Unknown")) {
                event.setCancelled(true);
                e = false;
                this.getPing();
            }
        }
    }

    public static void checkPing() {
        Terminal.print("Checking...");
        if (e) {
            Terminal.print("Please wait.");
        } else {
            Utils.mc.thePlayer.sendChatMessage("/...");
            e = true;
            s = System.currentTimeMillis();
        }
    }

    private void getPing() {
        int ping = (int) (System.currentTimeMillis() - s) - 20;
        if (ping < 0) {
            ping = 0;
        }

        Terminal.print("Your ping: " + ping + "ms");
        reset();
    }

    public static void reset() {
        e = false;
        s = 0L;
    }
}
