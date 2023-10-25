package intent.AquaDev.aqua.modules.visual;

import de.Hero.settings.Setting;
import events.Event;
import events.listeners.EventPostRender2D;
import gui.clickgui.ownClickgui.ClickguiScreen;
import intent.AquaDev.aqua.Aqua;
import intent.AquaDev.aqua.gui.novoline.ClickguiScreenNovoline;
import intent.AquaDev.aqua.modules.Category;
import intent.AquaDev.aqua.modules.Module;
import net.minecraft.client.gui.GuiScreen;

public class GUI
extends Module {
    public GUI() {
        super("GUI", "GUI", 0, Category.Visual);
        Aqua.setmgr.register(new Setting("RoundedPicker", (Module)this, true));
        Aqua.setmgr.register(new Setting("ColorPickerGlow", (Module)this, true));
        Aqua.setmgr.register(new Setting("GlowMode", (Module)this, "Complete", new String[]{"Complete", "AmbientLighting"}));
        Aqua.setmgr.register(new Setting("BooleanMode", (Module)this, "Novoline", new String[]{"Novoline", "Rounded", "Square"}));
        Aqua.setmgr.register(new Setting("Mode", (Module)this, "Classic", new String[]{"Modern", "Classic", "Skeet"}));
    }

    public void onEnable() {
        super.onEnable();
    }

    public void onDisable() {
        super.onDisable();
    }

    public void onEvent(Event event) {
        if (event instanceof EventPostRender2D) {
            if (Aqua.setmgr.getSetting("GUIMode").getCurrentMode().equalsIgnoreCase("Modern")) {
                mc.displayGuiScreen((GuiScreen)new ClickguiScreen(null));
                this.setState(false);
            }
            if (Aqua.setmgr.getSetting("GUIMode").getCurrentMode().equalsIgnoreCase("Skeet")) {
                mc.displayGuiScreen((GuiScreen)new ClickguiScreenNovoline(null));
                this.setState(false);
            }
            if (Aqua.setmgr.getSetting("GUIMode").getCurrentMode().equalsIgnoreCase("Classic")) {
                mc.displayGuiScreen((GuiScreen)new ClickguiScreenNovoline(null));
                this.setState(false);
            }
        }
    }
}
