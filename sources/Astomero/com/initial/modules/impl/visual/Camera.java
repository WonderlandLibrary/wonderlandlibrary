package com.initial.modules.impl.visual;

import com.initial.settings.impl.*;
import com.initial.modules.*;
import com.initial.settings.*;

public class Camera extends Module
{
    public static DoubleSet x;
    public static DoubleSet y;
    public static DoubleSet z;
    public static DoubleSet zoom;
    public static DoubleSet scale;
    
    public Camera() {
        super("Camera", 0, Category.VISUAL);
        this.addSettings(Camera.x, Camera.y, Camera.z, Camera.zoom, Camera.scale);
    }
    
    static {
        Camera.x = new DoubleSet("X", 1.0, -1.0, 2.0, 0.1);
        Camera.y = new DoubleSet("Y", 0.0, -1.0, 1.0, 0.1);
        Camera.z = new DoubleSet("Z", 0.0, -1.0, 1.0, 0.1);
        Camera.zoom = new DoubleSet("Zoom", 0.0, -2.0, 2.0, 0.1);
        Camera.scale = new DoubleSet("Scale", 0.9, -4.0, 4.0, 0.1);
    }
}
