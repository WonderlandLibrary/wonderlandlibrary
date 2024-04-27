/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.BlockLiquid
 *  net.minecraft.util.Vec3
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package net.dev.important.injection.forge.mixins.block;

import net.dev.important.Client;
import net.dev.important.modules.module.modules.movement.NoSlow;
import net.minecraft.block.BlockLiquid;
import net.minecraft.util.Vec3;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SideOnly(value=Side.CLIENT)
@Mixin(value={BlockLiquid.class})
public class MixinBlockLiquid {
    @Inject(method={"modifyAcceleration"}, at={@At(value="HEAD")}, cancellable=true)
    private void onModifyAcceleration(CallbackInfoReturnable<Vec3> callbackInfoReturnable) {
        NoSlow noSlow = (NoSlow)Client.moduleManager.getModule(NoSlow.class);
        if (noSlow.getState() && ((Boolean)noSlow.getLiquidPushValue().get()).booleanValue()) {
            callbackInfoReturnable.setReturnValue(new Vec3(0.0, 0.0, 0.0));
        }
    }
}

