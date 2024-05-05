/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.JvmField
 *  kotlin.jvm.internal.DefaultConstructorMarker
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.features.module.modules.movement;

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

    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }
}

