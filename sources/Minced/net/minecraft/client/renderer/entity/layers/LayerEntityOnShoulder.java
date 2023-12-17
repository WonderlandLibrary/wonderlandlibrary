// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity.layers;

import net.minecraft.entity.Entity;
import net.optifine.shaders.Shaders;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.src.Config;
import net.minecraft.client.model.ModelParrot;
import net.minecraft.client.renderer.entity.RenderParrot;
import net.minecraft.entity.passive.EntityParrot;
import net.minecraft.entity.EntityList;
import javax.annotation.Nullable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.client.renderer.GlStateManager;
import java.util.UUID;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;

public class LayerEntityOnShoulder implements LayerRenderer<EntityPlayer>
{
    private final RenderManager renderManager;
    protected RenderLivingBase<? extends EntityLivingBase> leftRenderer;
    private ModelBase leftModel;
    private ResourceLocation leftResource;
    private UUID leftUniqueId;
    private Class<?> leftEntityClass;
    protected RenderLivingBase<? extends EntityLivingBase> rightRenderer;
    private ModelBase rightModel;
    private ResourceLocation rightResource;
    private UUID rightUniqueId;
    private Class<?> rightEntityClass;
    
    public LayerEntityOnShoulder(final RenderManager p_i47370_1_) {
        this.renderManager = p_i47370_1_;
    }
    
    @Override
    public void doRenderLayer(final EntityPlayer entitylivingbaseIn, final float limbSwing, final float limbSwingAmount, final float partialTicks, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
        if (entitylivingbaseIn.getLeftShoulderEntity() != null || entitylivingbaseIn.getRightShoulderEntity() != null) {
            GlStateManager.enableRescaleNormal();
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            final NBTTagCompound nbttagcompound = entitylivingbaseIn.getLeftShoulderEntity();
            if (!nbttagcompound.isEmpty()) {
                final DataHolder layerentityonshoulder$dataholder = this.renderEntityOnShoulder(entitylivingbaseIn, this.leftUniqueId, nbttagcompound, this.leftRenderer, this.leftModel, this.leftResource, this.leftEntityClass, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, true);
                this.leftUniqueId = layerentityonshoulder$dataholder.entityId;
                this.leftRenderer = layerentityonshoulder$dataholder.renderer;
                this.leftResource = layerentityonshoulder$dataholder.textureLocation;
                this.leftModel = layerentityonshoulder$dataholder.model;
                this.leftEntityClass = layerentityonshoulder$dataholder.clazz;
            }
            final NBTTagCompound nbttagcompound2 = entitylivingbaseIn.getRightShoulderEntity();
            if (!nbttagcompound2.isEmpty()) {
                final DataHolder layerentityonshoulder$dataholder2 = this.renderEntityOnShoulder(entitylivingbaseIn, this.rightUniqueId, nbttagcompound2, this.rightRenderer, this.rightModel, this.rightResource, this.rightEntityClass, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, false);
                this.rightUniqueId = layerentityonshoulder$dataholder2.entityId;
                this.rightRenderer = layerentityonshoulder$dataholder2.renderer;
                this.rightResource = layerentityonshoulder$dataholder2.textureLocation;
                this.rightModel = layerentityonshoulder$dataholder2.model;
                this.rightEntityClass = layerentityonshoulder$dataholder2.clazz;
            }
            GlStateManager.disableRescaleNormal();
        }
    }
    
    private DataHolder renderEntityOnShoulder(final EntityPlayer p_192864_1_, @Nullable UUID p_192864_2_, final NBTTagCompound p_192864_3_, RenderLivingBase<? extends EntityLivingBase> p_192864_4_, ModelBase p_192864_5_, ResourceLocation p_192864_6_, Class<?> p_192864_7_, final float p_192864_8_, final float p_192864_9_, final float p_192864_10_, float p_192864_11_, final float p_192864_12_, final float p_192864_13_, final float p_192864_14_, final boolean p_192864_15_) {
        if (p_192864_2_ == null || !p_192864_2_.equals(p_192864_3_.getUniqueId("UUID"))) {
            p_192864_2_ = p_192864_3_.getUniqueId("UUID");
            p_192864_7_ = EntityList.getClassFromName(p_192864_3_.getString("id"));
            if (p_192864_7_ == EntityParrot.class) {
                p_192864_4_ = new RenderParrot(this.renderManager);
                p_192864_5_ = new ModelParrot();
                p_192864_6_ = RenderParrot.PARROT_TEXTURES[p_192864_3_.getInteger("Variant")];
            }
        }
        final Entity entity = Config.getRenderGlobal().renderedEntity;
        if (p_192864_1_ instanceof AbstractClientPlayer) {
            final AbstractClientPlayer abstractclientplayer = (AbstractClientPlayer)p_192864_1_;
            final Entity entity2 = (p_192864_2_ == this.leftUniqueId) ? abstractclientplayer.entityShoulderLeft : abstractclientplayer.entityShoulderRight;
            if (entity2 != null) {
                Config.getRenderGlobal().renderedEntity = entity2;
                if (Config.isShaders()) {
                    Shaders.nextEntity(entity2);
                }
            }
        }
        p_192864_4_.bindTexture(p_192864_6_);
        GlStateManager.pushMatrix();
        final float f = p_192864_1_.isSneaking() ? -1.3f : -1.5f;
        final float f2 = p_192864_15_ ? 0.4f : -0.4f;
        GlStateManager.translate(f2, f, 0.0f);
        if (p_192864_7_ == EntityParrot.class) {
            p_192864_11_ = 0.0f;
        }
        p_192864_5_.setLivingAnimations(p_192864_1_, p_192864_8_, p_192864_9_, p_192864_10_);
        p_192864_5_.setRotationAngles(p_192864_8_, p_192864_9_, p_192864_11_, p_192864_12_, p_192864_13_, p_192864_14_, p_192864_1_);
        p_192864_5_.render(p_192864_1_, p_192864_8_, p_192864_9_, p_192864_11_, p_192864_12_, p_192864_13_, p_192864_14_);
        GlStateManager.popMatrix();
        Config.getRenderGlobal().renderedEntity = entity;
        if (Config.isShaders()) {
            Shaders.nextEntity(entity);
        }
        return new DataHolder(p_192864_2_, p_192864_4_, p_192864_5_, p_192864_6_, p_192864_7_);
    }
    
    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
    
    class DataHolder
    {
        public UUID entityId;
        public RenderLivingBase<? extends EntityLivingBase> renderer;
        public ModelBase model;
        public ResourceLocation textureLocation;
        public Class<?> clazz;
        
        public DataHolder(final UUID p_i47463_2_, final RenderLivingBase<? extends EntityLivingBase> p_i47463_3_, final ModelBase p_i47463_4_, final ResourceLocation p_i47463_5_, final Class<?> p_i47463_6_) {
            this.entityId = p_i47463_2_;
            this.renderer = p_i47463_3_;
            this.model = p_i47463_4_;
            this.textureLocation = p_i47463_5_;
            this.clazz = p_i47463_6_;
        }
    }
}
