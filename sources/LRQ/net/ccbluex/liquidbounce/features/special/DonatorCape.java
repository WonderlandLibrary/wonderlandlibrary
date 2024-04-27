/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.concurrent.ThreadsKt
 *  kotlin.jvm.functions.Function0
 */
package net.ccbluex.liquidbounce.features.special;

import kotlin.concurrent.ThreadsKt;
import kotlin.jvm.functions.Function0;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Listenable;
import net.ccbluex.liquidbounce.event.SessionEvent;
import net.ccbluex.liquidbounce.features.special.DonatorCape;
import net.ccbluex.liquidbounce.ui.client.altmanager.sub.GuiDonatorCape;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.login.UserUtils;

public final class DonatorCape
extends MinecraftInstance
implements Listenable {
    @EventTarget
    public final void onSession(SessionEvent event) {
        block3: {
            block2: {
                if (!GuiDonatorCape.Companion.getCapeEnabled()) break block2;
                CharSequence charSequence = GuiDonatorCape.Companion.getTransferCode();
                boolean bl = false;
                if (!(charSequence.length() == 0) && UserUtils.INSTANCE.isValidTokenOffline(MinecraftInstance.mc.getSession().getToken())) break block3;
            }
            return;
        }
        ThreadsKt.thread$default((boolean)false, (boolean)false, null, null, (int)0, (Function0)onSession.1.INSTANCE, (int)31, null);
    }

    @Override
    public boolean handleEvents() {
        return true;
    }
}

