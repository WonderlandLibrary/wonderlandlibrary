// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.modules.impl.RENDER;

import ru.tuskevich.ui.dropui.setting.Setting;
import java.awt.Color;
import ru.tuskevich.ui.dropui.setting.imp.ColorSetting;
import ru.tuskevich.modules.Type;
import ru.tuskevich.modules.ModuleAnnotation;
import ru.tuskevich.modules.Module;

@ModuleAnnotation(name = "EnchantmentColor", type = Type.RENDER)
public class EnchantmentColor extends Module
{
    public ColorSetting enchantColor;
    
    public EnchantmentColor() {
        this.enchantColor = new ColorSetting("Color", new Color(120, 210, 210).getRGB());
        this.add(this.enchantColor);
    }
}
