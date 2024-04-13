package com.canon.majik.api.core;

import com.canon.majik.api.event.eventBus.EventBus;
import com.canon.majik.impl.hook.ConfigHook;
import com.canon.majik.impl.hook.Hooker;
import com.canon.majik.impl.hook.TitleHook;
import com.canon.majik.api.utils.Globals;
import com.canon.majik.api.utils.turok.render.font.TurokFont;
import net.minecraftforge.fml.common.ProgressManager;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class Initializer implements Globals {

    public static Logger logger;
    public static ModuleManager moduleManager;
    public static ForgeManager forgeManager;
    public static EventBus eventBus;
    public static CommandManager commandManager;
    public static RotationManager rotationManager;
    public static PlayerManager playerManager;
    private List<Hooker> hooks;
    ProgressManager progressManager = new ProgressManager();

    public static TurokFont CFont = new TurokFont(new Font("Verdana", 0, 16), true, true);

    public void preInit(FMLPreInitializationEvent event){
        logger = event.getModLog();
    }

    public void init(){
        hooks = Arrays.asList(
                new TitleHook(),
                new ConfigHook()
        );
        logger.info("Init");
        Thread exitThread = new Thread(() -> hooks.forEach(Hooker::unInit));
        Runtime.getRuntime().addShutdownHook(exitThread);
        moduleManager = new ModuleManager();
        progressManager.push("Init ModuleManager", 1);
        forgeManager = new ForgeManager();
        progressManager.push("Init ForgeManager", 2);
        eventBus = new EventBus();
        progressManager.push("Init EventBus", 3);
        commandManager = new CommandManager();
        progressManager.push("Init CommandManager", 4);
        rotationManager = new RotationManager();
        progressManager.push("Init RotationManager", 4);
        playerManager = new PlayerManager();
        progressManager.push("Init PlayerManager", 5);
        hooks.forEach(Hooker::init);
    }

    public void postInit(){

    }

}
