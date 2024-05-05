package vip.radium.module.impl.visuals;

import vip.radium.module.Module;
import vip.radium.module.ModuleCategory;
import vip.radium.module.ModuleInfo;
import vip.radium.module.ModuleManager;
import vip.radium.property.Property;
import vip.radium.utils.render.Colors;

@ModuleInfo(label = "Block Outline", category = ModuleCategory.VISUALS)
public final class BlockOutline extends Module {

    private final Property<Integer> blockOutlineColorProperty = new Property<>("Outline", Colors.PURPLE);

    private static BlockOutline instance;

    private static void lazyInit() {
        if (instance == null) {
            instance = ModuleManager.getInstance(BlockOutline.class);
        }
    }

    @Override
    public void onEnable() {
        lazyInit();
    }

    @Override
    public boolean isEnabled() {
        lazyInit();
        return super.isEnabled();
    }

    public static float getOutlineAlpha() {
        return (instance.blockOutlineColorProperty.getValue() >> 25 & 0xFF) / 255.0F;
    }

    public static int getOutlineColor() {
        return instance.blockOutlineColorProperty.getValue();
    }

    public static boolean isOutlineActive() {
        return instance.isEnabled();
    }

}
