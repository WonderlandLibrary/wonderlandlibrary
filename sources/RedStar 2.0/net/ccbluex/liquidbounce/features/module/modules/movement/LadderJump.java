package net.ccbluex.liquidbounce.features.module.modules.movement;

import kotlin.Metadata;
import kotlin.jvm.JvmField;
import kotlin.jvm.internal.DefaultConstructorMarker;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import org.jetbrains.annotations.Nullable;

@ModuleInfo(name="LadderJump", description="Boosts you up when touching a ladder.", category=ModuleCategory.MOVEMENT)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\n\n\n\b\n\n\u0000\n\n\b\b\u0000 20:B¢J02\b0H¨\b"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/movement/LadderJump;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "onUpdate", "", "event", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "Companion", "Pride"})
public final class LadderJump
extends Module {
    @JvmField
    public static boolean jumped;
    public static final Companion Companion;

    @EventTarget
    public final void onUpdate(@Nullable UpdateEvent event) {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP thePlayer = iEntityPlayerSP;
        if (thePlayer.getOnGround()) {
            if (thePlayer.isOnLadder()) {
                thePlayer.setMotionY(1.5);
                jumped = true;
            } else {
                jumped = false;
            }
        } else if (!thePlayer.isOnLadder() && jumped) {
            IEntityPlayerSP iEntityPlayerSP2 = thePlayer;
            iEntityPlayerSP2.setMotionY(iEntityPlayerSP2.getMotionY() + 0.059);
        }
    }

    static {
        Companion = new Companion(null);
    }

    @Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\n\n\u0000\n\b\n\n\u0000\b\u000020B\b¢R08@X¢\n\u0000¨"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/movement/LadderJump$Companion;", "", "()V", "jumped", "", "Pride"})
    public static final class Companion {
        private Companion() {
        }

        public Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }
}
