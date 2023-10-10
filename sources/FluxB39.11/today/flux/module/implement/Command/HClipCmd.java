package today.flux.module.implement.Command;

import today.flux.module.Command;
import today.flux.utility.MathUtils;

/**
 * Created by John on 2016/12/13.
 */
@Command.Info(name = "hclip", syntax = { "<distance>" }, help = "Teleport in the horizontal direction")
public class HClipCmd extends Command {
    @Override
    public void execute(String[] args) throws Error {
        if (args.length > 1) {
            this.syntaxError();
        } else if (args.length == 1) {
            if (MathUtils.isDouble(args[0])) {
                final float multiplier = Float.parseFloat(args[0]);
                final double mx = Math.cos(Math.toRadians(this.mc.thePlayer.rotationYaw + 90.0f));
                final double mz = Math.sin(Math.toRadians(this.mc.thePlayer.rotationYaw + 90.0f));
                final double x = 1.0f * multiplier * mx + 0.0f * multiplier * mz;
                final double z = 1.0f * multiplier * mz - 0.0f * multiplier * mx;
                this.mc.thePlayer.getEntityBoundingBox().offsetAndUpdate(x, 0.0, z);
            } else {
                this.syntaxError();
            }
        } else {
            this.syntaxError();
        }
    }
}