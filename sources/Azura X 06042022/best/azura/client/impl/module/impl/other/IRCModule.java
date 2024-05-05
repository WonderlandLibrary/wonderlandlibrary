package best.azura.client.impl.module.impl.other;

import best.azura.client.api.module.Category;
import best.azura.client.api.module.Module;
import best.azura.client.api.module.ModuleInfo;
import best.azura.client.api.ui.notification.Notification;
import best.azura.client.api.ui.notification.Type;
import best.azura.client.impl.Client;
import best.azura.client.impl.events.EventNameChange;
import best.azura.irc.impl.IRCConnector;
import best.azura.irc.impl.packets.client.C2NameChangePacket;
import best.azura.client.impl.value.BooleanValue;
import best.azura.eventbus.core.Event;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import com.google.gson.JsonObject;

@ModuleInfo(name = "IRC", description = "A chat for azura users", category = Category.OTHER)
public class IRCModule extends Module {

	public static final BooleanValue emotes = new BooleanValue("Emotes", "Should Emotes of other Azura Users be shown?", true);
	public static final BooleanValue funnyPacket = new BooleanValue("Funny Packet", "Should the client respond to Funny Packets?", true);
	public static final BooleanValue banNotifier = new BooleanValue("Ban Notifier", "Should the client react to Azura Users getting banned on your Server?", true);

	@EventHandler
	public Listener<Event> eventListener = this::handle;

	private void handle(Event event) {
		if (event instanceof EventNameChange) {

			// Update the name and sends an IRC Packet.
			if (Client.INSTANCE.getIrcConnector().getClientSocket() != null &&
					Client.INSTANCE.getIrcConnector().getClientSocket().isConnected()) {
				C2NameChangePacket c2NameChangePacket = new C2NameChangePacket(null);
				JsonObject jsonObject1 = new JsonObject();

				jsonObject1.addProperty("name", ((EventNameChange) event).newName);

				c2NameChangePacket.setContent(jsonObject1);
				Client.INSTANCE.getIrcConnector().sendPacket(c2NameChangePacket);
			}
		}
	}

	@Override
	public void onEnable() {
		super.onEnable();

		// Create a new IRC Connector if its null.
		if (Client.INSTANCE.getIrcConnector() == null) {
			Client.INSTANCE.setIrcConnector(new IRCConnector("irc.azura.best", 4739));
		}

		// Connect to the IRC.
		 try {
			if (Client.INSTANCE.getIrcConnector().getClientSocket() != null && Client.INSTANCE.getIrcConnector().getClientSocket().isClosed()) {
				try {
					Client.INSTANCE.getNotificationManager().addToQueue(new Notification("Azura Chat", "Connecting to chat", 1500, Type.INFO));
					Client.INSTANCE.getIrcConnector().startConnection();
				} catch (Exception ignored) {
					Client.INSTANCE.getNotificationManager().addToQueue(new Notification("Azura Chat", "Failed to connect to chat", 1500, Type.WARNING));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Client.INSTANCE.getNotificationManager().addToQueue(new Notification("Azura Chat", "Failed to connect to chat", 1500, Type.WARNING));
		}
	}

	@Override
	public void onDisable() {
		super.onDisable();
	}
}