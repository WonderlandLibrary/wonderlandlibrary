/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.event.HoverEvent
 *  net.minecraft.util.ChatStyle
 *  net.minecraft.util.IChatComponent
 */
package net.dev.important.injection.forge.mixins.performance;

import net.minecraft.entity.Entity;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.IChatComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={Entity.class})
public abstract class EntityMixin_DisplayNames {
    private long patcher$displayNameCachedAt;
    private IChatComponent patcher$cachedDisplayName;

    @Shadow
    protected abstract HoverEvent func_174823_aP();

    @Inject(method={"getDisplayName"}, at={@At(value="RETURN")})
    protected void patcher$cacheDisplayName(CallbackInfoReturnable<IChatComponent> cir) {
        this.patcher$cachedDisplayName = cir.getReturnValue();
        this.patcher$displayNameCachedAt = System.currentTimeMillis();
    }

    @Inject(method={"getDisplayName"}, at={@At(value="HEAD")}, cancellable=true)
    protected void patcher$returnCachedDisplayName(CallbackInfoReturnable<IChatComponent> cir) {
        if (System.currentTimeMillis() - this.patcher$displayNameCachedAt < 50L) {
            cir.setReturnValue(this.patcher$cachedDisplayName);
        }
    }

    @Redirect(method={"getDisplayName"}, at=@At(value="INVOKE", target="Lnet/minecraft/entity/Entity;getHoverEvent()Lnet/minecraft/event/HoverEvent;"))
    private HoverEvent patcher$doNotGetHoverEvent(Entity instance) {
        return null;
    }

    @Redirect(method={"getDisplayName"}, at=@At(value="INVOKE", target="Lnet/minecraft/util/ChatStyle;setChatHoverEvent(Lnet/minecraft/event/HoverEvent;)Lnet/minecraft/util/ChatStyle;"))
    private ChatStyle patcher$doNotSetHoverEvent(ChatStyle instance, HoverEvent event) {
        return null;
    }
}

