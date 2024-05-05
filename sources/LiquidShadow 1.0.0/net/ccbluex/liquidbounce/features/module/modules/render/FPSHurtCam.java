package net.ccbluex.liquidbounce.features.module.modules.render;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Render2DEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;

import java.awt.*;

@ModuleInfo(name = "FPSHurtCam",description = "Let you have a FPS hurt cam when you get hurt.",category = ModuleCategory.RENDER)
public class FPSHurtCam extends Module {
    private final IntegerValue rValue = new IntegerValue("R",255,0,255);
    private final IntegerValue gValue = new IntegerValue("G",80,0,255);
    private final IntegerValue bValue = new IntegerValue("B",80,0,255);
    private final IntegerValue alphaValue = new IntegerValue("Alpha",100,0,255);
    private final FloatValue heightValue = new FloatValue("Height",35,0,50);

    @EventTarget
    public void onRender2D(Render2DEvent render2DEvent) {
        if (mc.thePlayer.hurtTime > 0) {
            ScaledResolution scaledResolution = new ScaledResolution(mc);
            int width = scaledResolution.getScaledWidth();
            int height = scaledResolution.getScaledHeight();;
            RenderUtils.drawRect(0,0,width,heightValue.get(),new Color(rValue.get(),gValue.get(),bValue.get(),alphaValue.get()).getRGB());
            GL11.glColor4f(1,1,1,1);
        }
    }
}
