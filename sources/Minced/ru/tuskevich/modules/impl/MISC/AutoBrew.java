// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.modules.impl.MISC;

import net.minecraft.item.Item;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.inventory.Slot;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import ru.tuskevich.event.EventTarget;
import net.minecraft.item.ItemAir;
import net.minecraft.init.Items;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.ContainerBrewingStand;
import ru.tuskevich.event.events.impl.EventUpdate;
import ru.tuskevich.ui.dropui.setting.Setting;
import ru.tuskevich.ui.dropui.setting.imp.BooleanSetting;
import ru.tuskevich.ui.dropui.setting.imp.SliderSetting;
import ru.tuskevich.ui.dropui.setting.imp.ModeSetting;
import ru.tuskevich.util.math.TimerHelper;
import ru.tuskevich.modules.Type;
import ru.tuskevich.modules.ModuleAnnotation;
import ru.tuskevich.modules.Module;

@ModuleAnnotation(name = "AutoBrew", desc = "", type = Type.MISC)
public class AutoBrew extends Module
{
    private final TimerHelper timerHelper;
    private ModeSetting mode;
    private SliderSetting delay;
    private BooleanSetting addGunpowder;
    private BooleanSetting loot;
    
    public AutoBrew() {
        this.timerHelper = new TimerHelper();
        this.mode = new ModeSetting("AutoBrew Mode", "Strenght", new String[] { "Strenght", "Speed", "Fire Res." });
        this.delay = new SliderSetting("Slot Delay", 1000.0f, 0.0f, 10000.0f, 10.0f);
        this.addGunpowder = new BooleanSetting("Add Gunpowder", false);
        this.loot = new BooleanSetting("Loot", true);
        this.add(this.mode, this.addGunpowder, this.loot, this.delay);
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate event) {
        final Minecraft mc = AutoBrew.mc;
        if (Minecraft.player.openContainer instanceof ContainerBrewingStand) {
            if (this.isFuelEmpty()) {
                this.swapOneItem(Items.BLAZE_POWDER, 4);
            }
            for (int i = 0; i < 3; ++i) {
                if (this.getItem(i) instanceof ItemAir && this.timerHelper.hasReached(this.delay.getDoubleValue())) {
                    this.swapOneItem(this.findPotionInBrewingStand("water"), i);
                    this.timerHelper.reset();
                }
            }
            if (this.getItem(3) instanceof ItemAir) {
                if (this.identicalPotionsCheck("water")) {
                    this.swapOneItem(Items.NETHER_WART, 3);
                }
                if (this.mode.index == 0) {
                    if (this.identicalPotionsCheck("awkward")) {
                        this.swapOneItem(Items.BLAZE_POWDER, 3);
                    }
                }
                else if (this.mode.index == 1) {
                    if (this.identicalPotionsCheck("awkward")) {
                        this.swapOneItem(Items.SUGAR, 3);
                    }
                }
                else if (this.mode.index == 2 && this.identicalPotionsCheck("awkward")) {
                    this.swapOneItem(Items.MAGMA_CREAM, 3);
                }
                if (this.identicalPotionsCheck("strength") || this.identicalPotionsCheck("swiftness")) {
                    this.swapOneItem(Items.GLOWSTONE_DUST, 3);
                }
                if (this.identicalPotionsCheck("fire_resistance")) {
                    this.swapOneItem(Items.REDSTONE, 3);
                }
                if (this.identicalPotionsCheck("strong_strength") || this.identicalPotionsCheck("strong_swiftness") || this.identicalPotionsCheck("long_fire_resistance")) {
                    if (this.addGunpowder.get() && this.findItemInBrewingStand(Items.GUNPOWDER) != -1) {
                        if (this.identicalSplashPotionsCheck()) {
                            this.loot();
                        }
                        else {
                            this.swapOneItem(Items.GUNPOWDER, 3);
                        }
                    }
                    else {
                        this.loot();
                    }
                }
            }
        }
    }
    
    private void loot() {
        for (int i = 0; i < 3; ++i) {
            if (this.timerHelper.hasReached(this.delay.getDoubleValue())) {
                final PlayerControllerMP playerController = AutoBrew.mc.playerController;
                final Minecraft mc = AutoBrew.mc;
                final int windowId = Minecraft.player.openContainer.windowId;
                final int slotId = i;
                final int mouseButton = 0;
                final ClickType quick_MOVE = ClickType.QUICK_MOVE;
                final Minecraft mc2 = AutoBrew.mc;
                playerController.windowClick(windowId, slotId, mouseButton, quick_MOVE, Minecraft.player);
            }
        }
    }
    
    private boolean identicalSplashPotionsCheck() {
        boolean needIng = true;
        for (int i = 0; i < 3; ++i) {
            final Minecraft mc = AutoBrew.mc;
            if (Minecraft.player.openContainer.inventorySlots.get(i).getStack().getItem() != Items.SPLASH_POTION) {
                needIng = false;
            }
        }
        return needIng;
    }
    
    private boolean identicalPotionsCheck(final String potionType) {
        boolean needIng = true;
        for (int i = 0; i < 3; ++i) {
            final Minecraft mc = AutoBrew.mc;
            if (PotionUtils.getPotionFromItem(Minecraft.player.openContainer.inventorySlots.get(i).getStack()) != PotionType.getPotionTypeForName(potionType)) {
                needIng = false;
            }
        }
        return needIng;
    }
    
    public void swapOneItem(final Item item, final int to) {
        final int slot;
        if (this.timerHelper.hasReached(this.delay.getFloatValue()) && (slot = this.findItemInBrewingStand(item)) != -1) {
            this.swapOneItem(slot, to);
            this.timerHelper.reset();
        }
    }
    
    public void swapOneItem(final int from, final int to) {
        final PlayerControllerMP playerController = AutoBrew.mc.playerController;
        final Minecraft mc = AutoBrew.mc;
        final int windowId = Minecraft.player.openContainer.windowId;
        final int mouseButton = 0;
        final ClickType pickup = ClickType.PICKUP;
        final Minecraft mc2 = AutoBrew.mc;
        playerController.windowClick(windowId, from, mouseButton, pickup, Minecraft.player);
        final PlayerControllerMP playerController2 = AutoBrew.mc.playerController;
        final Minecraft mc3 = AutoBrew.mc;
        final int windowId2 = Minecraft.player.openContainer.windowId;
        final int mouseButton2 = 1;
        final ClickType pickup2 = ClickType.PICKUP;
        final Minecraft mc4 = AutoBrew.mc;
        playerController2.windowClick(windowId2, to, mouseButton2, pickup2, Minecraft.player);
        final PlayerControllerMP playerController3 = AutoBrew.mc.playerController;
        final Minecraft mc5 = AutoBrew.mc;
        final int windowId3 = Minecraft.player.openContainer.windowId;
        final int mouseButton3 = 0;
        final ClickType pickup3 = ClickType.PICKUP;
        final Minecraft mc6 = AutoBrew.mc;
        playerController3.windowClick(windowId3, from, mouseButton3, pickup3, Minecraft.player);
    }
    
    private Item getItem(final int slotId) {
        final Minecraft mc = AutoBrew.mc;
        return Minecraft.player.openContainer.inventorySlots.get(slotId).getStack().getItem();
    }
    
    private boolean isFuelEmpty() {
        return this.getItem(4) instanceof ItemAir;
    }
    
    private int findFuel() {
        return this.findItemInBrewingStand(Items.BLAZE_POWDER);
    }
    
    private int findPotionInBrewingStand(final String potionType) {
        for (int i = 5; i < 41; ++i) {
            final Minecraft mc = AutoBrew.mc;
            if (PotionUtils.getPotionFromItem(Minecraft.player.openContainer.inventorySlots.get(i).getStack()) == PotionType.getPotionTypeForName(potionType)) {
                return i;
            }
        }
        return -1;
    }
    
    private int findItemInBrewingStand(final Item item) {
        for (int i = 5; i < 41; ++i) {
            if (this.getItem(i) == item) {
                return i;
            }
        }
        return -1;
    }
}
