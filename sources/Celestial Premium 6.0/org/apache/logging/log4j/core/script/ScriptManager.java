/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.script;

import java.io.File;
import java.io.Serializable;
import java.nio.file.Path;
import java.security.AccessController;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import javax.script.Bindings;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleBindings;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.script.AbstractScript;
import org.apache.logging.log4j.core.script.ScriptFile;
import org.apache.logging.log4j.core.util.FileWatcher;
import org.apache.logging.log4j.core.util.WatchManager;
import org.apache.logging.log4j.status.StatusLogger;

public class ScriptManager
implements FileWatcher,
Serializable {
    private static final long serialVersionUID = -2534169384971965196L;
    private static final String KEY_THREADING = "THREADING";
    private static final Logger logger = StatusLogger.getLogger();
    private final Configuration configuration;
    private final ScriptEngineManager manager = new ScriptEngineManager();
    private final ConcurrentMap<String, ScriptRunner> scriptRunners = new ConcurrentHashMap<String, ScriptRunner>();
    private final String languages;
    private final WatchManager watchManager;

    public ScriptManager(Configuration configuration, WatchManager watchManager) {
        this.configuration = configuration;
        this.watchManager = watchManager;
        List<ScriptEngineFactory> factories = this.manager.getEngineFactories();
        if (logger.isDebugEnabled()) {
            StringBuilder sb = new StringBuilder();
            int factorySize = factories.size();
            logger.debug("Installed {} script engine{}", factorySize, factorySize != 1 ? "s" : "");
            for (ScriptEngineFactory factory : factories) {
                String threading = Objects.toString(factory.getParameter(KEY_THREADING), null);
                if (threading == null) {
                    threading = "Not Thread Safe";
                }
                StringBuilder names = new StringBuilder();
                List<String> languageNames = factory.getNames();
                for (String name : languageNames) {
                    if (names.length() > 0) {
                        names.append(", ");
                    }
                    names.append(name);
                }
                if (sb.length() > 0) {
                    sb.append(", ");
                }
                sb.append((CharSequence)names);
                boolean compiled = factory.getScriptEngine() instanceof Compilable;
                logger.debug("{} version: {}, language: {}, threading: {}, compile: {}, names: {}, factory class: {}", factory.getEngineName(), factory.getEngineVersion(), factory.getLanguageName(), threading, compiled, languageNames, factory.getClass().getName());
            }
            this.languages = sb.toString();
        } else {
            StringBuilder names = new StringBuilder();
            for (ScriptEngineFactory factory : factories) {
                for (String name : factory.getNames()) {
                    if (names.length() > 0) {
                        names.append(", ");
                    }
                    names.append(name);
                }
            }
            this.languages = names.toString();
        }
    }

    public void addScript(AbstractScript script) {
        ScriptEngine engine = this.manager.getEngineByName(script.getLanguage());
        if (engine == null) {
            logger.error("No ScriptEngine found for language " + script.getLanguage() + ". Available languages are: " + this.languages);
            return;
        }
        if (engine.getFactory().getParameter(KEY_THREADING) == null) {
            this.scriptRunners.put(script.getName(), new ThreadLocalScriptRunner(script));
        } else {
            this.scriptRunners.put(script.getName(), new MainScriptRunner(engine, script));
        }
        if (script instanceof ScriptFile) {
            ScriptFile scriptFile = (ScriptFile)script;
            Path path = scriptFile.getPath();
            if (scriptFile.isWatched() && path != null) {
                this.watchManager.watchFile(path.toFile(), this);
            }
        }
    }

    public Bindings createBindings(AbstractScript script) {
        return this.getScriptRunner(script).createBindings();
    }

    public AbstractScript getScript(String name) {
        ScriptRunner runner = (ScriptRunner)this.scriptRunners.get(name);
        return runner != null ? runner.getScript() : null;
    }

    @Override
    public void fileModified(File file) {
        ScriptRunner runner = (ScriptRunner)this.scriptRunners.get(file.toString());
        if (runner == null) {
            logger.info("{} is not a running script");
            return;
        }
        ScriptEngine engine = runner.getScriptEngine();
        AbstractScript script = runner.getScript();
        if (engine.getFactory().getParameter(KEY_THREADING) == null) {
            this.scriptRunners.put(script.getName(), new ThreadLocalScriptRunner(script));
        } else {
            this.scriptRunners.put(script.getName(), new MainScriptRunner(engine, script));
        }
    }

    public Object execute(String name, Bindings bindings) {
        ScriptRunner scriptRunner = (ScriptRunner)this.scriptRunners.get(name);
        if (scriptRunner == null) {
            logger.warn("No script named {} could be found");
            return null;
        }
        return AccessController.doPrivileged(() -> scriptRunner.execute(bindings));
    }

    private ScriptRunner getScriptRunner(AbstractScript script) {
        return (ScriptRunner)this.scriptRunners.get(script.getName());
    }

    private class ThreadLocalScriptRunner
    extends AbstractScriptRunner {
        private final AbstractScript script;
        private final ThreadLocal<MainScriptRunner> runners;

        public ThreadLocalScriptRunner(AbstractScript script) {
            this.runners = new ThreadLocal<MainScriptRunner>(){

                @Override
                protected MainScriptRunner initialValue() {
                    ScriptEngine engine = ScriptManager.this.manager.getEngineByName(ThreadLocalScriptRunner.this.script.getLanguage());
                    return new MainScriptRunner(engine, ThreadLocalScriptRunner.this.script);
                }
            };
            this.script = script;
        }

        @Override
        public Object execute(Bindings bindings) {
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

    private class MainScriptRunner
    extends AbstractScriptRunner {
        private final AbstractScript script;
        private final CompiledScript compiledScript;
        private final ScriptEngine scriptEngine;

        public MainScriptRunner(ScriptEngine scriptEngine, AbstractScript script) {
            this.script = script;
            this.scriptEngine = scriptEngine;
            CompiledScript compiled = null;
            if (scriptEngine instanceof Compilable) {
                logger.debug("Script {} is compilable", script.getName());
                compiled = AccessController.doPrivileged(() -> {
                    try {
                        return ((Compilable)((Object)scriptEngine)).compile(script.getScriptText());
                    }
                    catch (Throwable ex) {
                        logger.warn("Error compiling script", ex);
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
        public Object execute(Bindings bindings) {
            if (this.compiledScript != null) {
                try {
                    return this.compiledScript.eval(bindings);
                }
                catch (ScriptException ex) {
                    logger.error("Error running script " + this.script.getName(), (Throwable)ex);
                    return null;
                }
            }
            try {
                return this.scriptEngine.eval(this.script.getScriptText(), bindings);
            }
            catch (ScriptException ex) {
                logger.error("Error running script " + this.script.getName(), (Throwable)ex);
                return null;
            }
        }

        @Override
        public AbstractScript getScript() {
            return this.script;
        }
    }

    private static interface ScriptRunner {
        public Bindings createBindings();

        public Object execute(Bindings var1);

        public AbstractScript getScript();

        public ScriptEngine getScriptEngine();
    }

    private abstract class AbstractScriptRunner
    implements ScriptRunner {
        private static final String KEY_STATUS_LOGGER = "statusLogger";
        private static final String KEY_CONFIGURATION = "configuration";

        private AbstractScriptRunner() {
        }

        @Override
        public Bindings createBindings() {
            SimpleBindings bindings = new SimpleBindings();
            bindings.put(KEY_CONFIGURATION, (Object)ScriptManager.this.configuration);
            bindings.put(KEY_STATUS_LOGGER, (Object)logger);
            return bindings;
        }
    }
}

