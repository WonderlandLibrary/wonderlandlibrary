package wtf.evolution.module.impl.Render;

import wtf.evolution.module.Category;
import wtf.evolution.module.Module;
import wtf.evolution.module.ModuleInfo;
import wtf.evolution.settings.options.ColorSetting;

import java.awt.*;
@ModuleInfo(name = "EnchantmentColor", type = Category.Render)
public class EnchantmentColor extends Module {
    public ColorSetting enchantColor = new ColorSetting("Color", new Color(120, 210, 210).getRGB()).call(this);
}
