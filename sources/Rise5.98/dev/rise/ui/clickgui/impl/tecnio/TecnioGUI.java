package dev.rise.ui.clickgui.impl.tecnio;

import dev.rise.Rise;
import dev.rise.module.enums.Category;
import dev.rise.ui.clickgui.ClickGUIType;
import dev.rise.ui.clickgui.impl.tecnio.elements.Panel;
import dev.rise.ui.clickgui.impl.tecnio.elements.buttontype.Button;
import dev.rise.ui.clickgui.impl.tecnio.elements.buttontype.ModuleButton;
import net.minecraft.client.gui.GuiScreen;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This is a dropdown clickgui that is designed to be vibrant colorful and sexy.
 *
 * @author Tecnio
 * @since 01/04/2021
 */
public final class TecnioGUI extends GuiScreen implements ClickGUIType {

    // We store our panels here in order to quickly access them and keep the settings.
    private final List<Panel> panels = new ArrayList<>();

    /**
     * Put ever panel into a category.
     */
    public TecnioGUI() {
        int count = 0;

        for (final Category category : Category.values()) {
            panels.add(new Panel(count, 4, 100, 18, category, new Color(0xffE64D3A)));
            count += 120;
        }
    }

    /**
     * When the the gui is started this gets called
     * and we can enable our background blur here.
     */
    @Override
    public void initGui() {

    }

    /**
     * Call all of the draw methods on the gui.
     * This is done for the sake of cleaner code.
     */
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        for (final Panel panel : panels) {
            panel.drawPanel(mouseX, mouseY);
        }
    }

    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        for (final Panel panel : panels) {
            panel.mouseAction(mouseX, mouseY, true, mouseButton);

            if (panel.isOpen()) {
                for (final ModuleButton moduleButton : panel.getModuleButtons()) {
                    moduleButton.mouseAction(mouseX, mouseY, true, mouseButton);

                    if (moduleButton.isExtended()) {
                        for (final Button pan : moduleButton.getButtons()) {
                            pan.mouseAction(mouseX, mouseY, true, mouseButton);
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void mouseReleased(final int mouseX, final int mouseY, final int state) {
        for (final Panel catPan : panels) {
            catPan.mouseAction(mouseX, mouseY, false, state);

            if (catPan.isOpen()) {
                for (final ModuleButton moduleButton : catPan.getModuleButtons()) {
                    moduleButton.mouseAction(mouseX, mouseY, false, state);

                    if (moduleButton.isExtended()) {
                        for (final Button pan : moduleButton.getButtons()) {
                            pan.mouseAction(mouseX, mouseY, false, state);
                        }
                    }
                }
            }
        }
    }

    /**
     * Called when the user closes the gui.
     * We can remove the blur here.
     */
    @Override
    public void onGuiClosed() {
        Objects.requireNonNull(Rise.INSTANCE.getModuleManager().getModule("ClickGui")).toggleModule();
    }
}
