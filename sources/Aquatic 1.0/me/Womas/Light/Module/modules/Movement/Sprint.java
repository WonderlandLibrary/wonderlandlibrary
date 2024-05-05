package me.Womas.Light.Module.modules.Movement;

import me.Womas.Light.Module.Category;
import me.Womas.Light.Module.Template;
import org.lwjgl.input.Keyboard;

public class Sprint extends Template {
    public Sprint() {
        super("Sprint", Keyboard.KEY_G, Category.MOVEMENT);
    }

    @Override
    public void onEnable() {

    }
}
