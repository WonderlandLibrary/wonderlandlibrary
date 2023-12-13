// 
// Decompiled by Procyon v0.5.36
// 

package dev.xulu.newgui.util;

import com.elementars.eclient.module.render.NewGui;
import java.awt.Color;

public class ColorUtil
{
    public static Color getClickGUIColor() {
        return new Color(NewGui.red.getValue(), NewGui.green.getValue(), NewGui.blue.getValue());
    }
}
