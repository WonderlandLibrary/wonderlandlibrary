package today.flux.module.implement.Command;

import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.EventTarget;

import net.minecraft.network.play.client.C14PacketTabComplete;
import net.minecraft.network.play.server.S3APacketTabComplete;
import today.flux.event.PacketReceiveEvent;
import today.flux.module.Command;
import today.flux.utility.ChatUtils;
import today.flux.utility.DelayTimer;

@Command.Info(name = "plugins", syntax = {}, help = "Display the plugins of the server")
public class PDCmd extends Command {

	DelayTimer timer = new DelayTimer();

	@EventTarget
	public void onReceivePacket(PacketReceiveEvent event) {
		if (event.packet instanceof S3APacketTabComplete) {
			S3APacketTabComplete packet = (S3APacketTabComplete) event.packet;
			String[] commands = packet.func_149630_c();
			String message = "";
			int size = 0;
			String[] array;
			for (int length = (array = commands).length, i = 0; i < length; ++i) {
				final String command = array[i];
				final String pluginName = command.split(":")[0].substring(1);
				if (!message.contains(pluginName) && command.contains(":") && !pluginName.equalsIgnoreCase("minecraft")
						&& !pluginName.equalsIgnoreCase("bukkit")) {
					++size;
					if (message.isEmpty()) {
						message += pluginName;
					} else {
						message += "§8, §f" + pluginName;
					}
				}
			}
			if (!message.isEmpty()) {
				ChatUtils.sendMessageToPlayer("Plugins (" + size + "): §f" + message + "§8.");
			} else {
				ChatUtils.sendMessageToPlayer("Plugins: None Found!");
			}
			event.setCancelled(true);
			EventManager.unregister(this);
		}
		if (this.timer.hasPassed(20000)) {
			EventManager.unregister(this);
			ChatUtils.sendMessageToPlayer("Failed to find plugins. the server may not allow tab completion?");
		}
	}

	@Override
	public void execute(String[] args) throws Error {
		this.timer.reset();
		EventManager.register(this);
		mc.thePlayer.sendQueue.addToSendQueue(new C14PacketTabComplete("/"));
		ChatUtils.sendMessageToPlayer("Finding...");
	}

}
