/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Objects
 */
package net.minecraft.client.gui.spectator.categories;

import com.google.common.base.Objects;
import java.util.List;
import net.minecraft.client.gui.spectator.ISpectatorMenuObject;
import net.minecraft.client.gui.spectator.ISpectatorMenuView;
import net.minecraft.client.gui.spectator.SpectatorMenu;

public class SpectatorDetails {
    private final List<ISpectatorMenuObject> field_178682_b;
    private final int field_178683_c;
    private final ISpectatorMenuView field_178684_a;

    public SpectatorDetails(ISpectatorMenuView iSpectatorMenuView, List<ISpectatorMenuObject> list, int n) {
        this.field_178684_a = iSpectatorMenuView;
        this.field_178682_b = list;
        this.field_178683_c = n;
    }

    public ISpectatorMenuObject func_178680_a(int n) {
        return n >= 0 && n < this.field_178682_b.size() ? (ISpectatorMenuObject)Objects.firstNonNull((Object)this.field_178682_b.get(n), (Object)SpectatorMenu.field_178657_a) : SpectatorMenu.field_178657_a;
    }

    public int func_178681_b() {
        return this.field_178683_c;
    }
}

