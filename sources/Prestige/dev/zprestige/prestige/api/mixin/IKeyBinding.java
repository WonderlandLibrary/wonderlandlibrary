package dev.zprestige.prestige.api.mixin;

import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={KeyBinding.class})
public interface IKeyBinding {
    @Accessor(value="boundKey")
    public InputUtil.Key getBoundKey();
}
