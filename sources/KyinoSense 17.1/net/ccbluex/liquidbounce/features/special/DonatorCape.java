/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.util.Session
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.features.special;

import kotlin.Metadata;
import kotlin.concurrent.ThreadsKt;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Listenable;
import net.ccbluex.liquidbounce.event.SessionEvent;
import net.ccbluex.liquidbounce.features.special.DonatorCape;
import net.ccbluex.liquidbounce.ui.client.altmanager.sub.GuiDonatorCape;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.login.UserUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

@SideOnly(value=Side.CLIENT)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u00012\u00020\u0002B\u0005\u00a2\u0006\u0002\u0010\u0003J\b\u0010\u0004\u001a\u00020\u0005H\u0016J\u0010\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\tH\u0007\u00a8\u0006\n"}, d2={"Lnet/ccbluex/liquidbounce/features/special/DonatorCape;", "Lnet/ccbluex/liquidbounce/event/Listenable;", "Lnet/ccbluex/liquidbounce/utils/MinecraftInstance;", "()V", "handleEvents", "", "onSession", "", "event", "Lnet/ccbluex/liquidbounce/event/SessionEvent;", "KyinoClient"})
public final class DonatorCape
extends MinecraftInstance
implements Listenable {
    @EventTarget
    public final void onSession(@NotNull SessionEvent event) {
        block3: {
            block2: {
                Intrinsics.checkParameterIsNotNull(event, "event");
                if (!GuiDonatorCape.Companion.getCapeEnabled()) break block2;
                CharSequence charSequence = GuiDonatorCape.Companion.getTransferCode();
                boolean bl = false;
                if (charSequence.length() == 0) break block2;
                Session session = DonatorCape.access$getMc$p$s1046033730().field_71449_j;
                Intrinsics.checkExpressionValueIsNotNull(session, "mc.session");
                String string = session.func_148254_d();
                Intrinsics.checkExpressionValueIsNotNull(string, "mc.session.token");
                if (UserUtils.INSTANCE.isValidTokenOffline(string)) break block3;
            }
            return;
        }
        ThreadsKt.thread$default(false, false, null, null, 0, onSession.1.INSTANCE, 31, null);
    }

    @Override
    public boolean handleEvents() {
        return true;
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }
}

