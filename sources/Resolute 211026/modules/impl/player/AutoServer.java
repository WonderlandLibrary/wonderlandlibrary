// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.modules.impl.player;

import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C01PacketChatMessage;
import vip.Resolute.ui.notification.Notification;
import vip.Resolute.ui.notification.NotificationType;
import vip.Resolute.Resolute;
import net.minecraft.network.play.server.S02PacketChat;
import vip.Resolute.events.impl.EventPacket;
import vip.Resolute.events.Event;
import vip.Resolute.settings.Setting;
import vip.Resolute.settings.impl.NumberSetting;
import vip.Resolute.settings.impl.ModeSetting;
import vip.Resolute.modules.Module;

public class AutoServer extends Module
{
    public ModeSetting mode;
    public ModeSetting playMode;
    public NumberSetting delay;
    
    public AutoServer() {
        super("AutoServer", 0, "Automatically plays", Category.PLAYER);
        this.mode = new ModeSetting("Mode", "Hypixel", new String[] { "Hypixel" });
        this.playMode = new ModeSetting("Play Mode", "Solo Insane", new String[] { "Solo Insane", "Teams Insane" });
        this.delay = new NumberSetting("Delay", 1300.0, 100.0, 5000.0, 10.0);
        this.addSettings(this.mode, this.playMode, this.delay);
    }
    
    @Override
    public void onEvent(final Event e) {
        this.setSuffix("");
        if (e instanceof EventPacket && AutoServer.mc.theWorld != null && AutoServer.mc.thePlayer != null && ((EventPacket)e).getPacket() instanceof S02PacketChat) {
            final S02PacketChat packet = ((EventPacket)e).getPacket();
            if (!packet.getChatComponent().getUnformattedText().isEmpty()) {
                final String message = packet.getChatComponent().getUnformattedText();
                if (message.contains("You won! Want to play again?") || (message.contains("You died! Want to play again?") && onHypixel())) {
                    final Thread thread = new Thread() {
                        @Override
                        public void run() {
                            try {
                                Resolute.getNotificationManager().add(new Notification("Auto Play", "Sending you to a new game...    ", (long)AutoServer.this.delay.getValue(), NotificationType.SUCCESS));
                                Thread.sleep((long)AutoServer.this.delay.getValue());
                            }
                            catch (Exception var2) {
                                var2.printStackTrace();
                            }
                            if (AutoServer.this.playMode.is("Solo Insane")) {
                                Module.mc.getNetHandler().sendPacketNoEvent(new C01PacketChatMessage("/play solo_insane"));
                            }
                            if (AutoServer.this.playMode.is("Teams Insane")) {
                                Module.mc.getNetHandler().sendPacketNoEvent(new C01PacketChatMessage("/play teams_insane"));
                            }
                        }
                    };
                    thread.start();
                }
            }
        }
    }
    
    public static boolean onHypixel() {
        final ServerData serverData = AutoServer.mc.getCurrentServerData();
        return serverData != null && (serverData.serverIP.endsWith("hypixel.net") || serverData.serverIP.endsWith("hypixel.net:25565") || serverData.serverIP.equals("104.17.71.15") || serverData.serverIP.equals("104.17.71.15:25565"));
    }
}
