/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.script;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.status.StatusLogger;

public abstract class AbstractScript {
    protected static final Logger LOGGER = StatusLogger.getLogger();
    protected static final String DEFAULT_LANGUAGE = "JavaScript";
    private final String language;
    private final String scriptText;
    private final String name;

    public AbstractScript(String name, String language, String scriptText) {
        this.language = language;
        this.scriptText = scriptText;
        this.name = name == null ? this.toString() : name;
    }

    public String getLanguage() {
        return this.language;
    }

    public String getScriptText() {
        return this.scriptText;
    }

    public String getName() {
        return this.name;
    }
}

