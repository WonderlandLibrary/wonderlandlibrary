package client.module.impl.player.nofall;

import client.event.EventLink;
import client.event.Listener;
import client.event.impl.other.MotionEvent;
import client.module.impl.player.NoFall;
import client.value.Mode;

public class VanillaNoFall extends Mode<NoFall> {

    public VanillaNoFall(final String name, final NoFall parent) {
        super(name, parent);
    }

    @EventLink
    public final Listener<MotionEvent> onMotion = event -> {
/*        final KillAura killAura = Client.INSTANCE.getModuleManager().get(KillAura.class);
        if (!killAura.isEnabled() || Client.INSTANCE.getModuleManager().get(KillAura.class).getEntity() == null) */event.setOnGround(true);
    };
}
