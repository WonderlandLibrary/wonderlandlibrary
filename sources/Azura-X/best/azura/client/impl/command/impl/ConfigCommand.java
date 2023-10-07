package best.azura.client.impl.command.impl;

import best.azura.client.impl.Client;
import best.azura.client.api.command.ACommand;
import best.azura.client.impl.config.Config;
import best.azura.client.util.crypt.Crypter;
import best.azura.client.util.other.ChatUtil;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonStreamParser;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.Util;
import org.lwjgl.Sys;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class ConfigCommand extends ACommand {
	
	@Override
	public String getName() {
		return "config";
	}
	
	@Override
	public String getDescription() {
		return "Load or save configs";
	}
	
	@Override
	public String[] getAliases() {
		return new String[]{"cfg"};
	}
	
	@Override
	public void handleCommand(String[] args) {
		if (args.length == 0) {
			msg(Client.PREFIX + "§7Please use: §9.config <load/save/list/upload/folder> §7!");
		} else if (args[0].equalsIgnoreCase("list")) {
			
			if (args.length != 1) {
				msg(Client.PREFIX + "§7Please use: §9.config list§7!");
				return;
			}
			
			File configsDir = Client.INSTANCE.getConfigManager().getConfigDirectory();
			if (configsDir.listFiles() == null) {
				msg(Client.PREFIX + "§7No configs found!");
				return;
			}
			if (Objects.requireNonNull(configsDir.listFiles()).length == 0) {
				msg(Client.PREFIX + "§7No configs found!");
				return;
			}
			msg(Client.PREFIX + "§9Configs");
			for (File f : Objects.requireNonNull(configsDir.listFiles())) {
				String s = f.getName().replace(".json", "");
				ChatComponentText text = new ChatComponentText("§8-> " + Client.COLOR + s);
				ChatStyle style = text.getChatStyle();
				style.setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, ".config load " + s));
				style.setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText(".cfg load " + s)));
				text.setChatStyle(style);
				mc.thePlayer.addChatMessage(text);
			}
			
		} else if (args[0].equalsIgnoreCase("load")) {
			
			if (args.length == 1) {
				msg(Client.PREFIX + "§7Please use: §9.config load <name>§7!");
				return;
			}
			
			if (Client.INSTANCE.getConfigManager().getConfig(args[1]).load()) {
				msg(Client.PREFIX + "§7Loaded config §8'" + Client.COLOR + args[1] + "§8'§7!");
			} else {
				msg(Client.PREFIX + "§7Could not load config §8'" + Client.COLOR + args[1] + "§8'§7!");
			}
			
		} else if (args[0].equalsIgnoreCase("save")) {
			
			if (args.length == 1) {
				msg(Client.PREFIX + "§7Please use: " + Client.COLOR + ".config save <name>§7!");
				return;
			}
			
			Client.INSTANCE.getConfigManager().createConfig(args[1]).save();
			msg(Client.PREFIX + "§7Saved config §8'" + Client.COLOR + args[1] + "§8'§7!");
		} else if (args[0].equalsIgnoreCase("folder")) {
			
			if (args.length != 1) {
				msg(Client.PREFIX + "§7Please use: §9.config folder§7!");
				return;
			}
			
			{
				File file1 = Client.INSTANCE.getConfigManager().getClientDirectory();
				String s = file1.getAbsolutePath();
				
				if (Util.getOSType() == Util.EnumOS.OSX) {
					try {
						Runtime.getRuntime().exec(new String[]{"/usr/bin/open", s});
						return;
					} catch (IOException ignored) {
					}
				} else if (Util.getOSType() == Util.EnumOS.WINDOWS) {
					String s1 = String.format("cmd.exe /C start \"Open file\" \"%s\"", s);
					
					try {
						Runtime.getRuntime().exec(s1);
						return;
					} catch (IOException ignored) {
					}
				}
				
				boolean flag = false;
				
				try {
					Class<?> oclass = Class.forName("java.awt.Desktop");
					Object object = oclass.getMethod("getDesktop", new Class[0]).invoke(null);
					oclass.getMethod("browse", new Class[]{URI.class}).invoke(object, file1.toURI());
				} catch (Throwable throwable) {
					flag = true;
				}
				if (flag) {
					Sys.openURL("file://" + s);
				}
			}
		} else {
			msg(Client.PREFIX + "§7Please use: §9.config <load/save/list/upload/folder> §7!");
		}
	}
}