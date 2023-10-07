/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.BufferUtils
 *  org.lwjgl.opengl.GL11
 */
package me.kiras.aimwhere.libraries.slick.tests;

import java.nio.FloatBuffer;
import me.kiras.aimwhere.libraries.slick.AngelCodeFont;
import me.kiras.aimwhere.libraries.slick.Animation;
import me.kiras.aimwhere.libraries.slick.AppGameContainer;
import me.kiras.aimwhere.libraries.slick.BasicGame;
import me.kiras.aimwhere.libraries.slick.GameContainer;
import me.kiras.aimwhere.libraries.slick.Graphics;
import me.kiras.aimwhere.libraries.slick.Image;
import me.kiras.aimwhere.libraries.slick.SlickException;
import me.kiras.aimwhere.libraries.slick.SpriteSheet;
import me.kiras.aimwhere.libraries.slick.opengl.SlickCallable;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

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

    @Override
    public void init(GameContainer container) throws SlickException {
        this.image = new Image("testdata/rocket.png");
        this.back = new Image("testdata/sky.jpg");
        this.font = new AngelCodeFont("testdata/hiero.fnt", "testdata/hiero.png");
        SpriteSheet sheet = new SpriteSheet("testdata/homeranim.png", 36, 65);
        this.homer = new Animation(sheet, 0, 0, 7, 0, true, 150, true);
    }

    @Override
    public void render(GameContainer container, Graphics g) throws SlickException {
        g.scale(2.0f, 2.0f);
        g.fillRect(0.0f, 0.0f, 800.0f, 600.0f, this.back, 0.0f, 0.0f);
        g.resetTransform();
        g.drawImage(this.image, 100.0f, 100.0f);
        this.image.draw(100.0f, 200.0f, 80.0f, 200.0f);
        this.font.drawString(100.0f, 200.0f, "Text Drawn before the callable");
        SlickCallable callable = new SlickCallable(){

            @Override
            protected void performGLOperations() throws SlickException {
                SlickCallableTest.this.renderGL();
            }
        };
        callable.call();
        this.homer.draw(450.0f, 250.0f, 80.0f, 200.0f);
        this.font.drawString(150.0f, 300.0f, "Text Drawn after the callable");
    }

    public void renderGL() {
        FloatBuffer pos = BufferUtils.createFloatBuffer((int)4);
        pos.put(new float[]{5.0f, 5.0f, 10.0f, 0.0f}).flip();
        FloatBuffer red = BufferUtils.createFloatBuffer((int)4);
        red.put(new float[]{0.8f, 0.1f, 0.0f, 1.0f}).flip();
        GL11.glLight((int)16384, (int)4611, (FloatBuffer)pos);
        GL11.glEnable((int)16384);
        GL11.glEnable((int)2884);
        GL11.glEnable((int)2929);
        GL11.glEnable((int)2896);
        GL11.glMatrixMode((int)5889);
        GL11.glLoadIdentity();
        float h = 0.75f;
        GL11.glFrustum((double)-1.0, (double)1.0, (double)(-h), (double)h, (double)5.0, (double)60.0);
        GL11.glMatrixMode((int)5888);
        GL11.glLoadIdentity();
        GL11.glTranslatef((float)0.0f, (float)0.0f, (float)-40.0f);
        GL11.glRotatef((float)this.rot, (float)0.0f, (float)1.0f, (float)1.0f);
        GL11.glMaterial((int)1028, (int)5634, (FloatBuffer)red);
        this.gear(0.5f, 2.0f, 2.0f, 10, 0.7f);
    }

    private void gear(float inner_radius, float outer_radius, float width, int teeth, float tooth_depth) {
        float angle;
        int i;
        float r0 = inner_radius;
        float r1 = outer_radius - tooth_depth / 2.0f;
        float r2 = outer_radius + tooth_depth / 2.0f;
        float da = (float)Math.PI * 2 / (float)teeth / 4.0f;
        GL11.glShadeModel((int)7424);
        GL11.glNormal3f((float)0.0f, (float)0.0f, (float)1.0f);
        GL11.glBegin((int)8);
        for (i = 0; i <= teeth; ++i) {
            angle = (float)i * 2.0f * (float)Math.PI / (float)teeth;
            GL11.glVertex3f((float)(r0 * (float)Math.cos(angle)), (float)(r0 * (float)Math.sin(angle)), (float)(width * 0.5f));
            GL11.glVertex3f((float)(r1 * (float)Math.cos(angle)), (float)(r1 * (float)Math.sin(angle)), (float)(width * 0.5f));
            if (i >= teeth) continue;
            GL11.glVertex3f((float)(r0 * (float)Math.cos(angle)), (float)(r0 * (float)Math.sin(angle)), (float)(width * 0.5f));
            GL11.glVertex3f((float)(r1 * (float)Math.cos(angle + 3.0f * da)), (float)(r1 * (float)Math.sin(angle + 3.0f * da)), (float)(width * 0.5f));
        }
        GL11.glEnd();
        GL11.glBegin((int)7);
        for (i = 0; i < teeth; ++i) {
            angle = (float)i * 2.0f * (float)Math.PI / (float)teeth;
            GL11.glVertex3f((float)(r1 * (float)Math.cos(angle)), (float)(r1 * (float)Math.sin(angle)), (float)(width * 0.5f));
            GL11.glVertex3f((float)(r2 * (float)Math.cos(angle + da)), (float)(r2 * (float)Math.sin(angle + da)), (float)(width * 0.5f));
            GL11.glVertex3f((float)(r2 * (float)Math.cos(angle + 2.0f * da)), (float)(r2 * (float)Math.sin(angle + 2.0f * da)), (float)(width * 0.5f));
            GL11.glVertex3f((float)(r1 * (float)Math.cos(angle + 3.0f * da)), (float)(r1 * (float)Math.sin(angle + 3.0f * da)), (float)(width * 0.5f));
        }
        GL11.glEnd();
        GL11.glNormal3f((float)0.0f, (float)0.0f, (float)-1.0f);
        GL11.glBegin((int)8);
        for (i = 0; i <= teeth; ++i) {
            angle = (float)i * 2.0f * (float)Math.PI / (float)teeth;
            GL11.glVertex3f((float)(r1 * (float)Math.cos(angle)), (float)(r1 * (float)Math.sin(angle)), (float)(-width * 0.5f));
            GL11.glVertex3f((float)(r0 * (float)Math.cos(angle)), (float)(r0 * (float)Math.sin(angle)), (float)(-width * 0.5f));
            GL11.glVertex3f((float)(r1 * (float)Math.cos(angle + 3.0f * da)), (float)(r1 * (float)Math.sin(angle + 3.0f * da)), (float)(-width * 0.5f));
            GL11.glVertex3f((float)(r0 * (float)Math.cos(angle)), (float)(r0 * (float)Math.sin(angle)), (float)(-width * 0.5f));
        }
        GL11.glEnd();
        GL11.glBegin((int)7);
        for (i = 0; i < teeth; ++i) {
            angle = (float)i * 2.0f * (float)Math.PI / (float)teeth;
            GL11.glVertex3f((float)(r1 * (float)Math.cos(angle + 3.0f * da)), (float)(r1 * (float)Math.sin(angle + 3.0f * da)), (float)(-width * 0.5f));
            GL11.glVertex3f((float)(r2 * (float)Math.cos(angle + 2.0f * da)), (float)(r2 * (float)Math.sin(angle + 2.0f * da)), (float)(-width * 0.5f));
            GL11.glVertex3f((float)(r2 * (float)Math.cos(angle + da)), (float)(r2 * (float)Math.sin(angle + da)), (float)(-width * 0.5f));
            GL11.glVertex3f((float)(r1 * (float)Math.cos(angle)), (float)(r1 * (float)Math.sin(angle)), (float)(-width * 0.5f));
        }
        GL11.glEnd();
        GL11.glNormal3f((float)0.0f, (float)0.0f, (float)1.0f);
        GL11.glBegin((int)8);
        for (i = 0; i < teeth; ++i) {
            angle = (float)i * 2.0f * (float)Math.PI / (float)teeth;
            GL11.glVertex3f((float)(r1 * (float)Math.cos(angle)), (float)(r1 * (float)Math.sin(angle)), (float)(width * 0.5f));
            GL11.glVertex3f((float)(r1 * (float)Math.cos(angle)), (float)(r1 * (float)Math.sin(angle)), (float)(-width * 0.5f));
            float u = r2 * (float)Math.cos(angle + da) - r1 * (float)Math.cos(angle);
            float v = r2 * (float)Math.sin(angle + da) - r1 * (float)Math.sin(angle);
            float len = (float)Math.sqrt(u * u + v * v);
            GL11.glNormal3f((float)(v /= len), (float)(-(u /= len)), (float)0.0f);
            GL11.glVertex3f((float)(r2 * (float)Math.cos(angle + da)), (float)(r2 * (float)Math.sin(angle + da)), (float)(width * 0.5f));
            GL11.glVertex3f((float)(r2 * (float)Math.cos(angle + da)), (float)(r2 * (float)Math.sin(angle + da)), (float)(-width * 0.5f));
            GL11.glNormal3f((float)((float)Math.cos(angle)), (float)((float)Math.sin(angle)), (float)0.0f);
            GL11.glVertex3f((float)(r2 * (float)Math.cos(angle + 2.0f * da)), (float)(r2 * (float)Math.sin(angle + 2.0f * da)), (float)(width * 0.5f));
            GL11.glVertex3f((float)(r2 * (float)Math.cos(angle + 2.0f * da)), (float)(r2 * (float)Math.sin(angle + 2.0f * da)), (float)(-width * 0.5f));
            u = r1 * (float)Math.cos(angle + 3.0f * da) - r2 * (float)Math.cos(angle + 2.0f * da);
            v = r1 * (float)Math.sin(angle + 3.0f * da) - r2 * (float)Math.sin(angle + 2.0f * da);
            GL11.glNormal3f((float)v, (float)(-u), (float)0.0f);
            GL11.glVertex3f((float)(r1 * (float)Math.cos(angle + 3.0f * da)), (float)(r1 * (float)Math.sin(angle + 3.0f * da)), (float)(width * 0.5f));
            GL11.glVertex3f((float)(r1 * (float)Math.cos(angle + 3.0f * da)), (float)(r1 * (float)Math.sin(angle + 3.0f * da)), (float)(-width * 0.5f));
            GL11.glNormal3f((float)((float)Math.cos(angle)), (float)((float)Math.sin(angle)), (float)0.0f);
        }
        GL11.glVertex3f((float)(r1 * (float)Math.cos(0.0)), (float)(r1 * (float)Math.sin(0.0)), (float)(width * 0.5f));
        GL11.glVertex3f((float)(r1 * (float)Math.cos(0.0)), (float)(r1 * (float)Math.sin(0.0)), (float)(-width * 0.5f));
        GL11.glEnd();
        GL11.glShadeModel((int)7425);
        GL11.glBegin((int)8);
        for (i = 0; i <= teeth; ++i) {
            angle = (float)i * 2.0f * (float)Math.PI / (float)teeth;
            GL11.glNormal3f((float)(-((float)Math.cos(angle))), (float)(-((float)Math.sin(angle))), (float)0.0f);
            GL11.glVertex3f((float)(r0 * (float)Math.cos(angle)), (float)(r0 * (float)Math.sin(angle)), (float)(-width * 0.5f));
            GL11.glVertex3f((float)(r0 * (float)Math.cos(angle)), (float)(r0 * (float)Math.sin(angle)), (float)(width * 0.5f));
        }
        GL11.glEnd();
    }

    @Override
    public void update(GameContainer container, int delta) {
        this.rot += (float)delta * 0.1f;
    }

    public static void main(String[] argv) {
        try {
            AppGameContainer container = new AppGameContainer(new SlickCallableTest());
            container.setDisplayMode(800, 600, false);
            container.start();
        }
        catch (SlickException e) {
            e.printStackTrace();
        }
    }
}

