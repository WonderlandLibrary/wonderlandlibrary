package com.canon.majik.impl.modules.impl.render;

import com.canon.majik.impl.modules.api.Category;
import com.canon.majik.impl.modules.api.Module;
import com.canon.majik.impl.setting.settings.BooleanSetting;
import com.canon.majik.impl.setting.settings.NumberSetting;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumHand;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.client.event.RenderSpecificHandEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ViewModel extends Module {

    NumberSetting right_x = setting("RightX", 0, -2, 2);
    NumberSetting right_y = setting("RightY", 0, -2, 2);
    NumberSetting right_z = setting("RightZ", 0, -2, 2);

    NumberSetting left_x = setting("LeftX", 0, -2, 2);
    NumberSetting left_y = setting("LeftY", 0, -2, 2);
    NumberSetting left_z = setting("LeftZ", 0, -2, 2);

    BooleanSetting invisible = setting("InvisibleHands", false);
    NumberSetting fov = setting("FOV", 130, 0, 200);

    public ViewModel(String name, Category category) {
        super(name, category);
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @SubscribeEvent
    public void onView(RenderSpecificHandEvent event){
        if(invisible.getValue()){
            event.setCanceled(true);
        }
        if(event.getHand() == EnumHand.MAIN_HAND){
            GlStateManager.translate(right_x.getValue().floatValue(),right_y.getValue().floatValue(),right_z.getValue().floatValue());
        }else if(event.getHand() == EnumHand.OFF_HAND){
            GlStateManager.translate(left_x.getValue().floatValue(),left_y.getValue().floatValue(),left_z.getValue().floatValue());
        }
    }

    @SubscribeEvent
    public void onFov(EntityViewRenderEvent.FOVModifier event){
        event.setFOV(fov.getValue().floatValue());
    }
}
