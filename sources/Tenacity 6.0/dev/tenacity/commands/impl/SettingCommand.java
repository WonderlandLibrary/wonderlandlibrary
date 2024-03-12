// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.commands.impl;

import java.util.Iterator;
import dev.tenacity.module.Module;
import java.awt.Color;
import dev.tenacity.module.settings.impl.ColorSetting;
import dev.tenacity.module.settings.impl.MultipleBoolSetting;
import dev.tenacity.module.settings.impl.BooleanSetting;
import dev.tenacity.module.settings.impl.StringSetting;
import dev.tenacity.module.settings.impl.NumberSetting;
import dev.tenacity.module.settings.impl.ModeSetting;
import java.util.Arrays;
import dev.tenacity.module.settings.Setting;
import dev.tenacity.Tenacity;
import dev.tenacity.commands.Command;

public class SettingCommand extends Command
{
    public SettingCommand() {
        super("setting", "Changes settings of a module", ".setting module_name setting_name value", new String[0]);
    }
    
    @Override
    public void execute(final String[] args) {
        if (args.length < 1) {
            this.usage();
        }
        else {
            final String moduleName = args[0].replace("_", " ");
            final Module module = Tenacity.INSTANCE.getModuleCollection().getModuleByName(moduleName);
            if (module == null) {
                this.sendChatError("Module not found");
                this.usage();
                return;
            }
            final String sendSuccess = args[args.length - 1];
            if (sendSuccess.equals("sendSuccess=false")) {
                Command.sendSuccess = false;
            }
            try {
                if (args.length < 2) {
                    this.sendChatError("No setting specified for the module " + module.getName());
                    this.usage();
                    return;
                }
                boolean found = false;
                for (final Setting setting : module.getSettingsList()) {
                    String settingName = setting.name.replaceAll(" ", "_");
                    final String settingInputName = args[1];
                    if (settingName.equalsIgnoreCase(settingInputName)) {
                        found = true;
                        boolean setValue = false;
                        final String value = String.join(" ", (CharSequence[])Arrays.copyOfRange(args, 2, Command.sendSuccess ? args.length : (args.length - 1)));
                        if (setting instanceof ModeSetting) {
                            final ModeSetting modeSetting = (ModeSetting)setting;
                            for (final String mode : modeSetting.modes) {
                                if (mode.equalsIgnoreCase(value)) {
                                    modeSetting.setCurrentMode(mode);
                                    setValue = true;
                                    break;
                                }
                            }
                            if (!setValue) {
                                this.sendChatError("Invalid mode");
                            }
                        }
                        if (setting instanceof NumberSetting) {
                            final NumberSetting numberSetting = (NumberSetting)setting;
                            double valueDouble;
                            try {
                                valueDouble = Double.parseDouble(value);
                            }
                            catch (NumberFormatException e2) {
                                this.sendChatError("Invalid number");
                                return;
                            }
                            numberSetting.setValue(valueDouble);
                            this.sendChatWithInfo(numberSetting.getValue() + " value");
                            settingName = settingName.replaceAll("_", " ");
                            this.sendChatWithPrefix("Set " + module.getName() + " module's " + settingName + " to " + numberSetting.getValue());
                            break;
                        }
                        if (setting instanceof StringSetting) {
                            final StringSetting stringSetting = (StringSetting)setting;
                            stringSetting.setString(value);
                            setValue = true;
                        }
                        if (setting instanceof BooleanSetting) {
                            final BooleanSetting booleanSetting = (BooleanSetting)setting;
                            booleanSetting.setState(Boolean.parseBoolean(value));
                            setValue = true;
                        }
                        if (setting instanceof MultipleBoolSetting) {
                            final MultipleBoolSetting multipleBoolSetting = (MultipleBoolSetting)setting;
                            final String[] inputBool = value.split(" ");
                            final String boolName = inputBool[0].replace("_", " ");
                            final BooleanSetting booleanSetting2 = multipleBoolSetting.getSetting(boolName);
                            if (booleanSetting2 == null) {
                                this.sendChatError("Invalid boolean setting name, " + boolName + ". (In multiple bool setting)");
                                return;
                            }
                            booleanSetting2.setState(Boolean.parseBoolean(inputBool[1]));
                            settingName = settingName.replaceAll("_", " ");
                            this.sendChatWithPrefix("Set " + module.getName() + " module's " + settingName + "'s " + boolName + " to " + inputBool[1]);
                            break;
                        }
                        else {
                            if (setting instanceof ColorSetting) {
                                final ColorSetting colorSetting = (ColorSetting)setting;
                                Color newColor;
                                try {
                                    newColor = Color.decode("#" + value);
                                }
                                catch (NumberFormatException e3) {
                                    this.sendChatError("Invalid hex");
                                    return;
                                }
                                colorSetting.setColor(newColor);
                                setValue = true;
                            }
                            if (setValue) {
                                settingName = settingName.replaceAll("_", " ");
                                this.sendChatWithPrefix("Set " + module.getName() + " " + settingName + " to " + value);
                                break;
                            }
                            continue;
                        }
                    }
                }
                if (!found) {
                    this.sendChatError("Setting not found");
                }
            }
            catch (Exception e) {
                this.sendChatError("Error: " + e.getMessage());
                this.usage();
            }
            SettingCommand.sendSuccess = true;
        }
    }
}
