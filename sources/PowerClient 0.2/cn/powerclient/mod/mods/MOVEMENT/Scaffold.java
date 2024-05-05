/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.AveReborn.mod.mods.MOVEMENT;

import com.darkmagician6.eventapi.EventTarget;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.Stream;
import me.AveReborn.Value;
import me.AveReborn.events.EventMove;
import me.AveReborn.events.EventPlayerUpdate;
import me.AveReborn.events.EventPreMotion;
import me.AveReborn.mod.Category;
import me.AveReborn.mod.Mod;
import me.AveReborn.util.PlayerUtil;
import me.AveReborn.util.timeUtils.TimerUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCactus;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockDynamicLiquid;
import net.minecraft.block.BlockFire;
import net.minecraft.block.BlockSand;
import net.minecraft.block.BlockStaticLiquid;
import net.minecraft.block.BlockTripWireHook;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Timer;
import net.minecraft.util.Vec3;

public class Scaffold
extends Mod {
    float[] lastKnownRots;
    private BlockData blockData = null;
    private List<Block> invalid = Arrays.asList(Blocks.air, Blocks.water, Blocks.fire, Blocks.flowing_water, Blocks.lava, Blocks.flowing_lava);
    private TimerUtil timerMotion = new TimerUtil();
    private TimerUtil timerr = new TimerUtil();
    private int slot;
    private List<Block> invalidPlayer = Arrays.asList(Blocks.air, Blocks.water, Blocks.fire, Blocks.flowing_water, Blocks.lava, Blocks.flowing_lava, Blocks.anvil, Blocks.chest, Blocks.ender_chest, Blocks.enchanting_table, Blocks.web, Blocks.torch, Blocks.redstone_lamp, Blocks.sand, Blocks.cactus, Blocks.ladder, Blocks.slime_block, Blocks.tripwire_hook, Blocks.dispenser);
    public Value<Boolean> swing = new Value<Boolean>("Scaffold_Swing", false);

    public Scaffold() {
        super("Scaffold", Category.MOVEMENT);
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        this.mc.timer.timerSpeed = 1.0f;
        super.onDisable();
    }

    @EventTarget
    public void onPreMotion(EventPlayerUpdate event) {
        if (this.blockData != null && this.slot != -1 && this.mc.inGameHasFocus) {
            boolean dohax;
            if (this.mc.gameSettings.keyBindJump.pressed) {
                if (Minecraft.thePlayer.moveForward != 0.0f || Minecraft.thePlayer.moveStrafing != 0.0f) {
                    return;
                }
                Minecraft.thePlayer.motionY = 0.42;
                if (this.timerMotion.hasTimeElapsed(1500.0, true)) {
                    Minecraft.thePlayer.motionY = -0.28;
                    this.timerMotion.reset();
                    if (this.timerMotion.hasTimeElapsed(2.0, false)) {
                        Minecraft.thePlayer.motionY = 0.42;
                    }
                }
            }
            this.mc.rightClickDelayTimer = 4;
            boolean slowPlace = true;
            boolean bl2 = dohax = Minecraft.thePlayer.inventory.currentItem != this.slot;
            if ((PlayerUtil.MovementInput() || this.mc.gameSettings.keyBindJump.pressed) && this.timerr.hasTimeElapsed(this.random(70.0, 100.0), true)) {
                if (dohax) {
                    Minecraft.thePlayer.setSneaking(true);
                }
                Minecraft.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(this.slot));
                if (this.swing.getValueState().booleanValue()) {
                    Minecraft.thePlayer.swingItem();
                } else {
                    Minecraft.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
                }
                this.mc.playerController.onPlayerRightClick(Minecraft.thePlayer, this.mc.theWorld, Minecraft.thePlayer.inventoryContainer.getSlot(36 + this.slot).getStack(), this.blockData.position, this.blockData.face, new Vec3(this.blockData.position.getX(), this.blockData.position.getY(), this.blockData.position.getZ()));
                Minecraft.thePlayer.setSneaking(false);
                if (dohax) {
                    Minecraft.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(Minecraft.thePlayer.inventory.currentItem));
                }
            }
        }
    }

    @EventTarget
    public void onPreMotion(EventPreMotion event) {
        int tempSlot = this.getBlockSlot();
        this.blockData = null;
        this.slot = -1;
        if (Minecraft.thePlayer.getHeldItem() != null && !Minecraft.thePlayer.isSneaking() && tempSlot != -1 && this.mc.inGameHasFocus) {
            this.mc.rightClickDelayTimer = 0;
            this.slot = tempSlot;
            BlockPos blockBelow1 = new BlockPos(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY - 1.0, Minecraft.thePlayer.posZ);
            if (this.mc.theWorld.getBlockState(blockBelow1).getBlock() == Blocks.air) {
                this.blockData = this.getBlockData(blockBelow1, 1.0);
            }
            event.pitch = 82.5f;
            if (this.blockData != null) {
                float[] rots = this.getAngleBlockpos(new BlockPos(this.blockData.position.getX(), this.blockData.position.getY(), this.blockData.position.getZ()));
                this.lastKnownRots = rots;
                if (this.mc.gameSettings.keyBindForward.pressed || this.mc.gameSettings.keyBindBack.pressed || this.mc.gameSettings.keyBindLeft.pressed || this.mc.gameSettings.keyBindRight.pressed || this.mc.gameSettings.keyBindJump.pressed) {
                    event.yaw = rots[0];
                    event.pitch = 82.5f;
                }
            } else if (this.lastKnownRots != null) {
                if (this.timerr.hasTimeElapsed(this.random(100.0, 200.0), true)) {
                    Minecraft.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
                }
                event.yaw = this.lastKnownRots[0];
                event.pitch = this.lastKnownRots[1];
            }
        }
        int n2 = 0;
        try {
            int k2 = 0;
            while (k2 < 9) {
                ItemStack stackInSlot3 = Minecraft.thePlayer.inventory.getStackInSlot(k2);
                if (stackInSlot3 != null && stackInSlot3.getItem() instanceof ItemBlock) {
                    n2 += stackInSlot3.stackSize;
                }
                ++k2;
            }
        }
        catch (Exception ex2) {
            ex2.printStackTrace();
        }
    }

    @EventTarget
    public void onMove(EventMove event) {
        double x2 = event.getX();
        double y2 = event.getY();
        double z2 = event.getZ();
        if (Minecraft.thePlayer.onGround) {
            double increment = 0.05;
            while (x2 != 0.0) {
                if (!this.mc.theWorld.getCollidingBoundingBoxes(Minecraft.thePlayer, Minecraft.thePlayer.getEntityBoundingBox().offset(x2, -1.0, 0.0)).isEmpty()) break;
                if (x2 < increment && x2 >= - increment) {
                    x2 = 0.0;
                    continue;
                }
                if (x2 > 0.0) {
                    x2 -= increment;
                    continue;
                }
                x2 += increment;
            }
            while (z2 != 0.0) {
                if (!this.mc.theWorld.getCollidingBoundingBoxes(Minecraft.thePlayer, Minecraft.thePlayer.getEntityBoundingBox().offset(0.0, -1.0, z2)).isEmpty()) break;
                if (z2 < increment && z2 >= - increment) {
                    z2 = 0.0;
                    continue;
                }
                if (z2 > 0.0) {
                    z2 -= increment;
                    continue;
                }
                z2 += increment;
            }
            while (x2 != 0.0 && z2 != 0.0 && this.mc.theWorld.getCollidingBoundingBoxes(Minecraft.thePlayer, Minecraft.thePlayer.getEntityBoundingBox().offset(x2, -1.0, z2)).isEmpty()) {
                x2 = x2 < increment && x2 >= - increment ? 0.0 : (x2 > 0.0 ? (x2 -= increment) : (x2 += increment));
                if (z2 < increment && z2 >= - increment) {
                    z2 = 0.0;
                    continue;
                }
                if (z2 > 0.0) {
                    z2 -= increment;
                    continue;
                }
                z2 += increment;
            }
        }
        event.setX(x2);
        event.setY(y2);
        event.setZ(z2);
    }

    public float[] getAngleBlockpos(BlockPos target) {
        double xDiff = (double)target.getX() - Minecraft.thePlayer.posX;
        double yDiff = (double)target.getY() - Minecraft.thePlayer.posY;
        double zDiff = (double)target.getZ() - Minecraft.thePlayer.posZ;
        float yaw = (float)(Math.atan2(zDiff, xDiff) * 180.0 / 3.141592653589793) - 90.0f;
        float pitch = (float)((- Math.atan2((double)target.getY() + -1.0 - (Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.getEyeHeight()), Math.hypot(xDiff, zDiff))) * 180.0 / 3.141592653589793);
        if (yDiff > -0.2 && yDiff < 0.2) {
            pitch = (float)((- Math.atan2((double)target.getY() + -1.0 - (Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.getEyeHeight()), Math.hypot(xDiff, zDiff))) * 180.0 / 3.141592653589793);
        } else if (yDiff > -0.2) {
            pitch = (float)((- Math.atan2((double)target.getY() + -1.0 - (Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.getEyeHeight()), Math.hypot(xDiff, zDiff))) * 180.0 / 3.141592653589793);
        } else if (yDiff < 0.3) {
            pitch = (float)((- Math.atan2((double)target.getY() + -1.0 - (Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.getEyeHeight()), Math.hypot(xDiff, zDiff))) * 180.0 / 3.141592653589793);
        }
        return new float[]{yaw, pitch};
    }

    public double random(double min, double max) {
        Random random = new Random();
        return min + random.nextDouble() * (max - min);
    }

    private int getBlockSlot() {
        int i2 = 36;
        while (i2 < 45) {
            ItemStack itemStack = Minecraft.thePlayer.inventoryContainer.getSlot(i2).getStack();
            if (itemStack != null && itemStack.getItem() instanceof ItemBlock && !this.contains(this.invalidPlayer, ((ItemBlock)itemStack.getItem()).getBlock())) {
                return i2 - 36;
            }
            ++i2;
        }
        return -1;
    }

    private boolean contains(List<Block> list, final Block block) {
        return list.stream().anyMatch(new Predicate<Block>(){

            @Override
            public boolean test(Block e2) {
                return e2.equals(block);
            }
        });
    }

    public BlockData getBlockData(BlockPos pos, double d2) {
        return this.mc.theWorld.getBlockState(pos.add(0.0, 0.0, d2)).getBlock() != Blocks.air ? new BlockData(pos.add(0.0, 0.0, d2), EnumFacing.NORTH) : (this.mc.theWorld.getBlockState(pos.add(0.0, 0.0, - d2)).getBlock() != Blocks.air ? new BlockData(pos.add(0.0, 0.0, - d2), EnumFacing.SOUTH) : (this.mc.theWorld.getBlockState(pos.add(d2, 0.0, 0.0)).getBlock() != Blocks.air ? new BlockData(pos.add(d2, 0.0, 0.0), EnumFacing.WEST) : (this.mc.theWorld.getBlockState(pos.add(- d2, 0.0, 0.0)).getBlock() != Blocks.air ? new BlockData(pos.add(- d2, 0.0, 0.0), EnumFacing.EAST) : (this.mc.theWorld.getBlockState(pos.add(0, -1, 0)).getBlock() != Blocks.air ? new BlockData(pos.add(0, -1, 0), EnumFacing.UP) : null))));
    }

    public Vec3 getBlockSide(BlockPos pos, EnumFacing face) {
        return face == EnumFacing.NORTH ? new Vec3(pos.getX(), pos.getY(), (double)pos.getZ() - 0.5) : (face == EnumFacing.EAST ? new Vec3((double)pos.getX() + 0.5, pos.getY(), pos.getZ()) : (face == EnumFacing.SOUTH ? new Vec3(pos.getX(), pos.getY(), (double)pos.getZ() + 0.5) : (face == EnumFacing.WEST ? new Vec3((double)pos.getX() - 0.5, pos.getY(), pos.getZ()) : new Vec3(pos.getX(), pos.getY(), pos.getZ()))));
    }

    public class BlockData {
        public BlockPos position;
        public EnumFacing face;

        public BlockData(BlockPos position, EnumFacing face) {
            this.position = position;
            this.face = face;
        }
    }

}

