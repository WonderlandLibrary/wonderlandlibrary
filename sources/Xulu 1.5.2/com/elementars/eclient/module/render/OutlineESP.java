// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.module.render;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.entity.monster.EntitySpider;
import com.elementars.eclient.util.OutlineUtils2;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.client.renderer.entity.Render;
import java.util.Iterator;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import com.elementars.eclient.util.Wrapper;
import com.elementars.eclient.Xulu;
import com.elementars.eclient.util.TargetPlayers;
import dev.xulu.newgui.util.ColorUtil;
import com.elementars.eclient.friend.Friends;
import com.elementars.eclient.event.events.EventPostRenderLayers;
import com.elementars.eclient.event.EventTarget;
import net.minecraft.util.math.Vec3d;
import java.awt.Color;
import com.elementars.eclient.util.OutlineUtils;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;
import com.elementars.eclient.util.EntityUtil;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import com.elementars.eclient.event.Event;
import net.minecraft.entity.Entity;
import com.elementars.eclient.util.MathUtil;
import com.elementars.eclient.event.events.EventModelRender;
import net.minecraft.client.renderer.culling.Frustum;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Arrays;
import com.elementars.eclient.module.Category;
import net.minecraft.client.renderer.culling.ICamera;
import dev.xulu.settings.Value;
import com.elementars.eclient.module.Module;

public class OutlineESP extends Module
{
    public static boolean feQE;
    public static boolean krOE;
    public final Value<String> mode;
    public final Value<Float> width;
    public final Value<Boolean> chams;
    public final Value<Boolean> onTop;
    public final Value<Boolean> players;
    public final Value<Boolean> animals;
    public final Value<Boolean> mobs;
    public final Value<Boolean> crystals;
    public final Value<String> color;
    public final Value<Boolean> renderEntities;
    public final Value<Boolean> renderCrystals;
    public final Value<Boolean> friends;
    private final Value<Integer> red;
    private final Value<Integer> green;
    private final Value<Integer> blue;
    public static Value<Boolean> future;
    boolean fancyGraphics;
    float gamma;
    ICamera camera;
    
    public OutlineESP() {
        super("OutlineESP", "Outlines entities", 0, Category.RENDER, true);
        this.mode = this.register(new Value<String>("Mode", this, "Outline", new ArrayList<String>(Arrays.asList("Outline", "Wireframe", "Solid", "Shader"))));
        this.width = this.register(new Value<Float>("Line Width", this, 1.0f, 0.0f, 10.0f));
        this.chams = this.register(new Value<Boolean>("Chams", this, false));
        this.onTop = this.register(new Value<Boolean>("OnTop", this, true));
        this.players = this.register(new Value<Boolean>("Players", this, true));
        this.animals = this.register(new Value<Boolean>("Animals", this, true));
        this.mobs = this.register(new Value<Boolean>("Mobs", this, true));
        this.crystals = this.register(new Value<Boolean>("Crystals", this, true));
        this.color = this.register(new Value<String>("Color Mode", this, "Tracers", new String[] { "ClickGui", "Tracers", "Target", "Rainbow" }));
        this.renderEntities = this.register(new Value<Boolean>("Render Entities", this, true));
        this.renderCrystals = this.register(new Value<Boolean>("Render Crystals", this, true));
        this.friends = this.register(new Value<Boolean>("Friends", this, true));
        this.red = this.register(new Value<Integer>("Target Red", this, 255, 0, 255));
        this.green = this.register(new Value<Integer>("Target Green", this, 0, 0, 255));
        this.blue = this.register(new Value<Integer>("TargetBlue", this, 0, 0, 255));
        this.camera = (ICamera)new Frustum();
        OutlineESP.future = this.register(new Value<Boolean>("Future Colors", this, true));
    }
    
    @EventTarget
    public void onModelRender(final EventModelRender event) {
        final Vec3d view = MathUtil.interpolateEntity((Entity)OutlineESP.mc.player, event.getPartialTicks());
        this.camera.setPosition(view.x, view.y, view.z);
        if (!this.camera.isBoundingBoxInFrustum(event.entity.getEntityBoundingBox())) {
            return;
        }
        if (event.getEventState() == Event.State.PRE) {
            this.fancyGraphics = OutlineESP.mc.gameSettings.fancyGraphics;
            OutlineESP.mc.gameSettings.fancyGraphics = false;
            this.gamma = OutlineESP.mc.gameSettings.gammaSetting;
            OutlineESP.mc.gameSettings.gammaSetting = 10000.0f;
            if (this.onTop.getValue() && this.renderEntities.getValue()) {
                event.modelBase.render(event.entity, event.limbSwing, event.limbSwingAmount, event.ageInTicks, event.netHeadYaw, event.headPitch, event.scaleFactor);
            }
            if (this.mode.getValue().equalsIgnoreCase("Shader")) {
                if (event.entity instanceof EntityOtherPlayerMP && this.players.getValue()) {
                    event.entity.setGlowing(true);
                }
                else if (this.animals.getValue() && EntityUtil.isPassive(event.entity)) {
                    event.entity.setGlowing(true);
                }
                else if (this.mobs.getValue() && !EntityUtil.isPassive(event.entity)) {
                    event.entity.setGlowing(true);
                }
                else {
                    event.entity.setGlowing(false);
                }
            }
            else {
                event.entity.setGlowing(false);
                if (!this.mode.getValue().equalsIgnoreCase("Outline")) {
                    if (event.entity instanceof EntityOtherPlayerMP && this.players.getValue()) {
                        GL11.glPushMatrix();
                        GL11.glEnable(32823);
                        GL11.glPolygonOffset(1.0f, -100000.0f);
                        GL11.glPushAttrib(1048575);
                        if (this.mode.getValue().equalsIgnoreCase("Solid")) {
                            GL11.glPolygonMode(1028, 6913);
                        }
                        else {
                            GL11.glPolygonMode(1032, 6913);
                        }
                        GL11.glDisable(3553);
                        GL11.glDisable(2896);
                        GL11.glDisable(2929);
                        GL11.glEnable(2848);
                        GL11.glEnable(3042);
                        GL11.glBlendFunc(770, 771);
                        OutlineUtils.setColor(this.getColor((EntityPlayer)event.entity));
                        GL11.glLineWidth((float)this.width.getValue());
                        event.modelBase.render(event.entity, event.limbSwing, event.limbSwingAmount, event.ageInTicks, event.netHeadYaw, event.headPitch, event.scaleFactor);
                        GL11.glPopAttrib();
                        GL11.glPolygonOffset(1.0f, 100000.0f);
                        GL11.glDisable(32823);
                        GL11.glPopMatrix();
                    }
                    else if (this.animals.getValue() && EntityUtil.isPassive(event.entity)) {
                        GL11.glPushMatrix();
                        GL11.glPushAttrib(1048575);
                        GL11.glPolygonMode(1032, 6913);
                        GL11.glDisable(3553);
                        GL11.glDisable(2896);
                        GL11.glDisable(2929);
                        GL11.glEnable(2848);
                        GL11.glEnable(3042);
                        GL11.glBlendFunc(770, 771);
                        OutlineUtils.setColor(getEntityColor(event.entity));
                        GL11.glLineWidth((float)this.width.getValue());
                        event.modelBase.render(event.entity, event.limbSwing, event.limbSwingAmount, event.ageInTicks, event.netHeadYaw, event.headPitch, event.scaleFactor);
                        GL11.glPopAttrib();
                        GL11.glPopMatrix();
                    }
                    else if (this.mobs.getValue() && !EntityUtil.isPassive(event.entity)) {
                        GL11.glPushMatrix();
                        GL11.glPushAttrib(1048575);
                        GL11.glPolygonMode(1032, 6913);
                        GL11.glDisable(3553);
                        GL11.glDisable(2896);
                        GL11.glDisable(2929);
                        GL11.glEnable(2848);
                        GL11.glEnable(3042);
                        GL11.glBlendFunc(770, 771);
                        OutlineUtils.setColor(getEntityColor(event.entity));
                        GL11.glLineWidth((float)this.width.getValue());
                        event.modelBase.render(event.entity, event.limbSwing, event.limbSwingAmount, event.ageInTicks, event.netHeadYaw, event.headPitch, event.scaleFactor);
                        GL11.glPopAttrib();
                        GL11.glPopMatrix();
                    }
                }
                else if (event.entity instanceof EntityOtherPlayerMP && this.players.getValue()) {
                    final Color n = this.getColor((EntityPlayer)event.entity);
                    event.modelBase.render(event.entity, event.limbSwing, event.limbSwingAmount, event.ageInTicks, event.netHeadYaw, event.headPitch, event.scaleFactor);
                    OutlineUtils.renderOne(this.width.getValue());
                    event.modelBase.render(event.entity, event.limbSwing, event.limbSwingAmount, event.ageInTicks, event.netHeadYaw, event.headPitch, event.scaleFactor);
                    OutlineUtils.renderTwo();
                    event.modelBase.render(event.entity, event.limbSwing, event.limbSwingAmount, event.ageInTicks, event.netHeadYaw, event.headPitch, event.scaleFactor);
                    OutlineUtils.renderThree();
                    OutlineUtils.renderFour(n);
                    OutlineUtils.setColor(n);
                    event.modelBase.render(event.entity, event.limbSwing, event.limbSwingAmount, event.ageInTicks, event.netHeadYaw, event.headPitch, event.scaleFactor);
                    OutlineUtils.renderFive();
                    OutlineUtils.setColor(Color.WHITE);
                }
                else if (this.animals.getValue() && EntityUtil.isPassive(event.entity)) {
                    Color n;
                    if (OutlineESP.future.getValue()) {
                        n = new Color(0, 196, 0);
                    }
                    else {
                        n = new Color(5, 255, 240);
                    }
                    GL11.glLineWidth(5.0f);
                    event.modelBase.render(event.entity, event.limbSwing, event.limbSwingAmount, event.ageInTicks, event.netHeadYaw, event.headPitch, event.scaleFactor);
                    OutlineUtils.renderOne(this.width.getValue());
                    event.modelBase.render(event.entity, event.limbSwing, event.limbSwingAmount, event.ageInTicks, event.netHeadYaw, event.headPitch, event.scaleFactor);
                    OutlineUtils.renderTwo();
                    event.modelBase.render(event.entity, event.limbSwing, event.limbSwingAmount, event.ageInTicks, event.netHeadYaw, event.headPitch, event.scaleFactor);
                    OutlineUtils.renderThree();
                    OutlineUtils.renderFour(n);
                    event.modelBase.render(event.entity, event.limbSwing, event.limbSwingAmount, event.ageInTicks, event.netHeadYaw, event.headPitch, event.scaleFactor);
                    OutlineUtils.renderFive();
                }
                else if (this.mobs.getValue() && !EntityUtil.isPassive(event.entity) && !(event.entity instanceof EntityPlayer)) {
                    Color n;
                    if (OutlineESP.future.getValue()) {
                        n = new Color(191, 57, 59);
                    }
                    else {
                        n = new Color(255, 0, 102);
                    }
                    GL11.glLineWidth(5.0f);
                    event.modelBase.render(event.entity, event.limbSwing, event.limbSwingAmount, event.ageInTicks, event.netHeadYaw, event.headPitch, event.scaleFactor);
                    OutlineUtils.renderOne(this.width.getValue());
                    event.modelBase.render(event.entity, event.limbSwing, event.limbSwingAmount, event.ageInTicks, event.netHeadYaw, event.headPitch, event.scaleFactor);
                    OutlineUtils.renderTwo();
                    event.modelBase.render(event.entity, event.limbSwing, event.limbSwingAmount, event.ageInTicks, event.netHeadYaw, event.headPitch, event.scaleFactor);
                    OutlineUtils.renderThree();
                    OutlineUtils.renderFour(n);
                    event.modelBase.render(event.entity, event.limbSwing, event.limbSwingAmount, event.ageInTicks, event.netHeadYaw, event.headPitch, event.scaleFactor);
                    OutlineUtils.renderFive();
                }
            }
            if (!this.onTop.getValue() && this.renderEntities.getValue()) {
                event.modelBase.render(event.entity, event.limbSwing, event.limbSwingAmount, event.ageInTicks, event.netHeadYaw, event.headPitch, event.scaleFactor);
            }
            OutlineUtils.setColor(Color.WHITE);
            OutlineESP.mc.gameSettings.fancyGraphics = this.fancyGraphics;
            OutlineESP.mc.gameSettings.gammaSetting = this.gamma;
            event.setCancelled(true);
        }
    }
    
    @EventTarget
    public void renderPost(final EventPostRenderLayers event) {
        if (!event.renderer.bindEntityTexture((Entity)event.entity)) {
            return;
        }
        final Vec3d view = MathUtil.interpolateEntity((Entity)OutlineESP.mc.player, event.getPartialTicks());
        this.camera.setPosition(view.x, view.y, view.z);
        if (!this.camera.isBoundingBoxInFrustum(event.entity.getEntityBoundingBox())) {
            return;
        }
        if (event.getEventState() == Event.State.PRE) {
            this.fancyGraphics = OutlineESP.mc.gameSettings.fancyGraphics;
            OutlineESP.mc.gameSettings.fancyGraphics = false;
            this.gamma = OutlineESP.mc.gameSettings.gammaSetting;
            OutlineESP.mc.gameSettings.gammaSetting = 10000.0f;
            if (this.onTop.getValue() && this.renderEntities.getValue()) {
                event.modelBase.render((Entity)event.entity, event.limbSwing, event.limbSwingAmount, event.ageInTicks, event.netHeadYaw, event.headPitch, event.scaleIn);
            }
            if (this.mode.getValue().equalsIgnoreCase("Shader")) {
                if (event.entity instanceof EntityOtherPlayerMP && this.players.getValue()) {
                    event.entity.setGlowing(true);
                }
                else if (this.animals.getValue() && EntityUtil.isPassive((Entity)event.entity)) {
                    event.entity.setGlowing(true);
                }
                else if (this.mobs.getValue() && !EntityUtil.isPassive((Entity)event.entity)) {
                    event.entity.setGlowing(true);
                }
                else {
                    event.entity.setGlowing(false);
                }
            }
            else {
                event.entity.setGlowing(false);
                if (!this.mode.getValue().equalsIgnoreCase("Outline")) {
                    if (event.entity instanceof EntityOtherPlayerMP && this.players.getValue()) {
                        GL11.glPushMatrix();
                        GL11.glEnable(32823);
                        GL11.glPolygonOffset(1.0f, -100000.0f);
                        GL11.glPushAttrib(1048575);
                        if (this.mode.getValue().equalsIgnoreCase("Solid")) {
                            GL11.glPolygonMode(1028, 6913);
                        }
                        else {
                            GL11.glPolygonMode(1032, 6913);
                        }
                        GL11.glDisable(3553);
                        GL11.glDisable(2896);
                        GL11.glDisable(2929);
                        GL11.glEnable(2848);
                        GL11.glEnable(3042);
                        GL11.glBlendFunc(770, 771);
                        OutlineUtils.setColor(this.getColor((EntityPlayer)event.entity));
                        GL11.glLineWidth((float)this.width.getValue());
                        event.modelBase.render((Entity)event.entity, event.limbSwing, event.limbSwingAmount, event.ageInTicks, event.netHeadYaw, event.headPitch, event.scaleIn);
                        GL11.glPopAttrib();
                        GL11.glPolygonOffset(1.0f, 100000.0f);
                        GL11.glDisable(32823);
                        GL11.glPopMatrix();
                    }
                    else if (this.animals.getValue() && EntityUtil.isPassive((Entity)event.entity)) {
                        GL11.glPushMatrix();
                        GL11.glPushAttrib(1048575);
                        GL11.glPolygonMode(1032, 6913);
                        GL11.glDisable(3553);
                        GL11.glDisable(2896);
                        GL11.glDisable(2929);
                        GL11.glEnable(2848);
                        GL11.glEnable(3042);
                        GL11.glBlendFunc(770, 771);
                        OutlineUtils.setColor(getEntityColor((Entity)event.entity));
                        GL11.glLineWidth((float)this.width.getValue());
                        event.modelBase.render((Entity)event.entity, event.limbSwing, event.limbSwingAmount, event.ageInTicks, event.netHeadYaw, event.headPitch, event.scaleIn);
                        GL11.glPopAttrib();
                        GL11.glPopMatrix();
                    }
                    else if (this.mobs.getValue() && !EntityUtil.isPassive((Entity)event.entity)) {
                        GL11.glPushMatrix();
                        GL11.glPushAttrib(1048575);
                        GL11.glPolygonMode(1032, 6913);
                        GL11.glDisable(3553);
                        GL11.glDisable(2896);
                        GL11.glDisable(2929);
                        GL11.glEnable(2848);
                        GL11.glEnable(3042);
                        GL11.glBlendFunc(770, 771);
                        OutlineUtils.setColor(getEntityColor((Entity)event.entity));
                        GL11.glLineWidth((float)this.width.getValue());
                        event.modelBase.render((Entity)event.entity, event.limbSwing, event.limbSwingAmount, event.ageInTicks, event.netHeadYaw, event.headPitch, event.scaleIn);
                        GL11.glPopAttrib();
                        GL11.glPopMatrix();
                    }
                }
                else if (event.entity instanceof EntityOtherPlayerMP && this.players.getValue()) {
                    final Color n = this.getColor((EntityPlayer)event.entity);
                    event.modelBase.render((Entity)event.entity, event.limbSwing, event.limbSwingAmount, event.ageInTicks, event.netHeadYaw, event.headPitch, event.scaleIn);
                    OutlineUtils.renderOne(this.width.getValue());
                    event.modelBase.render((Entity)event.entity, event.limbSwing, event.limbSwingAmount, event.ageInTicks, event.netHeadYaw, event.headPitch, event.scaleIn);
                    OutlineUtils.renderTwo();
                    event.modelBase.render((Entity)event.entity, event.limbSwing, event.limbSwingAmount, event.ageInTicks, event.netHeadYaw, event.headPitch, event.scaleIn);
                    OutlineUtils.renderThree();
                    OutlineUtils.renderFour(n);
                    OutlineUtils.setColor(n);
                    event.modelBase.render((Entity)event.entity, event.limbSwing, event.limbSwingAmount, event.ageInTicks, event.netHeadYaw, event.headPitch, event.scaleIn);
                    OutlineUtils.renderFive();
                    OutlineUtils.setColor(Color.WHITE);
                }
                else if (this.animals.getValue() && EntityUtil.isPassive((Entity)event.entity)) {
                    Color n;
                    if (OutlineESP.future.getValue()) {
                        n = new Color(0, 196, 0);
                    }
                    else {
                        n = new Color(5, 255, 240);
                    }
                    GL11.glLineWidth(5.0f);
                    event.modelBase.render((Entity)event.entity, event.limbSwing, event.limbSwingAmount, event.ageInTicks, event.netHeadYaw, event.headPitch, event.scaleIn);
                    OutlineUtils.renderOne(this.width.getValue());
                    event.modelBase.render((Entity)event.entity, event.limbSwing, event.limbSwingAmount, event.ageInTicks, event.netHeadYaw, event.headPitch, event.scaleIn);
                    OutlineUtils.renderTwo();
                    event.modelBase.render((Entity)event.entity, event.limbSwing, event.limbSwingAmount, event.ageInTicks, event.netHeadYaw, event.headPitch, event.scaleIn);
                    OutlineUtils.renderThree();
                    OutlineUtils.renderFour(n);
                    event.modelBase.render((Entity)event.entity, event.limbSwing, event.limbSwingAmount, event.ageInTicks, event.netHeadYaw, event.headPitch, event.scaleIn);
                    OutlineUtils.renderFive();
                }
                else if (this.mobs.getValue() && !EntityUtil.isPassive((Entity)event.entity) && !(event.entity instanceof EntityPlayer)) {
                    Color n;
                    if (OutlineESP.future.getValue()) {
                        n = new Color(191, 57, 59);
                    }
                    else {
                        n = new Color(255, 0, 102);
                    }
                    GL11.glLineWidth(5.0f);
                    event.modelBase.render((Entity)event.entity, event.limbSwing, event.limbSwingAmount, event.ageInTicks, event.netHeadYaw, event.headPitch, event.scaleIn);
                    OutlineUtils.renderOne(this.width.getValue());
                    event.modelBase.render((Entity)event.entity, event.limbSwing, event.limbSwingAmount, event.ageInTicks, event.netHeadYaw, event.headPitch, event.scaleIn);
                    OutlineUtils.renderTwo();
                    event.modelBase.render((Entity)event.entity, event.limbSwing, event.limbSwingAmount, event.ageInTicks, event.netHeadYaw, event.headPitch, event.scaleIn);
                    OutlineUtils.renderThree();
                    OutlineUtils.renderFour(n);
                    event.modelBase.render((Entity)event.entity, event.limbSwing, event.limbSwingAmount, event.ageInTicks, event.netHeadYaw, event.headPitch, event.scaleIn);
                    OutlineUtils.renderFive();
                }
            }
            if (!this.onTop.getValue() && this.renderEntities.getValue()) {
                event.modelBase.render((Entity)event.entity, event.limbSwing, event.limbSwingAmount, event.ageInTicks, event.netHeadYaw, event.headPitch, event.scaleIn);
            }
            OutlineUtils.setColor(Color.WHITE);
            OutlineESP.mc.gameSettings.fancyGraphics = this.fancyGraphics;
            OutlineESP.mc.gameSettings.gammaSetting = this.gamma;
            event.setCancelled(true);
        }
    }
    
    private Color getColor(final EntityPlayer entity) {
        if (Friends.isFriend(entity.getName()) && this.friends.getValue()) {
            return new Color(0.27f, 0.7f, 0.92f);
        }
        if (this.color.getValue().equalsIgnoreCase("ClickGui")) {
            return ColorUtil.getClickGUIColor();
        }
        if (this.color.getValue().equalsIgnoreCase("Tracers")) {
            final float distance = OutlineESP.mc.player.getDistance((Entity)entity);
            if (distance <= 32.0f) {
                return new Color(1.0f - distance / 32.0f / 2.0f, distance / 32.0f, 0.0f);
            }
            return new Color(0.0f, 0.9f, 0.0f);
        }
        else if (this.color.getValue().equalsIgnoreCase("Target")) {
            if (TargetPlayers.targettedplayers.containsKey(entity.getName())) {
                return new Color(this.red.getValue() / 255.0f, this.green.getValue() / 255.0f, this.blue.getValue() / 255.0f);
            }
            return ColorUtil.getClickGUIColor();
        }
        else {
            if (this.color.getValue().equalsIgnoreCase("Rainbow")) {
                return new Color(Xulu.rgb);
            }
            return new Color(1.0f, 1.0f, 1.0f);
        }
    }
    
    public static void renderNormal2(final float n) {
        for (final Entity entity : Wrapper.getMinecraft().world.loadedEntityList) {
            if (entity != Wrapper.getMinecraft().getRenderViewEntity()) {
                if (!(entity instanceof EntityPlayer)) {
                    continue;
                }
                final Render entityRenderObject = Wrapper.getMinecraft().getRenderManager().getEntityRenderObject(entity);
                final RenderLivingBase renderer = (entityRenderObject instanceof RenderLivingBase) ? entityRenderObject : null;
                final double n2 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * n;
                final double n3 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * n;
                final double n4 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * n;
                if (renderer == null) {
                    continue;
                }
                renderer.doRender((EntityLivingBase)entity, n2 - OutlineESP.mc.renderManager.renderPosX, n3 - OutlineESP.mc.renderManager.renderPosY, n4 - OutlineESP.mc.renderManager.renderPosZ, entity.rotationYaw, OutlineESP.mc.getRenderPartialTicks());
            }
        }
    }
    
    public static void renderColor2(final float n) {
        for (final Entity entity : Wrapper.getMinecraft().world.loadedEntityList) {
            if (entity != Wrapper.getMinecraft().getRenderViewEntity()) {
                if (!(entity instanceof EntityPlayer)) {
                    continue;
                }
                final Render entityRenderObject = Wrapper.getMinecraft().getRenderManager().getEntityRenderObject(entity);
                final RenderLivingBase renderer = (entityRenderObject instanceof RenderLivingBase) ? entityRenderObject : null;
                if (entity instanceof EntityPlayer) {
                    GL11.glColor3f(5.0f, 255.0f, 240.0f);
                }
                else if (entity instanceof EntityEnderCrystal) {
                    OutlineUtils2.setColor(ColorUtil.getClickGUIColor());
                }
                else if (EntityUtil.isPassive(entity)) {
                    if (OutlineESP.future.getValue()) {
                        OutlineUtils2.setColor(new Color(0, 196, 0));
                    }
                    else {
                        OutlineUtils2.setColor(new Color(5, 255, 240));
                    }
                }
                else if (!EntityUtil.isPassive(entity) || entity instanceof EntitySpider) {
                    if (OutlineESP.future.getValue()) {
                        OutlineUtils2.setColor(new Color(191, 57, 59));
                    }
                    else {
                        OutlineUtils2.setColor(new Color(255, 0, 102));
                    }
                }
                else {
                    OutlineUtils2.setColor(new Color(1.0f, 1.0f, 0.0f));
                }
                final double n2 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * n;
                final double n3 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * n;
                final double n4 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * n;
                if (renderer == null) {
                    continue;
                }
                renderer.doRender((EntityLivingBase)entity, n2, n3, n4, entity.rotationYaw, OutlineESP.mc.getRenderPartialTicks());
            }
        }
    }
    
    public static void renderNormal(final float n) {
        for (final Entity entity : Wrapper.getMinecraft().world.loadedEntityList) {
            if (entity != Wrapper.getMinecraft().getRenderViewEntity()) {
                if (!(entity instanceof AbstractClientPlayer)) {
                    continue;
                }
                final AbstractClientPlayer player = (AbstractClientPlayer)entity;
                final double n2 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * n;
                final double n3 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * n;
                final double n4 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * n;
                OutlineESP.mc.renderGlobal.renderManager.playerRenderer.doRender(player, n2 - OutlineESP.mc.renderManager.renderPosX, n3 - OutlineESP.mc.renderManager.renderPosY, n4 - OutlineESP.mc.renderManager.renderPosZ, entity.rotationYaw, n);
            }
        }
    }
    
    public static void renderColor(final float n) {
        for (final Entity entity : Wrapper.getMinecraft().world.loadedEntityList) {
            if (entity != Wrapper.getMinecraft().getRenderViewEntity()) {
                if (!(entity instanceof AbstractClientPlayer)) {
                    continue;
                }
                final AbstractClientPlayer player = (AbstractClientPlayer)entity;
                final double n2 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * n;
                final double n3 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * n;
                final double n4 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * n;
                if (entity instanceof EntityPlayer) {
                    GL11.glColor3f(5.0f, 255.0f, 240.0f);
                }
                else if (entity instanceof EntityEnderCrystal) {
                    OutlineUtils2.setColor(ColorUtil.getClickGUIColor());
                }
                else if (EntityUtil.isPassive(entity)) {
                    if (OutlineESP.future.getValue()) {
                        OutlineUtils2.setColor(new Color(0, 196, 0));
                    }
                    else {
                        OutlineUtils2.setColor(new Color(5, 255, 240));
                    }
                }
                else if (!EntityUtil.isPassive(entity) || entity instanceof EntitySpider) {
                    if (OutlineESP.future.getValue()) {
                        OutlineUtils2.setColor(new Color(191, 57, 59));
                    }
                    else {
                        OutlineUtils2.setColor(new Color(255, 0, 102));
                    }
                }
                else {
                    OutlineUtils2.setColor(new Color(1.0f, 1.0f, 0.0f));
                }
                OutlineESP.mc.renderGlobal.renderManager.playerRenderer.doRender(player, n2 - OutlineESP.mc.renderManager.renderPosX, n3 - OutlineESP.mc.renderManager.renderPosY, n4 - OutlineESP.mc.renderManager.renderPosZ, entity.rotationYaw, n);
            }
        }
    }
    
    public static void oisD(final Color color) {
        GL11.glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, color.getAlpha() / 255.0f);
    }
    
    public static Color getEntityColor(final Entity entity) {
        if (entity instanceof EntityPlayer) {
            if (Friends.isFriend(entity.getName())) {
                return new Color(0.27f, 0.7f, 0.92f);
            }
            final float distance = OutlineESP.mc.player.getDistance(entity);
            if (distance <= 32.0f) {
                return new Color(1.0f - distance / 32.0f / 2.0f, distance / 32.0f, 0.0f);
            }
            return new Color(0.0f, 0.9f, 0.0f);
        }
        else {
            if (entity instanceof EntityEnderCrystal) {
                return ColorUtil.getClickGUIColor();
            }
            if (EntityUtil.isPassive(entity)) {
                if (OutlineESP.future.getValue()) {
                    return new Color(0, 196, 0);
                }
                return new Color(5, 255, 240);
            }
            else {
                if (EntityUtil.isPassive(entity) && !(entity instanceof EntitySpider)) {
                    return new Color(1.0f, 1.0f, 0.0f);
                }
                if (OutlineESP.future.getValue()) {
                    return new Color(191, 57, 59);
                }
                return new Color(255, 0, 102);
            }
        }
    }
}
