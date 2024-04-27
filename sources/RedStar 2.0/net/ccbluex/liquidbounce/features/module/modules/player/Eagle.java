package net.ccbluex.liquidbounce.features.module.modules.player;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.enums.BlockType;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IWorldClient;
import net.ccbluex.liquidbounce.api.minecraft.client.settings.IKeyBinding;
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="Eagle", description="Makes you eagle (aka. FastBridge).", category=ModuleCategory.PLAYER)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\n\n\n\b\n\n\b\n\n\u0000\b\u000020B¢J\b0HJ020H¨\b"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/player/Eagle;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "onDisable", "", "onUpdate", "event", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "Pride"})
public final class Eagle
extends Module {
    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP thePlayer = iEntityPlayerSP;
        IKeyBinding iKeyBinding = MinecraftInstance.mc.getGameSettings().getKeyBindSneak();
        IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
        if (iWorldClient == null) {
            Intrinsics.throwNpe();
        }
        iKeyBinding.setPressed(Intrinsics.areEqual(iWorldClient.getBlockState(new WBlockPos(thePlayer.getPosX(), thePlayer.getPosY() - 1.0, thePlayer.getPosZ())).getBlock(), MinecraftInstance.classProvider.getBlockEnum(BlockType.AIR)));
    }

    @Override
    public void onDisable() {
        if (MinecraftInstance.mc.getThePlayer() == null) {
            return;
        }
        if (!MinecraftInstance.mc.getGameSettings().isKeyDown(MinecraftInstance.mc.getGameSettings().getKeyBindSneak())) {
            MinecraftInstance.mc.getGameSettings().getKeyBindSneak().setPressed(false);
        }
    }
}
