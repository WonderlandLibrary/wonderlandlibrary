
package client.module.impl.other;

import client.Client;
import client.event.EventLink;
import client.event.Listener;
import client.event.impl.other.MoveEvent;
import client.module.Category;
import client.module.DevelopmentFeature;
import client.module.Module;
import client.module.ModuleInfo;
import client.util.ChatUtil;
import client.util.MoveUtil;
import client.util.liquidbounce.RandomUtils;
import client.util.liquidbounce.TimeUtils;

@DevelopmentFeature
@ModuleInfo(name = "Test", description = "(Dev) Testing module for " + Client.NAME + " Developers", category = Category.OTHER)
public class Test extends Module {
    @EventLink
    public final Listener<MoveEvent> onMove = event -> {
        if (MoveUtil.isMoving()) {
            if (mc.thePlayer.onGround) {
                MoveUtil.setSpeed(0.38);
            }
        }
    };
}
