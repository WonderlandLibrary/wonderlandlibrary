/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.PacketBuffer
 *  net.minecraft.util.IChatComponent
 *  net.minecraft.util.IChatComponent$Serializer
 */
package net.dev.important.injection.forge.mixins.bugfixes;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IChatComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={PacketBuffer.class})
public abstract class PacketBufferMixin_FixLog4jRCE {
    @Shadow
    public abstract String func_150789_c(int var1);

    @Inject(method={"readChatComponent"}, at={@At(value="HEAD")}, cancellable=true)
    public void readChatComponent(CallbackInfoReturnable<IChatComponent> cir) {
        String str = this.func_150789_c(Short.MAX_VALUE);
        int exploitIndex = str.indexOf("${");
        if (exploitIndex != -1 && str.lastIndexOf("}") > exploitIndex) {
            str = str.replaceAll("\\$\\{", "\\$\u0000{");
        }
        cir.setReturnValue(IChatComponent.Serializer.func_150699_a((String)str));
        cir.cancel();
    }
}

