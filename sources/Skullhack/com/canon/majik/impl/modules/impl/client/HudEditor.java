package com.canon.majik.impl.modules.impl.client;

import com.canon.majik.api.config.Config;
import com.canon.majik.api.event.eventBus.EventListener;
import com.canon.majik.api.event.events.TickEvent;
import com.canon.majik.impl.modules.api.Category;
import com.canon.majik.impl.modules.api.Module;
import com.canon.majik.impl.ui.clickgui.ClickGuiMain;
import org.lwjgl.input.Keyboard;

public class HudEditor extends Module {

    public static HudEditor instance;

    public HudEditor(String name, Category category) {
        super(name, category);
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
