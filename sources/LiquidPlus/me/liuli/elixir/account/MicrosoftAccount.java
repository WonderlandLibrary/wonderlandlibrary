/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonArray
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParser
 *  org.jetbrains.annotations.NotNull
 */
package me.liuli.elixir.account;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.collections.CollectionsKt;
import kotlin.collections.MapsKt;
import kotlin.io.TextStreamsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Charsets;
import kotlin.text.StringsKt;
import me.liuli.elixir.account.MinecraftAccount;
import me.liuli.elixir.compat.OAuthServer;
import me.liuli.elixir.compat.Session;
import me.liuli.elixir.exception.LoginException;
import me.liuli.elixir.utils.GsonExtensionKt;
import me.liuli.elixir.utils.HttpUtils;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u00000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\u0018\u0000 \u00192\u00020\u0001:\u0003\u0018\u0019\u001aB\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015H\u0016J\u0010\u0010\u0016\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015H\u0016J\b\u0010\u0017\u001a\u00020\u0013H\u0016R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0007\u001a\u00020\u0004X\u0096\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\b\u0010\t\"\u0004\b\n\u0010\u000bR\u000e\u0010\f\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\r\u001a\u00020\u000e8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u000f\u0010\u0010R\u000e\u0010\u0011\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001b"}, d2={"Lme/liuli/elixir/account/MicrosoftAccount;", "Lme/liuli/elixir/account/MinecraftAccount;", "()V", "accessToken", "", "authMethod", "Lme/liuli/elixir/account/MicrosoftAccount$AuthMethod;", "name", "getName", "()Ljava/lang/String;", "setName", "(Ljava/lang/String;)V", "refreshToken", "session", "Lme/liuli/elixir/compat/Session;", "getSession", "()Lme/liuli/elixir/compat/Session;", "uuid", "fromRawJson", "", "json", "Lcom/google/gson/JsonObject;", "toRawJson", "update", "AuthMethod", "Companion", "OAuthHandler", "Elixir"})
public final class MicrosoftAccount
extends MinecraftAccount {
    @NotNull
    public static final Companion Companion = new Companion(null);
    @NotNull
    private String name = "";
    @NotNull
    private String uuid = "";
    @NotNull
    private String accessToken = "";
    @NotNull
    private String refreshToken = "";
    @NotNull
    private AuthMethod authMethod = AuthMethod.Companion.getMICROSOFT();
    @NotNull
    public static final String XBOX_PRE_AUTH_URL = "https://login.live.com/oauth20_authorize.srf?client_id=<client_id>&redirect_uri=<redirect_uri>&response_type=code&display=touch&scope=<scope>";
    @NotNull
    public static final String XBOX_AUTH_URL = "https://login.live.com/oauth20_token.srf";
    @NotNull
    public static final String XBOX_XBL_URL = "https://user.auth.xboxlive.com/user/authenticate";
    @NotNull
    public static final String XBOX_XSTS_URL = "https://xsts.auth.xboxlive.com/xsts/authorize";
    @NotNull
    public static final String MC_AUTH_URL = "https://api.minecraftservices.com/authentication/login_with_xbox";
    @NotNull
    public static final String MC_PROFILE_URL = "https://api.minecraftservices.com/minecraft/profile";
    @NotNull
    public static final String XBOX_AUTH_DATA = "client_id=<client_id>&redirect_uri=<redirect_uri>&grant_type=authorization_code&code=";
    @NotNull
    public static final String XBOX_REFRESH_DATA = "client_id=<client_id>&scope=<scope>&grant_type=refresh_token&redirect_uri=<redirect_uri>&refresh_token=";
    @NotNull
    public static final String XBOX_XBL_DATA = "{\"Properties\":{\"AuthMethod\":\"RPS\",\"SiteName\":\"user.auth.xboxlive.com\",\"RpsTicket\":\"<rps_ticket>\"},\"RelyingParty\":\"http://auth.xboxlive.com\",\"TokenType\":\"JWT\"}";
    @NotNull
    public static final String XBOX_XSTS_DATA = "{\"Properties\":{\"SandboxId\":\"RETAIL\",\"UserTokens\":[\"<xbl_token>\"]},\"RelyingParty\":\"rp://api.minecraftservices.com/\",\"TokenType\":\"JWT\"}";
    @NotNull
    public static final String MC_AUTH_DATA = "{\"identityToken\":\"XBL3.0 x=<userhash>;<xsts_token>\"}";

    public MicrosoftAccount() {
        super("Microsoft");
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
        if (((CharSequence)this.uuid).length() == 0 || ((CharSequence)this.accessToken).length() == 0) {
            this.update();
        }
        return new Session(this.getName(), this.uuid, this.accessToken, "mojang");
    }

    @Override
    public void update() {
        String string;
        Pair[] pairArray = new Pair[]{TuplesKt.to("Content-Type", "application/json"), TuplesKt.to("Accept", "application/json")};
        Map jsonPostHeader = MapsKt.mapOf(pairArray);
        JsonParser jsonParser = new JsonParser();
        InputStream inputStream = HttpUtils.make$default(HttpUtils.INSTANCE, XBOX_AUTH_URL, "POST", Intrinsics.stringPlus(Companion.replaceKeys(this.authMethod, XBOX_REFRESH_DATA), this.refreshToken), MapsKt.mapOf(TuplesKt.to("Content-Type", "application/x-www-form-urlencoded")), null, 16, null).getInputStream();
        Intrinsics.checkNotNullExpressionValue(inputStream, "HttpUtils.make(\n        \u2026urlencoded\")).inputStream");
        JsonObject msRefreshJson = jsonParser.parse((Reader)new InputStreamReader(inputStream, Charsets.UTF_8)).getAsJsonObject();
        Intrinsics.checkNotNullExpressionValue(msRefreshJson, "msRefreshJson");
        String string2 = GsonExtensionKt.string(msRefreshJson, "access_token");
        if (string2 == null) {
            throw new LoginException("Microsoft access token is null");
        }
        String msAccessToken = string2;
        String string3 = GsonExtensionKt.string(msRefreshJson, "refresh_token");
        if (string3 == null) {
            throw new LoginException("Microsoft new refresh token is null");
        }
        this.refreshToken = string3;
        JsonParser jsonParser2 = new JsonParser();
        InputStream inputStream2 = HttpUtils.make$default(HttpUtils.INSTANCE, XBOX_XBL_URL, "POST", StringsKt.replace$default(XBOX_XBL_DATA, "<rps_ticket>", StringsKt.replace$default(this.authMethod.getRpsTicketRule(), "<access_token>", msAccessToken, false, 4, null), false, 4, null), jsonPostHeader, null, 16, null).getInputStream();
        Intrinsics.checkNotNullExpressionValue(inputStream2, "HttpUtils.make(XBOX_XBL_\u2026onPostHeader).inputStream");
        JsonObject xblJson = jsonParser2.parse((Reader)new InputStreamReader(inputStream2, Charsets.UTF_8)).getAsJsonObject();
        Intrinsics.checkNotNullExpressionValue(xblJson, "xblJson");
        String string4 = GsonExtensionKt.string(xblJson, "Token");
        if (string4 == null) {
            throw new LoginException("Microsoft XBL token is null");
        }
        String xblToken = string4;
        JsonObject jsonObject = GsonExtensionKt.obj(xblJson, "DisplayClaims");
        if (jsonObject == null) {
            string = null;
        } else {
            JsonArray jsonArray = GsonExtensionKt.array(jsonObject, "xui");
            if (jsonArray == null) {
                string = null;
            } else {
                JsonElement jsonElement = jsonArray.get(0);
                if (jsonElement == null) {
                    string = null;
                } else {
                    JsonObject jsonObject2 = jsonElement.getAsJsonObject();
                    string = jsonObject2 == null ? null : GsonExtensionKt.string(jsonObject2, "uhs");
                }
            }
        }
        if (string == null) {
            throw new LoginException("Microsoft XBL userhash is null");
        }
        String userhash = string;
        JsonParser jsonParser3 = new JsonParser();
        InputStream inputStream3 = HttpUtils.make$default(HttpUtils.INSTANCE, XBOX_XSTS_URL, "POST", StringsKt.replace$default(XBOX_XSTS_DATA, "<xbl_token>", xblToken, false, 4, null), jsonPostHeader, null, 16, null).getInputStream();
        Intrinsics.checkNotNullExpressionValue(inputStream3, "HttpUtils.make(XBOX_XSTS\u2026onPostHeader).inputStream");
        JsonObject xstsJson = jsonParser3.parse((Reader)new InputStreamReader(inputStream3, Charsets.UTF_8)).getAsJsonObject();
        Intrinsics.checkNotNullExpressionValue(xstsJson, "xstsJson");
        String string5 = GsonExtensionKt.string(xstsJson, "Token");
        if (string5 == null) {
            throw new LoginException("Microsoft XSTS token is null");
        }
        String xstsToken = string5;
        JsonParser jsonParser4 = new JsonParser();
        InputStream inputStream4 = HttpUtils.make$default(HttpUtils.INSTANCE, MC_AUTH_URL, "POST", StringsKt.replace$default(StringsKt.replace$default(MC_AUTH_DATA, "<userhash>", userhash, false, 4, null), "<xsts_token>", xstsToken, false, 4, null), jsonPostHeader, null, 16, null).getInputStream();
        Intrinsics.checkNotNullExpressionValue(inputStream4, "HttpUtils.make(MC_AUTH_U\u2026onPostHeader).inputStream");
        JsonObject mcJson = jsonParser4.parse((Reader)new InputStreamReader(inputStream4, Charsets.UTF_8)).getAsJsonObject();
        Intrinsics.checkNotNullExpressionValue(mcJson, "mcJson");
        String string6 = GsonExtensionKt.string(mcJson, "access_token");
        if (string6 == null) {
            throw new LoginException("Minecraft access token is null");
        }
        this.accessToken = string6;
        JsonParser jsonParser5 = new JsonParser();
        InputStream inputStream5 = HttpUtils.make$default(HttpUtils.INSTANCE, MC_PROFILE_URL, "GET", "", MapsKt.mapOf(TuplesKt.to("Authorization", Intrinsics.stringPlus("Bearer ", this.accessToken))), null, 16, null).getInputStream();
        Intrinsics.checkNotNullExpressionValue(inputStream5, "HttpUtils.make(MC_PROFIL\u2026ccessToken\")).inputStream");
        JsonObject mcProfileJson = jsonParser5.parse((Reader)new InputStreamReader(inputStream5, Charsets.UTF_8)).getAsJsonObject();
        Intrinsics.checkNotNullExpressionValue(mcProfileJson, "mcProfileJson");
        String string7 = GsonExtensionKt.string(mcProfileJson, "name");
        if (string7 == null) {
            throw new LoginException("Minecraft account name is null");
        }
        this.setName(string7);
        String string8 = GsonExtensionKt.string(mcProfileJson, "id");
        if (string8 == null) {
            throw new LoginException("Minecraft account uuid is null");
        }
        this.uuid = string8;
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public void toRawJson(@NotNull JsonObject json) {
        void $this$filterValues$iv;
        Intrinsics.checkNotNullParameter(json, "json");
        GsonExtensionKt.set(json, "name", this.getName());
        GsonExtensionKt.set(json, "refreshToken", this.refreshToken);
        Map<String, AuthMethod> map = AuthMethod.Companion.getRegistry();
        String string = "authMethod";
        JsonObject jsonObject = json;
        boolean $i$f$filterValues = false;
        LinkedHashMap result$iv = new LinkedHashMap();
        for (Map.Entry entry$iv : $this$filterValues$iv.entrySet()) {
            AuthMethod it = (AuthMethod)entry$iv.getValue();
            boolean bl = false;
            if (!Intrinsics.areEqual(it, this.authMethod)) continue;
            result$iv.put(entry$iv.getKey(), entry$iv.getValue());
        }
        Map map2 = result$iv;
        String string2 = (String)CollectionsKt.firstOrNull(map2.keySet());
        if (string2 == null) {
            throw new LoginException("Unregistered auth method");
        }
        GsonExtensionKt.set(jsonObject, string, string2);
    }

    @Override
    public void fromRawJson(@NotNull JsonObject json) {
        Intrinsics.checkNotNullParameter(json, "json");
        String string = GsonExtensionKt.string(json, "name");
        Intrinsics.checkNotNull(string);
        this.setName(string);
        String string2 = GsonExtensionKt.string(json, "refreshToken");
        Intrinsics.checkNotNull(string2);
        this.refreshToken = string2;
        Map<String, AuthMethod> map = AuthMethod.Companion.getRegistry();
        String string3 = GsonExtensionKt.string(json, "authMethod");
        Intrinsics.checkNotNull(string3);
        AuthMethod authMethod = map.get(string3);
        if (authMethod == null) {
            throw new LoginException("Unregistered auth method");
        }
        this.authMethod = authMethod;
    }

    @Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0016\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00042\u0006\u0010\u0012\u001a\u00020\u0013J\u0018\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u00172\b\b\u0002\u0010\u0018\u001a\u00020\u0013J \u0010\u0019\u001a\u00020\u00102\u0006\u0010\u001a\u001a\u00020\u00042\u0006\u0010\u001b\u001a\u00020\u00042\b\b\u0002\u0010\u0018\u001a\u00020\u0013J\u0016\u0010\u001c\u001a\u00020\u00042\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u001d\u001a\u00020\u0004R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001e"}, d2={"Lme/liuli/elixir/account/MicrosoftAccount$Companion;", "", "()V", "MC_AUTH_DATA", "", "MC_AUTH_URL", "MC_PROFILE_URL", "XBOX_AUTH_DATA", "XBOX_AUTH_URL", "XBOX_PRE_AUTH_URL", "XBOX_REFRESH_DATA", "XBOX_XBL_DATA", "XBOX_XBL_URL", "XBOX_XSTS_DATA", "XBOX_XSTS_URL", "buildFromAuthCode", "Lme/liuli/elixir/account/MicrosoftAccount;", "code", "method", "Lme/liuli/elixir/account/MicrosoftAccount$AuthMethod;", "buildFromOpenBrowser", "Lme/liuli/elixir/compat/OAuthServer;", "handler", "Lme/liuli/elixir/account/MicrosoftAccount$OAuthHandler;", "authMethod", "buildFromPassword", "username", "password", "replaceKeys", "string", "Elixir"})
    public static final class Companion {
        private Companion() {
        }

        @NotNull
        public final MicrosoftAccount buildFromAuthCode(@NotNull String code, @NotNull AuthMethod method) {
            Intrinsics.checkNotNullParameter(code, "code");
            Intrinsics.checkNotNullParameter(method, "method");
            JsonParser jsonParser = new JsonParser();
            Object object = HttpUtils.make$default(HttpUtils.INSTANCE, MicrosoftAccount.XBOX_AUTH_URL, "POST", Intrinsics.stringPlus(this.replaceKeys(method, MicrosoftAccount.XBOX_AUTH_DATA), code), MapsKt.mapOf(TuplesKt.to("Content-Type", "application/x-www-form-urlencoded")), null, 16, null).getInputStream();
            Intrinsics.checkNotNullExpressionValue(object, "HttpUtils.make(XBOX_AUTH\u2026urlencoded\")).inputStream");
            JsonObject data = jsonParser.parse((Reader)new InputStreamReader((InputStream)object, Charsets.UTF_8)).getAsJsonObject();
            if (!data.has("refresh_token")) {
                throw new LoginException("Failed to get refresh token");
            }
            Object it = object = new MicrosoftAccount();
            boolean bl = false;
            Intrinsics.checkNotNullExpressionValue(data, "data");
            String string = GsonExtensionKt.string(data, "refresh_token");
            Intrinsics.checkNotNull(string);
            ((MicrosoftAccount)it).refreshToken = string;
            ((MicrosoftAccount)it).authMethod = method;
            ((MicrosoftAccount)it).update();
            return object;
        }

        @NotNull
        public final MicrosoftAccount buildFromPassword(@NotNull String username, @NotNull String password, @NotNull AuthMethod authMethod) {
            Intrinsics.checkNotNullParameter(username, "username");
            Intrinsics.checkNotNullParameter(password, "password");
            Intrinsics.checkNotNullParameter(authMethod, "authMethod");
            HttpURLConnection preAuthConnection = HttpUtils.make$default(HttpUtils.INSTANCE, this.replaceKeys(authMethod, MicrosoftAccount.XBOX_PRE_AUTH_URL), "GET", null, null, null, 28, null);
            InputStream inputStream = preAuthConnection.getInputStream();
            Intrinsics.checkNotNullExpressionValue(inputStream, "preAuthConnection.inputStream");
            Charset charset = Charsets.UTF_8;
            String html = TextStreamsKt.readText(new InputStreamReader(inputStream, charset));
            List<String> list = preAuthConnection.getHeaderFields().get("Set-Cookie");
            if (list == null) {
                list = CollectionsKt.emptyList();
            }
            String cookies = CollectionsKt.joinToString$default(list, ";", null, null, 0, null, null, 62, null);
            String urlPost = me.liuli.elixir.account.MicrosoftAccount$Companion.buildFromPassword$findArgs(html, "urlPost");
            Object it = me.liuli.elixir.account.MicrosoftAccount$Companion.buildFromPassword$findArgs(html, "sFTTag");
            boolean bl = false;
            String string = it.substring(StringsKt.indexOf$default((CharSequence)it, "value=\"", 0, false, 6, null) + 7, it.length() - 3);
            Intrinsics.checkNotNullExpressionValue(string, "this as java.lang.String\u2026ing(startIndex, endIndex)");
            String ppft = string;
            preAuthConnection.disconnect();
            it = new Pair[]{TuplesKt.to("Cookie", cookies), TuplesKt.to("Content-Type", "application/x-www-form-urlencoded")};
            HttpURLConnection authConnection = HttpUtils.make$default(HttpUtils.INSTANCE, urlPost, "POST", "login=" + username + "&loginfmt=" + username + "&passwd=" + password + "&PPFT=" + ppft, MapsKt.mapOf(it), null, 16, null);
            it = authConnection.getInputStream();
            Intrinsics.checkNotNullExpressionValue(it, "authConnection.inputStream");
            Charset charset2 = Charsets.UTF_8;
            TextStreamsKt.readText(new InputStreamReader((InputStream)it, charset2));
            String it2 = authConnection.getURL().toString();
            boolean bl2 = false;
            Intrinsics.checkNotNullExpressionValue(it2, "it");
            if (!StringsKt.contains$default((CharSequence)it2, "code=", false, 2, null)) {
                throw new LoginException("Failed to get auth code from response");
            }
            String string2 = it2.substring(StringsKt.indexOf$default((CharSequence)it2, "code=", 0, false, 6, null) + 5);
            Intrinsics.checkNotNullExpressionValue(string2, "this as java.lang.String).substring(startIndex)");
            String pre = string2;
            String string3 = pre.substring(0, StringsKt.indexOf$default((CharSequence)pre, "&", 0, false, 6, null));
            Intrinsics.checkNotNullExpressionValue(string3, "this as java.lang.String\u2026ing(startIndex, endIndex)");
            String code = string3;
            authConnection.disconnect();
            return this.buildFromAuthCode(code, authMethod);
        }

        public static /* synthetic */ MicrosoftAccount buildFromPassword$default(Companion companion, String string, String string2, AuthMethod authMethod, int n, Object object) {
            if ((n & 4) != 0) {
                authMethod = AuthMethod.Companion.getMICROSOFT();
            }
            return companion.buildFromPassword(string, string2, authMethod);
        }

        @NotNull
        public final OAuthServer buildFromOpenBrowser(@NotNull OAuthHandler handler, @NotNull AuthMethod authMethod) {
            OAuthServer oAuthServer;
            Intrinsics.checkNotNullParameter(handler, "handler");
            Intrinsics.checkNotNullParameter(authMethod, "authMethod");
            OAuthServer it = oAuthServer = new OAuthServer(handler, authMethod, null, null, 12, null);
            boolean bl = false;
            it.start();
            return oAuthServer;
        }

        public static /* synthetic */ OAuthServer buildFromOpenBrowser$default(Companion companion, OAuthHandler oAuthHandler, AuthMethod authMethod, int n, Object object) {
            if ((n & 2) != 0) {
                authMethod = AuthMethod.Companion.getAZURE_APP();
            }
            return companion.buildFromOpenBrowser(oAuthHandler, authMethod);
        }

        @NotNull
        public final String replaceKeys(@NotNull AuthMethod method, @NotNull String string) {
            Intrinsics.checkNotNullParameter(method, "method");
            Intrinsics.checkNotNullParameter(string, "string");
            return StringsKt.replace$default(StringsKt.replace$default(StringsKt.replace$default(string, "<client_id>", method.getClientId(), false, 4, null), "<redirect_uri>", method.getRedirectUri(), false, 4, null), "<scope>", method.getScope(), false, 4, null);
        }

        private static final String buildFromPassword$findArgs(String resp, String arg) {
            if (!StringsKt.contains$default((CharSequence)resp, arg, false, 2, null)) {
                throw new LoginException(Intrinsics.stringPlus("Failed to find argument in response ", arg));
            }
            String string = resp.substring(StringsKt.indexOf$default((CharSequence)resp, Intrinsics.stringPlus(arg, ":'"), 0, false, 6, null) + arg.length() + 2);
            Intrinsics.checkNotNullExpressionValue(string, "this as java.lang.String).substring(startIndex)");
            String it = string;
            boolean bl = false;
            String string2 = it.substring(0, StringsKt.indexOf$default((CharSequence)it, "',", 0, false, 6, null));
            Intrinsics.checkNotNullExpressionValue(string2, "this as java.lang.String\u2026ing(startIndex, endIndex)");
            return string2;
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }

    @Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u000b\u0018\u0000 \r2\u00020\u0001:\u0001\rB%\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u0012\u0006\u0010\u0006\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0007R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\tR\u0011\u0010\u0006\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\tR\u0011\u0010\u0005\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\t\u00a8\u0006\u000e"}, d2={"Lme/liuli/elixir/account/MicrosoftAccount$AuthMethod;", "", "clientId", "", "redirectUri", "scope", "rpsTicketRule", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", "getClientId", "()Ljava/lang/String;", "getRedirectUri", "getRpsTicketRule", "getScope", "Companion", "Elixir"})
    public static final class AuthMethod {
        @NotNull
        public static final Companion Companion = new Companion(null);
        @NotNull
        private final String clientId;
        @NotNull
        private final String redirectUri;
        @NotNull
        private final String scope;
        @NotNull
        private final String rpsTicketRule;
        @NotNull
        private static final Map<String, AuthMethod> registry = new LinkedHashMap();
        @NotNull
        private static final AuthMethod MICROSOFT = new AuthMethod("00000000441cc96b", "https://login.live.com/oauth20_desktop.srf", "service::user.auth.xboxlive.com::MBI_SSL", "<access_token>");
        @NotNull
        private static final AuthMethod AZURE_APP = new AuthMethod("0add8caf-2cc6-4546-b798-c3d171217dd9", "http://localhost:1919/login", "XboxLive.signin%20offline_access", "d=<access_token>");

        public AuthMethod(@NotNull String clientId, @NotNull String redirectUri, @NotNull String scope, @NotNull String rpsTicketRule) {
            Intrinsics.checkNotNullParameter(clientId, "clientId");
            Intrinsics.checkNotNullParameter(redirectUri, "redirectUri");
            Intrinsics.checkNotNullParameter(scope, "scope");
            Intrinsics.checkNotNullParameter(rpsTicketRule, "rpsTicketRule");
            this.clientId = clientId;
            this.redirectUri = redirectUri;
            this.scope = scope;
            this.rpsTicketRule = rpsTicketRule;
        }

        @NotNull
        public final String getClientId() {
            return this.clientId;
        }

        @NotNull
        public final String getRedirectUri() {
            return this.redirectUri;
        }

        @NotNull
        public final String getScope() {
            return this.scope;
        }

        @NotNull
        public final String getRpsTicketRule() {
            return this.rpsTicketRule;
        }

        static {
            registry.put("MICROSOFT", MICROSOFT);
            registry.put("AZURE_APP", AZURE_APP);
        }

        @Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010%\n\u0002\u0010\u000e\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u0011\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006R\u0011\u0010\u0007\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\u0006R\u001d\u0010\t\u001a\u000e\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\u00040\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\r\u00a8\u0006\u000e"}, d2={"Lme/liuli/elixir/account/MicrosoftAccount$AuthMethod$Companion;", "", "()V", "AZURE_APP", "Lme/liuli/elixir/account/MicrosoftAccount$AuthMethod;", "getAZURE_APP", "()Lme/liuli/elixir/account/MicrosoftAccount$AuthMethod;", "MICROSOFT", "getMICROSOFT", "registry", "", "", "getRegistry", "()Ljava/util/Map;", "Elixir"})
        public static final class Companion {
            private Companion() {
            }

            @NotNull
            public final Map<String, AuthMethod> getRegistry() {
                return registry;
            }

            @NotNull
            public final AuthMethod getMICROSOFT() {
                return MICROSOFT;
            }

            @NotNull
            public final AuthMethod getAZURE_APP() {
                return AZURE_APP;
            }

            public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
                this();
            }
        }
    }

    @Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\bf\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&J\u0010\u0010\u0006\u001a\u00020\u00032\u0006\u0010\u0007\u001a\u00020\bH&J\u0010\u0010\t\u001a\u00020\u00032\u0006\u0010\n\u001a\u00020\u0005H&\u00a8\u0006\u000b"}, d2={"Lme/liuli/elixir/account/MicrosoftAccount$OAuthHandler;", "", "authError", "", "error", "", "authResult", "account", "Lme/liuli/elixir/account/MicrosoftAccount;", "openUrl", "url", "Elixir"})
    public static interface OAuthHandler {
        public void openUrl(@NotNull String var1);

        public void authResult(@NotNull MicrosoftAccount var1);

        public void authError(@NotNull String var1);
    }
}

