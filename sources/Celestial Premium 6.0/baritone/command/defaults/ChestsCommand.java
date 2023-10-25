/*
 * Decompiled with CFR 0.150.
 */
package baritone.command.defaults;

import baritone.api.IBaritone;
import baritone.api.cache.IRememberedInventory;
import baritone.api.command.Command;
import baritone.api.command.argument.IArgConsumer;
import baritone.api.command.exception.CommandException;
import baritone.api.command.exception.CommandInvalidStateException;
import baritone.api.utils.BetterBlockPos;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;

public class ChestsCommand
extends Command {
    public ChestsCommand(IBaritone baritone) {
        super(baritone, "chests");
    }

    @Override
    public void execute(String label, IArgConsumer args) throws CommandException {
        args.requireMax(0);
        Set<Map.Entry<BlockPos, IRememberedInventory>> entries = this.ctx.worldData().getContainerMemory().getRememberedInventories().entrySet();
        if (entries.isEmpty()) {
            throw new CommandInvalidStateException("No remembered inventories");
        }
        for (Map.Entry<BlockPos, IRememberedInventory> entry : entries) {
            BetterBlockPos pos = new BetterBlockPos(entry.getKey());
            IRememberedInventory inv = entry.getValue();
            this.logDirect(pos.toString());
            for (ItemStack item : inv.getContents()) {
                ITextComponent component = item.getTextComponent();
                component.appendText(String.format(" x %d", item.getCount()));
                this.logDirect(component);
            }
        }
    }

    @Override
    public Stream<String> tabComplete(String label, IArgConsumer args) {
        return Stream.empty();
    }

    @Override
    public String getShortDesc() {
        return "Display remembered inventories";
    }

    @Override
    public List<String> getLongDesc() {
        return Arrays.asList("The chests command lists remembered inventories, I guess?", "", "Usage:", "> chests");
    }
}

