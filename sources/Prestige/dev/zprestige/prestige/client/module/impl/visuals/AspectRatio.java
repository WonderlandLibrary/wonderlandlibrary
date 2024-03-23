package dev.zprestige.prestige.client.module.impl.visuals;

import dev.zprestige.prestige.client.event.EventListener;
import dev.zprestige.prestige.client.event.impl.Render2DEvent;
import dev.zprestige.prestige.client.module.Category;
import dev.zprestige.prestige.client.module.Module;
import dev.zprestige.prestige.client.setting.impl.ModeSetting;
import org.lwjgl.glfw.GLFW;

public class AspectRatio extends Module {
    public ModeSetting mode;

    public AspectRatio() {
        super("AspectRatio", Category.Visual, "Changes the aspect ratio of the game");
        mode = setting("Ratio", "1720", new String[]{"1800", "1720", "1600", "1440", "1300"});
    }

    @Override
    public void onDisable() {
        GLFW.glfwSetWindowSize(getMc().getWindow().getHandle(), 1920, getMc().getWindow().getHeight());
    }

    @EventListener
    public void event(Render2DEvent event) {
        GLFW.glfwSetWindowSize(getMc().getWindow().getHandle(), Integer.parseInt(mode.getObject()), getMc().getWindow().getHeight());
    }
}
