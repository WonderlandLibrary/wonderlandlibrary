package io.github.nevalackin.client.ui.framework;

import java.util.List;

public interface Animated {

    Component getParent();
    List<Component> getChildren();

    void resetAnimationState();

    default boolean shouldPlayAnimation() {
        final Component parent = this.getParent();
        if (parent instanceof Animated) {
            final Animated animated = (Animated) parent;
            return animated.shouldPlayAnimation();
        }

        return true;
    }

    default void resetChildrenAnimations() {
        for (final Component component : this.getChildren()) {
            if (component instanceof Animated) {
                final Animated animated = (Animated) component;
                animated.resetAnimationState();
            }
        }
    }
}
