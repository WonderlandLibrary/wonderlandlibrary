/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.Unit
 *  kotlin.concurrent.ThreadsKt
 *  kotlin.jvm.functions.Function0
 *  kotlin.jvm.internal.DefaultConstructorMarker
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.text.StringsKt
 */
package net.ccbluex.liquidbounce.features.module.modules.misc;

import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import kotlin.TypeCastException;
import kotlin.Unit;
import kotlin.concurrent.ThreadsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.LiquidBounce;
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
import net.ccbluex.liquidbounce.chat.packet.packets.ServerRequestJWTPacket;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.SessionEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.misc.LiquidChat;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.login.UserUtils;
import net.ccbluex.liquidbounce.utils.misc.StringUtils;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.value.BoolValue;

@ModuleInfo(name="LiquidChat", description="Allows you to chat with other LiquidBounce users.", category=ModuleCategory.MISC)
public final class LiquidChat
extends Module {
    private final jwtValue.1 jwtValue = new BoolValue(this, "JWT", false){
        final /* synthetic */ LiquidChat this$0;

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
    private final Client client = new Client(this){
        final /* synthetic */ LiquidChat this$0;

        public void onConnect() {
            ClientUtils.displayChatMessage("\u00a77[\u00a7a\u00a7lChat\u00a77] \u00a79Connecting to chat server...");
        }

        public void onConnected() {
            ClientUtils.displayChatMessage("\u00a77[\u00a7a\u00a7lChat\u00a77] \u00a79Connected to chat server!");
        }

        public void onHandshake(boolean success) {
        }

        public void onDisconnect() {
            ClientUtils.displayChatMessage("\u00a77[\u00a7a\u00a7lChat\u00a77] \u00a7cDisconnected from chat server!");
        }

        public void onLogon() {
            ClientUtils.displayChatMessage("\u00a77[\u00a7a\u00a7lChat\u00a77] \u00a79Logging in...");
        }

        /*
         * Enabled aggressive block sorting
         */
        public void onPacket(Packet packet) {
            Packet packet2 = packet;
            if (packet2 instanceof ClientMessagePacket) {
                IEntityPlayerSP thePlayer = MinecraftInstance.mc.getThePlayer();
                if (thePlayer == null) {
                    ClientUtils.getLogger().info("[LiquidChat] " + ((ClientMessagePacket)packet).getUser().getName() + ": " + ((ClientMessagePacket)packet).getContent());
                    return;
                }
                IIChatComponent chatComponent = MinecraftInstance.classProvider.createChatComponentText("\u00a77[\u00a7a\u00a7lChat\u00a77] \u00a79" + ((ClientMessagePacket)packet).getUser().getName() + ": ");
                IIChatComponent messageComponent = LiquidChat.access$toChatComponent(this.this$0, ((ClientMessagePacket)packet).getContent());
                chatComponent.appendSibling(messageComponent);
                thePlayer.addChatMessage(chatComponent);
                return;
            }
            if (packet2 instanceof ClientPrivateMessagePacket) {
                ClientUtils.displayChatMessage("\u00a77[\u00a7a\u00a7lChat\u00a77] \u00a7c(P)\u00a79 " + ((ClientPrivateMessagePacket)packet).getUser().getName() + ": \u00a77" + ((ClientPrivateMessagePacket)packet).getContent());
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
                ClientUtils.displayChatMessage("\u00a77[\u00a7a\u00a7lChat\u00a77] \u00a7cError: \u00a77" + message);
                return;
            }
            if (!(packet2 instanceof ClientSuccessPacket)) {
                if (!(packet2 instanceof ClientNewJWTPacket)) return;
                LiquidChat.Companion.setJwtToken(((ClientNewJWTPacket)packet).getToken());
                LiquidChat.access$getJwtValue$p(this.this$0).set(true);
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
                    ClientUtils.displayChatMessage("\u00a77[\u00a7a\u00a7lChat\u00a77] \u00a79Logged in!");
                    ClientUtils.displayChatMessage("====================================");
                    ClientUtils.displayChatMessage("\u00a7c>> \u00a7lLiquidChat");
                    ClientUtils.displayChatMessage("\u00a77Write message: \u00a7a.chat <message>");
                    ClientUtils.displayChatMessage("\u00a77Write private message: \u00a7a.pchat <user> <message>");
                    ClientUtils.displayChatMessage("====================================");
                    this.setLoggedIn(true);
                    return;
                }
                case 3: {
                    ClientUtils.displayChatMessage("\u00a77[\u00a7a\u00a7lChat\u00a77] \u00a79Successfully banned user!");
                    return;
                }
                case 1: {
                    ClientUtils.displayChatMessage("\u00a77[\u00a7a\u00a7lChat\u00a77] \u00a79Successfully unbanned user!");
                    return;
                }
            }
        }

        public void onError(Throwable cause) {
            ClientUtils.displayChatMessage("\u00a77[\u00a7a\u00a7lChat\u00a77] \u00a7c\u00a7lError: \u00a77" + cause.getClass().getName() + ": " + cause.getMessage());
        }
        {
            this.this$0 = $outer;
        }
    };
    private boolean loggedIn;
    private Thread loginThread;
    private final MSTimer connectTimer = new MSTimer();
    private final Pattern urlPattern;
    private static String jwtToken;
    public static final Companion Companion;

    public final Client getClient() {
        return this.client;
    }

    @Override
    public void onDisable() {
        this.loggedIn = false;
        this.client.disconnect();
    }

    @EventTarget
    public final void onSession(SessionEvent sessionEvent) {
        this.client.disconnect();
        this.connect();
    }

    @EventTarget
    public final void onUpdate(UpdateEvent updateEvent) {
        block6: {
            block5: {
                if (this.client.isConnected()) break block5;
                if (this.loginThread == null) break block6;
                Thread thread2 = this.loginThread;
                if (thread2 == null) {
                    Intrinsics.throwNpe();
                }
                if (!thread2.isAlive()) break block6;
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
                ClientUtils.displayChatMessage("\u00a77[\u00a7a\u00a7lChat\u00a77] \u00a7cError: \u00a77No token provided!");
                this.setState(false);
                return;
            }
        }
        this.loggedIn = false;
        this.loginThread = ThreadsKt.thread$default((boolean)false, (boolean)false, null, null, (int)0, (Function0)((Function0)new Function0<Unit>(this){
            final /* synthetic */ LiquidChat this$0;

            public final void invoke() {
                try {
                    this.this$0.getClient().connect();
                    if (((Boolean)LiquidChat.access$getJwtValue$p(this.this$0).get()).booleanValue()) {
                        this.this$0.getClient().loginJWT(LiquidChat.Companion.getJwtToken());
                    } else if (UserUtils.INSTANCE.isValidToken(MinecraftInstance.mc.getSession().getToken())) {
                        this.this$0.getClient().loginMojang();
                    }
                }
                catch (Exception cause) {
                    ClientUtils.getLogger().error("LiquidChat error", (Throwable)cause);
                    ClientUtils.displayChatMessage("\u00a77[\u00a7a\u00a7lChat\u00a77] \u00a7cError: \u00a77" + cause.getClass().getName() + ": " + cause.getMessage());
                }
                LiquidChat.access$setLoginThread$p(this.this$0, null);
            }
            {
                this.this$0 = liquidChat;
                super(0);
            }
        }), (int)31, null);
    }

    private final IIChatComponent toChatComponent(String string) {
        IIChatComponent component = null;
        Matcher matcher = this.urlPattern.matcher(string);
        int lastEnd = 0;
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            CharSequence charSequence = string;
            boolean bl = false;
            String string2 = charSequence;
            if (string2 == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
            }
            String part = string2.substring(lastEnd, start);
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
            String url = string4.substring(start, end);
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
        String end = string5.substring(lastEnd);
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
                String string6 = ((String)charSequence2).substring(lastEnd);
                iIChatComponent.appendText(string6);
            }
        }
        return component;
    }

    public LiquidChat() {
        LiquidBounce.INSTANCE.getCommandManager().registerCommand(new Command("chat", new String[]{"lc", "irc"}){

            @Override
            public void execute(String[] args) {
                if (args.length > 1) {
                    if (!this.getState()) {
                        this.chat("\u00a7cError: \u00a77LiquidChat is disabled!");
                        return;
                    }
                    if (!this.getClient().isConnected()) {
                        this.chat("\u00a7cError: \u00a7LiquidChat is currently not connected to the server!");
                        return;
                    }
                    String message = StringUtils.toCompleteString(args, 1);
                    this.getClient().sendMessage(message);
                } else {
                    this.chatSyntax("chat <message>");
                }
            }
        });
        LiquidBounce.INSTANCE.getCommandManager().registerCommand(new Command("pchat", new String[]{"privatechat", "lcpm"}){

            @Override
            public void execute(String[] args) {
                if (args.length > 2) {
                    if (!this.getState()) {
                        this.chat("\u00a7cError: \u00a77LiquidChat is disabled!");
                        return;
                    }
                    if (!this.getClient().isConnected()) {
                        this.chat("\u00a7cError: \u00a7LiquidChat is currently not connected to the server!");
                        return;
                    }
                    String target = args[1];
                    String message = StringUtils.toCompleteString(args, 2);
                    this.getClient().sendPrivateMessage(target, message);
                    this.chat("Message was successfully sent.");
                } else {
                    this.chatSyntax("pchat <username> <message>");
                }
            }
        });
        LiquidBounce.INSTANCE.getCommandManager().registerCommand(new Command("chattoken", new String[0]){

            @Override
            public void execute(String[] args) {
                if (args.length > 1) {
                    if (StringsKt.equals((String)args[1], (String)"set", (boolean)true)) {
                        if (args.length > 2) {
                            Companion.setJwtToken(StringUtils.toCompleteString(args, 2));
                            jwtValue.set(true);
                            if (this.getState()) {
                                this.setState(false);
                                this.setState(true);
                            }
                        } else {
                            this.chatSyntax("chattoken set <token>");
                        }
                    } else if (StringsKt.equals((String)args[1], (String)"generate", (boolean)true)) {
                        if (!this.getState()) {
                            this.chat("\u00a7cError: \u00a77LiquidChat is disabled!");
                            return;
                        }
                        this.getClient().sendPacket(new ServerRequestJWTPacket());
                    } else if (StringsKt.equals((String)args[1], (String)"copy", (boolean)true)) {
                        CharSequence charSequence = Companion.getJwtToken();
                        boolean bl = false;
                        if (charSequence.length() == 0) {
                            this.chat("\u00a7cError: \u00a77No token set! Generate one first using '" + LiquidBounce.INSTANCE.getCommandManager().getPrefix() + "chattoken generate'.");
                            return;
                        }
                        StringSelection stringSelection = new StringSelection(Companion.getJwtToken());
                        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, stringSelection);
                        this.chat("\u00a7aCopied to clipboard!");
                    }
                } else {
                    this.chatSyntax("chattoken <set/copy/generate>");
                }
            }
        });
        LiquidBounce.INSTANCE.getCommandManager().registerCommand(new Command("chatadmin", new String[0]){

            @Override
            public void execute(String[] args) {
                if (!this.getState()) {
                    this.chat("\u00a7cError: \u00a77LiquidChat is disabled!");
                    return;
                }
                if (args.length > 1) {
                    if (StringsKt.equals((String)args[1], (String)"ban", (boolean)true)) {
                        if (args.length > 2) {
                            this.getClient().banUser(args[2]);
                        } else {
                            this.chatSyntax("chatadmin ban <username>");
                        }
                    } else if (StringsKt.equals((String)args[1], (String)"unban", (boolean)true)) {
                        if (args.length > 2) {
                            this.getClient().unbanUser(args[2]);
                        } else {
                            this.chatSyntax("chatadmin unban <username>");
                        }
                    }
                } else {
                    this.chatSyntax("chatadmin <ban/unban>");
                }
            }
        });
        this.urlPattern = Pattern.compile("((?:[a-z0-9]{2,}:\\/\\/)?(?:(?:[0-9]{1,3}\\.){3}[0-9]{1,3}|(?:[-\\w_\\.]{1,}\\.[a-z]{2,}?))(?::[0-9]{1,5})?.*?(?=[!\"\u00a7 \n]|$))", 2);
    }

    static {
        Companion = new Companion(null);
        jwtToken = "";
    }

    public static final /* synthetic */ Thread access$getLoginThread$p(LiquidChat $this) {
        return $this.loginThread;
    }

    public static final /* synthetic */ void access$setLoginThread$p(LiquidChat $this, Thread thread2) {
        $this.loginThread = thread2;
    }

    public static final /* synthetic */ IIChatComponent access$toChatComponent(LiquidChat $this, String string) {
        return $this.toChatComponent(string);
    }

    public static final class Companion {
        public final String getJwtToken() {
            return jwtToken;
        }

        public final void setJwtToken(String string) {
            jwtToken = string;
        }

        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }
}

