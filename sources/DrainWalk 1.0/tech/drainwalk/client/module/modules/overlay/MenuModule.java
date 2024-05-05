package tech.drainwalk.client.module.modules.overlay;

import org.lwjgl.input.Keyboard;
import tech.drainwalk.DrainWalk;
import tech.drainwalk.client.module.category.Category;
import tech.drainwalk.client.module.Module;
import tech.drainwalk.font.FontManager;

public class MenuModule extends Module {
    public MenuModule() {
        super("Menu", Category.OVERLAY);
        addKey(Keyboard.KEY_RSHIFT);
    }

    @Override
    public void onEnable() {
        mc.displayGuiScreen(DrainWalk.getInstance().getMenuMain());
        setEnabled(false);
    }
}
