/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonObject
 *  org.jetbrains.annotations.NotNull
 */
package me.liuli.elixir.manage;

import com.google.gson.JsonObject;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import me.liuli.elixir.account.CrackedAccount;
import me.liuli.elixir.account.MicrosoftAccount;
import me.liuli.elixir.account.MinecraftAccount;
import me.liuli.elixir.account.MojangAccount;
import me.liuli.elixir.utils.GsonExtensionKt;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0016\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u0006J\u000e\u0010\b\u001a\u00020\u00042\u0006\u0010\t\u001a\u00020\nJ\u000e\u0010\u000b\u001a\u00020\n2\u0006\u0010\f\u001a\u00020\u0004\u00a8\u0006\r"}, d2={"Lme/liuli/elixir/manage/AccountSerializer;", "", "()V", "accountInstance", "Lme/liuli/elixir/account/MinecraftAccount;", "name", "", "password", "fromJson", "json", "Lcom/google/gson/JsonObject;", "toJson", "account", "Elixir"})
public final class AccountSerializer {
    @NotNull
    public static final AccountSerializer INSTANCE = new AccountSerializer();

    private AccountSerializer() {
    }

    @NotNull
    public final JsonObject toJson(@NotNull MinecraftAccount account) {
        Intrinsics.checkNotNullParameter(account, "account");
        JsonObject json = new JsonObject();
        account.toRawJson(json);
        String string = account.getClass().getCanonicalName();
        Intrinsics.checkNotNullExpressionValue(string, "account.javaClass.canonicalName");
        GsonExtensionKt.set(json, "type", string);
        return json;
    }

    @NotNull
    public final MinecraftAccount fromJson(@NotNull JsonObject json) {
        Intrinsics.checkNotNullParameter(json, "json");
        String string = GsonExtensionKt.string(json, "type");
        Intrinsics.checkNotNull(string);
        Object obj = Class.forName(string).newInstance();
        if (obj == null) {
            throw new NullPointerException("null cannot be cast to non-null type me.liuli.elixir.account.MinecraftAccount");
        }
        MinecraftAccount account = (MinecraftAccount)obj;
        account.fromRawJson(json);
        return account;
    }

    @NotNull
    public final MinecraftAccount accountInstance(@NotNull String name, @NotNull String password) {
        MinecraftAccount minecraftAccount;
        Intrinsics.checkNotNullParameter(name, "name");
        Intrinsics.checkNotNullParameter(password, "password");
        if (StringsKt.startsWith$default(name, "ms@", false, 2, null)) {
            String string = name.substring(3);
            Intrinsics.checkNotNullExpressionValue(string, "this as java.lang.String).substring(startIndex)");
            String realName = string;
            minecraftAccount = ((CharSequence)password).length() == 0 ? MicrosoftAccount.Companion.buildFromAuthCode(realName, MicrosoftAccount.AuthMethod.Companion.getMICROSOFT()) : MicrosoftAccount.Companion.buildFromPassword$default(MicrosoftAccount.Companion, realName, password, null, 4, null);
        } else if (((CharSequence)password).length() == 0) {
            CrackedAccount crackedAccount;
            CrackedAccount it = crackedAccount = new CrackedAccount();
            boolean bl = false;
            it.setName(name);
            minecraftAccount = crackedAccount;
        } else {
            MojangAccount mojangAccount;
            MojangAccount it = mojangAccount = new MojangAccount();
            boolean bl = false;
            it.setName(name);
            it.setPassword(password);
            minecraftAccount = mojangAccount;
        }
        return minecraftAccount;
    }
}

