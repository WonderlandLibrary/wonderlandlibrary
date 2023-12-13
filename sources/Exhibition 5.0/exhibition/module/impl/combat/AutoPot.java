// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.module.impl.combat;

import java.util.HashMap;
import java.util.Iterator;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.item.ItemPotion;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import exhibition.event.impl.EventPacket;
import exhibition.event.RegisterEvent;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import exhibition.util.RotationUtils;
import exhibition.util.PlayerUtil;
import net.minecraft.entity.player.EntityPlayer;
import exhibition.event.impl.EventMotion;
import exhibition.event.Event;
import exhibition.module.data.Setting;
import exhibition.module.data.ModuleData;
import exhibition.util.Timer;
import exhibition.module.Module;

public class AutoPot extends Module
{
    private static final String DELAY = "DELAY";
    private static final String HEALTH = "HEALTH";
    private final String PREDICT = "PREDICT";
    private String REGEN;
    private String OVERPOT;
    private Timer timer;
    public static int haltTicks;
    public static boolean potting;
    private boolean send;
    
    public AutoPot(final ModuleData data) {
        super(data);
        this.REGEN = "REGEN";
        this.OVERPOT = "OVERPOT";
        this.timer = new Timer();
        AutoPot.haltTicks = -1;
        ((HashMap<String, Setting<Integer>>)this.settings).put("HEALTH", new Setting<Integer>("HEALTH", 5, "Maximum health before healing.", 0.5, 0.5, 10.0));
        ((HashMap<String, Setting<Integer>>)this.settings).put("DELAY", new Setting<Integer>("DELAY", 350, "Delay before healing again.", 50.0, 100.0, 1000.0));
        ((HashMap<String, Setting<Boolean>>)this.settings).put("PREDICT", new Setting<Boolean>("PREDICT", false, "Predicts where to pot when moving."));
        ((HashMap<String, Setting<Boolean>>)this.settings).put(this.OVERPOT, new Setting<Boolean>(this.OVERPOT, true, "Pots earlier when armor is broken."));
        ((HashMap<String, Setting<Boolean>>)this.settings).put(this.REGEN, new Setting<Boolean>(this.REGEN, false, "Uses Regeneration pots."));
    }
    
    @RegisterEvent(events = { EventMotion.class, EventPacket.class })
    @Override
    public void onEvent(final Event event) {
        if (event instanceof EventMotion) {
            final EventMotion e = (EventMotion)event;
            if (e.isPre()) {
                if (AutoPot.potting && AutoPot.haltTicks < 0) {
                    AutoPot.potting = false;
                }
                float health = ((HashMap<K, Setting<Number>>)this.settings).get("HEALTH").getValue().floatValue() * 2.0f;
                if (AutoPot.mc.thePlayer.getEquipmentInSlot(4) == null && this.hasArmor(AutoPot.mc.thePlayer) && ((HashMap<K, Setting<Boolean>>)this.settings).get(this.OVERPOT).getValue()) {
                    health += ((AutoPot.mc.thePlayer.getEquipmentInSlot(1) == null) ? 6.0f : 3.0f);
                }
                final float delay = ((HashMap<K, Setting<Number>>)this.settings).get("DELAY").getValue().floatValue();
                if (PlayerUtil.isMoving()) {
                    if (AutoPot.mc.thePlayer.getHealth() <= health && this.getPotionFromInv() != -1 && this.timer.delay(delay)) {
                        AutoPot.haltTicks = 6;
                        this.swap(this.getPotionFromInv(), 6);
                        e.setPitch(120.0f);
                        if (((HashMap<K, Setting<Boolean>>)this.settings).get("PREDICT").getValue()) {
                            final double movedPosX = AutoPot.mc.thePlayer.posX + AutoPot.mc.thePlayer.motionX * 16.0;
                            final double movedPosY = AutoPot.mc.thePlayer.boundingBox.minY - 3.6;
                            final double movedPosZ = AutoPot.mc.thePlayer.posZ + AutoPot.mc.thePlayer.motionZ * 16.0;
                            final float[] predRot = RotationUtils.getRotationFromPosition(movedPosX, movedPosZ, movedPosY);
                            e.setYaw(predRot[0]);
                            e.setPitch(predRot[1]);
                        }
                        AutoPot.potting = true;
                        this.timer.reset();
                    }
                }
                else if (AutoPot.mc.thePlayer.getHealth() <= health && this.getPotionFromInv() != -1 && this.timer.delay(delay) && AutoPot.haltTicks < 0 && AutoPot.mc.thePlayer.isCollidedVertically) {
                    AutoPot.mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(AutoPot.mc.thePlayer.posX, AutoPot.mc.thePlayer.posY, AutoPot.mc.thePlayer.posZ, AutoPot.mc.thePlayer.rotationYaw, -90.0f, true));
                    this.swap(this.getPotionFromInv(), 6);
                    AutoPot.mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(6));
                    AutoPot.mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(AutoPot.mc.thePlayer.inventory.getCurrentItem()));
                    AutoPot.mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(AutoPot.mc.thePlayer.inventory.currentItem));
                    AutoPot.haltTicks = 5;
                    AutoPot.mc.thePlayer.jump();
                    AutoPot.potting = true;
                }
                --AutoPot.haltTicks;
            }
            if (e.isPost() && AutoPot.potting) {
                if (PlayerUtil.isMoving()) {
                    AutoPot.mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(6));
                    AutoPot.mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(AutoPot.mc.thePlayer.inventory.getCurrentItem()));
                    AutoPot.mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(AutoPot.mc.thePlayer.inventory.currentItem));
                }
                this.timer.reset();
            }
        }
    }
    
    private boolean hasArmor(final EntityPlayer player) {
        final ItemStack boots = player.inventory.armorInventory[0];
        final ItemStack pants = player.inventory.armorInventory[1];
        final ItemStack chest = player.inventory.armorInventory[2];
        final ItemStack head = player.inventory.armorInventory[3];
        return boots != null || pants != null || chest != null || head != null;
    }
    
    protected void swap(final int slot, final int hotbarNum) {
        AutoPot.mc.playerController.windowClick(AutoPot.mc.thePlayer.inventoryContainer.windowId, slot, hotbarNum, 2, AutoPot.mc.thePlayer);
    }
    
    private int getPotionFromInv() {
        int pot = -1;
        for (int i = 0; i < 45; ++i) {
            if (Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                final ItemStack is = Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(i).getStack();
                final Item item = is.getItem();
                if (item instanceof ItemPotion) {
                    final ItemPotion potion = (ItemPotion)item;
                    if (potion.getEffects(is) != null) {
                        for (final Object o : potion.getEffects(is)) {
                            final PotionEffect effect = (PotionEffect)o;
                            if ((effect.getPotionID() == Potion.heal.id || (effect.getPotionID() == Potion.regeneration.id && ((HashMap<K, Setting<Boolean>>)this.settings).get(this.REGEN).getValue() && !AutoPot.mc.thePlayer.isPotionActive(Potion.regeneration))) && ItemPotion.isSplash(is.getItemDamage())) {
                                pot = i;
                            }
                        }
                    }
                }
            }
        }
        return pot;
    }
}
