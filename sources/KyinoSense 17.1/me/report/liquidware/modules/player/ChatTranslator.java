/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParser
 *  net.minecraft.network.play.server.S02PacketChat
 *  net.minecraft.util.IChatComponent
 *  org.apache.http.HttpEntity
 *  org.apache.http.StatusLine
 *  org.apache.http.client.methods.CloseableHttpResponse
 *  org.apache.http.client.methods.HttpGet
 *  org.apache.http.client.methods.HttpUriRequest
 *  org.apache.http.impl.client.CloseableHttpClient
 *  org.apache.http.impl.client.HttpClients
 *  org.apache.http.util.EntityUtils
 *  org.jetbrains.annotations.NotNull
 */
package me.report.liquidware.modules.player;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.HashMap;
import java.util.Map;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.util.IChatComponent;
import obfuscator.NativeMethod;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="ChatTranslator", category=ModuleCategory.PLAYER, description="\u4f60\u60f3\u8981\u4ec0\u4e48\uff1f", array=false)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u00006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0007H\u0002J\u0010\u0010\u0010\u001a\u00020\u00072\u0006\u0010\u000f\u001a\u00020\u0007H\u0002J\u0010\u0010\u0011\u001a\u00020\u00072\u0006\u0010\u0012\u001a\u00020\u0007H\u0002J\u0010\u0010\u0013\u001a\u00020\u000e2\u0006\u0010\u0014\u001a\u00020\u0015H\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R*\u0010\u0005\u001a\u001e\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\u00070\u0006j\u000e\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\u0007`\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\t\u001a\n \u000b*\u0004\u0018\u00010\n0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0016"}, d2={"Lme/report/liquidware/modules/player/ChatTranslator;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "apiValue", "Lnet/ccbluex/liquidbounce/value/ListValue;", "cache", "Ljava/util/HashMap;", "", "Lkotlin/collections/HashMap;", "client", "Lorg/apache/http/impl/client/CloseableHttpClient;", "kotlin.jvm.PlatformType", "languageValue", "doTranslate", "", "msg", "getLink", "getResult", "data", "onPacket", "event", "Lnet/ccbluex/liquidbounce/event/PacketEvent;", "KyinoClient"})
public final class ChatTranslator
extends Module {
    private final CloseableHttpClient client = HttpClients.createDefault();
    private final HashMap<String, String> cache = new HashMap();
    private final ListValue languageValue = new ListValue("Language", new String[]{"Chinese", "English"}, "Chinese");
    private final ListValue apiValue = new ListValue("API", new String[]{"Google", "Bing", "YouDao"}, "Bing");

    @EventTarget
    @NativeMethod.Obfuscation(flags="+native,+tiger-black")
    public final void onPacket(@NotNull PacketEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        if (event.getPacket() instanceof S02PacketChat) {
            IChatComponent iChatComponent = ((S02PacketChat)event.getPacket()).func_148915_c();
            Intrinsics.checkExpressionValueIsNotNull(iChatComponent, "event.packet.chatComponent");
            String msg = iChatComponent.func_150254_d();
            Map map = this.cache;
            boolean bl = false;
            Map map2 = map;
            boolean bl2 = false;
            if (!map2.containsKey(msg)) {
                String string = msg;
                Intrinsics.checkExpressionValueIsNotNull(string, "msg");
                this.doTranslate(string);
            } else {
                String string;
                if (this.cache.containsKey(msg)) {
                    string = msg;
                } else {
                    String string2 = this.cache.get(msg);
                    if (string2 == null) {
                        Intrinsics.throwNpe();
                    }
                    string = string2;
                }
                ClientUtils.displayChatMessage(string);
            }
            event.cancelEvent();
        }
    }

    private final String getLink(String msg) {
        String string;
        String message = StringsKt.replace$default(msg, " ", "%20", false, 4, null);
        String string2 = (String)this.apiValue.get();
        boolean bl = false;
        String string3 = string2;
        if (string3 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        String string4 = string3.toLowerCase();
        Intrinsics.checkExpressionValueIsNotNull(string4, "(this as java.lang.String).toLowerCase()");
        switch (string4) {
            case "google": {
                string = "http://translate.google.cn/translate_a/single?client=gtx&dt=t&dj=1&ie=UTF-8&sl=auto&tl=" + (this.languageValue.equals("chinese") ? "zh_cn" : "en_us") + "&q=" + message;
                break;
            }
            case "bing": {
                string = "http://api.microsofttranslator.com/v2/Http.svc/Translate?appId=A4D660A48A6A97CCA791C34935E4C02BBB1BEC1C&from=&to=" + (this.languageValue.equals("chinese") ? "zh" : "en") + "&text=" + message;
                break;
            }
            case "youdao": {
                string = "http://fanyi.youdao.com/translate?&doctype=json&type=AUTO&i=" + message;
                break;
            }
            default: {
                string = "";
            }
        }
        return string;
    }

    private final String getResult(String data) {
        String string = (String)this.apiValue.get();
        boolean bl = false;
        String string2 = string;
        if (string2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        String string3 = string2.toLowerCase();
        Intrinsics.checkExpressionValueIsNotNull(string3, "(this as java.lang.String).toLowerCase()");
        switch (string3) {
            case "google": {
                JsonElement jsonElement = new JsonParser().parse(data);
                Intrinsics.checkExpressionValueIsNotNull(jsonElement, "JsonParser().parse(data)");
                JsonObject json = jsonElement.getAsJsonObject();
                JsonElement jsonElement2 = json.get("sentences");
                Intrinsics.checkExpressionValueIsNotNull(jsonElement2, "json.get(\"sentences\")");
                JsonElement jsonElement3 = jsonElement2.getAsJsonArray().get(0);
                Intrinsics.checkExpressionValueIsNotNull(jsonElement3, "json.get(\"sentences\").asJsonArray.get(0)");
                JsonElement jsonElement4 = jsonElement3.getAsJsonObject().get("trans");
                Intrinsics.checkExpressionValueIsNotNull(jsonElement4, "json.get(\"sentences\").as\u2026asJsonObject.get(\"trans\")");
                String string4 = jsonElement4.getAsString();
                Intrinsics.checkExpressionValueIsNotNull(string4, "json.get(\"sentences\").as\u2026ect.get(\"trans\").asString");
                return string4;
            }
            case "bing": {
                return StringsKt.replace$default(StringsKt.replace$default(data, "<string xmlns=\"http://schemas.microsoft.com/2003/10/Serialization/\">", "", false, 4, null), "</string>", "", false, 4, null);
            }
            case "youdao": {
                JsonElement jsonElement = new JsonParser().parse(data);
                Intrinsics.checkExpressionValueIsNotNull(jsonElement, "JsonParser().parse(data)");
                JsonObject json = jsonElement.getAsJsonObject();
                JsonElement jsonElement5 = json.get("translateResult");
                Intrinsics.checkExpressionValueIsNotNull(jsonElement5, "json.get(\"translateResult\")");
                JsonElement jsonElement6 = jsonElement5.getAsJsonArray().get(0);
                Intrinsics.checkExpressionValueIsNotNull(jsonElement6, "json.get(\"translateResult\").asJsonArray.get(0)");
                JsonElement jsonElement7 = jsonElement6.getAsJsonArray().get(0);
                Intrinsics.checkExpressionValueIsNotNull(jsonElement7, "json.get(\"translateResul\u2026get(0).asJsonArray.get(0)");
                JsonElement jsonElement8 = jsonElement7.getAsJsonObject().get("tgt");
                Intrinsics.checkExpressionValueIsNotNull(jsonElement8, "json.get(\"translateResul\u2026).asJsonObject.get(\"tgt\")");
                String string5 = jsonElement8.getAsString();
                Intrinsics.checkExpressionValueIsNotNull(string5, "json.get(\"translateResul\u2026bject.get(\"tgt\").asString");
                return string5;
            }
        }
        return "WRONG VALUE";
    }

    private final void doTranslate(String msg) {
        new Thread(new Runnable(this, msg){
            final /* synthetic */ ChatTranslator this$0;
            final /* synthetic */ String $msg;

            public final void run() {
                try {
                    CloseableHttpResponse response;
                    HttpGet request = new HttpGet(ChatTranslator.access$getLink(this.this$0, this.$msg));
                    CloseableHttpResponse closeableHttpResponse = response = ChatTranslator.access$getClient$p(this.this$0).execute((HttpUriRequest)request);
                    Intrinsics.checkExpressionValueIsNotNull(closeableHttpResponse, "response");
                    StatusLine statusLine = closeableHttpResponse.getStatusLine();
                    Intrinsics.checkExpressionValueIsNotNull(statusLine, "response.statusLine");
                    if (statusLine.getStatusCode() != 200) {
                        StringBuilder stringBuilder = new StringBuilder().append("resp code: ");
                        StatusLine statusLine2 = response.getStatusLine();
                        Intrinsics.checkExpressionValueIsNotNull(statusLine2, "response.statusLine");
                        throw (Throwable)new IllegalStateException(stringBuilder.append(statusLine2.getStatusCode()).append(" != 200").toString());
                    }
                    String string = EntityUtils.toString((HttpEntity)response.getEntity());
                    Intrinsics.checkExpressionValueIsNotNull(string, "EntityUtils.toString(response.entity)");
                    String result = ChatTranslator.access$getResult(this.this$0, string);
                    ((Map)ChatTranslator.access$getCache$p(this.this$0)).put(this.$msg, result);
                    ClientUtils.displayChatMessage(result);
                }
                catch (Exception e) {
                    e.printStackTrace();
                    ClientUtils.displayChatMessage(this.$msg);
                }
            }
            {
                this.this$0 = chatTranslator;
                this.$msg = string;
            }
        }).start();
    }

    public static final /* synthetic */ String access$getLink(ChatTranslator $this, String msg) {
        return $this.getLink(msg);
    }

    public static final /* synthetic */ CloseableHttpClient access$getClient$p(ChatTranslator $this) {
        return $this.client;
    }

    public static final /* synthetic */ String access$getResult(ChatTranslator $this, String data) {
        return $this.getResult(data);
    }

    public static final /* synthetic */ HashMap access$getCache$p(ChatTranslator $this) {
        return $this.cache;
    }
}

