package net.caffeinemc.phosphor.api.util;

import org.apache.commons.lang3.StringUtils;
import org.lwjgl.glfw.GLFW;

import static net.caffeinemc.phosphor.common.Phosphor.mc;

public class KeyUtils {
    public static String getKeyName(int keyCode) {
        switch (keyCode) {
            case GLFW.GLFW_MOUSE_BUTTON_2 -> {
                return "RMB";
            }
            case GLFW.GLFW_MOUSE_BUTTON_3 -> {
                return "MMB";
            }
            case GLFW.GLFW_KEY_UNKNOWN -> {
                return "None";
            }
            case GLFW.GLFW_KEY_ESCAPE -> {
                return "Esc";
            }
            case GLFW.GLFW_KEY_GRAVE_ACCENT -> {
                return "Grave Accent";
            }
            case GLFW.GLFW_KEY_WORLD_1 -> {
                return "World 1";
            }
            case GLFW.GLFW_KEY_WORLD_2 -> {
                return "World 2";
            }
            case GLFW.GLFW_KEY_PRINT_SCREEN -> {
                return "Print Screen";
            }
            case GLFW.GLFW_KEY_PAUSE -> {
                return "Pause";
            }
            case GLFW.GLFW_KEY_INSERT -> {
                return "Insert";
            }
            case GLFW.GLFW_KEY_DELETE -> {
                return "Delete";
            }
            case GLFW.GLFW_KEY_HOME -> {
                return "Home";
            }
            case GLFW.GLFW_KEY_PAGE_UP -> {
                return "Page Up";
            }
            case GLFW.GLFW_KEY_PAGE_DOWN -> {
                return "Page Down";
            }
            case GLFW.GLFW_KEY_END -> {
                return "End";
            }
            case GLFW.GLFW_KEY_TAB -> {
                return "Tab";
            }
            case GLFW.GLFW_KEY_LEFT_CONTROL -> {
                return "Left Control";
            }
            case GLFW.GLFW_KEY_RIGHT_CONTROL -> {
                return "Right Control";
            }
            case GLFW.GLFW_KEY_LEFT_ALT -> {
                return "Left Alt";
            }
            case GLFW.GLFW_KEY_RIGHT_ALT -> {
                return "Right Alt";
            }
            case GLFW.GLFW_KEY_LEFT_SHIFT -> {
                return "Left Shift";
            }
            case GLFW.GLFW_KEY_RIGHT_SHIFT -> {
                return "Right Shift";
            }
            case GLFW.GLFW_KEY_UP -> {
                return "Arrow Up";
            }
            case GLFW.GLFW_KEY_DOWN -> {
                return "Arrow Down";
            }
            case GLFW.GLFW_KEY_LEFT -> {
                return "Arrow Left";
            }
            case GLFW.GLFW_KEY_RIGHT -> {
                return "Arrow Right";
            }
            case GLFW.GLFW_KEY_APOSTROPHE -> {
                return "Apostrophe";
            }
            case GLFW.GLFW_KEY_BACKSPACE -> {
                return "Backspace";
            }
            case GLFW.GLFW_KEY_CAPS_LOCK -> {
                return "Caps Lock";
            }
            case GLFW.GLFW_KEY_MENU -> {
                return "Menu";
            }
            case GLFW.GLFW_KEY_LEFT_SUPER -> {
                return "Left Super";
            }
            case GLFW.GLFW_KEY_RIGHT_SUPER -> {
                return "Right Super";
            }
            case GLFW.GLFW_KEY_ENTER -> {
                return "Enter";
            }
            case GLFW.GLFW_KEY_KP_ENTER -> {
                return "Numpad Enter";
            }
            case GLFW.GLFW_KEY_NUM_LOCK -> {
                return "Num Lock";
            }
            case GLFW.GLFW_KEY_SPACE -> {
                return "Space";
            }
            case GLFW.GLFW_KEY_F1 -> {
                return "F1";
            }
            case GLFW.GLFW_KEY_F2 -> {
                return "F2";
            }
            case GLFW.GLFW_KEY_F3 -> {
                return "F3";
            }
            case GLFW.GLFW_KEY_F4 -> {
                return "F4";
            }
            case GLFW.GLFW_KEY_F5 -> {
                return "F5";
            }
            case GLFW.GLFW_KEY_F6 -> {
                return "F6";
            }
            case GLFW.GLFW_KEY_F7 -> {
                return "F7";
            }
            case GLFW.GLFW_KEY_F8 -> {
                return "F8";
            }
            case GLFW.GLFW_KEY_F9 -> {
                return "F9";
            }
            case GLFW.GLFW_KEY_F10 -> {
                return "F10";
            }
            case GLFW.GLFW_KEY_F11 -> {
                return "F11";
            }
            case GLFW.GLFW_KEY_F12 -> {
                return "F12";
            }
            case GLFW.GLFW_KEY_F13 -> {
                return "F13";
            }
            case GLFW.GLFW_KEY_F14 -> {
                return "F14";
            }
            case GLFW.GLFW_KEY_F15 -> {
                return "F15";
            }
            case GLFW.GLFW_KEY_F16 -> {
                return "F16";
            }
            case GLFW.GLFW_KEY_F17 -> {
                return "F17";
            }
            case GLFW.GLFW_KEY_F18 -> {
                return "F18";
            }
            case GLFW.GLFW_KEY_F19 -> {
                return "F19";
            }
            case GLFW.GLFW_KEY_F20 -> {
                return "F20";
            }
            case GLFW.GLFW_KEY_F21 -> {
                return "F21";
            }
            case GLFW.GLFW_KEY_F22 -> {
                return "F22";
            }
            case GLFW.GLFW_KEY_F23 -> {
                return "F23";
            }
            case GLFW.GLFW_KEY_F24 -> {
                return "F24";
            }
            case GLFW.GLFW_KEY_F25 -> {
                return "F25";
            }
            default -> {
                String keyName = GLFW.glfwGetKeyName(keyCode, 0);
                if (keyName == null) return "None";
                return StringUtils.capitalize(keyName);
            }
        }
    }

    public static boolean isKeyPressed(int keyCode) {
        if (keyCode < 0) return false;

        if (keyCode <= 8)
            return GLFW.glfwGetMouseButton(mc.getWindow().getHandle(), keyCode) == GLFW.GLFW_PRESS;

        return GLFW.glfwGetKey(mc.getWindow().getHandle(), keyCode) == GLFW.GLFW_PRESS;
    }
}
