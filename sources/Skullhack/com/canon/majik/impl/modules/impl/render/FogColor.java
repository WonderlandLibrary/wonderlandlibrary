package com.canon.majik.impl.modules.impl.render;

import com.canon.majik.api.utils.client.TColor;
import com.canon.majik.impl.modules.api.Category;
import com.canon.majik.impl.modules.api.Module;
import com.canon.majik.impl.setting.settings.ColorSetting;
import com.canon.majik.impl.setting.settings.NumberSetting;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class FogColor extends Module {

    ColorSetting color = setting("Color", new TColor(255));
    NumberSetting density = setting("Density", 0, 0, 100);

    public FogColor(String name, Category category) {
        super(name, category);
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @SubscribeEvent
    public void onRender(EntityViewRenderEvent.FogColors event){
        event.setRed(color.getValue().getRed());
        event.setGreen(color.getValue().getGreen());
        event.setBlue(color.getValue().getBlue());
    }

    @SubscribeEvent
    public void onDensity(EntityViewRenderEvent.FogDensity event){
        event.setDensity(density.getValue().floatValue());
    }
}
