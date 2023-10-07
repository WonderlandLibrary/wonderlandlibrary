// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.optifine;

import net.minecraft.item.ItemRecord;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import java.util.ArrayList;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class Reflector
{
    public static ReflectorClass ModLoader;
    public static ReflectorMethod ModLoader_renderWorldBlock;
    public static ReflectorMethod ModLoader_renderInvBlock;
    public static ReflectorMethod ModLoader_renderBlockIsItemFull3D;
    public static ReflectorMethod ModLoader_registerServer;
    public static ReflectorMethod ModLoader_getCustomAnimationLogic;
    public static ReflectorClass FMLRenderAccessLibrary;
    public static ReflectorMethod FMLRenderAccessLibrary_renderWorldBlock;
    public static ReflectorMethod FMLRenderAccessLibrary_renderInventoryBlock;
    public static ReflectorMethod FMLRenderAccessLibrary_renderItemAsFull3DBlock;
    public static ReflectorClass LightCache;
    public static ReflectorField LightCache_cache;
    public static ReflectorMethod LightCache_clear;
    public static ReflectorClass BlockCoord;
    public static ReflectorMethod BlockCoord_resetPool;
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
    public static ReflectorMethod MinecraftForgeClient_getItemRenderer;
    public static ReflectorClass ForgeHooksClient;
    public static ReflectorMethod ForgeHooksClient_onDrawBlockHighlight;
    public static ReflectorMethod ForgeHooksClient_orientBedCamera;
    public static ReflectorMethod ForgeHooksClient_renderEquippedItem;
    public static ReflectorMethod ForgeHooksClient_dispatchRenderLast;
    public static ReflectorMethod ForgeHooksClient_onTextureLoadPre;
    public static ReflectorMethod ForgeHooksClient_setRenderPass;
    public static ReflectorMethod ForgeHooksClient_onTextureStitchedPre;
    public static ReflectorMethod ForgeHooksClient_onTextureStitchedPost;
    public static ReflectorMethod ForgeHooksClient_renderFirstPersonHand;
    public static ReflectorMethod ForgeHooksClient_setWorldRendererRB;
    public static ReflectorMethod ForgeHooksClient_onPreRenderWorld;
    public static ReflectorMethod ForgeHooksClient_onPostRenderWorld;
    public static ReflectorClass FMLCommonHandler;
    public static ReflectorMethod FMLCommonHandler_instance;
    public static ReflectorMethod FMLCommonHandler_handleServerStarting;
    public static ReflectorMethod FMLCommonHandler_handleServerAboutToStart;
    public static ReflectorMethod FMLCommonHandler_enhanceCrashReport;
    public static ReflectorClass FMLClientHandler;
    public static ReflectorMethod FMLClientHandler_instance;
    public static ReflectorMethod FMLClientHandler_isLoading;
    public static ReflectorClass ItemRenderType;
    public static ReflectorField ItemRenderType_EQUIPPED;
    public static ReflectorClass ForgeWorldProvider;
    public static ReflectorMethod ForgeWorldProvider_getSkyRenderer;
    public static ReflectorMethod ForgeWorldProvider_getCloudRenderer;
    public static ReflectorMethod ForgeWorldProvider_getWeatherRenderer;
    public static ReflectorClass ForgeWorld;
    public static ReflectorMethod ForgeWorld_countEntities;
    public static ReflectorClass IRenderHandler;
    public static ReflectorMethod IRenderHandler_render;
    public static ReflectorClass DimensionManager;
    public static ReflectorMethod DimensionManager_getStaticDimensionIDs;
    public static ReflectorClass WorldEvent_Load;
    public static ReflectorConstructor WorldEvent_Load_Constructor;
    public static ReflectorClass DrawScreenEvent_Pre;
    public static ReflectorConstructor DrawScreenEvent_Pre_Constructor;
    public static ReflectorClass DrawScreenEvent_Post;
    public static ReflectorConstructor DrawScreenEvent_Post_Constructor;
    public static ReflectorClass EntityViewRenderEvent_FogColors;
    public static ReflectorConstructor EntityViewRenderEvent_FogColors_Constructor;
    public static ReflectorField EntityViewRenderEvent_FogColors_red;
    public static ReflectorField EntityViewRenderEvent_FogColors_green;
    public static ReflectorField EntityViewRenderEvent_FogColors_blue;
    public static ReflectorClass EntityViewRenderEvent_FogDensity;
    public static ReflectorConstructor EntityViewRenderEvent_FogDensity_Constructor;
    public static ReflectorField EntityViewRenderEvent_FogDensity_density;
    public static ReflectorClass EntityViewRenderEvent_RenderFogEvent;
    public static ReflectorConstructor EntityViewRenderEvent_RenderFogEvent_Constructor;
    public static ReflectorClass EventBus;
    public static ReflectorMethod EventBus_post;
    public static ReflectorClass Event_Result;
    public static ReflectorField Event_Result_DENY;
    public static ReflectorField Event_Result_ALLOW;
    public static ReflectorField Event_Result_DEFAULT;
    public static ReflectorClass ForgeEventFactory;
    public static ReflectorMethod ForgeEventFactory_canEntitySpawn;
    public static ReflectorMethod ForgeEventFactory_canEntityDespawn;
    public static ReflectorClass ChunkWatchEvent_UnWatch;
    public static ReflectorConstructor ChunkWatchEvent_UnWatch_Constructor;
    public static ReflectorClass ForgeBlock;
    public static ReflectorMethod ForgeBlock_getBedDirection;
    public static ReflectorMethod ForgeBlock_isBedFoot;
    public static ReflectorMethod ForgeBlock_canRenderInPass;
    public static ReflectorMethod ForgeBlock_hasTileEntity;
    public static ReflectorMethod ForgeBlock_canCreatureSpawn;
    public static ReflectorClass ForgeEntity;
    public static ReflectorField ForgeEntity_captureDrops;
    public static ReflectorField ForgeEntity_capturedDrops;
    public static ReflectorMethod ForgeEntity_shouldRenderInPass;
    public static ReflectorMethod ForgeEntity_canRiderInteract;
    public static ReflectorClass ForgeTileEntity;
    public static ReflectorMethod ForgeTileEntity_shouldRenderInPass;
    public static ReflectorMethod ForgeTileEntity_getRenderBoundingBox;
    public static ReflectorClass ForgeItem;
    public static ReflectorMethod ForgeItem_onEntitySwing;
    public static ReflectorClass ForgePotionEffect;
    public static ReflectorMethod ForgePotionEffect_isCurativeItem;
    public static ReflectorClass ForgeItemStack;
    public static ReflectorMethod ForgeItemStack_hasEffect;
    public static ReflectorClass ForgeItemRecord;
    public static ReflectorMethod ForgeItemRecord_getRecordResource;
    
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
        Config.dbg(callType + staticStr + " " + className + "." + methodName + "(" + Config.arrayToString(params) + ") => " + retVal);
    }
    
    private static void dbgCallVoid(final boolean isStatic, final String callType, final ReflectorMethod refMethod, final Object[] params) {
        final String className = refMethod.getTargetMethod().getDeclaringClass().getName();
        final String methodName = refMethod.getTargetMethod().getName();
        String staticStr = "";
        if (isStatic) {
            staticStr = " static";
        }
        Config.dbg(callType + staticStr + " " + className + "." + methodName + "(" + Config.arrayToString(params) + ")");
    }
    
    private static void dbgFieldValue(final boolean isStatic, final String accessType, final ReflectorField refField, final Object val) {
        final String className = refField.getTargetField().getDeclaringClass().getName();
        final String fieldName = refField.getTargetField().getName();
        String staticStr = "";
        if (isStatic) {
            staticStr = " static";
        }
        Config.dbg(accessType + staticStr + " " + className + "." + fieldName + " => " + val);
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
            final Field[] declaredFields;
            final Field[] e = declaredFields = cls.getDeclaredFields();
            for (final Field field : declaredFields) {
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
            final Field[] declaredFields;
            final Field[] e = declaredFields = cls.getDeclaredFields();
            for (final Field field : declaredFields) {
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
    
    static {
        Reflector.ModLoader = new ReflectorClass("ModLoader");
        Reflector.ModLoader_renderWorldBlock = new ReflectorMethod(Reflector.ModLoader, "renderWorldBlock");
        Reflector.ModLoader_renderInvBlock = new ReflectorMethod(Reflector.ModLoader, "renderInvBlock");
        Reflector.ModLoader_renderBlockIsItemFull3D = new ReflectorMethod(Reflector.ModLoader, "renderBlockIsItemFull3D");
        Reflector.ModLoader_registerServer = new ReflectorMethod(Reflector.ModLoader, "registerServer");
        Reflector.ModLoader_getCustomAnimationLogic = new ReflectorMethod(Reflector.ModLoader, "getCustomAnimationLogic");
        Reflector.FMLRenderAccessLibrary = new ReflectorClass("net.minecraft.src.FMLRenderAccessLibrary");
        Reflector.FMLRenderAccessLibrary_renderWorldBlock = new ReflectorMethod(Reflector.FMLRenderAccessLibrary, "renderWorldBlock");
        Reflector.FMLRenderAccessLibrary_renderInventoryBlock = new ReflectorMethod(Reflector.FMLRenderAccessLibrary, "renderInventoryBlock");
        Reflector.FMLRenderAccessLibrary_renderItemAsFull3DBlock = new ReflectorMethod(Reflector.FMLRenderAccessLibrary, "renderItemAsFull3DBlock");
        Reflector.LightCache = new ReflectorClass("LightCache");
        Reflector.LightCache_cache = new ReflectorField(Reflector.LightCache, "cache");
        Reflector.LightCache_clear = new ReflectorMethod(Reflector.LightCache, "clear");
        Reflector.BlockCoord = new ReflectorClass("BlockCoord");
        Reflector.BlockCoord_resetPool = new ReflectorMethod(Reflector.BlockCoord, "resetPool");
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
        Reflector.MinecraftForgeClient_getItemRenderer = new ReflectorMethod(Reflector.MinecraftForgeClient, "getItemRenderer");
        Reflector.ForgeHooksClient = new ReflectorClass("net.minecraftforge.client.ForgeHooksClient");
        Reflector.ForgeHooksClient_onDrawBlockHighlight = new ReflectorMethod(Reflector.ForgeHooksClient, "onDrawBlockHighlight");
        Reflector.ForgeHooksClient_orientBedCamera = new ReflectorMethod(Reflector.ForgeHooksClient, "orientBedCamera");
        Reflector.ForgeHooksClient_renderEquippedItem = new ReflectorMethod(Reflector.ForgeHooksClient, "renderEquippedItem");
        Reflector.ForgeHooksClient_dispatchRenderLast = new ReflectorMethod(Reflector.ForgeHooksClient, "dispatchRenderLast");
        Reflector.ForgeHooksClient_onTextureLoadPre = new ReflectorMethod(Reflector.ForgeHooksClient, "onTextureLoadPre");
        Reflector.ForgeHooksClient_setRenderPass = new ReflectorMethod(Reflector.ForgeHooksClient, "setRenderPass");
        Reflector.ForgeHooksClient_onTextureStitchedPre = new ReflectorMethod(Reflector.ForgeHooksClient, "onTextureStitchedPre");
        Reflector.ForgeHooksClient_onTextureStitchedPost = new ReflectorMethod(Reflector.ForgeHooksClient, "onTextureStitchedPost");
        Reflector.ForgeHooksClient_renderFirstPersonHand = new ReflectorMethod(Reflector.ForgeHooksClient, "renderFirstPersonHand");
        Reflector.ForgeHooksClient_setWorldRendererRB = new ReflectorMethod(Reflector.ForgeHooksClient, "setWorldRendererRB");
        Reflector.ForgeHooksClient_onPreRenderWorld = new ReflectorMethod(Reflector.ForgeHooksClient, "onPreRenderWorld");
        Reflector.ForgeHooksClient_onPostRenderWorld = new ReflectorMethod(Reflector.ForgeHooksClient, "onPostRenderWorld");
        Reflector.FMLCommonHandler = new ReflectorClass("net.minecraftforge.fml.common.FMLCommonHandler");
        Reflector.FMLCommonHandler_instance = new ReflectorMethod(Reflector.FMLCommonHandler, "instance");
        Reflector.FMLCommonHandler_handleServerStarting = new ReflectorMethod(Reflector.FMLCommonHandler, "handleServerStarting");
        Reflector.FMLCommonHandler_handleServerAboutToStart = new ReflectorMethod(Reflector.FMLCommonHandler, "handleServerAboutToStart");
        Reflector.FMLCommonHandler_enhanceCrashReport = new ReflectorMethod(Reflector.FMLCommonHandler, "enhanceCrashReport");
        Reflector.FMLClientHandler = new ReflectorClass("net.minecraftforge.fml.client.FMLClientHandler");
        Reflector.FMLClientHandler_instance = new ReflectorMethod(Reflector.FMLClientHandler, "instance");
        Reflector.FMLClientHandler_isLoading = new ReflectorMethod(Reflector.FMLClientHandler, "isLoading");
        Reflector.ItemRenderType = new ReflectorClass("net.minecraftforge.client.IItemRenderer$ItemRenderType");
        Reflector.ItemRenderType_EQUIPPED = new ReflectorField(Reflector.ItemRenderType, "EQUIPPED");
        Reflector.ForgeWorldProvider = new ReflectorClass(WorldProvider.class);
        Reflector.ForgeWorldProvider_getSkyRenderer = new ReflectorMethod(Reflector.ForgeWorldProvider, "getSkyRenderer");
        Reflector.ForgeWorldProvider_getCloudRenderer = new ReflectorMethod(Reflector.ForgeWorldProvider, "getCloudRenderer");
        Reflector.ForgeWorldProvider_getWeatherRenderer = new ReflectorMethod(Reflector.ForgeWorldProvider, "getWeatherRenderer");
        Reflector.ForgeWorld = new ReflectorClass(World.class);
        Reflector.ForgeWorld_countEntities = new ReflectorMethod(Reflector.ForgeWorld, "countEntities", new Class[] { EnumCreatureType.class, Boolean.TYPE });
        Reflector.IRenderHandler = new ReflectorClass("net.minecraftforge.client.IRenderHandler");
        Reflector.IRenderHandler_render = new ReflectorMethod(Reflector.IRenderHandler, "render");
        Reflector.DimensionManager = new ReflectorClass("net.minecraftforge.common.DimensionManager");
        Reflector.DimensionManager_getStaticDimensionIDs = new ReflectorMethod(Reflector.DimensionManager, "getStaticDimensionIDs");
        Reflector.WorldEvent_Load = new ReflectorClass("net.minecraftforge.event.world.WorldEvent$Load");
        Reflector.WorldEvent_Load_Constructor = new ReflectorConstructor(Reflector.WorldEvent_Load, new Class[] { World.class });
        Reflector.DrawScreenEvent_Pre = new ReflectorClass("net.minecraftforge.client.event.GuiScreenEvent$DrawScreenEvent$Pre");
        Reflector.DrawScreenEvent_Pre_Constructor = new ReflectorConstructor(Reflector.DrawScreenEvent_Pre, new Class[] { GuiScreen.class, Integer.TYPE, Integer.TYPE, Float.TYPE });
        Reflector.DrawScreenEvent_Post = new ReflectorClass("net.minecraftforge.client.event.GuiScreenEvent$DrawScreenEvent$Post");
        Reflector.DrawScreenEvent_Post_Constructor = new ReflectorConstructor(Reflector.DrawScreenEvent_Post, new Class[] { GuiScreen.class, Integer.TYPE, Integer.TYPE, Float.TYPE });
        Reflector.EntityViewRenderEvent_FogColors = new ReflectorClass("net.minecraftforge.client.event.EntityViewRenderEvent$FogColors");
        Reflector.EntityViewRenderEvent_FogColors_Constructor = new ReflectorConstructor(Reflector.EntityViewRenderEvent_FogColors, new Class[] { EntityRenderer.class, EntityLivingBase.class, Block.class, Double.TYPE, Float.TYPE, Float.TYPE, Float.TYPE });
        Reflector.EntityViewRenderEvent_FogColors_red = new ReflectorField(Reflector.EntityViewRenderEvent_FogColors, "red");
        Reflector.EntityViewRenderEvent_FogColors_green = new ReflectorField(Reflector.EntityViewRenderEvent_FogColors, "green");
        Reflector.EntityViewRenderEvent_FogColors_blue = new ReflectorField(Reflector.EntityViewRenderEvent_FogColors, "blue");
        Reflector.EntityViewRenderEvent_FogDensity = new ReflectorClass("net.minecraftforge.client.event.EntityViewRenderEvent$FogDensity");
        Reflector.EntityViewRenderEvent_FogDensity_Constructor = new ReflectorConstructor(Reflector.EntityViewRenderEvent_FogDensity, new Class[] { EntityRenderer.class, EntityLivingBase.class, Block.class, Double.TYPE, Float.TYPE });
        Reflector.EntityViewRenderEvent_FogDensity_density = new ReflectorField(Reflector.EntityViewRenderEvent_FogDensity, "density");
        Reflector.EntityViewRenderEvent_RenderFogEvent = new ReflectorClass("net.minecraftforge.client.event.EntityViewRenderEvent$RenderFogEvent");
        Reflector.EntityViewRenderEvent_RenderFogEvent_Constructor = new ReflectorConstructor(Reflector.EntityViewRenderEvent_RenderFogEvent, new Class[] { EntityRenderer.class, EntityLivingBase.class, Block.class, Double.TYPE, Integer.TYPE, Float.TYPE });
        Reflector.EventBus = new ReflectorClass("net.minecraftforge.fml.common.eventhandler.EventBus");
        Reflector.EventBus_post = new ReflectorMethod(Reflector.EventBus, "post");
        Reflector.Event_Result = new ReflectorClass("net.minecraftforge.fml.common.eventhandler.Event$Result");
        Reflector.Event_Result_DENY = new ReflectorField(Reflector.Event_Result, "DENY");
        Reflector.Event_Result_ALLOW = new ReflectorField(Reflector.Event_Result, "ALLOW");
        Reflector.Event_Result_DEFAULT = new ReflectorField(Reflector.Event_Result, "DEFAULT");
        Reflector.ForgeEventFactory = new ReflectorClass("net.minecraftforge.event.ForgeEventFactory");
        Reflector.ForgeEventFactory_canEntitySpawn = new ReflectorMethod(Reflector.ForgeEventFactory, "canEntitySpawn");
        Reflector.ForgeEventFactory_canEntityDespawn = new ReflectorMethod(Reflector.ForgeEventFactory, "canEntityDespawn");
        Reflector.ChunkWatchEvent_UnWatch = new ReflectorClass("net.minecraftforge.event.world.ChunkWatchEvent$UnWatch");
        Reflector.ChunkWatchEvent_UnWatch_Constructor = new ReflectorConstructor(Reflector.ChunkWatchEvent_UnWatch, new Class[] { ChunkCoordIntPair.class, EntityPlayerMP.class });
        Reflector.ForgeBlock = new ReflectorClass(Block.class);
        Reflector.ForgeBlock_getBedDirection = new ReflectorMethod(Reflector.ForgeBlock, "getBedDirection");
        Reflector.ForgeBlock_isBedFoot = new ReflectorMethod(Reflector.ForgeBlock, "isBedFoot");
        Reflector.ForgeBlock_canRenderInPass = new ReflectorMethod(Reflector.ForgeBlock, "canRenderInPass");
        Reflector.ForgeBlock_hasTileEntity = new ReflectorMethod(Reflector.ForgeBlock, "hasTileEntity", new Class[] { Integer.TYPE });
        Reflector.ForgeBlock_canCreatureSpawn = new ReflectorMethod(Reflector.ForgeBlock, "canCreatureSpawn");
        Reflector.ForgeEntity = new ReflectorClass(Entity.class);
        Reflector.ForgeEntity_captureDrops = new ReflectorField(Reflector.ForgeEntity, "captureDrops");
        Reflector.ForgeEntity_capturedDrops = new ReflectorField(Reflector.ForgeEntity, "capturedDrops");
        Reflector.ForgeEntity_shouldRenderInPass = new ReflectorMethod(Reflector.ForgeEntity, "shouldRenderInPass");
        Reflector.ForgeEntity_canRiderInteract = new ReflectorMethod(Reflector.ForgeEntity, "canRiderInteract");
        Reflector.ForgeTileEntity = new ReflectorClass(TileEntity.class);
        Reflector.ForgeTileEntity_shouldRenderInPass = new ReflectorMethod(Reflector.ForgeTileEntity, "shouldRenderInPass");
        Reflector.ForgeTileEntity_getRenderBoundingBox = new ReflectorMethod(Reflector.ForgeTileEntity, "getRenderBoundingBox");
        Reflector.ForgeItem = new ReflectorClass(Item.class);
        Reflector.ForgeItem_onEntitySwing = new ReflectorMethod(Reflector.ForgeItem, "onEntitySwing");
        Reflector.ForgePotionEffect = new ReflectorClass(PotionEffect.class);
        Reflector.ForgePotionEffect_isCurativeItem = new ReflectorMethod(Reflector.ForgePotionEffect, "isCurativeItem");
        Reflector.ForgeItemStack = new ReflectorClass(ItemStack.class);
        Reflector.ForgeItemStack_hasEffect = new ReflectorMethod(Reflector.ForgeItemStack, "hasEffect", new Class[] { Integer.TYPE });
        Reflector.ForgeItemRecord = new ReflectorClass(ItemRecord.class);
        Reflector.ForgeItemRecord_getRecordResource = new ReflectorMethod(Reflector.ForgeItemRecord, "getRecordResource", new Class[] { String.class });
    }
}
