package vestige.impl.processor;

import vestige.Vestige;
import vestige.api.event.Listener;
import vestige.api.event.impl.KeyPressedEvent;

public class KeybindProcessor {

    public KeybindProcessor() {
        Vestige.getInstance().getEventManager().register(this);
    }

    @Listener
    public void onKey(KeyPressedEvent e) {
        Vestige.getInstance().getModuleManager().getModules().stream().filter(m -> m.getKeybind().getKey() == e.getKey()).forEach(m -> m.toggle());
    }

}
