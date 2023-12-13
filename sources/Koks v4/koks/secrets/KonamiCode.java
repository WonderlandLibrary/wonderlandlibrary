package koks.secrets;

import god.buddy.aot.BCompiler;
import koks.Koks;
import koks.api.manager.secret.SecretHandler;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import java.util.ArrayList;
import java.util.List;

public class KonamiCode {
    private static final int[] KONAMI_PATTERN = new int[] {Keyboard.KEY_UP,Keyboard.KEY_UP, Keyboard.KEY_DOWN,Keyboard.KEY_DOWN,Keyboard.KEY_LEFT,Keyboard.KEY_RIGHT,Keyboard.KEY_LEFT,Keyboard.KEY_RIGHT,Keyboard.KEY_B,Keyboard.KEY_A,Keyboard.KEY_ESCAPE};
    public static final List<Integer> HISTORY = new ArrayList<>();

    @BCompiler(aot = BCompiler.AOT.AGGRESSIVE)
    public static void addKey(int input) {
        HISTORY.add(input);
        int wrongKeys = 0;
        if(HISTORY.size() >= 11) {
            for(int i = 0; i < 11; i++) {
                final int key = HISTORY.get(i);
                final int konamiKey = KONAMI_PATTERN[i];
                if(key != konamiKey) {
                    wrongKeys++;
                }
            }
            if(wrongKeys == 0) {
                SecretHandler.catMode = !SecretHandler.catMode;
                if(SecretHandler.catMode) {
                    Koks.name = "Cat";
                }else {
                    Koks.name = "Koks";
                }
                String author = "";
                for (String auth : Koks.authors)
                    author += auth + ", ";
                author = author.substring(0, author.length() - 2);
                Display.setTitle(Koks.name + " v" + Koks.version + " | by " + author + " | Minecraft 1.8.9");
            }
            HISTORY.clear();
        }
    }
}
