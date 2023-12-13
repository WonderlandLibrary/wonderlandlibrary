package net.minecraft.client.particle;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Callable;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import optifine.Config;
import optifine.Reflector;

public class EffectRenderer {
   private Map particleTypes = Maps.newHashMap();
   private static final String __OBFID = "CL_00000915";
   private static final ResourceLocation particleTextures = new ResourceLocation("textures/particle/particles.png");
   private Random rand = new Random();
   private TextureManager renderer;
   private List[][] fxLayers = new List[4][];
   protected World worldObj;
   private List particleEmitters = Lists.newArrayList();

   public void registerParticle(int var1, IParticleFactory var2) {
      this.particleTypes.put(var1, var2);
   }

   private void registerVanillaParticles() {
      this.registerParticle(EnumParticleTypes.EXPLOSION_NORMAL.getParticleID(), new EntityExplodeFX.Factory());
      this.registerParticle(EnumParticleTypes.WATER_BUBBLE.getParticleID(), new EntityBubbleFX.Factory());
      this.registerParticle(EnumParticleTypes.WATER_SPLASH.getParticleID(), new EntitySplashFX.Factory());
      this.registerParticle(EnumParticleTypes.WATER_WAKE.getParticleID(), new EntityFishWakeFX.Factory());
      this.registerParticle(EnumParticleTypes.WATER_DROP.getParticleID(), new EntityRainFX.Factory());
      this.registerParticle(EnumParticleTypes.SUSPENDED.getParticleID(), new EntitySuspendFX.Factory());
      this.registerParticle(EnumParticleTypes.SUSPENDED_DEPTH.getParticleID(), new EntityAuraFX.Factory());
      this.registerParticle(EnumParticleTypes.CRIT.getParticleID(), new EntityCrit2FX.Factory());
      this.registerParticle(EnumParticleTypes.CRIT_MAGIC.getParticleID(), new EntityCrit2FX.MagicFactory());
      this.registerParticle(EnumParticleTypes.SMOKE_NORMAL.getParticleID(), new EntitySmokeFX.Factory());
      this.registerParticle(EnumParticleTypes.SMOKE_LARGE.getParticleID(), new EntityCritFX.Factory());
      this.registerParticle(EnumParticleTypes.SPELL.getParticleID(), new EntitySpellParticleFX.Factory());
      this.registerParticle(EnumParticleTypes.SPELL_INSTANT.getParticleID(), new EntitySpellParticleFX.InstantFactory());
      this.registerParticle(EnumParticleTypes.SPELL_MOB.getParticleID(), new EntitySpellParticleFX.MobFactory());
      this.registerParticle(EnumParticleTypes.SPELL_MOB_AMBIENT.getParticleID(), new EntitySpellParticleFX.AmbientMobFactory());
      this.registerParticle(EnumParticleTypes.SPELL_WITCH.getParticleID(), new EntitySpellParticleFX.WitchFactory());
      this.registerParticle(EnumParticleTypes.DRIP_WATER.getParticleID(), new EntityDropParticleFX.WaterFactory());
      this.registerParticle(EnumParticleTypes.DRIP_LAVA.getParticleID(), new EntityDropParticleFX.LavaFactory());
      this.registerParticle(EnumParticleTypes.VILLAGER_ANGRY.getParticleID(), new EntityHeartFX.AngryVillagerFactory());
      this.registerParticle(EnumParticleTypes.VILLAGER_HAPPY.getParticleID(), new EntityAuraFX.HappyVillagerFactory());
      this.registerParticle(EnumParticleTypes.TOWN_AURA.getParticleID(), new EntityAuraFX.Factory());
      this.registerParticle(EnumParticleTypes.NOTE.getParticleID(), new EntityNoteFX.Factory());
      this.registerParticle(EnumParticleTypes.PORTAL.getParticleID(), new EntityPortalFX.Factory());
      this.registerParticle(EnumParticleTypes.ENCHANTMENT_TABLE.getParticleID(), new EntityEnchantmentTableParticleFX.EnchantmentTable());
      this.registerParticle(EnumParticleTypes.FLAME.getParticleID(), new EntityFlameFX.Factory());
      this.registerParticle(EnumParticleTypes.LAVA.getParticleID(), new EntityLavaFX.Factory());
      this.registerParticle(EnumParticleTypes.FOOTSTEP.getParticleID(), new EntityFootStepFX.Factory());
      this.registerParticle(EnumParticleTypes.CLOUD.getParticleID(), new EntityCloudFX.Factory());
      this.registerParticle(EnumParticleTypes.REDSTONE.getParticleID(), new EntityReddustFX.Factory());
      this.registerParticle(EnumParticleTypes.SNOWBALL.getParticleID(), new EntityBreakingFX.SnowballFactory());
      this.registerParticle(EnumParticleTypes.SNOW_SHOVEL.getParticleID(), new EntitySnowShovelFX.Factory());
      this.registerParticle(EnumParticleTypes.SLIME.getParticleID(), new EntityBreakingFX.SlimeFactory());
      this.registerParticle(EnumParticleTypes.HEART.getParticleID(), new EntityHeartFX.Factory());
      this.registerParticle(EnumParticleTypes.BARRIER.getParticleID(), new Barrier.Factory());
      this.registerParticle(EnumParticleTypes.ITEM_CRACK.getParticleID(), new EntityBreakingFX.Factory());
      this.registerParticle(EnumParticleTypes.BLOCK_CRACK.getParticleID(), new EntityDiggingFX.Factory());
      this.registerParticle(EnumParticleTypes.BLOCK_DUST.getParticleID(), new EntityBlockDustFX.Factory());
      this.registerParticle(EnumParticleTypes.EXPLOSION_HUGE.getParticleID(), new EntityHugeExplodeFX.Factory());
      this.registerParticle(EnumParticleTypes.EXPLOSION_LARGE.getParticleID(), new EntityLargeExplodeFX.Factory());
      this.registerParticle(EnumParticleTypes.FIREWORKS_SPARK.getParticleID(), new EntityFirework.Factory());
      this.registerParticle(EnumParticleTypes.MOB_APPEARANCE.getParticleID(), new MobAppearance.Factory());
   }

   private void moveToLayer(EntityFX var1, int var2, int var3) {
      for(int var4 = 0; var4 < 4; ++var4) {
         if (this.fxLayers[var4][var2].contains(var1)) {
            this.fxLayers[var4][var2].remove(var1);
            this.fxLayers[var4][var3].add(var1);
         }
      }

   }

   public void moveToNoAlphaLayer(EntityFX var1) {
      this.moveToLayer(var1, 0, 1);
   }

   public EffectRenderer(World var1, TextureManager var2) {
      this.worldObj = var1;
      this.renderer = var2;

      for(int var3 = 0; var3 < 4; ++var3) {
         this.fxLayers[var3] = new List[2];

         for(int var4 = 0; var4 < 2; ++var4) {
            this.fxLayers[var3][var4] = Lists.newArrayList();
         }
      }

      this.registerVanillaParticles();
   }

   public void renderParticles(Entity var1, float var2) {
      float var3 = ActiveRenderInfo.getRotationX();
      float var4 = ActiveRenderInfo.getRotationZ();
      float var5 = ActiveRenderInfo.getRotationYZ();
      float var6 = ActiveRenderInfo.getRotationXY();
      float var7 = ActiveRenderInfo.getRotationXZ();
      EntityFX.interpPosX = var1.lastTickPosX + (var1.posX - var1.lastTickPosX) * (double)var2;
      EntityFX.interpPosY = var1.lastTickPosY + (var1.posY - var1.lastTickPosY) * (double)var2;
      EntityFX.interpPosZ = var1.lastTickPosZ + (var1.posZ - var1.lastTickPosZ) * (double)var2;
      GlStateManager.enableBlend();
      GlStateManager.blendFunc(770, 771);
      GlStateManager.alphaFunc(516, 0.003921569F);

      for(int var8 = 0; var8 < 3; ++var8) {
         int var9 = var8;

         for(int var10 = 0; var10 < 2; ++var10) {
            if (!this.fxLayers[var9][var10].isEmpty()) {
               switch(var10) {
               case 0:
                  GlStateManager.depthMask(false);
                  break;
               case 1:
                  GlStateManager.depthMask(true);
               }

               switch(var9) {
               case 0:
               default:
                  this.renderer.bindTexture(particleTextures);
                  break;
               case 1:
                  this.renderer.bindTexture(TextureMap.locationBlocksTexture);
               }

               GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
               Tessellator var11 = Tessellator.getInstance();
               WorldRenderer var12 = var11.getWorldRenderer();
               var12.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);

               for(int var13 = 0; var13 < this.fxLayers[var9][var10].size(); ++var13) {
                  EntityFX var14 = (EntityFX)this.fxLayers[var9][var10].get(var13);

                  try {
                     var14.renderParticle(var12, var1, var2, var3, var7, var4, var5, var6);
                  } catch (Throwable var18) {
                     CrashReport var16 = CrashReport.makeCrashReport(var18, "Rendering Particle");
                     CrashReportCategory var17 = var16.makeCategory("Particle being rendered");
                     var17.addCrashSectionCallable("Particle", new Callable(this, var14) {
                        private static final String __OBFID = "CL_00000918";
                        private final EntityFX val$entityfx;
                        final EffectRenderer this$0;

                        public Object call() throws Exception {
                           return this.call();
                        }

                        {
                           this.this$0 = var1;
                           this.val$entityfx = var2;
                        }

                        public String call() throws Exception {
                           return this.val$entityfx.toString();
                        }
                     });
                     var17.addCrashSectionCallable("Particle Type", new Callable(this, var9) {
                        private final int val$j;
                        private static final String __OBFID = "CL_00000919";
                        final EffectRenderer this$0;

                        public Object call() throws Exception {
                           return this.call();
                        }

                        public String call() throws Exception {
                           return this.val$j == 0 ? "MISC_TEXTURE" : (this.val$j == 1 ? "TERRAIN_TEXTURE" : (this.val$j == 3 ? "ENTITY_PARTICLE_TEXTURE" : "Unknown - " + this.val$j));
                        }

                        {
                           this.this$0 = var1;
                           this.val$j = var2;
                        }
                     });
                     throw new ReportedException(var16);
                  }
               }

               var11.draw();
            }
         }
      }

      GlStateManager.depthMask(true);
      GlStateManager.disableBlend();
      GlStateManager.alphaFunc(516, 0.1F);
   }

   private void updateEffectAlphaLayer(List var1) {
      ArrayList var2 = Lists.newArrayList();

      for(int var3 = 0; var3 < var1.size(); ++var3) {
         EntityFX var4 = (EntityFX)var1.get(var3);
         this.tickParticle(var4);
         if (var4.isDead) {
            var2.add(var4);
         }
      }

      var1.removeAll(var2);
   }

   public void updateEffects() {
      for(int var1 = 0; var1 < 4; ++var1) {
         this.updateEffectLayer(var1);
      }

      ArrayList var5 = Lists.newArrayList();
      Iterator var3 = this.particleEmitters.iterator();

      while(var3.hasNext()) {
         Object var2 = var3.next();
         EntityParticleEmitter var4 = (EntityParticleEmitter)var2;
         var4.onUpdate();
         if (var4.isDead) {
            var5.add(var4);
         }
      }

      this.particleEmitters.removeAll(var5);
   }

   private void updateEffectLayer(int var1) {
      for(int var2 = 0; var2 < 2; ++var2) {
         this.updateEffectAlphaLayer(this.fxLayers[var1][var2]);
      }

   }

   public void addBlockHitEffects(BlockPos var1, EnumFacing var2) {
      IBlockState var3 = this.worldObj.getBlockState(var1);
      Block var4 = var3.getBlock();
      if (var4.getRenderType() != -1) {
         int var5 = var1.getX();
         int var6 = var1.getY();
         int var7 = var1.getZ();
         float var8 = 0.1F;
         double var9 = (double)var5 + this.rand.nextDouble() * (var4.getBlockBoundsMaxX() - var4.getBlockBoundsMinX() - (double)(var8 * 2.0F)) + (double)var8 + var4.getBlockBoundsMinX();
         double var11 = (double)var6 + this.rand.nextDouble() * (var4.getBlockBoundsMaxY() - var4.getBlockBoundsMinY() - (double)(var8 * 2.0F)) + (double)var8 + var4.getBlockBoundsMinY();
         double var13 = (double)var7 + this.rand.nextDouble() * (var4.getBlockBoundsMaxZ() - var4.getBlockBoundsMinZ() - (double)(var8 * 2.0F)) + (double)var8 + var4.getBlockBoundsMinZ();
         if (var2 == EnumFacing.DOWN) {
            var11 = (double)var6 + var4.getBlockBoundsMinY() - (double)var8;
         }

         if (var2 == EnumFacing.UP) {
            var11 = (double)var6 + var4.getBlockBoundsMaxY() + (double)var8;
         }

         if (var2 == EnumFacing.NORTH) {
            var13 = (double)var7 + var4.getBlockBoundsMinZ() - (double)var8;
         }

         if (var2 == EnumFacing.SOUTH) {
            var13 = (double)var7 + var4.getBlockBoundsMaxZ() + (double)var8;
         }

         if (var2 == EnumFacing.WEST) {
            var9 = (double)var5 + var4.getBlockBoundsMinX() - (double)var8;
         }

         if (var2 == EnumFacing.EAST) {
            var9 = (double)var5 + var4.getBlockBoundsMaxX() + (double)var8;
         }

         this.addEffect((new EntityDiggingFX(this.worldObj, var9, var11, var13, 0.0D, 0.0D, 0.0D, var3)).func_174846_a(var1).multiplyVelocity(0.2F).multipleParticleScaleBy(0.6F));
      }

   }

   public void renderLitParticles(Entity var1, float var2) {
      float var3 = 0.017453292F;
      float var4 = MathHelper.cos(var1.rotationYaw * 0.017453292F);
      float var5 = MathHelper.sin(var1.rotationYaw * 0.017453292F);
      float var6 = -var5 * MathHelper.sin(var1.rotationPitch * 0.017453292F);
      float var7 = var4 * MathHelper.sin(var1.rotationPitch * 0.017453292F);
      float var8 = MathHelper.cos(var1.rotationPitch * 0.017453292F);

      for(int var9 = 0; var9 < 2; ++var9) {
         List var10 = this.fxLayers[3][var9];
         if (!var10.isEmpty()) {
            Tessellator var11 = Tessellator.getInstance();
            WorldRenderer var12 = var11.getWorldRenderer();

            for(int var13 = 0; var13 < var10.size(); ++var13) {
               EntityFX var14 = (EntityFX)var10.get(var13);
               var14.renderParticle(var12, var1, var2, var4, var8, var5, var6, var7);
            }
         }
      }

   }

   public void addBlockDestroyEffects(BlockPos var1, IBlockState var2) {
      boolean var3;
      if (Reflector.ForgeBlock_addDestroyEffects.exists() && Reflector.ForgeBlock_isAir.exists()) {
         Block var4 = var2.getBlock();
         Reflector.callBoolean(var4, Reflector.ForgeBlock_isAir, this.worldObj, var1);
         var3 = !Reflector.callBoolean(var4, Reflector.ForgeBlock_isAir, this.worldObj, var1) && !Reflector.callBoolean(var4, Reflector.ForgeBlock_addDestroyEffects, this.worldObj, var1, this);
      } else {
         var3 = var2.getBlock().getMaterial() != Material.air;
      }

      if (var3) {
         var2 = var2.getBlock().getActualState(var2, this.worldObj, var1);
         byte var14 = 4;

         for(int var5 = 0; var5 < var14; ++var5) {
            for(int var6 = 0; var6 < var14; ++var6) {
               for(int var7 = 0; var7 < var14; ++var7) {
                  double var8 = (double)var1.getX() + ((double)var5 + 0.5D) / (double)var14;
                  double var10 = (double)var1.getY() + ((double)var6 + 0.5D) / (double)var14;
                  double var12 = (double)var1.getZ() + ((double)var7 + 0.5D) / (double)var14;
                  this.addEffect((new EntityDiggingFX(this.worldObj, var8, var10, var12, var8 - (double)var1.getX() - 0.5D, var10 - (double)var1.getY() - 0.5D, var12 - (double)var1.getZ() - 0.5D, var2)).func_174846_a(var1));
               }
            }
         }
      }

   }

   public void emitParticleAtEntity(Entity var1, EnumParticleTypes var2) {
      this.particleEmitters.add(new EntityParticleEmitter(this.worldObj, var1, var2));
   }

   public void clearEffects(World var1) {
      this.worldObj = var1;

      for(int var2 = 0; var2 < 4; ++var2) {
         for(int var3 = 0; var3 < 2; ++var3) {
            this.fxLayers[var2][var3].clear();
         }
      }

      this.particleEmitters.clear();
   }

   public void addEffect(EntityFX var1) {
      if (var1 != null && (!(var1 instanceof EntityFirework.SparkFX) || Config.isFireworkParticles())) {
         int var2 = var1.getFXLayer();
         int var3 = var1.getAlpha() != 1.0F ? 0 : 1;
         if (this.fxLayers[var2][var3].size() >= 4000) {
            this.fxLayers[var2][var3].remove(0);
         }

         if (!(var1 instanceof Barrier) || this.fxLayers[var2][var3] != false) {
            this.fxLayers[var2][var3].add(var1);
         }
      }

   }

   public void addBlockHitEffects(BlockPos var1, MovingObjectPosition var2) {
      Block var3 = this.worldObj.getBlockState(var1).getBlock();
      boolean var4 = Reflector.callBoolean(var3, Reflector.ForgeBlock_addHitEffects, this.worldObj, var2, this);
      if (var3 != null && !var4) {
         this.addBlockHitEffects(var1, var2.sideHit);
      }

   }

   public void moveToAlphaLayer(EntityFX var1) {
      this.moveToLayer(var1, 1, 0);
   }

   private void tickParticle(EntityFX var1) {
      try {
         var1.onUpdate();
      } catch (Throwable var7) {
         CrashReport var3 = CrashReport.makeCrashReport(var7, "Ticking Particle");
         CrashReportCategory var4 = var3.makeCategory("Particle being ticked");
         int var5 = var1.getFXLayer();
         var4.addCrashSectionCallable("Particle", new Callable(this, var1) {
            final EffectRenderer this$0;
            private final EntityFX val$p_178923_1_;
            private static final String __OBFID = "CL_00000916";

            {
               this.this$0 = var1;
               this.val$p_178923_1_ = var2;
            }

            public Object call() throws Exception {
               return this.call();
            }

            public String call() throws Exception {
               return this.val$p_178923_1_.toString();
            }
         });
         var4.addCrashSectionCallable("Particle Type", new Callable(this, var5) {
            private static final String __OBFID = "CL_00000917";
            private final int val$i;
            final EffectRenderer this$0;

            public Object call() throws Exception {
               return this.call();
            }

            {
               this.this$0 = var1;
               this.val$i = var2;
            }

            public String call() throws Exception {
               return this.val$i == 0 ? "MISC_TEXTURE" : (this.val$i == 1 ? "TERRAIN_TEXTURE" : (this.val$i == 3 ? "ENTITY_PARTICLE_TEXTURE" : "Unknown - " + this.val$i));
            }
         });
         throw new ReportedException(var3);
      }
   }

   public String getStatistics() {
      int var1 = 0;

      for(int var2 = 0; var2 < 4; ++var2) {
         for(int var3 = 0; var3 < 2; ++var3) {
            var1 += this.fxLayers[var2][var3].size();
         }
      }

      return "" + var1;
   }

   public EntityFX spawnEffectParticle(int var1, double var2, double var4, double var6, double var8, double var10, double var12, int... var14) {
      IParticleFactory var15 = (IParticleFactory)this.particleTypes.get(var1);
      if (var15 != null) {
         EntityFX var16 = var15.getEntityFX(var1, this.worldObj, var2, var4, var6, var8, var10, var12, var14);
         if (var16 != null) {
            this.addEffect(var16);
            return var16;
         }
      }

      return null;
   }
}
