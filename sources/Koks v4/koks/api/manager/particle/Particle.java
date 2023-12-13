package koks.api.manager.particle;

import koks.api.utils.Resolution;
import lombok.Getter;
import lombok.Setter;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import io.netty.util.internal.ThreadLocalRandom;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

@Getter @Setter
public class Particle {
    private double x;
    private double y;
    private double velocityX;
    private double velocityY;
    private double gravity = 7500;
    private double mass = 10000;
    private double time = 0.1;
    public Particle() {
        final Resolution sr = Resolution.getResolution();
        x = ThreadLocalRandom.current().nextInt(0, sr.getWidth());
        y = ThreadLocalRandom.current().nextInt(0, sr.getHeight());
        velocityX = ThreadLocalRandom.current().nextDouble(-1,1)*100;
        velocityY = ThreadLocalRandom.current().nextDouble(-1,1)*100;
    }
    public void update(Resolution sr, float gravity, int mouseX, int mouseY, double scrollOffset, double scrollSpeed) {
        mouseY += scrollOffset;
        int minDist = 7;
        time = 0.01;
        velocityX *= 0.98;
        velocityY *= 0.98;
        if (gravity != 0) {
            double distX = (double)mouseX - x;
            double distY = (double)mouseY - y;
            double dist2 = Math.pow(distX, 2) + Math.pow(distY, 2);
            double dist = Math.sqrt(dist2);
            distX /= dist;
            distY /= dist;
            double accelX = (double)gravity * this.gravity * mass * distX / dist2;
            double accelY = (double)gravity * this.gravity * mass * distY / dist2;
            double speed = Math.sqrt(accelX*accelX + accelY*accelY);
            if (speed >= 10500) {
                double angle = angularPoints(0, 0, accelX, accelY);
                double m = Math.min(10500,10000+speed/10000);
                accelX = m * Math.cos(Math.toRadians(angle));
                accelY = m * Math.sin(Math.toRadians(angle));
            }
            velocityX += accelX*time;
            velocityY += accelY*time;
        }
        if (Math.abs(velocityX) < 0.04 && Math.abs(velocityY) < 0.04) {
            velocityX = ThreadLocalRandom.current().nextDouble(-100,100);
            velocityY = ThreadLocalRandom.current().nextDouble(-100,100);
        }
        double preX = x;
        double preY = y;
        x += velocityX*time;
        y += velocityY*time;
        int correction = 0;
        if (x >= sr.getWidth()) {
            x = preX;
            correction = -1;
        }
        if (x <= 0) {
            x = preX;
            correction = -1;
        }
        if (y >= sr.getHeight()+scrollOffset) {
            y = preY;
            correction = 1;
        }
        if (y > sr.getHeight()+scrollOffset+velocityY*time+1) {
            y = sr.getHeight()+scrollOffset-2-ThreadLocalRandom.current().nextDouble(0, 0.1);
            double distFromMid = Math.abs(x-(double)Display.getWidth()/2d);
            double d = distFromMid/(Display.getWidth()/2F)*7;
            double v = scrollSpeed*(d);
            velocityY += v;
        }
        if (y <= 0) {
            y = preY;
            correction = 1;
        }
        if (correction != 0) {
            switch (correction) {
                case -1:
                    velocityX = -velocityX;
                    break;
                case 1:
                    velocityY = -velocityY;
                    break;
            }
        }
//		if (velocityX > 900) velocityX = 900;
//		if (velocityY > 900) velocityY = 900;
//		if (velocityX < -900) velocityX = -900;
//		if (velocityY < -900) velocityY = -900;
    }
    public void render(double scrollOffset) {
        GL11.glColor4f(1, 1, 1, 1);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION);
        worldrenderer.pos((double) x, (double) y + 0.5f-scrollOffset, 0.0D).endVertex();
        worldrenderer.pos((double) x + 0.5f, (double) y + 0.5f-scrollOffset, 0.0D).endVertex();
        worldrenderer.pos((double) x + 0.5f, (double) y-scrollOffset, 0.0D).endVertex();
        worldrenderer.pos((double) x, (double) y-scrollOffset, 0.0D).endVertex();
        tessellator.draw();
    }
    public static double[] pointFromAngle(double x,double y,double length,double angle) {
        angle *= Math.PI;
        angle /= 180;
        double x1 = Math.cos(angle);
        double y1 = Math.sin(angle);
        x1 *= length;
        y1 *= length;
        x1 += x;
        y1 += y;
        return new double[] {x1,y1};
    }
    public static double angularPoints(double x,double y,double x1,double y1) {
        double deltaX = x1 - x;
        double deltaY = y1 - y;
        double angle = Math.atan2(deltaY,deltaX)*180/Math.PI;
        if (angle < 0) angle += 360;
        return angle;
    }

}