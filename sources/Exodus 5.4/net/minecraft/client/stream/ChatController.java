/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 *  tv.twitch.AuthToken
 *  tv.twitch.Core
 *  tv.twitch.CoreAPI
 *  tv.twitch.ErrorCode
 *  tv.twitch.StandardCoreAPI
 *  tv.twitch.chat.Chat
 *  tv.twitch.chat.ChatAPI
 *  tv.twitch.chat.ChatBadgeData
 *  tv.twitch.chat.ChatChannelInfo
 *  tv.twitch.chat.ChatEmoticonData
 *  tv.twitch.chat.ChatEvent
 *  tv.twitch.chat.ChatRawMessage
 *  tv.twitch.chat.ChatTokenizationOption
 *  tv.twitch.chat.ChatTokenizedMessage
 *  tv.twitch.chat.ChatUserInfo
 *  tv.twitch.chat.IChatAPIListener
 *  tv.twitch.chat.IChatChannelListener
 *  tv.twitch.chat.StandardChatAPI
 */
package net.minecraft.client.stream;

import com.google.common.collect.Lists;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import net.minecraft.client.stream.TwitchStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tv.twitch.AuthToken;
import tv.twitch.Core;
import tv.twitch.CoreAPI;
import tv.twitch.ErrorCode;
import tv.twitch.StandardCoreAPI;
import tv.twitch.chat.Chat;
import tv.twitch.chat.ChatAPI;
import tv.twitch.chat.ChatBadgeData;
import tv.twitch.chat.ChatChannelInfo;
import tv.twitch.chat.ChatEmoticonData;
import tv.twitch.chat.ChatEvent;
import tv.twitch.chat.ChatRawMessage;
import tv.twitch.chat.ChatTokenizationOption;
import tv.twitch.chat.ChatTokenizedMessage;
import tv.twitch.chat.ChatUserInfo;
import tv.twitch.chat.IChatAPIListener;
import tv.twitch.chat.IChatChannelListener;
import tv.twitch.chat.StandardChatAPI;

public class ChatController {
    protected int field_175993_n = 500;
    protected HashMap<String, ChatChannelListener> field_175998_i;
    protected AuthToken field_153012_j;
    protected int field_153015_m = 128;
    protected EnumEmoticonMode field_175997_k;
    protected Core field_175992_e = null;
    protected String field_153006_d = "";
    protected String field_153004_b = "";
    protected String field_153007_e = "";
    protected Chat field_153008_f = null;
    protected ChatState field_153011_i = ChatState.Uninitialized;
    protected ChatListener field_153003_a = null;
    protected int field_175994_o = 2000;
    private static final Logger LOGGER = LogManager.getLogger();
    protected ChatEmoticonData field_175996_m = null;
    protected EnumEmoticonMode field_175995_l;
    protected IChatAPIListener field_175999_p;

    public boolean func_152993_m() {
        if (this.field_153011_i != ChatState.Initialized) {
            return false;
        }
        ErrorCode errorCode = this.field_153008_f.shutdown();
        if (ErrorCode.failed((ErrorCode)errorCode)) {
            String string = ErrorCode.getString((ErrorCode)errorCode);
            this.func_152995_h(String.format("Error shutting down chat: %s", string));
            return false;
        }
        this.func_152996_t();
        this.func_175985_a(ChatState.ShuttingDown);
        return true;
    }

    protected void func_152996_t() {
        if (this.field_175996_m != null) {
            ErrorCode errorCode = this.field_153008_f.clearEmoticonData();
            if (ErrorCode.succeeded((ErrorCode)errorCode)) {
                this.field_175996_m = null;
                try {
                    if (this.field_153003_a != null) {
                        this.field_153003_a.func_176024_e();
                    }
                }
                catch (Exception exception) {
                    this.func_152995_h(exception.toString());
                }
            } else {
                this.func_152995_h("Error clearing emoticon data: " + ErrorCode.getString((ErrorCode)errorCode));
            }
        }
    }

    public boolean func_175986_a(String string, String string2) {
        if (this.field_153011_i != ChatState.Initialized) {
            return false;
        }
        if (!this.field_175998_i.containsKey(string)) {
            this.func_152995_h("Not in channel: " + string);
            return false;
        }
        ChatChannelListener chatChannelListener = this.field_175998_i.get(string);
        return chatChannelListener.func_176037_b(string2);
    }

    protected void func_152988_s() {
        if (this.field_175996_m == null) {
            this.field_175996_m = new ChatEmoticonData();
            ErrorCode errorCode = this.field_153008_f.getEmoticonData(this.field_175996_m);
            if (ErrorCode.succeeded((ErrorCode)errorCode)) {
                try {
                    if (this.field_153003_a != null) {
                        this.field_153003_a.func_176021_d();
                    }
                }
                catch (Exception exception) {
                    this.func_152995_h(exception.toString());
                }
            } else {
                this.func_152995_h("Error preparing emoticon data: " + ErrorCode.getString((ErrorCode)errorCode));
            }
        }
    }

    public boolean func_175984_n() {
        if (this.field_153011_i != ChatState.Uninitialized) {
            return false;
        }
        this.func_175985_a(ChatState.Initializing);
        ErrorCode errorCode = this.field_175992_e.initialize(this.field_153006_d, null);
        if (ErrorCode.failed((ErrorCode)errorCode)) {
            this.func_175985_a(ChatState.Uninitialized);
            String string = ErrorCode.getString((ErrorCode)errorCode);
            this.func_152995_h(String.format("Error initializing Twitch sdk: %s", string));
            return false;
        }
        this.field_175995_l = this.field_175997_k;
        HashSet<ChatTokenizationOption> hashSet = new HashSet<ChatTokenizationOption>();
        switch (this.field_175997_k) {
            case None: {
                hashSet.add(ChatTokenizationOption.TTV_CHAT_TOKENIZATION_OPTION_NONE);
                break;
            }
            case Url: {
                hashSet.add(ChatTokenizationOption.TTV_CHAT_TOKENIZATION_OPTION_EMOTICON_URLS);
                break;
            }
            case TextureAtlas: {
                hashSet.add(ChatTokenizationOption.TTV_CHAT_TOKENIZATION_OPTION_EMOTICON_TEXTURES);
            }
        }
        errorCode = this.field_153008_f.initialize(hashSet, this.field_175999_p);
        if (ErrorCode.failed((ErrorCode)errorCode)) {
            this.field_175992_e.shutdown();
            this.func_175985_a(ChatState.Uninitialized);
            String string = ErrorCode.getString((ErrorCode)errorCode);
            this.func_152995_h(String.format("Error initializing Twitch chat: %s", string));
            return false;
        }
        this.func_175985_a(ChatState.Initialized);
        return true;
    }

    public ChatState func_153000_j() {
        return this.field_153011_i;
    }

    public void func_152997_n() {
        ErrorCode errorCode;
        if (this.field_153011_i != ChatState.Uninitialized && ErrorCode.failed((ErrorCode)(errorCode = this.field_153008_f.flushEvents()))) {
            String string = ErrorCode.getString((ErrorCode)errorCode);
            this.func_152995_h(String.format("Error flushing chat events: %s", string));
        }
    }

    protected boolean func_175987_a(String string, boolean bl) {
        if (this.field_153011_i != ChatState.Initialized) {
            return false;
        }
        if (this.field_175998_i.containsKey(string)) {
            this.func_152995_h("Already in channel: " + string);
            return false;
        }
        if (string != null && !string.equals("")) {
            ChatChannelListener chatChannelListener = new ChatChannelListener(string);
            this.field_175998_i.put(string, chatChannelListener);
            boolean bl2 = chatChannelListener.func_176038_a(bl);
            if (!bl2) {
                this.field_175998_i.remove(string);
            }
            return bl2;
        }
        return false;
    }

    protected void func_153001_r() {
        ErrorCode errorCode;
        if (this.field_175995_l != EnumEmoticonMode.None && this.field_175996_m == null && ErrorCode.failed((ErrorCode)(errorCode = this.field_153008_f.downloadEmoticonData()))) {
            String string = ErrorCode.getString((ErrorCode)errorCode);
            this.func_152995_h(String.format("Error trying to download emoticon data: %s", string));
        }
    }

    public boolean func_175990_d(String string) {
        if (!this.field_175998_i.containsKey(string)) {
            return false;
        }
        ChatChannelListener chatChannelListener = this.field_175998_i.get(string);
        return chatChannelListener.func_176040_a() == EnumChannelState.Connected;
    }

    public void func_152990_a(ChatListener chatListener) {
        this.field_153003_a = chatListener;
    }

    public ChatController() {
        this.field_153012_j = new AuthToken();
        this.field_175998_i = new HashMap();
        this.field_175997_k = EnumEmoticonMode.None;
        this.field_175995_l = EnumEmoticonMode.None;
        this.field_175999_p = new IChatAPIListener(){

            public void chatEmoticonDataDownloadCallback(ErrorCode errorCode) {
                if (ErrorCode.succeeded((ErrorCode)errorCode)) {
                    ChatController.this.func_152988_s();
                }
            }

            public void chatInitializationCallback(ErrorCode errorCode) {
                if (ErrorCode.succeeded((ErrorCode)errorCode)) {
                    ChatController.this.field_153008_f.setMessageFlushInterval(ChatController.this.field_175993_n);
                    ChatController.this.field_153008_f.setUserChangeEventInterval(ChatController.this.field_175994_o);
                    ChatController.this.func_153001_r();
                    ChatController.this.func_175985_a(ChatState.Initialized);
                } else {
                    ChatController.this.func_175985_a(ChatState.Uninitialized);
                }
                try {
                    if (ChatController.this.field_153003_a != null) {
                        ChatController.this.field_153003_a.func_176023_d(errorCode);
                    }
                }
                catch (Exception exception) {
                    ChatController.this.func_152995_h(exception.toString());
                }
            }

            public void chatShutdownCallback(ErrorCode errorCode) {
                if (ErrorCode.succeeded((ErrorCode)errorCode)) {
                    ErrorCode errorCode2 = ChatController.this.field_175992_e.shutdown();
                    if (ErrorCode.failed((ErrorCode)errorCode2)) {
                        String string = ErrorCode.getString((ErrorCode)errorCode2);
                        ChatController.this.func_152995_h(String.format("Error shutting down the Twitch sdk: %s", string));
                    }
                    ChatController.this.func_175985_a(ChatState.Uninitialized);
                } else {
                    ChatController.this.func_175985_a(ChatState.Initialized);
                    ChatController.this.func_152995_h(String.format("Error shutting down Twith chat: %s", errorCode));
                }
                try {
                    if (ChatController.this.field_153003_a != null) {
                        ChatController.this.field_153003_a.func_176022_e(errorCode);
                    }
                }
                catch (Exception exception) {
                    ChatController.this.func_152995_h(exception.toString());
                }
            }
        };
        this.field_175992_e = Core.getInstance();
        if (this.field_175992_e == null) {
            this.field_175992_e = new Core((CoreAPI)new StandardCoreAPI());
        }
        this.field_153008_f = new Chat((ChatAPI)new StandardChatAPI());
    }

    public boolean func_152986_d(String string) {
        return this.func_175987_a(string, false);
    }

    public EnumChannelState func_175989_e(String string) {
        if (!this.field_175998_i.containsKey(string)) {
            return EnumChannelState.Disconnected;
        }
        ChatChannelListener chatChannelListener = this.field_175998_i.get(string);
        return chatChannelListener.func_176040_a();
    }

    public boolean func_175991_l(String string) {
        if (this.field_153011_i != ChatState.Initialized) {
            return false;
        }
        if (!this.field_175998_i.containsKey(string)) {
            this.func_152995_h("Not in channel: " + string);
            return false;
        }
        ChatChannelListener chatChannelListener = this.field_175998_i.get(string);
        return chatChannelListener.func_176034_g();
    }

    protected void func_175985_a(ChatState chatState) {
        if (chatState != this.field_153011_i) {
            this.field_153011_i = chatState;
            try {
                if (this.field_153003_a != null) {
                    this.field_153003_a.func_176017_a(chatState);
                }
            }
            catch (Exception exception) {
                this.func_152995_h(exception.toString());
            }
        }
    }

    public void func_152994_a(AuthToken authToken) {
        this.field_153012_j = authToken;
    }

    public void func_175988_p() {
        if (this.func_153000_j() != ChatState.Uninitialized) {
            this.func_152993_m();
            if (this.func_153000_j() == ChatState.ShuttingDown) {
                while (this.func_153000_j() != ChatState.Uninitialized) {
                    try {
                        Thread.sleep(200L);
                        this.func_152997_n();
                    }
                    catch (InterruptedException interruptedException) {
                        // empty catch block
                    }
                }
            }
        }
    }

    public void func_152984_a(String string) {
        this.field_153006_d = string;
    }

    public void func_152998_c(String string) {
        this.field_153004_b = string;
    }

    protected void func_152995_h(String string) {
        LOGGER.error(TwitchStream.STREAM_MARKER, "[Chat controller] {}", new Object[]{string});
    }

    public static enum EnumChannelState {
        Created,
        Connecting,
        Connected,
        Disconnecting,
        Disconnected;

    }

    public static enum ChatState {
        Uninitialized,
        Initializing,
        Initialized,
        ShuttingDown;

    }

    public class ChatChannelListener
    implements IChatChannelListener {
        protected String field_176048_a = null;
        protected LinkedList<ChatRawMessage> field_176045_e;
        protected List<ChatUserInfo> field_176044_d;
        protected LinkedList<ChatTokenizedMessage> field_176042_f;
        protected boolean field_176046_b = false;
        protected ChatBadgeData field_176043_g = null;
        protected EnumChannelState field_176047_c = EnumChannelState.Created;

        protected void func_176033_j() {
            if (this.field_176043_g != null) {
                ErrorCode errorCode = ChatController.this.field_153008_f.clearBadgeData(this.field_176048_a);
                if (ErrorCode.succeeded((ErrorCode)errorCode)) {
                    this.field_176043_g = null;
                    try {
                        if (ChatController.this.field_153003_a != null) {
                            ChatController.this.field_153003_a.func_176020_d(this.field_176048_a);
                        }
                    }
                    catch (Exception exception) {
                        ChatController.this.func_152995_h(exception.toString());
                    }
                } else {
                    ChatController.this.func_152995_h("Error releasing badge data: " + ErrorCode.getString((ErrorCode)errorCode));
                }
            }
        }

        public void chatBadgeDataDownloadCallback(String string, ErrorCode errorCode) {
            if (ErrorCode.succeeded((ErrorCode)errorCode)) {
                this.func_176039_i();
            }
        }

        public void chatChannelUserChangeCallback(String string, ChatUserInfo[] chatUserInfoArray, ChatUserInfo[] chatUserInfoArray2, ChatUserInfo[] chatUserInfoArray3) {
            int n;
            int n2 = 0;
            while (n2 < chatUserInfoArray2.length) {
                n = this.field_176044_d.indexOf(chatUserInfoArray2[n2]);
                if (n >= 0) {
                    this.field_176044_d.remove(n);
                }
                ++n2;
            }
            n2 = 0;
            while (n2 < chatUserInfoArray3.length) {
                n = this.field_176044_d.indexOf(chatUserInfoArray3[n2]);
                if (n >= 0) {
                    this.field_176044_d.remove(n);
                }
                this.field_176044_d.add(chatUserInfoArray3[n2]);
                ++n2;
            }
            n2 = 0;
            while (n2 < chatUserInfoArray.length) {
                this.field_176044_d.add(chatUserInfoArray[n2]);
                ++n2;
            }
            try {
                if (ChatController.this.field_153003_a != null) {
                    ChatController.this.field_153003_a.func_176018_a(this.field_176048_a, chatUserInfoArray, chatUserInfoArray2, chatUserInfoArray3);
                }
            }
            catch (Exception exception) {
                ChatController.this.func_152995_h(exception.toString());
            }
        }

        private void func_176030_k() {
            if (this.field_176047_c != EnumChannelState.Disconnected) {
                this.func_176035_a(EnumChannelState.Disconnected);
                this.func_176036_d(this.field_176048_a);
                this.func_176033_j();
            }
        }

        protected void func_176041_h() {
            ErrorCode errorCode;
            if (ChatController.this.field_175995_l != EnumEmoticonMode.None && this.field_176043_g == null && ErrorCode.failed((ErrorCode)(errorCode = ChatController.this.field_153008_f.downloadBadgeData(this.field_176048_a)))) {
                String string = ErrorCode.getString((ErrorCode)errorCode);
                ChatController.this.func_152995_h(String.format("Error trying to download badge data: %s", string));
            }
        }

        protected void func_176035_a(EnumChannelState enumChannelState) {
            if (enumChannelState != this.field_176047_c) {
                this.field_176047_c = enumChannelState;
            }
        }

        public boolean func_176038_a(boolean bl) {
            this.field_176046_b = bl;
            ErrorCode errorCode = ErrorCode.TTV_EC_SUCCESS;
            errorCode = bl ? ChatController.this.field_153008_f.connectAnonymous(this.field_176048_a, (IChatChannelListener)this) : ChatController.this.field_153008_f.connect(this.field_176048_a, ChatController.this.field_153004_b, ChatController.this.field_153012_j.data, (IChatChannelListener)this);
            if (ErrorCode.failed((ErrorCode)errorCode)) {
                String string = ErrorCode.getString((ErrorCode)errorCode);
                ChatController.this.func_152995_h(String.format("Error connecting: %s", string));
                this.func_176036_d(this.field_176048_a);
                return false;
            }
            this.func_176035_a(EnumChannelState.Connecting);
            this.func_176041_h();
            return true;
        }

        public void func_176032_a(String string) {
            if (ChatController.this.field_175995_l == EnumEmoticonMode.None) {
                this.field_176045_e.clear();
                this.field_176042_f.clear();
            } else {
                ChatRawMessage chatRawMessage;
                ListIterator listIterator;
                if (this.field_176045_e.size() > 0) {
                    listIterator = this.field_176045_e.listIterator();
                    while (listIterator.hasNext()) {
                        chatRawMessage = (ChatRawMessage)listIterator.next();
                        if (!chatRawMessage.userName.equals(string)) continue;
                        listIterator.remove();
                    }
                }
                if (this.field_176042_f.size() > 0) {
                    listIterator = this.field_176042_f.listIterator();
                    while (listIterator.hasNext()) {
                        chatRawMessage = (ChatTokenizedMessage)listIterator.next();
                        if (!chatRawMessage.displayName.equals(string)) continue;
                        listIterator.remove();
                    }
                }
            }
            try {
                if (ChatController.this.field_153003_a != null) {
                    ChatController.this.field_153003_a.func_176019_a(this.field_176048_a, string);
                }
            }
            catch (Exception exception) {
                ChatController.this.func_152995_h(exception.toString());
            }
        }

        public EnumChannelState func_176040_a() {
            return this.field_176047_c;
        }

        public boolean func_176034_g() {
            switch (this.field_176047_c) {
                case Connecting: 
                case Connected: {
                    ErrorCode errorCode = ChatController.this.field_153008_f.disconnect(this.field_176048_a);
                    if (ErrorCode.failed((ErrorCode)errorCode)) {
                        String string = ErrorCode.getString((ErrorCode)errorCode);
                        ChatController.this.func_152995_h(String.format("Error disconnecting: %s", string));
                        return false;
                    }
                    this.func_176035_a(EnumChannelState.Disconnecting);
                    return true;
                }
            }
            return false;
        }

        public void chatChannelTokenizedMessageCallback(String string, ChatTokenizedMessage[] chatTokenizedMessageArray) {
            int n = 0;
            while (n < chatTokenizedMessageArray.length) {
                this.field_176042_f.addLast(chatTokenizedMessageArray[n]);
                ++n;
            }
            try {
                if (ChatController.this.field_153003_a != null) {
                    ChatController.this.field_153003_a.func_176025_a(this.field_176048_a, chatTokenizedMessageArray);
                }
            }
            catch (Exception exception) {
                ChatController.this.func_152995_h(exception.toString());
            }
            while (this.field_176042_f.size() > ChatController.this.field_153015_m) {
                this.field_176042_f.removeFirst();
            }
        }

        public ChatChannelListener(String string) {
            this.field_176044_d = Lists.newArrayList();
            this.field_176045_e = new LinkedList();
            this.field_176042_f = new LinkedList();
            this.field_176048_a = string;
        }

        public void chatChannelRawMessageCallback(String string, ChatRawMessage[] chatRawMessageArray) {
            int n = 0;
            while (n < chatRawMessageArray.length) {
                this.field_176045_e.addLast(chatRawMessageArray[n]);
                ++n;
            }
            try {
                if (ChatController.this.field_153003_a != null) {
                    ChatController.this.field_153003_a.func_180605_a(this.field_176048_a, chatRawMessageArray);
                }
            }
            catch (Exception exception) {
                ChatController.this.func_152995_h(exception.toString());
            }
            while (this.field_176045_e.size() > ChatController.this.field_153015_m) {
                this.field_176045_e.removeFirst();
            }
        }

        public void chatChannelMembershipCallback(String string, ChatEvent chatEvent, ChatChannelInfo chatChannelInfo) {
            switch (chatEvent) {
                case TTV_CHAT_JOINED_CHANNEL: {
                    this.func_176035_a(EnumChannelState.Connected);
                    this.func_176031_c(string);
                    break;
                }
                case TTV_CHAT_LEFT_CHANNEL: {
                    this.func_176030_k();
                }
            }
        }

        protected void func_176036_d(String string) {
            try {
                if (ChatController.this.field_153003_a != null) {
                    ChatController.this.field_153003_a.func_180607_b(string);
                }
            }
            catch (Exception exception) {
                ChatController.this.func_152995_h(exception.toString());
            }
        }

        public void chatClearCallback(String string, String string2) {
            this.func_176032_a(string2);
        }

        protected void func_176031_c(String string) {
            try {
                if (ChatController.this.field_153003_a != null) {
                    ChatController.this.field_153003_a.func_180606_a(string);
                }
            }
            catch (Exception exception) {
                ChatController.this.func_152995_h(exception.toString());
            }
        }

        protected void func_176039_i() {
            if (this.field_176043_g == null) {
                this.field_176043_g = new ChatBadgeData();
                ErrorCode errorCode = ChatController.this.field_153008_f.getBadgeData(this.field_176048_a, this.field_176043_g);
                if (ErrorCode.succeeded((ErrorCode)errorCode)) {
                    try {
                        if (ChatController.this.field_153003_a != null) {
                            ChatController.this.field_153003_a.func_176016_c(this.field_176048_a);
                        }
                    }
                    catch (Exception exception) {
                        ChatController.this.func_152995_h(exception.toString());
                    }
                } else {
                    ChatController.this.func_152995_h("Error preparing badge data: " + ErrorCode.getString((ErrorCode)errorCode));
                }
            }
        }

        public boolean func_176037_b(String string) {
            if (this.field_176047_c != EnumChannelState.Connected) {
                return false;
            }
            ErrorCode errorCode = ChatController.this.field_153008_f.sendMessage(this.field_176048_a, string);
            if (ErrorCode.failed((ErrorCode)errorCode)) {
                String string2 = ErrorCode.getString((ErrorCode)errorCode);
                ChatController.this.func_152995_h(String.format("Error sending chat message: %s", string2));
                return false;
            }
            return true;
        }

        public void chatStatusCallback(String string, ErrorCode errorCode) {
            if (!ErrorCode.succeeded((ErrorCode)errorCode)) {
                ChatController.this.field_175998_i.remove(string);
                this.func_176030_k();
            }
        }
    }

    public static enum EnumEmoticonMode {
        None,
        Url,
        TextureAtlas;

    }

    public static interface ChatListener {
        public void func_176019_a(String var1, String var2);

        public void func_176016_c(String var1);

        public void func_180606_a(String var1);

        public void func_176022_e(ErrorCode var1);

        public void func_180607_b(String var1);

        public void func_176025_a(String var1, ChatTokenizedMessage[] var2);

        public void func_176024_e();

        public void func_176018_a(String var1, ChatUserInfo[] var2, ChatUserInfo[] var3, ChatUserInfo[] var4);

        public void func_176023_d(ErrorCode var1);

        public void func_176021_d();

        public void func_176020_d(String var1);

        public void func_176017_a(ChatState var1);

        public void func_180605_a(String var1, ChatRawMessage[] var2);
    }
}

