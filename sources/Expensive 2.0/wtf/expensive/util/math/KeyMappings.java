package wtf.expensive.util.math;

import org.lwjgl.glfw.GLFW;

import java.util.HashMap;
import java.util.Map;

/**
 * @author dedinside
 * @since 25.06.2023
 */
public class KeyMappings {

    public static final Map<String, Integer> keyMap = new HashMap<>();
    public static final Map<Integer, String> reverseKeyMap = new HashMap<>();

   static {
        putMappings();
        reverseMappings();
    }
    public static int getKeyByName(String keyName) {
        for (int i = 30; i < GLFW.GLFW_KEY_LAST; i++) {
            String name = GLFW.glfwGetKeyName(i, -1);

            if (name != null && name.equalsIgnoreCase(keyName)) {
                return i;
            }
        }
        return -1;
    }


    private static void putMappings() {
        keyMap.put("A", GLFW.GLFW_KEY_A);
        keyMap.put("B", GLFW.GLFW_KEY_B);
        keyMap.put("C", GLFW.GLFW_KEY_C);
        keyMap.put("D", GLFW.GLFW_KEY_D);
        keyMap.put("E", GLFW.GLFW_KEY_E);
        keyMap.put("F", GLFW.GLFW_KEY_F);
        keyMap.put("G", GLFW.GLFW_KEY_G);
        keyMap.put("H", GLFW.GLFW_KEY_H);
        keyMap.put("I", GLFW.GLFW_KEY_I);
        keyMap.put("J", GLFW.GLFW_KEY_J);
        keyMap.put("K", GLFW.GLFW_KEY_K);
        keyMap.put("L", GLFW.GLFW_KEY_L);
        keyMap.put("M", GLFW.GLFW_KEY_M);
        keyMap.put("N", GLFW.GLFW_KEY_N);
        keyMap.put("O", GLFW.GLFW_KEY_O);
        keyMap.put("P", GLFW.GLFW_KEY_P);
        keyMap.put("Q", GLFW.GLFW_KEY_Q);
        keyMap.put("R", GLFW.GLFW_KEY_R);
        keyMap.put("S", GLFW.GLFW_KEY_S);
        keyMap.put("T", GLFW.GLFW_KEY_T);
        keyMap.put("U", GLFW.GLFW_KEY_U);
        keyMap.put("V", GLFW.GLFW_KEY_V);
        keyMap.put("W", GLFW.GLFW_KEY_W);
        keyMap.put("X", GLFW.GLFW_KEY_X);
        keyMap.put("Y", GLFW.GLFW_KEY_Y);
        keyMap.put("Z", GLFW.GLFW_KEY_Z);
        keyMap.put("0", GLFW.GLFW_KEY_0);
        keyMap.put("1", GLFW.GLFW_KEY_1);
        keyMap.put("2", GLFW.GLFW_KEY_2);
        keyMap.put("3", GLFW.GLFW_KEY_3);
        keyMap.put("4", GLFW.GLFW_KEY_4);
        keyMap.put("5", GLFW.GLFW_KEY_5);
        keyMap.put("6", GLFW.GLFW_KEY_6);
        keyMap.put("7", GLFW.GLFW_KEY_7);
        keyMap.put("8", GLFW.GLFW_KEY_8);
        keyMap.put("9", GLFW.GLFW_KEY_9);
        keyMap.put("F1", GLFW.GLFW_KEY_F1);
        keyMap.put("F2", GLFW.GLFW_KEY_F2);
        keyMap.put("F3", GLFW.GLFW_KEY_F3);
        keyMap.put("F4", GLFW.GLFW_KEY_F4);
        keyMap.put("F5", GLFW.GLFW_KEY_F5);
        keyMap.put("F6", GLFW.GLFW_KEY_F6);
        keyMap.put("F7", GLFW.GLFW_KEY_F7);
        keyMap.put("F8", GLFW.GLFW_KEY_F8);
        keyMap.put("F9", GLFW.GLFW_KEY_F9);
        keyMap.put("F10", GLFW.GLFW_KEY_F10);
        keyMap.put("F11", GLFW.GLFW_KEY_F11);
        keyMap.put("F12", GLFW.GLFW_KEY_F12);
        keyMap.put("NUMPAD1", GLFW.GLFW_KEY_KP_1);
        keyMap.put("NUMPAD2", GLFW.GLFW_KEY_KP_2);
        keyMap.put("NUMPAD3", GLFW.GLFW_KEY_KP_3);
        keyMap.put("NUMPAD4", GLFW.GLFW_KEY_KP_4);
        keyMap.put("NUMPAD5", GLFW.GLFW_KEY_KP_5);
        keyMap.put("NUMPAD6", GLFW.GLFW_KEY_KP_6);
        keyMap.put("NUMPAD7", GLFW.GLFW_KEY_KP_7);
        keyMap.put("NUMPAD8", GLFW.GLFW_KEY_KP_8);
        keyMap.put("NUMPAD9", GLFW.GLFW_KEY_KP_9);
        keyMap.put("SPACE", GLFW.GLFW_KEY_SPACE);
        keyMap.put("ENTER", GLFW.GLFW_KEY_ENTER);
        keyMap.put("ESCAPE", GLFW.GLFW_KEY_ESCAPE);
        keyMap.put("HOME", GLFW.GLFW_KEY_HOME);
        keyMap.put("INSERT", GLFW.GLFW_KEY_INSERT);
        keyMap.put("DELETE", GLFW.GLFW_KEY_DELETE);
        keyMap.put("END", GLFW.GLFW_KEY_END);
        keyMap.put("PAGEUP", GLFW.GLFW_KEY_PAGE_UP);
        keyMap.put("PAGEDOWN", GLFW.GLFW_KEY_PAGE_DOWN);
        keyMap.put("RIGHT", GLFW.GLFW_KEY_RIGHT);
        keyMap.put("LEFT", GLFW.GLFW_KEY_LEFT);
        keyMap.put("DOWN", GLFW.GLFW_KEY_DOWN);
        keyMap.put("UP", GLFW.GLFW_KEY_UP);
        keyMap.put("RIGHT_SHIFT", GLFW.GLFW_KEY_RIGHT_SHIFT);
        keyMap.put("LEFT_SHIFT", GLFW.GLFW_KEY_LEFT_SHIFT);
        keyMap.put("RIGHT_CONTROL", GLFW.GLFW_KEY_RIGHT_CONTROL);
        keyMap.put("LEFT_CONTROL", GLFW.GLFW_KEY_LEFT_CONTROL);
        keyMap.put("RIGHT_ALT", GLFW.GLFW_KEY_RIGHT_ALT);
        keyMap.put("LEFT_ALT", GLFW.GLFW_KEY_LEFT_ALT);
        keyMap.put("RIGHT_SUPER", GLFW.GLFW_KEY_RIGHT_SUPER);
        keyMap.put("LEFT_SUPER", GLFW.GLFW_KEY_LEFT_SUPER);
        keyMap.put("MENU", GLFW.GLFW_KEY_MENU);
        keyMap.put("CAPS_LOCK", GLFW.GLFW_KEY_CAPS_LOCK);
        keyMap.put("NUM_LOCK", GLFW.GLFW_KEY_NUM_LOCK);
        keyMap.put("SCROLL_LOCK", GLFW.GLFW_KEY_SCROLL_LOCK);
        keyMap.put("KP_DECIMAL", GLFW.GLFW_KEY_KP_DECIMAL);
        keyMap.put("KP_DIVIDE", GLFW.GLFW_KEY_KP_DIVIDE);
        keyMap.put("KP_MULTIPLY", GLFW.GLFW_KEY_KP_MULTIPLY);
        keyMap.put("KP_SUBTRACT", GLFW.GLFW_KEY_KP_SUBTRACT);
        keyMap.put("KP_PLUS", GLFW.GLFW_KEY_KP_ADD);
        keyMap.put("KP_ENTER", GLFW.GLFW_KEY_KP_ENTER);
        keyMap.put("KP_EQUAL", GLFW.GLFW_KEY_KP_EQUAL);
        keyMap.put("'", GLFW.GLFW_KEY_APOSTROPHE);
        keyMap.put("/", GLFW.GLFW_KEY_SLASH);
        keyMap.put("-", GLFW.GLFW_KEY_MINUS);
        keyMap.put("+", GLFW.GLFW_KEY_EQUAL);
        keyMap.put("BACK", GLFW.GLFW_KEY_BACKSPACE);
        keyMap.put("BACKSLASH", GLFW.GLFW_KEY_BACKSLASH);
        keyMap.put(".", GLFW.GLFW_KEY_PERIOD);
        keyMap.put("COMMA", GLFW.GLFW_KEY_COMMA);
        keyMap.put("PAUSE", GLFW.GLFW_KEY_PAUSE);

    }

    private static void reverseMappings() {
        for (Map.Entry<String, Integer> entry : keyMap.entrySet()) {
            reverseKeyMap.put(entry.getValue(), entry.getKey());
        }
    }
}
