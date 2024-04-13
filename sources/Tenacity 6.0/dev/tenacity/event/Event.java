package dev.tenacity.event;

import dev.tenacity.Tenacity;
import net.minecraft.entity.player.EntityPlayer;

public class Event {

    private boolean canceled;
    private boolean pre = true;

    public boolean isCanceled() {
        return canceled;
    }

    public final boolean isPre() {
        return pre;
    }

    public final boolean isPost() {
        return !pre;
    }

    public final void setPost() {
        pre = false;
    }

    public final void cancel() {
        canceled = true;
    }
}