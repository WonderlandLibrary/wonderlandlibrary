// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.reflect;

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

public class ReflectorForge
{
    public static boolean renderFirstPersonHand(final RenderGlobal renderGlobal, final float partialTicks, final int pass) {
        return false;
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
        return block.hasTileEntity();
    }
    
    public static boolean isItemDamaged(final ItemStack stack) {
        return stack.isItemDamaged();
    }
    
    public static boolean armorHasOverlay(final ItemArmor itemArmor, final ItemStack itemStack) {
        final int i = itemArmor.getColor(itemStack);
        return i != -1;
    }
    
    public static MapData getMapData(final ItemMap itemMap, final ItemStack stack, final World world) {
        return itemMap.getMapData(stack, world);
    }
    
    public static String[] getForgeModIds() {
        return new String[0];
    }
}
