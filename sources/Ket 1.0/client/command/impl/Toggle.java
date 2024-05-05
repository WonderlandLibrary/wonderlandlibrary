package client.command.impl;

import client.util.ChatUtil;
import net.minecraft.util.EnumChatFormatting;
import client.Client;
import client.command.Command;
import client.module.Module;

public final class Toggle extends Command {

    public Toggle() {
        super("Toggles the specified module", "toggle", "t");
    }

    @Override
    public void execute(final String[] args) {
        if (args.length != 2) {
            error(String.format(".%s <module>", args[0]));
            return;
        }
        final Module module = Client.INSTANCE.getModuleManager().get(args[1]);
        if (module == null) {
            ChatUtil.display("Invalid module");
            return;
        }
        module.toggle();
        ChatUtil.display((module.isEnabled() ? EnumChatFormatting.GREEN + "Enabled" : EnumChatFormatting.RED + "Disabled") + EnumChatFormatting.RESET + " %s", module.getInfo().name());
    }
}