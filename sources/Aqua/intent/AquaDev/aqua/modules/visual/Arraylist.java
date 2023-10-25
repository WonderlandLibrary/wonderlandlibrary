package intent.AquaDev.aqua.modules.visual;

import de.Hero.settings.Setting;
import events.Event;
import events.listeners.EventGlowArray;
import events.listeners.EventPostRender2D;
import events.listeners.EventRender2D;
import intent.AquaDev.aqua.Aqua;
import intent.AquaDev.aqua.fr.lavache.anime.Animate;
import intent.AquaDev.aqua.fr.lavache.anime.Easing;
import intent.AquaDev.aqua.modules.Category;
import intent.AquaDev.aqua.modules.Module;
import intent.AquaDev.aqua.modules.visual.Blur;
import intent.AquaDev.aqua.modules.visual.Shadow;
import intent.AquaDev.aqua.utils.shader.Array;
import intent.AquaDev.aqua.utils.shader.ShaderProgram;
import intent.AquaDev.aqua.utils.shader.ShaderStencilUtil;
import java.awt.Color;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.shader.Framebuffer;
import org.lwjgl.opengl.GL11;

public class Arraylist
extends Module {
    Minecraft MC = Minecraft.getMinecraft();

    public Arraylist() {
        super("Arraylist", "Arraylist", 0, Category.Visual);
        Aqua.setmgr.register(new Setting("Rainbow", (Module)this, true));
        Aqua.setmgr.register(new Setting("BiggerOffset", (Module)this, true));
        Aqua.setmgr.register(new Setting("Fade", (Module)this, true));
        Aqua.setmgr.register(new Setting("ShaderFade", (Module)this, true));
        Aqua.setmgr.register(new Setting("GlowRects", (Module)this, true));
        Aqua.setmgr.register(new Setting("GlowStrings", (Module)this, true));
        Aqua.setmgr.register(new Setting("Blur", (Module)this, true));
        Aqua.setmgr.register(new Setting("Background", (Module)this, true));
        Aqua.setmgr.register(new Setting("FontShadow", (Module)this, false));
        Aqua.setmgr.register(new Setting("ReverseFade", (Module)this, true));
        Aqua.setmgr.register(new Setting("Sigma", (Module)this, 5.0, 0.0, 50.0, true));
        Aqua.setmgr.register(new Setting("Multiplier", (Module)this, 1.0, 0.0, 3.0, false));
        Aqua.setmgr.register(new Setting("Alpha", (Module)this, 80.0, 5.0, 240.0, true));
        Aqua.setmgr.register(new Setting("Color", (Module)this));
        Aqua.setmgr.register(new Setting("Fonts", (Module)this, "Comfortaa", new String[]{"Comfortaa", "Minecraft"}));
        Aqua.setmgr.register(new Setting("Shader", (Module)this, "Glow", new String[]{"Glow", "Shadow"}));
    }

    public void onEnable() {
        Array.blurProgram = new ShaderProgram("vertex.vert", "alphaBlurArray.glsl");
        super.onEnable();
    }

    public void onDisable() {
        super.onDisable();
    }

    public static void drawGlowArray(Runnable runnable, boolean renderTwice) {
        EventGlowArray event = new EventGlowArray(runnable);
        Aqua.INSTANCE.onEvent((Event)event);
        if (!event.isCancelled() || renderTwice) {
            runnable.run();
        }
    }

    public void onEvent(Event event) {
        if (event instanceof EventRender2D) {
            if (Aqua.setmgr.getSetting("ArraylistFonts").getCurrentMode().equalsIgnoreCase("Comfortaa")) {
                if (Aqua.setmgr.getSetting("ArraylistBlur").isState()) {
                    if (!Aqua.setmgr.getSetting("ArraylistBiggerOffset").isState()) {
                        this.drawShaderRectsBlur();
                    } else {
                        this.drawShaderRectsBlurBiggerOffset();
                    }
                }
                if (Aqua.setmgr.getSetting("ArraylistGlowRects").isState()) {
                    if (!Aqua.setmgr.getSetting("ArraylistBiggerOffset").isState()) {
                        this.drawShaderRects();
                    } else {
                        this.drawShaderRectsBiggerOffset();
                    }
                }
            }
            if (Aqua.setmgr.getSetting("ArraylistFonts").getCurrentMode().equalsIgnoreCase("Minecraft")) {
                if (Aqua.setmgr.getSetting("ArraylistBlur").isState()) {
                    this.drawShaderRectsMinecraftBlur();
                }
                if (Aqua.setmgr.getSetting("ArraylistGlowRects").isState()) {
                    this.drawShaderRectsMinecraft();
                }
            }
        }
        if (event instanceof EventPostRender2D) {
            if (Aqua.setmgr.getSetting("ArraylistFonts").getCurrentMode().equalsIgnoreCase("Comfortaa")) {
                if (!Aqua.setmgr.getSetting("ArraylistBiggerOffset").isState()) {
                    this.drawRects2();
                } else {
                    this.drawRects2BiggerOffset();
                }
            }
            if (Aqua.setmgr.getSetting("ArraylistFonts").getCurrentMode().equalsIgnoreCase("Minecraft")) {
                this.drawRects2Minecraft();
            }
            if (Aqua.setmgr.getSetting("ArraylistFonts").getCurrentMode().equalsIgnoreCase("Comfortaa")) {
                if (Aqua.setmgr.getSetting("ArraylistGlowStrings").isState()) {
                    if (!Aqua.setmgr.getSetting("ArraylistBiggerOffset").isState()) {
                        Shadow.drawGlow(() -> this.drawStrings(), (boolean)false);
                    }
                } else {
                    Shadow.drawGlow(() -> this.drawStringsBiggerOffset(), (boolean)false);
                }
            }
            if (Aqua.setmgr.getSetting("ArraylistFonts").getCurrentMode().equalsIgnoreCase("Minecraft") && Aqua.setmgr.getSetting("ArraylistGlowStrings").isState()) {
                Shadow.drawGlow(() -> this.drawMinecraftStrings(), (boolean)false);
            }
            if (Aqua.setmgr.getSetting("ArraylistFonts").getCurrentMode().equalsIgnoreCase("Comfortaa")) {
                if (!Aqua.setmgr.getSetting("ArraylistBiggerOffset").isState()) {
                    this.drawStrings();
                } else {
                    this.drawStringsBiggerOffset();
                }
            }
            if (Aqua.setmgr.getSetting("ArraylistFonts").getCurrentMode().equalsIgnoreCase("Minecraft")) {
                this.drawMinecraftStrings();
            }
            Arraylist.drawGlowArray(() -> Gui.drawRect((int)-2001, (int)-2001, (int)-2000, (int)-2000, (int)Color.red.getRGB()), false);
            Array.checkSetup();
            GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            GlStateManager.pushMatrix();
            GlStateManager.enableAlpha();
            GlStateManager.alphaFunc((int)516, (float)0.0f);
            GlStateManager.blendFunc((int)770, (int)771);
            GlStateManager.enableDepth();
            GlStateManager.enableTexture2D();
            GlStateManager.disableLighting();
            ScaledResolution sr = new ScaledResolution(this.MC);
            double screenWidth = sr.getScaledWidth_double();
            double screenHeight = sr.getScaledHeight_double();
            Array.blurProgram.init();
            Array.setupBlurUniforms();
            Array.doBlurPass((int)0, (int)Array.input.framebufferTexture, (Framebuffer)Array.pass, (int)((int)screenWidth), (int)((int)screenHeight));
            Array.doBlurPass((int)1, (int)Array.pass.framebufferTexture, (Framebuffer)Array.output, (int)((int)screenWidth), (int)((int)screenHeight));
            Array.blurProgram.uninit();
            ShaderStencilUtil.initStencil();
            ShaderStencilUtil.bindWriteStencilBuffer();
            this.drawTexturedQuad1(Array.input.framebufferTexture, screenWidth, screenHeight);
            ShaderStencilUtil.bindReadStencilBuffer((int)0);
            this.drawTexturedQuad1(Array.output.framebufferTexture, screenWidth, screenHeight);
            ShaderStencilUtil.uninitStencilBuffer();
            GlStateManager.bindTexture((int)0);
            GlStateManager.alphaFunc((int)516, (float)0.2f);
            GlStateManager.disableAlpha();
            GlStateManager.popMatrix();
            GlStateManager.disableBlend();
            Array.input.framebufferClear();
            this.MC.getFramebuffer().bindFramebuffer(false);
        }
        if (event instanceof EventGlowArray) {
            Array.onArray((EventGlowArray)((EventGlowArray)event));
        }
    }

    public void drawShaderRectsBlur() {
        ScaledResolution sr = new ScaledResolution(mc);
        int index = 0;
        float offset2 = 0.0f;
        GL11.glBlendFunc((int)770, (int)771);
        float offset = 0.0f;
        List collect = (List)Aqua.moduleManager.modules.stream().filter(Module::isToggled).sorted(Comparator.comparingInt(value -> (int)((float)(-sr.getScaledWidth()) - Aqua.INSTANCE.comfortaa3.getWidth(value.getName())))).collect(Collectors.toList());
        int collectSize = collect.size();
        for (int i = 0; i < collectSize; ++i) {
            Module m1 = (Module)collect.get(i);
            Module nextModule = null;
            if (i < collectSize - 1) {
                nextModule = (Module)collect.get(i + 1);
            }
            if (!m1.isToggled()) continue;
            float wSet2 = (float)sr.getScaledWidth() - Aqua.INSTANCE.comfortaa3.getWidth(m1.getName()) - 5.0f;
            float finalOffset = offset;
            int finalIndex = ++index;
            int rainbow = Arraylist.rainbow((int)offset * 9);
            int color = Aqua.setmgr.getSetting("ArraylistReverseFade").isState() ? Arraylist.getGradientOffset(new Color(Aqua.setmgr.getSetting("HUDColor").getColor()), new Color(this.getColor2()), (double)index / 12.4).getRGB() : Arraylist.getGradientOffset(new Color(this.getColor2()), new Color(Aqua.setmgr.getSetting("HUDColor").getColor()), (double)index / 12.4).getRGB();
            int finalColor = Aqua.setmgr.getSetting("ArraylistFade").isState() ? color : new Color(Aqua.setmgr.getSetting("HUDColor").getColor()).getRGB();
            int doubleFinalColor = Aqua.setmgr.getSetting("ArraylistRainbow").isState() ? rainbow : finalColor;
            Gui.drawRect((int)((int)((float)sr.getScaledWidth() - 5.0f)), (int)((int)(offset + 4.0f)), (int)(sr.getScaledWidth() - 3), (int)((int)(offset + 15.0f)), (int)doubleFinalColor);
            if (Aqua.moduleManager.getModuleByName("Blur").isToggled() && Aqua.setmgr.getSetting("ArraylistBlur").isState()) {
                Blur.drawBlurred(() -> Gui.drawRect2((double)(wSet2 - 5.0f), (double)(finalOffset + 4.0f), (double)(sr.getScaledWidth() - 5), (double)(finalOffset + 15.0f), (int)Integer.MIN_VALUE), (boolean)false);
            }
            ++index;
            offset += 11.0f;
            offset2 += 1.0f;
            GL11.glDisable((int)3042);
        }
    }

    public void drawShaderRectsBlurBiggerOffset() {
        ScaledResolution sr = new ScaledResolution(mc);
        int index = 0;
        float offset2 = 0.0f;
        GL11.glBlendFunc((int)770, (int)771);
        float offset = 0.0f;
        List collect = (List)Aqua.moduleManager.modules.stream().filter(Module::isToggled).sorted(Comparator.comparingInt(value -> (int)((float)(-sr.getScaledWidth()) - Aqua.INSTANCE.comfortaa3.getWidth(value.getName())))).collect(Collectors.toList());
        int collectSize = collect.size();
        for (int i = 0; i < collectSize; ++i) {
            int doubleFinalColor;
            Module m1 = (Module)collect.get(i);
            Module nextModule = null;
            if (i < collectSize - 1) {
                nextModule = (Module)collect.get(i + 1);
            }
            if (!m1.isToggled()) continue;
            float wSet2 = (float)sr.getScaledWidth() - Aqua.INSTANCE.comfortaa3.getWidth(m1.getName()) - 5.0f;
            float finalOffset = offset;
            int finalIndex = ++index;
            int rainbow = Arraylist.rainbow((int)offset * 9);
            int color = Aqua.setmgr.getSetting("ArraylistReverseFade").isState() ? Arraylist.getGradientOffset(new Color(Aqua.setmgr.getSetting("HUDColor").getColor()), new Color(this.getColor2()), (double)index / 12.4).getRGB() : Arraylist.getGradientOffset(new Color(this.getColor2()), new Color(Aqua.setmgr.getSetting("HUDColor").getColor()), (double)index / 12.4).getRGB();
            int finalColor = Aqua.setmgr.getSetting("ArraylistFade").isState() ? color : new Color(Aqua.setmgr.getSetting("HUDColor").getColor()).getRGB();
            int n = doubleFinalColor = Aqua.setmgr.getSetting("ArraylistRainbow").isState() ? rainbow : finalColor;
            if (Aqua.moduleManager.getModuleByName("Blur").isToggled() && Aqua.setmgr.getSetting("ArraylistBlur").isState()) {
                Blur.drawBlurred(() -> Gui.drawRect2((double)(wSet2 - 5.0f), (double)(finalOffset + 2.0f), (double)(sr.getScaledWidth() - 5), (double)(finalOffset + 16.0f), (int)Integer.MIN_VALUE), (boolean)false);
            }
            ++index;
            offset += 14.0f;
            offset2 += 1.0f;
            GL11.glDisable((int)3042);
        }
    }

    public void drawShaderRects() {
        ScaledResolution sr = new ScaledResolution(mc);
        int index = 0;
        float offset2 = 0.0f;
        GL11.glBlendFunc((int)770, (int)771);
        float offset = 0.0f;
        List collect = (List)Aqua.moduleManager.modules.stream().filter(Module::isToggled).sorted(Comparator.comparingInt(value -> (int)((float)(-sr.getScaledWidth()) - Aqua.INSTANCE.comfortaa3.getWidth(value.getName())))).collect(Collectors.toList());
        int collectSize = collect.size();
        for (int i = 0; i < collectSize; ++i) {
            int doubleFinalColor;
            Module m1 = (Module)collect.get(i);
            Module nextModule = null;
            if (i < collectSize - 1) {
                nextModule = (Module)collect.get(i + 1);
            }
            if (!m1.isToggled()) continue;
            float wSet2 = (float)sr.getScaledWidth() - Aqua.INSTANCE.comfortaa3.getWidth(m1.getName()) - 5.0f;
            float finalOffset = offset;
            int finalIndex = ++index;
            int rainbow = Arraylist.rainbow((int)offset * 9);
            int color = Aqua.setmgr.getSetting("ArraylistReverseFade").isState() ? Arraylist.getGradientOffset(new Color(Aqua.setmgr.getSetting("HUDColor").getColor()), new Color(this.getColor2()), (double)index / 12.4).getRGB() : Arraylist.getGradientOffset(new Color(this.getColor2()), new Color(Aqua.setmgr.getSetting("HUDColor").getColor()), (double)index / 12.4).getRGB();
            int finalColor = Aqua.setmgr.getSetting("ArraylistFade").isState() ? color : new Color(Aqua.setmgr.getSetting("HUDColor").getColor()).getRGB();
            int n = doubleFinalColor = Aqua.setmgr.getSetting("ArraylistRainbow").isState() ? rainbow : finalColor;
            if (Aqua.setmgr.getSetting("ArraylistShader").getCurrentMode().equalsIgnoreCase("Glow")) {
                Arraylist.drawGlowArray(() -> Gui.drawRect2((double)(wSet2 - 5.0f), (double)(finalOffset + 4.0f), (double)(sr.getScaledWidth() - 5), (double)(finalOffset + 15.0f), (int)new Color(doubleFinalColor).getRGB()), false);
            } else {
                Shadow.drawGlow(() -> Gui.drawRect2((double)(wSet2 - 5.0f), (double)(finalOffset + 4.0f), (double)(sr.getScaledWidth() - 5), (double)(finalOffset + 15.0f), (int)Color.black.getRGB()), (boolean)false);
            }
            if (Aqua.moduleManager.getModuleByName("Blur").isToggled() && Aqua.setmgr.getSetting("ArraylistBlur").isState()) {
                Blur.drawBlurred(() -> Gui.drawRect2((double)(wSet2 - 5.0f), (double)(finalOffset + 4.0f), (double)(sr.getScaledWidth() - 5), (double)(finalOffset + 15.0f), (int)Integer.MIN_VALUE), (boolean)false);
            }
            ++index;
            offset += 11.0f;
            offset2 += 1.0f;
            GL11.glDisable((int)3042);
        }
    }

    public void drawShaderRectsBiggerOffset() {
        ScaledResolution sr = new ScaledResolution(mc);
        int index = 0;
        float offset2 = 0.0f;
        GL11.glBlendFunc((int)770, (int)771);
        float offset = 0.0f;
        List collect = (List)Aqua.moduleManager.modules.stream().filter(Module::isToggled).sorted(Comparator.comparingInt(value -> (int)((float)(-sr.getScaledWidth()) - Aqua.INSTANCE.comfortaa3.getWidth(value.getName())))).collect(Collectors.toList());
        int collectSize = collect.size();
        for (int i = 0; i < collectSize; ++i) {
            int doubleFinalColor;
            Module m1 = (Module)collect.get(i);
            Module nextModule = null;
            if (i < collectSize - 1) {
                nextModule = (Module)collect.get(i + 1);
            }
            if (!m1.isToggled()) continue;
            float wSet2 = (float)sr.getScaledWidth() - Aqua.INSTANCE.comfortaa3.getWidth(m1.getName()) - 5.0f;
            float finalOffset = offset;
            int finalIndex = ++index;
            int rainbow = Arraylist.rainbow((int)offset * 9);
            int color = Aqua.setmgr.getSetting("ArraylistReverseFade").isState() ? Arraylist.getGradientOffset(new Color(Aqua.setmgr.getSetting("HUDColor").getColor()), new Color(this.getColor2()), (double)index / 12.4).getRGB() : Arraylist.getGradientOffset(new Color(this.getColor2()), new Color(Aqua.setmgr.getSetting("HUDColor").getColor()), (double)index / 12.4).getRGB();
            int finalColor = Aqua.setmgr.getSetting("ArraylistFade").isState() ? color : new Color(Aqua.setmgr.getSetting("HUDColor").getColor()).getRGB();
            int n = doubleFinalColor = Aqua.setmgr.getSetting("ArraylistRainbow").isState() ? rainbow : finalColor;
            if (Aqua.setmgr.getSetting("ArraylistShader").getCurrentMode().equalsIgnoreCase("Glow")) {
                Gui.drawRect((int)((int)((float)sr.getScaledWidth() - 5.0f)), (int)((int)(offset + 2.0f)), (int)(sr.getScaledWidth() - 3), (int)((int)(offset + 16.0f)), (int)doubleFinalColor);
                Arraylist.drawGlowArray(() -> Gui.drawRect2((double)(wSet2 - 5.0f), (double)(finalOffset + 2.0f), (double)(sr.getScaledWidth() - 5), (double)(finalOffset + 16.0f), (int)new Color(doubleFinalColor).getRGB()), false);
            } else {
                float finalOffset1 = offset;
                Arraylist.drawGlowArray(() -> Gui.drawRect((int)((int)((float)sr.getScaledWidth() - 5.0f)), (int)((int)(finalOffset1 + 2.0f)), (int)(sr.getScaledWidth() - 3), (int)((int)(finalOffset1 + 16.0f)), (int)doubleFinalColor), false);
                Shadow.drawGlow(() -> Gui.drawRect2((double)(wSet2 - 5.0f), (double)(finalOffset + 2.0f), (double)(sr.getScaledWidth() - 5), (double)(finalOffset + 16.0f), (int)Color.black.getRGB()), (boolean)false);
                Gui.drawRect((int)((int)((float)sr.getScaledWidth() - 5.0f)), (int)((int)(offset + 2.0f)), (int)(sr.getScaledWidth() - 3), (int)((int)(offset + 16.0f)), (int)doubleFinalColor);
            }
            if (!Aqua.moduleManager.getModuleByName("Blur").isToggled() || Aqua.setmgr.getSetting("ArraylistBlur").isState()) {
                // empty if block
            }
            ++index;
            offset += 14.0f;
            offset2 += 1.0f;
            GL11.glDisable((int)3042);
        }
    }

    public void drawShaderRectsMinecraftBlur() {
        ScaledResolution sr = new ScaledResolution(mc);
        int index = 0;
        float offset2 = 0.0f;
        GL11.glBlendFunc((int)770, (int)771);
        float offset = 0.0f;
        List collect = (List)Aqua.moduleManager.modules.stream().filter(Module::isToggled).sorted(Comparator.comparingInt(value -> -sr.getScaledWidth() - Arraylist.mc.fontRendererObj.getStringWidth(value.getName()))).collect(Collectors.toList());
        int collectSize = collect.size();
        for (int i = 0; i < collectSize; ++i) {
            Module m1 = (Module)collect.get(i);
            Module nextModule = null;
            if (i < collectSize - 1) {
                nextModule = (Module)collect.get(i + 1);
            }
            if (!m1.isToggled()) continue;
            float wSet2 = sr.getScaledWidth() - Arraylist.mc.fontRendererObj.getStringWidth(m1.getName()) - 5;
            float finalOffset = offset;
            int finalIndex = ++index;
            int rainbow = Arraylist.rainbow((int)offset * 9);
            int color = Aqua.setmgr.getSetting("ArraylistReverseFade").isState() ? Arraylist.getGradientOffset(new Color(Aqua.setmgr.getSetting("HUDColor").getColor()), new Color(this.getColor2()), (double)index / 12.4).getRGB() : Arraylist.getGradientOffset(new Color(this.getColor2()), new Color(Aqua.setmgr.getSetting("HUDColor").getColor()), (double)index / 12.4).getRGB();
            int finalColor = Aqua.setmgr.getSetting("ArraylistFade").isState() ? color : new Color(Aqua.setmgr.getSetting("HUDColor").getColor()).getRGB();
            int doubleFinalColor = Aqua.setmgr.getSetting("ArraylistRainbow").isState() ? rainbow : finalColor;
            Gui.drawRect((int)((int)((float)sr.getScaledWidth() - 5.0f)), (int)((int)(offset + 4.0f)), (int)(sr.getScaledWidth() - 3), (int)((int)(offset + 15.0f)), (int)doubleFinalColor);
            if (Aqua.moduleManager.getModuleByName("Blur").isToggled()) {
                Blur.drawBlurred(() -> Gui.drawRect2((double)(wSet2 - 5.0f), (double)(finalOffset + 4.0f), (double)(sr.getScaledWidth() - 5), (double)(finalOffset + 15.0f), (int)Integer.MIN_VALUE), (boolean)false);
            }
            ++index;
            offset += 11.0f;
            offset2 += 1.0f;
            GL11.glDisable((int)3042);
        }
    }

    public void drawShaderRectsMinecraft() {
        ScaledResolution sr = new ScaledResolution(mc);
        int index = 0;
        float offset2 = 0.0f;
        GL11.glBlendFunc((int)770, (int)771);
        float offset = 0.0f;
        List collect = (List)Aqua.moduleManager.modules.stream().filter(Module::isToggled).sorted(Comparator.comparingInt(value -> -sr.getScaledWidth() - Arraylist.mc.fontRendererObj.getStringWidth(value.getName()))).collect(Collectors.toList());
        int collectSize = collect.size();
        for (int i = 0; i < collectSize; ++i) {
            int doubleFinalColor;
            Module m1 = (Module)collect.get(i);
            Module nextModule = null;
            if (i < collectSize - 1) {
                nextModule = (Module)collect.get(i + 1);
            }
            if (!m1.isToggled()) continue;
            float wSet2 = sr.getScaledWidth() - Arraylist.mc.fontRendererObj.getStringWidth(m1.getName()) - 5;
            float finalOffset = offset;
            int finalIndex = ++index;
            int rainbow = Arraylist.rainbow((int)offset * 9);
            int color = Aqua.setmgr.getSetting("ArraylistReverseFade").isState() ? Arraylist.getGradientOffset(new Color(Aqua.setmgr.getSetting("HUDColor").getColor()), new Color(this.getColor2()), (double)index / 12.4).getRGB() : Arraylist.getGradientOffset(new Color(this.getColor2()), new Color(Aqua.setmgr.getSetting("HUDColor").getColor()), (double)index / 12.4).getRGB();
            int finalColor = Aqua.setmgr.getSetting("ArraylistFade").isState() ? color : new Color(Aqua.setmgr.getSetting("HUDColor").getColor()).getRGB();
            int n = doubleFinalColor = Aqua.setmgr.getSetting("ArraylistRainbow").isState() ? rainbow : finalColor;
            if (Aqua.setmgr.getSetting("ArraylistShader").getCurrentMode().equalsIgnoreCase("Glow")) {
                Arraylist.drawGlowArray(() -> Gui.drawRect2((double)(wSet2 - 5.0f), (double)(finalOffset + 4.0f), (double)(sr.getScaledWidth() - 5), (double)(finalOffset + 15.0f), (int)new Color(doubleFinalColor).getRGB()), false);
            } else {
                Shadow.drawGlow(() -> Gui.drawRect2((double)(wSet2 - 5.0f), (double)(finalOffset + 4.0f), (double)(sr.getScaledWidth() - 5), (double)(finalOffset + 15.0f), (int)Color.black.getRGB()), (boolean)false);
            }
            if (Aqua.moduleManager.getModuleByName("Blur").isToggled()) {
                Blur.drawBlurred(() -> Gui.drawRect2((double)(wSet2 - 5.0f), (double)(finalOffset + 4.0f), (double)(sr.getScaledWidth() - 5), (double)(finalOffset + 15.0f), (int)Integer.MIN_VALUE), (boolean)false);
            }
            ++index;
            offset += 11.0f;
            offset2 += 1.0f;
            GL11.glDisable((int)3042);
        }
    }

    public void drawRects2() {
        ScaledResolution sr = new ScaledResolution(mc);
        int index = 0;
        float offset2 = 0.0f;
        GL11.glBlendFunc((int)770, (int)771);
        float offset = 0.0f;
        List collect = (List)Aqua.moduleManager.modules.stream().filter(Module::isToggled).sorted(Comparator.comparingInt(value -> (int)((float)(-sr.getScaledWidth()) - Aqua.INSTANCE.comfortaa3.getWidth(value.getName())))).collect(Collectors.toList());
        int collectSize = collect.size();
        for (int i = 0; i < collectSize; ++i) {
            Module m1 = (Module)collect.get(i);
            Module nextModule = null;
            if (i < collectSize - 1) {
                nextModule = (Module)collect.get(i + 1);
            }
            if (!m1.isToggled()) continue;
            float wSet2 = (float)sr.getScaledWidth() - Aqua.INSTANCE.comfortaa3.getWidth(m1.getName()) - 5.0f;
            ++index;
            float finalOffset = offset;
            int alphaBackground = (int)Aqua.setmgr.getSetting("ArraylistAlpha").getCurrentNumber();
            if (Aqua.setmgr.getSetting("ArraylistBackground").isState()) {
                Gui.drawRect2((double)(wSet2 - 5.0f), (double)(finalOffset + 4.0f), (double)(sr.getScaledWidth() - 5), (double)(finalOffset + 15.0f), (int)new Color(0, 0, 0, alphaBackground).getRGB());
            }
            ++index;
            offset += 11.0f;
            offset2 += 1.0f;
            GL11.glDisable((int)3042);
        }
    }

    public void drawRects2BiggerOffset() {
        ScaledResolution sr = new ScaledResolution(mc);
        int index = 0;
        float offset2 = 0.0f;
        GL11.glBlendFunc((int)770, (int)771);
        float offset = 0.0f;
        List collect = (List)Aqua.moduleManager.modules.stream().filter(Module::isToggled).sorted(Comparator.comparingInt(value -> (int)((float)(-sr.getScaledWidth()) - Aqua.INSTANCE.comfortaa3.getWidth(value.getName())))).collect(Collectors.toList());
        int collectSize = collect.size();
        for (int i = 0; i < collectSize; ++i) {
            Module m1 = (Module)collect.get(i);
            Module nextModule = null;
            if (i < collectSize - 1) {
                nextModule = (Module)collect.get(i + 1);
            }
            if (!m1.isToggled()) continue;
            float wSet2 = (float)sr.getScaledWidth() - Aqua.INSTANCE.comfortaa3.getWidth(m1.getName()) - 5.0f;
            ++index;
            float finalOffset = offset;
            int alphaBackground = (int)Aqua.setmgr.getSetting("ArraylistAlpha").getCurrentNumber();
            if (Aqua.setmgr.getSetting("ArraylistBackground").isState()) {
                Gui.drawRect2((double)(wSet2 - 5.0f), (double)(finalOffset + 2.0f), (double)(sr.getScaledWidth() - 5), (double)(finalOffset + 16.0f), (int)new Color(0, 0, 0, alphaBackground).getRGB());
            }
            offset += 14.0f;
            offset2 += 1.0f;
            GL11.glDisable((int)3042);
        }
    }

    public void drawRects2Minecraft() {
        ScaledResolution sr = new ScaledResolution(mc);
        int index = 0;
        float offset2 = 0.0f;
        GL11.glBlendFunc((int)770, (int)771);
        float offset = 0.0f;
        List collect = (List)Aqua.moduleManager.modules.stream().filter(Module::isToggled).sorted(Comparator.comparingInt(value -> -sr.getScaledWidth() - Arraylist.mc.fontRendererObj.getStringWidth(value.getName()))).collect(Collectors.toList());
        int collectSize = collect.size();
        for (int i = 0; i < collectSize; ++i) {
            Module m1 = (Module)collect.get(i);
            Module nextModule = null;
            if (i < collectSize - 1) {
                nextModule = (Module)collect.get(i + 1);
            }
            if (!m1.isToggled()) continue;
            float wSet2 = sr.getScaledWidth() - Arraylist.mc.fontRendererObj.getStringWidth(m1.getName()) - 5;
            ++index;
            float finalOffset = offset;
            int alphaBackground = (int)Aqua.setmgr.getSetting("ArraylistAlpha").getCurrentNumber();
            if (Aqua.setmgr.getSetting("ArraylistBackground").isState()) {
                Gui.drawRect2((double)(wSet2 - 5.0f), (double)(finalOffset + 4.0f), (double)(sr.getScaledWidth() - 5), (double)(finalOffset + 15.0f), (int)new Color(0, 0, 0, alphaBackground).getRGB());
            }
            ++index;
            offset += 11.0f;
            offset2 += 1.0f;
            GL11.glDisable((int)3042);
        }
    }

    public void drawMinecraftStrings() {
        ScaledResolution sr = new ScaledResolution(mc);
        int index = 0;
        float offset2 = 0.0f;
        GL11.glBlendFunc((int)770, (int)771);
        float offset = 0.0f;
        List collect = (List)Aqua.moduleManager.modules.stream().filter(Module::isToggled).sorted(Comparator.comparingInt(value -> -sr.getScaledWidth() - Arraylist.mc.fontRendererObj.getStringWidth(value.getName()))).collect(Collectors.toList());
        int collectSize = collect.size();
        for (int i = 0; i < collectSize; ++i) {
            int doubleFinalColor;
            Module m1 = (Module)collect.get(i);
            Module nextModule = null;
            if (i < collectSize - 1) {
                nextModule = (Module)collect.get(i + 1);
            }
            if (!m1.isToggled()) continue;
            float wSet2 = sr.getScaledWidth() - Arraylist.mc.fontRendererObj.getStringWidth(m1.getName()) - 5;
            int rainbow = Arraylist.rainbow((int)offset * 9);
            int color = Aqua.setmgr.getSetting("ArraylistReverseFade").isState() ? Arraylist.getGradientOffset(new Color(Aqua.setmgr.getSetting("HUDColor").getColor()), new Color(this.getColor2()), (double)index / 12.4).getRGB() : Arraylist.getGradientOffset(new Color(this.getColor2()), new Color(Aqua.setmgr.getSetting("HUDColor").getColor()), (double)index / 12.4).getRGB();
            int finalColor = Aqua.setmgr.getSetting("ArraylistFade").isState() ? color : new Color(Aqua.setmgr.getSetting("HUDColor").getColor()).getRGB();
            int n = doubleFinalColor = Aqua.setmgr.getSetting("ArraylistRainbow").isState() ? rainbow : finalColor;
            if (Aqua.setmgr.getSetting("ArraylistFontShadow").isState()) {
                Arraylist.mc.fontRendererObj.drawStringWithShadow(m1.getName(), wSet2 - 2.0f, offset + 6.0f, doubleFinalColor);
            } else {
                Arraylist.mc.fontRendererObj.drawString(m1.getName(), (int)(wSet2 - 2.0f), (int)(offset + 6.0f), doubleFinalColor);
            }
            ++index;
            ++index;
            offset += 11.0f;
            offset2 += 1.0f;
            GL11.glDisable((int)3042);
        }
    }

    public void drawStrings() {
        ScaledResolution sr = new ScaledResolution(mc);
        int index = 0;
        float offset2 = 0.0f;
        GL11.glBlendFunc((int)770, (int)771);
        float offset = 0.0f;
        List collect = (List)Aqua.moduleManager.modules.stream().filter(Module::isToggled).sorted(Comparator.comparingInt(value -> (int)((float)(-sr.getScaledWidth()) - Aqua.INSTANCE.comfortaa3.getWidth(value.getName())))).collect(Collectors.toList());
        int collectSize = collect.size();
        for (int i = 0; i < collectSize; ++i) {
            int doubleFinalColor;
            Module m1 = (Module)collect.get(i);
            Module nextModule = null;
            if (i < collectSize - 1) {
                nextModule = (Module)collect.get(i + 1);
            }
            if (!m1.isToggled()) continue;
            m1.anim.setEase(Easing.LINEAR).setMin(0.0f).setMax(Aqua.INSTANCE.comfortaa3.getWidth(m1.getName()) + 5.0f).setSpeed(25.0f).setReversed(!m1.isToggled()).update();
            Animate animate = m1.anim2.setEase(Easing.LINEAR).setMin(0.0f);
            Aqua.INSTANCE.comfortaa3.getClass();
            animate.setMax(9.0f).setSpeed(25.0f).setReversed(!m1.isToggled()).update();
            float wSet2 = (float)sr.getScaledWidth() - m1.anim.getValue();
            int rainbow = Arraylist.rainbow((int)offset * 9);
            int color = Aqua.setmgr.getSetting("ArraylistReverseFade").isState() ? Arraylist.getGradientOffset(new Color(Aqua.setmgr.getSetting("HUDColor").getColor()), new Color(this.getColor2()), (double)index / 12.4).getRGB() : Arraylist.getGradientOffset(new Color(this.getColor2()), new Color(Aqua.setmgr.getSetting("HUDColor").getColor()), (double)index / 12.4).getRGB();
            int finalColor = Aqua.setmgr.getSetting("ArraylistFade").isState() ? color : new Color(Aqua.setmgr.getSetting("HUDColor").getColor()).getRGB();
            int n = doubleFinalColor = Aqua.setmgr.getSetting("ArraylistRainbow").isState() ? rainbow : finalColor;
            if (Aqua.setmgr.getSetting("ArraylistFontShadow").isState()) {
                Aqua.INSTANCE.comfortaa3.drawStringWithShadow(m1.getName(), wSet2 - 3.0f, offset + 5.0f, doubleFinalColor);
            } else {
                Aqua.INSTANCE.comfortaa3.drawString(m1.getName(), wSet2 - 3.0f, offset + 5.0f, doubleFinalColor);
            }
            ++index;
            ++index;
            offset += m1.anim2.getValue() + 2.0f;
            offset2 += 1.0f;
            GL11.glDisable((int)3042);
        }
    }

    public void drawStringsBiggerOffset() {
        ScaledResolution sr = new ScaledResolution(mc);
        int index = 0;
        float offset2 = 0.0f;
        GL11.glBlendFunc((int)770, (int)771);
        float offset = 0.0f;
        List collect = (List)Aqua.moduleManager.modules.stream().filter(Module::isToggled).sorted(Comparator.comparingInt(value -> (int)((float)(-sr.getScaledWidth()) - Aqua.INSTANCE.comfortaa3.getWidth(value.getName())))).collect(Collectors.toList());
        int collectSize = collect.size();
        for (int i = 0; i < collectSize; ++i) {
            int doubleFinalColor;
            Module m1 = (Module)collect.get(i);
            Module nextModule = null;
            if (i < collectSize - 1) {
                nextModule = (Module)collect.get(i + 1);
            }
            if (!m1.isToggled()) continue;
            m1.anim.setEase(Easing.LINEAR).setMin(0.0f).setMax(Aqua.INSTANCE.comfortaa3.getWidth(m1.getName()) + 5.0f).setSpeed(65.0f).setReversed(!m1.isToggled()).update();
            Animate animate = m1.anim2.setEase(Easing.LINEAR).setMin(0.0f);
            Aqua.INSTANCE.comfortaa3.getClass();
            animate.setMax(9.0f).setSpeed(25.0f).setReversed(!m1.isToggled()).update();
            float wSet2 = (float)sr.getScaledWidth() - m1.anim.getValue();
            int rainbow = Arraylist.rainbow((int)offset * 9);
            int color = Aqua.setmgr.getSetting("ArraylistReverseFade").isState() ? Arraylist.getGradientOffset(new Color(Aqua.setmgr.getSetting("HUDColor").getColor()), new Color(this.getColor2()), (double)index / 12.4).getRGB() : Arraylist.getGradientOffset(new Color(this.getColor2()), new Color(Aqua.setmgr.getSetting("HUDColor").getColor()), (double)index / 12.4).getRGB();
            int finalColor = Aqua.setmgr.getSetting("ArraylistFade").isState() ? color : new Color(Aqua.setmgr.getSetting("HUDColor").getColor()).getRGB();
            int n = doubleFinalColor = Aqua.setmgr.getSetting("ArraylistRainbow").isState() ? rainbow : finalColor;
            if (Aqua.setmgr.getSetting("ArraylistFontShadow").isState()) {
                Aqua.INSTANCE.comfortaa3.drawStringWithShadow(m1.getName(), wSet2 - 3.0f, offset + 5.0f, doubleFinalColor);
            } else {
                Aqua.INSTANCE.comfortaa3.drawString(m1.getName(), wSet2 - 3.0f, offset + 4.0f, doubleFinalColor);
            }
            ++index;
            ++index;
            offset += m1.anim2.getValue() + 5.0f;
            offset2 += 1.0f;
            GL11.glDisable((int)3042);
        }
    }

    public void drawRects() {
        ScaledResolution sr = new ScaledResolution(mc);
        int index = 0;
        float offset2 = 0.0f;
        GL11.glBlendFunc((int)770, (int)771);
        int listSize = (int)Aqua.moduleManager.modules.stream().filter(Module::isToggled).count();
        List collect = (List)Aqua.moduleManager.modules.stream().filter(Module::isToggled).collect(Collectors.toList());
        int collectSize = collect.size();
        for (int i = 0; i < collectSize; ++i) {
            Module m = (Module)collect.get(i);
            Module nextModule = null;
            if (i < collectSize - 1) {
                nextModule = (Module)collect.get(i + 1);
            }
            float wSet2 = (float)sr.getScaledWidth() - Aqua.INSTANCE.comfortaa3.getWidth(m.getName()) - 5.0f;
            float wSetNext = sr.getScaledWidth();
            if (nextModule != null) {
                wSetNext = (float)sr.getScaledWidth() - Aqua.INSTANCE.comfortaa3.getWidth(nextModule.getName()) - 5.0f;
            }
            float finalWSetNext1 = wSetNext;
            int finalIndex1 = index++;
            float finalOffset3 = offset2;
            float finalOffset = offset2;
            Arraylist.drawGlowArray(() -> Gui.drawRect((int)((int)((float)sr.getScaledWidth() - 5.0f)), (int)((int)(finalOffset + 4.0f)), (int)(sr.getScaledWidth() - 3), (int)((int)(finalOffset + 15.0f)), (int)Aqua.setmgr.getSetting("HUDColor").getColor()), false);
            Gui.drawRect((int)((int)((float)sr.getScaledWidth() - 5.0f)), (int)((int)(offset2 + 4.0f)), (int)(sr.getScaledWidth() - 3), (int)((int)(offset2 + 15.0f)), (int)Aqua.setmgr.getSetting("HUDColor").getColor());
            offset2 += 11.0f;
        }
        GL11.glDisable((int)3042);
    }

    private void drawTexturedQuad1(int texture, double width, double height) {
        GlStateManager.enableBlend();
        GL11.glBindTexture((int)3553, (int)texture);
        GL11.glBegin((int)7);
        GL11.glTexCoord2d((double)0.0, (double)1.0);
        GL11.glVertex2d((double)0.0, (double)0.0);
        GL11.glTexCoord2d((double)0.0, (double)0.0);
        GL11.glVertex2d((double)0.0, (double)height);
        GL11.glTexCoord2d((double)1.0, (double)0.0);
        GL11.glVertex2d((double)width, (double)height);
        GL11.glTexCoord2d((double)1.0, (double)1.0);
        GL11.glVertex2d((double)width, (double)0.0);
        GL11.glEnd();
    }

    public static Color getGradientOffset(Color color1, Color color2, double index) {
        double offs = (double)Math.abs((long)(System.currentTimeMillis() / 13L)) / 60.0 + index;
        if (offs > 1.0) {
            double left = offs % 1.0;
            int off = (int)offs;
            offs = off % 2 == 0 ? left : 1.0 - left;
        }
        double inverse_percent = 1.0 - offs;
        int redPart = (int)((double)color1.getRed() * inverse_percent + (double)color2.getRed() * offs);
        int greenPart = (int)((double)color1.getGreen() * inverse_percent + (double)color2.getGreen() * offs);
        int bluePart = (int)((double)color1.getBlue() * inverse_percent + (double)color2.getBlue() * offs);
        return new Color(redPart, greenPart, bluePart);
    }

    public int getColor2() {
        try {
            return Aqua.setmgr.getSetting("ArraylistColor").getColor();
        }
        catch (Exception e) {
            return Color.white.getRGB();
        }
    }

    public static int rainbow(int delay) {
        double rainbowState = Math.ceil((double)((System.currentTimeMillis() + (long)delay) / 7L));
        return Color.getHSBColor((float)((float)((rainbowState %= 360.0) / 360.0)), (float)0.9f, (float)1.0f).getRGB();
    }
}
