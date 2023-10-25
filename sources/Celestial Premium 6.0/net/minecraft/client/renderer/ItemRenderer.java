/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.renderer;

import com.google.common.base.MoreObjects;
import java.awt.Color;
import java.util.Objects;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.storage.MapData;
import optifine.Config;
import optifine.DynamicLights;
import optifine.Reflector;
import optifine.ReflectorForge;
import org.celestial.client.Celestial;
import org.celestial.client.event.EventManager;
import org.celestial.client.event.events.impl.player.EventTransformSideFirstPerson;
import org.celestial.client.feature.impl.combat.KillAura;
import org.celestial.client.feature.impl.visuals.Chams;
import org.celestial.client.feature.impl.visuals.NoRender;
import org.celestial.client.feature.impl.visuals.SwingAnimations;
import org.celestial.client.feature.impl.visuals.ViewModel;
import org.celestial.client.helpers.misc.ClientHelper;
import org.lwjgl.opengl.GL11;
import shadersmod.client.Shaders;

public class ItemRenderer {
    private static final ResourceLocation RES_MAP_BACKGROUND = new ResourceLocation("textures/map/map_background.png");
    private static final ResourceLocation RES_UNDERWATER_OVERLAY = new ResourceLocation("textures/misc/underwater.png");
    private final Minecraft mc;
    private ItemStack itemStackMainHand = ItemStack.field_190927_a;
    private ItemStack itemStackOffHand = ItemStack.field_190927_a;
    private float equippedProgressMainHand;
    private float prevEquippedProgressMainHand;
    private float equippedProgressOffHand;
    private float prevEquippedProgressOffHand;
    private final RenderManager renderManager;
    private final RenderItem itemRenderer;
    private float spin;

    public ItemRenderer(Minecraft mcIn) {
        this.mc = mcIn;
        this.renderManager = mcIn.getRenderManager();
        this.itemRenderer = mcIn.getRenderItem();
    }

    public void renderItem(EntityLivingBase entityIn, ItemStack heldStack, ItemCameraTransforms.TransformType transform) {
        this.renderItemSide(entityIn, heldStack, transform, false);
    }

    public void renderItemSide(EntityLivingBase entitylivingbaseIn, ItemStack heldStack, ItemCameraTransforms.TransformType transform, boolean leftHanded) {
        if (!heldStack.isEmpty()) {
            boolean flag;
            Item item = heldStack.getItem();
            Block block = Block.getBlockFromItem(item);
            GlStateManager.pushMatrix();
            boolean bl = flag = this.itemRenderer.shouldRenderItemIn3D(heldStack) && block.getBlockLayer() == BlockRenderLayer.TRANSLUCENT;
            if (!(!flag || Config.isShaders() && Shaders.renderItemKeepDepthMask)) {
                GlStateManager.depthMask(false);
            }
            this.itemRenderer.renderItem(heldStack, entitylivingbaseIn, transform, leftHanded);
            if (flag) {
                GlStateManager.depthMask(true);
            }
            GlStateManager.popMatrix();
        }
    }

    private void rotateArroundXAndY(float angle, float angleY) {
        GlStateManager.pushMatrix();
        GlStateManager.rotate(angle, 1.0f, 0.0f, 0.0f);
        GlStateManager.rotate(angleY, 0.0f, 1.0f, 0.0f);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.popMatrix();
    }

    private void setLightmap() {
        EntityPlayerSP abstractclientplayer = this.mc.player;
        int i = this.mc.world.getCombinedLight(new BlockPos(abstractclientplayer.posX, abstractclientplayer.posY + (double)abstractclientplayer.getEyeHeight(), abstractclientplayer.posZ), 0);
        if (Config.isDynamicLights()) {
            i = DynamicLights.getCombinedLight(this.mc.getRenderViewEntity(), i);
        }
        float f = i & 0xFFFF;
        float f1 = i >> 16;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, f, f1);
    }

    private void rotateArm(float p_187458_1_) {
        EntityPlayerSP entityplayersp = this.mc.player;
        float f = entityplayersp.prevRenderArmPitch + (entityplayersp.renderArmPitch - entityplayersp.prevRenderArmPitch) * p_187458_1_;
        float f1 = entityplayersp.prevRenderArmYaw + (entityplayersp.renderArmYaw - entityplayersp.prevRenderArmYaw) * p_187458_1_;
        GlStateManager.rotate((entityplayersp.rotationPitch - f) * 0.1f, 1.0f, 0.0f, 0.0f);
        GlStateManager.rotate((entityplayersp.rotationYaw - f1) * 0.1f, 0.0f, 1.0f, 0.0f);
    }

    private float getMapAngleFromPitch(float pitch) {
        float f = 1.0f - pitch / 45.0f + 0.1f;
        f = MathHelper.clamp(f, 0.0f, 1.0f);
        f = -MathHelper.cos(f * (float)Math.PI) * 0.5f + 0.5f;
        return f;
    }

    private void renderArms() {
        if (!this.mc.player.isInvisible()) {
            GlStateManager.disableCull();
            GlStateManager.pushMatrix();
            GlStateManager.rotate(90.0f, 0.0f, 1.0f, 0.0f);
            this.renderArm(EnumHandSide.RIGHT);
            this.renderArm(EnumHandSide.LEFT);
            GlStateManager.popMatrix();
            GlStateManager.enableCull();
        }
    }

    private void renderArm(EnumHandSide p_187455_1_) {
        this.mc.getTextureManager().bindTexture(this.mc.player.getLocationSkin());
        Render render = this.renderManager.getEntityRenderObject(this.mc.player);
        RenderPlayer renderplayer = (RenderPlayer)render;
        GlStateManager.pushMatrix();
        float f = p_187455_1_ == EnumHandSide.RIGHT ? 1.0f : -1.0f;
        GlStateManager.rotate(92.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(45.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.rotate(f * -41.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.translate(f * 0.3f, -1.1f, 0.45f);
        if (p_187455_1_ == EnumHandSide.RIGHT) {
            renderplayer.renderRightArm(this.mc.player);
        } else {
            renderplayer.renderLeftArm(this.mc.player);
        }
        GlStateManager.popMatrix();
    }

    private void renderMapFirstPersonSide(float p_187465_1_, EnumHandSide p_187465_2_, float p_187465_3_, ItemStack p_187465_4_) {
        float f = p_187465_2_ == EnumHandSide.RIGHT ? 1.0f : -1.0f;
        GlStateManager.translate(f * 0.125f, -0.125f, 0.0f);
        if (!this.mc.player.isInvisible()) {
            GlStateManager.pushMatrix();
            GlStateManager.rotate(f * 10.0f, 0.0f, 0.0f, 1.0f);
            this.renderArmFirstPerson(p_187465_1_, p_187465_3_, p_187465_2_);
            GlStateManager.popMatrix();
        }
        GlStateManager.pushMatrix();
        GlStateManager.translate(f * 0.51f, -0.08f + p_187465_1_ * -1.2f, -0.75f);
        float f1 = MathHelper.sqrt(p_187465_3_);
        float f2 = MathHelper.sin(f1 * (float)Math.PI);
        float f3 = -0.5f * f2;
        float f4 = 0.4f * MathHelper.sin(f1 * ((float)Math.PI * 2));
        float f5 = -0.3f * MathHelper.sin(p_187465_3_ * (float)Math.PI);
        GlStateManager.translate(f * f3, f4 - 0.3f * f2, f5);
        GlStateManager.rotate(f2 * -45.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.rotate(f * f2 * -30.0f, 0.0f, 1.0f, 0.0f);
        this.renderMapFirstPerson(p_187465_4_);
        GlStateManager.popMatrix();
    }

    private void renderMapFirstPerson(float p_187463_1_, float p_187463_2_, float p_187463_3_) {
        float f = MathHelper.sqrt(p_187463_3_);
        float f1 = -0.2f * MathHelper.sin(p_187463_3_ * (float)Math.PI);
        float f2 = -0.4f * MathHelper.sin(f * (float)Math.PI);
        GlStateManager.translate(0.0f, -f1 / 2.0f, f2);
        float f3 = this.getMapAngleFromPitch(p_187463_1_);
        GlStateManager.translate(0.0f, 0.04f + p_187463_2_ * -1.2f + f3 * -0.5f, -0.72f);
        GlStateManager.rotate(f3 * -85.0f, 1.0f, 0.0f, 0.0f);
        this.renderArms();
        float f4 = MathHelper.sin(f * (float)Math.PI);
        GlStateManager.rotate(f4 * 20.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.scale(2.0f, 2.0f, 2.0f);
        this.renderMapFirstPerson(this.itemStackMainHand);
    }

    private void renderMapFirstPerson(ItemStack stack) {
        GlStateManager.rotate(180.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.scale(0.38f, 0.38f, 0.38f);
        GlStateManager.disableLighting();
        this.mc.getTextureManager().bindTexture(RES_MAP_BACKGROUND);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.translate(-0.5f, -0.5f, 0.0f);
        GlStateManager.scale(0.0078125f, 0.0078125f, 0.0078125f);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(-7.0, 135.0, 0.0).tex(0.0, 1.0).endVertex();
        bufferbuilder.pos(135.0, 135.0, 0.0).tex(1.0, 1.0).endVertex();
        bufferbuilder.pos(135.0, -7.0, 0.0).tex(1.0, 0.0).endVertex();
        bufferbuilder.pos(-7.0, -7.0, 0.0).tex(0.0, 0.0).endVertex();
        tessellator.draw();
        MapData mapdata = ReflectorForge.getMapData(Items.FILLED_MAP, stack, this.mc.world);
        if (mapdata != null) {
            this.mc.entityRenderer.getMapItemRenderer().renderMap(mapdata, false);
        }
        GlStateManager.enableLighting();
    }

    private void renderArmFirstPerson(float p_187456_1_, float p_187456_2_, EnumHandSide p_187456_3_) {
        EventTransformSideFirstPerson event = new EventTransformSideFirstPerson(p_187456_3_);
        EventManager.call(event);
        boolean flag = p_187456_3_ != EnumHandSide.LEFT;
        float f = flag ? 1.0f : -1.0f;
        float f1 = MathHelper.sqrt(p_187456_2_);
        float f2 = -0.3f * MathHelper.sin(f1 * (float)Math.PI);
        float f3 = 0.4f * MathHelper.sin(f1 * ((float)Math.PI * 2));
        float f4 = -0.4f * MathHelper.sin(p_187456_2_ * (float)Math.PI);
        GlStateManager.translate(f * (f2 + 0.64000005f), f3 + -0.6f + p_187456_1_ * -0.6f, f4 + -0.71999997f);
        GlStateManager.rotate(f * 45.0f, 0.0f, 1.0f, 0.0f);
        float f5 = MathHelper.sin(p_187456_2_ * p_187456_2_ * (float)Math.PI);
        float f6 = MathHelper.sin(f1 * (float)Math.PI);
        GlStateManager.rotate(f * f6 * 70.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(f * f5 * -20.0f, 0.0f, 0.0f, 1.0f);
        EntityPlayerSP abstractclientplayer = this.mc.player;
        this.mc.getTextureManager().bindTexture(abstractclientplayer.getLocationSkin());
        GlStateManager.translate(f * -1.0f, 3.6f, 3.5f);
        GlStateManager.rotate(f * 120.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.rotate(200.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.rotate(f * -135.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.translate(f * 5.6f, 0.0f, 0.0f);
        RenderPlayer renderplayer = (RenderPlayer)this.renderManager.getEntityRenderObject(abstractclientplayer);
        GlStateManager.disableCull();
        Color chamsColorValue = new Color(Chams.colorChams.getColor());
        Color color = new Color(chamsColorValue.getRed(), chamsColorValue.getGreen(), chamsColorValue.getBlue());
        if (Celestial.instance.featureManager.getFeatureByClass(Chams.class).getState() && Chams.chamsMode.currentMode.equals("Fill")) {
            GL11.glPushMatrix();
            GL11.glEnable(10754);
            GL11.glPolygonOffset(1.0f, 1000000.0f);
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0f, 240.0f);
            GL11.glEnable(3042);
            GL11.glDisable(3553);
            GL11.glDisable(2896);
            GL11.glBlendFunc(770, 771);
            if (!Chams.clientColor.getCurrentValue()) {
                GL11.glColor4f((float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, Chams.chamsAlpha.getCurrentValue());
            } else {
                GL11.glColor4f((float)ClientHelper.getClientColor().getRed() / 255.0f, (float)ClientHelper.getClientColor().getGreen() / 255.0f, (float)ClientHelper.getClientColor().getBlue() / 255.0f, Chams.chamsAlpha.getCurrentValue());
            }
            GL11.glDisable(2929);
            GL11.glDepthMask(false);
        }
        if (Celestial.instance.featureManager.getFeatureByClass(Chams.class).getState() && Chams.chamsMode.currentMode.equals("Cosmo")) {
            try {
                Chams.shaderAttach(this.mc.player);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        assert (renderplayer != null);
        if (flag) {
            renderplayer.renderRightArm(abstractclientplayer);
        } else {
            renderplayer.renderLeftArm(abstractclientplayer);
        }
        if (Celestial.instance.featureManager.getFeatureByClass(Chams.class).getState() && Chams.chamsMode.currentMode.equals("Fill")) {
            GL11.glEnable(2929);
            GL11.glDepthMask(true);
            if (!Chams.clientColor.getCurrentValue()) {
                GL11.glColor4f((float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, Chams.chamsAlpha.getCurrentValue());
            } else {
                GL11.glColor4f((float)ClientHelper.getClientColor().getRed() / 255.0f, (float)ClientHelper.getClientColor().getGreen() / 255.0f, (float)ClientHelper.getClientColor().getBlue() / 255.0f, Chams.chamsAlpha.getCurrentValue());
            }
            if (flag) {
                renderplayer.renderRightArm(abstractclientplayer);
            } else {
                renderplayer.renderLeftArm(abstractclientplayer);
            }
            GL11.glEnable(3553);
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            GL11.glDisable(3042);
            GL11.glEnable(2896);
            GL11.glPolygonOffset(1.0f, -1000000.0f);
            GL11.glDisable(10754);
            GL11.glPopMatrix();
        }
        if (Celestial.instance.featureManager.getFeatureByClass(Chams.class).getState() && Chams.chamsMode.currentMode.equals("Cosmo")) {
            Chams.shaderDetach();
        }
        GlStateManager.enableCull();
    }

    private void transformEatFirstPerson(float p_187454_1_, EnumHandSide p_187454_2_, ItemStack p_187454_3_) {
        int i;
        EventTransformSideFirstPerson event = new EventTransformSideFirstPerson(p_187454_2_);
        EventManager.call(event);
        float f = (float)this.mc.player.getItemInUseCount() - p_187454_1_ + 1.0f;
        float f1 = f / (float)p_187454_3_.getMaxItemUseDuration();
        float f3 = 1.0f - (float)Math.pow(f1, 27.0);
        int n = i = p_187454_2_ == EnumHandSide.RIGHT ? 1 : -1;
        if (f1 < 0.8f) {
            float f2 = MathHelper.abs(MathHelper.cos(f / 4.0f * (float)Math.PI) * 0.1f);
            GlStateManager.translate(0.0f, f2, 0.0f);
        }
        GlStateManager.translate(f3 * 0.6f * (float)i, f3 * -0.5f, f3 * 0.0f);
        if (Celestial.instance.featureManager.getFeatureByClass(ViewModel.class).getState()) {
            GlStateManager.rotate((float)i * f3 * 20.0f, 0.0f, 1.0f, 0.0f);
        } else {
            GlStateManager.rotate((float)i * f3 * 90.0f, 0.0f, 1.0f, 0.0f);
        }
        GlStateManager.rotate(f3 * 10.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.rotate((float)i * f3 * 30.0f, 0.0f, 0.0f, 1.0f);
        if (Celestial.instance.featureManager.getFeatureByClass(SwingAnimations.class).getState() && SwingAnimations.smallItem.getCurrentValue()) {
            GlStateManager.scale(SwingAnimations.smallItemSize.getCurrentValue(), SwingAnimations.smallItemSize.getCurrentValue(), SwingAnimations.smallItemSize.getCurrentValue());
        }
    }

    private void transformFirstPerson(EnumHandSide p_187453_1_, float p_187453_2_) {
        float angle = System.currentTimeMillis() / (long)((int)SwingAnimations.item360Speed.getCurrentValue()) % 360L;
        int i = p_187453_1_ == EnumHandSide.RIGHT ? 1 : -1;
        float f = MathHelper.sin(p_187453_2_ * p_187453_2_ * (float)Math.PI);
        GlStateManager.rotate((float)i * (45.0f + f * -20.0f), 0.0f, 1.0f, 0.0f);
        float f1 = MathHelper.sin(MathHelper.sqrt(p_187453_2_) * (float)Math.PI);
        GlStateManager.rotate((float)i * f1 * -20.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.rotate(f1 * -80.0f, 1.0f, 0.0f, 0.0f);
        if (Celestial.instance.featureManager.getFeatureByClass(SwingAnimations.class).getState() && SwingAnimations.item360.getCurrentValue()) {
            if (SwingAnimations.item360Hand.currentMode.equals("Left") && p_187453_1_ != EnumHandSide.LEFT || SwingAnimations.item360Hand.currentMode.equals("Right") && p_187453_1_ != EnumHandSide.RIGHT && !SwingAnimations.item360Hand.currentMode.equals("All")) {
                return;
            }
            GlStateManager.rotate(angle, 0.0f, SwingAnimations.item360Mode.currentMode.equals("Horizontal") ? 1.0f : 0.0f, SwingAnimations.item360Mode.currentMode.equals("Vertical") ? angle : 0.0f);
        } else {
            GlStateManager.rotate((float)i * -45.0f, 0.0f, 1.0f, 0.0f);
        }
        GlStateManager.translate(0.0f, 0.02f, 0.0f);
    }

    private void transformSideFirstPerson(EnumHandSide p_187459_1_, float p_187459_2_) {
        EventTransformSideFirstPerson event = new EventTransformSideFirstPerson(p_187459_1_);
        EventManager.call(event);
        int i = p_187459_1_ == EnumHandSide.RIGHT ? 1 : -1;
        GlStateManager.translate((float)i * 0.56f, -0.52f + p_187459_2_ * -0.6f, -0.72f);
        if (Celestial.instance.featureManager.getFeatureByClass(SwingAnimations.class).getState() && SwingAnimations.smallItem.getCurrentValue()) {
            GlStateManager.scale(SwingAnimations.smallItemSize.getCurrentValue(), SwingAnimations.smallItemSize.getCurrentValue(), SwingAnimations.smallItemSize.getCurrentValue());
        }
    }

    private void translate() {
        GlStateManager.translate(-0.5f, 0.08f, 0.0f);
        GlStateManager.rotate(20.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(-80.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.rotate(20.0f, 0.0f, 1.0f, 0.0f);
    }

    public void renderItemInFirstPerson(float partialTicks) {
        block9: {
            float f6;
            float f4;
            float f1;
            EntityPlayerSP abstractclientplayer;
            block10: {
                boolean flag1;
                EnumHand enumhand;
                float f;
                block7: {
                    float f5;
                    float f3;
                    block8: {
                        ItemStack itemstack;
                        abstractclientplayer = this.mc.player;
                        f = abstractclientplayer.getSwingProgress(partialTicks);
                        enumhand = MoreObjects.firstNonNull(abstractclientplayer.swingingHand, EnumHand.MAIN_HAND);
                        f1 = abstractclientplayer.prevRotationPitch + (abstractclientplayer.rotationPitch - abstractclientplayer.prevRotationPitch) * partialTicks;
                        float f2 = abstractclientplayer.prevRotationYaw + (abstractclientplayer.rotationYaw - abstractclientplayer.prevRotationYaw) * partialTicks;
                        boolean flag = true;
                        flag1 = true;
                        if (((EntityLivingBase)abstractclientplayer).isHandActive() && !(itemstack = abstractclientplayer.getActiveItemStack()).isEmpty() && itemstack.getItem() == Items.BOW) {
                            EnumHand enumhand1 = ((EntityLivingBase)abstractclientplayer).getActiveHand();
                            flag = enumhand1 == EnumHand.MAIN_HAND;
                            flag1 = !flag;
                        }
                        this.rotateArroundXAndY(f1, f2);
                        this.setLightmap();
                        this.rotateArm(partialTicks);
                        GlStateManager.enableRescaleNormal();
                        if (!flag) break block7;
                        f3 = enumhand == EnumHand.MAIN_HAND ? f : 0.0f;
                        f5 = 1.0f - (this.prevEquippedProgressMainHand + (this.equippedProgressMainHand - this.prevEquippedProgressMainHand) * partialTicks);
                        if (!Reflector.ForgeHooksClient_renderSpecificFirstPersonHand.exists()) break block8;
                        if (Reflector.callBoolean(Reflector.ForgeHooksClient_renderSpecificFirstPersonHand, new Object[]{EnumHand.MAIN_HAND, Float.valueOf(partialTicks), Float.valueOf(f1), Float.valueOf(f3), Float.valueOf(f5), this.itemStackMainHand})) break block7;
                    }
                    this.renderItemInFirstPerson(abstractclientplayer, partialTicks, f1, EnumHand.MAIN_HAND, f3, this.itemStackMainHand, f5);
                }
                if (!flag1) break block9;
                f4 = enumhand == EnumHand.OFF_HAND ? f : 0.0f;
                f6 = 1.0f - (this.prevEquippedProgressOffHand + (this.equippedProgressOffHand - this.prevEquippedProgressOffHand) * partialTicks);
                if (!Reflector.ForgeHooksClient_renderSpecificFirstPersonHand.exists()) break block10;
                if (Reflector.callBoolean(Reflector.ForgeHooksClient_renderSpecificFirstPersonHand, new Object[]{EnumHand.OFF_HAND, Float.valueOf(partialTicks), Float.valueOf(f1), Float.valueOf(f4), Float.valueOf(f6), this.itemStackOffHand})) break block9;
            }
            this.renderItemInFirstPerson(abstractclientplayer, partialTicks, f1, EnumHand.OFF_HAND, f4, this.itemStackOffHand, f6);
        }
        GlStateManager.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
    }

    private void transformFirstPersonItem(float equipProgress, float swingProgress) {
        GlStateManager.translate(0.56f, -0.44f, -0.71999997f);
        GlStateManager.translate(0.0f, equipProgress * -0.6f, 0.0f);
        GlStateManager.rotate(45.0f, 0.0f, 1.0f, 0.0f);
        float f = MathHelper.sin(swingProgress * swingProgress * (float)Math.PI);
        float f2 = MathHelper.sin(MathHelper.sqrt(swingProgress) * (float)Math.PI);
        GlStateManager.rotate(f * -20.0f, 0.0f, 0.0f, 0.0f);
        GlStateManager.rotate(f2 * -20.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.rotate(f2 * -80.0f, 0.01f, 0.0f, 0.0f);
        GlStateManager.translate(0.4f, 0.2f, 0.2f);
    }

    public void renderItemInFirstPerson(AbstractClientPlayer p_187457_1_, float p_187457_2_, float p_187457_3_, EnumHand p_187457_4_, float p_187457_5_, ItemStack p_187457_6_, float p_187457_7_) {
        if (!Config.isShaders() || !Shaders.isSkipRenderHand(p_187457_4_)) {
            boolean flag = p_187457_4_ == EnumHand.MAIN_HAND;
            EnumHandSide enumhandside = flag ? p_187457_1_.getPrimaryHand() : p_187457_1_.getPrimaryHand().opposite();
            GlStateManager.pushMatrix();
            if (p_187457_6_.isEmpty()) {
                if (flag && !p_187457_1_.isInvisible()) {
                    this.renderArmFirstPerson(p_187457_7_, p_187457_5_, enumhandside);
                }
            } else if (p_187457_6_.getItem() instanceof ItemMap) {
                if (flag && this.itemStackOffHand.isEmpty()) {
                    this.renderMapFirstPerson(p_187457_3_, p_187457_7_, p_187457_5_);
                } else {
                    this.renderMapFirstPersonSide(p_187457_7_, enumhandside, p_187457_5_, p_187457_6_);
                }
            } else {
                boolean flag1;
                boolean bl = flag1 = enumhandside == EnumHandSide.RIGHT;
                if (p_187457_1_.isHandActive() && p_187457_1_.getItemInUseCount() > 0 && p_187457_1_.getActiveHand() == p_187457_4_) {
                    int j = flag1 ? 1 : -1;
                    switch (p_187457_6_.getItemUseAction()) {
                        case NONE: {
                            this.transformSideFirstPerson(enumhandside, p_187457_7_);
                            break;
                        }
                        case EAT: 
                        case DRINK: {
                            this.transformEatFirstPerson(p_187457_2_, enumhandside, p_187457_6_);
                            this.transformSideFirstPerson(enumhandside, p_187457_7_);
                            break;
                        }
                        case BLOCK: {
                            this.transformSideFirstPerson(enumhandside, p_187457_7_);
                            break;
                        }
                        case BOW: {
                            this.transformSideFirstPerson(enumhandside, p_187457_7_);
                            GlStateManager.translate((float)j * -0.2785682f, 0.18344387f, 0.15731531f);
                            GlStateManager.rotate(-13.935f, 1.0f, 0.0f, 0.0f);
                            GlStateManager.rotate((float)j * 35.3f, 0.0f, 1.0f, 0.0f);
                            GlStateManager.rotate((float)j * -9.785f, 0.0f, 0.0f, 1.0f);
                            float f5 = (float)p_187457_6_.getMaxItemUseDuration() - ((float)this.mc.player.getItemInUseCount() - p_187457_2_ + 1.0f);
                            float f6 = f5 / 20.0f;
                            f6 = (f6 * f6 + f6 * 2.0f) / 3.0f;
                            if (f6 > 1.0f) {
                                f6 = 1.0f;
                            }
                            if (f6 > 0.1f) {
                                float f7 = MathHelper.sin((f5 - 0.1f) * 1.3f);
                                float f3 = f6 - 0.1f;
                                float f4 = f7 * f3;
                                GlStateManager.translate(f4 * 0.0f, f4 * 0.004f, f4 * 0.0f);
                            }
                            GlStateManager.translate(f6 * 0.0f, f6 * 0.0f, f6 * 0.04f);
                            GlStateManager.scale(1.0f, 1.0f, 1.0f + f6 * 0.2f);
                            GlStateManager.rotate((float)j * 45.0f, 0.0f, -1.0f, 0.0f);
                        }
                    }
                } else {
                    float f = -0.4f * MathHelper.sin(MathHelper.sqrt(p_187457_5_) * (float)Math.PI);
                    float f1 = 0.2f * MathHelper.sin(MathHelper.sqrt(p_187457_5_) * ((float)Math.PI * 2));
                    float f2 = -0.2f * MathHelper.sin(p_187457_5_ * (float)Math.PI);
                    int i = flag1 ? 1 : -1;
                    float equipProgress = 1.0f - (this.prevEquippedProgressMainHand + (this.equippedProgressMainHand - this.prevEquippedProgressMainHand) * p_187457_2_);
                    float swingprogress = this.mc.player.getSwingProgress(p_187457_2_);
                    String mode = SwingAnimations.swordAnim.getCurrentMode();
                    if (Celestial.instance.featureManager.getFeatureByClass(SwingAnimations.class).getState() && (this.mc.gameSettings.keyBindAttack.pressed && !SwingAnimations.auraOnly.getCurrentValue() || KillAura.target != null)) {
                        if (Celestial.instance.featureManager.getFeatureByClass(SwingAnimations.class).getState() && (this.mc.gameSettings.keyBindAttack.pressed && !SwingAnimations.auraOnly.getCurrentValue() || KillAura.target != null)) {
                            if (enumhandside != (this.mc.gameSettings.mainHand.equals((Object)EnumHandSide.LEFT) ? EnumHandSide.RIGHT : EnumHandSide.LEFT)) {
                                float smooth = swingprogress * 0.8f - swingprogress * swingprogress * 0.8f;
                                if (mode.equalsIgnoreCase("Celestial")) {
                                    this.transformFirstPersonItem(equipProgress / 3.0f, swingprogress);
                                    this.translate();
                                } else if (mode.equalsIgnoreCase("Spin")) {
                                    this.transformFirstPersonItem(0.0f, 0.0f);
                                    this.translate();
                                    GlStateManager.rotate(this.spin * SwingAnimations.spinSpeed.getCurrentValue(), this.spin, 0.0f, this.spin);
                                    this.spin += 1.0f;
                                } else if (mode.equalsIgnoreCase("Fap")) {
                                    GlStateManager.translate(0.96f, -0.02f, -0.71999997f);
                                    GlStateManager.translate(0.0f, -0.0f, 0.0f);
                                    GlStateManager.rotate(45.0f, 0.0f, 1.0f, 0.0f);
                                    float var3 = MathHelper.sin(0.0f);
                                    float var4 = MathHelper.sin(MathHelper.sqrt(0.0f) * (float)Math.PI);
                                    GlStateManager.rotate(var3 * -20.0f, 0.0f, 1.0f, 0.0f);
                                    GlStateManager.rotate(var4 * -20.0f, 0.0f, 0.0f, 1.0f);
                                    GlStateManager.rotate(var4 * -80.0f, 1.0f, 0.0f, 0.0f);
                                    GlStateManager.translate(-0.5f, 0.2f, 0.0f);
                                    GlStateManager.rotate(30.0f, 0.0f, 1.0f, 0.0f);
                                    GlStateManager.rotate(-80.0f, 1.0f, 0.0f, 0.0f);
                                    GlStateManager.rotate(60.0f, 0.0f, 1.0f, 0.0f);
                                    int alpha = (int)Math.min(255L, (System.currentTimeMillis() % 255L > 127L ? Math.abs(Math.abs(System.currentTimeMillis()) % 255L - 255L) : System.currentTimeMillis() % 255L) * 2L);
                                    float f5 = (double)f1 > 0.5 ? 1.0f - f1 : f1;
                                    GlStateManager.translate(0.3f, -0.0f, 0.4f);
                                    GlStateManager.rotate(0.0f, 0.0f, 0.0f, 1.0f);
                                    GlStateManager.translate(0.0f, 0.5f, 0.0f);
                                    GlStateManager.rotate(90.0f, 1.0f, 0.0f, -1.0f);
                                    GlStateManager.translate(0.6f, 0.5f, 0.0f);
                                    GlStateManager.rotate(-90.0f, 1.0f, 0.0f, -1.0f);
                                    GlStateManager.rotate(-10.0f, 1.0f, 0.0f, -1.0f);
                                    GlStateManager.rotate(-f5 * 10.0f, 10.0f, 10.0f, -9.0f);
                                    GlStateManager.rotate(10.0f, -1.0f, 0.0f, 0.0f);
                                    GlStateManager.translate(0.0, 0.0, -0.5);
                                    GlStateManager.rotate(this.mc.player.isSwingInProgress ? (float)(-alpha) / SwingAnimations.fapSmooth.getCurrentValue() : 1.0f, 1.0f, -0.0f, 1.0f);
                                    GlStateManager.translate(0.0, 0.0, 0.5);
                                } else if (mode.equalsIgnoreCase("Jello")) {
                                    GlStateManager.translate(0.56f, -0.52f, -0.71999997f);
                                    GlStateManager.translate(0.0f, equipProgress * -0.15f, 0.0f);
                                    GlStateManager.rotate(45.0f, 0.0f, 1.0f, 0.0f);
                                    GlStateManager.rotate(smooth * -90.0f, 0.0f, 1.0f, 0.0f);
                                    GlStateManager.scale(0.37f, 0.37f, 0.37f);
                                    GlStateManager.translate(-0.5f, 1.0f, 0.0f);
                                    GlStateManager.rotate(30.0f, 0.0f, 1.0f, 0.0f);
                                    GlStateManager.rotate(-80.0f, 1.0f, 0.0f, 0.0f);
                                    GlStateManager.rotate(60.0f, 0.0f, 1.0f, 0.0f);
                                } else if (mode.equalsIgnoreCase("Sigma")) {
                                    float sigma = MathHelper.sin(MathHelper.sqrt(swingprogress) * (float)Math.PI);
                                    GlStateManager.translate(0.56f, -0.42f, -0.71999997f);
                                    GlStateManager.translate(0.0f, equipProgress * 0.5f * -0.6f, 0.0f);
                                    GlStateManager.rotate(45.0f, 0.0f, 1.0f, 0.0f);
                                    float sigmaHatar1 = MathHelper.sin(0.0f);
                                    float sigmaHatar2 = MathHelper.sin(MathHelper.sqrt(0.0f) * (float)Math.PI);
                                    GlStateManager.rotate(sigmaHatar1 * -20.0f, 0.0f, 1.0f, 0.0f);
                                    GlStateManager.rotate(sigmaHatar2 * -20.0f, 0.0f, 0.0f, 1.0f);
                                    GlStateManager.rotate(sigmaHatar2 * -80.0f, 1.0f, 0.0f, 0.0f);
                                    GlStateManager.scale(0.4f, 0.4f, 0.4f);
                                    GlStateManager.rotate(-sigma * 55.0f / 2.0f, -8.0f, -0.0f, 9.0f);
                                    GlStateManager.rotate(-sigma * 45.0f, 1.0f, sigma / 2.0f, -0.0f);
                                    GlStateManager.translate(-0.5f, 0.2f, 0.0f);
                                    GlStateManager.rotate(30.0f, 0.0f, 1.0f, 0.0f);
                                    GlStateManager.rotate(-80.0f, 1.0f, 0.0f, 0.0f);
                                    GlStateManager.rotate(60.0f, 0.0f, 1.0f, 0.0f);
                                    GL11.glTranslated(1.2, 0.3, 0.5);
                                    GL11.glTranslatef(-1.0f, this.mc.player.isSneaking() ? -0.1f : -0.2f, 0.2f);
                                    GlStateManager.scale(1.2f, 1.2f, 1.2f);
                                } else if (mode.equalsIgnoreCase("Swank")) {
                                    GlStateManager.translate(0.56f, -0.52f, -0.9999997f);
                                    GlStateManager.translate(0.0f, 0.0f, 0.0f);
                                    this.transformFirstPersonItem(equipProgress / 2.0f, swingprogress);
                                    GlStateManager.translate(-0.7f, 0.2f, 0.0f);
                                    f2 = MathHelper.sin(MathHelper.sqrt(swingprogress) * (float)Math.PI);
                                    GlStateManager.rotate(f2 * 30.0f, -f2, -0.0f, 9.0f);
                                    GlStateManager.rotate(f2 * 40.0f, 1.0f, -f2, -0.0f);
                                } else if (mode.equalsIgnoreCase("Astolfo")) {
                                    GlStateManager.rotate(System.currentTimeMillis() / 16L * (long)((int)SwingAnimations.spinSpeed.getCurrentValue()) % 360L, 0.0f, 0.0f, -0.1f);
                                    this.transformFirstPersonItem(0.0f, 0.0f);
                                    this.translate();
                                } else if (mode.equalsIgnoreCase("Big")) {
                                    GlStateManager.translate(0.56f, -0.52f, -0.71999997f);
                                    GlStateManager.translate(0.0f, 0.0f, 0.0f);
                                    GlStateManager.rotate(45.0f, 0.0f, 1.0f, 0.0f);
                                    float sin1 = MathHelper.sin(swingprogress * swingprogress * (float)Math.PI);
                                    float sin2 = MathHelper.sin(MathHelper.sqrt(swingprogress) * (float)Math.PI);
                                    GlStateManager.rotate(sin1 * -20.0f, 0.0f, 1.0f, 0.0f);
                                    GlStateManager.rotate(sin2 * -20.0f, 0.0f, 0.0f, 1.0f);
                                    GlStateManager.rotate(sin2 * -40.0f, 1.0f, 0.0f, 0.0f);
                                    GlStateManager.scale(0.8f, 0.8f, 0.8f);
                                    GlStateManager.translate(-0.5f, 0.2f, 0.0f);
                                    GlStateManager.rotate(30.0f, 0.0f, 1.0f, 0.0f);
                                    GlStateManager.rotate(-80.0f, 1.0f, 0.0f, 0.0f);
                                    GlStateManager.rotate(60.0f, 0.0f, 1.0f, 0.0f);
                                } else if (mode.equalsIgnoreCase("Neutral")) {
                                    this.transformFirstPersonItem(0.0f, 0.0f);
                                    this.translate();
                                } else if (mode.equalsIgnoreCase("Custom")) {
                                    this.transformFirstPersonItem(0.0f, 0.0f);
                                    if (this.mc.player.isSwingInProgress) {
                                        GlStateManager.translate(SwingAnimations.SwingX.getCurrentValue(), SwingAnimations.SwingY.getCurrentValue(), SwingAnimations.SwingZ.getCurrentValue());
                                        GlStateManager.rotate(SwingAnimations.SwingAngle.getCurrentValue(), SwingAnimations.SwingRotate1.getCurrentValue(), SwingAnimations.SwingRotate2.getCurrentValue(), SwingAnimations.SwingRotate3.getCurrentValue());
                                        GlStateManager.scale(SwingAnimations.scale.getCurrentValue(), SwingAnimations.scale.getCurrentValue(), SwingAnimations.scale.getCurrentValue());
                                    } else {
                                        GlStateManager.translate(SwingAnimations.x.getCurrentValue(), SwingAnimations.y.getCurrentValue(), SwingAnimations.z.getCurrentValue());
                                        GlStateManager.rotate(SwingAnimations.angle.getCurrentValue(), SwingAnimations.rotate1.getCurrentValue(), SwingAnimations.rotate2.getCurrentValue(), SwingAnimations.rotate3.getCurrentValue());
                                        GlStateManager.scale(SwingAnimations.scale.getCurrentValue(), SwingAnimations.scale.getCurrentValue(), SwingAnimations.scale.getCurrentValue());
                                    }
                                }
                                if (Celestial.instance.featureManager.getFeatureByClass(ViewModel.class).getState()) {
                                    GlStateManager.scale(ViewModel.mainScaleX.getCurrentValue(), ViewModel.mainScaleY.getCurrentValue(), ViewModel.mainScaleZ.getCurrentValue());
                                } else if (Celestial.instance.featureManager.getFeatureByClass(SwingAnimations.class).getState() && SwingAnimations.smallItem.getCurrentValue()) {
                                    GlStateManager.scale(SwingAnimations.smallItemSize.getCurrentValue(), SwingAnimations.smallItemSize.getCurrentValue(), SwingAnimations.smallItemSize.getCurrentValue());
                                }
                            } else {
                                GlStateManager.translate((float)i * f, f1, f2);
                                this.transformSideFirstPerson(enumhandside, p_187457_7_);
                                this.transformFirstPerson(enumhandside, p_187457_5_);
                            }
                        } else {
                            this.transformSideFirstPerson(enumhandside, p_187457_7_);
                            this.transformFirstPerson(enumhandside, p_187457_5_);
                        }
                    } else {
                        GlStateManager.translate((float)i * f, f1, f2);
                        this.transformSideFirstPerson(enumhandside, p_187457_7_);
                        this.transformFirstPerson(enumhandside, p_187457_5_);
                    }
                }
                this.renderItemSide(p_187457_1_, p_187457_6_, flag1 ? ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND : ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND, !flag1);
            }
            GlStateManager.popMatrix();
        }
    }

    public void renderOverlays(float partialTicks) {
        GlStateManager.disableAlpha();
        if (this.mc.player.isEntityInsideOpaqueBlock()) {
            IBlockState iblockstate = this.mc.world.getBlockState(new BlockPos(this.mc.player));
            BlockPos blockpos = new BlockPos(this.mc.player);
            EntityPlayerSP entityplayer = this.mc.player;
            for (int i = 0; i < 8; ++i) {
                double d0 = entityplayer.posX + (double)(((float)((i >> 0) % 2) - 0.5f) * entityplayer.width * 0.8f);
                double d1 = entityplayer.posY + (double)(((float)((i >> 1) % 2) - 0.5f) * 0.1f);
                double d2 = entityplayer.posZ + (double)(((float)((i >> 2) % 2) - 0.5f) * entityplayer.width * 0.8f);
                BlockPos blockpos1 = new BlockPos(d0, d1 + (double)entityplayer.getEyeHeight(), d2);
                IBlockState iblockstate1 = this.mc.world.getBlockState(blockpos1);
                if (!iblockstate1.func_191058_s()) continue;
                iblockstate = iblockstate1;
                blockpos = blockpos1;
            }
            if (iblockstate.getRenderType() != EnumBlockRenderType.INVISIBLE) {
                Object object = Reflector.getFieldValue(Reflector.RenderBlockOverlayEvent_OverlayType_BLOCK);
                if (!Reflector.callBoolean(Reflector.ForgeEventFactory_renderBlockOverlay, this.mc.player, Float.valueOf(partialTicks), object, iblockstate, blockpos)) {
                    this.renderBlockInHand(this.mc.getBlockRendererDispatcher().getBlockModelShapes().getTexture(iblockstate));
                }
            }
        }
        if (!this.mc.player.isSpectator()) {
            if (this.mc.player.isInsideOfMaterial(Material.WATER)) {
                if (!Reflector.callBoolean(Reflector.ForgeEventFactory_renderWaterOverlay, this.mc.player, Float.valueOf(partialTicks))) {
                    this.renderWaterOverlayTexture(partialTicks);
                }
            }
            if (this.mc.player.isBurning()) {
                if (!Reflector.callBoolean(Reflector.ForgeEventFactory_renderFireOverlay, this.mc.player, Float.valueOf(partialTicks))) {
                    this.renderFireInFirstPerson();
                }
            }
        }
        GlStateManager.enableAlpha();
    }

    private void renderBlockInHand(TextureAtlasSprite partialTicks) {
        this.mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        float f = 0.1f;
        GlStateManager.color(0.1f, 0.1f, 0.1f, 0.5f);
        GlStateManager.pushMatrix();
        float f1 = -1.0f;
        float f2 = 1.0f;
        float f3 = -1.0f;
        float f4 = 1.0f;
        float f5 = -0.5f;
        float f6 = partialTicks.getMinU();
        float f7 = partialTicks.getMaxU();
        float f8 = partialTicks.getMinV();
        float f9 = partialTicks.getMaxV();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(-1.0, -1.0, -0.5).tex(f7, f9).endVertex();
        bufferbuilder.pos(1.0, -1.0, -0.5).tex(f6, f9).endVertex();
        bufferbuilder.pos(1.0, 1.0, -0.5).tex(f6, f8).endVertex();
        bufferbuilder.pos(-1.0, 1.0, -0.5).tex(f7, f8).endVertex();
        tessellator.draw();
        GlStateManager.popMatrix();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
    }

    private void renderWaterOverlayTexture(float partialTicks) {
        if (!Config.isShaders() || Shaders.isUnderwaterOverlay()) {
            this.mc.getTextureManager().bindTexture(RES_UNDERWATER_OVERLAY);
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuffer();
            float f = this.mc.player.getBrightness();
            GlStateManager.color(f, f, f, 0.5f);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.pushMatrix();
            float f1 = 4.0f;
            float f2 = -1.0f;
            float f3 = 1.0f;
            float f4 = -1.0f;
            float f5 = 1.0f;
            float f6 = -0.5f;
            float f7 = -this.mc.player.rotationYaw / 64.0f;
            float f8 = this.mc.player.rotationPitch / 64.0f;
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
            bufferbuilder.pos(-1.0, -1.0, -0.5).tex(4.0f + f7, 4.0f + f8).endVertex();
            bufferbuilder.pos(1.0, -1.0, -0.5).tex(0.0f + f7, 4.0f + f8).endVertex();
            bufferbuilder.pos(1.0, 1.0, -0.5).tex(0.0f + f7, 0.0f + f8).endVertex();
            bufferbuilder.pos(-1.0, 1.0, -0.5).tex(4.0f + f7, 0.0f + f8).endVertex();
            tessellator.draw();
            GlStateManager.popMatrix();
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.disableBlend();
        }
    }

    private void renderFireInFirstPerson() {
        if (Celestial.instance.featureManager.getFeatureByClass(NoRender.class).getState() && NoRender.fire.getCurrentValue()) {
            return;
        }
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 0.9f);
        GlStateManager.depthFunc(519);
        GlStateManager.depthMask(false);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        float f = 1.0f;
        for (int i = 0; i < 2; ++i) {
            GlStateManager.pushMatrix();
            TextureAtlasSprite textureatlassprite = this.mc.getTextureMapBlocks().getAtlasSprite("minecraft:blocks/fire_layer_1");
            this.mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            float f1 = textureatlassprite.getMinU();
            float f2 = textureatlassprite.getMaxU();
            float f3 = textureatlassprite.getMinV();
            float f4 = textureatlassprite.getMaxV();
            float f5 = -0.5f;
            float f6 = 0.5f;
            float f7 = -0.5f;
            float f8 = 0.5f;
            float f9 = -0.5f;
            GlStateManager.translate((float)(-(i * 2 - 1)) * 0.24f, -0.3f, 0.0f);
            GlStateManager.rotate((float)(i * 2 - 1) * 10.0f, 0.0f, 1.0f, 0.0f);
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
            bufferbuilder.pos(-0.5, -0.5, -0.5).tex(f2, f4).endVertex();
            bufferbuilder.pos(0.5, -0.5, -0.5).tex(f1, f4).endVertex();
            bufferbuilder.pos(0.5, 0.5, -0.5).tex(f1, f3).endVertex();
            bufferbuilder.pos(-0.5, 0.5, -0.5).tex(f2, f3).endVertex();
            tessellator.draw();
            GlStateManager.popMatrix();
        }
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.disableBlend();
        GlStateManager.depthMask(true);
        GlStateManager.depthFunc(515);
    }

    public void updateEquippedItem() {
        this.prevEquippedProgressMainHand = this.equippedProgressMainHand;
        this.prevEquippedProgressOffHand = this.equippedProgressOffHand;
        EntityPlayerSP entityplayersp = this.mc.player;
        ItemStack itemstack = entityplayersp.getHeldItemMainhand();
        ItemStack itemstack1 = entityplayersp.getHeldItemOffhand();
        if (entityplayersp.isRowingBoat()) {
            this.equippedProgressMainHand = MathHelper.clamp(this.equippedProgressMainHand - 0.4f, 0.0f, 1.0f);
            this.equippedProgressOffHand = MathHelper.clamp(this.equippedProgressOffHand - 0.4f, 0.0f, 1.0f);
        } else {
            float f = entityplayersp.getCooledAttackStrength(1.0f);
            if (Reflector.ForgeHooksClient_shouldCauseReequipAnimation.exists()) {
                boolean flag = Reflector.callBoolean(Reflector.ForgeHooksClient_shouldCauseReequipAnimation, this.itemStackMainHand, itemstack, entityplayersp.inventory.currentItem);
                boolean flag1 = Reflector.callBoolean(Reflector.ForgeHooksClient_shouldCauseReequipAnimation, this.itemStackOffHand, itemstack1, -1);
                if (!flag && !Objects.equals(this.itemStackMainHand, itemstack)) {
                    this.itemStackMainHand = itemstack;
                }
                if (!flag && !Objects.equals(this.itemStackOffHand, itemstack1)) {
                    this.itemStackOffHand = itemstack1;
                }
                this.equippedProgressMainHand += MathHelper.clamp((!flag ? f * f * f : 0.0f) - this.equippedProgressMainHand, -0.4f, 0.4f);
                this.equippedProgressOffHand += MathHelper.clamp((float)(!flag1 ? 1 : 0) - this.equippedProgressOffHand, -0.4f, 0.4f);
            } else {
                this.equippedProgressMainHand += MathHelper.clamp((Objects.equals(this.itemStackMainHand, itemstack) ? f * f * f : 0.0f) - this.equippedProgressMainHand, -0.4f, 0.4f);
                this.equippedProgressOffHand += MathHelper.clamp((float)(Objects.equals(this.itemStackOffHand, itemstack1) ? 1 : 0) - this.equippedProgressOffHand, -0.4f, 0.4f);
            }
        }
        if (this.equippedProgressMainHand < 0.1f) {
            this.itemStackMainHand = itemstack;
            if (Config.isShaders()) {
                Shaders.setItemToRenderMain(this.itemStackMainHand);
            }
        }
        if (this.equippedProgressOffHand < 0.1f) {
            this.itemStackOffHand = itemstack1;
            if (Config.isShaders()) {
                Shaders.setItemToRenderOff(this.itemStackOffHand);
            }
        }
    }

    public void resetEquippedProgress(EnumHand hand) {
        if (hand == EnumHand.MAIN_HAND) {
            this.equippedProgressMainHand = 0.0f;
        } else {
            this.equippedProgressOffHand = 0.0f;
        }
    }
}

