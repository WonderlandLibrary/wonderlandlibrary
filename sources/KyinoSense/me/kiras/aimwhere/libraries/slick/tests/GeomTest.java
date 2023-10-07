/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.FMLCommonHandler
 */
package me.kiras.aimwhere.libraries.slick.tests;

import me.kiras.aimwhere.libraries.slick.AppGameContainer;
import me.kiras.aimwhere.libraries.slick.BasicGame;
import me.kiras.aimwhere.libraries.slick.Color;
import me.kiras.aimwhere.libraries.slick.GameContainer;
import me.kiras.aimwhere.libraries.slick.Graphics;
import me.kiras.aimwhere.libraries.slick.SlickException;
import me.kiras.aimwhere.libraries.slick.geom.Circle;
import me.kiras.aimwhere.libraries.slick.geom.Ellipse;
import me.kiras.aimwhere.libraries.slick.geom.Rectangle;
import me.kiras.aimwhere.libraries.slick.geom.RoundedRectangle;
import me.kiras.aimwhere.libraries.slick.geom.Shape;
import me.kiras.aimwhere.libraries.slick.geom.Transform;
import me.kiras.aimwhere.libraries.slick.opengl.renderer.Renderer;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class GeomTest
extends BasicGame {
    private Shape rect = new Rectangle(100.0f, 100.0f, 100.0f, 100.0f);
    private Shape circle = new Circle(500.0f, 200.0f, 50.0f);
    private Shape rect1 = new Rectangle(150.0f, 120.0f, 50.0f, 100.0f).transform(Transform.createTranslateTransform(50.0f, 50.0f));
    private Shape rect2 = new Rectangle(310.0f, 210.0f, 50.0f, 100.0f).transform(Transform.createRotateTransform((float)Math.toRadians(45.0), 335.0f, 260.0f));
    private Shape circle1 = new Circle(150.0f, 90.0f, 30.0f);
    private Shape circle2 = new Circle(310.0f, 110.0f, 70.0f);
    private Shape circle3 = new Ellipse(510.0f, 150.0f, 70.0f, 70.0f);
    private Shape circle4 = new Ellipse(510.0f, 350.0f, 30.0f, 30.0f).transform(Transform.createTranslateTransform(-510.0f, -350.0f)).transform(Transform.createScaleTransform(2.0f, 2.0f)).transform(Transform.createTranslateTransform(510.0f, 350.0f));
    private Shape roundRect = new RoundedRectangle(50.0f, 175.0f, 100.0f, 100.0f, 20.0f);
    private Shape roundRect2 = new RoundedRectangle(50.0f, 280.0f, 50.0f, 50.0f, 20.0f, 20, 5);

    public GeomTest() {
        super("Geom Test");
    }

    @Override
    public void init(GameContainer container) throws SlickException {
    }

    @Override
    public void render(GameContainer container, Graphics g) {
        g.setColor(Color.white);
        g.drawString("Red indicates a collision, green indicates no collision", 50.0f, 420.0f);
        g.drawString("White are the targets", 50.0f, 435.0f);
        g.pushTransform();
        g.translate(100.0f, 100.0f);
        g.pushTransform();
        g.translate(-50.0f, -50.0f);
        g.scale(10.0f, 10.0f);
        g.setColor(Color.red);
        g.fillRect(0.0f, 0.0f, 5.0f, 5.0f);
        g.setColor(Color.white);
        g.drawRect(0.0f, 0.0f, 5.0f, 5.0f);
        g.popTransform();
        g.setColor(Color.green);
        g.fillRect(20.0f, 20.0f, 50.0f, 50.0f);
        g.popTransform();
        g.setColor(Color.white);
        g.draw(this.rect);
        g.draw(this.circle);
        g.setColor(this.rect1.intersects(this.rect) ? Color.red : Color.green);
        g.draw(this.rect1);
        g.setColor(this.rect2.intersects(this.rect) ? Color.red : Color.green);
        g.draw(this.rect2);
        g.setColor(this.roundRect.intersects(this.rect) ? Color.red : Color.green);
        g.draw(this.roundRect);
        g.setColor(this.circle1.intersects(this.rect) ? Color.red : Color.green);
        g.draw(this.circle1);
        g.setColor(this.circle2.intersects(this.rect) ? Color.red : Color.green);
        g.draw(this.circle2);
        g.setColor(this.circle3.intersects(this.circle) ? Color.red : Color.green);
        g.fill(this.circle3);
        g.setColor(this.circle4.intersects(this.circle) ? Color.red : Color.green);
        g.draw(this.circle4);
        g.fill(this.roundRect2);
        g.setColor(Color.blue);
        g.draw(this.roundRect2);
        g.setColor(Color.blue);
        g.draw(new Circle(100.0f, 100.0f, 50.0f));
        g.drawRect(50.0f, 50.0f, 100.0f, 100.0f);
    }

    @Override
    public void update(GameContainer container, int delta) {
    }

    @Override
    public void keyPressed(int key, char c) {
        if (key == 1) {
            FMLCommonHandler.instance().exitJava(0, true);
        }
    }

    public static void main(String[] argv) {
        try {
            Renderer.setRenderer(2);
            AppGameContainer container = new AppGameContainer(new GeomTest());
            container.setDisplayMode(800, 600, false);
            container.start();
        }
        catch (SlickException e) {
            e.printStackTrace();
        }
    }
}

