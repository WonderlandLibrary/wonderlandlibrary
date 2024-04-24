package Method.Client.utils.Factory;

import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.config.IConfigElement;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.util.List;

public class MethodConfig {
    static Property Opengui;
   public static boolean Guicheck;
    static Configuration config;

    public void preInit(FMLPreInitializationEvent event) {
        config = new Configuration(event.getSuggestedConfigurationFile());
        config.load();
        Opengui = config.get("managers", "Toggle Gui", false, "Opens gui if Right control is bound to something else");
        this.syncConfig();
    }

    public void syncConfig() {
        Guicheck = Opengui.getBoolean();
        if (config.hasChanged()) {
            config.save();
        }
    }

    public static String getString() {
        return config.toString();
    }

    public static List<IConfigElement> getConfigElements() {
        return (new ConfigElement(config.getCategory("managers"))).getChildElements();
    }

}
