package org.dreamcore.client.feature.impl.player;

import org.dreamcore.client.event.EventTarget;
import org.dreamcore.client.event.events.impl.player.EventWebSolid;
import org.dreamcore.client.feature.Feature;
import org.dreamcore.client.feature.impl.Type;

public class SolidWeb extends Feature {

    public SolidWeb() {
        super("SolidWeb", "Делает паутину полноценным блоком", Type.Misc);
    }

    @EventTarget
    public void onWebSolid(EventWebSolid event) {
        event.setCancelled(true);
    }
}
