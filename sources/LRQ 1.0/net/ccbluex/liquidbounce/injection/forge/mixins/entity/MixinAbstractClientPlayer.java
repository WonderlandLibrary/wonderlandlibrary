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
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package net.ccbluex.liquidbounce.injection.forge.mixins.entity;

import java.util.Objects;
import java.util.UUID;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.cape.CapeAPI;
import net.ccbluex.liquidbounce.cape.CapeInfo;
import net.ccbluex.liquidbounce.features.module.modules.misc.NameProtect;
import net.ccbluex.liquidbounce.features.module.modules.render.NoFOV;
import net.ccbluex.liquidbounce.injection.backend.ResourceLocationImplKt;
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
        if (!CapeAPI.INSTANCE.hasCapeService()) {
            return;
        }
        if (this.capeInfo == null) {
            this.capeInfo = CapeAPI.INSTANCE.loadCape(this.func_110124_au());
        }
        if (this.capeInfo != null && this.capeInfo.isCapeAvailable()) {
            callbackInfoReturnable.setReturnValue((Object)ResourceLocationImplKt.unwrap(this.capeInfo.getResourceLocation()));
        }
    }

    @Inject(method={"getFovModifier"}, at={@At(value="HEAD")}, cancellable=true)
    private void getFovModifier(CallbackInfoReturnable<Float> callbackInfoReturnable) {
        NoFOV fovModule = (NoFOV)LiquidBounce.moduleManager.getModule(NoFOV.class);
        if (Objects.requireNonNull(fovModule).getState()) {
            float newFOV = ((Float)fovModule.getFovValue().get()).floatValue();
            if (!this.func_184587_cr()) {
                callbackInfoReturnable.setReturnValue((Object)Float.valueOf(newFOV));
                return;
            }
            if (this.func_184607_cu().func_77973_b() != Items.field_151031_f) {
                callbackInfoReturnable.setReturnValue((Object)Float.valueOf(newFOV));
                return;
            }
            int i = this.func_184605_cv();
            float f1 = (float)i / 20.0f;
            f1 = f1 > 1.0f ? 1.0f : f1 * f1;
            callbackInfoReturnable.setReturnValue((Object)Float.valueOf(newFOV *= 1.0f - f1 * 0.15f));
        }
    }

    @Inject(method={"getLocationSkin()Lnet/minecraft/util/ResourceLocation;"}, at={@At(value="HEAD")}, cancellable=true)
    private void getSkin(CallbackInfoReturnable<ResourceLocation> callbackInfoReturnable) {
        NameProtect nameProtect = (NameProtect)LiquidBounce.moduleManager.getModule(NameProtect.class);
        if (Objects.requireNonNull(nameProtect).getState() && ((Boolean)nameProtect.skinProtectValue.get()).booleanValue()) {
            if (!((Boolean)nameProtect.allPlayersValue.get()).booleanValue() && !Objects.equals(this.func_146103_bH().getName(), Minecraft.func_71410_x().field_71439_g.func_146103_bH().getName())) {
                return;
            }
            callbackInfoReturnable.setReturnValue((Object)DefaultPlayerSkin.func_177334_a((UUID)this.func_110124_au()));
        }
    }
}

