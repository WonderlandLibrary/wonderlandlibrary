// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.block.model;

import org.apache.logging.log4j.LogManager;
import net.optifine.util.StrUtils;
import net.minecraft.client.renderer.texture.ITextureMapPopulator;
import java.util.Deque;
import com.google.common.collect.Queues;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.common.model.ITransformation;
import net.minecraft.util.EnumFacing;
import java.util.Comparator;
import net.minecraft.client.renderer.block.model.multipart.Multipart;
import net.minecraft.client.renderer.block.model.multipart.Selector;
import java.util.Collections;
import net.minecraft.init.Items;
import net.minecraft.init.Blocks;
import java.io.Closeable;
import net.optifine.util.TextureUtils;
import java.io.StringReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import java.io.Reader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.io.IOException;
import net.minecraft.client.resources.IResource;
import net.optifine.CustomItems;
import net.optifine.reflect.Reflector;
import net.minecraft.block.state.IBlockState;
import com.google.common.collect.Iterables;
import javax.annotation.Nullable;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.Iterator;
import net.minecraft.client.renderer.block.statemap.BlockStateMapper;
import net.minecraft.block.Block;
import net.minecraft.util.registry.IRegistry;
import com.google.common.collect.Maps;
import net.minecraftforge.registries.IRegistryDelegate;
import java.util.List;
import net.minecraft.item.Item;
import net.minecraft.util.registry.RegistrySimple;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.texture.TextureMap;
import java.util.Collection;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.IResourceManager;
import com.google.common.base.Joiner;
import java.util.Map;
import org.apache.logging.log4j.Logger;
import net.minecraft.util.ResourceLocation;
import java.util.Set;

public class ModelBakery
{
    private static final Set<ResourceLocation> LOCATIONS_BUILTIN_TEXTURES;
    private static final Logger LOGGER;
    protected static final ModelResourceLocation MODEL_MISSING;
    private static final String MISSING_MODEL_MESH;
    private static final Map<String, String> BUILT_IN_MODELS;
    private static final Joiner JOINER;
    private final IResourceManager resourceManager;
    private final Map<ResourceLocation, TextureAtlasSprite> sprites;
    private final Map<ResourceLocation, ModelBlock> models;
    private final Map<ModelResourceLocation, VariantList> variants;
    private final Map<ModelBlockDefinition, Collection<ModelResourceLocation>> multipartVariantMap;
    private final TextureMap textureMap;
    private final BlockModelShapes blockModelShapes;
    private final FaceBakery faceBakery;
    private final ItemModelGenerator itemModelGenerator;
    private final RegistrySimple<ModelResourceLocation, IBakedModel> bakedRegistry;
    private static final String EMPTY_MODEL_RAW;
    private static final ModelBlock MODEL_GENERATED;
    private static final ModelBlock MODEL_ENTITY;
    private final Map<String, ResourceLocation> itemLocations;
    private final Map<ResourceLocation, ModelBlockDefinition> blockDefinitions;
    private final Map<Item, List<String>> variantNames;
    private static Map<IRegistryDelegate<Item>, Set<String>> customVariantNames;
    
    public ModelBakery(final IResourceManager resourceManagerIn, final TextureMap textureMapIn, final BlockModelShapes blockModelShapesIn) {
        this.sprites = (Map<ResourceLocation, TextureAtlasSprite>)Maps.newHashMap();
        this.models = (Map<ResourceLocation, ModelBlock>)Maps.newLinkedHashMap();
        this.variants = (Map<ModelResourceLocation, VariantList>)Maps.newLinkedHashMap();
        this.multipartVariantMap = (Map<ModelBlockDefinition, Collection<ModelResourceLocation>>)Maps.newLinkedHashMap();
        this.faceBakery = new FaceBakery();
        this.itemModelGenerator = new ItemModelGenerator();
        this.bakedRegistry = new RegistrySimple<ModelResourceLocation, IBakedModel>();
        this.itemLocations = (Map<String, ResourceLocation>)Maps.newLinkedHashMap();
        this.blockDefinitions = (Map<ResourceLocation, ModelBlockDefinition>)Maps.newHashMap();
        this.variantNames = (Map<Item, List<String>>)Maps.newIdentityHashMap();
        this.resourceManager = resourceManagerIn;
        this.textureMap = textureMapIn;
        this.blockModelShapes = blockModelShapesIn;
    }
    
    public IRegistry<ModelResourceLocation, IBakedModel> setupModelRegistry() {
        this.loadBlocks();
        this.loadVariantItemModels();
        this.loadModelsCheck();
        this.loadSprites();
        this.makeItemModels();
        this.bakeBlockModels();
        this.bakeItemModels();
        return this.bakedRegistry;
    }
    
    private void loadBlocks() {
        final BlockStateMapper blockstatemapper = this.blockModelShapes.getBlockStateMapper();
        for (final Block block : Block.REGISTRY) {
            for (final ResourceLocation resourcelocation : blockstatemapper.getBlockstateLocations(block)) {
                try {
                    this.loadBlock(blockstatemapper, block, resourcelocation);
                }
                catch (Exception exception) {
                    ModelBakery.LOGGER.warn("Unable to load definition " + resourcelocation, (Throwable)exception);
                }
            }
        }
    }
    
    protected void loadBlock(final BlockStateMapper p_loadBlock_1_, final Block p_loadBlock_2_, final ResourceLocation p_loadBlock_3_) {
        final ModelBlockDefinition modelblockdefinition = this.getModelBlockDefinition(p_loadBlock_3_);
        final Map<IBlockState, ModelResourceLocation> map = p_loadBlock_1_.getVariants(p_loadBlock_2_);
        if (modelblockdefinition.hasMultipartData()) {
            final Collection<ModelResourceLocation> collection = (Collection<ModelResourceLocation>)Sets.newHashSet((Iterable)map.values());
            modelblockdefinition.getMultipartData().setStateContainer(p_loadBlock_2_.getBlockState());
            Collection<ModelResourceLocation> collection2 = this.multipartVariantMap.get(modelblockdefinition);
            if (collection2 == null) {
                collection2 = (Collection<ModelResourceLocation>)Lists.newArrayList();
            }
            collection2.addAll(Lists.newArrayList(Iterables.filter((Iterable)collection, (Predicate)new Predicate<ModelResourceLocation>() {
                public boolean apply(@Nullable final ModelResourceLocation p_apply_1_) {
                    return p_loadBlock_3_.equals(p_apply_1_);
                }
            })));
            this.registerMultipartVariant(modelblockdefinition, collection2);
        }
        for (final Map.Entry<IBlockState, ModelResourceLocation> entry : map.entrySet()) {
            final ModelResourceLocation modelresourcelocation = entry.getValue();
            if (p_loadBlock_3_.equals(modelresourcelocation)) {
                try {
                    if (Reflector.ForgeItem_delegate.exists()) {
                        this.registerVariant(modelblockdefinition, modelresourcelocation);
                    }
                    else {
                        this.variants.put(modelresourcelocation, modelblockdefinition.getVariant(modelresourcelocation.getVariant()));
                    }
                }
                catch (RuntimeException runtimeexception) {
                    if (modelblockdefinition.hasMultipartData()) {
                        continue;
                    }
                    ModelBakery.LOGGER.warn("Unable to load variant: " + modelresourcelocation.getVariant() + " from " + modelresourcelocation, (Throwable)runtimeexception);
                }
            }
        }
    }
    
    private void loadVariantItemModels() {
        this.variants.put(ModelBakery.MODEL_MISSING, new VariantList(Lists.newArrayList((Object[])new Variant[] { new Variant(new ResourceLocation(ModelBakery.MODEL_MISSING.getPath()), ModelRotation.X0_Y0, false, 1) })));
        this.loadStaticModels();
        this.loadVariantModels();
        this.loadMultipartVariantModels();
        this.loadItemModels();
        CustomItems.update();
        CustomItems.loadModels(this);
    }
    
    private void loadStaticModels() {
        final ResourceLocation resourcelocation = new ResourceLocation("item_frame");
        final ModelBlockDefinition modelblockdefinition = this.getModelBlockDefinition(resourcelocation);
        this.registerVariant(modelblockdefinition, new ModelResourceLocation(resourcelocation, "normal"));
        this.registerVariant(modelblockdefinition, new ModelResourceLocation(resourcelocation, "map"));
    }
    
    private void registerVariant(final ModelBlockDefinition blockstateDefinition, final ModelResourceLocation location) {
        try {
            this.variants.put(location, blockstateDefinition.getVariant(location.getVariant()));
        }
        catch (RuntimeException var4) {
            if (!blockstateDefinition.hasMultipartData()) {
                ModelBakery.LOGGER.warn("Unable to load variant: {} from {}", (Object)location.getVariant(), (Object)location);
            }
        }
    }
    
    private ModelBlockDefinition getModelBlockDefinition(final ResourceLocation location) {
        final ResourceLocation resourcelocation = this.getBlockstateLocation(location);
        ModelBlockDefinition modelblockdefinition = this.blockDefinitions.get(resourcelocation);
        if (modelblockdefinition == null) {
            modelblockdefinition = this.loadMultipartMBD(location, resourcelocation);
            this.blockDefinitions.put(resourcelocation, modelblockdefinition);
        }
        return modelblockdefinition;
    }
    
    private ModelBlockDefinition loadMultipartMBD(final ResourceLocation location, final ResourceLocation fileIn) {
        final List<ModelBlockDefinition> list = (List<ModelBlockDefinition>)Lists.newArrayList();
        try {
            for (final IResource iresource : this.resourceManager.getAllResources(fileIn)) {
                list.add(this.loadModelBlockDefinition(location, iresource));
            }
        }
        catch (IOException ioexception) {
            throw new RuntimeException("Encountered an exception when loading model definition of model " + fileIn, ioexception);
        }
        return new ModelBlockDefinition(list);
    }
    
    private ModelBlockDefinition loadModelBlockDefinition(final ResourceLocation location, final IResource resource) {
        InputStream inputstream = null;
        ModelBlockDefinition modelblockdefinition;
        try {
            inputstream = resource.getInputStream();
            if (Reflector.ForgeModelBlockDefinition_parseFromReader2.exists()) {
                modelblockdefinition = (ModelBlockDefinition)Reflector.call(Reflector.ForgeModelBlockDefinition_parseFromReader2, new InputStreamReader(inputstream, StandardCharsets.UTF_8), location);
            }
            else {
                modelblockdefinition = ModelBlockDefinition.parseFromReader(new InputStreamReader(inputstream, StandardCharsets.UTF_8));
            }
        }
        catch (Exception exception) {
            throw new RuntimeException("Encountered an exception when loading model definition of '" + location + "' from: '" + resource.getResourceLocation() + "' in resourcepack: '" + resource.getResourcePackName() + "'", exception);
        }
        finally {
            IOUtils.closeQuietly(inputstream);
        }
        return modelblockdefinition;
    }
    
    private ResourceLocation getBlockstateLocation(final ResourceLocation location) {
        return new ResourceLocation(location.getNamespace(), "blockstates/" + location.getPath() + ".json");
    }
    
    private void loadVariantModels() {
        for (final Map.Entry<ModelResourceLocation, VariantList> entry : this.variants.entrySet()) {
            this.loadVariantList(entry.getKey(), entry.getValue());
        }
    }
    
    private void loadMultipartVariantModels() {
        for (final Map.Entry<ModelBlockDefinition, Collection<ModelResourceLocation>> entry : this.multipartVariantMap.entrySet()) {
            final ModelResourceLocation modelresourcelocation = entry.getValue().iterator().next();
            for (final VariantList variantlist : entry.getKey().getMultipartVariants()) {
                this.loadVariantList(modelresourcelocation, variantlist);
            }
        }
    }
    
    private void loadVariantList(final ModelResourceLocation p_188638_1_, final VariantList p_188638_2_) {
        for (final Variant variant : p_188638_2_.getVariantList()) {
            final ResourceLocation resourcelocation = variant.getModelLocation();
            if (this.models.get(resourcelocation) == null) {
                try {
                    this.models.put(resourcelocation, this.loadModel(resourcelocation));
                }
                catch (Exception exception) {
                    ModelBakery.LOGGER.warn("Unable to load block model: '{}' for variant: '{}': {} ", (Object)resourcelocation, (Object)p_188638_1_, (Object)exception);
                }
            }
        }
    }
    
    private ModelBlock loadModel(ResourceLocation location) throws IOException {
        Reader reader = null;
        IResource iresource = null;
        ModelBlock modelblock8;
        try {
            final String s = location.getPath();
            if ("builtin/generated".equals(s)) {
                final ModelBlock modelblock4 = ModelBakery.MODEL_GENERATED;
                return modelblock4;
            }
            if (!"builtin/entity".equals(s)) {
                if (s.startsWith("builtin/")) {
                    final String s2 = s.substring("builtin/".length());
                    final String s3 = ModelBakery.BUILT_IN_MODELS.get(s2);
                    if (s3 == null) {
                        throw new FileNotFoundException(location.toString());
                    }
                    reader = new StringReader(s3);
                }
                else {
                    location = this.getModelLocation(location);
                    iresource = this.resourceManager.getResource(location);
                    reader = new InputStreamReader(iresource.getInputStream(), StandardCharsets.UTF_8);
                }
                final ModelBlock modelblock5 = ModelBlock.deserialize(reader);
                modelblock5.name = location.toString();
                final String s4 = TextureUtils.getBasePath(location.getPath());
                fixModelLocations(modelblock5, s4);
                final ModelBlock modelblock6 = modelblock5;
                return modelblock6;
            }
            final ModelBlock modelblock7 = modelblock8 = ModelBakery.MODEL_ENTITY;
        }
        finally {
            IOUtils.closeQuietly(reader);
            IOUtils.closeQuietly((Closeable)iresource);
        }
        return modelblock8;
    }
    
    private ResourceLocation getModelLocation(ResourceLocation location) {
        final String s = location.getPath();
        if (!s.startsWith("mcpatcher") && !s.startsWith("optifine")) {
            return new ResourceLocation(location.getNamespace(), "models/" + location.getPath() + ".json");
        }
        if (!s.endsWith(".json")) {
            location = new ResourceLocation(location.getNamespace(), s + ".json");
        }
        return location;
    }
    
    private void loadItemModels() {
        this.registerVariantNames();
        for (final Item item : Item.REGISTRY) {
            for (final String s : this.getVariantNames(item)) {
                final ResourceLocation resourcelocation = this.getItemLocation(s);
                final ResourceLocation resourcelocation2 = Item.REGISTRY.getNameForObject(item);
                this.loadItemModel(s, resourcelocation, resourcelocation2);
                if (item.hasCustomProperties()) {
                    final ModelBlock modelblock = this.models.get(resourcelocation);
                    if (modelblock == null) {
                        continue;
                    }
                    for (final ResourceLocation resourcelocation3 : modelblock.getOverrideLocations()) {
                        this.loadItemModel(resourcelocation3.toString(), resourcelocation3, resourcelocation2);
                    }
                }
            }
        }
    }
    
    public void loadItemModel(final String variantName, final ResourceLocation location, final ResourceLocation itemName) {
        this.itemLocations.put(variantName, location);
        if (this.models.get(location) == null) {
            try {
                final ModelBlock modelblock = this.loadModel(location);
                this.models.put(location, modelblock);
            }
            catch (Exception exception1) {
                ModelBakery.LOGGER.warn("Unable to load item model: '{}' for item: '{}'", (Object)location, (Object)itemName);
                ModelBakery.LOGGER.warn(exception1.getClass().getName() + ": " + exception1.getMessage());
            }
        }
    }
    
    private void registerVariantNames() {
        this.variantNames.clear();
        this.variantNames.put(Item.getItemFromBlock(Blocks.STONE), Lists.newArrayList((Object[])new String[] { "stone", "granite", "granite_smooth", "diorite", "diorite_smooth", "andesite", "andesite_smooth" }));
        this.variantNames.put(Item.getItemFromBlock(Blocks.DIRT), Lists.newArrayList((Object[])new String[] { "dirt", "coarse_dirt", "podzol" }));
        this.variantNames.put(Item.getItemFromBlock(Blocks.PLANKS), Lists.newArrayList((Object[])new String[] { "oak_planks", "spruce_planks", "birch_planks", "jungle_planks", "acacia_planks", "dark_oak_planks" }));
        this.variantNames.put(Item.getItemFromBlock(Blocks.SAPLING), Lists.newArrayList((Object[])new String[] { "oak_sapling", "spruce_sapling", "birch_sapling", "jungle_sapling", "acacia_sapling", "dark_oak_sapling" }));
        this.variantNames.put(Item.getItemFromBlock(Blocks.SAND), Lists.newArrayList((Object[])new String[] { "sand", "red_sand" }));
        this.variantNames.put(Item.getItemFromBlock(Blocks.LOG), Lists.newArrayList((Object[])new String[] { "oak_log", "spruce_log", "birch_log", "jungle_log" }));
        this.variantNames.put(Item.getItemFromBlock(Blocks.LEAVES), Lists.newArrayList((Object[])new String[] { "oak_leaves", "spruce_leaves", "birch_leaves", "jungle_leaves" }));
        this.variantNames.put(Item.getItemFromBlock(Blocks.SPONGE), Lists.newArrayList((Object[])new String[] { "sponge", "sponge_wet" }));
        this.variantNames.put(Item.getItemFromBlock(Blocks.SANDSTONE), Lists.newArrayList((Object[])new String[] { "sandstone", "chiseled_sandstone", "smooth_sandstone" }));
        this.variantNames.put(Item.getItemFromBlock(Blocks.RED_SANDSTONE), Lists.newArrayList((Object[])new String[] { "red_sandstone", "chiseled_red_sandstone", "smooth_red_sandstone" }));
        this.variantNames.put(Item.getItemFromBlock(Blocks.TALLGRASS), Lists.newArrayList((Object[])new String[] { "dead_bush", "tall_grass", "fern" }));
        this.variantNames.put(Item.getItemFromBlock(Blocks.DEADBUSH), Lists.newArrayList((Object[])new String[] { "dead_bush" }));
        this.variantNames.put(Item.getItemFromBlock(Blocks.WOOL), Lists.newArrayList((Object[])new String[] { "black_wool", "red_wool", "green_wool", "brown_wool", "blue_wool", "purple_wool", "cyan_wool", "silver_wool", "gray_wool", "pink_wool", "lime_wool", "yellow_wool", "light_blue_wool", "magenta_wool", "orange_wool", "white_wool" }));
        this.variantNames.put(Item.getItemFromBlock(Blocks.YELLOW_FLOWER), Lists.newArrayList((Object[])new String[] { "dandelion" }));
        this.variantNames.put(Item.getItemFromBlock(Blocks.RED_FLOWER), Lists.newArrayList((Object[])new String[] { "poppy", "blue_orchid", "allium", "houstonia", "red_tulip", "orange_tulip", "white_tulip", "pink_tulip", "oxeye_daisy" }));
        this.variantNames.put(Item.getItemFromBlock(Blocks.STONE_SLAB), Lists.newArrayList((Object[])new String[] { "stone_slab", "sandstone_slab", "cobblestone_slab", "brick_slab", "stone_brick_slab", "nether_brick_slab", "quartz_slab" }));
        this.variantNames.put(Item.getItemFromBlock(Blocks.STONE_SLAB2), Lists.newArrayList((Object[])new String[] { "red_sandstone_slab" }));
        this.variantNames.put(Item.getItemFromBlock(Blocks.STAINED_GLASS), Lists.newArrayList((Object[])new String[] { "black_stained_glass", "red_stained_glass", "green_stained_glass", "brown_stained_glass", "blue_stained_glass", "purple_stained_glass", "cyan_stained_glass", "silver_stained_glass", "gray_stained_glass", "pink_stained_glass", "lime_stained_glass", "yellow_stained_glass", "light_blue_stained_glass", "magenta_stained_glass", "orange_stained_glass", "white_stained_glass" }));
        this.variantNames.put(Item.getItemFromBlock(Blocks.MONSTER_EGG), Lists.newArrayList((Object[])new String[] { "stone_monster_egg", "cobblestone_monster_egg", "stone_brick_monster_egg", "mossy_brick_monster_egg", "cracked_brick_monster_egg", "chiseled_brick_monster_egg" }));
        this.variantNames.put(Item.getItemFromBlock(Blocks.STONEBRICK), Lists.newArrayList((Object[])new String[] { "stonebrick", "mossy_stonebrick", "cracked_stonebrick", "chiseled_stonebrick" }));
        this.variantNames.put(Item.getItemFromBlock(Blocks.WOODEN_SLAB), Lists.newArrayList((Object[])new String[] { "oak_slab", "spruce_slab", "birch_slab", "jungle_slab", "acacia_slab", "dark_oak_slab" }));
        this.variantNames.put(Item.getItemFromBlock(Blocks.COBBLESTONE_WALL), Lists.newArrayList((Object[])new String[] { "cobblestone_wall", "mossy_cobblestone_wall" }));
        this.variantNames.put(Item.getItemFromBlock(Blocks.ANVIL), Lists.newArrayList((Object[])new String[] { "anvil_intact", "anvil_slightly_damaged", "anvil_very_damaged" }));
        this.variantNames.put(Item.getItemFromBlock(Blocks.QUARTZ_BLOCK), Lists.newArrayList((Object[])new String[] { "quartz_block", "chiseled_quartz_block", "quartz_column" }));
        this.variantNames.put(Item.getItemFromBlock(Blocks.STAINED_HARDENED_CLAY), Lists.newArrayList((Object[])new String[] { "black_stained_hardened_clay", "red_stained_hardened_clay", "green_stained_hardened_clay", "brown_stained_hardened_clay", "blue_stained_hardened_clay", "purple_stained_hardened_clay", "cyan_stained_hardened_clay", "silver_stained_hardened_clay", "gray_stained_hardened_clay", "pink_stained_hardened_clay", "lime_stained_hardened_clay", "yellow_stained_hardened_clay", "light_blue_stained_hardened_clay", "magenta_stained_hardened_clay", "orange_stained_hardened_clay", "white_stained_hardened_clay" }));
        this.variantNames.put(Item.getItemFromBlock(Blocks.STAINED_GLASS_PANE), Lists.newArrayList((Object[])new String[] { "black_stained_glass_pane", "red_stained_glass_pane", "green_stained_glass_pane", "brown_stained_glass_pane", "blue_stained_glass_pane", "purple_stained_glass_pane", "cyan_stained_glass_pane", "silver_stained_glass_pane", "gray_stained_glass_pane", "pink_stained_glass_pane", "lime_stained_glass_pane", "yellow_stained_glass_pane", "light_blue_stained_glass_pane", "magenta_stained_glass_pane", "orange_stained_glass_pane", "white_stained_glass_pane" }));
        this.variantNames.put(Item.getItemFromBlock(Blocks.LEAVES2), Lists.newArrayList((Object[])new String[] { "acacia_leaves", "dark_oak_leaves" }));
        this.variantNames.put(Item.getItemFromBlock(Blocks.LOG2), Lists.newArrayList((Object[])new String[] { "acacia_log", "dark_oak_log" }));
        this.variantNames.put(Item.getItemFromBlock(Blocks.PRISMARINE), Lists.newArrayList((Object[])new String[] { "prismarine", "prismarine_bricks", "dark_prismarine" }));
        this.variantNames.put(Item.getItemFromBlock(Blocks.CARPET), Lists.newArrayList((Object[])new String[] { "black_carpet", "red_carpet", "green_carpet", "brown_carpet", "blue_carpet", "purple_carpet", "cyan_carpet", "silver_carpet", "gray_carpet", "pink_carpet", "lime_carpet", "yellow_carpet", "light_blue_carpet", "magenta_carpet", "orange_carpet", "white_carpet" }));
        this.variantNames.put(Item.getItemFromBlock(Blocks.DOUBLE_PLANT), Lists.newArrayList((Object[])new String[] { "sunflower", "syringa", "double_grass", "double_fern", "double_rose", "paeonia" }));
        this.variantNames.put(Items.COAL, Lists.newArrayList((Object[])new String[] { "coal", "charcoal" }));
        this.variantNames.put(Items.FISH, Lists.newArrayList((Object[])new String[] { "cod", "salmon", "clownfish", "pufferfish" }));
        this.variantNames.put(Items.COOKED_FISH, Lists.newArrayList((Object[])new String[] { "cooked_cod", "cooked_salmon" }));
        this.variantNames.put(Items.DYE, Lists.newArrayList((Object[])new String[] { "dye_black", "dye_red", "dye_green", "dye_brown", "dye_blue", "dye_purple", "dye_cyan", "dye_silver", "dye_gray", "dye_pink", "dye_lime", "dye_yellow", "dye_light_blue", "dye_magenta", "dye_orange", "dye_white" }));
        this.variantNames.put(Items.POTIONITEM, Lists.newArrayList((Object[])new String[] { "bottle_drinkable" }));
        this.variantNames.put(Items.SKULL, Lists.newArrayList((Object[])new String[] { "skull_skeleton", "skull_wither", "skull_zombie", "skull_char", "skull_creeper", "skull_dragon" }));
        this.variantNames.put(Items.SPLASH_POTION, Lists.newArrayList((Object[])new String[] { "bottle_splash" }));
        this.variantNames.put(Items.LINGERING_POTION, Lists.newArrayList((Object[])new String[] { "bottle_lingering" }));
        this.variantNames.put(Item.getItemFromBlock(Blocks.CONCRETE), Lists.newArrayList((Object[])new String[] { "black_concrete", "red_concrete", "green_concrete", "brown_concrete", "blue_concrete", "purple_concrete", "cyan_concrete", "silver_concrete", "gray_concrete", "pink_concrete", "lime_concrete", "yellow_concrete", "light_blue_concrete", "magenta_concrete", "orange_concrete", "white_concrete" }));
        this.variantNames.put(Item.getItemFromBlock(Blocks.CONCRETE_POWDER), Lists.newArrayList((Object[])new String[] { "black_concrete_powder", "red_concrete_powder", "green_concrete_powder", "brown_concrete_powder", "blue_concrete_powder", "purple_concrete_powder", "cyan_concrete_powder", "silver_concrete_powder", "gray_concrete_powder", "pink_concrete_powder", "lime_concrete_powder", "yellow_concrete_powder", "light_blue_concrete_powder", "magenta_concrete_powder", "orange_concrete_powder", "white_concrete_powder" }));
        this.variantNames.put(Item.getItemFromBlock(Blocks.AIR), Collections.emptyList());
        this.variantNames.put(Item.getItemFromBlock(Blocks.OAK_FENCE_GATE), Lists.newArrayList((Object[])new String[] { "oak_fence_gate" }));
        this.variantNames.put(Item.getItemFromBlock(Blocks.OAK_FENCE), Lists.newArrayList((Object[])new String[] { "oak_fence" }));
        this.variantNames.put(Items.OAK_DOOR, Lists.newArrayList((Object[])new String[] { "oak_door" }));
        this.variantNames.put(Items.BOAT, Lists.newArrayList((Object[])new String[] { "oak_boat" }));
        this.variantNames.put(Items.TOTEM_OF_UNDYING, Lists.newArrayList((Object[])new String[] { "totem" }));
        for (final Map.Entry<IRegistryDelegate<Item>, Set<String>> entry : ModelBakery.customVariantNames.entrySet()) {
            this.variantNames.put(entry.getKey().get(), Lists.newArrayList((Iterator)entry.getValue().iterator()));
        }
    }
    
    private List<String> getVariantNames(final Item stack) {
        List<String> list = this.variantNames.get(stack);
        if (list == null) {
            list = Collections.singletonList(Item.REGISTRY.getNameForObject(stack).toString());
        }
        return list;
    }
    
    private ResourceLocation getItemLocation(final String location) {
        ResourceLocation resourcelocation = new ResourceLocation(location);
        if (Reflector.ForgeHooksClient.exists()) {
            resourcelocation = new ResourceLocation(location.replaceAll("#.*", ""));
        }
        return new ResourceLocation(resourcelocation.getNamespace(), "item/" + resourcelocation.getPath());
    }
    
    private void bakeBlockModels() {
        for (final ModelResourceLocation modelresourcelocation : this.variants.keySet()) {
            final IBakedModel ibakedmodel = this.createRandomModelForVariantList(this.variants.get(modelresourcelocation), modelresourcelocation.toString());
            if (ibakedmodel != null) {
                this.bakedRegistry.putObject(modelresourcelocation, ibakedmodel);
            }
        }
        for (final Map.Entry<ModelBlockDefinition, Collection<ModelResourceLocation>> entry : this.multipartVariantMap.entrySet()) {
            final ModelBlockDefinition modelblockdefinition = entry.getKey();
            final Multipart multipart = modelblockdefinition.getMultipartData();
            final String s = Block.REGISTRY.getNameForObject(multipart.getStateContainer().getBlock()).toString();
            final MultipartBakedModel.Builder multipartbakedmodel$builder = new MultipartBakedModel.Builder();
            for (final Selector selector : multipart.getSelectors()) {
                final IBakedModel ibakedmodel2 = this.createRandomModelForVariantList(selector.getVariantList(), "selector of " + s);
                if (ibakedmodel2 != null) {
                    multipartbakedmodel$builder.putModel(selector.getPredicate(multipart.getStateContainer()), ibakedmodel2);
                }
            }
            final IBakedModel ibakedmodel3 = multipartbakedmodel$builder.makeMultipartModel();
            for (final ModelResourceLocation modelresourcelocation2 : entry.getValue()) {
                if (!modelblockdefinition.hasVariant(modelresourcelocation2.getVariant())) {
                    this.bakedRegistry.putObject(modelresourcelocation2, ibakedmodel3);
                }
            }
        }
    }
    
    @Nullable
    private IBakedModel createRandomModelForVariantList(final VariantList variantsIn, final String modelLocation) {
        if (variantsIn.getVariantList().isEmpty()) {
            return null;
        }
        final WeightedBakedModel.Builder weightedbakedmodel$builder = new WeightedBakedModel.Builder();
        int i = 0;
        for (final Variant variant : variantsIn.getVariantList()) {
            final ModelBlock modelblock = this.models.get(variant.getModelLocation());
            if (modelblock != null && modelblock.isResolved()) {
                if (modelblock.getElements().isEmpty()) {
                    ModelBakery.LOGGER.warn("Missing elements for: {}", (Object)modelLocation);
                }
                else {
                    final IBakedModel ibakedmodel = this.bakeModel(modelblock, variant.getRotation(), variant.isUvLock());
                    if (ibakedmodel == null) {
                        continue;
                    }
                    ++i;
                    weightedbakedmodel$builder.add(ibakedmodel, variant.getWeight());
                }
            }
            else {
                ModelBakery.LOGGER.warn("Missing model for: {}", (Object)modelLocation);
            }
        }
        IBakedModel ibakedmodel2 = null;
        if (i == 0) {
            ModelBakery.LOGGER.warn("No weighted models for: {}", (Object)modelLocation);
        }
        else if (i == 1) {
            ibakedmodel2 = weightedbakedmodel$builder.first();
        }
        else {
            ibakedmodel2 = weightedbakedmodel$builder.build();
        }
        return ibakedmodel2;
    }
    
    private void bakeItemModels() {
        for (final Map.Entry<String, ResourceLocation> entry : this.itemLocations.entrySet()) {
            final ResourceLocation resourcelocation = entry.getValue();
            ModelResourceLocation modelresourcelocation = new ModelResourceLocation(entry.getKey(), "inventory");
            if (Reflector.ForgeHooksClient.exists()) {
                modelresourcelocation = (ModelResourceLocation)Reflector.call(Reflector.ModelLoader_getInventoryVariant, entry.getKey());
            }
            final ModelBlock modelblock = this.models.get(resourcelocation);
            if (modelblock != null && modelblock.isResolved()) {
                if (modelblock.getElements().isEmpty()) {
                    ModelBakery.LOGGER.warn("Missing elements for: {}", (Object)resourcelocation);
                }
                else if (this.isCustomRenderer(modelblock)) {
                    this.bakedRegistry.putObject(modelresourcelocation, new BuiltInModel(modelblock.getAllTransforms(), modelblock.createOverrides()));
                }
                else {
                    final IBakedModel ibakedmodel = this.bakeModel(modelblock, ModelRotation.X0_Y0, false);
                    if (ibakedmodel == null) {
                        continue;
                    }
                    this.bakedRegistry.putObject(modelresourcelocation, ibakedmodel);
                }
            }
            else {
                ModelBakery.LOGGER.warn("Missing model for: {}", (Object)resourcelocation);
            }
        }
    }
    
    private Set<ResourceLocation> getVariantsTextureLocations() {
        final Set<ResourceLocation> set = (Set<ResourceLocation>)Sets.newHashSet();
        final List<ModelResourceLocation> list = (List<ModelResourceLocation>)Lists.newArrayList((Iterable)this.variants.keySet());
        Collections.sort(list, new Comparator<ModelResourceLocation>() {
            @Override
            public int compare(final ModelResourceLocation p_compare_1_, final ModelResourceLocation p_compare_2_) {
                return p_compare_1_.toString().compareTo(p_compare_2_.toString());
            }
        });
        for (final ModelResourceLocation modelresourcelocation : list) {
            final VariantList variantlist = this.variants.get(modelresourcelocation);
            for (final Variant variant : variantlist.getVariantList()) {
                final ModelBlock modelblock = this.models.get(variant.getModelLocation());
                if (modelblock == null) {
                    ModelBakery.LOGGER.warn("Missing model for: {}", (Object)modelresourcelocation);
                }
                else {
                    set.addAll(this.getTextureLocations(modelblock));
                }
            }
        }
        for (final ModelBlockDefinition modelblockdefinition : this.multipartVariantMap.keySet()) {
            for (final VariantList variantlist2 : modelblockdefinition.getMultipartData().getVariants()) {
                for (final Variant variant2 : variantlist2.getVariantList()) {
                    final ModelBlock modelblock2 = this.models.get(variant2.getModelLocation());
                    if (modelblock2 == null) {
                        ModelBakery.LOGGER.warn("Missing model for: {}", (Object)Block.REGISTRY.getNameForObject(modelblockdefinition.getMultipartData().getStateContainer().getBlock()));
                    }
                    else {
                        set.addAll(this.getTextureLocations(modelblock2));
                    }
                }
            }
        }
        set.addAll(ModelBakery.LOCATIONS_BUILTIN_TEXTURES);
        return set;
    }
    
    @Nullable
    public IBakedModel bakeModel(final ModelBlock modelBlockIn, final ModelRotation modelRotationIn, final boolean uvLocked) {
        final TextureAtlasSprite textureatlassprite = this.sprites.get(new ResourceLocation(modelBlockIn.resolveTextureName("particle")));
        final SimpleBakedModel.Builder simplebakedmodel$builder = new SimpleBakedModel.Builder(modelBlockIn, modelBlockIn.createOverrides()).setTexture(textureatlassprite);
        if (modelBlockIn.getElements().isEmpty()) {
            return null;
        }
        for (final BlockPart blockpart : modelBlockIn.getElements()) {
            for (final EnumFacing enumfacing : blockpart.mapFaces.keySet()) {
                final BlockPartFace blockpartface = blockpart.mapFaces.get(enumfacing);
                final TextureAtlasSprite textureatlassprite2 = this.sprites.get(new ResourceLocation(modelBlockIn.resolveTextureName(blockpartface.texture)));
                if (blockpartface.cullFace == null) {
                    simplebakedmodel$builder.addGeneralQuad(this.makeBakedQuad(blockpart, blockpartface, textureatlassprite2, enumfacing, modelRotationIn, uvLocked));
                }
                else {
                    simplebakedmodel$builder.addFaceQuad(modelRotationIn.rotateFace(blockpartface.cullFace), this.makeBakedQuad(blockpart, blockpartface, textureatlassprite2, enumfacing, modelRotationIn, uvLocked));
                }
            }
        }
        return simplebakedmodel$builder.makeBakedModel();
    }
    
    protected IBakedModel bakeModel(final ModelBlock p_bakeModel_1_, final ITransformation p_bakeModel_2_, final boolean p_bakeModel_3_) {
        final TextureAtlasSprite textureatlassprite = this.sprites.get(new ResourceLocation(p_bakeModel_1_.resolveTextureName("particle")));
        final SimpleBakedModel.Builder simplebakedmodel$builder = new SimpleBakedModel.Builder(p_bakeModel_1_, p_bakeModel_1_.createOverrides()).setTexture(textureatlassprite);
        if (p_bakeModel_1_.getElements().isEmpty()) {
            return null;
        }
        for (final BlockPart blockpart : p_bakeModel_1_.getElements()) {
            for (final EnumFacing enumfacing : blockpart.mapFaces.keySet()) {
                final BlockPartFace blockpartface = blockpart.mapFaces.get(enumfacing);
                final TextureAtlasSprite textureatlassprite2 = this.sprites.get(new ResourceLocation(p_bakeModel_1_.resolveTextureName(blockpartface.texture)));
                boolean flag = true;
                if (Reflector.ForgeHooksClient.exists()) {
                    flag = TRSRTransformation.isInteger(p_bakeModel_2_.getMatrix());
                }
                if (blockpartface.cullFace != null && flag) {
                    simplebakedmodel$builder.addFaceQuad(p_bakeModel_2_.rotate(blockpartface.cullFace), this.makeBakedQuad(blockpart, blockpartface, textureatlassprite2, enumfacing, p_bakeModel_2_, p_bakeModel_3_));
                }
                else {
                    simplebakedmodel$builder.addGeneralQuad(this.makeBakedQuad(blockpart, blockpartface, textureatlassprite2, enumfacing, p_bakeModel_2_, p_bakeModel_3_));
                }
            }
        }
        return simplebakedmodel$builder.makeBakedModel();
    }
    
    private BakedQuad makeBakedQuad(final BlockPart blockPartt, final BlockPartFace blockPartFaceIn, final TextureAtlasSprite sprite, final EnumFacing face, final ModelRotation transform, final boolean uvLocked) {
        return Reflector.ForgeHooksClient.exists() ? this.makeBakedQuad(blockPartt, blockPartFaceIn, sprite, face, transform, uvLocked) : this.faceBakery.makeBakedQuad(blockPartt.positionFrom, blockPartt.positionTo, blockPartFaceIn, sprite, face, transform, blockPartt.partRotation, uvLocked, blockPartt.shade);
    }
    
    protected BakedQuad makeBakedQuad(final BlockPart p_makeBakedQuad_1_, final BlockPartFace p_makeBakedQuad_2_, final TextureAtlasSprite p_makeBakedQuad_3_, final EnumFacing p_makeBakedQuad_4_, final ITransformation p_makeBakedQuad_5_, final boolean p_makeBakedQuad_6_) {
        return this.faceBakery.makeBakedQuad(p_makeBakedQuad_1_.positionFrom, p_makeBakedQuad_1_.positionTo, p_makeBakedQuad_2_, p_makeBakedQuad_3_, p_makeBakedQuad_4_, p_makeBakedQuad_5_, p_makeBakedQuad_1_.partRotation, p_makeBakedQuad_6_, p_makeBakedQuad_1_.shade);
    }
    
    private void loadModelsCheck() {
        this.loadModels();
        for (final ModelBlock modelblock : this.models.values()) {
            modelblock.getParentFromMap(this.models);
        }
        ModelBlock.checkModelHierarchy(this.models);
    }
    
    private void loadModels() {
        final Deque<ResourceLocation> deque = (Deque<ResourceLocation>)Queues.newArrayDeque();
        final Set<ResourceLocation> set = (Set<ResourceLocation>)Sets.newHashSet();
        for (final ResourceLocation resourcelocation : this.models.keySet()) {
            set.add(resourcelocation);
            this.addModelParentLocation(deque, set, this.models.get(resourcelocation));
        }
        while (!deque.isEmpty()) {
            final ResourceLocation resourcelocation2 = deque.pop();
            try {
                if (this.models.get(resourcelocation2) != null) {
                    continue;
                }
                final ModelBlock modelblock = this.loadModel(resourcelocation2);
                this.models.put(resourcelocation2, modelblock);
                this.addModelParentLocation(deque, set, modelblock);
            }
            catch (Exception var5) {
                ModelBakery.LOGGER.warn("In parent chain: {}; unable to load model: '{}'", (Object)ModelBakery.JOINER.join((Iterable)this.getParentPath(resourcelocation2)), (Object)resourcelocation2);
            }
            set.add(resourcelocation2);
        }
    }
    
    private void addModelParentLocation(final Deque<ResourceLocation> p_188633_1_, final Set<ResourceLocation> p_188633_2_, final ModelBlock p_188633_3_) {
        final ResourceLocation resourcelocation = p_188633_3_.getParentLocation();
        if (resourcelocation != null && !p_188633_2_.contains(resourcelocation)) {
            p_188633_1_.add(resourcelocation);
        }
    }
    
    private List<ResourceLocation> getParentPath(final ResourceLocation p_177573_1_) {
        final List<ResourceLocation> list = (List<ResourceLocation>)Lists.newArrayList((Object[])new ResourceLocation[] { p_177573_1_ });
        ResourceLocation resourcelocation = p_177573_1_;
        while ((resourcelocation = this.getParentLocation(resourcelocation)) != null) {
            list.add(0, resourcelocation);
        }
        return list;
    }
    
    @Nullable
    private ResourceLocation getParentLocation(final ResourceLocation p_177576_1_) {
        for (final Map.Entry<ResourceLocation, ModelBlock> entry : this.models.entrySet()) {
            final ModelBlock modelblock = entry.getValue();
            if (modelblock != null && p_177576_1_.equals(modelblock.getParentLocation())) {
                return entry.getKey();
            }
        }
        return null;
    }
    
    private Set<ResourceLocation> getTextureLocations(final ModelBlock p_177585_1_) {
        final Set<ResourceLocation> set = (Set<ResourceLocation>)Sets.newHashSet();
        for (final BlockPart blockpart : p_177585_1_.getElements()) {
            for (final BlockPartFace blockpartface : blockpart.mapFaces.values()) {
                final ResourceLocation resourcelocation = new ResourceLocation(p_177585_1_.resolveTextureName(blockpartface.texture));
                set.add(resourcelocation);
            }
        }
        set.add(new ResourceLocation(p_177585_1_.resolveTextureName("particle")));
        return set;
    }
    
    private void loadSprites() {
        final Set<ResourceLocation> set = this.getVariantsTextureLocations();
        set.addAll(this.getItemsTextureLocations());
        set.remove(TextureMap.LOCATION_MISSING_TEXTURE);
        final ITextureMapPopulator itexturemappopulator = new ITextureMapPopulator() {
            @Override
            public void registerSprites(final TextureMap textureMapIn) {
                for (final ResourceLocation resourcelocation : set) {
                    final TextureAtlasSprite textureatlassprite = textureMapIn.registerSprite(resourcelocation);
                    ModelBakery.this.sprites.put(resourcelocation, textureatlassprite);
                }
            }
        };
        this.textureMap.loadSprites(this.resourceManager, itexturemappopulator);
        this.sprites.put(new ResourceLocation("missingno"), this.textureMap.getMissingSprite());
    }
    
    private Set<ResourceLocation> getItemsTextureLocations() {
        final Set<ResourceLocation> set = (Set<ResourceLocation>)Sets.newHashSet();
        for (final ResourceLocation resourcelocation : this.itemLocations.values()) {
            final ModelBlock modelblock = this.models.get(resourcelocation);
            if (modelblock != null) {
                set.add(new ResourceLocation(modelblock.resolveTextureName("particle")));
                if (this.hasItemModel(modelblock)) {
                    for (final String s : ItemModelGenerator.LAYERS) {
                        set.add(new ResourceLocation(modelblock.resolveTextureName(s)));
                    }
                }
                else {
                    if (this.isCustomRenderer(modelblock)) {
                        continue;
                    }
                    for (final BlockPart blockpart : modelblock.getElements()) {
                        for (final BlockPartFace blockpartface : blockpart.mapFaces.values()) {
                            final ResourceLocation resourcelocation2 = new ResourceLocation(modelblock.resolveTextureName(blockpartface.texture));
                            set.add(resourcelocation2);
                        }
                    }
                }
            }
        }
        return set;
    }
    
    private boolean hasItemModel(@Nullable final ModelBlock p_177581_1_) {
        return p_177581_1_ != null && p_177581_1_.getRootModel() == ModelBakery.MODEL_GENERATED;
    }
    
    private boolean isCustomRenderer(@Nullable final ModelBlock p_177587_1_) {
        if (p_177587_1_ == null) {
            return false;
        }
        final ModelBlock modelblock = p_177587_1_.getRootModel();
        return modelblock == ModelBakery.MODEL_ENTITY;
    }
    
    private void makeItemModels() {
        for (final ResourceLocation resourcelocation : this.itemLocations.values()) {
            final ModelBlock modelblock = this.models.get(resourcelocation);
            if (this.hasItemModel(modelblock)) {
                final ModelBlock modelblock2 = this.makeItemModel(modelblock);
                if (modelblock2 != null) {
                    modelblock2.name = resourcelocation.toString();
                }
                this.models.put(resourcelocation, modelblock2);
            }
            else {
                if (!this.isCustomRenderer(modelblock)) {
                    continue;
                }
                this.models.put(resourcelocation, modelblock);
            }
        }
        for (final TextureAtlasSprite textureatlassprite : this.sprites.values()) {
            if (!textureatlassprite.hasAnimationMetadata()) {
                textureatlassprite.clearFramesTextureData();
            }
        }
    }
    
    private ModelBlock makeItemModel(final ModelBlock p_177582_1_) {
        return this.itemModelGenerator.makeItemModel(this.textureMap, p_177582_1_);
    }
    
    public ModelBlock getModelBlock(final ResourceLocation p_getModelBlock_1_) {
        final ModelBlock modelblock = this.models.get(p_getModelBlock_1_);
        return modelblock;
    }
    
    public static void fixModelLocations(final ModelBlock p_fixModelLocations_0_, final String p_fixModelLocations_1_) {
        final ResourceLocation resourcelocation = fixModelLocation(p_fixModelLocations_0_.parentLocation, p_fixModelLocations_1_);
        if (resourcelocation != p_fixModelLocations_0_.parentLocation) {
            p_fixModelLocations_0_.parentLocation = resourcelocation;
        }
        if (p_fixModelLocations_0_.textures != null) {
            for (final Map.Entry<String, String> entry : p_fixModelLocations_0_.textures.entrySet()) {
                final String s = entry.getValue();
                final String s2 = fixResourcePath(s, p_fixModelLocations_1_);
                if (s2 != s) {
                    entry.setValue(s2);
                }
            }
        }
    }
    
    public static ResourceLocation fixModelLocation(ResourceLocation p_fixModelLocation_0_, final String p_fixModelLocation_1_) {
        if (p_fixModelLocation_0_ == null || p_fixModelLocation_1_ == null) {
            return p_fixModelLocation_0_;
        }
        if (!p_fixModelLocation_0_.getNamespace().equals("minecraft")) {
            return p_fixModelLocation_0_;
        }
        final String s = p_fixModelLocation_0_.getPath();
        final String s2 = fixResourcePath(s, p_fixModelLocation_1_);
        if (s2 != s) {
            p_fixModelLocation_0_ = new ResourceLocation(p_fixModelLocation_0_.getNamespace(), s2);
        }
        return p_fixModelLocation_0_;
    }
    
    private static String fixResourcePath(String p_fixResourcePath_0_, final String p_fixResourcePath_1_) {
        p_fixResourcePath_0_ = TextureUtils.fixResourcePath(p_fixResourcePath_0_, p_fixResourcePath_1_);
        p_fixResourcePath_0_ = StrUtils.removeSuffix(p_fixResourcePath_0_, ".json");
        p_fixResourcePath_0_ = StrUtils.removeSuffix(p_fixResourcePath_0_, ".png");
        return p_fixResourcePath_0_;
    }
    
    protected void registerMultipartVariant(final ModelBlockDefinition p_registerMultipartVariant_1_, final Collection<ModelResourceLocation> p_registerMultipartVariant_2_) {
        this.multipartVariantMap.put(p_registerMultipartVariant_1_, p_registerMultipartVariant_2_);
    }
    
    public static void registerItemVariants(final Item p_registerItemVariants_0_, final ResourceLocation... p_registerItemVariants_1_) {
        final IRegistryDelegate iregistrydelegate = (IRegistryDelegate)Reflector.getFieldValue(p_registerItemVariants_0_, Reflector.ForgeItem_delegate);
        if (!ModelBakery.customVariantNames.containsKey(iregistrydelegate)) {
            ModelBakery.customVariantNames.put(iregistrydelegate, Sets.newHashSet());
        }
        for (final ResourceLocation resourcelocation : p_registerItemVariants_1_) {
            ModelBakery.customVariantNames.get(iregistrydelegate).add(resourcelocation.toString());
        }
    }
    
    static {
        LOCATIONS_BUILTIN_TEXTURES = Sets.newHashSet((Object[])new ResourceLocation[] { new ResourceLocation("blocks/water_flow"), new ResourceLocation("blocks/water_still"), new ResourceLocation("blocks/lava_flow"), new ResourceLocation("blocks/lava_still"), new ResourceLocation("blocks/water_overlay"), new ResourceLocation("blocks/destroy_stage_0"), new ResourceLocation("blocks/destroy_stage_1"), new ResourceLocation("blocks/destroy_stage_2"), new ResourceLocation("blocks/destroy_stage_3"), new ResourceLocation("blocks/destroy_stage_4"), new ResourceLocation("blocks/destroy_stage_5"), new ResourceLocation("blocks/destroy_stage_6"), new ResourceLocation("blocks/destroy_stage_7"), new ResourceLocation("blocks/destroy_stage_8"), new ResourceLocation("blocks/destroy_stage_9"), new ResourceLocation("items/empty_armor_slot_helmet"), new ResourceLocation("items/empty_armor_slot_chestplate"), new ResourceLocation("items/empty_armor_slot_leggings"), new ResourceLocation("items/empty_armor_slot_boots"), new ResourceLocation("items/empty_armor_slot_shield"), new ResourceLocation("blocks/shulker_top_white"), new ResourceLocation("blocks/shulker_top_orange"), new ResourceLocation("blocks/shulker_top_magenta"), new ResourceLocation("blocks/shulker_top_light_blue"), new ResourceLocation("blocks/shulker_top_yellow"), new ResourceLocation("blocks/shulker_top_lime"), new ResourceLocation("blocks/shulker_top_pink"), new ResourceLocation("blocks/shulker_top_gray"), new ResourceLocation("blocks/shulker_top_silver"), new ResourceLocation("blocks/shulker_top_cyan"), new ResourceLocation("blocks/shulker_top_purple"), new ResourceLocation("blocks/shulker_top_blue"), new ResourceLocation("blocks/shulker_top_brown"), new ResourceLocation("blocks/shulker_top_green"), new ResourceLocation("blocks/shulker_top_red"), new ResourceLocation("blocks/shulker_top_black") });
        LOGGER = LogManager.getLogger();
        MODEL_MISSING = new ModelResourceLocation("builtin/missing", "missing");
        MISSING_MODEL_MESH = "{    'textures': {       'particle': 'missingno',       'missingno': 'missingno'    },    'elements': [         {  'from': [ 0, 0, 0 ],            'to': [ 16, 16, 16 ],            'faces': {                'down':  { 'uv': [ 0, 0, 16, 16 ], 'cullface': 'down',  'texture': '#missingno' },                'up':    { 'uv': [ 0, 0, 16, 16 ], 'cullface': 'up',    'texture': '#missingno' },                'north': { 'uv': [ 0, 0, 16, 16 ], 'cullface': 'north', 'texture': '#missingno' },                'south': { 'uv': [ 0, 0, 16, 16 ], 'cullface': 'south', 'texture': '#missingno' },                'west':  { 'uv': [ 0, 0, 16, 16 ], 'cullface': 'west',  'texture': '#missingno' },                'east':  { 'uv': [ 0, 0, 16, 16 ], 'cullface': 'east',  'texture': '#missingno' }            }        }    ]}".replaceAll("'", "\"");
        BUILT_IN_MODELS = Maps.newHashMap();
        JOINER = Joiner.on(" -> ");
        EMPTY_MODEL_RAW = "{    'elements': [        {   'from': [0, 0, 0],            'to': [16, 16, 16],            'faces': {                'down': {'uv': [0, 0, 16, 16], 'texture': '' }            }        }    ]}".replaceAll("'", "\"");
        MODEL_GENERATED = ModelBlock.deserialize(ModelBakery.EMPTY_MODEL_RAW);
        MODEL_ENTITY = ModelBlock.deserialize(ModelBakery.EMPTY_MODEL_RAW);
        ModelBakery.customVariantNames = (Map<IRegistryDelegate<Item>, Set<String>>)Maps.newHashMap();
        ModelBakery.BUILT_IN_MODELS.put("missing", ModelBakery.MISSING_MODEL_MESH);
        ModelBakery.MODEL_GENERATED.name = "generation marker";
        ModelBakery.MODEL_ENTITY.name = "block entity marker";
    }
}
