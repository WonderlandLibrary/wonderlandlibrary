package net.minecraft.client.resources.model;

import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Queues;
import com.google.common.collect.Sets;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.vecmath.Matrix4f;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockPart;
import net.minecraft.client.renderer.block.model.BlockPartFace;
import net.minecraft.client.renderer.block.model.FaceBakery;
import net.minecraft.client.renderer.block.model.ItemModelGenerator;
import net.minecraft.client.renderer.block.model.ModelBlock;
import net.minecraft.client.renderer.block.model.ModelBlockDefinition;
import net.minecraft.client.renderer.texture.IIconCreator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.model.BuiltInModel;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.resources.model.ModelRotation;
import net.minecraft.client.resources.model.SimpleBakedModel;
import net.minecraft.client.resources.model.WeightedBakedModel;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IRegistry;
import net.minecraft.util.RegistrySimple;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ITransformation;
import net.minecraftforge.client.model.TRSRTransformation;
import net.minecraftforge.fml.common.registry.RegistryDelegate;
import net.optifine.CustomItems;
import net.optifine.reflect.Reflector;
import net.optifine.reflect.ReflectorField;
import net.optifine.reflect.ReflectorMethod;
import net.optifine.util.StrUtils;
import net.optifine.util.TextureUtils;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ModelBakery {
    private static final Set<ResourceLocation> LOCATIONS_BUILTIN_TEXTURES = Sets.newHashSet((Object[])new ResourceLocation[]{new ResourceLocation("blocks/water_flow"), new ResourceLocation("blocks/water_still"), new ResourceLocation("blocks/lava_flow"), new ResourceLocation("blocks/lava_still"), new ResourceLocation("blocks/destroy_stage_0"), new ResourceLocation("blocks/destroy_stage_1"), new ResourceLocation("blocks/destroy_stage_2"), new ResourceLocation("blocks/destroy_stage_3"), new ResourceLocation("blocks/destroy_stage_4"), new ResourceLocation("blocks/destroy_stage_5"), new ResourceLocation("blocks/destroy_stage_6"), new ResourceLocation("blocks/destroy_stage_7"), new ResourceLocation("blocks/destroy_stage_8"), new ResourceLocation("blocks/destroy_stage_9"), new ResourceLocation("items/empty_armor_slot_helmet"), new ResourceLocation("items/empty_armor_slot_chestplate"), new ResourceLocation("items/empty_armor_slot_leggings"), new ResourceLocation("items/empty_armor_slot_boots")});
    private static final Logger LOGGER = LogManager.getLogger();
    protected static final ModelResourceLocation MODEL_MISSING = new ModelResourceLocation("builtin/missing", "missing");
    private static final Map<String, String> BUILT_IN_MODELS = Maps.newHashMap();
    private static final Joiner JOINER = Joiner.on((String)" -> ");
    private final IResourceManager resourceManager;
    private final Map<ResourceLocation, TextureAtlasSprite> sprites = Maps.newHashMap();
    private final Map<ResourceLocation, ModelBlock> models = Maps.newLinkedHashMap();
    private final Map<ModelResourceLocation, ModelBlockDefinition.Variants> variants = Maps.newLinkedHashMap();
    private final TextureMap textureMap;
    private final BlockModelShapes blockModelShapes;
    private final FaceBakery faceBakery = new FaceBakery();
    private final ItemModelGenerator itemModelGenerator = new ItemModelGenerator();
    private RegistrySimple<ModelResourceLocation, IBakedModel> bakedRegistry = new RegistrySimple();
    private static final ModelBlock MODEL_GENERATED = ModelBlock.deserialize((String)"{\"elements\":[{  \"from\": [0, 0, 0],   \"to\": [16, 16, 16],   \"faces\": {       \"down\": {\"uv\": [0, 0, 16, 16], \"texture\":\"\"}   }}]}");
    private static final ModelBlock MODEL_COMPASS = ModelBlock.deserialize((String)"{\"elements\":[{  \"from\": [0, 0, 0],   \"to\": [16, 16, 16],   \"faces\": {       \"down\": {\"uv\": [0, 0, 16, 16], \"texture\":\"\"}   }}]}");
    private static final ModelBlock MODEL_CLOCK = ModelBlock.deserialize((String)"{\"elements\":[{  \"from\": [0, 0, 0],   \"to\": [16, 16, 16],   \"faces\": {       \"down\": {\"uv\": [0, 0, 16, 16], \"texture\":\"\"}   }}]}");
    private static final ModelBlock MODEL_ENTITY = ModelBlock.deserialize((String)"{\"elements\":[{  \"from\": [0, 0, 0],   \"to\": [16, 16, 16],   \"faces\": {       \"down\": {\"uv\": [0, 0, 16, 16], \"texture\":\"\"}   }}]}");
    private Map<String, ResourceLocation> itemLocations = Maps.newLinkedHashMap();
    private final Map<ResourceLocation, ModelBlockDefinition> blockDefinitions = Maps.newHashMap();
    private Map<Item, List<String>> variantNames = Maps.newIdentityHashMap();
    private static Map<RegistryDelegate<Item>, Set<String>> customVariantNames = Maps.newHashMap();

    public ModelBakery(IResourceManager p_i46085_1_, TextureMap p_i46085_2_, BlockModelShapes p_i46085_3_) {
        this.resourceManager = p_i46085_1_;
        this.textureMap = p_i46085_2_;
        this.blockModelShapes = p_i46085_3_;
    }

    public IRegistry<ModelResourceLocation, IBakedModel> setupModelRegistry() {
        this.loadVariantItemModels();
        this.loadModelsCheck();
        this.loadSprites();
        this.bakeItemModels();
        this.bakeBlockModels();
        return this.bakedRegistry;
    }

    private void loadVariantItemModels() {
        this.loadVariants((Collection<ModelResourceLocation>)this.blockModelShapes.getBlockStateMapper().putAllStateModelLocations().values());
        this.variants.put((Object)MODEL_MISSING, (Object)new ModelBlockDefinition.Variants(MODEL_MISSING.getVariant(), (List)Lists.newArrayList((Object[])new ModelBlockDefinition.Variant[]{new ModelBlockDefinition.Variant(new ResourceLocation(MODEL_MISSING.getResourcePath()), ModelRotation.X0_Y0, false, 1)})));
        ResourceLocation resourcelocation = new ResourceLocation("item_frame");
        ModelBlockDefinition modelblockdefinition = this.getModelBlockDefinition(resourcelocation);
        this.registerVariant(modelblockdefinition, new ModelResourceLocation(resourcelocation, "normal"));
        this.registerVariant(modelblockdefinition, new ModelResourceLocation(resourcelocation, "map"));
        this.loadVariantModels();
        this.loadItemModels();
    }

    private void loadVariants(Collection<ModelResourceLocation> p_177591_1_) {
        for (ModelResourceLocation modelresourcelocation : p_177591_1_) {
            try {
                ModelBlockDefinition modelblockdefinition = this.getModelBlockDefinition((ResourceLocation)modelresourcelocation);
                try {
                    this.registerVariant(modelblockdefinition, modelresourcelocation);
                }
                catch (Exception exception) {
                    LOGGER.warn("Unable to load variant: " + modelresourcelocation.getVariant() + " from " + modelresourcelocation, (Throwable)exception);
                }
            }
            catch (Exception exception1) {
                LOGGER.warn("Unable to load definition " + modelresourcelocation, (Throwable)exception1);
            }
        }
    }

    private void registerVariant(ModelBlockDefinition p_177569_1_, ModelResourceLocation p_177569_2_) {
        this.variants.put((Object)p_177569_2_, (Object)p_177569_1_.getVariants(p_177569_2_.getVariant()));
    }

    private ModelBlockDefinition getModelBlockDefinition(ResourceLocation p_177586_1_) {
        ResourceLocation resourcelocation = this.getBlockStateLocation(p_177586_1_);
        ModelBlockDefinition modelblockdefinition = (ModelBlockDefinition)this.blockDefinitions.get((Object)resourcelocation);
        if (modelblockdefinition == null) {
            ArrayList list = Lists.newArrayList();
            try {
                for (IResource iresource : this.resourceManager.getAllResources(resourcelocation)) {
                    InputStream inputstream = null;
                    try {
                        inputstream = iresource.getInputStream();
                        ModelBlockDefinition modelblockdefinition1 = ModelBlockDefinition.parseFromReader((Reader)new InputStreamReader(inputstream, Charsets.UTF_8));
                        list.add((Object)modelblockdefinition1);
                    }
                    catch (Exception exception) {
                        throw new RuntimeException("Encountered an exception when loading model definition of '" + p_177586_1_ + "' from: '" + iresource.getResourceLocation() + "' in resourcepack: '" + iresource.getResourcePackName() + "'", (Throwable)exception);
                    }
                    finally {
                        IOUtils.closeQuietly((InputStream)inputstream);
                    }
                }
            }
            catch (IOException ioexception) {
                throw new RuntimeException("Encountered an exception when loading model definition of model " + resourcelocation.toString(), (Throwable)ioexception);
            }
            modelblockdefinition = new ModelBlockDefinition((List)list);
            this.blockDefinitions.put((Object)resourcelocation, (Object)modelblockdefinition);
        }
        return modelblockdefinition;
    }

    private ResourceLocation getBlockStateLocation(ResourceLocation p_177584_1_) {
        return new ResourceLocation(p_177584_1_.getResourceDomain(), "blockstates/" + p_177584_1_.getResourcePath() + ".json");
    }

    private void loadVariantModels() {
        for (ModelResourceLocation modelresourcelocation : this.variants.keySet()) {
            for (ModelBlockDefinition.Variant modelblockdefinition$variant : ((ModelBlockDefinition.Variants)this.variants.get((Object)modelresourcelocation)).getVariants()) {
                ResourceLocation resourcelocation = modelblockdefinition$variant.getModelLocation();
                if (this.models.get((Object)resourcelocation) != null) continue;
                try {
                    ModelBlock modelblock = this.loadModel(resourcelocation);
                    this.models.put((Object)resourcelocation, (Object)modelblock);
                }
                catch (Exception exception) {
                    LOGGER.warn("Unable to load block model: '" + resourcelocation + "' for variant: '" + modelresourcelocation + "'", (Throwable)exception);
                }
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private ModelBlock loadModel(ResourceLocation p_177594_1_) throws IOException {
        ModelBlock modelblock;
        InputStreamReader reader;
        String s = p_177594_1_.getResourcePath();
        if ("builtin/generated".equals((Object)s)) {
            return MODEL_GENERATED;
        }
        if ("builtin/compass".equals((Object)s)) {
            return MODEL_COMPASS;
        }
        if ("builtin/clock".equals((Object)s)) {
            return MODEL_CLOCK;
        }
        if ("builtin/entity".equals((Object)s)) {
            return MODEL_ENTITY;
        }
        if (s.startsWith("builtin/")) {
            String s1 = s.substring("builtin/".length());
            String s2 = (String)BUILT_IN_MODELS.get((Object)s1);
            if (s2 == null) {
                throw new FileNotFoundException(p_177594_1_.toString());
            }
            reader = new StringReader(s2);
        } else {
            p_177594_1_ = this.getModelLocation(p_177594_1_);
            IResource iresource = this.resourceManager.getResource(p_177594_1_);
            reader = new InputStreamReader(iresource.getInputStream(), Charsets.UTF_8);
        }
        try {
            ModelBlock modelblock1 = ModelBlock.deserialize((Reader)reader);
            modelblock1.name = p_177594_1_.toString();
            modelblock = modelblock1;
            String s3 = TextureUtils.getBasePath((String)p_177594_1_.getResourcePath());
            ModelBakery.fixModelLocations(modelblock1, s3);
        }
        finally {
            reader.close();
        }
        return modelblock;
    }

    private ResourceLocation getModelLocation(ResourceLocation p_177580_1_) {
        ResourceLocation resourcelocation = p_177580_1_;
        String s = p_177580_1_.getResourcePath();
        if (!s.startsWith("mcpatcher") && !s.startsWith("optifine")) {
            return new ResourceLocation(p_177580_1_.getResourceDomain(), "models/" + p_177580_1_.getResourcePath() + ".json");
        }
        if (!s.endsWith(".json")) {
            resourcelocation = new ResourceLocation(p_177580_1_.getResourceDomain(), s + ".json");
        }
        return resourcelocation;
    }

    private void loadItemModels() {
        this.registerVariantNames();
        for (Item item : Item.itemRegistry) {
            for (String s : this.getVariantNames(item)) {
                ResourceLocation resourcelocation = this.getItemLocation(s);
                this.itemLocations.put((Object)s, (Object)resourcelocation);
                if (this.models.get((Object)resourcelocation) != null) continue;
                try {
                    ModelBlock modelblock = this.loadModel(resourcelocation);
                    this.models.put((Object)resourcelocation, (Object)modelblock);
                }
                catch (Exception exception) {
                    LOGGER.warn("Unable to load item model: '" + resourcelocation + "' for item: '" + Item.itemRegistry.getNameForObject((Object)item) + "'", (Throwable)exception);
                }
            }
        }
    }

    public void loadItemModel(String p_loadItemModel_1_, ResourceLocation p_loadItemModel_2_, ResourceLocation p_loadItemModel_3_) {
        this.itemLocations.put((Object)p_loadItemModel_1_, (Object)p_loadItemModel_2_);
        if (this.models.get((Object)p_loadItemModel_2_) == null) {
            try {
                ModelBlock modelblock = this.loadModel(p_loadItemModel_2_);
                this.models.put((Object)p_loadItemModel_2_, (Object)modelblock);
            }
            catch (Exception exception) {
                LOGGER.warn("Unable to load item model: '{}' for item: '{}'", new Object[]{p_loadItemModel_2_, p_loadItemModel_3_});
                LOGGER.warn(exception.getClass().getName() + ": " + exception.getMessage());
            }
        }
    }

    private void registerVariantNames() {
        this.variantNames.clear();
        this.variantNames.put((Object)Item.getItemFromBlock((Block)Blocks.stone), (Object)Lists.newArrayList((Object[])new String[]{"stone", "granite", "granite_smooth", "diorite", "diorite_smooth", "andesite", "andesite_smooth"}));
        this.variantNames.put((Object)Item.getItemFromBlock((Block)Blocks.dirt), (Object)Lists.newArrayList((Object[])new String[]{"dirt", "coarse_dirt", "podzol"}));
        this.variantNames.put((Object)Item.getItemFromBlock((Block)Blocks.planks), (Object)Lists.newArrayList((Object[])new String[]{"oak_planks", "spruce_planks", "birch_planks", "jungle_planks", "acacia_planks", "dark_oak_planks"}));
        this.variantNames.put((Object)Item.getItemFromBlock((Block)Blocks.sapling), (Object)Lists.newArrayList((Object[])new String[]{"oak_sapling", "spruce_sapling", "birch_sapling", "jungle_sapling", "acacia_sapling", "dark_oak_sapling"}));
        this.variantNames.put((Object)Item.getItemFromBlock((Block)Blocks.sand), (Object)Lists.newArrayList((Object[])new String[]{"sand", "red_sand"}));
        this.variantNames.put((Object)Item.getItemFromBlock((Block)Blocks.log), (Object)Lists.newArrayList((Object[])new String[]{"oak_log", "spruce_log", "birch_log", "jungle_log"}));
        this.variantNames.put((Object)Item.getItemFromBlock((Block)Blocks.leaves), (Object)Lists.newArrayList((Object[])new String[]{"oak_leaves", "spruce_leaves", "birch_leaves", "jungle_leaves"}));
        this.variantNames.put((Object)Item.getItemFromBlock((Block)Blocks.sponge), (Object)Lists.newArrayList((Object[])new String[]{"sponge", "sponge_wet"}));
        this.variantNames.put((Object)Item.getItemFromBlock((Block)Blocks.sandstone), (Object)Lists.newArrayList((Object[])new String[]{"sandstone", "chiseled_sandstone", "smooth_sandstone"}));
        this.variantNames.put((Object)Item.getItemFromBlock((Block)Blocks.red_sandstone), (Object)Lists.newArrayList((Object[])new String[]{"red_sandstone", "chiseled_red_sandstone", "smooth_red_sandstone"}));
        this.variantNames.put((Object)Item.getItemFromBlock((Block)Blocks.tallgrass), (Object)Lists.newArrayList((Object[])new String[]{"dead_bush", "tall_grass", "fern"}));
        this.variantNames.put((Object)Item.getItemFromBlock((Block)Blocks.deadbush), (Object)Lists.newArrayList((Object[])new String[]{"dead_bush"}));
        this.variantNames.put((Object)Item.getItemFromBlock((Block)Blocks.wool), (Object)Lists.newArrayList((Object[])new String[]{"black_wool", "red_wool", "green_wool", "brown_wool", "blue_wool", "purple_wool", "cyan_wool", "silver_wool", "gray_wool", "pink_wool", "lime_wool", "yellow_wool", "light_blue_wool", "magenta_wool", "orange_wool", "white_wool"}));
        this.variantNames.put((Object)Item.getItemFromBlock((Block)Blocks.yellow_flower), (Object)Lists.newArrayList((Object[])new String[]{"dandelion"}));
        this.variantNames.put((Object)Item.getItemFromBlock((Block)Blocks.red_flower), (Object)Lists.newArrayList((Object[])new String[]{"poppy", "blue_orchid", "allium", "houstonia", "red_tulip", "orange_tulip", "white_tulip", "pink_tulip", "oxeye_daisy"}));
        this.variantNames.put((Object)Item.getItemFromBlock((Block)Blocks.stone_slab), (Object)Lists.newArrayList((Object[])new String[]{"stone_slab", "sandstone_slab", "cobblestone_slab", "brick_slab", "stone_brick_slab", "nether_brick_slab", "quartz_slab"}));
        this.variantNames.put((Object)Item.getItemFromBlock((Block)Blocks.stone_slab2), (Object)Lists.newArrayList((Object[])new String[]{"red_sandstone_slab"}));
        this.variantNames.put((Object)Item.getItemFromBlock((Block)Blocks.stained_glass), (Object)Lists.newArrayList((Object[])new String[]{"black_stained_glass", "red_stained_glass", "green_stained_glass", "brown_stained_glass", "blue_stained_glass", "purple_stained_glass", "cyan_stained_glass", "silver_stained_glass", "gray_stained_glass", "pink_stained_glass", "lime_stained_glass", "yellow_stained_glass", "light_blue_stained_glass", "magenta_stained_glass", "orange_stained_glass", "white_stained_glass"}));
        this.variantNames.put((Object)Item.getItemFromBlock((Block)Blocks.monster_egg), (Object)Lists.newArrayList((Object[])new String[]{"stone_monster_egg", "cobblestone_monster_egg", "stone_brick_monster_egg", "mossy_brick_monster_egg", "cracked_brick_monster_egg", "chiseled_brick_monster_egg"}));
        this.variantNames.put((Object)Item.getItemFromBlock((Block)Blocks.stonebrick), (Object)Lists.newArrayList((Object[])new String[]{"stonebrick", "mossy_stonebrick", "cracked_stonebrick", "chiseled_stonebrick"}));
        this.variantNames.put((Object)Item.getItemFromBlock((Block)Blocks.wooden_slab), (Object)Lists.newArrayList((Object[])new String[]{"oak_slab", "spruce_slab", "birch_slab", "jungle_slab", "acacia_slab", "dark_oak_slab"}));
        this.variantNames.put((Object)Item.getItemFromBlock((Block)Blocks.cobblestone_wall), (Object)Lists.newArrayList((Object[])new String[]{"cobblestone_wall", "mossy_cobblestone_wall"}));
        this.variantNames.put((Object)Item.getItemFromBlock((Block)Blocks.anvil), (Object)Lists.newArrayList((Object[])new String[]{"anvil_intact", "anvil_slightly_damaged", "anvil_very_damaged"}));
        this.variantNames.put((Object)Item.getItemFromBlock((Block)Blocks.quartz_block), (Object)Lists.newArrayList((Object[])new String[]{"quartz_block", "chiseled_quartz_block", "quartz_column"}));
        this.variantNames.put((Object)Item.getItemFromBlock((Block)Blocks.stained_hardened_clay), (Object)Lists.newArrayList((Object[])new String[]{"black_stained_hardened_clay", "red_stained_hardened_clay", "green_stained_hardened_clay", "brown_stained_hardened_clay", "blue_stained_hardened_clay", "purple_stained_hardened_clay", "cyan_stained_hardened_clay", "silver_stained_hardened_clay", "gray_stained_hardened_clay", "pink_stained_hardened_clay", "lime_stained_hardened_clay", "yellow_stained_hardened_clay", "light_blue_stained_hardened_clay", "magenta_stained_hardened_clay", "orange_stained_hardened_clay", "white_stained_hardened_clay"}));
        this.variantNames.put((Object)Item.getItemFromBlock((Block)Blocks.stained_glass_pane), (Object)Lists.newArrayList((Object[])new String[]{"black_stained_glass_pane", "red_stained_glass_pane", "green_stained_glass_pane", "brown_stained_glass_pane", "blue_stained_glass_pane", "purple_stained_glass_pane", "cyan_stained_glass_pane", "silver_stained_glass_pane", "gray_stained_glass_pane", "pink_stained_glass_pane", "lime_stained_glass_pane", "yellow_stained_glass_pane", "light_blue_stained_glass_pane", "magenta_stained_glass_pane", "orange_stained_glass_pane", "white_stained_glass_pane"}));
        this.variantNames.put((Object)Item.getItemFromBlock((Block)Blocks.leaves2), (Object)Lists.newArrayList((Object[])new String[]{"acacia_leaves", "dark_oak_leaves"}));
        this.variantNames.put((Object)Item.getItemFromBlock((Block)Blocks.log2), (Object)Lists.newArrayList((Object[])new String[]{"acacia_log", "dark_oak_log"}));
        this.variantNames.put((Object)Item.getItemFromBlock((Block)Blocks.prismarine), (Object)Lists.newArrayList((Object[])new String[]{"prismarine", "prismarine_bricks", "dark_prismarine"}));
        this.variantNames.put((Object)Item.getItemFromBlock((Block)Blocks.carpet), (Object)Lists.newArrayList((Object[])new String[]{"black_carpet", "red_carpet", "green_carpet", "brown_carpet", "blue_carpet", "purple_carpet", "cyan_carpet", "silver_carpet", "gray_carpet", "pink_carpet", "lime_carpet", "yellow_carpet", "light_blue_carpet", "magenta_carpet", "orange_carpet", "white_carpet"}));
        this.variantNames.put((Object)Item.getItemFromBlock((Block)Blocks.double_plant), (Object)Lists.newArrayList((Object[])new String[]{"sunflower", "syringa", "double_grass", "double_fern", "double_rose", "paeonia"}));
        this.variantNames.put((Object)Items.bow, (Object)Lists.newArrayList((Object[])new String[]{"bow", "bow_pulling_0", "bow_pulling_1", "bow_pulling_2"}));
        this.variantNames.put((Object)Items.coal, (Object)Lists.newArrayList((Object[])new String[]{"coal", "charcoal"}));
        this.variantNames.put((Object)Items.fishing_rod, (Object)Lists.newArrayList((Object[])new String[]{"fishing_rod", "fishing_rod_cast"}));
        this.variantNames.put((Object)Items.fish, (Object)Lists.newArrayList((Object[])new String[]{"cod", "salmon", "clownfish", "pufferfish"}));
        this.variantNames.put((Object)Items.cooked_fish, (Object)Lists.newArrayList((Object[])new String[]{"cooked_cod", "cooked_salmon"}));
        this.variantNames.put((Object)Items.dye, (Object)Lists.newArrayList((Object[])new String[]{"dye_black", "dye_red", "dye_green", "dye_brown", "dye_blue", "dye_purple", "dye_cyan", "dye_silver", "dye_gray", "dye_pink", "dye_lime", "dye_yellow", "dye_light_blue", "dye_magenta", "dye_orange", "dye_white"}));
        this.variantNames.put((Object)Items.potionitem, (Object)Lists.newArrayList((Object[])new String[]{"bottle_drinkable", "bottle_splash"}));
        this.variantNames.put((Object)Items.skull, (Object)Lists.newArrayList((Object[])new String[]{"skull_skeleton", "skull_wither", "skull_zombie", "skull_char", "skull_creeper"}));
        this.variantNames.put((Object)Item.getItemFromBlock((Block)Blocks.oak_fence_gate), (Object)Lists.newArrayList((Object[])new String[]{"oak_fence_gate"}));
        this.variantNames.put((Object)Item.getItemFromBlock((Block)Blocks.oak_fence), (Object)Lists.newArrayList((Object[])new String[]{"oak_fence"}));
        this.variantNames.put((Object)Items.oak_door, (Object)Lists.newArrayList((Object[])new String[]{"oak_door"}));
        for (Map.Entry entry : customVariantNames.entrySet()) {
            this.variantNames.put(((RegistryDelegate)entry.getKey()).get(), (Object)Lists.newArrayList((Iterator)((Set)entry.getValue()).iterator()));
        }
        CustomItems.update();
        CustomItems.loadModels((ModelBakery)this);
    }

    private List<String> getVariantNames(Item p_177596_1_) {
        List list = (List)this.variantNames.get((Object)p_177596_1_);
        if (list == null) {
            list = Collections.singletonList((Object)((ResourceLocation)Item.itemRegistry.getNameForObject((Object)p_177596_1_)).toString());
        }
        return list;
    }

    private ResourceLocation getItemLocation(String p_177583_1_) {
        ResourceLocation resourcelocation = new ResourceLocation(p_177583_1_);
        if (Reflector.ForgeHooksClient.exists()) {
            resourcelocation = new ResourceLocation(p_177583_1_.replaceAll("#.*", ""));
        }
        return new ResourceLocation(resourcelocation.getResourceDomain(), "item/" + resourcelocation.getResourcePath());
    }

    private void bakeBlockModels() {
        for (ModelResourceLocation modelresourcelocation : this.variants.keySet()) {
            WeightedBakedModel.Builder weightedbakedmodel$builder = new WeightedBakedModel.Builder();
            int i = 0;
            for (ModelBlockDefinition.Variant modelblockdefinition$variant : ((ModelBlockDefinition.Variants)this.variants.get((Object)modelresourcelocation)).getVariants()) {
                ModelBlock modelblock = (ModelBlock)this.models.get((Object)modelblockdefinition$variant.getModelLocation());
                if (modelblock != null && modelblock.isResolved()) {
                    ++i;
                    weightedbakedmodel$builder.add(this.bakeModel(modelblock, modelblockdefinition$variant.getRotation(), modelblockdefinition$variant.isUvLocked()), modelblockdefinition$variant.getWeight());
                    continue;
                }
                LOGGER.warn("Missing model for: " + modelresourcelocation);
            }
            if (i == 0) {
                LOGGER.warn("No weighted models for: " + modelresourcelocation);
                continue;
            }
            if (i == 1) {
                this.bakedRegistry.putObject((Object)modelresourcelocation, (Object)weightedbakedmodel$builder.first());
                continue;
            }
            this.bakedRegistry.putObject((Object)modelresourcelocation, (Object)weightedbakedmodel$builder.build());
        }
        for (Map.Entry entry : this.itemLocations.entrySet()) {
            ModelBlock modelblock1;
            ResourceLocation resourcelocation = (ResourceLocation)entry.getValue();
            ModelResourceLocation modelresourcelocation1 = new ModelResourceLocation((String)entry.getKey(), "inventory");
            if (Reflector.ModelLoader_getInventoryVariant.exists()) {
                modelresourcelocation1 = (ModelResourceLocation)Reflector.call((ReflectorMethod)Reflector.ModelLoader_getInventoryVariant, (Object[])new Object[]{entry.getKey()});
            }
            if ((modelblock1 = (ModelBlock)this.models.get((Object)resourcelocation)) != null && modelblock1.isResolved()) {
                if (this.isCustomRenderer(modelblock1)) {
                    this.bakedRegistry.putObject((Object)modelresourcelocation1, (Object)new BuiltInModel(modelblock1.getAllTransforms()));
                    continue;
                }
                this.bakedRegistry.putObject((Object)modelresourcelocation1, (Object)this.bakeModel(modelblock1, ModelRotation.X0_Y0, false));
                continue;
            }
            LOGGER.warn("Missing model for: " + resourcelocation);
        }
    }

    private Set<ResourceLocation> getVariantsTextureLocations() {
        HashSet set = Sets.newHashSet();
        ArrayList list = Lists.newArrayList((Iterable)this.variants.keySet());
        Collections.sort((List)list, (Comparator)new /* Unavailable Anonymous Inner Class!! */);
        for (ModelResourceLocation modelresourcelocation : list) {
            ModelBlockDefinition.Variants modelblockdefinition$variants = (ModelBlockDefinition.Variants)this.variants.get((Object)modelresourcelocation);
            for (ModelBlockDefinition.Variant modelblockdefinition$variant : modelblockdefinition$variants.getVariants()) {
                ModelBlock modelblock = (ModelBlock)this.models.get((Object)modelblockdefinition$variant.getModelLocation());
                if (modelblock == null) {
                    LOGGER.warn("Missing model for: " + modelresourcelocation);
                    continue;
                }
                set.addAll(this.getTextureLocations(modelblock));
            }
        }
        set.addAll(LOCATIONS_BUILTIN_TEXTURES);
        return set;
    }

    public IBakedModel bakeModel(ModelBlock modelBlockIn, ModelRotation modelRotationIn, boolean uvLocked) {
        return this.bakeModel(modelBlockIn, (ITransformation)modelRotationIn, uvLocked);
    }

    protected IBakedModel bakeModel(ModelBlock p_bakeModel_1_, ITransformation p_bakeModel_2_, boolean p_bakeModel_3_) {
        TextureAtlasSprite textureatlassprite = (TextureAtlasSprite)this.sprites.get((Object)new ResourceLocation(p_bakeModel_1_.resolveTextureName("particle")));
        SimpleBakedModel.Builder simplebakedmodel$builder = new SimpleBakedModel.Builder(p_bakeModel_1_).setTexture(textureatlassprite);
        for (BlockPart blockpart : p_bakeModel_1_.getElements()) {
            for (EnumFacing enumfacing : blockpart.mapFaces.keySet()) {
                BlockPartFace blockpartface = (BlockPartFace)blockpart.mapFaces.get((Object)enumfacing);
                TextureAtlasSprite textureatlassprite1 = (TextureAtlasSprite)this.sprites.get((Object)new ResourceLocation(p_bakeModel_1_.resolveTextureName(blockpartface.texture)));
                boolean flag = true;
                if (Reflector.ForgeHooksClient.exists()) {
                    flag = TRSRTransformation.isInteger((Matrix4f)p_bakeModel_2_.getMatrix());
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
        ArrayDeque deque = Queues.newArrayDeque();
        HashSet set = Sets.newHashSet();
        for (ResourceLocation resourcelocation : this.models.keySet()) {
            set.add((Object)resourcelocation);
            ResourceLocation resourcelocation1 = ((ModelBlock)this.models.get((Object)resourcelocation)).getParentLocation();
            if (resourcelocation1 == null) continue;
            deque.add((Object)resourcelocation1);
        }
        while (!deque.isEmpty()) {
            ResourceLocation resourcelocation2 = (ResourceLocation)deque.pop();
            try {
                if (this.models.get((Object)resourcelocation2) != null) continue;
                ModelBlock modelblock = this.loadModel(resourcelocation2);
                this.models.put((Object)resourcelocation2, (Object)modelblock);
                ResourceLocation resourcelocation3 = modelblock.getParentLocation();
                if (resourcelocation3 != null && !set.contains((Object)resourcelocation3)) {
                    deque.add((Object)resourcelocation3);
                }
            }
            catch (Exception var6) {
                LOGGER.warn("In parent chain: " + JOINER.join(this.getParentPath(resourcelocation2)) + "; unable to load model: '" + resourcelocation2 + "'");
            }
            set.add((Object)resourcelocation2);
        }
    }

    private List<ResourceLocation> getParentPath(ResourceLocation p_177573_1_) {
        ArrayList list = Lists.newArrayList((Object[])new ResourceLocation[]{p_177573_1_});
        ResourceLocation resourcelocation = p_177573_1_;
        while ((resourcelocation = this.getParentLocation(resourcelocation)) != null) {
            list.add(0, (Object)resourcelocation);
        }
        return list;
    }

    private ResourceLocation getParentLocation(ResourceLocation p_177576_1_) {
        for (Map.Entry entry : this.models.entrySet()) {
            ModelBlock modelblock = (ModelBlock)entry.getValue();
            if (modelblock == null || !p_177576_1_.equals((Object)modelblock.getParentLocation())) continue;
            return (ResourceLocation)entry.getKey();
        }
        return null;
    }

    private Set<ResourceLocation> getTextureLocations(ModelBlock p_177585_1_) {
        HashSet set = Sets.newHashSet();
        for (BlockPart blockpart : p_177585_1_.getElements()) {
            for (BlockPartFace blockpartface : blockpart.mapFaces.values()) {
                ResourceLocation resourcelocation = new ResourceLocation(p_177585_1_.resolveTextureName(blockpartface.texture));
                set.add((Object)resourcelocation);
            }
        }
        set.add((Object)new ResourceLocation(p_177585_1_.resolveTextureName("particle")));
        return set;
    }

    private void loadSprites() {
        Set<ResourceLocation> set = this.getVariantsTextureLocations();
        set.addAll(this.getItemsTextureLocations());
        set.remove((Object)TextureMap.LOCATION_MISSING_TEXTURE);
        2 iiconcreator = new /* Unavailable Anonymous Inner Class!! */;
        this.textureMap.loadSprites(this.resourceManager, (IIconCreator)iiconcreator);
        this.sprites.put((Object)new ResourceLocation("missingno"), (Object)this.textureMap.getMissingSprite());
    }

    private Set<ResourceLocation> getItemsTextureLocations() {
        HashSet set = Sets.newHashSet();
        for (ResourceLocation resourcelocation : this.itemLocations.values()) {
            ModelBlock modelblock = (ModelBlock)this.models.get((Object)resourcelocation);
            if (modelblock == null) continue;
            set.add((Object)new ResourceLocation(modelblock.resolveTextureName("particle")));
            if (this.hasItemModel(modelblock)) {
                for (String s : ItemModelGenerator.LAYERS) {
                    ResourceLocation resourcelocation2 = new ResourceLocation(modelblock.resolveTextureName(s));
                    if (modelblock.getRootModel() == MODEL_COMPASS && !TextureMap.LOCATION_MISSING_TEXTURE.equals((Object)resourcelocation2)) {
                        TextureAtlasSprite.setLocationNameCompass((String)resourcelocation2.toString());
                    } else if (modelblock.getRootModel() == MODEL_CLOCK && !TextureMap.LOCATION_MISSING_TEXTURE.equals((Object)resourcelocation2)) {
                        TextureAtlasSprite.setLocationNameClock((String)resourcelocation2.toString());
                    }
                    set.add((Object)resourcelocation2);
                }
                continue;
            }
            if (this.isCustomRenderer(modelblock)) continue;
            for (BlockPart blockpart : modelblock.getElements()) {
                for (BlockPartFace blockpartface : blockpart.mapFaces.values()) {
                    ResourceLocation resourcelocation1 = new ResourceLocation(modelblock.resolveTextureName(blockpartface.texture));
                    set.add((Object)resourcelocation1);
                }
            }
        }
        return set;
    }

    private boolean hasItemModel(ModelBlock p_177581_1_) {
        if (p_177581_1_ == null) {
            return false;
        }
        ModelBlock modelblock = p_177581_1_.getRootModel();
        return modelblock == MODEL_GENERATED || modelblock == MODEL_COMPASS || modelblock == MODEL_CLOCK;
    }

    private boolean isCustomRenderer(ModelBlock p_177587_1_) {
        if (p_177587_1_ == null) {
            return false;
        }
        ModelBlock modelblock = p_177587_1_.getRootModel();
        return modelblock == MODEL_ENTITY;
    }

    private void bakeItemModels() {
        for (ResourceLocation resourcelocation : this.itemLocations.values()) {
            ModelBlock modelblock = (ModelBlock)this.models.get((Object)resourcelocation);
            if (this.hasItemModel(modelblock)) {
                ModelBlock modelblock1 = this.makeItemModel(modelblock);
                if (modelblock1 != null) {
                    modelblock1.name = resourcelocation.toString();
                }
                this.models.put((Object)resourcelocation, (Object)modelblock1);
                continue;
            }
            if (!this.isCustomRenderer(modelblock)) continue;
            this.models.put((Object)resourcelocation, (Object)modelblock);
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
        ModelBlock modelblock = (ModelBlock)this.models.get((Object)p_getModelBlock_1_);
        return modelblock;
    }

    public static void fixModelLocations(ModelBlock p_fixModelLocations_0_, String p_fixModelLocations_1_) {
        Map map;
        ResourceLocation resourcelocation = ModelBakery.fixModelLocation(p_fixModelLocations_0_.getParentLocation(), p_fixModelLocations_1_);
        if (resourcelocation != p_fixModelLocations_0_.getParentLocation()) {
            Reflector.setFieldValue((Object)p_fixModelLocations_0_, (ReflectorField)Reflector.ModelBlock_parentLocation, (Object)resourcelocation);
        }
        if ((map = (Map)Reflector.getFieldValue((Object)p_fixModelLocations_0_, (ReflectorField)Reflector.ModelBlock_textures)) != null) {
            for (Map.Entry entry : map.entrySet()) {
                String s = (String)entry.getValue();
                String s1 = ModelBakery.fixResourcePath(s, p_fixModelLocations_1_);
                if (s1 == s) continue;
                entry.setValue((Object)s1);
            }
        }
    }

    public static ResourceLocation fixModelLocation(ResourceLocation p_fixModelLocation_0_, String p_fixModelLocation_1_) {
        if (p_fixModelLocation_0_ != null && p_fixModelLocation_1_ != null) {
            if (!p_fixModelLocation_0_.getResourceDomain().equals((Object)"minecraft")) {
                return p_fixModelLocation_0_;
            }
            String s = p_fixModelLocation_0_.getResourcePath();
            String s1 = ModelBakery.fixResourcePath(s, p_fixModelLocation_1_);
            if (s1 != s) {
                p_fixModelLocation_0_ = new ResourceLocation(p_fixModelLocation_0_.getResourceDomain(), s1);
            }
            return p_fixModelLocation_0_;
        }
        return p_fixModelLocation_0_;
    }

    private static String fixResourcePath(String p_fixResourcePath_0_, String p_fixResourcePath_1_) {
        p_fixResourcePath_0_ = TextureUtils.fixResourcePath((String)p_fixResourcePath_0_, (String)p_fixResourcePath_1_);
        p_fixResourcePath_0_ = StrUtils.removeSuffix((String)p_fixResourcePath_0_, (String)".json");
        p_fixResourcePath_0_ = StrUtils.removeSuffix((String)p_fixResourcePath_0_, (String)".png");
        return p_fixResourcePath_0_;
    }

    @Deprecated
    public static void addVariantName(Item p_addVariantName_0_, String ... p_addVariantName_1_) {
        RegistryDelegate registrydelegate = (RegistryDelegate)Reflector.getFieldValue((Object)p_addVariantName_0_, (ReflectorField)Reflector.ForgeItem_delegate);
        if (customVariantNames.containsKey((Object)registrydelegate)) {
            ((Set)customVariantNames.get((Object)registrydelegate)).addAll((Collection)Lists.newArrayList((Object[])p_addVariantName_1_));
        } else {
            customVariantNames.put((Object)registrydelegate, (Object)Sets.newHashSet((Object[])p_addVariantName_1_));
        }
    }

    public static <T extends ResourceLocation> void registerItemVariants(Item p_registerItemVariants_0_, T ... p_registerItemVariants_1_) {
        RegistryDelegate registrydelegate = (RegistryDelegate)Reflector.getFieldValue((Object)p_registerItemVariants_0_, (ReflectorField)Reflector.ForgeItem_delegate);
        if (!customVariantNames.containsKey((Object)registrydelegate)) {
            customVariantNames.put((Object)registrydelegate, (Object)Sets.newHashSet());
        }
        for (T resourcelocation : p_registerItemVariants_1_) {
            ((Set)customVariantNames.get((Object)registrydelegate)).add((Object)resourcelocation.toString());
        }
    }

    static /* synthetic */ Map access$000(ModelBakery x0) {
        return x0.sprites;
    }

    static {
        BUILT_IN_MODELS.put((Object)"missing", (Object)"{ \"textures\": {   \"particle\": \"missingno\",   \"missingno\": \"missingno\"}, \"elements\": [ {     \"from\": [ 0, 0, 0 ],     \"to\": [ 16, 16, 16 ],     \"faces\": {         \"down\":  { \"uv\": [ 0, 0, 16, 16 ], \"cullface\": \"down\", \"texture\": \"#missingno\" },         \"up\":    { \"uv\": [ 0, 0, 16, 16 ], \"cullface\": \"up\", \"texture\": \"#missingno\" },         \"north\": { \"uv\": [ 0, 0, 16, 16 ], \"cullface\": \"north\", \"texture\": \"#missingno\" },         \"south\": { \"uv\": [ 0, 0, 16, 16 ], \"cullface\": \"south\", \"texture\": \"#missingno\" },         \"west\":  { \"uv\": [ 0, 0, 16, 16 ], \"cullface\": \"west\", \"texture\": \"#missingno\" },         \"east\":  { \"uv\": [ 0, 0, 16, 16 ], \"cullface\": \"east\", \"texture\": \"#missingno\" }    }}]}");
        ModelBakery.MODEL_GENERATED.name = "generation marker";
        ModelBakery.MODEL_COMPASS.name = "compass generation marker";
        ModelBakery.MODEL_CLOCK.name = "class generation marker";
        ModelBakery.MODEL_ENTITY.name = "block entity marker";
    }
}
