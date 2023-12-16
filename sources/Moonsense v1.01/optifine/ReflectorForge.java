// 
// Decompiled by Procyon v0.5.36
// 

package optifine;

import net.minecraft.item.ItemStack;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;
import net.minecraft.util.ResourceLocation;

public class ReflectorForge
{
    public static void FMLClientHandler_trackBrokenTexture(final ResourceLocation loc, final String message) {
        if (!Reflector.FMLClientHandler_trackBrokenTexture.exists()) {
            final Object instance = Reflector.call(Reflector.FMLClientHandler_instance, new Object[0]);
            Reflector.call(instance, Reflector.FMLClientHandler_trackBrokenTexture, loc, message);
        }
    }
    
    public static void FMLClientHandler_trackMissingTexture(final ResourceLocation loc) {
        if (!Reflector.FMLClientHandler_trackMissingTexture.exists()) {
            final Object instance = Reflector.call(Reflector.FMLClientHandler_instance, new Object[0]);
            Reflector.call(instance, Reflector.FMLClientHandler_trackMissingTexture, loc);
        }
    }
    
    public static void putLaunchBlackboard(final String key, final Object value) {
        final Map blackboard = (Map)Reflector.getFieldValue(Reflector.Launch_blackboard);
        if (blackboard != null) {
            blackboard.put(key, value);
        }
    }
    
    public static InputStream getOptiFineResourceStream(String path) {
        if (!Reflector.OptiFineClassTransformer_instance.exists()) {
            return null;
        }
        final Object instance = Reflector.getFieldValue(Reflector.OptiFineClassTransformer_instance);
        if (instance == null) {
            return null;
        }
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        final byte[] bytes = (byte[])Reflector.call(instance, Reflector.OptiFineClassTransformer_getOptiFineResource, path);
        if (bytes == null) {
            return null;
        }
        final ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        return in;
    }
    
    public static boolean blockHasTileEntity(final IBlockState state) {
        final Block block = state.getBlock();
        return Reflector.ForgeBlock_hasTileEntity.exists() ? Reflector.callBoolean(block, Reflector.ForgeBlock_hasTileEntity, state) : block.hasTileEntity();
    }
    
    public static boolean isItemDamaged(final ItemStack stack) {
        return Reflector.ForgeItem_showDurabilityBar.exists() ? Reflector.callBoolean(stack.getItem(), Reflector.ForgeItem_showDurabilityBar, stack) : stack.isItemDamaged();
    }
}
