package net.minecraft.client.renderer.entity;

import java.awt.Color;
import java.util.List;
import java.util.concurrent.Callable;
import my.NewSnake.Tank.module.modules.PLAYER.GlintColor;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockHugeMushroom;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockPrismarine;
import net.minecraft.block.BlockQuartz;
import net.minecraft.block.BlockRedSandstone;
import net.minecraft.block.BlockSand;
import net.minecraft.block.BlockSandStone;
import net.minecraft.block.BlockSilverfish;
import net.minecraft.block.BlockStone;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.block.BlockStoneSlab;
import net.minecraft.block.BlockStoneSlabNew;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.BlockWall;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererChestHelper;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFishFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3i;
import optifine.Config;
import optifine.CustomColors;
import optifine.CustomItems;
import optifine.Reflector;
import shadersmod.client.Shaders;
import shadersmod.client.ShadersRender;

public class RenderItem implements IResourceManagerReloadListener {
   public ModelManager modelManager = null;
   private ModelResourceLocation modelLocation = null;
   private static final String __OBFID = "CL_00001003";
   private final ItemModelMesher itemModelMesher;
   public float zLevel;
   private final TextureManager textureManager;
   private boolean field_175058_l = true;
   private boolean renderItemGui = false;
   private static final ResourceLocation RES_ITEM_GLINT = new ResourceLocation("textures/misc/enchanted_item_glint.png");

   private void registerBlock(Block var1, String var2) {
      this.registerBlock(var1, 0, var2);
   }

   private void renderEffect(IBakedModel var1) {
      if ((!Config.isCustomItems() || CustomItems.isUseGlint()) && (!Config.isShaders() || !Shaders.isShadowPass)) {
         GlStateManager.depthMask(false);
         GlStateManager.depthFunc(514);
         GlStateManager.disableLighting();
         GlStateManager.blendFunc(768, 1);
         this.textureManager.bindTexture(RES_ITEM_GLINT);
         if (Config.isShaders() && !this.renderItemGui) {
            ShadersRender.renderEnchantedGlintBegin();
         }

         GlStateManager.matrixMode(5890);
         GlStateManager.pushMatrix();
         GlStateManager.scale(8.0F, 8.0F, 8.0F);
         float var2 = (float)(Minecraft.getSystemTime() % 3000L) / 3000.0F / 8.0F;
         GlStateManager.translate(var2, 0.0F, 0.0F);
         GlStateManager.rotate(-50.0F, 0.0F, 0.0F, 1.0F);
         int var3;
         if ((new GlintColor()).getInstance().isEnabled()) {
            if (GlintColor.Rainbow) {
               var3 = GlintColor.getRainbow(6000, -15);
            } else {
               var3 = Color.HSBtoRGB(0.0F, 0.0F, 1.0F);
            }
         } else {
            var3 = -8372020;
         }

         this.renderModel(var1, var3);
         GlStateManager.popMatrix();
         GlStateManager.pushMatrix();
         GlStateManager.scale(8.0F, 8.0F, 8.0F);
         float var4 = (float)(Minecraft.getSystemTime() % 4873L) / 4873.0F / 8.0F;
         GlStateManager.translate(-var4, 0.0F, 0.0F);
         GlStateManager.rotate(10.0F, 0.0F, 0.0F, 1.0F);
         this.renderModel(var1, var3);
         GlStateManager.popMatrix();
         GlStateManager.matrixMode(5888);
         GlStateManager.blendFunc(770, 771);
         GlStateManager.enableLighting();
         GlStateManager.depthFunc(515);
         GlStateManager.depthMask(true);
         this.textureManager.bindTexture(TextureMap.locationBlocksTexture);
         if (Config.isShaders() && !this.renderItemGui) {
            ShadersRender.renderEnchantedGlintEnd();
         }
      }

   }

   public ItemModelMesher getItemModelMesher() {
      return this.itemModelMesher;
   }

   public void renderItemAndEffectIntoGUI(ItemStack var1, int var2, int var3) {
      if (var1 != null && var1.getItem() != null) {
         this.zLevel += 50.0F;

         try {
            this.renderItemIntoGUI(var1, var2, var3);
         } catch (Throwable var7) {
            CrashReport var5 = CrashReport.makeCrashReport(var7, "Rendering item");
            CrashReportCategory var6 = var5.makeCategory("Item being rendered");
            var6.addCrashSectionCallable("Item Type", new Callable(this, var1) {
               private final ItemStack val$stack;
               private static final String __OBFID = "CL_00001004";
               final RenderItem this$0;

               public String call() throws Exception {
                  return String.valueOf(this.val$stack.getItem());
               }

               public Object call() throws Exception {
                  return this.call();
               }

               {
                  this.this$0 = var1;
                  this.val$stack = var2;
               }
            });
            var6.addCrashSectionCallable("Item Aux", new Callable(this, var1) {
               private static final String __OBFID = "CL_00001005";
               final RenderItem this$0;
               private final ItemStack val$stack;

               public String call() throws Exception {
                  return String.valueOf(this.val$stack.getMetadata());
               }

               public Object call() throws Exception {
                  return this.call();
               }

               {
                  this.this$0 = var1;
                  this.val$stack = var2;
               }
            });
            var6.addCrashSectionCallable("Item NBT", new Callable(this, var1) {
               private final ItemStack val$stack;
               final RenderItem this$0;
               private static final String __OBFID = "CL_00001006";

               {
                  this.this$0 = var1;
                  this.val$stack = var2;
               }

               public String call() throws Exception {
                  return String.valueOf(this.val$stack.getTagCompound());
               }

               public Object call() throws Exception {
                  return this.call();
               }
            });
            var6.addCrashSectionCallable("Item Foil", new Callable(this, var1) {
               final RenderItem this$0;
               private final ItemStack val$stack;
               private static final String __OBFID = "CL_00001007";

               public Object call() throws Exception {
                  return this.call();
               }

               public String call() throws Exception {
                  return String.valueOf(this.val$stack.hasEffect());
               }

               {
                  this.this$0 = var1;
                  this.val$stack = var2;
               }
            });
            throw new ReportedException(var5);
         }

         this.zLevel -= 50.0F;
      }

   }

   public static void forgeHooksClient_putQuadColor(WorldRenderer var0, BakedQuad var1, int var2) {
      float var3 = (float)(var2 & 255);
      float var4 = (float)(var2 >>> 8 & 255);
      float var5 = (float)(var2 >>> 16 & 255);
      float var6 = (float)(var2 >>> 24 & 255);
      int[] var7 = var1.getVertexData();
      int var8 = var7.length / 4;

      for(int var9 = 0; var9 < 4; ++var9) {
         int var10 = var7[3 + var8 * var9];
         float var11 = (float)(var10 & 255);
         float var12 = (float)(var10 >>> 8 & 255);
         float var13 = (float)(var10 >>> 16 & 255);
         float var14 = (float)(var10 >>> 24 & 255);
         int var15 = Math.min(255, (int)(var3 * var11 / 255.0F));
         int var16 = Math.min(255, (int)(var4 * var12 / 255.0F));
         int var17 = Math.min(255, (int)(var5 * var13 / 255.0F));
         int var18 = Math.min(255, (int)(var6 * var14 / 255.0F));
         var0.putColorRGBA(var0.getColorIndex(4 - var9), var15, var16, var17, var18);
      }

   }

   private void renderModel(IBakedModel var1, int var2, ItemStack var3) {
      Tessellator var4 = Tessellator.getInstance();
      WorldRenderer var5 = var4.getWorldRenderer();
      boolean var6 = Minecraft.getMinecraft().getTextureMapBlocks().isTextureBound();
      boolean var7 = Config.isMultiTexture() && var6;
      if (var7) {
         var5.setBlockLayer(EnumWorldBlockLayer.SOLID);
      }

      var5.begin(7, DefaultVertexFormats.ITEM);
      EnumFacing[] var11;
      int var10 = (var11 = EnumFacing.VALUES).length;

      for(int var9 = 0; var9 < var10; ++var9) {
         EnumFacing var8 = var11[var9];
         this.renderQuads(var5, var1.getFaceQuads(var8), var2, var3);
      }

      this.renderQuads(var5, var1.getGeneralQuads(), var2, var3);
      var4.draw();
      if (var7) {
         var5.setBlockLayer((EnumWorldBlockLayer)null);
         GlStateManager.bindCurrentTexture();
      }

   }

   private void renderModel(IBakedModel var1, ItemStack var2) {
      this.renderModel(var1, -1, var2);
   }

   private void registerItem(Item var1, String var2) {
      this.registerItem(var1, 0, var2);
   }

   private void renderQuads(WorldRenderer var1, List var2, int var3, ItemStack var4) {
      boolean var5 = var3 == -1 && var4 != null;
      int var6 = 0;

      for(int var7 = var2.size(); var6 < var7; ++var6) {
         BakedQuad var8 = (BakedQuad)var2.get(var6);
         int var9 = var3;
         if (var5 && var8.hasTintIndex()) {
            var9 = var4.getItem().getColorFromItemStack(var4, var8.getTintIndex());
            if (Config.isCustomColors()) {
               var9 = CustomColors.getColorFromItemStack(var4, var8.getTintIndex(), var9);
            }

            if (EntityRenderer.anaglyphEnable) {
               var9 = TextureUtil.anaglyphColor(var9);
            }

            var9 |= -16777216;
         }

         this.renderQuad(var1, var8, var9);
      }

   }

   public void renderModel(IBakedModel var1, int var2) {
      this.renderModel(var1, var2, (ItemStack)null);
   }

   protected void registerItem(Item var1, int var2, String var3) {
      this.itemModelMesher.register(var1, var2, new ModelResourceLocation(var3, "inventory"));
   }

   public RenderItem(TextureManager var1, ModelManager var2) {
      this.textureManager = var1;
      this.modelManager = var2;
      if (Reflector.ItemModelMesherForge_Constructor.exists()) {
         this.itemModelMesher = (ItemModelMesher)Reflector.newInstance(Reflector.ItemModelMesherForge_Constructor, var2);
      } else {
         this.itemModelMesher = new ItemModelMesher(var2);
      }

      this.registerItems();
   }

   public void renderItem(ItemStack var1, IBakedModel var2) {
      if (var1 != null) {
         GlStateManager.pushMatrix();
         GlStateManager.scale(0.5F, 0.5F, 0.5F);
         if (var2.isBuiltInRenderer()) {
            GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.translate(-0.5F, -0.5F, -0.5F);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.enableRescaleNormal();
            TileEntityItemStackRenderer.instance.renderByItem(var1);
         } else {
            if (Config.isCustomItems()) {
               var2 = CustomItems.getCustomItemModel(var1, var2, this.modelLocation);
            }

            GlStateManager.translate(-0.5F, -0.5F, -0.5F);
            this.renderModel(var2, var1);
            if (var1.hasEffect() && (!Config.isCustomItems() || !CustomItems.renderCustomEffect(this, var1, var2))) {
               this.renderEffect(var2);
            }
         }

         GlStateManager.popMatrix();
      }

   }

   public void renderItemOverlays(FontRenderer var1, ItemStack var2, int var3, int var4) {
      this.renderItemOverlayIntoGUI(var1, var2, var3, var4, (String)null);
   }

   public void onResourceManagerReload(IResourceManager var1) {
      this.itemModelMesher.rebuildCache();
   }

   private void registerItems() {
      this.registerBlock(Blocks.anvil, "anvil_intact");
      this.registerBlock(Blocks.anvil, 1, "anvil_slightly_damaged");
      this.registerBlock(Blocks.anvil, 2, "anvil_very_damaged");
      this.registerBlock(Blocks.carpet, EnumDyeColor.BLACK.getMetadata(), "black_carpet");
      this.registerBlock(Blocks.carpet, EnumDyeColor.BLUE.getMetadata(), "blue_carpet");
      this.registerBlock(Blocks.carpet, EnumDyeColor.BROWN.getMetadata(), "brown_carpet");
      this.registerBlock(Blocks.carpet, EnumDyeColor.CYAN.getMetadata(), "cyan_carpet");
      this.registerBlock(Blocks.carpet, EnumDyeColor.GRAY.getMetadata(), "gray_carpet");
      this.registerBlock(Blocks.carpet, EnumDyeColor.GREEN.getMetadata(), "green_carpet");
      this.registerBlock(Blocks.carpet, EnumDyeColor.LIGHT_BLUE.getMetadata(), "light_blue_carpet");
      this.registerBlock(Blocks.carpet, EnumDyeColor.LIME.getMetadata(), "lime_carpet");
      this.registerBlock(Blocks.carpet, EnumDyeColor.MAGENTA.getMetadata(), "magenta_carpet");
      this.registerBlock(Blocks.carpet, EnumDyeColor.ORANGE.getMetadata(), "orange_carpet");
      this.registerBlock(Blocks.carpet, EnumDyeColor.PINK.getMetadata(), "pink_carpet");
      this.registerBlock(Blocks.carpet, EnumDyeColor.PURPLE.getMetadata(), "purple_carpet");
      this.registerBlock(Blocks.carpet, EnumDyeColor.RED.getMetadata(), "red_carpet");
      this.registerBlock(Blocks.carpet, EnumDyeColor.SILVER.getMetadata(), "silver_carpet");
      this.registerBlock(Blocks.carpet, EnumDyeColor.WHITE.getMetadata(), "white_carpet");
      this.registerBlock(Blocks.carpet, EnumDyeColor.YELLOW.getMetadata(), "yellow_carpet");
      this.registerBlock(Blocks.cobblestone_wall, BlockWall.EnumType.MOSSY.getMetadata(), "mossy_cobblestone_wall");
      this.registerBlock(Blocks.cobblestone_wall, BlockWall.EnumType.NORMAL.getMetadata(), "cobblestone_wall");
      this.registerBlock(Blocks.dirt, BlockDirt.DirtType.COARSE_DIRT.getMetadata(), "coarse_dirt");
      this.registerBlock(Blocks.dirt, BlockDirt.DirtType.DIRT.getMetadata(), "dirt");
      this.registerBlock(Blocks.dirt, BlockDirt.DirtType.PODZOL.getMetadata(), "podzol");
      this.registerBlock(Blocks.double_plant, BlockDoublePlant.EnumPlantType.FERN.getMeta(), "double_fern");
      this.registerBlock(Blocks.double_plant, BlockDoublePlant.EnumPlantType.GRASS.getMeta(), "double_grass");
      this.registerBlock(Blocks.double_plant, BlockDoublePlant.EnumPlantType.PAEONIA.getMeta(), "paeonia");
      this.registerBlock(Blocks.double_plant, BlockDoublePlant.EnumPlantType.ROSE.getMeta(), "double_rose");
      this.registerBlock(Blocks.double_plant, BlockDoublePlant.EnumPlantType.SUNFLOWER.getMeta(), "sunflower");
      this.registerBlock(Blocks.double_plant, BlockDoublePlant.EnumPlantType.SYRINGA.getMeta(), "syringa");
      this.registerBlock(Blocks.leaves, BlockPlanks.EnumType.BIRCH.getMetadata(), "birch_leaves");
      this.registerBlock(Blocks.leaves, BlockPlanks.EnumType.JUNGLE.getMetadata(), "jungle_leaves");
      this.registerBlock(Blocks.leaves, BlockPlanks.EnumType.OAK.getMetadata(), "oak_leaves");
      this.registerBlock(Blocks.leaves, BlockPlanks.EnumType.SPRUCE.getMetadata(), "spruce_leaves");
      this.registerBlock(Blocks.leaves2, BlockPlanks.EnumType.ACACIA.getMetadata() - 4, "acacia_leaves");
      this.registerBlock(Blocks.leaves2, BlockPlanks.EnumType.DARK_OAK.getMetadata() - 4, "dark_oak_leaves");
      this.registerBlock(Blocks.log, BlockPlanks.EnumType.BIRCH.getMetadata(), "birch_log");
      this.registerBlock(Blocks.log, BlockPlanks.EnumType.JUNGLE.getMetadata(), "jungle_log");
      this.registerBlock(Blocks.log, BlockPlanks.EnumType.OAK.getMetadata(), "oak_log");
      this.registerBlock(Blocks.log, BlockPlanks.EnumType.SPRUCE.getMetadata(), "spruce_log");
      this.registerBlock(Blocks.log2, BlockPlanks.EnumType.ACACIA.getMetadata() - 4, "acacia_log");
      this.registerBlock(Blocks.log2, BlockPlanks.EnumType.DARK_OAK.getMetadata() - 4, "dark_oak_log");
      this.registerBlock(Blocks.monster_egg, BlockSilverfish.EnumType.CHISELED_STONEBRICK.getMetadata(), "chiseled_brick_monster_egg");
      this.registerBlock(Blocks.monster_egg, BlockSilverfish.EnumType.COBBLESTONE.getMetadata(), "cobblestone_monster_egg");
      this.registerBlock(Blocks.monster_egg, BlockSilverfish.EnumType.CRACKED_STONEBRICK.getMetadata(), "cracked_brick_monster_egg");
      this.registerBlock(Blocks.monster_egg, BlockSilverfish.EnumType.MOSSY_STONEBRICK.getMetadata(), "mossy_brick_monster_egg");
      this.registerBlock(Blocks.monster_egg, BlockSilverfish.EnumType.STONE.getMetadata(), "stone_monster_egg");
      this.registerBlock(Blocks.monster_egg, BlockSilverfish.EnumType.STONEBRICK.getMetadata(), "stone_brick_monster_egg");
      this.registerBlock(Blocks.planks, BlockPlanks.EnumType.ACACIA.getMetadata(), "acacia_planks");
      this.registerBlock(Blocks.planks, BlockPlanks.EnumType.BIRCH.getMetadata(), "birch_planks");
      this.registerBlock(Blocks.planks, BlockPlanks.EnumType.DARK_OAK.getMetadata(), "dark_oak_planks");
      this.registerBlock(Blocks.planks, BlockPlanks.EnumType.JUNGLE.getMetadata(), "jungle_planks");
      this.registerBlock(Blocks.planks, BlockPlanks.EnumType.OAK.getMetadata(), "oak_planks");
      this.registerBlock(Blocks.planks, BlockPlanks.EnumType.SPRUCE.getMetadata(), "spruce_planks");
      this.registerBlock(Blocks.prismarine, BlockPrismarine.EnumType.BRICKS.getMetadata(), "prismarine_bricks");
      this.registerBlock(Blocks.prismarine, BlockPrismarine.EnumType.DARK.getMetadata(), "dark_prismarine");
      this.registerBlock(Blocks.prismarine, BlockPrismarine.EnumType.ROUGH.getMetadata(), "prismarine");
      this.registerBlock(Blocks.quartz_block, BlockQuartz.EnumType.CHISELED.getMetadata(), "chiseled_quartz_block");
      this.registerBlock(Blocks.quartz_block, BlockQuartz.EnumType.DEFAULT.getMetadata(), "quartz_block");
      this.registerBlock(Blocks.quartz_block, BlockQuartz.EnumType.LINES_Y.getMetadata(), "quartz_column");
      this.registerBlock(Blocks.red_flower, BlockFlower.EnumFlowerType.ALLIUM.getMeta(), "allium");
      this.registerBlock(Blocks.red_flower, BlockFlower.EnumFlowerType.BLUE_ORCHID.getMeta(), "blue_orchid");
      this.registerBlock(Blocks.red_flower, BlockFlower.EnumFlowerType.HOUSTONIA.getMeta(), "houstonia");
      this.registerBlock(Blocks.red_flower, BlockFlower.EnumFlowerType.ORANGE_TULIP.getMeta(), "orange_tulip");
      this.registerBlock(Blocks.red_flower, BlockFlower.EnumFlowerType.OXEYE_DAISY.getMeta(), "oxeye_daisy");
      this.registerBlock(Blocks.red_flower, BlockFlower.EnumFlowerType.PINK_TULIP.getMeta(), "pink_tulip");
      this.registerBlock(Blocks.red_flower, BlockFlower.EnumFlowerType.POPPY.getMeta(), "poppy");
      this.registerBlock(Blocks.red_flower, BlockFlower.EnumFlowerType.RED_TULIP.getMeta(), "red_tulip");
      this.registerBlock(Blocks.red_flower, BlockFlower.EnumFlowerType.WHITE_TULIP.getMeta(), "white_tulip");
      this.registerBlock(Blocks.sand, BlockSand.EnumType.RED_SAND.getMetadata(), "red_sand");
      this.registerBlock(Blocks.sand, BlockSand.EnumType.SAND.getMetadata(), "sand");
      this.registerBlock(Blocks.sandstone, BlockSandStone.EnumType.CHISELED.getMetadata(), "chiseled_sandstone");
      this.registerBlock(Blocks.sandstone, BlockSandStone.EnumType.DEFAULT.getMetadata(), "sandstone");
      this.registerBlock(Blocks.sandstone, BlockSandStone.EnumType.SMOOTH.getMetadata(), "smooth_sandstone");
      this.registerBlock(Blocks.red_sandstone, BlockRedSandstone.EnumType.CHISELED.getMetadata(), "chiseled_red_sandstone");
      this.registerBlock(Blocks.red_sandstone, BlockRedSandstone.EnumType.DEFAULT.getMetadata(), "red_sandstone");
      this.registerBlock(Blocks.red_sandstone, BlockRedSandstone.EnumType.SMOOTH.getMetadata(), "smooth_red_sandstone");
      this.registerBlock(Blocks.sapling, BlockPlanks.EnumType.ACACIA.getMetadata(), "acacia_sapling");
      this.registerBlock(Blocks.sapling, BlockPlanks.EnumType.BIRCH.getMetadata(), "birch_sapling");
      this.registerBlock(Blocks.sapling, BlockPlanks.EnumType.DARK_OAK.getMetadata(), "dark_oak_sapling");
      this.registerBlock(Blocks.sapling, BlockPlanks.EnumType.JUNGLE.getMetadata(), "jungle_sapling");
      this.registerBlock(Blocks.sapling, BlockPlanks.EnumType.OAK.getMetadata(), "oak_sapling");
      this.registerBlock(Blocks.sapling, BlockPlanks.EnumType.SPRUCE.getMetadata(), "spruce_sapling");
      this.registerBlock(Blocks.sponge, 0, "sponge");
      this.registerBlock(Blocks.sponge, 1, "sponge_wet");
      this.registerBlock(Blocks.stained_glass, EnumDyeColor.BLACK.getMetadata(), "black_stained_glass");
      this.registerBlock(Blocks.stained_glass, EnumDyeColor.BLUE.getMetadata(), "blue_stained_glass");
      this.registerBlock(Blocks.stained_glass, EnumDyeColor.BROWN.getMetadata(), "brown_stained_glass");
      this.registerBlock(Blocks.stained_glass, EnumDyeColor.CYAN.getMetadata(), "cyan_stained_glass");
      this.registerBlock(Blocks.stained_glass, EnumDyeColor.GRAY.getMetadata(), "gray_stained_glass");
      this.registerBlock(Blocks.stained_glass, EnumDyeColor.GREEN.getMetadata(), "green_stained_glass");
      this.registerBlock(Blocks.stained_glass, EnumDyeColor.LIGHT_BLUE.getMetadata(), "light_blue_stained_glass");
      this.registerBlock(Blocks.stained_glass, EnumDyeColor.LIME.getMetadata(), "lime_stained_glass");
      this.registerBlock(Blocks.stained_glass, EnumDyeColor.MAGENTA.getMetadata(), "magenta_stained_glass");
      this.registerBlock(Blocks.stained_glass, EnumDyeColor.ORANGE.getMetadata(), "orange_stained_glass");
      this.registerBlock(Blocks.stained_glass, EnumDyeColor.PINK.getMetadata(), "pink_stained_glass");
      this.registerBlock(Blocks.stained_glass, EnumDyeColor.PURPLE.getMetadata(), "purple_stained_glass");
      this.registerBlock(Blocks.stained_glass, EnumDyeColor.RED.getMetadata(), "red_stained_glass");
      this.registerBlock(Blocks.stained_glass, EnumDyeColor.SILVER.getMetadata(), "silver_stained_glass");
      this.registerBlock(Blocks.stained_glass, EnumDyeColor.WHITE.getMetadata(), "white_stained_glass");
      this.registerBlock(Blocks.stained_glass, EnumDyeColor.YELLOW.getMetadata(), "yellow_stained_glass");
      this.registerBlock(Blocks.stained_glass_pane, EnumDyeColor.BLACK.getMetadata(), "black_stained_glass_pane");
      this.registerBlock(Blocks.stained_glass_pane, EnumDyeColor.BLUE.getMetadata(), "blue_stained_glass_pane");
      this.registerBlock(Blocks.stained_glass_pane, EnumDyeColor.BROWN.getMetadata(), "brown_stained_glass_pane");
      this.registerBlock(Blocks.stained_glass_pane, EnumDyeColor.CYAN.getMetadata(), "cyan_stained_glass_pane");
      this.registerBlock(Blocks.stained_glass_pane, EnumDyeColor.GRAY.getMetadata(), "gray_stained_glass_pane");
      this.registerBlock(Blocks.stained_glass_pane, EnumDyeColor.GREEN.getMetadata(), "green_stained_glass_pane");
      this.registerBlock(Blocks.stained_glass_pane, EnumDyeColor.LIGHT_BLUE.getMetadata(), "light_blue_stained_glass_pane");
      this.registerBlock(Blocks.stained_glass_pane, EnumDyeColor.LIME.getMetadata(), "lime_stained_glass_pane");
      this.registerBlock(Blocks.stained_glass_pane, EnumDyeColor.MAGENTA.getMetadata(), "magenta_stained_glass_pane");
      this.registerBlock(Blocks.stained_glass_pane, EnumDyeColor.ORANGE.getMetadata(), "orange_stained_glass_pane");
      this.registerBlock(Blocks.stained_glass_pane, EnumDyeColor.PINK.getMetadata(), "pink_stained_glass_pane");
      this.registerBlock(Blocks.stained_glass_pane, EnumDyeColor.PURPLE.getMetadata(), "purple_stained_glass_pane");
      this.registerBlock(Blocks.stained_glass_pane, EnumDyeColor.RED.getMetadata(), "red_stained_glass_pane");
      this.registerBlock(Blocks.stained_glass_pane, EnumDyeColor.SILVER.getMetadata(), "silver_stained_glass_pane");
      this.registerBlock(Blocks.stained_glass_pane, EnumDyeColor.WHITE.getMetadata(), "white_stained_glass_pane");
      this.registerBlock(Blocks.stained_glass_pane, EnumDyeColor.YELLOW.getMetadata(), "yellow_stained_glass_pane");
      this.registerBlock(Blocks.stained_hardened_clay, EnumDyeColor.BLACK.getMetadata(), "black_stained_hardened_clay");
      this.registerBlock(Blocks.stained_hardened_clay, EnumDyeColor.BLUE.getMetadata(), "blue_stained_hardened_clay");
      this.registerBlock(Blocks.stained_hardened_clay, EnumDyeColor.BROWN.getMetadata(), "brown_stained_hardened_clay");
      this.registerBlock(Blocks.stained_hardened_clay, EnumDyeColor.CYAN.getMetadata(), "cyan_stained_hardened_clay");
      this.registerBlock(Blocks.stained_hardened_clay, EnumDyeColor.GRAY.getMetadata(), "gray_stained_hardened_clay");
      this.registerBlock(Blocks.stained_hardened_clay, EnumDyeColor.GREEN.getMetadata(), "green_stained_hardened_clay");
      this.registerBlock(Blocks.stained_hardened_clay, EnumDyeColor.LIGHT_BLUE.getMetadata(), "light_blue_stained_hardened_clay");
      this.registerBlock(Blocks.stained_hardened_clay, EnumDyeColor.LIME.getMetadata(), "lime_stained_hardened_clay");
      this.registerBlock(Blocks.stained_hardened_clay, EnumDyeColor.MAGENTA.getMetadata(), "magenta_stained_hardened_clay");
      this.registerBlock(Blocks.stained_hardened_clay, EnumDyeColor.ORANGE.getMetadata(), "orange_stained_hardened_clay");
      this.registerBlock(Blocks.stained_hardened_clay, EnumDyeColor.PINK.getMetadata(), "pink_stained_hardened_clay");
      this.registerBlock(Blocks.stained_hardened_clay, EnumDyeColor.PURPLE.getMetadata(), "purple_stained_hardened_clay");
      this.registerBlock(Blocks.stained_hardened_clay, EnumDyeColor.RED.getMetadata(), "red_stained_hardened_clay");
      this.registerBlock(Blocks.stained_hardened_clay, EnumDyeColor.SILVER.getMetadata(), "silver_stained_hardened_clay");
      this.registerBlock(Blocks.stained_hardened_clay, EnumDyeColor.WHITE.getMetadata(), "white_stained_hardened_clay");
      this.registerBlock(Blocks.stained_hardened_clay, EnumDyeColor.YELLOW.getMetadata(), "yellow_stained_hardened_clay");
      this.registerBlock(Blocks.stone, BlockStone.EnumType.ANDESITE.getMetadata(), "andesite");
      this.registerBlock(Blocks.stone, BlockStone.EnumType.ANDESITE_SMOOTH.getMetadata(), "andesite_smooth");
      this.registerBlock(Blocks.stone, BlockStone.EnumType.DIORITE.getMetadata(), "diorite");
      this.registerBlock(Blocks.stone, BlockStone.EnumType.DIORITE_SMOOTH.getMetadata(), "diorite_smooth");
      this.registerBlock(Blocks.stone, BlockStone.EnumType.GRANITE.getMetadata(), "granite");
      this.registerBlock(Blocks.stone, BlockStone.EnumType.GRANITE_SMOOTH.getMetadata(), "granite_smooth");
      this.registerBlock(Blocks.stone, BlockStone.EnumType.STONE.getMetadata(), "stone");
      this.registerBlock(Blocks.stonebrick, BlockStoneBrick.EnumType.CRACKED.getMetadata(), "cracked_stonebrick");
      this.registerBlock(Blocks.stonebrick, BlockStoneBrick.EnumType.DEFAULT.getMetadata(), "stonebrick");
      this.registerBlock(Blocks.stonebrick, BlockStoneBrick.EnumType.CHISELED.getMetadata(), "chiseled_stonebrick");
      this.registerBlock(Blocks.stonebrick, BlockStoneBrick.EnumType.MOSSY.getMetadata(), "mossy_stonebrick");
      this.registerBlock(Blocks.stone_slab, BlockStoneSlab.EnumType.BRICK.getMetadata(), "brick_slab");
      this.registerBlock(Blocks.stone_slab, BlockStoneSlab.EnumType.COBBLESTONE.getMetadata(), "cobblestone_slab");
      this.registerBlock(Blocks.stone_slab, BlockStoneSlab.EnumType.WOOD.getMetadata(), "old_wood_slab");
      this.registerBlock(Blocks.stone_slab, BlockStoneSlab.EnumType.NETHERBRICK.getMetadata(), "nether_brick_slab");
      this.registerBlock(Blocks.stone_slab, BlockStoneSlab.EnumType.QUARTZ.getMetadata(), "quartz_slab");
      this.registerBlock(Blocks.stone_slab, BlockStoneSlab.EnumType.SAND.getMetadata(), "sandstone_slab");
      this.registerBlock(Blocks.stone_slab, BlockStoneSlab.EnumType.SMOOTHBRICK.getMetadata(), "stone_brick_slab");
      this.registerBlock(Blocks.stone_slab, BlockStoneSlab.EnumType.STONE.getMetadata(), "stone_slab");
      this.registerBlock(Blocks.stone_slab2, BlockStoneSlabNew.EnumType.RED_SANDSTONE.getMetadata(), "red_sandstone_slab");
      this.registerBlock(Blocks.tallgrass, BlockTallGrass.EnumType.DEAD_BUSH.getMeta(), "dead_bush");
      this.registerBlock(Blocks.tallgrass, BlockTallGrass.EnumType.FERN.getMeta(), "fern");
      this.registerBlock(Blocks.tallgrass, BlockTallGrass.EnumType.GRASS.getMeta(), "tall_grass");
      this.registerBlock(Blocks.wooden_slab, BlockPlanks.EnumType.ACACIA.getMetadata(), "acacia_slab");
      this.registerBlock(Blocks.wooden_slab, BlockPlanks.EnumType.BIRCH.getMetadata(), "birch_slab");
      this.registerBlock(Blocks.wooden_slab, BlockPlanks.EnumType.DARK_OAK.getMetadata(), "dark_oak_slab");
      this.registerBlock(Blocks.wooden_slab, BlockPlanks.EnumType.JUNGLE.getMetadata(), "jungle_slab");
      this.registerBlock(Blocks.wooden_slab, BlockPlanks.EnumType.OAK.getMetadata(), "oak_slab");
      this.registerBlock(Blocks.wooden_slab, BlockPlanks.EnumType.SPRUCE.getMetadata(), "spruce_slab");
      this.registerBlock(Blocks.wool, EnumDyeColor.BLACK.getMetadata(), "black_wool");
      this.registerBlock(Blocks.wool, EnumDyeColor.BLUE.getMetadata(), "blue_wool");
      this.registerBlock(Blocks.wool, EnumDyeColor.BROWN.getMetadata(), "brown_wool");
      this.registerBlock(Blocks.wool, EnumDyeColor.CYAN.getMetadata(), "cyan_wool");
      this.registerBlock(Blocks.wool, EnumDyeColor.GRAY.getMetadata(), "gray_wool");
      this.registerBlock(Blocks.wool, EnumDyeColor.GREEN.getMetadata(), "green_wool");
      this.registerBlock(Blocks.wool, EnumDyeColor.LIGHT_BLUE.getMetadata(), "light_blue_wool");
      this.registerBlock(Blocks.wool, EnumDyeColor.LIME.getMetadata(), "lime_wool");
      this.registerBlock(Blocks.wool, EnumDyeColor.MAGENTA.getMetadata(), "magenta_wool");
      this.registerBlock(Blocks.wool, EnumDyeColor.ORANGE.getMetadata(), "orange_wool");
      this.registerBlock(Blocks.wool, EnumDyeColor.PINK.getMetadata(), "pink_wool");
      this.registerBlock(Blocks.wool, EnumDyeColor.PURPLE.getMetadata(), "purple_wool");
      this.registerBlock(Blocks.wool, EnumDyeColor.RED.getMetadata(), "red_wool");
      this.registerBlock(Blocks.wool, EnumDyeColor.SILVER.getMetadata(), "silver_wool");
      this.registerBlock(Blocks.wool, EnumDyeColor.WHITE.getMetadata(), "white_wool");
      this.registerBlock(Blocks.wool, EnumDyeColor.YELLOW.getMetadata(), "yellow_wool");
      this.registerBlock(Blocks.acacia_stairs, "acacia_stairs");
      this.registerBlock(Blocks.activator_rail, "activator_rail");
      this.registerBlock(Blocks.beacon, "beacon");
      this.registerBlock(Blocks.bedrock, "bedrock");
      this.registerBlock(Blocks.birch_stairs, "birch_stairs");
      this.registerBlock(Blocks.bookshelf, "bookshelf");
      this.registerBlock(Blocks.brick_block, "brick_block");
      this.registerBlock(Blocks.brick_block, "brick_block");
      this.registerBlock(Blocks.brick_stairs, "brick_stairs");
      this.registerBlock(Blocks.brown_mushroom, "brown_mushroom");
      this.registerBlock(Blocks.cactus, "cactus");
      this.registerBlock(Blocks.clay, "clay");
      this.registerBlock(Blocks.coal_block, "coal_block");
      this.registerBlock(Blocks.coal_ore, "coal_ore");
      this.registerBlock(Blocks.cobblestone, "cobblestone");
      this.registerBlock(Blocks.crafting_table, "crafting_table");
      this.registerBlock(Blocks.dark_oak_stairs, "dark_oak_stairs");
      this.registerBlock(Blocks.daylight_detector, "daylight_detector");
      this.registerBlock(Blocks.deadbush, "dead_bush");
      this.registerBlock(Blocks.detector_rail, "detector_rail");
      this.registerBlock(Blocks.diamond_block, "diamond_block");
      this.registerBlock(Blocks.diamond_ore, "diamond_ore");
      this.registerBlock(Blocks.dispenser, "dispenser");
      this.registerBlock(Blocks.dropper, "dropper");
      this.registerBlock(Blocks.emerald_block, "emerald_block");
      this.registerBlock(Blocks.emerald_ore, "emerald_ore");
      this.registerBlock(Blocks.enchanting_table, "enchanting_table");
      this.registerBlock(Blocks.end_portal_frame, "end_portal_frame");
      this.registerBlock(Blocks.end_stone, "end_stone");
      this.registerBlock(Blocks.oak_fence, "oak_fence");
      this.registerBlock(Blocks.spruce_fence, "spruce_fence");
      this.registerBlock(Blocks.birch_fence, "birch_fence");
      this.registerBlock(Blocks.jungle_fence, "jungle_fence");
      this.registerBlock(Blocks.dark_oak_fence, "dark_oak_fence");
      this.registerBlock(Blocks.acacia_fence, "acacia_fence");
      this.registerBlock(Blocks.oak_fence_gate, "oak_fence_gate");
      this.registerBlock(Blocks.spruce_fence_gate, "spruce_fence_gate");
      this.registerBlock(Blocks.birch_fence_gate, "birch_fence_gate");
      this.registerBlock(Blocks.jungle_fence_gate, "jungle_fence_gate");
      this.registerBlock(Blocks.dark_oak_fence_gate, "dark_oak_fence_gate");
      this.registerBlock(Blocks.acacia_fence_gate, "acacia_fence_gate");
      this.registerBlock(Blocks.furnace, "furnace");
      this.registerBlock(Blocks.glass, "glass");
      this.registerBlock(Blocks.glass_pane, "glass_pane");
      this.registerBlock(Blocks.glowstone, "glowstone");
      this.registerBlock(Blocks.golden_rail, "golden_rail");
      this.registerBlock(Blocks.gold_block, "gold_block");
      this.registerBlock(Blocks.gold_ore, "gold_ore");
      this.registerBlock(Blocks.grass, "grass");
      this.registerBlock(Blocks.gravel, "gravel");
      this.registerBlock(Blocks.hardened_clay, "hardened_clay");
      this.registerBlock(Blocks.hay_block, "hay_block");
      this.registerBlock(Blocks.heavy_weighted_pressure_plate, "heavy_weighted_pressure_plate");
      this.registerBlock(Blocks.hopper, "hopper");
      this.registerBlock(Blocks.ice, "ice");
      this.registerBlock(Blocks.iron_bars, "iron_bars");
      this.registerBlock(Blocks.iron_block, "iron_block");
      this.registerBlock(Blocks.iron_ore, "iron_ore");
      this.registerBlock(Blocks.iron_trapdoor, "iron_trapdoor");
      this.registerBlock(Blocks.jukebox, "jukebox");
      this.registerBlock(Blocks.jungle_stairs, "jungle_stairs");
      this.registerBlock(Blocks.ladder, "ladder");
      this.registerBlock(Blocks.lapis_block, "lapis_block");
      this.registerBlock(Blocks.lapis_ore, "lapis_ore");
      this.registerBlock(Blocks.lever, "lever");
      this.registerBlock(Blocks.light_weighted_pressure_plate, "light_weighted_pressure_plate");
      this.registerBlock(Blocks.lit_pumpkin, "lit_pumpkin");
      this.registerBlock(Blocks.melon_block, "melon_block");
      this.registerBlock(Blocks.mossy_cobblestone, "mossy_cobblestone");
      this.registerBlock(Blocks.mycelium, "mycelium");
      this.registerBlock(Blocks.netherrack, "netherrack");
      this.registerBlock(Blocks.nether_brick, "nether_brick");
      this.registerBlock(Blocks.nether_brick_fence, "nether_brick_fence");
      this.registerBlock(Blocks.nether_brick_stairs, "nether_brick_stairs");
      this.registerBlock(Blocks.noteblock, "noteblock");
      this.registerBlock(Blocks.oak_stairs, "oak_stairs");
      this.registerBlock(Blocks.obsidian, "obsidian");
      this.registerBlock(Blocks.packed_ice, "packed_ice");
      this.registerBlock(Blocks.piston, "piston");
      this.registerBlock(Blocks.pumpkin, "pumpkin");
      this.registerBlock(Blocks.quartz_ore, "quartz_ore");
      this.registerBlock(Blocks.quartz_stairs, "quartz_stairs");
      this.registerBlock(Blocks.rail, "rail");
      this.registerBlock(Blocks.redstone_block, "redstone_block");
      this.registerBlock(Blocks.redstone_lamp, "redstone_lamp");
      this.registerBlock(Blocks.redstone_ore, "redstone_ore");
      this.registerBlock(Blocks.redstone_torch, "redstone_torch");
      this.registerBlock(Blocks.red_mushroom, "red_mushroom");
      this.registerBlock(Blocks.sandstone_stairs, "sandstone_stairs");
      this.registerBlock(Blocks.red_sandstone_stairs, "red_sandstone_stairs");
      this.registerBlock(Blocks.sea_lantern, "sea_lantern");
      this.registerBlock(Blocks.slime_block, "slime");
      this.registerBlock(Blocks.snow, "snow");
      this.registerBlock(Blocks.snow_layer, "snow_layer");
      this.registerBlock(Blocks.soul_sand, "soul_sand");
      this.registerBlock(Blocks.spruce_stairs, "spruce_stairs");
      this.registerBlock(Blocks.sticky_piston, "sticky_piston");
      this.registerBlock(Blocks.stone_brick_stairs, "stone_brick_stairs");
      this.registerBlock(Blocks.stone_button, "stone_button");
      this.registerBlock(Blocks.stone_pressure_plate, "stone_pressure_plate");
      this.registerBlock(Blocks.stone_stairs, "stone_stairs");
      this.registerBlock(Blocks.tnt, "tnt");
      this.registerBlock(Blocks.torch, "torch");
      this.registerBlock(Blocks.trapdoor, "trapdoor");
      this.registerBlock(Blocks.tripwire_hook, "tripwire_hook");
      this.registerBlock(Blocks.vine, "vine");
      this.registerBlock(Blocks.waterlily, "waterlily");
      this.registerBlock(Blocks.web, "web");
      this.registerBlock(Blocks.wooden_button, "wooden_button");
      this.registerBlock(Blocks.wooden_pressure_plate, "wooden_pressure_plate");
      this.registerBlock(Blocks.yellow_flower, BlockFlower.EnumFlowerType.DANDELION.getMeta(), "dandelion");
      this.registerBlock(Blocks.chest, "chest");
      this.registerBlock(Blocks.trapped_chest, "trapped_chest");
      this.registerBlock(Blocks.ender_chest, "ender_chest");
      this.registerItem(Items.iron_shovel, "iron_shovel");
      this.registerItem(Items.iron_pickaxe, "iron_pickaxe");
      this.registerItem(Items.iron_axe, "iron_axe");
      this.registerItem(Items.flint_and_steel, "flint_and_steel");
      this.registerItem(Items.apple, "apple");
      this.registerItem(Items.bow, 0, "bow");
      this.registerItem(Items.bow, 1, "bow_pulling_0");
      this.registerItem(Items.bow, 2, "bow_pulling_1");
      this.registerItem(Items.bow, 3, "bow_pulling_2");
      this.registerItem(Items.arrow, "arrow");
      this.registerItem(Items.coal, 0, "coal");
      this.registerItem(Items.coal, 1, "charcoal");
      this.registerItem(Items.diamond, "diamond");
      this.registerItem(Items.iron_ingot, "iron_ingot");
      this.registerItem(Items.gold_ingot, "gold_ingot");
      this.registerItem(Items.iron_sword, "iron_sword");
      this.registerItem(Items.wooden_sword, "wooden_sword");
      this.registerItem(Items.wooden_shovel, "wooden_shovel");
      this.registerItem(Items.wooden_pickaxe, "wooden_pickaxe");
      this.registerItem(Items.wooden_axe, "wooden_axe");
      this.registerItem(Items.stone_sword, "stone_sword");
      this.registerItem(Items.stone_shovel, "stone_shovel");
      this.registerItem(Items.stone_pickaxe, "stone_pickaxe");
      this.registerItem(Items.stone_axe, "stone_axe");
      this.registerItem(Items.diamond_sword, "diamond_sword");
      this.registerItem(Items.diamond_shovel, "diamond_shovel");
      this.registerItem(Items.diamond_pickaxe, "diamond_pickaxe");
      this.registerItem(Items.diamond_axe, "diamond_axe");
      this.registerItem(Items.stick, "stick");
      this.registerItem(Items.bowl, "bowl");
      this.registerItem(Items.mushroom_stew, "mushroom_stew");
      this.registerItem(Items.golden_sword, "golden_sword");
      this.registerItem(Items.golden_shovel, "golden_shovel");
      this.registerItem(Items.golden_pickaxe, "golden_pickaxe");
      this.registerItem(Items.golden_axe, "golden_axe");
      this.registerItem(Items.string, "string");
      this.registerItem(Items.feather, "feather");
      this.registerItem(Items.gunpowder, "gunpowder");
      this.registerItem(Items.wooden_hoe, "wooden_hoe");
      this.registerItem(Items.stone_hoe, "stone_hoe");
      this.registerItem(Items.iron_hoe, "iron_hoe");
      this.registerItem(Items.diamond_hoe, "diamond_hoe");
      this.registerItem(Items.golden_hoe, "golden_hoe");
      this.registerItem(Items.wheat_seeds, "wheat_seeds");
      this.registerItem(Items.wheat, "wheat");
      this.registerItem(Items.bread, "bread");
      this.registerItem(Items.leather_helmet, "leather_helmet");
      this.registerItem(Items.leather_chestplate, "leather_chestplate");
      this.registerItem(Items.leather_leggings, "leather_leggings");
      this.registerItem(Items.leather_boots, "leather_boots");
      this.registerItem(Items.chainmail_helmet, "chainmail_helmet");
      this.registerItem(Items.chainmail_chestplate, "chainmail_chestplate");
      this.registerItem(Items.chainmail_leggings, "chainmail_leggings");
      this.registerItem(Items.chainmail_boots, "chainmail_boots");
      this.registerItem(Items.iron_helmet, "iron_helmet");
      this.registerItem(Items.iron_chestplate, "iron_chestplate");
      this.registerItem(Items.iron_leggings, "iron_leggings");
      this.registerItem(Items.iron_boots, "iron_boots");
      this.registerItem(Items.diamond_helmet, "diamond_helmet");
      this.registerItem(Items.diamond_chestplate, "diamond_chestplate");
      this.registerItem(Items.diamond_leggings, "diamond_leggings");
      this.registerItem(Items.diamond_boots, "diamond_boots");
      this.registerItem(Items.golden_helmet, "golden_helmet");
      this.registerItem(Items.golden_chestplate, "golden_chestplate");
      this.registerItem(Items.golden_leggings, "golden_leggings");
      this.registerItem(Items.golden_boots, "golden_boots");
      this.registerItem(Items.flint, "flint");
      this.registerItem(Items.porkchop, "porkchop");
      this.registerItem(Items.cooked_porkchop, "cooked_porkchop");
      this.registerItem(Items.painting, "painting");
      this.registerItem(Items.golden_apple, "golden_apple");
      this.registerItem(Items.golden_apple, 1, "golden_apple");
      this.registerItem(Items.sign, "sign");
      this.registerItem(Items.oak_door, "oak_door");
      this.registerItem(Items.spruce_door, "spruce_door");
      this.registerItem(Items.birch_door, "birch_door");
      this.registerItem(Items.jungle_door, "jungle_door");
      this.registerItem(Items.acacia_door, "acacia_door");
      this.registerItem(Items.dark_oak_door, "dark_oak_door");
      this.registerItem(Items.bucket, "bucket");
      this.registerItem(Items.water_bucket, "water_bucket");
      this.registerItem(Items.lava_bucket, "lava_bucket");
      this.registerItem(Items.minecart, "minecart");
      this.registerItem(Items.saddle, "saddle");
      this.registerItem(Items.iron_door, "iron_door");
      this.registerItem(Items.redstone, "redstone");
      this.registerItem(Items.snowball, "snowball");
      this.registerItem(Items.boat, "boat");
      this.registerItem(Items.leather, "leather");
      this.registerItem(Items.milk_bucket, "milk_bucket");
      this.registerItem(Items.brick, "brick");
      this.registerItem(Items.clay_ball, "clay_ball");
      this.registerItem(Items.reeds, "reeds");
      this.registerItem(Items.paper, "paper");
      this.registerItem(Items.book, "book");
      this.registerItem(Items.slime_ball, "slime_ball");
      this.registerItem(Items.chest_minecart, "chest_minecart");
      this.registerItem(Items.furnace_minecart, "furnace_minecart");
      this.registerItem(Items.egg, "egg");
      this.registerItem(Items.compass, "compass");
      this.registerItem(Items.fishing_rod, "fishing_rod");
      this.registerItem(Items.fishing_rod, 1, "fishing_rod_cast");
      this.registerItem(Items.clock, "clock");
      this.registerItem(Items.glowstone_dust, "glowstone_dust");
      this.registerItem(Items.fish, ItemFishFood.FishType.COD.getMetadata(), "cod");
      this.registerItem(Items.fish, ItemFishFood.FishType.SALMON.getMetadata(), "salmon");
      this.registerItem(Items.fish, ItemFishFood.FishType.CLOWNFISH.getMetadata(), "clownfish");
      this.registerItem(Items.fish, ItemFishFood.FishType.PUFFERFISH.getMetadata(), "pufferfish");
      this.registerItem(Items.cooked_fish, ItemFishFood.FishType.COD.getMetadata(), "cooked_cod");
      this.registerItem(Items.cooked_fish, ItemFishFood.FishType.SALMON.getMetadata(), "cooked_salmon");
      this.registerItem(Items.dye, EnumDyeColor.BLACK.getDyeDamage(), "dye_black");
      this.registerItem(Items.dye, EnumDyeColor.RED.getDyeDamage(), "dye_red");
      this.registerItem(Items.dye, EnumDyeColor.GREEN.getDyeDamage(), "dye_green");
      this.registerItem(Items.dye, EnumDyeColor.BROWN.getDyeDamage(), "dye_brown");
      this.registerItem(Items.dye, EnumDyeColor.BLUE.getDyeDamage(), "dye_blue");
      this.registerItem(Items.dye, EnumDyeColor.PURPLE.getDyeDamage(), "dye_purple");
      this.registerItem(Items.dye, EnumDyeColor.CYAN.getDyeDamage(), "dye_cyan");
      this.registerItem(Items.dye, EnumDyeColor.SILVER.getDyeDamage(), "dye_silver");
      this.registerItem(Items.dye, EnumDyeColor.GRAY.getDyeDamage(), "dye_gray");
      this.registerItem(Items.dye, EnumDyeColor.PINK.getDyeDamage(), "dye_pink");
      this.registerItem(Items.dye, EnumDyeColor.LIME.getDyeDamage(), "dye_lime");
      this.registerItem(Items.dye, EnumDyeColor.YELLOW.getDyeDamage(), "dye_yellow");
      this.registerItem(Items.dye, EnumDyeColor.LIGHT_BLUE.getDyeDamage(), "dye_light_blue");
      this.registerItem(Items.dye, EnumDyeColor.MAGENTA.getDyeDamage(), "dye_magenta");
      this.registerItem(Items.dye, EnumDyeColor.ORANGE.getDyeDamage(), "dye_orange");
      this.registerItem(Items.dye, EnumDyeColor.WHITE.getDyeDamage(), "dye_white");
      this.registerItem(Items.bone, "bone");
      this.registerItem(Items.sugar, "sugar");
      this.registerItem(Items.cake, "cake");
      this.registerItem(Items.bed, "bed");
      this.registerItem(Items.repeater, "repeater");
      this.registerItem(Items.cookie, "cookie");
      this.registerItem(Items.shears, "shears");
      this.registerItem(Items.melon, "melon");
      this.registerItem(Items.pumpkin_seeds, "pumpkin_seeds");
      this.registerItem(Items.melon_seeds, "melon_seeds");
      this.registerItem(Items.beef, "beef");
      this.registerItem(Items.cooked_beef, "cooked_beef");
      this.registerItem(Items.chicken, "chicken");
      this.registerItem(Items.cooked_chicken, "cooked_chicken");
      this.registerItem(Items.rabbit, "rabbit");
      this.registerItem(Items.cooked_rabbit, "cooked_rabbit");
      this.registerItem(Items.mutton, "mutton");
      this.registerItem(Items.cooked_mutton, "cooked_mutton");
      this.registerItem(Items.rabbit_foot, "rabbit_foot");
      this.registerItem(Items.rabbit_hide, "rabbit_hide");
      this.registerItem(Items.rabbit_stew, "rabbit_stew");
      this.registerItem(Items.rotten_flesh, "rotten_flesh");
      this.registerItem(Items.ender_pearl, "ender_pearl");
      this.registerItem(Items.blaze_rod, "blaze_rod");
      this.registerItem(Items.ghast_tear, "ghast_tear");
      this.registerItem(Items.gold_nugget, "gold_nugget");
      this.registerItem(Items.nether_wart, "nether_wart");
      this.itemModelMesher.register(Items.potionitem, new ItemMeshDefinition(this) {
         final RenderItem this$0;
         private static final String __OBFID = "CL_00002440";

         public ModelResourceLocation getModelLocation(ItemStack var1) {
            return ItemPotion.isSplash(var1.getMetadata()) ? new ModelResourceLocation("bottle_splash", "inventory") : new ModelResourceLocation("bottle_drinkable", "inventory");
         }

         {
            this.this$0 = var1;
         }
      });
      this.registerItem(Items.glass_bottle, "glass_bottle");
      this.registerItem(Items.spider_eye, "spider_eye");
      this.registerItem(Items.fermented_spider_eye, "fermented_spider_eye");
      this.registerItem(Items.blaze_powder, "blaze_powder");
      this.registerItem(Items.magma_cream, "magma_cream");
      this.registerItem(Items.brewing_stand, "brewing_stand");
      this.registerItem(Items.cauldron, "cauldron");
      this.registerItem(Items.ender_eye, "ender_eye");
      this.registerItem(Items.speckled_melon, "speckled_melon");
      this.itemModelMesher.register(Items.spawn_egg, new ItemMeshDefinition(this) {
         private static final String __OBFID = "CL_00002439";
         final RenderItem this$0;

         {
            this.this$0 = var1;
         }

         public ModelResourceLocation getModelLocation(ItemStack var1) {
            return new ModelResourceLocation("spawn_egg", "inventory");
         }
      });
      this.registerItem(Items.experience_bottle, "experience_bottle");
      this.registerItem(Items.fire_charge, "fire_charge");
      this.registerItem(Items.writable_book, "writable_book");
      this.registerItem(Items.emerald, "emerald");
      this.registerItem(Items.item_frame, "item_frame");
      this.registerItem(Items.flower_pot, "flower_pot");
      this.registerItem(Items.carrot, "carrot");
      this.registerItem(Items.potato, "potato");
      this.registerItem(Items.baked_potato, "baked_potato");
      this.registerItem(Items.poisonous_potato, "poisonous_potato");
      this.registerItem(Items.map, "map");
      this.registerItem(Items.golden_carrot, "golden_carrot");
      this.registerItem(Items.skull, 0, "skull_skeleton");
      this.registerItem(Items.skull, 1, "skull_wither");
      this.registerItem(Items.skull, 2, "skull_zombie");
      this.registerItem(Items.skull, 3, "skull_char");
      this.registerItem(Items.skull, 4, "skull_creeper");
      this.registerItem(Items.carrot_on_a_stick, "carrot_on_a_stick");
      this.registerItem(Items.nether_star, "nether_star");
      this.registerItem(Items.pumpkin_pie, "pumpkin_pie");
      this.registerItem(Items.firework_charge, "firework_charge");
      this.registerItem(Items.comparator, "comparator");
      this.registerItem(Items.netherbrick, "netherbrick");
      this.registerItem(Items.quartz, "quartz");
      this.registerItem(Items.tnt_minecart, "tnt_minecart");
      this.registerItem(Items.hopper_minecart, "hopper_minecart");
      this.registerItem(Items.armor_stand, "armor_stand");
      this.registerItem(Items.iron_horse_armor, "iron_horse_armor");
      this.registerItem(Items.golden_horse_armor, "golden_horse_armor");
      this.registerItem(Items.diamond_horse_armor, "diamond_horse_armor");
      this.registerItem(Items.lead, "lead");
      this.registerItem(Items.name_tag, "name_tag");
      this.itemModelMesher.register(Items.banner, new ItemMeshDefinition(this) {
         private static final String __OBFID = "CL_00002438";
         final RenderItem this$0;

         {
            this.this$0 = var1;
         }

         public ModelResourceLocation getModelLocation(ItemStack var1) {
            return new ModelResourceLocation("banner", "inventory");
         }
      });
      this.registerItem(Items.record_13, "record_13");
      this.registerItem(Items.record_cat, "record_cat");
      this.registerItem(Items.record_blocks, "record_blocks");
      this.registerItem(Items.record_chirp, "record_chirp");
      this.registerItem(Items.record_far, "record_far");
      this.registerItem(Items.record_mall, "record_mall");
      this.registerItem(Items.record_mellohi, "record_mellohi");
      this.registerItem(Items.record_stal, "record_stal");
      this.registerItem(Items.record_strad, "record_strad");
      this.registerItem(Items.record_ward, "record_ward");
      this.registerItem(Items.record_11, "record_11");
      this.registerItem(Items.record_wait, "record_wait");
      this.registerItem(Items.prismarine_shard, "prismarine_shard");
      this.registerItem(Items.prismarine_crystals, "prismarine_crystals");
      this.itemModelMesher.register(Items.enchanted_book, new ItemMeshDefinition(this) {
         private static final String __OBFID = "CL_00002437";
         final RenderItem this$0;

         public ModelResourceLocation getModelLocation(ItemStack var1) {
            return new ModelResourceLocation("enchanted_book", "inventory");
         }

         {
            this.this$0 = var1;
         }
      });
      this.itemModelMesher.register(Items.filled_map, new ItemMeshDefinition(this) {
         final RenderItem this$0;
         private static final String __OBFID = "CL_00002436";

         public ModelResourceLocation getModelLocation(ItemStack var1) {
            return new ModelResourceLocation("filled_map", "inventory");
         }

         {
            this.this$0 = var1;
         }
      });
      this.registerBlock(Blocks.command_block, "command_block");
      this.registerItem(Items.fireworks, "fireworks");
      this.registerItem(Items.command_block_minecart, "command_block_minecart");
      this.registerBlock(Blocks.barrier, "barrier");
      this.registerBlock(Blocks.mob_spawner, "mob_spawner");
      this.registerItem(Items.written_book, "written_book");
      this.registerBlock(Blocks.brown_mushroom_block, BlockHugeMushroom.EnumType.ALL_INSIDE.getMetadata(), "brown_mushroom_block");
      this.registerBlock(Blocks.red_mushroom_block, BlockHugeMushroom.EnumType.ALL_INSIDE.getMetadata(), "red_mushroom_block");
      this.registerBlock(Blocks.dragon_egg, "dragon_egg");
      if (Reflector.ModelLoader_onRegisterItems.exists()) {
         Reflector.call(Reflector.ModelLoader_onRegisterItems, this.itemModelMesher);
      }

   }

   public void renderItemModelForEntity(ItemStack var1, EntityLivingBase var2, ItemCameraTransforms.TransformType var3) {
      if (var1 != null && var2 != null) {
         IBakedModel var4 = this.itemModelMesher.getItemModel(var1);
         if (var2 instanceof EntityPlayer) {
            EntityPlayer var5 = (EntityPlayer)var2;
            Item var6 = var1.getItem();
            ModelResourceLocation var7 = null;
            if (var6 == Items.fishing_rod && var5.fishEntity != null) {
               var7 = new ModelResourceLocation("fishing_rod_cast", "inventory");
            } else if (var6 == Items.bow && var5.getItemInUse() != null) {
               int var8 = var1.getMaxItemUseDuration() - var5.getItemInUseCount();
               if (var8 >= 18) {
                  var7 = new ModelResourceLocation("bow_pulling_2", "inventory");
               } else if (var8 > 13) {
                  var7 = new ModelResourceLocation("bow_pulling_1", "inventory");
               } else if (var8 > 0) {
                  var7 = new ModelResourceLocation("bow_pulling_0", "inventory");
               }
            } else if (Reflector.ForgeItem_getModel.exists()) {
               var7 = (ModelResourceLocation)Reflector.call(var6, Reflector.ForgeItem_getModel, var1, var5, var5.getItemInUseCount());
            }

            this.modelLocation = var7;
            if (var7 != null) {
               var4 = this.itemModelMesher.getModelManager().getModel(var7);
            }
         }

         this.renderItemModelTransform(var1, var4, var3);
         this.modelLocation = null;
      }

   }

   public boolean shouldRenderItemIn3D(ItemStack var1) {
      IBakedModel var2 = this.itemModelMesher.getItemModel(var1);
      return var2 == null ? false : var2.isGui3d();
   }

   public void renderItemIntoGUI(ItemStack var1, int var2, int var3) {
      this.renderItemGui = true;
      IBakedModel var4 = this.itemModelMesher.getItemModel(var1);
      GlStateManager.pushMatrix();
      this.textureManager.bindTexture(TextureMap.locationBlocksTexture);
      this.textureManager.getTexture(TextureMap.locationBlocksTexture).setBlurMipmap(false, false);
      GlStateManager.enableRescaleNormal();
      GlStateManager.enableAlpha();
      GlStateManager.alphaFunc(516, 0.1F);
      GlStateManager.enableBlend();
      GlStateManager.blendFunc(770, 771);
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      this.setupGuiTransform(var2, var3, var4.isGui3d());
      if (Reflector.ForgeHooksClient_handleCameraTransforms.exists()) {
         var4 = (IBakedModel)Reflector.call(Reflector.ForgeHooksClient_handleCameraTransforms, var4, ItemCameraTransforms.TransformType.GUI);
      } else {
         var4.getItemCameraTransforms().applyTransform(ItemCameraTransforms.TransformType.GUI);
      }

      this.renderItem(var1, var4);
      GlStateManager.disableAlpha();
      GlStateManager.disableRescaleNormal();
      GlStateManager.disableLighting();
      GlStateManager.popMatrix();
      this.textureManager.bindTexture(TextureMap.locationBlocksTexture);
      this.textureManager.getTexture(TextureMap.locationBlocksTexture).restoreLastBlurMipmap();
      this.renderItemGui = false;
   }

   protected void registerBlock(Block var1, int var2, String var3) {
      this.registerItem(Item.getItemFromBlock(var1), var2, var3);
   }

   private void setupGuiTransform(int var1, int var2, boolean var3) {
      GlStateManager.translate((float)var1, (float)var2, 100.0F + this.zLevel);
      GlStateManager.translate(8.0F, 8.0F, 0.0F);
      GlStateManager.scale(1.0F, 1.0F, -1.0F);
      GlStateManager.scale(0.5F, 0.5F, 0.5F);
      if (var3) {
         GlStateManager.scale(40.0F, 40.0F, 40.0F);
         GlStateManager.rotate(210.0F, 1.0F, 0.0F, 0.0F);
         GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
         GlStateManager.enableLighting();
      } else {
         GlStateManager.scale(64.0F, 64.0F, 64.0F);
         GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
         GlStateManager.disableLighting();
      }

   }

   private void putQuadNormal(WorldRenderer var1, BakedQuad var2) {
      Vec3i var3 = var2.getFace().getDirectionVec();
      var1.putNormal((float)var3.getX(), (float)var3.getY(), (float)var3.getZ());
   }

   public void func_175039_a(boolean var1) {
      this.field_175058_l = var1;
   }

   protected void renderItemModelTransform(ItemStack var1, IBakedModel var2, ItemCameraTransforms.TransformType var3) {
      this.textureManager.bindTexture(TextureMap.locationBlocksTexture);
      this.textureManager.getTexture(TextureMap.locationBlocksTexture).setBlurMipmap(false, false);
      this.preTransform(var1);
      GlStateManager.enableRescaleNormal();
      GlStateManager.alphaFunc(516, 0.1F);
      GlStateManager.enableBlend();
      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
      GlStateManager.pushMatrix();
      if (Reflector.ForgeHooksClient_handleCameraTransforms.exists()) {
         var2 = (IBakedModel)Reflector.call(Reflector.ForgeHooksClient_handleCameraTransforms, var2, var3);
      } else {
         ItemCameraTransforms var4 = var2.getItemCameraTransforms();
         var4.applyTransform(var3);
         if (var4.getTransform(var3) < 0) {
            GlStateManager.cullFace(1028);
         }
      }

      this.renderItem(var1, var2);
      GlStateManager.cullFace(1029);
      GlStateManager.popMatrix();
      GlStateManager.disableRescaleNormal();
      GlStateManager.disableBlend();
      this.textureManager.bindTexture(TextureMap.locationBlocksTexture);
      this.textureManager.getTexture(TextureMap.locationBlocksTexture).restoreLastBlurMipmap();
   }

   public void func_181564_a(ItemStack var1, ItemCameraTransforms.TransformType var2) {
      if (var1 != null) {
         IBakedModel var3 = this.itemModelMesher.getItemModel(var1);
         this.renderItemModelTransform(var1, var3, var2);
      }

   }

   public void renderItem(ItemStack var1, IBakedModel var2, boolean var3) {
      if (var3) {
         this.renderItem(var1, var2);
      } else {
         GlStateManager.pushMatrix();
         GlStateManager.disableDepth();
         GlStateManager.scale(0.5F, 0.5F, 0.5F);
         if (var2.isBuiltInRenderer()) {
            GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.translate(-0.5F, -0.5F, -0.5F);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.enableRescaleNormal();
            TileEntityRendererChestHelper.instance.renderByItem(var1);
         } else {
            GlStateManager.translate(-0.5F, -0.5F, -0.5F);
            this.renderItem(var1, var2);
         }

         GlStateManager.enableDepth();
         GlStateManager.popMatrix();
      }

   }

   public void renderItemOverlayIntoGUI(FontRenderer var1, ItemStack var2, int var3, int var4, String var5) {
      if (var2 != null) {
         if (var2.stackSize != 1 || var5 != null) {
            String var6 = var5 == null ? String.valueOf(var2.stackSize) : var5;
            if (var5 == null && var2.stackSize < 1) {
               var6 = EnumChatFormatting.RED + String.valueOf(var2.stackSize);
            }

            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            GlStateManager.disableBlend();
            var1.drawStringWithShadow(var6, (float)(var3 + 19 - 2 - var1.getStringWidth(var6)), (float)(var4 + 6 + 3), 16777215);
            GlStateManager.enableLighting();
            GlStateManager.enableDepth();
         }

         boolean var11 = var2.isItemDamaged();
         if (Reflector.ForgeItem_showDurabilityBar.exists()) {
            var11 = Reflector.callBoolean(var2.getItem(), Reflector.ForgeItem_showDurabilityBar, var2);
         }

         if (var11) {
            int var7 = (int)Math.round(13.0D - (double)var2.getItemDamage() * 13.0D / (double)var2.getMaxDamage());
            int var8 = (int)Math.round(255.0D - (double)var2.getItemDamage() * 255.0D / (double)var2.getMaxDamage());
            if (Reflector.ForgeItem_getDurabilityForDisplay.exists()) {
               double var9 = Reflector.callDouble(var2.getItem(), Reflector.ForgeItem_getDurabilityForDisplay, var2);
               var7 = (int)Math.round(13.0D - var9 * 13.0D);
               var8 = (int)Math.round(255.0D - var9 * 255.0D);
            }

            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            GlStateManager.disableTexture2D();
            GlStateManager.disableAlpha();
            GlStateManager.disableBlend();
            Tessellator var12 = Tessellator.getInstance();
            WorldRenderer var10 = var12.getWorldRenderer();
            this.func_181565_a(var10, var3 + 2, var4 + 13, 13, 2, 0, 0, 0, 255);
            this.func_181565_a(var10, var3 + 2, var4 + 13, 12, 1, (255 - var8) / 4, 64, 0, 255);
            this.func_181565_a(var10, var3 + 2, var4 + 13, var7, 1, 255 - var8, var8, 0, 255);
            if (!Reflector.ForgeHooksClient.exists()) {
               GlStateManager.enableBlend();
            }

            GlStateManager.enableAlpha();
            GlStateManager.enableTexture2D();
            GlStateManager.enableLighting();
            GlStateManager.enableDepth();
         }
      }

   }

   private void renderQuad(WorldRenderer var1, BakedQuad var2, int var3) {
      if (var1.isMultiTexture()) {
         var1.addVertexData(var2.getVertexDataSingle());
         var1.putSprite(var2.getSprite());
      } else {
         var1.addVertexData(var2.getVertexData());
      }

      if (Reflector.IColoredBakedQuad.exists() && Reflector.IColoredBakedQuad.isInstance(var2)) {
         forgeHooksClient_putQuadColor(var1, var2, var3);
      } else {
         var1.putColor4(var3);
      }

      this.putQuadNormal(var1, var2);
   }

   private void func_181565_a(WorldRenderer var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9) {
      var1.begin(7, DefaultVertexFormats.POSITION_COLOR);
      var1.pos((double)(var2 + 0), (double)(var3 + 0), 0.0D).color(var6, var7, var8, var9).endVertex();
      var1.pos((double)(var2 + 0), (double)(var3 + var5), 0.0D).color(var6, var7, var8, var9).endVertex();
      var1.pos((double)(var2 + var4), (double)(var3 + var5), 0.0D).color(var6, var7, var8, var9).endVertex();
      var1.pos((double)(var2 + var4), (double)(var3 + 0), 0.0D).color(var6, var7, var8, var9).endVertex();
      Tessellator.getInstance().draw();
   }

   private void preTransform(ItemStack var1) {
      IBakedModel var2 = this.itemModelMesher.getItemModel(var1);
      Item var3 = var1.getItem();
      if (var3 != null) {
         boolean var4 = var2.isGui3d();
         if (!var4) {
            GlStateManager.scale(2.0F, 2.0F, 2.0F);
         }

         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      }

   }
}
