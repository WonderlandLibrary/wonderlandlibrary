// 
// Decompiled by Procyon v0.5.36
// 

package Core;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

public class Engine
{
    private static boolean initGL;
    private static int width;
    private static int height;
    
    static {
        Engine.initGL = false;
        Engine.width = 0;
        Engine.height = 0;
    }
    
    public static boolean isInit() {
        return Engine.initGL;
    }
    
    public static int getWidth() {
        return Engine.width;
    }
    
    public static int getHeight() {
        return Engine.height;
    }
    
    public static void initGL(final int W, final int H) {
        if (!Engine.initGL) {
            try {
                System.setProperty("org.lwjgl.opengl.Window.undecorated", "true");
                Display.setDisplayMode(new DisplayMode(W, H));
                Display.create();
                Display.setLocation(0, 5000);
                Engine.width = W;
                Engine.height = H;
                Engine.initGL = true;
            }
            catch (Exception e) {
                e.printStackTrace();
                System.exit(0);
            }
        }
    }
}
