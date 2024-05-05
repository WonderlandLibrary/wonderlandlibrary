package net.minecraft.entity;

import java.util.concurrent.Callable;

class EntityTracker.1
implements Callable<String> {
    final /* synthetic */ int val$updateFrequency;

    EntityTracker.1(int n) {
        this.val$updateFrequency = n;
    }

    public String call() throws Exception {
        String s = "Once per " + this.val$updateFrequency + " ticks";
        if (this.val$updateFrequency == Integer.MAX_VALUE) {
            s = "Maximum (" + s + ")";
        }
        return s;
    }
}
