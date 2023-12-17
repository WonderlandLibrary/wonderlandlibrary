// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.client.renderer.EntityRenderer;
import ru.tuskevich.modules.impl.RENDER.NameTags;
import ru.tuskevich.modules.impl.RENDER.ESP;
import net.minecraft.entity.player.EntityPlayer;
import ru.tuskevich.modules.Module;
import ru.tuskevich.Minced;
import ru.tuskevich.modules.impl.MISC.Optimization;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.state.IBlockState;
import java.util.Iterator;
import net.minecraft.world.World;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.entity.EntityLiving;
import net.optifine.shaders.Shaders;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.src.Config;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import javax.annotation.Nullable;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.util.ResourceLocation;
import net.optifine.entity.model.IEntityRenderer;
import net.minecraft.entity.Entity;

public abstract class Render<T extends Entity> implements IEntityRenderer
{
    private static final ResourceLocation SHADOW_TEXTURES;
    protected final RenderManager renderManager;
    public float shadowSize;
    protected float shadowOpaque;
    protected boolean renderOutlines;
    private Class entityClass;
    private ResourceLocation locationTextureCustom;
    
    protected Render(final RenderManager renderManager) {
        this.shadowOpaque = 1.0f;
        this.entityClass = null;
        this.locationTextureCustom = null;
        this.renderManager = renderManager;
    }
    
    public void setRenderOutlines(final boolean renderOutlinesIn) {
        this.renderOutlines = renderOutlinesIn;
    }
    
    public boolean shouldRender(final T livingEntity, final ICamera camera, final double camX, final double camY, final double camZ) {
        AxisAlignedBB axisalignedbb = livingEntity.getRenderBoundingBox().grow(0.5);
        if (axisalignedbb.hasNaN() || axisalignedbb.getAverageEdgeLength() == 0.0) {
            axisalignedbb = new AxisAlignedBB(livingEntity.posX - 2.0, livingEntity.posY - 2.0, livingEntity.posZ - 2.0, livingEntity.posX + 2.0, livingEntity.posY + 2.0, livingEntity.posZ + 2.0);
        }
        return livingEntity.isInRangeToRender3d(camX, camY, camZ) && (livingEntity.ignoreFrustumCheck || camera.isBoundingBoxInFrustum(axisalignedbb));
    }
    
    public void doRender(final T entity, final double x, final double y, final double z, final float entityYaw, final float partialTicks) {
        if (!this.renderOutlines) {
            this.renderName(entity, x, y, z);
        }
    }
    
    protected int getTeamColor(final T entityIn) {
        int i = 16777215;
        final ScorePlayerTeam scoreplayerteam = (ScorePlayerTeam)entityIn.getTeam();
        if (scoreplayerteam != null) {
            final String s = FontRenderer.getFormatFromString(scoreplayerteam.getPrefix());
            if (s.length() >= 2) {
                i = this.getFontRendererFromRenderManager().getColorCode(s.charAt(1));
            }
        }
        return i;
    }
    
    protected void renderName(final T entity, final double x, final double y, final double z) {
        if (this.canRenderName(entity)) {
            this.renderLivingLabel(entity, entity.getDisplayName().getFormattedText(), x, y, z, 64);
        }
    }
    
    protected boolean canRenderName(final T entity) {
        return entity.getAlwaysRenderNameTagForRender() && entity.hasCustomName();
    }
    
    protected void renderEntityName(final T entityIn, final double x, final double y, final double z, final String name, final double distanceSq) {
        this.renderLivingLabel(entityIn, name, x, y, z, 64);
    }
    
    @Nullable
    protected abstract ResourceLocation getEntityTexture(final T p0);
    
    protected boolean bindEntityTexture(final T entity) {
        ResourceLocation resourcelocation = this.getEntityTexture(entity);
        if (this.locationTextureCustom != null) {
            resourcelocation = this.locationTextureCustom;
        }
        if (resourcelocation == null) {
            return false;
        }
        this.bindTexture(resourcelocation);
        return true;
    }
    
    public void bindTexture(final ResourceLocation location) {
        this.renderManager.renderEngine.bindTexture(location);
    }
    
    private void renderEntityOnFire(final Entity entity, final double x, final double y, final double z, final float partialTicks) {
        GlStateManager.disableLighting();
        final TextureMap texturemap = Minecraft.getMinecraft().getTextureMapBlocks();
        final TextureAtlasSprite textureatlassprite = texturemap.getAtlasSprite("minecraft:blocks/fire_layer_0");
        final TextureAtlasSprite textureatlassprite2 = texturemap.getAtlasSprite("minecraft:blocks/fire_layer_1");
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)x, (float)y, (float)z);
        final float f = entity.width * 1.4f;
        GlStateManager.scale(f, f, f);
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();
        float f2 = 0.5f;
        final float f3 = 0.0f;
        float f4 = entity.height / f;
        float f5 = (float)(entity.posY - entity.getEntityBoundingBox().minY);
        GlStateManager.rotate(-this.renderManager.playerViewY, 0.0f, 1.0f, 0.0f);
        GlStateManager.translate(0.0f, 0.0f, -0.3f + (int)f4 * 0.02f);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        float f6 = 0.0f;
        int i = 0;
        final boolean flag = Config.isMultiTexture();
        if (flag) {
            bufferbuilder.setBlockLayer(BlockRenderLayer.SOLID);
        }
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        while (f4 > 0.0f) {
            final TextureAtlasSprite textureatlassprite3 = (i % 2 == 0) ? textureatlassprite : textureatlassprite2;
            bufferbuilder.setSprite(textureatlassprite3);
            this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            float f7 = textureatlassprite3.getMinU();
            final float f8 = textureatlassprite3.getMinV();
            float f9 = textureatlassprite3.getMaxU();
            final float f10 = textureatlassprite3.getMaxV();
            if (i / 2 % 2 == 0) {
                final float f11 = f9;
                f9 = f7;
                f7 = f11;
            }
            bufferbuilder.pos(f2 - 0.0f, 0.0f - f5, f6).tex(f9, f10).endVertex();
            bufferbuilder.pos(-f2 - 0.0f, 0.0f - f5, f6).tex(f7, f10).endVertex();
            bufferbuilder.pos(-f2 - 0.0f, 1.4f - f5, f6).tex(f7, f8).endVertex();
            bufferbuilder.pos(f2 - 0.0f, 1.4f - f5, f6).tex(f9, f8).endVertex();
            f4 -= 0.45f;
            f5 -= 0.45f;
            f2 *= 0.9f;
            f6 += 0.03f;
            ++i;
        }
        tessellator.draw();
        if (flag) {
            bufferbuilder.setBlockLayer(null);
            GlStateManager.bindCurrentTexture();
        }
        GlStateManager.popMatrix();
        GlStateManager.enableLighting();
    }
    
    private void renderShadow(final Entity entityIn, final double x, final double y, final double z, final float shadowAlpha, final float partialTicks) {
        if (!Config.isShaders() || !Shaders.shouldSkipDefaultShadow) {
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            this.renderManager.renderEngine.bindTexture(Render.SHADOW_TEXTURES);
            final World world = this.getWorldFromRenderManager();
            GlStateManager.depthMask(false);
            float f = this.shadowSize;
            if (entityIn instanceof EntityLiving) {
                final EntityLiving entityliving = (EntityLiving)entityIn;
                f *= entityliving.getRenderSizeModifier();
                if (entityliving.isChild()) {
                    f *= 0.5f;
                }
            }
            final double d5 = entityIn.lastTickPosX + (entityIn.posX - entityIn.lastTickPosX) * partialTicks;
            final double d6 = entityIn.lastTickPosY + (entityIn.posY - entityIn.lastTickPosY) * partialTicks;
            final double d7 = entityIn.lastTickPosZ + (entityIn.posZ - entityIn.lastTickPosZ) * partialTicks;
            final int i = MathHelper.floor(d5 - f);
            final int j = MathHelper.floor(d5 + f);
            final int k = MathHelper.floor(d6 - f);
            final int l = MathHelper.floor(d6);
            final int i2 = MathHelper.floor(d7 - f);
            final int j2 = MathHelper.floor(d7 + f);
            final double d8 = x - d5;
            final double d9 = y - d6;
            final double d10 = z - d7;
            final Tessellator tessellator = Tessellator.getInstance();
            final BufferBuilder bufferbuilder = tessellator.getBuffer();
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            for (final BlockPos blockpos : BlockPos.getAllInBoxMutable(new BlockPos(i, k, i2), new BlockPos(j, l, j2))) {
                final IBlockState iblockstate = world.getBlockState(blockpos.down());
                if (iblockstate.getRenderType() != EnumBlockRenderType.INVISIBLE && world.getLightFromNeighbors(blockpos) > 3) {
                    this.renderShadowSingle(iblockstate, x, y, z, blockpos, shadowAlpha, f, d8, d9, d10);
                }
            }
            tessellator.draw();
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.disableBlend();
            GlStateManager.depthMask(true);
        }
    }
    
    private World getWorldFromRenderManager() {
        return this.renderManager.world;
    }
    
    private void renderShadowSingle(final IBlockState state, final double p_188299_2_, final double p_188299_4_, final double p_188299_6_, final BlockPos p_188299_8_, final float p_188299_9_, final float p_188299_10_, final double p_188299_11_, final double p_188299_13_, final double p_188299_15_) {
        if (state.isFullCube()) {
            final Tessellator tessellator = Tessellator.getInstance();
            final BufferBuilder bufferbuilder = tessellator.getBuffer();
            double d0 = (p_188299_9_ - (p_188299_4_ - (p_188299_8_.getY() + p_188299_13_)) / 2.0) * 0.5 * this.getWorldFromRenderManager().getLightBrightness(p_188299_8_);
            if (d0 >= 0.0) {
                if (d0 > 1.0) {
                    d0 = 1.0;
                }
                final AxisAlignedBB axisalignedbb = state.getBoundingBox(this.getWorldFromRenderManager(), p_188299_8_);
                final double d2 = p_188299_8_.getX() + axisalignedbb.minX + p_188299_11_;
                final double d3 = p_188299_8_.getX() + axisalignedbb.maxX + p_188299_11_;
                final double d4 = p_188299_8_.getY() + axisalignedbb.minY + p_188299_13_ + 0.015625;
                final double d5 = p_188299_8_.getZ() + axisalignedbb.minZ + p_188299_15_;
                final double d6 = p_188299_8_.getZ() + axisalignedbb.maxZ + p_188299_15_;
                final float f = (float)((p_188299_2_ - d2) / 2.0 / p_188299_10_ + 0.5);
                final float f2 = (float)((p_188299_2_ - d3) / 2.0 / p_188299_10_ + 0.5);
                final float f3 = (float)((p_188299_6_ - d5) / 2.0 / p_188299_10_ + 0.5);
                final float f4 = (float)((p_188299_6_ - d6) / 2.0 / p_188299_10_ + 0.5);
                bufferbuilder.pos(d2, d4, d5).tex(f, f3).color(1.0f, 1.0f, 1.0f, (float)d0).endVertex();
                bufferbuilder.pos(d2, d4, d6).tex(f, f4).color(1.0f, 1.0f, 1.0f, (float)d0).endVertex();
                bufferbuilder.pos(d3, d4, d6).tex(f2, f4).color(1.0f, 1.0f, 1.0f, (float)d0).endVertex();
                bufferbuilder.pos(d3, d4, d5).tex(f2, f3).color(1.0f, 1.0f, 1.0f, (float)d0).endVertex();
            }
        }
    }
    
    public static void renderOffsetAABB(final AxisAlignedBB boundingBox, final double x, final double y, final double z) {
        GlStateManager.disableTexture2D();
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        bufferbuilder.setTranslation(x, y, z);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_NORMAL);
        bufferbuilder.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).normal(0.0f, 0.0f, -1.0f).endVertex();
        bufferbuilder.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).normal(0.0f, 0.0f, -1.0f).endVertex();
        bufferbuilder.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).normal(0.0f, 0.0f, -1.0f).endVertex();
        bufferbuilder.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).normal(0.0f, 0.0f, -1.0f).endVertex();
        bufferbuilder.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).normal(0.0f, 0.0f, 1.0f).endVertex();
        bufferbuilder.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).normal(0.0f, 0.0f, 1.0f).endVertex();
        bufferbuilder.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).normal(0.0f, 0.0f, 1.0f).endVertex();
        bufferbuilder.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).normal(0.0f, 0.0f, 1.0f).endVertex();
        bufferbuilder.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).normal(0.0f, -1.0f, 0.0f).endVertex();
        bufferbuilder.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).normal(0.0f, -1.0f, 0.0f).endVertex();
        bufferbuilder.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).normal(0.0f, -1.0f, 0.0f).endVertex();
        bufferbuilder.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).normal(0.0f, -1.0f, 0.0f).endVertex();
        bufferbuilder.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).normal(0.0f, 1.0f, 0.0f).endVertex();
        bufferbuilder.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).normal(0.0f, 1.0f, 0.0f).endVertex();
        bufferbuilder.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).normal(0.0f, 1.0f, 0.0f).endVertex();
        bufferbuilder.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).normal(0.0f, 1.0f, 0.0f).endVertex();
        bufferbuilder.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).normal(-1.0f, 0.0f, 0.0f).endVertex();
        bufferbuilder.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).normal(-1.0f, 0.0f, 0.0f).endVertex();
        bufferbuilder.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).normal(-1.0f, 0.0f, 0.0f).endVertex();
        bufferbuilder.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).normal(-1.0f, 0.0f, 0.0f).endVertex();
        bufferbuilder.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).normal(1.0f, 0.0f, 0.0f).endVertex();
        bufferbuilder.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).normal(1.0f, 0.0f, 0.0f).endVertex();
        bufferbuilder.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).normal(1.0f, 0.0f, 0.0f).endVertex();
        bufferbuilder.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).normal(1.0f, 0.0f, 0.0f).endVertex();
        tessellator.draw();
        bufferbuilder.setTranslation(0.0, 0.0, 0.0);
        GlStateManager.enableTexture2D();
    }
    
    public void doRenderShadowAndFire(final Entity entityIn, final double x, final double y, final double z, final float yaw, final float partialTicks) {
        if (this.renderManager.options != null) {
            if (this.renderManager.options.entityShadows && this.shadowSize > 0.0f && !entityIn.isInvisible() && this.renderManager.isRenderShadow()) {
                final double d0 = this.renderManager.getDistanceToCamera(entityIn.posX, entityIn.posY, entityIn.posZ);
                final float f = (float)((1.0 - d0 / 256.0) * this.shadowOpaque);
                if (f > 0.0f) {
                    final Optimization Optimization = (Optimization)Minced.getInstance().manager.getModule(Optimization.class);
                    if (!Minced.getInstance().manager.getModule(Optimization.class).state || !Optimization.shadow.get()) {
                        this.renderShadow(entityIn, x, y, z, f, partialTicks);
                    }
                }
            }
            if (entityIn.canRenderOnFire() && (!(entityIn instanceof EntityPlayer) || !((EntityPlayer)entityIn).isSpectator())) {
                this.renderEntityOnFire(entityIn, x, y, z, partialTicks);
            }
        }
    }
    
    public FontRenderer getFontRendererFromRenderManager() {
        return this.renderManager.getFontRenderer();
    }
    
    protected void renderLivingLabel(final T entityIn, final String str, final double x, final double y, final double z, final int maxDistance) {
        if (Minced.getInstance().manager.getModule(ESP.class).state || Minced.getInstance().manager.getModule(NameTags.class).state) {
            return;
        }
        final double d0 = entityIn.getDistanceSq(this.renderManager.renderViewEntity);
        if (d0 <= maxDistance * maxDistance) {
            final boolean flag = entityIn.isSneaking();
            final float f = this.renderManager.playerViewY;
            final float f2 = this.renderManager.playerViewX;
            final boolean flag2 = this.renderManager.options.thirdPersonView == 2;
            final float f3 = entityIn.height + 0.5f - (flag ? 0.25f : 0.0f);
            final int i = "deadmau5".equals(str) ? -10 : 0;
            EntityRenderer.drawNameplate(this.getFontRendererFromRenderManager(), str, (float)x, (float)y + f3, (float)z, i, f, f2, flag2, flag);
        }
    }
    
    public RenderManager getRenderManager() {
        return this.renderManager;
    }
    
    public boolean isMultipass() {
        return false;
    }
    
    public void renderMultipass(final T entityIn, final double x, final double y, final double z, final float entityYaw, final float partialTicks) {
    }
    
    @Override
    public Class getEntityClass() {
        return this.entityClass;
    }
    
    @Override
    public void setEntityClass(final Class p_setEntityClass_1_) {
        this.entityClass = p_setEntityClass_1_;
    }
    
    @Override
    public ResourceLocation getLocationTextureCustom() {
        return this.locationTextureCustom;
    }
    
    @Override
    public void setLocationTextureCustom(final ResourceLocation p_setLocationTextureCustom_1_) {
        this.locationTextureCustom = p_setLocationTextureCustom_1_;
    }
    
    static {
        SHADOW_TEXTURES = new ResourceLocation("textures/misc/shadow.png");
    }
}
