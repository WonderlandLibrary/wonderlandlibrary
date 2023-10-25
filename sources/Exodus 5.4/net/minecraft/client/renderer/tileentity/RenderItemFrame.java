/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package net.minecraft.client.renderer.tileentity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureCompass;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSkull;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.MapData;
import org.lwjgl.opengl.GL11;

public class RenderItemFrame
extends Render<EntityItemFrame> {
    private final ModelResourceLocation itemFrameModel;
    private static final ResourceLocation mapBackgroundTextures = new ResourceLocation("textures/map/map_background.png");
    private final ModelResourceLocation mapModel;
    private RenderItem itemRenderer;
    private final Minecraft mc = Minecraft.getMinecraft();

    @Override
    protected ResourceLocation getEntityTexture(EntityItemFrame entityItemFrame) {
        return null;
    }

    private void renderItem(EntityItemFrame entityItemFrame) {
        ItemStack itemStack = entityItemFrame.getDisplayedItem();
        if (itemStack != null) {
            EntityItem entityItem = new EntityItem(entityItemFrame.worldObj, 0.0, 0.0, 0.0, itemStack);
            Item item = entityItem.getEntityItem().getItem();
            entityItem.getEntityItem().stackSize = 1;
            entityItem.hoverStart = 0.0f;
            GlStateManager.pushMatrix();
            GlStateManager.disableLighting();
            int n = entityItemFrame.getRotation();
            if (item == Items.filled_map) {
                n = n % 4 * 2;
            }
            GlStateManager.rotate((float)n * 360.0f / 8.0f, 0.0f, 0.0f, 1.0f);
            if (item == Items.filled_map) {
                this.renderManager.renderEngine.bindTexture(mapBackgroundTextures);
                GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f);
                float f = 0.0078125f;
                GlStateManager.scale(f, f, f);
                GlStateManager.translate(-64.0f, -64.0f, 0.0f);
                MapData mapData = Items.filled_map.getMapData(entityItem.getEntityItem(), entityItemFrame.worldObj);
                GlStateManager.translate(0.0f, 0.0f, -1.0f);
                if (mapData != null) {
                    this.mc.entityRenderer.getMapItemRenderer().renderMap(mapData, true);
                }
            } else {
                TextureAtlasSprite textureAtlasSprite = null;
                if (item == Items.compass) {
                    textureAtlasSprite = this.mc.getTextureMapBlocks().getAtlasSprite(TextureCompass.field_176608_l);
                    this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
                    if (textureAtlasSprite instanceof TextureCompass) {
                        TextureCompass textureCompass = (TextureCompass)textureAtlasSprite;
                        double d = textureCompass.currentAngle;
                        double d2 = textureCompass.angleDelta;
                        textureCompass.currentAngle = 0.0;
                        textureCompass.angleDelta = 0.0;
                        textureCompass.updateCompass(entityItemFrame.worldObj, entityItemFrame.posX, entityItemFrame.posZ, MathHelper.wrapAngleTo180_float(180 + entityItemFrame.facingDirection.getHorizontalIndex() * 90), false, true);
                        textureCompass.currentAngle = d;
                        textureCompass.angleDelta = d2;
                    } else {
                        textureAtlasSprite = null;
                    }
                }
                GlStateManager.scale(0.5f, 0.5f, 0.5f);
                if (!this.itemRenderer.shouldRenderItemIn3D(entityItem.getEntityItem()) || item instanceof ItemSkull) {
                    GlStateManager.rotate(180.0f, 0.0f, 1.0f, 0.0f);
                }
                GlStateManager.pushAttrib();
                RenderHelper.enableStandardItemLighting();
                this.itemRenderer.func_181564_a(entityItem.getEntityItem(), ItemCameraTransforms.TransformType.FIXED);
                RenderHelper.disableStandardItemLighting();
                GlStateManager.popAttrib();
                if (textureAtlasSprite != null && textureAtlasSprite.getFrameCount() > 0) {
                    textureAtlasSprite.updateAnimation();
                }
            }
            GlStateManager.enableLighting();
            GlStateManager.popMatrix();
        }
    }

    public RenderItemFrame(RenderManager renderManager, RenderItem renderItem) {
        super(renderManager);
        this.itemFrameModel = new ModelResourceLocation("item_frame", "normal");
        this.mapModel = new ModelResourceLocation("item_frame", "map");
        this.itemRenderer = renderItem;
    }

    @Override
    public void doRender(EntityItemFrame entityItemFrame, double d, double d2, double d3, float f, float f2) {
        GlStateManager.pushMatrix();
        BlockPos blockPos = entityItemFrame.getHangingPosition();
        double d4 = (double)blockPos.getX() - entityItemFrame.posX + d;
        double d5 = (double)blockPos.getY() - entityItemFrame.posY + d2;
        double d6 = (double)blockPos.getZ() - entityItemFrame.posZ + d3;
        GlStateManager.translate(d4 + 0.5, d5 + 0.5, d6 + 0.5);
        GlStateManager.rotate(180.0f - entityItemFrame.rotationYaw, 0.0f, 1.0f, 0.0f);
        this.renderManager.renderEngine.bindTexture(TextureMap.locationBlocksTexture);
        BlockRendererDispatcher blockRendererDispatcher = this.mc.getBlockRendererDispatcher();
        ModelManager modelManager = blockRendererDispatcher.getBlockModelShapes().getModelManager();
        IBakedModel iBakedModel = entityItemFrame.getDisplayedItem() != null && entityItemFrame.getDisplayedItem().getItem() == Items.filled_map ? modelManager.getModel(this.mapModel) : modelManager.getModel(this.itemFrameModel);
        GlStateManager.pushMatrix();
        GlStateManager.translate(-0.5f, -0.5f, -0.5f);
        blockRendererDispatcher.getBlockModelRenderer().renderModelBrightnessColor(iBakedModel, 1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.popMatrix();
        GlStateManager.translate(0.0f, 0.0f, 0.4375f);
        this.renderItem(entityItemFrame);
        GlStateManager.popMatrix();
        this.renderName(entityItemFrame, d + (double)((float)entityItemFrame.facingDirection.getFrontOffsetX() * 0.3f), d2 - 0.25, d3 + (double)((float)entityItemFrame.facingDirection.getFrontOffsetZ() * 0.3f));
    }

    @Override
    protected void renderName(EntityItemFrame entityItemFrame, double d, double d2, double d3) {
        if (Minecraft.isGuiEnabled() && entityItemFrame.getDisplayedItem() != null && entityItemFrame.getDisplayedItem().hasDisplayName() && this.renderManager.pointedEntity == entityItemFrame) {
            float f;
            float f2 = 1.6f;
            float f3 = 0.016666668f * f2;
            double d4 = entityItemFrame.getDistanceSqToEntity(this.renderManager.livingPlayer);
            float f4 = f = entityItemFrame.isSneaking() ? 32.0f : 64.0f;
            if (d4 < (double)(f * f)) {
                String string = entityItemFrame.getDisplayedItem().getDisplayName();
                if (entityItemFrame.isSneaking()) {
                    FontRenderer fontRenderer = this.getFontRendererFromRenderManager();
                    GlStateManager.pushMatrix();
                    GlStateManager.translate((float)d + 0.0f, (float)d2 + entityItemFrame.height + 0.5f, (float)d3);
                    GL11.glNormal3f((float)0.0f, (float)1.0f, (float)0.0f);
                    GlStateManager.rotate(-RenderManager.playerViewY, 0.0f, 1.0f, 0.0f);
                    GlStateManager.rotate(RenderManager.playerViewX, 1.0f, 0.0f, 0.0f);
                    GlStateManager.scale(-f3, -f3, f3);
                    GlStateManager.disableLighting();
                    GlStateManager.translate(0.0f, 0.25f / f3, 0.0f);
                    GlStateManager.depthMask(false);
                    GlStateManager.enableBlend();
                    GlStateManager.blendFunc(770, 771);
                    Tessellator tessellator = Tessellator.getInstance();
                    WorldRenderer worldRenderer = tessellator.getWorldRenderer();
                    int n = fontRenderer.getStringWidth(string) / 2;
                    GlStateManager.disableTexture2D();
                    worldRenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
                    worldRenderer.pos(-n - 1, -1.0, 0.0).color(0.0f, 0.0f, 0.0f, 0.25f).endVertex();
                    worldRenderer.pos(-n - 1, 8.0, 0.0).color(0.0f, 0.0f, 0.0f, 0.25f).endVertex();
                    worldRenderer.pos(n + 1, 8.0, 0.0).color(0.0f, 0.0f, 0.0f, 0.25f).endVertex();
                    worldRenderer.pos(n + 1, -1.0, 0.0).color(0.0f, 0.0f, 0.0f, 0.25f).endVertex();
                    tessellator.draw();
                    GlStateManager.enableTexture2D();
                    GlStateManager.depthMask(true);
                    fontRenderer.drawString(string, -fontRenderer.getStringWidth(string) / 2, 0.0, 0x20FFFFFF);
                    GlStateManager.enableLighting();
                    GlStateManager.disableBlend();
                    GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                    GlStateManager.popMatrix();
                } else {
                    this.renderLivingLabel(entityItemFrame, string, d, d2, d3, 64);
                }
            }
        }
    }
}

