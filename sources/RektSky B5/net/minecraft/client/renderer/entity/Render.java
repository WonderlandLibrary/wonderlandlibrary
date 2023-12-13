/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.renderer.entity;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;
import tk.rektsky.module.ModulesManager;
import tk.rektsky.module.impl.render.NameTags;

public abstract class Render<T extends Entity> {
    private static final ResourceLocation shadowTextures = new ResourceLocation("textures/misc/shadow.png");
    protected final RenderManager renderManager;
    protected float shadowSize;
    protected float shadowOpaque = 1.0f;

    protected Render(RenderManager renderManager) {
        this.renderManager = renderManager;
    }

    public boolean shouldRender(T livingEntity, ICamera camera, double camX, double camY, double camZ) {
        AxisAlignedBB axisalignedbb = ((Entity)livingEntity).getEntityBoundingBox();
        if (axisalignedbb.func_181656_b() || axisalignedbb.getAverageEdgeLength() == 0.0) {
            axisalignedbb = new AxisAlignedBB(((Entity)livingEntity).posX - 2.0, ((Entity)livingEntity).posY - 2.0, ((Entity)livingEntity).posZ - 2.0, ((Entity)livingEntity).posX + 2.0, ((Entity)livingEntity).posY + 2.0, ((Entity)livingEntity).posZ + 2.0);
        }
        return ((Entity)livingEntity).isInRangeToRender3d(camX, camY, camZ) && (((Entity)livingEntity).ignoreFrustumCheck || camera.isBoundingBoxInFrustum(axisalignedbb));
    }

    public void doRender(T entity, double x2, double y2, double z2, float entityYaw, float partialTicks) {
        this.renderName(entity, x2, y2, z2);
    }

    protected void renderName(T entity, double x2, double y2, double z2) {
        if (this.canRenderName(entity)) {
            this.renderLivingLabel(entity, ((Entity)entity).getDisplayName().getFormattedText(), x2, y2, z2, 64);
        }
    }

    protected boolean canRenderName(T entity) {
        return ((Entity)entity).getAlwaysRenderNameTagForRender() && ((Entity)entity).hasCustomName();
    }

    protected void renderOffsetLivingLabel(T entityIn, double x2, double y2, double z2, String str, float p_177069_9_, double p_177069_10_) {
        this.renderLivingLabel(entityIn, str, x2, y2, z2, 64);
    }

    protected abstract ResourceLocation getEntityTexture(T var1);

    protected boolean bindEntityTexture(T entity) {
        ResourceLocation resourcelocation = this.getEntityTexture(entity);
        if (resourcelocation == null) {
            return false;
        }
        this.bindTexture(resourcelocation);
        return true;
    }

    public void bindTexture(ResourceLocation location) {
        this.renderManager.renderEngine.bindTexture(location);
    }

    private void renderEntityOnFire(Entity entity, double x2, double y2, double z2, float partialTicks) {
        GlStateManager.disableLighting();
        TextureMap texturemap = Minecraft.getMinecraft().getTextureMapBlocks();
        TextureAtlasSprite textureatlassprite = texturemap.getAtlasSprite("minecraft:blocks/fire_layer_0");
        TextureAtlasSprite textureatlassprite1 = texturemap.getAtlasSprite("minecraft:blocks/fire_layer_1");
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)x2, (float)y2, (float)z2);
        float f2 = entity.width * 1.4f;
        GlStateManager.scale(f2, f2, f2);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        float f1 = 0.5f;
        float f22 = 0.0f;
        float f3 = entity.height / f2;
        float f4 = (float)(entity.posY - entity.getEntityBoundingBox().minY);
        GlStateManager.rotate(-this.renderManager.playerViewY, 0.0f, 1.0f, 0.0f);
        GlStateManager.translate(0.0f, 0.0f, -0.3f + (float)((int)f3) * 0.02f);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        float f5 = 0.0f;
        int i2 = 0;
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        while (f3 > 0.0f) {
            TextureAtlasSprite textureatlassprite2 = i2 % 2 == 0 ? textureatlassprite : textureatlassprite1;
            this.bindTexture(TextureMap.locationBlocksTexture);
            float f6 = textureatlassprite2.getMinU();
            float f7 = textureatlassprite2.getMinV();
            float f8 = textureatlassprite2.getMaxU();
            float f9 = textureatlassprite2.getMaxV();
            if (i2 / 2 % 2 == 0) {
                float f10 = f8;
                f8 = f6;
                f6 = f10;
            }
            worldrenderer.pos(f1 - f22, 0.0f - f4, f5).tex(f8, f9).endVertex();
            worldrenderer.pos(-f1 - f22, 0.0f - f4, f5).tex(f6, f9).endVertex();
            worldrenderer.pos(-f1 - f22, 1.4f - f4, f5).tex(f6, f7).endVertex();
            worldrenderer.pos(f1 - f22, 1.4f - f4, f5).tex(f8, f7).endVertex();
            f3 -= 0.45f;
            f4 -= 0.45f;
            f1 *= 0.9f;
            f5 += 0.03f;
            ++i2;
        }
        tessellator.draw();
        GlStateManager.popMatrix();
        GlStateManager.enableLighting();
    }

    private void renderShadow(Entity entityIn, double x2, double y2, double z2, float shadowAlpha, float partialTicks) {
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        this.renderManager.renderEngine.bindTexture(shadowTextures);
        World world = this.getWorldFromRenderManager();
        GlStateManager.depthMask(false);
        float f2 = this.shadowSize;
        if (entityIn instanceof EntityLiving) {
            EntityLiving entityliving = (EntityLiving)entityIn;
            f2 *= entityliving.getRenderSizeModifier();
            if (entityliving.isChild()) {
                f2 *= 0.5f;
            }
        }
        double d5 = entityIn.lastTickPosX + (entityIn.posX - entityIn.lastTickPosX) * (double)partialTicks;
        double d0 = entityIn.lastTickPosY + (entityIn.posY - entityIn.lastTickPosY) * (double)partialTicks;
        double d1 = entityIn.lastTickPosZ + (entityIn.posZ - entityIn.lastTickPosZ) * (double)partialTicks;
        int i2 = MathHelper.floor_double(d5 - (double)f2);
        int j2 = MathHelper.floor_double(d5 + (double)f2);
        int k2 = MathHelper.floor_double(d0 - (double)f2);
        int l2 = MathHelper.floor_double(d0);
        int i1 = MathHelper.floor_double(d1 - (double)f2);
        int j1 = MathHelper.floor_double(d1 + (double)f2);
        double d2 = x2 - d5;
        double d3 = y2 - d0;
        double d4 = z2 - d1;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        for (BlockPos blockPos : BlockPos.getAllInBoxMutable(new BlockPos(i2, k2, i1), new BlockPos(j2, l2, j1))) {
            Block block = world.getBlockState(blockPos.down()).getBlock();
            if (block.getRenderType() == -1 || world.getLightFromNeighbors(blockPos) <= 3) continue;
            this.func_180549_a(block, x2, y2, z2, blockPos, shadowAlpha, f2, d2, d3, d4);
        }
        tessellator.draw();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.disableBlend();
        GlStateManager.depthMask(true);
    }

    private World getWorldFromRenderManager() {
        return this.renderManager.worldObj;
    }

    private void func_180549_a(Block blockIn, double p_180549_2_, double p_180549_4_, double p_180549_6_, BlockPos pos, float p_180549_9_, float p_180549_10_, double p_180549_11_, double p_180549_13_, double p_180549_15_) {
        if (blockIn.isFullCube()) {
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            double d0 = ((double)p_180549_9_ - (p_180549_4_ - ((double)pos.getY() + p_180549_13_)) / 2.0) * 0.5 * (double)this.getWorldFromRenderManager().getLightBrightness(pos);
            if (d0 >= 0.0) {
                if (d0 > 1.0) {
                    d0 = 1.0;
                }
                double d1 = (double)pos.getX() + blockIn.getBlockBoundsMinX() + p_180549_11_;
                double d2 = (double)pos.getX() + blockIn.getBlockBoundsMaxX() + p_180549_11_;
                double d3 = (double)pos.getY() + blockIn.getBlockBoundsMinY() + p_180549_13_ + 0.015625;
                double d4 = (double)pos.getZ() + blockIn.getBlockBoundsMinZ() + p_180549_15_;
                double d5 = (double)pos.getZ() + blockIn.getBlockBoundsMaxZ() + p_180549_15_;
                float f2 = (float)((p_180549_2_ - d1) / 2.0 / (double)p_180549_10_ + 0.5);
                float f1 = (float)((p_180549_2_ - d2) / 2.0 / (double)p_180549_10_ + 0.5);
                float f22 = (float)((p_180549_6_ - d4) / 2.0 / (double)p_180549_10_ + 0.5);
                float f3 = (float)((p_180549_6_ - d5) / 2.0 / (double)p_180549_10_ + 0.5);
                worldrenderer.pos(d1, d3, d4).tex(f2, f22).color(1.0f, 1.0f, 1.0f, (float)d0).endVertex();
                worldrenderer.pos(d1, d3, d5).tex(f2, f3).color(1.0f, 1.0f, 1.0f, (float)d0).endVertex();
                worldrenderer.pos(d2, d3, d5).tex(f1, f3).color(1.0f, 1.0f, 1.0f, (float)d0).endVertex();
                worldrenderer.pos(d2, d3, d4).tex(f1, f22).color(1.0f, 1.0f, 1.0f, (float)d0).endVertex();
            }
        }
    }

    public static void renderOffsetAABB(AxisAlignedBB boundingBox, double x2, double y2, double z2) {
        GlStateManager.disableTexture2D();
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        worldrenderer.setTranslation(x2, y2, z2);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_NORMAL);
        worldrenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).normal(0.0f, 0.0f, -1.0f).endVertex();
        worldrenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).normal(0.0f, 0.0f, -1.0f).endVertex();
        worldrenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).normal(0.0f, 0.0f, -1.0f).endVertex();
        worldrenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).normal(0.0f, 0.0f, -1.0f).endVertex();
        worldrenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).normal(0.0f, 0.0f, 1.0f).endVertex();
        worldrenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).normal(0.0f, 0.0f, 1.0f).endVertex();
        worldrenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).normal(0.0f, 0.0f, 1.0f).endVertex();
        worldrenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).normal(0.0f, 0.0f, 1.0f).endVertex();
        worldrenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).normal(0.0f, -1.0f, 0.0f).endVertex();
        worldrenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).normal(0.0f, -1.0f, 0.0f).endVertex();
        worldrenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).normal(0.0f, -1.0f, 0.0f).endVertex();
        worldrenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).normal(0.0f, -1.0f, 0.0f).endVertex();
        worldrenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).normal(0.0f, 1.0f, 0.0f).endVertex();
        worldrenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).normal(0.0f, 1.0f, 0.0f).endVertex();
        worldrenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).normal(0.0f, 1.0f, 0.0f).endVertex();
        worldrenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).normal(0.0f, 1.0f, 0.0f).endVertex();
        worldrenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).normal(-1.0f, 0.0f, 0.0f).endVertex();
        worldrenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).normal(-1.0f, 0.0f, 0.0f).endVertex();
        worldrenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).normal(-1.0f, 0.0f, 0.0f).endVertex();
        worldrenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).normal(-1.0f, 0.0f, 0.0f).endVertex();
        worldrenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).normal(1.0f, 0.0f, 0.0f).endVertex();
        worldrenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).normal(1.0f, 0.0f, 0.0f).endVertex();
        worldrenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).normal(1.0f, 0.0f, 0.0f).endVertex();
        worldrenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).normal(1.0f, 0.0f, 0.0f).endVertex();
        tessellator.draw();
        worldrenderer.setTranslation(0.0, 0.0, 0.0);
        GlStateManager.enableTexture2D();
    }

    public void doRenderShadowAndFire(Entity entityIn, double x2, double y2, double z2, float yaw, float partialTicks) {
        if (this.renderManager.options != null) {
            double d0;
            float f2;
            if (this.renderManager.options.field_181151_V && this.shadowSize > 0.0f && !entityIn.isInvisible() && this.renderManager.isRenderShadow() && (f2 = (float)((1.0 - (d0 = this.renderManager.getDistanceToCamera(entityIn.posX, entityIn.posY, entityIn.posZ)) / 256.0) * (double)this.shadowOpaque)) > 0.0f) {
                this.renderShadow(entityIn, x2, y2, z2, f2, partialTicks);
            }
            if (!(!entityIn.canRenderOnFire() || entityIn instanceof EntityPlayer && ((EntityPlayer)entityIn).isSpectator())) {
                this.renderEntityOnFire(entityIn, x2, y2, z2, partialTicks);
            }
        }
    }

    public FontRenderer getFontRendererFromRenderManager() {
        return this.renderManager.getFontRenderer();
    }

    protected void renderLivingLabel(T entityIn, String str, double x2, double y2, double z2, int maxDistance) {
        if (ModulesManager.getModuleByClass(NameTags.class).isToggled() && entityIn instanceof EntityPlayer) {
            return;
        }
        double d0 = ((Entity)entityIn).getDistanceSqToEntity(this.renderManager.livingPlayer);
        if (d0 <= (double)(maxDistance * maxDistance)) {
            FontRenderer fontrenderer = this.getFontRendererFromRenderManager();
            float f2 = 1.6f;
            float f1 = 0.016666668f * f2;
            GlStateManager.pushMatrix();
            GlStateManager.translate((float)x2 + 0.0f, (float)y2 + ((Entity)entityIn).height + 0.5f, (float)z2);
            GL11.glNormal3f(0.0f, 1.0f, 0.0f);
            GlStateManager.rotate(-this.renderManager.playerViewY, 0.0f, 1.0f, 0.0f);
            GlStateManager.rotate(this.renderManager.playerViewX, 1.0f, 0.0f, 0.0f);
            GlStateManager.scale(-f1, -f1, f1);
            GlStateManager.disableLighting();
            GlStateManager.depthMask(false);
            GlStateManager.disableDepth();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            int i2 = 0;
            if (str.equals("deadmau5")) {
                i2 = -10;
            }
            int j2 = fontrenderer.getStringWidth(str) / 2;
            GlStateManager.disableTexture2D();
            worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
            worldrenderer.pos(-j2 - 1, -1 + i2, 0.0).color(0.0f, 0.0f, 0.0f, 0.25f).endVertex();
            worldrenderer.pos(-j2 - 1, 8 + i2, 0.0).color(0.0f, 0.0f, 0.0f, 0.25f).endVertex();
            worldrenderer.pos(j2 + 1, 8 + i2, 0.0).color(0.0f, 0.0f, 0.0f, 0.25f).endVertex();
            worldrenderer.pos(j2 + 1, -1 + i2, 0.0).color(0.0f, 0.0f, 0.0f, 0.25f).endVertex();
            tessellator.draw();
            GlStateManager.enableTexture2D();
            fontrenderer.drawString(str, -fontrenderer.getStringWidth(str) / 2, i2, 0x20FFFFFF);
            GlStateManager.enableDepth();
            GlStateManager.depthMask(true);
            fontrenderer.drawString(str, -fontrenderer.getStringWidth(str) / 2, i2, -1);
            GlStateManager.enableLighting();
            GlStateManager.disableBlend();
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.popMatrix();
        }
    }

    public RenderManager getRenderManager() {
        return this.renderManager;
    }
}

