package tech.atani.client.utility.render.particle;

import com.sun.javafx.geom.Vec2f;
import net.minecraft.client.gui.ScaledResolution;
import tech.atani.client.utility.interfaces.Methods;
import tech.atani.client.utility.render.shader.shaders.RoundedShader;

import java.awt.*;
import java.util.concurrent.ThreadLocalRandom;

public class Particle implements Methods {

    private double x;
    private double y;

    private double xMotion = 0.0;
    private double yMotion = 0.0;

    private long lastMS;

    private Vec2f point;

    public Particle(double x, double y, ScaledResolution scaledResolution) {
        this.x = x;
        this.y = y;
        point = new Vec2f((float) ThreadLocalRandom.current().nextDouble(scaledResolution.getScaledWidth()), (float) ThreadLocalRandom.current().nextDouble(scaledResolution.getScaledHeight()));
    }

    public void render(double mouseX, double mouseY, double animation) {
        Vec2f position = new Vec2f((float) x, (float) y);
        double dist = (1.0 / Math.sqrt(new Vec2f((float) mouseX, (float) mouseY).distanceSq(position)) * 16.0);
        dist = Math.min(dist, 2.0);

        double newMotionX = (x - mouseX) * dist;
        double newMotionY = (y - mouseY) * dist;

        // Get to the random point
        newMotionX += (point.x - x) * 0.7 * animation;
        newMotionY += (point.y - y) * 0.7 * animation;

        xMotion += newMotionX;
        yMotion += newMotionY;

        long delta = System.currentTimeMillis() - this.lastMS;
        this.lastMS = System.currentTimeMillis();

        double deltaTime = Math.min(delta / 144F, 1.0);

        x += xMotion * deltaTime;
        y += yMotion * deltaTime;

        xMotion *= 0.98 * deltaTime;
        yMotion *= 0.98 * deltaTime;

        Color fillColor = Color.white;
        fillColor = new Color(fillColor.getRed(), fillColor.getGreen(), fillColor.getBlue(), (int) (animation * 255));
        RoundedShader.drawRound((float) x, (float) y, 2, 2, 1, fillColor);
    }
}