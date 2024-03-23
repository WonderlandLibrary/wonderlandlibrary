package dev.zprestige.prestige.client.ui.drawables;

public class Drawable {
    public float x;
    public float y;
    public float width;
    public float height;
    public String text;

    public Drawable(float x, float y, float width, float height, String text) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.text = text;
    }

    public float getX() {
        return this.x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return this.y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getWidth() {
        return this.width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return this.height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void clear() {
        this.text = null;
    }

    public void charTyped(char c, int n) {
    }

    public void keyPressed(int n, int n2, int n3) {
    }

    public void render(int n, int n2, float f, float f2) {
    }

    public void mouseClicked(double d, double d2, int n) {
    }

    public void mouseReleased(double d, double d2, int n) {
    }
}
