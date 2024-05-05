package tech.drainwalk.client.scripting;

import lombok.Getter;

import javax.script.ScriptEngineManager;
import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class ScriptManager {
    public static final File DIRECTORY = new File("C:/DrainWalk/game/scripts");
    @Getter
    private static final ScriptEngineManager engineManager = new ScriptEngineManager();

    @Getter
    ArrayList<Script> scripts;

    public ScriptManager() {
        scripts = new ArrayList<>();
        System.out.println(DIRECTORY.getAbsolutePath());
        try {
            for (File file : Objects.requireNonNull(DIRECTORY.listFiles())) {
                if (file.getName().endsWith(".js")) {
                    System.out.println(file.getAbsolutePath());
                    scripts.add(new Script(file.getAbsolutePath()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
