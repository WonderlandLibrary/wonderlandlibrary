package client.module.impl.other;

import client.event.EventLink;
import client.event.Listener;
import client.event.impl.other.TickEvent;
import client.module.Category;
import client.module.Module;
import client.module.ModuleInfo;
import client.value.impl.NumberValue;
import client.value.impl.StringValue;

@ModuleInfo(name = "Spammer", description = "", category = Category.OTHER)
public class Spammer extends Module {

    private final NumberValue delay = new NumberValue("Delay", this, 1, 0, 4, 0.1);
    private final StringValue message = new StringValue("Message", this, "");

    @EventLink
    public final Listener<TickEvent> onTick = event -> {
        if (mc.thePlayer.ticksExisted % (delay.getValue().doubleValue() * 20.0F) == 0) mc.thePlayer.sendChatMessage(message.getValue());
    };

}
