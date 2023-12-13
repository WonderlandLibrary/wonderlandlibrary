// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.resources.model;

import org.apache.logging.log4j.LogManager;
import net.minecraft.client.renderer.texture.IIconCreator;
import java.util.ArrayDeque;
import com.google.common.collect.Queues;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockPartFace;
import net.minecraft.util.EnumFacing;
import net.minecraft.client.renderer.block.model.BlockPart;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import java.util.HashSet;
import java.util.Comparator;
import com.google.common.collect.Sets;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import java.util.Collections;
import net.minecraft.init.Items;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import java.io.StringReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.io.IOException;
import org.apache.commons.io.IOUtils;
import java.io.Reader;
import java.io.InputStreamReader;
import com.google.common.base.Charsets;
import net.minecraft.client.resources.IResource;
import java.util.Iterator;
import java.util.List;
import com.google.common.collect.Lists;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.renderer.block.model.ModelBlockDefinition;
import java.util.Collection;
import net.minecraft.util.IRegistry;
import com.google.common.collect.Maps;
import net.minecraft.client.renderer.block.model.ModelBlock;
import net.minecraft.util.RegistrySimple;
import net.minecraft.client.renderer.block.model.ItemModelGenerator;
import net.minecraft.client.renderer.block.model.FaceBakery;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.IResourceManager;
import com.google.common.base.Joiner;
import java.util.Map;
import org.apache.logging.log4j.Logger;
import java.util.Set;

public class ModelBakery
{
    private static final Set field_177602_b;
    private static final Logger LOGGER;
    protected static final ModelResourceLocation MODEL_MISSING;
    private static final Map BUILT_IN_MODELS;
    private static final Joiner field_177601_e;
    private final IResourceManager resourceManager;
    private final Map field_177599_g;
    private final Map models;
    private final Map variants;
    private final TextureMap textureMap;
    private final BlockModelShapes blockModelShapes;
    private final FaceBakery field_177607_l;
    private final ItemModelGenerator itemModelGenerator;
    private RegistrySimple bakedRegistry;
    private static final ModelBlock MODEL_GENERATED;
    private static final ModelBlock MODEL_COMPASS;
    private static final ModelBlock MODEL_CLOCK;
    private static final ModelBlock MODEL_ENTITY;
    private Map itemLocations;
    private final Map field_177614_t;
    private Map variantNames;
    private static final String __OBFID = "CL_00002391";
    
    public ModelBakery(final IResourceManager p_i46085_1_, final TextureMap p_i46085_2_, final BlockModelShapes p_i46085_3_) {
        this.field_177599_g = Maps.newHashMap();
        this.models = Maps.newLinkedHashMap();
        this.variants = Maps.newLinkedHashMap();
        this.field_177607_l = new FaceBakery();
        this.itemModelGenerator = new ItemModelGenerator();
        this.bakedRegistry = new RegistrySimple();
        this.itemLocations = Maps.newLinkedHashMap();
        this.field_177614_t = Maps.newHashMap();
        this.variantNames = Maps.newIdentityHashMap();
        this.resourceManager = p_i46085_1_;
        this.textureMap = p_i46085_2_;
        this.blockModelShapes = p_i46085_3_;
    }
    
    public IRegistry setupModelRegistry() {
        this.func_177577_b();
        this.func_177597_h();
        this.func_177572_j();
        this.bakeItemModels();
        this.bakeBlockModels();
        return this.bakedRegistry;
    }
    
    private void func_177577_b() {
        this.loadVariants(this.blockModelShapes.getBlockStateMapper().func_178446_a().values());
        this.variants.put(ModelBakery.MODEL_MISSING, new ModelBlockDefinition.Variants(ModelBakery.MODEL_MISSING.func_177518_c(), Lists.newArrayList((Object[])new ModelBlockDefinition.Variant[] { new ModelBlockDefinition.Variant(new ResourceLocation(ModelBakery.MODEL_MISSING.getResourcePath()), ModelRotation.X0_Y0, false, 1) })));
        final ResourceLocation var1 = new ResourceLocation("item_frame");
        final ModelBlockDefinition var2 = this.getModelBlockDefinition(var1);
        this.registerVariant(var2, new ModelResourceLocation(var1, "normal"));
        this.registerVariant(var2, new ModelResourceLocation(var1, "map"));
        this.func_177595_c();
        this.loadItemModels();
    }
    
    private void loadVariants(final Collection p_177591_1_) {
        for (final ModelResourceLocation var3 : p_177591_1_) {
            try {
                final ModelBlockDefinition var4 = this.getModelBlockDefinition(var3);
                try {
                    this.registerVariant(var4, var3);
                }
                catch (Exception var6) {
                    ModelBakery.LOGGER.warn("Unable to load variant: " + var3.func_177518_c() + " from " + var3);
                }
            }
            catch (Exception var5) {
                ModelBakery.LOGGER.warn("Unable to load definition " + var3, (Throwable)var5);
            }
        }
    }
    
    private void registerVariant(final ModelBlockDefinition p_177569_1_, final ModelResourceLocation p_177569_2_) {
        this.variants.put(p_177569_2_, p_177569_1_.func_178330_b(p_177569_2_.func_177518_c()));
    }
    
    private ModelBlockDefinition getModelBlockDefinition(final ResourceLocation p_177586_1_) {
        final ResourceLocation var2 = this.getBlockStateLocation(p_177586_1_);
        ModelBlockDefinition var3 = this.field_177614_t.get(var2);
        if (var3 == null) {
            final ArrayList var4 = Lists.newArrayList();
            try {
                for (final IResource var6 : this.resourceManager.getAllResources(var2)) {
                    InputStream var7 = null;
                    try {
                        var7 = var6.getInputStream();
                        final ModelBlockDefinition var8 = ModelBlockDefinition.func_178331_a(new InputStreamReader(var7, Charsets.UTF_8));
                        var4.add(var8);
                    }
                    catch (Exception var9) {
                        throw new RuntimeException("Encountered an exception when loading model definition of '" + p_177586_1_ + "' from: '" + var6.func_177241_a() + "' in resourcepack: '" + var6.func_177240_d() + "'", var9);
                    }
                    finally {
                        IOUtils.closeQuietly(var7);
                    }
                }
            }
            catch (IOException var10) {
                throw new RuntimeException("Encountered an exception when loading model definition of model " + var2.toString(), var10);
            }
            var3 = new ModelBlockDefinition(var4);
            this.field_177614_t.put(var2, var3);
        }
        return var3;
    }
    
    private ResourceLocation getBlockStateLocation(final ResourceLocation p_177584_1_) {
        return new ResourceLocation(p_177584_1_.getResourceDomain(), "blockstates/" + p_177584_1_.getResourcePath() + ".json");
    }
    
    private void func_177595_c() {
        for (final ModelResourceLocation var2 : this.variants.keySet()) {
            for (final ModelBlockDefinition.Variant var4 : this.variants.get(var2).getVariants()) {
                final ResourceLocation var5 = var4.getModelLocation();
                if (this.models.get(var5) == null) {
                    try {
                        final ModelBlock var6 = this.loadModel(var5);
                        this.models.put(var5, var6);
                    }
                    catch (Exception var7) {
                        ModelBakery.LOGGER.warn("Unable to load block model: '" + var5 + "' for variant: '" + var2 + "'", (Throwable)var7);
                    }
                }
            }
        }
    }
    
    private ModelBlock loadModel(final ResourceLocation p_177594_1_) throws IOException {
        final String var3 = p_177594_1_.getResourcePath();
        if ("builtin/generated".equals(var3)) {
            return ModelBakery.MODEL_GENERATED;
        }
        if ("builtin/compass".equals(var3)) {
            return ModelBakery.MODEL_COMPASS;
        }
        if ("builtin/clock".equals(var3)) {
            return ModelBakery.MODEL_CLOCK;
        }
        if ("builtin/entity".equals(var3)) {
            return ModelBakery.MODEL_ENTITY;
        }
        Object var6;
        if (var3.startsWith("builtin/")) {
            final String var4 = var3.substring("builtin/".length());
            final String var5 = ModelBakery.BUILT_IN_MODELS.get(var4);
            if (var5 == null) {
                throw new FileNotFoundException(p_177594_1_.toString());
            }
            var6 = new StringReader(var5);
        }
        else {
            final IResource var7 = this.resourceManager.getResource(this.getModelLocation(p_177594_1_));
            var6 = new InputStreamReader(var7.getInputStream(), Charsets.UTF_8);
        }
        ModelBlock var9;
        try {
            final ModelBlock var8 = ModelBlock.deserialize((Reader)var6);
            var8.field_178317_b = p_177594_1_.toString();
            var9 = var8;
        }
        finally {
            ((Reader)var6).close();
        }
        return var9;
    }
    
    private ResourceLocation getModelLocation(final ResourceLocation p_177580_1_) {
        return new ResourceLocation(p_177580_1_.getResourceDomain(), "models/" + p_177580_1_.getResourcePath() + ".json");
    }
    
    private void loadItemModels() {
        this.registerVariantNames();
        for (final Item var2 : Item.itemRegistry) {
            final List var3 = this.getVariantNames(var2);
            for (final String var5 : var3) {
                final ResourceLocation var6 = this.getItemLocation(var5);
                this.itemLocations.put(var5, var6);
                if (this.models.get(var6) == null) {
                    try {
                        final ModelBlock var7 = this.loadModel(var6);
                        this.models.put(var6, var7);
                    }
                    catch (Exception var8) {
                        ModelBakery.LOGGER.warn("Unable to load item model: '" + var6 + "' for item: '" + Item.itemRegistry.getNameForObject(var2) + "'", (Throwable)var8);
                    }
                }
            }
        }
    }
    
    private void registerVariantNames() {
        this.variantNames.put(Item.getItemFromBlock(Blocks.stone), Lists.newArrayList((Object[])new String[] { "stone", "granite", "granite_smooth", "diorite", "diorite_smooth", "andesite", "andesite_smooth" }));
        this.variantNames.put(Item.getItemFromBlock(Blocks.dirt), Lists.newArrayList((Object[])new String[] { "dirt", "coarse_dirt", "podzol" }));
        this.variantNames.put(Item.getItemFromBlock(Blocks.planks), Lists.newArrayList((Object[])new String[] { "oak_planks", "spruce_planks", "birch_planks", "jungle_planks", "acacia_planks", "dark_oak_planks" }));
        this.variantNames.put(Item.getItemFromBlock(Blocks.sapling), Lists.newArrayList((Object[])new String[] { "oak_sapling", "spruce_sapling", "birch_sapling", "jungle_sapling", "acacia_sapling", "dark_oak_sapling" }));
        this.variantNames.put(Item.getItemFromBlock(Blocks.sand), Lists.newArrayList((Object[])new String[] { "sand", "red_sand" }));
        this.variantNames.put(Item.getItemFromBlock(Blocks.log), Lists.newArrayList((Object[])new String[] { "oak_log", "spruce_log", "birch_log", "jungle_log" }));
        this.variantNames.put(Item.getItemFromBlock(Blocks.leaves), Lists.newArrayList((Object[])new String[] { "oak_leaves", "spruce_leaves", "birch_leaves", "jungle_leaves" }));
        this.variantNames.put(Item.getItemFromBlock(Blocks.sponge), Lists.newArrayList((Object[])new String[] { "sponge", "sponge_wet" }));
        this.variantNames.put(Item.getItemFromBlock(Blocks.sandstone), Lists.newArrayList((Object[])new String[] { "sandstone", "chiseled_sandstone", "smooth_sandstone" }));
        this.variantNames.put(Item.getItemFromBlock(Blocks.red_sandstone), Lists.newArrayList((Object[])new String[] { "red_sandstone", "chiseled_red_sandstone", "smooth_red_sandstone" }));
        this.variantNames.put(Item.getItemFromBlock(Blocks.tallgrass), Lists.newArrayList((Object[])new String[] { "dead_bush", "tall_grass", "fern" }));
        this.variantNames.put(Item.getItemFromBlock(Blocks.deadbush), Lists.newArrayList((Object[])new String[] { "dead_bush" }));
        this.variantNames.put(Item.getItemFromBlock(Blocks.wool), Lists.newArrayList((Object[])new String[] { "black_wool", "red_wool", "green_wool", "brown_wool", "blue_wool", "purple_wool", "cyan_wool", "silver_wool", "gray_wool", "pink_wool", "lime_wool", "yellow_wool", "light_blue_wool", "magenta_wool", "orange_wool", "white_wool" }));
        this.variantNames.put(Item.getItemFromBlock(Blocks.yellow_flower), Lists.newArrayList((Object[])new String[] { "dandelion" }));
        this.variantNames.put(Item.getItemFromBlock(Blocks.red_flower), Lists.newArrayList((Object[])new String[] { "poppy", "blue_orchid", "allium", "houstonia", "red_tulip", "orange_tulip", "white_tulip", "pink_tulip", "oxeye_daisy" }));
        this.variantNames.put(Item.getItemFromBlock(Blocks.stone_slab), Lists.newArrayList((Object[])new String[] { "stone_slab", "sandstone_slab", "cobblestone_slab", "brick_slab", "stone_brick_slab", "nether_brick_slab", "quartz_slab" }));
        this.variantNames.put(Item.getItemFromBlock(Blocks.stone_slab2), Lists.newArrayList((Object[])new String[] { "red_sandstone_slab" }));
        this.variantNames.put(Item.getItemFromBlock(Blocks.stained_glass), Lists.newArrayList((Object[])new String[] { "black_stained_glass", "red_stained_glass", "green_stained_glass", "brown_stained_glass", "blue_stained_glass", "purple_stained_glass", "cyan_stained_glass", "silver_stained_glass", "gray_stained_glass", "pink_stained_glass", "lime_stained_glass", "yellow_stained_glass", "light_blue_stained_glass", "magenta_stained_glass", "orange_stained_glass", "white_stained_glass" }));
        this.variantNames.put(Item.getItemFromBlock(Blocks.monster_egg), Lists.newArrayList((Object[])new String[] { "stone_monster_egg", "cobblestone_monster_egg", "stone_brick_monster_egg", "mossy_brick_monster_egg", "cracked_brick_monster_egg", "chiseled_brick_monster_egg" }));
        this.variantNames.put(Item.getItemFromBlock(Blocks.stonebrick), Lists.newArrayList((Object[])new String[] { "stonebrick", "mossy_stonebrick", "cracked_stonebrick", "chiseled_stonebrick" }));
        this.variantNames.put(Item.getItemFromBlock(Blocks.wooden_slab), Lists.newArrayList((Object[])new String[] { "oak_slab", "spruce_slab", "birch_slab", "jungle_slab", "acacia_slab", "dark_oak_slab" }));
        this.variantNames.put(Item.getItemFromBlock(Blocks.cobblestone_wall), Lists.newArrayList((Object[])new String[] { "cobblestone_wall", "mossy_cobblestone_wall" }));
        this.variantNames.put(Item.getItemFromBlock(Blocks.anvil), Lists.newArrayList((Object[])new String[] { "anvil_intact", "anvil_slightly_damaged", "anvil_very_damaged" }));
        this.variantNames.put(Item.getItemFromBlock(Blocks.quartz_block), Lists.newArrayList((Object[])new String[] { "quartz_block", "chiseled_quartz_block", "quartz_column" }));
        this.variantNames.put(Item.getItemFromBlock(Blocks.stained_hardened_clay), Lists.newArrayList((Object[])new String[] { "black_stained_hardened_clay", "red_stained_hardened_clay", "green_stained_hardened_clay", "brown_stained_hardened_clay", "blue_stained_hardened_clay", "purple_stained_hardened_clay", "cyan_stained_hardened_clay", "silver_stained_hardened_clay", "gray_stained_hardened_clay", "pink_stained_hardened_clay", "lime_stained_hardened_clay", "yellow_stained_hardened_clay", "light_blue_stained_hardened_clay", "magenta_stained_hardened_clay", "orange_stained_hardened_clay", "white_stained_hardened_clay" }));
        this.variantNames.put(Item.getItemFromBlock(Blocks.stained_glass_pane), Lists.newArrayList((Object[])new String[] { "black_stained_glass_pane", "red_stained_glass_pane", "green_stained_glass_pane", "brown_stained_glass_pane", "blue_stained_glass_pane", "purple_stained_glass_pane", "cyan_stained_glass_pane", "silver_stained_glass_pane", "gray_stained_glass_pane", "pink_stained_glass_pane", "lime_stained_glass_pane", "yellow_stained_glass_pane", "light_blue_stained_glass_pane", "magenta_stained_glass_pane", "orange_stained_glass_pane", "white_stained_glass_pane" }));
        this.variantNames.put(Item.getItemFromBlock(Blocks.leaves2), Lists.newArrayList((Object[])new String[] { "acacia_leaves", "dark_oak_leaves" }));
        this.variantNames.put(Item.getItemFromBlock(Blocks.log2), Lists.newArrayList((Object[])new String[] { "acacia_log", "dark_oak_log" }));
        this.variantNames.put(Item.getItemFromBlock(Blocks.prismarine), Lists.newArrayList((Object[])new String[] { "prismarine", "prismarine_bricks", "dark_prismarine" }));
        this.variantNames.put(Item.getItemFromBlock(Blocks.carpet), Lists.newArrayList((Object[])new String[] { "black_carpet", "red_carpet", "green_carpet", "brown_carpet", "blue_carpet", "purple_carpet", "cyan_carpet", "silver_carpet", "gray_carpet", "pink_carpet", "lime_carpet", "yellow_carpet", "light_blue_carpet", "magenta_carpet", "orange_carpet", "white_carpet" }));
        this.variantNames.put(Item.getItemFromBlock(Blocks.double_plant), Lists.newArrayList((Object[])new String[] { "sunflower", "syringa", "double_grass", "double_fern", "double_rose", "paeonia" }));
        this.variantNames.put(Items.bow, Lists.newArrayList((Object[])new String[] { "bow", "bow_pulling_0", "bow_pulling_1", "bow_pulling_2" }));
        this.variantNames.put(Items.coal, Lists.newArrayList((Object[])new String[] { "coal", "charcoal" }));
        this.variantNames.put(Items.fishing_rod, Lists.newArrayList((Object[])new String[] { "fishing_rod", "fishing_rod_cast" }));
        this.variantNames.put(Items.fish, Lists.newArrayList((Object[])new String[] { "cod", "salmon", "clownfish", "pufferfish" }));
        this.variantNames.put(Items.cooked_fish, Lists.newArrayList((Object[])new String[] { "cooked_cod", "cooked_salmon" }));
        this.variantNames.put(Items.dye, Lists.newArrayList((Object[])new String[] { "dye_black", "dye_red", "dye_green", "dye_brown", "dye_blue", "dye_purple", "dye_cyan", "dye_silver", "dye_gray", "dye_pink", "dye_lime", "dye_yellow", "dye_light_blue", "dye_magenta", "dye_orange", "dye_white" }));
        this.variantNames.put(Items.potionitem, Lists.newArrayList((Object[])new String[] { "bottle_drinkable", "bottle_splash" }));
        this.variantNames.put(Items.skull, Lists.newArrayList((Object[])new String[] { "skull_skeleton", "skull_wither", "skull_zombie", "skull_char", "skull_creeper" }));
        this.variantNames.put(Item.getItemFromBlock(Blocks.oak_fence_gate), Lists.newArrayList((Object[])new String[] { "oak_fence_gate" }));
        this.variantNames.put(Item.getItemFromBlock(Blocks.oak_fence), Lists.newArrayList((Object[])new String[] { "oak_fence" }));
        this.variantNames.put(Items.oak_door, Lists.newArrayList((Object[])new String[] { "oak_door" }));
    }
    
    private List getVariantNames(final Item p_177596_1_) {
        List var2 = this.variantNames.get(p_177596_1_);
        if (var2 == null) {
            var2 = Collections.singletonList(((ResourceLocation)Item.itemRegistry.getNameForObject(p_177596_1_)).toString());
        }
        return var2;
    }
    
    private ResourceLocation getItemLocation(final String p_177583_1_) {
        final ResourceLocation var2 = new ResourceLocation(p_177583_1_);
        return new ResourceLocation(var2.getResourceDomain(), "item/" + var2.getResourcePath());
    }
    
    private void bakeBlockModels() {
        for (final ModelResourceLocation var2 : this.variants.keySet()) {
            final WeightedBakedModel.Builder var3 = new WeightedBakedModel.Builder();
            int var4 = 0;
            for (final ModelBlockDefinition.Variant var6 : this.variants.get(var2).getVariants()) {
                final ModelBlock var7 = this.models.get(var6.getModelLocation());
                if (var7 != null && var7.isResolved()) {
                    ++var4;
                    var3.add(this.bakeModel(var7, var6.getRotation(), var6.isUvLocked()), var6.getWeight());
                }
                else {
                    ModelBakery.LOGGER.warn("Missing model for: " + var2);
                }
            }
            if (var4 == 0) {
                ModelBakery.LOGGER.warn("No weighted models for: " + var2);
            }
            else if (var4 == 1) {
                this.bakedRegistry.putObject(var2, var3.first());
            }
            else {
                this.bakedRegistry.putObject(var2, var3.build());
            }
        }
        for (final Map.Entry var8 : this.itemLocations.entrySet()) {
            final ResourceLocation var9 = var8.getValue();
            final ModelResourceLocation var10 = new ModelResourceLocation(var8.getKey(), "inventory");
            final ModelBlock var11 = this.models.get(var9);
            if (var11 != null && var11.isResolved()) {
                if (this.isCustomRenderer(var11)) {
                    this.bakedRegistry.putObject(var10, new BuiltInModel(new ItemCameraTransforms(var11.getThirdPersonTransform(), var11.getFirstPersonTransform(), var11.getHeadTransform(), var11.getInGuiTransform())));
                }
                else {
                    this.bakedRegistry.putObject(var10, this.bakeModel(var11, ModelRotation.X0_Y0, false));
                }
            }
            else {
                ModelBakery.LOGGER.warn("Missing model for: " + var9);
            }
        }
    }
    
    private Set func_177575_g() {
        final HashSet var1 = Sets.newHashSet();
        final ArrayList var2 = Lists.newArrayList((Iterable)this.variants.keySet());
        Collections.sort((List<Object>)var2, new Comparator() {
            private static final String __OBFID = "CL_00002390";
            
            public int func_177505_a(final ModelResourceLocation p_177505_1_, final ModelResourceLocation p_177505_2_) {
                return p_177505_1_.toString().compareTo(p_177505_2_.toString());
            }
            
            @Override
            public int compare(final Object p_compare_1_, final Object p_compare_2_) {
                return this.func_177505_a((ModelResourceLocation)p_compare_1_, (ModelResourceLocation)p_compare_2_);
            }
        });
        for (final ModelResourceLocation var4 : var2) {
            final ModelBlockDefinition.Variants var5 = this.variants.get(var4);
            for (final ModelBlockDefinition.Variant var7 : var5.getVariants()) {
                final ModelBlock var8 = this.models.get(var7.getModelLocation());
                if (var8 == null) {
                    ModelBakery.LOGGER.warn("Missing model for: " + var4);
                }
                else {
                    var1.addAll(this.func_177585_a(var8));
                }
            }
        }
        var1.addAll(ModelBakery.field_177602_b);
        return var1;
    }
    
    private IBakedModel bakeModel(final ModelBlock p_177578_1_, final ModelRotation p_177578_2_, final boolean p_177578_3_) {
        final TextureAtlasSprite var4 = this.field_177599_g.get(new ResourceLocation(p_177578_1_.resolveTextureName("particle")));
        final SimpleBakedModel.Builder var5 = new SimpleBakedModel.Builder(p_177578_1_).func_177646_a(var4);
        for (final BlockPart var7 : p_177578_1_.getElements()) {
            for (final EnumFacing var9 : var7.field_178240_c.keySet()) {
                final BlockPartFace var10 = var7.field_178240_c.get(var9);
                final TextureAtlasSprite var11 = this.field_177599_g.get(new ResourceLocation(p_177578_1_.resolveTextureName(var10.field_178242_d)));
                if (var10.field_178244_b == null) {
                    var5.func_177648_a(this.func_177589_a(var7, var10, var11, var9, p_177578_2_, p_177578_3_));
                }
                else {
                    var5.func_177650_a(p_177578_2_.func_177523_a(var10.field_178244_b), this.func_177589_a(var7, var10, var11, var9, p_177578_2_, p_177578_3_));
                }
            }
        }
        return var5.func_177645_b();
    }
    
    private BakedQuad func_177589_a(final BlockPart p_177589_1_, final BlockPartFace p_177589_2_, final TextureAtlasSprite p_177589_3_, final EnumFacing p_177589_4_, final ModelRotation p_177589_5_, final boolean p_177589_6_) {
        return this.field_177607_l.func_178414_a(p_177589_1_.field_178241_a, p_177589_1_.field_178239_b, p_177589_2_, p_177589_3_, p_177589_4_, p_177589_5_, p_177589_1_.field_178237_d, p_177589_6_, p_177589_1_.field_178238_e);
    }
    
    private void func_177597_h() {
        this.func_177574_i();
        for (final ModelBlock var2 : this.models.values()) {
            var2.getParentFromMap(this.models);
        }
        ModelBlock.func_178312_b(this.models);
    }
    
    private void func_177574_i() {
        final ArrayDeque var1 = Queues.newArrayDeque();
        final HashSet var2 = Sets.newHashSet();
        for (final ResourceLocation var4 : this.models.keySet()) {
            var2.add(var4);
            final ResourceLocation var5 = this.models.get(var4).getParentLocation();
            if (var5 != null) {
                var1.add(var5);
            }
        }
        while (!var1.isEmpty()) {
            final ResourceLocation var6 = var1.pop();
            try {
                if (this.models.get(var6) != null) {
                    continue;
                }
                final ModelBlock var7 = this.loadModel(var6);
                this.models.put(var6, var7);
                final ResourceLocation var5 = var7.getParentLocation();
                if (var5 != null && !var2.contains(var5)) {
                    var1.add(var5);
                }
            }
            catch (Exception var8) {
                ModelBakery.LOGGER.warn("In parent chain: " + ModelBakery.field_177601_e.join((Iterable)this.func_177573_e(var6)) + "; unable to load model: '" + var6 + "'", (Throwable)var8);
            }
            var2.add(var6);
        }
    }
    
    private List func_177573_e(final ResourceLocation p_177573_1_) {
        final ArrayList var2 = Lists.newArrayList((Object[])new ResourceLocation[] { p_177573_1_ });
        ResourceLocation var3 = p_177573_1_;
        while ((var3 = this.func_177576_f(var3)) != null) {
            var2.add(0, var3);
        }
        return var2;
    }
    
    private ResourceLocation func_177576_f(final ResourceLocation p_177576_1_) {
        for (final Map.Entry var3 : this.models.entrySet()) {
            final ModelBlock var4 = var3.getValue();
            if (var4 != null && p_177576_1_.equals(var4.getParentLocation())) {
                return var3.getKey();
            }
        }
        return null;
    }
    
    private Set func_177585_a(final ModelBlock p_177585_1_) {
        final HashSet var2 = Sets.newHashSet();
        for (final BlockPart var4 : p_177585_1_.getElements()) {
            for (final BlockPartFace var6 : var4.field_178240_c.values()) {
                final ResourceLocation var7 = new ResourceLocation(p_177585_1_.resolveTextureName(var6.field_178242_d));
                var2.add(var7);
            }
        }
        var2.add(new ResourceLocation(p_177585_1_.resolveTextureName("particle")));
        return var2;
    }
    
    private void func_177572_j() {
        final Set var1 = this.func_177575_g();
        var1.addAll(this.func_177571_k());
        var1.remove(TextureMap.field_174945_f);
        final IIconCreator var2 = new IIconCreator() {
            private static final String __OBFID = "CL_00002389";
            
            @Override
            public void func_177059_a(final TextureMap p_177059_1_) {
                for (final ResourceLocation var3 : var1) {
                    final TextureAtlasSprite var4 = p_177059_1_.func_174942_a(var3);
                    ModelBakery.this.field_177599_g.put(var3, var4);
                }
            }
        };
        this.textureMap.func_174943_a(this.resourceManager, var2);
        this.field_177599_g.put(new ResourceLocation("missingno"), this.textureMap.func_174944_f());
    }
    
    private Set func_177571_k() {
        final HashSet var1 = Sets.newHashSet();
        for (final ResourceLocation var3 : this.itemLocations.values()) {
            final ModelBlock var4 = this.models.get(var3);
            if (var4 != null) {
                var1.add(new ResourceLocation(var4.resolveTextureName("particle")));
                if (this.func_177581_b(var4)) {
                    for (final String var6 : ItemModelGenerator.LAYERS) {
                        final ResourceLocation var7 = new ResourceLocation(var4.resolveTextureName(var6));
                        if (var4.getRootModel() == ModelBakery.MODEL_COMPASS && !TextureMap.field_174945_f.equals(var7)) {
                            TextureAtlasSprite.func_176603_b(var7.toString());
                        }
                        else if (var4.getRootModel() == ModelBakery.MODEL_CLOCK && !TextureMap.field_174945_f.equals(var7)) {
                            TextureAtlasSprite.func_176602_a(var7.toString());
                        }
                        var1.add(var7);
                    }
                }
                else {
                    if (this.isCustomRenderer(var4)) {
                        continue;
                    }
                    for (final BlockPart var8 : var4.getElements()) {
                        for (final BlockPartFace var10 : var8.field_178240_c.values()) {
                            final ResourceLocation var11 = new ResourceLocation(var4.resolveTextureName(var10.field_178242_d));
                            var1.add(var11);
                        }
                    }
                }
            }
        }
        return var1;
    }
    
    private boolean func_177581_b(final ModelBlock p_177581_1_) {
        if (p_177581_1_ == null) {
            return false;
        }
        final ModelBlock var2 = p_177581_1_.getRootModel();
        return var2 == ModelBakery.MODEL_GENERATED || var2 == ModelBakery.MODEL_COMPASS || var2 == ModelBakery.MODEL_CLOCK;
    }
    
    private boolean isCustomRenderer(final ModelBlock p_177587_1_) {
        if (p_177587_1_ == null) {
            return false;
        }
        final ModelBlock var2 = p_177587_1_.getRootModel();
        return var2 == ModelBakery.MODEL_ENTITY;
    }
    
    private void bakeItemModels() {
        for (final ResourceLocation var2 : this.itemLocations.values()) {
            final ModelBlock var3 = this.models.get(var2);
            if (this.func_177581_b(var3)) {
                final ModelBlock var4 = this.func_177582_d(var3);
                if (var4 != null) {
                    var4.field_178317_b = var2.toString();
                }
                this.models.put(var2, var4);
            }
            else {
                if (!this.isCustomRenderer(var3)) {
                    continue;
                }
                this.models.put(var2, var3);
            }
        }
        for (final TextureAtlasSprite var5 : this.field_177599_g.values()) {
            if (!var5.hasAnimationMetadata()) {
                var5.clearFramesTextureData();
            }
        }
    }
    
    private ModelBlock func_177582_d(final ModelBlock p_177582_1_) {
        return this.itemModelGenerator.func_178392_a(this.textureMap, p_177582_1_);
    }
    
    static {
        field_177602_b = Sets.newHashSet((Object[])new ResourceLocation[] { new ResourceLocation("blocks/water_flow"), new ResourceLocation("blocks/water_still"), new ResourceLocation("blocks/lava_flow"), new ResourceLocation("blocks/lava_still"), new ResourceLocation("blocks/destroy_stage_0"), new ResourceLocation("blocks/destroy_stage_1"), new ResourceLocation("blocks/destroy_stage_2"), new ResourceLocation("blocks/destroy_stage_3"), new ResourceLocation("blocks/destroy_stage_4"), new ResourceLocation("blocks/destroy_stage_5"), new ResourceLocation("blocks/destroy_stage_6"), new ResourceLocation("blocks/destroy_stage_7"), new ResourceLocation("blocks/destroy_stage_8"), new ResourceLocation("blocks/destroy_stage_9"), new ResourceLocation("items/empty_armor_slot_helmet"), new ResourceLocation("items/empty_armor_slot_chestplate"), new ResourceLocation("items/empty_armor_slot_leggings"), new ResourceLocation("items/empty_armor_slot_boots") });
        LOGGER = LogManager.getLogger();
        MODEL_MISSING = new ModelResourceLocation("builtin/missing", "missing");
        (BUILT_IN_MODELS = Maps.newHashMap()).put("missing", "{ \"textures\": {   \"particle\": \"missingno\",   \"missingno\": \"missingno\"}, \"elements\": [ {     \"from\": [ 0, 0, 0 ],     \"to\": [ 16, 16, 16 ],     \"faces\": {         \"down\":  { \"uv\": [ 0, 0, 16, 16 ], \"cullface\": \"down\", \"texture\": \"#missingno\" },         \"up\":    { \"uv\": [ 0, 0, 16, 16 ], \"cullface\": \"up\", \"texture\": \"#missingno\" },         \"north\": { \"uv\": [ 0, 0, 16, 16 ], \"cullface\": \"north\", \"texture\": \"#missingno\" },         \"south\": { \"uv\": [ 0, 0, 16, 16 ], \"cullface\": \"south\", \"texture\": \"#missingno\" },         \"west\":  { \"uv\": [ 0, 0, 16, 16 ], \"cullface\": \"west\", \"texture\": \"#missingno\" },         \"east\":  { \"uv\": [ 0, 0, 16, 16 ], \"cullface\": \"east\", \"texture\": \"#missingno\" }    }}]}");
        field_177601_e = Joiner.on(" -> ");
        MODEL_GENERATED = ModelBlock.deserialize("{\"elements\":[{  \"from\": [0, 0, 0],   \"to\": [16, 16, 16],   \"faces\": {       \"down\": {\"uv\": [0, 0, 16, 16], \"texture\":\"\"}   }}]}");
        MODEL_COMPASS = ModelBlock.deserialize("{\"elements\":[{  \"from\": [0, 0, 0],   \"to\": [16, 16, 16],   \"faces\": {       \"down\": {\"uv\": [0, 0, 16, 16], \"texture\":\"\"}   }}]}");
        MODEL_CLOCK = ModelBlock.deserialize("{\"elements\":[{  \"from\": [0, 0, 0],   \"to\": [16, 16, 16],   \"faces\": {       \"down\": {\"uv\": [0, 0, 16, 16], \"texture\":\"\"}   }}]}");
        MODEL_ENTITY = ModelBlock.deserialize("{\"elements\":[{  \"from\": [0, 0, 0],   \"to\": [16, 16, 16],   \"faces\": {       \"down\": {\"uv\": [0, 0, 16, 16], \"texture\":\"\"}   }}]}");
        ModelBakery.MODEL_GENERATED.field_178317_b = "generation marker";
        ModelBakery.MODEL_COMPASS.field_178317_b = "compass generation marker";
        ModelBakery.MODEL_CLOCK.field_178317_b = "class generation marker";
        ModelBakery.MODEL_ENTITY.field_178317_b = "block entity marker";
    }
}
