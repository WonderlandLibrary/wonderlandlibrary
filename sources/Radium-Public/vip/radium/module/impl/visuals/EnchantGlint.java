package vip.radium.module.impl.visuals;

import vip.radium.module.Module;
import vip.radium.module.ModuleCategory;
import vip.radium.module.ModuleInfo;
import vip.radium.module.ModuleManager;
import vip.radium.property.Property;
import vip.radium.utils.render.Colors;

@ModuleInfo(label = "Enchant Glint", category = ModuleCategory.VISUALS)
public final class EnchantGlint extends Module {

    private final Property<Integer> itemColorProperty = new Property<>("Item Glint", Colors.RED);
    private final Property<Integer> armorModelColorProperty = new Property<>("Armor Glint", Colors.RED);

    private static EnchantGlint getInstance() {
        return ModuleManager.getInstance(EnchantGlint.class);
    }

    public static boolean isGlintEnabled() {
        return getInstance().isEnabled();
    }

    public static int getItemColor() {
        return getInstance().itemColorProperty.getValue();
    }

    public static int getArmorModelColor() {
        return getInstance().armorModelColorProperty.getValue();
    }
}
