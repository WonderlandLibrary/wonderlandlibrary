/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.api.scripting;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import jdk.Exported;
import jdk.nashorn.api.scripting.ClassFilter;
import jdk.nashorn.api.scripting.NashornScriptEngine;
import jdk.nashorn.internal.runtime.Context;
import jdk.nashorn.internal.runtime.Version;

@Exported
public final class NashornScriptEngineFactory
implements ScriptEngineFactory {
    private static final String[] DEFAULT_OPTIONS = new String[]{"-doe"};
    private static final List<String> names = NashornScriptEngineFactory.immutableList("nashorn", "Nashorn", "js", "JS", "JavaScript", "javascript", "ECMAScript", "ecmascript");
    private static final List<String> mimeTypes = NashornScriptEngineFactory.immutableList("application/javascript", "application/ecmascript", "text/javascript", "text/ecmascript");
    private static final List<String> extensions = NashornScriptEngineFactory.immutableList("js");

    @Override
    public String getEngineName() {
        return (String)this.getParameter("javax.script.engine");
    }

    @Override
    public String getEngineVersion() {
        return (String)this.getParameter("javax.script.engine_version");
    }

    @Override
    public List<String> getExtensions() {
        return Collections.unmodifiableList(extensions);
    }

    @Override
    public String getLanguageName() {
        return (String)this.getParameter("javax.script.language");
    }

    @Override
    public String getLanguageVersion() {
        return (String)this.getParameter("javax.script.language_version");
    }

    @Override
    public String getMethodCallSyntax(String obj, String method, String ... args2) {
        StringBuilder sb = new StringBuilder().append(obj).append('.').append(method).append('(');
        int len = args2.length;
        if (len > 0) {
            sb.append(args2[0]);
        }
        for (int i = 1; i < len; ++i) {
            sb.append(',').append(args2[i]);
        }
        sb.append(')');
        return sb.toString();
    }

    @Override
    public List<String> getMimeTypes() {
        return Collections.unmodifiableList(mimeTypes);
    }

    @Override
    public List<String> getNames() {
        return Collections.unmodifiableList(names);
    }

    @Override
    public String getOutputStatement(String toDisplay) {
        return "print(" + toDisplay + ")";
    }

    @Override
    public Object getParameter(String key) {
        switch (key) {
            case "javax.script.name": {
                return "javascript";
            }
            case "javax.script.engine": {
                return "Oracle Nashorn";
            }
            case "javax.script.engine_version": {
                return Version.version();
            }
            case "javax.script.language": {
                return "ECMAScript";
            }
            case "javax.script.language_version": {
                return "ECMA - 262 Edition 5.1";
            }
            case "THREADING": {
                return null;
            }
        }
        return null;
    }

    @Override
    public String getProgram(String ... statements) {
        StringBuilder sb = new StringBuilder();
        for (String statement : statements) {
            sb.append(statement).append(';');
        }
        return sb.toString();
    }

    @Override
    public ScriptEngine getScriptEngine() {
        try {
            return new NashornScriptEngine(this, DEFAULT_OPTIONS, NashornScriptEngineFactory.getAppClassLoader(), null);
        }
        catch (RuntimeException e) {
            if (Context.DEBUG) {
                e.printStackTrace();
            }
            throw e;
        }
    }

    public ScriptEngine getScriptEngine(ClassLoader appLoader) {
        return this.newEngine(DEFAULT_OPTIONS, appLoader, null);
    }

    public ScriptEngine getScriptEngine(ClassFilter classFilter) {
        return this.newEngine(DEFAULT_OPTIONS, NashornScriptEngineFactory.getAppClassLoader(), Objects.requireNonNull(classFilter));
    }

    public ScriptEngine getScriptEngine(String ... args2) {
        return this.newEngine(Objects.requireNonNull(args2), NashornScriptEngineFactory.getAppClassLoader(), null);
    }

    public ScriptEngine getScriptEngine(String[] args2, ClassLoader appLoader) {
        return this.newEngine(Objects.requireNonNull(args2), appLoader, null);
    }

    public ScriptEngine getScriptEngine(String[] args2, ClassLoader appLoader, ClassFilter classFilter) {
        return this.newEngine(Objects.requireNonNull(args2), appLoader, Objects.requireNonNull(classFilter));
    }

    private ScriptEngine newEngine(String[] args2, ClassLoader appLoader, ClassFilter classFilter) {
        NashornScriptEngineFactory.checkConfigPermission();
        try {
            return new NashornScriptEngine(this, args2, appLoader, classFilter);
        }
        catch (RuntimeException e) {
            if (Context.DEBUG) {
                e.printStackTrace();
            }
            throw e;
        }
    }

    private static void checkConfigPermission() {
        SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            sm.checkPermission(new RuntimePermission("nashorn.setConfig"));
        }
    }

    private static List<String> immutableList(String ... elements) {
        return Collections.unmodifiableList(Arrays.asList(elements));
    }

    private static ClassLoader getAppClassLoader() {
        ClassLoader ccl = Thread.currentThread().getContextClassLoader();
        return ccl == null ? NashornScriptEngineFactory.class.getClassLoader() : ccl;
    }
}

