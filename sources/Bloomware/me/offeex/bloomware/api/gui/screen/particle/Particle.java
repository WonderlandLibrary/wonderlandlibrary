package me.offeex.bloomware.api.gui.screen.particle;

import me.offeex.bloomware.api.structure.ColorMutable;
import me.offeex.bloomware.api.util.RenderUtil;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;

public class Particle {
    private int x;
    private int y;
    private int size;
    private final int speed;
    private final int wind;
    private double dx, dy;
    private final ColorMutable color;

    public Particle(int x, int y, int size, int speed, int wind, ColorMutable color) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.speed = speed;
        this.wind = wind;
        this.color = color;
    }

    public void updateMovement() {
        this.x += dx > 0 ? Math.ceil(dx) : Math.floor(dx);
        this.y += Math.ceil(dy);
    }

    public void move() {
        dx = speed * Math.cos(Math.toRadians(wind));
        dy = speed * Math.sin(Math.toRadians(wind));
    }

    public void draw(MatrixStack stack, boolean glow) {
        if (glow) RenderUtil.INSTANCE.drawGlow2D(stack, x, y - 3, x + size, y + size + 3, color.withAlphaToRGBA(70));
        DrawableHelper.fill(stack, x, y, x + size, y + size, color.getArgb());
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
