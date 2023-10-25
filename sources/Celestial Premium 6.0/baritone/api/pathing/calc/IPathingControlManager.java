/*
 * Decompiled with CFR 0.150.
 */
package baritone.api.pathing.calc;

import baritone.api.process.IBaritoneProcess;
import baritone.api.process.PathingCommand;
import java.util.Optional;

public interface IPathingControlManager {
    public void registerProcess(IBaritoneProcess var1);

    public Optional<IBaritoneProcess> mostRecentInControl();

    public Optional<PathingCommand> mostRecentCommand();
}

