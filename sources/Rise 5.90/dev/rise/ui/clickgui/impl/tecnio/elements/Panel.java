package dev.rise.ui.clickgui.impl.tecnio.elements;

import dev.rise.Rise;
import dev.rise.font.CustomFont;
import dev.rise.module.Module;
import dev.rise.module.enums.Category;
import dev.rise.ui.clickgui.impl.tecnio.elements.buttontype.Button;
import dev.rise.ui.clickgui.impl.tecnio.elements.buttontype.ModuleButton;
import dev.rise.util.render.RenderUtil;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.util.ArrayList;

@Getter
@Setter
public final class Panel extends Button {
    private Category category;
    private Color color;

    private boolean dragged, open;
    private int mouseX2, mouseY2;

    private float count;

    public ArrayList<ModuleButton> moduleButtons = new ArrayList<>();

    public Panel(final float x, final float y, final float width, final float height, final Category cat, final Color color) {
        super(x, y, width, height);

        category = cat;
        this.color = color;

        int count = 0;

        final float startY = y + height;

        for (final Module mod : Rise.INSTANCE.getModuleManager().getModuleList()) {
            if (mod.getModuleInfo().category() == category) {
                moduleButtons.add(new ModuleButton(x, startY + height * count, width, height, mod, color));
                ++count;
            }
        }
    }

    @Override
    public void drawPanel(final int mouseX, final int mouseY) {
        if (dragged) {
            this.setX(mouseX2 + mouseX);
            this.setY(mouseY2 + mouseY);
        }

        RenderUtil.rect(this.getX(), this.getY(), this.getWidth(), this.getHeight(), new Color(0xff181A17));
        CustomFont.drawCenteredString(StringUtils.capitalize(category.name().toLowerCase()), this.getX() + (getWidth() / 2.0F), this.getY() + this.getHeight() / 2 - (CustomFont.getHeight() / 2.0F), 0xffffffff);

        count = 0;

        if (open) {
            final float startY = this.getY() + this.getHeight();

            for (final ModuleButton modulePanel : moduleButtons) {
                modulePanel.setX(this.getX());
                modulePanel.setY(startY + count);

                modulePanel.drawPanel(mouseX, mouseY);

                count += modulePanel.getFinalHeight();
            }
        }

        // The clients theme color.
        final Color color = new Color(Rise.CLIENT_THEME_COLOR);

        // Vertical
        RenderUtil.gradient(this.getX() - 1, this.getY(), 1, this.getHeight() + count + 2, color.brighter().brighter(), color.darker());
        RenderUtil.gradient(this.getX() + this.getWidth() - 1, this.getY(), 1, this.getHeight() + count + 2, color.brighter().brighter(), color.darker());

        // Horizontal
        RenderUtil.gradient(this.getX() - 1, this.getY() - 1, this.getWidth() + 1, 1, color.darker(), color.brighter().brighter());
        RenderUtil.gradient(this.getX() - 1, this.getY() + this.getHeight() + count + 1, this.getWidth(), 1, color.darker(), color.brighter().brighter());

        //Black bar at the bottom
        RenderUtil.rect(this.getX(), this.getY() + 1 + this.getHeight() + count - 1, this.getWidth() - 1, 1, new Color(0xff232623));

        // I legit put the gradient colors on specific orders to make it good love me.
    }

    @Override
    public void mouseAction(final int mouseX, final int mouseY, final boolean click, final int button) {
        if (isHovered(mouseX, mouseY)) {
            if (click) {
                if (button == 0) {
                    dragged = true;
                    mouseX2 = (int) (this.getX() - mouseX);
                    mouseY2 = (int) (this.getY() - mouseY);
                } else {
                    open = !open;
                }
            }
        }

        if (!click) dragged = false;
    }
}
