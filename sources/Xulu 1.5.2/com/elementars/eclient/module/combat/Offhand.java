// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.module.combat;

import net.minecraft.inventory.EntityEquipmentSlot;
import com.elementars.eclient.event.EventTarget;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import com.elementars.eclient.event.events.LocalPlayerUpdateEvent;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Items;
import com.google.common.collect.Maps;
import com.elementars.eclient.module.Category;
import dev.xulu.settings.Value;
import net.minecraft.item.Item;
import java.util.Map;
import com.elementars.eclient.module.Module;

public class Offhand extends Module
{
    private Map<String, Item> itemMap;
    private final Value<String> item;
    public final Value<Boolean> conserveGapples;
    private final Value<Boolean> gapOnSword;
    private final Value<Integer> delay;
    public static boolean switchInProgress;
    boolean didFirst;
    int switchDelay1;
    int switchDelay2;
    int slot;
    
    public Offhand() {
        super("Offhand", "Automatically places items in your offhand", 0, Category.COMBAT, true);
        this.itemMap = (Map<String, Item>)Maps.newHashMap();
        this.item = this.register(new Value<String>("Item", this, "Crystals", new String[] { "Crystals", "Gapples" }));
        this.conserveGapples = this.register(new Value<Boolean>("Conserve Gap", this, true));
        this.gapOnSword = this.register(new Value<Boolean>("Gap On Sword", this, false));
        this.delay = this.register(new Value<Integer>("Delay", this, 1, 0, 5));
        this.didFirst = false;
        this.switchDelay1 = -1;
        this.switchDelay2 = -1;
        this.slot = -1;
        this.itemMap.put("Crystals", Items.END_CRYSTAL);
        this.itemMap.put("Gapples", Items.GOLDEN_APPLE);
    }
    
    @Override
    public void onUpdate() {
        if (Offhand.switchInProgress) {
            return;
        }
        if (Offhand.mc.currentScreen instanceof GuiContainer) {
            return;
        }
        if (AutoTotem.offhand_delay != 0) {
            return;
        }
        if (this.isOk() && Offhand.mc.player.getHeldItemOffhand().getItem() != this.itemMap.get(this.getItem())) {
            final int slot = (this.getSlot() < 9) ? (this.getSlot() + 36) : this.getSlot();
            if (this.getSlot() != -1) {
                this.slot = slot;
                Offhand.switchInProgress = true;
            }
        }
    }
    
    @EventTarget
    public void onPlayerUpdate(final LocalPlayerUpdateEvent event) {
        if (Offhand.switchInProgress) {
            if (this.switchDelay1 > 0) {
                --this.switchDelay1;
            }
            if (this.switchDelay2 > 0) {
                --this.switchDelay2;
            }
            if (!this.didFirst) {
                Offhand.mc.playerController.windowClick(0, this.slot, 0, ClickType.PICKUP, (EntityPlayer)Offhand.mc.player);
                this.switchDelay1 = this.delay.getValue();
                this.didFirst = true;
                return;
            }
            if (this.switchDelay1 == 0) {
                Offhand.mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, (EntityPlayer)Offhand.mc.player);
                this.switchDelay1 = -1;
                this.switchDelay2 = this.delay.getValue();
                return;
            }
            if (this.switchDelay2 == 0) {
                Offhand.mc.playerController.windowClick(0, this.slot, 0, ClickType.PICKUP, (EntityPlayer)Offhand.mc.player);
                this.switchDelay1 = -1;
                this.switchDelay2 = -1;
                this.slot = -1;
                Offhand.switchInProgress = false;
                this.didFirst = false;
            }
        }
    }
    
    private boolean isOk() {
        return Offhand.mc.player.getHealth() + Offhand.mc.player.getAbsorptionAmount() > Module.getModuleT(AutoTotem.class).health.getValue() && Offhand.mc.player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() != Items.ELYTRA;
    }
    
    String getItem() {
        if (this.gapOnSword.getValue() && Offhand.mc.player.getHeldItemMainhand().getItem() == Items.DIAMOND_SWORD) {
            return "Gapples";
        }
        return this.item.getValue();
    }
    
    int getSlot() {
        int slot = -1;
        for (int i = 45; i > -1; --i) {
            if (Offhand.mc.player.inventory.getStackInSlot(i).getItem() == this.itemMap.get(this.getItem())) {
                slot = i;
                break;
            }
        }
        return slot;
    }
    
    @Override
    public String getHudInfo() {
        int items = 0;
        for (int i = 45; i > -1; --i) {
            if (Offhand.mc.player.inventory.getStackInSlot(i).getItem() == this.itemMap.get(this.item.getValue())) {
                items += Offhand.mc.player.inventory.getStackInSlot(i).getCount();
            }
        }
        return items + "";
    }
    
    static {
        Offhand.switchInProgress = false;
    }
}
