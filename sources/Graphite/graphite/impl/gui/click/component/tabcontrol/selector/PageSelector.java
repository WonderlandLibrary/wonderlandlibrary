package graphite.impl.gui.click.component.tabcontrol.selector;

import graphite.impl.gui.click.framework.Component;

public interface PageSelector<T extends PageSelector> {
    Component getParent();

    @SuppressWarnings("unchecked")
    default T getSelectorParent() {
        return (T) this.getParent();
    }

    default int getSelectedIdx() {
        return this.getSelectorParent().getSelectedIdx();
    }

    default void onPageSelect(final int idx) {
        this.getSelectorParent().onPageSelect(idx);
    }
}