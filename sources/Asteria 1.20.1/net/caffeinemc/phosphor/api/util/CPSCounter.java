package net.caffeinemc.phosphor.api.util;

import net.caffeinemc.phosphor.api.event.events.MousePressEvent;
import net.caffeinemc.phosphor.api.event.events.MouseUpdateEvent;
import net.caffeinemc.phosphor.api.event.orbit.EventHandler;
import net.minecraft.util.Util;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;

public class CPSCounter {
    public static final ArrayList<Long> leftClick = new ArrayList<>();
    public static final ArrayList<Long> rightClick = new ArrayList<>();
    private static double leftCpsAverage = 0;
    private static double rightCpsAverage = 0;

    @EventHandler
    private static void onMousePress(MousePressEvent event) {
        if (event.action == GLFW.GLFW_PRESS) {
            switch (event.button) {
                case GLFW.GLFW_MOUSE_BUTTON_LEFT -> {
                    leftClick.add(Util.getMeasuringTimeMs());

                    if (leftCpsAverage == 0) {
                        leftCpsAverage = getLeftCPS();
                    } else {
                        leftCpsAverage = MathUtils.getAverage(leftCpsAverage, getLeftCPS());
                    }
                }
                case GLFW.GLFW_MOUSE_BUTTON_RIGHT -> {
                    rightClick.add(Util.getMeasuringTimeMs());

                    if (rightCpsAverage == 0) {
                        rightCpsAverage = getRightCPS();
                    } else {
                        rightCpsAverage = MathUtils.getAverage(rightCpsAverage, getRightCPS());
                    }
                }
            }
        }
    }

    @EventHandler
    private static void onMouseUpdate(MouseUpdateEvent event) {
        leftClick.removeIf((clickTime) -> Util.getMeasuringTimeMs() - clickTime >= 1000);
        rightClick.removeIf((clickTime) -> Util.getMeasuringTimeMs() - clickTime >= 1000);
    }

    public static int getLeftCPS() {
        return leftClick.size();
    }

    public static double getLeftCpsAverage() {
        return leftCpsAverage;
    }

    public static int getRightCPS() {
        return rightClick.size();
    }

    public static double getRightCpsAverage() {
        return rightCpsAverage;
    }
}
