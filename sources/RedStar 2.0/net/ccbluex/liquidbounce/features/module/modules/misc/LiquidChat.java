package net.ccbluex.liquidbounce.features.module.modules.misc;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.Unit;
import kotlin.concurrent.ThreadsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.event.IClickEvent;
import net.ccbluex.liquidbounce.api.minecraft.util.IIChatComponent;
import net.ccbluex.liquidbounce.api.minecraft.util.WEnumChatFormatting;
import net.ccbluex.liquidbounce.chat.Client;
import net.ccbluex.liquidbounce.chat.packet.packets.ClientErrorPacket;
import net.ccbluex.liquidbounce.chat.packet.packets.ClientMessagePacket;
import net.ccbluex.liquidbounce.chat.packet.packets.ClientNewJWTPacket;
import net.ccbluex.liquidbounce.chat.packet.packets.ClientPrivateMessagePacket;
import net.ccbluex.liquidbounce.chat.packet.packets.ClientSuccessPacket;
import net.ccbluex.liquidbounce.chat.packet.packets.Packet;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.SessionEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.login.UserUtils;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.value.BoolValue;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="LiquidChat", description="Allows you to chat with other LiquidBounce users.", category=ModuleCategory.MISC)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000Z\n\n\n\b\n\n\b\n\n\u0000\n\n\b\n\n\u0000\n\n\u0000\n\n\b\n\n\b\n\n\b\n\n\u0000\n\n\u0000\n\n\b\b\u0000 !20:!B¢J\b0HJ\b0HJ020HJ020HJ020 HR0¢\b\n\u0000\bR0\bX¢\n\u0000R\t0\n¢\b\n\u0000\b\fR\r0X¢\n\u0000R0X¢\n\u0000R\n *00X¢\n\u0000¨\""}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/misc/LiquidChat;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "client", "Lnet/ccbluex/liquidbounce/chat/Client;", "getClient", "()Lnet/ccbluex/liquidbounce/chat/Client;", "connectTimer", "Lnet/ccbluex/liquidbounce/utils/timer/MSTimer;", "jwtValue", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "getJwtValue", "()Lnet/ccbluex/liquidbounce/value/BoolValue;", "loggedIn", "", "loginThread", "Ljava/lang/Thread;", "urlPattern", "Ljava/util/regex/Pattern;", "kotlin.jvm.PlatformType", "connect", "", "onDisable", "onSession", "sessionEvent", "Lnet/ccbluex/liquidbounce/event/SessionEvent;", "onUpdate", "updateEvent", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "toChatComponent", "Lnet/ccbluex/liquidbounce/api/minecraft/util/IIChatComponent;", "string", "", "Companion", "Pride"})
public final class LiquidChat
extends Module {
    @NotNull
    private final BoolValue jwtValue;
    @NotNull
    private final Client client;
    private boolean loggedIn;
    private Thread loginThread;
    private final MSTimer connectTimer;
    private final Pattern urlPattern;
    @NotNull
    private static String jwtToken;
    public static final Companion Companion;

    @NotNull
    public final BoolValue getJwtValue() {
        return this.jwtValue;
    }

    @NotNull
    public final Client getClient() {
        return this.client;
    }

    @Override
    public void onDisable() {
        this.loggedIn = false;
        this.client.disconnect();
    }

    @EventTarget
    public final void onSession(@NotNull SessionEvent sessionEvent) {
        Intrinsics.checkParameterIsNotNull(sessionEvent, "sessionEvent");
        this.client.disconnect();
        this.connect();
    }

    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent updateEvent) {
        block6: {
            block5: {
                Intrinsics.checkParameterIsNotNull(updateEvent, "updateEvent");
                if (this.client.isConnected()) break block5;
                if (this.loginThread == null) break block6;
                Thread thread = this.loginThread;
                if (thread == null) {
                    Intrinsics.throwNpe();
                }
                if (!thread.isAlive()) break block6;
            }
            return;
        }
        if (this.connectTimer.hasTimePassed(5000L)) {
            this.connect();
            this.connectTimer.reset();
        }
    }

    private final void connect() {
        block7: {
            block6: {
                if (this.client.isConnected()) break block6;
                if (this.loginThread == null) break block7;
                Thread thread2 = this.loginThread;
                if (thread2 == null) {
                    Intrinsics.throwNpe();
                }
                if (!thread2.isAlive()) break block7;
            }
            return;
        }
        if (((Boolean)this.jwtValue.get()).booleanValue()) {
            CharSequence charSequence = jwtToken;
            boolean bl = false;
            if (charSequence.length() == 0) {
                ClientUtils.displayChatMessage("§7[§a§lChat§7] §cError: §7No token provided!");
                this.setState(false);
                return;
            }
        }
        this.loggedIn = false;
        this.loginThread = ThreadsKt.thread$default(false, false, null, null, 0, new Function0<Unit>(this){
            final LiquidChat this$0;

            public final void invoke() {
                try {
                    this.this$0.getClient().connect();
                    if (((Boolean)this.this$0.getJwtValue().get()).booleanValue()) {
                        this.this$0.getClient().loginJWT(LiquidChat.Companion.getJwtToken());
                    } else if (UserUtils.INSTANCE.isValidToken(MinecraftInstance.mc.getSession().getToken())) {
                        this.this$0.getClient().loginMojang();
                    }
                }
                catch (Exception cause) {
                    ClientUtils.getLogger().error("LiquidChat error", (Throwable)cause);
                    ClientUtils.displayChatMessage("§7[§a§lChat§7] §cError: §7" + cause.getClass().getName() + ": " + cause.getMessage());
                }
                LiquidChat.access$setLoginThread$p(this.this$0, null);
            }
            {
                this.this$0 = liquidChat;
                super(0);
            }
        }, 31, null);
    }

    private final IIChatComponent toChatComponent(String string) {
        IIChatComponent component = null;
        Matcher matcher = this.urlPattern.matcher(string);
        int lastEnd = 0;
        while (matcher.find()) {
            String url;
            String part;
            int start = matcher.start();
            int end = matcher.end();
            CharSequence charSequence = string;
            boolean bl = false;
            String string2 = charSequence;
            if (string2 == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
            }
            Intrinsics.checkExpressionValueIsNotNull(string2.substring(lastEnd, start), "(this as java.lang.Strin…ing(startIndex, endIndex)");
            charSequence = part;
            bl = false;
            if (charSequence.length() > 0) {
                if (component == null) {
                    component = MinecraftInstance.classProvider.createChatComponentText(part);
                    component.getChatStyle().setColor(WEnumChatFormatting.GRAY);
                } else {
                    component.appendText(part);
                }
            }
            lastEnd = end;
            String string3 = string;
            boolean bl2 = false;
            String string4 = string3;
            if (string4 == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
            }
            Intrinsics.checkExpressionValueIsNotNull(string4.substring(start, end), "(this as java.lang.Strin…ing(startIndex, endIndex)");
            try {
                if (new URI(url).getScheme() != null) {
                    IIChatComponent link = MinecraftInstance.classProvider.createChatComponentText(url);
                    link.getChatStyle().setChatClickEvent(MinecraftInstance.classProvider.createClickEvent(IClickEvent.WAction.OPEN_URL, url));
                    link.getChatStyle().setUnderlined(true);
                    link.getChatStyle().setColor(WEnumChatFormatting.GRAY);
                    if (component == null) {
                        component = link;
                        continue;
                    }
                    component.appendSibling(link);
                    continue;
                }
            }
            catch (URISyntaxException uRISyntaxException) {
                // empty catch block
            }
            if (component == null) {
                component = MinecraftInstance.classProvider.createChatComponentText(url);
                component.getChatStyle().setColor(WEnumChatFormatting.GRAY);
                continue;
            }
            component.appendText(url);
        }
        CharSequence charSequence = string;
        boolean bl = false;
        String string5 = charSequence;
        if (string5 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        String string6 = string5.substring(lastEnd);
        Intrinsics.checkExpressionValueIsNotNull(string6, "(this as java.lang.String).substring(startIndex)");
        String end = string6;
        if (component == null) {
            component = MinecraftInstance.classProvider.createChatComponentText(end);
            component.getChatStyle().setColor(WEnumChatFormatting.GRAY);
        } else {
            charSequence = end;
            bl = false;
            if (charSequence.length() > 0) {
                charSequence = string;
                IIChatComponent iIChatComponent = component;
                bl = false;
                CharSequence charSequence2 = charSequence;
                if (charSequence2 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                }
                String string7 = ((String)charSequence2).substring(lastEnd);
                Intrinsics.checkExpressionValueIsNotNull(string7, "(this as java.lang.String).substring(startIndex)");
                String string8 = string7;
                iIChatComponent.appendText(string8);
            }
        }
        return component;
    }

    public LiquidChat() {
        this.setState(true);
        this.setArray(false);
        this.jwtValue = new BoolValue(this, "JWT", false){
            final LiquidChat this$0;

            protected void onChanged(boolean oldValue, boolean newValue) {
                if (this.this$0.getState()) {
                    this.this$0.setState(false);
                    this.this$0.setState(true);
                }
            }
            {
                this.this$0 = $outer;
                super($super_call_param$1, $super_call_param$2);
            }
        };
        this.client = new Client(this){
            final LiquidChat this$0;

            public void onConnect() {
                ClientUtils.displayChatMessage("§7[§a§lChat§7] §9Connecting to chat server...");
            }

            public void onConnected() {
                ClientUtils.displayChatMessage("§7[§a§lChat§7] §9Connected to chat server!");
            }

            public void onHandshake(boolean success) {
            }

            public void onDisconnect() {
                ClientUtils.displayChatMessage("§7[§a§lChat§7] §cDisconnected from chat server!");
            }

            public void onLogon() {
                ClientUtils.displayChatMessage("§7[§a§lChat§7] §9Logging in...");
            }

            /*
             * Enabled aggressive block sorting
             */
            public void onPacket(@NotNull Packet packet) {
                Intrinsics.checkParameterIsNotNull(packet, "packet");
                Packet packet2 = packet;
                if (packet2 instanceof ClientMessagePacket) {
                    IEntityPlayerSP thePlayer = MinecraftInstance.mc.getThePlayer();
                    if (thePlayer == null) {
                        ClientUtils.getLogger().info("[LiquidChat] " + ((ClientMessagePacket)packet).getUser().getName() + ": " + ((ClientMessagePacket)packet).getContent());
                        return;
                    }
                    IIChatComponent chatComponent = MinecraftInstance.classProvider.createChatComponentText("§7[§a§lChat§7] §9" + ((ClientMessagePacket)packet).getUser().getName() + ": ");
                    IIChatComponent messageComponent = LiquidChat.access$toChatComponent(this.this$0, ((ClientMessagePacket)packet).getContent());
                    chatComponent.appendSibling(messageComponent);
                    thePlayer.addChatMessage(chatComponent);
                    return;
                }
                if (packet2 instanceof ClientPrivateMessagePacket) {
                    ClientUtils.displayChatMessage("§7[§a§lChat§7] §c(P)§9 " + ((ClientPrivateMessagePacket)packet).getUser().getName() + ": §7" + ((ClientPrivateMessagePacket)packet).getContent());
                    return;
                }
                if (packet2 instanceof ClientErrorPacket) {
                    String string;
                    switch (((ClientErrorPacket)packet).getMessage()) {
                        case "NotSupported": {
                            string = "This method is not supported!";
                            break;
                        }
                        case "LoginFailed": {
                            string = "Login Failed!";
                            break;
                        }
                        case "NotLoggedIn": {
                            string = "You must be logged in to use the chat! Enable LiquidChat.";
                            break;
                        }
                        case "AlreadyLoggedIn": {
                            string = "You are already logged in!";
                            break;
                        }
                        case "MojangRequestMissing": {
                            string = "Mojang request missing!";
                            break;
                        }
                        case "NotPermitted": {
                            string = "You are missing the required permissions!";
                            break;
                        }
                        case "NotBanned": {
                            string = "You are not banned!";
                            break;
                        }
                        case "Banned": {
                            string = "You are banned!";
                            break;
                        }
                        case "RateLimited": {
                            string = "You have been rate limited. Please try again later.";
                            break;
                        }
                        case "PrivateMessageNotAccepted": {
                            string = "Private message not accepted!";
                            break;
                        }
                        case "EmptyMessage": {
                            string = "You are trying to send an empty message!";
                            break;
                        }
                        case "MessageTooLong": {
                            string = "Message is too long!";
                            break;
                        }
                        case "InvalidCharacter": {
                            string = "Message contains a non-ASCII character!";
                            break;
                        }
                        case "InvalidId": {
                            string = "The given ID is invalid!";
                            break;
                        }
                        case "Internal": {
                            string = "An internal server error occurred!";
                            break;
                        }
                        default: {
                            string = ((ClientErrorPacket)packet).getMessage();
                        }
                    }
                    String message = string;
                    ClientUtils.displayChatMessage("§7[§a§lChat§7] §cError: §7" + message);
                    return;
                }
                if (!(packet2 instanceof ClientSuccessPacket)) {
                    if (!(packet2 instanceof ClientNewJWTPacket)) return;
                    LiquidChat.Companion.setJwtToken(((ClientNewJWTPacket)packet).getToken());
                    this.this$0.getJwtValue().set(true);
                    this.this$0.setState(false);
                    this.this$0.setState(true);
                    return;
                }
                String string = ((ClientSuccessPacket)packet).getReason();
                int n = -1;
                switch (string.hashCode()) {
                    case 81873590: {
                        if (!string.equals("Unban")) return;
                        n = 1;
                        break;
                    }
                    case 73596745: {
                        if (!string.equals("Login")) return;
                        n = 2;
                        break;
                    }
                    case 66543: {
                        if (!string.equals("Ban")) return;
                        n = 3;
                    }
                }
                switch (n) {
                    case 2: {
                        ClientUtils.displayChatMessage("§7[§a§lChat§7] §9Logged in!");
                        ClientUtils.displayChatMessage("====================================");
                        ClientUtils.displayChatMessage("§c>> §lLiquidChat");
                        ClientUtils.displayChatMessage("§7Write message: §a.chat <message>");
                        ClientUtils.displayChatMessage("§7Write private message: §a.pchat <user> <message>");
                        ClientUtils.displayChatMessage("====================================");
                        this.setLoggedIn(true);
                        return;
                    }
                    case 3: {
                        ClientUtils.displayChatMessage("§7[§a§lChat§7] §9Successfully banned user!");
                        return;
                    }
                    case 1: {
                        ClientUtils.displayChatMessage("§7[§a§lChat§7] §9Successfully unbanned user!");
                        return;
                    }
                }
            }

            public void onError(@NotNull Throwable cause) {
                Intrinsics.checkParameterIsNotNull(cause, "cause");
                ClientUtils.displayChatMessage("§7[§a§lChat§7] §c§lError: §7" + cause.getClass().getName() + ": " + cause.getMessage());
            }
            {
                this.this$0 = $outer;
            }
        };
        this.connectTimer = new MSTimer();
        this.urlPattern = Pattern.compile("((?:[a-z0-9]{2,}:\\/\\/)?(?:(?:[0-9]{1,3}\\.){3}[0-9]{1,3}|(?:[-\\w_\\.]{1,}\\.[a-z]{2,}?))(?::[0-9]{1,5})?.*?(?=[!\"§ \n]|$))", 2);
    }

    static {
        Companion = new Companion(null);
        jwtToken = "";
    }

    public static final Thread access$getLoginThread$p(LiquidChat $this) {
        return $this.loginThread;
    }

    public static final void access$setLoginThread$p(LiquidChat $this, Thread thread) {
        $this.loginThread = thread;
    }

    public static final IIChatComponent access$toChatComponent(LiquidChat $this, String string) {
        return $this.toChatComponent(string);
    }

    @Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\n\n\u0000\n\b\n\n\b\b\u000020B\b¢R0X¢\n\u0000\b\"\b\b¨\t"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/misc/LiquidChat$Companion;", "", "()V", "jwtToken", "", "getJwtToken", "()Ljava/lang/String;", "setJwtToken", "(Ljava/lang/String;)V", "Pride"})
    public static final class Companion {
        @NotNull
        public final String getJwtToken() {
            return jwtToken;
        }

        public final void setJwtToken(@NotNull String string) {
            Intrinsics.checkParameterIsNotNull(string, "<set-?>");
            jwtToken = string;
        }

        private Companion() {
        }

        public Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }
}
