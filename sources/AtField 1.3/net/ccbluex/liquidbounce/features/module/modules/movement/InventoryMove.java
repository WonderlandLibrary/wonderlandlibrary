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
    private final IKeyBinding[] affectedBindings;
    private final BoolValue aacAdditionProValue;
    private final BoolValue noMoveClicksValue;
    private final BoolValue undetectable = new BoolValue("Undetectable", false);

    public InventoryMove() {
        this.aacAdditionProValue = new BoolValue("AACAdditionPro", false);
        this.noMoveClicksValue = new BoolValue("NoMoveClicks", false);
        this.affectedBindings = new IKeyBinding[]{MinecraftInstance.mc.getGameSettings().getKeyBindForward(), MinecraftInstance.mc.getGameSettings().getKeyBindBack(), MinecraftInstance.mc.getGameSettings().getKeyBindRight(), MinecraftInstance.mc.getGameSettings().getKeyBindLeft(), MinecraftInstance.mc.getGameSettings().getKeyBindJump(), MinecraftInstance.mc.getGameSettings().getKeyBindSprint()};
    }

    @Override
    public String getTag() {
        return (Boolean)this.aacAdditionProValue.get() != false ? "AACAdditionPro" : null;
    }

    @EventTarget
    public final void onClick(ClickWindowEvent clickWindowEvent) {
        if (((Boolean)this.noMoveClicksValue.get()).booleanValue() && MovementUtils.isMoving()) {
            clickWindowEvent.cancelEvent();
        }
    }

    public final BoolValue getAacAdditionProValue() {
        return this.aacAdditionProValue;
    }

    @EventTarget
    public final void onUpdate(UpdateEvent updateEvent) {
        this.tick();
    }

    public final void tick() {
        if (!(MinecraftInstance.classProvider.isGuiChat(MinecraftInstance.mc.getCurrentScreen()) || MinecraftInstance.classProvider.isGuiIngameMenu(MinecraftInstance.mc.getCurrentScreen()) || ((Boolean)this.undetectable.get()).booleanValue() && MinecraftInstance.classProvider.isGuiContainer(MinecraftInstance.mc.getCurrentScreen()))) {
            for (IKeyBinding iKeyBinding : this.affectedBindings) {
                iKeyBinding.setPressed(MinecraftInstance.mc.getGameSettings().isKeyDown(iKeyBinding));
            }
        }
    }

    @Override
    public void onDisable() {
        boolean bl = MinecraftInstance.mc.getCurrentScreen() != null;
        for (IKeyBinding iKeyBinding : this.affectedBindings) {
            if (MinecraftInstance.mc.getGameSettings().isKeyDown(iKeyBinding) && !bl) continue;
            iKeyBinding.setPressed(false);
        }
    }
}

