/*
 * Decompiled with CFR 0.150.
 */
package baritone.api.process;

import baritone.api.process.IBaritoneProcess;
import java.nio.file.Path;

public interface IExploreProcess
extends IBaritoneProcess {
    public void explore(int var1, int var2);

    public void applyJsonFilter(Path var1, boolean var2) throws Exception;
}

