package graphite.impl.gui.click.component.tabcontrol.selector;

import graphite.api.module.util.Category;
import graphite.impl.gui.click.component.RootComponent;
import graphite.impl.gui.click.framework.Component;

public final class PageSelectorComponent extends Component implements PageSelector<RootComponent> {
	private double pageButtonOffset;
	private final double pageButtonWidth = 48.3;

    public PageSelectorComponent(Component parent) {
        super(parent, 0, 0, 0, 0);

        for (final Category category : Category.values()) {
            this.addPageButton(category.name(), category.ordinal());
        }
        
        this.addPageButton("Configuration", 5);
    }

    @Override
    public double getWidth() {
        return this.getChildren().size() * pageButtonWidth - .5;
    }

    private void addPageButton(final String label, final int idx) {
    	final PageButtonComponent pageButton = new PageButtonComponent(this, label, idx, this.pageButtonOffset, 0, pageButtonWidth, 13);
        this.addChild(pageButton);
        this.pageButtonOffset += pageButton.getWidth() - .5;
    }
}