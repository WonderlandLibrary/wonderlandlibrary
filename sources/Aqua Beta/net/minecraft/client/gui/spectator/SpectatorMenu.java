package net.minecraft.client.gui.spectator;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.spectator.BaseSpectatorGroup;
import net.minecraft.client.gui.spectator.ISpectatorMenuObject;
import net.minecraft.client.gui.spectator.ISpectatorMenuRecipient;
import net.minecraft.client.gui.spectator.ISpectatorMenuView;
import net.minecraft.client.gui.spectator.SpectatorMenu;
import net.minecraft.client.gui.spectator.categories.SpectatorDetails;

public class SpectatorMenu {
    private static final ISpectatorMenuObject field_178655_b = new EndSpectatorObject(null);
    private static final ISpectatorMenuObject field_178656_c = new MoveMenuObject(-1, true);
    private static final ISpectatorMenuObject field_178653_d = new MoveMenuObject(1, true);
    private static final ISpectatorMenuObject field_178654_e = new MoveMenuObject(1, false);
    public static final ISpectatorMenuObject field_178657_a = new /* Unavailable Anonymous Inner Class!! */;
    private final ISpectatorMenuRecipient field_178651_f;
    private final List<SpectatorDetails> field_178652_g = Lists.newArrayList();
    private ISpectatorMenuView field_178659_h = new BaseSpectatorGroup();
    private int field_178660_i = -1;
    private int field_178658_j;

    public SpectatorMenu(ISpectatorMenuRecipient p_i45497_1_) {
        this.field_178651_f = p_i45497_1_;
    }

    public ISpectatorMenuObject func_178643_a(int p_178643_1_) {
        int i = p_178643_1_ + this.field_178658_j * 6;
        return this.field_178658_j > 0 && p_178643_1_ == 0 ? field_178656_c : (p_178643_1_ == 7 ? (i < this.field_178659_h.func_178669_a().size() ? field_178653_d : field_178654_e) : (p_178643_1_ == 8 ? field_178655_b : (i >= 0 && i < this.field_178659_h.func_178669_a().size() ? (ISpectatorMenuObject)Objects.firstNonNull((Object)this.field_178659_h.func_178669_a().get(i), (Object)field_178657_a) : field_178657_a)));
    }

    public List<ISpectatorMenuObject> func_178642_a() {
        ArrayList list = Lists.newArrayList();
        for (int i = 0; i <= 8; ++i) {
            list.add((Object)this.func_178643_a(i));
        }
        return list;
    }

    public ISpectatorMenuObject func_178645_b() {
        return this.func_178643_a(this.field_178660_i);
    }

    public ISpectatorMenuView func_178650_c() {
        return this.field_178659_h;
    }

    public void func_178644_b(int p_178644_1_) {
        ISpectatorMenuObject ispectatormenuobject = this.func_178643_a(p_178644_1_);
        if (ispectatormenuobject != field_178657_a) {
            if (this.field_178660_i == p_178644_1_ && ispectatormenuobject.func_178662_A_()) {
                ispectatormenuobject.func_178661_a(this);
            } else {
                this.field_178660_i = p_178644_1_;
            }
        }
    }

    public void func_178641_d() {
        this.field_178651_f.func_175257_a(this);
    }

    public int func_178648_e() {
        return this.field_178660_i;
    }

    public void func_178647_a(ISpectatorMenuView p_178647_1_) {
        this.field_178652_g.add((Object)this.func_178646_f());
        this.field_178659_h = p_178647_1_;
        this.field_178660_i = -1;
        this.field_178658_j = 0;
    }

    public SpectatorDetails func_178646_f() {
        return new SpectatorDetails(this.field_178659_h, this.func_178642_a(), this.field_178660_i);
    }

    static /* synthetic */ int access$102(SpectatorMenu x0, int x1) {
        x0.field_178658_j = x1;
        return x0.field_178658_j;
    }
}
