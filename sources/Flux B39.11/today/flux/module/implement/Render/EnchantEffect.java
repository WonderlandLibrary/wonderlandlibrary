package today.flux.module.implement.Render;

import today.flux.module.Category;
import today.flux.module.Module;
import today.flux.module.value.ColorValue;
import today.flux.module.value.ModeValue;

import java.awt.*;

public class EnchantEffect extends Module {
    public EnchantEffect() {
        super("EnchantEffect", Category.Render, false);
    }

    public static ColorValue enchantEffectColours = new ColorValue("EnchantEffect", "Enchant Glint", Color.RED);
}
