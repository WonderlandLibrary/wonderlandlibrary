package Velo.api.Util.Render;
import net.java.games.input.Mouse;
import net.minecraft.client.Minecraft;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.MouseHelper;

import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Particles {
    List<Particle> particles = new ArrayList<>();
    double maxDist;   
    public Particles(int amount) {
    	ScaledResolution w = new ScaledResolution(Minecraft.getMinecraft());
        Random r = new Random();
        for (int i = 0; i < amount; i++) {
            Particle c = new Particle();
            c.posX = r.nextInt(w.getScaledWidth());
            c.posY = r.nextInt(w.getScaledHeight());
            c.rotation = (Math.random() - 0.5) * Math.PI * 2;
            particles.add(c);
        }
    }
    public double x, y, width, height;
    

    public void tick() {
        ScaledResolution w = new ScaledResolution(Minecraft.getMinecraft());
        maxDist = Math.sqrt(w.getScaledWidth() * w.getScaledWidth() + w.getScaledHeight() * w.getScaledHeight()) / 9;
      
        for (Particle particle : particles) {
            double sin = Math.sin(particle.rotation);
            double cos = Math.cos(particle.rotation);
            particle.posX += sin * particle.speed;
            particle.posY += cos * particle.speed;
            particle.speed = 0.01f;
            MouseHelper m = new MouseHelper();
            double mDist = Math.sqrt(Math.pow(particle.posX - m.deltaX / 2, 2) + Math.pow(particle.posY - m.deltaY / 2, 2));
            if (mDist < maxDist / 2) {
                double ang = -Math.atan2(m.deltaX / 2 - particle.posX, particle.posY - m.deltaY / 2);
                ang = ang * (180 / Math.PI);
                particle.rotation = Math.toRadians(ang);
            }

            boolean bounced = false;
            boolean useState2 = false;
            if (particle.posX < 0) {
                particle.posX = 0;
                bounced = true;
                useState2 = true;
            }
            if (particle.posX > w.getScaledWidth()) {
                particle.posX = w.getScaledWidth();
                bounced = true;
                useState2 = true;
            }
            if (particle.posY < 0) {
                particle.posY = 0;
                bounced = true;
            }
            if (particle.posY > w.getScaledHeight()) {
                particle.posY = w.getScaledHeight();
                bounced = true;
            }
            if (bounced) {
                double rot = (useState2 ? 360 : 180) - Math.toDegrees(particle.rotation);
                particle.rotation = Math.toRadians(rot);
            }
        }
    }

    public void render() {
        for (Particle particle : particles) {
            for (Particle particle1 : particles) {
                double dist = Math.sqrt(Math.pow(particle.posX - particle1.posX, 2) + Math.pow(particle.posY - particle1.posY, 2));
                if (dist < maxDist) {
                    int red = (int) (dist / maxDist * 255);
                    int green = Math.abs(red - 255);
                    Color c1 = Color.getHSBColor((float) (((double) (System.currentTimeMillis() % 10000)) / 10000), (float) green / 255, 1);
                    Color c = new Color(c1.getRed(), c1.getGreen(), c1.getBlue(), green);
                    renderLineScreen(particle.posX, particle.posY, particle1.posX, particle1.posY, c, 1);
                }
            }
            MouseHelper m = new MouseHelper();

            double mDist = Math.sqrt(Math.pow(particle.posX - m.deltaX / 2, 2) + Math.pow(particle.posY - m.deltaY / 2, 2));
            if (mDist < maxDist * 2) {
                int red = (int) (mDist / (maxDist * 2) * 255);
                int green = Math.abs(red - 255);
                Color c1 = Color.getHSBColor((float) (((double) (System.currentTimeMillis() % 10000)) / 10000), (float) green / 255, 1);
                Color c = new Color(c1.getRed(), c1.getGreen(), c1.getBlue(), green);
                renderLineScreen(particle.posX, particle.posY, m.deltaX / 2, m.deltaY / 2, c, 1);
            }
        }
    }

    void renderLineScreen(double fromX, double fromY, double toX, double toY, Color col, int width) {
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glLineWidth(width);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glColor4f(col.getRed() / 255F, col.getGreen() / 255F, col.getBlue() / 255F, col.getAlpha() / 255F);
        GL11.glBegin(GL11.GL_LINES);
        {
            GL11.glVertex2d(fromX, fromY);
            GL11.glVertex2d(toX, toY);
        }
        GL11.glEnd();
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glPopMatrix();
    }
}

class Particle {
    public double posX;
    public double posY;
    public double rotation;
    public double speed;

    public Particle() {
        posX = 0;
        posY = 0;
        rotation = 0;
        speed = -12312;
    }
    
    public double[] getRelativeCoords(int x, int y) {
        //if(!isPosInRectangle(x, y))
        //	return null;
        
        return new double[]{(double) x - x, (double) y -y};
    }
    
}