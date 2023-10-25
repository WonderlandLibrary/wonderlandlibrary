package net.minecraft.client.util;

import com.google.common.collect.Lists;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import net.minecraft.client.util.JsonException;

/*
 * Exception performing whole class analysis ignored.
 */
public class JsonException
extends IOException {
    private final List<Entry> field_151383_a = Lists.newArrayList();
    private final String exceptionMessage;

    public JsonException(String message) {
        this.field_151383_a.add((Object)new Entry(null));
        this.exceptionMessage = message;
    }

    public JsonException(String message, Throwable cause) {
        super(cause);
        this.field_151383_a.add((Object)new Entry(null));
        this.exceptionMessage = message;
    }

    public void func_151380_a(String p_151380_1_) {
        Entry.access$100((Entry)((Entry)this.field_151383_a.get(0)), (String)p_151380_1_);
    }

    public void func_151381_b(String p_151381_1_) {
        Entry.access$202((Entry)((Entry)this.field_151383_a.get(0)), (String)p_151381_1_);
        this.field_151383_a.add(0, (Object)new Entry(null));
    }

    public String getMessage() {
        return "Invalid " + ((Entry)this.field_151383_a.get(this.field_151383_a.size() - 1)).toString() + ": " + this.exceptionMessage;
    }

    public static JsonException func_151379_a(Exception p_151379_0_) {
        if (p_151379_0_ instanceof JsonException) {
            return (JsonException)p_151379_0_;
        }
        String s = p_151379_0_.getMessage();
        if (p_151379_0_ instanceof FileNotFoundException) {
            s = "File not found";
        }
        return new JsonException(s, (Throwable)p_151379_0_);
    }
}
