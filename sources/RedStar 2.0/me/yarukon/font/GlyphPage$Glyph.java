package me.yarukon.font;

class GlyphPage$Glyph {
    private int x;
    private int y;
    private int width;
    private int height;

    GlyphPage$Glyph(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    GlyphPage$Glyph() {
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    static int access$002(GlyphPage$Glyph x0, int x1) {
        x0.width = x1;
        return x0.width;
    }

    static int access$102(GlyphPage$Glyph x0, int x1) {
        x0.height = x1;
        return x0.height;
    }

    static int access$000(GlyphPage$Glyph x0) {
        return x0.width;
    }

    static int access$202(GlyphPage$Glyph x0, int x1) {
        x0.x = x1;
        return x0.x;
    }

    static int access$302(GlyphPage$Glyph x0, int x1) {
        x0.y = x1;
        return x0.y;
    }

    static int access$100(GlyphPage$Glyph x0) {
        return x0.height;
    }

    static int access$200(GlyphPage$Glyph x0) {
        return x0.x;
    }

    static int access$300(GlyphPage$Glyph x0) {
        return x0.y;
    }
}
