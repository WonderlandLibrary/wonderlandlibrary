// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.mixin.mixins;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import com.elementars.eclient.module.render.OutlineESP;
import com.elementars.eclient.Xulu;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.client.renderer.entity.Render;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.entity.Entity;

@Mixin({ Render.class })
public abstract class MixinRenderer<T extends Entity>
{
    @Shadow
    protected abstract boolean bindEntityTexture(final T p0);
    
    @Redirect(method = { "doRender" }, at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/entity/Render;renderOutlines:Z"))
    public boolean doRender(final Render render) {
        return Xulu.MODULE_MANAGER.getModuleByName("OutlineESP").isToggled() && OutlineESP.krOE;
    }
}
