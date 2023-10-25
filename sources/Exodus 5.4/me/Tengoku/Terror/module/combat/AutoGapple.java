/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.module.combat;

import de.Hero.settings.Setting;
import me.Tengoku.Terror.Exodus;
import me.Tengoku.Terror.event.EventTarget;
import me.Tengoku.Terror.event.events.EventUpdate;
import me.Tengoku.Terror.module.Category;
import me.Tengoku.Terror.module.Module;
import me.Tengoku.Terror.util.TimerUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemAppleGold;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.BlockPos;

public class AutoGapple
extends Module {
    private TimerUtils timer;
    public Setting autoHeal;
    public Setting delay;
    public Setting healPercent;
    FontRenderer fr = Minecraft.fontRendererObj;
    public Setting speed;
    public Setting other;
    public Setting jumpBoost;

    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {
        if (Minecraft.thePlayer != null) {
            if (Minecraft.theWorld != null) {
                int n = this.findGoldenApple();
                int n2 = Minecraft.thePlayer.inventory.currentItem;
                if ((double)Minecraft.thePlayer.getHealth() < this.healPercent.getValDouble() && n != -1) {
                    Minecraft.thePlayer.inventory.currentItem = n;
                    Minecraft.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(new BlockPos(Minecraft.thePlayer), 255, Minecraft.thePlayer.getHeldItem(), 0.0f, 0.0f, 0.0f));
                    if (Minecraft.thePlayer.isEating()) {
                        Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
                    } else {
                        Minecraft.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(n2));
                    }
                }
            }
        }
    }

    public AutoGapple() {
        super("Auto Gapple", 0, Category.COMBAT, "Automatically eats golden apples for you.");
        this.timer = new TimerUtils();
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

    @Override
    public void setup() {
        this.delay = new Setting("Delay", this, 150.0, 50.0, 400.0, true);
        Exodus.INSTANCE.settingsManager.addSetting(this.delay);
        this.healPercent = new Setting("Health", this, 15.0, 1.0, 20.0, true);
        Exodus.INSTANCE.settingsManager.addSetting(this.healPercent);
    }
}

