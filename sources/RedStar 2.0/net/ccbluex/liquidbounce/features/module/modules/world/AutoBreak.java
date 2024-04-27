package net.ccbluex.liquidbounce.features.module.modules.world;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.enums.BlockType;
import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IWorldClient;
import net.ccbluex.liquidbounce.api.minecraft.client.settings.IKeyBinding;
import net.ccbluex.liquidbounce.api.minecraft.util.IMovingObjectPosition;
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="AutoBreak", description="Automatically breaks the block you are looking at.", category=ModuleCategory.WORLD)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\n\n\n\b\n\n\b\n\n\u0000\b\u000020B¢J\b0HJ020H¨\b"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/world/AutoBreak;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "onDisable", "", "onUpdate", "event", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "Pride"})
public final class AutoBreak
extends Module {
    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent event) {
        block8: {
            block7: {
                Intrinsics.checkParameterIsNotNull(event, "event");
                if (MinecraftInstance.mc.getObjectMouseOver() == null) break block7;
                IMovingObjectPosition iMovingObjectPosition = MinecraftInstance.mc.getObjectMouseOver();
                if (iMovingObjectPosition == null) {
                    Intrinsics.throwNpe();
                }
                if (iMovingObjectPosition.getBlockPos() != null && MinecraftInstance.mc.getTheWorld() != null) break block8;
            }
            return;
        }
        IKeyBinding iKeyBinding = MinecraftInstance.mc.getGameSettings().getKeyBindAttack();
        IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
        if (iWorldClient == null) {
            Intrinsics.throwNpe();
        }
        IMovingObjectPosition iMovingObjectPosition = MinecraftInstance.mc.getObjectMouseOver();
        if (iMovingObjectPosition == null) {
            Intrinsics.throwNpe();
        }
        WBlockPos wBlockPos = iMovingObjectPosition.getBlockPos();
        if (wBlockPos == null) {
            Intrinsics.throwNpe();
        }
        iKeyBinding.setPressed(Intrinsics.areEqual(iWorldClient.getBlockState(wBlockPos).getBlock(), MinecraftInstance.classProvider.getBlockEnum(BlockType.AIR)) ^ true);
    }

    @Override
    public void onDisable() {
        if (!MinecraftInstance.mc.getGameSettings().isKeyDown(MinecraftInstance.mc.getGameSettings().getKeyBindAttack())) {
            MinecraftInstance.mc.getGameSettings().getKeyBindAttack().setPressed(false);
        }
    }
}
