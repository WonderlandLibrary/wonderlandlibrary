package me.aquavit.liquidsense.utils.client;

import me.aquavit.liquidsense.LiquidSense;
import me.aquavit.liquidsense.module.modules.client.Target;
import me.aquavit.liquidsense.module.modules.misc.Spammer;
import me.aquavit.liquidsense.module.modules.misc.NameProtect;
import me.aquavit.liquidsense.module.Module;
import me.aquavit.liquidsense.utils.misc.HttpUtils;
import me.aquavit.liquidsense.utils.render.ColorUtils;
import me.aquavit.liquidsense.value.*;
import org.lwjgl.input.Keyboard;
import me.aquavit.liquidsense.module.ModuleCategory;

import me.aquavit.liquidsense.utils.misc.StringUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class SettingsUtils {
    public void executeScript(String script) {
        String[] lines = script.split("\\r?\\n");
        for (int index = 0; index < lines.length; index++) {
            String line = lines[index].trim();

            if (line.isEmpty() || line.startsWith("#")) {
                continue;
            }

            String[] args = line.split(" ");

            if (args.length <= 1) {
                ClientUtils.displayChatMessage("§7[§3§lAutoSettings§7] §cSyntax error at line '" + index + "' in setting script.\n§8§lLine: §7" + line);
                continue;
            }

            switch (args[0]) {
                case "chat":
                    ClientUtils.displayChatMessage("§7[§3§lAutoSettings§7] §e" + ColorUtils.translateAlternateColorCodes(StringUtils.toCompleteString(args, 1)));
                    break;

                case "unchat":
                    ClientUtils.displayChatMessage(ColorUtils.translateAlternateColorCodes(StringUtils.toCompleteString(args, 1)));
                    break;

                case "load":
                    String urlRaw = StringUtils.toCompleteString(args, 1);
                    String url = urlRaw.startsWith("http") ? urlRaw : LiquidSense.CLIENT_CLOUD + "/settings/" + urlRaw.toLowerCase();
                    try {
                        ClientUtils.displayChatMessage("§7[§3§lAutoSettings§7] §7Loading settings from §a§l" + url + "§7...");
                        executeScript(HttpUtils.get(url));
                        ClientUtils.displayChatMessage("§7[§3§lAutoSettings§7] §7Loaded settings from §a§l" + url + "§7.");
                    } catch (Exception e) {
                        ClientUtils.displayChatMessage("§7[§3§lAutoSettings§7] §7Failed to load settings from §a§l" + url + "§7.");
                    }
                    break;

                case "targetPlayer":
                case "targetPlayers":
                    Target.player.set(true);
                    ClientUtils.displayChatMessage("§7[§3§lAutoSettings§7] §a§l" + args[0] + "§7 set to §c§l" + Target.player.get() + "§7.");
                    break;

                case "targetMobs":
                    Target.mob.set(true);
                    ClientUtils.displayChatMessage("§7[§3§lAutoSettings§7] §a§l" + args[0] + "§7 set to §c§l" + Target.mob.get() + "§7.");
                    break;

                case "targetAnimals":
                    Target.animal.set(true);
                    ClientUtils.displayChatMessage("§7[§3§lAutoSettings§7] §a§l" + args[0] + "§7 set to §c§l" + Target.animal.get() + "§7.");
                    break;

                case "targetInvisible":
                    Target.invisible.set(true);
                    ClientUtils.displayChatMessage("§7[§3§lAutoSettings§7] §a§l" + args[0] + "§7 set to §c§l" + Target.invisible.get() + "§7.");
                    break;

                case "targetDead":
                    Target.dead.set(true);
                    ClientUtils.displayChatMessage("§7[§3§lAutoSettings§7] §a§l" + args[0] + "§7 set to §c§l" + Target.dead.get() + "§7.");
                    break;

                default:
                    if (args.length != 3) {
                        ClientUtils.displayChatMessage("§7[§3§lAutoSettings§7] §cSyntax error at line '" + index + "' in setting script.\n§8§lLine: §7" + line);
                        continue;
                    }

                    String moduleName = args[0];
                    String valueName = args[1];
                    String value = args[2];
                    Module module = LiquidSense.moduleManager.getModule(moduleName);

                    if (module == null) {
                        ClientUtils.displayChatMessage("§7[§3§lAutoSettings§7] §cModule §a§l" + moduleName + "§c was not found!");
                        continue;
                    }

                    if (valueName.equalsIgnoreCase("toggle")) {
                        module.setState(value.equalsIgnoreCase("true"));
                        ClientUtils.displayChatMessage("§7[§3§lAutoSettings§7] §a§l" + module.getName() + "§7 was toggled §c§l" + (module.getState() ? "on" : "off") + "§7.");
                        continue;
                    }

                    if (valueName.equalsIgnoreCase("bind")) {
                        module.setKeyBind(Keyboard.getKeyIndex(value));
                        ClientUtils.displayChatMessage("§7[§3§lAutoSettings§7] §a§l" + module.getName() + "§7 was bound to §c§l" + Keyboard.getKeyName(module.getKeyBind()) + "§7.");
                        continue;
                    }

                    Value<?> moduleValue = module.getValue(valueName);

                    if (moduleValue == null) {
                        ClientUtils.displayChatMessage("§7[§3§lAutoSettings§7] §cValue §a§l" + valueName + "§c not found in module §a§l" + moduleName + "§c.");
                        continue;
                    }

                    try {
                        if (moduleValue instanceof BoolValue) {
                            ((BoolValue) moduleValue).changeValue(Boolean.parseBoolean(value));
                        } else if (moduleValue instanceof FloatValue) {
                            ((FloatValue) moduleValue).changeValue(Float.parseFloat(value));
                        } else if (moduleValue instanceof IntegerValue) {
                            ((IntegerValue) moduleValue).changeValue(Integer.parseInt(value));
                        } else if (moduleValue instanceof TextValue) {
                            ((TextValue) moduleValue).changeValue(value);
                        } else if (moduleValue instanceof ListValue) {
                            ((ListValue) moduleValue).changeValue(value);
                        }

                        ClientUtils.displayChatMessage("§7[§3§lAutoSettings§7] §a§l" + module.getName() + "§7 value §8§l" + moduleValue.getName() + "§7 set to §c§l" + value + "§7.");
                    } catch (Exception e) {
                        ClientUtils.displayChatMessage("§7[§3§lAutoSettings§7] §a§l" + e.getClass().getName() + "§7(" + e.getMessage() + ") §cAn Exception occurred while setting §a§l" + value + "§c to §a§l" + moduleValue.getName() + "§c in §a§l" + module.getName() + "§c.");
                    }
            }
        }

        LiquidSense.fileManager.saveConfig(LiquidSense.fileManager.valuesConfig);
    }

    public String generateScript(final boolean values, final boolean binds, final boolean states) {
        StringBuilder stringBuilder = new StringBuilder();
        LiquidSense.moduleManager.getModules().stream().filter(module -> module.getCategory() != ModuleCategory.RENDER && !(module instanceof NameProtect) && !(module instanceof Spammer)).forEach(module -> {
            if (values) {
                for (Value<?> value : module.getValues()) {
                    stringBuilder.append(module.getName()).append(" ").append(value.getName()).append(" ").append(value.get()).append("\n");
                }
            }
            if (states) {
                stringBuilder.append(module.getName()).append(" toggle ").append(module.getState()).append("\n");
            }
            if (binds) {
                stringBuilder.append(module.getName()).append(" bind ").append(Keyboard.getKeyName(module.getKeyBind())).append("\n");
            }
        });
        return stringBuilder.toString();
    }
}
