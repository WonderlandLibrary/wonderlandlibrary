// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.modules.impl.RENDER;

import java.awt.Color;
import ru.tuskevich.ui.dropui.setting.Setting;
import ru.tuskevich.ui.dropui.setting.imp.SliderSetting;
import ru.tuskevich.ui.dropui.setting.imp.ColorSetting;
import ru.tuskevich.modules.Type;
import ru.tuskevich.modules.ModuleAnnotation;
import ru.tuskevich.modules.Module;

@ModuleAnnotation(name = "CustomFog", type = Type.RENDER)
public class CustomFog extends Module
{
    public static ColorSetting customColor;
    public static SliderSetting distance;
    
    public CustomFog() {
        this.add(CustomFog.customColor, CustomFog.distance);
    }
    
    static {
        CustomFog.customColor = new ColorSetting("Custom Color", new Color(16777215).getRGB());
        CustomFog.distance = new SliderSetting("Fog Distance", 0.01f, 0.01f, 1.0f, 0.01f);
    }
}
