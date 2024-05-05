package me.earth.earthhack.impl.services.client;

import me.earth.earthhack.api.module.Module;
import me.earth.earthhack.impl.modules.client.commands.Commands;
import me.earth.earthhack.impl.modules.client.fakeplayer.FakePlayer;
import me.earth.earthhack.impl.modules.client.mcf.MiddleClickFriends;
import me.earth.earthhack.impl.modules.client.pingbypass.PingBypass;
import me.earth.earthhack.impl.modules.client.pingbypass.submodules.sSafety.ServerSafety;
import me.earth.earthhack.impl.modules.client.pingbypass.submodules.ssurround.ServerSurround;
import me.earth.earthhack.impl.modules.client.pingbypass.submodules.sautocrystal.ServerAutoCrystal;
import me.earth.earthhack.impl.modules.client.pingbypass.submodules.sautototem.ServerAutoTotem;

import java.util.ArrayList;
import java.util.List;

public class ModuleManager
{
    private static final ModuleManager INSTANCE = new ModuleManager();

    /** Best iteration speed, and getting by name is only required for commands. */
    private final List<Module> modules = new ArrayList<>();

    private ModuleManager() { /* This is a Singleton. */ }

    public static ModuleManager getInstance()
    {
        return INSTANCE;
    }

    public void init()
    {
        register(MiddleClickFriends.getInstance());
        register(Commands.getInstance());
        register(FakePlayer.getInstance());
        register(ServerAutoCrystal.getInstance());
        register(ServerAutoTotem.getInstance());
        register(ServerSafety.getInstance());
        register(PingBypass.getInstance());
        register(ServerSurround.getInstance());
    }

    public void load()
    {
        for (Module module : modules)
        {
            module.load();
        }
    }

    public void register(Module module)
    {
        if (module != null)
        {
            modules.add(module);
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends Module> T getModule(String name)
    {
        for (Module module : modules)
        {
            if (module.getName().equalsIgnoreCase(name))
            {
                return (T) module;
            }
        }

        return null;
    }

    public List<Module> getModules()
    {
        return modules;
    }

}
