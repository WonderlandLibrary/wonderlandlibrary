package io.github.nevalackin.client.binding;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.nevalackin.client.account.Account;
import io.github.nevalackin.client.file.FileManager;
import io.github.nevalackin.client.core.KetamineClient;
import io.github.nevalackin.client.event.game.input.InputEvent;
import io.github.nevalackin.client.event.game.input.State;
import io.github.nevalackin.homoBus.Listener;
import io.github.nevalackin.homoBus.annotations.EventLink;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public final class BindManagerImpl implements BindManager {

    private final Map<Bindable, Bind> binds = new HashMap<>();

    public BindManagerImpl() {
        KetamineClient.getInstance().getEventBus().subscribe(this);
    }

    @EventLink
    private final Listener<InputEvent> onInput = event -> {
        for (final Map.Entry<Bindable, Bind> bindsEntry : this.binds.entrySet()) {
            final Bind bind = bindsEntry.getValue();
            if (bind == null) continue;
            if (bind.getInputType() == event.getType() && bind.getCode() == event.getButton()) {
                final Bindable bindable = bindsEntry.getKey();
                switch (bind.getBindType()) {
                    case TOGGLE:
                        if (event.getState() == State.PRESSED)
                            bindable.toggle();
                        break;
                    case HOLD:
                        switch (event.getState()) {
                            case PRESSED:
                                bindable.setActive(true);
                                break;
                            case RELEASED:
                                bindable.setActive(false);
                                break;
                        }
                        break;
                }
            }
        }
    };

    @Override
    public void save() {
        final FileManager fileManager = KetamineClient.getInstance().getFileManager();
        final File bindsFile = fileManager.getFile("binds");

        final JsonObject bindsObject = new JsonObject();

        for (final Bindable bindable : this.getBinds().keySet()) {
            bindable.saveBind(bindsObject);
        }

        fileManager.writeJson(bindsFile, bindsObject);
    }

    @Override
    public void load() {
        final FileManager fileManager = KetamineClient.getInstance().getFileManager();
        final File bindsFile = fileManager.getFile("binds");
        final JsonElement object = fileManager.parse(bindsFile);

        if (object.isJsonObject()) {
            for (final Map.Entry<String, JsonElement> entry : object.getAsJsonObject().entrySet()) {
                if (entry.getValue().isJsonObject()) {
                    this.getBinds().keySet().stream()
                        .filter(b -> b.getName().equals(entry.getKey()))
                        .forEach(b -> b.loadBind(entry.getValue().getAsJsonObject()));
                }
            }
        }
    }

    @Override
    public Map<Bindable, Bind> getBinds() {
        return this.binds;
    }
}
