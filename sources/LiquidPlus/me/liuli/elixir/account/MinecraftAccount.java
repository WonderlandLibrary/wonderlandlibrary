/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonObject
 *  org.jetbrains.annotations.NotNull
 */
package me.liuli.elixir.account;

import com.google.gson.JsonObject;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import me.liuli.elixir.compat.Session;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\b&\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0010\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0010H&J\u0010\u0010\u0011\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0010H&J\b\u0010\u0012\u001a\u00020\u000eH&R\u0012\u0010\u0005\u001a\u00020\u0003X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0006\u0010\u0007R\u0012\u0010\b\u001a\u00020\tX\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\n\u0010\u000bR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\u0007\u00a8\u0006\u0013"}, d2={"Lme/liuli/elixir/account/MinecraftAccount;", "", "type", "", "(Ljava/lang/String;)V", "name", "getName", "()Ljava/lang/String;", "session", "Lme/liuli/elixir/compat/Session;", "getSession", "()Lme/liuli/elixir/compat/Session;", "getType", "fromRawJson", "", "json", "Lcom/google/gson/JsonObject;", "toRawJson", "update", "Elixir"})
public abstract class MinecraftAccount {
    @NotNull
    private final String type;

    public MinecraftAccount(@NotNull String type) {
        Intrinsics.checkNotNullParameter(type, "type");
        this.type = type;
    }

    @NotNull
    public final String getType() {
        return this.type;
    }

    @NotNull
    public abstract String getName();

    @NotNull
    public abstract Session getSession();

    public abstract void update();

    public abstract void toRawJson(@NotNull JsonObject var1);

    public abstract void fromRawJson(@NotNull JsonObject var1);
}

