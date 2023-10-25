/*
 * Decompiled with CFR 0.150.
 */
package baritone.api.process;

import baritone.api.process.IBaritoneProcess;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.entity.Entity;

public interface IFollowProcess
extends IBaritoneProcess {
    public void follow(Predicate<Entity> var1);

    public List<Entity> following();

    public Predicate<Entity> currentFilter();

    default public void cancel() {
        this.onLostControl();
    }
}

