/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.multiplayer.PlayerControllerMP
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.item.ItemStack
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package net.ccbluex.liquidbounce.injection.forge.mixins.entity;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.AttackEvent;
import net.ccbluex.liquidbounce.event.ClickWindowEvent;
import net.ccbluex.liquidbounce.features.module.modules.fun.AbortBreaking;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SideOnly(value=Side.CLIENT)
@Mixin(value={PlayerControllerMP.class})
public class MixinPlayerControllerMP {
    @Inject(method={"attackEntity"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/multiplayer/PlayerControllerMP;syncCurrentPlayItem()V")})
    private void attackEntity(EntityPlayer entityPlayer, Entity targetEntity, CallbackInfo callbackInfo) {
        LiquidBounce.eventManager.callEvent(new AttackEvent(targetEntity));
    }

    @Inject(method={"getIsHittingBlock"}, at={@At(value="HEAD")}, cancellable=true)
    private void getIsHittingBlock(CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (LiquidBounce.moduleManager.getModule(AbortBreaking.class).getState()) {
            callbackInfoReturnable.setReturnValue(false);
        }
    }

    @Inject(method={"windowClick"}, at={@At(value="HEAD")}, cancellable=true)
    private void windowClick(int windowId, int slotId, int mouseButtonClicked, int mode, EntityPlayer playerIn, CallbackInfoReturnable<ItemStack> callbackInfo) {
        ClickWindowEvent event = new ClickWindowEvent(windowId, slotId, mouseButtonClicked, mode);
        LiquidBounce.eventManager.callEvent(event);
        if (event.isCancelled()) {
            callbackInfo.cancel();
        }
    }
}

