package today.flux.module.implement.Command;

import com.alibaba.fastjson.JSONObject;
import com.soterdev.SoterObfuscator;
import net.minecraft.util.EnumChatFormatting;
import today.flux.Flux;
import today.flux.config.Cloud;
import today.flux.config.preset.PresetManager;
import today.flux.module.Command;
import today.flux.utility.ChatUtils;
import today.flux.utility.HttpUtil;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.net.URL;
import java.util.Base64;

@Command.Info(name = "preset", syntax = { "list", "load <name>",
		"add <name> [binds(true/false)]",
		"delete <name>",
		"upload <name>",
		"download <id> <name>"}, help = "Manage presets")
public class PresetCmd extends Command {

	@Override
	@SoterObfuscator.Obfuscation(flags = "+native")
	public void execute(String[] args) throws Error {
		if (args.length < 1) {
			this.syntaxError();
			return;
		}

		if (args[0].equalsIgnoreCase("list")) {
			this.showPresets();
			return;
		}

		if (args[0].equalsIgnoreCase("load")) {
			if (args.length < 2) {
				this.syntaxError();
				return;
			}

			String presetName = args[1];

			if (!PresetManager.loadPreset(presetName)) {
				ChatUtils.sendMessageToPlayer(EnumChatFormatting.RED + "That preset doesn't exist.");
				this.showPresets();
			} else {
				ChatUtils.sendMessageToPlayer("Loaded preset " + EnumChatFormatting.YELLOW + presetName);
			}
			return;
		}

		if (args[0].equalsIgnoreCase("add")) {
			if (args.length < 2) {
				this.syntaxError();
				return;
			}

			String presetName = args[1];
			boolean binds = args.length > 2 && args[2].equalsIgnoreCase("true");

			if (PresetManager.presets.contains(presetName)) {
				ChatUtils.sendMessageToPlayer(EnumChatFormatting.RED + "That preset already exists.");
			} else {
				PresetManager.addPreset(presetName, binds);
				ChatUtils.sendMessageToPlayer(EnumChatFormatting.GREEN + "Created preset " + EnumChatFormatting.YELLOW + presetName);
			}

			Flux.INSTANCE.getConfig().loadPresets();
			return;
		}

		if (args[0].equalsIgnoreCase("delete")) {
			if (args.length < 2) {
				this.syntaxError();
				return;
			}

			String presetName = args[1];

			if (PresetManager.deletePreset(presetName)) {
				ChatUtils.sendMessageToPlayer("Deleted preset");
			} else {
				ChatUtils.sendMessageToPlayer(EnumChatFormatting.RED + "That preset doesn't exist.");
				this.showPresets();
			}
			return;
		}

		if (args[0].equalsIgnoreCase("upload")) {
			if (args.length < 2) {
				this.syntaxError();
				return;
			}

			String presetName = args[1];

			if (PresetManager.presets.contains(presetName)) {
				ChatUtils.sendMessageToPlayer(EnumChatFormatting.GREEN + "Uploading...");
				new Thread() {
					@Override
					@SoterObfuscator.Obfuscation(flags = "+native")
					public void run() {
						String result = Cloud.uploadConfig(Base64.getEncoder().encodeToString(JSONObject.toJSONString(PresetManager.getPreset(presetName)).getBytes()));
						if (result != null && result.length() == 32) {
							Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
							StringSelection selection = new StringSelection(result);
							clipboard.setContents(selection, null);
							ChatUtils.sendMessageToPlayer(EnumChatFormatting.GREEN + "Config Uploaded! ID: " + result + " (Copied)");

						} else {
							ChatUtils.sendMessageToPlayer(EnumChatFormatting.RED + "Failed to upload config!");
						}
					}
				}.start();
			} else {
				ChatUtils.sendMessageToPlayer(EnumChatFormatting.RED + "That preset doesn't exist.");
				this.showPresets();
			}
			return;
		}

		if (args[0].equalsIgnoreCase("download")) {
			if (args.length < 3) {
				this.syntaxError();
				return;
			}

			String presetId = args[1];
			String presetName = args[2];

			if (!PresetManager.presets.contains(presetName)) {
				ChatUtils.sendMessageToPlayer(EnumChatFormatting.GREEN + "Downloading...");
				new Thread() {
					@Override
					@SoterObfuscator.Obfuscation(flags = "+native")
					public void run() {
						try {
							String result = HttpUtil.performGetRequest(new URL("https://flux.today/config_beta/" + presetId));
							result = new String(Base64.getDecoder().decode(result));
							if (result.length() > 100) {
								BufferedWriter writer = new BufferedWriter(new FileWriter("Flux/presets/" + presetName + ".prs"));
								writer.write(result);
								writer.flush();
								writer.close();
								Flux.INSTANCE.getConfig().loadPresets();
								ChatUtils.sendMessageToPlayer(EnumChatFormatting.GREEN + "Downloaded successfully!");
							}
						} catch (Throwable e) {
							ChatUtils.sendMessageToPlayer(EnumChatFormatting.RED + "Failed to download!");
							e.printStackTrace();
						}
					}
				}.start();
			} else {
				ChatUtils.sendMessageToPlayer(EnumChatFormatting.RED + "That preset has already existed.");
			}
			return;
		}

		this.syntaxError();
	}

	private void showPresets() {
		StringBuilder stringBuilder = new StringBuilder();

		PresetManager.presets.forEach(preset -> stringBuilder.append(preset).append(EnumChatFormatting.DARK_GRAY).append(", ").append(EnumChatFormatting.WHITE));

		stringBuilder.delete(Math.max(stringBuilder.length() - 4, 0), stringBuilder.length());
		ChatUtils.sendMessageToPlayer(EnumChatFormatting.GOLD + "Available Presets:\n" + stringBuilder);
	}
}