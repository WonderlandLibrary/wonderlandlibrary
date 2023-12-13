/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.module.impl.combat;

import org.greenrobot.eventbus.Subscribe;
import tk.rektsky.event.impl.PacketSentEvent;
import tk.rektsky.module.Category;
import tk.rektsky.module.Module;

public class Criticals
extends Module {
    public Criticals() {
        super("Criticals", "Makes it so you have critical hits", Category.COMBAT);
    }

    @Subscribe
    public void onSendPacket(PacketSentEvent event) {
    }
}

