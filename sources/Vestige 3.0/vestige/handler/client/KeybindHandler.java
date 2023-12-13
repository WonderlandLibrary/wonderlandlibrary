package vestige.handler.client;

import vestige.Vestige;
import vestige.event.Listener;
import vestige.event.impl.KeyPressEvent;

public class KeybindHandler {

    public KeybindHandler() {
        Vestige.instance.getEventManager().register(this);
    }

    @Listener
    public void onKeyPress(KeyPressEvent event) {
        Vestige.instance.getModuleManager().modules.stream().filter(m -> m.getKey() == event.getKey()).forEach(m -> m.toggle());
    }

}