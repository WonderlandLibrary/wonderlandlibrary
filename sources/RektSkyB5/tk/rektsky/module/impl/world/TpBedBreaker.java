/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.module.impl.world;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;
import org.greenrobot.eventbus.Subscribe;
import tk.rektsky.event.impl.PacketSentEvent;
import tk.rektsky.event.impl.WorldTickEvent;
import tk.rektsky.module.Category;
import tk.rektsky.module.Module;
import tk.rektsky.utils.PathUtils;

public class TpBedBreaker
extends Module {
    public EntityLivingBase target;
    public List<Vec3> path = new ArrayList<Vec3>();

    public TpBedBreaker() {
        super("TpBedBreaker", "Make you go brr with infinity bed breaking technology", Category.WORLD, false);
    }

    @Override
    public void onEnable() {
        this.path.clear();
        this.target = null;
    }

    @Override
    public void onDisable() {
    }

    @Subscribe
    public void onTick(WorldTickEvent event) {
        Vec3i pos = null;
        for (TileEntity tileEntity : this.mc.theWorld.loadedTileEntityList) {
            if (tileEntity.getBlockType() != Blocks.bed) continue;
            if (pos == null) {
                pos = tileEntity.getPos();
            }
            if (!(this.mc.thePlayer.getDistance(tileEntity.getPos().getX(), tileEntity.getPos().getY(), tileEntity.getPos().getZ()) < this.mc.thePlayer.getDistance(pos.getX(), pos.getY(), pos.getZ()))) continue;
            pos = tileEntity.getPos();
        }
        if (pos != null) {
            List<Vec3> blinkPath = PathUtils.findBlinkPath(pos.getX(), pos.getY(), pos.getZ());
            for (Vec3 vec3 : blinkPath) {
                this.mc.thePlayer.sendQueue.addToSendQueueSilent(new C03PacketPlayer.C04PacketPlayerPosition(vec3.xCoord, vec3.yCoord, vec3.zCoord, true));
            }
            this.mc.thePlayer.setPosition(pos.getX(), pos.getY(), pos.getZ());
        }
    }

    @Subscribe
    public void onPacket(PacketSentEvent event) {
    }
}

