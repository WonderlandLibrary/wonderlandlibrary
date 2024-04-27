package me.aquavit.liquidsense.command.commands;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import me.aquavit.liquidsense.LiquidSense;
import me.aquavit.liquidsense.utils.client.SettingsUtils;
import me.aquavit.liquidsense.command.Command;
import me.aquavit.liquidsense.utils.misc.HttpUtils;
import me.aquavit.liquidsense.utils.login.Callback;
import me.aquavit.liquidsense.ui.client.hud.element.elements.extend.ColorType;
import me.aquavit.liquidsense.ui.client.hud.element.elements.extend.Notification;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AutoSettingsCommand extends Command {

    private final Object loadingLock = new Object();
    private List<String> autoSettingFiles = new ArrayList<>();

    public AutoSettingsCommand() {
        super("autosettings", "setting", "settings", "config", "autosetting");
    }

    public void execute(String[] args) {
        if (args.length <= 1) {
            chatSyntax("settings <load/list>");
            return;
        }

        String arg = args[1].toLowerCase();

        switch (arg) {
            case "load":
                if (args.length < 3) {
                    chatSyntax("settings load <name/url>");
                    return;
                }

                String url = args[2].startsWith("http") ? args[2] : LiquidSense.CLIENT_CLOUD + "/settings/" + args[2].toLowerCase();

                chat("Loading settings...");

                new Thread(() -> {
                    try {
                        String settings = HttpUtils.get(url);

                        chat("Applying settings...");
                        new SettingsUtils().executeScript(settings);
                        chat("§6Settings applied successfully");
                        LiquidSense.hud.addNotification(new Notification("Updated Settings", "Setting was updated.", ColorType.INFO, 1500, 500));
                        playEdit();
                    } catch (Exception exception) {
                        exception.printStackTrace();
                        chat("Failed to fetch auto settings.");
                    }
                }).start();
                break;
            case "list":
                chat("Loading settings...");

                loadSettings(false, null, settingList -> {
                    for (String setting : settingList) {
                        chat("> " + setting);
                    }
                });
                break;
        }
    }

    private void loadSettings(boolean useCached, Long join, Callback<List<String>> callback) {
        Thread thread = new Thread(() -> {
            synchronized (loadingLock) {
                if (useCached && autoSettingFiles != null) {
                    callback.done(autoSettingFiles);
                    return;
                }

                try {
                    JsonElement json = new JsonParser().parse(HttpUtils.get("https://api.github.com/repos/CCBlueX/LiquidCloud/contents/LiquidBounce/settings"));

                    List<String> autoSettings = new ArrayList<>();

                    if (json instanceof JsonArray){
                        for (JsonElement setting : ((JsonArray) json))
                            autoSettings.add(setting.getAsJsonObject().get("name").getAsString());
                    }

                    callback.done(autoSettings);

                    this.autoSettingFiles = autoSettings;
                } catch (Exception e) {
                    e.printStackTrace();
                    chat("Failed to fetch auto settings list.");
                }
            }
        });

        thread.start();

        if (join != null) {
            try {
                thread.join(join);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<String> tabComplete(String[] args) {
        if (args.length == 0) return new ArrayList<>();

        switch (args.length) {
            case 1:
                return Lists.newArrayList("list", "load").stream()
                        .filter(command -> command.toLowerCase().startsWith(args[0].toLowerCase()))
                        .collect(Collectors.toList());
            case 2:
                if (args[0].equalsIgnoreCase("load")) {
                    if (autoSettingFiles == null) {
                        this.loadSettings(true, (long) 500, list -> {});
                    }

                    if (autoSettingFiles != null) {
                        return autoSettingFiles.stream()
                                .filter(setting -> setting.toLowerCase().startsWith(args[1].toLowerCase()))
                                .collect(Collectors.toList());
                    }
                }
                return new ArrayList<>();
            default:
                return new ArrayList<>();
        }
    }
}
