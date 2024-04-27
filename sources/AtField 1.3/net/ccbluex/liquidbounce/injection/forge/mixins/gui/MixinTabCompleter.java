/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.TabCompleter
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.At$Shift
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package net.ccbluex.liquidbounce.injection.forge.mixins.gui;

import java.util.Comparator;
import java.util.List;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.minecraft.util.TabCompleter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={TabCompleter.class})
public abstract class MixinTabCompleter {
    @Shadow
    protected boolean field_186846_c;
    @Shadow
    protected List field_186849_f;
    @Shadow
    protected boolean field_186847_d;

    @Inject(method={"complete"}, at={@At(value="HEAD")})
    private void complete(CallbackInfo callbackInfo) {
        this.field_186849_f.sort(Comparator.comparing(MixinTabCompleter::lambda$complete$0));
    }

    @Inject(method={"requestCompletions"}, at={@At(value="HEAD")}, cancellable=true)
    private void handleClientCommandCompletion(String string, CallbackInfo callbackInfo) {
        if (LiquidBounce.commandManager.autoComplete(string)) {
            this.field_186847_d = true;
            String[] stringArray = LiquidBounce.commandManager.getLatestAutoComplete();
            if (string.toLowerCase().endsWith(stringArray[stringArray.length - 1].toLowerCase())) {
                return;
            }
            this.func_186840_a(stringArray);
            callbackInfo.cancel();
        }
    }

    @Shadow
    public abstract void func_186840_a(String ... var1);

    private static Boolean lambda$complete$0(String string) {
        return !LiquidBounce.fileManager.friendsConfig.isFriend(string);
    }

    @Inject(method={"setCompletions"}, at={@At(value="INVOKE", target="Lnet/minecraft/util/TabCompleter;complete()V", shift=At.Shift.BEFORE)}, cancellable=true)
    private void onAutocompleteResponse(String[] stringArray, CallbackInfo callbackInfo) {
        if (LiquidBounce.commandManager.getLatestAutoComplete().length != 0) {
            callbackInfo.cancel();
        }
    }
}

