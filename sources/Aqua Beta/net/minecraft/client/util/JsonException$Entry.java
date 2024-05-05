package net.minecraft.client.util;

import com.google.common.collect.Lists;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

public static class JsonException.Entry {
    private String field_151376_a = null;
    private final List<String> field_151375_b = Lists.newArrayList();

    private JsonException.Entry() {
    }

    private void func_151373_a(String p_151373_1_) {
        this.field_151375_b.add(0, (Object)p_151373_1_);
    }

    public String func_151372_b() {
        return StringUtils.join(this.field_151375_b, (String)"->");
    }

    public String toString() {
        return this.field_151376_a != null ? (!this.field_151375_b.isEmpty() ? this.field_151376_a + " " + this.func_151372_b() : this.field_151376_a) : (!this.field_151375_b.isEmpty() ? "(Unknown file) " + this.func_151372_b() : "(Unknown file)");
    }

    static /* synthetic */ void access$100(JsonException.Entry x0, String x1) {
        x0.func_151373_a(x1);
    }

    static /* synthetic */ String access$202(JsonException.Entry x0, String x1) {
        x0.field_151376_a = x1;
        return x0.field_151376_a;
    }
}
