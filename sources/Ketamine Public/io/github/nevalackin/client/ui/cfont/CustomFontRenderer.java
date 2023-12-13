package io.github.nevalackin.client.ui.cfont;

public interface CustomFontRenderer {

    double DEFAULT_SCALE = 0.25;
    int DEFAULT_WEIGHT = 500;
    float BASE_FONT = 32.f;

    void draw(final CharSequence text,
              final double x,
              final double y,
              final double scale,
              final int weight,
              final int colour);

    default void draw(final CharSequence text,
                      final double x,
                      final double y,
                      final double scale,
                      final int colour) {
        this.draw(text, x, y, scale, DEFAULT_WEIGHT, colour);
    }

    default void draw(final CharSequence text,
                      final double x,
                      final double y,
                      final int weight,
                      final int colour) {
        this.draw(text, x, y, DEFAULT_SCALE, weight, colour);
    }


    default void draw(final CharSequence text,
                      final double x,
                      final double y,
                      final int colour) {
        this.draw(text, x, y, DEFAULT_SCALE, DEFAULT_WEIGHT, colour);
    }

    double getWidth(final CharSequence text,
                    final double scale,
                    final int weight);

    default double getWidth(final CharSequence text, final double scale) {
        return this.getWidth(text, scale, DEFAULT_WEIGHT);
    }

    default double getWidth(final CharSequence text, final int weight) {
        return this.getWidth(text, DEFAULT_SCALE, weight);
    }

    default double getWidth(final CharSequence text) {
        return this.getWidth(text, DEFAULT_SCALE, DEFAULT_WEIGHT);
    }

    double getHeight(final CharSequence text,
                     final double scale,
                     final int weight);

    default double getHeight(final CharSequence text, final double scale) {
        return this.getHeight(text, scale, DEFAULT_WEIGHT);
    }

    default double getHeight(final CharSequence text, final int weight) {
        return this.getHeight(text, DEFAULT_SCALE, weight);
    }

    default double getHeight(final CharSequence text) {
        return this.getHeight(text, DEFAULT_SCALE, DEFAULT_WEIGHT);
    }
}
