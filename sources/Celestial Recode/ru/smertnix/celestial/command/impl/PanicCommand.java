package ru.smertnix.celestial.command.impl;

import com.mojang.realmsclient.gui.ChatFormatting;

import ru.smertnix.celestial.Celestial;
import ru.smertnix.celestial.command.CommandAbstract;
import ru.smertnix.celestial.feature.Feature;
import ru.smertnix.celestial.feature.impl.FeatureCategory;
import ru.smertnix.celestial.utils.other.ChatUtils;

public class PanicCommand extends CommandAbstract {

    public PanicCommand() {
        super("panic", "Disabled all modules", ".panic", "panic");
    }

    @Override
    public void execute(String... args) {
        if (args[0].equalsIgnoreCase("panic")) {
            for (Feature feature : Celestial.instance.featureManager.getAllFeatures()) {
                if (feature.isEnabled()) {
                    feature.toggle();
                }
            }
            ChatUtils.addChatMessage(ChatFormatting.GREEN + "Модули " + ChatFormatting.RED + "были" + ChatFormatting.WHITE + "выключены!");
        }
    }
}
