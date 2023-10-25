/*
 * Decompiled with CFR 0.150.
 */
package baritone.command.defaults;

import baritone.api.IBaritone;
import baritone.api.command.Command;
import baritone.api.command.argument.IArgConsumer;
import baritone.api.command.exception.CommandException;
import baritone.api.utils.BetterBlockPos;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class RenderCommand
extends Command {
    public RenderCommand(IBaritone baritone) {
        super(baritone, "render");
    }

    @Override
    public void execute(String label, IArgConsumer args) throws CommandException {
        args.requireMax(0);
        BetterBlockPos origin = this.ctx.playerFeet();
        int renderDistance = (RenderCommand.mc.gameSettings.renderDistanceChunks + 1) * 16;
        RenderCommand.mc.renderGlobal.markBlockRangeForRenderUpdate(origin.x - renderDistance, 0, origin.z - renderDistance, origin.x + renderDistance, 255, origin.z + renderDistance);
        this.logDirect("Done");
    }

    @Override
    public Stream<String> tabComplete(String label, IArgConsumer args) {
        return Stream.empty();
    }

    @Override
    public String getShortDesc() {
        return "Fix glitched chunks";
    }

    @Override
    public List<String> getLongDesc() {
        return Arrays.asList("The render command fixes glitched chunk rendering without having to reload all of them.", "", "Usage:", "> render");
    }
}

