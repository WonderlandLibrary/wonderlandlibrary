package vip.radium.gui.csgo.component.impl.sub.text;

import vip.radium.gui.csgo.SkeetUI;
import vip.radium.gui.csgo.component.Component;
import vip.radium.gui.font.FontRenderer;
import vip.radium.utils.render.LockedResolution;

public final class TextComponent extends Component {

    private static final FontRenderer FONT_RENDERER = SkeetUI.FONT_RENDERER;
    private final String text;

    public TextComponent(Component parent, String text, float x, float y) {
        super(parent, x, y, FONT_RENDERER.getWidth(text), FONT_RENDERER.getHeight(text));
        this.text = text;
    }

    @Override
    public void drawComponent(LockedResolution resolution, int mouseX, int mouseY) {
        if (SkeetUI.shouldRenderText())
            FONT_RENDERER.drawString(text, getX(), getY(), SkeetUI.getColor(0xE6E6E6));
    }
}
