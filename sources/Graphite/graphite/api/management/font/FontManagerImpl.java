package graphite.api.management.font;

import graphite.api.util.render.font.TTFFontRenderer;

import java.awt.*;
import java.util.HashMap;

public final class FontManagerImpl implements FontManager {
    private final HashMap<String, TTFFontRenderer> registered = new HashMap<>();

    public FontManagerImpl() {
        this.register("VERDANA_9", new TTFFontRenderer(new Font("Verdana", Font.PLAIN, 9), true, false));
        this.register("VERDANA_BOLD_9", new TTFFontRenderer(new Font("Verdana", Font.BOLD, 9), true, false));
        this.register("TAHOMA_9", new TTFFontRenderer(new Font("Tahoma", Font.PLAIN, 9), false, false));
        this.register("TAHOMA_16", new TTFFontRenderer(new Font("Tahoma", Font.PLAIN, 16), true, true));
        this.register("TAHOMA_BOLD_16", new TTFFontRenderer(new Font("Tahoma", Font.BOLD, 16), true, true));
    }

    @Override
    public <T extends TTFFontRenderer> void register(final String tag, final T font) {
        this.registered.put(tag, font);
    }

    @Override
    public TTFFontRenderer getFontRenderer(String tag) {
        return this.registered.get(tag);
    }
}
