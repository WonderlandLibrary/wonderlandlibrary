package today.flux.module.implement.Movement;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.block.BlockLiquid;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import today.flux.event.BBSetEvent;
import today.flux.event.JumpEvent;
import today.flux.event.PreUpdateEvent;
import today.flux.module.Category;
import today.flux.module.Module;
import today.flux.module.implement.Combat.Criticals;
import today.flux.module.value.ModeValue;
import today.flux.utility.BlockUtils;
import today.flux.utility.ChatUtils;
import today.flux.utility.DelayTimer;

public class Jesus extends Module {
    public Jesus() {
        super("Jesus", Category.Movement, mode);
    }

    public static ModeValue mode = new ModeValue("Jesus", "Mode", "Solid", "Solid", "BHop", "Motion");

    private DelayTimer timer = new DelayTimer();

    @EventTarget
    public void onBounding(BBSetEvent event) {
        if (!mode.getValue().equals("Solid"))
            return;

        if (event.getBlock() instanceof BlockLiquid && !this.mc.thePlayer.isInWater() && !this.mc.thePlayer.isInLava() && this.isPossible(event.pos)) {
        	event.setBoundingBox(new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0).contract(0.0, 2.0E-12, 0.0)
					.offset(event.getPos().getX(), event.getPos().getY(), event.getPos().getZ()));
        }
    }

    @EventTarget
    public void onJump(JumpEvent event) {
        if (!mode.getValue().equals("Solid")) return;

        if (this.mc.thePlayer.onGround && !this.mc.thePlayer.isInWater() && BlockUtils.isOnLiquid() && !this.mc.thePlayer.isSneaking())
            event.setCancelled(true);
    }

    @EventTarget
    public void onUpdatePre1(PreUpdateEvent event) {
        if (!mode.getValue().equals("Solid")) return;

        if (this.mc.thePlayer.onGround && !this.mc.thePlayer.isInWater() && BlockUtils.isOnLiquid() && !this.mc.thePlayer.isSneaking()) {
            if (mc.gameSettings.keyBindJump.isKeyDown()) mc.thePlayer.motionY = 0.1;

        	event.y += this.mc.thePlayer.ticksExisted % 2 == 0 ? 2.0E-12 : 0.0;

            Criticals.delayHoped.reset();
            ChatUtils.debug("Jesus [" + this.mc.thePlayer.ticksExisted + "]");
        }
    }


    @EventTarget
    public void onUpdatePre(PreUpdateEvent event) {
        if (BlockUtils.isInLiquid() && this.mc.thePlayer.movementInput.jump) {
            this.mc.thePlayer.movementInput.jump = false;
            this.mc.thePlayer.setJumping(false);
        }

        if (mode.getValue().equals("Motion")) {
            if (BlockUtils.isInLiquid()) {
                this.mc.thePlayer.motionY = 0.1;
            }
            return;
        }

        if (mode.getValue().equals("BHop")) {
            if (BlockUtils.isInLiquid() && timer.hasPassed(200) && !this.mc.gameSettings.keyBindSneak.isKeyDown() && !this.mc.thePlayer.isInWater()) {
                float forward = this.mc.thePlayer.movementInput.moveForward;
                float strafe = this.mc.thePlayer.movementInput.moveStrafe;

                if ((forward != 0.0F || strafe != 0.0F)) {
                    this.mc.thePlayer.motionX = 0;
                    this.mc.thePlayer.motionZ = 0;

                    //jump
                    this.mc.thePlayer.motionY = 0.4D;
                    this.mc.thePlayer.isAirBorne = true;

                    //move
                    float var4 = strafe * strafe + forward * forward;
                    var4 = MathHelper.sqrt_float(var4);

                    if (var4 < 1.0F) {
                        var4 = 1.0F;
                    }

                    var4 = 0.26f / var4;
                    strafe *= var4;
                    forward *= var4;
                    float var5 = MathHelper.sin(this.mc.thePlayer.rotationYaw * (float) Math.PI / 180.0F);
                    float var6 = MathHelper.cos(this.mc.thePlayer.rotationYaw * (float) Math.PI / 180.0F);
                    this.mc.thePlayer.motionX += (double) (strafe * var6 - forward * var5);
                    this.mc.thePlayer.motionZ += (double) (forward * var6 + strafe * var5);

                    timer.reset();
                } else {
                    this.mc.thePlayer.motionX = 0;
                    this.mc.thePlayer.motionZ = 0;

                    this.mc.thePlayer.motionY = 0.085D;
                }
            } else if (this.mc.thePlayer.isInWater() && this.mc.thePlayer.movementInput.jump) {
                this.mc.thePlayer.motionY = 0.08;
            }
            return;
        }
    }

    private boolean isPossible(BlockPos pos) {
        final int i = this.mc.theWorld.getBlockState(pos).getBlock().getMetaFromState(this.mc.theWorld.getBlockState(pos));
        return i < 5 && this.mc.thePlayer.fallDistance <= 3.0f && !this.mc.thePlayer.isSneaking();
    }
}
