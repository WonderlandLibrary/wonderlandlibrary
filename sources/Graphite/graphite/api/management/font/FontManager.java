package graphite.api.management.font;

import graphite.api.util.render.font.TTFFontRenderer;

public interface FontManager {
    <T extends TTFFontRenderer> void register(final String tag, final T font);

    TTFFontRenderer getFontRenderer(final String tag);
}
