package intent.AquaDev.aqua.modules.visual;

import de.Hero.settings.Setting;
import events.Event;
import events.listeners.EventPostRender2D;
import events.listeners.EventRender2D;
import intent.AquaDev.aqua.Aqua;
import intent.AquaDev.aqua.modules.Category;
import intent.AquaDev.aqua.modules.Module;
import intent.AquaDev.aqua.modules.combat.Killaura;
import intent.AquaDev.aqua.modules.visual.Arraylist;
import intent.AquaDev.aqua.modules.visual.Blur;
import intent.AquaDev.aqua.modules.visual.ESP;
import intent.AquaDev.aqua.modules.visual.Shadow;
import intent.AquaDev.aqua.utils.RenderUtil;
import intent.AquaDev.aqua.utils.StencilUtil;
import java.awt.Color;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class TargetHUD
extends Module {
    float lostHealthPercentage = 0.0f;
    float lastHealthPercentage = 0.0f;

    public TargetHUD() {
        super("TargetHUD", "TargetHUD", 0, Category.Visual);
        Aqua.setmgr.register(new Setting("ClientColor", (Module)this, true));
        Aqua.setmgr.register(new Setting("CornerRadius", (Module)this, 4.0, 0.0, 12.0, false));
        Aqua.setmgr.register(new Setting("Mode", (Module)this, "Glow", new String[]{"Glow", "Shadow"}));
        Aqua.setmgr.register(new Setting("RenderMode", (Module)this, "2D", new String[]{"2D", "Rotation"}));
        Aqua.setmgr.register(new Setting("TargetHUDMode", (Module)this, "Glow", new String[]{"1", "2", "NovolineOld", "Classic", "Hanabi", "Rise", "RiseGlow"}));
        Aqua.setmgr.register(new Setting("Color", (Module)this));
    }

    public void onEnable() {
        super.onEnable();
    }

    public void onDisable() {
        super.onDisable();
    }

    public void onEvent(Event e) {
        if (e instanceof EventPostRender2D && Killaura.target != null) {
            if (Aqua.setmgr.getSetting("TargetHUDTargetHUDMode").getCurrentMode().equalsIgnoreCase("Classic")) {
                if (Aqua.setmgr.getSetting("TargetHUDRenderMode").getCurrentMode().equalsIgnoreCase("Rotation")) {
                    this.drawTargetHUDClassicFollowing((EntityLivingBase)Killaura.target);
                }
                if (!Aqua.setmgr.getSetting("TargetHUDRenderMode").getCurrentMode().equalsIgnoreCase("Rotation")) {
                    this.drawTargetHUDClassic((EntityLivingBase)Killaura.target);
                }
            }
            if (Aqua.setmgr.getSetting("TargetHUDTargetHUDMode").getCurrentMode().equalsIgnoreCase("NovolineOld")) {
                if (Aqua.setmgr.getSetting("TargetHUDRenderMode").getCurrentMode().equalsIgnoreCase("Rotation")) {
                    this.drawTargetHUDOldNovolineFollowing((EntityLivingBase)Killaura.target);
                }
                if (!Aqua.setmgr.getSetting("TargetHUDRenderMode").getCurrentMode().equalsIgnoreCase("Rotation")) {
                    this.drawTargetHUDOldNovoline((EntityLivingBase)Killaura.target);
                }
            }
            if (Aqua.setmgr.getSetting("TargetHUDTargetHUDMode").getCurrentMode().equalsIgnoreCase("Rise")) {
                this.drawTargetHUDRise((EntityLivingBase)Killaura.target);
            }
            if (Aqua.setmgr.getSetting("TargetHUDTargetHUDMode").getCurrentMode().equalsIgnoreCase("RiseGlow")) {
                this.drawTargetHUDRiseGlow((EntityLivingBase)Killaura.target);
            }
            if (Aqua.setmgr.getSetting("TargetHUDTargetHUDMode").getCurrentMode().equalsIgnoreCase("Hanabi")) {
                this.drawTargetHUDHanabi((EntityLivingBase)Killaura.target);
            }
            if (Aqua.setmgr.getSetting("TargetHUDTargetHUDMode").getCurrentMode().equalsIgnoreCase("1")) {
                this.drawTargetHUD((EntityLivingBase)Killaura.target);
            }
            if (Aqua.setmgr.getSetting("TargetHUDTargetHUDMode").getCurrentMode().equalsIgnoreCase("2")) {
                this.drawTargetHUD2((EntityLivingBase)Killaura.target);
            }
        }
        if (e instanceof EventRender2D && Killaura.target != null) {
            if (Aqua.setmgr.getSetting("TargetHUDTargetHUDMode").getCurrentMode().equalsIgnoreCase("1")) {
                this.drawTargetHUDShaders((EntityLivingBase)Killaura.target);
            }
            if (Aqua.setmgr.getSetting("TargetHUDTargetHUDMode").getCurrentMode().equalsIgnoreCase("Rise") || Aqua.setmgr.getSetting("TargetHUDTargetHUDMode").getCurrentMode().equalsIgnoreCase("RiseGlow")) {
                this.drawTargetHUDRiseShaders((EntityLivingBase)Killaura.target);
            }
            if (Aqua.setmgr.getSetting("TargetHUDTargetHUDMode").getCurrentMode().equalsIgnoreCase("2")) {
                this.drawTargetHUD2Blur((EntityLivingBase)Killaura.target);
            }
        }
    }

    public void drawTargetHUDClassic(EntityLivingBase target1) {
        ScaledResolution s = new ScaledResolution(mc);
        float cornerRadius = (float)Aqua.setmgr.getSetting("TargetHUDCornerRadius").getCurrentNumber();
        Color backgroundColor = new Color(0, 0, 0, 120);
        Color emptyBarColor = new Color(59, 59, 59, 160);
        Color healthBarColor = Color.green;
        Color distBarColor = new Color(20, 81, 208);
        GL11.glPushMatrix();
        int left = s.getScaledWidth() / 2 + 5 + 62;
        int right2 = 142;
        int right = s.getScaledWidth() / 2 + right2;
        int right3 = 80 + Aqua.INSTANCE.comfortaa3.getStringWidth(target1.getName()) + 29;
        int top = s.getScaledHeight() / 2 - 25 + 70;
        int bottom = s.getScaledHeight() / 2 + 25 + 70;
        float curTargetHealth = target1.getHealth();
        float healthProcent = target1.getHealth() / target1.getMaxHealth();
        if (this.lastHealthPercentage != healthProcent) {
            this.lostHealthPercentage += this.lastHealthPercentage - healthProcent;
        }
        this.lastHealthPercentage = healthProcent;
        float finalHealthPercentage = healthProcent + this.lostHealthPercentage;
        this.lostHealthPercentage = Math.max((float)0.0f, (float)(this.lostHealthPercentage - this.lostHealthPercentage / 20.0f));
        float calculatedHealth = finalHealthPercentage;
        int rectRight = right + Aqua.INSTANCE.comfortaa3.getStringWidth(target1.getName()) / 2 - 5;
        float healthPos = calculatedHealth * (float)right3;
        RenderUtil.drawRoundedRect2Alpha((double)(left - 8), (double)(top + 9), (double)((float)(right + Aqua.INSTANCE.comfortaa3.getStringWidth(target1.getName())) - (float)s.getScaledWidth() / 2.0f - 20.0f), (double)((float)bottom / 6.0f - 6.0f), (double)0.0, (Color)Color.black);
        RenderUtil.drawRoundedRect2Alpha((double)(left - 7), (double)(top + 10), (double)((float)(right + Aqua.INSTANCE.comfortaa3.getStringWidth(target1.getName())) - (float)s.getScaledWidth() / 2.0f - 22.0f), (double)((float)bottom / 6.0f - 8.0f), (double)0.0, (Color)new Color(30, 30, 30, 255));
        if (this.lastHealthPercentage != healthProcent) {
            this.lostHealthPercentage += this.lastHealthPercentage - healthProcent;
        }
        this.lastHealthPercentage = healthProcent;
        this.lostHealthPercentage = Math.max((float)0.0f, (float)(this.lostHealthPercentage - this.lostHealthPercentage / 20.0f));
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        int[] color = Aqua.setmgr.getSetting("TargetHUDClientColor").isState() ? ESP.getRGB((int)this.getColor()) : ESP.getRGB((int)this.getColor2());
        RenderUtil.drawRoundedRect((double)(left - 2), (double)(bottom + 2), (double)healthPos, (double)((float)bottom / 15.0f - 19.0f), (double)0.0, (int)new Color(color[0], color[1], color[2]).getRGB());
        Aqua.INSTANCE.comfortaa3.drawString(target1.getName(), (float)(left + 50), (float)(top + 15), -1);
        TargetHUD.mc.fontRendererObj.drawString("\u2764", left + 50, top + 32, -1);
        Aqua.INSTANCE.comfortaa3.drawString("Health : " + Math.round((float)curTargetHealth) + ".0", (float)(left + 60), (float)(bottom - 19), -1);
        List NetworkMoment = GuiPlayerTabOverlay.field_175252_a.sortedCopy((Iterable)TargetHUD.mc.thePlayer.sendQueue.getPlayerInfoMap());
        for (Object nextObject : NetworkMoment) {
            NetworkPlayerInfo networkPlayerInfo = (NetworkPlayerInfo)nextObject;
            if (TargetHUD.mc.theWorld.getPlayerEntityByUUID(networkPlayerInfo.getGameProfile().getId()) != target1) continue;
            GlStateManager.enableCull();
            mc.getTextureManager().bindTexture(networkPlayerInfo.getLocationSkin());
            GlStateManager.pushMatrix();
            if (target1.hurtTime != 0) {
                GL11.glColor4f((float)1.0f, (float)0.0f, (float)0.0f, (float)1.0f);
            }
            Gui.drawScaledCustomSizeModalRect((int)(s.getScaledWidth() / 2 + 10 + 55), (int)(s.getScaledHeight() / 2 - 5 + 65), (float)8.0f, (float)8.0f, (int)8, (int)8, (int)34, (int)34, (float)64.0f, (float)66.0f);
            GlStateManager.popMatrix();
        }
        GL11.glPopMatrix();
    }

    public void drawTargetHUDRiseShaders(EntityLivingBase target1) {
        ScaledResolution s = new ScaledResolution(mc);
        float nameWidth = 38.0f;
        float posX = (float)TargetHUD.mc.displayWidth / (float)(TargetHUD.mc.gameSettings.guiScale * 2) - 38.0f - 45.0f + 80.0f;
        float posY = (float)TargetHUD.mc.displayHeight / (float)(TargetHUD.mc.gameSettings.guiScale * 2) + 20.0f + 50.0f;
        int left = s.getScaledWidth() / 2 + 25;
        int right2 = 142;
        int top = s.getScaledHeight() / 2 - 25 + 70;
        Blur.drawBlurred(() -> RenderUtil.drawRoundedRect((double)(posX + 38.0f + 2.0f), (double)(posY - 34.0f), (double)(129.0f + (float)Aqua.INSTANCE.comfortaa3.getStringWidth(target1.getName()) / 2.0f - 18.0f), (double)40.0, (double)8.0, (int)new Color(0, 0, 0, 0).getRGB()), (boolean)false);
    }

    public void drawTargetHUDRise(EntityLivingBase target1) {
        ScaledResolution sr = new ScaledResolution(mc);
        ScaledResolution s = new ScaledResolution(mc);
        int left = s.getScaledWidth() / 2 + 25;
        int right2 = 142;
        int right = s.getScaledWidth() / 2 + right2;
        int right3 = 80 + Aqua.INSTANCE.comfortaa3.getStringWidth(target1.getName()) / 2 - 15;
        int top = s.getScaledHeight() / 2 - 25 + 70;
        int bottom = s.getScaledHeight() / 2 + 25 + 48;
        float curTargetHealth = target1.getHealth();
        float maxTargetHealth = target1.getMaxHealth();
        float healthProcent = target1.getHealth() / target1.getMaxHealth();
        if (this.lastHealthPercentage != healthProcent) {
            this.lostHealthPercentage += this.lastHealthPercentage - healthProcent;
        }
        this.lastHealthPercentage = healthProcent;
        float finalHealthPercentage = healthProcent + this.lostHealthPercentage;
        this.lostHealthPercentage = Math.max((float)0.0f, (float)(this.lostHealthPercentage - this.lostHealthPercentage / 20.0f));
        float calculatedHealth = finalHealthPercentage;
        int rectRight = right + Aqua.INSTANCE.comfortaa3.getStringWidth(target1.getName()) / 2 - 5;
        float healthPos = calculatedHealth * (float)right3;
        float nameWidth = 38.0f;
        int scaleOffset = (int)((float)((EntityPlayer)target1).hurtTime * 0.35f);
        float posX = (float)TargetHUD.mc.displayWidth / (float)(TargetHUD.mc.gameSettings.guiScale * 2) - 38.0f - 45.0f + 80.0f;
        float posY = (float)TargetHUD.mc.displayHeight / (float)(TargetHUD.mc.gameSettings.guiScale * 2) + 20.0f + 50.0f;
        RenderUtil.drawRoundedRect2Alpha((double)(posX + 38.0f + 2.0f), (double)(posY - 34.0f), (double)(129.0f + (float)Aqua.INSTANCE.comfortaa3.getStringWidth(target1.getName()) / 2.0f - 18.0f), (double)40.0, (double)8.0, (Color)new Color(0, 0, 0, 20));
        if (target1 instanceof AbstractClientPlayer) {
            StencilUtil.write((boolean)false);
            RenderUtil.circle((double)(posX + 38.0f + 6.0f + (float)scaleOffset / 2.0f), (double)(posY - 34.0f + 5.0f + (float)scaleOffset / 2.0f), (double)(30 - scaleOffset), (Color)Color.BLACK);
            StencilUtil.erase((boolean)true);
            double offset = -(((AbstractClientPlayer)target1).hurtTime * 23);
            RenderUtil.color((Color)new Color(255, (int)(255.0 + offset), (int)(255.0 + offset)));
            EntityPlayer en = (EntityPlayer)target1;
            TargetHUD.renderPlayerModelTexture(posX + 38.0f + 6.0f + (float)scaleOffset / 2.0f, posY - 34.0f + 5.0f + (float)scaleOffset / 2.0f, 3.0f, 3.0f, 3, 3, 30 - scaleOffset, 30 - scaleOffset, 24.0f, 24.5f, (AbstractClientPlayer)en);
            GlStateManager.resetColor();
            StencilUtil.dispose();
        }
        float offset = 39.0f;
        float drawBarPosX = posX + 38.0f;
        int color = Aqua.setmgr.getSetting("HUDColor").getColor();
        RenderUtil.drawRoundedRect((double)(left + 50), (double)(bottom - 9), (double)healthPos, (double)((float)bottom / 15.0f - 16.0f), (double)2.0, (int)Aqua.setmgr.getSetting("HUDColor").getColor());
        Aqua.INSTANCE.comfortaa3.drawString(target1.getName(), (float)(left + 63), (float)top, -1);
    }

    public void drawTargetHUDRiseGlow(EntityLivingBase target1) {
        ScaledResolution sr = new ScaledResolution(mc);
        ScaledResolution s = new ScaledResolution(mc);
        int left = s.getScaledWidth() / 2 + 25;
        int right2 = 142;
        int right = s.getScaledWidth() / 2 + right2;
        int right3 = 80 + Aqua.INSTANCE.comfortaa3.getStringWidth(target1.getName()) / 2 - 15;
        int top = s.getScaledHeight() / 2 - 25 + 70;
        int bottom = s.getScaledHeight() / 2 + 25 + 48;
        float curTargetHealth = target1.getHealth();
        float maxTargetHealth = target1.getMaxHealth();
        float healthProcent = target1.getHealth() / target1.getMaxHealth();
        if (this.lastHealthPercentage != healthProcent) {
            this.lostHealthPercentage += this.lastHealthPercentage - healthProcent;
        }
        this.lastHealthPercentage = healthProcent;
        float finalHealthPercentage = healthProcent + this.lostHealthPercentage;
        this.lostHealthPercentage = Math.max((float)0.0f, (float)(this.lostHealthPercentage - this.lostHealthPercentage / 20.0f));
        float calculatedHealth = finalHealthPercentage;
        int rectRight = right + Aqua.INSTANCE.comfortaa3.getStringWidth(target1.getName()) / 2 - 5;
        float healthPos = calculatedHealth * (float)right3;
        float nameWidth = 38.0f;
        int scaleOffset = (int)((float)((EntityPlayer)target1).hurtTime * 0.35f);
        float posX = (float)TargetHUD.mc.displayWidth / (float)(TargetHUD.mc.gameSettings.guiScale * 2) - 38.0f - 45.0f + 80.0f;
        float posY = (float)TargetHUD.mc.displayHeight / (float)(TargetHUD.mc.gameSettings.guiScale * 2) + 20.0f + 50.0f;
        RenderUtil.drawRoundedRect2Alpha((double)(posX + 38.0f + 2.0f), (double)(posY - 34.0f), (double)(129.0f + (float)Aqua.INSTANCE.comfortaa3.getStringWidth(target1.getName()) / 2.0f - 18.0f), (double)40.0, (double)8.0, (Color)new Color(0, 0, 0, 20));
        if (target1 instanceof AbstractClientPlayer) {
            StencilUtil.write((boolean)false);
            RenderUtil.circle((double)(posX + 38.0f + 6.0f + (float)scaleOffset / 2.0f), (double)(posY - 34.0f + 5.0f + (float)scaleOffset / 2.0f), (double)(30 - scaleOffset), (Color)Color.BLACK);
            StencilUtil.erase((boolean)true);
            double offset = -(((AbstractClientPlayer)target1).hurtTime * 23);
            RenderUtil.color((Color)new Color(255, (int)(255.0 + offset), (int)(255.0 + offset)));
            EntityPlayer en = (EntityPlayer)target1;
            TargetHUD.renderPlayerModelTexture(posX + 38.0f + 6.0f + (float)scaleOffset / 2.0f, posY - 34.0f + 5.0f + (float)scaleOffset / 2.0f, 3.0f, 3.0f, 3, 3, 30 - scaleOffset, 30 - scaleOffset, 24.0f, 24.5f, (AbstractClientPlayer)en);
            GlStateManager.resetColor();
            StencilUtil.dispose();
        }
        float offset = 39.0f;
        float drawBarPosX = posX + 38.0f;
        int color = Aqua.setmgr.getSetting("HUDColor").getColor();
        Arraylist.drawGlowArray(() -> RenderUtil.drawRoundedRect((double)(left + 50), (double)(bottom - 9), (double)healthPos, (double)((float)bottom / 15.0f - 16.0f), (double)2.0, (int)Aqua.setmgr.getSetting("HUDColor").getColor()), (boolean)false);
        RenderUtil.drawRoundedRect((double)(left + 50), (double)(bottom - 9), (double)healthPos, (double)((float)bottom / 15.0f - 16.0f), (double)2.0, (int)Aqua.setmgr.getSetting("HUDColor").getColor());
        Arraylist.drawGlowArray(() -> Aqua.INSTANCE.comfortaa3.drawString(target1.getName(), (float)(left + 63), (float)top, -1), (boolean)false);
        Aqua.INSTANCE.comfortaa3.drawString(target1.getName(), (float)(left + 63), (float)top, -1);
    }

    public void drawTargetHUDClassicFollowing(EntityLivingBase target1) {
        ScaledResolution s = new ScaledResolution(mc);
        float cornerRadius = (float)Aqua.setmgr.getSetting("TargetHUDCornerRadius").getCurrentNumber();
        Color backgroundColor = new Color(0, 0, 0, 120);
        Color emptyBarColor = new Color(59, 59, 59, 160);
        Color healthBarColor = Color.green;
        Color distBarColor = new Color(20, 81, 208);
        GL11.glPushMatrix();
        int left = (int)((double)(s.getScaledWidth() / 2 - 140) - RenderUtil.interpolate((double)TargetHUD.mc.thePlayer.getRotationYawHead(), (double)TargetHUD.mc.thePlayer.rotationYawHead, (double)1.0));
        int right2 = 142;
        int right = s.getScaledWidth() / 2 + right2;
        int right3 = 80 + Aqua.INSTANCE.comfortaa3.getStringWidth(target1.getName()) + 29;
        int top = (int)((double)(s.getScaledHeight() / 2) + RenderUtil.interpolate((double)TargetHUD.mc.thePlayer.getRotationPitchHead(), (double)TargetHUD.mc.thePlayer.rotationPitchHead, (double)1.0) - 30.0);
        int bottom = s.getScaledHeight() / 2 + 25 + 70;
        float curTargetHealth = target1.getHealth();
        float healthProcent = target1.getHealth() / target1.getMaxHealth();
        if (this.lastHealthPercentage != healthProcent) {
            this.lostHealthPercentage += this.lastHealthPercentage - healthProcent;
        }
        this.lastHealthPercentage = healthProcent;
        float finalHealthPercentage = healthProcent + this.lostHealthPercentage;
        this.lostHealthPercentage = Math.max((float)0.0f, (float)(this.lostHealthPercentage - this.lostHealthPercentage / 20.0f));
        float calculatedHealth = finalHealthPercentage;
        int rectRight = right + Aqua.INSTANCE.comfortaa3.getStringWidth(target1.getName()) / 2 - 5;
        float healthPos = calculatedHealth * (float)right3;
        RenderUtil.drawRoundedRect2Alpha((double)(left - 8), (double)(top + 9), (double)((float)(right + Aqua.INSTANCE.comfortaa3.getStringWidth(target1.getName())) - (float)s.getScaledWidth() / 2.0f - 20.0f), (double)((float)bottom / 6.0f - 6.0f), (double)0.0, (Color)Color.black);
        RenderUtil.drawRoundedRect2Alpha((double)(left - 7), (double)(top + 10), (double)((float)(right + Aqua.INSTANCE.comfortaa3.getStringWidth(target1.getName())) - (float)s.getScaledWidth() / 2.0f - 22.0f), (double)((float)bottom / 6.0f - 8.0f), (double)0.0, (Color)new Color(30, 30, 30, 255));
        if (this.lastHealthPercentage != healthProcent) {
            this.lostHealthPercentage += this.lastHealthPercentage - healthProcent;
        }
        this.lastHealthPercentage = healthProcent;
        this.lostHealthPercentage = Math.max((float)0.0f, (float)(this.lostHealthPercentage - this.lostHealthPercentage / 20.0f));
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        int[] color = Aqua.setmgr.getSetting("TargetHUDClientColor").isState() ? ESP.getRGB((int)this.getColor()) : ESP.getRGB((int)this.getColor2());
        RenderUtil.drawRoundedRect((double)(left - 2), (double)(top + 50), (double)healthPos, (double)((float)bottom / 15.0f - 19.0f), (double)0.0, (int)new Color(color[0], color[1], color[2]).getRGB());
        Aqua.INSTANCE.comfortaa3.drawString(target1.getName(), (float)(left + 50), (float)(top + 15), -1);
        TargetHUD.mc.fontRendererObj.drawString("\u2764", left + 50, top + 32, -1);
        Aqua.INSTANCE.comfortaa3.drawString("Health : " + Math.round((float)curTargetHealth) + ".0", (float)(left + 60), (float)(top + 31), -1);
        List NetworkMoment = GuiPlayerTabOverlay.field_175252_a.sortedCopy((Iterable)TargetHUD.mc.thePlayer.sendQueue.getPlayerInfoMap());
        for (Object nextObject : NetworkMoment) {
            NetworkPlayerInfo networkPlayerInfo = (NetworkPlayerInfo)nextObject;
            if (TargetHUD.mc.theWorld.getPlayerEntityByUUID(networkPlayerInfo.getGameProfile().getId()) != target1) continue;
            GlStateManager.enableCull();
            mc.getTextureManager().bindTexture(networkPlayerInfo.getLocationSkin());
            GlStateManager.pushMatrix();
            if (target1.hurtTime != 0) {
                GL11.glColor4f((float)1.0f, (float)0.0f, (float)0.0f, (float)1.0f);
            }
            Gui.drawScaledCustomSizeModalRect((int)((int)((double)(s.getScaledWidth() / 2 - 142) - RenderUtil.interpolate((double)TargetHUD.mc.thePlayer.getRotationYawHead(), (double)TargetHUD.mc.thePlayer.rotationYawHead, (double)1.0))), (int)(top + 14), (float)8.0f, (float)8.0f, (int)8, (int)8, (int)34, (int)34, (float)64.0f, (float)66.0f);
            GlStateManager.popMatrix();
        }
        GL11.glPopMatrix();
    }

    public void drawTargetHUDOldNovoline(EntityLivingBase target1) {
        ScaledResolution s = new ScaledResolution(mc);
        float cornerRadius = (float)Aqua.setmgr.getSetting("TargetHUDCornerRadius").getCurrentNumber();
        Color backgroundColor = new Color(0, 0, 0, 120);
        Color emptyBarColor = new Color(59, 59, 59, 160);
        Color healthBarColor = Color.green;
        Color distBarColor = new Color(20, 81, 208);
        GL11.glPushMatrix();
        int left = s.getScaledWidth() / 2 + 5 + 62;
        int right2 = 142;
        int right = s.getScaledWidth() / 2 + right2;
        int right3 = Aqua.INSTANCE.comfortaa3.getStringWidth(target1.getName()) + 29;
        int top = s.getScaledHeight() / 2 - 25 + 70;
        int bottom = s.getScaledHeight() / 2 + 25 + 70;
        float curTargetHealth = target1.getHealth();
        float healthProcent = target1.getHealth() / target1.getMaxHealth();
        if (this.lastHealthPercentage != healthProcent) {
            this.lostHealthPercentage += this.lastHealthPercentage - healthProcent;
        }
        this.lastHealthPercentage = healthProcent;
        float finalHealthPercentage = healthProcent + this.lostHealthPercentage;
        this.lostHealthPercentage = Math.max((float)0.0f, (float)(this.lostHealthPercentage - this.lostHealthPercentage / 20.0f));
        float calculatedHealth = finalHealthPercentage;
        int rectRight = right + Aqua.INSTANCE.comfortaa3.getStringWidth(target1.getName()) / 2 - 5;
        float healthPos = calculatedHealth * (float)right3;
        RenderUtil.drawRoundedRect2Alpha((double)(left - 6), (double)(top + 12), (double)((float)(right + Aqua.INSTANCE.comfortaa3.getStringWidth(target1.getName())) - (float)s.getScaledWidth() / 2.0f - 62.0f), (double)((float)bottom / 6.0f - 18.0f), (double)0.0, (Color)Color.BLACK);
        RenderUtil.drawRoundedRect2Alpha((double)(left - 5), (double)(top + 13), (double)((float)(right + Aqua.INSTANCE.comfortaa3.getStringWidth(target1.getName())) - (float)s.getScaledWidth() / 2.0f - 64.0f), (double)((float)bottom / 6.0f - 20.0f), (double)0.0, (Color)new Color(45, 45, 45, 255));
        if (this.lastHealthPercentage != healthProcent) {
            this.lostHealthPercentage += this.lastHealthPercentage - healthProcent;
        }
        this.lastHealthPercentage = healthProcent;
        this.lostHealthPercentage = Math.max((float)0.0f, (float)(this.lostHealthPercentage - this.lostHealthPercentage / 20.0f));
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        int[] color = Aqua.setmgr.getSetting("TargetHUDClientColor").isState() ? ESP.getRGB((int)this.getColor()) : ESP.getRGB((int)this.getColor2());
        RenderUtil.drawRoundedRect((double)(left + 40), (double)(bottom - 20), (double)((float)(right + Aqua.INSTANCE.comfortaa3.getStringWidth(target1.getName())) - (float)s.getScaledWidth() / 2.0f - 113.0f), (double)((float)bottom / 15.0f - 11.0f), (double)0.0, (int)Color.black.getRGB());
        RenderUtil.drawRoundedRect((double)(left + 40), (double)(bottom - 20), (double)healthPos, (double)((float)bottom / 15.0f - 11.0f), (double)0.0, (int)new Color(color[0], color[1], color[2]).getRGB());
        TargetHUD.mc.fontRendererObj.drawStringWithShadow(target1.getName(), (float)(left + 40), (float)(top + 17), -1);
        List NetworkMoment = GuiPlayerTabOverlay.field_175252_a.sortedCopy((Iterable)TargetHUD.mc.thePlayer.sendQueue.getPlayerInfoMap());
        for (Object nextObject : NetworkMoment) {
            NetworkPlayerInfo networkPlayerInfo = (NetworkPlayerInfo)nextObject;
            if (TargetHUD.mc.theWorld.getPlayerEntityByUUID(networkPlayerInfo.getGameProfile().getId()) != target1) continue;
            GlStateManager.enableCull();
            mc.getTextureManager().bindTexture(networkPlayerInfo.getLocationSkin());
            GlStateManager.pushMatrix();
            if (target1.hurtTime != 0) {
                // empty if block
            }
            Gui.drawScaledCustomSizeModalRect((int)(s.getScaledWidth() / 2 + 10 + 54), (int)(s.getScaledHeight() / 2 - 5 + 64), (float)8.0f, (float)8.0f, (int)8, (int)8, (int)36, (int)36, (float)64.0f, (float)66.0f);
            GlStateManager.popMatrix();
        }
        GL11.glPopMatrix();
    }

    public void drawTargetHUDOldNovolineFollowing(EntityLivingBase target1) {
        ScaledResolution s = new ScaledResolution(mc);
        float cornerRadius = (float)Aqua.setmgr.getSetting("TargetHUDCornerRadius").getCurrentNumber();
        Color backgroundColor = new Color(0, 0, 0, 120);
        Color emptyBarColor = new Color(59, 59, 59, 160);
        Color healthBarColor = Color.green;
        Color distBarColor = new Color(20, 81, 208);
        GL11.glPushMatrix();
        float angle = TargetHUD.getAngle((Entity)target1) % 360.0f + 180.0f;
        int left = (int)((double)(s.getScaledWidth() / 2 - 140) - RenderUtil.interpolate((double)TargetHUD.mc.thePlayer.getRotationYawHead(), (double)TargetHUD.mc.thePlayer.rotationYawHead, (double)1.0));
        int right2 = 142;
        int right = s.getScaledWidth() / 2 + right2;
        int right3 = Aqua.INSTANCE.comfortaa3.getStringWidth(target1.getName()) + 29;
        int top = (int)((double)(s.getScaledHeight() / 2) + RenderUtil.interpolate((double)TargetHUD.mc.thePlayer.getRotationPitchHead(), (double)TargetHUD.mc.thePlayer.rotationPitchHead, (double)1.0) - 30.0);
        int bottom = s.getScaledHeight() / 2 + 25 + 60;
        float curTargetHealth = target1.getHealth();
        float healthProcent = target1.getHealth() / target1.getMaxHealth();
        if (this.lastHealthPercentage != healthProcent) {
            this.lostHealthPercentage += this.lastHealthPercentage - healthProcent;
        }
        this.lastHealthPercentage = healthProcent;
        float finalHealthPercentage = healthProcent + this.lostHealthPercentage;
        this.lostHealthPercentage = Math.max((float)0.0f, (float)(this.lostHealthPercentage - this.lostHealthPercentage / 20.0f));
        float calculatedHealth = finalHealthPercentage;
        int rectRight = right + Aqua.INSTANCE.comfortaa3.getStringWidth(target1.getName()) / 2 - 5;
        float healthPos = calculatedHealth * (float)right3;
        RenderUtil.drawRoundedRect2Alpha((double)(left - 6), (double)(top + 12), (double)((float)(right + Aqua.INSTANCE.comfortaa3.getStringWidth(target1.getName())) - (float)s.getScaledWidth() / 2.0f - 62.0f), (double)((float)bottom / 6.0f - 18.0f), (double)0.0, (Color)Color.BLACK);
        RenderUtil.drawRoundedRect2Alpha((double)(left - 5), (double)(top + 13), (double)((float)(right + Aqua.INSTANCE.comfortaa3.getStringWidth(target1.getName())) - (float)s.getScaledWidth() / 2.0f - 64.0f), (double)((float)bottom / 6.0f - 20.0f), (double)0.0, (Color)new Color(45, 45, 45, 255));
        if (this.lastHealthPercentage != healthProcent) {
            this.lostHealthPercentage += this.lastHealthPercentage - healthProcent;
        }
        this.lastHealthPercentage = healthProcent;
        this.lostHealthPercentage = Math.max((float)0.0f, (float)(this.lostHealthPercentage - this.lostHealthPercentage / 20.0f));
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        int[] color = Aqua.setmgr.getSetting("TargetHUDClientColor").isState() ? ESP.getRGB((int)this.getColor()) : ESP.getRGB((int)this.getColor2());
        RenderUtil.drawRoundedRect((double)(left + 40), (double)(top + 30), (double)((float)(right + Aqua.INSTANCE.comfortaa3.getStringWidth(target1.getName())) - (float)s.getScaledWidth() / 2.0f - 113.0f), (double)((float)bottom / 15.0f - 11.0f), (double)0.0, (int)Color.black.getRGB());
        RenderUtil.drawRoundedRect((double)(left + 40), (double)(top + 30), (double)healthPos, (double)((float)bottom / 15.0f - 11.0f), (double)0.0, (int)new Color(color[0], color[1], color[2]).getRGB());
        TargetHUD.mc.fontRendererObj.drawStringWithShadow(target1.getName(), (float)(left + 40), (float)(top + 17), -1);
        List NetworkMoment = GuiPlayerTabOverlay.field_175252_a.sortedCopy((Iterable)TargetHUD.mc.thePlayer.sendQueue.getPlayerInfoMap());
        for (Object nextObject : NetworkMoment) {
            NetworkPlayerInfo networkPlayerInfo = (NetworkPlayerInfo)nextObject;
            if (TargetHUD.mc.theWorld.getPlayerEntityByUUID(networkPlayerInfo.getGameProfile().getId()) != target1) continue;
            GlStateManager.enableCull();
            mc.getTextureManager().bindTexture(networkPlayerInfo.getLocationSkin());
            GlStateManager.pushMatrix();
            if (target1.hurtTime != 0) {
                // empty if block
            }
            Gui.drawScaledCustomSizeModalRect((int)((int)((double)(s.getScaledWidth() / 2 - 143) - RenderUtil.interpolate((double)TargetHUD.mc.thePlayer.getRotationYawHead(), (double)TargetHUD.mc.thePlayer.rotationYawHead, (double)1.0))), (int)(top + 14), (float)8.0f, (float)8.0f, (int)8, (int)8, (int)35, (int)34, (float)64.0f, (float)66.0f);
            GlStateManager.popMatrix();
        }
        GL11.glPopMatrix();
    }

    public void drawTargetHUD2(EntityLivingBase target1) {
        ScaledResolution s = new ScaledResolution(mc);
        float cornerRadius = (float)Aqua.setmgr.getSetting("TargetHUDCornerRadius").getCurrentNumber();
        Color backgroundColor = new Color(0, 0, 0, 120);
        Color emptyBarColor = new Color(59, 59, 59, 160);
        Color healthBarColor = Color.green;
        Color distBarColor = new Color(20, 81, 208);
        GL11.glPushMatrix();
        int left = s.getScaledWidth() / 2 + 5 + 62;
        int right2 = 142;
        int right = s.getScaledWidth() / 2 + right2;
        int right3 = 80 + Aqua.INSTANCE.comfortaa3.getStringWidth(target1.getName()) + 29;
        int top = s.getScaledHeight() / 2 - 25 + 70;
        int bottom = s.getScaledHeight() / 2 + 25 + 70;
        float curTargetHealth = target1.getHealth();
        float healthProcent = target1.getHealth() / target1.getMaxHealth();
        if (this.lastHealthPercentage != healthProcent) {
            this.lostHealthPercentage += this.lastHealthPercentage - healthProcent;
        }
        this.lastHealthPercentage = healthProcent;
        float finalHealthPercentage = healthProcent + this.lostHealthPercentage;
        this.lostHealthPercentage = Math.max((float)0.0f, (float)(this.lostHealthPercentage - this.lostHealthPercentage / 20.0f));
        float calculatedHealth = finalHealthPercentage;
        int rectRight = right + Aqua.INSTANCE.comfortaa3.getStringWidth(target1.getName()) / 2 - 5;
        float healthPos = calculatedHealth * (float)right3;
        RenderUtil.drawRoundedRect2Alpha((double)(left - 7), (double)(top + 10), (double)((float)(right + Aqua.INSTANCE.comfortaa3.getStringWidth(target1.getName())) - (float)s.getScaledWidth() / 2.0f - 22.0f), (double)((float)bottom / 6.0f - 8.0f), (double)cornerRadius, (Color)new Color(20, 20, 20, 90));
        if (this.lastHealthPercentage != healthProcent) {
            this.lostHealthPercentage += this.lastHealthPercentage - healthProcent;
        }
        this.lastHealthPercentage = healthProcent;
        this.lostHealthPercentage = Math.max((float)0.0f, (float)(this.lostHealthPercentage - this.lostHealthPercentage / 20.0f));
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        int[] color = Aqua.setmgr.getSetting("TargetHUDClientColor").isState() ? ESP.getRGB((int)this.getColor()) : ESP.getRGB((int)this.getColor2());
        RenderUtil.drawRoundedRect((double)(left - 2), (double)(bottom + 2), (double)healthPos, (double)((float)bottom / 15.0f - 19.0f), (double)0.0, (int)new Color(color[0], color[1], color[2]).getRGB());
        Aqua.INSTANCE.comfortaa3.drawString(target1.getName(), (float)(left + 50), (float)(top + 15), -1);
        TargetHUD.mc.fontRendererObj.drawString("\u2764", left + 50, top + 32, -1);
        Aqua.INSTANCE.comfortaa3.drawString("Health : " + Math.round((float)curTargetHealth) + ".0", (float)(left + 60), (float)(bottom - 19), -1);
        List NetworkMoment = GuiPlayerTabOverlay.field_175252_a.sortedCopy((Iterable)TargetHUD.mc.thePlayer.sendQueue.getPlayerInfoMap());
        for (Object nextObject : NetworkMoment) {
            NetworkPlayerInfo networkPlayerInfo = (NetworkPlayerInfo)nextObject;
            if (TargetHUD.mc.theWorld.getPlayerEntityByUUID(networkPlayerInfo.getGameProfile().getId()) != target1) continue;
            GlStateManager.enableCull();
            mc.getTextureManager().bindTexture(networkPlayerInfo.getLocationSkin());
            GlStateManager.pushMatrix();
            if (target1.hurtTime != 0) {
                // empty if block
            }
            Gui.drawScaledCustomSizeModalRect((int)(s.getScaledWidth() / 2 + 10 + 55), (int)(s.getScaledHeight() / 2 - 5 + 65), (float)8.0f, (float)8.0f, (int)8, (int)8, (int)34, (int)34, (float)64.0f, (float)66.0f);
            GlStateManager.popMatrix();
        }
        GL11.glPopMatrix();
    }

    public void drawTargetHUD2Blur(EntityLivingBase target1) {
        ScaledResolution s = new ScaledResolution(mc);
        float cornerRadius = (float)Aqua.setmgr.getSetting("TargetHUDCornerRadius").getCurrentNumber();
        int left = s.getScaledWidth() / 2 + 5 + 62;
        int right2 = 142;
        int right = s.getScaledWidth() / 2 + right2;
        int top = s.getScaledHeight() / 2 - 25 + 70;
        int bottom = s.getScaledHeight() / 2 + 25 + 70;
        float healthProcent = target1.getHealth() / target1.getMaxHealth();
        if (this.lastHealthPercentage != healthProcent) {
            this.lostHealthPercentage += this.lastHealthPercentage - healthProcent;
        }
        this.lastHealthPercentage = healthProcent;
        float finalHealthPercentage = healthProcent + this.lostHealthPercentage;
        this.lostHealthPercentage = Math.max((float)0.0f, (float)(this.lostHealthPercentage - this.lostHealthPercentage / 20.0f));
        float calculatedHealth = finalHealthPercentage;
        Blur.drawBlurred(() -> RenderUtil.drawRoundedRect2Alpha((double)(left - 7), (double)(top + 10), (double)((float)(right + Aqua.INSTANCE.comfortaa3.getStringWidth(target1.getName())) - (float)s.getScaledWidth() / 2.0f - 22.0f), (double)((float)bottom / 6.0f - 8.0f), (double)cornerRadius, (Color)new Color(20, 20, 20, 90)), (boolean)false);
    }

    public void drawTargetHUD(EntityLivingBase target1) {
        ScaledResolution s = new ScaledResolution(mc);
        float cornerRadius = (float)Aqua.setmgr.getSetting("TargetHUDCornerRadius").getCurrentNumber();
        Color backgroundColor = new Color(0, 0, 0, 120);
        Color emptyBarColor = new Color(59, 59, 59, 160);
        Color healthBarColor = Color.green;
        Color distBarColor = new Color(20, 81, 208);
        int left = s.getScaledWidth() / 2 + 5 + 62;
        int right2 = 142;
        int right = s.getScaledWidth() / 2 + right2;
        int right3 = 80 + Aqua.INSTANCE.comfortaa3.getStringWidth(target1.getName()) / 2 - 15;
        int top = s.getScaledHeight() / 2 - 25 + 70;
        int bottom = s.getScaledHeight() / 2 + 25 + 70;
        float curTargetHealth = target1.getHealth();
        float healthProcent = target1.getHealth() / target1.getMaxHealth();
        if (this.lastHealthPercentage != healthProcent) {
            this.lostHealthPercentage += this.lastHealthPercentage - healthProcent;
        }
        this.lastHealthPercentage = healthProcent;
        float finalHealthPercentage = healthProcent + this.lostHealthPercentage;
        this.lostHealthPercentage = Math.max((float)0.0f, (float)(this.lostHealthPercentage - this.lostHealthPercentage / 20.0f));
        float calculatedHealth = finalHealthPercentage;
        int rectRight = right + Aqua.INSTANCE.comfortaa3.getStringWidth(target1.getName()) / 2 - 5;
        float healthPos = calculatedHealth * (float)right3;
        RenderUtil.drawRoundedRect2Alpha((double)(left - 7), (double)(top + 10), (double)((float)(right + Aqua.INSTANCE.comfortaa3.getStringWidth(target1.getName())) - (float)s.getScaledWidth() / 2.0f - 22.0f), (double)((float)bottom / 6.0f - 5.0f), (double)cornerRadius, (Color)new Color(20, 20, 20, 90));
        if (this.lastHealthPercentage != healthProcent) {
            this.lostHealthPercentage += this.lastHealthPercentage - healthProcent;
        }
        this.lastHealthPercentage = healthProcent;
        this.lostHealthPercentage = Math.max((float)0.0f, (float)(this.lostHealthPercentage - this.lostHealthPercentage / 20.0f));
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        int[] color = Aqua.setmgr.getSetting("TargetHUDClientColor").isState() ? ESP.getRGB((int)this.getColor()) : ESP.getRGB((int)this.getColor2());
        RenderUtil.drawRoundedRect((double)(left + 50), (double)bottom, (double)(healthPos + (float)Aqua.INSTANCE.comfortaa3.getStringWidth(target1.getName()) / 2.0f - 7.0f), (double)((float)bottom - (float)s.getScaledHeight() / 2.0f - 87.0f), (double)2.0, (int)new Color(color[0], color[1], color[2]).getRGB());
        Aqua.INSTANCE.comfortaa3.drawString(target1.getName(), (float)(left + 50), (float)(top + 15), -1);
        TargetHUD.mc.fontRendererObj.drawString("\u2764", left + 50, top + 32, -1);
        Aqua.INSTANCE.comfortaa3.drawString("Health : " + Math.round((float)curTargetHealth) + ".0", (float)(left + 60), (float)(bottom - 19), -1);
        List NetworkMoment = GuiPlayerTabOverlay.field_175252_a.sortedCopy((Iterable)TargetHUD.mc.thePlayer.sendQueue.getPlayerInfoMap());
        for (Object nextObject : NetworkMoment) {
            NetworkPlayerInfo networkPlayerInfo = (NetworkPlayerInfo)nextObject;
            if (TargetHUD.mc.theWorld.getPlayerEntityByUUID(networkPlayerInfo.getGameProfile().getId()) != target1) continue;
            GlStateManager.enableCull();
            mc.getTextureManager().bindTexture(networkPlayerInfo.getLocationSkin());
            Gui.drawScaledCustomSizeModalRect((int)(s.getScaledWidth() / 2 + 10 + 55), (int)(s.getScaledHeight() / 2 - 5 + 65), (float)8.0f, (float)8.0f, (int)8, (int)8, (int)43, (int)43, (float)64.0f, (float)66.0f);
        }
    }

    public void drawTargetHUDHanabi(EntityLivingBase target1) {
        ScaledResolution s = new ScaledResolution(mc);
        Color backgroundColor = new Color(0, 0, 0, 120);
        Color emptyBarColor = new Color(59, 59, 59, 160);
        Color healthBarColor = Color.green;
        Color distBarColor = new Color(20, 81, 208);
        int left = s.getScaledWidth() / 2 + 5 + 62;
        int right2 = 142;
        int right = s.getScaledWidth() / 2 + right2;
        int right3 = 159;
        int top = s.getScaledHeight() / 2 - 25 + 70;
        int bottom = s.getScaledHeight() / 2 + 25 + 70;
        float curTargetHealth = target1.getHealth();
        float maxTargetHealth = target1.getMaxHealth();
        float healthProcent = target1.getHealth() / target1.getMaxHealth();
        if (this.lastHealthPercentage != healthProcent) {
            this.lostHealthPercentage += this.lastHealthPercentage - healthProcent;
        }
        this.lastHealthPercentage = healthProcent;
        float finalHealthPercentage = healthProcent + this.lostHealthPercentage;
        this.lostHealthPercentage = Math.max((float)0.0f, (float)(this.lostHealthPercentage - this.lostHealthPercentage / 20.0f));
        float calculatedHealth = finalHealthPercentage;
        int rectRight = right + Aqua.INSTANCE.comfortaa3.getStringWidth(target1.getName()) / 2;
        float healthPos = calculatedHealth * (float)right3;
        RenderUtil.drawRoundedRect2Alpha((double)(left + 2), (double)(top + 18), (double)(right - 488 - 5 + 30), (double)((float)bottom / 6.0f - 14.0f), (double)0.0, (Color)new Color(0, 0, 0, 200));
        if (this.lastHealthPercentage != healthProcent) {
            this.lostHealthPercentage += this.lastHealthPercentage - healthProcent;
        }
        this.lastHealthPercentage = healthProcent;
        this.lostHealthPercentage = Math.max((float)0.0f, (float)(this.lostHealthPercentage - this.lostHealthPercentage / 20.0f));
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        Aqua.INSTANCE.comfortaa3.drawString("" + Math.round((float)curTargetHealth) + ".0", (float)(left + 140), (float)(bottom - 4), -1);
        TargetHUD.mc.fontRendererObj.drawString("\u2764", left + 130, bottom - 3, -1);
        RenderUtil.drawRoundedRectGradient((double)(left + 2), (double)(bottom + 8), (double)healthPos, (double)((float)bottom / 15.0f - 19.0f), (double)0.0, (Color)new Color(0, 216, 245), (Color)new Color(0, 55, 245));
        Aqua.INSTANCE.comfortaa3.drawString(target1.getName(), (float)(left + 44), (float)(top + 23), -1);
        Aqua.INSTANCE.comfortaa5.drawString("XYZ:" + Math.round((double)TargetHUD.mc.thePlayer.posX) + " " + Math.round((double)TargetHUD.mc.thePlayer.posY) + " " + Math.round((double)TargetHUD.mc.thePlayer.posZ) + " | Hurt: true", (float)(left + 44), (float)(top + 37), -1);
        List NetworkMoment = GuiPlayerTabOverlay.field_175252_a.sortedCopy((Iterable)TargetHUD.mc.thePlayer.sendQueue.getPlayerInfoMap());
        for (Object nextObject : NetworkMoment) {
            NetworkPlayerInfo networkPlayerInfo = (NetworkPlayerInfo)nextObject;
            if (TargetHUD.mc.theWorld.getPlayerEntityByUUID(networkPlayerInfo.getGameProfile().getId()) != target1) continue;
            GlStateManager.enableCull();
            mc.getTextureManager().bindTexture(networkPlayerInfo.getLocationSkin());
            Gui.drawScaledCustomSizeModalRect((int)(s.getScaledWidth() / 2 + 10 + 62), (int)(s.getScaledHeight() / 2 - 5 + 70), (float)8.0f, (float)8.0f, (int)8, (int)8, (int)36, (int)36, (float)64.0f, (float)66.0f);
        }
    }

    public void drawTargetHUDShaders(EntityLivingBase target1) {
        int[] color;
        ScaledResolution s = new ScaledResolution(mc);
        float cornerRadius = (float)Aqua.setmgr.getSetting("TargetHUDCornerRadius").getCurrentNumber();
        Color backgroundColor = new Color(0, 0, 0, 120);
        Color emptyBarColor = new Color(59, 59, 59, 160);
        Color healthBarColor = Color.green;
        Color distBarColor = new Color(20, 81, 208);
        int left = s.getScaledWidth() / 2 + 5 + 62;
        int right2 = 142;
        int right = s.getScaledWidth() / 2 + right2;
        int right3 = 80 + Aqua.INSTANCE.comfortaa3.getStringWidth(target1.getName()) / 2 - 15;
        int top = s.getScaledHeight() / 2 - 25 + 70;
        int bottom = s.getScaledHeight() / 2 + 25 + 70;
        float curTargetHealth = target1.getHealth();
        float healthProcent = target1.getHealth() / target1.getMaxHealth();
        if (this.lastHealthPercentage != healthProcent) {
            this.lostHealthPercentage += this.lastHealthPercentage - healthProcent;
        }
        this.lastHealthPercentage = healthProcent;
        float finalHealthPercentage = healthProcent + this.lostHealthPercentage;
        this.lostHealthPercentage = Math.max((float)0.0f, (float)(this.lostHealthPercentage - this.lostHealthPercentage / 20.0f));
        float calculatedHealth = finalHealthPercentage;
        int rectRight = right + Aqua.INSTANCE.comfortaa3.getStringWidth(target1.getName()) / 2 - 5;
        float healthPos = calculatedHealth * (float)right3;
        int[] nArray = color = Aqua.setmgr.getSetting("TargetHUDClientColor").isState() ? ESP.getRGB((int)this.getColor()) : ESP.getRGB((int)this.getColor2());
        if (Aqua.setmgr.getSetting("TargetHUDMode").getCurrentMode().equalsIgnoreCase("Glow")) {
            Arraylist.drawGlowArray(() -> RenderUtil.drawRoundedRect((double)(left - 7), (double)(top + 10), (double)((float)(right + Aqua.INSTANCE.comfortaa3.getStringWidth(target1.getName())) - (float)s.getScaledWidth() / 2.0f - 22.0f), (double)((float)bottom / 6.0f - 5.0f), (double)cornerRadius, (int)new Color(color[0], color[1], color[2]).getRGB()), (boolean)false);
        }
        if (Aqua.setmgr.getSetting("TargetHUDMode").getCurrentMode().equalsIgnoreCase("Shadow")) {
            Shadow.drawGlow(() -> RenderUtil.drawRoundedRect((double)(left - 7), (double)(top + 10), (double)((float)(right + Aqua.INSTANCE.comfortaa3.getStringWidth(target1.getName())) - (float)s.getScaledWidth() / 2.0f - 22.0f), (double)((float)bottom / 6.0f - 5.0f), (double)cornerRadius, (int)new Color(20, 20, 20, 255).getRGB()), (boolean)false);
        }
        Blur.drawBlurred(() -> RenderUtil.drawRoundedRect((double)(left - 7), (double)(top + 10), (double)((float)(right + Aqua.INSTANCE.comfortaa3.getStringWidth(target1.getName())) - (float)s.getScaledWidth() / 2.0f - 22.0f), (double)((float)bottom / 6.0f - 5.0f), (double)cornerRadius, (int)new Color(20, 20, 20, 150).getRGB()), (boolean)false);
        if (this.lastHealthPercentage != healthProcent) {
            this.lostHealthPercentage += this.lastHealthPercentage - healthProcent;
        }
        this.lastHealthPercentage = healthProcent;
        this.lostHealthPercentage = Math.max((float)0.0f, (float)(this.lostHealthPercentage - this.lostHealthPercentage / 20.0f));
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        Arraylist.drawGlowArray(() -> RenderUtil.drawRoundedRect((double)(left + 50), (double)bottom, (double)(healthPos + (float)Aqua.INSTANCE.comfortaa3.getStringWidth(target1.getName()) / 2.0f - 7.0f), (double)((float)bottom - (float)s.getScaledHeight() / 2.0f - 87.0f), (double)2.0, (int)new Color(color[0], color[1], color[2]).getRGB()), (boolean)false);
        RenderUtil.drawRoundedRect((double)(left + 50), (double)bottom, (double)(healthPos + (float)Aqua.INSTANCE.comfortaa3.getStringWidth(target1.getName()) / 2.0f - 7.0f), (double)((float)bottom - (float)s.getScaledHeight() / 2.0f - 87.0f), (double)2.0, (int)new Color(color[0], color[1], color[2]).getRGB());
        List NetworkMoment = GuiPlayerTabOverlay.field_175252_a.sortedCopy((Iterable)TargetHUD.mc.thePlayer.sendQueue.getPlayerInfoMap());
        for (Object nextObject : NetworkMoment) {
            NetworkPlayerInfo networkPlayerInfo = (NetworkPlayerInfo)nextObject;
            if (TargetHUD.mc.theWorld.getPlayerEntityByUUID(networkPlayerInfo.getGameProfile().getId()) != target1) continue;
            GlStateManager.enableCull();
            mc.getTextureManager().bindTexture(networkPlayerInfo.getLocationSkin());
            Gui.drawScaledCustomSizeModalRect((int)(s.getScaledWidth() / 2 + 10 + 55), (int)(s.getScaledHeight() / 2 - 5 + 65), (float)8.0f, (float)8.0f, (int)8, (int)8, (int)43, (int)43, (float)64.0f, (float)66.0f);
        }
    }

    public int getColor2() {
        try {
            return Aqua.setmgr.getSetting("TargetHUDColor").getColor();
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

    public static float getAngle(Entity entity) {
        double x = RenderUtil.interpolate((double)entity.posX, (double)entity.lastTickPosX, (double)1.0) - RenderUtil.interpolate((double)TargetHUD.mc.thePlayer.posX, (double)TargetHUD.mc.thePlayer.lastTickPosX, (double)1.0);
        double z = RenderUtil.interpolate((double)entity.posZ, (double)entity.lastTickPosZ, (double)1.0) - RenderUtil.interpolate((double)TargetHUD.mc.thePlayer.posZ, (double)TargetHUD.mc.thePlayer.lastTickPosZ, (double)1.0);
        float yaw = (float)(-Math.toDegrees((double)Math.atan2((double)x, (double)z)));
        return (float)((double)yaw - RenderUtil.interpolate((double)TargetHUD.mc.thePlayer.rotationYaw, (double)TargetHUD.mc.thePlayer.prevRotationYaw, (double)1.0));
    }

    public static void renderPlayerModelTexture(double x, double y, float u, float v, int uWidth, int vHeight, int width, int height, float tileWidth, float tileHeight, AbstractClientPlayer target) {
        ResourceLocation skin = target.getLocationSkin();
        Minecraft.getMinecraft().getTextureManager().bindTexture(skin);
        GL11.glEnable((int)3042);
        Gui.drawScaledCustomSizeModalRect((int)((int)x), (int)((int)y), (float)u, (float)v, (int)uWidth, (int)vHeight, (int)width, (int)height, (float)tileWidth, (float)tileHeight);
        GL11.glDisable((int)3042);
    }
}
