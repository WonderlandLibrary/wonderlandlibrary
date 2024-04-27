/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonObject
 *  org.jetbrains.annotations.NotNull
 */
package me.liuli.elixir.account;

import com.google.gson.JsonObject;
import java.util.UUID;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Charsets;
import me.liuli.elixir.account.MinecraftAccount;
import me.liuli.elixir.compat.Session;
import me.liuli.elixir.utils.GsonExtensionKt;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0010H\u0016J\u0010\u0010\u0011\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0010H\u0016J\b\u0010\u0012\u001a\u00020\u000eH\u0016R\u001a\u0010\u0003\u001a\u00020\u0004X\u0096\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u0014\u0010\t\u001a\u00020\n8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u000b\u0010\f\u00a8\u0006\u0013"}, d2={"Lme/liuli/elixir/account/CrackedAccount;", "Lme/liuli/elixir/account/MinecraftAccount;", "()V", "name", "", "getName", "()Ljava/lang/String;", "setName", "(Ljava/lang/String;)V", "session", "Lme/liuli/elixir/compat/Session;", "getSession", "()Lme/liuli/elixir/compat/Session;", "fromRawJson", "", "json", "Lcom/google/gson/JsonObject;", "toRawJson", "update", "Elixir"})
public final class CrackedAccount
extends MinecraftAccount {
    @NotNull
    private String name = "";

    public CrackedAccount() {
        super("Cracked");
    }

    @Override
    @NotNull
    public String getName() {
        return this.name;
    }

    public void setName(@NotNull String string) {
        Intrinsics.checkNotNullParameter(string, "<set-?>");
        this.name = string;
    }

    @Override
    @NotNull
    public Session getSession() {
        String string = this.getName();
        byte[] byArray = this.getName().getBytes(Charsets.UTF_8);
        Intrinsics.checkNotNullExpressionValue(byArray, "this as java.lang.String).getBytes(charset)");
        String string2 = UUID.nameUUIDFromBytes(byArray).toString();
        Intrinsics.checkNotNullExpressionValue(string2, "nameUUIDFromBytes(name.t\u2026arsets.UTF_8)).toString()");
        return new Session(string, string2, "-", "legacy");
    }

    @Override
    public void update() {
    }

    @Override
    public void toRawJson(@NotNull JsonObject json) {
        Intrinsics.checkNotNullParameter(json, "json");
        GsonExtensionKt.set(json, "name", this.getName());
    }

    @Override
    public void fromRawJson(@NotNull JsonObject json) {
        Intrinsics.checkNotNullParameter(json, "json");
        String string = GsonExtensionKt.string(json, "name");
        Intrinsics.checkNotNull(string);
        this.setName(string);
    }
}

