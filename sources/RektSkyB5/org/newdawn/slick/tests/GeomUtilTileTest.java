/*
 * Decompiled with CFR 0.152.
 */
package org.newdawn.slick.tests;

import java.util.ArrayList;
import java.util.HashSet;
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
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

public class GeomUtilTileTest
extends BasicGame
implements GeomUtilListener {
    private Shape source;
    private Shape cut;
    private Shape[] result;
    private GeomUtil util = new GeomUtil();
    private ArrayList original = new ArrayList();
    private ArrayList combined = new ArrayList();
    private ArrayList intersections = new ArrayList();
    private ArrayList used = new ArrayList();
    private ArrayList[][] quadSpace;
    private Shape[][] quadSpaceShapes;

    public GeomUtilTileTest() {
        super("GeomUtilTileTest");
    }

    private void generateSpace(ArrayList shapes, float minx, float miny, float maxx, float maxy, int segments) {
        this.quadSpace = new ArrayList[segments][segments];
        this.quadSpaceShapes = new Shape[segments][segments];
        float dx = (maxx - minx) / (float)segments;
        float dy = (maxy - miny) / (float)segments;
        for (int x2 = 0; x2 < segments; ++x2) {
            for (int y2 = 0; y2 < segments; ++y2) {
                this.quadSpace[x2][y2] = new ArrayList();
                Polygon segmentPolygon = new Polygon();
                segmentPolygon.addPoint(minx + dx * (float)x2, miny + dy * (float)y2);
                segmentPolygon.addPoint(minx + dx * (float)x2 + dx, miny + dy * (float)y2);
                segmentPolygon.addPoint(minx + dx * (float)x2 + dx, miny + dy * (float)y2 + dy);
                segmentPolygon.addPoint(minx + dx * (float)x2, miny + dy * (float)y2 + dy);
                for (int i2 = 0; i2 < shapes.size(); ++i2) {
                    Shape shape = (Shape)shapes.get(i2);
                    if (!this.collides(shape, segmentPolygon)) continue;
                    this.quadSpace[x2][y2].add(shape);
                }
                this.quadSpaceShapes[x2][y2] = segmentPolygon;
            }
        }
    }

    private void removeFromQuadSpace(Shape shape) {
        int segments = this.quadSpace.length;
        for (int x2 = 0; x2 < segments; ++x2) {
            for (int y2 = 0; y2 < segments; ++y2) {
                this.quadSpace[x2][y2].remove(shape);
            }
        }
    }

    private void addToQuadSpace(Shape shape) {
        int segments = this.quadSpace.length;
        for (int x2 = 0; x2 < segments; ++x2) {
            for (int y2 = 0; y2 < segments; ++y2) {
                if (!this.collides(shape, this.quadSpaceShapes[x2][y2])) continue;
                this.quadSpace[x2][y2].add(shape);
            }
        }
    }

    public void init() {
        int size = 10;
        int[][] map = new int[][]{{0, 0, 0, 0, 0, 0, 0, 3, 0, 0}, {0, 1, 1, 1, 0, 0, 1, 1, 1, 0}, {0, 1, 1, 0, 0, 0, 5, 1, 6, 0}, {0, 1, 2, 0, 0, 0, 4, 1, 1, 0}, {0, 1, 1, 0, 0, 0, 1, 1, 0, 0}, {0, 0, 0, 0, 3, 0, 1, 1, 0, 0}, {0, 0, 0, 1, 1, 0, 0, 0, 1, 0}, {0, 0, 0, 1, 1, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}};
        for (int x2 = 0; x2 < map[0].length; ++x2) {
            block9: for (int y2 = 0; y2 < map.length; ++y2) {
                if (map[y2][x2] == 0) continue;
                switch (map[y2][x2]) {
                    case 1: {
                        Polygon p2 = new Polygon();
                        p2.addPoint(x2 * 32, y2 * 32);
                        p2.addPoint(x2 * 32 + 32, y2 * 32);
                        p2.addPoint(x2 * 32 + 32, y2 * 32 + 32);
                        p2.addPoint(x2 * 32, y2 * 32 + 32);
                        this.original.add(p2);
                        continue block9;
                    }
                    case 2: {
                        Polygon poly = new Polygon();
                        poly.addPoint(x2 * 32, y2 * 32);
                        poly.addPoint(x2 * 32 + 32, y2 * 32);
                        poly.addPoint(x2 * 32, y2 * 32 + 32);
                        this.original.add(poly);
                        continue block9;
                    }
                    case 3: {
                        Circle ellipse = new Circle((float)(x2 * 32 + 16), (float)(y2 * 32 + 32), 16.0f, 16);
                        this.original.add(ellipse);
                        continue block9;
                    }
                    case 4: {
                        Polygon p2 = new Polygon();
                        p2.addPoint(x2 * 32 + 32, y2 * 32);
                        p2.addPoint(x2 * 32 + 32, y2 * 32 + 32);
                        p2.addPoint(x2 * 32, y2 * 32 + 32);
                        this.original.add(p2);
                        continue block9;
                    }
                    case 5: {
                        Polygon p3 = new Polygon();
                        p3.addPoint(x2 * 32, y2 * 32);
                        p3.addPoint(x2 * 32 + 32, y2 * 32);
                        p3.addPoint(x2 * 32 + 32, y2 * 32 + 32);
                        this.original.add(p3);
                        continue block9;
                    }
                    case 6: {
                        Polygon p4 = new Polygon();
                        p4.addPoint(x2 * 32, y2 * 32);
                        p4.addPoint(x2 * 32 + 32, y2 * 32);
                        p4.addPoint(x2 * 32, y2 * 32 + 32);
                        this.original.add(p4);
                    }
                }
            }
        }
        long before = System.currentTimeMillis();
        this.generateSpace(this.original, 0.0f, 0.0f, (size + 1) * 32, (size + 1) * 32, 8);
        this.combined = this.combineQuadSpace();
        long after = System.currentTimeMillis();
        System.out.println("Combine took: " + (after - before));
        System.out.println("Combine result: " + this.combined.size());
    }

    private ArrayList combineQuadSpace() {
        boolean updated = true;
        while (updated) {
            updated = false;
            for (int x2 = 0; x2 < this.quadSpace.length; ++x2) {
                for (int y2 = 0; y2 < this.quadSpace.length; ++y2) {
                    ArrayList shapes = this.quadSpace[x2][y2];
                    int before = shapes.size();
                    this.combine(shapes);
                    int after = shapes.size();
                    updated |= before != after;
                }
            }
        }
        HashSet result = new HashSet();
        for (int x3 = 0; x3 < this.quadSpace.length; ++x3) {
            for (int y3 = 0; y3 < this.quadSpace.length; ++y3) {
                result.addAll(this.quadSpace[x3][y3]);
            }
        }
        return new ArrayList(result);
    }

    private ArrayList combine(ArrayList shapes) {
        ArrayList last = shapes;
        ArrayList current = shapes;
        boolean first = true;
        while (current.size() != last.size() || first) {
            first = false;
            last = current;
            current = this.combineImpl(current);
        }
        ArrayList<Shape> pruned = new ArrayList<Shape>();
        for (int i2 = 0; i2 < current.size(); ++i2) {
            pruned.add(((Shape)current.get(i2)).prune());
        }
        return pruned;
    }

    private ArrayList combineImpl(ArrayList shapes) {
        ArrayList result = new ArrayList(shapes);
        if (this.quadSpace != null) {
            result = shapes;
        }
        for (int i2 = 0; i2 < shapes.size(); ++i2) {
            Shape first = (Shape)shapes.get(i2);
            for (int j2 = i2 + 1; j2 < shapes.size(); ++j2) {
                Shape[] joined;
                Shape second = (Shape)shapes.get(j2);
                if (!first.intersects(second) || (joined = this.util.union(first, second)).length != 1) continue;
                if (this.quadSpace != null) {
                    this.removeFromQuadSpace(first);
                    this.removeFromQuadSpace(second);
                    this.addToQuadSpace(joined[0]);
                } else {
                    result.remove(first);
                    result.remove(second);
                    result.add(joined[0]);
                }
                return result;
            }
        }
        return result;
    }

    public boolean collides(Shape shape1, Shape shape2) {
        float[] pt;
        int i2;
        if (shape1.intersects(shape2)) {
            return true;
        }
        for (i2 = 0; i2 < shape1.getPointCount(); ++i2) {
            pt = shape1.getPoint(i2);
            if (!shape2.contains(pt[0], pt[1])) continue;
            return true;
        }
        for (i2 = 0; i2 < shape2.getPointCount(); ++i2) {
            pt = shape2.getPoint(i2);
            if (!shape1.contains(pt[0], pt[1])) continue;
            return true;
        }
        return false;
    }

    public void init(GameContainer container) throws SlickException {
        this.util.setListener(this);
        this.init();
    }

    public void update(GameContainer container, int delta) throws SlickException {
    }

    public void render(GameContainer container, Graphics g2) throws SlickException {
        Shape shape;
        int i2;
        g2.setColor(Color.green);
        for (i2 = 0; i2 < this.original.size(); ++i2) {
            shape = (Shape)this.original.get(i2);
            g2.draw(shape);
        }
        g2.setColor(Color.white);
        if (this.quadSpaceShapes != null) {
            g2.draw(this.quadSpaceShapes[0][0]);
        }
        g2.translate(0.0f, 320.0f);
        for (i2 = 0; i2 < this.combined.size(); ++i2) {
            g2.setColor(Color.white);
            shape = (Shape)this.combined.get(i2);
            g2.draw(shape);
            for (int j2 = 0; j2 < shape.getPointCount(); ++j2) {
                g2.setColor(Color.yellow);
                float[] pt = shape.getPoint(j2);
                g2.fillOval(pt[0] - 1.0f, pt[1] - 1.0f, 3.0f, 3.0f);
            }
        }
    }

    public static void main(String[] argv) {
        try {
            AppGameContainer container = new AppGameContainer(new GeomUtilTileTest());
            container.setDisplayMode(800, 600, false);
            container.start();
        }
        catch (SlickException e2) {
            e2.printStackTrace();
        }
    }

    public void pointExcluded(float x2, float y2) {
    }

    public void pointIntersected(float x2, float y2) {
        this.intersections.add(new Vector2f(x2, y2));
    }

    public void pointUsed(float x2, float y2) {
        this.used.add(new Vector2f(x2, y2));
    }
}

