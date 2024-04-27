/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.network.play.server.SPacketChat
 *  net.minecraft.util.text.ITextComponent
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
package ad.novoline.module;

import java.util.HashMap;
import java.util.Map;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.util.text.ITextComponent;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="ChatTranslator", category=ModuleCategory.MISC, description="\u7ffb\u8bd1")
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u00006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0007H\u0002J\u0010\u0010\u0010\u001a\u00020\u00072\u0006\u0010\u000f\u001a\u00020\u0007H\u0002J\u0010\u0010\u0011\u001a\u00020\u00072\u0006\u0010\u0012\u001a\u00020\u0007H\u0002J\u0010\u0010\u0013\u001a\u00020\u000e2\u0006\u0010\u0014\u001a\u00020\u0015H\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R*\u0010\u0005\u001a\u001e\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\u00070\u0006j\u000e\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\u0007`\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\t\u001a\n \u000b*\u0004\u0018\u00010\n0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0016"}, d2={"Lad/novoline/module/ChatTranslator;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "apiValue", "Lnet/ccbluex/liquidbounce/value/ListValue;", "cache", "Ljava/util/HashMap;", "", "Lkotlin/collections/HashMap;", "client", "Lorg/apache/http/impl/client/CloseableHttpClient;", "kotlin.jvm.PlatformType", "languageValue", "doTranslate", "", "msg", "getLink", "getResult", "data", "onPacket", "event", "Lnet/ccbluex/liquidbounce/event/PacketEvent;", "LiKingSense"})
public final class ChatTranslator
extends Module {
    private final ListValue languageValue = new ListValue("Language", new String[]{"Chinese", "English"}, "Chinese");
    private final ListValue apiValue = new ListValue("API", new String[]{"Google", "Bing", "YouDao"}, "Bing");
    private final CloseableHttpClient client = HttpClients.createDefault();
    private final HashMap<String, String> cache = new HashMap();

    @EventTarget
    public final void onPacket(@NotNull PacketEvent event) {
        Intrinsics.checkParameterIsNotNull((Object)event, (String)"event");
        if (event.getPacket() instanceof SPacketChat) {
            ITextComponent iTextComponent = ((SPacketChat)event.getPacket()).func_148915_c();
            Intrinsics.checkExpressionValueIsNotNull((Object)iTextComponent, (String)"event.packet.chatComponent");
            String msg = iTextComponent.func_150254_d();
            Map map = this.cache;
            boolean bl = false;
            Map map2 = map;
            boolean bl2 = false;
            if (!map2.containsKey(msg)) {
                String string = msg;
                Intrinsics.checkExpressionValueIsNotNull((Object)string, (String)"msg");
                this.doTranslate(string);
            } else {
                ClientUtils.displayChatMessage(this.cache.containsKey(msg) ? msg : this.cache.get(msg));
            }
            event.cancelEvent();
        }
    }

    /*
     * Exception decompiling
     */
    private final String getLink(String msg) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl5 : INVOKESTATIC - null : Stack underflow
         *     at org.benf.cfr.reader.bytecode.analysis.stack.StackSim.getChange(StackSim.java:81)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:242)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:1559)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:434)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    /*
     * Exception decompiling
     */
    private final String getResult(String data) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl76 : INVOKESTATIC - null : Stack underflow
         *     at org.benf.cfr.reader.bytecode.analysis.stack.StackSim.getChange(StackSim.java:81)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:242)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:1559)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:434)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
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
                    Intrinsics.checkExpressionValueIsNotNull((Object)closeableHttpResponse, (String)"response");
                    StatusLine statusLine = closeableHttpResponse.getStatusLine();
                    Intrinsics.checkExpressionValueIsNotNull((Object)statusLine, (String)"response.statusLine");
                    if (statusLine.getStatusCode() != 200) {
                        StringBuilder stringBuilder = new StringBuilder().append("resp code: ");
                        StatusLine statusLine2 = response.getStatusLine();
                        Intrinsics.checkExpressionValueIsNotNull((Object)statusLine2, (String)"response.statusLine");
                        throw (Throwable)new IllegalStateException(stringBuilder.append(statusLine2.getStatusCode()).append(" != 200").toString());
                    }
                    String string = EntityUtils.toString((HttpEntity)response.getEntity());
                    Intrinsics.checkExpressionValueIsNotNull((Object)string, (String)"EntityUtils.toString(response.entity)");
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

