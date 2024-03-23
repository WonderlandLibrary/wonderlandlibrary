package dev.zprestige.prestige.api.mixin;

import java.util.Map;
import net.minecraft.client.gl.ShaderProgram;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={ShaderProgram.class})
public interface IShaderProgram {
    @Accessor(value="loadedUniforms")
    Map getUniformsHook();
}
