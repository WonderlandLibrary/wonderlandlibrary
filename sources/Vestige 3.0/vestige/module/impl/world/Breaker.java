package vestige.module.impl.world;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockLiquid;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import org.lwjgl.input.Mouse;
import vestige.event.Listener;
import vestige.event.impl.JumpEvent;
import vestige.event.impl.MotionEvent;
import vestige.event.impl.StrafeEvent;
import vestige.event.impl.TickEvent;
import vestige.module.Category;
import vestige.module.Module;
import vestige.setting.impl.BooleanSetting;
import vestige.setting.impl.IntegerSetting;
import vestige.util.player.FixedRotations;
import vestige.util.player.RotationsUtil;

public class Breaker extends Module {

    private BlockPos bedPos;

    private FixedRotations rotations;

    private final IntegerSetting range = new IntegerSetting("Range", 4, 1, 6, 1);

    private final BooleanSetting rotate = new BooleanSetting("Rotate", true);
    private final BooleanSetting moveFix = new BooleanSetting("Move fix", () -> rotate.isEnabled(), false);

    private final BooleanSetting hypixel = new BooleanSetting("Hypixel", false);

    public Breaker() {
        super("Breaker", Category.WORLD);
        this.addSettings(range, rotate, moveFix, hypixel);
    }

    @Override
    public void onEnable() {
        bedPos = null;

        rotations = new FixedRotations(mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch);
    }

    @Override
    public void onDisable() {
        mc.gameSettings.keyBindAttack.pressed = Mouse.isButtonDown(0);
    }

    @Listener
    public void onTick(TickEvent event) {
        bedPos = null;

        boolean found = false;

        float yaw = rotations.getYaw();
        float pitch = rotations.getPitch();

        for(double x = mc.thePlayer.posX - range.getValue(); x <= mc.thePlayer.posX + range.getValue(); x++) {
            for(double y = mc.thePlayer.posY + mc.thePlayer.getEyeHeight() - range.getValue(); y <= mc.thePlayer.posY + mc.thePlayer.getEyeHeight() + range.getValue(); y++) {
                for(double z = mc.thePlayer.posZ - range.getValue(); z <= mc.thePlayer.posZ + range.getValue(); z++) {
                    BlockPos pos = new BlockPos(x, y, z);

                    if(mc.theWorld.getBlockState(pos).getBlock() instanceof BlockBed && !found) {
                        bedPos = pos;

                        if(hypixel.isEnabled() && isBlockOver(bedPos)) {
                            BlockPos posOver = pos.add(0, 1, 0);

                            mc.objectMouseOver = new MovingObjectPosition(new Vec3(posOver.getX() + 0.5, posOver.getY() + 1, posOver.getZ() + 0.5), EnumFacing.UP, posOver);

                            mc.gameSettings.keyBindAttack.pressed = true;

                            float rots[] = RotationsUtil.getRotationsToPosition(posOver.getX() + 0.5, posOver.getY() + 1, posOver.getZ() + 0.5);

                            yaw = rots[0];
                            pitch = rots[1];
                        } else {
                            mc.objectMouseOver = new MovingObjectPosition(new Vec3(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5), EnumFacing.UP, bedPos);

                            mc.gameSettings.keyBindAttack.pressed = true;

                            float rots[] = RotationsUtil.getRotationsToPosition(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);

                            yaw = rots[0];
                            pitch = rots[1];
                        }

                        found = true;
                    }
                }
            }
        }

        if(!found) {
            mc.gameSettings.keyBindAttack.pressed = Mouse.isButtonDown(0);
        }

        rotations.updateRotations(yaw, pitch);
    }

    public boolean isBlockOver(BlockPos pos) {
        BlockPos posOver = pos.add(0, 1, 0);

        Block block = mc.theWorld.getBlockState(posOver).getBlock();

        return !(block instanceof BlockAir || block instanceof BlockLiquid);
    }

    public boolean isBreakingBed() {
        return bedPos != null;
    }

    @Listener
    public void onStrafe(StrafeEvent event) {
        if(bedPos != null && rotate.isEnabled() && moveFix.isEnabled()) {
            event.setYaw(rotations.getYaw());
        }
    }

    @Listener
    public void onJump(JumpEvent event) {
        if(bedPos != null && rotate.isEnabled() && moveFix.isEnabled()) {
            event.setYaw(rotations.getYaw());
        }
    }

    @Listener
    public void onMotion(MotionEvent event) {
        if(bedPos != null && rotate.isEnabled()) {
            event.setYaw(rotations.getYaw());
            event.setPitch(rotations.getPitch());
        }
    }

}
