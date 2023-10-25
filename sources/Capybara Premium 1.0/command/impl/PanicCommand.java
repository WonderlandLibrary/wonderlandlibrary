package fun.rich.client.command.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import fun.rich.client.Rich;
import fun.rich.client.command.CommandAbstract;
import fun.rich.client.feature.Feature;
import fun.rich.client.feature.impl.FeatureCategory;
import fun.rich.client.utils.other.ChatUtils;

public class PanicCommand extends CommandAbstract {

    public PanicCommand() {
        super("panic", "Disabled all modules", ".panic", "panic");
    }

    @Override
    public void execute(String... args) {
        if (args[0].equalsIgnoreCase("panic")) {
            for (Feature feature : Rich.instance.featureManager.getAllFeatures()) {
                if (feature.isEnabled()) {
                    feature.toggle();
                }
            }
            ChatUtils.addChatMessage(ChatFormatting.GREEN + "Успешно " + ChatFormatting.RED + "выключенны " + ChatFormatting.WHITE + "все модули!");
        }
    }
}
