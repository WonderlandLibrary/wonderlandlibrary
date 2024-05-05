package graphite.impl.gui.click.component.tabcontrol.page;

import graphite.api.util.render.RenderUtil;
import graphite.impl.gui.click.component.tabcontrol.page.groupbox.GroupBoxComponent;
import graphite.impl.gui.click.framework.Component;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public abstract class PageComponent extends Component {
    public PageComponent(Component parent) {
        super(parent, 0, 0, 0, 0);

        this.onInit();
    }

    @Override
    public void onDraw(ScaledResolution scaledResolution, int mouseX, int mouseY) {
        final double x = this.getX();
        final double y = this.getY();
        final double width = this.getWidth();
        final double height = this.getHeight();
        
        {
            RenderUtil.drawBorderedRect(x, y, x + width, y + height, .5, new Color(32, 32, 38).getRGB(), new Color(64, 64, 80).getRGB());

            final double xMargin = width / 30.0;
            final double yMargin = height / 30.0;

            final double xGap = xMargin / 1.5;
            final double yGap = yMargin / 0.75;

            final double canvasWidth = width - xMargin * 2.0;
            final double canvasHeight = height - yMargin;

            final double minGroupBoxSize = 150;

            double groupBoxWidth;
            int groupBoxesPerRow = 3;

            int i = 5;
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
                child.setWidth(groupBoxWidth);
                final double groupBoxHeight = child.getHeight();

                repositionGroupBox:
                {
                    final int currColumn = j % groupBoxesPerRow;

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
                child.onDraw(scaledResolution, mouseX, mouseY);
            }
        }
    }

    @Override
    public void onMouseClick(int mouseX, int mouseY, int button) {
    }

    public abstract void onInit();
}