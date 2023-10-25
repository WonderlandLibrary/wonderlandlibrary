package wtf.evolution.module.impl.Combat;

import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.EnumHand;
import wtf.evolution.event.EventTarget;
import wtf.evolution.event.events.impl.EventMotion;
import wtf.evolution.helpers.animation.Counter;
import wtf.evolution.module.Category;
import wtf.evolution.module.Module;
import wtf.evolution.module.ModuleInfo;
import wtf.evolution.settings.options.BooleanSetting;
import wtf.evolution.settings.options.SliderSetting;

@ModuleInfo(name = "AutoPotion", type = Category.Combat)
public class AutoPotion extends Module {

    public static BooleanSetting onlyGround = new BooleanSetting("onlyGround", true);
    public static BooleanSetting strenght = new BooleanSetting("Strenght", true);
    public static BooleanSetting speed = new BooleanSetting("Speed", true);
    public static BooleanSetting fire_resistance = new BooleanSetting("Fire Resistance", true);
    public static BooleanSetting hotberOnly = new BooleanSetting("Hotbar Only", true);
    public SliderSetting delay = new SliderSetting("Delay", 100, 10, 1000, 10f);
    private final Counter timerUtils = new Counter();

    public AutoPotion() {
        addSettings(onlyGround,strenght,speed,fire_resistance,hotberOnly,delay);
    }

    ItemStack held;

    @EventTarget
    public void onPre(EventMotion event) {

        if (timerUtils.hasReached(delay.get())) {
            if (strenght.get()) {
                if (!mc.player.isPotionActive(MobEffects.STRENGTH)) {
                    throwPotion(5);
                }
            }
            if (speed.get()) {
                if (!mc.player.isPotionActive(MobEffects.SPEED)) {
                    throwPotion(1);
                }
            }
            if (fire_resistance.get()) {
                if (!mc.player.isPotionActive(MobEffects.FIRE_RESISTANCE)) {
                    throwPotion(12);
                }
            }
            timerUtils.reset();
        }


    }

    private int getPotionIndexInv(int id) {
        for (int i = 0; i < 45; i++) {
            int index = i < 9 ? i + 36 : i;
            for (PotionEffect potion : PotionUtils.getEffectsFromStack(mc.player.inventory.getStackInSlot(index))) {
                if (potion.getPotion() == Potion.getPotionById(id) && mc.player.inventory.getStackInSlot(i).getItem() == Items.SPLASH_POTION)
                    return index;
            }

        }
        return -1;
    }

    private int getPotionIndexHb(int id) {
        for (int i = 0; i < 9; i++) {
            for (PotionEffect potion : PotionUtils.getEffectsFromStack(mc.player.inventory.getStackInSlot(i))) {
                if (potion.getPotion() == Potion.getPotionById(id) && mc.player.inventory.getStackInSlot(i).getItem() == Items.SPLASH_POTION)
                    return i;
            }
        }
        return -1;
    }

    private void throwPotion(int potionId) {
        if (onlyGround.get() && !mc.player.onGround) {
            return;
        }

        int index = -1;
        if (getPotionIndexHb(potionId) == -1) {
            index = getPotionIndexInv(potionId);
        } else {
            index = getPotionIndexHb(potionId);
        }

        if (index == -1) return;
        if (!hotberOnly.get() && index > 9) {
            mc.playerController.windowClick(0, index, 0, ClickType.PICKUP, mc.player);
            mc.playerController.windowClick(0, 44, 0, ClickType.PICKUP, mc.player);
            throwPot(index);
            mc.playerController.windowClick(0, 44, 0, ClickType.PICKUP, mc.player);
        } else {
            throwPot(index);
        }

    }

    void throwPot(int index) {
        mc.player.connection.sendPacket(new CPacketHeldItemChange(index < 9 ? index : 8));
        mc.player.connection.sendPacket(new CPacketPlayer.Rotation(mc.player.rotationYaw, 90, mc.player.onGround));
        mc.player.rotationPitchHead = 90;
        mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
        mc.player.connection.sendPacket(new CPacketHeldItemChange(mc.player.inventory.currentItem));
    }

    enum Potions {
        STRENGTH, SPEED, FIRERES
    }
}
