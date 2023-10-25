/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 */
package net.minecraft.client.renderer.entity;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.model.ModelZombieVillager;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerCustomHead;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.layers.LayerVillagerArmor;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.util.ResourceLocation;

public class RenderZombie
extends RenderBiped<EntityZombie> {
    private final List<LayerRenderer<EntityZombie>> field_177122_o;
    private final List<LayerRenderer<EntityZombie>> field_177121_n;
    private static final ResourceLocation zombieVillagerTextures;
    private final ModelZombieVillager zombieVillagerModel;
    private static final ResourceLocation zombieTextures;
    private final ModelBiped field_82434_o;

    private void func_82427_a(EntityZombie entityZombie) {
        if (entityZombie.isVillager()) {
            this.mainModel = this.zombieVillagerModel;
            this.layerRenderers = this.field_177121_n;
        } else {
            this.mainModel = this.field_82434_o;
            this.layerRenderers = this.field_177122_o;
        }
        this.modelBipedMain = (ModelBiped)this.mainModel;
    }

    @Override
    public void doRender(EntityZombie entityZombie, double d, double d2, double d3, float f, float f2) {
        this.func_82427_a(entityZombie);
        super.doRender(entityZombie, d, d2, d3, f, f2);
    }

    @Override
    protected void rotateCorpse(EntityZombie entityZombie, float f, float f2, float f3) {
        if (entityZombie.isConverting()) {
            f2 += (float)(Math.cos((double)entityZombie.ticksExisted * 3.25) * Math.PI * 0.25);
        }
        super.rotateCorpse(entityZombie, f, f2, f3);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityZombie entityZombie) {
        return entityZombie.isVillager() ? zombieVillagerTextures : zombieTextures;
    }

    public RenderZombie(RenderManager renderManager) {
        super(renderManager, new ModelZombie(), 0.5f, 1.0f);
        LayerRenderer layerRenderer = (LayerRenderer)this.layerRenderers.get(0);
        this.field_82434_o = this.modelBipedMain;
        this.zombieVillagerModel = new ModelZombieVillager();
        this.addLayer(new LayerHeldItem(this));
        LayerBipedArmor layerBipedArmor = new LayerBipedArmor(this){

            @Override
            protected void initArmor() {
                this.field_177189_c = new ModelZombie(0.5f, true);
                this.field_177186_d = new ModelZombie(1.0f, true);
            }
        };
        this.addLayer(layerBipedArmor);
        this.field_177122_o = Lists.newArrayList((Iterable)this.layerRenderers);
        if (layerRenderer instanceof LayerCustomHead) {
            this.removeLayer(layerRenderer);
            this.addLayer(new LayerCustomHead(this.zombieVillagerModel.bipedHead));
        }
        this.removeLayer(layerBipedArmor);
        this.addLayer(new LayerVillagerArmor(this));
        this.field_177121_n = Lists.newArrayList((Iterable)this.layerRenderers);
    }

    static {
        zombieTextures = new ResourceLocation("textures/entity/zombie/zombie.png");
        zombieVillagerTextures = new ResourceLocation("textures/entity/zombie/zombie_villager.png");
    }
}

