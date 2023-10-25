package fun.expensive.client.command.impl;


import com.mojang.realmsclient.gui.ChatFormatting;
import fun.rich.client.command.CommandAbstract;
import fun.rich.client.ui.notification.NotificationMode;
import fun.rich.client.ui.notification.NotificationRenderer;
import fun.rich.client.utils.other.ChatUtils;

public class FakeNameCommand
        extends CommandAbstract {
    public static String oldName;
    public static String currentName;
    public static boolean canChange;

    public FakeNameCommand() {
        super("fakename", "fakename", "§6.fakename" + ChatFormatting.WHITE + " set §3<name> |" + ChatFormatting.WHITE + " reset", "§6.fakename" + ChatFormatting.WHITE + " set §3<name> |" + ChatFormatting.WHITE + " reset", "fakename");
    }

    @Override
    public void execute(String ... arguments) {
        try {
            if (arguments.length >= 2) {
                oldName = FakeNameCommand.mc.player.getName();
                if (arguments[0].equalsIgnoreCase("fakename")) {
                    if (arguments[1].equalsIgnoreCase("set")) {
                        currentName = arguments[2];
                        canChange = true;
                        ChatUtils.addChatMessage(ChatFormatting.GREEN + "Successfully" + ChatFormatting.WHITE + " changed your name to " + ChatFormatting.RED + arguments[2]);
                        NotificationRenderer.queue("FakeName Manager", ChatFormatting.GREEN + "Successfully" + ChatFormatting.WHITE + " changed your name to " + ChatFormatting.RED + arguments[2], 4, NotificationMode.SUCCESS);
                    }
                    if (arguments[1].equalsIgnoreCase("reset")) {
                        currentName = oldName;
                        canChange = false;
                        ChatUtils.addChatMessage(ChatFormatting.GREEN + "Successfully" + ChatFormatting.WHITE + " reset your name!");
                        NotificationRenderer.queue("FakeName Manager", ChatFormatting.GREEN + "Successfully" + ChatFormatting.WHITE + " reset your name!", 4, NotificationMode.SUCCESS);
                    }
                }
            } else {
                ChatUtils.addChatMessage(this.getUsage());
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }
}
