package client.value;

import client.Client;
import client.util.MinecraftInstance;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@RequiredArgsConstructor
public abstract class Mode<T> implements MinecraftInstance {
    private final String name;
    private final T parent;
    private final List<Value<?>> values = new ArrayList<>();

    public final void register() {
        Client.INSTANCE.getEventBus().register(this);
        onEnable();
    }

    public final void unregister() {
        Client.INSTANCE.getEventBus().unregister(this);
        onDisable();
    }

    public void onEnable() {}
    public void onDisable() {}
}