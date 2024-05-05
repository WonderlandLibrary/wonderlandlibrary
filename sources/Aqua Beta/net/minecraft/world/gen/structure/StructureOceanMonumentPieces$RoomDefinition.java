package net.minecraft.world.gen.structure;

import net.minecraft.util.EnumFacing;

static class StructureOceanMonumentPieces.RoomDefinition {
    int field_175967_a;
    StructureOceanMonumentPieces.RoomDefinition[] field_175965_b = new StructureOceanMonumentPieces.RoomDefinition[6];
    boolean[] field_175966_c = new boolean[6];
    boolean field_175963_d;
    boolean field_175964_e;
    int field_175962_f;

    public StructureOceanMonumentPieces.RoomDefinition(int p_i45584_1_) {
        this.field_175967_a = p_i45584_1_;
    }

    public void func_175957_a(EnumFacing p_175957_1_, StructureOceanMonumentPieces.RoomDefinition p_175957_2_) {
        this.field_175965_b[p_175957_1_.getIndex()] = p_175957_2_;
        p_175957_2_.field_175965_b[p_175957_1_.getOpposite().getIndex()] = this;
    }

    public void func_175958_a() {
        for (int i = 0; i < 6; ++i) {
            this.field_175966_c[i] = this.field_175965_b[i] != null;
        }
    }

    public boolean func_175959_a(int p_175959_1_) {
        if (this.field_175964_e) {
            return true;
        }
        this.field_175962_f = p_175959_1_;
        for (int i = 0; i < 6; ++i) {
            if (this.field_175965_b[i] == null || !this.field_175966_c[i] || this.field_175965_b[i].field_175962_f == p_175959_1_ || !this.field_175965_b[i].func_175959_a(p_175959_1_)) continue;
            return true;
        }
        return false;
    }

    public boolean func_175961_b() {
        return this.field_175967_a >= 75;
    }

    public int func_175960_c() {
        int i = 0;
        for (int j = 0; j < 6; ++j) {
            if (!this.field_175966_c[j]) continue;
            ++i;
        }
        return i;
    }
}
