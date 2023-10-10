/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.module.impl.player;

import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import org.greenrobot.eventbus.Subscribe;
import tk.rektsky.Client;
import tk.rektsky.event.impl.WorldTickEvent;
import tk.rektsky.module.Category;
import tk.rektsky.module.Module;
import tk.rektsky.module.impl.render.Notification;
import tk.rektsky.utils.display.ColorUtil;

public class Teleport
extends Module {
    public Teleport() {
        super("Teleport", "Teleport you to the position you are looking at", Category.PLAYER);
    }

    @Override
    public void onEnable() {
        Vec3 eyeEight = this.mc.thePlayer.getPositionEyes(this.mc.timer.renderPartialTicks);
        Vec3 lookVec = this.mc.thePlayer.getLook(this.mc.timer.renderPartialTicks);
        Vec3 vec = eyeEight.addVector(lookVec.xCoord * 70.0, lookVec.yCoord * 70.0, lookVec.zCoord * 70.0);
        MovingObjectPosition movingObjectPosition = this.mc.thePlayer.worldObj.rayTraceBlocks(eyeEight, vec, false, false, true);
        BlockPos blockPos = movingObjectPosition.getBlockPos();
        if (this.mc.thePlayer.serverSideTeleport((double)blockPos.getX() + 0.5, (double)blockPos.getY() + 0.5, (double)blockPos.getZ() + 0.5, true, false)) {
            this.mc.thePlayer.setPosition((double)blockPos.getX() + 0.5, (double)blockPos.getY() + 0.5, (double)blockPos.getZ() + 0.5);
        } else {
            Client.notify(new Notification.PopupMessage("Teleport", "Failed to teleport, because you are about to be vanilla kicked", ColorUtil.NotificationColors.RED, 20));
        }
    }

    @Subscribe
    public void onTick(WorldTickEvent event) {
        this.setToggled(false);
    }
}

