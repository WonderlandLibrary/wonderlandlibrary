package today.flux.module.implement.Render;

import org.lwjgl.input.Keyboard;
import today.flux.Flux;
import today.flux.gui.clickgui.classic.ClickGUI;
import today.flux.gui.clickgui.skeet.SkeetClickGUI;
import today.flux.module.Category;
import today.flux.module.Module;
import today.flux.module.value.ColorValue;
import today.flux.module.value.ModeValue;

import java.awt.*;

public class CGUI extends Module {
    public static ModeValue GuiMode = new ModeValue("ClickGUI", "Mode", "New", "New", "Classical", "Skeet");

    public CGUI() {
        super("ClickGUI", Category.Render, false);
        this.setBind(Keyboard.KEY_RSHIFT);
        this.setSilent(true);
    }

    public static ColorValue skeetGUiColours = new ColorValue("ClickGUI", "Skeet GUI", Color.RED);
    public static ColorValue cursorColours = new ColorValue("ClickGUI", "Cursor", Color.RED);

    @Override
    public void onEnable() {
        if(mc.currentScreen instanceof today.flux.gui.crink.NewClickGUI || mc.currentScreen instanceof ClickGUI) {
            this.disable();
            return;
        }

        if (CGUI.GuiMode.isCurrentMode("New")) {
            if (Flux.INSTANCE.getNewClickGUI() == null) {
            	Flux.INSTANCE.setNewClickGUI(new today.flux.gui.crink.NewClickGUI());
            }

            mc.displayGuiScreen(Flux.INSTANCE.getNewClickGUI());
        } else if (CGUI.GuiMode.isCurrentMode("Classical")) {

            if (Flux.INSTANCE.getClickGUI() == null) {
                Flux.INSTANCE.setClickGUI(new ClickGUI());
            }

            mc.displayGuiScreen(Flux.INSTANCE.getClickGUI());
        } else {
            if (Flux.INSTANCE.getSkeetClickGUI() == null) {
                Flux.INSTANCE.setSkeetClickGUI(new SkeetClickGUI());
            }

            mc.displayGuiScreen(Flux.INSTANCE.getSkeetClickGUI());
            SkeetClickGUI.alpha = 0.0;
            SkeetClickGUI.open = true;
            Flux.INSTANCE.getSkeetClickGUI().targetAlpha = 255.0;
            Flux.INSTANCE.getSkeetClickGUI().closed = false;
        }
        this.disable();
    }
}
