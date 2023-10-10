package tech.atani.client.listener.event.minecraft.player.combat;

import tech.atani.client.listener.event.Event;

public class KnockbackModifierEvent extends Event {
    private boolean flag;

    public KnockbackModifierEvent(boolean flag) {
        this.flag = flag;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}
