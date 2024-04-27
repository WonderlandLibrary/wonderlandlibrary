/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.command.CommandHandler
 */
package net.dev.important.injection.forge.mixins.bugfixes;

import java.util.Locale;
import net.minecraft.command.CommandHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(value={CommandHandler.class})
public class CommandHandlerMixin_CaseCommands {
    @ModifyArg(method={"executeCommand"}, at=@At(value="INVOKE", target="Ljava/util/Map;get(Ljava/lang/Object;)Ljava/lang/Object;", remap=false))
    private Object patcher$makeLowerCaseForGet(Object s) {
        if (s instanceof String) {
            return ((String)s).toLowerCase(Locale.ENGLISH);
        }
        return s;
    }

    @ModifyArg(method={"registerCommand"}, at=@At(value="INVOKE", target="Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", remap=false), index=0)
    private Object patcher$makeLowerCaseForPut(Object s) {
        if (s instanceof String) {
            return ((String)s).toLowerCase(Locale.ENGLISH);
        }
        return s;
    }
}

