package de.tired.util.render.shaderloader;

import de.tired.base.interfaces.IHook;
import de.tired.util.render.shaderloader.shader.list.BackGroundShader;
import de.tired.util.render.shaderloader.list.BlurShader;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;

public class ShaderRenderer implements IHook {

    private static BlurShader blurShader = new BlurShader();
    private static BackGroundShader backGroundShader;

    public ShaderRenderer() {
        backGroundShader = new BackGroundShader(0);
    }

    public static void renderBG() {
        final ScaledResolution sr = new ScaledResolution(MC);
        Gui.drawRect(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), new Color(30, 30, 30).getRGB());
    }

    public static void startBlur() {
        blurShader.startBlur();
    }

    public static void stopBlur() {
        blurShader.stopBlur();
    }

    public static void stopBlur(int radius) {
        blurShader.stopBlur();
    }


}
