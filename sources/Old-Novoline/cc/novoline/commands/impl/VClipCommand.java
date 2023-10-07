package cc.novoline.commands.impl;

import cc.novoline.Novoline;
import cc.novoline.commands.NovoCommand;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.command.CommandException;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class VClipCommand extends NovoCommand {

    /* constructors */
    public VClipCommand(@NonNull Novoline novoline) {
        super(novoline, "vc", "vclip");
    }

    /* methods */
    @Override
    public void process(String[] args) throws CommandException {
        if (args.length != 1) {
            notifyError("Use .vclip (height)");
            return;
        }

        final EntityPlayerSP p = Minecraft.getInstance().player;
        final double height = getDouble(args[0]);

        p.setPositionAndUpdate(p.posX, p.posY + height, p.posZ);
    }

}
