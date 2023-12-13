// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.modules.misc;

import net.minecraft.inventory.Slot;
import java.io.IOException;
import net.minecraft.item.Item;
import net.augustus.utils.RandomUtil;
import net.minecraft.client.gui.inventory.GuiChest;
import net.augustus.events.EventEarlyTick;
import net.minecraft.item.ItemStack;
import net.augustus.events.EventClick;
import net.lenni0451.eventapi.reflection.EventTarget;
import net.minecraft.client.gui.GuiIngameMenu;
import net.augustus.events.EventRender3D;
import net.minecraft.client.audio.SoundCategory;
import net.augustus.modules.Categorys;
import java.awt.Color;
import net.augustus.settings.StringValue;
import net.augustus.utils.TimeHelper;
import net.augustus.settings.BooleanValue;
import net.augustus.modules.Module;

public class AutoPlay extends Module
{
    public final BooleanValue cancelSound;
    private final TimeHelper delayTimer;
    public float[] rots;
    public float[] lastRots;
    public StringValue mode;
    public StringValue gameMode;
    private float volume;
    
    public AutoPlay() {
        super("AutoPlay", Color.green, Categorys.MISC);
        this.cancelSound = new BooleanValue(1, "CancelSound", this, true);
        this.delayTimer = new TimeHelper();
        this.rots = new float[2];
        this.lastRots = new float[2];
        this.mode = new StringValue(2, "Mode", this, "MinemenRanked", new String[] { "MinemenUnranked", "MinemenRanked" });
        this.gameMode = new StringValue(3, "Gamemode", this, "Sumo", new String[] { "Sumo", "Boxing" });
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
        this.volume = AutoPlay.mc.gameSettings.getSoundLevel(SoundCategory.MASTER);
        if (this.cancelSound.getBoolean()) {
            AutoPlay.mc.gameSettings.setSoundLevel(SoundCategory.MASTER, 0.0f);
        }
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
        AutoPlay.mc.gameSettings.setSoundLevel(SoundCategory.MASTER, this.volume);
    }
    
    @EventTarget
    public void onEventRender3D(final EventRender3D eventRender3D) {
        if (AutoPlay.mc.currentScreen instanceof GuiIngameMenu) {
            AutoPlay.mc.currentScreen = null;
        }
    }
    
    @EventTarget
    public void onEventClick(final EventClick eventClick) {
        if (AutoPlay.mc.currentScreen == null) {
            final ItemStack currStack = AutoPlay.mc.thePlayer.inventoryContainer.getSlot(AutoPlay.mc.thePlayer.inventory.currentItem + 36).getStack();
            if (currStack != null) {
                if (currStack.getDisplayName().contains("Unranked") && this.mode.getSelected().equals("MinemenUnranked")) {
                    AutoPlay.mc.gameSettings.keyBindUseItem.setPressTime(1);
                }
                if (!currStack.getDisplayName().contains("Unranked") && currStack.getDisplayName().contains("Ranked") && this.mode.getSelected().equals("MinemenRanked")) {
                    AutoPlay.mc.gameSettings.keyBindUseItem.setPressTime(1);
                }
                if (currStack.getDisplayName().contains("Play Again") && this.mode.getSelected().equals("MinemenUnranked")) {
                    AutoPlay.mc.gameSettings.keyBindUseItem.setPressTime(1);
                }
            }
        }
    }
    
    @EventTarget
    public void onEventEarlyTick(final EventEarlyTick eventEarlyTick) {
        if (AutoPlay.mc.currentScreen == null) {
            this.delayTimer.reset();
            for (int i = 36; i < AutoPlay.mc.thePlayer.inventoryContainer.inventorySlots.size(); ++i) {
                final ItemStack itemStack = AutoPlay.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (itemStack != null) {
                    final Item item = itemStack.getItem();
                    final String selected = this.mode.getSelected();
                    switch (selected) {
                        case "MinemenUnranked": {
                            if (itemStack.getDisplayName().contains("Unranked")) {
                                AutoPlay.mc.thePlayer.inventory.currentItem = i - 36;
                                break;
                            }
                            if (itemStack.getDisplayName().contains("Play Again")) {
                                AutoPlay.mc.thePlayer.inventory.currentItem = i - 36;
                                break;
                            }
                            break;
                        }
                        case "MinemenRanked": {
                            if (!itemStack.getDisplayName().contains("Unranked") && itemStack.getDisplayName().contains("Ranked")) {
                                AutoPlay.mc.thePlayer.inventory.currentItem = i - 36;
                                break;
                            }
                            break;
                        }
                    }
                }
            }
        }
        else if (AutoPlay.mc.currentScreen instanceof GuiChest && this.delayTimer.reached((long)(400.0 + RandomUtil.nextSecureInt(-50, 50)))) {
            final GuiChest guiChest = (GuiChest)AutoPlay.mc.currentScreen;
            for (int j = 0; j < guiChest.inventorySlots.inventorySlots.size(); ++j) {
                final ItemStack itemStack2 = guiChest.inventorySlots.getSlot(j).getStack();
                if (itemStack2 != null) {
                    final String selected2 = this.mode.getSelected();
                    switch (selected2) {
                        case "MinemenUnranked": {
                            if (itemStack2.getDisplayName().contains("Solo")) {
                                this.clickItem(j);
                                break;
                            }
                        }
                        case "MinemenRanked": {
                            if (itemStack2.getDisplayName().contains(this.gameMode.getSelected())) {
                                this.clickItem(j);
                                break;
                            }
                            break;
                        }
                    }
                }
            }
        }
    }
    
    private void clickItem(final int slot) {
        final GuiChest chest = (GuiChest)AutoPlay.mc.currentScreen;
        final Slot slot2 = chest.inventorySlots.getSlot(slot);
        try {
            chest.mouseClicked(slot2.xDisplayPosition + 2 + chest.guiLeft, slot2.yDisplayPosition + 2 + chest.guiTop, 0);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        this.delayTimer.reset();
    }
}
