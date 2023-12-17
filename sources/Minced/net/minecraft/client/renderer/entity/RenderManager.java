// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity;

import java.util.Collections;
import net.minecraft.util.math.Vec3d;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.RenderGlobal;
import net.optifine.shaders.Shaders;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ReportedException;
import net.minecraft.crash.CrashReport;
import net.optifine.entity.model.CustomEntityModels;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockBed;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.EntityLivingBase;
import javax.annotation.Nullable;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.optifine.reflect.Reflector;
import net.optifine.player.PlayerItemsLayer;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.projectile.EntityLlamaSpit;
import net.minecraft.entity.passive.EntityLlama;
import net.minecraft.entity.passive.EntityDonkey;
import net.minecraft.entity.passive.EntityMule;
import net.minecraft.entity.passive.EntityZombieHorse;
import net.minecraft.entity.passive.EntitySkeletonHorse;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.EntityAreaEffectCloud;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityMinecartMobSpawner;
import net.minecraft.entity.item.EntityMinecartTNT;
import net.minecraft.entity.projectile.EntityEvokerFangs;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.projectile.EntityShulkerBullet;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.entity.projectile.EntityDragonFireball;
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
import net.minecraft.entity.projectile.EntitySpectralArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.entity.EntityLeashKnot;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.monster.EntityIllusionIllager;
import net.minecraft.entity.monster.EntityVex;
import net.minecraft.entity.monster.EntityVindicator;
import net.minecraft.entity.monster.EntityEvoker;
import net.minecraft.entity.monster.EntityPolarBear;
import net.minecraft.entity.monster.EntityShulker;
import net.minecraft.entity.monster.EntityElderGuardian;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityGiantZombie;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntityHusk;
import net.minecraft.entity.monster.EntityZombieVillager;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityStray;
import net.minecraft.entity.monster.EntityWitherSkeleton;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEndermite;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.entity.passive.EntityParrot;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityCaveSpider;
import com.google.common.collect.Maps;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.gui.FontRenderer;
import java.util.Map;

public class RenderManager
{
    private final Map<Class, Render> entityRenderMap;
    private final Map<String, RenderPlayer> skinMap;
    private final RenderPlayer playerRenderer;
    private FontRenderer textRenderer;
    public double renderPosX;
    public double renderPosY;
    public double renderPosZ;
    public TextureManager renderEngine;
    public World world;
    public Entity renderViewEntity;
    public Entity pointedEntity;
    public float playerViewY;
    public float playerViewX;
    public GameSettings options;
    public double viewerPosX;
    public double viewerPosY;
    public double viewerPosZ;
    private boolean renderOutlines;
    private boolean renderShadow;
    private boolean debugBoundingBox;
    public Render renderRender;
    
    public RenderManager(final TextureManager renderEngineIn, final RenderItem itemRendererIn) {
        this.entityRenderMap = (Map<Class, Render>)Maps.newHashMap();
        this.skinMap = (Map<String, RenderPlayer>)Maps.newHashMap();
        this.renderShadow = true;
        this.renderRender = null;
        this.renderEngine = renderEngineIn;
        this.entityRenderMap.put(EntityCaveSpider.class, new RenderCaveSpider(this));
        this.entityRenderMap.put(EntitySpider.class, new RenderSpider(this));
        this.entityRenderMap.put(EntityPig.class, new RenderPig(this));
        this.entityRenderMap.put(EntitySheep.class, new RenderSheep(this));
        this.entityRenderMap.put(EntityCow.class, new RenderCow(this));
        this.entityRenderMap.put(EntityMooshroom.class, new RenderMooshroom(this));
        this.entityRenderMap.put(EntityWolf.class, new RenderWolf(this));
        this.entityRenderMap.put(EntityChicken.class, new RenderChicken(this));
        this.entityRenderMap.put(EntityOcelot.class, new RenderOcelot(this));
        this.entityRenderMap.put(EntityRabbit.class, new RenderRabbit(this));
        this.entityRenderMap.put(EntityParrot.class, new RenderParrot(this));
        this.entityRenderMap.put(EntitySilverfish.class, new RenderSilverfish(this));
        this.entityRenderMap.put(EntityEndermite.class, new RenderEndermite(this));
        this.entityRenderMap.put(EntityCreeper.class, new RenderCreeper(this));
        this.entityRenderMap.put(EntityEnderman.class, new RenderEnderman(this));
        this.entityRenderMap.put(EntitySnowman.class, new RenderSnowMan(this));
        this.entityRenderMap.put(EntitySkeleton.class, new RenderSkeleton(this));
        this.entityRenderMap.put(EntityWitherSkeleton.class, new RenderWitherSkeleton(this));
        this.entityRenderMap.put(EntityStray.class, new RenderStray(this));
        this.entityRenderMap.put(EntityWitch.class, new RenderWitch(this));
        this.entityRenderMap.put(EntityBlaze.class, new RenderBlaze(this));
        this.entityRenderMap.put(EntityPigZombie.class, new RenderPigZombie(this));
        this.entityRenderMap.put(EntityZombie.class, new RenderZombie(this));
        this.entityRenderMap.put(EntityZombieVillager.class, new RenderZombieVillager(this));
        this.entityRenderMap.put(EntityHusk.class, new RenderHusk(this));
        this.entityRenderMap.put(EntitySlime.class, new RenderSlime(this));
        this.entityRenderMap.put(EntityMagmaCube.class, new RenderMagmaCube(this));
        this.entityRenderMap.put(EntityGiantZombie.class, new RenderGiantZombie(this, 6.0f));
        this.entityRenderMap.put(EntityGhast.class, new RenderGhast(this));
        this.entityRenderMap.put(EntitySquid.class, new RenderSquid(this));
        this.entityRenderMap.put(EntityVillager.class, new RenderVillager(this));
        this.entityRenderMap.put(EntityIronGolem.class, new RenderIronGolem(this));
        this.entityRenderMap.put(EntityBat.class, new RenderBat(this));
        this.entityRenderMap.put(EntityGuardian.class, new RenderGuardian(this));
        this.entityRenderMap.put(EntityElderGuardian.class, new RenderElderGuardian(this));
        this.entityRenderMap.put(EntityShulker.class, new RenderShulker(this));
        this.entityRenderMap.put(EntityPolarBear.class, new RenderPolarBear(this));
        this.entityRenderMap.put(EntityEvoker.class, new RenderEvoker(this));
        this.entityRenderMap.put(EntityVindicator.class, new RenderVindicator(this));
        this.entityRenderMap.put(EntityVex.class, new RenderVex(this));
        this.entityRenderMap.put(EntityIllusionIllager.class, new RenderIllusionIllager(this));
        this.entityRenderMap.put(EntityDragon.class, new RenderDragon(this));
        this.entityRenderMap.put(EntityEnderCrystal.class, new RenderEnderCrystal(this));
        this.entityRenderMap.put(EntityWither.class, new RenderWither(this));
        this.entityRenderMap.put(Entity.class, new RenderEntity(this));
        this.entityRenderMap.put(EntityPainting.class, new RenderPainting(this));
        this.entityRenderMap.put(EntityItemFrame.class, new RenderItemFrame(this, itemRendererIn));
        this.entityRenderMap.put(EntityLeashKnot.class, new RenderLeashKnot(this));
        this.entityRenderMap.put(EntityTippedArrow.class, new RenderTippedArrow(this));
        this.entityRenderMap.put(EntitySpectralArrow.class, new RenderSpectralArrow(this));
        this.entityRenderMap.put(EntitySnowball.class, new RenderSnowball(this, Items.SNOWBALL, itemRendererIn));
        this.entityRenderMap.put(EntityEnderPearl.class, new RenderSnowball(this, Items.ENDER_PEARL, itemRendererIn));
        this.entityRenderMap.put(EntityEnderEye.class, new RenderSnowball(this, Items.ENDER_EYE, itemRendererIn));
        this.entityRenderMap.put(EntityEgg.class, new RenderSnowball(this, Items.EGG, itemRendererIn));
        this.entityRenderMap.put(EntityPotion.class, new RenderPotion(this, itemRendererIn));
        this.entityRenderMap.put(EntityExpBottle.class, new RenderSnowball(this, Items.EXPERIENCE_BOTTLE, itemRendererIn));
        this.entityRenderMap.put(EntityFireworkRocket.class, new RenderSnowball(this, Items.FIREWORKS, itemRendererIn));
        this.entityRenderMap.put(EntityLargeFireball.class, new RenderFireball(this, 2.0f));
        this.entityRenderMap.put(EntitySmallFireball.class, new RenderFireball(this, 0.5f));
        this.entityRenderMap.put(EntityDragonFireball.class, new RenderDragonFireball(this));
        this.entityRenderMap.put(EntityWitherSkull.class, new RenderWitherSkull(this));
        this.entityRenderMap.put(EntityShulkerBullet.class, new RenderShulkerBullet(this));
        this.entityRenderMap.put(EntityItem.class, new RenderEntityItem(this, itemRendererIn));
        this.entityRenderMap.put(EntityXPOrb.class, new RenderXPOrb(this));
        this.entityRenderMap.put(EntityTNTPrimed.class, new RenderTNTPrimed(this));
        this.entityRenderMap.put(EntityFallingBlock.class, new RenderFallingBlock(this));
        this.entityRenderMap.put(EntityArmorStand.class, new RenderArmorStand(this));
        this.entityRenderMap.put(EntityEvokerFangs.class, new RenderEvokerFangs(this));
        this.entityRenderMap.put(EntityMinecartTNT.class, new RenderTntMinecart(this));
        this.entityRenderMap.put(EntityMinecartMobSpawner.class, new RenderMinecartMobSpawner(this));
        this.entityRenderMap.put(EntityMinecart.class, new RenderMinecart(this));
        this.entityRenderMap.put(EntityBoat.class, new RenderBoat(this));
        this.entityRenderMap.put(EntityFishHook.class, new RenderFish(this));
        this.entityRenderMap.put(EntityAreaEffectCloud.class, new RenderAreaEffectCloud(this));
        this.entityRenderMap.put(EntityHorse.class, new RenderHorse(this));
        this.entityRenderMap.put(EntitySkeletonHorse.class, new RenderAbstractHorse(this));
        this.entityRenderMap.put(EntityZombieHorse.class, new RenderAbstractHorse(this));
        this.entityRenderMap.put(EntityMule.class, new RenderAbstractHorse(this, 0.92f));
        this.entityRenderMap.put(EntityDonkey.class, new RenderAbstractHorse(this, 0.87f));
        this.entityRenderMap.put(EntityLlama.class, new RenderLlama(this));
        this.entityRenderMap.put(EntityLlamaSpit.class, new RenderLlamaSpit(this));
        this.entityRenderMap.put(EntityLightningBolt.class, new RenderLightningBolt(this));
        this.playerRenderer = new RenderPlayer(this);
        this.skinMap.put("default", this.playerRenderer);
        this.skinMap.put("slim", new RenderPlayer(this, true));
        PlayerItemsLayer.register(this.skinMap);
        if (Reflector.RenderingRegistry_loadEntityRenderers.exists()) {
            Reflector.call(Reflector.RenderingRegistry_loadEntityRenderers, this, this.entityRenderMap);
        }
    }
    
    public void setRenderPosition(final double renderPosXIn, final double renderPosYIn, final double renderPosZIn) {
        this.renderPosX = renderPosXIn;
        this.renderPosY = renderPosYIn;
        this.renderPosZ = renderPosZIn;
    }
    
    public <T extends Entity> Render<T> getEntityClassRenderObject(final Class<? extends Entity> entityClass) {
        Render<T> render = this.entityRenderMap.get(entityClass);
        if (render == null && entityClass != Entity.class) {
            render = (Render<T>)this.getEntityClassRenderObject((Class<? extends Entity>)entityClass.getSuperclass());
            this.entityRenderMap.put(entityClass, render);
        }
        return render;
    }
    
    @Nullable
    public <T extends Entity> Render<T> getEntityRenderObject(final Entity entityIn) {
        if (entityIn instanceof AbstractClientPlayer) {
            final String s = ((AbstractClientPlayer)entityIn).getSkinType();
            final RenderPlayer renderplayer = this.skinMap.get(s);
            return (Render<T>)((renderplayer != null) ? renderplayer : this.playerRenderer);
        }
        return this.getEntityClassRenderObject(entityIn.getClass());
    }
    
    public void cacheActiveRenderInfo(final World worldIn, final FontRenderer textRendererIn, final Entity livingPlayerIn, final Entity pointedEntityIn, final GameSettings optionsIn, final float partialTicks) {
        this.world = worldIn;
        this.options = optionsIn;
        this.renderViewEntity = livingPlayerIn;
        this.pointedEntity = pointedEntityIn;
        this.textRenderer = textRendererIn;
        if (livingPlayerIn instanceof EntityLivingBase && ((EntityLivingBase)livingPlayerIn).isPlayerSleeping()) {
            final IBlockState iblockstate = worldIn.getBlockState(new BlockPos(livingPlayerIn));
            final Block block = iblockstate.getBlock();
            if (Reflector.callBoolean(block, Reflector.ForgeBlock_isBed, iblockstate, worldIn, new BlockPos(livingPlayerIn), (EntityLivingBase)livingPlayerIn)) {
                final EnumFacing enumfacing = (EnumFacing)Reflector.call(block, Reflector.ForgeBlock_getBedDirection, iblockstate, worldIn, new BlockPos(livingPlayerIn));
                final int i = enumfacing.getHorizontalIndex();
                this.playerViewY = (float)(i * 90 + 180);
                this.playerViewX = 0.0f;
            }
            else if (block == Blocks.BED) {
                final int j = iblockstate.getValue((IProperty<EnumFacing>)BlockBed.FACING).getHorizontalIndex();
                this.playerViewY = (float)(j * 90 + 180);
                this.playerViewX = 0.0f;
            }
        }
        else {
            this.playerViewY = livingPlayerIn.prevRotationYaw + (livingPlayerIn.rotationYaw - livingPlayerIn.prevRotationYaw) * partialTicks;
            this.playerViewX = livingPlayerIn.prevRotationPitch + (livingPlayerIn.rotationPitch - livingPlayerIn.prevRotationPitch) * partialTicks;
        }
        if (optionsIn.thirdPersonView == 2) {
            this.playerViewY += 180.0f;
        }
        this.viewerPosX = livingPlayerIn.lastTickPosX + (livingPlayerIn.posX - livingPlayerIn.lastTickPosX) * partialTicks;
        this.viewerPosY = livingPlayerIn.lastTickPosY + (livingPlayerIn.posY - livingPlayerIn.lastTickPosY) * partialTicks;
        this.viewerPosZ = livingPlayerIn.lastTickPosZ + (livingPlayerIn.posZ - livingPlayerIn.lastTickPosZ) * partialTicks;
    }
    
    public void setPlayerViewY(final float playerViewYIn) {
        this.playerViewY = playerViewYIn;
    }
    
    public boolean isRenderShadow() {
        return this.renderShadow;
    }
    
    public void setRenderShadow(final boolean renderShadowIn) {
        this.renderShadow = renderShadowIn;
    }
    
    public void setDebugBoundingBox(final boolean debugBoundingBoxIn) {
        this.debugBoundingBox = debugBoundingBoxIn;
    }
    
    public boolean isDebugBoundingBox() {
        return this.debugBoundingBox;
    }
    
    public boolean isRenderMultipass(final Entity entityIn) {
        return this.getEntityRenderObject(entityIn).isMultipass();
    }
    
    public boolean shouldRender(final Entity entityIn, final ICamera camera, final double camX, final double camY, final double camZ) {
        final Render<Entity> render = this.getEntityRenderObject(entityIn);
        return render != null && render.shouldRender(entityIn, camera, camX, camY, camZ);
    }
    
    public void renderEntityStatic(final Entity entityIn, final float partialTicks, final boolean p_188388_3_) {
        if (entityIn.ticksExisted == 0) {
            entityIn.lastTickPosX = entityIn.posX;
            entityIn.lastTickPosY = entityIn.posY;
            entityIn.lastTickPosZ = entityIn.posZ;
        }
        final double d0 = entityIn.lastTickPosX + (entityIn.posX - entityIn.lastTickPosX) * partialTicks;
        final double d2 = entityIn.lastTickPosY + (entityIn.posY - entityIn.lastTickPosY) * partialTicks;
        final double d3 = entityIn.lastTickPosZ + (entityIn.posZ - entityIn.lastTickPosZ) * partialTicks;
        final float f = entityIn.prevRotationYaw + (entityIn.rotationYaw - entityIn.prevRotationYaw) * partialTicks;
        int i = entityIn.getBrightnessForRender();
        if (entityIn.isBurning()) {
            i = 15728880;
        }
        final int j = i % 65536;
        final int k = i / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)j, (float)k);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.renderEntity(entityIn, d0 - this.renderPosX, d2 - this.renderPosY, d3 - this.renderPosZ, f, partialTicks, p_188388_3_);
    }
    
    public void renderEntity(final Entity entityIn, final double x, final double y, final double z, final float yaw, final float partialTicks, final boolean p_188391_10_) {
        Render<Entity> render = null;
        try {
            render = this.getEntityRenderObject(entityIn);
            if (render != null && this.renderEngine != null) {
                try {
                    render.setRenderOutlines(this.renderOutlines);
                    if (CustomEntityModels.isActive()) {
                        this.renderRender = render;
                    }
                    render.doRender(entityIn, x, y, z, yaw, partialTicks);
                }
                catch (Throwable throwable2) {
                    throw new ReportedException(CrashReport.makeCrashReport(throwable2, "Rendering entity in world"));
                }
                try {
                    if (!this.renderOutlines) {
                        render.doRenderShadowAndFire(entityIn, x, y, z, yaw, partialTicks);
                    }
                }
                catch (Throwable throwable3) {
                    throw new ReportedException(CrashReport.makeCrashReport(throwable3, "Post-rendering entity in world"));
                }
                if (this.debugBoundingBox && !entityIn.isInvisible() && !p_188391_10_ && !Minecraft.getMinecraft().isReducedDebug()) {
                    try {
                        this.renderDebugBoundingBox(entityIn, x, y, z, yaw, partialTicks);
                    }
                    catch (Throwable throwable4) {
                        throw new ReportedException(CrashReport.makeCrashReport(throwable4, "Rendering entity hitbox in world"));
                    }
                }
            }
        }
        catch (Throwable throwable5) {
            final CrashReport crashreport = CrashReport.makeCrashReport(throwable5, "Rendering entity in world");
            final CrashReportCategory crashreportcategory = crashreport.makeCategory("Entity being rendered");
            entityIn.addEntityCrashInfo(crashreportcategory);
            final CrashReportCategory crashreportcategory2 = crashreport.makeCategory("Renderer details");
            crashreportcategory2.addCrashSection("Assigned renderer", render);
            crashreportcategory2.addCrashSection("Location", CrashReportCategory.getCoordinateInfo(x, y, z));
            crashreportcategory2.addCrashSection("Rotation", yaw);
            crashreportcategory2.addCrashSection("Delta", partialTicks);
            throw new ReportedException(crashreport);
        }
    }
    
    public void renderMultipass(final Entity entityIn, final float partialTicks) {
        if (entityIn.ticksExisted == 0) {
            entityIn.lastTickPosX = entityIn.posX;
            entityIn.lastTickPosY = entityIn.posY;
            entityIn.lastTickPosZ = entityIn.posZ;
        }
        final double d0 = entityIn.lastTickPosX + (entityIn.posX - entityIn.lastTickPosX) * partialTicks;
        final double d2 = entityIn.lastTickPosY + (entityIn.posY - entityIn.lastTickPosY) * partialTicks;
        final double d3 = entityIn.lastTickPosZ + (entityIn.posZ - entityIn.lastTickPosZ) * partialTicks;
        final float f = entityIn.prevRotationYaw + (entityIn.rotationYaw - entityIn.prevRotationYaw) * partialTicks;
        int i = entityIn.getBrightnessForRender();
        if (entityIn.isBurning()) {
            i = 15728880;
        }
        final int j = i % 65536;
        final int k = i / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)j, (float)k);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        final Render<Entity> render = this.getEntityRenderObject(entityIn);
        if (render != null && this.renderEngine != null) {
            render.renderMultipass(entityIn, d0 - this.renderPosX, d2 - this.renderPosY, d3 - this.renderPosZ, f, partialTicks);
        }
    }
    
    private void renderDebugBoundingBox(final Entity entityIn, final double x, final double y, final double z, final float entityYaw, final float partialTicks) {
        if (!Shaders.isShadowPass) {
            GlStateManager.depthMask(false);
            GlStateManager.disableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.disableCull();
            GlStateManager.disableBlend();
            final float f = entityIn.width / 2.0f;
            final AxisAlignedBB axisalignedbb = entityIn.getEntityBoundingBox();
            RenderGlobal.drawBoundingBox(axisalignedbb.minX - entityIn.posX + x, axisalignedbb.minY - entityIn.posY + y, axisalignedbb.minZ - entityIn.posZ + z, axisalignedbb.maxX - entityIn.posX + x, axisalignedbb.maxY - entityIn.posY + y, axisalignedbb.maxZ - entityIn.posZ + z, 1.0f, 1.0f, 1.0f, 1.0f);
            final Entity[] aentity = entityIn.getParts();
            if (aentity != null) {
                for (final Entity entity : aentity) {
                    final double d0 = (entity.posX - entity.prevPosX) * partialTicks;
                    final double d2 = (entity.posY - entity.prevPosY) * partialTicks;
                    final double d3 = (entity.posZ - entity.prevPosZ) * partialTicks;
                    final AxisAlignedBB axisalignedbb2 = entity.getEntityBoundingBox();
                    RenderGlobal.drawBoundingBox(axisalignedbb2.minX - this.renderPosX + d0, axisalignedbb2.minY - this.renderPosY + d2, axisalignedbb2.minZ - this.renderPosZ + d3, axisalignedbb2.maxX - this.renderPosX + d0, axisalignedbb2.maxY - this.renderPosY + d2, axisalignedbb2.maxZ - this.renderPosZ + d3, 0.25f, 1.0f, 0.0f, 1.0f);
                }
            }
            if (entityIn instanceof EntityLivingBase) {
                final float f2 = 0.01f;
                RenderGlobal.drawBoundingBox(x - f, y + entityIn.getEyeHeight() - 0.009999999776482582, z - f, x + f, y + entityIn.getEyeHeight() + 0.009999999776482582, z + f, 1.0f, 0.0f, 0.0f, 1.0f);
            }
            final Tessellator tessellator = Tessellator.getInstance();
            final BufferBuilder bufferbuilder = tessellator.getBuffer();
            final Vec3d vec3d = entityIn.getLook(partialTicks);
            bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
            bufferbuilder.pos(x, y + entityIn.getEyeHeight(), z).color(0, 0, 255, 255).endVertex();
            bufferbuilder.pos(x + vec3d.x * 2.0, y + entityIn.getEyeHeight() + vec3d.y * 2.0, z + vec3d.z * 2.0).color(0, 0, 255, 255).endVertex();
            tessellator.draw();
            GlStateManager.enableTexture2D();
            GlStateManager.enableLighting();
            GlStateManager.enableCull();
            GlStateManager.disableBlend();
            GlStateManager.depthMask(true);
        }
    }
    
    public void setWorld(@Nullable final World worldIn) {
        this.world = worldIn;
        if (worldIn == null) {
            this.renderViewEntity = null;
        }
    }
    
    public double getDistanceToCamera(final double x, final double y, final double z) {
        final double d0 = x - this.viewerPosX;
        final double d2 = y - this.viewerPosY;
        final double d3 = z - this.viewerPosZ;
        return d0 * d0 + d2 * d2 + d3 * d3;
    }
    
    public FontRenderer getFontRenderer() {
        return this.textRenderer;
    }
    
    public void setRenderOutlines(final boolean renderOutlinesIn) {
        this.renderOutlines = renderOutlinesIn;
    }
    
    public Map<Class, Render> getEntityRenderMap() {
        return this.entityRenderMap;
    }
    
    public Map<String, RenderPlayer> getSkinMap() {
        return Collections.unmodifiableMap((Map<? extends String, ? extends RenderPlayer>)this.skinMap);
    }
}
