/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.module.combat;

import me.Tengoku.Terror.event.EventTarget;
import me.Tengoku.Terror.event.events.EventUpdate;
import me.Tengoku.Terror.module.Category;
import me.Tengoku.Terror.module.Module;
import me.Tengoku.Terror.util.TimerUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.item.ItemAppleGold;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.BlockPos;

public class AutoHeal
extends Module {
    public static boolean doSoup;
    private int slot = -1;
    FontRenderer fr;
    public static boolean healing;
    private final TimerUtils timer = new TimerUtils();
    private int currentslot = -1;
    protected boolean sendingPacket = false;
    protected boolean switchedItem = false;
    protected int lastSlot = 0;

    public AutoHeal() {
        super("Auto Heal", 0, Category.COMBAT, "Heals you using gapples and potions from your inventory.");
        this.fr = Minecraft.fontRendererObj;
    }

    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {
        if (Minecraft.thePlayer != null) {
            int n;
            if (Minecraft.theWorld != null && (n = this.findGoldenApple()) != -1) {
                if (Minecraft.thePlayer.getHealth() < 15.0f) {
                    if (Minecraft.thePlayer.inventory.currentItem != n) {
                        this.lastSlot = Minecraft.thePlayer.inventory.currentItem;
                    }
                    Minecraft.thePlayer.onGround = true;
                    Minecraft.gameSettings.keyBindJump.pressed = false;
                    Minecraft.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(n));
                    this.switchedItem = true;
                    Minecraft.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(new BlockPos(Minecraft.thePlayer), 255, Minecraft.thePlayer.getHeldItem(), 0.0f, 0.0f, 0.0f));
                    if (Minecraft.thePlayer.isEating()) {
                        if (this.sendingPacket) {
                            int n2 = 0;
                            while ((double)n2 < 10.0) {
                                Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
                                ++n2;
                            }
                            this.sendingPacket = false;
                        } else {
                            this.sendingPacket = true;
                        }
                    }
                } else if (this.switchedItem) {
                    Minecraft.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(this.lastSlot));
                    this.switchedItem = false;
                    if (n != -1) {
                        int n3 = Minecraft.thePlayer.inventory.getStackInSlot((int)n).stackSize;
                        ScaledResolution scaledResolution = new ScaledResolution(mc);
                        Gui.drawRect(scaledResolution.getScaledWidth() / 2 + 9, scaledResolution.getScaledHeight() / 2 - 1, scaledResolution.getScaledWidth() / 2 + 10 + this.fr.getStringWidth(String.valueOf(n3)), scaledResolution.getScaledHeight() / 2 + this.fr.FONT_HEIGHT - 1, Integer.MIN_VALUE);
                        this.fr.drawString(String.valueOf(n3), scaledResolution.getScaledWidth() / 2 + 10, scaledResolution.getScaledHeight() / 2, -1);
                    }
                }
            }
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    protected int findGoldenApple() {
        int n = 0;
        while (n < 9) {
            if (Minecraft.thePlayer.inventoryContainer.getSlot(36 + n).getHasStack()) {
                if (Minecraft.thePlayer.inventoryContainer.getSlot(36 + n).getStack().getItem() instanceof ItemAppleGold) {
                    return n;
                }
            }
            ++n;
        }
        return -1;
    }

    static {
        healing = false;
    }
}

