package org.dreamcore.client.feature.impl.combat;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.EnumHand;
import org.dreamcore.client.event.EventTarget;
import org.dreamcore.client.event.events.impl.player.EventPreMotion;
import org.dreamcore.client.feature.Feature;
import org.dreamcore.client.feature.impl.Type;
import org.dreamcore.client.helpers.misc.TimerHelper;
import org.dreamcore.client.settings.impl.BooleanSetting;
import org.dreamcore.client.settings.impl.NumberSetting;

public class AutoPotion extends Feature {

    public TimerHelper timerHelper = new TimerHelper();
    public NumberSetting delay;
    public BooleanSetting onlyGround = new BooleanSetting("Only Ground", true, () -> true);

    public AutoPotion() {
        super("AutoPotion", "Автоматически бросает Splash зелья находящиеся в инвентаре", Type.Combat);
        this.delay = new NumberSetting("Throw Delay", 300, 10, 300, 10, () -> true);
        addSettings(delay, onlyGround);
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @EventTarget
    public void onPreMotion(EventPreMotion event) {
        if (onlyGround.getBoolValue() && !mc.player.onGround) {
            return;
        }

        if (!mc.player.isPotionActive((Potion.getPotionById(1))) && isPotionOnHotBar(Potions.SPEED)) {
            event.setPitch(90);
            mc.player.rotationPitchHead = 90;
            if (event.getPitch() == 90) {
                throwPot(Potions.SPEED);
            }
        } else if (!mc.player.isPotionActive((Potion.getPotionById(5))) && isPotionOnHotBar(Potions.STRENGTH)) {
            event.setPitch(90);
            mc.player.rotationPitchHead = 90;
            if (event.getPitch() == 90) {
                throwPot(Potions.STRENGTH);
            }
        } else if (!mc.player.isPotionActive((Potion.getPotionById(12))) && isPotionOnHotBar(Potions.FIRERES)) {
            event.setPitch(90);
            mc.player.rotationPitchHead = 90;
            if (event.getPitch() == 90) {
                throwPot(Potions.FIRERES);
            }
        }
    }

    public void throwPot(Potions potion) {
        int slot = getPotionSlot(potion);
        if (timerHelper.hasReached(delay.getNumberValue())) {
            mc.player.connection.sendPacket(new CPacketHeldItemChange(slot));
            mc.playerController.updateController();
            mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
            mc.playerController.updateController();
            mc.player.connection.sendPacket(new CPacketHeldItemChange(mc.player.inventory.currentItem));
            timerHelper.reset();
        }
    }

    public int getPotionSlot(Potions potion) {
        for (int i = 0; i < 9; ++i) {
            if (this.isStackPotion(mc.player.inventory.getStackInSlot(i), potion))
                return i;
        }
        return -1;
    }

    public boolean isPotionOnHotBar(Potions potions) {
        for (int i = 0; i < 9; ++i) {
            if (isStackPotion(mc.player.inventory.getStackInSlot(i), potions)) {
                return true;
            }
        }
        return false;
    }

    public boolean isStackPotion(ItemStack stack, Potions potion) {
        if (stack == null)
            return false;

        Item item = stack.getItem();

        if (item == Items.SPLASH_POTION) {
            int id = 5;

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
            }

            for (PotionEffect effect : PotionUtils.getEffectsFromStack(stack)) {
                if (effect.getPotion() == Potion.getPotionById(id)) {
                    return true;
                }
            }
        }
        return false;
    }

    public enum Potions {
        STRENGTH, SPEED, FIRERES
    }
}