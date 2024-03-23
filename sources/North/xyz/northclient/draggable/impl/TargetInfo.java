package xyz.northclient.draggable.impl;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderSkeleton;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;
import xyz.northclient.NorthSingleton;
import xyz.northclient.UIHook;
import xyz.northclient.draggable.AbstractDraggable;
import xyz.northclient.features.EventLink;
import xyz.northclient.features.events.ShaderEvent;
import xyz.northclient.features.modules.ESP2D;
import xyz.northclient.features.modules.KillAura;
import xyz.northclient.features.values.BoolValue;
import xyz.northclient.features.values.ModeValue;
import xyz.northclient.theme.ColorUtil;
import xyz.northclient.util.animations.util.Easings;
import xyz.northclient.util.font.FontUtil;
import xyz.northclient.util.shader.RectUtil;
import xyz.northclient.util.shader.StencilUtil;
import xyz.northclient.util.shader.impl.RenderCallback;

import java.awt.*;
import java.util.ArrayList;

public class TargetInfo extends AbstractDraggable {
    public EntityLivingBase Target;

    public ModeValue fontMode = new ModeValue("Font", this)
            .add(new Watermark.StringMode("Product Sans", this))
            .add(new Watermark.StringMode("Minecraft", this))
            .add(new Watermark.StringMode("Exhibition", this))
            .add(new Watermark.StringMode("SF", this))
            .setDefault("Minecraft");

    public ModeValue mode = new ModeValue("Mode", this)
            .add(new Watermark.StringMode("Asyncware", this))
            .add(new Watermark.StringMode("Nova", this))
            .add(new Watermark.StringMode("Groszus", this))
            .add(new Watermark.StringMode("Astolfo",this))
            .setDefault("Asyncware");

    public BoolValue track = new BoolValue("Track", this)
            .setDefault(false);
    public BoolValue alwaysFullHP = new BoolValue("Always full HP", this)
            .setDefault(false);
    public TargetInfo() {
        super(false);
    }


    @Override
    public void Init() {
        //Initable position
        X = 128;
        Y = 128;
        AllowRender = true;
    }


    public ArrayList<RenderCallback> blooms = new ArrayList<>();
    public ArrayList<RenderCallback> blurs = new ArrayList<>();

    public Vector4f track(float partialTicks) {
        final double renderX = mc.getRenderManager().renderPosX;
        final double renderY = mc.getRenderManager().renderPosY;
        final double renderZ = mc.getRenderManager().renderPosZ;
        final int factor = new ScaledResolution(mc).getScaleFactor();
        return ESP2D.calc(Target, partialTicks, renderX, renderY, renderZ, factor);
    }

    //<inheritdoc /> (From AbstractDraggable)
    @Override
    public Vector2f Render() {
        UIHook.ArraylistFont font = new UIHook.MinecraftArraylistFont(mc.exhiFontRendererObj);
        switch (fontMode.get().getName()) {
            case "Product Sans":
                font = new UIHook.CustomArraylistFont(FontUtil.DefaultSmall);
                break;
            case "Minecraft":
                font = new UIHook.MinecraftArraylistFont(mc.fontRendererObj);
                break;
            case "Exhibition":
                font = new UIHook.MinecraftArraylistFont(mc.exhiFontRendererObj);
                break;
            case "SF":
                font = new UIHook.CustomArraylistFont(FontUtil.SFProRegular);
                break;
            default:
                new UIHook.MinecraftArraylistFont(mc.exhiFontRendererObj);
                break;
        }


        Target = KillAura.target;
        if (mc.currentScreen instanceof GuiChat) {
            Target = mc.thePlayer;
        }
        if (Target == null)
            return new Vector2f(0, 0);
        GlStateManager.pushMatrix();
        if (track.get() && Target != mc.thePlayer) {
            Vector4f calc = track(mc.timer.renderPartialTicks);
            GlStateManager.translate(calc.x, calc.y + 50, 1);
        } else {
            GlStateManager.translate(X, Y, 0);
        }

        setSuffix(mode.get().getName());

        int health, maxHealth;

        switch (mode.get().getName()) {
            case "Asyncware":
                Gui.drawRect(0, 0, 130, 60, new Color(0, 0, 0, 200).getRGB());

                font.drawString(Target.getName(), 10, 10, -1);
                Gui.drawRect(0, 58, (int) Target.pies50.getValue(), 60, -1);

                health = (int) Target.getHealth() * 130;
                maxHealth = (int) Target.getMaxHealth();

                if (alwaysFullHP.get().booleanValue()) {
                    health = 20 * 125;
                    maxHealth = 20;
                }

                Target.pies50.update();
                Target.pies50.animate((double) health / maxHealth, 0.15f);

                GlStateManager.popMatrix();
                return new Vector2f(130, 60);
            case "Nova":
                RectUtil.drawBloom(0, 0, 130, 50, 16, new Color(27, 21, 20, 190));
                RectUtil.drawRoundedRect(0, 0, 130, 50, 10, new Color(27, 21, 20, 190));
                drawroundedHead(Target, 0 + 8, 0 + 8, 32, 6);
                font.drawStringWithShadow(Target.getName(), 0 + 49, 0 + 10, -1);

                health = (int) Target.getHealth() * 69;
                maxHealth = (int) Target.getMaxHealth();

                if (alwaysFullHP.get().booleanValue()) {
                    health = 20 * 125;
                    maxHealth = 20;
                }

                Target.pies50.update();
                Target.pies50.animate((int) (health / maxHealth), 0.2, Easings.SINE_OUT);

                int width = (int) Target.pies50.getValue();

                RectUtil.drawRoundedRect(0 + 49, 0 + 30, 69, 5, 1, new Color(38, 37, 34));

                RectUtil.drawBloom(49, 30, width, 5, 16, NorthSingleton.INSTANCE.getUiHook().getTheme().getBoolClickguiTheme());
                RectUtil.drawRoundedRect(0 + 49, 0 + 30, width, 5, 1, NorthSingleton.INSTANCE.getUiHook().getTheme().getBoolClickguiTheme());
                Gui.drawRect(0, 0, 0, 0, 0);

                GlStateManager.popMatrix();
                return new Vector2f(130, 50);
            case "Groszus":
                StencilUtil.initStencil();
                StencilUtil.bindWriteStencilBuffer();
                RectUtil.drawRoundedBloom(0 + 6, 0 + 6, 128, 38, 2, 7, NorthSingleton.INSTANCE.getUiHook().getTheme().getBoolClickguiTheme());
                StencilUtil.readStencilBufferExclude();
                RectUtil.drawRoundedBloom(0 + 2, 0 + 2, 137, 47, 2, 50, NorthSingleton.INSTANCE.getUiHook().getTheme().getBoolClickguiTheme().darker());
                StencilUtil.uninitStencilBuffer();

                RectUtil.drawRoundedRect(0, 0, 140, 50, 7, new Color(20, 20, 20, 140));

                drawroundedHead(Target, 13, 6, 28, 6);
                new UIHook.CustomArraylistFont(FontUtil.DefaultSmallBold).drawStringWithShadow(Target.getName(), 49, 10, -1);

                health = (int) Target.getHealth() * 125;
                maxHealth = (int) Target.getMaxHealth();

                if (alwaysFullHP.get().booleanValue()) {
                    health = 20 * 125;
                    maxHealth = 20;
                }

                font.drawStringWithShadow(Math.round((float) health / 125) + "HP - Dist: " + Math.round(mc.thePlayer.getDistanceToEntity(Target)), 49, 25, -1);

                Target.pies50.update();
                Target.pies50.animate((int) (health / maxHealth), 0.2, Easings.SINE_OUT);

                RectUtil.drawRoundedRect(7, 37, 125, 8, 3, new Color(30, 30, 30));
                RectUtil.drawBloom(7, 37, (int) Target.pies50.getValue(), 8, 16, NorthSingleton.INSTANCE.getUiHook().getTheme().getBoolClickguiTheme());
                RectUtil.drawRoundedRect(7, 37, (int) Target.pies50.getValue(), 8, 3, NorthSingleton.INSTANCE.getUiHook().getTheme().getBoolClickguiTheme());
                Gui.drawRect(0, 0, 0, 0, 0);

                GlStateManager.popMatrix();
                return new Vector2f(130, 50);
            case "Astolfo":
                Target.pies50.update();
                Target.pies50.animate(Target.getHealth(),0.15f, Easings.SINE_OUT);

                float animProcess = (float) Target.pies50.getValue();

                GlStateManager.pushMatrix();
                GlStateManager.scale(0.9,0.9,0.9);

                Gui.drawRect(0,0,0+200,0+65,new Color(0,0,0,120).getRGB());
                mc.fontRendererObj.drawStringWithShadow("",0,0,-1);
                GuiInventory.drawEntityOnScreen(0+20,0+60,28,-30,-30,Target);
                mc.fontRendererObj.drawStringWithShadow(Target.getName(),0+50,0+10,-1);
                GlStateManager.pushMatrix();
                GlStateManager.translate(0,0,0);
                GlStateManager.scale(2.5,2.5,0);
                GlStateManager.translate(-0,-0,0);
                mc.fontRendererObj.drawStringWithShadow(String.valueOf(Math.round(Target.getHealth())) + "❤",0+20,0+10,ColorUtil.darker(ColorUtil.GetColor(0),1f));
                GlStateManager.popMatrix();
                double percent = (animProcess * 140) / Target.getMaxHealth();

                int x = 50;
                int y = 48;
                Gui.drawRect(0+x,0+y,0+x+140,0+y+10,new Color(0,0,0,200).getRGB());
                Gui.drawRect(0+x,0+y, (int) (0+x+percent),0+y+10, ColorUtil.darker(ColorUtil.GetColor(0),0.4f));
                Gui.drawRect(0+x,0+y, (int) (0+x+percent-5),0+y+10, ColorUtil.GetColor(0));
                GlStateManager.popMatrix();
                GlStateManager.popMatrix();
                return new Vector2f(200,65);
            default:
                return new Vector2f(0, 0);
        }
    }

    @EventLink
    public void onShader(ShaderEvent event) {
        if (event.getStage() == ShaderEvent.ShaderStage.BLOOM) {
            Gui.drawRect(0, 0, 100, 100, -1);
            blooms.forEach(RenderCallback::render);
        }
        blooms.clear();
    }

    @Override
    public String getDraggableName() {
        return "Target Info";
    }

    public static void drawroundedHead(EntityLivingBase ent, final double x, final double y, final double size, final double round) {
        StencilUtil.initStencil();
        StencilUtil.bindWriteStencilBuffer();
        RectUtil.cipaorangutana((float) x, (float) y, (float) (size), (float) (size), (float) round, Color.white.getRGB());
        StencilUtil.bindReadStencilBuffer(1);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0.0F);
        GlStateManager.enableTexture2D();

        ResourceLocation res = ent instanceof EntityPlayer ? ((AbstractClientPlayer) ent).getLocationSkin() : RenderSkeleton.getEntityTexture2();

        Minecraft.getMinecraft().getTextureManager().bindTexture(res);

        GL11.glColor4f(1, 1, 1, 1);
        Gui.drawScaledCustomSizeModalRect((int) x, (int) y, 4F, 4F, 4, 4, (int) size, (int) size, 32, 32);
        GlStateManager.disableBlend();
        StencilUtil.uninitStencilBuffer();
    }

    public static void drawroundedHeadRes(ResourceLocation ent, final double x, final double y, final double size, final double round) {
        StencilUtil.initStencil();
        StencilUtil.bindWriteStencilBuffer();
        RectUtil.cipaorangutana((float) x, (float) y, (float) (size), (float) (size), (float) round, Color.white.getRGB());
        StencilUtil.bindReadStencilBuffer(1);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0.0F);
        GlStateManager.enableTexture2D();


        Minecraft.getMinecraft().getTextureManager().bindTexture(ent);

        GL11.glColor4f(1, 1, 1, 1);
        Gui.drawScaledCustomSizeModalRect((int) x, (int) y, 4F, 4F, 4, 4, (int) size, (int) size, 32, 32);
        GlStateManager.disableBlend();
        StencilUtil.uninitStencilBuffer();
    }


    public static void drawFace(double d, double f, int size, EntityLivingBase target) {
        if (target instanceof AbstractClientPlayer) {
            try {
                GL11.glPushMatrix();
                ResourceLocation skin = ((AbstractClientPlayer) target).getLocationSkin();
                Minecraft.getMinecraft().getTextureManager().bindTexture(skin);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glColor4f(1, 1, 1, 1);
                Gui.drawScaledCustomSizeModalRect((int) d, (int) f, 25, 25, 25, 25, size, size, 200, 200);
                //GL11.glDisable(GL11.GL_BLEND);
                GL11.glPopMatrix();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            GlStateManager.pushMatrix();
            Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("PlayerModel.png"));
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glColor4f(1, 1, 1, 1);
            Gui.drawScaledCustomSizeModalRect((int) d, (int) f, 15, 27, 512, 512, size, size, 517, 534);
            //GL11.glDisable(GL11.GL_BLEND);
            GL11.glPopMatrix();
        }
    }
}
