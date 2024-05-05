/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.AbstractClientPlayer
 *  net.minecraft.client.resources.DefaultPlayerSkin
 *  net.minecraft.init.Items
 *  net.minecraft.util.ResourceLocation
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package net.ccbluex.liquidbounce.injection.forge.mixins.entity;

import java.util.Objects;
import java.util.UUID;
import me.report.liquidware.modules.render.Cape;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.cape.CapeInfo;
import net.ccbluex.liquidbounce.features.module.modules.fun.NameProtect;
import net.ccbluex.liquidbounce.features.module.modules.render.NoFOV;
import net.ccbluex.liquidbounce.injection.forge.mixins.entity.MixinEntityPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.init.Items;
import net.minecraft.util.ResourceLocation;
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
    private CapeInfo capeInfo;

    @Inject(method={"getLocationCape"}, at={@At(value="HEAD")}, cancellable=true)
    private void getCape(CallbackInfoReturnable<ResourceLocation> callbackInfoReturnable) {
        Cape capeMod = (Cape)LiquidBounce.moduleManager.getModule(Cape.class);
        if (capeMod.getState() && Objects.equals(this.func_146103_bH().getName(), Minecraft.func_71410_x().field_71439_g.func_146103_bH().getName())) {
            callbackInfoReturnable.setReturnValue(capeMod.getCapeLocation((String)capeMod.getStyleValue().get()));
        }
    }

    @Inject(method={"getFovModifier"}, at={@At(value="HEAD")}, cancellable=true)
    private void getFovModifier(CallbackInfoReturnable<Float> callbackInfoReturnable) {
        NoFOV fovModule = (NoFOV)LiquidBounce.moduleManager.getModule(NoFOV.class);
        if (fovModule.getState()) {
            float newFOV = ((Float)fovModule.getFovValue().get()).floatValue();
            if (!this.func_71039_bw()) {
                callbackInfoReturnable.setReturnValue(Float.valueOf(newFOV));
                return;
            }
            if (this.func_71011_bu().func_77973_b() != Items.field_151031_f) {
                callbackInfoReturnable.setReturnValue(Float.valueOf(newFOV));
                return;
            }
            int i = this.func_71057_bx();
            float f1 = (float)i / 20.0f;
            f1 = f1 > 1.0f ? 1.0f : f1 * f1;
            callbackInfoReturnable.setReturnValue(Float.valueOf(newFOV *= 1.0f - f1 * 0.15f));
        }
    }

    @Inject(method={"getLocationSkin()Lnet/minecraft/util/ResourceLocation;"}, at={@At(value="HEAD")}, cancellable=true)
    private void getSkin(CallbackInfoReturnable<ResourceLocation> callbackInfoReturnable) {
        NameProtect nameProtect = (NameProtect)LiquidBounce.moduleManager.getModule(NameProtect.class);
        if (nameProtect.getState() && ((Boolean)nameProtect.skinProtectValue.get()).booleanValue()) {
            if (!((Boolean)nameProtect.allPlayersValue.get()).booleanValue() && !Objects.equals(this.func_146103_bH().getName(), Minecraft.func_71410_x().field_71439_g.func_146103_bH().getName())) {
                return;
            }
            callbackInfoReturnable.setReturnValue(DefaultPlayerSkin.func_177334_a((UUID)this.func_110124_au()));
        }
    }
}

