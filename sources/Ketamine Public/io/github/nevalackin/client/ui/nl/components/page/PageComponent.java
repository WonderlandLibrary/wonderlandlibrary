package io.github.nevalackin.client.ui.nl.components.page;

import io.github.nevalackin.client.ui.framework.Animated;
import io.github.nevalackin.client.ui.framework.Component;
import io.github.nevalackin.client.ui.nl.components.page.groupBox.GroupBoxComponent;
import io.github.nevalackin.client.util.render.DrawUtil;
import net.minecraft.client.gui.ScaledResolution;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.lwjgl.opengl.GL11.glScaled;
import static org.lwjgl.opengl.GL11.glTranslated;

public abstract class PageComponent extends Component implements Animated {

    private double fadeInProgress;

    public double pageSelectorButtonY;

    public PageComponent(Component parent) {
        super(parent, 0, 0, 0, 0);

        this.onInit();

        // Sort from longest to shorts
        this.getChildren().sort(Comparator.comparingDouble(Component::getHeight).reversed());
    }

    @Override
    public void onDraw(ScaledResolution scaledResolution, int mouseX, int mouseY) {
        final double x = this.getX();
        final double y = this.getY();
        final double width = this.getWidth();
        final double height = this.getHeight();

        this.fadeInProgress = DrawUtil.animateProgress(this.fadeInProgress, 1.0, 3);
        final double smooth = DrawUtil.bezierBlendAnimation(this.fadeInProgress);

        glTranslated(x, this.pageSelectorButtonY, 0);
        glScaled(smooth, smooth, 1);
        glTranslated(-x, -this.pageSelectorButtonY, 0);

        // Margins should be 1/30ths of their respective dimension
        final double xMargin = width / 30.0;
        final double yMargin = height / 30.0;

        // Inter groupbox margin
        final double xGap = xMargin / 2.0;
        final double yGap = yMargin / 2.0;
        // Store dimensions with no margins
        final double canvasWidth = width - xMargin * 2.0;
        final double canvasHeight = height - yMargin;

        final double minGroupBoxSize = 150;

        // Default is 1 group box filling entire canvas
        double groupBoxWidth;
        int groupBoxesPerRow = 1;

        // algorithm for finding the best configuration to get most group boxes in row and calculating width
        int i = 5; // start at 5 max
        while (i >= 1) {
            final double groupBoxSize = (canvasWidth - xGap * (i - 1)) / i;

            if (groupBoxSize > minGroupBoxSize) {
                if (i < groupBoxesPerRow) break;
                groupBoxesPerRow = i;
            }

            i--;
        }

        final List<Component> children = this.getChildren();
        final int size = children.size();

        groupBoxesPerRow = Math.min(size, groupBoxesPerRow);
        groupBoxWidth = (canvasWidth - xGap * (groupBoxesPerRow - 1)) / groupBoxesPerRow;

        double[] columns = new double[groupBoxesPerRow];
        Arrays.fill(columns, yMargin);

        for (int j = 0; j < size; j++) {
            final Component child = children.get(j);
            if (!(child instanceof GroupBoxComponent)) continue;
            final GroupBoxComponent groupBox = (GroupBoxComponent) child;
            // Resize all group boxes
            child.setWidth(groupBoxWidth);
            // Store group box height
            final double groupBoxHeight = child.getHeight();

            // Reposition group box
            repositionGroupBox:
            {
                final int currColumn = j % groupBoxesPerRow;

                // If at least on second row
                if (j >= groupBoxesPerRow) {
                    double smallest = Double.MAX_VALUE;
                    int smallestColumn = -1;

                    for (int column = 0; column < groupBoxesPerRow; column++) {
                        final double columnHeight = columns[column] + groupBoxHeight;

                        if (columnHeight < smallest) {
                            smallest = columnHeight;
                            smallestColumn = column;
                        }
                    }

                    if (smallestColumn != -1) {
                        child.setX(xMargin + (groupBoxWidth + xGap) * smallestColumn);
                        child.setY(smallest - groupBoxHeight);
                        groupBox.pageColumn = smallestColumn;
                        break repositionGroupBox;
                    }
                }

                child.setX(xMargin + (groupBoxWidth + xGap) * currColumn);
                child.setY(yMargin);
                groupBox.pageColumn = currColumn;
            }

            groupBox.maxHeight = canvasHeight - columns[groupBox.pageColumn];
            columns[groupBox.pageColumn] += groupBoxHeight + yGap;
            // Draw group box after repositioning it
            child.onDraw(scaledResolution, mouseX, mouseY);
        }

        glScaled(-smooth, -smooth, 1);
    }

    @Override
    public void resetAnimationState() {
        this.fadeInProgress = 0.0;

        this.resetChildrenAnimations();
    }

    @Override
    public boolean shouldPlayAnimation() {
        return this.fadeInProgress >= 1.0;
    }

    @Override
    public void onMouseClick(int mouseX, int mouseY, int button) {
        // Implemented in RootComponent
    }

    public abstract void onInit();
}
