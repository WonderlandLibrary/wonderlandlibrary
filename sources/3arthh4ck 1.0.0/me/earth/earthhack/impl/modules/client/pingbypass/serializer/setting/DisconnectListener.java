package me.earth.earthhack.impl.modules.client.pingbypass.serializer.setting;

import me.earth.earthhack.api.event.bus.EventListener;
import me.earth.earthhack.impl.event.events.network.DisconnectEvent;

public class DisconnectListener extends EventListener<DisconnectEvent>
{
    private final SettingSerializer serializer;

    protected DisconnectListener(SettingSerializer serializer)
    {
        super(DisconnectEvent.class);
        this.serializer = serializer;
    }

    @Override
    public void invoke(DisconnectEvent event)
    {
        serializer.clear();
    }

}
