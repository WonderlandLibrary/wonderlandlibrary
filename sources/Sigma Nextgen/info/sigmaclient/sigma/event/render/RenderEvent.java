package info.sigmaclient.sigma.event.render;

import info.sigmaclient.sigma.gui.font.JelloFontUtil;
import info.sigmaclient.sigma.modules.gui.Shader;
import info.sigmaclient.sigma.sigma5.SelfDestructManager;
import info.sigmaclient.sigma.utils.render.RenderUtils;
import info.sigmaclient.sigma.utils.render.rendermanagers.GlStateManager;
import info.sigmaclient.sigma.utils.render.rendermanagers.ScaledResolution;
import info.sigmaclient.sigma.SigmaNG;
import info.sigmaclient.sigma.event.Event;
import info.sigmaclient.sigma.event.defaultevent.Render;
import info.sigmaclient.sigma.modules.gui.TabGUI;
import info.sigmaclient.sigma.utils.TimerUtil;
import info.sigmaclient.sigma.gui.hud.JelloTabGUI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureUtil;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.awt.*;
import java.nio.IntBuffer;

import static info.sigmaclient.sigma.modules.Module.mc;

public class RenderEvent extends Event {
    public float renderTime;
    private static TimerUtil timerUtil = new TimerUtil();
    public RenderEvent(float renderTime){
        this.eventID = 1;
        GlStateManager.color(1,1,1,1);
        if(SelfDestructManager.destruct) return;
        Render.render();
        GlStateManager.disableLighting();
        GlStateManager.enableTexture2D();
        RenderUtils.startBlend();
        GlStateManager.disableAlpha();
        GlStateManager.enableBlend();
        RenderUtils.startBlend();
        JelloFontUtil.jelloFont16.drawCenteredString("", 0, 0, 0);

        GlStateManager.color(1,1,1,1);
        if(SigmaNG.getSigmaNG().moduleManager.getModule(TabGUI.class).enabled && !Shader.isEnable()){
            if(timerUtil.hasTimeElapsed(100)){
                if(mc.gameSettings.ofFastRender) {
                    JelloTabGUI.colorTop = new Color(0, 0, 0).getRGB();
                    JelloTabGUI.colorTopRight = new Color(0, 0, 0).getRGB();

                    JelloTabGUI.colorBottom = new Color(0, 0, 0).getRGB();
                    JelloTabGUI.colorBottomRight = new Color(0, 0, 0).getRGB();

                    JelloTabGUI.colorNotification = new Color(0, 0, 0).getRGB();
                    JelloTabGUI.colorNotificationBottom = new Color(0, 0, 0).getRGB();
                    return;
                }
                ScaledResolution sr = new ScaledResolution(Minecraft.getInstance());
                int p_148259_2_ = 0, p_148259_3_ = 0;
                IntBuffer pixelBuffer;
                int[] pixelValues;

//                if (OpenGlHelper.isFramebufferEnabled()) {
                    p_148259_2_ = 180;
                    p_148259_3_ = 280;
//                }
                int var6 = p_148259_2_ * p_148259_3_;

                pixelBuffer = BufferUtils.createIntBuffer(var6);
                pixelValues = new int[var6];

                GL11.glPixelStorei(GL11.GL_PACK_ALIGNMENT, 1);
                GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
                pixelBuffer.clear();

                GL11.glReadPixels(0, sr.getScaledHeight()- (p_148259_3_-sr.getScaledHeight())/*728*/, p_148259_2_, p_148259_3_, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, pixelBuffer);

                pixelBuffer.get(pixelValues);
                TextureUtil.func_147953_a(pixelValues, p_148259_2_, p_148259_3_);

                try {
                    JelloTabGUI.colorTop = pixelValues[(45 * sr.getScaleFactor()) * p_148259_2_ + 10];
                    JelloTabGUI.colorTopRight = pixelValues[(45 * sr.getScaleFactor()) * p_148259_2_ + 130];

                    JelloTabGUI.colorBottom = pixelValues[((45 + 77) * sr.getScaleFactor()) * p_148259_2_ + 10];
                    JelloTabGUI.colorBottomRight = pixelValues[((45 + 77) * sr.getScaleFactor()) * p_148259_2_ + 130];

                    JelloTabGUI.colorNotification = pixelValues[(10) * p_148259_2_ + 270];
                    JelloTabGUI.colorNotificationBottom = pixelValues[(77) * p_148259_2_ + 270];

                }catch (ArrayIndexOutOfBoundsException e){
                    JelloTabGUI.colorTop = new Color(0, 0, 0).getRGB();
                    JelloTabGUI.colorTopRight = new Color(0, 0, 0).getRGB();

                    JelloTabGUI.colorBottom = new Color(0, 0, 0).getRGB();
                    JelloTabGUI.colorBottomRight = new Color(0, 0, 0).getRGB();

                    JelloTabGUI.colorNotification = new Color(0, 0, 0).getRGB();
                    JelloTabGUI.colorNotificationBottom = new Color(0, 0, 0).getRGB();
                }
                timerUtil.reset();
            }
        }

        this.renderTime = renderTime;
        GlStateManager.color(1,1,1,1);
    }
}
