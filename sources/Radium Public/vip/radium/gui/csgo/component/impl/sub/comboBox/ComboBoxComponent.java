package vip.radium.gui.csgo.component.impl.sub.comboBox;

import net.minecraft.client.gui.Gui;
import org.lwjgl.opengl.GL11;
import vip.radium.gui.csgo.SkeetUI;
import vip.radium.gui.csgo.component.ButtonComponent;
import vip.radium.gui.csgo.component.Component;
import vip.radium.gui.csgo.component.ExpandableComponent;
import vip.radium.gui.csgo.component.PredicateComponent;
import vip.radium.gui.font.FontRenderer;
import vip.radium.utils.StringUtils;
import vip.radium.utils.Wrapper;
import vip.radium.utils.render.LockedResolution;
import vip.radium.utils.render.OGLUtils;
import vip.radium.utils.render.RenderingUtils;

import java.util.List;

public abstract class ComboBoxComponent extends ButtonComponent implements PredicateComponent, ExpandableComponent {

    private boolean expanded;

    public ComboBoxComponent(Component parent, float x, float y, float width, float height) {
        super(parent, x, y, width, height);
    }

    private String getDisplayString() {
        if (isMultiSelectable()) {
            final List<Enum<?>> values = getMultiSelectValues();
            final int len = values.size();

            if (len == 0) {
                return "-";
            }

            if (len == 1) {
                return StringUtils.upperSnakeCaseToPascal(values.get(0).name());
            }

            final StringBuilder sb = new StringBuilder(StringUtils.upperSnakeCaseToPascal(values.get(0).name())).append(", ");
            for (int i = 1; i < len; i++) {
                sb.append(StringUtils.upperSnakeCaseToPascal(values.get(i).name()));
                if (i != len - 1)
                    sb.append(", ");
            }
            return sb.toString();
        } else {
            return StringUtils.upperSnakeCaseToPascal(getValue().name());
        }
    }

    @Override
    public void drawComponent(LockedResolution lockedResolution, int mouseX, int mouseY) {
        final float x = getX();
        final float y = getY();
        final float width = getWidth();
        final float height = getHeight();

        Gui.drawRect(x, y, x + width, y + height, SkeetUI.getColor(0x0D0D0D));

        final boolean hovered = isHovered(mouseX, mouseY);

        RenderingUtils.drawGradientRect(x + 0.5F, y + 0.5F, x + width - 0.5F, y + height - 0.5F,
                false,
                SkeetUI.getColor(hovered ?
                        RenderingUtils.darker(0x1E1E1E, 1.4F) :
                        0x1E1E1E),
                SkeetUI.getColor(hovered ?
                        RenderingUtils.darker(0x232323, 1.4F) :
                        0x232323));

        GL11.glColor4f(0.6F, 0.6F, 0.6F, (float) SkeetUI.getAlpha() / 255.0F);
        RenderingUtils.drawAndRotateArrow(x + width - 5, y + height / 2 - 0.5F, 3, isExpanded());

        if (SkeetUI.shouldRenderText()) {
            GL11.glEnable(GL11.GL_SCISSOR_TEST);
            OGLUtils.startScissorBox(lockedResolution, (int) x + 2, (int) y + 1,
                    (int) width - 8, (int) height - 1);
            SkeetUI.FONT_RENDERER.drawString(getDisplayString(),
                    x + 2,
                    y + height / 3,
                    SkeetUI.getColor(0x969696));
            GL11.glDisable(GL11.GL_SCISSOR_TEST);
        }

        // Render over other components (Slider numbers are translated +1 so we need to use +2 here)
        GL11.glTranslatef(0.0F, 0.0F, 2.0F);
        if (expanded) {
            Enum<?>[] values = getValues();
            final float dropDownHeight = values.length * height;
            Gui.drawRect(x, y + height, x + width, y + height + dropDownHeight + 0.5F,
                    SkeetUI.getColor(0x0D0D0D));
            float valueBoxHeight = height;
            final Enum<?>[] enums = getValues();
            for (final Enum<?> value : enums) {
                boolean valueBoxHovered = mouseX >= x && mouseY >= y + valueBoxHeight &&
                        mouseX <= x + width && mouseY < y + valueBoxHeight + height;
                Gui.drawRect(x + 0.5F, y + valueBoxHeight,
                        x + width - 0.5F, y + valueBoxHeight + height,
                        SkeetUI.getColor(valueBoxHovered ? RenderingUtils.darker(0x232323, 0.7F) : 0x232323));
                final boolean selected;
                if (isMultiSelectable()) {
                    selected = getMultiSelectValues().contains(value);
                } else {
                    selected = value == getValue();
                }
                int color = selected ? SkeetUI.getColor() : SkeetUI.getColor(0xDCDCDC);
                FontRenderer fr;
                if (selected || valueBoxHovered) {
                    fr = SkeetUI.GROUP_BOX_HEADER_RENDERER;
                } else {
                    fr = SkeetUI.FONT_RENDERER;
                }
                fr.drawString(StringUtils.upperSnakeCaseToPascal(value.name()), x + 2, y + valueBoxHeight + 4, color);
                valueBoxHeight += height;
            }
        }
        GL11.glTranslatef(0.0F, 0.0F, -2.0F);
    }

    @Override
    public void onMouseClick(int mouseX, int mouseY, int button) {
        if (isHovered(mouseX, mouseY))
            onPress(button);

        if (isExpanded() && button == 0) {
            final float x = getX();
            final float y = getY();
            final float height = getHeight();
            final float width = getWidth();
            float valueBoxHeight = height;
            for (int i = 0; i < getValues().length; i++) {
                if (mouseX >= x && mouseY >= y + valueBoxHeight && mouseX <= x + width && mouseY <= y + valueBoxHeight + height) {
                    setValue(i);
                    if (!isMultiSelectable())
                        expandOrClose();
                    return;
                }
                valueBoxHeight += height;
            }
        }
    }

    private void expandOrClose() {
        setExpanded(!isExpanded());
    }

    @Override
    public void onPress(int mouseButton) {
        if (mouseButton == 1)
            expandOrClose();
    }

    @Override
    public float getExpandedX() {
        return getX();
    }

    @Override
    public float getExpandedY() {
        return getY();
    }

    public abstract Enum<?> getValue();

    public abstract void setValue(int index);

    public abstract List<Enum<?>> getMultiSelectValues();

    public abstract boolean isMultiSelectable();

    public abstract Enum<?>[] getValues();

    @Override
    public boolean isExpanded() {
        return expanded;
    }

    @Override
    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    @Override
    public float getExpandedWidth() {
        return getWidth();
    }

    @Override
    public float getExpandedHeight() {
        final float height = getHeight();
        return height + getValues().length * height + height;
    }
}
