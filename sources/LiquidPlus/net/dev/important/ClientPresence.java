/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 *  org.lwjgl.opengl.Display
 */
package net.dev.important;

import com.jagrosh.discordipc.IPCClient;
import com.jagrosh.discordipc.IPCListener;
import com.jagrosh.discordipc.entities.DiscordBuild;
import com.jagrosh.discordipc.entities.RichPresence;
import com.jagrosh.discordipc.entities.pipe.PipeStatus;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.concurrent.ThreadsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import net.dev.important.Client;
import net.dev.important.modules.module.Module;
import net.dev.important.utils.ClientUtils;
import net.dev.important.utils.MinecraftInstance;
import oh.yalan.NativeClass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;
import org.lwjgl.opengl.Display;

@NativeClass
@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0010%\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0004\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0014\u001a\u00020\u0015H\u0002J\u0006\u0010\u0016\u001a\u00020\u0015J\u0006\u0010\u0017\u001a\u00020\u0015J\u0006\u0010\u0018\u001a\u00020\u0015R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0005\u001a\u000e\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\b\u001a\u0004\u0018\u00010\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001a\u0010\f\u001a\u00020\u000bX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\u000e\"\u0004\b\u000f\u0010\u0010R\u0016\u0010\u0011\u001a\n \u0013*\u0004\u0018\u00010\u00120\u0012X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0019"}, d2={"Lnet/dev/important/ClientPresence;", "Lnet/dev/important/utils/MinecraftInstance;", "()V", "appID", "", "assets", "", "", "ipcClient", "Lcom/jagrosh/discordipc/IPCClient;", "running", "", "showRichPresenceValue", "getShowRichPresenceValue", "()Z", "setShowRichPresenceValue", "(Z)V", "timestamp", "Ljava/time/OffsetDateTime;", "kotlin.jvm.PlatformType", "loadConfiguration", "", "setup", "shutdown", "update", "LiquidBounce"})
public final class ClientPresence
extends MinecraftInstance {
    private boolean showRichPresenceValue = true;
    @Nullable
    private IPCClient ipcClient;
    private long appID;
    @NotNull
    private final Map<String, String> assets = new LinkedHashMap();
    private final OffsetDateTime timestamp = OffsetDateTime.now();
    private boolean running;

    public final boolean getShowRichPresenceValue() {
        return this.showRichPresenceValue;
    }

    public final void setShowRichPresenceValue(boolean bl) {
        this.showRichPresenceValue = bl;
    }

    public final void setup() {
        try {
            this.running = true;
            this.loadConfiguration();
            IPCClient iPCClient = this.ipcClient = new IPCClient(this.appID);
            if (iPCClient != null) {
                iPCClient.setListener(new IPCListener(this){
                    final /* synthetic */ ClientPresence this$0;
                    {
                        this.this$0 = $receiver;
                    }

                    public void onReady(@Nullable IPCClient client) {
                        ThreadsKt.thread$default(false, false, null, null, 0, new Function0<Unit>(this.this$0){
                            final /* synthetic */ ClientPresence this$0;
                            {
                                this.this$0 = $receiver;
                                super(0);
                            }

                            public final void invoke() {
                                while (ClientPresence.access$getRunning$p(this.this$0)) {
                                    this.this$0.update();
                                    try {
                                        Thread.sleep(1000L);
                                    }
                                    catch (InterruptedException interruptedException) {}
                                }
                            }
                        }, 31, null);
                    }

                    public void onClose(@Nullable IPCClient client, @Nullable JSONObject json) {
                        ClientPresence.access$setRunning$p(this.this$0, false);
                    }
                });
            }
            IPCClient iPCClient2 = this.ipcClient;
            if (iPCClient2 != null) {
                iPCClient2.connect(new DiscordBuild[0]);
            }
        }
        catch (Throwable e) {
            ClientUtils.getLogger().error("Failed to setup Discord RPC.", e);
        }
    }

    /*
     * Unable to fully structure code
     */
    public final void update() {
        block12: {
            block11: {
                builder = new RichPresence.Builder();
                builder.setStartTimestamp(this.timestamp);
                if (this.assets.containsKey("new")) {
                    builder.setLargeImage(this.assets.get("new"), "build ");
                }
                serverData = MinecraftInstance.mc.func_147104_D();
                builder.setDetails(Display.isActive() ? (MinecraftInstance.mc.func_71387_A() || serverData != null ? "Playing" : "Idle...") : "AFK");
                builder.setState(Intrinsics.stringPlus("IGN: ", MinecraftInstance.mc.field_71449_j.func_111285_a()));
                if (!MinecraftInstance.mc.func_71387_A() && serverData == null) break block11;
                v0 = new StringBuilder().append("in ");
                if (MinecraftInstance.mc.func_71387_A()) ** GOTO lbl-1000
                v1 = serverData;
                if (v1 == null) lbl-1000:
                // 2 sources

                {
                    v2 = "Singleplayer";
                } else {
                    v2 = v1.field_78845_b;
                }
                var3_3 = Client.INSTANCE.getModuleManager().getModules();
                var12_5 = v0.append((Object)v2).append(" - ");
                var11_7 = this.assets.get("astolfo");
                var10_9 = builder;
                $i$f$count = false;
                if ($this$count$iv instanceof Collection && ((Collection)$this$count$iv).isEmpty()) {
                    v3 = 0;
                } else {
                    count$iv = 0;
                    for (T element$iv : $this$count$iv) {
                        it = (Module)element$iv;
                        $i$a$-count-ClientPresence$update$1 = false;
                        if (!it.getState() || ++count$iv >= 0) continue;
                        CollectionsKt.throwCountOverflow();
                    }
                    v3 = count$iv;
                }
                var13_23 = v3;
                var10_9.setSmallImage(var11_7, var12_5.append(var13_23).append('/').append(Client.INSTANCE.getModuleManager().getModules().size()).append(" enabled.").toString());
                break block12;
            }
            $this$count$iv = Client.INSTANCE.getModuleManager().getModules();
            var12_6 = new StringBuilder();
            var11_8 = this.assets.get("astolfo");
            var10_10 = builder;
            $i$f$count = false;
            if ($this$count$iv instanceof Collection && ((Collection)$this$count$iv).isEmpty()) {
                v4 = 0;
            } else {
                count$iv = 0;
                for (T element$iv : $this$count$iv) {
                    it = (Module)element$iv;
                    $i$a$-count-ClientPresence$update$2 = false;
                    if (!it.getState() || ++count$iv >= 0) continue;
                    CollectionsKt.throwCountOverflow();
                }
                v4 = count$iv;
            }
            var13_24 = v4;
            var10_10.setSmallImage(var11_8, var12_6.append(var13_24).append('/').append(Client.INSTANCE.getModuleManager().getModules().size()).append(" enabled.").toString());
        }
        v5 = this.ipcClient;
        if ((v5 == null ? null : v5.getStatus()) == PipeStatus.CONNECTED) {
            v6 = this.ipcClient;
            if (v6 != null) {
                v6.sendRichPresence(builder.build());
            }
        }
    }

    public final void shutdown() {
        IPCClient iPCClient = this.ipcClient;
        if ((iPCClient == null ? null : iPCClient.getStatus()) != PipeStatus.CONNECTED) {
            return;
        }
        try {
            IPCClient iPCClient2 = this.ipcClient;
            if (iPCClient2 != null) {
                iPCClient2.close();
            }
        }
        catch (Throwable e) {
            ClientUtils.getLogger().error("Failed to close Discord RPC.", e);
        }
    }

    private final void loadConfiguration() {
        this.appID = 874149528486445106L;
        this.assets.put("new", "new");
        this.assets.put("astolfo", "astolfo");
    }

    public static final /* synthetic */ boolean access$getRunning$p(ClientPresence $this) {
        return $this.running;
    }

    public static final /* synthetic */ void access$setRunning$p(ClientPresence $this, boolean bl) {
        $this.running = bl;
    }
}

