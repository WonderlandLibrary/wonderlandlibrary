package xyz.northclient.util.animations.util;

@FunctionalInterface
public interface Easing {
    /**
     * Easing's method
     *
     * @param value
     * @return animation formula
     */
    double ease(double value);
}