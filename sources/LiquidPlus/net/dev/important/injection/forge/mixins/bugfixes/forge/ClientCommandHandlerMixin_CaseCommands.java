/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.command.ICommandSender
 *  net.minecraftforge.client.ClientCommandHandler
 */
package net.dev.important.injection.forge.mixins.bugfixes.forge;

import java.util.Locale;
import net.minecraft.command.ICommandSender;
import net.minecraftforge.client.ClientCommandHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={ClientCommandHandler.class})
public class ClientCommandHandlerMixin_CaseCommands {
    @ModifyArg(method={"executeCommand", "func_71556_a"}, at=@At(value="INVOKE", target="Ljava/util/Map;get(Ljava/lang/Object;)Ljava/lang/Object;", remap=false), remap=false)
    private Object patcher$makeLowerCaseForGet(Object s) {
        if (s instanceof String) {
            return ((String)s).toLowerCase(Locale.ENGLISH);
        }
        return s;
    }

    @Inject(method={"executeCommand", "func_71556_a"}, at={@At(value="HEAD")}, cancellable=true, remap=false)
    private void patcher$checkForSlash(ICommandSender sender, String message, CallbackInfoReturnable<Integer> cir) {
        if (!message.trim().startsWith("/")) {
            cir.setReturnValue(0);
        }
    }
}

