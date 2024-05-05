/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.features.module.modules.movement;

import net.ccbluex.liquidbounce.api.minecraft.client.settings.IKeyBinding;
import net.ccbluex.liquidbounce.event.ClickWindowEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.value.BoolValue;

@ModuleInfo(name="InventoryMove", description="Allows you to walk while an inventory is opened.", category=ModuleCategory.MOVEMENT)
public final class InventoryMove
extends Module {
    private final BoolValue undetectable = new BoolValue("Undetectable", false);
    private final BoolValue aacAdditionProValue = new BoolValue("AACAdditionPro", false);
    private final BoolValue noMoveClicksValue = new BoolValue("NoMoveClicks", false);
    private final IKeyBinding[] affectedBindings = new IKeyBinding[]{MinecraftInstance.mc.getGameSettings().getKeyBindForward(), MinecraftInstance.mc.getGameSettings().getKeyBindBack(), MinecraftInstance.mc.getGameSettings().getKeyBindRight(), MinecraftInstance.mc.getGameSettings().getKeyBindLeft(), MinecraftInstance.mc.getGameSettings().getKeyBindJump(), MinecraftInstance.mc.getGameSettings().getKeyBindSprint()};

    public final BoolValue getAacAdditionProValue() {
        return this.aacAdditionProValue;
    }

    @EventTarget
    public final void onUpdate(UpdateEvent event) {
        this.tick();
    }

    public final void tick() {
        if (!(MinecraftInstance.classProvider.isGuiChat(MinecraftInstance.mc.getCurrentScreen()) || MinecraftInstance.classProvider.isGuiIngameMenu(MinecraftInstance.mc.getCurrentScreen()) || ((Boolean)this.undetectable.get()).booleanValue() && MinecraftInstance.classProvider.isGuiContainer(MinecraftInstance.mc.getCurrentScreen()))) {
            for (IKeyBinding affectedBinding : this.affectedBindings) {
                affectedBinding.setPressed(MinecraftInstance.mc.getGameSettings().isKeyDown(affectedBinding));
            }
        }
    }

    @EventTarget
    public final void onClick(ClickWindowEvent event) {
        if (((Boolean)this.noMoveClicksValue.get()).booleanValue() && MovementUtils.isMoving()) {
            event.cancelEvent();
        }
    }

    @Override
    public void onDisable() {
        boolean isIngame = MinecraftInstance.mc.getCurrentScreen() != null;
        for (IKeyBinding affectedBinding : this.affectedBindings) {
            if (MinecraftInstance.mc.getGameSettings().isKeyDown(affectedBinding) && !isIngame) continue;
            affectedBinding.setPressed(false);
        }
    }

    @Override
    public String getTag() {
        return (Boolean)this.aacAdditionProValue.get() != false ? "AACAdditionPro" : null;
    }
}

