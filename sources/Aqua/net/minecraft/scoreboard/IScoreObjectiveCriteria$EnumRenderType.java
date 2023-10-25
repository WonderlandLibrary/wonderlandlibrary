package net.minecraft.scoreboard;

import com.google.common.collect.Maps;
import java.util.Map;

public static enum IScoreObjectiveCriteria.EnumRenderType {
    INTEGER("integer"),
    HEARTS("hearts");

    private static final Map<String, IScoreObjectiveCriteria.EnumRenderType> field_178801_c;
    private final String field_178798_d;

    private IScoreObjectiveCriteria.EnumRenderType(String p_i45548_3_) {
        this.field_178798_d = p_i45548_3_;
    }

    public String func_178796_a() {
        return this.field_178798_d;
    }

    public static IScoreObjectiveCriteria.EnumRenderType func_178795_a(String p_178795_0_) {
        IScoreObjectiveCriteria.EnumRenderType iscoreobjectivecriteria$enumrendertype = (IScoreObjectiveCriteria.EnumRenderType)((Object)field_178801_c.get((Object)p_178795_0_));
        return iscoreobjectivecriteria$enumrendertype == null ? INTEGER : iscoreobjectivecriteria$enumrendertype;
    }

    static {
        field_178801_c = Maps.newHashMap();
        for (IScoreObjectiveCriteria.EnumRenderType iscoreobjectivecriteria$enumrendertype : IScoreObjectiveCriteria.EnumRenderType.values()) {
            field_178801_c.put((Object)iscoreobjectivecriteria$enumrendertype.func_178796_a(), (Object)iscoreobjectivecriteria$enumrendertype);
        }
    }
}
