package info.sigmaclient.sigma.modules.gui;


import com.google.common.collect.Lists;
import info.sigmaclient.sigma.utils.render.rendermanagers.GlStateManager;
import info.sigmaclient.sigma.utils.render.rendermanagers.ScaledResolution;
import info.sigmaclient.sigma.SigmaNG;
import info.sigmaclient.sigma.event.render.RenderEvent;
import info.sigmaclient.sigma.modules.Category;
import info.sigmaclient.sigma.modules.Module;
import info.sigmaclient.sigma.utils.font.CustomFont;
import info.sigmaclient.sigma.utils.font.FontUtil;
import info.sigmaclient.sigma.utils.render.RenderUtils;
import info.sigmaclient.sigma.gui.font.JelloFontUtil;
import info.sigmaclient.sigma.utils.render.StencilUtil;
import info.sigmaclient.sigma.utils.render.anims.PartialTicksAnim;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.List;

import static net.minecraft.client.gui.AbstractGui.drawModalRectWithCustomSizedTexture;
import top.fl0wowp4rty.phantomshield.annotations.Native;


public class Compass extends Module {
    public Compass() {
        super("Compass", Category.Gui, "Jello compass");
    }

    public static class Degree {

        public String text;
        public int type;

        public Degree(String s, int t){
            text = s;
            type = t;
        }

    }
    public CompassHUD compass = new CompassHUD(325, 325, 1, 2, true);
    public static class CompassHUD {

        public float innerWidth;
        public float outerWidth;
        public boolean shadow;
        public float scale;
        public int accuracy;

        public List<Degree> degrees = Lists.newArrayList();

        public CompassHUD(float i, float o, float s, int a, boolean sh){
            innerWidth = i;
            outerWidth = o;
            scale = s;
            accuracy = a;
            shadow = sh;


            degrees.add(new Degree("N", 1));
            degrees.add(new Degree("195", 2));
            degrees.add(new Degree("210", 2));
            degrees.add(new Degree("NE", 3));
            degrees.add(new Degree("240", 2));
            degrees.add(new Degree("255", 2));
            degrees.add(new Degree("E", 1));
            degrees.add(new Degree("285", 2));
            degrees.add(new Degree("300", 2));
            degrees.add(new Degree("SE", 3));
            degrees.add(new Degree("330", 2));
            degrees.add(new Degree("345", 2));
            degrees.add(new Degree("S", 1));
            degrees.add(new Degree("15", 2));
            degrees.add(new Degree("30", 2));
            degrees.add(new Degree("SW", 3));
            degrees.add(new Degree("60", 2));
            degrees.add(new Degree("75", 2));
            degrees.add(new Degree("W", 1));
            degrees.add(new Degree("105", 2));
            degrees.add(new Degree("120", 2));
            degrees.add(new Degree("NW", 3));
            degrees.add(new Degree("150", 2));
            degrees.add(new Degree("165", 2));
        }

        private void drawTexture(float x, float y, float w, float h, float w2, float h2) {
            drawModalRectWithCustomSizedTexture(x, y, 0, 0, w, h, w2, h2);
        }
        public void draw(ScaledResolution sr) {
            if (shadow) {
                final boolean enableBlend = GL11.glIsEnabled(3042);
                final boolean disableAlpha = !GL11.glIsEnabled(3008);
                if (!enableBlend) {
                    GL11.glEnable(3042);
                }
                if (!disableAlpha) {
                    GL11.glDisable(3008);
                }
                GlStateManager.color(1, 1, 1, 1F);
                Minecraft.getInstance().getTextureManager().bindTexture(new ResourceLocation("sigma/shadow.png"));
                drawTexture((int) (sr.getScaledWidth() / 2 - 325.5f / 2), -8, (int) 325.5f, (int) 76.5D + 10f, 325.5F, 76.5F + 10f);
                if (!enableBlend) {
                    GL11.glDisable(3042);
                }
                if (!disableAlpha) {
                    GL11.glEnable(3008);
                }
            }

            float center = sr.getScaledWidth() / 2f;

            int count = 0;
            float yaaahhrewindTime = (Minecraft.getInstance().player.rotationYaw % 360) * 2 + 360 * 3;

            for (Degree d : degrees) {

                float location = center + (count * 30) - yaaahhrewindTime;
                float completeLocation = (float) (d.type == 1 ? (location - JelloFontUtil.jelloFontBig.getStringWidth(d.text) / 2) : d.type == 2 ? (location - JelloFontUtil.jelloFontMarker.getStringWidth(d.text) / 2) : (location - JelloFontUtil.jelloFontMedium.getStringWidth(d.text) / 2));

                int opacity = opacity(sr, completeLocation);

                if (d.type == 1 && opacity != 16777215) {
                    GlStateManager.color(1, 1, 1, 1);
                    JelloFontUtil.jelloFontBig.drawString(d.text, completeLocation, -75 + 100 - 2.5f, opacity(sr, completeLocation));
                }

                if (d.type == 2 && opacity != 16777215) {
                    GlStateManager.color(1, 1, 1, 1);
                    RenderUtils.drawRect(location - 0.5f, -75 + 100 + 4, location + 0.5f, -75 + 105 + 4, opacity2(sr, completeLocation));
                    GlStateManager.color(1, 1, 1, 1);
                    JelloFontUtil.jelloFontMarker.drawString(d.text, completeLocation, -75 + 105 + 3.5f + 4, opacity(sr, completeLocation));
                }

                if (d.type == 3 && opacity != 16777215) {
                    GlStateManager.color(1, 1, 1, 1);
                    JelloFontUtil.jelloFontMedium.drawString(d.text, completeLocation, -75 + 100 +
                            JelloFontUtil.jelloFontBig.getHeight() / 2f - JelloFontUtil.jelloFontMedium.getHeight() / 2f - 2, opacity(sr, completeLocation));
                }

                count++;
            }
            for (Degree d : degrees) {

                float location = center + (count * 30) - yaaahhrewindTime;
                float completeLocation = (float) (d.type == 1 ? (location - JelloFontUtil.jelloFontBig.getStringWidth(d.text) / 2) : d.type == 2 ? (location - JelloFontUtil.jelloFontMarker.getStringWidth(d.text) / 2) : (location - JelloFontUtil.jelloFontMedium.getStringWidth(d.text) / 2));

                if (d.type == 1) {
                    GlStateManager.color(1, 1, 1, 1);
                    JelloFontUtil.jelloFontBig.drawString(d.text, completeLocation, -75 + 100 - 2.5f, opacity(sr, completeLocation));
                }

                if (d.type == 2) {
                    GlStateManager.color(1, 1, 1, 1);
                    RenderUtils.drawRect(location - 0.5f, -75 + 100 + 4, location + 0.5f, -75 + 105 + 4, opacity2(sr, completeLocation));
                    GlStateManager.color(1, 1, 1, 1);
                    JelloFontUtil.jelloFontMarker.drawString(d.text, completeLocation, -75 + 105 + 3.5f + 4, opacity(sr, completeLocation));
                }

                if (d.type == 3) {
                    GlStateManager.color(1, 1, 1, 1);
                    JelloFontUtil.jelloFontMedium.drawString(d.text, completeLocation, -75 + 100 +
                            JelloFontUtil.jelloFontBig.getHeight() / 2f - JelloFontUtil.jelloFontMedium.getHeight() / 2f - 2, opacity(sr, completeLocation));
                }

                count++;
            }
            for (Degree d : degrees) {

                float location = center + (count * 30) - yaaahhrewindTime;
                float completeLocation = (float) (d.type == 1 ? (location - JelloFontUtil.jelloFontBig.getStringWidth(d.text) / 2) : d.type == 2 ? (location - JelloFontUtil.jelloFontMarker.getStringWidth(d.text) / 2) : (location - JelloFontUtil.jelloFontMedium.getStringWidth(d.text) / 2));

                if (d.type == 1) {
                    GlStateManager.color(1, 1, 1, 1);
                    JelloFontUtil.jelloFontBig.drawString(d.text, completeLocation, -75 + 100 - 2.5f, opacity(sr, completeLocation));
                }

                if (d.type == 2) {
                    GlStateManager.color(1, 1, 1, 1);
                    RenderUtils.drawRect(location - 0.5f, -75 + 100 + 4, location + 0.5f, -75 + 105 + 4, opacity2(sr, completeLocation));
                    GlStateManager.color(1, 1, 1, 1);
                    JelloFontUtil.jelloFontMarker.drawString(d.text, completeLocation, -75 + 105 + 3.5f + 4, opacity(sr, completeLocation));
                }

                if (d.type == 3) {
                    GlStateManager.color(1, 1, 1, 1);
                    JelloFontUtil.jelloFontMedium.drawString(d.text, completeLocation, -75 + 100 +
                            JelloFontUtil.jelloFontBig.getHeight() / 2f - JelloFontUtil.jelloFontMedium.getHeight() / 2f - 2, opacity(sr, completeLocation));
                }

                count++;
            }

        }

        public int opacity(ScaledResolution sr, float offset){
            float offs = 255-Math.abs(sr.getScaledWidth()/2f - offset) * 1.4f;
            Color c = new Color(255, 255, 255, (int)Math.min(Math.max(0, offs), 200));

            return c.getRGB();
        }
        public int opacity2(ScaledResolution sr, float offset){
            float offs = 255-Math.abs(sr.getScaledWidth()/2f - offset) * 1.8f;
            Color c = new Color(255, 255, 255, (int)Math.min(Math.max(0, offs), 200) / 2);

            return c.getRGB();
        }


    }
    PartialTicksAnim inter = new PartialTicksAnim(0);
    @Override
    public void onRenderEvent(RenderEvent event) {
        if(SigmaNG.getSigmaNG().gameMode == SigmaNG.GAME_MODE.SIGMA) {
            compass.draw(new ScaledResolution(mc));
         }else{
            ScaledResolution sr = new ScaledResolution(mc);
            float yaw = (Minecraft.getInstance().player.rotationYaw % 360) * 2 + 360 * 3;
//            inter.interpolate(yaw, 5);
//            yaw = inter.getValue();
            int count = 10, c = 0, w = 40;
            float yOff = 30, x = sr.getScaledWidth() / 2f - count / 2f * w;
//            RenderUtils.drawRect(sr.getScaledWidth() / 2f - 1, yOff + 7, sr.getScaledWidth() / 2f + 1, yOff + 14, ColorChanger.getColor(10, 10).getRGB());
//            Shader.addBloom(()->RenderUtils.drawRect(x - 3, yOff - 3, x + count * w, yOff + 15, ColorChanger.getColor(10, 10).getRGB()));
            StencilUtil.initStencilToWrite();
            RenderUtils.drawRect(x - 3, yOff - 3, x + count * w, yOff + 15, -1);
            StencilUtil.readStencilBuffer(1);
            for(float i = 0; i < 360 * 3; i += 10){
                int k = (int) i - 360;
                if(k >= 360){
                    k = 720 - k;
                }
                if(k <= 0){
                    k = -k;
                }
                float xx = x + count / 2f * w + c * w - yaw;
                String kk = k + "";
                CustomFont f = FontUtil.sfuiFont24;
                if(k == 90){
                    f = FontUtil.sfuiFont32;
                    kk = "W";
                }else if(k == 180){
                    f = FontUtil.sfuiFont32;
                    kk = "N";
                }else if(k == 270){
                    f = FontUtil.sfuiFont32;
                    kk = "E";
                }else if(k % 360 == 0){
                    f = FontUtil.sfuiFont32;
                    kk = "S";
                }
                int al = (int)(Math.max(230 - Math.min(Math.abs(xx - sr.getScaledWidth() / 2f), 230), 0));
                f.drawCenteredString(kk, xx, yOff, new Color(255, 255, 255, al).getRGB());
                c++;
            }
            StencilUtil.uninitStencilBuffer();
        }
    }

}
