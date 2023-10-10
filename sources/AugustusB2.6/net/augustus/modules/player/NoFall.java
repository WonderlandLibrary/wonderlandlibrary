// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.modules.player;

import net.minecraft.item.ItemStack;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.util.MovingObjectPosition;
import net.augustus.utils.RandomUtil;
import net.augustus.events.EventUpdate;
import net.minecraft.item.Item;
import net.augustus.events.EventEarlyTick;
import net.augustus.events.EventTick;
import net.lenni0451.eventapi.reflection.EventTarget;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.augustus.events.EventSendPacket;
import net.augustus.modules.Categorys;
import java.awt.Color;
import net.augustus.utils.RotationUtil;
import net.minecraft.util.BlockPos;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.StringValue;
import net.augustus.utils.TimeHelper;
import net.augustus.modules.Module;

public class NoFall extends Module
{
    private final TimeHelper timeHelper;
    private final TimeHelper timeHelper2;
    public StringValue mode;
    public DoubleValue fallDistance;
    public DoubleValue legitFallDistance;
    public DoubleValue lookRange;
    public DoubleValue yawSpeed;
    public DoubleValue pitchSpeed;
    public DoubleValue delay;
    private boolean rotated;
    private boolean clickTimer;
    private BlockPos b;
    private float[] rots;
    private float[] lastRots;
    private RotationUtil rotationUtil;
    private int slotID;
    private BlockPos waterPos;
    private boolean matrixOnGround;
    
    public NoFall() {
        super("NoFall", new Color(222, 80, 95, 255), Categorys.PLAYER);
        this.timeHelper = new TimeHelper();
        this.timeHelper2 = new TimeHelper();
        this.mode = new StringValue(1, "Mode", this, "OnGround", new String[] { "OnGround", "NoGround", "Stop", "Legit" });
        this.fallDistance = new DoubleValue(2, "MinFallDist", this, 2.0, 1.0, 24.0, 0);
        this.legitFallDistance = new DoubleValue(3, "MinFallDist", this, 6.0, 2.0, 24.0, 1);
        this.lookRange = new DoubleValue(7, "AimRange", this, 8.0, 5.0, 30.0, 0);
        this.yawSpeed = new DoubleValue(5, "YawSpeed", this, 180.0, 0.0, 180.0, 0);
        this.pitchSpeed = new DoubleValue(6, "PitchSpeed", this, 180.0, 0.0, 180.0, 0);
        this.delay = new DoubleValue(8, "Delay", this, 110.0, 0.0, 2000.0, 0);
        this.rots = new float[2];
        this.lastRots = new float[2];
        this.rotationUtil = new RotationUtil();
        this.slotID = 0;
    }
    
    @Override
    public void onDisable() {
    }
    
    @Override
    public void onEnable() {
        this.rotationUtil = new RotationUtil();
        if (NoFall.mc.thePlayer == null) {
            return;
        }
        this.slotID = NoFall.mc.thePlayer.inventory.currentItem;
        this.setDisplayName(super.getName() + " §8" + this.mode.getSelected());
    }
    
    @EventTarget
    public void onEventSendPacket(final EventSendPacket eventSendPacket) {
        final Packet packet = eventSendPacket.getPacket();
        final String selected = this.mode.getSelected();
        switch (selected) {
            case "NoGround": {
                if (packet instanceof C03PacketPlayer) {
                    final C03PacketPlayer c03PacketPlayer = (C03PacketPlayer)packet;
                    c03PacketPlayer.setOnGround(false);
                    break;
                }
                break;
            }
        }
    }
    
    @EventTarget
    public void onEventTick(final EventTick eventTick) {
        if (NoFall.mc.thePlayer == null) {
            return;
        }
        final String selected = this.mode.getSelected();
        switch (selected) {
            case "OnGround": {
                if (NoFall.mc.thePlayer.fallDistance > this.fallDistance.getValue()) {
                    NoFall.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
                    break;
                }
                break;
            }
            case "Stop": {
                if (NoFall.mc.thePlayer.fallDistance > 2.0f) {
                    NoFall.mc.thePlayer.motionY = 0.0;
                    NoFall.mc.thePlayer.onGround = true;
                    NoFall.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
                    NoFall.mc.thePlayer.fallDistance = 0.0f;
                    break;
                }
                break;
            }
        }
    }
    
    @EventTarget
    public void onEventEarlyTick(final EventEarlyTick eventEarlyTick) {
        this.setDisplayName(super.getName() + " §8" + this.mode.getSelected());
        if (!this.mode.getSelected().equals("Legit")) {
            return;
        }
        if (NoFall.mc.thePlayer == null) {
            return;
        }
        final int bestItem = this.getItem();
        if (bestItem == -1) {
            return;
        }
        if (NoFall.mc.thePlayer.inventoryContainer.getSlot(bestItem + 36).getStack().getItem().equals(Item.getItemById(325)) && NoFall.mc.thePlayer.isInWater()) {
            if (this.waterPos == null) {
                this.waterPos = this.getWaterPos();
            }
            this.b = this.waterPos;
        }
        else {
            if (NoFall.mc.thePlayer.fallDistance < this.legitFallDistance.getValue()) {
                this.rotated = false;
                return;
            }
            this.b = this.getBlockPos();
        }
        if (this.b == null) {
            return;
        }
        this.rots = this.rotationUtil.scaffoldRots(this.b.getX() + 0.5, this.b.getY() + 1, this.b.getZ() + 0.5, this.lastRots[0], this.lastRots[1], (float)this.yawSpeed.getValue(), (float)this.pitchSpeed.getValue(), true);
        if (!NoFall.mc.thePlayer.inventoryContainer.getSlot(bestItem + 36).getStack().getItem().equals(Item.getItemById(325)) || !NoFall.mc.thePlayer.isInWater()) {
            this.setPitch();
        }
        else {
            this.setRotation();
        }
        this.lastRots = this.rots;
    }
    
    @EventTarget
    public void onEventClick(final EventUpdate eventClick) {
        if (!this.mode.getSelected().equals("Legit") || this.b == null || !this.rotated) {
            return;
        }
        final int currentItem = NoFall.mc.thePlayer.inventory.currentItem;
        final int bestItem = this.getItem();
        if (bestItem != this.slotID) {
            this.slotID = bestItem;
        }
        if (this.slotID == -1) {
            return;
        }
        if (this.clickTimer) {
            if (NoFall.mc.thePlayer.inventoryContainer.getSlot(this.slotID + 36).getStack().getItem().equals(Item.getItemById(325)) && NoFall.mc.thePlayer.isInWater()) {
                if (this.timeHelper.reached((long)this.delay.getValue() + RandomUtil.nextLong(-20L, 20L)) && this.timeHelper2.reached(360L + RandomUtil.nextLong(0L, 50L))) {
                    NoFall.mc.thePlayer.inventory.currentItem = this.slotID;
                    NoFall.mc.rightClickMouse();
                    NoFall.mc.thePlayer.inventory.currentItem = currentItem;
                    this.clickTimer = false;
                    this.timeHelper.reset();
                    this.timeHelper2.reset();
                }
            }
            else if (NoFall.mc.thePlayer.fallDistance >= this.legitFallDistance.getValue()) {
                if (NoFall.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && NoFall.mc.objectMouseOver.getBlockPos().equals(this.b) && !NoFall.mc.thePlayer.inventoryContainer.getSlot(this.slotID + 36).getStack().getItem().equals(Item.getItemById(325)) && this.timeHelper.reached((long)this.delay.getValue() + RandomUtil.nextLong(-20L, 20L))) {
                    if (NoFall.mc.thePlayer.inventoryContainer.getSlot(this.slotID + 36).getStack().getItem().equals(Item.getItemById(326))) {
                        this.waterPos = new BlockPos(this.b.getX(), this.b.getY() + 1, this.b.getZ());
                    }
                    NoFall.mc.thePlayer.inventory.currentItem = this.slotID;
                    NoFall.mc.rightClickMouse();
                    NoFall.mc.thePlayer.inventory.currentItem = currentItem;
                    this.clickTimer = false;
                    this.timeHelper.reset();
                    this.timeHelper2.reset();
                }
            }
        }
        else {
            this.clickTimer = true;
        }
    }
    
    private BlockPos getBlockPos() {
        final BlockPos b = new BlockPos(NoFall.mc.thePlayer.posX + NoFall.mc.thePlayer.motionX * 2.0, NoFall.mc.thePlayer.posY, NoFall.mc.thePlayer.posZ + NoFall.mc.thePlayer.motionZ * 2.0);
        for (int y = b.getY() - 1; y > b.getY() - this.lookRange.getValue() + 1.0; --y) {
            if (this.isValidBlock(new BlockPos(b.getX(), y, b.getZ()))) {
                return new BlockPos(b.getX(), y, b.getZ());
            }
        }
        return null;
    }
    
    private BlockPos getWaterPos() {
        final BlockPos b = new BlockPos(NoFall.mc.thePlayer.posX, NoFall.mc.thePlayer.posY, NoFall.mc.thePlayer.posZ);
        for (int y = b.getY() + 1; y > b.getY() - 5; --y) {
            if (this.isValidBlock(new BlockPos(b.getX(), y, b.getZ()))) {
                return new BlockPos(b.getX(), y, b.getZ());
            }
        }
        return null;
    }
    
    private boolean isValidBlock(final BlockPos blockPos) {
        final Block block = NoFall.mc.theWorld.getBlockState(blockPos).getBlock();
        return !(block instanceof BlockLiquid) && !(block instanceof BlockAir);
    }
    
    private int getItem() {
        for (int i = 36; i < NoFall.mc.thePlayer.inventoryContainer.inventorySlots.size(); ++i) {
            final ItemStack itemStack = NoFall.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (itemStack != null && (((itemStack.getItem() == Item.getItemById(30) || itemStack.getItem() == Item.getItemById(326)) && !NoFall.mc.thePlayer.isInWater()) || (itemStack.getItem() == Item.getItemById(325) && NoFall.mc.thePlayer.fallDistance < 1.0f && NoFall.mc.thePlayer.isInWater()))) {
                return i - 36;
            }
        }
        return -1;
    }
    
    private void setRotation() {
        if (NoFall.mc.currentScreen != null || (NoFall.mm.killAura.isToggled() && NoFall.mm.killAura.target != null)) {
            return;
        }
        NoFall.mc.thePlayer.rotationYaw = this.rots[0];
        NoFall.mc.thePlayer.rotationPitch = this.rots[1];
        NoFall.mc.thePlayer.prevRotationYaw = this.lastRots[0];
        NoFall.mc.thePlayer.prevRotationPitch = this.lastRots[1];
        this.rotated = true;
    }
    
    private void setPitch() {
        if (NoFall.mc.currentScreen != null || (NoFall.mm.killAura.isToggled() && NoFall.mm.killAura.target != null)) {
            return;
        }
        NoFall.mc.thePlayer.rotationPitch = this.rots[1];
        NoFall.mc.thePlayer.prevRotationPitch = this.lastRots[1];
        this.rotated = true;
    }
}
