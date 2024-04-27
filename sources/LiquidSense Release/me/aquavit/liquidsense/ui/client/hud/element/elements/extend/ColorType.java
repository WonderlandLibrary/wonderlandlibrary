package me.aquavit.liquidsense.ui.client.hud.element.elements.extend;

import java.awt.*;

public enum ColorType {

    SUCCESS(new Color(0x60E066)),
    ERROR(new Color(0xFF2F3A)),
    WARNING(new Color(0xF5FD00)),
    INFO(new Color(0x6490A7)),
    WTF(new Color(0x6490A7));

    public Color renderColor;

    ColorType(Color renderColor) {
        this.renderColor = renderColor;
    }
}