/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.AveReborn.mod.mods.MOVEMENT;

import com.darkmagician6.eventapi.EventTarget;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import me.AveReborn.Value;
import me.AveReborn.events.EventMove;
import me.AveReborn.events.EventPostMotion;
import me.AveReborn.events.EventPreMotion;
import me.AveReborn.events.EventSafeWalk;
import me.AveReborn.mod.Category;
import me.AveReborn.mod.Mod;
import me.AveReborn.util.CombatUtil;
import me.AveReborn.util.PlayerUtil;
import me.AveReborn.util.timeUtils.TimeHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
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
import net.minecraft.util.Vec3i;
import net.minecraft.world.World;

public class Scaffold2
extends Mod {
    private int slot;
    private BlockData blockData;
    private TimeHelper time = new TimeHelper();
    private TimeHelper delay = new TimeHelper();
    private Value<Boolean> hypixelfast = new Value<Boolean>("Scaffold2_HypixelFast", false);
    private Value noSwing = new Value<Boolean>("Scaffold2_NoSwing", false);
    private Value silent = new Value<Boolean>("Scaffold2_Silent", true);
    private Value Sprint = new Value<Boolean>("Scaffold2_Sprint", true);
    private Value sneak = new Value<Boolean>("Scaffold2_AutoSneak", true);
    private Value delayValue = new Value<Double>("Scaffold2_Delay", 250.0, 40.0, 1000.0, 10.0);
    private Value<Boolean> tower = new Value<Boolean>("Scaffold2_Tower", false);
    public static Value mode = new Value("Scaffold2", "Mode", 0);
    public static boolean issafe = false;
    private int fastint;
    private TimeHelper timer2 = new TimeHelper();
    private boolean adtap;

    public Scaffold2() {
        super("Scaffold2", Category.MOVEMENT);
        Scaffold2.mode.mode.add("Normal");
        Scaffold2.mode.mode.add("AAC");
        Scaffold2.mode.mode.add("Hypixel");
    }

    @EventTarget
    public void onPre(EventPreMotion event) {
        if (this.tower.getValueState().booleanValue() && Minecraft.getMinecraft().gameSettings.keyBindJump.pressed) {
            double blockBelow = -2.0;
            if (Minecraft.thePlayer.onGround) {
                Minecraft.thePlayer.motionY = 0.41;
            }
            if (Minecraft.thePlayer.motionY < 0.1 && !(this.mc.theWorld.getBlockState(new BlockPos(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY, Minecraft.thePlayer.posZ).add(0.0, -2.0, 0.0)).getBlock() instanceof BlockAir)) {
                Minecraft.thePlayer.motionY = -2.0;
            }
        }
        this.showValue = mode;
        if (Minecraft.thePlayer != null) {
            this.blockData = this.getBlockData(new BlockPos(Minecraft.thePlayer).add(0.0, -0.35, 0.0), 1);
            int block = this.getBlockItem();
            Item item = Minecraft.thePlayer.inventory.getStackInSlot(block).getItem();
            if (block != -1 && item != null && item instanceof ItemBlock) {
                if (((Boolean)this.silent.getValueState()).booleanValue()) {
                    Minecraft.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(block));
                }
                if (!((Boolean)this.Sprint.getValueState()).booleanValue()) {
                    Minecraft.thePlayer.setSprinting(false);
                }
                if ((mode.isCurrentMode("AAC") || mode.isCurrentMode("Hypixel")) && mode.isCurrentMode("Hypixel")) {
                    float random = new Random().nextFloat() * 3.0f;
                    if (this.mc.gameSettings.keyBindForward.pressed) {
                        event.yaw = Minecraft.thePlayer.rotationYaw >= 180.0f + random ? Minecraft.thePlayer.rotationYaw - 180.0f + random : Minecraft.thePlayer.rotationYaw + 180.0f + random;
                    } else if (this.mc.gameSettings.keyBindBack.pressed) {
                        event.yaw = Minecraft.thePlayer.rotationYaw + random;
                    } else if (this.mc.gameSettings.keyBindLeft.pressed) {
                        event.yaw = Minecraft.thePlayer.rotationYaw + 90.0f + random;
                    } else if (this.mc.gameSettings.keyBindRight.pressed) {
                        event.yaw = Minecraft.thePlayer.rotationYaw - 90.0f + random;
                    }
                    PlayerUtil.setSpeed(PlayerUtil.getSpeed() * 0.9);
                    this.mc.timer.timerSpeed = 1.0f;
                    event.pitch = 81.0f + new Random().nextFloat() * 4.0f;
                    if (this.mc.gameSettings.keyBindJump.pressed) {
                        event.pitch = 88.0f + new Random().nextFloat();
                    }
                    this.mc.playerController.onPlayerRightClick(Minecraft.thePlayer, this.mc.theWorld, Minecraft.thePlayer.inventoryContainer.getSlot(36 + this.slot).getStack(), this.blockData.position, this.blockData.face, new Vec3(this.blockData.position.getX(), this.blockData.position.getY(), this.blockData.position.getZ()));
                }
            }
            if (this.blockData != null && block != -1 && item != null && item instanceof ItemBlock) {
                Vec3 pos = this.getBlockSide(this.blockData.position, this.blockData.face);
                float[] rot = CombatUtil.getRotationsNeededBlock(pos.xCoord, pos.yCoord, pos.zCoord);
                float[] arrf = CombatUtil.getDirectionToBlock(pos.xCoord, pos.yCoord, pos.zCoord, this.blockData.face);
            }
        }
    }

    @Override
    public void portMove(float yaw, float multiplyer, float up2) {
        double moveX = (- Math.sin(Math.toRadians(yaw))) * (double)multiplyer;
        double moveZ = Math.cos(Math.toRadians(yaw)) * (double)multiplyer;
        double moveY = up2;
        Minecraft.thePlayer.setPosition(moveX + Minecraft.thePlayer.posX, moveY + Minecraft.thePlayer.posY, moveZ + Minecraft.thePlayer.posZ);
    }

    private double getDoubleRandom(double min, double max) {
        return ThreadLocalRandom.current().nextDouble(min, max);
    }

    @EventTarget
    public void onPost(EventPostMotion event) {
    }

    public Block getBlock(BlockPos pos) {
        return this.mc.theWorld.getBlockState(pos).getBlock();
    }

    public Block getBlockUnderPlayer(EntityPlayer player) {
        return this.getBlock(new BlockPos(player.posX, player.posY - 1.0, player.posZ));
    }

    @EventTarget
    public void safeWalk(EventMove event) {
        double x2 = event.getX();
        double y2 = event.getY();
        double z2 = event.getZ();
        if (Minecraft.thePlayer.onGround) {
            double increment = 0.05;
            while (x2 != 0.0) {
                if (!this.mc.theWorld.getCollidingBoundingBoxes(Minecraft.thePlayer, Minecraft.thePlayer.getEntityBoundingBox().offset(x2, -1.0, 0.0)).isEmpty()) break;
                if (x2 < 0.05 && x2 >= -0.05) {
                    x2 = 0.0;
                    continue;
                }
                if (x2 > 0.0) {
                    x2 -= 0.05;
                    continue;
                }
                x2 += 0.05;
            }
            while (z2 != 0.0) {
                if (!this.mc.theWorld.getCollidingBoundingBoxes(Minecraft.thePlayer, Minecraft.thePlayer.getEntityBoundingBox().offset(0.0, -1.0, z2)).isEmpty()) break;
                if (z2 < 0.05 && z2 >= -0.05) {
                    z2 = 0.0;
                    continue;
                }
                if (z2 > 0.0) {
                    z2 -= 0.05;
                    continue;
                }
                z2 += 0.05;
            }
            while (x2 != 0.0 && z2 != 0.0 && this.mc.theWorld.getCollidingBoundingBoxes(Minecraft.thePlayer, Minecraft.thePlayer.getEntityBoundingBox().offset(x2, -1.0, z2)).isEmpty()) {
                x2 = x2 < 0.05 && x2 >= -0.05 ? 0.0 : (x2 > 0.0 ? (x2 -= 0.05) : (x2 += 0.05));
                if (z2 < 0.05 && z2 >= -0.05) {
                    z2 = 0.0;
                    continue;
                }
                if (z2 > 0.0) {
                    z2 -= 0.05;
                    continue;
                }
                z2 += 0.05;
            }
        }
        event.setX(x2);
        event.setY(y2);
        event.setZ(z2);
    }

    @EventTarget
    public void onSafe(EventSafeWalk event) {
        if (this.timer2.isDelayComplete(this.fastint)) {
            if (this.fastint == 1000) {
                this.mc.timer.timerSpeed = 1.0f;
                this.fastint = 220;
            } else {
                this.mc.timer.timerSpeed = 1.2455555f;
                this.fastint = 1000;
            }
            this.timer2.reset();
        }
        if (this.hypixelfast.getValueState().booleanValue()) {
            this.setDisplayName("HypixelFast:" + this.fastint);
        } else {
            this.setDisplayName(null);
        }
        if (((Boolean)this.sneak.getValueState()).booleanValue()) {
            if (this.getBlockUnderPlayer(Minecraft.thePlayer) instanceof BlockAir) {
                if (Minecraft.thePlayer.onGround) {
                    KeyBinding.setKeyBindState(this.mc.gameSettings.keyBindSneak.getKeyCode(), true);
                }
            } else if (Minecraft.thePlayer.onGround) {
                KeyBinding.setKeyBindState(this.mc.gameSettings.keyBindSneak.getKeyCode(), false);
            }
        }
        if (Minecraft.thePlayer != null && this.blockData != null) {
            int block = this.getBlockItem();
            Random rand = new Random();
            Item item = Minecraft.thePlayer.inventory.getStackInSlot(block).getItem();
            if (block != -1 && item != null && item instanceof ItemBlock) {
                Vec3 hitVec = new Vec3(this.blockData.position).addVector(0.2, 0.3, 0.2).add(new Vec3(this.blockData.face.getDirectionVec()).scale(0.0));
                if ((!mode.isCurrentMode("AAC") && !mode.isCurrentMode("Hypixel") || this.delay.isDelayComplete(mode.isCurrentMode("Hypixel") ? 0 + rand.nextInt(20) : ((Double)this.delayValue.getValueState()).intValue())) && this.mc.playerController.onPlayerRightClick(Minecraft.thePlayer, this.mc.theWorld, Minecraft.thePlayer.inventory.getStackInSlot(block), this.blockData.position.add(new Random().nextFloat() * 0.1f, 0.0, new Random().nextFloat() * 0.1f), this.blockData.face, hitVec)) {
                    this.delay.reset();
                    if (this.hypixelfast.getValueState().booleanValue()) {
                        if (this.mc.gameSettings.keyBindForward.pressed && this.fastint != 220) {
                            PlayerUtil.setSpeed(0.42);
                        } else {
                            PlayerUtil.setSpeed(0.0);
                        }
                    }
                    this.blockData = null;
                    this.time.reset();
                    if (((Boolean)this.noSwing.getValueState()).booleanValue()) {
                        Minecraft.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
                    } else {
                        Minecraft.thePlayer.swingItem();
                    }
                }
            }
            this.delay.reset();
        }
    }

    private boolean canPlace(EntityPlayerSP player, WorldClient worldIn, ItemStack heldStack, BlockPos hitPos, EnumFacing side, Vec3 vec3) {
        return heldStack.getItem() instanceof ItemBlock ? ((ItemBlock)heldStack.getItem()).canPlaceBlockOnSide(worldIn, hitPos, side, player, heldStack) : false;
    }

    private void sendCurrentItem() {
        Minecraft.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(Minecraft.thePlayer.inventory.currentItem));
    }

    private int getBlockItem() {
        int block = -1;
        int i2 = 8;
        while (i2 >= 0) {
            int itemid;
            if (Minecraft.thePlayer.inventory.getStackInSlot(i2) != null && Minecraft.thePlayer.inventory.getStackInSlot(i2).getItem() instanceof ItemBlock && (Minecraft.thePlayer.getHeldItem() == Minecraft.thePlayer.inventory.getStackInSlot(i2) || ((Boolean)this.silent.getValueState()).booleanValue()) && (itemid = Item.getIdFromItem(Minecraft.thePlayer.inventory.getStackInSlot(i2).getItem())) != 146 && itemid != 50 && itemid != 145 && itemid != 130 && itemid != 146 && itemid != 116) {
                block = i2;
            }
            --i2;
        }
        return block;
    }

    public BlockData getBlockData(BlockPos pos, int i2) {
        return this.mc.theWorld.getBlockState(pos.add(0, 0, i2)).getBlock() != Blocks.air ? new BlockData(this, pos.add(0, 0, i2), EnumFacing.NORTH) : (this.mc.theWorld.getBlockState(pos.add(0, 0, - i2)).getBlock() != Blocks.air ? new BlockData(this, pos.add(0, 0, - i2), EnumFacing.SOUTH) : (this.mc.theWorld.getBlockState(pos.add(i2, 0, 0)).getBlock() != Blocks.air ? new BlockData(this, pos.add(i2, 0, 0), EnumFacing.WEST) : (this.mc.theWorld.getBlockState(pos.add(- i2, 0, 0)).getBlock() != Blocks.air ? new BlockData(this, pos.add(- i2, 0, 0), EnumFacing.EAST) : (this.mc.theWorld.getBlockState(pos.add(0, - i2, 0)).getBlock() != Blocks.air ? new BlockData(this, pos.add(0, - i2, 0), EnumFacing.UP) : null))));
    }

    public Vec3 getBlockSide(BlockPos pos, EnumFacing face) {
        return face == EnumFacing.NORTH ? new Vec3(pos.getX(), pos.getY(), (double)pos.getZ() - 0.5) : (face == EnumFacing.EAST ? new Vec3((double)pos.getX() + 0.5, pos.getY(), pos.getZ()) : (face == EnumFacing.SOUTH ? new Vec3(pos.getX(), pos.getY(), (double)pos.getZ() + 0.5) : (face == EnumFacing.WEST ? new Vec3((double)pos.getX() - 0.5, pos.getY(), pos.getZ()) : new Vec3(pos.getX(), pos.getY(), pos.getZ()))));
    }

    @Override
    public void onEnable() {
        this.fastint = 300;
        this.timer2.reset();
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        this.sendCurrentItem();
        this.mc.gameSettings.keyBindSneak.pressed = false;
        this.mc.timer.timerSpeed = 1.0f;
        this.timer2.reset();
        this.fastint = 1000;
    }

    public class BlockData {
        public BlockPos position;
        public EnumFacing face;
        final Scaffold2 this$0;

        public BlockData(Scaffold2 var1, BlockPos position, EnumFacing face) {
            this.this$0 = var1;
            this.position = position;
            this.face = face;
        }
    }

}

