package net.minecraft.client.gui;

import com.google.common.base.Objects;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import net.minecraft.client.gui.GuiPageButtonList;

public static class GuiPageButtonList.EditBoxEntry
extends GuiPageButtonList.GuiListEntry {
    private final Predicate<String> field_178951_a;

    public GuiPageButtonList.EditBoxEntry(int p_i45534_1_, String p_i45534_2_, boolean p_i45534_3_, Predicate<String> p_i45534_4_) {
        super(p_i45534_1_, p_i45534_2_, p_i45534_3_);
        this.field_178951_a = (Predicate)Objects.firstNonNull(p_i45534_4_, (Object)Predicates.alwaysTrue());
    }

    public Predicate<String> func_178950_a() {
        return this.field_178951_a;
    }
}
