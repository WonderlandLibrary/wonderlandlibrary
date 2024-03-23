package xyz.northclient.util.animations.bezier;

import lombok.Data;

@Data
public class Point {

    private double x;
    private double y;

    public Point(double x, double y) {
        setX(x);
        setY(y);
    }

    public Point(Point point) {
        setX(point.getX());
        setY(point.getY());
    }

    public Point copy() {
        return new Point(this);
    }

    public Point scale(double x, double y) {
        setX(getX() * x);
        setY(getY() * y);

        return this;
    }

    public Point scale(double scale) {
        setX(getX() * scale);
        setY(getY() * scale);

        return this;
    }

    public Point add(double x, double y) {
        setX(getX() + x);
        setY(getY() + y);

        return this;
    }

    public Point set(Point point) {
        setX(point.getX());
        setY(point.getY());

        return this;
    }

    public Point add(Point point) {
        setX(getX() + point.getX());
        setY(getY() + point.getY());

        return this;
    }

}