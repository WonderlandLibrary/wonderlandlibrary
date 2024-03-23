package dev.zprestige.prestige.api.mixin;

import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value={Mouse.class})
public interface IMouse {
    @Invoker(value="onMouseButton")
    public void handleMouseButton(long var1, int var3, int var4, int var5);
}
