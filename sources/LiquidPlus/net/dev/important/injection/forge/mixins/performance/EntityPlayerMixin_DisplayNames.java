/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.event.HoverEvent
 *  net.minecraft.util.ChatStyle
 *  net.minecraft.util.IChatComponent
 */
package net.dev.important.injection.forge.mixins.performance;

import net.dev.important.injection.forge.mixins.performance.EntityMixin_DisplayNames;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.IChatComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={EntityPlayer.class})
public abstract class EntityPlayerMixin_DisplayNames
extends EntityMixin_DisplayNames {
    @Inject(method={"getDisplayName"}, at={@At(value="RETURN")})
    private void patcher$cachePlayerDisplayName(CallbackInfoReturnable<IChatComponent> cir) {
        super.patcher$cacheDisplayName(cir);
    }

    @Inject(method={"getDisplayName"}, at={@At(value="HEAD")}, cancellable=true)
    private void patcher$returnCachedPlayerDisplayName(CallbackInfoReturnable<IChatComponent> cir) {
        super.patcher$returnCachedDisplayName(cir);
    }

    @Redirect(method={"getDisplayName"}, at=@At(value="INVOKE", target="Lnet/minecraft/entity/player/EntityPlayer;getHoverEvent()Lnet/minecraft/event/HoverEvent;"))
    private HoverEvent patcher$onlyGetHoverEventInSinglePlayer(EntityPlayer instance) {
        return Minecraft.func_71410_x().func_71387_A() ? ((EntityPlayerMixin_DisplayNames)instance).func_174823_aP() : null;
    }

    @Redirect(method={"getDisplayName"}, at=@At(value="INVOKE", target="Lnet/minecraft/util/ChatStyle;setChatHoverEvent(Lnet/minecraft/event/HoverEvent;)Lnet/minecraft/util/ChatStyle;"))
    private ChatStyle patcher$onlySetHoverEventInSinglePlayer(ChatStyle instance, HoverEvent event) {
        return Minecraft.func_71410_x().func_71387_A() ? instance.func_150209_a(event) : null;
    }
}

