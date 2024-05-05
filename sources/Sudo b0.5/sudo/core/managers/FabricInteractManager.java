package sudo.core.managers;

import org.lwjgl.glfw.GLFW;
import sudo.Client;
import sudo.module.Mod;
import sudo.module.ModuleManager;
import sudo.module.client.ClickGuiMod;
import sudo.ui.screens.clickgui.ClickGUI;

import static sudo.utils.world.WorldUtils.mc;

public class FabricInteractManager {

    public void init(){
        Client.EventBus.register(this);
    }

    public void onKeyPress(int key, int action) {
        if (action == GLFW.GLFW_PRESS) {
            if (mc.currentScreen==null) {
                if (key==GLFW.GLFW_KEY_RIGHT_SHIFT || key== ModuleManager.INSTANCE.getModule(ClickGuiMod.class).getKey()) mc.setScreen(ClickGUI.INSTANCE);
                for (Mod module : ModuleManager.INSTANCE.getModules()) {
                    if (key==module.getKey()) module.toggle();
                }
            }
        }
    }

    public void onTick() {
        if (mc.player != null) {
            for (Mod module : ModuleManager.INSTANCE.getEnabledModules()) {
                module.onTick();
            }
        }
    }

    public void onRun(){
        if(mc.player != null){
            for(Mod module : ModuleManager.INSTANCE.getEnabledModules()){
                module.onRun();
            }
        }
    }
}
