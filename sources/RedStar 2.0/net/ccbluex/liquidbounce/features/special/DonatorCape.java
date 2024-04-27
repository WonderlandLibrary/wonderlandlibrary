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
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\"\n\n\n\n\b\n\n\u0000\n\n\u0000\n\n\u0000\u00002020B¢J\b0HJ02\b0\tH¨\n"}, d2={"Lnet/ccbluex/liquidbounce/features/special/DonatorCape;", "Lnet/ccbluex/liquidbounce/event/Listenable;", "Lnet/ccbluex/liquidbounce/utils/MinecraftInstance;", "()V", "handleEvents", "", "onSession", "", "event", "Lnet/ccbluex/liquidbounce/event/SessionEvent;", "Pride"})
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
                if (!(charSequence.length() == 0) && UserUtils.INSTANCE.isValidTokenOffline(MinecraftInstance.mc.getSession().getToken())) break block3;
            }
            return;
        }
        ThreadsKt.thread$default(false, false, null, null, 0, onSession.1.INSTANCE, 31, null);
    }

    @Override
    public boolean handleEvents() {
        return true;
    }
}
