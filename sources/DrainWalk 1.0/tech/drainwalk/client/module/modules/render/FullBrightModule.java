package tech.drainwalk.client.module.modules.render;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import tech.drainwalk.client.module.Module;
import tech.drainwalk.client.module.category.Category;
import tech.drainwalk.client.module.category.Type;
import tech.drainwalk.client.option.options.SelectOption;
import tech.drainwalk.client.option.options.SelectOptionValue;
import tech.drainwalk.events.UpdateEvent;

public class FullBrightModule extends Module {

    private final SelectOption typeCombo = new SelectOption("Тип", 0,
            new SelectOptionValue("ГАММА НА ТЫЩУ"),
            new SelectOptionValue("ЗЕЛЬЕ"));

    public FullBrightModule() {
        super("Убрать ебучую ночь", Category.RENDER);
        addType(Type.SECONDARY);
        register(
                typeCombo
        );
    }

    @EventTarget
    public void onUpdate(UpdateEvent updateEvent) {
        if (typeCombo.getValueByIndex(0)) {
            mc.gameSettings.gammaSetting = 1000;
        } else {
            mc.gameSettings.gammaSetting = 0;
        }
        if (typeCombo.getValueByIndex(1)) {
            mc.player.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 999999999, 1));
        } else {
            mc.player.removePotionEffect(MobEffects.NIGHT_VISION);
        }
    }

    @Override
    public void onDisable() {
        mc.gameSettings.gammaSetting = 0;
        mc.player.removePotionEffect(MobEffects.NIGHT_VISION);
        super.onDisable();
    }
}
