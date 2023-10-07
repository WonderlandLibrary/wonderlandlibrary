package cc.novoline.gui.group;

import cc.novoline.gui.screen.alt.repository.Alt;
import net.minecraft.client.Minecraft;

import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * @author xDelsy
 */
public class GuiGroupPlayerBox extends GuiRoundedGroupWithLines<Alt> {

    /* methods */
    private final Predicate<Alt> shouldRender = alt -> alt != null && alt.getPlayer() != null;

    /* constructors */
    public GuiGroupPlayerBox(int xPosition, int yPosition, int width, int height, Supplier<Alt> supplier) {
        super("Alt Info", xPosition, yPosition, width, height, 15, supplier);
    }

    /* methods */
    @Override
    public void drawGroup(Minecraft mc, int mouseX, int mouseY) {
        superDrawGroup(mc, mouseX, mouseY);

        final Alt alt = this.supplier.get();

        if (this.shouldRender.test(alt)) {
            drawLines();
        }
    }

}
