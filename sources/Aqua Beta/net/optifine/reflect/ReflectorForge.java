package net.optifine.reflect;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapData;
import net.optifine.reflect.Reflector;
import net.optifine.reflect.ReflectorField;
import net.optifine.reflect.ReflectorMethod;

public class ReflectorForge {
    public static Object EVENT_RESULT_ALLOW = Reflector.getFieldValue((ReflectorField)Reflector.Event_Result_ALLOW);
    public static Object EVENT_RESULT_DENY = Reflector.getFieldValue((ReflectorField)Reflector.Event_Result_DENY);
    public static Object EVENT_RESULT_DEFAULT = Reflector.getFieldValue((ReflectorField)Reflector.Event_Result_DEFAULT);

    public static void FMLClientHandler_trackBrokenTexture(ResourceLocation loc, String message) {
        if (!Reflector.FMLClientHandler_trackBrokenTexture.exists()) {
            Object object = Reflector.call((ReflectorMethod)Reflector.FMLClientHandler_instance, (Object[])new Object[0]);
            Reflector.call((Object)object, (ReflectorMethod)Reflector.FMLClientHandler_trackBrokenTexture, (Object[])new Object[]{loc, message});
        }
    }

    public static void FMLClientHandler_trackMissingTexture(ResourceLocation loc) {
        if (!Reflector.FMLClientHandler_trackMissingTexture.exists()) {
            Object object = Reflector.call((ReflectorMethod)Reflector.FMLClientHandler_instance, (Object[])new Object[0]);
            Reflector.call((Object)object, (ReflectorMethod)Reflector.FMLClientHandler_trackMissingTexture, (Object[])new Object[]{loc});
        }
    }

    public static void putLaunchBlackboard(String key, Object value) {
        Map map = (Map)Reflector.getFieldValue((ReflectorField)Reflector.Launch_blackboard);
        if (map != null) {
            map.put((Object)key, value);
        }
    }

    public static boolean renderFirstPersonHand(RenderGlobal renderGlobal, float partialTicks, int pass) {
        return !Reflector.ForgeHooksClient_renderFirstPersonHand.exists() ? false : Reflector.callBoolean((ReflectorMethod)Reflector.ForgeHooksClient_renderFirstPersonHand, (Object[])new Object[]{renderGlobal, Float.valueOf((float)partialTicks), pass});
    }

    public static InputStream getOptiFineResourceStream(String path) {
        byte[] abyte;
        if (!Reflector.OptiFineClassTransformer_instance.exists()) {
            return null;
        }
        Object object = Reflector.getFieldValue((ReflectorField)Reflector.OptiFineClassTransformer_instance);
        if (object == null) {
            return null;
        }
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        if ((abyte = (byte[])Reflector.call((Object)object, (ReflectorMethod)Reflector.OptiFineClassTransformer_getOptiFineResource, (Object[])new Object[]{path})) == null) {
            return null;
        }
        ByteArrayInputStream inputstream = new ByteArrayInputStream(abyte);
        return inputstream;
    }

    public static boolean blockHasTileEntity(IBlockState state) {
        Block block = state.getBlock();
        return !Reflector.ForgeBlock_hasTileEntity.exists() ? block.hasTileEntity() : Reflector.callBoolean((Object)block, (ReflectorMethod)Reflector.ForgeBlock_hasTileEntity, (Object[])new Object[]{state});
    }

    public static boolean isItemDamaged(ItemStack stack) {
        return !Reflector.ForgeItem_showDurabilityBar.exists() ? stack.isItemDamaged() : Reflector.callBoolean((Object)stack.getItem(), (ReflectorMethod)Reflector.ForgeItem_showDurabilityBar, (Object[])new Object[]{stack});
    }

    public static boolean armorHasOverlay(ItemArmor itemArmor, ItemStack itemStack) {
        int i = itemArmor.getColor(itemStack);
        return i != -1;
    }

    public static MapData getMapData(ItemMap itemMap, ItemStack stack, World world) {
        return Reflector.ForgeHooksClient.exists() ? ((ItemMap)stack.getItem()).getMapData(stack, world) : itemMap.getMapData(stack, world);
    }

    public static String[] getForgeModIds() {
        if (!Reflector.Loader.exists()) {
            return new String[0];
        }
        Object object = Reflector.call((ReflectorMethod)Reflector.Loader_instance, (Object[])new Object[0]);
        List list = (List)Reflector.call((Object)object, (ReflectorMethod)Reflector.Loader_getActiveModList, (Object[])new Object[0]);
        if (list == null) {
            return new String[0];
        }
        ArrayList list1 = new ArrayList();
        for (Object object1 : list) {
            String s;
            if (!Reflector.ModContainer.isInstance(object1) || (s = Reflector.callString((Object)object1, (ReflectorMethod)Reflector.ModContainer_getModId, (Object[])new Object[0])) == null) continue;
            list1.add((Object)s);
        }
        String[] astring = (String[])list1.toArray((Object[])new String[list1.size()]);
        return astring;
    }

    public static boolean canEntitySpawn(EntityLiving entityliving, World world, float x, float y, float z) {
        Object object = Reflector.call((ReflectorMethod)Reflector.ForgeEventFactory_canEntitySpawn, (Object[])new Object[]{entityliving, world, Float.valueOf((float)x), Float.valueOf((float)y), Float.valueOf((float)z)});
        return object == EVENT_RESULT_ALLOW || object == EVENT_RESULT_DEFAULT && entityliving.getCanSpawnHere() && entityliving.isNotColliding();
    }

    public static boolean doSpecialSpawn(EntityLiving entityliving, World world, float x, int y, float z) {
        return Reflector.ForgeEventFactory_doSpecialSpawn.exists() ? Reflector.callBoolean((ReflectorMethod)Reflector.ForgeEventFactory_doSpecialSpawn, (Object[])new Object[]{entityliving, world, Float.valueOf((float)x), y, Float.valueOf((float)z)}) : false;
    }
}
