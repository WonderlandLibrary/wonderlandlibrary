package me.earth.earthhack.impl;

import me.earth.earthhack.api.event.bus.api.EventBus;
import me.earth.earthhack.api.event.bus.instance.Bus;
import me.earth.earthhack.api.util.Globals;
import me.earth.earthhack.impl.services.chat.CommandManager;
import me.earth.earthhack.impl.services.client.ModuleManager;
import me.earth.earthhack.impl.services.config.ConfigManager;
import me.earth.earthhack.impl.services.config.FileManager;
import me.earth.earthhack.impl.services.minecraft.EventManager;
import me.earth.earthhack.impl.services.thread.LookUpManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.Display;

@Mod(modid = Earthhack.MODID, name = Earthhack.NAME, version = Earthhack.VERSION)
public class Earthhack implements Globals
{
    public static final String MODID = "pingbypassclient";
    public static final String NAME = "3arthh4ck";
    public static final String VERSION = "1.0.0";
    public static final EventBus EVENT_BUS = Bus.EVENT_BUS;

    public static Logger logger;
    public static boolean running;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        running = true;
        logger.info("Initializing 3arthh4ck.");
        Display.setTitle(NAME + " - " + VERSION);

        FileManager.getInstance().init();
        ModuleManager.getInstance().init();
        EventManager.getInstance().init();
        LookUpManager.getInstance().init();
        CommandManager.getInstance().init();
        ConfigManager.getInstance().load();
        ModuleManager.getInstance().load();

        logger.info("3arthh4ck initialized.");
    }

}
