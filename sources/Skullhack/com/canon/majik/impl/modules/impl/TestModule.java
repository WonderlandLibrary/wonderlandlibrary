package com.canon.majik.impl.modules.impl;

import com.canon.majik.api.event.eventBus.EventListener;
import com.canon.majik.api.event.events.TickEvent;
import com.canon.majik.impl.modules.api.Category;
import com.canon.majik.impl.modules.api.Module;
import net.minecraft.util.text.TextComponentString;
import org.lwjgl.input.Keyboard;

public class TestModule extends Module {

    public TestModule() {
        super("TestModule", 0, Category.CLIENT);
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @EventListener
    public void onUpdate(TickEvent event){
        mc.player.sendMessage(new TextComponentString("test"));
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

}
