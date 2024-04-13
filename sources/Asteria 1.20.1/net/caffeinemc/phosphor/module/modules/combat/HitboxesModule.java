package net.caffeinemc.phosphor.module.modules.combat;

import net.caffeinemc.phosphor.api.util.MathUtils;
import net.caffeinemc.phosphor.module.Module;
import net.caffeinemc.phosphor.module.setting.settings.BooleanSetting;
import net.caffeinemc.phosphor.module.setting.settings.ModeSetting;
import net.caffeinemc.phosphor.module.setting.settings.NumberSetting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;
import net.minecraft.item.SwordItem;

public class HitboxesModule extends Module {
    private final NumberSetting expand = new NumberSetting("Expansion", this, 0, 0, 1, 0.1);
    private final BooleanSetting render = new BooleanSetting("Enable Render", this, true);
    private final ModeSetting mode = new ModeSetting("Mode", this, "Only Players", "All Entities", "Only Players");
    private final ModeSetting itemWhitelist = new ModeSetting("Item Whitelist", this, "Only Weapon", "All Items", "Only Weapon");
    private final NumberSetting hitboxChance = new NumberSetting("Hitbox Chance", this, 100d, 0d, 100d, 1d);

    public HitboxesModule() {
        super("Hitboxes", "Expands hitboxes", Category.COMBAT);
    }

    public float getHitboxSize(Entity entity) {
        switch (mode.getMode()) {
            case "Only Players" -> {
                if (!(entity instanceof PlayerEntity))
                    return 0;
            }
        }

        switch (itemWhitelist.getMode()) {
            case "Only Weapon" -> {
                Item mainHandItem = mc.player.getMainHandStack().getItem();
                if (!(mainHandItem instanceof SwordItem || mainHandItem instanceof AxeItem))
                    return 0;
            }
        }

        int randomNum = MathUtils.getRandomInt(1, 100);

        if (randomNum <= hitboxChance.getIValue())
            return expand.getFValue();

        return 0;
    }

    public double getRenderHitboxSize() {
        if (!render.isEnabled())
            return 0;

        return expand.getValue();
    }
}
