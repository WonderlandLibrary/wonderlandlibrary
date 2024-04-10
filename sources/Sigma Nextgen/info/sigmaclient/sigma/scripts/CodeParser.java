package info.sigmaclient.sigma.scripts;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class CodeParser {
    ClassRedirectioner classRedirectioner = new ClassRedirectioner();
    public String error = "";
    ScriptEngineManager manager = new ScriptEngineManager();
    public ScriptEngine engine = manager.getEngineByName("JavaScript");
    public EventRunnable parseCode(String str){
        return event -> {
            try {
                engine.eval(str);
            } catch (ScriptException e) {
                error = e.getMessage();
            }
        };
    }
}
