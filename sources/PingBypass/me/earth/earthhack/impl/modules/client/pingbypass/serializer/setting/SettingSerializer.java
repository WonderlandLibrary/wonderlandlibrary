package me.earth.earthhack.impl.modules.client.pingbypass.serializer.setting;

import me.earth.earthhack.api.event.bus.SubscriberImpl;
import me.earth.earthhack.api.event.events.SettingEvent;
import me.earth.earthhack.api.module.Module;
import me.earth.earthhack.api.setting.Setting;
import me.earth.earthhack.api.util.Globals;
import me.earth.earthhack.impl.Earthhack;
import me.earth.earthhack.impl.modules.client.pingbypass.serializer.Serializer;
import me.earth.earthhack.impl.util.thread.ThreadUtil;
import net.minecraft.network.play.client.CPacketChatMessage;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;


public class SettingSerializer extends SubscriberImpl implements Globals, Serializer<Setting<?>>
{
    private final Set<Module> modules      = new HashSet<>();
    private final Set<Setting<?>> settings = new HashSet<>();
    private final Set<Setting<?>> changed  = new LinkedHashSet<>();

    public SettingSerializer(Module...modules)
    {
        init(modules);
        this.listeners.add(new SettingListener(this));
        this.listeners.add(new DisconnectListener(this));
        this.listeners.add(new TickListener(this));
    }

    private void init(Module...modules)
    {
        this.modules.addAll(Arrays.asList(modules));
        this.modules.forEach(module -> module.getSettings().forEach(setting ->
        {
            if (isSettingSerializable(setting))
            {
                settings.add(setting);
            }
        }));

        clear();
    }

    public void onSettingChange(SettingEvent<?> event)
    {
        Setting<?> setting = event.getSetting();
        if (settings.contains(setting))
        {
            ThreadUtil.scheduleNext(() -> changed.add(setting));
        }
    }

    protected void onTick()
    {
        if (mc.player != null && mc.getConnection() != null && !changed.isEmpty())
        {
            Setting<?> setting = pollSetting();
            if (setting != null)
            {
                serializeAndSend(setting);
            }
        }
    }

    public void clear()
    {
        synchronized (changed)
        {
            changed.clear();
            changed.addAll(settings);
        }
    }

    private Setting<?> pollSetting()
    {
        if (!changed.isEmpty())
        {
            Setting<?> setting = changed.iterator().next();
            changed.remove(setting);
            return setting;
        }

        return null;
    }

    @Override
    public void serializeAndSend(Setting<?> setting)
    {
        String command = "@Server" + setting.getContainer().getName() + " " + setting.getName() + " " + setting.getValue().toString();
        Earthhack.logger.info(command);
        CPacketChatMessage packet = new CPacketChatMessage(command);
        Objects.requireNonNull(mc.getConnection()).sendPacket(packet);
    }

    //TODO: Setting Blacklist instead.
    private boolean isSettingSerializable(Setting<?> setting)
    {
        return !setting.getName().equalsIgnoreCase("Bind")
                && !setting.getName().equalsIgnoreCase("Hidden")
                && !setting.getName().equalsIgnoreCase("Name")
                && !setting.getName().equalsIgnoreCase("IP")
                && !setting.getName().equalsIgnoreCase("Port")
                && !setting.getName().equalsIgnoreCase("Pings");
    }

}
