package graphite.api.util.render.font;

public interface FontRenderer {

    int drawString(String text, float x, float y, int color);

    int drawStringWithShadow(String text, float x, float y, int color);

    default void drawStringWithOutline(String text, float x, float y, int color, int outlineColor) {
        this.drawString(text, x - 0.5f, y, outlineColor);
        this.drawString(text, x + 0.5f, y, outlineColor);
        this.drawString(text, x, y - 0.5f, outlineColor);
        this.drawString(text, x, y + 0.5f, outlineColor);
        this.drawString(text, x, y, color);
    }

    float getWidth(String text);

    default float getHeight(String text) {
        return 11.0F;
    };

}