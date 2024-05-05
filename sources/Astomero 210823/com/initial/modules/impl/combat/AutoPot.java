package com.initial.modules.impl.combat;

import com.initial.settings.impl.*;
import com.initial.utils.player.*;
import com.initial.modules.*;
import com.initial.settings.*;
import com.initial.events.impl.*;
import net.minecraft.potion.*;
import com.initial.utils.network.*;
import net.minecraft.network.*;
import net.minecraft.network.play.client.*;
import net.minecraft.item.*;
import com.initial.events.*;
import net.minecraft.util.*;

public class AutoPot extends Module
{
    public ModeSet mode;
    DoubleSet healthSet;
    DoubleSet delaySet;
    ModuleCategory potionsCat;
    public BooleanSet speedPotionSet;
    public BooleanSet regenPotionSet;
    public BooleanSet healingPotionSet;
    int oldSlot;
    int stage;
    TimerHelper throwAgainTimer;
    
    public AutoPot() {
        super("AutoPot", 0, Category.COMBAT);
        this.mode = new ModeSet("Mode", "Silent", new String[] { "Silent", "Real", "Tick" });
        this.healthSet = new DoubleSet("Regen HP", 18.0, 2.0, 20.0, 1.0, " hp");
        this.delaySet = new DoubleSet("Delay", 700.0, 200.0, 4000.0, 5.0, " ms");
        this.potionsCat = new ModuleCategory("Potions...");
        this.speedPotionSet = new BooleanSet("Speed", true);
        this.regenPotionSet = new BooleanSet("Regen", true);
        this.healingPotionSet = new BooleanSet("Healing", true);
        this.oldSlot = 0;
        this.stage = 0;
        this.throwAgainTimer = new TimerHelper();
        this.addSettings(this.mode, this.healthSet, this.delaySet, this.potionsCat);
        this.potionsCat.addCatSettings(this.speedPotionSet, this.regenPotionSet, this.healingPotionSet);
    }
    
    @EventTarget
    public void onPre(final EventMotion e) {
        this.setDisplayName("Auto Pot");
        int potState = -1;
        for (int a = 0; a < 9; ++a) {
            if (this.mc.thePlayer.inventory.getStackInSlot(a) != null && this.mc.thePlayer.inventory.getStackInSlot(a).getItem() instanceof ItemPotion) {
                potState = a;
                break;
            }
        }
        if (potState == -1) {
            return;
        }
        final ItemStack pot = this.mc.thePlayer.inventory.getStackInSlot(potState);
        if (this.isHealingPot(pot) && this.healingPotionSet.isEnabled() && this.mc.thePlayer.getHealth() < this.healthSet.getValue() && this.throwAgainTimer.timeElapsed((long)this.delaySet.getValue())) {
            e.setPitch(90.0f);
            this.stage = 1;
            this.throwAgainTimer.reset();
        }
        if (this.isSwiftnessPot(pot) && this.speedPotionSet.isEnabled() && !this.mc.thePlayer.isPotionActive(Potion.moveSpeed) && this.throwAgainTimer.timeElapsed((long)this.delaySet.getValue())) {
            e.setPitch(90.0f);
            this.stage = 1;
            this.throwAgainTimer.reset();
        }
        if (this.isRegenPot(pot) && this.regenPotionSet.isEnabled() && this.mc.thePlayer.getHealth() < this.healthSet.getValue() && !this.mc.thePlayer.isPotionActive(Potion.regeneration) && this.throwAgainTimer.timeElapsed((long)this.delaySet.getValue())) {
            e.setPitch(90.0f);
            this.stage = 1;
            this.throwAgainTimer.reset();
        }
        if (this.stage >= 1) {
            switch (this.stage) {
                case 1: {
                    e.setPitch(90.0f);
                    if (this.mode.is("Real")) {
                        this.oldSlot = this.mc.thePlayer.inventory.currentItem;
                        this.mc.thePlayer.inventory.currentItem = potState;
                    }
                    ++this.stage;
                    break;
                }
                case 2: {
                    e.setPitch(90.0f);
                    if (this.mode.is("Silent")) {
                        PacketUtil.sendPacket(new C09PacketHeldItemChange(potState));
                        PacketUtil.sendPacket(new C08PacketPlayerBlockPlacement(this.mc.thePlayer.inventory.getStackInSlot(potState)));
                        PacketUtil.sendPacket(new C09PacketHeldItemChange(this.mc.thePlayer.inventory.currentItem));
                    }
                    if (this.mode.is("Tick")) {
                        PacketUtil.sendPacket(new C09PacketHeldItemChange(potState));
                        PacketUtil.sendPacket(new C08PacketPlayerBlockPlacement(this.mc.thePlayer.inventory.getStackInSlot(potState)));
                    }
                    if (this.mode.is("Real")) {
                        PacketUtil.sendPacket(new C08PacketPlayerBlockPlacement(this.mc.thePlayer.inventory.getStackInSlot(potState)));
                    }
                    ++this.stage;
                    break;
                }
                case 3: {
                    e.setPitch(90.0f);
                    if (this.mode.is("Tick")) {
                        PacketUtil.sendPacket(new C09PacketHeldItemChange(this.mc.thePlayer.inventory.currentItem));
                    }
                    if (this.mode.is("Real")) {
                        this.mc.thePlayer.inventory.currentItem = this.oldSlot;
                    }
                    this.stage = 0;
                    break;
                }
            }
        }
    }
    
    private boolean isHealingPot(final ItemStack i) {
        return EnumChatFormatting.getTextWithoutFormattingCodes(i.getDisplayName().toLowerCase()).contains("healing");
    }
    
    private boolean isSwiftnessPot(final ItemStack i) {
        return EnumChatFormatting.getTextWithoutFormattingCodes(i.getDisplayName().toLowerCase()).contains("swiftness") || EnumChatFormatting.getTextWithoutFormattingCodes(i.getDisplayName().toLowerCase()).contains("speed");
    }
    
    private boolean isRegenPot(final ItemStack i) {
        return EnumChatFormatting.getTextWithoutFormattingCodes(i.getDisplayName().toLowerCase()).contains("regeneration");
    }
    
    private boolean isSplash(final ItemStack i) {
        return i.getDisplayName().toLowerCase().contains("splash");
    }
}
