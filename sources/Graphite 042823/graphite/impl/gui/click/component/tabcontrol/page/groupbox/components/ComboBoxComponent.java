package graphite.impl.gui.click.component.tabcontrol.page.groupbox.components;

import graphite.api.util.render.RenderUtil;
import graphite.api.util.render.font.TTFFontRenderer;
import graphite.impl.Graphite;
import graphite.impl.gui.click.component.Theme;
import graphite.impl.gui.click.framework.Component;
import graphite.impl.gui.click.framework.Expandable;
import graphite.impl.gui.click.framework.Predicated;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class ComboBoxComponent extends Component implements Expandable, Predicated {

    private final String label;

    private final Consumer<Integer> setValueFunc, unsetValueFunc;
    private final Supplier<int[]> getSelectedFunc;
    private final Supplier<Integer> getValueFunc;
    private final Supplier<String[]> getValuesFunc;
    private final Supplier<Boolean> dependency;

    private final boolean multiSelection;

    private int[] cachedSelectedStringValues;
    private String selectedValuesString;
    private boolean expanded;

    private final int expandedBoxYOffset = 1;

    public ComboBoxComponent(Component parent, String label,
                             Consumer<Integer> setValueFunc, Consumer<Integer> unsetValueFunc,
                             Supplier<int[]> getSelectedFunc, Supplier<Integer> getValueFunc,
                             Supplier<String[]> getValuesFunc, Supplier<Boolean> dependency, boolean multiSelection) {
        super(parent, 0, 0, 0, 15);

        this.label = label;

        this.setValueFunc = setValueFunc;
        this.unsetValueFunc = unsetValueFunc;
        this.getSelectedFunc = getSelectedFunc;
        this.getValueFunc = getValueFunc;
        this.getValuesFunc = getValuesFunc;
        this.dependency = dependency;

        this.multiSelection = multiSelection;
    }

    private void updateSelected() {
        if (!Arrays.equals(this.getSelectedFunc.get(), this.cachedSelectedStringValues)) {
            this.selectedValuesString = this.getSelected();
            this.cachedSelectedStringValues = this.getSelectedFunc.get();
        }
    }

    private String getSelected() {
        final String[] values = this.getValuesFunc.get();
        if (values.length == 0) return "";
        final String array = Arrays.toString(Arrays.stream(this.getSelectedFunc.get())
                .mapToObj(i -> values[i])
                .toArray(String[]::new));
        return array.substring(1, array.length()-1);
    }

    @Override
    public void onDraw(ScaledResolution scaledResolution, int mouseX, int mouseY) {
        final double x = this.getX();
        final double y = this.getY();
        final double width = this.getWidth();
        final double height = this.getHeight();

        final TTFFontRenderer fontRenderer = Graphite.INSTANCE.getFontManager().getFontRenderer("VERDANA_9");
        
        {
            fontRenderer.drawStringWithShadow(this.label, (float) x - .5F, (float) y + .5F, new Color(205, 205, 205).getRGB());
        }

        if (this.multiSelection) this.updateSelected();
        else this.selectedValuesString = this.getValuesFunc.get()[this.getValueFunc.get()];

        {
            RenderUtil.drawRect(x, y + 5, x + width, y + height, new Color(18,18,24).getRGB());
            RenderUtil.drawGradientRect(x + .5, y + 5.5, x + width - .5, y + height - .5, false, new Color(41, 41, 51).getRGB(), new Color(36, 36, 44).getRGB());
            
            RenderUtil.drawArrow((float) (x + width - 6), (float) (y + height - (height / 3)), 2.5F, expanded, 0xff9c9ca8);

            fontRenderer.drawStringWithShadow(this.multiSelection ? truncate(this.selectedValuesString, 21) : this.selectedValuesString, (float) (x + 3), (float) (y + height - 5.5), new Color(201,201,201).getRGB());

            GL11.glTranslated(0.0, 0.0F, 2.0);
            if (expanded) {
                RenderUtil.drawBorderedRect(x, y + height + expandedBoxYOffset, x + width, y + calculateExpandedHeight() + 17, .5, new Color(40, 40, 50).getRGB(), new Color(18, 18, 24).getRGB());

                final double valuesXPos = x + 1.5;
                double valuesYPos = y + height + expandedBoxYOffset + 1.5;
                double valuesWidth = width - 3;
                for (final String value : getValuesFunc.get()) {
                    boolean elementHovered = mouseX >= valuesXPos && mouseY >= valuesYPos &&
                            mouseX <= valuesXPos + valuesWidth && mouseY < valuesYPos + 6.5;

                    final boolean selected;
                    if (this.multiSelection) {
                        selected = selectedValuesString.contains(value);
                    } else {
                        selected = Objects.equals(value, selectedValuesString);
                    }

                    int color = selected ? Theme.getUIColor() : 0xFFc9c9da;

                    if (elementHovered)
                        RenderUtil.drawRect(valuesXPos, valuesYPos, valuesXPos + valuesWidth, valuesYPos + 6.5, new Color(54, 54, 64).getRGB());

                    fontRenderer.drawString(value, (float) valuesXPos + 1.5F, (float) valuesYPos + 2.5F, color);

                    valuesYPos += 7;
                }
            }
            GL11.glTranslated(0.0, 0.0, -2.0);
        }
    }

    private String truncate(String str, int width) {
        if(str.length() > width )
            return str.substring( 0, width ) + "...";

        return str;
    }

    @Override
    public void onMouseClick(int mouseX, int mouseY, int button) {
        boolean comboBoxHovered = mouseX >= this.getX() && mouseY >= this.getY() + 5 &&
                mouseX <= this.getX() + this.getWidth() && mouseY < this.getY() + this.getHeight();

        boolean valuesBoxHovered = mouseX >= this.getX() && mouseY >= this.getY() + this.getHeight() + expandedBoxYOffset &&
                mouseX <= this.getX() + this.getWidth() && mouseY < this.getY() + calculateExpandedHeight() + 17;

        if (button == 0) {
            if (comboBoxHovered)
                setExpanded(!isExpanded());

            if (valuesBoxHovered) {
                final double valuesXPos = this.getX() + 1.5;
                double valuesYPos = this.getY() + this.getHeight() + expandedBoxYOffset + 1.5;
                double valuesWidth = this.getWidth() - 3;

                // TODO: Value selecting
            }
        }
    }

    @Override
    public boolean isVisible() {
        return this.dependency != null ? this.dependency.get() : true;
    }

    @Override
    public boolean isExpanded() {
        return expanded;
    }

    @Override
    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    @Override
    public double getExpandedX() {
        return this.getX();
    }

    @Override
    public double getExpandedY() {
        return this.getY();
    }

    @Override
    public double getExpandedWidth() {
        return this.getWidth();
    }

    @Override
    public double calculateExpandedHeight() {
        double height = 1.5;

        for (final String ignored : this.getValuesFunc.get()) {
            height += 7;
        }

        return height;
    }
}
