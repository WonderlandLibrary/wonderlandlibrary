package io.github.nevalackin.client.ui.nl.components.selector;

import io.github.nevalackin.client.module.Category;
import io.github.nevalackin.client.ui.cfont.StaticallySizedImage;
import io.github.nevalackin.client.ui.framework.Component;
import io.github.nevalackin.client.ui.nl.components.RootComponent;
import io.github.nevalackin.client.util.misc.ResourceUtil;

import javax.imageio.ImageIO;
import java.io.IOException;

public final class PageSelectorComponent extends Component implements PageSelector<RootComponent> {

    private double pageButtonOffset;

    private final double margin = 4;
    private final double pageButtonWidth;

    public PageSelectorComponent(Component parent) {
        super(parent, 0, 0, 0, 0);

        this.pageButtonWidth = this.getWidth() - margin * 2.0;

        for (final Category category : Category.values()) {
            this.addPageLabel(category.toString());

            for (final Category.SubCategory subCategory : category.getSubCategories()) {
                this.addPageButton(subCategory.toString(), String.format("icons/ui/%s/%s.png", category.name().toLowerCase(), subCategory.toString().toLowerCase()), subCategory.ordinal());
            }
        }
    }

    @Override
    public double getWidth() {
        return this.getParent().getWidth() / 5.0;
    }

    private void addPageLabel(final String label) {
        final PageSelectorLabelComponent labelComponent = new PageSelectorLabelComponent(this, label, this.margin, this.pageButtonOffset, this.pageButtonWidth, 16);
        this.addChild(labelComponent);
        this.pageButtonOffset += labelComponent.getHeight();
    }

    private void addPageButton(final String label, final String iconPath, final int idx) {
        try {
            final StaticallySizedImage icon = new StaticallySizedImage(ImageIO.read(ResourceUtil.getResourceStream(iconPath)), true, 2);
            final PageButtonComponent pageButton = new PageButtonComponent(this, label, icon, idx, this.margin, this.pageButtonOffset, this.pageButtonWidth, 18);
            this.addChild(pageButton);
            this.pageButtonOffset += pageButton.getHeight();
        } catch (IOException ignored) {
        }
    }
}
