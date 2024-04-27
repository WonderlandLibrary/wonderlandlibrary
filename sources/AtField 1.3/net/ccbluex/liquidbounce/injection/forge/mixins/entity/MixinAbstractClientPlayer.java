/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.AbstractClientPlayer
 *  net.minecraft.client.resources.DefaultPlayerSkin
 *  net.minecraft.init.Items
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package net.ccbluex.liquidbounce.injection.forge.mixins.entity;

import java.util.Objects;
import java.util.UUID;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.modules.misc.NameProtect;
import net.ccbluex.liquidbounce.features.module.modules.render.NoFOV;
import net.ccbluex.liquidbounce.injection.forge.mixins.entity.MixinEntityPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.init.Items;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SideOnly(value=Side.CLIENT)
@Mixin(value={AbstractClientPlayer.class})
public abstract class MixinAbstractClientPlayer
extends MixinEntityPlayer {
    @Inject(method={"getFovModifier"}, at={@At(value="HEAD")}, cancellable=true)
    private void getFovModifier(CallbackInfoReturnable callbackInfoReturnable) {
        NoFOV noFOV = (NoFOV)LiquidBounce.moduleManager.getModule(NoFOV.class);
        if (Objects.requireNonNull(noFOV).getState()) {
            float f = ((Float)noFOV.getFovValue().get()).floatValue();
            if (!this.func_184587_cr()) {
                callbackInfoReturnable.setReturnValue((Object)Float.valueOf(f));
                return;
            }
            if (this.func_184607_cu().func_77973_b() != Items.field_151031_f) {
                callbackInfoReturnable.setReturnValue((Object)Float.valueOf(f));
                return;
            }
            int n = this.func_184605_cv();
            float f2 = (float)n / 20.0f;
            f2 = f2 > 1.0f ? 1.0f : f2 * f2;
            callbackInfoReturnable.setReturnValue((Object)Float.valueOf(f *= 1.0f - f2 * 0.15f));
        }
    }

    @Inject(method={"getLocationSkin()Lnet/minecraft/util/ResourceLocation;"}, at={@At(value="HEAD")}, cancellable=true)
    private void getSkin(CallbackInfoReturnable callbackInfoReturnable) {
        NameProtect nameProtect = (NameProtect)LiquidBounce.moduleManager.getModule(NameProtect.class);
        if (Objects.requireNonNull(nameProtect).getState() && ((Boolean)nameProtect.skinProtectValue.get()).booleanValue()) {
            if (!((Boolean)nameProtect.allPlayersValue.get()).booleanValue() && !Objects.equals(this.func_146103_bH().getName(), Minecraft.func_71410_x().field_71439_g.func_146103_bH().getName())) {
                return;
            }
            callbackInfoReturnable.setReturnValue((Object)DefaultPlayerSkin.func_177334_a((UUID)this.func_110124_au()));
        }
    }
}

