package me.robbanrobbin.jigsaw.client.commands;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

import me.robbanrobbin.jigsaw.client.commands.option.CommandOption;
import me.robbanrobbin.jigsaw.client.commands.option.NoCommandOption;
import me.robbanrobbin.jigsaw.client.commands.option.SingleCustomOptionActivator;
import net.minecraft.entity.player.EntityPlayer;

public class CommandCoords extends Command {
	
	@Override
	public CommandOption[] setOptions() {
		return new CommandOption[] {
			new SingleCustomOptionActivator("get", "playerName") {
				@Override
				public void run(String arg) {
					String playerName = arg;
					for (Object o : mc.world.playerEntities) {
						EntityPlayer player = (EntityPlayer)o;
						if (player.getName().equalsIgnoreCase(playerName)) {
							String coords = "x=" + (Math.round(player.posX * 10.0) / 10.0) + ", y=" + (Math.round(player.posY * 10.0) / 10.0) + ", z=" + (Math.round(player.posZ * 10.0) / 10.0);
							addResult(player.getName() + " : " + coords);
							copyToClipboard(coords);
							return;
						}
					}
					addResult("§cCould not find player: " + playerName + ". Make sure the player is loaded!");
				}
			},
			new NoCommandOption() {
				@Override
				public void run() {
					String playerName = mc.player.getName();
					for (Object o : mc.world.playerEntities) {
						EntityPlayer player = (EntityPlayer)o;
						if (player.getName().equalsIgnoreCase(playerName)) {
							String coords = "x=" + (Math.round(player.posX * 10.0) / 10.0) + ", y=" + (Math.round(player.posY * 10.0) / 10.0) + ", z=" + (Math.round(player.posZ * 10.0) / 10.0);
							addResult(
									player.getName() + " : " + coords);
							copyToClipboard(coords);
							return;
						}
					}
				}
			}
		};
	}

	@Override
	public String getActivator() {
		return "coordinates";
	}

	@Override
	public String getDesc() {
		return "Gets the location of a player and copies it to your clipboard. If you only enter 'coords' it will find your position instead.";
	}

	public static void copyToClipboard(String text) {
		Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
        Clipboard clipboard = defaultToolkit.getSystemClipboard();

		clipboard.setContents(new StringSelection(text), null);
	}
}
