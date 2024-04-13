package com.canon.majik.impl.modules.impl.client;

import com.canon.majik.api.config.Config;
import com.canon.majik.api.event.eventBus.EventListener;
import com.canon.majik.api.event.events.TickEvent;
import com.canon.majik.api.utils.client.TColor;
import com.canon.majik.impl.modules.api.Category;
import com.canon.majik.impl.modules.api.Module;
import com.canon.majik.impl.setting.settings.BooleanSetting;
import com.canon.majik.impl.setting.settings.ColorSetting;
import com.canon.majik.impl.ui.clickgui.ClickGuiMain;
import org.lwjgl.input.Keyboard;

public class ClickGui extends Module {

    public ColorSetting color = setting("Color", new TColor(0, 102, 164));
    public BooleanSetting cfont = setting("CFont", true);

    public static ClickGui instance;

    public ClickGui(String name, int key, Category category) {
        super(name, key, category);
        instance = this;
    }

    @Override
    public void onEnable() {
        if(nullCheck()) return;

        mc.displayGuiScreen(new ClickGuiMain());
    }

    @EventListener
    public void onUpdate(TickEvent eventUpdate){
        if(nullCheck()) return;
        if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)){
            toggle();
            Config.saveConfig();
        }
    }
}
