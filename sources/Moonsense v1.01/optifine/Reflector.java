// 
// Decompiled by Procyon v0.5.36
// 

package optifine;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import java.util.Map;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.item.ItemRecord;
import net.minecraft.potion.PotionEffect;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.tileentity.RenderItemFrame;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.client.resources.model.ModelRotation;

public class Reflector
{
    public static ReflectorClass MinecraftForge;
    public static ReflectorField MinecraftForge_EVENT_BUS;
    public static ReflectorClass ForgeHooks;
    public static ReflectorMethod ForgeHooks_onLivingSetAttackTarget;
    public static ReflectorMethod ForgeHooks_onLivingUpdate;
    public static ReflectorMethod ForgeHooks_onLivingAttack;
    public static ReflectorMethod ForgeHooks_onLivingHurt;
    public static ReflectorMethod ForgeHooks_onLivingDeath;
    public static ReflectorMethod ForgeHooks_onLivingDrops;
    public static ReflectorMethod ForgeHooks_onLivingFall;
    public static ReflectorMethod ForgeHooks_onLivingJump;
    public static ReflectorClass MinecraftForgeClient;
    public static ReflectorMethod MinecraftForgeClient_getRenderPass;
    public static ReflectorMethod MinecraftForgeClient_onRebuildChunk;
    public static ReflectorClass ForgeHooksClient;
    public static ReflectorMethod ForgeHooksClient_onDrawBlockHighlight;
    public static ReflectorMethod ForgeHooksClient_orientBedCamera;
    public static ReflectorMethod ForgeHooksClient_dispatchRenderLast;
    public static ReflectorMethod ForgeHooksClient_setRenderPass;
    public static ReflectorMethod ForgeHooksClient_onTextureStitchedPre;
    public static ReflectorMethod ForgeHooksClient_onTextureStitchedPost;
    public static ReflectorMethod ForgeHooksClient_renderFirstPersonHand;
    public static ReflectorMethod ForgeHooksClient_getOffsetFOV;
    public static ReflectorMethod ForgeHooksClient_drawScreen;
    public static ReflectorMethod ForgeHooksClient_onFogRender;
    public static ReflectorMethod ForgeHooksClient_setRenderLayer;
    public static ReflectorMethod ForgeHooksClient_transform;
    public static ReflectorMethod ForgeHooksClient_getMatrix;
    public static ReflectorMethod ForgeHooksClient_fillNormal;
    public static ReflectorMethod ForgeHooksClient_handleCameraTransforms;
    public static ReflectorMethod ForgeHooksClient_getArmorModel;
    public static ReflectorMethod ForgeHooksClient_getArmorTexture;
    public static ReflectorMethod ForgeHooksClient_putQuadColor;
    public static ReflectorMethod ForgeHooksClient_loadEntityShader;
    public static ReflectorMethod ForgeHooksClient_getFogDensity;
    public static ReflectorClass FMLCommonHandler;
    public static ReflectorMethod FMLCommonHandler_instance;
    public static ReflectorMethod FMLCommonHandler_handleServerStarting;
    public static ReflectorMethod FMLCommonHandler_handleServerAboutToStart;
    public static ReflectorMethod FMLCommonHandler_enhanceCrashReport;
    public static ReflectorMethod FMLCommonHandler_getBrandings;
    public static ReflectorMethod FMLCommonHandler_callFuture;
    public static ReflectorClass FMLClientHandler;
    public static ReflectorMethod FMLClientHandler_instance;
    public static ReflectorMethod FMLClientHandler_isLoading;
    public static ReflectorMethod FMLClientHandler_trackBrokenTexture;
    public static ReflectorMethod FMLClientHandler_trackMissingTexture;
    public static ReflectorClass ForgeWorldProvider;
    public static ReflectorMethod ForgeWorldProvider_getSkyRenderer;
    public static ReflectorMethod ForgeWorldProvider_getCloudRenderer;
    public static ReflectorMethod ForgeWorldProvider_getWeatherRenderer;
    public static ReflectorClass ForgeWorld;
    public static ReflectorMethod ForgeWorld_countEntities;
    public static ReflectorMethod ForgeWorld_getPerWorldStorage;
    public static ReflectorClass IRenderHandler;
    public static ReflectorMethod IRenderHandler_render;
    public static ReflectorClass DimensionManager;
    public static ReflectorMethod DimensionManager_getStaticDimensionIDs;
    public static ReflectorClass WorldEvent_Load;
    public static ReflectorConstructor WorldEvent_Load_Constructor;
    public static ReflectorClass RenderItemInFrameEvent;
    public static ReflectorConstructor RenderItemInFrameEvent_Constructor;
    public static ReflectorClass DrawScreenEvent_Pre;
    public static ReflectorConstructor DrawScreenEvent_Pre_Constructor;
    public static ReflectorClass DrawScreenEvent_Post;
    public static ReflectorConstructor DrawScreenEvent_Post_Constructor;
    public static ReflectorClass EntityViewRenderEvent_FogColors;
    public static ReflectorConstructor EntityViewRenderEvent_FogColors_Constructor;
    public static ReflectorField EntityViewRenderEvent_FogColors_red;
    public static ReflectorField EntityViewRenderEvent_FogColors_green;
    public static ReflectorField EntityViewRenderEvent_FogColors_blue;
    public static ReflectorClass EntityViewRenderEvent_CameraSetup;
    public static ReflectorConstructor EntityViewRenderEvent_CameraSetup_Constructor;
    public static ReflectorField EntityViewRenderEvent_CameraSetup_yaw;
    public static ReflectorField EntityViewRenderEvent_CameraSetup_pitch;
    public static ReflectorField EntityViewRenderEvent_CameraSetup_roll;
    public static ReflectorClass RenderLivingEvent_Pre;
    public static ReflectorConstructor RenderLivingEvent_Pre_Constructor;
    public static ReflectorClass RenderLivingEvent_Post;
    public static ReflectorConstructor RenderLivingEvent_Post_Constructor;
    public static ReflectorClass RenderLivingEvent_Specials_Pre;
    public static ReflectorConstructor RenderLivingEvent_Specials_Pre_Constructor;
    public static ReflectorClass RenderLivingEvent_Specials_Post;
    public static ReflectorConstructor RenderLivingEvent_Specials_Post_Constructor;
    public static ReflectorClass EventBus;
    public static ReflectorMethod EventBus_post;
    public static ReflectorClass Event_Result;
    public static ReflectorField Event_Result_DENY;
    public static ReflectorField Event_Result_ALLOW;
    public static ReflectorField Event_Result_DEFAULT;
    public static ReflectorClass ForgeEventFactory;
    public static ReflectorMethod ForgeEventFactory_canEntitySpawn;
    public static ReflectorMethod ForgeEventFactory_canEntityDespawn;
    public static ReflectorMethod ForgeEventFactory_renderBlockOverlay;
    public static ReflectorMethod ForgeEventFactory_renderWaterOverlay;
    public static ReflectorMethod ForgeEventFactory_renderFireOverlay;
    public static ReflectorClass RenderBlockOverlayEvent_OverlayType;
    public static ReflectorField RenderBlockOverlayEvent_OverlayType_BLOCK;
    public static ReflectorClass ChunkWatchEvent_UnWatch;
    public static ReflectorConstructor ChunkWatchEvent_UnWatch_Constructor;
    public static ReflectorClass ForgeBlock;
    public static ReflectorMethod ForgeBlock_getBedDirection;
    public static ReflectorMethod ForgeBlock_isBed;
    public static ReflectorMethod ForgeBlock_isBedFoot;
    public static ReflectorMethod ForgeBlock_hasTileEntity;
    public static ReflectorMethod ForgeBlock_canCreatureSpawn;
    public static ReflectorMethod ForgeBlock_addHitEffects;
    public static ReflectorMethod ForgeBlock_addDestroyEffects;
    public static ReflectorMethod ForgeBlock_isAir;
    public static ReflectorMethod ForgeBlock_canRenderInLayer;
    public static ReflectorMethod ForgeBlock_getExtendedState;
    public static ReflectorClass ForgeEntity;
    public static ReflectorField ForgeEntity_captureDrops;
    public static ReflectorField ForgeEntity_capturedDrops;
    public static ReflectorMethod ForgeEntity_shouldRenderInPass;
    public static ReflectorMethod ForgeEntity_canRiderInteract;
    public static ReflectorMethod ForgeEntity_shouldRiderSit;
    public static ReflectorClass ForgeTileEntity;
    public static ReflectorMethod ForgeTileEntity_shouldRenderInPass;
    public static ReflectorMethod ForgeTileEntity_getRenderBoundingBox;
    public static ReflectorMethod ForgeTileEntity_canRenderBreaking;
    public static ReflectorClass ForgeItem;
    public static ReflectorMethod ForgeItem_onEntitySwing;
    public static ReflectorMethod ForgeItem_shouldCauseReequipAnimation;
    public static ReflectorMethod ForgeItem_getModel;
    public static ReflectorMethod ForgeItem_showDurabilityBar;
    public static ReflectorMethod ForgeItem_getDurabilityForDisplay;
    public static ReflectorClass ForgePotionEffect;
    public static ReflectorMethod ForgePotionEffect_isCurativeItem;
    public static ReflectorClass ForgeItemRecord;
    public static ReflectorMethod ForgeItemRecord_getRecordResource;
    public static ReflectorClass ForgeVertexFormatElementEnumUseage;
    public static ReflectorMethod ForgeVertexFormatElementEnumUseage_preDraw;
    public static ReflectorMethod ForgeVertexFormatElementEnumUseage_postDraw;
    public static ReflectorClass BlamingTransformer;
    public static ReflectorMethod BlamingTransformer_onCrash;
    public static ReflectorClass CoreModManager;
    public static ReflectorMethod CoreModManager_onCrash;
    public static ReflectorClass ISmartBlockModel;
    public static ReflectorMethod ISmartBlockModel_handleBlockState;
    public static ReflectorClass Launch;
    public static ReflectorField Launch_blackboard;
    public static ReflectorClass SplashScreen;
    public static ReflectorClass LightUtil;
    public static ReflectorField LightUtil_tessellator;
    public static ReflectorField LightUtil_itemConsumer;
    public static ReflectorMethod LightUtil_putBakedQuad;
    public static ReflectorMethod LightUtil_renderQuadColor;
    public static ReflectorClass IExtendedBlockState;
    public static ReflectorMethod IExtendedBlockState_getClean;
    public static ReflectorClass ItemModelMesherForge;
    public static ReflectorConstructor ItemModelMesherForge_Constructor;
    public static ReflectorClass ModelLoader;
    public static ReflectorMethod ModelLoader_onRegisterItems;
    public static ReflectorClass Attributes;
    public static ReflectorField Attributes_DEFAULT_BAKED_FORMAT;
    public static ReflectorClass BetterFoliageClient;
    public static ReflectorClass IColoredBakedQuad;
    public static ReflectorClass ForgeBiomeGenBase;
    public static ReflectorMethod ForgeBiomeGenBase_getWaterColorMultiplier;
    public static ReflectorClass RenderingRegistry;
    public static ReflectorMethod RenderingRegistry_loadEntityRenderers;
    public static ReflectorClass ForgeTileEntityRendererDispatcher;
    public static ReflectorMethod ForgeTileEntityRendererDispatcher_preDrawBatch;
    public static ReflectorMethod ForgeTileEntityRendererDispatcher_drawBatch;
    public static ReflectorClass OptiFineClassTransformer;
    public static ReflectorField OptiFineClassTransformer_instance;
    public static ReflectorMethod OptiFineClassTransformer_getOptiFineResource;
    public static ReflectorClass ForgeModContainer;
    public static ReflectorField ForgeModContainer_forgeLightPipelineEnabled;
    
    static {
        Reflector.MinecraftForge = new ReflectorClass("net.minecraftforge.common.MinecraftForge");
        Reflector.MinecraftForge_EVENT_BUS = new ReflectorField(Reflector.MinecraftForge, "EVENT_BUS");
        Reflector.ForgeHooks = new ReflectorClass("net.minecraftforge.common.ForgeHooks");
        Reflector.ForgeHooks_onLivingSetAttackTarget = new ReflectorMethod(Reflector.ForgeHooks, "onLivingSetAttackTarget");
        Reflector.ForgeHooks_onLivingUpdate = new ReflectorMethod(Reflector.ForgeHooks, "onLivingUpdate");
        Reflector.ForgeHooks_onLivingAttack = new ReflectorMethod(Reflector.ForgeHooks, "onLivingAttack");
        Reflector.ForgeHooks_onLivingHurt = new ReflectorMethod(Reflector.ForgeHooks, "onLivingHurt");
        Reflector.ForgeHooks_onLivingDeath = new ReflectorMethod(Reflector.ForgeHooks, "onLivingDeath");
        Reflector.ForgeHooks_onLivingDrops = new ReflectorMethod(Reflector.ForgeHooks, "onLivingDrops");
        Reflector.ForgeHooks_onLivingFall = new ReflectorMethod(Reflector.ForgeHooks, "onLivingFall");
        Reflector.ForgeHooks_onLivingJump = new ReflectorMethod(Reflector.ForgeHooks, "onLivingJump");
        Reflector.MinecraftForgeClient = new ReflectorClass("net.minecraftforge.client.MinecraftForgeClient");
        Reflector.MinecraftForgeClient_getRenderPass = new ReflectorMethod(Reflector.MinecraftForgeClient, "getRenderPass");
        Reflector.MinecraftForgeClient_onRebuildChunk = new ReflectorMethod(Reflector.MinecraftForgeClient, "onRebuildChunk");
        Reflector.ForgeHooksClient = new ReflectorClass("net.minecraftforge.client.ForgeHooksClient");
        Reflector.ForgeHooksClient_onDrawBlockHighlight = new ReflectorMethod(Reflector.ForgeHooksClient, "onDrawBlockHighlight");
        Reflector.ForgeHooksClient_orientBedCamera = new ReflectorMethod(Reflector.ForgeHooksClient, "orientBedCamera");
        Reflector.ForgeHooksClient_dispatchRenderLast = new ReflectorMethod(Reflector.ForgeHooksClient, "dispatchRenderLast");
        Reflector.ForgeHooksClient_setRenderPass = new ReflectorMethod(Reflector.ForgeHooksClient, "setRenderPass");
        Reflector.ForgeHooksClient_onTextureStitchedPre = new ReflectorMethod(Reflector.ForgeHooksClient, "onTextureStitchedPre");
        Reflector.ForgeHooksClient_onTextureStitchedPost = new ReflectorMethod(Reflector.ForgeHooksClient, "onTextureStitchedPost");
        Reflector.ForgeHooksClient_renderFirstPersonHand = new ReflectorMethod(Reflector.ForgeHooksClient, "renderFirstPersonHand");
        Reflector.ForgeHooksClient_getOffsetFOV = new ReflectorMethod(Reflector.ForgeHooksClient, "getOffsetFOV");
        Reflector.ForgeHooksClient_drawScreen = new ReflectorMethod(Reflector.ForgeHooksClient, "drawScreen");
        Reflector.ForgeHooksClient_onFogRender = new ReflectorMethod(Reflector.ForgeHooksClient, "onFogRender");
        Reflector.ForgeHooksClient_setRenderLayer = new ReflectorMethod(Reflector.ForgeHooksClient, "setRenderLayer");
        Reflector.ForgeHooksClient_transform = new ReflectorMethod(Reflector.ForgeHooksClient, "transform");
        Reflector.ForgeHooksClient_getMatrix = new ReflectorMethod(Reflector.ForgeHooksClient, "getMatrix", new Class[] { ModelRotation.class });
        Reflector.ForgeHooksClient_fillNormal = new ReflectorMethod(Reflector.ForgeHooksClient, "fillNormal");
        Reflector.ForgeHooksClient_handleCameraTransforms = new ReflectorMethod(Reflector.ForgeHooksClient, "handleCameraTransforms");
        Reflector.ForgeHooksClient_getArmorModel = new ReflectorMethod(Reflector.ForgeHooksClient, "getArmorModel");
        Reflector.ForgeHooksClient_getArmorTexture = new ReflectorMethod(Reflector.ForgeHooksClient, "getArmorTexture");
        Reflector.ForgeHooksClient_putQuadColor = new ReflectorMethod(Reflector.ForgeHooksClient, "putQuadColor");
        Reflector.ForgeHooksClient_loadEntityShader = new ReflectorMethod(Reflector.ForgeHooksClient, "loadEntityShader");
        Reflector.ForgeHooksClient_getFogDensity = new ReflectorMethod(Reflector.ForgeHooksClient, "getFogDensity");
        Reflector.FMLCommonHandler = new ReflectorClass("net.minecraftforge.fml.common.FMLCommonHandler");
        Reflector.FMLCommonHandler_instance = new ReflectorMethod(Reflector.FMLCommonHandler, "instance");
        Reflector.FMLCommonHandler_handleServerStarting = new ReflectorMethod(Reflector.FMLCommonHandler, "handleServerStarting");
        Reflector.FMLCommonHandler_handleServerAboutToStart = new ReflectorMethod(Reflector.FMLCommonHandler, "handleServerAboutToStart");
        Reflector.FMLCommonHandler_enhanceCrashReport = new ReflectorMethod(Reflector.FMLCommonHandler, "enhanceCrashReport");
        Reflector.FMLCommonHandler_getBrandings = new ReflectorMethod(Reflector.FMLCommonHandler, "getBrandings");
        Reflector.FMLCommonHandler_callFuture = new ReflectorMethod(Reflector.FMLCommonHandler, "callFuture");
        Reflector.FMLClientHandler = new ReflectorClass("net.minecraftforge.fml.client.FMLClientHandler");
        Reflector.FMLClientHandler_instance = new ReflectorMethod(Reflector.FMLClientHandler, "instance");
        Reflector.FMLClientHandler_isLoading = new ReflectorMethod(Reflector.FMLClientHandler, "isLoading");
        Reflector.FMLClientHandler_trackBrokenTexture = new ReflectorMethod(Reflector.FMLClientHandler, "trackBrokenTexture");
        Reflector.FMLClientHandler_trackMissingTexture = new ReflectorMethod(Reflector.FMLClientHandler, "trackMissingTexture");
        Reflector.ForgeWorldProvider = new ReflectorClass(WorldProvider.class);
        Reflector.ForgeWorldProvider_getSkyRenderer = new ReflectorMethod(Reflector.ForgeWorldProvider, "getSkyRenderer");
        Reflector.ForgeWorldProvider_getCloudRenderer = new ReflectorMethod(Reflector.ForgeWorldProvider, "getCloudRenderer");
        Reflector.ForgeWorldProvider_getWeatherRenderer = new ReflectorMethod(Reflector.ForgeWorldProvider, "getWeatherRenderer");
        Reflector.ForgeWorld = new ReflectorClass(World.class);
        Reflector.ForgeWorld_countEntities = new ReflectorMethod(Reflector.ForgeWorld, "countEntities", new Class[] { EnumCreatureType.class, Boolean.TYPE });
        Reflector.ForgeWorld_getPerWorldStorage = new ReflectorMethod(Reflector.ForgeWorld, "getPerWorldStorage");
        Reflector.IRenderHandler = new ReflectorClass("net.minecraftforge.client.IRenderHandler");
        Reflector.IRenderHandler_render = new ReflectorMethod(Reflector.IRenderHandler, "render");
        Reflector.DimensionManager = new ReflectorClass("net.minecraftforge.common.DimensionManager");
        Reflector.DimensionManager_getStaticDimensionIDs = new ReflectorMethod(Reflector.DimensionManager, "getStaticDimensionIDs");
        Reflector.WorldEvent_Load = new ReflectorClass("net.minecraftforge.event.world.WorldEvent$Load");
        Reflector.WorldEvent_Load_Constructor = new ReflectorConstructor(Reflector.WorldEvent_Load, new Class[] { World.class });
        Reflector.RenderItemInFrameEvent = new ReflectorClass("net.minecraftforge.client.event.RenderItemInFrameEvent");
        Reflector.RenderItemInFrameEvent_Constructor = new ReflectorConstructor(Reflector.RenderItemInFrameEvent, new Class[] { EntityItemFrame.class, RenderItemFrame.class });
        Reflector.DrawScreenEvent_Pre = new ReflectorClass("net.minecraftforge.client.event.GuiScreenEvent$DrawScreenEvent$Pre");
        Reflector.DrawScreenEvent_Pre_Constructor = new ReflectorConstructor(Reflector.DrawScreenEvent_Pre, new Class[] { GuiScreen.class, Integer.TYPE, Integer.TYPE, Float.TYPE });
        Reflector.DrawScreenEvent_Post = new ReflectorClass("net.minecraftforge.client.event.GuiScreenEvent$DrawScreenEvent$Post");
        Reflector.DrawScreenEvent_Post_Constructor = new ReflectorConstructor(Reflector.DrawScreenEvent_Post, new Class[] { GuiScreen.class, Integer.TYPE, Integer.TYPE, Float.TYPE });
        Reflector.EntityViewRenderEvent_FogColors = new ReflectorClass("net.minecraftforge.client.event.EntityViewRenderEvent$FogColors");
        Reflector.EntityViewRenderEvent_FogColors_Constructor = new ReflectorConstructor(Reflector.EntityViewRenderEvent_FogColors, new Class[] { EntityRenderer.class, Entity.class, Block.class, Double.TYPE, Float.TYPE, Float.TYPE, Float.TYPE });
        Reflector.EntityViewRenderEvent_FogColors_red = new ReflectorField(Reflector.EntityViewRenderEvent_FogColors, "red");
        Reflector.EntityViewRenderEvent_FogColors_green = new ReflectorField(Reflector.EntityViewRenderEvent_FogColors, "green");
        Reflector.EntityViewRenderEvent_FogColors_blue = new ReflectorField(Reflector.EntityViewRenderEvent_FogColors, "blue");
        Reflector.EntityViewRenderEvent_CameraSetup = new ReflectorClass("net.minecraftforge.client.event.EntityViewRenderEvent$CameraSetup");
        Reflector.EntityViewRenderEvent_CameraSetup_Constructor = new ReflectorConstructor(Reflector.EntityViewRenderEvent_CameraSetup, new Class[] { EntityRenderer.class, Entity.class, Block.class, Double.TYPE, Float.TYPE, Float.TYPE, Float.TYPE });
        Reflector.EntityViewRenderEvent_CameraSetup_yaw = new ReflectorField(Reflector.EntityViewRenderEvent_CameraSetup, "yaw");
        Reflector.EntityViewRenderEvent_CameraSetup_pitch = new ReflectorField(Reflector.EntityViewRenderEvent_CameraSetup, "pitch");
        Reflector.EntityViewRenderEvent_CameraSetup_roll = new ReflectorField(Reflector.EntityViewRenderEvent_CameraSetup, "roll");
        Reflector.RenderLivingEvent_Pre = new ReflectorClass("net.minecraftforge.client.event.RenderLivingEvent$Pre");
        Reflector.RenderLivingEvent_Pre_Constructor = new ReflectorConstructor(Reflector.RenderLivingEvent_Pre, new Class[] { EntityLivingBase.class, RendererLivingEntity.class, Double.TYPE, Double.TYPE, Double.TYPE });
        Reflector.RenderLivingEvent_Post = new ReflectorClass("net.minecraftforge.client.event.RenderLivingEvent$Post");
        Reflector.RenderLivingEvent_Post_Constructor = new ReflectorConstructor(Reflector.RenderLivingEvent_Post, new Class[] { EntityLivingBase.class, RendererLivingEntity.class, Double.TYPE, Double.TYPE, Double.TYPE });
        Reflector.RenderLivingEvent_Specials_Pre = new ReflectorClass("net.minecraftforge.client.event.RenderLivingEvent$Specials$Pre");
        Reflector.RenderLivingEvent_Specials_Pre_Constructor = new ReflectorConstructor(Reflector.RenderLivingEvent_Specials_Pre, new Class[] { EntityLivingBase.class, RendererLivingEntity.class, Double.TYPE, Double.TYPE, Double.TYPE });
        Reflector.RenderLivingEvent_Specials_Post = new ReflectorClass("net.minecraftforge.client.event.RenderLivingEvent$Specials$Post");
        Reflector.RenderLivingEvent_Specials_Post_Constructor = new ReflectorConstructor(Reflector.RenderLivingEvent_Specials_Post, new Class[] { EntityLivingBase.class, RendererLivingEntity.class, Double.TYPE, Double.TYPE, Double.TYPE });
        Reflector.EventBus = new ReflectorClass("net.minecraftforge.fml.common.eventhandler.EventBus");
        Reflector.EventBus_post = new ReflectorMethod(Reflector.EventBus, "post");
        Reflector.Event_Result = new ReflectorClass("net.minecraftforge.fml.common.eventhandler.Event$Result");
        Reflector.Event_Result_DENY = new ReflectorField(Reflector.Event_Result, "DENY");
        Reflector.Event_Result_ALLOW = new ReflectorField(Reflector.Event_Result, "ALLOW");
        Reflector.Event_Result_DEFAULT = new ReflectorField(Reflector.Event_Result, "DEFAULT");
        Reflector.ForgeEventFactory = new ReflectorClass("net.minecraftforge.event.ForgeEventFactory");
        Reflector.ForgeEventFactory_canEntitySpawn = new ReflectorMethod(Reflector.ForgeEventFactory, "canEntitySpawn");
        Reflector.ForgeEventFactory_canEntityDespawn = new ReflectorMethod(Reflector.ForgeEventFactory, "canEntityDespawn");
        Reflector.ForgeEventFactory_renderBlockOverlay = new ReflectorMethod(Reflector.ForgeEventFactory, "renderBlockOverlay");
        Reflector.ForgeEventFactory_renderWaterOverlay = new ReflectorMethod(Reflector.ForgeEventFactory, "renderWaterOverlay");
        Reflector.ForgeEventFactory_renderFireOverlay = new ReflectorMethod(Reflector.ForgeEventFactory, "renderFireOverlay");
        Reflector.RenderBlockOverlayEvent_OverlayType = new ReflectorClass("net.minecraftforge.client.event.RenderBlockOverlayEvent$OverlayType");
        Reflector.RenderBlockOverlayEvent_OverlayType_BLOCK = new ReflectorField(Reflector.RenderBlockOverlayEvent_OverlayType, "BLOCK");
        Reflector.ChunkWatchEvent_UnWatch = new ReflectorClass("net.minecraftforge.event.world.ChunkWatchEvent$UnWatch");
        Reflector.ChunkWatchEvent_UnWatch_Constructor = new ReflectorConstructor(Reflector.ChunkWatchEvent_UnWatch, new Class[] { ChunkCoordIntPair.class, EntityPlayerMP.class });
        Reflector.ForgeBlock = new ReflectorClass(Block.class);
        Reflector.ForgeBlock_getBedDirection = new ReflectorMethod(Reflector.ForgeBlock, "getBedDirection");
        Reflector.ForgeBlock_isBed = new ReflectorMethod(Reflector.ForgeBlock, "isBed");
        Reflector.ForgeBlock_isBedFoot = new ReflectorMethod(Reflector.ForgeBlock, "isBedFoot");
        Reflector.ForgeBlock_hasTileEntity = new ReflectorMethod(Reflector.ForgeBlock, "hasTileEntity", new Class[] { IBlockState.class });
        Reflector.ForgeBlock_canCreatureSpawn = new ReflectorMethod(Reflector.ForgeBlock, "canCreatureSpawn");
        Reflector.ForgeBlock_addHitEffects = new ReflectorMethod(Reflector.ForgeBlock, "addHitEffects");
        Reflector.ForgeBlock_addDestroyEffects = new ReflectorMethod(Reflector.ForgeBlock, "addDestroyEffects");
        Reflector.ForgeBlock_isAir = new ReflectorMethod(Reflector.ForgeBlock, "isAir");
        Reflector.ForgeBlock_canRenderInLayer = new ReflectorMethod(Reflector.ForgeBlock, "canRenderInLayer");
        Reflector.ForgeBlock_getExtendedState = new ReflectorMethod(Reflector.ForgeBlock, "getExtendedState");
        Reflector.ForgeEntity = new ReflectorClass(Entity.class);
        Reflector.ForgeEntity_captureDrops = new ReflectorField(Reflector.ForgeEntity, "captureDrops");
        Reflector.ForgeEntity_capturedDrops = new ReflectorField(Reflector.ForgeEntity, "capturedDrops");
        Reflector.ForgeEntity_shouldRenderInPass = new ReflectorMethod(Reflector.ForgeEntity, "shouldRenderInPass");
        Reflector.ForgeEntity_canRiderInteract = new ReflectorMethod(Reflector.ForgeEntity, "canRiderInteract");
        Reflector.ForgeEntity_shouldRiderSit = new ReflectorMethod(Reflector.ForgeEntity, "shouldRiderSit");
        Reflector.ForgeTileEntity = new ReflectorClass(TileEntity.class);
        Reflector.ForgeTileEntity_shouldRenderInPass = new ReflectorMethod(Reflector.ForgeTileEntity, "shouldRenderInPass");
        Reflector.ForgeTileEntity_getRenderBoundingBox = new ReflectorMethod(Reflector.ForgeTileEntity, "getRenderBoundingBox");
        Reflector.ForgeTileEntity_canRenderBreaking = new ReflectorMethod(Reflector.ForgeTileEntity, "canRenderBreaking");
        Reflector.ForgeItem = new ReflectorClass(Item.class);
        Reflector.ForgeItem_onEntitySwing = new ReflectorMethod(Reflector.ForgeItem, "onEntitySwing");
        Reflector.ForgeItem_shouldCauseReequipAnimation = new ReflectorMethod(Reflector.ForgeItem, "shouldCauseReequipAnimation");
        Reflector.ForgeItem_getModel = new ReflectorMethod(Reflector.ForgeItem, "getModel");
        Reflector.ForgeItem_showDurabilityBar = new ReflectorMethod(Reflector.ForgeItem, "showDurabilityBar");
        Reflector.ForgeItem_getDurabilityForDisplay = new ReflectorMethod(Reflector.ForgeItem, "getDurabilityForDisplay");
        Reflector.ForgePotionEffect = new ReflectorClass(PotionEffect.class);
        Reflector.ForgePotionEffect_isCurativeItem = new ReflectorMethod(Reflector.ForgePotionEffect, "isCurativeItem");
        Reflector.ForgeItemRecord = new ReflectorClass(ItemRecord.class);
        Reflector.ForgeItemRecord_getRecordResource = new ReflectorMethod(Reflector.ForgeItemRecord, "getRecordResource", new Class[] { String.class });
        Reflector.ForgeVertexFormatElementEnumUseage = new ReflectorClass(VertexFormatElement.EnumUseage.class);
        Reflector.ForgeVertexFormatElementEnumUseage_preDraw = new ReflectorMethod(Reflector.ForgeVertexFormatElementEnumUseage, "preDraw");
        Reflector.ForgeVertexFormatElementEnumUseage_postDraw = new ReflectorMethod(Reflector.ForgeVertexFormatElementEnumUseage, "postDraw");
        Reflector.BlamingTransformer = new ReflectorClass("net.minecraftforge.fml.common.asm.transformers.BlamingTransformer");
        Reflector.BlamingTransformer_onCrash = new ReflectorMethod(Reflector.BlamingTransformer, "onCrash");
        Reflector.CoreModManager = new ReflectorClass("net.minecraftforge.fml.relauncher.CoreModManager");
        Reflector.CoreModManager_onCrash = new ReflectorMethod(Reflector.CoreModManager, "onCrash");
        Reflector.ISmartBlockModel = new ReflectorClass("net.minecraftforge.client.model.ISmartBlockModel");
        Reflector.ISmartBlockModel_handleBlockState = new ReflectorMethod(Reflector.ISmartBlockModel, "handleBlockState");
        Reflector.Launch = new ReflectorClass("net.minecraft.launchwrapper.Launch");
        Reflector.Launch_blackboard = new ReflectorField(Reflector.Launch, "blackboard");
        Reflector.SplashScreen = new ReflectorClass("net.minecraftforge.fml.client.SplashProgress");
        Reflector.LightUtil = new ReflectorClass("net.minecraftforge.client.model.pipeline.LightUtil");
        Reflector.LightUtil_tessellator = new ReflectorField(Reflector.LightUtil, "tessellator");
        Reflector.LightUtil_itemConsumer = new ReflectorField(Reflector.LightUtil, "itemConsumer");
        Reflector.LightUtil_putBakedQuad = new ReflectorMethod(Reflector.LightUtil, "putBakedQuad");
        Reflector.LightUtil_renderQuadColor = new ReflectorMethod(Reflector.LightUtil, "renderQuadColor");
        Reflector.IExtendedBlockState = new ReflectorClass("net.minecraftforge.common.property.IExtendedBlockState");
        Reflector.IExtendedBlockState_getClean = new ReflectorMethod(Reflector.IExtendedBlockState, "getClean");
        Reflector.ItemModelMesherForge = new ReflectorClass("net.minecraftforge.client.ItemModelMesherForge");
        Reflector.ItemModelMesherForge_Constructor = new ReflectorConstructor(Reflector.ItemModelMesherForge, new Class[] { ModelManager.class });
        Reflector.ModelLoader = new ReflectorClass("net.minecraftforge.client.model.ModelLoader");
        Reflector.ModelLoader_onRegisterItems = new ReflectorMethod(Reflector.ModelLoader, "onRegisterItems");
        Reflector.Attributes = new ReflectorClass("net.minecraftforge.client.model.Attributes");
        Reflector.Attributes_DEFAULT_BAKED_FORMAT = new ReflectorField(Reflector.Attributes, "DEFAULT_BAKED_FORMAT");
        Reflector.BetterFoliageClient = new ReflectorClass("mods.betterfoliage.client.BetterFoliageClient");
        Reflector.IColoredBakedQuad = new ReflectorClass("net.minecraftforge.client.model.IColoredBakedQuad");
        Reflector.ForgeBiomeGenBase = new ReflectorClass(BiomeGenBase.class);
        Reflector.ForgeBiomeGenBase_getWaterColorMultiplier = new ReflectorMethod(Reflector.ForgeBiomeGenBase, "getWaterColorMultiplier");
        Reflector.RenderingRegistry = new ReflectorClass("net.minecraftforge.fml.client.registry.RenderingRegistry");
        Reflector.RenderingRegistry_loadEntityRenderers = new ReflectorMethod(Reflector.RenderingRegistry, "loadEntityRenderers", new Class[] { RenderManager.class, Map.class });
        Reflector.ForgeTileEntityRendererDispatcher = new ReflectorClass(TileEntityRendererDispatcher.class);
        Reflector.ForgeTileEntityRendererDispatcher_preDrawBatch = new ReflectorMethod(Reflector.ForgeTileEntityRendererDispatcher, "preDrawBatch");
        Reflector.ForgeTileEntityRendererDispatcher_drawBatch = new ReflectorMethod(Reflector.ForgeTileEntityRendererDispatcher, "drawBatch");
        Reflector.OptiFineClassTransformer = new ReflectorClass("optifine.OptiFineClassTransformer");
        Reflector.OptiFineClassTransformer_instance = new ReflectorField(Reflector.OptiFineClassTransformer, "instance");
        Reflector.OptiFineClassTransformer_getOptiFineResource = new ReflectorMethod(Reflector.OptiFineClassTransformer, "getOptiFineResource");
        Reflector.ForgeModContainer = new ReflectorClass("net.minecraftforge.common.ForgeModContainer");
        Reflector.ForgeModContainer_forgeLightPipelineEnabled = new ReflectorField(Reflector.ForgeModContainer, "forgeLightPipelineEnabled");
    }
    
    public static void callVoid(final ReflectorMethod refMethod, final Object... params) {
        try {
            final Method e = refMethod.getTargetMethod();
            if (e == null) {
                return;
            }
            e.invoke(null, params);
        }
        catch (Throwable var3) {
            handleException(var3, null, refMethod, params);
        }
    }
    
    public static boolean callBoolean(final ReflectorMethod refMethod, final Object... params) {
        try {
            final Method e = refMethod.getTargetMethod();
            if (e == null) {
                return false;
            }
            final Boolean retVal = (Boolean)e.invoke(null, params);
            return retVal;
        }
        catch (Throwable var4) {
            handleException(var4, null, refMethod, params);
            return false;
        }
    }
    
    public static int callInt(final ReflectorMethod refMethod, final Object... params) {
        try {
            final Method e = refMethod.getTargetMethod();
            if (e == null) {
                return 0;
            }
            final Integer retVal = (Integer)e.invoke(null, params);
            return retVal;
        }
        catch (Throwable var4) {
            handleException(var4, null, refMethod, params);
            return 0;
        }
    }
    
    public static float callFloat(final ReflectorMethod refMethod, final Object... params) {
        try {
            final Method e = refMethod.getTargetMethod();
            if (e == null) {
                return 0.0f;
            }
            final Float retVal = (Float)e.invoke(null, params);
            return retVal;
        }
        catch (Throwable var4) {
            handleException(var4, null, refMethod, params);
            return 0.0f;
        }
    }
    
    public static double callDouble(final ReflectorMethod refMethod, final Object... params) {
        try {
            final Method e = refMethod.getTargetMethod();
            if (e == null) {
                return 0.0;
            }
            final Double retVal = (Double)e.invoke(null, params);
            return retVal;
        }
        catch (Throwable var4) {
            handleException(var4, null, refMethod, params);
            return 0.0;
        }
    }
    
    public static String callString(final ReflectorMethod refMethod, final Object... params) {
        try {
            final Method e = refMethod.getTargetMethod();
            if (e == null) {
                return null;
            }
            final String retVal = (String)e.invoke(null, params);
            return retVal;
        }
        catch (Throwable var4) {
            handleException(var4, null, refMethod, params);
            return null;
        }
    }
    
    public static Object call(final ReflectorMethod refMethod, final Object... params) {
        try {
            final Method e = refMethod.getTargetMethod();
            if (e == null) {
                return null;
            }
            final Object retVal = e.invoke(null, params);
            return retVal;
        }
        catch (Throwable var4) {
            handleException(var4, null, refMethod, params);
            return null;
        }
    }
    
    public static void callVoid(final Object obj, final ReflectorMethod refMethod, final Object... params) {
        try {
            if (obj == null) {
                return;
            }
            final Method e = refMethod.getTargetMethod();
            if (e == null) {
                return;
            }
            e.invoke(obj, params);
        }
        catch (Throwable var4) {
            handleException(var4, obj, refMethod, params);
        }
    }
    
    public static boolean callBoolean(final Object obj, final ReflectorMethod refMethod, final Object... params) {
        try {
            final Method e = refMethod.getTargetMethod();
            if (e == null) {
                return false;
            }
            final Boolean retVal = (Boolean)e.invoke(obj, params);
            return retVal;
        }
        catch (Throwable var5) {
            handleException(var5, obj, refMethod, params);
            return false;
        }
    }
    
    public static int callInt(final Object obj, final ReflectorMethod refMethod, final Object... params) {
        try {
            final Method e = refMethod.getTargetMethod();
            if (e == null) {
                return 0;
            }
            final Integer retVal = (Integer)e.invoke(obj, params);
            return retVal;
        }
        catch (Throwable var5) {
            handleException(var5, obj, refMethod, params);
            return 0;
        }
    }
    
    public static float callFloat(final Object obj, final ReflectorMethod refMethod, final Object... params) {
        try {
            final Method e = refMethod.getTargetMethod();
            if (e == null) {
                return 0.0f;
            }
            final Float retVal = (Float)e.invoke(obj, params);
            return retVal;
        }
        catch (Throwable var5) {
            handleException(var5, obj, refMethod, params);
            return 0.0f;
        }
    }
    
    public static double callDouble(final Object obj, final ReflectorMethod refMethod, final Object... params) {
        try {
            final Method e = refMethod.getTargetMethod();
            if (e == null) {
                return 0.0;
            }
            final Double retVal = (Double)e.invoke(obj, params);
            return retVal;
        }
        catch (Throwable var5) {
            handleException(var5, obj, refMethod, params);
            return 0.0;
        }
    }
    
    public static String callString(final Object obj, final ReflectorMethod refMethod, final Object... params) {
        try {
            final Method e = refMethod.getTargetMethod();
            if (e == null) {
                return null;
            }
            final String retVal = (String)e.invoke(obj, params);
            return retVal;
        }
        catch (Throwable var5) {
            handleException(var5, obj, refMethod, params);
            return null;
        }
    }
    
    public static Object call(final Object obj, final ReflectorMethod refMethod, final Object... params) {
        try {
            final Method e = refMethod.getTargetMethod();
            if (e == null) {
                return null;
            }
            final Object retVal = e.invoke(obj, params);
            return retVal;
        }
        catch (Throwable var5) {
            handleException(var5, obj, refMethod, params);
            return null;
        }
    }
    
    public static Object getFieldValue(final ReflectorField refField) {
        return getFieldValue(null, refField);
    }
    
    public static Object getFieldValue(final Object obj, final ReflectorField refField) {
        try {
            final Field e = refField.getTargetField();
            if (e == null) {
                return null;
            }
            final Object value = e.get(obj);
            return value;
        }
        catch (Throwable var4) {
            var4.printStackTrace();
            return null;
        }
    }
    
    public static float getFieldValueFloat(final Object obj, final ReflectorField refField, final float def) {
        final Object val = getFieldValue(obj, refField);
        if (!(val instanceof Float)) {
            return def;
        }
        final Float valFloat = (Float)val;
        return valFloat;
    }
    
    public static void setFieldValue(final ReflectorField refField, final Object value) {
        setFieldValue(null, refField, value);
    }
    
    public static void setFieldValue(final Object obj, final ReflectorField refField, final Object value) {
        try {
            final Field e = refField.getTargetField();
            if (e == null) {
                return;
            }
            e.set(obj, value);
        }
        catch (Throwable var4) {
            var4.printStackTrace();
        }
    }
    
    public static boolean postForgeBusEvent(final ReflectorConstructor constr, final Object... params) {
        final Object event = newInstance(constr, params);
        return event != null && postForgeBusEvent(event);
    }
    
    public static boolean postForgeBusEvent(final Object event) {
        if (event == null) {
            return false;
        }
        final Object eventBus = getFieldValue(Reflector.MinecraftForge_EVENT_BUS);
        if (eventBus == null) {
            return false;
        }
        final Object ret = call(eventBus, Reflector.EventBus_post, event);
        if (!(ret instanceof Boolean)) {
            return false;
        }
        final Boolean retBool = (Boolean)ret;
        return retBool;
    }
    
    public static Object newInstance(final ReflectorConstructor constr, final Object... params) {
        final Constructor c = constr.getTargetConstructor();
        if (c == null) {
            return null;
        }
        try {
            final Object e = c.newInstance(params);
            return e;
        }
        catch (Throwable var4) {
            handleException(var4, constr, params);
            return null;
        }
    }
    
    public static Method getMethod(final Class cls, final String methodName, final Class[] paramTypes) {
        final Method[] ms = cls.getDeclaredMethods();
        for (int i = 0; i < ms.length; ++i) {
            final Method m = ms[i];
            if (m.getName().equals(methodName)) {
                final Class[] types = m.getParameterTypes();
                if (matchesTypes(paramTypes, types)) {
                    return m;
                }
            }
        }
        return null;
    }
    
    public static Method[] getMethods(final Class cls, final String methodName) {
        final ArrayList listMethods = new ArrayList();
        final Method[] ms = cls.getDeclaredMethods();
        for (int methods = 0; methods < ms.length; ++methods) {
            final Method m = ms[methods];
            if (m.getName().equals(methodName)) {
                listMethods.add(m);
            }
        }
        final Method[] var6 = listMethods.toArray(new Method[listMethods.size()]);
        return var6;
    }
    
    public static boolean matchesTypes(final Class[] pTypes, final Class[] cTypes) {
        if (pTypes.length != cTypes.length) {
            return false;
        }
        for (int i = 0; i < cTypes.length; ++i) {
            final Class pType = pTypes[i];
            final Class cType = cTypes[i];
            if (pType != cType) {
                return false;
            }
        }
        return true;
    }
    
    private static void dbgCall(final boolean isStatic, final String callType, final ReflectorMethod refMethod, final Object[] params, final Object retVal) {
        final String className = refMethod.getTargetMethod().getDeclaringClass().getName();
        final String methodName = refMethod.getTargetMethod().getName();
        String staticStr = "";
        if (isStatic) {
            staticStr = " static";
        }
        Config.dbg(String.valueOf(callType) + staticStr + " " + className + "." + methodName + "(" + Config.arrayToString(params) + ") => " + retVal);
    }
    
    private static void dbgCallVoid(final boolean isStatic, final String callType, final ReflectorMethod refMethod, final Object[] params) {
        final String className = refMethod.getTargetMethod().getDeclaringClass().getName();
        final String methodName = refMethod.getTargetMethod().getName();
        String staticStr = "";
        if (isStatic) {
            staticStr = " static";
        }
        Config.dbg(String.valueOf(callType) + staticStr + " " + className + "." + methodName + "(" + Config.arrayToString(params) + ")");
    }
    
    private static void dbgFieldValue(final boolean isStatic, final String accessType, final ReflectorField refField, final Object val) {
        final String className = refField.getTargetField().getDeclaringClass().getName();
        final String fieldName = refField.getTargetField().getName();
        String staticStr = "";
        if (isStatic) {
            staticStr = " static";
        }
        Config.dbg(String.valueOf(accessType) + staticStr + " " + className + "." + fieldName + " => " + val);
    }
    
    private static void handleException(final Throwable e, final Object obj, final ReflectorMethod refMethod, final Object[] params) {
        if (e instanceof InvocationTargetException) {
            e.printStackTrace();
        }
        else {
            if (e instanceof IllegalArgumentException) {
                Config.warn("*** IllegalArgumentException ***");
                Config.warn("Method: " + refMethod.getTargetMethod());
                Config.warn("Object: " + obj);
                Config.warn("Parameter classes: " + Config.arrayToString(getClasses(params)));
                Config.warn("Parameters: " + Config.arrayToString(params));
            }
            Config.warn("*** Exception outside of method ***");
            Config.warn("Method deactivated: " + refMethod.getTargetMethod());
            refMethod.deactivate();
            e.printStackTrace();
        }
    }
    
    private static void handleException(final Throwable e, final ReflectorConstructor refConstr, final Object[] params) {
        if (e instanceof InvocationTargetException) {
            e.printStackTrace();
        }
        else {
            if (e instanceof IllegalArgumentException) {
                Config.warn("*** IllegalArgumentException ***");
                Config.warn("Constructor: " + refConstr.getTargetConstructor());
                Config.warn("Parameter classes: " + Config.arrayToString(getClasses(params)));
                Config.warn("Parameters: " + Config.arrayToString(params));
            }
            Config.warn("*** Exception outside of constructor ***");
            Config.warn("Constructor deactivated: " + refConstr.getTargetConstructor());
            refConstr.deactivate();
            e.printStackTrace();
        }
    }
    
    private static Object[] getClasses(final Object[] objs) {
        if (objs == null) {
            return new Class[0];
        }
        final Class[] classes = new Class[objs.length];
        for (int i = 0; i < classes.length; ++i) {
            final Object obj = objs[i];
            if (obj != null) {
                classes[i] = obj.getClass();
            }
        }
        return classes;
    }
    
    public static Field getField(final Class cls, final Class fieldType) {
        try {
            final Field[] e = cls.getDeclaredFields();
            for (int i = 0; i < e.length; ++i) {
                final Field field = e[i];
                if (field.getType() == fieldType) {
                    field.setAccessible(true);
                    return field;
                }
            }
            return null;
        }
        catch (Exception var5) {
            return null;
        }
    }
    
    public static Field[] getFields(final Class cls, final Class fieldType) {
        final ArrayList list = new ArrayList();
        try {
            final Field[] e = cls.getDeclaredFields();
            for (int fields = 0; fields < e.length; ++fields) {
                final Field field = e[fields];
                if (field.getType() == fieldType) {
                    field.setAccessible(true);
                    list.add(field);
                }
            }
            final Field[] var7 = list.toArray(new Field[list.size()]);
            return var7;
        }
        catch (Exception var8) {
            return null;
        }
    }
}
