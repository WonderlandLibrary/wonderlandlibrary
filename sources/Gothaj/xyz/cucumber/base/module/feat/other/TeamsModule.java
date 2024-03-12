package xyz.cucumber.base.module.feat.other;

import org.lwjgl.input.Keyboard;

import xyz.cucumber.base.module.ArrayPriority;
import xyz.cucumber.base.module.Category;
import xyz.cucumber.base.module.Mod;
import xyz.cucumber.base.module.ModuleInfo;

@ModuleInfo(category = Category.OTHER, description = "Disallows to attack teammates", name = "Teams", key = Keyboard.KEY_NONE, priority = ArrayPriority.LOW)

public class TeamsModule extends Mod {

}
