package io.github.nevalackin.client.ui.nl.components.selector;

import io.github.nevalackin.client.ui.framework.Component;

public interface PageSelector<T extends PageSelector> {

    Component getParent();

    @SuppressWarnings("unchecked")
    default T getSelectorParent() {
        return (T) this.getParent();
    }

    default int getSelectedIdx() {
        return this.getSelectorParent().getSelectedIdx();
    }

    default void onPageSelect(final int idx, final double y) {
        this.getSelectorParent().onPageSelect(idx, y);
    }
}
