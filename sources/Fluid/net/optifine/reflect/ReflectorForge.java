// 
// Decompiled by Procyon v0.6.0
// 

package net.optifine.reflect;

import net.minecraft.entity.EntityLiving;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.storage.MapData;
import net.minecraft.world.World;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import net.minecraft.client.renderer.RenderGlobal;
import java.util.Map;
import net.minecraft.util.ResourceLocation;

public class ReflectorForge
{
    public static Object EVENT_RESULT_ALLOW;
    public static Object EVENT_RESULT_DENY;
    public static Object EVENT_RESULT_DEFAULT;
    
    static {
        ReflectorForge.EVENT_RESULT_ALLOW = Reflector.getFieldValue(Reflector.Event_Result_ALLOW);
        ReflectorForge.EVENT_RESULT_DENY = Reflector.getFieldValue(Reflector.Event_Result_DENY);
        ReflectorForge.EVENT_RESULT_DEFAULT = Reflector.getFieldValue(Reflector.Event_Result_DEFAULT);
    }
    
    public static void FMLClientHandler_trackBrokenTexture(final ResourceLocation loc, final String message) {
        if (!Reflector.FMLClientHandler_trackBrokenTexture.exists()) {
            final Object object = Reflector.call(Reflector.FMLClientHandler_instance, new Object[0]);
            Reflector.call(object, Reflector.FMLClientHandler_trackBrokenTexture, loc, message);
        }
    }
    
    public static void FMLClientHandler_trackMissingTexture(final ResourceLocation loc) {
        if (!Reflector.FMLClientHandler_trackMissingTexture.exists()) {
            final Object object = Reflector.call(Reflector.FMLClientHandler_instance, new Object[0]);
            Reflector.call(object, Reflector.FMLClientHandler_trackMissingTexture, loc);
        }
    }
    
    public static void putLaunchBlackboard(final String key, final Object value) {
        final Map map = (Map)Reflector.getFieldValue(Reflector.Launch_blackboard);
        if (map != null) {
            map.put(key, value);
        }
    }
    
    public static boolean renderFirstPersonHand(final RenderGlobal renderGlobal, final float partialTicks, final int pass) {
        return Reflector.ForgeHooksClient_renderFirstPersonHand.exists() && Reflector.callBoolean(Reflector.ForgeHooksClient_renderFirstPersonHand, renderGlobal, partialTicks, pass);
    }
    
    public static InputStream getOptiFineResourceStream(String path) {
        if (!Reflector.OptiFineClassTransformer_instance.exists()) {
            return null;
        }
        final Object object = Reflector.getFieldValue(Reflector.OptiFineClassTransformer_instance);
        if (object == null) {
            return null;
        }
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        final byte[] abyte = (byte[])Reflector.call(object, Reflector.OptiFineClassTransformer_getOptiFineResource, path);
        if (abyte == null) {
            return null;
        }
        final InputStream inputstream = new ByteArrayInputStream(abyte);
        return inputstream;
    }
    
    public static boolean blockHasTileEntity(final IBlockState state) {
        final Block block = state.getBlock();
        return Reflector.ForgeBlock_hasTileEntity.exists() ? Reflector.callBoolean(block, Reflector.ForgeBlock_hasTileEntity, state) : block.hasTileEntity();
    }
    
    public static boolean isItemDamaged(final ItemStack stack) {
        return Reflector.ForgeItem_showDurabilityBar.exists() ? Reflector.callBoolean(stack.getItem(), Reflector.ForgeItem_showDurabilityBar, stack) : stack.isItemDamaged();
    }
    
    public static boolean armorHasOverlay(final ItemArmor itemArmor, final ItemStack itemStack) {
        final int i = itemArmor.getColor(itemStack);
        return i != -1;
    }
    
    public static MapData getMapData(final ItemMap itemMap, final ItemStack stack, final World world) {
        return Reflector.ForgeHooksClient.exists() ? ((ItemMap)stack.getItem()).getMapData(stack, world) : itemMap.getMapData(stack, world);
    }
    
    public static String[] getForgeModIds() {
        if (!Reflector.Loader.exists()) {
            return new String[0];
        }
        final Object object = Reflector.call(Reflector.Loader_instance, new Object[0]);
        final List list = (List)Reflector.call(object, Reflector.Loader_getActiveModList, new Object[0]);
        if (list == null) {
            return new String[0];
        }
        final List<String> list2 = new ArrayList<String>();
        for (final Object object2 : list) {
            if (Reflector.ModContainer.isInstance(object2)) {
                final String s = Reflector.callString(object2, Reflector.ModContainer_getModId, new Object[0]);
                if (s == null) {
                    continue;
                }
                list2.add(s);
            }
        }
        final String[] astring = list2.toArray(new String[list2.size()]);
        return astring;
    }
    
    public static boolean canEntitySpawn(final EntityLiving entityliving, final World world, final float x, final float y, final float z) {
        final Object object = Reflector.call(Reflector.ForgeEventFactory_canEntitySpawn, entityliving, world, x, y, z);
        return object == ReflectorForge.EVENT_RESULT_ALLOW || (object == ReflectorForge.EVENT_RESULT_DEFAULT && entityliving.getCanSpawnHere() && entityliving.isNotColliding());
    }
    
    public static boolean doSpecialSpawn(final EntityLiving entityliving, final World world, final float x, final int y, final float z) {
        return Reflector.ForgeEventFactory_doSpecialSpawn.exists() && Reflector.callBoolean(Reflector.ForgeEventFactory_doSpecialSpawn, entityliving, world, x, y, z);
    }
}
