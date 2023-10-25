package org.dreamcore.client.feature.impl.misc;

import org.dreamcore.client.dreamcore;
import org.dreamcore.client.event.EventTarget;
import org.dreamcore.client.event.events.impl.packet.EventMessage;
import org.dreamcore.client.feature.Feature;
import org.dreamcore.client.feature.impl.Type;

public class ChatAppend extends Feature {

    public ChatAppend() {
        super("ChatAppend", "Дописывает название чита в сообщение", Type.Misc);
    }

    @EventTarget
    public void onChatMessage(EventMessage event) {
        if (event.getMessage().startsWith("/"))
            return;

        event.message = event.message + " | " + dreamcore.instance.name;
    }
}
