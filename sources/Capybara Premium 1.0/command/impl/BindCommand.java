package fun.expensive.client.command.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import fun.rich.client.Rich;
import fun.rich.client.command.CommandAbstract;
import fun.rich.client.feature.Feature;
import fun.rich.client.ui.notification.NotificationMode;
import fun.rich.client.ui.notification.NotificationRenderer;
import fun.rich.client.utils.other.ChatUtils;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.input.Keyboard;

public class BindCommand extends CommandAbstract {

    public BindCommand() {
        super("bind", "bind", "§6.bind" + ChatFormatting.RED + " add " + "§7<name> §7<key> " + TextFormatting.RED + "\n" + "[" + TextFormatting.WHITE + "CAPYBARA PREMIUM" + TextFormatting.GRAY + "] " + "§6.bind " + ChatFormatting.RED + "remove " + "§7<name> §7<key> " + "\n" + "[" + TextFormatting.WHITE + "Capybara Premium" + TextFormatting.GRAY + "] " + "§6.bind " + ChatFormatting.RED + "list ", "bind");
    }

    @Override
    public void execute(String... arguments) {
        try {
            if (arguments.length == 4) {
                String moduleName = arguments[2];
                String bind = arguments[3].toUpperCase();
                Feature feature = Rich.instance.featureManager.getFeature(moduleName);
                if (arguments[0].equalsIgnoreCase("bind") && arguments[1].equalsIgnoreCase("add")) {
                    feature.setBind(Keyboard.getKeyIndex(bind));
                    ChatUtils.addChatMessage(ChatFormatting.GREEN + feature.getLabel() + ChatFormatting.WHITE + " was set on key " + ChatFormatting.RED + "\"" + bind + "\"");
                    NotificationRenderer.queue("Bind Manager", ChatFormatting.GREEN + feature.getLabel() + ChatFormatting.WHITE + " was set on key " + ChatFormatting.RED + "\"" + bind + "\"", 1, NotificationMode.SUCCESS);

                } else if (arguments[0].equalsIgnoreCase("bind") && arguments[1].equalsIgnoreCase("remove")) {
                    feature.setBind(0);
                    ChatUtils.addChatMessage(ChatFormatting.GREEN + feature.getLabel() + ChatFormatting.WHITE + " bind was deleted from key " + ChatFormatting.RED + "\"" + bind + "\"");
                    NotificationRenderer.queue("Bind Manager", ChatFormatting.GREEN + feature.getLabel() + ChatFormatting.WHITE + " bind was deleted from key " + ChatFormatting.RED + "\"" + bind + "\"", 1, NotificationMode.SUCCESS);
                }
            } else if (arguments.length == 2) {
                if (arguments[0].equalsIgnoreCase("bind") && arguments[1].equalsIgnoreCase("list")) {
                    for (Feature f : Rich.instance.featureManager.getAllFeatures()) {
                        if (f.getBind() != 0) {
                            ChatUtils.addChatMessage(f.getLabel() + " : " + Keyboard.getKeyName(f.getBind()));
                        }

                    }
                } else {
                    ChatUtils.addChatMessage(this.getUsage());
                }

            } else if (arguments[0].equalsIgnoreCase("bind")) {
                ChatUtils.addChatMessage(this.getUsage());
            }

        } catch (Exception ignored) {

        }
    }
}