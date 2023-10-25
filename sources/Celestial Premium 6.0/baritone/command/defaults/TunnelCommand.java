/*
 * Decompiled with CFR 0.150.
 */
package baritone.command.defaults;

import baritone.api.IBaritone;
import baritone.api.command.Command;
import baritone.api.command.argument.IArgConsumer;
import baritone.api.command.exception.CommandException;
import baritone.api.pathing.goals.GoalStrictDirection;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class TunnelCommand
extends Command {
    public TunnelCommand(IBaritone baritone) {
        super(baritone, "tunnel");
    }

    @Override
    public void execute(String label, IArgConsumer args) throws CommandException {
        args.requireMax(3);
        if (args.hasExactly(3)) {
            boolean cont = true;
            int height = Integer.parseInt(args.getArgs().get(0).getValue());
            int width = Integer.parseInt(args.getArgs().get(1).getValue());
            int depth = Integer.parseInt(args.getArgs().get(2).getValue());
            if (width < 1 || height < 2 || depth < 1 || height > 255) {
                this.logDirect("Width and depth must at least be 1 block; Height must at least be 2 blocks, and cannot be greater than the build limit.");
                cont = false;
            }
            if (cont) {
                BlockPos corner2;
                BlockPos corner1;
                --height;
                EnumFacing enumFacing = this.ctx.player().getHorizontalFacing();
                int addition = --width % 2 == 0 ? 0 : 1;
                switch (enumFacing) {
                    case EAST: {
                        corner1 = new BlockPos(this.ctx.playerFeet().x, this.ctx.playerFeet().y, this.ctx.playerFeet().z - width / 2);
                        corner2 = new BlockPos(this.ctx.playerFeet().x + depth, this.ctx.playerFeet().y + height, this.ctx.playerFeet().z + width / 2 + addition);
                        break;
                    }
                    case WEST: {
                        corner1 = new BlockPos(this.ctx.playerFeet().x, this.ctx.playerFeet().y, this.ctx.playerFeet().z + width / 2 + addition);
                        corner2 = new BlockPos(this.ctx.playerFeet().x - depth, this.ctx.playerFeet().y + height, this.ctx.playerFeet().z - width / 2);
                        break;
                    }
                    case NORTH: {
                        corner1 = new BlockPos(this.ctx.playerFeet().x - width / 2, this.ctx.playerFeet().y, this.ctx.playerFeet().z);
                        corner2 = new BlockPos(this.ctx.playerFeet().x + width / 2 + addition, this.ctx.playerFeet().y + height, this.ctx.playerFeet().z - depth);
                        break;
                    }
                    case SOUTH: {
                        corner1 = new BlockPos(this.ctx.playerFeet().x + width / 2 + addition, this.ctx.playerFeet().y, this.ctx.playerFeet().z);
                        corner2 = new BlockPos(this.ctx.playerFeet().x - width / 2, this.ctx.playerFeet().y + height, this.ctx.playerFeet().z + depth);
                        break;
                    }
                    default: {
                        throw new IllegalStateException("Unexpected value: " + enumFacing);
                    }
                }
                this.logDirect(String.format("Creating a tunnel %s block(s) high, %s block(s) wide, and %s block(s) deep", height + 1, width + 1, depth));
                this.baritone.getBuilderProcess().clearArea(corner1, corner2);
            }
        } else {
            GoalStrictDirection goal = new GoalStrictDirection(this.ctx.playerFeet(), this.ctx.player().getHorizontalFacing());
            this.baritone.getCustomGoalProcess().setGoalAndPath(goal);
            this.logDirect(String.format("Goal: %s", ((Object)goal).toString()));
        }
    }

    @Override
    public Stream<String> tabComplete(String label, IArgConsumer args) {
        return Stream.empty();
    }

    @Override
    public String getShortDesc() {
        return "Set a goal to tunnel in your current direction";
    }

    @Override
    public List<String> getLongDesc() {
        return Arrays.asList("The tunnel command sets a goal that tells Baritone to mine completely straight in the direction that you're facing.", "", "Usage:", "> tunnel - No arguments, mines in a 1x2 radius.", "> tunnel <height> <width> <depth> - Tunnels in a user defined height, width and depth.");
    }
}

