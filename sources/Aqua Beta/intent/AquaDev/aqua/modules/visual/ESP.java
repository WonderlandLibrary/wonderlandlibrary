package intent.AquaDev.aqua.modules.visual;

import de.Hero.settings.Setting;
import events.Event;
import events.listeners.EventGlowESP;
import events.listeners.EventPostRender2D;
import events.listeners.EventPostRender3D;
import events.listeners.EventRender3D;
import intent.AquaDev.aqua.Aqua;
import intent.AquaDev.aqua.modules.Category;
import intent.AquaDev.aqua.modules.Module;
import intent.AquaDev.aqua.modules.visual.Arraylist;
import intent.AquaDev.aqua.utils.shader.Glow;
import intent.AquaDev.aqua.utils.shader.ShaderProgram;
import intent.AquaDev.aqua.utils.shader.ShaderStencilUtil;
import java.awt.Color;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public class ESP
extends Module {
    Minecraft MC = Minecraft.getMinecraft();
    private final Framebuffer input;
    private final Framebuffer pass;
    private final Framebuffer output;
    private ShaderProgram outlineProgram;
    public static double stringWidth;
    public static double glowStrength;

    public ESP() {
        super("ESP", "ESP", 0, Category.Visual);
        this.input = new Framebuffer(this.MC.displayWidth, this.MC.displayHeight, true);
        this.pass = new Framebuffer(this.MC.displayWidth, this.MC.displayHeight, true);
        this.output = new Framebuffer(this.MC.displayWidth, this.MC.displayHeight, true);
        this.outlineProgram = new ShaderProgram("vertex.vert", "outline.glsl");
        Aqua.setmgr.register(new Setting("Sigma", (Module)this, 5.0, 0.0, 50.0, true));
        Aqua.setmgr.register(new Setting("Multiplier", (Module)this, 1.0, 0.0, 3.0, false));
        Aqua.setmgr.register(new Setting("OutlineWidth", (Module)this, 1.0, 0.0, 20.0, false));
        Aqua.setmgr.register(new Setting("Animals", (Module)this, true));
        Aqua.setmgr.register(new Setting("Mobs", (Module)this, false));
        Aqua.setmgr.register(new Setting("Players", (Module)this, true));
        Aqua.setmgr.register(new Setting("Rainbow", (Module)this, false));
        Aqua.setmgr.register(new Setting("AstolfoColors", (Module)this, false));
        Aqua.setmgr.register(new Setting("Chests", (Module)this, true));
        Aqua.setmgr.register(new Setting("EnderChests", (Module)this, false));
        Aqua.setmgr.register(new Setting("ClientColor", (Module)this, true));
        Aqua.setmgr.register(new Setting("Mode", (Module)this, "Shader", new String[]{"Shader", "Hizzy"}));
        Aqua.setmgr.register(new Setting("Color", (Module)this));
    }

    public void onEnable() {
        this.outlineProgram = new ShaderProgram("vertex.vert", "outline.glsl");
        Glow.blurProgram = new ShaderProgram("vertex.vert", "alphaBlurESP.glsl");
        super.onEnable();
    }

    public void onDisable() {
        super.onDisable();
    }

    public static void drawGlowESP(Runnable runnable, boolean renderTwice) {
        EventGlowESP event = new EventGlowESP(runnable);
        Aqua.INSTANCE.onEvent((Event)event);
        if (!event.isCancelled() || renderTwice) {
            runnable.run();
        }
    }

    public void onEvent(Event event) {
        ScaledResolution sr;
        if (event instanceof EventPostRender3D && Aqua.setmgr.getSetting("ESPMode").getCurrentMode().equalsIgnoreCase("Hizzy")) {
            Arraylist.drawGlowArray(() -> this.drawHizzyESP(), (boolean)false);
            this.drawHizzyESP();
        }
        if (event instanceof EventRender3D) {
            TileEntity tileEntity;
            int i;
            int loadedTileEntityListSize;
            List loadedTileEntityList;
            GlStateManager.pushMatrix();
            GlStateManager.disableLighting();
            this.input.bindFramebuffer(false);
            this.MC.getRenderManager().setRenderOutlines(false);
            if (Aqua.setmgr.getSetting("ESPChests").isState()) {
                loadedTileEntityList = ESP.mc.theWorld.loadedTileEntityList;
                loadedTileEntityListSize = loadedTileEntityList.size();
                for (i = 0; i < loadedTileEntityListSize; ++i) {
                    tileEntity = (TileEntity)loadedTileEntityList.get(i);
                    if (!(tileEntity instanceof TileEntityChest)) continue;
                    GlStateManager.disableTexture2D();
                    TileEntityRendererDispatcher.instance.renderTileEntity(tileEntity, ESP.mc.timer.renderPartialTicks, 1);
                    GlStateManager.enableTexture2D();
                }
            }
            if (Aqua.setmgr.getSetting("ESPEnderChests").isState()) {
                loadedTileEntityList = ESP.mc.theWorld.loadedTileEntityList;
                loadedTileEntityListSize = loadedTileEntityList.size();
                for (i = 0; i < loadedTileEntityListSize; ++i) {
                    tileEntity = (TileEntity)loadedTileEntityList.get(i);
                    if (!(tileEntity instanceof TileEntityEnderChest)) continue;
                    GlStateManager.disableTexture2D();
                    TileEntityRendererDispatcher.instance.renderTileEntity(tileEntity, ESP.mc.timer.renderPartialTicks, 1);
                    GlStateManager.enableTexture2D();
                }
            }
            if (Aqua.setmgr.getSetting("ESPMode").getCurrentMode().equalsIgnoreCase("Shader") && Aqua.setmgr.getSetting("ESPPlayers").isState()) {
                for (Entity entity : this.MC.theWorld.loadedEntityList) {
                    if (!(entity instanceof EntityPlayer) || entity == ESP.mc.thePlayer && ESP.mc.gameSettings.thirdPersonView != 1 && ESP.mc.gameSettings.thirdPersonView != 2) continue;
                    mc.getRenderManager().renderEntityStatic(entity, ((EventRender3D)event).getPartialTicks(), false);
                }
            }
            if (Aqua.setmgr.getSetting("ESPAnimals").isState()) {
                for (Entity entity : this.MC.theWorld.loadedEntityList) {
                    if (!(entity instanceof EntityAnimal)) continue;
                    mc.getRenderManager().renderEntityStatic(entity, ((EventRender3D)event).getPartialTicks(), false);
                }
            }
            if (Aqua.setmgr.getSetting("ESPMobs").isState()) {
                for (Entity entity : this.MC.theWorld.loadedEntityList) {
                    if (!(entity instanceof EntityMob)) continue;
                    mc.getRenderManager().renderEntityStatic(entity, ((EventRender3D)event).getPartialTicks(), false);
                }
            }
            mc.getRenderManager().setRenderOutlines(false);
            GlStateManager.enableLighting();
            GlStateManager.popMatrix();
            mc.getFramebuffer().bindFramebuffer(false);
        }
        if (event instanceof EventPostRender2D) {
            ESP.drawGlowESP(() -> Gui.drawRect((int)-2001, (int)-2001, (int)-2000, (int)-2000, (int)Color.red.getRGB()), false);
            Glow.checkSetup();
            GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            GlStateManager.pushMatrix();
            GlStateManager.enableAlpha();
            GlStateManager.alphaFunc((int)516, (float)0.0f);
            GlStateManager.blendFunc((int)770, (int)771);
            GlStateManager.enableDepth();
            GlStateManager.enableTexture2D();
            GlStateManager.disableLighting();
            sr = new ScaledResolution(this.MC);
            double screenWidth = sr.getScaledWidth_double();
            double screenHeight = sr.getScaledHeight_double();
            Glow.blurProgram.init();
            Glow.setupBlurUniforms();
            Glow.doBlurPass((int)0, (int)Glow.input.framebufferTexture, (Framebuffer)Glow.pass, (int)((int)screenWidth), (int)((int)screenHeight));
            Glow.doBlurPass((int)1, (int)Glow.pass.framebufferTexture, (Framebuffer)Glow.output, (int)((int)screenWidth), (int)((int)screenHeight));
            Glow.blurProgram.uninit();
            ShaderStencilUtil.initStencil();
            ShaderStencilUtil.bindWriteStencilBuffer();
            this.drawTexturedQuad1(Glow.input.framebufferTexture, screenWidth, screenHeight);
            ShaderStencilUtil.bindReadStencilBuffer((int)0);
            this.drawTexturedQuad1(Glow.output.framebufferTexture, screenWidth, screenHeight);
            ShaderStencilUtil.uninitStencilBuffer();
            GlStateManager.bindTexture((int)0);
            GlStateManager.alphaFunc((int)516, (float)0.2f);
            GlStateManager.disableAlpha();
            GlStateManager.popMatrix();
            GlStateManager.disableBlend();
            Glow.input.framebufferClear();
            this.MC.getFramebuffer().bindFramebuffer(false);
        } else if (event instanceof EventGlowESP) {
            Glow.onGlowEvent((EventGlowESP)((EventGlowESP)event));
        }
        if (event instanceof EventPostRender2D) {
            this.checkSetup();
            this.pass.framebufferClear();
            this.output.framebufferClear();
            GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            GlStateManager.pushMatrix();
            GlStateManager.enableAlpha();
            GlStateManager.alphaFunc((int)516, (float)0.0f);
            GlStateManager.blendFunc((int)770, (int)771);
            GlStateManager.enableTexture2D();
            sr = new ScaledResolution(this.MC);
            double screenWidth = sr.getScaledWidth_double();
            double screenHeight = sr.getScaledHeight_double();
            GlStateManager.setActiveTexture((int)33984);
            this.outlineProgram.init();
            GL20.glUniform2f((int)this.outlineProgram.uniform("texelSize"), (float)(1.0f / (float)this.MC.displayWidth), (float)(1.0f / (float)this.MC.displayHeight));
            GL20.glUniform1i((int)this.outlineProgram.uniform("texture"), (int)0);
            float width = (float)Aqua.setmgr.getSetting("ESPOutlineWidth").getCurrentNumber();
            GL20.glUniform1f((int)this.outlineProgram.uniform("outline_width"), (float)width);
            int[] rgb = Aqua.setmgr.getSetting("ESPAstolfoColors").isState() ? ESP.getRGB(ESP.SkyRainbow(20, 1.0f, 0.5f).getRGB()) : (Aqua.setmgr.getSetting("ESPRainbow").isState() ? ESP.getRGB(ESP.rainbowESP(0)) : (Aqua.setmgr.getSetting("ESPClientColor").isState() ? ESP.getRGB(this.getColor()) : ESP.getRGB(this.getColor2())));
            GL20.glUniform3f((int)this.outlineProgram.uniform("mixColor"), (float)((float)rgb[0] / 255.0f), (float)((float)rgb[1] / 255.0f), (float)((float)rgb[2] / 255.0f));
            this.doOutlinePass(0, this.input.framebufferTexture, this.output, (int)screenWidth, (int)screenHeight);
            this.outlineProgram.uninit();
            ESP.drawGlowESP(() -> this.drawTexturedQuad1(this.output.framebufferTexture, screenWidth, screenHeight), false);
            ShaderStencilUtil.initStencil();
            ShaderStencilUtil.bindWriteStencilBuffer();
            this.drawTexturedQuad(this.input.framebufferTexture, screenWidth, screenHeight);
            ShaderStencilUtil.bindReadStencilBuffer((int)0);
            this.drawTexturedQuad(this.output.framebufferTexture, screenWidth, screenHeight);
            ShaderStencilUtil.uninitStencilBuffer();
            GlStateManager.bindTexture((int)0);
            GlStateManager.alphaFunc((int)516, (float)0.2f);
            GlStateManager.disableAlpha();
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
            this.input.framebufferClear();
            mc.getFramebuffer().bindFramebuffer(false);
        }
    }

    public void doOutlinePass(int pass, int texture, Framebuffer out, int width, int height) {
        out.framebufferClear();
        out.bindFramebuffer(false);
        GL20.glUniform2f((int)this.outlineProgram.uniform("direction"), (float)(1 - pass), (float)pass);
        GL11.glBindTexture((int)3553, (int)texture);
        this.outlineProgram.doRenderPass((float)width, (float)height);
    }

    public void checkSetup() {
        this.input.checkSetup(this.MC.displayWidth, this.MC.displayHeight);
        this.pass.checkSetup(this.MC.displayWidth, this.MC.displayHeight);
        this.output.checkSetup(this.MC.displayWidth, this.MC.displayHeight);
    }

    private void drawTexturedQuad(int texture, double width, double height) {
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

    public int getColor2() {
        try {
            return Aqua.setmgr.getSetting("ESPColor").getColor();
        }
        catch (Exception e) {
            return Color.white.getRGB();
        }
    }

    public int getColor() {
        try {
            return Aqua.setmgr.getSetting("HUDColor").getColor();
        }
        catch (Exception e) {
            return Color.white.getRGB();
        }
    }

    public static int[] getRGB(int hex) {
        int a = hex >> 24 & 0xFF;
        int r = hex >> 16 & 0xFF;
        int g = hex >> 8 & 0xFF;
        int b = hex & 0xFF;
        return new int[]{r, g, b, a};
    }

    public static int rainbowESP(int delay) {
        float rainbowSpeed = 25.0f;
        double rainbowState = Math.ceil((double)(System.currentTimeMillis() + (long)delay)) / 25.0;
        return Color.getHSBColor((float)((float)((rainbowState %= 360.0) / 360.0)), (float)0.9f, (float)1.0f).getRGB();
    }

    public static Color SkyRainbow(int counter, float bright, float st) {
        double d;
        double v1 = Math.ceil((double)(System.currentTimeMillis() + (long)counter * 109L)) / 6.0;
        return Color.getHSBColor((float)((double)((float)(d / 360.0)) < 0.5 ? -((float)(v1 / 360.0)) : (float)((v1 %= 360.0) / 360.0)), (float)st, (float)bright);
    }

    public void drawHizzyESP() {
        for (Object o : ESP.mc.theWorld.loadedEntityList) {
            Entity e = (Entity)o;
            if (!(e instanceof EntityPlayer) || e == ESP.mc.thePlayer) continue;
            double x = e.lastTickPosX + (e.posX - e.lastTickPosX) * (double)ESP.mc.timer.renderPartialTicks - RenderManager.renderPosX;
            double y = e.lastTickPosY + (e.posY - e.lastTickPosY) * (double)ESP.mc.timer.renderPartialTicks - RenderManager.renderPosY;
            double z = e.lastTickPosZ + (e.posZ - e.lastTickPosZ) * (double)ESP.mc.timer.renderPartialTicks - RenderManager.renderPosZ;
            GL11.glPushMatrix();
            GL11.glTranslated((double)x, (double)(y - 0.2), (double)z);
            GL11.glScalef((float)0.03f, (float)0.03f, (float)0.03f);
            GL11.glRotated((double)(-ESP.mc.getRenderManager().playerViewY), (double)0.0, (double)1.0, (double)0.0);
            GlStateManager.disableDepth();
            GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            mc.getTextureManager().bindTexture(new ResourceLocation("Aqua/gui/max.png"));
            Gui.drawModalRectWithCustomSizedTexture((int)50, (int)90, (float)0.0f, (float)0.0f, (int)-100, (int)-100, (float)-100.0f, (float)-100.0f);
            GlStateManager.enableDepth();
            GL11.glPopMatrix();
        }
    }
}
