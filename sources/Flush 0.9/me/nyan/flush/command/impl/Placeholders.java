package me.nyan.flush.command.impl;

import me.nyan.flush.command.Command;
import me.nyan.flush.customhud.setting.impl.TextSetting;
import me.nyan.flush.utils.other.ChatUtils;

import java.util.Map;

public class Placeholders extends Command {
    public Placeholders() {
        super("Placeholders", "Lists all CustomHud text placeholders", "Placeholders");
    }

    @Override
    public void onCommand(String[] args, String message) {
        ChatUtils.println("§9Placeholders:");
        for (Map.Entry<String, String> entry : TextSetting.getPlaceholders().entrySet()) {
            ChatUtils.println("\"$" + entry.getKey() + "\": " + entry.getValue());
        }
    }
}
