package koks.manager.event;

import koks.Koks;
import koks.manager.module.Module;

/**
 * @author deleteboys | lmao | kroko
 * @created on 12.09.2020 : 20:39
 */
public class Event {

    public boolean canceled;

    public boolean isCanceled() {
        return canceled;
    }

    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }

    public void onFire() {
        for (Module module : Koks.getKoks().moduleManager.getModules()) {
            module.onEvent(this);
        }
    }
}
