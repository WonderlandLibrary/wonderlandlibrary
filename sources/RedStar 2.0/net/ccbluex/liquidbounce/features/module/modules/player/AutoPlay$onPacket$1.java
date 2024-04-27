package net.ccbluex.liquidbounce.features.module.modules.player;

import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import me.sound.SoundPlayer;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=3, d1={"\u0000\b\n\u0000\n\n\u0000\u00000H\n¢\b"}, d2={"<anonymous>", "", "invoke"})
final class AutoPlay$onPacket$1
extends Lambda
implements Function0<Unit> {
    public static final AutoPlay$onPacket$1 INSTANCE = new /* invalid duplicate definition of identical inner class */;

    @Override
    public final void invoke() {
        new SoundPlayer().playSound(SoundPlayer.SoundType.VICTORY, LiquidBounce.INSTANCE.getModuleManager().getToggleVolume());
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        iEntityPlayerSP.sendChatMessage("/join");
    }

    AutoPlay$onPacket$1() {
    }
}
