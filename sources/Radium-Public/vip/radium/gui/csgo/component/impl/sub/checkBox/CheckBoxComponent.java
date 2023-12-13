package vip.radium.gui.csgo.component.impl.sub.checkBox;

import net.minecraft.client.gui.Gui;
import vip.radium.gui.csgo.SkeetUI;
import vip.radium.gui.csgo.component.ButtonComponent;
import vip.radium.gui.csgo.component.Component;
import vip.radium.gui.csgo.component.PredicateComponent;
import vip.radium.utils.render.LockedResolution;
import vip.radium.utils.render.RenderingUtils;

public abstract class CheckBoxComponent extends ButtonComponent implements PredicateComponent {

    public CheckBoxComponent(Component parent, float x, float y, float width, float height) {
        super(parent, x, y, width, height);
    }

    @Override
    public void drawComponent(LockedResolution resolution, int mouseX, int mouseY) {
        final float x = getX();
        final float y = getY();
        final float width = getWidth();
        final float height = getHeight();

        Gui.drawRect(x, y, x + width, y + height, SkeetUI.getColor(0x0D0D0D));

        final boolean checked = isChecked();
        final boolean hovered = isHovered(mouseX, mouseY);
        RenderingUtils.drawGradientRect(x + 0.5F, y + 0.5F, x + width - 0.5F, y + height - 0.5F,
                                        false,
                                        checked ? SkeetUI.getColor() :
                                            SkeetUI.getColor(hovered ?
                                                                 RenderingUtils.darker(0x494949, 1.4F) :
                                                                 0x494949),
                                        checked ? RenderingUtils.darker(SkeetUI.getColor(), 0.8F) :
                                            SkeetUI.getColor(hovered ?
                                                                 RenderingUtils.darker(0x303030, 1.4F) :
                                                                 0x303030));
    }

    @Override
    public void onPress(int mouseButton) {
        if (mouseButton == 0)
            setChecked(!isChecked());
    }

    public abstract boolean isChecked();

    public abstract void setChecked(boolean checked);
}
