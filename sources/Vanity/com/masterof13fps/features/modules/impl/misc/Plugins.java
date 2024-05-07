package com.masterof13fps.features.modules.impl.misc;

import com.masterof13fps.features.modules.Module;
import com.masterof13fps.features.modules.ModuleInfo;
import com.masterof13fps.features.modules.Category;
import com.masterof13fps.manager.eventmanager.Event;
import com.masterof13fps.manager.eventmanager.impl.EventPacket;
import com.masterof13fps.manager.notificationmanager.NotificationType;
import net.minecraft.network.play.client.C14PacketTabComplete;
import net.minecraft.network.play.server.S3APacketTabComplete;

@ModuleInfo(name = "Plugins", category = Category.MISC, description = "Finds plugins of a server")
public class Plugins extends Module {

    @Override
    public void onToggle() {

    }

    @Override
    public void onEnable() {
        sendPacket(new C14PacketTabComplete("/"));
    }

    @Override
    public void onDisable() {

    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof EventPacket) {
            if (((EventPacket) event).getType() == EventPacket.Type.RECEIVE) {
                if ((((EventPacket) event).getPacket() instanceof S3APacketTabComplete)) {
                    S3APacketTabComplete packet = (S3APacketTabComplete) ((EventPacket) event).getPacket();
                    String[] commands = packet.matches();
                    StringBuilder message = new StringBuilder();
                    int size = 0;
                    String[] array;
                    int length = (array = commands).length;
                    for (int i = 0; i < length; i++) {
                        String command = array[i];
                        String pluginName = command.split(":")[0].substring(1);
                        if ((!message.toString().contains(pluginName)) && (command.contains(":")) && (!pluginName.equalsIgnoreCase("minecraft")) &&
                                (!pluginName.equalsIgnoreCase("bukkit"))) {
                            size++;
                            if (message.length() == 0) {
                                message.append(pluginName);
                            } else {
                                message.append("§§8, §§7").append(pluginName);
                            }
                        }
                    }
                    if (message.length() > 0) {
                        notify.notification("Plugins gefunden!", "Es wurden Plugins gefunden! Siehe im Chat nach für weitere Informationen!", NotificationType.INFO, 5);
                        notify.chat("Plugins (" + size + "): §7" + message);
                        this.toggle();
                    } else {
                        notify.notification("Keine Plugins gefunden!", "Es wurden keine Plugins gefunden!", NotificationType.INFO, 5);
                        this.toggle();
                    }
                    event.setCancelled(true);
                }
            }
        }
    }
}
