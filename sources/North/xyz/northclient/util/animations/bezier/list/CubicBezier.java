package xyz.northclient.util.animations.bezier.list;


import xyz.northclient.util.animations.bezier.Bezier;
import xyz.northclient.util.animations.bezier.Point;

public class CubicBezier extends Bezier {

    @Override
    public double getValue(double t) {
        double dt = 1.0 - t;
        double dt2 = dt * dt;
        double t2 = t * t;

        Point temp = getStartPoint().copy();

        return getStart().copy()
                .scale(dt2, dt)
                .add(temp.scale(3 * dt2 * t))
                .add(temp.set(getEndPoint()).scale(3 * dt * t2))
                .add(temp.set(getEnd()).scale(t2 * t))
                .getY();
    }

    public static class Builder {
        private CubicBezier bezier = new CubicBezier();

        public Builder(CubicBezier bezier) {
            this.bezier = bezier;
        }

        public Builder() {}

        public Builder startPoint(Point point) {
            bezier.setStartPoint(point);

            return this;
        }

        public Builder endPoint(Point point) {
            bezier.setEndPoint(point);

            return this;
        }

        public CubicBezier build() {
            return bezier;
        }
    }

}