package io.github.nevalackin.client.ui.nl.components;

import io.github.nevalackin.client.ui.framework.Component;

public final class PopupWindowComponent extends WindowComponent {

    // TODO :: Popups

    private final String title, body;

    public PopupWindowComponent(Component parent, String title, String body, PopupType type, double x, double y, double width, double height, Decoration... decorations) {
        super(parent, x, y, width, height, decorations);

        this.title = title;
        this.body = body;
    }

    public enum PopupType {
        OK, YES_NO,
    }
}
