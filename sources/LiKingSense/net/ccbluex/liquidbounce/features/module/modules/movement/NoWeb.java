/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.TypeCastException
 *  kotlin.jvm.internal.Intrinsics
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.features.module.modules.movement;

import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.value.ListValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ModuleInfo(name="NoWeb", description="Prevents you from getting slowed down in webs.", category=ModuleCategory.MOVEMENT)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u0005\u001a\u0004\u0018\u00010\u00068VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0007\u0010\b\u00a8\u0006\r"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/movement/NoWeb;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "modeValue", "Lnet/ccbluex/liquidbounce/value/ListValue;", "tag", "", "getTag", "()Ljava/lang/String;", "onUpdate", "", "event", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "LiKingSense"})
public final class NoWeb
extends Module {
    public final ListValue modeValue = new ListValue("Mode", new String[]{"None", "AAC", "LAAC", "Rewi"}, "None");

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent event) {
        IEntityPlayerSP thePlayer;
        block10: {
            block11: {
                String string;
                Intrinsics.checkParameterIsNotNull((Object)event, (String)"event");
                IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP == null) {
                    return;
                }
                thePlayer = iEntityPlayerSP;
                if (!thePlayer.isInWeb()) {
                    return;
                }
                String string2 = string = (String)this.modeValue.get();
                if (string2 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                }
                String string3 = string2.toLowerCase();
                Intrinsics.checkExpressionValueIsNotNull((Object)string3, (String)"(this as java.lang.String).toLowerCase()");
                string = string3;
                switch (string.hashCode()) {
                    case 3497029: {
                        if (!string.equals("rewi")) return;
                        break block10;
                    }
                    case 96323: {
                        if (!string.equals("aac")) return;
                        break;
                    }
                    case 3313751: {
                        if (!string.equals("laac")) return;
                        break block11;
                    }
                    case 3387192: {
                        if (!string.equals("none")) return;
                        thePlayer.setInWeb(false);
                        return;
                    }
                }
                thePlayer.setJumpMovementFactor(0.59f);
                if (MinecraftInstance.mc.getGameSettings().getKeyBindSneak().isKeyDown()) return;
                thePlayer.setMotionY(0.0);
                return;
            }
            thePlayer.setJumpMovementFactor(thePlayer.getMovementInput().getMoveStrafe() != 0.0f ? 1.0f : 1.21f);
            if (!MinecraftInstance.mc.getGameSettings().getKeyBindSneak().isKeyDown()) {
                thePlayer.setMotionY(0.0);
            }
            if (!thePlayer.getOnGround()) return;
            thePlayer.jump();
            return;
        }
        thePlayer.setJumpMovementFactor(0.42f);
        if (!thePlayer.getOnGround()) return;
        thePlayer.jump();
        return;
    }

    @Override
    @Nullable
    public String getTag() {
        return (String)this.modeValue.get();
    }
}

