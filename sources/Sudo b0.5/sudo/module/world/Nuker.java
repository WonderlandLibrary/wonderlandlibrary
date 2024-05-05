package sudo.module.world;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import sudo.module.Mod;
import sudo.module.settings.NumberSetting;
import sudo.utils.player.RotationUtils;
import sudo.utils.render.RenderUtils;

public class Nuker extends Mod {

	final List<BlockPos> renders = new ArrayList<>();
	public NumberSetting radius = new NumberSetting("Radius", 0, 6, 5, 1);
	public NumberSetting delay = new NumberSetting("Delay", 0, 100, 0, 1);

	public Nuker() {
		super("Nuker", "Automatically breaks block arround you", Category.WORLD, 0);
		addSettings(radius, delay);
	}

	int delayTicks = 0;
	@Override
	public void onTick() {
		BlockPos ppos1 = mc.player.getBlockPos();
		renders.clear();
		for (double y = radius.getValue(); y > -radius.getValue() - 1; y--) {
            for (double x = -radius.getValue(); x < radius.getValue() + 1; x++) {
                for (double z = -radius.getValue(); z < radius.getValue() + 1; z++) {
                    BlockPos vp = new BlockPos(x, y, z);
                    BlockPos np = ppos1.add(vp);
                    Vec3d vp1 = new Vec3d(np.getX(), np.getY(), np.getZ());
                    if (vp1.distanceTo(mc.player.getPos()) >= mc.interactionManager.getReachDistance() - 0.2)
                        continue;
                    BlockState bs = mc.world.getBlockState(np);
                    if (!bs.isAir() && bs.getBlock() != Blocks.WATER && bs.getBlock() != Blocks.LAVA && bs.getBlock() != Blocks.BEDROCK && mc.world.getWorldBorder().contains(np) && delayTicks <= 0) {
                        renders.add(np);
                            mc.interactionManager.updateBlockBreakingProgress(np, Direction.DOWN);
                          RotationUtils.setSilentYaw((float)RotationUtils.getYaw(np));
                          RotationUtils.setSilentPitch((float)RotationUtils.getPitch(np));
                          delayTicks = (int) delay.getValue();
                    }
                    if (delayTicks > 0) delayTicks--;
                }
            }
        }
		super.onTick();
	}
	
	@Override
	public void onDisable() {
		RotationUtils.resetYaw();
		RotationUtils.resetPitch();
		super.onDisable();
	}
	
	@Override
	public void onWorldRender(MatrixStack matrices) {
		for (BlockPos pos : renders) {
			Vec3d render = RenderUtils.getRenderPosition(pos);
			RenderUtils.drawOutlineBox(matrices, new Box(render.x, render.y, render.z, render.x + 1, render.y + 1, render.z + 1), new Color(255,255,255), true);
		}
		super.onWorldRender(matrices);
	}
}