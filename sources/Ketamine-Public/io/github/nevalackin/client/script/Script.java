package io.github.nevalackin.client.script;

import io.github.nevalackin.client.core.KetamineClient;
import io.github.nevalackin.client.event.player.UpdatePositionEvent;
import io.github.nevalackin.client.event.render.overlay.RenderGameOverlayEvent;
import io.github.nevalackin.homoBus.Listener;
import io.github.nevalackin.homoBus.annotations.EventLink;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class Script {

    private static final ScriptEngineManager MANAGER = new ScriptEngineManager();

    private final ScriptEngine engine;
    private final Invocable invocable;

    private final String name;
    private final String version;
    private final String author;

    private boolean enabled;

    private boolean invalid;

    public Script(final String source) {
        this.engine = MANAGER.getEngineByName("nashorn");

        // TODO :: Add variables
        this.engine.put("script", this);

        try {
            this.engine.eval(source);
        } catch (final ScriptException e) {
            this.invalid = true;
        }

        this.invocable = (Invocable) this.engine;

        this.name = (String) this.engine.get("name");
        this.version = (String) this.engine.get("version");
        this.author = (String) this.engine.get("author");

        this.callFunc("onLoad");
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public String getAuthor() {
        return author;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        if (this.invalid) return;

        this.enabled = enabled;

        if (enabled) {
            this.callFunc("onEnable");
            KetamineClient.getInstance().getEventBus().subscribe(this);
        } else {
            KetamineClient.getInstance().getEventBus().unsubscribe(this);
            this.callFunc("onDisable");
        }
    }

    @EventLink
    private final Listener<UpdatePositionEvent> onUpdatePosition = event -> {
        this.callFunc("onUpdate", event);
    };

    @EventLink
    private final Listener<RenderGameOverlayEvent> onRenderGameOverlay = event -> {
        this.callFunc("onRender", event);
    };

    private void callFunc(final String funcName, final Object... args) {
        try {
            this.invocable.invokeFunction(funcName, args);
        } catch (final NoSuchMethodException ignored) {
            // TODO :: Check for if func exists
        } catch (final Exception e) {
            this.invalid = true;
        }
    }
}
