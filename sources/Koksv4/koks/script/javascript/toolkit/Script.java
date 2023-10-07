package koks.script.javascript.toolkit;

import javax.script.ScriptEngine;

public class Script {

    String name;
    String version;
    String author;

    public Script(ScriptEngine engine) {
        /*engine.put("register", new RegisterScript());*/
    }

    /*public class RegisterScript implements Function<JSObject, Script> {

        @Override
        public Script apply(JSObject jsObject) {
            name = (String) jsObject.getMember("name");
            version = (String) jsObject.getMember("version");
            author = (String) jsObject.getMember("author");
            return null;
        }
    }*/

}
