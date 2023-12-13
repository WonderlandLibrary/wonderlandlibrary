// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.module.combat;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.item.ItemEnderPearl;
import com.elementars.eclient.event.events.LocalPlayerUpdateEvent;
import net.minecraft.world.World;
import net.minecraft.util.EnumHand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import com.elementars.eclient.event.EventTarget;
import net.minecraft.init.Items;
import net.minecraft.util.math.RayTraceResult;
import com.elementars.eclient.module.misc.MCF;
import net.minecraft.client.gui.inventory.GuiContainer;
import com.elementars.eclient.event.events.EventMiddleClick;
import com.elementars.eclient.module.Category;
import dev.xulu.settings.Value;
import com.elementars.eclient.module.Module;

public class MiddleClickPearl extends Module
{
    private final Value<String> mode;
    private final Value<Integer> delayA;
    private final Value<Integer> delay;
    private int slot;
    private int slot2;
    int time;
    boolean hasClicked;
    boolean isFinishingTask;
    boolean isThrowingPearl;
    public static boolean switchInProgress;
    boolean didFirst;
    int switchDelay1;
    int switchDelay2;
    
    public MiddleClickPearl() {
        super("MiddleClickPearl", "Throws a pearl without it being in your hotbar", 0, Category.COMBAT, true);
        this.mode = this.register(new Value<String>("Mode", this, "Switch", new String[] { "Switch", "Instant" }));
        this.delayA = this.register(new Value<Integer>("Click Delay", this, 1, 0, 5)).visibleWhen(integer -> this.mode.getValue().equalsIgnoreCase("Switch"));
        this.delay = this.register(new Value<Integer>("Switch Delay", this, 10, 0, 80)).visibleWhen(integer -> this.mode.getValue().equalsIgnoreCase("Switch"));
        this.slot = -1;
        this.slot2 = -1;
        this.hasClicked = false;
        this.isFinishingTask = false;
        this.isThrowingPearl = false;
        this.didFirst = false;
        this.switchDelay1 = -1;
        this.switchDelay2 = -1;
    }
    
    @EventTarget
    public void onMiddleClick(final EventMiddleClick event) {
        if (MiddleClickPearl.mc.currentScreen instanceof GuiContainer) {
            return;
        }
        final RayTraceResult ray = MCF.mc.objectMouseOver;
        if (ray.typeOfHit == RayTraceResult.Type.ENTITY) {
            return;
        }
        if (ray.typeOfHit == RayTraceResult.Type.BLOCK) {
            return;
        }
        if (this.mode.getValue().equalsIgnoreCase("Instant")) {
            this.throwPearl();
            return;
        }
        this.slot = ((this.getSlot() < 9) ? (this.getSlot() + 36) : this.getSlot());
        this.slot2 = ((this.getSlot2() < 9) ? (this.getSlot2() + 36) : this.getSlot2());
        if (this.getSlot() == -1) {
            this.sendDebugMessage("No pearl found!");
            return;
        }
        if (this.getSlot() != -1 && this.getSlot2() != -1 && MiddleClickPearl.mc.player.getHeldItemMainhand().getItem() != Items.ENDER_PEARL) {
            MiddleClickPearl.mc.addScheduledTask(() -> {
                MiddleClickPearl.switchInProgress = true;
                this.isThrowingPearl = true;
                return;
            });
            this.hasClicked = true;
        }
    }
    
    @Override
    public void onUpdate() {
        if (MiddleClickPearl.switchInProgress) {
            if (this.switchDelay1 > 0) {
                --this.switchDelay1;
            }
            if (this.switchDelay2 > 0) {
                --this.switchDelay2;
            }
            if (!this.didFirst) {
                MiddleClickPearl.mc.playerController.windowClick(0, this.slot, 0, ClickType.PICKUP, (EntityPlayer)MiddleClickPearl.mc.player);
                this.switchDelay1 = this.delayA.getValue();
                this.didFirst = true;
                return;
            }
            if (this.switchDelay1 == 0) {
                MiddleClickPearl.mc.playerController.windowClick(0, this.slot2, 0, ClickType.PICKUP, (EntityPlayer)MiddleClickPearl.mc.player);
                this.switchDelay1 = -1;
                this.switchDelay2 = this.delayA.getValue();
                return;
            }
            if (this.switchDelay2 == 0) {
                MiddleClickPearl.mc.playerController.windowClick(0, this.slot, 0, ClickType.PICKUP, (EntityPlayer)MiddleClickPearl.mc.player);
                this.switchDelay1 = -1;
                this.switchDelay2 = -1;
                MiddleClickPearl.switchInProgress = false;
                this.didFirst = false;
                if (this.isThrowingPearl) {
                    MiddleClickPearl.mc.playerController.processRightClick((EntityPlayer)MiddleClickPearl.mc.player, (World)MiddleClickPearl.mc.world, EnumHand.MAIN_HAND);
                    MiddleClickPearl.mc.player.swingArm(EnumHand.MAIN_HAND);
                    this.isThrowingPearl = false;
                }
                if (this.isFinishingTask) {
                    this.slot = -1;
                    this.slot2 = -1;
                    this.time = 0;
                    this.hasClicked = false;
                    this.isFinishingTask = false;
                    MiddleClickPearl.mc.playerController.updateController();
                }
            }
        }
    }
    
    @EventTarget
    public void onPlayerUpdate(final LocalPlayerUpdateEvent event) {
        if (MiddleClickPearl.mc.player.getHeldItemMainhand().getItem() == Items.ENDER_PEARL && this.hasClicked) {
            ++this.time;
            if (this.time < this.delay.getValue()) {
                return;
            }
            MiddleClickPearl.mc.addScheduledTask(() -> {
                MiddleClickPearl.switchInProgress = true;
                this.isFinishingTask = true;
            });
        }
        else if (this.getSlot() == -1 && this.hasClicked) {
            MiddleClickPearl.mc.addScheduledTask(() -> {
                MiddleClickPearl.switchInProgress = true;
                this.isFinishingTask = true;
            });
        }
    }
    
    int getSlot() {
        int slot = -1;
        for (int i = 45; i > -1; --i) {
            if (MiddleClickPearl.mc.player.inventory.getStackInSlot(i).getItem() == Items.ENDER_PEARL) {
                slot = i;
                break;
            }
        }
        return slot;
    }
    
    int getSlot2() {
        int slot = -1;
        for (int i = 45; i > -1; --i) {
            if (MiddleClickPearl.mc.player.inventory.getStackInSlot(i) == MiddleClickPearl.mc.player.getHeldItemMainhand()) {
                slot = i;
                break;
            }
        }
        return slot;
    }
    
    private void throwPearl() {
        final int pearlSlot = findHotbarBlock(ItemEnderPearl.class);
        final boolean offhand = MiddleClickPearl.mc.player.getHeldItemOffhand().getItem() == Items.ENDER_PEARL;
        if (pearlSlot != -1 || offhand) {
            final int oldslot = MiddleClickPearl.mc.player.inventory.currentItem;
            if (!offhand) {
                switchToHotbarSlot(pearlSlot, false);
            }
            MiddleClickPearl.mc.playerController.processRightClick((EntityPlayer)MiddleClickPearl.mc.player, (World)MiddleClickPearl.mc.world, offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND);
            if (!offhand) {
                switchToHotbarSlot(oldslot, false);
            }
        }
    }
    
    public static void switchToHotbarSlot(final int slot, final boolean silent) {
        if (MiddleClickPearl.mc.player.inventory.currentItem == slot || slot < 0) {
            return;
        }
        if (silent) {
            MiddleClickPearl.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(slot));
            MiddleClickPearl.mc.playerController.updateController();
        }
        else {
            MiddleClickPearl.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(slot));
            MiddleClickPearl.mc.player.inventory.currentItem = slot;
            MiddleClickPearl.mc.playerController.updateController();
        }
    }
    
    public static void switchToHotbarSlot(final Class clazz, final boolean silent) {
        final int slot = findHotbarBlock(clazz);
        if (slot > -1) {
            switchToHotbarSlot(slot, silent);
        }
    }
    
    public static int findHotbarBlock(final Class clazz) {
        for (int i = 0; i < 9; ++i) {
            final ItemStack stack = MiddleClickPearl.mc.player.inventory.getStackInSlot(i);
            if (stack != ItemStack.EMPTY) {
                if (clazz.isInstance(stack.getItem())) {
                    return i;
                }
                if (stack.getItem() instanceof ItemBlock) {
                    final Block block = ((ItemBlock)stack.getItem()).getBlock();
                    if (clazz.isInstance(block)) {
                        return i;
                    }
                }
            }
        }
        return -1;
    }
    
    static {
        MiddleClickPearl.switchInProgress = false;
    }
}
