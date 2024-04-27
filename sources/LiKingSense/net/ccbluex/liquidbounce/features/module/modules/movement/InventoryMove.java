/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.internal.Intrinsics
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.features.module.modules.movement;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ModuleInfo(name="InventoryMove", description="Allows you to walk while an inventory is opened.", category=ModuleCategory.MOVEMENT)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000>\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0014H\u0007J\b\u0010\u0015\u001a\u00020\u0012H\u0016J\u0010\u0010\u0016\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0017H\u0007J\u0006\u0010\u0018\u001a\u00020\u0012R\u0011\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006R\u0016\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bX\u0082\u0004\u00a2\u0006\u0004\n\u0002\u0010\nR\u000e\u0010\u000b\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\f\u001a\u0004\u0018\u00010\r8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u000e\u0010\u000fR\u000e\u0010\u0010\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0019"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/movement/InventoryMove;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "aacAdditionProValue", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "getAacAdditionProValue", "()Lnet/ccbluex/liquidbounce/value/BoolValue;", "affectedBindings", "", "Lnet/ccbluex/liquidbounce/api/minecraft/client/settings/IKeyBinding;", "[Lnet/ccbluex/liquidbounce/api/minecraft/client/settings/IKeyBinding;", "noMoveClicksValue", "tag", "", "getTag", "()Ljava/lang/String;", "undetectable", "onClick", "", "event", "Lnet/ccbluex/liquidbounce/event/ClickWindowEvent;", "onDisable", "onUpdate", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "tick", "LiKingSense"})
public final class InventoryMove
extends Module {
    public final BoolValue undetectable = new BoolValue("Undetectable", false);
    @NotNull
    public final BoolValue aacAdditionProValue = new BoolValue("AACAdditionPro", false);
    public final BoolValue noMoveClicksValue = new BoolValue("NoMoveClicks", false);
    public final IKeyBinding[] affectedBindings = new IKeyBinding[]{MinecraftInstance.mc.getGameSettings().getKeyBindForward(), MinecraftInstance.mc.getGameSettings().getKeyBindBack(), MinecraftInstance.mc.getGameSettings().getKeyBindRight(), MinecraftInstance.mc.getGameSettings().getKeyBindLeft(), MinecraftInstance.mc.getGameSettings().getKeyBindJump(), MinecraftInstance.mc.getGameSettings().getKeyBindSprint()};

    @NotNull
    public final BoolValue getAacAdditionProValue() {
        return this.aacAdditionProValue;
    }

    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent event) {
        Intrinsics.checkParameterIsNotNull((Object)event, (String)"event");
        this.tick();
    }

    /*
     * WARNING - void declaration
     */
    public final void tick() {
        if (!(MinecraftInstance.classProvider.isGuiChat(MinecraftInstance.mc.getCurrentScreen()) || MinecraftInstance.classProvider.isGuiIngameMenu(MinecraftInstance.mc.getCurrentScreen()) || ((Boolean)this.undetectable.get()).booleanValue() && MinecraftInstance.classProvider.isGuiContainer(MinecraftInstance.mc.getCurrentScreen()))) {
            void var2_4;
            IKeyBinding[] iKeyBindingArray = this.affectedBindings;
            int n = iKeyBindingArray.length;
            while (var2_4 < n) {
                IKeyBinding affectedBinding = iKeyBindingArray[var2_4];
                affectedBinding.setPressed(MinecraftInstance.mc.getGameSettings().isKeyDown(affectedBinding));
                ++var2_4;
            }
        }
    }

    @EventTarget
    public final void onClick(@NotNull ClickWindowEvent event) {
        Intrinsics.checkParameterIsNotNull((Object)event, (String)"event");
        if (((Boolean)this.noMoveClicksValue.get()).booleanValue() && MovementUtils.isMoving()) {
            event.cancelEvent();
        }
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public void onDisable() {
        void var3_5;
        int isIngame = MinecraftInstance.mc.getCurrentScreen() != null ? 1 : 0;
        IKeyBinding[] iKeyBindingArray = this.affectedBindings;
        int n = iKeyBindingArray.length;
        while (var3_5 < n) {
            IKeyBinding affectedBinding = iKeyBindingArray[var3_5];
            if (!MinecraftInstance.mc.getGameSettings().isKeyDown(affectedBinding) || isIngame != 0) {
                affectedBinding.setPressed(false);
            }
            ++var3_5;
        }
    }

    @Override
    @Nullable
    public String getTag() {
        return "Vanilla";
    }
}

