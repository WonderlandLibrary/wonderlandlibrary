/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.module.specials.flys;

import net.minecraft.block.BlockAir;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import org.greenrobot.eventbus.Subscribe;
import org.lwjgl.input.Keyboard;
import tk.rektsky.event.impl.BlockBBEvent;
import tk.rektsky.event.impl.ClientTickPostEvent;
import tk.rektsky.event.impl.MotionUpdateEvent;
import tk.rektsky.event.impl.UpdateSprintingEvent;
import tk.rektsky.module.ModulesManager;
import tk.rektsky.module.impl.movement.Fly;
import tk.rektsky.module.impl.movement.Speed;
import tk.rektsky.module.impl.render.Notification;
import tk.rektsky.utils.MovementUtil;
import tk.rektsky.utils.display.ColorUtil;

public class VerusFly {
    static Minecraft mc = Minecraft.getMinecraft();
    public double launchY;
    int jumps = 0;
    public long startTime = 0L;
    public boolean sawWarning = false;
    public boolean flagging = false;

    public void onDisable() {
        this.startTime = System.currentTimeMillis();
    }

    public void onEnable() {
        this.sawWarning = false;
        this.flagging = false;
        if (!VerusFly.mc.thePlayer.onGround) {
            ModulesManager.getModuleByClass(Fly.class).setToggled(false);
            Notification.displayNotification(new Notification.PopupMessage("Fly", "You are going to flag fly if you are not on the ground", ColorUtil.NotificationColors.YELLOW, 40));
            this.flagging = true;
            return;
        }
        this.launchY = VerusFly.mc.thePlayer.posY;
        Speed speed = ModulesManager.getModuleByClass(Speed.class);
        if (speed != null && speed.isToggled()) {
            speed.setToggled(false);
        }
        this.jumps = 0;
    }

    @Subscribe
    public void onClientTickPost(ClientTickPostEvent event) {
        if (mc.getRenderViewEntity() != null && VerusFly.mc.thePlayer != null) {
            EntityPlayerSP entity = VerusFly.mc.thePlayer;
            double orig = VerusFly.mc.getRenderViewEntity().posY;
            VerusFly.mc.getRenderViewEntity().posY = this.launchY;
            VerusFly.mc.thePlayer = entity;
            if (VerusFly.mc.theWorld.getBlockState(mc.getRenderViewEntity().getPosition()).getBlock().isBlockSolid(VerusFly.mc.theWorld, mc.getRenderViewEntity().getPosition(), EnumFacing.UP)) {
                VerusFly.mc.getRenderViewEntity().posY = orig;
            }
            if (VerusFly.mc.theWorld.getBlockState(mc.getRenderViewEntity().getPosition()).getBlock().isBlockSolid(VerusFly.mc.theWorld, new BlockPos(mc.getRenderViewEntity().getPosition().getX(), mc.getRenderViewEntity().getPosition().getY() + 1, mc.getRenderViewEntity().getPosition().getZ()), EnumFacing.UP)) {
                VerusFly.mc.getRenderViewEntity().posY = orig;
            }
            VerusFly.mc.getRenderViewEntity().fallDistance = 0.0f;
        }
    }

    @Subscribe
    public void onTick(MotionUpdateEvent event) {
        if (!event.isPre()) {
            return;
        }
        Minecraft mc = Minecraft.getMinecraft();
        if (!(mc.thePlayer.isInWeb || mc.thePlayer.isInLava() || mc.thePlayer.isInWater() || mc.thePlayer.isOnLadder() || mc.thePlayer.ridingEntity != null)) {
            if (mc.thePlayer.onGround) {
                if (Keyboard.isKeyDown(57)) {
                    this.launchY += 1.0;
                }
                if (Keyboard.isKeyDown(42)) {
                    this.launchY -= 1.0;
                }
                mc.thePlayer.jump();
            }
            if (MovementUtil.isMoving()) {
                mc.thePlayer.setSprinting(false);
                mc.gameSettings.keyBindJump.pressed = false;
                MovementUtil.strafe(0.3);
            } else {
                MovementUtil.strafe(0.0);
            }
        }
    }

    @Subscribe
    public void onSprint(UpdateSprintingEvent event) {
        event.setSprinting(false);
    }

    @Subscribe
    public void onBB(BlockBBEvent event) {
        if (event.getBlock() instanceof BlockAir && (double)event.getPos().getY() <= this.launchY) {
            event.setBoundingBox(AxisAlignedBB.fromBounds(event.getX(), event.getY(), event.getZ(), event.getX() + 1.0, this.launchY, event.getZ() + 1.0));
        }
    }
}

