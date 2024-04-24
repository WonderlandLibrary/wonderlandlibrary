package me.offeex.bloomware.mixin.accessor;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.client.util.InputUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(InputUtil.Type.class)
public interface IInputUtil {
    @Accessor("map")
    Int2ObjectMap<InputUtil.Key> getMap();
}
