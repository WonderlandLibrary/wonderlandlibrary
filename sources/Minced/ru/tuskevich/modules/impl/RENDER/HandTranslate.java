// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.modules.impl.RENDER;

import ru.tuskevich.ui.dropui.setting.Setting;
import ru.tuskevich.ui.dropui.setting.imp.BooleanSetting;
import ru.tuskevich.ui.dropui.setting.imp.SliderSetting;
import ru.tuskevich.ui.dropui.setting.imp.ModeSetting;
import ru.tuskevich.modules.Type;
import ru.tuskevich.modules.ModuleAnnotation;
import ru.tuskevich.modules.Module;

@ModuleAnnotation(name = "HandTranslate", desc = "\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd\ufffd\ufffd\ufffd, \ufffd \ufffd\ufffd\ufffd\ufffd\ufffd \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd", type = Type.RENDER)
public class HandTranslate extends Module
{
    public static ModeSetting swordAnim;
    public static SliderSetting right_x;
    public static SliderSetting right_y;
    public static SliderSetting right_z;
    public static SliderSetting left_x;
    public static SliderSetting left_y;
    public static SliderSetting left_z;
    public static BooleanSetting slowess;
    public static SliderSetting swipePower;
    
    public HandTranslate() {
        this.add(HandTranslate.swordAnim, HandTranslate.swipePower, HandTranslate.slowess, HandTranslate.right_x, HandTranslate.right_y, HandTranslate.right_z, HandTranslate.left_x, HandTranslate.left_y, HandTranslate.left_z);
    }
    
    static {
        HandTranslate.swordAnim = new ModeSetting("Mode", "Type 1", new String[] { "Type 1", "Type 2", "Type 3", "Type 4" });
        HandTranslate.right_x = new SliderSetting("RightX", 0.0f, -2.0f, 2.0f, 0.1f);
        HandTranslate.right_y = new SliderSetting("RightY", 0.0f, -2.0f, 2.0f, 0.1f);
        HandTranslate.right_z = new SliderSetting("RightZ", 0.0f, -2.0f, 2.0f, 0.1f);
        HandTranslate.left_x = new SliderSetting("LeftX", 0.0f, -2.0f, 2.0f, 0.1f);
        HandTranslate.left_y = new SliderSetting("LeftY", 0.0f, -2.0f, 2.0f, 0.1f);
        HandTranslate.left_z = new SliderSetting("LeftZ", 0.0f, -2.0f, 2.0f, 0.1f);
        HandTranslate.slowess = new BooleanSetting("Slow Animation", false);
        HandTranslate.swipePower = new SliderSetting("Swipe Power", 8.0f, 1.0f, 10.0f, 1.0f, () -> HandTranslate.swordAnim.is("Type 2"));
    }
}
