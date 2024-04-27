package net.ccbluex.liquidbounce.features.module.modules.player;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.MinecraftVersion;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemStack;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="AutoFish", description="Automatically catches fish when using a rod.", category=ModuleCategory.PLAYER, supportedVersions={MinecraftVersion.MC_1_8})
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\n\n\n\b\n\n\u0000\n\n\u0000\n\n\u0000\b\u000020B¢J020\bHR0X¢\n\u0000¨\t"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/player/AutoFish;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "rodOutTimer", "Lnet/ccbluex/liquidbounce/utils/timer/MSTimer;", "onUpdate", "", "event", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "Pride"})
public final class AutoFish
extends Module {
    private final MSTimer rodOutTimer = new MSTimer();

    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent event) {
        IEntityPlayerSP thePlayer;
        block6: {
            block5: {
                Intrinsics.checkParameterIsNotNull(event, "event");
                IEntityPlayerSP iEntityPlayerSP = thePlayer = MinecraftInstance.mc.getThePlayer();
                if ((iEntityPlayerSP != null ? iEntityPlayerSP.getHeldItem() : null) == null) break block5;
                IItemStack iItemStack = thePlayer.getHeldItem();
                if (iItemStack == null) {
                    Intrinsics.throwNpe();
                }
                if (MinecraftInstance.classProvider.isItemFishingRod(iItemStack.getItem())) break block6;
            }
            return;
        }
        IEntity fishEntity = thePlayer.getFishEntity();
        if (this.rodOutTimer.hasTimePassed(500L) && fishEntity == null || fishEntity != null && fishEntity.getMotionX() == 0.0 && fishEntity.getMotionZ() == 0.0 && fishEntity.getMotionY() != 0.0) {
            MinecraftInstance.mc.rightClickMouse();
            this.rodOutTimer.reset();
        }
    }
}
