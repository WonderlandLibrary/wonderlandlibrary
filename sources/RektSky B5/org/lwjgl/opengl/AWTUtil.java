/*
 * Decompiled with CFR 0.152.
 */
package org.lwjgl.opengl;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.IllegalComponentStateException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.nio.IntBuffer;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import org.lwjgl.LWJGLException;
import org.lwjgl.LWJGLUtil;

final class AWTUtil {
    AWTUtil() {
    }

    public static boolean hasWheel() {
        return true;
    }

    public static int getButtonCount() {
        return 3;
    }

    public static int getNativeCursorCapabilities() {
        if (LWJGLUtil.getPlatform() != 2 || LWJGLUtil.isMacOSXEqualsOrBetterThan(10, 4)) {
            int cursor_colors = Toolkit.getDefaultToolkit().getMaximumCursorColors();
            boolean supported = cursor_colors >= Short.MAX_VALUE && AWTUtil.getMaxCursorSize() > 0;
            int caps = supported ? 3 : 4;
            return caps;
        }
        return 0;
    }

    public static Robot createRobot(final Component component) {
        try {
            return AccessController.doPrivileged(new PrivilegedExceptionAction<Robot>(){

                @Override
                public Robot run() throws Exception {
                    return new Robot(component.getGraphicsConfiguration().getDevice());
                }
            });
        }
        catch (PrivilegedActionException e2) {
            LWJGLUtil.log("Got exception while creating robot: " + e2.getCause());
            return null;
        }
    }

    private static int transformY(Component component, int y2) {
        return component.getHeight() - 1 - y2;
    }

    private static Point getPointerLocation(Component component) {
        try {
            GraphicsConfiguration config = component.getGraphicsConfiguration();
            if (config != null) {
                PointerInfo pointer_info = AccessController.doPrivileged(new PrivilegedExceptionAction<PointerInfo>(){

                    @Override
                    public PointerInfo run() throws Exception {
                        return MouseInfo.getPointerInfo();
                    }
                });
                GraphicsDevice device = pointer_info.getDevice();
                if (device == config.getDevice()) {
                    return pointer_info.getLocation();
                }
                return null;
            }
        }
        catch (Exception e2) {
            LWJGLUtil.log("Failed to query pointer location: " + e2.getCause());
        }
        return null;
    }

    public static Point getCursorPosition(Component component) {
        try {
            Point pointer_location = AWTUtil.getPointerLocation(component);
            if (pointer_location != null) {
                Point location = component.getLocationOnScreen();
                pointer_location.translate(-location.x, -location.y);
                pointer_location.move(pointer_location.x, AWTUtil.transformY(component, pointer_location.y));
                return pointer_location;
            }
        }
        catch (IllegalComponentStateException e2) {
            LWJGLUtil.log("Failed to set cursor position: " + e2);
        }
        catch (NoClassDefFoundError e3) {
            LWJGLUtil.log("Failed to query cursor position: " + e3);
        }
        return null;
    }

    public static void setCursorPosition(Component component, Robot robot, int x2, int y2) {
        if (robot != null) {
            try {
                Point location = component.getLocationOnScreen();
                int transformed_x = location.x + x2;
                int transformed_y = location.y + AWTUtil.transformY(component, y2);
                robot.mouseMove(transformed_x, transformed_y);
            }
            catch (IllegalComponentStateException e2) {
                LWJGLUtil.log("Failed to set cursor position: " + e2);
            }
        }
    }

    public static int getMinCursorSize() {
        Dimension min_size = Toolkit.getDefaultToolkit().getBestCursorSize(0, 0);
        return Math.max(min_size.width, min_size.height);
    }

    public static int getMaxCursorSize() {
        Dimension max_size = Toolkit.getDefaultToolkit().getBestCursorSize(10000, 10000);
        return Math.min(max_size.width, max_size.height);
    }

    public static Cursor createCursor(int width, int height, int xHotspot, int yHotspot, int numImages, IntBuffer images, IntBuffer delays) throws LWJGLException {
        BufferedImage cursor_image = new BufferedImage(width, height, 2);
        int[] pixels = new int[images.remaining()];
        int old_position = images.position();
        images.get(pixels);
        images.position(old_position);
        cursor_image.setRGB(0, 0, width, height, pixels, 0, width);
        return Toolkit.getDefaultToolkit().createCustomCursor(cursor_image, new Point(xHotspot, yHotspot), "LWJGL Custom cursor");
    }
}

