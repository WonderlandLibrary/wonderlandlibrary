package com.canon.majik;

import com.canon.majik.api.core.Initializer;
import com.canon.majik.api.utils.Globals;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(name = Globals.name, modid = Globals.modid, version = Globals.version)
public class Client {

    Initializer init = new Initializer();

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event){
        init.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event){
        init.init();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event){
        init.postInit();
    }

}
