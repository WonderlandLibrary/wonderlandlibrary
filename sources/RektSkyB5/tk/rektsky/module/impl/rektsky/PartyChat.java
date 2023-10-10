/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.module.impl.rektsky;

import tk.rektsky.event.Event;
import tk.rektsky.event.impl.ChatEvent;
import tk.rektsky.module.Category;
import tk.rektsky.module.Module;

public class PartyChat
extends Module {
    public PartyChat() {
        super("PartyChat", "Add /p prefix before your message and make it send to party", Category.REKTSKY, false);
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof ChatEvent && !((ChatEvent)event).getMessage().startsWith("/") && !((ChatEvent)event).getMessage().startsWith(".")) {
            ((ChatEvent)event).setCanceled(true);
            this.mc.thePlayer.sendChatMessage("/p " + ((ChatEvent)event).getMessage());
        }
    }
}

