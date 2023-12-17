// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.modules.impl.PLAYER;

import java.util.Iterator;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import ru.tuskevich.event.EventTarget;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.client.Minecraft;
import ru.tuskevich.modules.impl.COMBAT.KillAura;
import ru.tuskevich.event.events.impl.EventUpdate;
import ru.tuskevich.ui.dropui.setting.Setting;
import ru.tuskevich.util.math.TimerUtility;
import ru.tuskevich.ui.dropui.setting.imp.MultiBoxSetting;
import ru.tuskevich.modules.Type;
import ru.tuskevich.modules.ModuleAnnotation;
import ru.tuskevich.modules.Module;

@ModuleAnnotation(name = "AutoPotion", desc = "\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd\ufffd\ufffd\ufffd\ufffd", type = Type.PLAYER)
public class AutoPot extends Module
{
    MultiBoxSetting potions;
    public TimerUtility timerHelper;
    
    public AutoPot() {
        this.potions = new MultiBoxSetting("Potions Selection", new String[] { "Strength", "Speed", "Fire" });
        this.timerHelper = new TimerUtility();
        this.add(this.potions);
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate eventUpdate) {
        if (KillAura.target != null) {
            final Minecraft mc = AutoPot.mc;
            if (Minecraft.player.getCooledAttackStrength(1.0f) > 0.5f) {
                return;
            }
        }
        final Minecraft mc2 = AutoPot.mc;
        boolean b = false;
        Label_0110: {
            Label_0105: {
                if (Minecraft.player.onGround) {
                    final Minecraft mc3 = AutoPot.mc;
                    if (!Minecraft.player.isOnLadder()) {
                        final Minecraft mc4 = AutoPot.mc;
                        if (!Minecraft.player.isRiding()) {
                            final Minecraft mc5 = AutoPot.mc;
                            if (!Minecraft.player.capabilities.isFlying) {
                                final Minecraft mc6 = AutoPot.mc;
                                if (Minecraft.player.isInWater()) {
                                    final Minecraft mc7 = AutoPot.mc;
                                    if (!Minecraft.player.onGround) {
                                        break Label_0105;
                                    }
                                }
                                b = false;
                                break Label_0110;
                            }
                        }
                    }
                }
            }
            b = true;
        }
        final boolean check = b;
        final Minecraft mc8 = AutoPot.mc;
        boolean b2 = false;
        Label_0224: {
            if (Minecraft.player.isPotionActive(MobEffects.SPEED) || !isPotionOnHotBar(Potions.SPEED) || !this.potions.get(1)) {
                final Minecraft mc9 = AutoPot.mc;
                if (Minecraft.player.isPotionActive(MobEffects.STRENGTH) || !isPotionOnHotBar(Potions.STRENGTH) || !this.potions.get(0)) {
                    final Minecraft mc10 = AutoPot.mc;
                    if (Minecraft.player.isPotionActive(MobEffects.FIRE_RESISTANCE) || !isPotionOnHotBar(Potions.FIRERES) || !this.potions.get(2)) {
                        b2 = false;
                        break Label_0224;
                    }
                }
            }
            b2 = true;
        }
        final boolean throwCheck = b2;
        if (throwCheck && !check && this.timerHelper.hasTimeElapsed(400L)) {
            final Minecraft mc11 = AutoPot.mc;
            Minecraft.player.setSprinting(false);
            final Minecraft mc12 = AutoPot.mc;
            Minecraft.player.onGround = false;
            final Minecraft mc13 = AutoPot.mc;
            final NetHandlerPlayClient connection = Minecraft.player.connection;
            final Minecraft mc14 = AutoPot.mc;
            final float rotationYaw = Minecraft.player.rotationYaw;
            final float pitchIn = 90.0f;
            final Minecraft mc15 = AutoPot.mc;
            connection.sendPacket(new CPacketPlayer.Rotation(rotationYaw, pitchIn, Minecraft.player.onGround));
            final Minecraft mc16 = AutoPot.mc;
            if (!Minecraft.player.isPotionActive(MobEffects.SPEED) && isPotionOnHotBar(Potions.SPEED) && this.potions.get(1)) {
                this.throwPotion(Potions.SPEED);
            }
            final Minecraft mc17 = AutoPot.mc;
            if (!Minecraft.player.isPotionActive(MobEffects.STRENGTH) && isPotionOnHotBar(Potions.STRENGTH) && this.potions.get(0)) {
                this.throwPotion(Potions.STRENGTH);
            }
            final Minecraft mc18 = AutoPot.mc;
            if (!Minecraft.player.isPotionActive(MobEffects.FIRE_RESISTANCE) && isPotionOnHotBar(Potions.FIRERES) && this.potions.get(2)) {
                this.throwPotion(Potions.FIRERES);
            }
            final Minecraft mc19 = AutoPot.mc;
            final NetHandlerPlayClient connection2 = Minecraft.player.connection;
            final Minecraft mc20 = AutoPot.mc;
            connection2.sendPacket(new CPacketHeldItemChange(Minecraft.player.inventory.currentItem));
            this.timerHelper.reset();
        }
    }
    
    public void throwPotion(final Potions potion) {
        final int slot = getPotionSlot(potion);
        final Minecraft mc = AutoPot.mc;
        Minecraft.player.connection.sendPacket(new CPacketHeldItemChange(slot));
        AutoPot.mc.playerController.updateController();
        final Minecraft mc2 = AutoPot.mc;
        Minecraft.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
        AutoPot.mc.playerController.updateController();
        final Minecraft mc3 = AutoPot.mc;
        Minecraft.player.connection.sendPacket(new CPacketHeldItemChange(slot));
    }
    
    public static int getPotionSlot(final Potions potion) {
        for (int i = 0; i < 9; ++i) {
            final Minecraft mc = AutoPot.mc;
            if (isStackPotion(Minecraft.player.inventory.getStackInSlot(i), potion)) {
                return i;
            }
        }
        return -1;
    }
    
    public static boolean isPotionOnHotBar(final Potions potions) {
        return getPotionSlot(potions) != -1;
    }
    
    public static boolean isStackPotion(final ItemStack stack, final Potions potion) {
        if (stack == null) {
            return false;
        }
        final Item item = stack.getItem();
        if (item == Items.SPLASH_POTION) {
            int id = 0;
            switch (potion) {
                case STRENGTH: {
                    id = 5;
                    break;
                }
                case SPEED: {
                    id = 1;
                    break;
                }
                case FIRERES: {
                    id = 12;
                    break;
                }
                case HEAL: {
                    id = 6;
                    break;
                }
            }
            for (final PotionEffect effect : PotionUtils.getEffectsFromStack(stack)) {
                if (effect.getPotion() == Potion.getPotionById(id)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public enum Potions
    {
        STRENGTH, 
        SPEED, 
        FIRERES, 
        HEAL;
    }
}
