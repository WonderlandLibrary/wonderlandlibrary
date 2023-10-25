package space.clowdy;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import space.clowdy.modules.ModuleManager;
import space.clowdy.ui.ModuleScreen;
import space.clowdy.utils.KeyboardHandler;

@Mod("pvpgood")
public class Client {
     public static ModuleScreen clickGui;
     public static Client instance;
     private static final Logger logger = LogManager.getLogger();

     private void init(FMLCommonSetupEvent fMLCommonSetupEvent) {
          instance = this;
          ModuleManager.addModules();
          clickGui = new ModuleScreen();
     }

     public Client() {
          FMLJavaModLoadingContext.get().getModEventBus().addListener(this::init);
          MinecraftForge.EVENT_BUS.register(this);
          MinecraftForge.EVENT_BUS.register(new KeyboardHandler());
     }
}
