/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package me.liuli.elixir.compat;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.collections.CollectionsKt;
import kotlin.collections.MapsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import kotlin.text.Charsets;
import kotlin.text.StringsKt;
import me.liuli.elixir.account.MicrosoftAccount;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\u0018\u00002\u00020\u0001:\u0001\u0014B+\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007\u0012\b\b\u0002\u0010\b\u001a\u00020\t\u00a2\u0006\u0002\u0010\nJ\u0006\u0010\u000f\u001a\u00020\u0010J\u0010\u0010\u0011\u001a\u00020\u00102\b\b\u0002\u0010\u0012\u001a\u00020\u0013R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0015"}, d2={"Lme/liuli/elixir/compat/OAuthServer;", "", "handler", "Lme/liuli/elixir/account/MicrosoftAccount$OAuthHandler;", "authMethod", "Lme/liuli/elixir/account/MicrosoftAccount$AuthMethod;", "httpServer", "Lcom/sun/net/httpserver/HttpServer;", "context", "", "(Lme/liuli/elixir/account/MicrosoftAccount$OAuthHandler;Lme/liuli/elixir/account/MicrosoftAccount$AuthMethod;Lcom/sun/net/httpserver/HttpServer;Ljava/lang/String;)V", "getHandler", "()Lme/liuli/elixir/account/MicrosoftAccount$OAuthHandler;", "threadPoolExecutor", "Ljava/util/concurrent/ThreadPoolExecutor;", "start", "", "stop", "isInterrupt", "", "OAuthHttpHandler", "Elixir"})
public final class OAuthServer {
    @NotNull
    private final MicrosoftAccount.OAuthHandler handler;
    @NotNull
    private final MicrosoftAccount.AuthMethod authMethod;
    @NotNull
    private final HttpServer httpServer;
    @NotNull
    private final String context;
    @NotNull
    private final ThreadPoolExecutor threadPoolExecutor;

    public OAuthServer(@NotNull MicrosoftAccount.OAuthHandler handler, @NotNull MicrosoftAccount.AuthMethod authMethod, @NotNull HttpServer httpServer, @NotNull String context) {
        Intrinsics.checkNotNullParameter(handler, "handler");
        Intrinsics.checkNotNullParameter(authMethod, "authMethod");
        Intrinsics.checkNotNullParameter(httpServer, "httpServer");
        Intrinsics.checkNotNullParameter(context, "context");
        this.handler = handler;
        this.authMethod = authMethod;
        this.httpServer = httpServer;
        this.context = context;
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        if (executorService == null) {
            throw new NullPointerException("null cannot be cast to non-null type java.util.concurrent.ThreadPoolExecutor");
        }
        this.threadPoolExecutor = (ThreadPoolExecutor)executorService;
    }

    public /* synthetic */ OAuthServer(MicrosoftAccount.OAuthHandler oAuthHandler, MicrosoftAccount.AuthMethod authMethod, HttpServer httpServer, String string, int n, DefaultConstructorMarker defaultConstructorMarker) {
        if ((n & 2) != 0) {
            authMethod = MicrosoftAccount.AuthMethod.Companion.getAZURE_APP();
        }
        if ((n & 4) != 0) {
            HttpServer httpServer2 = HttpServer.create(new InetSocketAddress("localhost", 1919), 0);
            Intrinsics.checkNotNullExpressionValue(httpServer2, "create(InetSocketAddress(\"localhost\", 1919), 0)");
            httpServer = httpServer2;
        }
        if ((n & 8) != 0) {
            string = "/login";
        }
        this(oAuthHandler, authMethod, httpServer, string);
    }

    @NotNull
    public final MicrosoftAccount.OAuthHandler getHandler() {
        return this.handler;
    }

    public final void start() {
        this.httpServer.createContext(this.context, new OAuthHttpHandler(this, this.authMethod));
        this.httpServer.setExecutor(this.threadPoolExecutor);
        this.httpServer.start();
        this.handler.openUrl(MicrosoftAccount.Companion.replaceKeys(this.authMethod, "https://login.live.com/oauth20_authorize.srf?client_id=<client_id>&redirect_uri=<redirect_uri>&response_type=code&display=touch&scope=<scope>"));
    }

    public final void stop(boolean isInterrupt) {
        this.httpServer.stop(0);
        this.threadPoolExecutor.shutdown();
        if (isInterrupt) {
            this.handler.authError("Has been interrupted");
        }
    }

    public static /* synthetic */ void stop$default(OAuthServer oAuthServer, boolean bl, int n, Object object) {
        if ((n & 1) != 0) {
            bl = true;
        }
        oAuthServer.stop(bl);
    }

    @Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u00002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0000\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u0010\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0016J \u0010\u000b\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fH\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0010"}, d2={"Lme/liuli/elixir/compat/OAuthServer$OAuthHttpHandler;", "Lcom/sun/net/httpserver/HttpHandler;", "server", "Lme/liuli/elixir/compat/OAuthServer;", "authMethod", "Lme/liuli/elixir/account/MicrosoftAccount$AuthMethod;", "(Lme/liuli/elixir/compat/OAuthServer;Lme/liuli/elixir/account/MicrosoftAccount$AuthMethod;)V", "handle", "", "exchange", "Lcom/sun/net/httpserver/HttpExchange;", "response", "message", "", "code", "", "Elixir"})
    public static final class OAuthHttpHandler
    implements HttpHandler {
        @NotNull
        private final OAuthServer server;
        @NotNull
        private final MicrosoftAccount.AuthMethod authMethod;

        public OAuthHttpHandler(@NotNull OAuthServer server, @NotNull MicrosoftAccount.AuthMethod authMethod) {
            Intrinsics.checkNotNullParameter(server, "server");
            Intrinsics.checkNotNullParameter(authMethod, "authMethod");
            this.server = server;
            this.authMethod = authMethod;
        }

        /*
         * WARNING - void declaration
         */
        @Override
        public void handle(@NotNull HttpExchange exchange) {
            void $this$associateTo$iv$iv;
            Object object;
            void $this$mapTo$iv$iv;
            Intrinsics.checkNotNullParameter(exchange, "exchange");
            String[] stringArray = exchange.getRequestURI().getQuery();
            Intrinsics.checkNotNullExpressionValue(stringArray, "exchange.requestURI.query");
            CharSequence charSequence = (CharSequence)stringArray;
            stringArray = new String[]{"&"};
            Iterable $this$map$iv = StringsKt.split$default(charSequence, stringArray, false, 0, 6, null);
            boolean $i$f$map = false;
            Iterable iterable = $this$map$iv;
            Iterable destination$iv$iv = new ArrayList(CollectionsKt.collectionSizeOrDefault($this$map$iv, 10));
            boolean $i$f$mapTo = false;
            for (Object item$iv$iv : $this$mapTo$iv$iv) {
                void it;
                String string = (String)item$iv$iv;
                Collection collection = destination$iv$iv;
                boolean bl = false;
                object = new String[]{"="};
                collection.add(StringsKt.split$default((CharSequence)it, (String[])object, false, 0, 6, null));
            }
            Iterable $this$associate$iv = (List)destination$iv$iv;
            boolean $i$f$associate = false;
            int capacity$iv = RangesKt.coerceAtLeast(MapsKt.mapCapacity(CollectionsKt.collectionSizeOrDefault($this$associate$iv, 10)), 16);
            destination$iv$iv = $this$associate$iv;
            Map destination$iv$iv2 = new LinkedHashMap(capacity$iv);
            boolean $i$f$associateTo = false;
            for (Object element$iv$iv : $this$associateTo$iv$iv) {
                Map map = destination$iv$iv2;
                List it = (List)element$iv$iv;
                boolean bl = false;
                object = TuplesKt.to(it.get(0), it.get(1));
                map.put(((Pair)object).getFirst(), ((Pair)object).getSecond());
            }
            Map query = destination$iv$iv2;
            if (query.containsKey("code")) {
                try {
                    MicrosoftAccount.OAuthHandler oAuthHandler = this.server.getHandler();
                    Object v = query.get("code");
                    Intrinsics.checkNotNull(v);
                    oAuthHandler.authResult(MicrosoftAccount.Companion.buildFromAuthCode((String)v, this.authMethod));
                    this.response(exchange, "Login Success", 200);
                }
                catch (Exception e) {
                    this.server.getHandler().authError(e.toString());
                    this.response(exchange, Intrinsics.stringPlus("Error: ", e), 500);
                }
            } else {
                this.server.getHandler().authError("No code in the query");
                this.response(exchange, "No code in the query", 500);
            }
            this.server.stop(false);
        }

        private final void response(HttpExchange exchange, String message, int code) {
            String string = message;
            byte[] byArray = string.getBytes(Charsets.UTF_8);
            Intrinsics.checkNotNullExpressionValue(byArray, "this as java.lang.String).getBytes(charset)");
            byte[] byArray2 = byArray;
            exchange.sendResponseHeaders(code, byArray2.length);
            exchange.getResponseBody().write(byArray2);
            exchange.close();
        }
    }
}

