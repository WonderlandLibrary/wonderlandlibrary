package com.ohare.client.module.modules.combat;

import com.ohare.client.event.events.player.UpdateEvent;
import com.ohare.client.module.Module;
import com.ohare.client.utils.value.impl.EnumValue;
import com.sun.xml.internal.ws.util.StringUtils;
import dorkbox.messageBus.annotations.Subscribe;

import java.awt.*;

public class Criticals extends Module {
    public EnumValue<Mode> mode = new EnumValue("Mode", Mode.PACKETS);

    public Criticals() {
        super("Criticals", Category.COMBAT, new Color(120, 120, 150, 255).getRGB());
        addValues(mode);
        setDescription("Make Criticals Hit");
        setRenderlabel("Criticals");
    }

    public enum Mode {
        PACKETS, MINI, DEV, DEV2
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        if (mc.thePlayer == null) return;
        setSuffix(StringUtils.capitalize(mode.getValue().name().toLowerCase()));
    }
}
