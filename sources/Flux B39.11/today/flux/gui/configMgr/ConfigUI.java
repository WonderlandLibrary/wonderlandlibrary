package today.flux.gui.configMgr;


import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import today.flux.config.preset.PresetManager;
import today.flux.gui.AbstractGuiScreen;
import today.flux.gui.clickgui.classic.RenderUtil;
import today.flux.gui.fontRenderer.FontManager;
import today.flux.gui.other.SavePresetScreen;
import today.flux.module.implement.Render.Hud;
import today.flux.utility.AnimationUtils;
import today.flux.utility.ColorUtils;

import java.util.concurrent.CopyOnWriteArrayList;

public class ConfigUI {

    public today.flux.gui.crink.NewClickGUI cgui;
    public Minecraft mc = Minecraft.getMinecraft();
    public MouseInputHandler handler = new MouseInputHandler(0);
    public CopyOnWriteArrayList<ConfigSlot> slots = new CopyOnWriteArrayList<>();
    public CopyOnWriteArrayList<ConfigSlot> cloudConfig = new CopyOnWriteArrayList<ConfigSlot>();

    public String currentConfig = "Default";
    public boolean cursorInFrame = false;

    //滚动
    public float minY = -100;
    public float scrollY;
    public float scrollAnimation = 0;

    public float animation = 0;
    public boolean extended = false;

    public ConfigUI(today.flux.gui.crink.NewClickGUI parent) {
        this.cgui = parent;
        try {
            this.sync();
        } catch(Exception ignored) {
        }
    }

    public int backgroundColor = 0;
    public int fontColor = 0;

    public void init() {
        this.sync();
    }

    public void render(float mouseX, float mouseY, float partialTicks, AbstractGuiScreen parent) {

        float width = parent.curWidth;
        float height = parent.curHeight;
        float target = extended ? 100 : 0;

        this.animation = AnimationUtils.getAnimationState(this.animation, target, Math.max(10, (Math.abs(this.animation - (target))) * 40) * 0.3f);

        backgroundColor = Hud.isLightMode ? 0xffedebed : 0xff40444b;
        fontColor = Hud.isLightMode ? 0xff504f50 : 0xffcdcdcd;

        if (extended) {
            if (RenderUtil.isHovering(mouseX, mouseY, width - 95, height - 5 - animation, width - 5, height - 5)) {
                cursorInFrame = true;
                if (scrollY <= minY)
                    scrollY = minY;
                if (scrollY >= 0)
                    scrollY = 0;

                scrollAnimation = AnimationUtils.getAnimationState(scrollAnimation, scrollY, (float) (Math.max(10, (Math.abs(scrollAnimation - scrollY)) * 40) * 0.3));
                minY = 100;
            } else {
                cursorInFrame = false;
            }
        } else {
            cursorInFrame = false;
            scrollAnimation = 0;
            scrollY = 0;
        }

        RenderUtil.drawRect(width - 95, height - 25 - animation, width - 5f, height - 23 - animation, 0xff4286f5);

        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_SCISSOR_TEST);

        if (this.animation > 0) {
            parent.doGlScissor((int)width - 195, (int)height - 123, 190, 118);
        } else {
            parent.doGlScissor((int)width - 95, (int)height - 23, 90, 18);
        }

        RenderUtil.drawRect(width - 95, height - 23 - animation, width - 5, height - 5, backgroundColor);

        if (this.animation > 0) {
            float startY = height - animation + scrollAnimation + 20;
            float moduleYShouldBe = 0;

            if (this.slots.size() > 0) {
                for (ConfigSlot s : this.slots) {

                    if (startY > height - 115 && startY < height + 20) {
                        s.render(width - 95, startY);
                    }

                    moduleYShouldBe += 20;
                    startY += 20;
                }
            }

            if (this.cloudConfig.size() > 0) {
                for (ConfigSlot s : this.cloudConfig) {
                    if (startY > height - 115 && startY < height + 20) {
                        s.render(width - 95, startY);
                    }
                    moduleYShouldBe += 20;
                    startY += 20;
                }
            }

            minY -= moduleYShouldBe;
        }

        //蒙版
        RenderUtil.drawRect(width - 95, height - 23 - animation, width - 5, height - 5 - animation, backgroundColor);
        this.drawGradientRect(width - 95, height - 5 - animation, width - 5, height - animation, RenderUtil.reAlpha(ColorUtils.BLACK.c, 0.25f), RenderUtil.reAlpha(ColorUtils.BLACK.c, 0.0f));

        FontManager.sans16.drawString(this.currentConfig, width - 80, height - 20 - animation, fontColor);
        FontManager.icon14.drawString("v", width - 90, height - 18 - animation, fontColor);
        this.drawGradientSideways(width - 40, height - 23 - animation, width - 22, height - 5 - animation, RenderUtil.reAlpha(backgroundColor, 0f), backgroundColor);
        RenderUtil.drawRect(width - 22, height - 23 - animation, width - 5, height - 5 - animation, backgroundColor);

        //添加按钮
        FontManager.icon15.drawString("u", width - 18, height - 18 - animation, fontColor);

        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        GL11.glPopMatrix();

        //滚轮
        if (extended) {
            if (RenderUtil.isHovering(mouseX, mouseY, width - 95, height - 5 - animation, width - 5, height - 5)) {
                int wheel = Mouse.getDWheel() / 2;

                scrollY += wheel;

                if (scrollY <= minY)
                    scrollY = minY;
                if (scrollY >= 0)
                    scrollY = 0;
            }
        }
    }

    public void onMouseClicked(int mouseX, int mouseY, int mouseButton, AbstractGuiScreen parent) {
        float width = parent.curWidth;
        float height = parent.curHeight;

        if (RenderUtil.isHovering(mouseX, mouseY, width - 95, height - 23 - animation, width - 5, height - 5 - animation)) {

            if(RenderUtil.isHovering(mouseX, mouseY, width - 20, height - 20 - animation, width - 8, height - 8 - animation)) {
                mc.displayGuiScreen(new SavePresetScreen(cgui));
                return;
            }

            extended = !extended;
        }

        if (this.animation > 0) {
            if (RenderUtil.isHovering(mouseX, mouseY, width - 95, height - 5 - animation, width - 5, height - 5)) {
                if (this.slots.size() > 0) {
                    for (ConfigSlot s : this.slots) {
                        s.onClicked((float) mouseX, (float) mouseY, mouseButton);
                    }
                }

                if (this.cloudConfig.size() > 0) {
                    for (ConfigSlot s : this.cloudConfig) {
                        s.onClicked((float) mouseX, (float) mouseY, mouseButton);
                    }
                }
            }
        }
    }

    public void onClose() {
        for (ConfigSlot s : this.slots) {
            s.selected = false;
        }

        for (ConfigSlot s : this.cloudConfig) {
            s.selected = false;
        }
    }

    public void sync() {
        if(!slots.isEmpty())
            this.slots.clear();

        for (String p : PresetManager.presets) {
            this.slots.add(new ConfigSlot(this, p.replace("#", ""), p.startsWith("#")));
        }
    }

    public void loadPreset(String configName) {
        this.currentConfig = configName;
        PresetManager.loadPreset(configName);
        this.sync();
    }

    public void deletePreset(String configName) {
        PresetManager.deletePreset(configName);
        this.sync();
    }

    public void drawGradientSideways(float left, float top, float right, float bottom, int startColor, int endColor) {
        float f = (float) (startColor >> 24 & 255) / 255.0F;
        float f1 = (float) (startColor >> 16 & 255) / 255.0F;
        float f2 = (float) (startColor >> 8 & 255) / 255.0F;
        float f3 = (float) (startColor & 255) / 255.0F;
        float f4 = (float) (endColor >> 24 & 255) / 255.0F;
        float f5 = (float) (endColor >> 16 & 255) / 255.0F;
        float f6 = (float) (endColor >> 8 & 255) / 255.0F;
        float f7 = (float) (endColor & 255) / 255.0F;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(7425);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        worldrenderer.pos(right, top, 0).color(f5, f6, f7, f4).endVertex();
        worldrenderer.pos(left, top, 0).color(f1, f2, f3, f).endVertex();
        worldrenderer.pos(left, bottom, 0).color(f1, f2, f3, f).endVertex();
        worldrenderer.pos(right, bottom, 0).color(f5, f6, f7, f4).endVertex();
        Tessellator.getInstance().draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    public void drawGradientRect(float left, float top, float right, float bottom, int startColor, int endColor) {
        float f = (float) (startColor >> 24 & 255) / 255.0F;
        float f1 = (float) (startColor >> 16 & 255) / 255.0F;
        float f2 = (float) (startColor >> 8 & 255) / 255.0F;
        float f3 = (float) (startColor & 255) / 255.0F;
        float f4 = (float) (endColor >> 24 & 255) / 255.0F;
        float f5 = (float) (endColor >> 16 & 255) / 255.0F;
        float f6 = (float) (endColor >> 8 & 255) / 255.0F;
        float f7 = (float) (endColor & 255) / 255.0F;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(7425);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        worldrenderer.pos(right, top, 0).color(f1, f2, f3, f).endVertex();
        worldrenderer.pos(left, top, 0).color(f1, f2, f3, f).endVertex();
        worldrenderer.pos(left, bottom, 0).color(f5, f6, f7, f4).endVertex();
        worldrenderer.pos(right, bottom, 0).color(f5, f6, f7, f4).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    public static class MouseInputHandler {
        public boolean clicked;
        private final int button;

        public MouseInputHandler(int key) {
            this.button = key;
        }

        public boolean canExcecute() {
            if (Mouse.isButtonDown(button)) {
                if (!this.clicked) {
                    this.clicked = true;
                    return true;
                }
            } else {
                this.clicked = false;
            }
            return false;
        }
    }

}
