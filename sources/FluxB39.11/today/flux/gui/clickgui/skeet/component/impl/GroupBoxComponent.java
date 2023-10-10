package today.flux.gui.clickgui.skeet.component.impl;

import net.minecraft.client.gui.Gui;
import today.flux.gui.clickgui.skeet.SkeetClickGUI;
import today.flux.gui.clickgui.skeet.component.impl.sub.checkBox.CheckBoxTextComponent;
import today.flux.gui.clickgui.skeet.component.impl.sub.comboBox.ComboBoxComponent;
import today.flux.gui.clickgui.skeet.component.impl.sub.comboBox.ComboBoxTextComponent;
import today.flux.gui.clickgui.skeet.LockedResolution;
import today.flux.gui.clickgui.skeet.component.ButtonComponent;
import today.flux.gui.clickgui.skeet.component.Component;

public final class GroupBoxComponent extends Component {
    private final String name;

    public GroupBoxComponent(Component parent, String name, float x, float y, float width, float height) {
        super(parent, x, y, width, height);
        this.name = name;
    }

    @Override
    public void drawComponent(LockedResolution resolution, int mouseX, int mouseY) {
        float x = this.getX();
        float y = this.getY();
        float width = this.getWidth();
        float height = this.getHeight();
        float length = SkeetClickGUI.GROUP_BOX_HEADER_RENDERER.getStringWidth(this.name);
        Gui.drawRect(x, y, x + width, y + height, SkeetClickGUI.getColor(789516));
        Gui.drawRect(x + 0.5f, y + 0.5f, x + width - 0.5f, y + height - 0.5f, SkeetClickGUI.getColor(0x282828));
        Gui.drawRect(x + 4.0f, y, x + 4.0f + length + 2.0f, y + 1.0f, SkeetClickGUI.getColor(0x171717));
        Gui.drawRect(x + 1.0f, y + 1.0f, x + width - 1.0f, y + height - 1.0f, SkeetClickGUI.getColor(0x171717));

        if (SkeetClickGUI.shouldRenderText()) {
            SkeetClickGUI.GROUP_BOX_HEADER_RENDERER.drawOutlinedString(this.name, x + 5.0f, y - 3f, SkeetClickGUI.getColor(0xDCDCDC), SkeetClickGUI.getColor(0));
        }

        float childY = 5f;
        for (Component component : this.children) {
            component.setX((component instanceof CheckBoxTextComponent || component instanceof ScrollListComponent || component instanceof ButtonComponent) ? 3f : 10f);
            component.setY(childY);
            component.drawComponent(resolution, mouseX, mouseY);

            childY += component.getHeight() + 4f;
        }
    }

    @Override
    public void onMouseClick(int mouseX, int mouseY, int button) {
        for (Component child : this.children) {
            if (child instanceof ComboBoxTextComponent) {
                final ComboBoxTextComponent comboBoxTextComponent = (ComboBoxTextComponent)child;
                final ComboBoxComponent comboBox = comboBoxTextComponent.getComboBoxComponent();

                if (!comboBox.isExpanded()) {
                    continue;
                }

                final float x = comboBox.getX();
                final float y = comboBoxTextComponent.getY() + comboBoxTextComponent.getHeight();

                if (mouseX >= x && mouseY > y && mouseX <= x + comboBox.getWidth() && mouseY < y + comboBox.getExpandedHeight()) {
                    comboBoxTextComponent.onMouseClick(mouseX, mouseY, button);
                    return;
                }
            }
        }
        super.onMouseClick(mouseX, mouseY, button);
    }

    @Override
    public float getHeight() {
        float initHeight;
        float height = initHeight = super.getHeight();

        for (Component component : this.getChildren()) {
            height += component.getHeight() + 4f;
        }

        float heightWithComponents = height;
        return heightWithComponents - initHeight > initHeight ? heightWithComponents : initHeight;
    }
}

