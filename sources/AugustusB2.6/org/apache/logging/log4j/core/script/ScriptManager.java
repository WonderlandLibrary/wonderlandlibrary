// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.script;

import javax.script.ScriptException;
import javax.script.CompiledScript;
import javax.script.SimpleBindings;
import org.apache.logging.log4j.status.StatusLogger;
import java.security.AccessController;
import java.io.File;
import javax.script.Bindings;
import java.nio.file.Path;
import javax.script.ScriptEngine;
import java.util.Iterator;
import java.util.List;
import javax.script.Compilable;
import java.util.Objects;
import javax.script.ScriptEngineFactory;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.logging.log4j.core.util.WatchManager;
import java.util.concurrent.ConcurrentMap;
import javax.script.ScriptEngineManager;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.Logger;
import java.io.Serializable;
import org.apache.logging.log4j.core.util.FileWatcher;

public class ScriptManager implements FileWatcher, Serializable
{
    private static final long serialVersionUID = -2534169384971965196L;
    private static final String KEY_THREADING = "THREADING";
    private static final Logger logger;
    private final Configuration configuration;
    private final ScriptEngineManager manager;
    private final ConcurrentMap<String, ScriptRunner> scriptRunners;
    private final String languages;
    private final WatchManager watchManager;
    
    public ScriptManager(final Configuration configuration, final WatchManager watchManager) {
        this.manager = new ScriptEngineManager();
        this.scriptRunners = new ConcurrentHashMap<String, ScriptRunner>();
        this.configuration = configuration;
        this.watchManager = watchManager;
        final List<ScriptEngineFactory> factories = this.manager.getEngineFactories();
        if (ScriptManager.logger.isDebugEnabled()) {
            final StringBuilder sb = new StringBuilder();
            final int factorySize = factories.size();
            ScriptManager.logger.debug("Installed {} script engine{}", (Object)factorySize, (factorySize != 1) ? "s" : "");
            for (final ScriptEngineFactory factory : factories) {
                String threading = Objects.toString(factory.getParameter("THREADING"), null);
                if (threading == null) {
                    threading = "Not Thread Safe";
                }
                final StringBuilder names = new StringBuilder();
                final List<String> languageNames = factory.getNames();
                for (final String name : languageNames) {
                    if (names.length() > 0) {
                        names.append(", ");
                    }
                    names.append(name);
                }
                if (sb.length() > 0) {
                    sb.append(", ");
                }
                sb.append((CharSequence)names);
                final boolean compiled = factory.getScriptEngine() instanceof Compilable;
                ScriptManager.logger.debug("{} version: {}, language: {}, threading: {}, compile: {}, names: {}, factory class: {}", factory.getEngineName(), factory.getEngineVersion(), factory.getLanguageName(), threading, compiled, languageNames, factory.getClass().getName());
            }
            this.languages = sb.toString();
        }
        else {
            final StringBuilder names2 = new StringBuilder();
            for (final ScriptEngineFactory factory2 : factories) {
                for (final String name2 : factory2.getNames()) {
                    if (names2.length() > 0) {
                        names2.append(", ");
                    }
                    names2.append(name2);
                }
            }
            this.languages = names2.toString();
        }
    }
    
    public void addScript(final AbstractScript script) {
        final ScriptEngine engine = this.manager.getEngineByName(script.getLanguage());
        if (engine == null) {
            ScriptManager.logger.error("No ScriptEngine found for language " + script.getLanguage() + ". Available languages are: " + this.languages);
            return;
        }
        if (engine.getFactory().getParameter("THREADING") == null) {
            this.scriptRunners.put(script.getName(), new ThreadLocalScriptRunner(script));
        }
        else {
            this.scriptRunners.put(script.getName(), new MainScriptRunner(engine, script));
        }
        if (script instanceof ScriptFile) {
            final ScriptFile scriptFile = (ScriptFile)script;
            final Path path = scriptFile.getPath();
            if (scriptFile.isWatched() && path != null) {
                this.watchManager.watchFile(path.toFile(), this);
            }
        }
    }
    
    public Bindings createBindings(final AbstractScript script) {
        return this.getScriptRunner(script).createBindings();
    }
    
    public AbstractScript getScript(final String name) {
        final ScriptRunner runner = this.scriptRunners.get(name);
        return (runner != null) ? runner.getScript() : null;
    }
    
    @Override
    public void fileModified(final File file) {
        final ScriptRunner runner = this.scriptRunners.get(file.toString());
        if (runner == null) {
            ScriptManager.logger.info("{} is not a running script", file.getName());
            return;
        }
        final ScriptEngine engine = runner.getScriptEngine();
        final AbstractScript script = runner.getScript();
        if (engine.getFactory().getParameter("THREADING") == null) {
            this.scriptRunners.put(script.getName(), new ThreadLocalScriptRunner(script));
        }
        else {
            this.scriptRunners.put(script.getName(), new MainScriptRunner(engine, script));
        }
    }
    
    public Object execute(final String name, final Bindings bindings) {
        final ScriptRunner scriptRunner = this.scriptRunners.get(name);
        if (scriptRunner == null) {
            ScriptManager.logger.warn("No script named {} could be found", name);
            return null;
        }
        return AccessController.doPrivileged(() -> scriptRunner.execute(bindings));
    }
    
    private ScriptRunner getScriptRunner(final AbstractScript script) {
        return this.scriptRunners.get(script.getName());
    }
    
    static {
        logger = StatusLogger.getLogger();
    }
    
    private abstract class AbstractScriptRunner implements ScriptRunner
    {
        private static final String KEY_STATUS_LOGGER = "statusLogger";
        private static final String KEY_CONFIGURATION = "configuration";
        
        @Override
        public Bindings createBindings() {
            final SimpleBindings bindings = new SimpleBindings();
            bindings.put("configuration", (Object)ScriptManager.this.configuration);
            bindings.put("statusLogger", (Object)ScriptManager.logger);
            return bindings;
        }
    }
    
    private class MainScriptRunner extends AbstractScriptRunner
    {
        private final AbstractScript script;
        private final CompiledScript compiledScript;
        private final ScriptEngine scriptEngine;
        
        public MainScriptRunner(final ScriptEngine scriptEngine, final AbstractScript script) {
            this.script = script;
            this.scriptEngine = scriptEngine;
            CompiledScript compiled = null;
            if (scriptEngine instanceof Compilable) {
                ScriptManager.logger.debug("Script {} is compilable", script.getName());
                compiled = AccessController.doPrivileged(() -> {
                    try {
                        return ((Compilable)scriptEngine).compile(script.getScriptText());
                    }
                    catch (Throwable ex) {
                        ScriptManager.logger.warn("Error compiling script", ex);
                        return null;
                    }
                });
            }
            this.compiledScript = compiled;
        }
        
        @Override
        public ScriptEngine getScriptEngine() {
            return this.scriptEngine;
        }
        
        @Override
        public Object execute(final Bindings bindings) {
            if (this.compiledScript != null) {
                try {
                    return this.compiledScript.eval(bindings);
                }
                catch (ScriptException ex) {
                    ScriptManager.logger.error("Error running script " + this.script.getName(), ex);
                    return null;
                }
            }
            try {
                return this.scriptEngine.eval(this.script.getScriptText(), bindings);
            }
            catch (ScriptException ex) {
                ScriptManager.logger.error("Error running script " + this.script.getName(), ex);
                return null;
            }
        }
        
        @Override
        public AbstractScript getScript() {
            return this.script;
        }
    }
    
    private class ThreadLocalScriptRunner extends AbstractScriptRunner
    {
        private final AbstractScript script;
        private final ThreadLocal<MainScriptRunner> runners;
        
        public ThreadLocalScriptRunner(final AbstractScript script) {
            this.runners = new ThreadLocal<MainScriptRunner>() {
                @Override
                protected MainScriptRunner initialValue() {
                    final ScriptEngine engine = ScriptManager.this.manager.getEngineByName(ThreadLocalScriptRunner.this.script.getLanguage());
                    return new MainScriptRunner(engine, ThreadLocalScriptRunner.this.script);
                }
            };
            this.script = script;
        }
        
        @Override
        public Object execute(final Bindings bindings) {
            return this.runners.get().execute(bindings);
        }
        
        @Override
        public AbstractScript getScript() {
            return this.script;
        }
        
        @Override
        public ScriptEngine getScriptEngine() {
            return this.runners.get().getScriptEngine();
        }
    }
    
    private interface ScriptRunner
    {
        Bindings createBindings();
        
        Object execute(final Bindings bindings);
        
        AbstractScript getScript();
        
        ScriptEngine getScriptEngine();
    }
}
