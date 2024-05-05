package me.earth.earthhack.impl.modules.client.pingbypass;

import me.earth.earthhack.api.event.bus.instance.Bus;
import me.earth.earthhack.api.module.Category;
import me.earth.earthhack.api.module.Module;
import me.earth.earthhack.api.setting.Setting;
import me.earth.earthhack.api.setting.settings.BooleanSetting;
import me.earth.earthhack.api.setting.settings.NumberSetting;
import me.earth.earthhack.api.setting.settings.StringSetting;
import me.earth.earthhack.impl.modules.client.fakeplayer.FakePlayer;
import me.earth.earthhack.impl.modules.client.pingbypass.serializer.friend.FriendSerializer;
import me.earth.earthhack.impl.modules.client.pingbypass.serializer.setting.SettingSerializer;
import me.earth.earthhack.impl.modules.client.pingbypass.submodules.sSafety.ServerSafety;
import me.earth.earthhack.impl.modules.client.pingbypass.submodules.ssurround.ServerSurround;
import me.earth.earthhack.impl.modules.client.pingbypass.submodules.sautocrystal.ServerAutoCrystal;
import me.earth.earthhack.impl.modules.client.pingbypass.submodules.sautototem.ServerAutoTotem;
import me.earth.earthhack.impl.modules.client.pingbypass.submodules.sinventory.ServerInventory;
import me.earth.earthhack.impl.util.math.StopWatch;
import me.earth.earthhack.impl.util.network.ServerUtil;

@SuppressWarnings("unused")
public class PingBypass extends Module
{
    private static final PingBypass INSTANCE = new PingBypass();

    final Setting<String> ip       = register(new StringSetting("IP", "Proxy-IP"));
    final Setting<String> port     = register(new StringSetting("Port", "0"));
    final Setting<Boolean> noRender = register(new BooleanSetting("NoRender", false));
    final Setting<Integer> pings   = register(new NumberSetting<>("Pings", 5000, 500, 10000));

    SettingSerializer serializer;
    FriendSerializer friendSerializer;

    StopWatch timer = new StopWatch();
    long startTime;
    int serverPing;
    long ping;
    boolean handled;

    private PingBypass()
    {
        super("PingBypass", Category.Client);
        this.listeners.add(new TickListener(this));
        this.listeners.add(new KeepAliveListener(this));
        this.listeners.add(new LoginListener(this));
    }

    public static PingBypass getInstance()
    {
        return INSTANCE;
    }

    @Override
    protected void onLoad()
    {
        serializer = new SettingSerializer(this, ServerAutoTotem.getInstance(), ServerAutoCrystal.getInstance(), FakePlayer.getInstance(), ServerSafety.getInstance(), ServerInventory.getInstance(), ServerSurround.getInstance());
        this.listeners.addAll(serializer.getListeners());
        friendSerializer = new FriendSerializer();
        this.listeners.addAll(friendSerializer.getListeners());

        if (Bus.EVENT_BUS.isSubscribed(this))
        {
            Bus.EVENT_BUS.unsubscribe(this);
            Bus.EVENT_BUS.subscribe(this);
        }
    }

    @Override
    protected void onEnable()
    {
        ServerUtil.disconnectFromMC();
        serializer.clear();
        friendSerializer.clear();
    }

    @Override
    protected void onDisable()
    {
        ServerUtil.disconnectFromMC();
        serializer.clear();
        friendSerializer.clear();
    }

    @Override
    public String getDisplayInfo()
    {
        return ping + "ms";
    }

    public long getPing()
    {
        return ping;
    }

    public int getServerPing()
    {
        return serverPing;
    }

    public String getIp()
    {
        return ip.getValue();
    }

    public void setIp(String ip)
    {
        this.ip.setValue(ip);
    }

    public void setPort(String port)
    {
        this.port.setValue(port);
    }

    public String getPortAsString()
    {
        return port.getValue();
    }

    public int getPort()
    {
        try
        {
            return Integer.parseInt(port.getValue());
        }
        catch (NumberFormatException e)
        {
            e.printStackTrace();
        }

        return 0;
    }

}
