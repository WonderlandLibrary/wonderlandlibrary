/*
 * Decompiled with CFR 0.150.
 */
package baritone.api.selection;

import baritone.api.selection.ISelection;
import baritone.api.utils.BetterBlockPos;
import net.minecraft.util.EnumFacing;

public interface ISelectionManager {
    public ISelection addSelection(ISelection var1);

    public ISelection addSelection(BetterBlockPos var1, BetterBlockPos var2);

    public ISelection removeSelection(ISelection var1);

    public ISelection[] removeAllSelections();

    public ISelection[] getSelections();

    public ISelection getOnlySelection();

    public ISelection getLastSelection();

    public ISelection expand(ISelection var1, EnumFacing var2, int var3);

    public ISelection contract(ISelection var1, EnumFacing var2, int var3);

    public ISelection shift(ISelection var1, EnumFacing var2, int var3);
}

