package me.aquavit.liquidsense.module.modules.render;

import me.aquavit.liquidsense.LiquidSense;
import me.aquavit.liquidsense.module.Module;
import me.aquavit.liquidsense.module.ModuleCategory;
import me.aquavit.liquidsense.module.ModuleInfo;
import me.aquavit.liquidsense.utils.timer.MSTimer;
import me.aquavit.liquidsense.value.FloatValue;
import me.aquavit.liquidsense.value.ListValue;
import net.minecraft.client.renderer.GlStateManager;


@ModuleInfo(name = "ItemRotate", description = "ItemRotate", category = ModuleCategory.RENDER)
public class ItemRotate extends Module {
    private final ListValue modeValue = new ListValue("Mode", new String[]{"Straight", "Forward", "Nano", "Uh"}, "Straight");
    private final FloatValue RotateSpeedValue = new FloatValue("RotateSpeed", 8F, 1F, 15F);
    private static float delay = 0;

    public FloatValue getValue() {
        return RotateSpeedValue;
    }

    public ListValue getModeValue() {
        return modeValue;
    }

    public static void ItemRenderRotate() {
        MSTimer rotationTimer = new MSTimer();
        final ItemRotate ItemRotate = (ItemRotate) LiquidSense.moduleManager.getModule(ItemRotate.class);
        if(ItemRotate.getModeValue().get().equalsIgnoreCase("Straight")){
            GlStateManager.rotate(delay, 0.0F, 1.0F, 0.0F);
        }
        if(ItemRotate.getModeValue().get().equalsIgnoreCase("Forward")){
            GlStateManager.rotate(delay, 1.0F, 1.0F, 0.0F);
        }
        if(ItemRotate.getModeValue().get().equalsIgnoreCase("Nano")){
            GlStateManager.rotate(delay, 0.0F, 0.0F, 0.0F);
        }
        if(ItemRotate.getModeValue().get().equalsIgnoreCase("Uh")){
            GlStateManager.rotate(delay, 1.0F, 0.0F, 1.0F);
        }

        if (rotationTimer.hasTimePassed(1L)) {
            ++delay;
            delay = delay + ItemRotate.getValue().get();
            rotationTimer.reset();
        }
        if (delay > 360.0F) {
            delay = 0.0F;
        }
    }

    @Override
    public String getTag() {
        return modeValue.get();
    }

}
