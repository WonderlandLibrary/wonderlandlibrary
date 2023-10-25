/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.renderer.block.model;

import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Queues;
import com.google.common.collect.Sets;
import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockPart;
import net.minecraft.client.renderer.block.model.BlockPartFace;
import net.minecraft.client.renderer.block.model.BuiltInModel;
import net.minecraft.client.renderer.block.model.FaceBakery;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemModelGenerator;
import net.minecraft.client.renderer.block.model.ModelBlock;
import net.minecraft.client.renderer.block.model.ModelBlockDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.client.renderer.block.model.MultipartBakedModel;
import net.minecraft.client.renderer.block.model.SimpleBakedModel;
import net.minecraft.client.renderer.block.model.Variant;
import net.minecraft.client.renderer.block.model.VariantList;
import net.minecraft.client.renderer.block.model.WeightedBakedModel;
import net.minecraft.client.renderer.block.model.multipart.Multipart;
import net.minecraft.client.renderer.block.model.multipart.Selector;
import net.minecraft.client.renderer.block.statemap.BlockStateMapper;
import net.minecraft.client.renderer.texture.ITextureMapPopulator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.IRegistry;
import net.minecraft.util.registry.RegistrySimple;
import net.minecraftforge.common.model.ITransformation;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.registries.IRegistryDelegate;
import optifine.CustomItems;
import optifine.Reflector;
import optifine.StrUtils;
import optifine.TextureUtils;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ModelBakery {
    private static final Set<ResourceLocation> LOCATIONS_BUILTIN_TEXTURES = Sets.newHashSet(new ResourceLocation("blocks/water_flow"), new ResourceLocation("blocks/water_still"), new ResourceLocation("blocks/lava_flow"), new ResourceLocation("blocks/lava_still"), new ResourceLocation("blocks/water_overlay"), new ResourceLocation("blocks/destroy_stage_0"), new ResourceLocation("blocks/destroy_stage_1"), new ResourceLocation("blocks/destroy_stage_2"), new ResourceLocation("blocks/destroy_stage_3"), new ResourceLocation("blocks/destroy_stage_4"), new ResourceLocation("blocks/destroy_stage_5"), new ResourceLocation("blocks/destroy_stage_6"), new ResourceLocation("blocks/destroy_stage_7"), new ResourceLocation("blocks/destroy_stage_8"), new ResourceLocation("blocks/destroy_stage_9"), new ResourceLocation("items/empty_armor_slot_helmet"), new ResourceLocation("items/empty_armor_slot_chestplate"), new ResourceLocation("items/empty_armor_slot_leggings"), new ResourceLocation("items/empty_armor_slot_boots"), new ResourceLocation("items/empty_armor_slot_shield"), new ResourceLocation("blocks/shulker_top_white"), new ResourceLocation("blocks/shulker_top_orange"), new ResourceLocation("blocks/shulker_top_magenta"), new ResourceLocation("blocks/shulker_top_light_blue"), new ResourceLocation("blocks/shulker_top_yellow"), new ResourceLocation("blocks/shulker_top_lime"), new ResourceLocation("blocks/shulker_top_pink"), new ResourceLocation("blocks/shulker_top_gray"), new ResourceLocation("blocks/shulker_top_silver"), new ResourceLocation("blocks/shulker_top_cyan"), new ResourceLocation("blocks/shulker_top_purple"), new ResourceLocation("blocks/shulker_top_blue"), new ResourceLocation("blocks/shulker_top_brown"), new ResourceLocation("blocks/shulker_top_green"), new ResourceLocation("blocks/shulker_top_red"), new ResourceLocation("blocks/shulker_top_black"));
    private static final Logger LOGGER = LogManager.getLogger();
    protected static final ModelResourceLocation MODEL_MISSING = new ModelResourceLocation("builtin/missing", "missing");
    private static final String MISSING_MODEL_MESH = "{    'textures': {       'particle': 'missingno',       'missingno': 'missingno'    },    'elements': [         {  'from': [ 0, 0, 0 ],            'to': [ 16, 16, 16 ],            'faces': {                'down':  { 'uv': [ 0, 0, 16, 16 ], 'cullface': 'down',  'texture': '#missingno' },                'up':    { 'uv': [ 0, 0, 16, 16 ], 'cullface': 'up',    'texture': '#missingno' },                'north': { 'uv': [ 0, 0, 16, 16 ], 'cullface': 'north', 'texture': '#missingno' },                'south': { 'uv': [ 0, 0, 16, 16 ], 'cullface': 'south', 'texture': '#missingno' },                'west':  { 'uv': [ 0, 0, 16, 16 ], 'cullface': 'west',  'texture': '#missingno' },                'east':  { 'uv': [ 0, 0, 16, 16 ], 'cullface': 'east',  'texture': '#missingno' }            }        }    ]}".replaceAll("'", "\"");
    private static final Map<String, String> BUILT_IN_MODELS = Maps.newHashMap();
    private static final Joiner JOINER = Joiner.on(" -> ");
    private final IResourceManager resourceManager;
    private final Map<ResourceLocation, TextureAtlasSprite> sprites = Maps.newHashMap();
    private final Map<ResourceLocation, ModelBlock> models = Maps.newLinkedHashMap();
    private final Map<ModelResourceLocation, VariantList> variants = Maps.newLinkedHashMap();
    private final Map<ModelBlockDefinition, Collection<ModelResourceLocation>> multipartVariantMap = Maps.newLinkedHashMap();
    private final TextureMap textureMap;
    private final BlockModelShapes blockModelShapes;
    private final FaceBakery faceBakery = new FaceBakery();
    private final ItemModelGenerator itemModelGenerator = new ItemModelGenerator();
    private final RegistrySimple<ModelResourceLocation, IBakedModel> bakedRegistry = new RegistrySimple();
    private static final String EMPTY_MODEL_RAW = "{    'elements': [        {   'from': [0, 0, 0],            'to': [16, 16, 16],            'faces': {                'down': {'uv': [0, 0, 16, 16], 'texture': '' }            }        }    ]}".replaceAll("'", "\"");
    private static final ModelBlock MODEL_GENERATED = ModelBlock.deserialize(EMPTY_MODEL_RAW);
    private static final ModelBlock MODEL_ENTITY = ModelBlock.deserialize(EMPTY_MODEL_RAW);
    private final Map<String, ResourceLocation> itemLocations = Maps.newLinkedHashMap();
    private final Map<ResourceLocation, ModelBlockDefinition> blockDefinitions = Maps.newHashMap();
    private final Map<Item, List<String>> variantNames = Maps.newIdentityHashMap();
    private static Map<IRegistryDelegate<Item>, Set<String>> customVariantNames = Maps.newHashMap();

    public ModelBakery(IResourceManager resourceManagerIn, TextureMap textureMapIn, BlockModelShapes blockModelShapesIn) {
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
        BlockStateMapper blockstatemapper = this.blockModelShapes.getBlockStateMapper();
        for (Block block : Block.REGISTRY) {
            for (ResourceLocation resourcelocation : blockstatemapper.getBlockstateLocations(block)) {
                try {
                    this.loadBlock(blockstatemapper, block, resourcelocation);
                }
                catch (Exception exception) {
                    LOGGER.warn("Unable to load definition " + resourcelocation, (Throwable)exception);
                }
            }
        }
    }

    protected void loadBlock(BlockStateMapper p_loadBlock_1_, Block p_loadBlock_2_, final ResourceLocation p_loadBlock_3_) {
        ModelBlockDefinition modelblockdefinition = this.getModelBlockDefinition(p_loadBlock_3_);
        Map<IBlockState, ModelResourceLocation> map = p_loadBlock_1_.getVariants(p_loadBlock_2_);
        if (modelblockdefinition.hasMultipartData()) {
            HashSet<ModelResourceLocation> collection = Sets.newHashSet(map.values());
            modelblockdefinition.getMultipartData().setStateContainer(p_loadBlock_2_.getBlockState());
            Collection<ModelResourceLocation> collection1 = this.multipartVariantMap.get(modelblockdefinition);
            if (collection1 == null) {
                collection1 = Lists.newArrayList();
            }
            collection1.addAll(Lists.newArrayList(Iterables.filter(collection, new Predicate<ModelResourceLocation>(){

                @Override
                public boolean apply(@Nullable ModelResourceLocation p_apply_1_) {
                    return p_loadBlock_3_.equals(p_apply_1_);
                }
            })));
            this.registerMultipartVariant(modelblockdefinition, collection1);
        }
        for (Map.Entry<IBlockState, ModelResourceLocation> entry : map.entrySet()) {
            ModelResourceLocation modelresourcelocation = entry.getValue();
            if (!p_loadBlock_3_.equals(modelresourcelocation)) continue;
            try {
                if (Reflector.ForgeItem_delegate.exists()) {
                    this.registerVariant(modelblockdefinition, modelresourcelocation);
                    continue;
                }
                this.variants.put(modelresourcelocation, modelblockdefinition.getVariant(modelresourcelocation.getVariant()));
            }
            catch (RuntimeException runtimeexception) {
                if (modelblockdefinition.hasMultipartData()) continue;
                LOGGER.warn("Unable to load variant: " + modelresourcelocation.getVariant() + " from " + modelresourcelocation, (Throwable)runtimeexception);
            }
        }
    }

    private void loadVariantItemModels() {
        this.variants.put(MODEL_MISSING, new VariantList(Lists.newArrayList(new Variant(new ResourceLocation(MODEL_MISSING.getPath()), ModelRotation.X0_Y0, false, 1))));
        this.func_191401_d();
        this.loadVariantModels();
        this.loadMultipartVariantModels();
        this.loadItemModels();
        CustomItems.update();
        CustomItems.loadModels(this);
    }

    private void func_191401_d() {
        ResourceLocation resourcelocation = new ResourceLocation("item_frame");
        ModelBlockDefinition modelblockdefinition = this.getModelBlockDefinition(resourcelocation);
        this.registerVariant(modelblockdefinition, new ModelResourceLocation(resourcelocation, "normal"));
        this.registerVariant(modelblockdefinition, new ModelResourceLocation(resourcelocation, "map"));
    }

    private void registerVariant(ModelBlockDefinition blockstateDefinition, ModelResourceLocation location) {
        block2: {
            try {
                this.variants.put(location, blockstateDefinition.getVariant(location.getVariant()));
            }
            catch (RuntimeException var4) {
                if (blockstateDefinition.hasMultipartData()) break block2;
                LOGGER.warn("Unable to load variant: {} from {}", location.getVariant(), location);
            }
        }
    }

    private ModelBlockDefinition getModelBlockDefinition(ResourceLocation location) {
        ResourceLocation resourcelocation = this.getBlockstateLocation(location);
        ModelBlockDefinition modelblockdefinition = this.blockDefinitions.get(resourcelocation);
        if (modelblockdefinition == null) {
            modelblockdefinition = this.loadMultipartMBD(location, resourcelocation);
            this.blockDefinitions.put(resourcelocation, modelblockdefinition);
        }
        return modelblockdefinition;
    }

    private ModelBlockDefinition loadMultipartMBD(ResourceLocation location, ResourceLocation fileIn) {
        ArrayList<ModelBlockDefinition> list = Lists.newArrayList();
        try {
            for (IResource iresource : this.resourceManager.getAllResources(fileIn)) {
                list.add(this.loadModelBlockDefinition(location, iresource));
            }
        }
        catch (IOException ioexception) {
            throw new RuntimeException("Encountered an exception when loading model definition of model " + fileIn, ioexception);
        }
        return new ModelBlockDefinition(list);
    }

    private ModelBlockDefinition loadModelBlockDefinition(ResourceLocation location, IResource resource) {
        ModelBlockDefinition modelblockdefinition;
        InputStream inputstream = null;
        try {
            inputstream = resource.getInputStream();
            modelblockdefinition = Reflector.ForgeModelBlockDefinition_parseFromReader2.exists() ? (ModelBlockDefinition)Reflector.call(Reflector.ForgeModelBlockDefinition_parseFromReader2, new InputStreamReader(inputstream, StandardCharsets.UTF_8), location) : ModelBlockDefinition.parseFromReader(new InputStreamReader(inputstream, StandardCharsets.UTF_8));
        }
        catch (Exception exception) {
            throw new RuntimeException("Encountered an exception when loading model definition of '" + location + "' from: '" + resource.getResourceLocation() + "' in resourcepack: '" + resource.getResourcePackName() + "'", exception);
        }
        finally {
            IOUtils.closeQuietly(inputstream);
        }
        return modelblockdefinition;
    }

    private ResourceLocation getBlockstateLocation(ResourceLocation location) {
        return new ResourceLocation(location.getNamespace(), "blockstates/" + location.getPath() + ".json");
    }

    private void loadVariantModels() {
        for (Map.Entry<ModelResourceLocation, VariantList> entry : this.variants.entrySet()) {
            this.loadVariantList(entry.getKey(), entry.getValue());
        }
    }

    private void loadMultipartVariantModels() {
        for (Map.Entry<ModelBlockDefinition, Collection<ModelResourceLocation>> entry : this.multipartVariantMap.entrySet()) {
            ModelResourceLocation modelresourcelocation = entry.getValue().iterator().next();
            for (VariantList variantlist : entry.getKey().getMultipartVariants()) {
                this.loadVariantList(modelresourcelocation, variantlist);
            }
        }
    }

    private void loadVariantList(ModelResourceLocation p_188638_1_, VariantList p_188638_2_) {
        for (Variant variant : p_188638_2_.getVariantList()) {
            ResourceLocation resourcelocation = variant.getModelLocation();
            if (this.models.get(resourcelocation) != null) continue;
            try {
                this.models.put(resourcelocation, this.loadModel(resourcelocation));
            }
            catch (Exception exception) {
                LOGGER.warn("Unable to load block model: '{}' for variant: '{}': {} ", resourcelocation, p_188638_1_, exception);
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private ModelBlock loadModel(ResourceLocation location) throws IOException {
        ModelBlock modelblock1;
        IResource iresource;
        Reader reader;
        block9: {
            ModelBlock modelblock2;
            String s;
            block8: {
                ModelBlock modelblock4;
                reader = null;
                iresource = null;
                s = location.getPath();
                if (!"builtin/generated".equals(s)) break block8;
                ModelBlock modelBlock = modelblock4 = MODEL_GENERATED;
                IOUtils.closeQuietly(reader);
                IOUtils.closeQuietly(iresource);
                return modelBlock;
            }
            if ("builtin/entity".equals(s)) break block9;
            if (s.startsWith("builtin/")) {
                String s2 = s.substring("builtin/".length());
                String s1 = BUILT_IN_MODELS.get(s2);
                if (s1 == null) {
                    throw new FileNotFoundException(location.toString());
                }
                reader = new StringReader(s1);
            } else {
                location = this.getModelLocation(location);
                iresource = this.resourceManager.getResource(location);
                reader = new InputStreamReader(iresource.getInputStream(), StandardCharsets.UTF_8);
            }
            ModelBlock modelblock3 = ModelBlock.deserialize(reader);
            modelblock3.name = location.toString();
            String s3 = TextureUtils.getBasePath(location.getPath());
            ModelBakery.fixModelLocations(modelblock3, s3);
            ModelBlock modelBlock = modelblock2 = modelblock3;
            IOUtils.closeQuietly(reader);
            IOUtils.closeQuietly((Closeable)iresource);
            return modelBlock;
        }
        try {
            ModelBlock modelblock;
            modelblock1 = modelblock = MODEL_ENTITY;
        }
        finally {
            IOUtils.closeQuietly(reader);
            IOUtils.closeQuietly(iresource);
        }
        return modelblock1;
    }

    private ResourceLocation getModelLocation(ResourceLocation location) {
        String s = location.getPath();
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
        for (Item item : Item.REGISTRY) {
            for (String s : this.getVariantNames(item)) {
                ModelBlock modelblock;
                ResourceLocation resourcelocation = this.getItemLocation(s);
                ResourceLocation resourcelocation1 = Item.REGISTRY.getNameForObject(item);
                this.loadItemModel(s, resourcelocation, resourcelocation1);
                if (!item.hasCustomProperties() || (modelblock = this.models.get(resourcelocation)) == null) continue;
                for (ResourceLocation resourcelocation2 : modelblock.getOverrideLocations()) {
                    this.loadItemModel(resourcelocation2.toString(), resourcelocation2, resourcelocation1);
                }
            }
        }
    }

    public void loadItemModel(String variantName, ResourceLocation location, ResourceLocation itemName) {
        this.itemLocations.put(variantName, location);
        if (this.models.get(location) == null) {
            try {
                ModelBlock modelblock = this.loadModel(location);
                this.models.put(location, modelblock);
            }
            catch (Exception exception1) {
                LOGGER.warn("Unable to load item model: '{}' for item: '{}'", location, itemName);
                LOGGER.warn(exception1.getClass().getName() + ": " + exception1.getMessage());
            }
        }
    }

    private void registerVariantNames() {
        this.variantNames.clear();
        this.variantNames.put(Item.getItemFromBlock(Blocks.STONE), Lists.newArrayList("stone", "granite", "granite_smooth", "diorite", "diorite_smooth", "andesite", "andesite_smooth"));
        this.variantNames.put(Item.getItemFromBlock(Blocks.DIRT), Lists.newArrayList("dirt", "coarse_dirt", "podzol"));
        this.variantNames.put(Item.getItemFromBlock(Blocks.PLANKS), Lists.newArrayList("oak_planks", "spruce_planks", "birch_planks", "jungle_planks", "acacia_planks", "dark_oak_planks"));
        this.variantNames.put(Item.getItemFromBlock(Blocks.SAPLING), Lists.newArrayList("oak_sapling", "spruce_sapling", "birch_sapling", "jungle_sapling", "acacia_sapling", "dark_oak_sapling"));
        this.variantNames.put(Item.getItemFromBlock(Blocks.SAND), Lists.newArrayList("sand", "red_sand"));
        this.variantNames.put(Item.getItemFromBlock(Blocks.LOG), Lists.newArrayList("oak_log", "spruce_log", "birch_log", "jungle_log"));
        this.variantNames.put(Item.getItemFromBlock(Blocks.LEAVES), Lists.newArrayList("oak_leaves", "spruce_leaves", "birch_leaves", "jungle_leaves"));
        this.variantNames.put(Item.getItemFromBlock(Blocks.SPONGE), Lists.newArrayList("sponge", "sponge_wet"));
        this.variantNames.put(Item.getItemFromBlock(Blocks.SANDSTONE), Lists.newArrayList("sandstone", "chiseled_sandstone", "smooth_sandstone"));
        this.variantNames.put(Item.getItemFromBlock(Blocks.RED_SANDSTONE), Lists.newArrayList("red_sandstone", "chiseled_red_sandstone", "smooth_red_sandstone"));
        this.variantNames.put(Item.getItemFromBlock(Blocks.TALLGRASS), Lists.newArrayList("dead_bush", "tall_grass", "fern"));
        this.variantNames.put(Item.getItemFromBlock(Blocks.DEADBUSH), Lists.newArrayList("dead_bush"));
        this.variantNames.put(Item.getItemFromBlock(Blocks.WOOL), Lists.newArrayList("black_wool", "red_wool", "green_wool", "brown_wool", "blue_wool", "purple_wool", "cyan_wool", "silver_wool", "gray_wool", "pink_wool", "lime_wool", "yellow_wool", "light_blue_wool", "magenta_wool", "orange_wool", "white_wool"));
        this.variantNames.put(Item.getItemFromBlock(Blocks.YELLOW_FLOWER), Lists.newArrayList("dandelion"));
        this.variantNames.put(Item.getItemFromBlock(Blocks.RED_FLOWER), Lists.newArrayList("poppy", "blue_orchid", "allium", "houstonia", "red_tulip", "orange_tulip", "white_tulip", "pink_tulip", "oxeye_daisy"));
        this.variantNames.put(Item.getItemFromBlock(Blocks.STONE_SLAB), Lists.newArrayList("stone_slab", "sandstone_slab", "cobblestone_slab", "brick_slab", "stone_brick_slab", "nether_brick_slab", "quartz_slab"));
        this.variantNames.put(Item.getItemFromBlock(Blocks.STONE_SLAB2), Lists.newArrayList("red_sandstone_slab"));
        this.variantNames.put(Item.getItemFromBlock(Blocks.STAINED_GLASS), Lists.newArrayList("black_stained_glass", "red_stained_glass", "green_stained_glass", "brown_stained_glass", "blue_stained_glass", "purple_stained_glass", "cyan_stained_glass", "silver_stained_glass", "gray_stained_glass", "pink_stained_glass", "lime_stained_glass", "yellow_stained_glass", "light_blue_stained_glass", "magenta_stained_glass", "orange_stained_glass", "white_stained_glass"));
        this.variantNames.put(Item.getItemFromBlock(Blocks.MONSTER_EGG), Lists.newArrayList("stone_monster_egg", "cobblestone_monster_egg", "stone_brick_monster_egg", "mossy_brick_monster_egg", "cracked_brick_monster_egg", "chiseled_brick_monster_egg"));
        this.variantNames.put(Item.getItemFromBlock(Blocks.STONEBRICK), Lists.newArrayList("stonebrick", "mossy_stonebrick", "cracked_stonebrick", "chiseled_stonebrick"));
        this.variantNames.put(Item.getItemFromBlock(Blocks.WOODEN_SLAB), Lists.newArrayList("oak_slab", "spruce_slab", "birch_slab", "jungle_slab", "acacia_slab", "dark_oak_slab"));
        this.variantNames.put(Item.getItemFromBlock(Blocks.COBBLESTONE_WALL), Lists.newArrayList("cobblestone_wall", "mossy_cobblestone_wall"));
        this.variantNames.put(Item.getItemFromBlock(Blocks.ANVIL), Lists.newArrayList("anvil_intact", "anvil_slightly_damaged", "anvil_very_damaged"));
        this.variantNames.put(Item.getItemFromBlock(Blocks.QUARTZ_BLOCK), Lists.newArrayList("quartz_block", "chiseled_quartz_block", "quartz_column"));
        this.variantNames.put(Item.getItemFromBlock(Blocks.STAINED_HARDENED_CLAY), Lists.newArrayList("black_stained_hardened_clay", "red_stained_hardened_clay", "green_stained_hardened_clay", "brown_stained_hardened_clay", "blue_stained_hardened_clay", "purple_stained_hardened_clay", "cyan_stained_hardened_clay", "silver_stained_hardened_clay", "gray_stained_hardened_clay", "pink_stained_hardened_clay", "lime_stained_hardened_clay", "yellow_stained_hardened_clay", "light_blue_stained_hardened_clay", "magenta_stained_hardened_clay", "orange_stained_hardened_clay", "white_stained_hardened_clay"));
        this.variantNames.put(Item.getItemFromBlock(Blocks.STAINED_GLASS_PANE), Lists.newArrayList("black_stained_glass_pane", "red_stained_glass_pane", "green_stained_glass_pane", "brown_stained_glass_pane", "blue_stained_glass_pane", "purple_stained_glass_pane", "cyan_stained_glass_pane", "silver_stained_glass_pane", "gray_stained_glass_pane", "pink_stained_glass_pane", "lime_stained_glass_pane", "yellow_stained_glass_pane", "light_blue_stained_glass_pane", "magenta_stained_glass_pane", "orange_stained_glass_pane", "white_stained_glass_pane"));
        this.variantNames.put(Item.getItemFromBlock(Blocks.LEAVES2), Lists.newArrayList("acacia_leaves", "dark_oak_leaves"));
        this.variantNames.put(Item.getItemFromBlock(Blocks.LOG2), Lists.newArrayList("acacia_log", "dark_oak_log"));
        this.variantNames.put(Item.getItemFromBlock(Blocks.PRISMARINE), Lists.newArrayList("prismarine", "prismarine_bricks", "dark_prismarine"));
        this.variantNames.put(Item.getItemFromBlock(Blocks.CARPET), Lists.newArrayList("black_carpet", "red_carpet", "green_carpet", "brown_carpet", "blue_carpet", "purple_carpet", "cyan_carpet", "silver_carpet", "gray_carpet", "pink_carpet", "lime_carpet", "yellow_carpet", "light_blue_carpet", "magenta_carpet", "orange_carpet", "white_carpet"));
        this.variantNames.put(Item.getItemFromBlock(Blocks.DOUBLE_PLANT), Lists.newArrayList("sunflower", "syringa", "double_grass", "double_fern", "double_rose", "paeonia"));
        this.variantNames.put(Items.COAL, Lists.newArrayList("coal", "charcoal"));
        this.variantNames.put(Items.FISH, Lists.newArrayList("cod", "salmon", "clownfish", "pufferfish"));
        this.variantNames.put(Items.COOKED_FISH, Lists.newArrayList("cooked_cod", "cooked_salmon"));
        this.variantNames.put(Items.DYE, Lists.newArrayList("dye_black", "dye_red", "dye_green", "dye_brown", "dye_blue", "dye_purple", "dye_cyan", "dye_silver", "dye_gray", "dye_pink", "dye_lime", "dye_yellow", "dye_light_blue", "dye_magenta", "dye_orange", "dye_white"));
        this.variantNames.put(Items.POTIONITEM, Lists.newArrayList("bottle_drinkable"));
        this.variantNames.put(Items.SKULL, Lists.newArrayList("skull_skeleton", "skull_wither", "skull_zombie", "skull_char", "skull_creeper", "skull_dragon"));
        this.variantNames.put(Items.SPLASH_POTION, Lists.newArrayList("bottle_splash"));
        this.variantNames.put(Items.LINGERING_POTION, Lists.newArrayList("bottle_lingering"));
        this.variantNames.put(Item.getItemFromBlock(Blocks.field_192443_dR), Lists.newArrayList("black_concrete", "red_concrete", "green_concrete", "brown_concrete", "blue_concrete", "purple_concrete", "cyan_concrete", "silver_concrete", "gray_concrete", "pink_concrete", "lime_concrete", "yellow_concrete", "light_blue_concrete", "magenta_concrete", "orange_concrete", "white_concrete"));
        this.variantNames.put(Item.getItemFromBlock(Blocks.field_192444_dS), Lists.newArrayList("black_concrete_powder", "red_concrete_powder", "green_concrete_powder", "brown_concrete_powder", "blue_concrete_powder", "purple_concrete_powder", "cyan_concrete_powder", "silver_concrete_powder", "gray_concrete_powder", "pink_concrete_powder", "lime_concrete_powder", "yellow_concrete_powder", "light_blue_concrete_powder", "magenta_concrete_powder", "orange_concrete_powder", "white_concrete_powder"));
        this.variantNames.put(Item.getItemFromBlock(Blocks.AIR), Collections.emptyList());
        this.variantNames.put(Item.getItemFromBlock(Blocks.OAK_FENCE_GATE), Lists.newArrayList("oak_fence_gate"));
        this.variantNames.put(Item.getItemFromBlock(Blocks.OAK_FENCE), Lists.newArrayList("oak_fence"));
        this.variantNames.put(Items.OAK_DOOR, Lists.newArrayList("oak_door"));
        this.variantNames.put(Items.BOAT, Lists.newArrayList("oak_boat"));
        this.variantNames.put(Items.TOTEM_OF_UNDYING, Lists.newArrayList("totem"));
        for (Map.Entry<IRegistryDelegate<Item>, Set<String>> entry : customVariantNames.entrySet()) {
            this.variantNames.put(entry.getKey().get(), Lists.newArrayList(entry.getValue().iterator()));
        }
    }

    private List<String> getVariantNames(Item stack) {
        List<String> list = this.variantNames.get(stack);
        if (list == null) {
            list = Collections.singletonList(Item.REGISTRY.getNameForObject(stack).toString());
        }
        return list;
    }

    private ResourceLocation getItemLocation(String location) {
        ResourceLocation resourcelocation = new ResourceLocation(location);
        if (Reflector.ForgeHooksClient.exists()) {
            resourcelocation = new ResourceLocation(location.replaceAll("#.*", ""));
        }
        return new ResourceLocation(resourcelocation.getNamespace(), "item/" + resourcelocation.getPath());
    }

    private void bakeBlockModels() {
        for (ModelResourceLocation modelResourceLocation : this.variants.keySet()) {
            IBakedModel ibakedmodel = this.createRandomModelForVariantList(this.variants.get(modelResourceLocation), modelResourceLocation.toString());
            if (ibakedmodel == null) continue;
            this.bakedRegistry.putObject(modelResourceLocation, ibakedmodel);
        }
        for (Map.Entry entry : this.multipartVariantMap.entrySet()) {
            ModelBlockDefinition modelblockdefinition = (ModelBlockDefinition)entry.getKey();
            Multipart multipart = modelblockdefinition.getMultipartData();
            String s = Block.REGISTRY.getNameForObject(multipart.getStateContainer().getBlock()).toString();
            MultipartBakedModel.Builder multipartbakedmodel$builder = new MultipartBakedModel.Builder();
            for (Selector selector : multipart.getSelectors()) {
                IBakedModel ibakedmodel1 = this.createRandomModelForVariantList(selector.getVariantList(), "selector of " + s);
                if (ibakedmodel1 == null) continue;
                multipartbakedmodel$builder.putModel(selector.getPredicate(multipart.getStateContainer()), ibakedmodel1);
            }
            IBakedModel ibakedmodel2 = multipartbakedmodel$builder.makeMultipartModel();
            for (ModelResourceLocation modelresourcelocation1 : (Collection)entry.getValue()) {
                if (modelblockdefinition.hasVariant(modelresourcelocation1.getVariant())) continue;
                this.bakedRegistry.putObject(modelresourcelocation1, ibakedmodel2);
            }
        }
    }

    @Nullable
    private IBakedModel createRandomModelForVariantList(VariantList variantsIn, String modelLocation) {
        if (variantsIn.getVariantList().isEmpty()) {
            return null;
        }
        WeightedBakedModel.Builder weightedbakedmodel$builder = new WeightedBakedModel.Builder();
        int i = 0;
        for (Variant variant : variantsIn.getVariantList()) {
            ModelBlock modelblock = this.models.get(variant.getModelLocation());
            if (modelblock != null && modelblock.isResolved()) {
                if (modelblock.getElements().isEmpty()) {
                    LOGGER.warn("Missing elements for: {}", modelLocation);
                    continue;
                }
                IBakedModel ibakedmodel = this.bakeModel(modelblock, variant.getRotation(), variant.isUvLock());
                if (ibakedmodel == null) continue;
                ++i;
                weightedbakedmodel$builder.add(ibakedmodel, variant.getWeight());
                continue;
            }
            LOGGER.warn("Missing model for: {}", modelLocation);
        }
        IBakedModel ibakedmodel1 = null;
        if (i == 0) {
            LOGGER.warn("No weighted models for: {}", modelLocation);
        } else {
            ibakedmodel1 = i == 1 ? weightedbakedmodel$builder.first() : weightedbakedmodel$builder.build();
        }
        return ibakedmodel1;
    }

    private void bakeItemModels() {
        for (Map.Entry<String, ResourceLocation> entry : this.itemLocations.entrySet()) {
            ModelBlock modelblock;
            ResourceLocation resourcelocation = entry.getValue();
            ModelResourceLocation modelresourcelocation = new ModelResourceLocation(entry.getKey(), "inventory");
            if (Reflector.ForgeHooksClient.exists()) {
                modelresourcelocation = (ModelResourceLocation)Reflector.call(Reflector.ModelLoader_getInventoryVariant, entry.getKey());
            }
            if ((modelblock = this.models.get(resourcelocation)) != null && modelblock.isResolved()) {
                if (modelblock.getElements().isEmpty()) {
                    LOGGER.warn("Missing elements for: {}", resourcelocation);
                    continue;
                }
                if (this.isCustomRenderer(modelblock)) {
                    this.bakedRegistry.putObject(modelresourcelocation, new BuiltInModel(modelblock.getAllTransforms(), modelblock.createOverrides()));
                    continue;
                }
                IBakedModel ibakedmodel = this.bakeModel(modelblock, ModelRotation.X0_Y0, false);
                if (ibakedmodel == null) continue;
                this.bakedRegistry.putObject(modelresourcelocation, ibakedmodel);
                continue;
            }
            LOGGER.warn("Missing model for: {}", resourcelocation);
        }
    }

    private Set<ResourceLocation> getVariantsTextureLocations() {
        HashSet<ResourceLocation> set = Sets.newHashSet();
        ArrayList<ModelResourceLocation> list = Lists.newArrayList(this.variants.keySet());
        Collections.sort(list, new Comparator<ModelResourceLocation>(){

            @Override
            public int compare(ModelResourceLocation p_compare_1_, ModelResourceLocation p_compare_2_) {
                return p_compare_1_.toString().compareTo(p_compare_2_.toString());
            }
        });
        for (ModelResourceLocation modelresourcelocation : list) {
            VariantList variantlist = this.variants.get(modelresourcelocation);
            for (Variant variant : variantlist.getVariantList()) {
                ModelBlock modelblock = this.models.get(variant.getModelLocation());
                if (modelblock == null) {
                    LOGGER.warn("Missing model for: {}", modelresourcelocation);
                    continue;
                }
                set.addAll(this.getTextureLocations(modelblock));
            }
        }
        for (ModelBlockDefinition modelblockdefinition : this.multipartVariantMap.keySet()) {
            for (VariantList variantlist1 : modelblockdefinition.getMultipartData().getVariants()) {
                for (Variant variant1 : variantlist1.getVariantList()) {
                    ModelBlock modelblock1 = this.models.get(variant1.getModelLocation());
                    if (modelblock1 == null) {
                        LOGGER.warn("Missing model for: {}", Block.REGISTRY.getNameForObject(modelblockdefinition.getMultipartData().getStateContainer().getBlock()));
                        continue;
                    }
                    set.addAll(this.getTextureLocations(modelblock1));
                }
            }
        }
        set.addAll(LOCATIONS_BUILTIN_TEXTURES);
        return set;
    }

    @Nullable
    public IBakedModel bakeModel(ModelBlock modelBlockIn, ModelRotation modelRotationIn, boolean uvLocked) {
        TextureAtlasSprite textureatlassprite = this.sprites.get(new ResourceLocation(modelBlockIn.resolveTextureName("particle")));
        SimpleBakedModel.Builder simplebakedmodel$builder = new SimpleBakedModel.Builder(modelBlockIn, modelBlockIn.createOverrides()).setTexture(textureatlassprite);
        if (modelBlockIn.getElements().isEmpty()) {
            return null;
        }
        for (BlockPart blockpart : modelBlockIn.getElements()) {
            for (EnumFacing enumfacing : blockpart.mapFaces.keySet()) {
                BlockPartFace blockpartface = blockpart.mapFaces.get(enumfacing);
                TextureAtlasSprite textureatlassprite1 = this.sprites.get(new ResourceLocation(modelBlockIn.resolveTextureName(blockpartface.texture)));
                if (blockpartface.cullFace == null) {
                    simplebakedmodel$builder.addGeneralQuad(this.makeBakedQuad(blockpart, blockpartface, textureatlassprite1, enumfacing, modelRotationIn, uvLocked));
                    continue;
                }
                simplebakedmodel$builder.addFaceQuad(modelRotationIn.rotateFace(blockpartface.cullFace), this.makeBakedQuad(blockpart, blockpartface, textureatlassprite1, enumfacing, modelRotationIn, uvLocked));
            }
        }
        return simplebakedmodel$builder.makeBakedModel();
    }

    protected IBakedModel bakeModel(ModelBlock p_bakeModel_1_, ITransformation p_bakeModel_2_, boolean p_bakeModel_3_) {
        TextureAtlasSprite textureatlassprite = this.sprites.get(new ResourceLocation(p_bakeModel_1_.resolveTextureName("particle")));
        SimpleBakedModel.Builder simplebakedmodel$builder = new SimpleBakedModel.Builder(p_bakeModel_1_, p_bakeModel_1_.createOverrides()).setTexture(textureatlassprite);
        if (p_bakeModel_1_.getElements().isEmpty()) {
            return null;
        }
        for (BlockPart blockpart : p_bakeModel_1_.getElements()) {
            for (EnumFacing enumfacing : blockpart.mapFaces.keySet()) {
                BlockPartFace blockpartface = blockpart.mapFaces.get(enumfacing);
                TextureAtlasSprite textureatlassprite1 = this.sprites.get(new ResourceLocation(p_bakeModel_1_.resolveTextureName(blockpartface.texture)));
                boolean flag = true;
                if (Reflector.ForgeHooksClient.exists()) {
                    flag = TRSRTransformation.isInteger(p_bakeModel_2_.getMatrix());
                }
                if (blockpartface.cullFace != null && flag) {
                    simplebakedmodel$builder.addFaceQuad(p_bakeModel_2_.rotate(blockpartface.cullFace), this.makeBakedQuad(blockpart, blockpartface, textureatlassprite1, enumfacing, p_bakeModel_2_, p_bakeModel_3_));
                    continue;
                }
                simplebakedmodel$builder.addGeneralQuad(this.makeBakedQuad(blockpart, blockpartface, textureatlassprite1, enumfacing, p_bakeModel_2_, p_bakeModel_3_));
            }
        }
        return simplebakedmodel$builder.makeBakedModel();
    }

    private BakedQuad makeBakedQuad(BlockPart p_177589_1_, BlockPartFace p_177589_2_, TextureAtlasSprite p_177589_3_, EnumFacing p_177589_4_, ModelRotation p_177589_5_, boolean p_177589_6_) {
        return Reflector.ForgeHooksClient.exists() ? this.makeBakedQuad(p_177589_1_, p_177589_2_, p_177589_3_, p_177589_4_, p_177589_5_, p_177589_6_) : this.faceBakery.makeBakedQuad(p_177589_1_.positionFrom, p_177589_1_.positionTo, p_177589_2_, p_177589_3_, p_177589_4_, p_177589_5_, p_177589_1_.partRotation, p_177589_6_, p_177589_1_.shade);
    }

    protected BakedQuad makeBakedQuad(BlockPart p_makeBakedQuad_1_, BlockPartFace p_makeBakedQuad_2_, TextureAtlasSprite p_makeBakedQuad_3_, EnumFacing p_makeBakedQuad_4_, ITransformation p_makeBakedQuad_5_, boolean p_makeBakedQuad_6_) {
        return this.faceBakery.makeBakedQuad(p_makeBakedQuad_1_.positionFrom, p_makeBakedQuad_1_.positionTo, p_makeBakedQuad_2_, p_makeBakedQuad_3_, p_makeBakedQuad_4_, p_makeBakedQuad_5_, p_makeBakedQuad_1_.partRotation, p_makeBakedQuad_6_, p_makeBakedQuad_1_.shade);
    }

    private void loadModelsCheck() {
        this.loadModels();
        for (ModelBlock modelblock : this.models.values()) {
            modelblock.getParentFromMap(this.models);
        }
        ModelBlock.checkModelHierarchy(this.models);
    }

    private void loadModels() {
        ArrayDeque<ResourceLocation> deque = Queues.newArrayDeque();
        HashSet<ResourceLocation> set = Sets.newHashSet();
        for (ResourceLocation resourcelocation : this.models.keySet()) {
            set.add(resourcelocation);
            this.addModelParentLocation(deque, set, this.models.get(resourcelocation));
        }
        while (!deque.isEmpty()) {
            ResourceLocation resourcelocation1 = (ResourceLocation)deque.pop();
            try {
                if (this.models.get(resourcelocation1) != null) continue;
                ModelBlock modelblock = this.loadModel(resourcelocation1);
                this.models.put(resourcelocation1, modelblock);
                this.addModelParentLocation(deque, set, modelblock);
            }
            catch (Exception var5) {
                LOGGER.warn("In parent chain: {}; unable to load model: '{}'", JOINER.join(this.getParentPath(resourcelocation1)), resourcelocation1);
            }
            set.add(resourcelocation1);
        }
    }

    private void addModelParentLocation(Deque<ResourceLocation> p_188633_1_, Set<ResourceLocation> p_188633_2_, ModelBlock p_188633_3_) {
        ResourceLocation resourcelocation = p_188633_3_.getParentLocation();
        if (resourcelocation != null && !p_188633_2_.contains(resourcelocation)) {
            p_188633_1_.add(resourcelocation);
        }
    }

    private List<ResourceLocation> getParentPath(ResourceLocation p_177573_1_) {
        ArrayList<ResourceLocation> list = Lists.newArrayList(p_177573_1_);
        ResourceLocation resourcelocation = p_177573_1_;
        while ((resourcelocation = this.getParentLocation(resourcelocation)) != null) {
            list.add(0, resourcelocation);
        }
        return list;
    }

    @Nullable
    private ResourceLocation getParentLocation(ResourceLocation p_177576_1_) {
        for (Map.Entry<ResourceLocation, ModelBlock> entry : this.models.entrySet()) {
            ModelBlock modelblock = entry.getValue();
            if (modelblock == null || !p_177576_1_.equals(modelblock.getParentLocation())) continue;
            return entry.getKey();
        }
        return null;
    }

    private Set<ResourceLocation> getTextureLocations(ModelBlock p_177585_1_) {
        HashSet<ResourceLocation> set = Sets.newHashSet();
        for (BlockPart blockpart : p_177585_1_.getElements()) {
            for (BlockPartFace blockpartface : blockpart.mapFaces.values()) {
                ResourceLocation resourcelocation = new ResourceLocation(p_177585_1_.resolveTextureName(blockpartface.texture));
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
        ITextureMapPopulator itexturemappopulator = new ITextureMapPopulator(){

            @Override
            public void registerSprites(TextureMap textureMapIn) {
                for (ResourceLocation resourcelocation : set) {
                    TextureAtlasSprite textureatlassprite = textureMapIn.registerSprite(resourcelocation);
                    ModelBakery.this.sprites.put(resourcelocation, textureatlassprite);
                }
            }
        };
        this.textureMap.loadSprites(this.resourceManager, itexturemappopulator);
        this.sprites.put(new ResourceLocation("missingno"), this.textureMap.getMissingSprite());
    }

    private Set<ResourceLocation> getItemsTextureLocations() {
        HashSet<ResourceLocation> set = Sets.newHashSet();
        for (ResourceLocation resourcelocation : this.itemLocations.values()) {
            ModelBlock modelblock = this.models.get(resourcelocation);
            if (modelblock == null) continue;
            set.add(new ResourceLocation(modelblock.resolveTextureName("particle")));
            if (this.hasItemModel(modelblock)) {
                for (String s : ItemModelGenerator.LAYERS) {
                    set.add(new ResourceLocation(modelblock.resolveTextureName(s)));
                }
                continue;
            }
            if (this.isCustomRenderer(modelblock)) continue;
            for (BlockPart blockpart : modelblock.getElements()) {
                for (BlockPartFace blockpartface : blockpart.mapFaces.values()) {
                    ResourceLocation resourcelocation1 = new ResourceLocation(modelblock.resolveTextureName(blockpartface.texture));
                    set.add(resourcelocation1);
                }
            }
        }
        return set;
    }

    private boolean hasItemModel(@Nullable ModelBlock p_177581_1_) {
        if (p_177581_1_ == null) {
            return false;
        }
        return p_177581_1_.getRootModel() == MODEL_GENERATED;
    }

    private boolean isCustomRenderer(@Nullable ModelBlock p_177587_1_) {
        if (p_177587_1_ == null) {
            return false;
        }
        ModelBlock modelblock = p_177587_1_.getRootModel();
        return modelblock == MODEL_ENTITY;
    }

    private void makeItemModels() {
        for (ResourceLocation resourcelocation : this.itemLocations.values()) {
            ModelBlock modelblock = this.models.get(resourcelocation);
            if (this.hasItemModel(modelblock)) {
                ModelBlock modelblock1 = this.makeItemModel(modelblock);
                if (modelblock1 != null) {
                    modelblock1.name = resourcelocation.toString();
                }
                this.models.put(resourcelocation, modelblock1);
                continue;
            }
            if (!this.isCustomRenderer(modelblock)) continue;
            this.models.put(resourcelocation, modelblock);
        }
        for (TextureAtlasSprite textureatlassprite : this.sprites.values()) {
            if (textureatlassprite.hasAnimationMetadata()) continue;
            textureatlassprite.clearFramesTextureData();
        }
    }

    private ModelBlock makeItemModel(ModelBlock p_177582_1_) {
        return this.itemModelGenerator.makeItemModel(this.textureMap, p_177582_1_);
    }

    public ModelBlock getModelBlock(ResourceLocation p_getModelBlock_1_) {
        ModelBlock modelblock = this.models.get(p_getModelBlock_1_);
        return modelblock;
    }

    public static void fixModelLocations(ModelBlock p_fixModelLocations_0_, String p_fixModelLocations_1_) {
        ResourceLocation resourcelocation = ModelBakery.fixModelLocation(p_fixModelLocations_0_.parentLocation, p_fixModelLocations_1_);
        if (resourcelocation != p_fixModelLocations_0_.parentLocation) {
            p_fixModelLocations_0_.parentLocation = resourcelocation;
        }
        if (p_fixModelLocations_0_.textures != null) {
            for (Map.Entry<String, String> entry : p_fixModelLocations_0_.textures.entrySet()) {
                String s = entry.getValue();
                String s1 = ModelBakery.fixResourcePath(s, p_fixModelLocations_1_);
                if (s1 == s) continue;
                entry.setValue(s1);
            }
        }
    }

    public static ResourceLocation fixModelLocation(ResourceLocation p_fixModelLocation_0_, String p_fixModelLocation_1_) {
        if (p_fixModelLocation_0_ != null && p_fixModelLocation_1_ != null) {
            if (!p_fixModelLocation_0_.getNamespace().equals("minecraft")) {
                return p_fixModelLocation_0_;
            }
            String s = p_fixModelLocation_0_.getPath();
            String s1 = ModelBakery.fixResourcePath(s, p_fixModelLocation_1_);
            if (s1 != s) {
                p_fixModelLocation_0_ = new ResourceLocation(p_fixModelLocation_0_.getNamespace(), s1);
            }
            return p_fixModelLocation_0_;
        }
        return p_fixModelLocation_0_;
    }

    private static String fixResourcePath(String p_fixResourcePath_0_, String p_fixResourcePath_1_) {
        p_fixResourcePath_0_ = TextureUtils.fixResourcePath(p_fixResourcePath_0_, p_fixResourcePath_1_);
        p_fixResourcePath_0_ = StrUtils.removeSuffix(p_fixResourcePath_0_, ".json");
        p_fixResourcePath_0_ = StrUtils.removeSuffix(p_fixResourcePath_0_, ".png");
        return p_fixResourcePath_0_;
    }

    protected void registerMultipartVariant(ModelBlockDefinition p_registerMultipartVariant_1_, Collection<ModelResourceLocation> p_registerMultipartVariant_2_) {
        this.multipartVariantMap.put(p_registerMultipartVariant_1_, p_registerMultipartVariant_2_);
    }

    public static void registerItemVariants(Item p_registerItemVariants_0_, ResourceLocation ... p_registerItemVariants_1_) {
        IRegistryDelegate iregistrydelegate = (IRegistryDelegate)Reflector.getFieldValue(p_registerItemVariants_0_, Reflector.ForgeItem_delegate);
        if (!customVariantNames.containsKey(iregistrydelegate)) {
            customVariantNames.put(iregistrydelegate, Sets.newHashSet());
        }
        for (ResourceLocation resourcelocation : p_registerItemVariants_1_) {
            customVariantNames.get(iregistrydelegate).add(resourcelocation.toString());
        }
    }

    static {
        BUILT_IN_MODELS.put("missing", MISSING_MODEL_MESH);
        ModelBakery.MODEL_GENERATED.name = "generation marker";
        ModelBakery.MODEL_ENTITY.name = "block entity marker";
    }
}

