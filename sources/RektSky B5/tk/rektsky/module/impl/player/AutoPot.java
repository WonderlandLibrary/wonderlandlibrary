/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.module.impl.player;

import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import tk.rektsky.event.Event;
import tk.rektsky.event.impl.WorldTickEvent;
import tk.rektsky.module.Category;
import tk.rektsky.module.Module;
import tk.rektsky.module.settings.DoubleSetting;
import tk.rektsky.utils.combat.RotationUtil;

public class AutoPot
extends Module {
    private int lastThrowTick = 0;
    private int ticks = 0;
    public DoubleSetting delay = new DoubleSetting("Delay", 0.0, 60.0, 10.0);
    public DoubleSetting health = new DoubleSetting("Health", 0.0, 20.0, 10.0);

    public AutoPot() {
        super("AutoPot", "Auto give you potion like p2w players", Category.PLAYER);
    }

    @Override
    public void onEnable() {
        this.ticks = 0;
        this.lastThrowTick = 0;
    }

    @Override
    public void onDisable() {
        RotationUtil.setRotating("autopot", false);
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof WorldTickEvent) {
            ++this.ticks;
            if ((double)this.mc.thePlayer.getHealth() < this.health.getValue()) {
                if ((double)(this.ticks - this.lastThrowTick) > this.delay.getValue()) {
                    RotationUtil.setReverseRotating("autopot", false);
                    RotationUtil.setPitch(90.0f);
                    RotationUtil.setYaw(this.mc.thePlayer.rotationYaw);
                    int slot = this.getSlot();
                    if (slot == -1) {
                        return;
                    }
                    this.mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(slot));
                    this.mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(this.mc.thePlayer.getHeldItem()));
                    this.mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(this.mc.thePlayer.inventory.currentItem));
                    this.lastThrowTick = this.ticks;
                } else {
                    RotationUtil.setReverseRotating("autopot", true);
                }
            }
        }
    }

    public int getSlot() {
        int item = -1;
        for (int i2 = 36; i2 < 45; ++i2) {
            if (this.mc.thePlayer.inventoryContainer.getSlot(i2).getStack() == null || !(this.mc.thePlayer.inventoryContainer.getSlot(i2).getStack().getItem() instanceof ItemPotion)) continue;
            ItemPotion itemPotion = (ItemPotion)this.mc.thePlayer.inventoryContainer.getSlot(i2).getStack().getItem();
            ItemStack itemStack = this.mc.thePlayer.inventoryContainer.getSlot(i2).getStack();
            for (PotionEffect effect : itemPotion.getEffects(itemStack)) {
                if (effect.getPotionID() != Potion.heal.getId()) continue;
                return i2 - 36;
            }
        }
        return item;
    }
}

