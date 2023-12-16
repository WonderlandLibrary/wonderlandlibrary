package wtf.expensive.util.render;

import net.minecraft.client.Minecraft;

public class ScaleMath {

    public static Vec2i getMouse(int mouseX, int mouseY){
        return new Vec2i((int) (mouseX * Minecraft.getInstance().getMainWindow().getGuiScaleFactor() / 2), (int) (mouseY * Minecraft.getInstance().getMainWindow().getGuiScaleFactor() / 2));
    }

}