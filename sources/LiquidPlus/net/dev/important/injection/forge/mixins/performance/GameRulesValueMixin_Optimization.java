/*
 * Decompiled with CFR 0.152.
 */
package net.dev.important.injection.forge.mixins.performance;

import java.util.Objects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets={"net.minecraft.world.GameRules$Value"})
public class GameRulesValueMixin_Optimization {
    @Shadow
    private String field_82762_a;

    @Inject(method={"setValue(Ljava/lang/String;)V"}, at={@At(value="HEAD")}, cancellable=true)
    private void patcher$cancelIfUnchanged(String value, CallbackInfo ci) {
        if (Objects.equals(this.field_82762_a, value)) {
            ci.cancel();
        }
    }
}

