package dev.rise.module.impl.render;

import dev.rise.module.Module;
import dev.rise.module.api.ModuleInfo;
import dev.rise.module.enums.Category;
import dev.rise.setting.impl.BooleanSetting;
import dev.rise.setting.impl.NumberSetting;
import store.intent.intentguard.annotation.Exclude;
import store.intent.intentguard.annotation.Strategy;

@Exclude({Strategy.NUMBER_OBFUSCATION, Strategy.FLOW_OBFUSCATION})
@ModuleInfo(name = "PopOutAnimation", description = "Gives Guis an animation", category = Category.RENDER)
public class PopOutAnimation extends Module {
    private final BooleanSetting clickGui = new BooleanSetting("ClickGui", this, true);
    public static boolean clickGuiValue;
    private final BooleanSetting inventories = new BooleanSetting("Inventories", this, true);
    public static boolean inventoriesValue;

    private final NumberSetting startingSize = new NumberSetting("Starting Size", this, 0.1, 0, 1, 0.01);
    public static float startingSizeValue;
    private final NumberSetting speed = new NumberSetting("Speed", this, 0.1, 0.01, 1, 0.01);
    public static float speedValue;

    @Override
    public void onUpdateAlwaysInGui() {
        super.onUpdateAlwaysInGui();

        clickGuiValue = clickGui.isEnabled();
        inventoriesValue = inventories.isEnabled();
        startingSizeValue = (float) startingSize.getValue();
        speedValue = (float) speed.getValue();
    }
}
