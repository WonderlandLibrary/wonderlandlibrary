package fun.expensive.client.feature.impl.combat;

import fun.rich.client.event.EventTarget;
import fun.rich.client.event.events.impl.player.EventPreMotion;
import fun.expensive.client.feature.Feature;
import fun.expensive.client.feature.impl.FeatureCategory;
import fun.rich.client.ui.settings.Setting;
import fun.rich.client.ui.settings.impl.BooleanSetting;
import fun.rich.client.ui.settings.impl.NumberSetting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.EnumHand;

public class AutoPotion extends Feature {
    public NumberSetting delay;

    public BooleanSetting onlyGround = new BooleanSetting("Only Ground", true, () -> Boolean.valueOf(true));

    public BooleanSetting strenght = new BooleanSetting("Strenght", true, () -> Boolean.valueOf(true));

    public BooleanSetting speed = new BooleanSetting("Speed", true, () -> Boolean.valueOf(true));

    public BooleanSetting fire_resistance = new BooleanSetting("Fire Resistance", true, () -> Boolean.valueOf(true));

    private BooleanSetting hotberOnly = new BooleanSetting("Hotbar Only", true, () -> Boolean.valueOf(true));

    ItemStack held;

    public AutoPotion() {
        super("AutoPotion", "\u0410\u0432\u0442\u043e\u043c\u0430\u0442\u0438\u0447\u0435\u0441\u043a\u0438 \u0431\u0440\u043e\u0441\u0430\u0435\u0442 Splash \u0437\u0435\u043b\u044c\u044f \u043d\u0430\u0445\u043e\u0434\u044f\u0449\u0438\u0435\u0441\u044f \u0432 \u0438\u043d\u0432\u0435\u043d\u0442\u0430\u0440\u0435", FeatureCategory.Combat);
        this.delay = new NumberSetting("Throw Delay", 300.0F, 10.0F, 800.0F, 10.0F, () -> Boolean.valueOf(true));
        addSettings(new Setting[] { (Setting)this.delay, (Setting)this.hotberOnly, (Setting)this.onlyGround, (Setting)this.strenght, (Setting)this.speed, (Setting)this.fire_resistance });
    }

    @EventTarget
    public void onPre(EventPreMotion event) {
        if (this.timerHelper.hasReached(this.delay.getNumberValue())) {
            if (this.strenght.getBoolValue() &&
                    !mc.player.isPotionActive(MobEffects.STRENGTH))
                throwPotion(5);
            if (this.speed.getBoolValue() &&
                    !mc.player.isPotionActive(MobEffects.SPEED))
                throwPotion(1);
            if (this.fire_resistance.getBoolValue() &&
                    !mc.player.isPotionActive(MobEffects.FIRE_RESISTANCE))
                throwPotion(12);
            this.timerHelper.reset();
        }
    }

    private int getPotionIndexInv(int id) {
        for (int i = 0; i < 45; i++) {
            int index = (i < 9) ? (i + 36) : i;
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
        if (this.onlyGround.getBoolValue() && !mc.player.onGround)
            return;
        int index = -1;
        if (getPotionIndexHb(potionId) == -1) {
            index = getPotionIndexInv(potionId);
        } else {
            index = getPotionIndexHb(potionId);
        }
        if (index == -1)
            return;
        if (!this.hotberOnly.getBoolValue() && index > 9) {
            mc.playerController.windowClick(0, index, 0, ClickType.PICKUP, (EntityPlayer)mc.player);
            mc.playerController.windowClick(0, 44, 0, ClickType.PICKUP, (EntityPlayer)mc.player);
            throwPot(index);
            mc.playerController.windowClick(0, 44, 0, ClickType.PICKUP, (EntityPlayer)mc.player);
        } else {
            throwPot(index);
        }
    }

    void throwPot(int index) {
        mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange((index < 9) ? index : 8));
        mc.player.connection.sendPacket((Packet)new CPacketPlayer.Rotation(mc.player.rotationYaw, 90.0F, mc.player.onGround));
        mc.player.rotationPitchHead = 90.0F;
        mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
        mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(mc.player.inventory.currentItem));
    }

    enum Potions {
        STRENGTH, SPEED, FIRERES;
    }
}
