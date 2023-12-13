/*
 * Decompiled with CFR 0_122.
 */
package me.arithmo.module.impl.combat;

import java.util.List;
import me.arithmo.event.Event;
import me.arithmo.event.RegisterEvent;
import me.arithmo.event.impl.EventMotion;
import me.arithmo.event.impl.EventPacket;
import me.arithmo.module.Module;
import me.arithmo.module.data.ModuleData;
import me.arithmo.module.data.Setting;
import me.arithmo.module.data.SettingsMap;
import me.arithmo.util.PlayerUtil;
import me.arithmo.util.Timer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class AutoPot
extends Module {
    private static final String DELAY = "DELAY";
    private static final String HEALTH = "HEALTH";
    private final String PSILENT = "PSILENT";
    private String OVERPOT = "OVERPOT";
    private Timer timer = new Timer();
    public static int haltTicks;
    public static boolean potting;
    private boolean send;

    public AutoPot(ModuleData data) {
        super(data);
        haltTicks = -1;
        this.settings.put("HEALTH", new Setting<Integer>("HEALTH", 5, "Maximum health before healing.", 0.5, 0.5, 10.0));
        this.settings.put("DELAY", new Setting<Integer>("DELAY", 350, "Delay before healing again.", 50.0, 100.0, 1000.0));
        this.settings.put("PSILENT", new Setting<Boolean>("PSILENT", false, "Silently pots with out looking. \u00a72(EXPERIMENTAL)"));
        this.settings.put(this.OVERPOT, new Setting<Boolean>(this.OVERPOT, true, "Pots earlier when armor is broken."));
    }

    @RegisterEvent(events={EventMotion.class, EventPacket.class})
    public void onEvent(Event event) {
        if (event instanceof EventMotion) {
            EventMotion e = (EventMotion)event;
            if (e.isPre()) {
                float health;
                if (potting && haltTicks < 0) {
                    potting = false;
                }
                if ((health = ((Number)((Setting)this.settings.get("HEALTH")).getValue()).floatValue() * 2.0f) < 3.0f && AutoPot.mc.thePlayer.getEquipmentInSlot(4) == null && ((Boolean)((Setting)this.settings.get(this.OVERPOT)).getValue()).booleanValue()) {
                    health += 2.0f;
                }
                float delay = ((Number)((Setting)this.settings.get("DELAY")).getValue()).floatValue();
                if (PlayerUtil.isMoving()) {
                    if (AutoPot.mc.thePlayer.getHealth() <= health && AutoPot.getPotionFromInv() != -1 && this.timer.delay(delay)) {
                        this.swap(AutoPot.getPotionFromInv(), 6);
                        e.setPitch(120.0f);
                        potting = true;
                        this.timer.reset();
                    }
                } else if (AutoPot.mc.thePlayer.getHealth() <= health && AutoPot.getPotionFromInv() != -1 && this.timer.delay(delay) && haltTicks < 0 && AutoPot.mc.thePlayer.isCollidedVertically) {
                    mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(AutoPot.mc.thePlayer.posX, AutoPot.mc.thePlayer.posY, AutoPot.mc.thePlayer.posZ, AutoPot.mc.thePlayer.rotationYaw, -90.0f, true));
                    this.swap(AutoPot.getPotionFromInv(), 6);
                    mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(6));
                    mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(AutoPot.mc.thePlayer.inventory.getCurrentItem()));
                    mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(AutoPot.mc.thePlayer.inventory.currentItem));
                    haltTicks = 5;
                    AutoPot.mc.thePlayer.jump();
                    potting = true;
                }
                --haltTicks;
            }
            if (e.isPost() && potting) {
                if (PlayerUtil.isMoving()) {
                    mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(6));
                    mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(AutoPot.mc.thePlayer.inventory.getCurrentItem()));
                    mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(AutoPot.mc.thePlayer.inventory.currentItem));
                    if (((Boolean)((Setting)this.settings.get("PSILENT")).getValue()).booleanValue()) {
                        mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(AutoPot.mc.thePlayer.rotationYaw, AutoPot.mc.thePlayer.rotationPitch, true));
                    }
                }
                this.timer.reset();
            }
        }
        if (event instanceof EventPacket && ((Boolean)((Setting)this.settings.get("PSILENT")).getValue()).booleanValue()) {
            EventPacket ep = (EventPacket)event;
            if (ep.isOutgoing() && ep.getPacket() instanceof C03PacketPlayer && !potting && this.send) {
                this.send = false;
                return;
            }
            if (potting && ep.isOutgoing() && ep.getPacket() instanceof C03PacketPlayer.C05PacketPlayerLook && !this.send) {
                C03PacketPlayer.C05PacketPlayerLook packet = (C03PacketPlayer.C05PacketPlayerLook)ep.getPacket();
                ep.setCancelled(true);
                mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C03PacketPlayer.C05PacketPlayerLook(AutoPot.mc.thePlayer.rotationYaw, AutoPot.mc.thePlayer.rotationPitch, true));
                mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C03PacketPlayer.C05PacketPlayerLook(AutoPot.mc.thePlayer.rotationYaw, 120.0f, true));
                this.send = true;
            }
        }
    }

    protected void swap(int slot, int hotbarNum) {
        AutoPot.mc.playerController.windowClick(AutoPot.mc.thePlayer.inventoryContainer.windowId, slot, hotbarNum, 2, AutoPot.mc.thePlayer);
    }

    public static int getPotionFromInv() {
        int pot = -1;
        for (int i = 0; i < 45; ++i) {
            ItemPotion potion;
            Item item;
            ItemStack is;
            if (!Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(i).getHasStack() || !((item = (is = Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(i).getStack()).getItem()) instanceof ItemPotion) || (potion = (ItemPotion)item).getEffects(is) == null) continue;
            for (Object o : potion.getEffects(is)) {
                PotionEffect effect = (PotionEffect)o;
                if (effect.getPotionID() != Potion.heal.id || !ItemPotion.isSplash(is.getItemDamage())) continue;
                pot = i;
            }
        }
        return pot;
    }
}

