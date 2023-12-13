/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.module.impl.player;

import java.util.ArrayList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;
import org.greenrobot.eventbus.Subscribe;
import tk.rektsky.event.impl.WorldTickEvent;
import tk.rektsky.module.Category;
import tk.rektsky.module.Module;

public class TPChestAura
extends Module {
    public static ArrayList<BlockPos> position = new ArrayList();

    public TPChestAura() {
        super("TPChestAura", "TPAura, but for chests", Category.PLAYER, false);
    }

    @Override
    public void onEnable() {
        Vec3i pos = null;
        for (TileEntity tileEntity : this.mc.theWorld.loadedTileEntityList) {
            if (!(tileEntity instanceof TileEntityChest) || position.contains(tileEntity.getPos()) || pos != null && !(this.mc.thePlayer.getDistanceSqToCenter((BlockPos)pos) > this.mc.thePlayer.getDistanceSqToCenter(tileEntity.getPos()))) continue;
            pos = tileEntity.getPos();
        }
        if (pos != null && this.mc.thePlayer.serverSideTeleport(pos.getX(), pos.getY(), pos.getZ(), true, false)) {
            this.mc.playerController.onPlayerRightClick(this.mc.thePlayer, this.mc.theWorld, this.mc.thePlayer.getHeldItem(), (BlockPos)pos, EnumFacing.DOWN, new Vec3(pos.getX(), pos.getY(), pos.getZ()));
            position.add((BlockPos)pos);
        }
    }

    @Subscribe
    public void onTick(WorldTickEvent event) {
        this.setToggled(false);
    }
}

