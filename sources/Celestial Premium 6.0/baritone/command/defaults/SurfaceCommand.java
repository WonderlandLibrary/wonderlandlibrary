/*
 * Decompiled with CFR 0.150.
 */
package baritone.command.defaults;

import baritone.api.IBaritone;
import baritone.api.command.Command;
import baritone.api.command.argument.IArgConsumer;
import baritone.api.command.exception.CommandException;
import baritone.api.pathing.goals.GoalBlock;
import baritone.api.utils.BetterBlockPos;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import net.minecraft.block.BlockAir;

public class SurfaceCommand
extends Command {
    protected SurfaceCommand(IBaritone baritone) {
        super(baritone, "surface", "top");
    }

    @Override
    public void execute(String label, IArgConsumer args) throws CommandException {
        int startingYPos;
        BetterBlockPos playerPos = this.baritone.getPlayerContext().playerFeet();
        int surfaceLevel = this.baritone.getPlayerContext().world().getSeaLevel();
        int worldHeight = this.baritone.getPlayerContext().world().getActualHeight();
        if (playerPos.getY() > surfaceLevel && SurfaceCommand.mc.world.getBlockState(playerPos.up()).getBlock() instanceof BlockAir) {
            this.logDirect("Already at surface");
            return;
        }
        for (int currentIteratedY = startingYPos = Math.max(playerPos.getY(), surfaceLevel); currentIteratedY < worldHeight; ++currentIteratedY) {
            BetterBlockPos newPos = new BetterBlockPos(playerPos.getX(), currentIteratedY, playerPos.getZ());
            if (SurfaceCommand.mc.world.getBlockState(newPos).getBlock() instanceof BlockAir || newPos.getY() <= playerPos.getY()) continue;
            GoalBlock goal = new GoalBlock(newPos.up());
            this.logDirect(String.format("Going to: %s", ((Object)goal).toString()));
            this.baritone.getCustomGoalProcess().setGoalAndPath(goal);
            return;
        }
        this.logDirect("No higher location found");
    }

    @Override
    public Stream<String> tabComplete(String label, IArgConsumer args) {
        return Stream.empty();
    }

    @Override
    public String getShortDesc() {
        return "Used to get out of caves, mines, ...";
    }

    @Override
    public List<String> getLongDesc() {
        return Arrays.asList("The surface/top command tells Baritone to head towards the closest surface-like area.", "", "This can be the surface or the highest available air space, depending on circumstances.", "", "Usage:", "> surface - Used to get out of caves, mines, ...", "> top - Used to get out of caves, mines, ...");
    }
}

