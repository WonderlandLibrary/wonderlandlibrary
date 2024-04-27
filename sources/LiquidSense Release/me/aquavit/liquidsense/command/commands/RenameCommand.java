package me.aquavit.liquidsense.command.commands;

import me.aquavit.liquidsense.command.Command;
import me.aquavit.liquidsense.utils.misc.StringUtils;
import net.minecraft.item.ItemStack;

import static me.aquavit.liquidsense.utils.render.ColorUtils.translateAlternateColorCodes;

public class RenameCommand extends Command {
    public RenameCommand() {
        super("rename");
    }

    @Override
    public void execute(final String[] args) {
        if (args.length <= 1) {
            chatSyntax(".rename <displayName>");
            return;
        }
        if (mc.playerController.isNotCreative()) {
            chat("§c§lError: §3You need creative mode.");
            return;
        }
        final ItemStack item = mc.thePlayer.getHeldItem();
        if (item == null || item.getItem() == null) {
            chat("§c§lError: §3You need to hold a item.");
            return;
        }
        item.setStackDisplayName(translateAlternateColorCodes(StringUtils.toCompleteString(args, 1)));
        chat("§3Item renamed to '" + item.getDisplayName() + "§3'");
    }
}
