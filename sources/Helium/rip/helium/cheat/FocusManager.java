package rip.helium.cheat;

import java.util.ArrayList;

public class FocusManager {

    public static ArrayList<String> focusedName = new ArrayList<>();

    public void addFocus(String name) {
        focusedName.add(name);
    }

    public void removeFocus(String string) {
        focusedName.remove(string);
    }

    public boolean isFocused(String name) {
        return focusedName.contains(name);
    }
}
