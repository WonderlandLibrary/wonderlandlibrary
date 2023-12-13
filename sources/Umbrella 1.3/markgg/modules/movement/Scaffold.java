/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 */
package markgg.modules.movement;

import markgg.events.Event;
import markgg.events.listeners.EventMotion;
import markgg.modules.Module;
import markgg.settings.BooleanSetting;
import markgg.utilities.movement.MovementUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCarpet;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockSkull;
import net.minecraft.block.BlockSnow;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import org.lwjgl.input.Keyboard;

public class Scaffold
extends Module {
    int lastItem;
    float yaw;
    float pitch = 90.0f;
    public BooleanSetting tower = new BooleanSetting("Tower", true);

    public Scaffold() {
        super("Scaffold", 0, Module.Category.MOVEMENT);
        this.addSettings(this.tower);
    }

    private boolean canPlaceBlock(BlockPos pos) {
        Block block = this.mc.theWorld.getBlockState(pos).getBlock();
        return !this.mc.theWorld.isAirBlock(pos) && !(block instanceof BlockLiquid);
    }

    @Override
    public void onEnable() {
    }

    @Override
    public void onEvent(Event e) {
        if (e instanceof EventMotion) {
            double posY;
            double yDif;
            BlockData data;
            BlockPos playerBlock;
            EventMotion event = (EventMotion)e;
            if (e.isPost()) {
                if (this.tower.isEnabled() && Keyboard.isKeyDown((int)this.mc.gameSettings.keyBindJump.getKeyCode()) && !MovementUtil.isMoving()) {
                    this.mc.thePlayer.motionY = 0.2f;
                }
                playerBlock = new BlockPos(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ);
                data = null;
                yDif = 1.0;
                for (posY = this.mc.thePlayer.posY - 1.0; posY > 0.0; posY -= 1.0) {
                    BlockData newData = this.getBlockData(new BlockPos(this.mc.thePlayer.posX, posY, this.mc.thePlayer.posZ));
                    if (newData == null || !((yDif = this.mc.thePlayer.posY - posY) <= 3.0)) continue;
                    data = newData;
                    break;
                }
                if (data == null) {
                    return;
                }
                if (data.pos == new BlockPos(0, -1, 0)) {
                    this.mc.thePlayer.motionX = 0.0;
                    this.mc.thePlayer.motionZ = 0.0;
                }
                for (int i = 0; i < 9; ++i) {
                    ItemStack itemStack = this.mc.thePlayer.inventory.getStackInSlot(i);
                    if (itemStack == null) continue;
                    int stackSize = itemStack.stackSize;
                    if (!(itemStack.getItem() instanceof ItemBlock)) continue;
                    this.lastItem = this.mc.thePlayer.inventory.currentItem;
                    this.mc.thePlayer.inventory.currentItem = i;
                }
                if (this.mc.thePlayer.getHeldItem() != null && this.mc.thePlayer.getHeldItem().getItem() instanceof ItemBlock) {
                    this.mc.playerController.func_178890_a(this.mc.thePlayer, this.mc.theWorld, this.mc.thePlayer.getHeldItem(), data.pos, data.face, new Vec3(data.pos.getX(), data.pos.getY(), data.pos.getZ()));
                    this.mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
                }
                this.mc.thePlayer.inventory.currentItem = this.lastItem;
            }
            if (e.isPre()) {
                playerBlock = new BlockPos(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ);
                data = null;
                yDif = 1.0;
                for (posY = this.mc.thePlayer.posY - 1.0; posY > 0.0; posY -= 1.0) {
                    BlockData newData = this.getBlockData(new BlockPos(this.mc.thePlayer.posX, posY, this.mc.thePlayer.posZ));
                    if (newData == null || !((yDif = this.mc.thePlayer.posY - posY) <= 3.0)) continue;
                    data = newData;
                    break;
                }
                if (data == null) {
                    return;
                }
                if (data.pos == new BlockPos(0, -1, 0)) {
                    this.mc.thePlayer.motionX = 0.0;
                    this.mc.thePlayer.motionZ = 0.0;
                }
                this.yaw = data.face == EnumFacing.UP ? 90.0f : (data.face == EnumFacing.NORTH ? 360.0f : (data.face == EnumFacing.EAST ? 90.0f : (data.face == EnumFacing.SOUTH ? 180.0f : (data.face == EnumFacing.WEST ? 270.0f : 90.0f))));
                event.setYaw(this.yaw);
                event.setPitch(this.pitch);
            }
        }
    }

    private boolean isPosSolid(BlockPos pos) {
        Block block = this.mc.theWorld.getBlockState(pos).getBlock();
        return (block.getMaterial().isSolid() || !block.isTranslucent() || block.isSolidFullCube() || block instanceof BlockLadder || block instanceof BlockCarpet || block instanceof BlockSnow || block instanceof BlockSkull) && !block.getMaterial().isLiquid() && !(block instanceof BlockContainer);
    }

    private BlockData getBlockData(BlockPos pos) {
        if (this.mc.theWorld.getBlockState(pos.add(0, -1, 0)).getBlock() != Blocks.air) {
            return new BlockData(pos.add(0, -1, 0), EnumFacing.UP);
        }
        if (this.mc.theWorld.getBlockState(pos.add(-1, 0, 0)).getBlock() != Blocks.air) {
            return new BlockData(pos.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (this.mc.theWorld.getBlockState(pos.add(1, 0, 0)).getBlock() != Blocks.air) {
            return new BlockData(pos.add(1, 0, 0), EnumFacing.WEST);
        }
        if (this.mc.theWorld.getBlockState(pos.add(0, 0, -1)).getBlock() != Blocks.air) {
            return new BlockData(pos.add(0, 0, -1), EnumFacing.SOUTH);
        }
        if (this.mc.theWorld.getBlockState(pos.add(0, 0, 1)).getBlock() != Blocks.air) {
            return new BlockData(pos.add(0, 0, 1), EnumFacing.NORTH);
        }
        return null;
    }

    public class BlockData {
        public final BlockPos pos;
        public final EnumFacing face;

        BlockData(BlockPos pos, EnumFacing face) {
            this.pos = pos;
            this.face = face;
        }
    }
}

