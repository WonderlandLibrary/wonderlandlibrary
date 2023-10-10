/*
 * Decompiled with CFR 0.152.
 */
package org.newdawn.slick.tests;

import java.util.ArrayList;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.GeomUtil;
import org.newdawn.slick.geom.GeomUtilListener;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.geom.Vector2f;

public class GeomUtilTest
extends BasicGame
implements GeomUtilListener {
    private Shape source;
    private Shape cut;
    private Shape[] result;
    private ArrayList points = new ArrayList();
    private ArrayList marks = new ArrayList();
    private ArrayList exclude = new ArrayList();
    private boolean dynamic;
    private GeomUtil util = new GeomUtil();
    private int xp;
    private int yp;
    private Circle circle;
    private Shape rect;
    private Polygon star;
    private boolean union;

    public GeomUtilTest() {
        super("GeomUtilTest");
    }

    public void init() {
        Polygon source = new Polygon();
        source.addPoint(100.0f, 100.0f);
        source.addPoint(150.0f, 80.0f);
        source.addPoint(210.0f, 120.0f);
        source.addPoint(340.0f, 150.0f);
        source.addPoint(150.0f, 200.0f);
        source.addPoint(120.0f, 250.0f);
        this.source = source;
        this.circle = new Circle(0.0f, 0.0f, 50.0f);
        this.rect = new Rectangle(-100.0f, -40.0f, 200.0f, 80.0f);
        this.star = new Polygon();
        float dis = 40.0f;
        for (int i2 = 0; i2 < 360; i2 += 30) {
            dis = dis == 40.0f ? 60.0f : 40.0f;
            double x2 = Math.cos(Math.toRadians(i2)) * (double)dis;
            double y2 = Math.sin(Math.toRadians(i2)) * (double)dis;
            this.star.addPoint((float)x2, (float)y2);
        }
        this.cut = this.circle;
        this.cut.setLocation(203.0f, 78.0f);
        this.xp = (int)this.cut.getCenterX();
        this.yp = (int)this.cut.getCenterY();
        this.makeBoolean();
    }

    public void init(GameContainer container) throws SlickException {
        this.util.setListener(this);
        this.init();
        container.setVSync(true);
    }

    public void update(GameContainer container, int delta) throws SlickException {
        if (container.getInput().isKeyPressed(57)) {
            boolean bl = this.dynamic = !this.dynamic;
        }
        if (container.getInput().isKeyPressed(28)) {
            this.union = !this.union;
            this.makeBoolean();
        }
        if (container.getInput().isKeyPressed(2)) {
            this.cut = this.circle;
            this.circle.setCenterX(this.xp);
            this.circle.setCenterY(this.yp);
            this.makeBoolean();
        }
        if (container.getInput().isKeyPressed(3)) {
            this.cut = this.rect;
            this.rect.setCenterX(this.xp);
            this.rect.setCenterY(this.yp);
            this.makeBoolean();
        }
        if (container.getInput().isKeyPressed(4)) {
            this.cut = this.star;
            this.star.setCenterX(this.xp);
            this.star.setCenterY(this.yp);
            this.makeBoolean();
        }
        if (this.dynamic) {
            this.xp = container.getInput().getMouseX();
            this.yp = container.getInput().getMouseY();
            this.makeBoolean();
        }
    }

    private void makeBoolean() {
        this.marks.clear();
        this.points.clear();
        this.exclude.clear();
        this.cut.setCenterX(this.xp);
        this.cut.setCenterY(this.yp);
        this.result = this.union ? this.util.union(this.source, this.cut) : this.util.subtract(this.source, this.cut);
    }

    public void render(GameContainer container, Graphics g2) throws SlickException {
        Vector2f pt;
        int i2;
        g2.drawString("Space - toggle movement of cutting shape", 530.0f, 10.0f);
        g2.drawString("1,2,3 - select cutting shape", 530.0f, 30.0f);
        g2.drawString("Mouse wheel - rotate shape", 530.0f, 50.0f);
        g2.drawString("Enter - toggle union/subtract", 530.0f, 70.0f);
        g2.drawString("MODE: " + (this.union ? "Union" : "Cut"), 530.0f, 200.0f);
        g2.setColor(Color.green);
        g2.draw(this.source);
        g2.setColor(Color.red);
        g2.draw(this.cut);
        g2.setColor(Color.white);
        for (i2 = 0; i2 < this.exclude.size(); ++i2) {
            pt = (Vector2f)this.exclude.get(i2);
            g2.drawOval(pt.x - 3.0f, pt.y - 3.0f, 7.0f, 7.0f);
        }
        g2.setColor(Color.yellow);
        for (i2 = 0; i2 < this.points.size(); ++i2) {
            pt = (Vector2f)this.points.get(i2);
            g2.fillOval(pt.x - 1.0f, pt.y - 1.0f, 3.0f, 3.0f);
        }
        g2.setColor(Color.white);
        for (i2 = 0; i2 < this.marks.size(); ++i2) {
            pt = (Vector2f)this.marks.get(i2);
            g2.fillOval(pt.x - 1.0f, pt.y - 1.0f, 3.0f, 3.0f);
        }
        g2.translate(0.0f, 300.0f);
        g2.setColor(Color.white);
        if (this.result != null) {
            for (i2 = 0; i2 < this.result.length; ++i2) {
                g2.draw(this.result[i2]);
            }
            g2.drawString("Polys:" + this.result.length, 10.0f, 100.0f);
            g2.drawString("X:" + this.xp, 10.0f, 120.0f);
            g2.drawString("Y:" + this.yp, 10.0f, 130.0f);
        }
    }

    public static void main(String[] argv) {
        try {
            AppGameContainer container = new AppGameContainer(new GeomUtilTest());
            container.setDisplayMode(800, 600, false);
            container.start();
        }
        catch (SlickException e2) {
            e2.printStackTrace();
        }
    }

    public void pointExcluded(float x2, float y2) {
        this.exclude.add(new Vector2f(x2, y2));
    }

    public void pointIntersected(float x2, float y2) {
        this.marks.add(new Vector2f(x2, y2));
    }

    public void pointUsed(float x2, float y2) {
        this.points.add(new Vector2f(x2, y2));
    }

    public void mouseWheelMoved(int change) {
        if (this.dynamic) {
            this.cut = change < 0 ? this.cut.transform(Transform.createRotateTransform((float)Math.toRadians(10.0), this.cut.getCenterX(), this.cut.getCenterY())) : this.cut.transform(Transform.createRotateTransform((float)Math.toRadians(-10.0), this.cut.getCenterX(), this.cut.getCenterY()));
        }
    }
}

