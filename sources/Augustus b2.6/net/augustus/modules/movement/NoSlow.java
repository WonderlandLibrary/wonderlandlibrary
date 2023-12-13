// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.modules.movement;

import net.minecraft.network.play.client.C16PacketClientStatus;
import net.augustus.events.EventClick;
import net.augustus.events.EventUpdate;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.augustus.events.EventPostMotion;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.Packet;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.augustus.events.EventPreMotion;
import net.lenni0451.eventapi.reflection.EventTarget;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemSword;
import net.augustus.events.EventNoSlow;
import net.augustus.settings.Setting;
import net.augustus.modules.Categorys;
import java.awt.Color;
import net.minecraft.item.ItemStack;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.BooleansSetting;
import net.augustus.settings.BooleanValue;
import net.augustus.utils.TimeHelper;
import net.augustus.modules.Module;

public class NoSlow extends Module
{
    private final TimeHelper timeHelper;
    public BooleanValue startSlow;
    public BooleanValue swordSlowdown;
    public BooleanValue swordSprint;
    public BooleanValue swordSwitch;
    public BooleanValue swordToggle;
    public BooleanValue swordBug;
    public BooleanValue swordTimer;
    public BooleanValue bowSlowdown;
    public BooleanValue bowSprint;
    public BooleanValue bowSwitch;
    public BooleanValue bowToggle;
    public BooleanValue bowTimer;
    public BooleanValue restSlowdown;
    public BooleanValue restSprint;
    public BooleanValue restSwitch;
    public BooleanValue restToggle;
    public BooleanValue restBug;
    public BooleanValue restTimer;
    public BooleansSetting sword;
    public BooleansSetting bow;
    public BooleansSetting rest;
    public DoubleValue swordForward;
    public DoubleValue swordStrafe;
    public DoubleValue bowForward;
    public DoubleValue bowStrafe;
    public DoubleValue restForward;
    public DoubleValue restStrafe;
    public DoubleValue timerSword;
    public DoubleValue timerBow;
    public DoubleValue timerRest;
    private int counter;
    private ItemStack lastItemStack;
    
    public NoSlow() {
        super("NoSlow", new Color(10, 40, 53), Categorys.MOVEMENT);
        this.timeHelper = new TimeHelper();
        this.startSlow = new BooleanValue(16, "StartSlow", this, false);
        this.swordSlowdown = new BooleanValue(1, "Slowdown", this, false);
        this.swordSprint = new BooleanValue(2, "Sprint", this, false);
        this.swordSwitch = new BooleanValue(3, "Switch", this, false);
        this.swordToggle = new BooleanValue(4, "Toggle", this, false);
        this.swordBug = new BooleanValue(111, "Bug", this, false);
        this.swordTimer = new BooleanValue(5, "Timer", this, false);
        this.bowSlowdown = new BooleanValue(6, "Slowdown", this, false);
        this.bowSprint = new BooleanValue(7, "Sprint", this, false);
        this.bowSwitch = new BooleanValue(8, "Switch", this, false);
        this.bowToggle = new BooleanValue(9, "Toggle", this, false);
        this.bowTimer = new BooleanValue(10, "Timer", this, false);
        this.restSlowdown = new BooleanValue(11, "Slowdown", this, false);
        this.restSprint = new BooleanValue(12, "Sprint", this, false);
        this.restSwitch = new BooleanValue(13, "Switch", this, false);
        this.restToggle = new BooleanValue(14, "Toggle", this, false);
        this.restBug = new BooleanValue(1111, "Bug", this, false);
        this.restTimer = new BooleanValue(15, "Timer", this, false);
        this.sword = new BooleansSetting(25, "Sword", this, new Setting[] { this.swordSlowdown, this.swordSprint, this.swordSwitch, this.swordToggle, this.swordBug, this.swordTimer });
        this.bow = new BooleansSetting(26, "Bow", this, new Setting[] { this.bowSlowdown, this.bowSprint, this.bowSwitch, this.bowToggle, this.bowTimer });
        this.rest = new BooleansSetting(27, "Rest", this, new Setting[] { this.restSlowdown, this.restSprint, this.restToggle, this.restBug, this.restTimer });
        this.swordForward = new DoubleValue(16, "SwordForward", this, 0.2, 0.0, 1.0, 2);
        this.swordStrafe = new DoubleValue(17, "SwordStrafe", this, 0.2, 0.0, 1.0, 2);
        this.bowForward = new DoubleValue(18, "BowForward", this, 0.2, 0.0, 1.0, 2);
        this.bowStrafe = new DoubleValue(19, "BowStrafe", this, 0.2, 0.0, 1.0, 2);
        this.restForward = new DoubleValue(20, "RestForward", this, 0.2, 0.0, 1.0, 2);
        this.restStrafe = new DoubleValue(21, "RestStrafe", this, 0.2, 0.0, 1.0, 2);
        this.timerSword = new DoubleValue(22, "TimerSword", this, 0.2, 0.1, 2.0, 2);
        this.timerBow = new DoubleValue(23, "TimerBow", this, 0.2, 0.1, 2.0, 2);
        this.timerRest = new DoubleValue(24, "TimerRest", this, 0.2, 0.1, 2.0, 2);
        this.counter = 0;
        this.lastItemStack = null;
    }
    
    @EventTarget
    public void onEventNoSlow(final EventNoSlow eventNoSlow) {
        final ItemStack currentItem = NoSlow.mc.thePlayer.getCurrentEquippedItem();
        if (currentItem == null || !NoSlow.mc.thePlayer.isUsingItem() || (NoSlow.mc.thePlayer.moveForward == 0.0f && NoSlow.mc.thePlayer.moveStrafing == 0.0f)) {
            this.timeHelper.reset();
            return;
        }
        if (!this.timeHelper.reached(400L) && this.startSlow.getBoolean()) {
            return;
        }
        if (currentItem.getItem() instanceof ItemSword) {
            eventNoSlow.setSprint(this.swordSprint.getBoolean());
            if (this.swordSlowdown.getBoolean()) {
                eventNoSlow.setMoveForward((float)this.swordForward.getValue());
                eventNoSlow.setMoveStrafe((float)this.swordStrafe.getValue());
            }
        }
        else if (currentItem.getItem() instanceof ItemBow) {
            eventNoSlow.setSprint(this.bowSprint.getBoolean());
            if (this.bowSlowdown.getBoolean()) {
                eventNoSlow.setMoveForward((float)this.bowForward.getValue());
                eventNoSlow.setMoveStrafe((float)this.bowStrafe.getValue());
            }
        }
        else {
            eventNoSlow.setSprint(this.restSprint.getBoolean());
            if (this.restSlowdown.getBoolean()) {
                eventNoSlow.setMoveForward((float)this.restForward.getValue());
                eventNoSlow.setMoveStrafe((float)this.restStrafe.getValue());
            }
        }
    }
    
    @EventTarget
    public void onEventPreMotion(final EventPreMotion eventPreMotion) {
        final ItemStack currentItem = NoSlow.mc.thePlayer.getCurrentEquippedItem();
        if (currentItem == null || !NoSlow.mc.thePlayer.isUsingItem() || (NoSlow.mc.thePlayer.moveForward == 0.0f && NoSlow.mc.thePlayer.moveStrafing == 0.0f)) {
            return;
        }
        if (currentItem.getItem() instanceof ItemSword) {
            if (this.swordToggle.getBoolean()) {
                NoSlow.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
            }
            if (this.swordSwitch.getBoolean()) {
                final int slotIDtoSwitch = (NoSlow.mc.thePlayer.inventory.currentItem == 7) ? (NoSlow.mc.thePlayer.inventory.currentItem - 2) : (NoSlow.mc.thePlayer.inventory.currentItem + 2);
                NoSlow.mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(slotIDtoSwitch));
                NoSlow.mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(NoSlow.mc.thePlayer.inventory.currentItem));
            }
        }
        else if (currentItem.getItem() instanceof ItemBow) {
            if (this.bowToggle.getBoolean()) {
                NoSlow.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
            }
        }
        else if (this.restToggle.getBoolean()) {
            NoSlow.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
        }
    }
    
    @EventTarget
    public void onEventPostMotion(final EventPostMotion eventPostMotion) {
        final ItemStack currentItem = NoSlow.mc.thePlayer.getCurrentEquippedItem();
        if (currentItem == null || !NoSlow.mc.thePlayer.isUsingItem() || (NoSlow.mc.thePlayer.moveForward == 0.0f && NoSlow.mc.thePlayer.moveStrafing == 0.0f)) {
            return;
        }
        if (currentItem.getItem() instanceof ItemSword) {
            if (this.swordToggle.getBoolean()) {
                NoSlow.mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(NoSlow.mc.thePlayer.inventory.getCurrentItem()));
            }
        }
        else if (currentItem.getItem() instanceof ItemBow) {
            if (this.bowToggle.getBoolean()) {
                NoSlow.mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(NoSlow.mc.thePlayer.inventory.getCurrentItem()));
            }
        }
        else if (this.restToggle.getBoolean()) {
            NoSlow.mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(NoSlow.mc.thePlayer.inventory.getCurrentItem()));
        }
    }
    
    @EventTarget
    public void onEventUpdate(final EventUpdate eventUpdate) {
        final ItemStack currentItem = NoSlow.mc.thePlayer.getCurrentEquippedItem();
        if (currentItem == null || !NoSlow.mc.thePlayer.isUsingItem() || (NoSlow.mc.thePlayer.moveForward == 0.0f && NoSlow.mc.thePlayer.moveStrafing == 0.0f)) {
            return;
        }
        if (currentItem.getItem() instanceof ItemSword) {
            if (this.swordTimer.getBoolean()) {
                NoSlow.mc.getTimer().timerSpeed = (float)this.timerSword.getValue();
            }
            else {
                NoSlow.mc.getTimer().timerSpeed = 1.0f;
            }
        }
        else if (currentItem.getItem() instanceof ItemBow) {
            if (this.bowTimer.getBoolean()) {
                NoSlow.mc.getTimer().timerSpeed = (float)this.timerBow.getValue();
            }
            else {
                NoSlow.mc.getTimer().timerSpeed = 1.0f;
            }
            if (this.bowSwitch.getBoolean()) {
                final int slotIDtoSwitch = (NoSlow.mc.thePlayer.inventory.currentItem == 7) ? (NoSlow.mc.thePlayer.inventory.currentItem - 2) : (NoSlow.mc.thePlayer.inventory.currentItem + 2);
                NoSlow.mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(slotIDtoSwitch));
                NoSlow.mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(NoSlow.mc.thePlayer.inventory.currentItem));
            }
        }
        else {
            if (this.restTimer.getBoolean()) {
                NoSlow.mc.getTimer().timerSpeed = (float)this.timerRest.getValue();
            }
            else {
                NoSlow.mc.getTimer().timerSpeed = 1.0f;
            }
            if (this.restSwitch.getBoolean()) {
                final int slotIDtoSwitch = (NoSlow.mc.thePlayer.inventory.currentItem == 7) ? (NoSlow.mc.thePlayer.inventory.currentItem - 2) : (NoSlow.mc.thePlayer.inventory.currentItem + 2);
                NoSlow.mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(slotIDtoSwitch));
                NoSlow.mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(NoSlow.mc.thePlayer.inventory.currentItem));
            }
        }
    }
    
    @EventTarget
    public void onEventClick(final EventClick eventClick) {
        final ItemStack currentItem = NoSlow.mc.thePlayer.getCurrentEquippedItem();
        if (currentItem == null || !NoSlow.mc.thePlayer.isUsingItem() || (NoSlow.mc.thePlayer.moveForward == 0.0f && NoSlow.mc.thePlayer.moveStrafing == 0.0f)) {
            this.counter = 0;
            return;
        }
        if (this.lastItemStack != null && !this.lastItemStack.equals(currentItem)) {
            this.counter = 0;
        }
        if (currentItem.getItem() instanceof ItemSword) {
            if (this.swordBug.getBoolean()) {
                eventClick.setShouldRightClick(false);
                if (this.counter != 1) {
                    NoSlow.mc.thePlayer.sendQueue.addToSendQueueDirect(new C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT));
                    NoSlow.mc.thePlayer.stopUsingItem();
                    NoSlow.mc.thePlayer.closeScreen();
                    eventClick.setCanceled(true);
                    this.counter = 1;
                }
            }
        }
        else if (!(currentItem.getItem() instanceof ItemBow)) {
            if (this.restBug.getBoolean()) {
                eventClick.setShouldRightClick(false);
                if (this.counter != 3) {
                    NoSlow.mc.thePlayer.sendQueue.addToSendQueueDirect(new C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT));
                    NoSlow.mc.thePlayer.stopUsingItem();
                    NoSlow.mc.thePlayer.closeScreen();
                    eventClick.setCanceled(true);
                    this.counter = 3;
                }
            }
        }
        if (eventClick.isCanceled()) {
            NoSlow.mc.sendClickBlockToController(NoSlow.mc.currentScreen == null && NoSlow.mc.gameSettings.keyBindAttack.isKeyDown() && NoSlow.mc.inGameHasFocus);
        }
        this.lastItemStack = currentItem;
    }
    
    @Override
    public void onDisable() {
        NoSlow.mc.getTimer().timerSpeed = 1.0f;
    }
}
