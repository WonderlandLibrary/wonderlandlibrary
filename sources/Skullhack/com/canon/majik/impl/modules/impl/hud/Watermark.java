package com.canon.majik.impl.modules.impl.hud;

import com.canon.majik.api.core.Initializer;
import com.canon.majik.api.event.eventBus.EventListener;
import com.canon.majik.api.event.events.Render2DEvent;
import com.canon.majik.api.utils.Globals;
import com.canon.majik.api.utils.client.TColor;
import com.canon.majik.impl.modules.api.Module;
import com.canon.majik.impl.setting.settings.ColorSetting;

public class Watermark extends Module {

    ColorSetting color = setting("Color", new TColor(255,0,0));

    public Watermark(String name) {
        super(name);
    }

    @EventListener
    public void onRender(Render2DEvent event){
        if(nullCheck()) return;
        Initializer.CFont.drawString(Globals.name, x.getValue().intValue(), y.getValue().intValue(), color.getValue().getRGB());
    }
}
