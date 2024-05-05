package io.github.nevalackin.client.ui.nl.components.page.groupBox.components;

import io.github.nevalackin.client.ui.cfont.CustomFontRenderer;
import io.github.nevalackin.client.ui.framework.*;
import io.github.nevalackin.client.core.KetamineClient;
import io.github.nevalackin.client.util.render.ColourUtil;
import io.github.nevalackin.client.util.render.DrawUtil;
import net.minecraft.client.gui.ScaledResolution;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class DropDownComponent extends Component implements Animated, Expandable, Predicated {

    private final String name;

    private final Consumer<Integer> setValueFunc, unsetValueFunc;
    private final Supplier<int[]> getSelectedFunc;
    private final Supplier<Integer> getValueFunc;
    private final Supplier<String[]> getValuesFunc;
    private final Supplier<Boolean> dependency;

    private final boolean multiSelection;

    private ExpandState state;
    private double progress;

    private double hoveredFadeInProgress;

    private int[] cachedSelectedStringValues;
    private String selectedValuesString;

    public DropDownComponent(Component parent, String name,
                             Consumer<Integer> setValueFunc, Consumer<Integer> unsetValueFunc,
                             Supplier<int[]> getSelectedFunc, Supplier<Integer> getValueFunc,
                             Supplier<String[]> getValuesFunc, Supplier<Boolean> dependency, boolean multiSelection, double height) {
        super(parent, 0, 0, 0, height);

        this.name = name;

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

        this.hoveredFadeInProgress = DrawUtil.animateProgress(this.hoveredFadeInProgress, this.isHovered(mouseX, mouseY) ? 1.0 : 0.0, 5);
        final double smoothHovered = DrawUtil.bezierBlendAnimation(this.hoveredFadeInProgress);

        final CustomFontRenderer fontRenderer = KetamineClient.getInstance().getFontRenderer();

        // Draw label
        {
            fontRenderer.draw(this.name, x, y + height / 2.0 - fontRenderer.getHeight(this.name) / 2.0, 0.22,
                              ColourUtil.fadeBetween(this.getTheme().getTextColour(), this.getTheme().getHighlightedTextColour(), smoothHovered));
        }

        if (this.multiSelection) this.updateSelected();
        else this.selectedValuesString = this.getValuesFunc.get()[this.getValueFunc.get()];

        // Draw box
        {
            fontRenderer.draw(this.selectedValuesString, x + width - fontRenderer.getWidth(this.selectedValuesString, 0.22),
                              y + height / 2.0 - fontRenderer.getHeight(this.selectedValuesString) / 2.0, 0.22,
                              this.getTheme().getTextColour());
        }
    }

    @Override
    public void onMouseClick(int mouseX, int mouseY, int button) {
        if (!this.multiSelection) {
            if (button == 0) {
                this.setValueFunc.accept(this.getValueFunc.get() + 1);
            } else if (button == 1) {
                this.setValueFunc.accept(this.getValueFunc.get() - 1);
            }
        }
    }

    @Override
    public void onMouseRelease(int button) {

    }

    @Override
    public void onKeyPress(int keyCode) {

    }

    @Override
    public void resetAnimationState() {
        this.setState(ExpandState.CLOSED);
        this.progress = 0.0;
    }

    @Override
    public void setState(ExpandState state) {
        this.state = state;
    }

    @Override
    public ExpandState getState() {
        return this.state;
    }

    @Override
    public double getExpandedX() {
        return this.getX();
    }

    @Override
    public double getExpandedY() {
        return this.getY() + this.getHeight();
    }

    @Override
    public double getExpandedWidth() {
        return this.getWidth();
    }

    @Override
    public double calculateExpandedHeight() {
        double height = 0;

        final CustomFontRenderer fontRenderer = KetamineClient.getInstance().getFontRenderer();

        for (final String value : this.getValuesFunc.get()) {
            height += fontRenderer.getWidth(value, 0.22, 700) + 2;
        }

        return height;
    }

    @Override
    public double getExpandProgress() {
        return this.progress;
    }

    @Override
    public boolean isVisible() {
        return this.dependency != null ? this.dependency.get() : true;
    }
}
