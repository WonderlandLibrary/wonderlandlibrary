package me.robbanrobbin.jigsaw.client.commands;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import net.minecraft.entity.player.EntityPlayer;

public class CommandCoords extends Command {

	@Override
	public void run(String[] commands) {
		String playerName = null;
		if (commands.length > 1) {
			playerName = commands[1];
		} else {
			playerName = mc.thePlayer.getName();
		}
		for (Object o : mc.theWorld.playerEntities) {
			EntityPlayer player = (EntityPlayer)o;
			if (player.getName().equalsIgnoreCase(playerName)) {
				String coords = "x=" + (Math.round(player.posX * 10.0) / 10.0) + ", y=" + (Math.round(player.posY * 10.0) / 10.0) + ", z=" + (Math.round(player.posZ * 10.0) / 10.0);
				Jigsaw.chatMessage(
						player.getName() + " : " + coords);
				copyToClipboard(coords);
				return;
			}
		}
		Jigsaw.chatMessage("§cCould not find player: " + playerName + ". Make sure the player is loaded!");
	}

	@Override
	public String getActivator() {
		return ".coords";
	}

	@Override
	public String getSyntax() {
		return ".coords, .coords <player>";
	}

	@Override
	public String getDesc() {
		return "Prints the location of a player and copies it to your clipboard. If you only enter '.coords' it will find your position instead.";
	}

	public static void copyToClipboard(String text) {
		Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
        Clipboard clipboard = defaultToolkit.getSystemClipboard();

		clipboard.setContents(new StringSelection(text), null);
	}
}
