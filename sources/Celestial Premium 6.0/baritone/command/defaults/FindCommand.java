/*
 * Decompiled with CFR 0.150.
 */
package baritone.command.defaults;

import baritone.api.IBaritone;
import baritone.api.command.Command;
import baritone.api.command.argument.IArgConsumer;
import baritone.api.command.datatypes.BlockById;
import baritone.api.command.exception.CommandException;
import baritone.api.utils.BetterBlockPos;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import net.minecraft.block.Block;

public class FindCommand
extends Command {
    public FindCommand(IBaritone baritone) {
        super(baritone, "find");
    }

    @Override
    public void execute(String label, IArgConsumer args) throws CommandException {
        ArrayList toFind = new ArrayList();
        while (args.hasAny()) {
            toFind.add(args.getDatatypeFor(BlockById.INSTANCE));
        }
        BetterBlockPos origin = this.ctx.playerFeet();
        toFind.stream().flatMap(block -> this.ctx.worldData().getCachedWorld().getLocationsOf(Block.REGISTRY.getNameForObject((Block)block).getPath(), Integer.MAX_VALUE, origin.x, origin.y, 4).stream()).map(BetterBlockPos::new).map(BetterBlockPos::toString).forEach(this::logDirect);
    }

    @Override
    public Stream<String> tabComplete(String label, IArgConsumer args) {
        return args.tabCompleteDatatype(BlockById.INSTANCE);
    }

    @Override
    public String getShortDesc() {
        return "Find positions of a certain block";
    }

    @Override
    public List<String> getLongDesc() {
        return Arrays.asList("", "", "Usage:", "> ");
    }
}

