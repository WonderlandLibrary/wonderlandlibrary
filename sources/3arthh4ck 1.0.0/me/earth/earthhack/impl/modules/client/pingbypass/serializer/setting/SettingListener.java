package me.earth.earthhack.impl.modules.client.pingbypass.serializer.setting;

import me.earth.earthhack.api.event.bus.EventListener;
import me.earth.earthhack.api.event.events.SettingEvent;

public class SettingListener extends EventListener<SettingEvent<?>>
{
    private final SettingSerializer serializer;

    protected SettingListener(SettingSerializer serializer)
    {
        super(SettingEvent.class);
        this.serializer = serializer;
    }

    @Override
    public void invoke(SettingEvent<?> event)
    {
        serializer.onSettingChange(event);
    }

}
