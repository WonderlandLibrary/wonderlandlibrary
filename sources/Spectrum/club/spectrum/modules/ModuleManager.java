package club.spectrum.modules;

import club.spectrum.Spectrum;
import club.spectrum.modules.Module;
import club.spectrum.modules.impl.movement.Sprint;
import club.spectrum.ui.HUD;
import net.minecraft.client.Minecraft;
import net.minecraft.util.IChatComponent;

import java.util.ArrayList;
import java.util.List;

/**
 * The module manager class.
 * Registers the modules, sets versioning, among other things
 *
 * @author v4n1ty
 * @since 27/09/2023
 */
public class ModuleManager {

    public static List<Module> modules = new ArrayList<Module>();

    public ModuleManager() {
        // Combat

        // Movement
        addModule(new Sprint());

        // Player

        // Visual

        // Exploit

        // Misc
    }

    public void addModule(Module Module) {
        this.modules.add(Module);
    }

    public List<Module> getModules() {
        return modules;
    }

    public Module getModulesByName(String ModuleName) {
        for (Module mod : modules) {
            if (mod.getName().trim().equalsIgnoreCase(ModuleName) || (mod.toString().trim().equalsIgnoreCase(ModuleName.trim()))) {
                return mod;
            }
        }
        return null;
    }

    public Module getModule(Class<? extends Module> clazz) {
        for (Module m : modules) {
            if (m.getClass() == clazz){
                return m;
            }
        }
        return null;
    }

    public static void onUpdate() {
        for (Module m : modules) {
            m.onUpdate();
        }
    }

    public static void onRender() {
        for (Module m : modules) {
            m.onRender();
        }
    }

    public static void onKey(int k) {
        for (Module m : modules) {
            if (m.getKey() == k) {
                m.toggle();
            }
        }
    }

    public static void addChatMessage(String message) {
        message = Spectrum.INSTANCE.Prefix + "§r" + " " + message;

        Minecraft.getMinecraft().thePlayer.addChatMessage(new IChatComponent(message));
    }
}
