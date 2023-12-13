package dev.rise.config;

import dev.rise.Rise;
import dev.rise.module.Module;
import dev.rise.notifications.NotificationType;
import dev.rise.setting.Setting;
import dev.rise.setting.impl.BooleanSetting;
import dev.rise.setting.impl.ModeSetting;
import dev.rise.setting.impl.NumberSetting;
import dev.rise.ui.guitheme.Theme;
import dev.rise.util.misc.FileUtil;
import dev.rise.util.render.theme.ThemeUtil;
import lombok.experimental.UtilityClass;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

@UtilityClass
public final class ConfigHandler {

    private final String s = File.separator;

    public void save(final String name) {
        final StringBuilder configBuilder = new StringBuilder();
        configBuilder.append("Rise_Version_").append(Rise.CLIENT_VERSION).append("\r\n");
        configBuilder.append("MainMenuTheme_").append(Rise.INSTANCE.getGuiTheme().getCurrentTheme()).append("\r\n");
        configBuilder.append("ClientName_").append(ThemeUtil.getCustomClientName()).append("\r\n");

        for (final Module m : Rise.INSTANCE.getModuleManager().getModuleList()) {
            final String moduleName = m.getModuleInfo().name();
            configBuilder.append("Toggle_").append(moduleName).append("_").append(m.isEnabled()).append("\r\n");

            for (final Setting setting : m.getSettings()) {
                if (setting instanceof BooleanSetting) {
                    configBuilder.append("BooleanSetting_").append(moduleName).append("_").append(setting.name).append("_").append(((BooleanSetting) setting).enabled).append("\r\n");
                }
                if (setting instanceof NumberSetting) {
                    configBuilder.append("NumberSetting_").append(moduleName).append("_").append(setting.name).append("_").append(((NumberSetting) setting).value).append("\r\n");
                }
                if (setting instanceof ModeSetting) {
                    configBuilder.append("ModeSetting_").append(moduleName).append("_").append(setting.name).append("_").append(((ModeSetting) setting).getMode()).append("\r\n");
                }
            }
        }

        FileUtil.saveFile("Config" + s + name + ".txt", true, configBuilder.toString());
        Rise.INSTANCE.getNotificationManager().registerNotification("Config saved " + name + ".");
    }

    public void load(final String name) {
        final String config = FileUtil.loadFile("Config" + s + name + ".txt");
        if (config == null) {
            Rise.INSTANCE.getNotificationManager().registerNotification("Config does not exist.");
            return;
        }

        final String[] configLines = config.split("\r\n");

        for (final Module m : Rise.INSTANCE.getModuleManager().getModuleList()) {
            if (m.isEnabled()) {
                m.toggleModule();
            }
        }

        boolean gotConfigVersion = false;
        for (final String line : configLines) {

            final String[] split = line.split("_");
            if (split[0].contains("Rise")) {
                if (split[1].contains("Version")) {
                    gotConfigVersion = true;

                    final String clientVersion = Rise.CLIENT_VERSION;
                    final String configVersion = split[2];

                    if (!clientVersion.equalsIgnoreCase(configVersion)) {
                        Rise.addChatMessage("This config was made in a different version of Rise! Incompatibilities are expected.");
                        Rise.INSTANCE.getNotificationManager().registerNotification(
                                "This config was made in a different version of Rise! Incompatibilities are expected.", NotificationType.WARNING
                        );
                    }
                }
            }

            if (split[0].contains("MainMenuTheme")) {
                Rise.INSTANCE.getGuiTheme().setCurrentTheme(Theme.valueOf(split[1]));
                continue;
            }

            if (split[0].contains("ClientName")) {
                ThemeUtil.setCustomClientName(split.length > 1 ? split[1] : "");
                continue;
            }

            if (split[0].contains("ChatMSG")) {
                Rise.addChatMessage(split[1]);
            }

            if (split.length < 3) continue;

            if (split[0].contains("Toggle")) {
                if (split[2].contains("true")) {
                    if (Rise.INSTANCE.getModuleManager().getModule(split[1]) != null) {
                        final Module module = Objects.requireNonNull(Rise.INSTANCE.getModuleManager().getModule(split[1]));

                        if (!module.isEnabled()) {
                            module.toggleModule();
                        }
                    }
                }
            }

            final Setting setting = Rise.INSTANCE.getModuleManager().getSetting(split[1], split[2]);

            if (Rise.INSTANCE.getModuleManager().getModule(split[1]) == null)
                continue;

            if (split[0].contains("BooleanSetting") && setting instanceof BooleanSetting) {
                if (split[3].contains("true")) {
                    ((BooleanSetting) setting).enabled = true;
                }

                if (split[3].contains("false")) {
                    ((BooleanSetting) setting).enabled = false;
                }
            }

            if (split[0].contains("NumberSetting") && setting instanceof NumberSetting) {
                ((NumberSetting) setting).setValue(Double.parseDouble(split[3]));
            }

            if (split[0].contains("ModeSetting") && setting instanceof ModeSetting) {
                ((ModeSetting) setting).set(split[3]);
            }
        }
        if (!gotConfigVersion) {
            Rise.addChatMessage("This config was made in a different version of Rise! Incompatibilities are expected.");
            Rise.INSTANCE.getNotificationManager().registerNotification(
                    "This config was made in a different version of Rise! Incompatibilities are expected.", NotificationType.WARNING
            );
        }


        //Notification
        Rise.INSTANCE.getNotificationManager().registerNotification("Config loaded " + name + ".");
        Rise.amountOfConfigsLoaded++;
    }

    public void loadFromList(final List<String> list) {
        for (final Module m : Rise.INSTANCE.getModuleManager().getModuleList()) {
            if (m.isEnabled() && !m.getModuleInfo().name().toLowerCase().contains("clickgui")) {
                m.toggleModule();
            }
        }

        for (final String line : list) {
            if (line == null) return;

            final String[] split = line.split("_");

            if (Rise.INSTANCE.getModuleManager().getModule(split[1]) != null) {
                final Module module = Objects.requireNonNull(Rise.INSTANCE.getModuleManager().getModule(split[1]));

                if (module.getModuleInfo().name().toLowerCase().contains("clickgui")) {
                    continue;
                }
            }

            if (split[0].contains("MainMenuTheme")) {
                Rise.INSTANCE.getGuiTheme().setCurrentTheme(Theme.valueOf(split[1]));
                continue;
            }

            if (split[0].contains("ClientName")) {
                ThemeUtil.setCustomClientName(split[1]);
                continue;
            }

            if (split[0].contains("Toggle")) {
                if (split[2].contains("true")) {
                    if (Rise.INSTANCE.getModuleManager().getModule(split[1]) != null) {
                        final Module module = Objects.requireNonNull(Rise.INSTANCE.getModuleManager().getModule(split[1]));

                        if (!module.isEnabled()) {
                            module.toggleModule();
                        }
                    }
                }
            }

            final Setting setting = Rise.INSTANCE.getModuleManager().getSetting(split[1], split[2]);

            if (split[0].contains("BooleanSetting") && setting instanceof BooleanSetting) {
                if (split[3].contains("true")) {
                    ((BooleanSetting) setting).enabled = true;
                }

                if (split[3].contains("false")) {
                    ((BooleanSetting) setting).enabled = false;
                }
            }

            if (split[0].contains("NumberSetting") && setting instanceof NumberSetting) {
                ((NumberSetting) setting).setValue(Double.parseDouble(split[3]));
            }

            if (split[0].contains("ModeSetting") && setting instanceof ModeSetting) {
                ((ModeSetting) setting).set(split[3]);
            }
        }

        Rise.amountOfConfigsLoaded++;
    }

    public void list() {
        if (!FileUtil.exists("Config\\"))
            Rise.INSTANCE.getNotificationManager().registerNotification("No configs created.");
        final File configFolder = FileUtil.getFileOrPath("Config\\");

        if (configFolder.listFiles() == null || Objects.requireNonNull(configFolder.listFiles()).length < 1) {
            Rise.INSTANCE.getNotificationManager().registerNotification("No configs created.");
        } else {
            Rise.addChatMessage("List of configuration files: ");

            for (final File file : Objects.requireNonNull(configFolder.listFiles())) {
                Rise.addChatMessage(" - " + file.getName().replace(".txt", ""));
            }
        }
    }

    public void delete(final String name) {
        if (FileUtil.exists("Config\\" + name + ".txt")) {
            Rise.INSTANCE.getNotificationManager().registerNotification("Config does not exist.");
            return;
        }

        FileUtil.delete("Config\\" + name + ".txt");
        Rise.INSTANCE.getNotificationManager().registerNotification("Config " + name + " has been deleted.");
    }

    public void loadFromRes(final String name) {
        final URL defaultImage = ConfigHandler.class.getResource(s + "assets" + s + "minecraft" + s + "rise" + s + "defaultcfg" + s + name + ".txt");
        final File loadFile;
        try {
            loadFile = new File(defaultImage.toURI());
        } catch (final URISyntaxException e) {
            e.printStackTrace();
            Rise.INSTANCE.getNotificationManager().registerNotification("Error while loading config");
            return;
        }

        Rise.INSTANCE.getNotificationManager().registerNotification(loadFile.getAbsolutePath());

        final boolean exists = loadFile.exists();

        if (!exists) {
            Rise.INSTANCE.getNotificationManager().registerNotification("Error while loading config");
            return;
        }

        Scanner scan = null;

        try {
            scan = new Scanner(loadFile);
        } catch (final IOException e1) {
            Rise.INSTANCE.getNotificationManager().registerNotification("Error while loading config");
            e1.printStackTrace();
        }

        for (final Module m : Rise.INSTANCE.getModuleManager().getModuleList()) {
            if (m.isEnabled()) {
                m.toggleModule();
            }
        }

        while (true) {
            assert scan != null;
            if (!scan.hasNextLine()) break;

            final String line = scan.nextLine();

            if (line == null) return;

            final String[] spit = line.split("_");

            if (spit[0].contains("Toggle")) {
                if (spit[2].contains("true")) {
                    if (Rise.INSTANCE.getModuleManager().getModule(spit[1]) != null) {
                        final Module module = Objects.requireNonNull(Rise.INSTANCE.getModuleManager().getModule(spit[1]));

                        if (!module.isEnabled()) {
                            module.toggleModule();
                        }
                    }
                }
            }

            final Setting setting = Rise.INSTANCE.getModuleManager().getSetting(spit[1], spit[2]);

            if (spit[0].contains("BooleanSetting") && setting instanceof BooleanSetting) {
                if (spit[3].contains("true")) {
                    ((BooleanSetting) setting).enabled = true;
                }

                if (spit[3].contains("false")) {
                    ((BooleanSetting) setting).enabled = false;
                }
            }

            if (spit[0].contains("NumberSetting") && setting instanceof NumberSetting) {
                ((NumberSetting) setting).setValue(Double.parseDouble(spit[3]));
            }

            if (spit[0].contains("ModeSetting") && setting instanceof ModeSetting) {
                ((ModeSetting) setting).set(spit[3]);
            }
        }

        //Notification
        Rise.INSTANCE.getNotificationManager().registerNotification("Config loaded " + name + ".");
        Rise.amountOfConfigsLoaded++;
    }
}
