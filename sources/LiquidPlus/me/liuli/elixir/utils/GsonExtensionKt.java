/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.GsonBuilder
 *  com.google.gson.JsonArray
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package me.liuli.elixir.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
@Metadata(mv={1, 6, 0}, k=2, xi=48, d1={"\u0000F\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u0006\n\u0002\b\u0006\n\u0002\u0010\t\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\f\n\u0002\u0010\u0004\n\u0002\b\u0004\u001a\u0014\u0010\u0000\u001a\u0004\u0018\u00010\u0001*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003\u001a\u0014\u0010\u0000\u001a\u0004\u0018\u00010\u0001*\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006\u001a\u0019\u0010\u0007\u001a\u0004\u0018\u00010\b*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\t\u001a\u0019\u0010\u0007\u001a\u0004\u0018\u00010\b*\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\u0002\u0010\n\u001a\u0019\u0010\u000b\u001a\u0004\u0018\u00010\f*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\r\u001a\u0019\u0010\u000b\u001a\u0004\u0018\u00010\f*\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\u0002\u0010\u000e\u001a\u0019\u0010\u000f\u001a\u0004\u0018\u00010\u0003*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0010\u001a\u0019\u0010\u000f\u001a\u0004\u0018\u00010\u0003*\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\u0002\u0010\u0011\u001a\u0019\u0010\u0012\u001a\u0004\u0018\u00010\u0013*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0014\u001a\u0019\u0010\u0012\u001a\u0004\u0018\u00010\u0013*\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\u0002\u0010\u0015\u001a\u0014\u0010\u0016\u001a\u0004\u0018\u00010\u0004*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003\u001a\u0014\u0010\u0016\u001a\u0004\u0018\u00010\u0004*\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006\u001a\u001d\u0010\u0017\u001a\u00020\u0018*\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0019\u001a\u00020\u001aH\u0086\u0002\u001a\u001d\u0010\u0017\u001a\u00020\u0018*\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0019\u001a\u00020\bH\u0086\u0002\u001a\u001d\u0010\u0017\u001a\u00020\u0018*\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0019\u001a\u00020\u001bH\u0086\u0002\u001a\u001d\u0010\u0017\u001a\u00020\u0018*\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0019\u001a\u00020\u001cH\u0086\u0002\u001a\u001d\u0010\u0017\u001a\u00020\u0018*\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0019\u001a\u00020\u0006H\u0086\u0002\u001a\u0014\u0010\u001d\u001a\u0004\u0018\u00010\u0006*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003\u001a\u0014\u0010\u001d\u001a\u0004\u0018\u00010\u0006*\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006\u001a\u0014\u0010\u001e\u001a\u00020\u0006*\u00020\u001a2\b\b\u0002\u0010\u001f\u001a\u00020\b\u00a8\u0006 "}, d2={"array", "Lcom/google/gson/JsonArray;", "index", "", "Lcom/google/gson/JsonObject;", "key", "", "boolean", "", "(Lcom/google/gson/JsonArray;I)Ljava/lang/Boolean;", "(Lcom/google/gson/JsonObject;Ljava/lang/String;)Ljava/lang/Boolean;", "double", "", "(Lcom/google/gson/JsonArray;I)Ljava/lang/Double;", "(Lcom/google/gson/JsonObject;Ljava/lang/String;)Ljava/lang/Double;", "int", "(Lcom/google/gson/JsonArray;I)Ljava/lang/Integer;", "(Lcom/google/gson/JsonObject;Ljava/lang/String;)Ljava/lang/Integer;", "long", "", "(Lcom/google/gson/JsonArray;I)Ljava/lang/Long;", "(Lcom/google/gson/JsonObject;Ljava/lang/String;)Ljava/lang/Long;", "obj", "set", "", "value", "Lcom/google/gson/JsonElement;", "", "", "string", "toJsonString", "prettyPrint", "Elixir"})
public final class GsonExtensionKt {
    public static final void set(@NotNull JsonObject $this$set, @NotNull String key, @NotNull JsonElement value) {
        Intrinsics.checkNotNullParameter($this$set, "<this>");
        Intrinsics.checkNotNullParameter(key, "key");
        Intrinsics.checkNotNullParameter(value, "value");
        $this$set.add(key, value);
    }

    public static final void set(@NotNull JsonObject $this$set, @NotNull String key, char value) {
        Intrinsics.checkNotNullParameter($this$set, "<this>");
        Intrinsics.checkNotNullParameter(key, "key");
        $this$set.addProperty(key, Character.valueOf(value));
    }

    public static final void set(@NotNull JsonObject $this$set, @NotNull String key, @NotNull Number value) {
        Intrinsics.checkNotNullParameter($this$set, "<this>");
        Intrinsics.checkNotNullParameter(key, "key");
        Intrinsics.checkNotNullParameter(value, "value");
        $this$set.addProperty(key, value);
    }

    public static final void set(@NotNull JsonObject $this$set, @NotNull String key, @NotNull String value) {
        Intrinsics.checkNotNullParameter($this$set, "<this>");
        Intrinsics.checkNotNullParameter(key, "key");
        Intrinsics.checkNotNullParameter(value, "value");
        $this$set.addProperty(key, value);
    }

    public static final void set(@NotNull JsonObject $this$set, @NotNull String key, boolean value) {
        Intrinsics.checkNotNullParameter($this$set, "<this>");
        Intrinsics.checkNotNullParameter(key, "key");
        $this$set.addProperty(key, Boolean.valueOf(value));
    }

    @NotNull
    public static final String toJsonString(@NotNull JsonElement $this$toJsonString, boolean prettyPrint) {
        Intrinsics.checkNotNullParameter($this$toJsonString, "<this>");
        Gson gson = prettyPrint ? new GsonBuilder().setPrettyPrinting().create() : new GsonBuilder().create();
        String string = gson.toJson($this$toJsonString);
        Intrinsics.checkNotNullExpressionValue(string, "gson.toJson(this)");
        return string;
    }

    public static /* synthetic */ String toJsonString$default(JsonElement jsonElement, boolean bl, int n, Object object) {
        if ((n & 1) != 0) {
            bl = false;
        }
        return GsonExtensionKt.toJsonString(jsonElement, bl);
    }

    @Nullable
    public static final String string(@NotNull JsonObject $this$string, @NotNull String key) {
        Intrinsics.checkNotNullParameter($this$string, "<this>");
        Intrinsics.checkNotNullParameter(key, "key");
        return $this$string.has(key) ? $this$string.get(key).getAsString() : (String)null;
    }

    @Nullable
    public static final Integer int(@NotNull JsonObject $this$int, @NotNull String key) {
        Intrinsics.checkNotNullParameter($this$int, "<this>");
        Intrinsics.checkNotNullParameter(key, "key");
        return $this$int.has(key) ? Integer.valueOf($this$int.get(key).getAsInt()) : (Integer)null;
    }

    @Nullable
    public static final Long long(@NotNull JsonObject $this$long, @NotNull String key) {
        Intrinsics.checkNotNullParameter($this$long, "<this>");
        Intrinsics.checkNotNullParameter(key, "key");
        return $this$long.has(key) ? Long.valueOf($this$long.get(key).getAsLong()) : (Long)null;
    }

    @Nullable
    public static final Double double(@NotNull JsonObject $this$double, @NotNull String key) {
        Intrinsics.checkNotNullParameter($this$double, "<this>");
        Intrinsics.checkNotNullParameter(key, "key");
        return $this$double.has(key) ? Double.valueOf($this$double.get(key).getAsDouble()) : (Double)null;
    }

    @Nullable
    public static final Boolean boolean(@NotNull JsonObject $this$boolean, @NotNull String key) {
        Intrinsics.checkNotNullParameter($this$boolean, "<this>");
        Intrinsics.checkNotNullParameter(key, "key");
        return $this$boolean.has(key) ? Boolean.valueOf($this$boolean.get(key).getAsBoolean()) : (Boolean)null;
    }

    @Nullable
    public static final JsonObject obj(@NotNull JsonObject $this$obj, @NotNull String key) {
        Intrinsics.checkNotNullParameter($this$obj, "<this>");
        Intrinsics.checkNotNullParameter(key, "key");
        return $this$obj.has(key) ? $this$obj.get(key).getAsJsonObject() : (JsonObject)null;
    }

    @Nullable
    public static final JsonArray array(@NotNull JsonObject $this$array, @NotNull String key) {
        Intrinsics.checkNotNullParameter($this$array, "<this>");
        Intrinsics.checkNotNullParameter(key, "key");
        return $this$array.has(key) ? $this$array.get(key).getAsJsonArray() : (JsonArray)null;
    }

    @Nullable
    public static final String string(@NotNull JsonArray $this$string, int index) {
        Intrinsics.checkNotNullParameter($this$string, "<this>");
        return $this$string.size() > index ? $this$string.get(index).getAsString() : (String)null;
    }

    @Nullable
    public static final Integer int(@NotNull JsonArray $this$int, int index) {
        Intrinsics.checkNotNullParameter($this$int, "<this>");
        return $this$int.size() > index ? Integer.valueOf($this$int.get(index).getAsInt()) : (Integer)null;
    }

    @Nullable
    public static final Long long(@NotNull JsonArray $this$long, int index) {
        Intrinsics.checkNotNullParameter($this$long, "<this>");
        return $this$long.size() > index ? Long.valueOf($this$long.get(index).getAsLong()) : (Long)null;
    }

    @Nullable
    public static final Double double(@NotNull JsonArray $this$double, int index) {
        Intrinsics.checkNotNullParameter($this$double, "<this>");
        return $this$double.size() > index ? Double.valueOf($this$double.get(index).getAsDouble()) : (Double)null;
    }

    @Nullable
    public static final Boolean boolean(@NotNull JsonArray $this$boolean, int index) {
        Intrinsics.checkNotNullParameter($this$boolean, "<this>");
        return $this$boolean.size() > index ? Boolean.valueOf($this$boolean.get(index).getAsBoolean()) : (Boolean)null;
    }

    @Nullable
    public static final JsonObject obj(@NotNull JsonArray $this$obj, int index) {
        Intrinsics.checkNotNullParameter($this$obj, "<this>");
        return $this$obj.size() > index ? $this$obj.get(index).getAsJsonObject() : (JsonObject)null;
    }

    @Nullable
    public static final JsonArray array(@NotNull JsonArray $this$array, int index) {
        Intrinsics.checkNotNullParameter($this$array, "<this>");
        return $this$array.size() > index ? $this$array.get(index).getAsJsonArray() : (JsonArray)null;
    }
}

