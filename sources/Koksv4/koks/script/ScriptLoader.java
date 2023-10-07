package koks.script;

import koks.script.javascript.JSApi;

import javax.script.ScriptException;
import java.io.*;

public class ScriptLoader {
    public final JSApi jsApi;
    public final File DIR;

    public ScriptLoader(File DIR) {
        jsApi = new JSApi();
        this.DIR = new File(DIR, "Scripts");
        if (!this.DIR.exists())
            this.DIR.mkdirs();
    }

    public void loadScript(String name, Type type) {
        try {
            final BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(DIR, name + getEnding(type))));
            String script = "";

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                script += line + "\n";
            }

            switch (type) {
                case JAVA:
                    jsApi.load(script);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getEnding(Type type) {
        switch (type) {
            case JAVA:
                return ".js";
            default:
                return null;
        }
    }

    public enum Type {
        JAVA;
    }
}
