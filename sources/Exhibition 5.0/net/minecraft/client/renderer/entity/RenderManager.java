// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.util.Vec3;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.ReportedException;
import net.minecraft.crash.CrashReport;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockBed;
import net.minecraft.util.EnumFacing;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.client.model.ModelHorse;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.ai.EntityMinecartMobSpawner;
import net.minecraft.entity.item.EntityMinecartTNT;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.client.renderer.tileentity.RenderWitherSkull;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.entity.item.EntityEnderEye;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.init.Items;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.EntityLeashKnot;
import net.minecraft.client.renderer.tileentity.RenderItemFrame;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.client.renderer.tileentity.RenderEnderCrystal;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.client.model.ModelSquid;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.client.model.ModelZombie;
import net.minecraft.entity.monster.EntityGiantZombie;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.client.model.ModelSlime;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEndermite;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.client.model.ModelRabbit;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.client.model.ModelOcelot;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.client.model.ModelChicken;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.client.model.ModelWolf;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.client.model.ModelCow;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.client.model.ModelSheep2;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelPig;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityCaveSpider;
import com.google.common.collect.Maps;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.gui.FontRenderer;
import java.util.Map;

public class RenderManager
{
    private Map entityRenderMap;
    private Map field_178636_l;
    private RenderPlayer field_178637_m;
    private FontRenderer textRenderer;
    public static double renderPosX;
    public static double renderPosY;
    public static double renderPosZ;
    public TextureManager renderEngine;
    public World worldObj;
    public Entity livingPlayer;
    public Entity field_147941_i;
    public static float playerViewY;
    public static float playerViewX;
    public GameSettings options;
    public double viewerPosX;
    public double viewerPosY;
    public double viewerPosZ;
    private boolean field_178639_r;
    private boolean field_178638_s;
    private boolean debugBoundingBox;
    private static final String __OBFID = "CL_00000991";
    
    public RenderManager(final TextureManager p_i46180_1_, final RenderItem p_i46180_2_) {
        this.entityRenderMap = Maps.newHashMap();
        this.field_178636_l = Maps.newHashMap();
        this.field_178639_r = false;
        this.field_178638_s = true;
        this.debugBoundingBox = false;
        this.renderEngine = p_i46180_1_;
        this.entityRenderMap.put(EntityCaveSpider.class, new RenderCaveSpider(this));
        this.entityRenderMap.put(EntitySpider.class, new RenderSpider(this));
        this.entityRenderMap.put(EntityPig.class, new RenderPig(this, new ModelPig(), 0.7f));
        this.entityRenderMap.put(EntitySheep.class, new RenderSheep(this, new ModelSheep2(), 0.7f));
        this.entityRenderMap.put(EntityCow.class, new RenderCow(this, new ModelCow(), 0.7f));
        this.entityRenderMap.put(EntityMooshroom.class, new RenderMooshroom(this, new ModelCow(), 0.7f));
        this.entityRenderMap.put(EntityWolf.class, new RenderWolf(this, new ModelWolf(), 0.5f));
        this.entityRenderMap.put(EntityChicken.class, new RenderChicken(this, new ModelChicken(), 0.3f));
        this.entityRenderMap.put(EntityOcelot.class, new RenderOcelot(this, new ModelOcelot(), 0.4f));
        this.entityRenderMap.put(EntityRabbit.class, new RenderRabbit(this, new ModelRabbit(), 0.3f));
        this.entityRenderMap.put(EntitySilverfish.class, new RenderSilverfish(this));
        this.entityRenderMap.put(EntityEndermite.class, new RenderEndermite(this));
        this.entityRenderMap.put(EntityCreeper.class, new RenderCreeper(this));
        this.entityRenderMap.put(EntityEnderman.class, new RenderEnderman(this));
        this.entityRenderMap.put(EntitySnowman.class, new RenderSnowMan(this));
        this.entityRenderMap.put(EntitySkeleton.class, new RenderSkeleton(this));
        this.entityRenderMap.put(EntityWitch.class, new RenderWitch(this));
        this.entityRenderMap.put(EntityBlaze.class, new RenderBlaze(this));
        this.entityRenderMap.put(EntityPigZombie.class, new RenderPigZombie(this));
        this.entityRenderMap.put(EntityZombie.class, new RenderZombie(this));
        this.entityRenderMap.put(EntitySlime.class, new RenderSlime(this, new ModelSlime(16), 0.25f));
        this.entityRenderMap.put(EntityMagmaCube.class, new RenderMagmaCube(this));
        this.entityRenderMap.put(EntityGiantZombie.class, new RenderGiantZombie(this, new ModelZombie(), 0.5f, 6.0f));
        this.entityRenderMap.put(EntityGhast.class, new RenderGhast(this));
        this.entityRenderMap.put(EntitySquid.class, new RenderSquid(this, new ModelSquid(), 0.7f));
        this.entityRenderMap.put(EntityVillager.class, new RenderVillager(this));
        this.entityRenderMap.put(EntityIronGolem.class, new RenderIronGolem(this));
        this.entityRenderMap.put(EntityBat.class, new RenderBat(this));
        this.entityRenderMap.put(EntityGuardian.class, new RenderGuardian(this));
        this.entityRenderMap.put(EntityDragon.class, new RenderDragon(this));
        this.entityRenderMap.put(EntityEnderCrystal.class, new RenderEnderCrystal(this));
        this.entityRenderMap.put(EntityWither.class, new RenderWither(this));
        this.entityRenderMap.put(Entity.class, new RenderEntity(this));
        this.entityRenderMap.put(EntityPainting.class, new RenderPainting(this));
        this.entityRenderMap.put(EntityItemFrame.class, new RenderItemFrame(this, p_i46180_2_));
        this.entityRenderMap.put(EntityLeashKnot.class, new RenderLeashKnot(this));
        this.entityRenderMap.put(EntityArrow.class, new RenderArrow(this));
        this.entityRenderMap.put(EntitySnowball.class, new RenderSnowball(this, Items.snowball, p_i46180_2_));
        this.entityRenderMap.put(EntityEnderPearl.class, new RenderSnowball(this, Items.ender_pearl, p_i46180_2_));
        this.entityRenderMap.put(EntityEnderEye.class, new RenderSnowball(this, Items.ender_eye, p_i46180_2_));
        this.entityRenderMap.put(EntityEgg.class, new RenderSnowball(this, Items.egg, p_i46180_2_));
        this.entityRenderMap.put(EntityPotion.class, new RenderPotion(this, p_i46180_2_));
        this.entityRenderMap.put(EntityExpBottle.class, new RenderSnowball(this, Items.experience_bottle, p_i46180_2_));
        this.entityRenderMap.put(EntityFireworkRocket.class, new RenderSnowball(this, Items.fireworks, p_i46180_2_));
        this.entityRenderMap.put(EntityLargeFireball.class, new RenderFireball(this, 2.0f));
        this.entityRenderMap.put(EntitySmallFireball.class, new RenderFireball(this, 0.5f));
        this.entityRenderMap.put(EntityWitherSkull.class, new RenderWitherSkull(this));
        this.entityRenderMap.put(EntityItem.class, new RenderEntityItem(this, p_i46180_2_));
        this.entityRenderMap.put(EntityXPOrb.class, new RenderXPOrb(this));
        this.entityRenderMap.put(EntityTNTPrimed.class, new RenderTNTPrimed(this));
        this.entityRenderMap.put(EntityFallingBlock.class, new RenderFallingBlock(this));
        this.entityRenderMap.put(EntityArmorStand.class, new ArmorStandRenderer(this));
        this.entityRenderMap.put(EntityMinecartTNT.class, new RenderTntMinecart(this));
        this.entityRenderMap.put(EntityMinecartMobSpawner.class, new RenderMinecartMobSpawner(this));
        this.entityRenderMap.put(EntityMinecart.class, new RenderMinecart(this));
        this.entityRenderMap.put(EntityBoat.class, new RenderBoat(this));
        this.entityRenderMap.put(EntityFishHook.class, new RenderFish(this));
        this.entityRenderMap.put(EntityHorse.class, new RenderHorse(this, new ModelHorse(), 0.75f));
        this.entityRenderMap.put(EntityLightningBolt.class, new RenderLightningBolt(this));
        this.field_178637_m = new RenderPlayer(this);
        this.field_178636_l.put("default", this.field_178637_m);
        this.field_178636_l.put("slim", new RenderPlayer(this, true));
    }
    
    public void func_178628_a(final double p_178628_1_, final double p_178628_3_, final double p_178628_5_) {
        RenderManager.renderPosX = p_178628_1_;
        RenderManager.renderPosY = p_178628_3_;
        RenderManager.renderPosZ = p_178628_5_;
    }
    
    public Render getEntityClassRenderObject(final Class p_78715_1_) {
        Render var2 = (Render)this.entityRenderMap.get(p_78715_1_);
        if (var2 == null && p_78715_1_ != Entity.class) {
            var2 = this.getEntityClassRenderObject(p_78715_1_.getSuperclass());
            this.entityRenderMap.put(p_78715_1_, var2);
        }
        return var2;
    }
    
    public Render getEntityRenderObject(final Entity p_78713_1_) {
        if (p_78713_1_ instanceof AbstractClientPlayer) {
            final String var2 = ((AbstractClientPlayer)p_78713_1_).func_175154_l();
            final RenderPlayer var3 = this.field_178636_l.get(var2);
            return (var3 != null) ? var3 : this.field_178637_m;
        }
        return this.getEntityClassRenderObject(p_78713_1_.getClass());
    }
    
    public void func_180597_a(final World worldIn, final FontRenderer p_180597_2_, final Entity p_180597_3_, final Entity p_180597_4_, final GameSettings p_180597_5_, final float p_180597_6_) {
        this.worldObj = worldIn;
        this.options = p_180597_5_;
        this.livingPlayer = p_180597_3_;
        this.field_147941_i = p_180597_4_;
        this.textRenderer = p_180597_2_;
        if (p_180597_3_ instanceof EntityLivingBase && ((EntityLivingBase)p_180597_3_).isPlayerSleeping()) {
            final IBlockState var7 = worldIn.getBlockState(new BlockPos(p_180597_3_));
            final Block var8 = var7.getBlock();
            if (var8 == Blocks.bed) {
                final int var9 = ((EnumFacing)var7.getValue(BlockBed.AGE)).getHorizontalIndex();
                RenderManager.playerViewY = var9 * 90 + 180;
                RenderManager.playerViewX = 0.0f;
            }
        }
        else {
            RenderManager.playerViewY = p_180597_3_.prevRotationYaw + (p_180597_3_.rotationYaw - p_180597_3_.prevRotationYaw) * p_180597_6_;
            RenderManager.playerViewX = p_180597_3_.prevRotationPitch + (p_180597_3_.rotationPitch - p_180597_3_.prevRotationPitch) * p_180597_6_;
        }
        if (p_180597_5_.thirdPersonView == 2) {
            RenderManager.playerViewY += 180.0f;
        }
        this.viewerPosX = p_180597_3_.lastTickPosX + (p_180597_3_.posX - p_180597_3_.lastTickPosX) * p_180597_6_;
        this.viewerPosY = p_180597_3_.lastTickPosY + (p_180597_3_.posY - p_180597_3_.lastTickPosY) * p_180597_6_;
        this.viewerPosZ = p_180597_3_.lastTickPosZ + (p_180597_3_.posZ - p_180597_3_.lastTickPosZ) * p_180597_6_;
    }
    
    public void func_178631_a(final float p_178631_1_) {
        RenderManager.playerViewY = p_178631_1_;
    }
    
    public boolean func_178627_a() {
        return this.field_178638_s;
    }
    
    public void func_178633_a(final boolean p_178633_1_) {
        this.field_178638_s = p_178633_1_;
    }
    
    public void func_178629_b(final boolean p_178629_1_) {
        this.debugBoundingBox = p_178629_1_;
    }
    
    public boolean func_178634_b() {
        return this.debugBoundingBox;
    }
    
    public boolean renderEntitySimple(final Entity p_147937_1_, final float p_147937_2_) {
        return this.renderEntityStatic(p_147937_1_, p_147937_2_, false);
    }
    
    public boolean func_178635_a(final Entity p_178635_1_, final ICamera p_178635_2_, final double p_178635_3_, final double p_178635_5_, final double p_178635_7_) {
        final Render var9 = this.getEntityRenderObject(p_178635_1_);
        return var9 != null && var9.func_177071_a(p_178635_1_, p_178635_2_, p_178635_3_, p_178635_5_, p_178635_7_);
    }
    
    public boolean renderEntityStatic(final Entity p_147936_1_, final float p_147936_2_, final boolean p_147936_3_) {
        if (p_147936_1_.ticksExisted == 0) {
            p_147936_1_.lastTickPosX = p_147936_1_.posX;
            p_147936_1_.lastTickPosY = p_147936_1_.posY;
            p_147936_1_.lastTickPosZ = p_147936_1_.posZ;
        }
        final double var4 = p_147936_1_.lastTickPosX + (p_147936_1_.posX - p_147936_1_.lastTickPosX) * p_147936_2_;
        final double var5 = p_147936_1_.lastTickPosY + (p_147936_1_.posY - p_147936_1_.lastTickPosY) * p_147936_2_;
        final double var6 = p_147936_1_.lastTickPosZ + (p_147936_1_.posZ - p_147936_1_.lastTickPosZ) * p_147936_2_;
        final float var7 = p_147936_1_.prevRotationYaw + (p_147936_1_.rotationYaw - p_147936_1_.prevRotationYaw) * p_147936_2_;
        int var8 = p_147936_1_.getBrightnessForRender(p_147936_2_);
        if (p_147936_1_.isBurning()) {
            var8 = 15728880;
        }
        final int var9 = var8 % 65536;
        final int var10 = var8 / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, var9 / 1.0f, var10 / 1.0f);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        return this.doRenderEntity(p_147936_1_, var4 - RenderManager.renderPosX, var5 - RenderManager.renderPosY, var6 - RenderManager.renderPosZ, var7, p_147936_2_, p_147936_3_);
    }
    
    public void func_178630_b(final Entity p_178630_1_, final float p_178630_2_) {
        final double var3 = p_178630_1_.lastTickPosX + (p_178630_1_.posX - p_178630_1_.lastTickPosX) * p_178630_2_;
        final double var4 = p_178630_1_.lastTickPosY + (p_178630_1_.posY - p_178630_1_.lastTickPosY) * p_178630_2_;
        final double var5 = p_178630_1_.lastTickPosZ + (p_178630_1_.posZ - p_178630_1_.lastTickPosZ) * p_178630_2_;
        final Render var6 = this.getEntityRenderObject(p_178630_1_);
        if (var6 != null && this.renderEngine != null) {
            final int var7 = p_178630_1_.getBrightnessForRender(p_178630_2_);
            final int var8 = var7 % 65536;
            final int var9 = var7 / 65536;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, var8 / 1.0f, var9 / 1.0f);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            var6.func_177067_a(p_178630_1_, var3 - RenderManager.renderPosX, var4 - RenderManager.renderPosY, var5 - RenderManager.renderPosZ);
        }
    }
    
    public boolean renderEntityWithPosYaw(final Entity p_147940_1_, final double p_147940_2_, final double p_147940_4_, final double p_147940_6_, final float p_147940_8_, final float p_147940_9_) {
        return this.doRenderEntity(p_147940_1_, p_147940_2_, p_147940_4_, p_147940_6_, p_147940_8_, p_147940_9_, false);
    }
    
    public boolean doRenderEntity(final Entity p_147939_1_, final double p_147939_2_, final double p_147939_4_, final double p_147939_6_, final float p_147939_8_, final float p_147939_9_, final boolean p_147939_10_) {
        Render var11 = null;
        try {
            var11 = this.getEntityRenderObject(p_147939_1_);
            if (var11 != null && this.renderEngine != null) {
                try {
                    if (var11 instanceof RendererLivingEntity) {
                        ((RendererLivingEntity)var11).func_177086_a(this.field_178639_r);
                    }
                    var11.doRender(p_147939_1_, p_147939_2_, p_147939_4_, p_147939_6_, p_147939_8_, p_147939_9_);
                }
                catch (Throwable var12) {
                    throw new ReportedException(CrashReport.makeCrashReport(var12, "Rendering entity in world"));
                }
                try {
                    if (!this.field_178639_r) {
                        var11.doRenderShadowAndFire(p_147939_1_, p_147939_2_, p_147939_4_, p_147939_6_, p_147939_8_, p_147939_9_);
                    }
                }
                catch (Throwable var13) {
                    throw new ReportedException(CrashReport.makeCrashReport(var13, "Post-rendering entity in world"));
                }
                if (!this.debugBoundingBox || p_147939_1_.isInvisible() || p_147939_10_) {
                    return true;
                }
                try {
                    this.renderDebugBoundingBox(p_147939_1_, p_147939_2_, p_147939_4_, p_147939_6_, p_147939_8_, p_147939_9_);
                    return true;
                }
                catch (Throwable var14) {
                    throw new ReportedException(CrashReport.makeCrashReport(var14, "Rendering entity hitbox in world"));
                }
            }
            if (this.renderEngine != null) {
                return false;
            }
            return true;
        }
        catch (Throwable var16) {
            final CrashReport var15 = CrashReport.makeCrashReport(var16, "Rendering entity in world");
            final CrashReportCategory var17 = var15.makeCategory("Entity being rendered");
            p_147939_1_.addEntityCrashInfo(var17);
            final CrashReportCategory var18 = var15.makeCategory("Renderer details");
            var18.addCrashSection("Assigned renderer", var11);
            var18.addCrashSection("Location", CrashReportCategory.getCoordinateInfo(p_147939_2_, p_147939_4_, p_147939_6_));
            var18.addCrashSection("Rotation", p_147939_8_);
            var18.addCrashSection("Delta", p_147939_9_);
            throw new ReportedException(var15);
        }
    }
    
    public double getRenderPosX() {
        return RenderManager.renderPosX;
    }
    
    public double getRenderPosY() {
        return RenderManager.renderPosY;
    }
    
    public double getRenderPosZ() {
        return RenderManager.renderPosZ;
    }
    
    private void renderDebugBoundingBox(final Entity p_85094_1_, final double p_85094_2_, final double p_85094_4_, final double p_85094_6_, final float p_85094_8_, final float p_85094_9_) {
        GlStateManager.depthMask(false);
        GlStateManager.disableTextures();
        GlStateManager.disableLighting();
        GlStateManager.disableCull();
        GlStateManager.disableBlend();
        final float var10 = p_85094_1_.width / 2.0f;
        final AxisAlignedBB var11 = p_85094_1_.getEntityBoundingBox();
        final AxisAlignedBB var12 = new AxisAlignedBB(var11.minX - p_85094_1_.posX + p_85094_2_, var11.minY - p_85094_1_.posY + p_85094_4_, var11.minZ - p_85094_1_.posZ + p_85094_6_, var11.maxX - p_85094_1_.posX + p_85094_2_, var11.maxY - p_85094_1_.posY + p_85094_4_, var11.maxZ - p_85094_1_.posZ + p_85094_6_);
        RenderGlobal.drawOutlinedBoundingBox(var12, 16777215);
        if (p_85094_1_ instanceof EntityLivingBase) {
            final float var13 = 0.01f;
            RenderGlobal.drawOutlinedBoundingBox(new AxisAlignedBB(p_85094_2_ - var10, p_85094_4_ + p_85094_1_.getEyeHeight() - 0.009999999776482582, p_85094_6_ - var10, p_85094_2_ + var10, p_85094_4_ + p_85094_1_.getEyeHeight() + 0.009999999776482582, p_85094_6_ + var10), 16711680);
        }
        final Tessellator var14 = Tessellator.getInstance();
        final WorldRenderer var15 = var14.getWorldRenderer();
        final Vec3 var16 = p_85094_1_.getLook(p_85094_9_);
        var15.startDrawing(3);
        var15.func_178991_c(255);
        var15.addVertex(p_85094_2_, p_85094_4_ + p_85094_1_.getEyeHeight(), p_85094_6_);
        var15.addVertex(p_85094_2_ + var16.xCoord * 2.0, p_85094_4_ + p_85094_1_.getEyeHeight() + var16.yCoord * 2.0, p_85094_6_ + var16.zCoord * 2.0);
        var14.draw();
        GlStateManager.enableTextures();
        GlStateManager.enableLighting();
        GlStateManager.enableCull();
        GlStateManager.disableBlend();
        GlStateManager.depthMask(true);
    }
    
    public void set(final World worldIn) {
        this.worldObj = worldIn;
    }
    
    public double getDistanceToCamera(final double p_78714_1_, final double p_78714_3_, final double p_78714_5_) {
        final double var7 = p_78714_1_ - this.viewerPosX;
        final double var8 = p_78714_3_ - this.viewerPosY;
        final double var9 = p_78714_5_ - this.viewerPosZ;
        return var7 * var7 + var8 * var8 + var9 * var9;
    }
    
    public FontRenderer getFontRenderer() {
        return this.textRenderer;
    }
    
    public void func_178632_c(final boolean p_178632_1_) {
        this.field_178639_r = p_178632_1_;
    }
}
