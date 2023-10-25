/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.cmd.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import org.apache.commons.lang3.math.NumberUtils;
import org.celestial.client.Celestial;
import org.celestial.client.cmd.CommandAbstract;
import org.celestial.client.feature.Feature;
import org.celestial.client.helpers.misc.ChatHelper;
import org.celestial.client.settings.impl.BooleanSetting;
import org.celestial.client.settings.impl.NumberSetting;
import org.celestial.client.ui.notification.NotificationManager;
import org.celestial.client.ui.notification.NotificationType;

public class SettingCommand
extends CommandAbstract {
    public SettingCommand() {
        super("set", "set", "\u00a76.set" + (Object)((Object)ChatFormatting.LIGHT_PURPLE) + "\u00a73 <module>" + (Object)((Object)ChatFormatting.LIGHT_PURPLE) + " setting name" + (Object)((Object)ChatFormatting.GREEN) + " true / false", "\u00a76.set" + (Object)((Object)ChatFormatting.LIGHT_PURPLE) + "\u00a73v<module>" + (Object)((Object)ChatFormatting.LIGHT_PURPLE) + " setting name" + (Object)((Object)ChatFormatting.GREEN) + " true / false", "set");
    }

    @Override
    public void execute(String ... arguments) {
        block11: {
            try {
                if (arguments.length >= 2) {
                    if (!arguments[0].equalsIgnoreCase("set")) break block11;
                    String moduleName = arguments[1];
                    String settingName = arguments[2];
                    Feature feature = Celestial.instance.featureManager.getFeatureByLabel(moduleName);
                    settingName = settingName.replace("_", " ");
                    if (feature == null) break block11;
                    if (arguments[3].equals("true") || arguments[3].equals("false")) {
                        BooleanSetting booleanSetting = (BooleanSetting)feature.getSettingByName(settingName);
                        if (booleanSetting != null) {
                            boolean state;
                            boolean bl = arguments[3].equals("true") ? true : (state = arguments[3].equals("false") ? false : booleanSetting.getCurrentValue());
                            if (!arguments[3].equals("false") && !arguments[3].equals("true")) {
                                ChatHelper.addChatMessage((Object)((Object)ChatFormatting.RED) + "Invalid boolean! " + (Object)((Object)ChatFormatting.WHITE) + "(Use " + (Object)((Object)ChatFormatting.GREEN) + "\"true\" " + (Object)((Object)ChatFormatting.WHITE) + "or " + (Object)((Object)ChatFormatting.GREEN) + "\"false\"" + (Object)((Object)ChatFormatting.WHITE) + ")");
                                NotificationManager.publicity("Setting Mananger", (Object)((Object)ChatFormatting.RED) + "Invalid boolean! " + (Object)((Object)ChatFormatting.WHITE) + "(Use " + (Object)((Object)ChatFormatting.GREEN) + "\"true\" " + (Object)((Object)ChatFormatting.WHITE) + "or " + (Object)((Object)ChatFormatting.GREEN) + "\"false\"" + (Object)((Object)ChatFormatting.WHITE) + ")", 4, NotificationType.ERROR);
                                return;
                            }
                            booleanSetting.setValue(state);
                            ChatHelper.addChatMessage((Object)((Object)ChatFormatting.GREEN) + "Successfully set " + (Object)((Object)ChatFormatting.WHITE) + "\"" + settingName.toUpperCase() + "\" to " + (arguments[3].equals("true") ? (Object)((Object)ChatFormatting.GREEN) + "true" : (Object)((Object)ChatFormatting.RED) + "false"));
                            NotificationManager.publicity("Setting Mananger", (Object)((Object)ChatFormatting.GREEN) + "Successfully set " + (Object)((Object)ChatFormatting.WHITE) + "\"" + settingName.toUpperCase() + "\" to " + (arguments[3].equals("true") ? (Object)((Object)ChatFormatting.GREEN) + "true" : (Object)((Object)ChatFormatting.RED) + "false"), 4, arguments[3].equals("true") ? NotificationType.SUCCESS : NotificationType.INFO);
                        }
                    } else if (NumberUtils.isParsable(arguments[3])) {
                        NumberSetting numberSetting = (NumberSetting)feature.getSettingByName(settingName);
                        if (numberSetting != null) {
                            numberSetting.setCurrentValue(Float.parseFloat(arguments[3]));
                            ChatHelper.addChatMessage((Object)((Object)ChatFormatting.GREEN) + "Successfully set " + (Object)((Object)ChatFormatting.WHITE) + "\"" + settingName.toUpperCase() + "\" to " + (Object)((Object)ChatFormatting.GREEN) + arguments[3]);
                            NotificationManager.publicity("Setting Mananger", (Object)((Object)ChatFormatting.GREEN) + "Successfully set " + (Object)((Object)ChatFormatting.WHITE) + "\"" + settingName.toUpperCase() + "\" to " + (Object)((Object)ChatFormatting.GREEN) + arguments[3], 4, arguments[3].equals("true") ? NotificationType.SUCCESS : NotificationType.INFO);
                        }
                    } else {
                        ChatHelper.addChatMessage((Object)((Object)ChatFormatting.RED) + "Invalid number! " + (Object)((Object)ChatFormatting.WHITE) + "(Use " + (Object)((Object)ChatFormatting.GREEN) + "\"0.5\" for example)");
                        NotificationManager.publicity("Setting Mananger", (Object)((Object)ChatFormatting.RED) + "Invalid number! " + (Object)((Object)ChatFormatting.WHITE) + "(Use " + (Object)((Object)ChatFormatting.GREEN) + "\"0.5\" for example)", 4, NotificationType.ERROR);
                    }
                    break block11;
                }
                ChatHelper.addChatMessage(this.getUsage());
                ChatHelper.addChatMessage((Object)((Object)ChatFormatting.LIGHT_PURPLE) + "Example: " + (Object)((Object)ChatFormatting.WHITE) + ".set " + (Object)((Object)ChatFormatting.RED) + "killaura only_crits " + (Object)((Object)ChatFormatting.GREEN) + "true / false");
                ChatHelper.addChatMessage((Object)((Object)ChatFormatting.LIGHT_PURPLE) + "Example: " + (Object)((Object)ChatFormatting.WHITE) + ".set " + (Object)((Object)ChatFormatting.RED) + "flight flight_speed " + (Object)((Object)ChatFormatting.GREEN) + "10");
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
    }
}

