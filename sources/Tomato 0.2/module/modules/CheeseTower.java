/*
 * Decompiled with CFR 0_115.
 */
package cow.milkgod.cheese.module.modules;

import com.darkmagician6.eventapi.EventTarget;
import cow.milkgod.cheese.events.EventPacketSent;
import cow.milkgod.cheese.events.EventPreMotionUpdates;
import cow.milkgod.cheese.module.Category;
import cow.milkgod.cheese.module.Module;
import cow.milkgod.cheese.properties.Property;
import cow.milkgod.cheese.utils.Timer;
import cow.milkgod.cheese.utils.Wrapper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;

public class CheeseTower
extends Module {
    private float[] rotations;
    private final Property slowmode;
    private static Timer time = new Timer();

    public CheeseTower() {
        super("Tower", 0, Category.PLAYER, -2120734829, true, "Uruguay", new String[]{"cheeset"});
        this.slowmode = new Property<Boolean>(this, "tower_slowmode", false);
    }

    @EventTarget
    public void onPre(EventPreMotionUpdates event) {
        BlockPos sent = new BlockPos(Wrapper.mc.thePlayer.posX, Wrapper.mc.thePlayer.posY - 1.0, Wrapper.mc.thePlayer.posZ);
        EnumFacing player = this.getFacingDirection(sent);
        try {
            if (time.hasTimeElapsed((Boolean)this.slowmode.getValue() != false ? 150 : 75, false) && Wrapper.mc.thePlayer.getCurrentEquippedItem().getItem() != null && Wrapper.mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBlock) {
                Wrapper.mc.thePlayer.setPosition(Wrapper.mc.thePlayer.posX, Wrapper.mc.thePlayer.posY + 1.1, Wrapper.mc.thePlayer.posZ);
                this.rotations = Wrapper.getBlockRotations(Wrapper.mc.thePlayer.posX, Wrapper.mc.thePlayer.posY - 1.0, Wrapper.mc.thePlayer.posZ);
                event.setYaw(this.rotations[0]);
                event.setPitch(this.rotations[1]);
                if (!Wrapper.mc.thePlayer.onGround) {
                    Wrapper.mc.playerController.func_178890_a(Wrapper.mc.thePlayer, Wrapper.mc.theWorld, Wrapper.mc.thePlayer.getCurrentEquippedItem(), sent, player, new Vec3(Wrapper.mc.thePlayer.posX, Wrapper.mc.thePlayer.posY - 1.0, Wrapper.mc.thePlayer.posZ));
                    Wrapper.mc.thePlayer.swingItem();
                }
                time.reset();
            }
        }
        catch (Exception var4_4) {
            // empty catch block
        }
    }

    public void onSent(EventPacketSent sent1) {
        if (sent1.getPacket() instanceof C03PacketPlayer) {
            C03PacketPlayer player1 = (C03PacketPlayer)sent1.getPacket();
            this.rotations = Wrapper.getBlockRotations(Wrapper.mc.thePlayer.posX, Wrapper.mc.thePlayer.posY - 1.0, Wrapper.mc.thePlayer.posZ);
            player1.yaw = this.rotations[0];
            player1.pitch = this.rotations[1];
        }
    }

    private EnumFacing getFacingDirection(BlockPos pos) {
        EnumFacing direction = null;
        if (!Wrapper.mc.theWorld.getBlockState(pos.add(0, 1, 0)).getBlock().isSolidFullCube()) {
            direction = EnumFacing.UP;
        } else if (!Wrapper.mc.theWorld.getBlockState(pos.add(0, -1, 0)).getBlock().isSolidFullCube()) {
            direction = EnumFacing.DOWN;
        } else if (!Wrapper.mc.theWorld.getBlockState(pos.add(1, 0, 0)).getBlock().isSolidFullCube()) {
            direction = EnumFacing.EAST;
        } else if (!Wrapper.mc.theWorld.getBlockState(pos.add(-1, 0, 0)).getBlock().isSolidFullCube()) {
            direction = EnumFacing.WEST;
        } else if (!Wrapper.mc.theWorld.getBlockState(pos.add(0, 0, 1)).getBlock().isSolidFullCube()) {
            direction = EnumFacing.SOUTH;
        } else if (!Wrapper.mc.theWorld.getBlockState(pos.add(0, 0, 1)).getBlock().isSolidFullCube()) {
            direction = EnumFacing.NORTH;
        }
        return direction;
    }
}

