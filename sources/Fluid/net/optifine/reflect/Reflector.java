// 
// Decompiled by Procyon v0.6.0
// 

package net.optifine.reflect;

import java.lang.reflect.InvocationTargetException;
import net.optifine.util.ArrayUtils;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import net.optifine.Log;
import java.lang.reflect.Method;
import net.minecraft.client.renderer.tileentity.TileEntitySkullRenderer;
import net.minecraft.client.model.ModelSign;
import net.minecraft.client.renderer.tileentity.TileEntitySignRenderer;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.client.renderer.tileentity.TileEntityEnderChestRenderer;
import net.minecraft.client.model.ModelBook;
import net.minecraft.client.renderer.tileentity.TileEntityEnchantmentTableRenderer;
import net.minecraft.tileentity.TileEntityEnchantmentTable;
import net.minecraft.client.model.ModelChest;
import net.minecraft.client.renderer.tileentity.TileEntityChestRenderer;
import net.minecraft.tileentity.TileEntityBrewingStand;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.client.model.ModelBanner;
import net.minecraft.client.renderer.tileentity.TileEntityBannerRenderer;
import net.minecraft.client.model.ModelSkeletonHead;
import net.minecraft.client.renderer.tileentity.RenderWitherSkull;
import net.minecraft.client.renderer.entity.RenderMinecart;
import net.minecraft.client.renderer.entity.RenderBoat;
import net.minecraft.client.model.ModelWolf;
import net.minecraft.client.model.ModelWither;
import net.minecraft.client.model.ModelWitch;
import net.minecraft.client.model.ModelSquid;
import net.minecraft.client.model.ModelSlime;
import net.minecraft.client.model.ModelSilverfish;
import net.minecraft.client.model.ModelRabbit;
import net.minecraft.client.model.ModelOcelot;
import net.minecraft.client.model.ModelMagmaCube;
import net.minecraft.client.model.ModelLeashKnot;
import net.minecraft.client.renderer.entity.RenderLeashKnot;
import net.minecraft.client.model.ModelHorse;
import net.minecraft.client.model.ModelGuardian;
import net.minecraft.client.model.ModelGhast;
import net.minecraft.client.model.ModelEnderMite;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.tileentity.RenderEnderCrystal;
import net.minecraft.client.model.ModelEnderCrystal;
import net.minecraft.client.model.ModelDragon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.renderer.block.model.ModelBlock;
import net.minecraft.client.model.ModelBlaze;
import net.minecraft.client.model.ModelBat;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelHumanoidHead;
import net.minecraft.client.resources.DefaultResourcePack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiHopper;
import net.minecraft.client.gui.inventory.GuiFurnace;
import net.minecraft.world.IWorldNameable;
import net.minecraft.client.gui.GuiEnchantment;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiBrewingStand;
import net.minecraft.inventory.IInventory;
import net.minecraft.client.gui.inventory.GuiBeacon;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.util.LongHashMap;
import net.minecraft.client.multiplayer.ChunkProviderClient;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.renderer.tileentity.RenderItemFrame;
import net.minecraft.entity.item.EntityItemFrame;
import java.util.Map;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.world.WorldProvider;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.potion.PotionEffect;
import net.minecraft.item.Item;
import net.minecraft.client.resources.model.ModelRotation;
import com.google.common.base.Optional;
import javax.vecmath.Matrix4f;
import net.minecraft.world.World;
import net.minecraft.entity.EntityLiving;
import net.minecraft.world.ChunkCache;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.ChunkCoordIntPair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Reflector
{
    private static final Logger LOGGER;
    private static boolean logForge;
    public static ReflectorClass BetterFoliageClient;
    public static ReflectorClass BlamingTransformer;
    public static ReflectorMethod BlamingTransformer_onCrash;
    public static ReflectorClass ChunkWatchEvent_UnWatch;
    public static ReflectorConstructor ChunkWatchEvent_UnWatch_Constructor;
    public static ReflectorClass CoreModManager;
    public static ReflectorMethod CoreModManager_onCrash;
    public static ReflectorClass DimensionManager;
    public static ReflectorMethod DimensionManager_createProviderFor;
    public static ReflectorMethod DimensionManager_getStaticDimensionIDs;
    public static ReflectorClass DrawScreenEvent_Pre;
    public static ReflectorConstructor DrawScreenEvent_Pre_Constructor;
    public static ReflectorClass DrawScreenEvent_Post;
    public static ReflectorConstructor DrawScreenEvent_Post_Constructor;
    public static ReflectorClass EntityViewRenderEvent_CameraSetup;
    public static ReflectorConstructor EntityViewRenderEvent_CameraSetup_Constructor;
    public static ReflectorField EntityViewRenderEvent_CameraSetup_yaw;
    public static ReflectorField EntityViewRenderEvent_CameraSetup_pitch;
    public static ReflectorField EntityViewRenderEvent_CameraSetup_roll;
    public static ReflectorClass EntityViewRenderEvent_FogColors;
    public static ReflectorConstructor EntityViewRenderEvent_FogColors_Constructor;
    public static ReflectorField EntityViewRenderEvent_FogColors_red;
    public static ReflectorField EntityViewRenderEvent_FogColors_green;
    public static ReflectorField EntityViewRenderEvent_FogColors_blue;
    public static ReflectorClass Event;
    public static ReflectorMethod Event_isCanceled;
    public static ReflectorClass EventBus;
    public static ReflectorMethod EventBus_post;
    public static ReflectorClass Event_Result;
    public static ReflectorField Event_Result_DENY;
    public static ReflectorField Event_Result_ALLOW;
    public static ReflectorField Event_Result_DEFAULT;
    public static ReflectorClass ExtendedBlockState;
    public static ReflectorConstructor ExtendedBlockState_Constructor;
    public static ReflectorClass FMLClientHandler;
    public static ReflectorMethod FMLClientHandler_instance;
    public static ReflectorMethod FMLClientHandler_handleLoadingScreen;
    public static ReflectorMethod FMLClientHandler_isLoading;
    public static ReflectorMethod FMLClientHandler_trackBrokenTexture;
    public static ReflectorMethod FMLClientHandler_trackMissingTexture;
    public static ReflectorClass FMLCommonHandler;
    public static ReflectorMethod FMLCommonHandler_callFuture;
    public static ReflectorMethod FMLCommonHandler_enhanceCrashReport;
    public static ReflectorMethod FMLCommonHandler_getBrandings;
    public static ReflectorMethod FMLCommonHandler_handleServerAboutToStart;
    public static ReflectorMethod FMLCommonHandler_handleServerStarting;
    public static ReflectorMethod FMLCommonHandler_instance;
    public static ReflectorClass ForgeBiome;
    public static ReflectorMethod ForgeBiome_getWaterColorMultiplier;
    public static ReflectorClass ForgeBlock;
    public static ReflectorMethod ForgeBlock_addDestroyEffects;
    public static ReflectorMethod ForgeBlock_addHitEffects;
    public static ReflectorMethod ForgeBlock_canCreatureSpawn;
    public static ReflectorMethod ForgeBlock_canRenderInLayer;
    public static ReflectorMethod ForgeBlock_doesSideBlockRendering;
    public static ReflectorMethod ForgeBlock_getBedDirection;
    public static ReflectorMethod ForgeBlock_getExtendedState;
    public static ReflectorMethod ForgeBlock_getLightOpacity;
    public static ReflectorMethod ForgeBlock_getLightValue;
    public static ReflectorMethod ForgeBlock_hasTileEntity;
    public static ReflectorMethod ForgeBlock_isAir;
    public static ReflectorMethod ForgeBlock_isBed;
    public static ReflectorMethod ForgeBlock_isBedFoot;
    public static ReflectorMethod ForgeBlock_isSideSolid;
    public static ReflectorClass ForgeChunkCache;
    public static ReflectorMethod ForgeChunkCache_isSideSolid;
    public static ReflectorClass ForgeEntity;
    public static ReflectorMethod ForgeEntity_canRiderInteract;
    public static ReflectorField ForgeEntity_captureDrops;
    public static ReflectorField ForgeEntity_capturedDrops;
    public static ReflectorMethod ForgeEntity_shouldRenderInPass;
    public static ReflectorMethod ForgeEntity_shouldRiderSit;
    public static ReflectorClass ForgeEventFactory;
    public static ReflectorMethod ForgeEventFactory_canEntityDespawn;
    public static ReflectorMethod ForgeEventFactory_canEntitySpawn;
    public static ReflectorMethod ForgeEventFactory_doSpecialSpawn;
    public static ReflectorMethod ForgeEventFactory_getMaxSpawnPackSize;
    public static ReflectorMethod ForgeEventFactory_renderBlockOverlay;
    public static ReflectorMethod ForgeEventFactory_renderFireOverlay;
    public static ReflectorMethod ForgeEventFactory_renderWaterOverlay;
    public static ReflectorClass ForgeHooks;
    public static ReflectorMethod ForgeHooks_onLivingAttack;
    public static ReflectorMethod ForgeHooks_onLivingDeath;
    public static ReflectorMethod ForgeHooks_onLivingDrops;
    public static ReflectorMethod ForgeHooks_onLivingFall;
    public static ReflectorMethod ForgeHooks_onLivingHurt;
    public static ReflectorMethod ForgeHooks_onLivingJump;
    public static ReflectorMethod ForgeHooks_onLivingSetAttackTarget;
    public static ReflectorMethod ForgeHooks_onLivingUpdate;
    public static ReflectorClass ForgeHooksClient;
    public static ReflectorMethod ForgeHooksClient_applyTransform;
    public static ReflectorMethod ForgeHooksClient_dispatchRenderLast;
    public static ReflectorMethod ForgeHooksClient_drawScreen;
    public static ReflectorMethod ForgeHooksClient_fillNormal;
    public static ReflectorMethod ForgeHooksClient_handleCameraTransforms;
    public static ReflectorMethod ForgeHooksClient_getArmorModel;
    public static ReflectorMethod ForgeHooksClient_getArmorTexture;
    public static ReflectorMethod ForgeHooksClient_getFogDensity;
    public static ReflectorMethod ForgeHooksClient_getFOVModifier;
    public static ReflectorMethod ForgeHooksClient_getMatrix;
    public static ReflectorMethod ForgeHooksClient_getOffsetFOV;
    public static ReflectorMethod ForgeHooksClient_loadEntityShader;
    public static ReflectorMethod ForgeHooksClient_onDrawBlockHighlight;
    public static ReflectorMethod ForgeHooksClient_onFogRender;
    public static ReflectorMethod ForgeHooksClient_onTextureStitchedPre;
    public static ReflectorMethod ForgeHooksClient_onTextureStitchedPost;
    public static ReflectorMethod ForgeHooksClient_orientBedCamera;
    public static ReflectorMethod ForgeHooksClient_putQuadColor;
    public static ReflectorMethod ForgeHooksClient_renderFirstPersonHand;
    public static ReflectorMethod ForgeHooksClient_renderMainMenu;
    public static ReflectorMethod ForgeHooksClient_setRenderLayer;
    public static ReflectorMethod ForgeHooksClient_setRenderPass;
    public static ReflectorMethod ForgeHooksClient_transform;
    public static ReflectorClass ForgeItem;
    public static ReflectorField ForgeItem_delegate;
    public static ReflectorMethod ForgeItem_getDurabilityForDisplay;
    public static ReflectorMethod ForgeItem_getModel;
    public static ReflectorMethod ForgeItem_onEntitySwing;
    public static ReflectorMethod ForgeItem_shouldCauseReequipAnimation;
    public static ReflectorMethod ForgeItem_showDurabilityBar;
    public static ReflectorClass ForgeModContainer;
    public static ReflectorField ForgeModContainer_forgeLightPipelineEnabled;
    public static ReflectorClass ForgePotionEffect;
    public static ReflectorMethod ForgePotionEffect_isCurativeItem;
    public static ReflectorClass ForgeTileEntity;
    public static ReflectorMethod ForgeTileEntity_canRenderBreaking;
    public static ReflectorMethod ForgeTileEntity_getRenderBoundingBox;
    public static ReflectorMethod ForgeTileEntity_hasFastRenderer;
    public static ReflectorMethod ForgeTileEntity_shouldRenderInPass;
    public static ReflectorClass ForgeVertexFormatElementEnumUseage;
    public static ReflectorMethod ForgeVertexFormatElementEnumUseage_preDraw;
    public static ReflectorMethod ForgeVertexFormatElementEnumUseage_postDraw;
    public static ReflectorClass ForgeWorld;
    public static ReflectorMethod ForgeWorld_countEntities;
    public static ReflectorMethod ForgeWorld_getPerWorldStorage;
    public static ReflectorClass ForgeWorldProvider;
    public static ReflectorMethod ForgeWorldProvider_getCloudRenderer;
    public static ReflectorMethod ForgeWorldProvider_getSkyRenderer;
    public static ReflectorMethod ForgeWorldProvider_getWeatherRenderer;
    public static ReflectorMethod ForgeWorldProvider_getSaveFolder;
    public static ReflectorClass GuiModList;
    public static ReflectorConstructor GuiModList_Constructor;
    public static ReflectorClass IColoredBakedQuad;
    public static ReflectorClass IExtendedBlockState;
    public static ReflectorMethod IExtendedBlockState_getClean;
    public static ReflectorClass IModel;
    public static ReflectorMethod IModel_getTextures;
    public static ReflectorClass IRenderHandler;
    public static ReflectorMethod IRenderHandler_render;
    public static ReflectorClass ItemModelMesherForge;
    public static ReflectorConstructor ItemModelMesherForge_Constructor;
    public static ReflectorClass Launch;
    public static ReflectorField Launch_blackboard;
    public static ReflectorClass LightUtil;
    public static ReflectorField LightUtil_itemConsumer;
    public static ReflectorMethod LightUtil_putBakedQuad;
    public static ReflectorMethod LightUtil_renderQuadColor;
    public static ReflectorField LightUtil_tessellator;
    public static ReflectorClass Loader;
    public static ReflectorMethod Loader_getActiveModList;
    public static ReflectorMethod Loader_instance;
    public static ReflectorClass MinecraftForge;
    public static ReflectorField MinecraftForge_EVENT_BUS;
    public static ReflectorClass MinecraftForgeClient;
    public static ReflectorMethod MinecraftForgeClient_getRenderPass;
    public static ReflectorMethod MinecraftForgeClient_onRebuildChunk;
    public static ReflectorClass ModContainer;
    public static ReflectorMethod ModContainer_getModId;
    public static ReflectorClass ModelLoader;
    public static ReflectorField ModelLoader_stateModels;
    public static ReflectorMethod ModelLoader_onRegisterItems;
    public static ReflectorMethod ModelLoader_getInventoryVariant;
    public static ReflectorField ModelLoader_textures;
    public static ReflectorClass ModelLoader_VanillaLoader;
    public static ReflectorField ModelLoader_VanillaLoader_INSTANCE;
    public static ReflectorMethod ModelLoader_VanillaLoader_loadModel;
    public static ReflectorClass RenderBlockOverlayEvent_OverlayType;
    public static ReflectorField RenderBlockOverlayEvent_OverlayType_BLOCK;
    public static ReflectorClass RenderingRegistry;
    public static ReflectorMethod RenderingRegistry_loadEntityRenderers;
    public static ReflectorClass RenderItemInFrameEvent;
    public static ReflectorConstructor RenderItemInFrameEvent_Constructor;
    public static ReflectorClass RenderLivingEvent_Pre;
    public static ReflectorConstructor RenderLivingEvent_Pre_Constructor;
    public static ReflectorClass RenderLivingEvent_Post;
    public static ReflectorConstructor RenderLivingEvent_Post_Constructor;
    public static ReflectorClass RenderLivingEvent_Specials_Pre;
    public static ReflectorConstructor RenderLivingEvent_Specials_Pre_Constructor;
    public static ReflectorClass RenderLivingEvent_Specials_Post;
    public static ReflectorConstructor RenderLivingEvent_Specials_Post_Constructor;
    public static ReflectorClass SplashScreen;
    public static ReflectorClass WorldEvent_Load;
    public static ReflectorConstructor WorldEvent_Load_Constructor;
    private static boolean logVanilla;
    public static ReflectorClass ChunkProviderClient;
    public static ReflectorField ChunkProviderClient_chunkMapping;
    public static ReflectorClass EntityVillager;
    public static ReflectorField EntityVillager_careerId;
    public static ReflectorField EntityVillager_careerLevel;
    public static ReflectorClass GuiBeacon;
    public static ReflectorField GuiBeacon_tileBeacon;
    public static ReflectorClass GuiBrewingStand;
    public static ReflectorField GuiBrewingStand_tileBrewingStand;
    public static ReflectorClass GuiChest;
    public static ReflectorField GuiChest_lowerChestInventory;
    public static ReflectorClass GuiEnchantment;
    public static ReflectorField GuiEnchantment_nameable;
    public static ReflectorClass GuiFurnace;
    public static ReflectorField GuiFurnace_tileFurnace;
    public static ReflectorClass GuiHopper;
    public static ReflectorField GuiHopper_hopperInventory;
    public static ReflectorClass GuiMainMenu;
    public static ReflectorField GuiMainMenu_splashText;
    public static ReflectorClass Minecraft;
    public static ReflectorField Minecraft_defaultResourcePack;
    public static ReflectorClass ModelHumanoidHead;
    public static ReflectorField ModelHumanoidHead_head;
    public static ReflectorClass ModelBat;
    public static ReflectorFields ModelBat_ModelRenderers;
    public static ReflectorClass ModelBlaze;
    public static ReflectorField ModelBlaze_blazeHead;
    public static ReflectorField ModelBlaze_blazeSticks;
    public static ReflectorClass ModelBlock;
    public static ReflectorField ModelBlock_parentLocation;
    public static ReflectorField ModelBlock_textures;
    public static ReflectorClass ModelDragon;
    public static ReflectorFields ModelDragon_ModelRenderers;
    public static ReflectorClass ModelEnderCrystal;
    public static ReflectorFields ModelEnderCrystal_ModelRenderers;
    public static ReflectorClass RenderEnderCrystal;
    public static ReflectorField RenderEnderCrystal_modelEnderCrystal;
    public static ReflectorClass ModelEnderMite;
    public static ReflectorField ModelEnderMite_bodyParts;
    public static ReflectorClass ModelGhast;
    public static ReflectorField ModelGhast_body;
    public static ReflectorField ModelGhast_tentacles;
    public static ReflectorClass ModelGuardian;
    public static ReflectorField ModelGuardian_body;
    public static ReflectorField ModelGuardian_eye;
    public static ReflectorField ModelGuardian_spines;
    public static ReflectorField ModelGuardian_tail;
    public static ReflectorClass ModelHorse;
    public static ReflectorFields ModelHorse_ModelRenderers;
    public static ReflectorClass RenderLeashKnot;
    public static ReflectorField RenderLeashKnot_leashKnotModel;
    public static ReflectorClass ModelMagmaCube;
    public static ReflectorField ModelMagmaCube_core;
    public static ReflectorField ModelMagmaCube_segments;
    public static ReflectorClass ModelOcelot;
    public static ReflectorFields ModelOcelot_ModelRenderers;
    public static ReflectorClass ModelRabbit;
    public static ReflectorFields ModelRabbit_renderers;
    public static ReflectorClass ModelSilverfish;
    public static ReflectorField ModelSilverfish_bodyParts;
    public static ReflectorField ModelSilverfish_wingParts;
    public static ReflectorClass ModelSlime;
    public static ReflectorFields ModelSlime_ModelRenderers;
    public static ReflectorClass ModelSquid;
    public static ReflectorField ModelSquid_body;
    public static ReflectorField ModelSquid_tentacles;
    public static ReflectorClass ModelWitch;
    public static ReflectorField ModelWitch_mole;
    public static ReflectorField ModelWitch_hat;
    public static ReflectorClass ModelWither;
    public static ReflectorField ModelWither_bodyParts;
    public static ReflectorField ModelWither_heads;
    public static ReflectorClass ModelWolf;
    public static ReflectorField ModelWolf_tail;
    public static ReflectorField ModelWolf_mane;
    public static ReflectorClass OptiFineClassTransformer;
    public static ReflectorField OptiFineClassTransformer_instance;
    public static ReflectorMethod OptiFineClassTransformer_getOptiFineResource;
    public static ReflectorClass RenderBoat;
    public static ReflectorField RenderBoat_modelBoat;
    public static ReflectorClass RenderMinecart;
    public static ReflectorField RenderMinecart_modelMinecart;
    public static ReflectorClass RenderWitherSkull;
    public static ReflectorField RenderWitherSkull_model;
    public static ReflectorClass TileEntityBannerRenderer;
    public static ReflectorField TileEntityBannerRenderer_bannerModel;
    public static ReflectorClass TileEntityBeacon;
    public static ReflectorField TileEntityBeacon_customName;
    public static ReflectorClass TileEntityBrewingStand;
    public static ReflectorField TileEntityBrewingStand_customName;
    public static ReflectorClass TileEntityChestRenderer;
    public static ReflectorField TileEntityChestRenderer_simpleChest;
    public static ReflectorField TileEntityChestRenderer_largeChest;
    public static ReflectorClass TileEntityEnchantmentTable;
    public static ReflectorField TileEntityEnchantmentTable_customName;
    public static ReflectorClass TileEntityEnchantmentTableRenderer;
    public static ReflectorField TileEntityEnchantmentTableRenderer_modelBook;
    public static ReflectorClass TileEntityEnderChestRenderer;
    public static ReflectorField TileEntityEnderChestRenderer_modelChest;
    public static ReflectorClass TileEntityFurnace;
    public static ReflectorField TileEntityFurnace_customName;
    public static ReflectorClass TileEntitySignRenderer;
    public static ReflectorField TileEntitySignRenderer_model;
    public static ReflectorClass TileEntitySkullRenderer;
    public static ReflectorField TileEntitySkullRenderer_skeletonHead;
    public static ReflectorField TileEntitySkullRenderer_humanoidHead;
    
    static {
        LOGGER = LogManager.getLogger();
        Reflector.logForge = logEntry("*** Reflector Forge ***");
        Reflector.BetterFoliageClient = new ReflectorClass("mods.betterfoliage.client.BetterFoliageClient");
        Reflector.BlamingTransformer = new ReflectorClass("net.minecraftforge.fml.common.asm.transformers.BlamingTransformer");
        Reflector.BlamingTransformer_onCrash = new ReflectorMethod(Reflector.BlamingTransformer, "onCrash");
        Reflector.ChunkWatchEvent_UnWatch = new ReflectorClass("net.minecraftforge.event.world.ChunkWatchEvent$UnWatch");
        Reflector.ChunkWatchEvent_UnWatch_Constructor = new ReflectorConstructor(Reflector.ChunkWatchEvent_UnWatch, new Class[] { ChunkCoordIntPair.class, EntityPlayerMP.class });
        Reflector.CoreModManager = new ReflectorClass("net.minecraftforge.fml.relauncher.CoreModManager");
        Reflector.CoreModManager_onCrash = new ReflectorMethod(Reflector.CoreModManager, "onCrash");
        Reflector.DimensionManager = new ReflectorClass("net.minecraftforge.common.DimensionManager");
        Reflector.DimensionManager_createProviderFor = new ReflectorMethod(Reflector.DimensionManager, "createProviderFor");
        Reflector.DimensionManager_getStaticDimensionIDs = new ReflectorMethod(Reflector.DimensionManager, "getStaticDimensionIDs");
        Reflector.DrawScreenEvent_Pre = new ReflectorClass("net.minecraftforge.client.event.GuiScreenEvent$DrawScreenEvent$Pre");
        Reflector.DrawScreenEvent_Pre_Constructor = new ReflectorConstructor(Reflector.DrawScreenEvent_Pre, new Class[] { GuiScreen.class, Integer.TYPE, Integer.TYPE, Float.TYPE });
        Reflector.DrawScreenEvent_Post = new ReflectorClass("net.minecraftforge.client.event.GuiScreenEvent$DrawScreenEvent$Post");
        Reflector.DrawScreenEvent_Post_Constructor = new ReflectorConstructor(Reflector.DrawScreenEvent_Post, new Class[] { GuiScreen.class, Integer.TYPE, Integer.TYPE, Float.TYPE });
        Reflector.EntityViewRenderEvent_CameraSetup = new ReflectorClass("net.minecraftforge.client.event.EntityViewRenderEvent$CameraSetup");
        Reflector.EntityViewRenderEvent_CameraSetup_Constructor = new ReflectorConstructor(Reflector.EntityViewRenderEvent_CameraSetup, new Class[] { EntityRenderer.class, Entity.class, Block.class, Double.TYPE, Float.TYPE, Float.TYPE, Float.TYPE });
        Reflector.EntityViewRenderEvent_CameraSetup_yaw = new ReflectorField(Reflector.EntityViewRenderEvent_CameraSetup, "yaw");
        Reflector.EntityViewRenderEvent_CameraSetup_pitch = new ReflectorField(Reflector.EntityViewRenderEvent_CameraSetup, "pitch");
        Reflector.EntityViewRenderEvent_CameraSetup_roll = new ReflectorField(Reflector.EntityViewRenderEvent_CameraSetup, "roll");
        Reflector.EntityViewRenderEvent_FogColors = new ReflectorClass("net.minecraftforge.client.event.EntityViewRenderEvent$FogColors");
        Reflector.EntityViewRenderEvent_FogColors_Constructor = new ReflectorConstructor(Reflector.EntityViewRenderEvent_FogColors, new Class[] { EntityRenderer.class, Entity.class, Block.class, Double.TYPE, Float.TYPE, Float.TYPE, Float.TYPE });
        Reflector.EntityViewRenderEvent_FogColors_red = new ReflectorField(Reflector.EntityViewRenderEvent_FogColors, "red");
        Reflector.EntityViewRenderEvent_FogColors_green = new ReflectorField(Reflector.EntityViewRenderEvent_FogColors, "green");
        Reflector.EntityViewRenderEvent_FogColors_blue = new ReflectorField(Reflector.EntityViewRenderEvent_FogColors, "blue");
        Reflector.Event = new ReflectorClass("net.minecraftforge.fml.common.eventhandler.Event");
        Reflector.Event_isCanceled = new ReflectorMethod(Reflector.Event, "isCanceled");
        Reflector.EventBus = new ReflectorClass("net.minecraftforge.fml.common.eventhandler.EventBus");
        Reflector.EventBus_post = new ReflectorMethod(Reflector.EventBus, "post");
        Reflector.Event_Result = new ReflectorClass("net.minecraftforge.fml.common.eventhandler.Event$Result");
        Reflector.Event_Result_DENY = new ReflectorField(Reflector.Event_Result, "DENY");
        Reflector.Event_Result_ALLOW = new ReflectorField(Reflector.Event_Result, "ALLOW");
        Reflector.Event_Result_DEFAULT = new ReflectorField(Reflector.Event_Result, "DEFAULT");
        Reflector.ExtendedBlockState = new ReflectorClass("net.minecraftforge.common.property.ExtendedBlockState");
        Reflector.ExtendedBlockState_Constructor = new ReflectorConstructor(Reflector.ExtendedBlockState, new Class[] { Block.class, IProperty[].class, IUnlistedProperty[].class });
        Reflector.FMLClientHandler = new ReflectorClass("net.minecraftforge.fml.client.FMLClientHandler");
        Reflector.FMLClientHandler_instance = new ReflectorMethod(Reflector.FMLClientHandler, "instance");
        Reflector.FMLClientHandler_handleLoadingScreen = new ReflectorMethod(Reflector.FMLClientHandler, "handleLoadingScreen");
        Reflector.FMLClientHandler_isLoading = new ReflectorMethod(Reflector.FMLClientHandler, "isLoading");
        Reflector.FMLClientHandler_trackBrokenTexture = new ReflectorMethod(Reflector.FMLClientHandler, "trackBrokenTexture");
        Reflector.FMLClientHandler_trackMissingTexture = new ReflectorMethod(Reflector.FMLClientHandler, "trackMissingTexture");
        Reflector.FMLCommonHandler = new ReflectorClass("net.minecraftforge.fml.common.FMLCommonHandler");
        Reflector.FMLCommonHandler_callFuture = new ReflectorMethod(Reflector.FMLCommonHandler, "callFuture");
        Reflector.FMLCommonHandler_enhanceCrashReport = new ReflectorMethod(Reflector.FMLCommonHandler, "enhanceCrashReport");
        Reflector.FMLCommonHandler_getBrandings = new ReflectorMethod(Reflector.FMLCommonHandler, "getBrandings");
        Reflector.FMLCommonHandler_handleServerAboutToStart = new ReflectorMethod(Reflector.FMLCommonHandler, "handleServerAboutToStart");
        Reflector.FMLCommonHandler_handleServerStarting = new ReflectorMethod(Reflector.FMLCommonHandler, "handleServerStarting");
        Reflector.FMLCommonHandler_instance = new ReflectorMethod(Reflector.FMLCommonHandler, "instance");
        Reflector.ForgeBiome = new ReflectorClass(BiomeGenBase.class);
        Reflector.ForgeBiome_getWaterColorMultiplier = new ReflectorMethod(Reflector.ForgeBiome, "getWaterColorMultiplier");
        Reflector.ForgeBlock = new ReflectorClass(Block.class);
        Reflector.ForgeBlock_addDestroyEffects = new ReflectorMethod(Reflector.ForgeBlock, "addDestroyEffects");
        Reflector.ForgeBlock_addHitEffects = new ReflectorMethod(Reflector.ForgeBlock, "addHitEffects");
        Reflector.ForgeBlock_canCreatureSpawn = new ReflectorMethod(Reflector.ForgeBlock, "canCreatureSpawn");
        Reflector.ForgeBlock_canRenderInLayer = new ReflectorMethod(Reflector.ForgeBlock, "canRenderInLayer", new Class[] { EnumWorldBlockLayer.class });
        Reflector.ForgeBlock_doesSideBlockRendering = new ReflectorMethod(Reflector.ForgeBlock, "doesSideBlockRendering");
        Reflector.ForgeBlock_getBedDirection = new ReflectorMethod(Reflector.ForgeBlock, "getBedDirection");
        Reflector.ForgeBlock_getExtendedState = new ReflectorMethod(Reflector.ForgeBlock, "getExtendedState");
        Reflector.ForgeBlock_getLightOpacity = new ReflectorMethod(Reflector.ForgeBlock, "getLightOpacity", new Class[] { IBlockAccess.class, BlockPos.class });
        Reflector.ForgeBlock_getLightValue = new ReflectorMethod(Reflector.ForgeBlock, "getLightValue", new Class[] { IBlockAccess.class, BlockPos.class });
        Reflector.ForgeBlock_hasTileEntity = new ReflectorMethod(Reflector.ForgeBlock, "hasTileEntity", new Class[] { IBlockState.class });
        Reflector.ForgeBlock_isAir = new ReflectorMethod(Reflector.ForgeBlock, "isAir");
        Reflector.ForgeBlock_isBed = new ReflectorMethod(Reflector.ForgeBlock, "isBed");
        Reflector.ForgeBlock_isBedFoot = new ReflectorMethod(Reflector.ForgeBlock, "isBedFoot");
        Reflector.ForgeBlock_isSideSolid = new ReflectorMethod(Reflector.ForgeBlock, "isSideSolid");
        Reflector.ForgeChunkCache = new ReflectorClass(ChunkCache.class);
        Reflector.ForgeChunkCache_isSideSolid = new ReflectorMethod(Reflector.ForgeChunkCache, "isSideSolid");
        Reflector.ForgeEntity = new ReflectorClass(Entity.class);
        Reflector.ForgeEntity_canRiderInteract = new ReflectorMethod(Reflector.ForgeEntity, "canRiderInteract");
        Reflector.ForgeEntity_captureDrops = new ReflectorField(Reflector.ForgeEntity, "captureDrops");
        Reflector.ForgeEntity_capturedDrops = new ReflectorField(Reflector.ForgeEntity, "capturedDrops");
        Reflector.ForgeEntity_shouldRenderInPass = new ReflectorMethod(Reflector.ForgeEntity, "shouldRenderInPass");
        Reflector.ForgeEntity_shouldRiderSit = new ReflectorMethod(Reflector.ForgeEntity, "shouldRiderSit");
        Reflector.ForgeEventFactory = new ReflectorClass("net.minecraftforge.event.ForgeEventFactory");
        Reflector.ForgeEventFactory_canEntityDespawn = new ReflectorMethod(Reflector.ForgeEventFactory, "canEntityDespawn");
        Reflector.ForgeEventFactory_canEntitySpawn = new ReflectorMethod(Reflector.ForgeEventFactory, "canEntitySpawn");
        Reflector.ForgeEventFactory_doSpecialSpawn = new ReflectorMethod(Reflector.ForgeEventFactory, "doSpecialSpawn", new Class[] { EntityLiving.class, World.class, Float.TYPE, Float.TYPE, Float.TYPE });
        Reflector.ForgeEventFactory_getMaxSpawnPackSize = new ReflectorMethod(Reflector.ForgeEventFactory, "getMaxSpawnPackSize");
        Reflector.ForgeEventFactory_renderBlockOverlay = new ReflectorMethod(Reflector.ForgeEventFactory, "renderBlockOverlay");
        Reflector.ForgeEventFactory_renderFireOverlay = new ReflectorMethod(Reflector.ForgeEventFactory, "renderFireOverlay");
        Reflector.ForgeEventFactory_renderWaterOverlay = new ReflectorMethod(Reflector.ForgeEventFactory, "renderWaterOverlay");
        Reflector.ForgeHooks = new ReflectorClass("net.minecraftforge.common.ForgeHooks");
        Reflector.ForgeHooks_onLivingAttack = new ReflectorMethod(Reflector.ForgeHooks, "onLivingAttack");
        Reflector.ForgeHooks_onLivingDeath = new ReflectorMethod(Reflector.ForgeHooks, "onLivingDeath");
        Reflector.ForgeHooks_onLivingDrops = new ReflectorMethod(Reflector.ForgeHooks, "onLivingDrops");
        Reflector.ForgeHooks_onLivingFall = new ReflectorMethod(Reflector.ForgeHooks, "onLivingFall");
        Reflector.ForgeHooks_onLivingHurt = new ReflectorMethod(Reflector.ForgeHooks, "onLivingHurt");
        Reflector.ForgeHooks_onLivingJump = new ReflectorMethod(Reflector.ForgeHooks, "onLivingJump");
        Reflector.ForgeHooks_onLivingSetAttackTarget = new ReflectorMethod(Reflector.ForgeHooks, "onLivingSetAttackTarget");
        Reflector.ForgeHooks_onLivingUpdate = new ReflectorMethod(Reflector.ForgeHooks, "onLivingUpdate");
        Reflector.ForgeHooksClient = new ReflectorClass("net.minecraftforge.client.ForgeHooksClient");
        Reflector.ForgeHooksClient_applyTransform = new ReflectorMethod(Reflector.ForgeHooksClient, "applyTransform", new Class[] { Matrix4f.class, Optional.class });
        Reflector.ForgeHooksClient_dispatchRenderLast = new ReflectorMethod(Reflector.ForgeHooksClient, "dispatchRenderLast");
        Reflector.ForgeHooksClient_drawScreen = new ReflectorMethod(Reflector.ForgeHooksClient, "drawScreen");
        Reflector.ForgeHooksClient_fillNormal = new ReflectorMethod(Reflector.ForgeHooksClient, "fillNormal");
        Reflector.ForgeHooksClient_handleCameraTransforms = new ReflectorMethod(Reflector.ForgeHooksClient, "handleCameraTransforms");
        Reflector.ForgeHooksClient_getArmorModel = new ReflectorMethod(Reflector.ForgeHooksClient, "getArmorModel");
        Reflector.ForgeHooksClient_getArmorTexture = new ReflectorMethod(Reflector.ForgeHooksClient, "getArmorTexture");
        Reflector.ForgeHooksClient_getFogDensity = new ReflectorMethod(Reflector.ForgeHooksClient, "getFogDensity");
        Reflector.ForgeHooksClient_getFOVModifier = new ReflectorMethod(Reflector.ForgeHooksClient, "getFOVModifier");
        Reflector.ForgeHooksClient_getMatrix = new ReflectorMethod(Reflector.ForgeHooksClient, "getMatrix", new Class[] { ModelRotation.class });
        Reflector.ForgeHooksClient_getOffsetFOV = new ReflectorMethod(Reflector.ForgeHooksClient, "getOffsetFOV");
        Reflector.ForgeHooksClient_loadEntityShader = new ReflectorMethod(Reflector.ForgeHooksClient, "loadEntityShader");
        Reflector.ForgeHooksClient_onDrawBlockHighlight = new ReflectorMethod(Reflector.ForgeHooksClient, "onDrawBlockHighlight");
        Reflector.ForgeHooksClient_onFogRender = new ReflectorMethod(Reflector.ForgeHooksClient, "onFogRender");
        Reflector.ForgeHooksClient_onTextureStitchedPre = new ReflectorMethod(Reflector.ForgeHooksClient, "onTextureStitchedPre");
        Reflector.ForgeHooksClient_onTextureStitchedPost = new ReflectorMethod(Reflector.ForgeHooksClient, "onTextureStitchedPost");
        Reflector.ForgeHooksClient_orientBedCamera = new ReflectorMethod(Reflector.ForgeHooksClient, "orientBedCamera");
        Reflector.ForgeHooksClient_putQuadColor = new ReflectorMethod(Reflector.ForgeHooksClient, "putQuadColor");
        Reflector.ForgeHooksClient_renderFirstPersonHand = new ReflectorMethod(Reflector.ForgeHooksClient, "renderFirstPersonHand");
        Reflector.ForgeHooksClient_renderMainMenu = new ReflectorMethod(Reflector.ForgeHooksClient, "renderMainMenu");
        Reflector.ForgeHooksClient_setRenderLayer = new ReflectorMethod(Reflector.ForgeHooksClient, "setRenderLayer");
        Reflector.ForgeHooksClient_setRenderPass = new ReflectorMethod(Reflector.ForgeHooksClient, "setRenderPass");
        Reflector.ForgeHooksClient_transform = new ReflectorMethod(Reflector.ForgeHooksClient, "transform");
        Reflector.ForgeItem = new ReflectorClass(Item.class);
        Reflector.ForgeItem_delegate = new ReflectorField(Reflector.ForgeItem, "delegate");
        Reflector.ForgeItem_getDurabilityForDisplay = new ReflectorMethod(Reflector.ForgeItem, "getDurabilityForDisplay");
        Reflector.ForgeItem_getModel = new ReflectorMethod(Reflector.ForgeItem, "getModel");
        Reflector.ForgeItem_onEntitySwing = new ReflectorMethod(Reflector.ForgeItem, "onEntitySwing");
        Reflector.ForgeItem_shouldCauseReequipAnimation = new ReflectorMethod(Reflector.ForgeItem, "shouldCauseReequipAnimation");
        Reflector.ForgeItem_showDurabilityBar = new ReflectorMethod(Reflector.ForgeItem, "showDurabilityBar");
        Reflector.ForgeModContainer = new ReflectorClass("net.minecraftforge.common.ForgeModContainer");
        Reflector.ForgeModContainer_forgeLightPipelineEnabled = new ReflectorField(Reflector.ForgeModContainer, "forgeLightPipelineEnabled");
        Reflector.ForgePotionEffect = new ReflectorClass(PotionEffect.class);
        Reflector.ForgePotionEffect_isCurativeItem = new ReflectorMethod(Reflector.ForgePotionEffect, "isCurativeItem");
        Reflector.ForgeTileEntity = new ReflectorClass(TileEntity.class);
        Reflector.ForgeTileEntity_canRenderBreaking = new ReflectorMethod(Reflector.ForgeTileEntity, "canRenderBreaking");
        Reflector.ForgeTileEntity_getRenderBoundingBox = new ReflectorMethod(Reflector.ForgeTileEntity, "getRenderBoundingBox");
        Reflector.ForgeTileEntity_hasFastRenderer = new ReflectorMethod(Reflector.ForgeTileEntity, "hasFastRenderer");
        Reflector.ForgeTileEntity_shouldRenderInPass = new ReflectorMethod(Reflector.ForgeTileEntity, "shouldRenderInPass");
        Reflector.ForgeVertexFormatElementEnumUseage = new ReflectorClass(VertexFormatElement.EnumUsage.class);
        Reflector.ForgeVertexFormatElementEnumUseage_preDraw = new ReflectorMethod(Reflector.ForgeVertexFormatElementEnumUseage, "preDraw");
        Reflector.ForgeVertexFormatElementEnumUseage_postDraw = new ReflectorMethod(Reflector.ForgeVertexFormatElementEnumUseage, "postDraw");
        Reflector.ForgeWorld = new ReflectorClass(World.class);
        Reflector.ForgeWorld_countEntities = new ReflectorMethod(Reflector.ForgeWorld, "countEntities", new Class[] { EnumCreatureType.class, Boolean.TYPE });
        Reflector.ForgeWorld_getPerWorldStorage = new ReflectorMethod(Reflector.ForgeWorld, "getPerWorldStorage");
        Reflector.ForgeWorldProvider = new ReflectorClass(WorldProvider.class);
        Reflector.ForgeWorldProvider_getCloudRenderer = new ReflectorMethod(Reflector.ForgeWorldProvider, "getCloudRenderer");
        Reflector.ForgeWorldProvider_getSkyRenderer = new ReflectorMethod(Reflector.ForgeWorldProvider, "getSkyRenderer");
        Reflector.ForgeWorldProvider_getWeatherRenderer = new ReflectorMethod(Reflector.ForgeWorldProvider, "getWeatherRenderer");
        Reflector.ForgeWorldProvider_getSaveFolder = new ReflectorMethod(Reflector.ForgeWorldProvider, "getSaveFolder");
        Reflector.GuiModList = new ReflectorClass("net.minecraftforge.fml.client.GuiModList");
        Reflector.GuiModList_Constructor = new ReflectorConstructor(Reflector.GuiModList, new Class[] { GuiScreen.class });
        Reflector.IColoredBakedQuad = new ReflectorClass("net.minecraftforge.client.model.IColoredBakedQuad");
        Reflector.IExtendedBlockState = new ReflectorClass("net.minecraftforge.common.property.IExtendedBlockState");
        Reflector.IExtendedBlockState_getClean = new ReflectorMethod(Reflector.IExtendedBlockState, "getClean");
        Reflector.IModel = new ReflectorClass("net.minecraftforge.client.model.IModel");
        Reflector.IModel_getTextures = new ReflectorMethod(Reflector.IModel, "getTextures");
        Reflector.IRenderHandler = new ReflectorClass("net.minecraftforge.client.IRenderHandler");
        Reflector.IRenderHandler_render = new ReflectorMethod(Reflector.IRenderHandler, "render");
        Reflector.ItemModelMesherForge = new ReflectorClass("net.minecraftforge.client.ItemModelMesherForge");
        Reflector.ItemModelMesherForge_Constructor = new ReflectorConstructor(Reflector.ItemModelMesherForge, new Class[] { ModelManager.class });
        Reflector.Launch = new ReflectorClass("net.minecraft.launchwrapper.Launch");
        Reflector.Launch_blackboard = new ReflectorField(Reflector.Launch, "blackboard");
        Reflector.LightUtil = new ReflectorClass("net.minecraftforge.client.model.pipeline.LightUtil");
        Reflector.LightUtil_itemConsumer = new ReflectorField(Reflector.LightUtil, "itemConsumer");
        Reflector.LightUtil_putBakedQuad = new ReflectorMethod(Reflector.LightUtil, "putBakedQuad");
        Reflector.LightUtil_renderQuadColor = new ReflectorMethod(Reflector.LightUtil, "renderQuadColor");
        Reflector.LightUtil_tessellator = new ReflectorField(Reflector.LightUtil, "tessellator");
        Reflector.Loader = new ReflectorClass("net.minecraftforge.fml.common.Loader");
        Reflector.Loader_getActiveModList = new ReflectorMethod(Reflector.Loader, "getActiveModList");
        Reflector.Loader_instance = new ReflectorMethod(Reflector.Loader, "instance");
        Reflector.MinecraftForge = new ReflectorClass("net.minecraftforge.common.MinecraftForge");
        Reflector.MinecraftForge_EVENT_BUS = new ReflectorField(Reflector.MinecraftForge, "EVENT_BUS");
        Reflector.MinecraftForgeClient = new ReflectorClass("net.minecraftforge.client.MinecraftForgeClient");
        Reflector.MinecraftForgeClient_getRenderPass = new ReflectorMethod(Reflector.MinecraftForgeClient, "getRenderPass");
        Reflector.MinecraftForgeClient_onRebuildChunk = new ReflectorMethod(Reflector.MinecraftForgeClient, "onRebuildChunk");
        Reflector.ModContainer = new ReflectorClass("net.minecraftforge.fml.common.ModContainer");
        Reflector.ModContainer_getModId = new ReflectorMethod(Reflector.ModContainer, "getModId");
        Reflector.ModelLoader = new ReflectorClass("net.minecraftforge.client.model.ModelLoader");
        Reflector.ModelLoader_stateModels = new ReflectorField(Reflector.ModelLoader, "stateModels");
        Reflector.ModelLoader_onRegisterItems = new ReflectorMethod(Reflector.ModelLoader, "onRegisterItems");
        Reflector.ModelLoader_getInventoryVariant = new ReflectorMethod(Reflector.ModelLoader, "getInventoryVariant");
        Reflector.ModelLoader_textures = new ReflectorField(Reflector.ModelLoader, "textures");
        Reflector.ModelLoader_VanillaLoader = new ReflectorClass("net.minecraftforge.client.model.ModelLoader$VanillaLoader");
        Reflector.ModelLoader_VanillaLoader_INSTANCE = new ReflectorField(Reflector.ModelLoader_VanillaLoader, "instance");
        Reflector.ModelLoader_VanillaLoader_loadModel = new ReflectorMethod(Reflector.ModelLoader_VanillaLoader, "loadModel");
        Reflector.RenderBlockOverlayEvent_OverlayType = new ReflectorClass("net.minecraftforge.client.event.RenderBlockOverlayEvent$OverlayType");
        Reflector.RenderBlockOverlayEvent_OverlayType_BLOCK = new ReflectorField(Reflector.RenderBlockOverlayEvent_OverlayType, "BLOCK");
        Reflector.RenderingRegistry = new ReflectorClass("net.minecraftforge.fml.client.registry.RenderingRegistry");
        Reflector.RenderingRegistry_loadEntityRenderers = new ReflectorMethod(Reflector.RenderingRegistry, "loadEntityRenderers", new Class[] { RenderManager.class, Map.class });
        Reflector.RenderItemInFrameEvent = new ReflectorClass("net.minecraftforge.client.event.RenderItemInFrameEvent");
        Reflector.RenderItemInFrameEvent_Constructor = new ReflectorConstructor(Reflector.RenderItemInFrameEvent, new Class[] { EntityItemFrame.class, RenderItemFrame.class });
        Reflector.RenderLivingEvent_Pre = new ReflectorClass("net.minecraftforge.client.event.RenderLivingEvent$Pre");
        Reflector.RenderLivingEvent_Pre_Constructor = new ReflectorConstructor(Reflector.RenderLivingEvent_Pre, new Class[] { EntityLivingBase.class, RendererLivingEntity.class, Double.TYPE, Double.TYPE, Double.TYPE });
        Reflector.RenderLivingEvent_Post = new ReflectorClass("net.minecraftforge.client.event.RenderLivingEvent$Post");
        Reflector.RenderLivingEvent_Post_Constructor = new ReflectorConstructor(Reflector.RenderLivingEvent_Post, new Class[] { EntityLivingBase.class, RendererLivingEntity.class, Double.TYPE, Double.TYPE, Double.TYPE });
        Reflector.RenderLivingEvent_Specials_Pre = new ReflectorClass("net.minecraftforge.client.event.RenderLivingEvent$Specials$Pre");
        Reflector.RenderLivingEvent_Specials_Pre_Constructor = new ReflectorConstructor(Reflector.RenderLivingEvent_Specials_Pre, new Class[] { EntityLivingBase.class, RendererLivingEntity.class, Double.TYPE, Double.TYPE, Double.TYPE });
        Reflector.RenderLivingEvent_Specials_Post = new ReflectorClass("net.minecraftforge.client.event.RenderLivingEvent$Specials$Post");
        Reflector.RenderLivingEvent_Specials_Post_Constructor = new ReflectorConstructor(Reflector.RenderLivingEvent_Specials_Post, new Class[] { EntityLivingBase.class, RendererLivingEntity.class, Double.TYPE, Double.TYPE, Double.TYPE });
        Reflector.SplashScreen = new ReflectorClass("net.minecraftforge.fml.client.SplashProgress");
        Reflector.WorldEvent_Load = new ReflectorClass("net.minecraftforge.event.world.WorldEvent$Load");
        Reflector.WorldEvent_Load_Constructor = new ReflectorConstructor(Reflector.WorldEvent_Load, new Class[] { World.class });
        Reflector.logVanilla = logEntry("*** Reflector Vanilla ***");
        Reflector.ChunkProviderClient = new ReflectorClass(ChunkProviderClient.class);
        Reflector.ChunkProviderClient_chunkMapping = new ReflectorField(Reflector.ChunkProviderClient, LongHashMap.class);
        Reflector.EntityVillager = new ReflectorClass(EntityVillager.class);
        Reflector.EntityVillager_careerId = new ReflectorField(new FieldLocatorTypes(EntityVillager.class, new Class[0], Integer.TYPE, new Class[] { Integer.TYPE, Boolean.TYPE, Boolean.TYPE, InventoryBasic.class }, "EntityVillager.careerId"));
        Reflector.EntityVillager_careerLevel = new ReflectorField(new FieldLocatorTypes(EntityVillager.class, new Class[] { Integer.TYPE }, Integer.TYPE, new Class[] { Boolean.TYPE, Boolean.TYPE, InventoryBasic.class }, "EntityVillager.careerLevel"));
        Reflector.GuiBeacon = new ReflectorClass(GuiBeacon.class);
        Reflector.GuiBeacon_tileBeacon = new ReflectorField(Reflector.GuiBeacon, IInventory.class);
        Reflector.GuiBrewingStand = new ReflectorClass(GuiBrewingStand.class);
        Reflector.GuiBrewingStand_tileBrewingStand = new ReflectorField(Reflector.GuiBrewingStand, IInventory.class);
        Reflector.GuiChest = new ReflectorClass(GuiChest.class);
        Reflector.GuiChest_lowerChestInventory = new ReflectorField(Reflector.GuiChest, IInventory.class, 1);
        Reflector.GuiEnchantment = new ReflectorClass(GuiEnchantment.class);
        Reflector.GuiEnchantment_nameable = new ReflectorField(Reflector.GuiEnchantment, IWorldNameable.class);
        Reflector.GuiFurnace = new ReflectorClass(GuiFurnace.class);
        Reflector.GuiFurnace_tileFurnace = new ReflectorField(Reflector.GuiFurnace, IInventory.class);
        Reflector.GuiHopper = new ReflectorClass(GuiHopper.class);
        Reflector.GuiHopper_hopperInventory = new ReflectorField(Reflector.GuiHopper, IInventory.class, 1);
        Reflector.GuiMainMenu = new ReflectorClass(GuiMainMenu.class);
        Reflector.GuiMainMenu_splashText = new ReflectorField(Reflector.GuiMainMenu, String.class);
        Reflector.Minecraft = new ReflectorClass(Minecraft.class);
        Reflector.Minecraft_defaultResourcePack = new ReflectorField(Reflector.Minecraft, DefaultResourcePack.class);
        Reflector.ModelHumanoidHead = new ReflectorClass(ModelHumanoidHead.class);
        Reflector.ModelHumanoidHead_head = new ReflectorField(Reflector.ModelHumanoidHead, ModelRenderer.class);
        Reflector.ModelBat = new ReflectorClass(ModelBat.class);
        Reflector.ModelBat_ModelRenderers = new ReflectorFields(Reflector.ModelBat, ModelRenderer.class, 6);
        Reflector.ModelBlaze = new ReflectorClass(ModelBlaze.class);
        Reflector.ModelBlaze_blazeHead = new ReflectorField(Reflector.ModelBlaze, ModelRenderer.class);
        Reflector.ModelBlaze_blazeSticks = new ReflectorField(Reflector.ModelBlaze, ModelRenderer[].class);
        Reflector.ModelBlock = new ReflectorClass(ModelBlock.class);
        Reflector.ModelBlock_parentLocation = new ReflectorField(Reflector.ModelBlock, ResourceLocation.class);
        Reflector.ModelBlock_textures = new ReflectorField(Reflector.ModelBlock, Map.class);
        Reflector.ModelDragon = new ReflectorClass(ModelDragon.class);
        Reflector.ModelDragon_ModelRenderers = new ReflectorFields(Reflector.ModelDragon, ModelRenderer.class, 12);
        Reflector.ModelEnderCrystal = new ReflectorClass(ModelEnderCrystal.class);
        Reflector.ModelEnderCrystal_ModelRenderers = new ReflectorFields(Reflector.ModelEnderCrystal, ModelRenderer.class, 3);
        Reflector.RenderEnderCrystal = new ReflectorClass(RenderEnderCrystal.class);
        Reflector.RenderEnderCrystal_modelEnderCrystal = new ReflectorField(Reflector.RenderEnderCrystal, ModelBase.class, 0);
        Reflector.ModelEnderMite = new ReflectorClass(ModelEnderMite.class);
        Reflector.ModelEnderMite_bodyParts = new ReflectorField(Reflector.ModelEnderMite, ModelRenderer[].class);
        Reflector.ModelGhast = new ReflectorClass(ModelGhast.class);
        Reflector.ModelGhast_body = new ReflectorField(Reflector.ModelGhast, ModelRenderer.class);
        Reflector.ModelGhast_tentacles = new ReflectorField(Reflector.ModelGhast, ModelRenderer[].class);
        Reflector.ModelGuardian = new ReflectorClass(ModelGuardian.class);
        Reflector.ModelGuardian_body = new ReflectorField(Reflector.ModelGuardian, ModelRenderer.class, 0);
        Reflector.ModelGuardian_eye = new ReflectorField(Reflector.ModelGuardian, ModelRenderer.class, 1);
        Reflector.ModelGuardian_spines = new ReflectorField(Reflector.ModelGuardian, ModelRenderer[].class, 0);
        Reflector.ModelGuardian_tail = new ReflectorField(Reflector.ModelGuardian, ModelRenderer[].class, 1);
        Reflector.ModelHorse = new ReflectorClass(ModelHorse.class);
        Reflector.ModelHorse_ModelRenderers = new ReflectorFields(Reflector.ModelHorse, ModelRenderer.class, 39);
        Reflector.RenderLeashKnot = new ReflectorClass(RenderLeashKnot.class);
        Reflector.RenderLeashKnot_leashKnotModel = new ReflectorField(Reflector.RenderLeashKnot, ModelLeashKnot.class);
        Reflector.ModelMagmaCube = new ReflectorClass(ModelMagmaCube.class);
        Reflector.ModelMagmaCube_core = new ReflectorField(Reflector.ModelMagmaCube, ModelRenderer.class);
        Reflector.ModelMagmaCube_segments = new ReflectorField(Reflector.ModelMagmaCube, ModelRenderer[].class);
        Reflector.ModelOcelot = new ReflectorClass(ModelOcelot.class);
        Reflector.ModelOcelot_ModelRenderers = new ReflectorFields(Reflector.ModelOcelot, ModelRenderer.class, 8);
        Reflector.ModelRabbit = new ReflectorClass(ModelRabbit.class);
        Reflector.ModelRabbit_renderers = new ReflectorFields(Reflector.ModelRabbit, ModelRenderer.class, 12);
        Reflector.ModelSilverfish = new ReflectorClass(ModelSilverfish.class);
        Reflector.ModelSilverfish_bodyParts = new ReflectorField(Reflector.ModelSilverfish, ModelRenderer[].class, 0);
        Reflector.ModelSilverfish_wingParts = new ReflectorField(Reflector.ModelSilverfish, ModelRenderer[].class, 1);
        Reflector.ModelSlime = new ReflectorClass(ModelSlime.class);
        Reflector.ModelSlime_ModelRenderers = new ReflectorFields(Reflector.ModelSlime, ModelRenderer.class, 4);
        Reflector.ModelSquid = new ReflectorClass(ModelSquid.class);
        Reflector.ModelSquid_body = new ReflectorField(Reflector.ModelSquid, ModelRenderer.class);
        Reflector.ModelSquid_tentacles = new ReflectorField(Reflector.ModelSquid, ModelRenderer[].class);
        Reflector.ModelWitch = new ReflectorClass(ModelWitch.class);
        Reflector.ModelWitch_mole = new ReflectorField(Reflector.ModelWitch, ModelRenderer.class, 0);
        Reflector.ModelWitch_hat = new ReflectorField(Reflector.ModelWitch, ModelRenderer.class, 1);
        Reflector.ModelWither = new ReflectorClass(ModelWither.class);
        Reflector.ModelWither_bodyParts = new ReflectorField(Reflector.ModelWither, ModelRenderer[].class, 0);
        Reflector.ModelWither_heads = new ReflectorField(Reflector.ModelWither, ModelRenderer[].class, 1);
        Reflector.ModelWolf = new ReflectorClass(ModelWolf.class);
        Reflector.ModelWolf_tail = new ReflectorField(Reflector.ModelWolf, ModelRenderer.class, 6);
        Reflector.ModelWolf_mane = new ReflectorField(Reflector.ModelWolf, ModelRenderer.class, 7);
        Reflector.OptiFineClassTransformer = new ReflectorClass("optifine.OptiFineClassTransformer");
        Reflector.OptiFineClassTransformer_instance = new ReflectorField(Reflector.OptiFineClassTransformer, "instance");
        Reflector.OptiFineClassTransformer_getOptiFineResource = new ReflectorMethod(Reflector.OptiFineClassTransformer, "getOptiFineResource");
        Reflector.RenderBoat = new ReflectorClass(RenderBoat.class);
        Reflector.RenderBoat_modelBoat = new ReflectorField(Reflector.RenderBoat, ModelBase.class);
        Reflector.RenderMinecart = new ReflectorClass(RenderMinecart.class);
        Reflector.RenderMinecart_modelMinecart = new ReflectorField(Reflector.RenderMinecart, ModelBase.class);
        Reflector.RenderWitherSkull = new ReflectorClass(RenderWitherSkull.class);
        Reflector.RenderWitherSkull_model = new ReflectorField(Reflector.RenderWitherSkull, ModelSkeletonHead.class);
        Reflector.TileEntityBannerRenderer = new ReflectorClass(TileEntityBannerRenderer.class);
        Reflector.TileEntityBannerRenderer_bannerModel = new ReflectorField(Reflector.TileEntityBannerRenderer, ModelBanner.class);
        Reflector.TileEntityBeacon = new ReflectorClass(TileEntityBeacon.class);
        Reflector.TileEntityBeacon_customName = new ReflectorField(Reflector.TileEntityBeacon, String.class);
        Reflector.TileEntityBrewingStand = new ReflectorClass(TileEntityBrewingStand.class);
        Reflector.TileEntityBrewingStand_customName = new ReflectorField(Reflector.TileEntityBrewingStand, String.class);
        Reflector.TileEntityChestRenderer = new ReflectorClass(TileEntityChestRenderer.class);
        Reflector.TileEntityChestRenderer_simpleChest = new ReflectorField(Reflector.TileEntityChestRenderer, ModelChest.class, 0);
        Reflector.TileEntityChestRenderer_largeChest = new ReflectorField(Reflector.TileEntityChestRenderer, ModelChest.class, 1);
        Reflector.TileEntityEnchantmentTable = new ReflectorClass(TileEntityEnchantmentTable.class);
        Reflector.TileEntityEnchantmentTable_customName = new ReflectorField(Reflector.TileEntityEnchantmentTable, String.class);
        Reflector.TileEntityEnchantmentTableRenderer = new ReflectorClass(TileEntityEnchantmentTableRenderer.class);
        Reflector.TileEntityEnchantmentTableRenderer_modelBook = new ReflectorField(Reflector.TileEntityEnchantmentTableRenderer, ModelBook.class);
        Reflector.TileEntityEnderChestRenderer = new ReflectorClass(TileEntityEnderChestRenderer.class);
        Reflector.TileEntityEnderChestRenderer_modelChest = new ReflectorField(Reflector.TileEntityEnderChestRenderer, ModelChest.class);
        Reflector.TileEntityFurnace = new ReflectorClass(TileEntityFurnace.class);
        Reflector.TileEntityFurnace_customName = new ReflectorField(Reflector.TileEntityFurnace, String.class);
        Reflector.TileEntitySignRenderer = new ReflectorClass(TileEntitySignRenderer.class);
        Reflector.TileEntitySignRenderer_model = new ReflectorField(Reflector.TileEntitySignRenderer, ModelSign.class);
        Reflector.TileEntitySkullRenderer = new ReflectorClass(TileEntitySkullRenderer.class);
        Reflector.TileEntitySkullRenderer_skeletonHead = new ReflectorField(Reflector.TileEntitySkullRenderer, ModelSkeletonHead.class, 0);
        Reflector.TileEntitySkullRenderer_humanoidHead = new ReflectorField(Reflector.TileEntitySkullRenderer, ModelSkeletonHead.class, 1);
    }
    
    public static void callVoid(final ReflectorMethod refMethod, final Object... params) {
        try {
            final Method method = refMethod.getTargetMethod();
            if (method == null) {
                return;
            }
            method.invoke(null, params);
        }
        catch (final Throwable throwable) {
            handleException(throwable, null, refMethod, params);
        }
    }
    
    public static boolean callBoolean(final ReflectorMethod refMethod, final Object... params) {
        try {
            final Method method = refMethod.getTargetMethod();
            if (method == null) {
                return false;
            }
            final Boolean obool = (Boolean)method.invoke(null, params);
            return obool;
        }
        catch (final Throwable throwable) {
            handleException(throwable, null, refMethod, params);
            return false;
        }
    }
    
    public static int callInt(final ReflectorMethod refMethod, final Object... params) {
        try {
            final Method method = refMethod.getTargetMethod();
            if (method == null) {
                return 0;
            }
            final Integer integer = (Integer)method.invoke(null, params);
            return integer;
        }
        catch (final Throwable throwable) {
            handleException(throwable, null, refMethod, params);
            return 0;
        }
    }
    
    public static float callFloat(final ReflectorMethod refMethod, final Object... params) {
        try {
            final Method method = refMethod.getTargetMethod();
            if (method == null) {
                return 0.0f;
            }
            final Float f = (Float)method.invoke(null, params);
            return f;
        }
        catch (final Throwable throwable) {
            handleException(throwable, null, refMethod, params);
            return 0.0f;
        }
    }
    
    public static double callDouble(final ReflectorMethod refMethod, final Object... params) {
        try {
            final Method method = refMethod.getTargetMethod();
            if (method == null) {
                return 0.0;
            }
            final Double d0 = (Double)method.invoke(null, params);
            return d0;
        }
        catch (final Throwable throwable) {
            handleException(throwable, null, refMethod, params);
            return 0.0;
        }
    }
    
    public static String callString(final ReflectorMethod refMethod, final Object... params) {
        try {
            final Method method = refMethod.getTargetMethod();
            if (method == null) {
                return null;
            }
            final String s = (String)method.invoke(null, params);
            return s;
        }
        catch (final Throwable throwable) {
            handleException(throwable, null, refMethod, params);
            return null;
        }
    }
    
    public static Object call(final ReflectorMethod refMethod, final Object... params) {
        try {
            final Method method = refMethod.getTargetMethod();
            if (method == null) {
                return null;
            }
            final Object object = method.invoke(null, params);
            return object;
        }
        catch (final Throwable throwable) {
            handleException(throwable, null, refMethod, params);
            return null;
        }
    }
    
    public static void callVoid(final Object obj, final ReflectorMethod refMethod, final Object... params) {
        try {
            if (obj == null) {
                return;
            }
            final Method method = refMethod.getTargetMethod();
            if (method == null) {
                return;
            }
            method.invoke(obj, params);
        }
        catch (final Throwable throwable) {
            handleException(throwable, obj, refMethod, params);
        }
    }
    
    public static boolean callBoolean(final Object obj, final ReflectorMethod refMethod, final Object... params) {
        try {
            final Method method = refMethod.getTargetMethod();
            if (method == null) {
                return false;
            }
            final Boolean obool = (Boolean)method.invoke(obj, params);
            return obool;
        }
        catch (final Throwable throwable) {
            handleException(throwable, obj, refMethod, params);
            return false;
        }
    }
    
    public static int callInt(final Object obj, final ReflectorMethod refMethod, final Object... params) {
        try {
            final Method method = refMethod.getTargetMethod();
            if (method == null) {
                return 0;
            }
            final Integer integer = (Integer)method.invoke(obj, params);
            return integer;
        }
        catch (final Throwable throwable) {
            handleException(throwable, obj, refMethod, params);
            return 0;
        }
    }
    
    public static float callFloat(final Object obj, final ReflectorMethod refMethod, final Object... params) {
        try {
            final Method method = refMethod.getTargetMethod();
            if (method == null) {
                return 0.0f;
            }
            final Float f = (Float)method.invoke(obj, params);
            return f;
        }
        catch (final Throwable throwable) {
            handleException(throwable, obj, refMethod, params);
            return 0.0f;
        }
    }
    
    public static double callDouble(final Object obj, final ReflectorMethod refMethod, final Object... params) {
        try {
            final Method method = refMethod.getTargetMethod();
            if (method == null) {
                return 0.0;
            }
            final Double d0 = (Double)method.invoke(obj, params);
            return d0;
        }
        catch (final Throwable throwable) {
            handleException(throwable, obj, refMethod, params);
            return 0.0;
        }
    }
    
    public static String callString(final Object obj, final ReflectorMethod refMethod, final Object... params) {
        try {
            final Method method = refMethod.getTargetMethod();
            if (method == null) {
                return null;
            }
            final String s = (String)method.invoke(obj, params);
            return s;
        }
        catch (final Throwable throwable) {
            handleException(throwable, obj, refMethod, params);
            return null;
        }
    }
    
    public static Object call(final Object obj, final ReflectorMethod refMethod, final Object... params) {
        try {
            final Method method = refMethod.getTargetMethod();
            if (method == null) {
                return null;
            }
            final Object object = method.invoke(obj, params);
            return object;
        }
        catch (final Throwable throwable) {
            handleException(throwable, obj, refMethod, params);
            return null;
        }
    }
    
    public static Object getFieldValue(final ReflectorField refField) {
        return getFieldValue(null, refField);
    }
    
    public static Object getFieldValue(final Object obj, final ReflectorField refField) {
        try {
            final Field field = refField.getTargetField();
            if (field == null) {
                return null;
            }
            final Object object = field.get(obj);
            return object;
        }
        catch (final Throwable throwable) {
            Log.error("", throwable);
            return null;
        }
    }
    
    public static boolean getFieldValueBoolean(final ReflectorField refField, final boolean def) {
        try {
            final Field field = refField.getTargetField();
            if (field == null) {
                return def;
            }
            final boolean flag = field.getBoolean(null);
            return flag;
        }
        catch (final Throwable throwable) {
            Log.error("", throwable);
            return def;
        }
    }
    
    public static boolean getFieldValueBoolean(final Object obj, final ReflectorField refField, final boolean def) {
        try {
            final Field field = refField.getTargetField();
            if (field == null) {
                return def;
            }
            final boolean flag = field.getBoolean(obj);
            return flag;
        }
        catch (final Throwable throwable) {
            Log.error("", throwable);
            return def;
        }
    }
    
    public static Object getFieldValue(final ReflectorFields refFields, final int index) {
        final ReflectorField reflectorfield = refFields.getReflectorField(index);
        return (reflectorfield == null) ? null : getFieldValue(reflectorfield);
    }
    
    public static Object getFieldValue(final Object obj, final ReflectorFields refFields, final int index) {
        final ReflectorField reflectorfield = refFields.getReflectorField(index);
        return (reflectorfield == null) ? null : getFieldValue(obj, reflectorfield);
    }
    
    public static float getFieldValueFloat(final Object obj, final ReflectorField refField, final float def) {
        try {
            final Field field = refField.getTargetField();
            if (field == null) {
                return def;
            }
            final float f = field.getFloat(obj);
            return f;
        }
        catch (final Throwable throwable) {
            Log.error("", throwable);
            return def;
        }
    }
    
    public static int getFieldValueInt(final Object obj, final ReflectorField refField, final int def) {
        try {
            final Field field = refField.getTargetField();
            if (field == null) {
                return def;
            }
            final int i = field.getInt(obj);
            return i;
        }
        catch (final Throwable throwable) {
            Log.error("", throwable);
            return def;
        }
    }
    
    public static long getFieldValueLong(final Object obj, final ReflectorField refField, final long def) {
        try {
            final Field field = refField.getTargetField();
            if (field == null) {
                return def;
            }
            final long i = field.getLong(obj);
            return i;
        }
        catch (final Throwable throwable) {
            Log.error("", throwable);
            return def;
        }
    }
    
    public static boolean setFieldValue(final ReflectorField refField, final Object value) {
        return setFieldValue(null, refField, value);
    }
    
    public static boolean setFieldValue(final Object obj, final ReflectorField refField, final Object value) {
        try {
            final Field field = refField.getTargetField();
            if (field == null) {
                return false;
            }
            field.set(obj, value);
            return true;
        }
        catch (final Throwable throwable) {
            Log.error("", throwable);
            return false;
        }
    }
    
    public static boolean setFieldValueInt(final ReflectorField refField, final int value) {
        return setFieldValueInt(null, refField, value);
    }
    
    public static boolean setFieldValueInt(final Object obj, final ReflectorField refField, final int value) {
        try {
            final Field field = refField.getTargetField();
            if (field == null) {
                return false;
            }
            field.setInt(obj, value);
            return true;
        }
        catch (final Throwable throwable) {
            Log.error("", throwable);
            return false;
        }
    }
    
    public static boolean postForgeBusEvent(final ReflectorConstructor constr, final Object... params) {
        final Object object = newInstance(constr, params);
        return object != null && postForgeBusEvent(object);
    }
    
    public static boolean postForgeBusEvent(final Object event) {
        if (event == null) {
            return false;
        }
        final Object object = getFieldValue(Reflector.MinecraftForge_EVENT_BUS);
        if (object == null) {
            return false;
        }
        final Object object2 = call(object, Reflector.EventBus_post, event);
        if (!(object2 instanceof Boolean)) {
            return false;
        }
        final Boolean obool = (Boolean)object2;
        return obool;
    }
    
    public static Object newInstance(final ReflectorConstructor constr, final Object... params) {
        final Constructor constructor = constr.getTargetConstructor();
        if (constructor == null) {
            return null;
        }
        try {
            final Object object = constructor.newInstance(params);
            return object;
        }
        catch (final Throwable throwable) {
            handleException(throwable, constr, params);
            return null;
        }
    }
    
    public static boolean matchesTypes(final Class[] pTypes, final Class[] cTypes) {
        if (pTypes.length != cTypes.length) {
            return false;
        }
        for (int i = 0; i < cTypes.length; ++i) {
            final Class oclass = pTypes[i];
            final Class oclass2 = cTypes[i];
            if (oclass != oclass2) {
                return false;
            }
        }
        return true;
    }
    
    private static void dbgCall(final boolean isStatic, final String callType, final ReflectorMethod refMethod, final Object[] params, final Object retVal) {
        final String s = refMethod.getTargetMethod().getDeclaringClass().getName();
        final String s2 = refMethod.getTargetMethod().getName();
        String s3 = "";
        if (isStatic) {
            s3 = " static";
        }
        Log.dbg(String.valueOf(callType) + s3 + " " + s + "." + s2 + "(" + ArrayUtils.arrayToString(params) + ") => " + retVal);
    }
    
    private static void dbgCallVoid(final boolean isStatic, final String callType, final ReflectorMethod refMethod, final Object[] params) {
        final String s = refMethod.getTargetMethod().getDeclaringClass().getName();
        final String s2 = refMethod.getTargetMethod().getName();
        String s3 = "";
        if (isStatic) {
            s3 = " static";
        }
        Log.dbg(String.valueOf(callType) + s3 + " " + s + "." + s2 + "(" + ArrayUtils.arrayToString(params) + ")");
    }
    
    private static void dbgFieldValue(final boolean isStatic, final String accessType, final ReflectorField refField, final Object val) {
        final String s = refField.getTargetField().getDeclaringClass().getName();
        final String s2 = refField.getTargetField().getName();
        String s3 = "";
        if (isStatic) {
            s3 = " static";
        }
        Log.dbg(String.valueOf(accessType) + s3 + " " + s + "." + s2 + " => " + val);
    }
    
    private static void handleException(final Throwable e, final Object obj, final ReflectorMethod refMethod, final Object[] params) {
        if (e instanceof InvocationTargetException) {
            final Throwable throwable = e.getCause();
            if (throwable instanceof RuntimeException) {
                final RuntimeException runtimeexception = (RuntimeException)throwable;
                throw runtimeexception;
            }
            Log.error("", e);
        }
        else {
            Log.warn("*** Exception outside of method ***");
            Log.warn("Method deactivated: " + refMethod.getTargetMethod());
            refMethod.deactivate();
            if (e instanceof IllegalArgumentException) {
                Log.warn("*** IllegalArgumentException ***");
                Log.warn("Method: " + refMethod.getTargetMethod());
                Log.warn("Object: " + obj);
                Log.warn("Parameter classes: " + ArrayUtils.arrayToString(getClasses(params)));
                Log.warn("Parameters: " + ArrayUtils.arrayToString(params));
            }
            Log.warn("", e);
        }
    }
    
    private static void handleException(final Throwable e, final ReflectorConstructor refConstr, final Object[] params) {
        if (e instanceof InvocationTargetException) {
            Log.error("", e);
        }
        else {
            Log.warn("*** Exception outside of constructor ***");
            Log.warn("Constructor deactivated: " + refConstr.getTargetConstructor());
            refConstr.deactivate();
            if (e instanceof IllegalArgumentException) {
                Log.warn("*** IllegalArgumentException ***");
                Log.warn("Constructor: " + refConstr.getTargetConstructor());
                Log.warn("Parameter classes: " + ArrayUtils.arrayToString(getClasses(params)));
                Log.warn("Parameters: " + ArrayUtils.arrayToString(params));
            }
            Log.warn("", e);
        }
    }
    
    private static Object[] getClasses(final Object[] objs) {
        if (objs == null) {
            return new Class[0];
        }
        final Class[] aclass = new Class[objs.length];
        for (int i = 0; i < aclass.length; ++i) {
            final Object object = objs[i];
            if (object != null) {
                aclass[i] = object.getClass();
            }
        }
        return aclass;
    }
    
    private static ReflectorField[] getReflectorFields(final ReflectorClass parentClass, final Class fieldType, final int count) {
        final ReflectorField[] areflectorfield = new ReflectorField[count];
        for (int i = 0; i < areflectorfield.length; ++i) {
            areflectorfield[i] = new ReflectorField(parentClass, fieldType, i);
        }
        return areflectorfield;
    }
    
    private static boolean logEntry(final String str) {
        Reflector.LOGGER.info("[OptiFine] " + str);
        return true;
    }
    
    private static boolean registerResolvable(final String str) {
        final IResolvable iresolvable = new IResolvable() {
            @Override
            public void resolve() {
                Reflector.LOGGER.info("[OptiFine] " + str);
            }
        };
        ReflectorResolver.register(iresolvable);
        return true;
    }
}
