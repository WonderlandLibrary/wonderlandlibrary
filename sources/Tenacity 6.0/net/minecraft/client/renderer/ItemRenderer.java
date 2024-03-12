// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer;

import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.material.Material;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.item.EnumAction;
import dev.tenacity.event.Event;
import dev.tenacity.event.impl.render.CustomBlockRenderEvent;
import dev.tenacity.utils.player.InventoryUtils;
import net.minecraft.item.ItemSword;
import dev.tenacity.module.impl.combat.KillAura;
import net.minecraft.item.ItemMap;
import dev.tenacity.module.Module;
import dev.tenacity.module.impl.render.Animations;
import dev.tenacity.Tenacity;
import net.minecraft.world.storage.MapData;
import net.minecraft.world.World;
import net.minecraft.init.Items;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.client.entity.EntityPlayerSP;
import net.optifine.DynamicLights;
import net.minecraft.util.BlockPos;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.item.Item;
import net.optifine.shaders.Shaders;
import net.minecraft.src.Config;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemStack;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class ItemRenderer
{
    private static final ResourceLocation RES_MAP_BACKGROUND;
    private static final ResourceLocation RES_UNDERWATER_OVERLAY;
    private final Minecraft mc;
    private ItemStack itemToRender;
    private float equippedProgress;
    private float prevEquippedProgress;
    private final RenderManager renderManager;
    private final RenderItem itemRenderer;
    private int equippedItemSlot;
    
    public ItemRenderer(final Minecraft mcIn) {
        this.equippedItemSlot = -1;
        this.mc = mcIn;
        this.renderManager = mcIn.getRenderManager();
        this.itemRenderer = mcIn.getRenderItem();
    }
    
    public void renderItem(final EntityLivingBase entityIn, final ItemStack heldStack, final ItemCameraTransforms.TransformType transform) {
        if (heldStack != null) {
            final Item item = heldStack.getItem();
            final Block block = Block.getBlockFromItem(item);
            GlStateManager.pushMatrix();
            if (this.itemRenderer.shouldRenderItemIn3D(heldStack)) {
                GlStateManager.scale(2.0f, 2.0f, 2.0f);
                if (this.isBlockTranslucent(block) && (!Config.isShaders() || !Shaders.renderItemKeepDepthMask)) {
                    GlStateManager.depthMask(false);
                }
            }
            this.itemRenderer.renderItemModelForEntity(heldStack, entityIn, transform);
            if (this.isBlockTranslucent(block)) {
                GlStateManager.depthMask(true);
            }
            GlStateManager.popMatrix();
        }
    }
    
    private boolean isBlockTranslucent(final Block blockIn) {
        return blockIn != null && blockIn.getBlockLayer() == EnumWorldBlockLayer.TRANSLUCENT;
    }
    
    private void rotateArroundXAndY(final float angle, final float angleY) {
        GlStateManager.pushMatrix();
        GlStateManager.rotate(angle, 1.0f, 0.0f, 0.0f);
        GlStateManager.rotate(angleY, 0.0f, 1.0f, 0.0f);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.popMatrix();
    }
    
    private void setLightMapFromPlayer(final AbstractClientPlayer clientPlayer) {
        int i = this.mc.theWorld.getCombinedLight(new BlockPos(clientPlayer.posX, clientPlayer.posY + clientPlayer.getEyeHeight(), clientPlayer.posZ), 0);
        if (Config.isDynamicLights()) {
            i = DynamicLights.getCombinedLight(this.mc.getRenderViewEntity(), i);
        }
        final float f = (float)(i & 0xFFFF);
        final float f2 = (float)(i >> 16);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, f, f2);
    }
    
    private void rotateWithPlayerRotations(final EntityPlayerSP entityplayerspIn, final float partialTicks) {
        final float f = entityplayerspIn.prevRenderArmPitch + (entityplayerspIn.renderArmPitch - entityplayerspIn.prevRenderArmPitch) * partialTicks;
        final float f2 = entityplayerspIn.prevRenderArmYaw + (entityplayerspIn.renderArmYaw - entityplayerspIn.prevRenderArmYaw) * partialTicks;
        GlStateManager.rotate((entityplayerspIn.rotationPitch - f) * 0.1f, 1.0f, 0.0f, 0.0f);
        GlStateManager.rotate((entityplayerspIn.rotationYaw - f2) * 0.1f, 0.0f, 1.0f, 0.0f);
    }
    
    private float getMapAngleFromPitch(final float pitch) {
        float f = 1.0f - pitch / 45.0f + 0.1f;
        f = MathHelper.clamp_float(f, 0.0f, 1.0f);
        f = -MathHelper.cos(f * 3.1415927f) * 0.5f + 0.5f;
        return f;
    }
    
    private void renderRightArm(final RenderPlayer renderPlayerIn) {
        GlStateManager.pushMatrix();
        GlStateManager.rotate(54.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(64.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.rotate(-62.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.translate(0.25f, -0.85f, 0.75f);
        renderPlayerIn.renderRightArm(this.mc.thePlayer);
        GlStateManager.popMatrix();
    }
    
    private void renderLeftArm(final RenderPlayer renderPlayerIn) {
        GlStateManager.pushMatrix();
        GlStateManager.rotate(92.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(45.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.rotate(41.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.translate(-0.3f, -1.1f, 0.45f);
        renderPlayerIn.renderLeftArm(this.mc.thePlayer);
        GlStateManager.popMatrix();
    }
    
    private void renderPlayerArms(final AbstractClientPlayer clientPlayer) {
        this.mc.getTextureManager().bindTexture(clientPlayer.getLocationSkin());
        final Render<AbstractClientPlayer> render = this.renderManager.getEntityRenderObject(this.mc.thePlayer);
        final RenderPlayer renderplayer = (RenderPlayer)render;
        if (!clientPlayer.isInvisible()) {
            GlStateManager.disableCull();
            this.renderRightArm(renderplayer);
            this.renderLeftArm(renderplayer);
            GlStateManager.enableCull();
        }
    }
    
    private void renderItemMap(final AbstractClientPlayer clientPlayer, final float pitch, final float equipmentProgress, final float swingProgress) {
        final float f = -0.4f * MathHelper.sin(MathHelper.sqrt_float(swingProgress) * 3.1415927f);
        final float f2 = 0.2f * MathHelper.sin(MathHelper.sqrt_float(swingProgress) * 3.1415927f * 2.0f);
        final float f3 = -0.2f * MathHelper.sin(swingProgress * 3.1415927f);
        GlStateManager.translate(f, f2, f3);
        final float f4 = this.getMapAngleFromPitch(pitch);
        GlStateManager.translate(0.0f, 0.04f, -0.72f);
        GlStateManager.translate(0.0f, equipmentProgress * -1.2f, 0.0f);
        GlStateManager.translate(0.0f, f4 * -0.5f, 0.0f);
        GlStateManager.rotate(90.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(f4 * -85.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.rotate(0.0f, 1.0f, 0.0f, 0.0f);
        this.renderPlayerArms(clientPlayer);
        final float f5 = MathHelper.sin(swingProgress * swingProgress * 3.1415927f);
        final float f6 = MathHelper.sin(MathHelper.sqrt_float(swingProgress) * 3.1415927f);
        GlStateManager.rotate(f5 * -20.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(f6 * -20.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.rotate(f6 * -80.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.scale(0.38f, 0.38f, 0.38f);
        GlStateManager.rotate(90.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.rotate(0.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.translate(-1.0f, -1.0f, 0.0f);
        GlStateManager.scale(0.015625f, 0.015625f, 0.015625f);
        this.mc.getTextureManager().bindTexture(ItemRenderer.RES_MAP_BACKGROUND);
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GL11.glNormal3f(0.0f, 0.0f, -1.0f);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(-7.0, 135.0, 0.0).tex(0.0, 1.0).endVertex();
        worldrenderer.pos(135.0, 135.0, 0.0).tex(1.0, 1.0).endVertex();
        worldrenderer.pos(135.0, -7.0, 0.0).tex(1.0, 0.0).endVertex();
        worldrenderer.pos(-7.0, -7.0, 0.0).tex(0.0, 0.0).endVertex();
        tessellator.draw();
        final MapData mapdata = Items.filled_map.getMapData(this.itemToRender, this.mc.theWorld);
        if (mapdata != null) {
            this.mc.entityRenderer.getMapItemRenderer().renderMap(mapdata, false);
        }
    }
    
    private void renderPlayerArm(final AbstractClientPlayer clientPlayer, final float equipProgress, final float swingProgress) {
        final float f = -0.3f * MathHelper.sin(MathHelper.sqrt_float(swingProgress) * 3.1415927f);
        final float f2 = 0.4f * MathHelper.sin(MathHelper.sqrt_float(swingProgress) * 3.1415927f * 2.0f);
        final float f3 = -0.4f * MathHelper.sin(swingProgress * 3.1415927f);
        GlStateManager.translate(f, f2, f3);
        GlStateManager.translate(0.64000005f, -0.6f, -0.71999997f);
        GlStateManager.translate(0.0f, equipProgress * -0.6f, 0.0f);
        GlStateManager.rotate(45.0f, 0.0f, 1.0f, 0.0f);
        final float f4 = MathHelper.sin(swingProgress * swingProgress * 3.1415927f);
        final float f5 = MathHelper.sin(MathHelper.sqrt_float(swingProgress) * 3.1415927f);
        GlStateManager.rotate(f5 * 70.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(f4 * -20.0f, 0.0f, 0.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(clientPlayer.getLocationSkin());
        GlStateManager.translate(-1.0f, 3.6f, 3.5f);
        GlStateManager.rotate(120.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.rotate(200.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.rotate(-135.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.scale(1.0f, 1.0f, 1.0f);
        GlStateManager.translate(5.6f, 0.0f, 0.0f);
        final Render<AbstractClientPlayer> render = this.renderManager.getEntityRenderObject(this.mc.thePlayer);
        GlStateManager.disableCull();
        final RenderPlayer renderplayer = (RenderPlayer)render;
        renderplayer.renderRightArm(this.mc.thePlayer);
        GlStateManager.enableCull();
    }
    
    private void doItemUsedTransformations(final float swingProgress) {
        final float f = -0.4f * MathHelper.sin(MathHelper.sqrt_float(swingProgress) * 3.1415927f);
        final float f2 = 0.2f * MathHelper.sin(MathHelper.sqrt_float(swingProgress) * 3.1415927f * 2.0f);
        final float f3 = -0.2f * MathHelper.sin(swingProgress * 3.1415927f);
        GlStateManager.translate(f, f2, f3);
    }
    
    private void performDrinking(final AbstractClientPlayer clientPlayer, final float partialTicks) {
        final float f = clientPlayer.getItemInUseCount() - partialTicks + 1.0f;
        final float f2 = f / this.itemToRender.getMaxItemUseDuration();
        float f3 = MathHelper.abs(MathHelper.cos(f / 4.0f * 3.1415927f) * 0.1f);
        if (f2 >= 0.8f) {
            f3 = 0.0f;
        }
        GlStateManager.translate(0.0f, f3, 0.0f);
        final float f4 = 1.0f - (float)Math.pow(f2, 27.0);
        GlStateManager.translate(f4 * 0.6f, f4 * -0.5f, f4 * 0.0f);
        GlStateManager.rotate(f4 * 90.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(f4 * 10.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.rotate(f4 * 30.0f, 0.0f, 0.0f, 1.0f);
    }
    
    private void transformFirstPersonItem(final float equipProgress, final float swingProgress) {
        final boolean animations = Tenacity.INSTANCE.isEnabled(Animations.class);
        final double x = animations ? (0.56 + Animations.x.getValue() * 0.01) : 0.56;
        final double y = animations ? (0.52 - Animations.y.getValue() * 0.01) : 0.52;
        final double size = animations ? (0.4 + Animations.size.getValue() * 0.01) : 0.4;
        GlStateManager.translate(x, -y, -0.7199999690055847);
        GlStateManager.translate(0.0f, equipProgress * -0.6f, 0.0f);
        GlStateManager.rotate(45.0f, 0.0f, 1.0f, 0.0f);
        final float f = MathHelper.sin(swingProgress * swingProgress * 3.1415927f);
        final float f2 = MathHelper.sin(MathHelper.sqrt_float(swingProgress) * 3.1415927f);
        GlStateManager.rotate(f * -20.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(f2 * -20.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.rotate(f2 * -80.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.scale(size, size, size);
    }
    
    private void doBowTransformations(final float partialTicks, final AbstractClientPlayer clientPlayer) {
        GlStateManager.rotate(-18.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.rotate(-12.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(-8.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.translate(-0.9f, 0.2f, 0.0f);
        final float f = this.itemToRender.getMaxItemUseDuration() - (clientPlayer.getItemInUseCount() - partialTicks + 1.0f);
        float f2 = f / 20.0f;
        f2 = (f2 * f2 + f2 * 2.0f) / 3.0f;
        if (f2 > 1.0f) {
            f2 = 1.0f;
        }
        if (f2 > 0.1f) {
            final float f3 = MathHelper.sin((f - 0.1f) * 1.3f);
            final float f4 = f2 - 0.1f;
            final float f5 = f3 * f4;
            GlStateManager.translate(f5 * 0.0f, f5 * 0.01f, f5 * 0.0f);
        }
        GlStateManager.translate(f2 * 0.0f, f2 * 0.0f, f2 * 0.1f);
        GlStateManager.scale(1.0f, 1.0f, 1.0f + f2 * 0.2f);
    }
    
    private void doBlockTransformations() {
        GlStateManager.translate(-0.5f, 0.2f, 0.0f);
        GlStateManager.rotate(30.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(-80.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.rotate(60.0f, 0.0f, 1.0f, 0.0f);
    }
    
    public void renderItemInFirstPerson(final float partialTicks) {
        if (!Config.isShaders() || !Shaders.isSkipRenderHand()) {
            final float f = 1.0f - (this.prevEquippedProgress + (this.equippedProgress - this.prevEquippedProgress) * partialTicks);
            final AbstractClientPlayer abstractclientplayer = this.mc.thePlayer;
            final float swingProgress = abstractclientplayer.getSwingProgress(partialTicks);
            final float f2 = abstractclientplayer.prevRotationPitch + (abstractclientplayer.rotationPitch - abstractclientplayer.prevRotationPitch) * partialTicks;
            final float f3 = abstractclientplayer.prevRotationYaw + (abstractclientplayer.rotationYaw - abstractclientplayer.prevRotationYaw) * partialTicks;
            this.rotateArroundXAndY(f2, f3);
            this.setLightMapFromPlayer(abstractclientplayer);
            this.rotateWithPlayerRotations((EntityPlayerSP)abstractclientplayer, partialTicks);
            GlStateManager.enableRescaleNormal();
            GlStateManager.pushMatrix();
            if (this.itemToRender != null) {
                if (this.itemToRender.getItem() instanceof ItemMap) {
                    this.renderItemMap(abstractclientplayer, f2, f, swingProgress);
                }
                else if (abstractclientplayer.getItemInUseCount() > 0 || (KillAura.blocking && InventoryUtils.getHeldItem() instanceof ItemSword)) {
                    final EnumAction enumaction = this.itemToRender.getItemUseAction();
                    final float var15 = MathHelper.sin(swingProgress * swingProgress * 3.1415927f);
                    final float var16 = MathHelper.sin(MathHelper.sqrt_float(swingProgress) * 3.1415927f);
                    switch (enumaction) {
                        case NONE: {
                            this.transformFirstPersonItem(f, 0.0f);
                            break;
                        }
                        case EAT:
                        case DRINK: {
                            this.performDrinking(abstractclientplayer, partialTicks);
                            this.transformFirstPersonItem(f, swingProgress);
                            break;
                        }
                        case BLOCK: {
                            if (Tenacity.INSTANCE.isEnabled(Animations.class)) {
                                final String mode = Animations.mode.getMode();
                                switch (mode) {
                                    case "Stella": {
                                        this.transformFirstPersonItem(-0.1f, swingProgress);
                                        GlStateManager.translate(-0.5f, 0.4f, -0.2f);
                                        GlStateManager.rotate(32.0f, 0.0f, 1.0f, 0.0f);
                                        GlStateManager.rotate(-70.0f, 1.0f, 0.0f, 0.0f);
                                        GlStateManager.rotate(40.0f, 0.0f, 1.0f, 0.0f);
                                        break;
                                    }
                                    case "Middle": {
                                        GlStateManager.popMatrix();
                                        GL11.glRotated(25.0, 0.0, 0.2, 0.0);
                                        this.transformFirstPersonItem(0.0f, swingProgress);
                                        GlStateManager.scale(0.9f, 0.9f, 0.9f);
                                        this.doBlockTransformations();
                                        GlStateManager.pushMatrix();
                                        break;
                                    }
                                    case "1.7": {
                                        this.transformFirstPersonItem(f, swingProgress);
                                        GlStateManager.translate(0.0, 0.3, 0.0);
                                        this.doBlockTransformations();
                                        break;
                                    }
                                    case "Smooth": {
                                        this.transformFirstPersonItem(swingProgress / 5.0f, swingProgress);
                                        GlStateManager.translate(0.0, 0.2, 0.0);
                                        GlStateManager.rotate(-var15, 4.0f, -0.8f, -1.0f);
                                        this.doBlockTransformations();
                                        break;
                                    }
                                    case "Exhi": {
                                        this.transformFirstPersonItem(f / 2.0f, 0.0f);
                                        GlStateManager.rotate(-var16 * 40.0f / 2.0f, var16 / 2.0f, -0.0f, 9.0f);
                                        GlStateManager.rotate(-var16 * 30.0f, 1.0f, var16 / 2.0f, -0.0f);
                                        this.doBlockTransformations();
                                        GL11.glTranslatef(-0.05f, this.mc.thePlayer.isSneaking() ? -0.2f : 0.0f, 0.1f);
                                        break;
                                    }
                                    case "Exhi 2": {
                                        this.transformFirstPersonItem(f / 2.0f, swingProgress);
                                        GlStateManager.rotate(var16 * 30.0f, -var16, -0.0f, 9.0f);
                                        GlStateManager.rotate(var16 * 40.0f, 1.0f, -var16, -0.0f);
                                        GlStateManager.translate(0.0f, 0.3f, 0.0f);
                                        this.doBlockTransformations();
                                        GL11.glTranslatef(-0.05f, this.mc.thePlayer.isSneaking() ? -0.2f : 0.0f, 0.1f);
                                        break;
                                    }
                                    case "Exhi 3": {
                                        this.transformFirstPersonItem(f / 2.0f, swingProgress);
                                        this.doBlockTransformations();
                                        GL11.glTranslatef(-0.05f, this.mc.thePlayer.isSneaking() ? -0.2f : 0.0f, 0.3f);
                                        break;
                                    }
                                    case "Exhi 4": {
                                        this.transformFirstPersonItem(f / 2.0f, 0.0f);
                                        GlStateManager.translate(0.0f, 0.3f, -0.0f);
                                        GlStateManager.rotate(-var16 * 30.0f, 1.0f, 0.0f, 2.0f);
                                        GlStateManager.rotate(-var16 * 44.0f, 1.5f, (float)(var16 / 1.2), 0.0f);
                                        this.doBlockTransformations();
                                        GL11.glTranslatef(-0.05f, this.mc.thePlayer.isSneaking() ? -0.2f : 0.0f, 0.3f);
                                        break;
                                    }
                                    case "Exhi 5": {
                                        this.transformFirstPersonItem(f / 2.0f, swingProgress);
                                        GlStateManager.rotate(var16 * 30.0f, -var16, 0.0f, 9.0f);
                                        GlStateManager.rotate(var16 * 40.0f, 1.0f, -var16, -0.0f);
                                        this.doBlockTransformations();
                                        GL11.glTranslatef(-0.05f, this.mc.thePlayer.isSneaking() ? -0.2f : 0.0f, 0.3f);
                                        break;
                                    }
                                    case "Shred": {
                                        this.transformFirstPersonItem(f / 2.0f, swingProgress);
                                        GlStateManager.rotate(var15 * 30.0f / 2.0f, -var15, -0.0f, 9.0f);
                                        GlStateManager.rotate(var15 * 40.0f, 1.0f, -var15 / 2.0f, -0.0f);
                                        this.doBlockTransformations();
                                        GL11.glTranslatef(-0.05f, this.mc.thePlayer.isSneaking() ? -0.2f : 0.0f, 0.1f);
                                        break;
                                    }
                                    case "Sigma": {
                                        this.transformFirstPersonItem(f * 0.5f, 0.0f);
                                        GlStateManager.rotate(-var15 * 55.0f / 2.0f, -8.0f, -0.0f, 9.0f);
                                        GlStateManager.rotate(-var15 * 45.0f, 1.0f, var15 / 2.0f, -0.0f);
                                        this.doBlockTransformations();
                                        GL11.glTranslated(1.2, 0.3, 0.5);
                                        GL11.glTranslatef(-1.0f, this.mc.thePlayer.isSneaking() ? -0.1f : -0.2f, 0.2f);
                                        break;
                                    }
                                    case "Custom": {
                                        final CustomBlockRenderEvent blockRenderEvent = new CustomBlockRenderEvent((equipProg, swingProg) -> this.transformFirstPersonItem(equipProg, swingProgress), this::doBlockTransformations, swingProgress, f);
                                        Tenacity.INSTANCE.getEventProtocol().handleEvent(blockRenderEvent);
                                        break;
                                    }
                                }
                                break;
                            }
                            this.transformFirstPersonItem(f, 0.0f);
                            this.doBlockTransformations();
                            break;
                        }
                        case BOW: {
                            this.transformFirstPersonItem(f, swingProgress);
                            this.doBowTransformations(partialTicks, abstractclientplayer);
                            break;
                        }
                    }
                }
                else {
                    if (!Animations.smallSwing.isEnabled()) {
                        this.doItemUsedTransformations(swingProgress);
                    }
                    this.transformFirstPersonItem(f, swingProgress);
                }
                this.renderItem(abstractclientplayer, this.itemToRender, ItemCameraTransforms.TransformType.FIRST_PERSON);
            }
            else if (!abstractclientplayer.isInvisible()) {
                this.renderPlayerArm(abstractclientplayer, f, swingProgress);
            }
            GlStateManager.popMatrix();
            GlStateManager.disableRescaleNormal();
            RenderHelper.disableStandardItemLighting();
            this.attemptSwing();
        }
    }
    
    private void attemptSwing() {
        if (this.mc.thePlayer.getItemInUseCount() > 0) {
            final boolean mouseDown = this.mc.gameSettings.keyBindAttack.isKeyDown() && this.mc.gameSettings.keyBindUseItem.isKeyDown();
            if (mouseDown && this.mc.objectMouseOver != null && this.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                this.mc.thePlayer.swingItem();
            }
        }
    }
    
    public void renderOverlays(final float partialTicks) {
        GlStateManager.disableAlpha();
        if (this.mc.thePlayer.isEntityInsideOpaqueBlock()) {
            IBlockState iblockstate = this.mc.theWorld.getBlockState(new BlockPos(this.mc.thePlayer));
            BlockPos blockpos = new BlockPos(this.mc.thePlayer);
            final EntityPlayer entityplayer = this.mc.thePlayer;
            for (int i = 0; i < 8; ++i) {
                final double d0 = entityplayer.posX + ((i >> 0) % 2 - 0.5f) * entityplayer.width * 0.8f;
                final double d2 = entityplayer.posY + ((i >> 1) % 2 - 0.5f) * 0.1f;
                final double d3 = entityplayer.posZ + ((i >> 2) % 2 - 0.5f) * entityplayer.width * 0.8f;
                final BlockPos blockpos2 = new BlockPos(d0, d2 + entityplayer.getEyeHeight(), d3);
                final IBlockState iblockstate2 = this.mc.theWorld.getBlockState(blockpos2);
                if (iblockstate2.getBlock().isVisuallyOpaque()) {
                    iblockstate = iblockstate2;
                    blockpos = blockpos2;
                }
            }
        }
        if (!this.mc.thePlayer.isSpectator()) {
            if (this.mc.thePlayer.isInsideOfMaterial(Material.water)) {
                this.renderWaterOverlayTexture(partialTicks);
            }
            if (this.mc.thePlayer.isBurning()) {
                this.renderFireInFirstPerson(partialTicks);
            }
        }
        GlStateManager.enableAlpha();
    }
    
    private void renderBlockInHand(final float partialTicks, final TextureAtlasSprite atlas) {
        this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        final float f = 0.1f;
        GlStateManager.color(0.1f, 0.1f, 0.1f, 0.5f);
        GlStateManager.pushMatrix();
        final float f2 = -1.0f;
        final float f3 = 1.0f;
        final float f4 = -1.0f;
        final float f5 = 1.0f;
        final float f6 = -0.5f;
        final float f7 = atlas.getMinU();
        final float f8 = atlas.getMaxU();
        final float f9 = atlas.getMinV();
        final float f10 = atlas.getMaxV();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(-1.0, -1.0, -0.5).tex(f8, f10).endVertex();
        worldrenderer.pos(1.0, -1.0, -0.5).tex(f7, f10).endVertex();
        worldrenderer.pos(1.0, 1.0, -0.5).tex(f7, f9).endVertex();
        worldrenderer.pos(-1.0, 1.0, -0.5).tex(f8, f9).endVertex();
        tessellator.draw();
        GlStateManager.popMatrix();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
    }
    
    private void renderWaterOverlayTexture(final float partialTicks) {
        if (!Config.isShaders() || Shaders.isUnderwaterOverlay()) {
            this.mc.getTextureManager().bindTexture(ItemRenderer.RES_UNDERWATER_OVERLAY);
            final Tessellator tessellator = Tessellator.getInstance();
            final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            final float f = this.mc.thePlayer.getBrightness(partialTicks);
            GlStateManager.color(f, f, f, 0.5f);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.pushMatrix();
            final float f2 = 4.0f;
            final float f3 = -1.0f;
            final float f4 = 1.0f;
            final float f5 = -1.0f;
            final float f6 = 1.0f;
            final float f7 = -0.5f;
            final float f8 = -this.mc.thePlayer.rotationYaw / 64.0f;
            final float f9 = this.mc.thePlayer.rotationPitch / 64.0f;
            worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
            worldrenderer.pos(-1.0, -1.0, -0.5).tex(4.0f + f8, 4.0f + f9).endVertex();
            worldrenderer.pos(1.0, -1.0, -0.5).tex(0.0f + f8, 4.0f + f9).endVertex();
            worldrenderer.pos(1.0, 1.0, -0.5).tex(0.0f + f8, 0.0f + f9).endVertex();
            worldrenderer.pos(-1.0, 1.0, -0.5).tex(4.0f + f8, 0.0f + f9).endVertex();
            tessellator.draw();
            GlStateManager.popMatrix();
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.disableBlend();
        }
    }
    
    private void renderFireInFirstPerson(final float partialTicks) {
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 0.9f);
        GlStateManager.depthFunc(519);
        GlStateManager.depthMask(false);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        final float f = 1.0f;
        for (int i = 0; i < 2; ++i) {
            GlStateManager.pushMatrix();
            final TextureAtlasSprite textureatlassprite = this.mc.getTextureMapBlocks().getAtlasSprite("minecraft:blocks/fire_layer_1");
            this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
            final float f2 = textureatlassprite.getMinU();
            final float f3 = textureatlassprite.getMaxU();
            final float f4 = textureatlassprite.getMinV();
            final float f5 = textureatlassprite.getMaxV();
            final float f6 = (0.0f - f) / 2.0f;
            final float f7 = f6 + f;
            final float f8 = 0.0f - f / 2.0f;
            final float f9 = f8 + f;
            final float f10 = -0.5f;
            GlStateManager.translate(-(i * 2 - 1) * 0.24f, -0.3f, 0.0f);
            GlStateManager.rotate((i * 2 - 1) * 10.0f, 0.0f, 1.0f, 0.0f);
            worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
            worldrenderer.setSprite(textureatlassprite);
            worldrenderer.pos(f6, f8, f10).tex(f3, f5).endVertex();
            worldrenderer.pos(f7, f8, f10).tex(f2, f5).endVertex();
            worldrenderer.pos(f7, f9, f10).tex(f2, f4).endVertex();
            worldrenderer.pos(f6, f9, f10).tex(f3, f4).endVertex();
            tessellator.draw();
            GlStateManager.popMatrix();
        }
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.disableBlend();
        GlStateManager.depthMask(true);
        GlStateManager.depthFunc(515);
    }
    
    public void updateEquippedItem() {
        this.prevEquippedProgress = this.equippedProgress;
        final EntityPlayer entityplayer = this.mc.thePlayer;
        final ItemStack itemstack = entityplayer.inventory.getCurrentItem();
        boolean flag = false;
        if (this.itemToRender != null && itemstack != null) {
            if (!this.itemToRender.getIsItemStackEqual(itemstack)) {
                flag = true;
            }
        }
        else {
            flag = (this.itemToRender != null || itemstack != null);
        }
        final float f2 = 0.4f;
        final float f3 = flag ? 0.0f : 1.0f;
        final float f4 = MathHelper.clamp_float(f3 - this.equippedProgress, -f2, f2);
        this.equippedProgress += f4;
        if (this.equippedProgress < 0.1f) {
            this.itemToRender = itemstack;
            this.equippedItemSlot = entityplayer.inventory.currentItem;
            if (Config.isShaders()) {
                Shaders.setItemToRenderMain(itemstack);
            }
        }
    }
    
    public void resetEquippedProgress() {
        this.equippedProgress = 0.0f;
    }
    
    public void resetEquippedProgress2() {
        this.equippedProgress = 0.0f;
    }
    
    static {
        RES_MAP_BACKGROUND = new ResourceLocation("textures/map/map_background.png");
        RES_UNDERWATER_OVERLAY = new ResourceLocation("textures/misc/underwater.png");
    }
}
