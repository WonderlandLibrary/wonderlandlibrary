package net.caffeinemc.phosphor.mixin;

import net.caffeinemc.phosphor.api.event.events.SlotCheckEvent;
import net.caffeinemc.phosphor.api.util.KeyUtils;
import net.caffeinemc.phosphor.common.Phosphor;
import net.caffeinemc.phosphor.module.modules.combat.ChestStealerModule;
import net.caffeinemc.phosphor.module.modules.combat.InventoryTotemModule;
import net.caffeinemc.phosphor.module.modules.combat.PotRefillModule;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(HandledScreen.class)
public abstract class HandledScreenMixin {
    @Shadow protected abstract boolean isPointOverSlot(Slot slot, double pointX, double pointY);

    @Redirect(method = "render",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screen/ingame/HandledScreen;isPointOverSlot(Lnet/minecraft/screen/slot/Slot;DD)Z"))
    private boolean onIsPointOverSlot(HandledScreen instance, Slot slot, double pointX, double pointY) {
        SlotCheckEvent event = Phosphor.EVENTBUS.post(SlotCheckEvent.get(instance, slot));
        if (event.isCancelled()) return event.cancelOutput;

        return this.isPointOverSlot(slot, pointX, pointY);
    }
    
    @ModifyArgs(method = "render", 
            at = @At(value = "INVOKE", 
                    target = "Lnet/minecraft/client/gui/screen/ingame/HandledScreen;isPointOverSlot(Lnet/minecraft/screen/slot/Slot;DD)Z"))
    private void modifyArg(Args args) {
        if ((HandledScreen) (Object) this instanceof InventoryScreen) {
            PotRefillModule potRefill = Phosphor.moduleManager().getModule(PotRefillModule.class);
            if (potRefill != null && potRefill.isEnabled() && potRefill.mode.is("Normal")) {
                if (KeyUtils.isKeyPressed(potRefill.activateKey.getKeyCode())) {
                    args.set(2, 0d);
                    args.set(3, 0d);
                    return;
                }
            }

            InventoryTotemModule inventoryTotem = Phosphor.moduleManager().getModule(InventoryTotemModule.class);
            if (inventoryTotem != null && inventoryTotem.isEnabled() && inventoryTotem.mode.is("Normal")) {
                if (!KeyUtils.isKeyPressed(inventoryTotem.activateKey.getKeyCode()) && inventoryTotem.workOnKey.isEnabled())
                    return;

                if (inventoryTotem.searchTotems()) {
                    args.set(2, 0d);
                    args.set(3, 0d);
                    return;
                }
            }
        } else if ((HandledScreen) (Object) this instanceof GenericContainerScreen) {
            ChestStealerModule chestStealer = Phosphor.moduleManager().getModule(ChestStealerModule.class);
            if (chestStealer != null && chestStealer.isEnabled() && chestStealer.mode.is("Normal")) {
                if (!KeyUtils.isKeyPressed(chestStealer.activateKey.getKeyCode()) && chestStealer.workOnKey.isEnabled())
                    return;

                if (chestStealer.hasEmptySlots()) {
                    args.set(2, 0d);
                    args.set(3, 0d);
                    return;
                }
            }
        }
    }
}
