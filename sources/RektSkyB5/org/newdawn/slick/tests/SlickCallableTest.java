/*
 * Decompiled with CFR 0.152.
 */
package org.newdawn.slick.tests;

import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.Animation;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.opengl.SlickCallable;

public class SlickCallableTest
extends BasicGame {
    private Image image;
    private Image back;
    private float rot;
    private AngelCodeFont font;
    private Animation homer;

    public SlickCallableTest() {
        super("Slick Callable Test");
    }

    public void init(GameContainer container) throws SlickException {
        this.image = new Image("testdata/rocket.png");
        this.back = new Image("testdata/sky.jpg");
        this.font = new AngelCodeFont("testdata/hiero.fnt", "testdata/hiero.png");
        SpriteSheet sheet = new SpriteSheet("testdata/homeranim.png", 36, 65);
        this.homer = new Animation(sheet, 0, 0, 7, 0, true, 150, true);
    }

    public void render(GameContainer container, Graphics g2) throws SlickException {
        g2.scale(2.0f, 2.0f);
        g2.fillRect(0.0f, 0.0f, 800.0f, 600.0f, this.back, 0.0f, 0.0f);
        g2.resetTransform();
        g2.drawImage(this.image, 100.0f, 100.0f);
        this.image.draw(100.0f, 200.0f, 80.0f, 200.0f);
        this.font.drawString(100.0f, 200.0f, "Text Drawn before the callable");
        SlickCallable callable = new SlickCallable(){

            protected void performGLOperations() throws SlickException {
                SlickCallableTest.this.renderGL();
            }
        };
        callable.call();
        this.homer.draw(450.0f, 250.0f, 80.0f, 200.0f);
        this.font.drawString(150.0f, 300.0f, "Text Drawn after the callable");
    }

    public void renderGL() {
        FloatBuffer pos = BufferUtils.createFloatBuffer(4);
        pos.put(new float[]{5.0f, 5.0f, 10.0f, 0.0f}).flip();
        FloatBuffer red = BufferUtils.createFloatBuffer(4);
        red.put(new float[]{0.8f, 0.1f, 0.0f, 1.0f}).flip();
        GL11.glLight(16384, 4611, pos);
        GL11.glEnable(16384);
        GL11.glEnable(2884);
        GL11.glEnable(2929);
        GL11.glEnable(2896);
        GL11.glMatrixMode(5889);
        GL11.glLoadIdentity();
        float h2 = 0.75f;
        GL11.glFrustum(-1.0, 1.0, -h2, h2, 5.0, 60.0);
        GL11.glMatrixMode(5888);
        GL11.glLoadIdentity();
        GL11.glTranslatef(0.0f, 0.0f, -40.0f);
        GL11.glRotatef(this.rot, 0.0f, 1.0f, 1.0f);
        GL11.glMaterial(1028, 5634, red);
        this.gear(0.5f, 2.0f, 2.0f, 10, 0.7f);
    }

    private void gear(float inner_radius, float outer_radius, float width, int teeth, float tooth_depth) {
        float angle;
        int i2;
        float r0 = inner_radius;
        float r1 = outer_radius - tooth_depth / 2.0f;
        float r2 = outer_radius + tooth_depth / 2.0f;
        float da = (float)Math.PI * 2 / (float)teeth / 4.0f;
        GL11.glShadeModel(7424);
        GL11.glNormal3f(0.0f, 0.0f, 1.0f);
        GL11.glBegin(8);
        for (i2 = 0; i2 <= teeth; ++i2) {
            angle = (float)i2 * 2.0f * (float)Math.PI / (float)teeth;
            GL11.glVertex3f(r0 * (float)Math.cos(angle), r0 * (float)Math.sin(angle), width * 0.5f);
            GL11.glVertex3f(r1 * (float)Math.cos(angle), r1 * (float)Math.sin(angle), width * 0.5f);
            if (i2 >= teeth) continue;
            GL11.glVertex3f(r0 * (float)Math.cos(angle), r0 * (float)Math.sin(angle), width * 0.5f);
            GL11.glVertex3f(r1 * (float)Math.cos(angle + 3.0f * da), r1 * (float)Math.sin(angle + 3.0f * da), width * 0.5f);
        }
        GL11.glEnd();
        GL11.glBegin(7);
        for (i2 = 0; i2 < teeth; ++i2) {
            angle = (float)i2 * 2.0f * (float)Math.PI / (float)teeth;
            GL11.glVertex3f(r1 * (float)Math.cos(angle), r1 * (float)Math.sin(angle), width * 0.5f);
            GL11.glVertex3f(r2 * (float)Math.cos(angle + da), r2 * (float)Math.sin(angle + da), width * 0.5f);
            GL11.glVertex3f(r2 * (float)Math.cos(angle + 2.0f * da), r2 * (float)Math.sin(angle + 2.0f * da), width * 0.5f);
            GL11.glVertex3f(r1 * (float)Math.cos(angle + 3.0f * da), r1 * (float)Math.sin(angle + 3.0f * da), width * 0.5f);
        }
        GL11.glEnd();
        GL11.glNormal3f(0.0f, 0.0f, -1.0f);
        GL11.glBegin(8);
        for (i2 = 0; i2 <= teeth; ++i2) {
            angle = (float)i2 * 2.0f * (float)Math.PI / (float)teeth;
            GL11.glVertex3f(r1 * (float)Math.cos(angle), r1 * (float)Math.sin(angle), -width * 0.5f);
            GL11.glVertex3f(r0 * (float)Math.cos(angle), r0 * (float)Math.sin(angle), -width * 0.5f);
            GL11.glVertex3f(r1 * (float)Math.cos(angle + 3.0f * da), r1 * (float)Math.sin(angle + 3.0f * da), -width * 0.5f);
            GL11.glVertex3f(r0 * (float)Math.cos(angle), r0 * (float)Math.sin(angle), -width * 0.5f);
        }
        GL11.glEnd();
        GL11.glBegin(7);
        for (i2 = 0; i2 < teeth; ++i2) {
            angle = (float)i2 * 2.0f * (float)Math.PI / (float)teeth;
            GL11.glVertex3f(r1 * (float)Math.cos(angle + 3.0f * da), r1 * (float)Math.sin(angle + 3.0f * da), -width * 0.5f);
            GL11.glVertex3f(r2 * (float)Math.cos(angle + 2.0f * da), r2 * (float)Math.sin(angle + 2.0f * da), -width * 0.5f);
            GL11.glVertex3f(r2 * (float)Math.cos(angle + da), r2 * (float)Math.sin(angle + da), -width * 0.5f);
            GL11.glVertex3f(r1 * (float)Math.cos(angle), r1 * (float)Math.sin(angle), -width * 0.5f);
        }
        GL11.glEnd();
        GL11.glNormal3f(0.0f, 0.0f, 1.0f);
        GL11.glBegin(8);
        for (i2 = 0; i2 < teeth; ++i2) {
            angle = (float)i2 * 2.0f * (float)Math.PI / (float)teeth;
            GL11.glVertex3f(r1 * (float)Math.cos(angle), r1 * (float)Math.sin(angle), width * 0.5f);
            GL11.glVertex3f(r1 * (float)Math.cos(angle), r1 * (float)Math.sin(angle), -width * 0.5f);
            float u2 = r2 * (float)Math.cos(angle + da) - r1 * (float)Math.cos(angle);
            float v2 = r2 * (float)Math.sin(angle + da) - r1 * (float)Math.sin(angle);
            float len = (float)Math.sqrt(u2 * u2 + v2 * v2);
            GL11.glNormal3f(v2 /= len, -(u2 /= len), 0.0f);
            GL11.glVertex3f(r2 * (float)Math.cos(angle + da), r2 * (float)Math.sin(angle + da), width * 0.5f);
            GL11.glVertex3f(r2 * (float)Math.cos(angle + da), r2 * (float)Math.sin(angle + da), -width * 0.5f);
            GL11.glNormal3f((float)Math.cos(angle), (float)Math.sin(angle), 0.0f);
            GL11.glVertex3f(r2 * (float)Math.cos(angle + 2.0f * da), r2 * (float)Math.sin(angle + 2.0f * da), width * 0.5f);
            GL11.glVertex3f(r2 * (float)Math.cos(angle + 2.0f * da), r2 * (float)Math.sin(angle + 2.0f * da), -width * 0.5f);
            u2 = r1 * (float)Math.cos(angle + 3.0f * da) - r2 * (float)Math.cos(angle + 2.0f * da);
            v2 = r1 * (float)Math.sin(angle + 3.0f * da) - r2 * (float)Math.sin(angle + 2.0f * da);
            GL11.glNormal3f(v2, -u2, 0.0f);
            GL11.glVertex3f(r1 * (float)Math.cos(angle + 3.0f * da), r1 * (float)Math.sin(angle + 3.0f * da), width * 0.5f);
            GL11.glVertex3f(r1 * (float)Math.cos(angle + 3.0f * da), r1 * (float)Math.sin(angle + 3.0f * da), -width * 0.5f);
            GL11.glNormal3f((float)Math.cos(angle), (float)Math.sin(angle), 0.0f);
        }
        GL11.glVertex3f(r1 * (float)Math.cos(0.0), r1 * (float)Math.sin(0.0), width * 0.5f);
        GL11.glVertex3f(r1 * (float)Math.cos(0.0), r1 * (float)Math.sin(0.0), -width * 0.5f);
        GL11.glEnd();
        GL11.glShadeModel(7425);
        GL11.glBegin(8);
        for (i2 = 0; i2 <= teeth; ++i2) {
            angle = (float)i2 * 2.0f * (float)Math.PI / (float)teeth;
            GL11.glNormal3f(-((float)Math.cos(angle)), -((float)Math.sin(angle)), 0.0f);
            GL11.glVertex3f(r0 * (float)Math.cos(angle), r0 * (float)Math.sin(angle), -width * 0.5f);
            GL11.glVertex3f(r0 * (float)Math.cos(angle), r0 * (float)Math.sin(angle), width * 0.5f);
        }
        GL11.glEnd();
    }

    public void update(GameContainer container, int delta) {
        this.rot += (float)delta * 0.1f;
    }

    public static void main(String[] argv) {
        try {
            AppGameContainer container = new AppGameContainer(new SlickCallableTest());
            container.setDisplayMode(800, 600, false);
            container.start();
        }
        catch (SlickException e2) {
            e2.printStackTrace();
        }
    }
}

