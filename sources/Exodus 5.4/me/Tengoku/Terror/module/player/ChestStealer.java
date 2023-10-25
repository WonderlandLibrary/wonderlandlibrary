/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.module.player;

import de.Hero.settings.Setting;
import java.util.ArrayList;
import me.Tengoku.Terror.Exodus;
import me.Tengoku.Terror.event.EventTarget;
import me.Tengoku.Terror.event.events.EventMotion;
import me.Tengoku.Terror.event.events.EventUpdate;
import me.Tengoku.Terror.module.Category;
import me.Tengoku.Terror.module.Module;
import me.Tengoku.Terror.util.Timer;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.ItemStack;

public class ChestStealer
extends Module {
    private Timer timer = new Timer();

    public ChestStealer() {
        super("Stealer", 25, Category.PLAYER, "Automatically steals all the items from the chest.");
    }

    @EventTarget
    public void onEvent(EventUpdate eventUpdate) {
        ContainerChest containerChest;
        String string = Exodus.INSTANCE.settingsManager.getSettingByName("Stealer Mode").getValString();
        this.setDisplayName("Stealer \ufffdf" + string);
        if (Minecraft.thePlayer.openContainer != null) {
            if (Minecraft.thePlayer.openContainer instanceof ContainerChest) {
                containerChest = (ContainerChest)Minecraft.thePlayer.openContainer;
                if (this.isChestEmpty(containerChest)) {
                    Minecraft.thePlayer.closeScreen();
                }
            }
        }
        if (string.equalsIgnoreCase("Instant")) {
            if (Minecraft.thePlayer.openContainer != null) {
                if (Minecraft.thePlayer.openContainer instanceof ContainerChest) {
                    containerChest = (ContainerChest)Minecraft.thePlayer.openContainer;
                    if (!containerChest.getLowerChestInventory().getName().contains("Menu") | containerChest.getLowerChestInventory().getName().contains("Play")) {
                        int n = 0;
                        while (n < containerChest.getLowerChestInventory().getSizeInventory()) {
                            if (containerChest.getLowerChestInventory().getStackInSlot(n) != null) {
                                Minecraft.playerController.windowClick(containerChest.windowId, n, 0, 1, Minecraft.thePlayer);
                            }
                            ++n;
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    private boolean isChestEmpty(ContainerChest containerChest) {
        int n = 0;
        while (n < containerChest.getLowerChestInventory().getSizeInventory()) {
            ItemStack itemStack = containerChest.getLowerChestInventory().getStackInSlot(n);
            if (itemStack != null) {
                return false;
            }
            ++n;
        }
        return true;
    }

    @Override
    public void setup() {
        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add("Delayed");
        arrayList.add("Instant");
        Exodus.INSTANCE.settingsManager.addSetting(new Setting("Stealer Mode", (Module)this, "Instant", arrayList));
        Exodus.INSTANCE.settingsManager.addSetting(new Setting("Delay", this, 100.0, 50.0, 1000.0, true));
    }

    @EventTarget
    public void onEvent(EventMotion eventMotion) {
        String string = Exodus.INSTANCE.settingsManager.getSettingByName("Stealer Mode").getValString();
        if (eventMotion.isPre() && string.equalsIgnoreCase("Delayed")) {
            if (Minecraft.thePlayer.openContainer != null) {
                if (Minecraft.thePlayer.openContainer instanceof ContainerChest) {
                    ContainerChest containerChest = (ContainerChest)Minecraft.thePlayer.openContainer;
                    if (!containerChest.getLowerChestInventory().getName().contains("Menu") | containerChest.getLowerChestInventory().getName().contains("Play") && !containerChest.getInventory().isEmpty()) {
                        int n = 0;
                        while (n < containerChest.getLowerChestInventory().getSizeInventory()) {
                            if (containerChest.getLowerChestInventory().getStackInSlot(n) != null) {
                                if (this.timer.hasTimeElapsed((long)(Exodus.INSTANCE.settingsManager.getSettingByModule("Delay", this).getValDouble() / 10.0), true)) {
                                    Minecraft.playerController.windowClick(containerChest.windowId, n, 0, 1, Minecraft.thePlayer);
                                }
                                this.timer.reset();
                            }
                            ++n;
                        }
                    }
                }
            }
        }
    }
}

