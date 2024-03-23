package xyz.northclient.util.animations.bezier;

import lombok.Data;

@Data
public abstract class Bezier {

    private final Point start = new Point(0, 0);
    private final Point end = new Point(1, 1);

    private Point startPoint;
    private Point endPoint;

    public Bezier(Point startPoint, Point endPoint) {
        setStartPoint(startPoint);
        setEndPoint(endPoint);
    }

    public Bezier() {}

    public abstract double getValue(double t);

}