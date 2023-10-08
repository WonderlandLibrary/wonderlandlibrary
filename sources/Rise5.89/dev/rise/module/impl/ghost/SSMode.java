package dev.rise.module.impl.ghost;

import dev.rise.Rise;
import dev.rise.event.impl.motion.PreMotionEvent;
import dev.rise.module.Module;
import dev.rise.module.api.ModuleInfo;
import dev.rise.module.enums.Category;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.util.Tuple;
import org.lwjgl.opengl.Display;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@ModuleInfo(name = "SelfDestruct", description = "Makes Rise look like normal Minecraft when enabled", category = Category.GHOST)
public final class SSMode extends Module {

    @Override
    protected void onEnable() {
        Display.setTitle("Minecraft 1.8.9");
        mc.resetWindowIcon();
        Rise.INSTANCE.guiMainMenu = new GuiMainMenu();
        mc.displayGuiScreen(null);
        Rise.INSTANCE.destructed = true;
        this.setEnabled(false);
    }
}
