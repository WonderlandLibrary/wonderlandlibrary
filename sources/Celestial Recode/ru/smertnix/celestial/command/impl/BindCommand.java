package ru.smertnix.celestial.command.impl;

import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.util.text.TextFormatting;
import ru.smertnix.celestial.Celestial;
import ru.smertnix.celestial.command.CommandAbstract;
import ru.smertnix.celestial.feature.Feature;
import ru.smertnix.celestial.ui.notification.NotificationMode;
import ru.smertnix.celestial.ui.notification.NotificationRenderer;
import ru.smertnix.celestial.utils.other.ChatUtils;

import org.lwjgl.input.Keyboard;

public class BindCommand extends CommandAbstract {

    public BindCommand() {
        super("bind", "bind", ".bind" + ChatFormatting.RED + " add " + "<name> <key> " + TextFormatting.RED + "\n" + "[" + TextFormatting.WHITE + "Celestial" + TextFormatting.GRAY + "] " + ".bind " + ChatFormatting.RED + "remove " + "�7<name> �7<key> " + "\n" + "[" + TextFormatting.WHITE + "Rich Premium" + TextFormatting.GRAY + "] " + ".bind " + ChatFormatting.RED + "list ", "bind");
    }

    @Override
    public void execute(String... arguments) {
        try {
        	if (arguments.length == 3) {
                String moduleName = arguments[1];
                String bind = arguments[2].toUpperCase();
                Feature feature = Celestial.instance.featureManager.getFeature(moduleName);
                 if (arguments[2].equalsIgnoreCase("none")) {
                    feature.setBind(0);
                    ChatUtils.addChatMessage(ChatFormatting.WHITE + "Бинд "  + ChatFormatting.RED + "\'" + feature.getLabel().replaceAll(" ", "") + "\'" + ChatFormatting.WHITE + " не был найден!");
                } else {
                    feature.setBind(Keyboard.getKeyIndex(bind));
                    ChatUtils.addChatMessage(ChatFormatting.WHITE + "Бинд"  + ChatFormatting.RED + "\'" + feature.getLabel().replaceAll(" ", "") + "\'" + ChatFormatting.WHITE + " не был найден - " + ChatFormatting.RED + bind);

                }
            } else if (arguments.length == 1) {
                if (arguments[1].equalsIgnoreCase("list")) {
                    for (Feature f : Celestial.instance.featureManager.getAllFeatures()) {
                        if (f.getBind() != 0) {
                        }

                    }
                } else  if (arguments[1].equalsIgnoreCase("clear")) {
                    for (Feature f : Celestial.instance.featureManager.getAllFeatures()) {
                        if (f.getBind() != 0) {
                            f.setBind(0);
                        }
                    }
                    ChatUtils.addChatMessage(ChatFormatting.WHITE + "Все бинды были очищены!");
                } else {
                }

            } else if (arguments[0].equalsIgnoreCase("bind")) {
            }

        } catch (Exception ignored) {

        }
    }
}