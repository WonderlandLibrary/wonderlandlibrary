/*
 * Decompiled with CFR 0.152.
 */
package me.kiras.aimwhere.libraries.slick.tests;

import java.util.ArrayList;
import java.util.HashSet;
import me.kiras.aimwhere.libraries.slick.AppGameContainer;
import me.kiras.aimwhere.libraries.slick.BasicGame;
import me.kiras.aimwhere.libraries.slick.Color;
import me.kiras.aimwhere.libraries.slick.GameContainer;
import me.kiras.aimwhere.libraries.slick.Graphics;
import me.kiras.aimwhere.libraries.slick.SlickException;
import me.kiras.aimwhere.libraries.slick.geom.Circle;
import me.kiras.aimwhere.libraries.slick.geom.GeomUtil;
import me.kiras.aimwhere.libraries.slick.geom.GeomUtilListener;
import me.kiras.aimwhere.libraries.slick.geom.Polygon;
import me.kiras.aimwhere.libraries.slick.geom.Shape;
import me.kiras.aimwhere.libraries.slick.geom.Vector2f;

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
        for (int x = 0; x < segments; ++x) {
            for (int y = 0; y < segments; ++y) {
                this.quadSpace[x][y] = new ArrayList();
                Polygon segmentPolygon = new Polygon();
                segmentPolygon.addPoint(minx + dx * (float)x, miny + dy * (float)y);
                segmentPolygon.addPoint(minx + dx * (float)x + dx, miny + dy * (float)y);
                segmentPolygon.addPoint(minx + dx * (float)x + dx, miny + dy * (float)y + dy);
                segmentPolygon.addPoint(minx + dx * (float)x, miny + dy * (float)y + dy);
                for (int i = 0; i < shapes.size(); ++i) {
                    Shape shape = (Shape)shapes.get(i);
                    if (!this.collides(shape, segmentPolygon)) continue;
                    this.quadSpace[x][y].add(shape);
                }
                this.quadSpaceShapes[x][y] = segmentPolygon;
            }
        }
    }

    private void removeFromQuadSpace(Shape shape) {
        int segments = this.quadSpace.length;
        for (int x = 0; x < segments; ++x) {
            for (int y = 0; y < segments; ++y) {
                this.quadSpace[x][y].remove(shape);
            }
        }
    }

    private void addToQuadSpace(Shape shape) {
        int segments = this.quadSpace.length;
        for (int x = 0; x < segments; ++x) {
            for (int y = 0; y < segments; ++y) {
                if (!this.collides(shape, this.quadSpaceShapes[x][y])) continue;
                this.quadSpace[x][y].add(shape);
            }
        }
    }

    public void init() {
        int size = 10;
        int[][] map = new int[][]{{0, 0, 0, 0, 0, 0, 0, 3, 0, 0}, {0, 1, 1, 1, 0, 0, 1, 1, 1, 0}, {0, 1, 1, 0, 0, 0, 5, 1, 6, 0}, {0, 1, 2, 0, 0, 0, 4, 1, 1, 0}, {0, 1, 1, 0, 0, 0, 1, 1, 0, 0}, {0, 0, 0, 0, 3, 0, 1, 1, 0, 0}, {0, 0, 0, 1, 1, 0, 0, 0, 1, 0}, {0, 0, 0, 1, 1, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}};
        for (int x = 0; x < map[0].length; ++x) {
            block9: for (int y = 0; y < map.length; ++y) {
                if (map[y][x] == 0) continue;
                switch (map[y][x]) {
                    case 1: {
                        Polygon p2 = new Polygon();
                        p2.addPoint(x * 32, y * 32);
                        p2.addPoint(x * 32 + 32, y * 32);
                        p2.addPoint(x * 32 + 32, y * 32 + 32);
                        p2.addPoint(x * 32, y * 32 + 32);
                        this.original.add(p2);
                        continue block9;
                    }
                    case 2: {
                        Polygon poly = new Polygon();
                        poly.addPoint(x * 32, y * 32);
                        poly.addPoint(x * 32 + 32, y * 32);
                        poly.addPoint(x * 32, y * 32 + 32);
                        this.original.add(poly);
                        continue block9;
                    }
                    case 3: {
                        Circle ellipse = new Circle((float)(x * 32 + 16), (float)(y * 32 + 32), 16.0f, 16);
                        this.original.add(ellipse);
                        continue block9;
                    }
                    case 4: {
                        Polygon p = new Polygon();
                        p.addPoint(x * 32 + 32, y * 32);
                        p.addPoint(x * 32 + 32, y * 32 + 32);
                        p.addPoint(x * 32, y * 32 + 32);
                        this.original.add(p);
                        continue block9;
                    }
                    case 5: {
                        Polygon p3 = new Polygon();
                        p3.addPoint(x * 32, y * 32);
                        p3.addPoint(x * 32 + 32, y * 32);
                        p3.addPoint(x * 32 + 32, y * 32 + 32);
                        this.original.add(p3);
                        continue block9;
                    }
                    case 6: {
                        Polygon p4 = new Polygon();
                        p4.addPoint(x * 32, y * 32);
                        p4.addPoint(x * 32 + 32, y * 32);
                        p4.addPoint(x * 32, y * 32 + 32);
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
            for (int x = 0; x < this.quadSpace.length; ++x) {
                for (int y = 0; y < this.quadSpace.length; ++y) {
                    ArrayList shapes = this.quadSpace[x][y];
                    int before = shapes.size();
                    this.combine(shapes);
                    int after = shapes.size();
                    updated |= before != after;
                }
            }
        }
        HashSet result = new HashSet();
        for (int x = 0; x < this.quadSpace.length; ++x) {
            for (int y = 0; y < this.quadSpace.length; ++y) {
                result.addAll(this.quadSpace[x][y]);
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
        for (int i = 0; i < current.size(); ++i) {
            pruned.add(((Shape)current.get(i)).prune());
        }
        return pruned;
    }

    private ArrayList combineImpl(ArrayList shapes) {
        ArrayList result = new ArrayList(shapes);
        if (this.quadSpace != null) {
            result = shapes;
        }
        for (int i = 0; i < shapes.size(); ++i) {
            Shape first = (Shape)shapes.get(i);
            for (int j = i + 1; j < shapes.size(); ++j) {
                Shape[] joined;
                Shape second = (Shape)shapes.get(j);
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
        int i;
        if (shape1.intersects(shape2)) {
            return true;
        }
        for (i = 0; i < shape1.getPointCount(); ++i) {
            pt = shape1.getPoint(i);
            if (!shape2.contains(pt[0], pt[1])) continue;
            return true;
        }
        for (i = 0; i < shape2.getPointCount(); ++i) {
            pt = shape2.getPoint(i);
            if (!shape1.contains(pt[0], pt[1])) continue;
            return true;
        }
        return false;
    }

    @Override
    public void init(GameContainer container) throws SlickException {
        this.util.setListener(this);
        this.init();
    }

    @Override
    public void update(GameContainer container, int delta) throws SlickException {
    }

    @Override
    public void render(GameContainer container, Graphics g) throws SlickException {
        Shape shape;
        int i;
        g.setColor(Color.green);
        for (i = 0; i < this.original.size(); ++i) {
            shape = (Shape)this.original.get(i);
            g.draw(shape);
        }
        g.setColor(Color.white);
        if (this.quadSpaceShapes != null) {
            g.draw(this.quadSpaceShapes[0][0]);
        }
        g.translate(0.0f, 320.0f);
        for (i = 0; i < this.combined.size(); ++i) {
            g.setColor(Color.white);
            shape = (Shape)this.combined.get(i);
            g.draw(shape);
            for (int j = 0; j < shape.getPointCount(); ++j) {
                g.setColor(Color.yellow);
                float[] pt = shape.getPoint(j);
                g.fillOval(pt[0] - 1.0f, pt[1] - 1.0f, 3.0f, 3.0f);
            }
        }
    }

    public static void main(String[] argv) {
        try {
            AppGameContainer container = new AppGameContainer(new GeomUtilTileTest());
            container.setDisplayMode(800, 600, false);
            container.start();
        }
        catch (SlickException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void pointExcluded(float x, float y) {
    }

    @Override
    public void pointIntersected(float x, float y) {
        this.intersections.add(new Vector2f(x, y));
    }

    @Override
    public void pointUsed(float x, float y) {
        this.used.add(new Vector2f(x, y));
    }
}

