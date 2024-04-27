package me.AquaVit.liquidSense.API;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class Menu {
    public static List<Menu> instances = new ArrayList<>();
    public double x;
    public double y;
    private double motionX;
    private double motionY;
    private final double radius;
    public static double maxMotion = 0.8;
    //private final int blink = 0;

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public Menu(double x, double y) {
        this.x = x;
        this.y = y;
        maxMotion = 1.5;
        this.motionY = Math.random() * maxMotion;
        this.motionX = Math.random() * maxMotion;
        instances.add(this);
        this.radius = Math.random() + 0.1;
    }

    public double distance(double x, double y) {
        double a = this.x - x;
        double b = this.y - y;
        return Math.sqrt(a * a + b * b);
    }

    public void drawLineTo(Menu ball) {
        GL11.glPushMatrix();
        GL11.glBegin(1);
        GL11.glColor4f(255.0f, 255.0f, 255.0f, 255.0f);
        GL11.glVertex2d(this.getX(), this.getY());
        GL11.glVertex2d(ball.getX(), ball.getY());
        GL11.glEnd();
        GL11.glPopMatrix();
    }

    private void checkOutOfFrame() {
        ScaledResolution resolution = new ScaledResolution(Minecraft.getMinecraft());
        if (this.x > (double)resolution.getScaledWidth() + this.radius) {
            this.x = -this.radius;
            this.y = (double)resolution.getScaledHeight() - this.y;
        } else if (this.x <= -this.radius) {
            this.x = (double)resolution.getScaledWidth() + this.radius;
            this.y = (double)resolution.getScaledHeight() - this.y;
        }
        if (this.y > (double)resolution.getScaledHeight() + this.radius) {
            this.y = -this.radius;
            this.x = (double)resolution.getScaledWidth() - this.x;
        } else if (this.y <= -this.radius) {
            this.y = (double)resolution.getScaledHeight() + this.radius;
            this.x = (double)resolution.getScaledWidth() - this.x;
        }
    }

    private void animate() {
        this.checkOutOfFrame();
        this.motionY += Math.random() / 20.0 - 0.025;
        this.motionX += Math.random() / 20.0 - 0.025;
        if (this.motionY > maxMotion) {
            this.motionY = maxMotion;
        } else if (this.motionY < 0.0) {
            this.motionY = 0.0;
        }
        if (this.motionX > maxMotion) {
            this.motionX = maxMotion;
        } else if (this.motionX < 0.0) {
            this.motionX = 0.0;
        }
        this.y -= this.motionY;
        this.x -= this.motionX;
    }

    public void renderAsSnow() {
        this.animate();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glPushMatrix();
        GL11.glBegin(6);
        for (double d = 0.0; d <= 100.0; d += 4.0) {
            double Angle = d * 0.16283185307179587;
            double Y = this.radius * Math.cos(Angle) + this.y;
            double X = this.radius * Math.sin(Angle) + this.x;
            GL11.glVertex2d(X, Y);
        }
        GL11.glEnd();
        GL11.glPopMatrix();
    }

    public void render() {
        this.animate();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glPushMatrix();
        GL11.glBegin(6);
        for (double d = 0.0; d <= 100.0; d += 4.0) {
            double Angle = d * 0.16283185307179587;
            double Y = this.radius * Math.cos(Angle) + this.y;
            double X = this.radius * Math.sin(Angle) + this.x;
            GL11.glVertex2d(X, Y);
        }
        GL11.glEnd();
        GL11.glPopMatrix();
    }
}