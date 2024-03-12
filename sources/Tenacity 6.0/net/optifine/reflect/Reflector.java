// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.reflect;

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
import java.util.Map;
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
import org.apache.logging.log4j.LogManager;
import java.lang.reflect.InvocationTargetException;
import net.optifine.util.ArrayUtils;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import net.optifine.Log;
import java.lang.reflect.Method;
import org.apache.logging.log4j.Logger;

public class Reflector
{
    private static final Logger LOGGER;
    public static ReflectorMethod ForgeHooksClient_applyTransform;
    private static final boolean logForge;
    private static final boolean logVanilla;
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
    
    public static void callVoid(final ReflectorMethod refMethod, final Object... params) {
        try {
            final Method method = refMethod.getTargetMethod();
            if (method == null) {
                return;
            }
            method.invoke(null, params);
        }
        catch (Throwable throwable) {
            handleException(throwable, null, refMethod, params);
        }
    }
    
    public static boolean callBoolean(final ReflectorMethod refMethod, final Object... params) {
        try {
            final Method method = refMethod.getTargetMethod();
            return method != null && (boolean)method.invoke(null, params);
        }
        catch (Throwable throwable) {
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
            return (int)method.invoke(null, params);
        }
        catch (Throwable throwable) {
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
            return (float)method.invoke(null, params);
        }
        catch (Throwable throwable) {
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
            return (double)method.invoke(null, params);
        }
        catch (Throwable throwable) {
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
            return (String)method.invoke(null, params);
        }
        catch (Throwable throwable) {
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
            return method.invoke(null, params);
        }
        catch (Throwable throwable) {
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
        catch (Throwable throwable) {
            handleException(throwable, obj, refMethod, params);
        }
    }
    
    public static boolean callBoolean(final Object obj, final ReflectorMethod refMethod, final Object... params) {
        try {
            final Method method = refMethod.getTargetMethod();
            return method != null && (boolean)method.invoke(obj, params);
        }
        catch (Throwable throwable) {
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
            return (int)method.invoke(obj, params);
        }
        catch (Throwable throwable) {
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
            return (float)method.invoke(obj, params);
        }
        catch (Throwable throwable) {
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
            return (double)method.invoke(obj, params);
        }
        catch (Throwable throwable) {
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
            return (String)method.invoke(obj, params);
        }
        catch (Throwable throwable) {
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
            return method.invoke(obj, params);
        }
        catch (Throwable throwable) {
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
            return field.get(obj);
        }
        catch (Throwable throwable) {
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
            return field.getBoolean(null);
        }
        catch (Throwable throwable) {
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
            return field.getBoolean(obj);
        }
        catch (Throwable throwable) {
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
            return field.getFloat(obj);
        }
        catch (Throwable throwable) {
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
            return field.getInt(obj);
        }
        catch (Throwable throwable) {
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
            return field.getLong(obj);
        }
        catch (Throwable throwable) {
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
        catch (Throwable throwable) {
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
        catch (Throwable throwable) {
            Log.error("", throwable);
            return false;
        }
    }
    
    public static boolean postForgeBusEvent(final ReflectorConstructor constr, final Object... params) {
        final Object object = newInstance(constr, params);
        return object != null && postForgeBusEvent(object);
    }
    
    public static boolean postForgeBusEvent(final Object event) {
        return false;
    }
    
    public static Object newInstance(final ReflectorConstructor constr, final Object... params) {
        final Constructor constructor = constr.getTargetConstructor();
        if (constructor == null) {
            return null;
        }
        try {
            return constructor.newInstance(params);
        }
        catch (Throwable throwable) {
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
        Log.dbg(callType + s3 + " " + s + "." + s2 + "(" + ArrayUtils.arrayToString(params) + ") => " + retVal);
    }
    
    private static void dbgCallVoid(final boolean isStatic, final String callType, final ReflectorMethod refMethod, final Object[] params) {
        final String s = refMethod.getTargetMethod().getDeclaringClass().getName();
        final String s2 = refMethod.getTargetMethod().getName();
        String s3 = "";
        if (isStatic) {
            s3 = " static";
        }
        Log.dbg(callType + s3 + " " + s + "." + s2 + "(" + ArrayUtils.arrayToString(params) + ")");
    }
    
    private static void dbgFieldValue(final boolean isStatic, final String accessType, final ReflectorField refField, final Object val) {
        final String s = refField.getTargetField().getDeclaringClass().getName();
        final String s2 = refField.getTargetField().getName();
        String s3 = "";
        if (isStatic) {
            s3 = " static";
        }
        Log.dbg(accessType + s3 + " " + s + "." + s2 + " => " + val);
    }
    
    private static void handleException(final Throwable e, final Object obj, final ReflectorMethod refMethod, final Object[] params) {
        if (e instanceof InvocationTargetException) {
            final Throwable throwable = e.getCause();
            if (throwable instanceof RuntimeException) {
                throw (RuntimeException)throwable;
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
        final IResolvable iresolvable = () -> Reflector.LOGGER.info("[OptiFine] " + str);
        ReflectorResolver.register(iresolvable);
        return true;
    }
    
    static {
        LOGGER = LogManager.getLogger();
        logForge = logEntry("*** Reflector Forge ***");
        logVanilla = logEntry("*** Reflector Vanilla ***");
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
}
