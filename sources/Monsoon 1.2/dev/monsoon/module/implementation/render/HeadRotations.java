package dev.monsoon.module.implementation.render;

import org.lwjgl.input.Keyboard;

import dev.monsoon.event.Event;
import dev.monsoon.event.listeners.EventMotion;
import dev.monsoon.module.base.Module;
import dev.monsoon.util.misc.Timer;
import dev.monsoon.module.enums.Category;



public class HeadRotations extends Module {
    public Timer timer = new Timer();

    @Override
    public void onEvent(Event event) {
        if (event instanceof EventMotion && event.isPre()) {
            this.mc.thePlayer.rotationYawHead = ((EventMotion)event).getYaw();
            this.mc.thePlayer.renderYawOffset = ((EventMotion)event).getYaw();
        }
    }

    public HeadRotations() {
        super("HeadRotations", Keyboard.KEY_NONE, Category.RENDER);
    }

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
    }
}