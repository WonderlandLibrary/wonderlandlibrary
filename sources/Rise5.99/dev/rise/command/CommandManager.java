package dev.rise.command;

import dev.rise.Rise;
import dev.rise.module.Module;
import dev.rise.setting.Setting;
import dev.rise.setting.impl.BooleanSetting;
import dev.rise.setting.impl.ModeSetting;
import dev.rise.setting.impl.NumberSetting;

public final class CommandManager {

    public static Command[] COMMANDS;

    public void callCommand(final String input) {
        final String[] spit = input.split(" ");
        final String command = spit[0];
        final String args = input.substring(command.length()).trim();

        for (final Command c : COMMANDS) {
            for (final String alias : c.getCommandInfo().aliases()) {
                if (alias.equalsIgnoreCase(command)) {
                    try {
                        c.onCommand(args, args.split(" "));
                    } catch (final Exception e) {
                        e.printStackTrace();
                        Rise.INSTANCE.getNotificationManager().registerNotification(
                                "Invalid command usage \"" + c.getCommandInfo().syntax() + "\"."
                        );
                    }

                    return;
                }
            }
        }

        for (final Module module : Rise.INSTANCE.getModuleManager().getModuleList()) {
            if (module.getModuleInfo().name().equalsIgnoreCase(command)) {
                if (spit.length > 1) {

                    if (module.getSettingAlternative(spit[1]) != null) {
                        final Setting setting = module.getSettingAlternative(spit[1]);

                        try {
                            try {
                                if (setting instanceof BooleanSetting) {
                                    ((BooleanSetting) setting).setEnabled(Boolean.parseBoolean(spit[2]));
                                } else if (setting instanceof NumberSetting) {
                                    ((NumberSetting) setting).setValue(Double.parseDouble(spit[2]));
                                } else if (setting instanceof ModeSetting) {
                                    ((ModeSetting) setting).set(spit[2]);
                                }


                            } catch (final NumberFormatException ignored) {
                                Rise.INSTANCE.getNotificationManager().registerNotification("Something went wrong! Please type out the setting's name without spaces (eg. Rotation Mode -> RotationMode)");
                                return;
                            }
                        } catch (final ArrayIndexOutOfBoundsException ignored) {
                            Rise.INSTANCE.getNotificationManager().registerNotification("Something went wrong! Please type out the setting's name without spaces (eg. Rotation Mode -> RotationMode)");
                        }

                        return;
                    }

                    Rise.INSTANCE.getNotificationManager().registerNotification("Setting " + spit[1].toLowerCase() + " in " + command.toLowerCase() + " doesn't exist! Please type out the setting's name without spaces (eg. Rotation Mode -> RotationMode)");
                    return;
                }
            }
        }

        Rise.INSTANCE.getNotificationManager().registerNotification("Module or command " + command.toLowerCase() + " doesn't exist.");
    }
}
