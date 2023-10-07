package info.sigmaclient.management.command.impl;

import info.sigmaclient.event.Event;
import info.sigmaclient.management.notifications.Notifications;
import info.sigmaclient.module.data.Options;
import info.sigmaclient.module.data.Setting;
import info.sigmaclient.Client;
import info.sigmaclient.management.command.Command;
import info.sigmaclient.module.Module;
import info.sigmaclient.util.StringConversions;
import info.sigmaclient.util.misc.ChatUtil;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arithmo on 5/20/2017 at 9:42 PM.
 */
public class Config extends Command {

    public Config(String[] names, String description) {
        super(names, description);
    }

    @Override
    public void fire(String[] args) {
        if (args == null) {
            printUsage();
            return;
        }
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("hypixel")) {
                ChatUtil.printChat(chatPrefix + "Applying settings, please wait.");
                new Thread(() ->
                {
                    loadConfig("https://sigma-client.000webhostapp.com/configs/hypixel.txt");
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                    }
                    ChatUtil.printChat(chatPrefix + "\247bHypixel \2477has been loaded!");
                    Notifications.getManager().post("Config Loaded", "Hypixel has been loaded!", Notifications.Type.INFO);
                }).start();
                return;
            }
            if (args[0].equalsIgnoreCase("mineplex")) {
                ChatUtil.printChat(chatPrefix + "Applying settings, please wait.");
                new Thread(() ->
                {
                    loadConfig("https://pastebin.com/raw/sb8duTTB");
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                    }
                    ChatUtil.printChat(chatPrefix + "Mineplex has been loaded!");
                    Notifications.getManager().post("Config Loaded", "Mineplex has been loaded!", Notifications.Type.INFO);
                }).start();
                return;
            }
        } else if (args.length == 2 && args[0].equalsIgnoreCase("custom")) {
            ChatUtil.printChat(chatPrefix + "Custom configs coming soon.");
            //loadConfig(args[1]);
            return;
        }
        printUsage();
    }

    public void loadConfig(String url) {
        final List<String> readContent = new ArrayList<String>();
        try {
            URL configURL = new URL(url);
            final BufferedReader in = new BufferedReader(new InputStreamReader(configURL.openStream()));
            String str;
            while ((str = in.readLine()) != null) {
                readContent.add(str);
            }
            in.close();
        } catch (Exception e) {
            ChatUtil.printChat("Error loading config.");
            return;
        }
        if (!readContent.isEmpty()) {
            for (String line : readContent) {
                String[] split = line.split(":");
                for (Module module : Client.getModuleManager().getArray()) {
                    if (split[0].equalsIgnoreCase("FEM") && split[1].equalsIgnoreCase(module.getName()) && !module.isEnabled()) {
                        module.toggle();
                    } else if (module.getName().equalsIgnoreCase(split[0])) {
                        Setting setting = Module.getSetting(module.getSettings(), split[1]);
                        String settingValue = split[2];
                        if (setting != null) {
                            System.out.println(setting.getValue() + " " + settingValue);
                            if (setting.getValue() instanceof Number) {
                                Object newValue = (StringConversions.castNumber(settingValue, setting.getValue()));
                                if (newValue != null) {
                                    setting.setValue(newValue);
                                }
                            } // If the setting is supposed to be a string
                            else if (setting.getValue().getClass().equals(String.class)) {
                                String parsed = settingValue.toString().replaceAll("_", " ");
                                setting.setValue(parsed);
                            } // If the setting is supposed to be a boolean
                            else if (setting.getValue().getClass().equals(Boolean.class)) {
                                setting.setValue(Boolean.parseBoolean(settingValue));
                            } // If the setting is supposed to be an option
                            else if (setting.getValue().getClass().equals(Options.class)) {
                                Options dank = ((Options) setting.getValue());
                                for (String str : dank.getOptions()) {
                                    if (str.equalsIgnoreCase(settingValue)) {
                                        dank.setSelected(settingValue);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onEvent(Event event) {

    }

    @Override
    public String getUsage() {
        return "config <mineplex/hypixel/custom> [Raw text URL]";
    }

}
