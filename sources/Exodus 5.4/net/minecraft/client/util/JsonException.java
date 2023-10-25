/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  org.apache.commons.lang3.StringUtils
 */
package net.minecraft.client.util;

import com.google.common.collect.Lists;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

public class JsonException
extends IOException {
    private final List<Entry> field_151383_a = Lists.newArrayList();
    private final String field_151382_b;

    public JsonException(String string, Throwable throwable) {
        super(throwable);
        this.field_151383_a.add(new Entry());
        this.field_151382_b = string;
    }

    public void func_151380_a(String string) {
        this.field_151383_a.get(0).func_151373_a(string);
    }

    public void func_151381_b(String string) {
        this.field_151383_a.get(0).field_151376_a = string;
        this.field_151383_a.add(0, new Entry());
    }

    public JsonException(String string) {
        this.field_151383_a.add(new Entry());
        this.field_151382_b = string;
    }

    public static JsonException func_151379_a(Exception exception) {
        if (exception instanceof JsonException) {
            return (JsonException)exception;
        }
        String string = exception.getMessage();
        if (exception instanceof FileNotFoundException) {
            string = "File not found";
        }
        return new JsonException(string, exception);
    }

    @Override
    public String getMessage() {
        return "Invalid " + this.field_151383_a.get(this.field_151383_a.size() - 1).toString() + ": " + this.field_151382_b;
    }

    public static class Entry {
        private String field_151376_a = null;
        private final List<String> field_151375_b = Lists.newArrayList();

        public String func_151372_b() {
            return StringUtils.join(this.field_151375_b, (String)"->");
        }

        private void func_151373_a(String string) {
            this.field_151375_b.add(0, string);
        }

        private Entry() {
        }

        public String toString() {
            return this.field_151376_a != null ? (!this.field_151375_b.isEmpty() ? String.valueOf(this.field_151376_a) + " " + this.func_151372_b() : this.field_151376_a) : (!this.field_151375_b.isEmpty() ? "(Unknown file) " + this.func_151372_b() : "(Unknown file)");
        }
    }
}

