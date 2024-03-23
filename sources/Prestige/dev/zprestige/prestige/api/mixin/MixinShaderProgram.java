package dev.zprestige.prestige.api.mixin;

import dev.zprestige.prestige.client.shader.GlProgram;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import dev.zprestige.prestige.client.Prestige;
import net.minecraft.client.gl.ShaderProgram;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ ShaderProgram.class })
public class MixinShaderProgram {
    @ModifyArg(method = { "<init>" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Identifier;<init>(Ljava/lang/String;)V"), require = 0)
    String fixIdentifier(final String s) {
        if (Prestige.Companion.getSelfDestructed()) {
            return s;
        }
        if ((Object) this instanceof GlProgram.Shader) {
            final String[] split = s.split(":");
            if (split.length == 2) {
                if (split[0].startsWith("shaders/core/")) {
                    return split[0].replace("shaders/core", "").substring(1) + ":shaders/core/" + split[1];
                }
            }
            return s;
        }
        return s;
    }
}
