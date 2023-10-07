// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.client.renderer.entity.layers.LayerVillagerArmor;
import net.minecraft.client.renderer.entity.layers.LayerCustomHead;
import com.google.common.collect.Lists;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.model.ModelZombie;
import java.util.List;
import net.minecraft.client.model.ModelZombieVillager;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.util.ResourceLocation;

public class RenderZombie extends RenderBiped
{
    private static final ResourceLocation zombieTextures;
    private static final ResourceLocation zombieVillagerTextures;
    private final ModelBiped field_82434_o;
    private final ModelZombieVillager zombieVillagerModel;
    private final List field_177121_n;
    private final List field_177122_o;
    private static final String __OBFID = "CL_00001037";
    
    public RenderZombie(final RenderManager p_i46127_1_) {
        super(p_i46127_1_, new ModelZombie(), 0.5f, 1.0f);
        final LayerRenderer var2 = this.field_177097_h.get(0);
        this.field_82434_o = this.modelBipedMain;
        this.zombieVillagerModel = new ModelZombieVillager();
        this.addLayer(new LayerHeldItem(this));
        final LayerBipedArmor var3 = new LayerBipedArmor(this) {
            private static final String __OBFID = "CL_00002429";
            
            @Override
            protected void func_177177_a() {
                this.field_177189_c = new ModelZombie(0.5f, true);
                this.field_177186_d = new ModelZombie(1.0f, true);
            }
        };
        this.addLayer(var3);
        this.field_177122_o = Lists.newArrayList((Iterable)this.field_177097_h);
        if (var2 instanceof LayerCustomHead) {
            this.func_177089_b(var2);
            this.addLayer(new LayerCustomHead(this.zombieVillagerModel.bipedHead));
        }
        this.func_177089_b(var3);
        this.addLayer(new LayerVillagerArmor(this));
        this.field_177121_n = Lists.newArrayList((Iterable)this.field_177097_h);
    }
    
    public void func_180579_a(final EntityZombie p_180579_1_, final double p_180579_2_, final double p_180579_4_, final double p_180579_6_, final float p_180579_8_, final float p_180579_9_) {
        this.func_82427_a(p_180579_1_);
        super.doRender(p_180579_1_, p_180579_2_, p_180579_4_, p_180579_6_, p_180579_8_, p_180579_9_);
    }
    
    protected ResourceLocation func_180578_a(final EntityZombie p_180578_1_) {
        return p_180578_1_.isVillager() ? RenderZombie.zombieVillagerTextures : RenderZombie.zombieTextures;
    }
    
    private void func_82427_a(final EntityZombie p_82427_1_) {
        if (p_82427_1_.isVillager()) {
            this.mainModel = this.zombieVillagerModel;
            this.field_177097_h = this.field_177121_n;
        }
        else {
            this.mainModel = this.field_82434_o;
            this.field_177097_h = this.field_177122_o;
        }
        this.modelBipedMain = (ModelBiped)this.mainModel;
    }
    
    protected void rotateCorpse(final EntityZombie p_77043_1_, final float p_77043_2_, float p_77043_3_, final float p_77043_4_) {
        if (p_77043_1_.isConverting()) {
            p_77043_3_ += (float)(Math.cos(p_77043_1_.ticksExisted * 3.25) * 3.141592653589793 * 0.25);
        }
        super.rotateCorpse(p_77043_1_, p_77043_2_, p_77043_3_, p_77043_4_);
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final EntityLiving p_110775_1_) {
        return this.func_180578_a((EntityZombie)p_110775_1_);
    }
    
    @Override
    public void doRender(final EntityLiving p_76986_1_, final double p_76986_2_, final double p_76986_4_, final double p_76986_6_, final float p_76986_8_, final float p_76986_9_) {
        this.func_180579_a((EntityZombie)p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }
    
    @Override
    protected void rotateCorpse(final EntityLivingBase p_77043_1_, final float p_77043_2_, final float p_77043_3_, final float p_77043_4_) {
        this.rotateCorpse((EntityZombie)p_77043_1_, p_77043_2_, p_77043_3_, p_77043_4_);
    }
    
    @Override
    public void doRender(final EntityLivingBase p_76986_1_, final double p_76986_2_, final double p_76986_4_, final double p_76986_6_, final float p_76986_8_, final float p_76986_9_) {
        this.func_180579_a((EntityZombie)p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final Entity p_110775_1_) {
        return this.func_180578_a((EntityZombie)p_110775_1_);
    }
    
    @Override
    public void doRender(final Entity p_76986_1_, final double p_76986_2_, final double p_76986_4_, final double p_76986_6_, final float p_76986_8_, final float p_76986_9_) {
        this.func_180579_a((EntityZombie)p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }
    
    static {
        zombieTextures = new ResourceLocation("textures/entity/zombie/zombie.png");
        zombieVillagerTextures = new ResourceLocation("textures/entity/zombie/zombie_villager.png");
    }
}
