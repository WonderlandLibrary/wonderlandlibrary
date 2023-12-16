// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.features.modules.type.hud;

import moonsense.enums.ModuleCategory;
import java.util.List;
import net.minecraft.client.gui.GuiScreen;
import moonsense.utils.KeyBinding;
import moonsense.settings.Setting;
import moonsense.features.SCModule;

public class ScrollableTooltipsModule extends SCModule
{
    public static ScrollableTooltipsModule INSTANCE;
    private final Setting horizontalScrollingKey;
    private int scrollY;
    private boolean allowScrolling;
    private int scrollX;
    
    public ScrollableTooltipsModule() {
        super("Scrollable Tooltips", "Allows you to view item descriptions that go off of the screen.");
        this.scrollY = 0;
        this.scrollX = 0;
        ScrollableTooltipsModule.INSTANCE = this;
        this.horizontalScrollingKey = new Setting(this, "Horizontal Scrolling Key").setDefault(new KeyBinding(42));
    }
    
    public void drawHoveringText(final GuiScreen screen, final List textLines, final int x, final int y) {
    }
    
    @Override
    public ModuleCategory getCategory() {
        return ModuleCategory.HUD;
    }
}
