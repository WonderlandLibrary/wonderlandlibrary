package markgg.modules.impl.misc;

import markgg.RazeClient;
import markgg.event.handler.EventHandler;
import markgg.event.handler.Listener;
import markgg.event.impl.MotionEvent;
import markgg.event.impl.PacketEvent;
import markgg.modules.Module;
import markgg.modules.ModuleInfo;
import markgg.settings.BooleanSetting;
import markgg.settings.ModeSetting;
import markgg.settings.NumberSetting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.util.EnumChatFormatting;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@ModuleInfo(name = "AutoHypixel", category = Module.Category.MISC)
public class AutoHypixel extends Module {

	public BooleanSetting autoGG = new BooleanSetting("Auto GG", this,false);
	public NumberSetting autoGGDelay = new NumberSetting("Auto GG Delay", this, 1000, 0, 5000, 250);
	public BooleanSetting autoQuitOnBan = new BooleanSetting("Auto Quit On Ban", this, true);
	public BooleanSetting autoReconnect = new BooleanSetting("Auto Reconnect", this, false);
	public BooleanSetting quickMath = new BooleanSetting("Quick Math", this,false);
	public BooleanSetting autoBounty = new BooleanSetting("Target Bounty", this,false);
	public NumberSetting minBounty = new NumberSetting("Minimal Bounty", this,500, 50, 5000, 5);

	private markgg.util.timer.Timer autoGGTimer = null;
	private List<Entity> entities = new CopyOnWriteArrayList<>();

	@EventHandler
	private Listener<PacketEvent> packetEvent = e -> {
		if(e.getPacket() instanceof S02PacketChat) {
			String unformatted = EnumChatFormatting.getTextWithoutFormattingCodes(((S02PacketChat) e.getPacket()).getChatComponent().getUnformattedText()), a = unformatted.replace(" ", "");
			if (a.startsWith("1stKiller")) {
				if(autoGG.getValue()) {
					autoGGTimer = new markgg.util.timer.Timer();
					autoGGTimer.reset();
				}
			}

			if(autoReconnect.getValue())
				if(((S02PacketChat) e.getPacket()).getChatComponent().getUnformattedText().contains("Flying or related"))
					mc.thePlayer.sendQueue.addToSendQueueSilent(new C01PacketChatMessage("/back"));

			if(quickMath.getValue())
				if(((S02PacketChat) e.getPacket()).getChatComponent().getUnformattedText().contains("QUICK MATHS! Solve: ")) {
					String[] array = ((S02PacketChat) e.getPacket()).getChatComponent().getUnformattedText().split("Solve: ");
					ScriptEngineManager mgr = new ScriptEngineManager();
					ScriptEngine engine = mgr.getEngineByName("JavaScript");

					try {
						mc.thePlayer.sendQueue.addToSendQueueSilent(new C01PacketChatMessage(engine.eval(array[1].replace("x", "*")).toString()));
					} catch (ScriptException he) {
						he.printStackTrace();
					}
				}

			if (unformatted.contains("A player has been removed from your game.") && autoQuitOnBan.getValue()) {
				new Thread(() -> {
					try {
						Thread.sleep(500);
					}catch (InterruptedException ex) {
						ex.printStackTrace();
					}
					mc.thePlayer.sendChatMessage("/hub");
					RazeClient.addChatMessage("Left the game due to someone being banned!");
				}).start();
			}
		}
	};

	@EventHandler
	private Listener<MotionEvent> motionEvent = e -> {
		if(autoGG.getValue() && autoGGTimer != null && autoGGTimer.hasTimeElapsed((long) autoGGDelay.getValue(), false)) {
			mc.thePlayer.sendChatMessage("gg");
			autoGGTimer.reset();
			autoGGTimer = null;
		}

		if(autoBounty.getValue()) {
			for (Entity player : entities)
				if(!mc.theWorld.playerEntities.contains(player))
					entities.remove(player);

			for (EntityPlayer player : mc.theWorld.playerEntities)
				if (!player.equals(mc.thePlayer)) {
					String display = player.getDisplayName().getUnformattedText();
					String name = player.getName();

					if (display.contains("\u00A76\u00A7l")) {
						String[] split = display.split(" ");
						if (split.length > 2) {
							int bounty = Integer.parseInt(
									split[split.length - 1]
											.replace("\u00A76\u00A71l", "")
											.replace("g", "")
							);
							if (bounty >= minBounty.getValue()) {
								if (!entities.contains(player)) {
									entities.add(player);

									RazeClient.addChatMessage(name + " has " + bounty + "g on him!");
								}
							}
						}
					}
				}
		}
	};

}
