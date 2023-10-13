/*
 * Decompiled with CFR 0.150.
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
    private static final Logger LOGGER = LogManager.getLogger();
    protected ChatListener field_153003_a = null;
    protected String field_153004_b = "";
    protected String field_153006_d = "";
    protected String field_153007_e = "";
    protected Core field_175992_e = null;
    protected Chat field_153008_f = null;
    protected ChatState field_153011_i = ChatState.Uninitialized;
    protected AuthToken field_153012_j = new AuthToken();
    protected HashMap field_175998_i = new HashMap();
    protected int field_153015_m = 128;
    protected EnumEmoticonMode field_175997_k = EnumEmoticonMode.None;
    protected EnumEmoticonMode field_175995_l = EnumEmoticonMode.None;
    protected ChatEmoticonData field_175996_m = null;
    protected int field_175993_n = 500;
    protected int field_175994_o = 2000;
    protected IChatAPIListener field_175999_p = new IChatAPIListener(){
        private static final String __OBFID = "CL_00002373";

        public void chatInitializationCallback(ErrorCode p_chatInitializationCallback_1_) {
            if (ErrorCode.succeeded((ErrorCode)p_chatInitializationCallback_1_)) {
                ChatController.this.field_153008_f.setMessageFlushInterval(ChatController.this.field_175993_n);
                ChatController.this.field_153008_f.setUserChangeEventInterval(ChatController.this.field_175994_o);
                ChatController.this.func_153001_r();
                ChatController.this.func_175985_a(ChatState.Initialized);
            } else {
                ChatController.this.func_175985_a(ChatState.Uninitialized);
            }
            try {
                if (ChatController.this.field_153003_a != null) {
                    ChatController.this.field_153003_a.func_176023_d(p_chatInitializationCallback_1_);
                }
            }
            catch (Exception var3) {
                ChatController.this.func_152995_h(var3.toString());
            }
        }

        public void chatShutdownCallback(ErrorCode p_chatShutdownCallback_1_) {
            if (ErrorCode.succeeded((ErrorCode)p_chatShutdownCallback_1_)) {
                ErrorCode var2 = ChatController.this.field_175992_e.shutdown();
                if (ErrorCode.failed((ErrorCode)var2)) {
                    String var3 = ErrorCode.getString((ErrorCode)var2);
                    ChatController.this.func_152995_h(String.format("Error shutting down the Twitch sdk: %s", var3));
                }
                ChatController.this.func_175985_a(ChatState.Uninitialized);
            } else {
                ChatController.this.func_175985_a(ChatState.Initialized);
                ChatController.this.func_152995_h(String.format("Error shutting down Twith chat: %s", new Object[]{p_chatShutdownCallback_1_}));
            }
            try {
                if (ChatController.this.field_153003_a != null) {
                    ChatController.this.field_153003_a.func_176022_e(p_chatShutdownCallback_1_);
                }
            }
            catch (Exception var4) {
                ChatController.this.func_152995_h(var4.toString());
            }
        }

        public void chatEmoticonDataDownloadCallback(ErrorCode p_chatEmoticonDataDownloadCallback_1_) {
            if (ErrorCode.succeeded((ErrorCode)p_chatEmoticonDataDownloadCallback_1_)) {
                ChatController.this.func_152988_s();
            }
        }
    };
    private static final String __OBFID = "CL_00001819";

    public void func_152990_a(ChatListener p_152990_1_) {
        this.field_153003_a = p_152990_1_;
    }

    public void func_152994_a(AuthToken p_152994_1_) {
        this.field_153012_j = p_152994_1_;
    }

    public void func_152984_a(String p_152984_1_) {
        this.field_153006_d = p_152984_1_;
    }

    public void func_152998_c(String p_152998_1_) {
        this.field_153004_b = p_152998_1_;
    }

    public ChatState func_153000_j() {
        return this.field_153011_i;
    }

    public boolean func_175990_d(String p_175990_1_) {
        if (!this.field_175998_i.containsKey(p_175990_1_)) {
            return false;
        }
        ChatChannelListener var2 = (ChatChannelListener)this.field_175998_i.get(p_175990_1_);
        return var2.func_176040_a() == EnumChannelState.Connected;
    }

    public EnumChannelState func_175989_e(String p_175989_1_) {
        if (!this.field_175998_i.containsKey(p_175989_1_)) {
            return EnumChannelState.Disconnected;
        }
        ChatChannelListener var2 = (ChatChannelListener)this.field_175998_i.get(p_175989_1_);
        return var2.func_176040_a();
    }

    public ChatController() {
        this.field_175992_e = Core.getInstance();
        if (this.field_175992_e == null) {
            this.field_175992_e = new Core((CoreAPI)new StandardCoreAPI());
        }
        this.field_153008_f = new Chat((ChatAPI)new StandardChatAPI());
    }

    public boolean func_175984_n() {
        if (this.field_153011_i != ChatState.Uninitialized) {
            return false;
        }
        this.func_175985_a(ChatState.Initializing);
        ErrorCode var1 = this.field_175992_e.initialize(this.field_153006_d, null);
        if (ErrorCode.failed((ErrorCode)var1)) {
            this.func_175985_a(ChatState.Uninitialized);
            String var4 = ErrorCode.getString((ErrorCode)var1);
            this.func_152995_h(String.format("Error initializing Twitch sdk: %s", var4));
            return false;
        }
        this.field_175995_l = this.field_175997_k;
        HashSet<ChatTokenizationOption> var2 = new HashSet<ChatTokenizationOption>();
        switch (this.field_175997_k) {
            case None: {
                var2.add(ChatTokenizationOption.TTV_CHAT_TOKENIZATION_OPTION_NONE);
                break;
            }
            case Url: {
                var2.add(ChatTokenizationOption.TTV_CHAT_TOKENIZATION_OPTION_EMOTICON_URLS);
                break;
            }
            case TextureAtlas: {
                var2.add(ChatTokenizationOption.TTV_CHAT_TOKENIZATION_OPTION_EMOTICON_TEXTURES);
            }
        }
        var1 = this.field_153008_f.initialize(var2, this.field_175999_p);
        if (ErrorCode.failed((ErrorCode)var1)) {
            this.field_175992_e.shutdown();
            this.func_175985_a(ChatState.Uninitialized);
            String var3 = ErrorCode.getString((ErrorCode)var1);
            this.func_152995_h(String.format("Error initializing Twitch chat: %s", var3));
            return false;
        }
        this.func_175985_a(ChatState.Initialized);
        return true;
    }

    public boolean func_152986_d(String p_152986_1_) {
        return this.func_175987_a(p_152986_1_, false);
    }

    protected boolean func_175987_a(String p_175987_1_, boolean p_175987_2_) {
        if (this.field_153011_i != ChatState.Initialized) {
            return false;
        }
        if (this.field_175998_i.containsKey(p_175987_1_)) {
            this.func_152995_h("Already in channel: " + p_175987_1_);
            return false;
        }
        if (p_175987_1_ != null && !p_175987_1_.equals("")) {
            ChatChannelListener var3 = new ChatChannelListener(p_175987_1_);
            this.field_175998_i.put(p_175987_1_, var3);
            boolean var4 = var3.func_176038_a(p_175987_2_);
            if (!var4) {
                this.field_175998_i.remove(p_175987_1_);
            }
            return var4;
        }
        return false;
    }

    public boolean func_175991_l(String p_175991_1_) {
        if (this.field_153011_i != ChatState.Initialized) {
            return false;
        }
        if (!this.field_175998_i.containsKey(p_175991_1_)) {
            this.func_152995_h("Not in channel: " + p_175991_1_);
            return false;
        }
        ChatChannelListener var2 = (ChatChannelListener)this.field_175998_i.get(p_175991_1_);
        return var2.func_176034_g();
    }

    public boolean func_152993_m() {
        if (this.field_153011_i != ChatState.Initialized) {
            return false;
        }
        ErrorCode var1 = this.field_153008_f.shutdown();
        if (ErrorCode.failed((ErrorCode)var1)) {
            String var2 = ErrorCode.getString((ErrorCode)var1);
            this.func_152995_h(String.format("Error shutting down chat: %s", var2));
            return false;
        }
        this.func_152996_t();
        this.func_175985_a(ChatState.ShuttingDown);
        return true;
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

    public void func_152997_n() {
        ErrorCode var1;
        if (this.field_153011_i != ChatState.Uninitialized && ErrorCode.failed((ErrorCode)(var1 = this.field_153008_f.flushEvents()))) {
            String var2 = ErrorCode.getString((ErrorCode)var1);
            this.func_152995_h(String.format("Error flushing chat events: %s", var2));
        }
    }

    public boolean func_175986_a(String p_175986_1_, String p_175986_2_) {
        if (this.field_153011_i != ChatState.Initialized) {
            return false;
        }
        if (!this.field_175998_i.containsKey(p_175986_1_)) {
            this.func_152995_h("Not in channel: " + p_175986_1_);
            return false;
        }
        ChatChannelListener var3 = (ChatChannelListener)this.field_175998_i.get(p_175986_1_);
        return var3.func_176037_b(p_175986_2_);
    }

    protected void func_175985_a(ChatState p_175985_1_) {
        if (p_175985_1_ != this.field_153011_i) {
            this.field_153011_i = p_175985_1_;
            try {
                if (this.field_153003_a != null) {
                    this.field_153003_a.func_176017_a(p_175985_1_);
                }
            }
            catch (Exception var3) {
                this.func_152995_h(var3.toString());
            }
        }
    }

    protected void func_153001_r() {
        ErrorCode var1;
        if (this.field_175995_l != EnumEmoticonMode.None && this.field_175996_m == null && ErrorCode.failed((ErrorCode)(var1 = this.field_153008_f.downloadEmoticonData()))) {
            String var2 = ErrorCode.getString((ErrorCode)var1);
            this.func_152995_h(String.format("Error trying to download emoticon data: %s", var2));
        }
    }

    protected void func_152988_s() {
        if (this.field_175996_m == null) {
            this.field_175996_m = new ChatEmoticonData();
            ErrorCode var1 = this.field_153008_f.getEmoticonData(this.field_175996_m);
            if (ErrorCode.succeeded((ErrorCode)var1)) {
                try {
                    if (this.field_153003_a != null) {
                        this.field_153003_a.func_176021_d();
                    }
                }
                catch (Exception var3) {
                    this.func_152995_h(var3.toString());
                }
            } else {
                this.func_152995_h("Error preparing emoticon data: " + ErrorCode.getString((ErrorCode)var1));
            }
        }
    }

    protected void func_152996_t() {
        if (this.field_175996_m != null) {
            ErrorCode var1 = this.field_153008_f.clearEmoticonData();
            if (ErrorCode.succeeded((ErrorCode)var1)) {
                this.field_175996_m = null;
                try {
                    if (this.field_153003_a != null) {
                        this.field_153003_a.func_176024_e();
                    }
                }
                catch (Exception var3) {
                    this.func_152995_h(var3.toString());
                }
            } else {
                this.func_152995_h("Error clearing emoticon data: " + ErrorCode.getString((ErrorCode)var1));
            }
        }
    }

    protected void func_152995_h(String p_152995_1_) {
        LOGGER.error(TwitchStream.field_152949_a, "[Chat controller] {}", new Object[]{p_152995_1_});
    }

    public class ChatChannelListener
    implements IChatChannelListener {
        protected String field_176048_a = null;
        protected boolean field_176046_b = false;
        protected EnumChannelState field_176047_c = EnumChannelState.Created;
        protected List field_176044_d = Lists.newArrayList();
        protected LinkedList field_176045_e = new LinkedList();
        protected LinkedList field_176042_f = new LinkedList();
        protected ChatBadgeData field_176043_g = null;
        private static final String __OBFID = "CL_00002370";

        public ChatChannelListener(String p_i46061_2_) {
            this.field_176048_a = p_i46061_2_;
        }

        public EnumChannelState func_176040_a() {
            return this.field_176047_c;
        }

        public boolean func_176038_a(boolean p_176038_1_) {
            this.field_176046_b = p_176038_1_;
            ErrorCode var2 = ErrorCode.TTV_EC_SUCCESS;
            var2 = p_176038_1_ ? ChatController.this.field_153008_f.connectAnonymous(this.field_176048_a, (IChatChannelListener)this) : ChatController.this.field_153008_f.connect(this.field_176048_a, ChatController.this.field_153004_b, ChatController.this.field_153012_j.data, (IChatChannelListener)this);
            if (ErrorCode.failed((ErrorCode)var2)) {
                String var3 = ErrorCode.getString((ErrorCode)var2);
                ChatController.this.func_152995_h(String.format("Error connecting: %s", var3));
                this.func_176036_d(this.field_176048_a);
                return false;
            }
            this.func_176035_a(EnumChannelState.Connecting);
            this.func_176041_h();
            return true;
        }

        public boolean func_176034_g() {
            switch (this.field_176047_c) {
                case Connected: 
                case Connecting: {
                    ErrorCode var1 = ChatController.this.field_153008_f.disconnect(this.field_176048_a);
                    if (ErrorCode.failed((ErrorCode)var1)) {
                        String var2 = ErrorCode.getString((ErrorCode)var1);
                        ChatController.this.func_152995_h(String.format("Error disconnecting: %s", var2));
                        return false;
                    }
                    this.func_176035_a(EnumChannelState.Disconnecting);
                    return true;
                }
            }
            return false;
        }

        protected void func_176035_a(EnumChannelState p_176035_1_) {
            if (p_176035_1_ != this.field_176047_c) {
                this.field_176047_c = p_176035_1_;
            }
        }

        public void func_176032_a(String p_176032_1_) {
            if (ChatController.this.field_175995_l == EnumEmoticonMode.None) {
                this.field_176045_e.clear();
                this.field_176042_f.clear();
            } else {
                ListIterator var2;
                if (this.field_176045_e.size() > 0) {
                    var2 = this.field_176045_e.listIterator();
                    while (var2.hasNext()) {
                        ChatRawMessage var3 = (ChatRawMessage)var2.next();
                        if (!var3.userName.equals(p_176032_1_)) continue;
                        var2.remove();
                    }
                }
                if (this.field_176042_f.size() > 0) {
                    var2 = this.field_176042_f.listIterator();
                    while (var2.hasNext()) {
                        ChatTokenizedMessage var5 = (ChatTokenizedMessage)var2.next();
                        if (!var5.displayName.equals(p_176032_1_)) continue;
                        var2.remove();
                    }
                }
            }
            try {
                if (ChatController.this.field_153003_a != null) {
                    ChatController.this.field_153003_a.func_176019_a(this.field_176048_a, p_176032_1_);
                }
            }
            catch (Exception var4) {
                ChatController.this.func_152995_h(var4.toString());
            }
        }

        public boolean func_176037_b(String p_176037_1_) {
            if (this.field_176047_c != EnumChannelState.Connected) {
                return false;
            }
            ErrorCode var2 = ChatController.this.field_153008_f.sendMessage(this.field_176048_a, p_176037_1_);
            if (ErrorCode.failed((ErrorCode)var2)) {
                String var3 = ErrorCode.getString((ErrorCode)var2);
                ChatController.this.func_152995_h(String.format("Error sending chat message: %s", var3));
                return false;
            }
            return true;
        }

        protected void func_176041_h() {
            ErrorCode var1;
            if (ChatController.this.field_175995_l != EnumEmoticonMode.None && this.field_176043_g == null && ErrorCode.failed((ErrorCode)(var1 = ChatController.this.field_153008_f.downloadBadgeData(this.field_176048_a)))) {
                String var2 = ErrorCode.getString((ErrorCode)var1);
                ChatController.this.func_152995_h(String.format("Error trying to download badge data: %s", var2));
            }
        }

        protected void func_176039_i() {
            if (this.field_176043_g == null) {
                this.field_176043_g = new ChatBadgeData();
                ErrorCode var1 = ChatController.this.field_153008_f.getBadgeData(this.field_176048_a, this.field_176043_g);
                if (ErrorCode.succeeded((ErrorCode)var1)) {
                    try {
                        if (ChatController.this.field_153003_a != null) {
                            ChatController.this.field_153003_a.func_176016_c(this.field_176048_a);
                        }
                    }
                    catch (Exception var3) {
                        ChatController.this.func_152995_h(var3.toString());
                    }
                } else {
                    ChatController.this.func_152995_h("Error preparing badge data: " + ErrorCode.getString((ErrorCode)var1));
                }
            }
        }

        protected void func_176033_j() {
            if (this.field_176043_g != null) {
                ErrorCode var1 = ChatController.this.field_153008_f.clearBadgeData(this.field_176048_a);
                if (ErrorCode.succeeded((ErrorCode)var1)) {
                    this.field_176043_g = null;
                    try {
                        if (ChatController.this.field_153003_a != null) {
                            ChatController.this.field_153003_a.func_176020_d(this.field_176048_a);
                        }
                    }
                    catch (Exception var3) {
                        ChatController.this.func_152995_h(var3.toString());
                    }
                } else {
                    ChatController.this.func_152995_h("Error releasing badge data: " + ErrorCode.getString((ErrorCode)var1));
                }
            }
        }

        protected void func_176031_c(String p_176031_1_) {
            try {
                if (ChatController.this.field_153003_a != null) {
                    ChatController.this.field_153003_a.func_180606_a(p_176031_1_);
                }
            }
            catch (Exception var3) {
                ChatController.this.func_152995_h(var3.toString());
            }
        }

        protected void func_176036_d(String p_176036_1_) {
            try {
                if (ChatController.this.field_153003_a != null) {
                    ChatController.this.field_153003_a.func_180607_b(p_176036_1_);
                }
            }
            catch (Exception var3) {
                ChatController.this.func_152995_h(var3.toString());
            }
        }

        private void func_176030_k() {
            if (this.field_176047_c != EnumChannelState.Disconnected) {
                this.func_176035_a(EnumChannelState.Disconnected);
                this.func_176036_d(this.field_176048_a);
                this.func_176033_j();
            }
        }

        public void chatStatusCallback(String p_chatStatusCallback_1_, ErrorCode p_chatStatusCallback_2_) {
            if (!ErrorCode.succeeded((ErrorCode)p_chatStatusCallback_2_)) {
                ChatController.this.field_175998_i.remove(p_chatStatusCallback_1_);
                this.func_176030_k();
            }
        }

        public void chatChannelMembershipCallback(String p_chatChannelMembershipCallback_1_, ChatEvent p_chatChannelMembershipCallback_2_, ChatChannelInfo p_chatChannelMembershipCallback_3_) {
            switch (p_chatChannelMembershipCallback_2_) {
                case TTV_CHAT_JOINED_CHANNEL: {
                    this.func_176035_a(EnumChannelState.Connected);
                    this.func_176031_c(p_chatChannelMembershipCallback_1_);
                    break;
                }
                case TTV_CHAT_LEFT_CHANNEL: {
                    this.func_176030_k();
                }
            }
        }

        public void chatChannelUserChangeCallback(String p_chatChannelUserChangeCallback_1_, ChatUserInfo[] p_chatChannelUserChangeCallback_2_, ChatUserInfo[] p_chatChannelUserChangeCallback_3_, ChatUserInfo[] p_chatChannelUserChangeCallback_4_) {
            int var6;
            int var5;
            for (var5 = 0; var5 < p_chatChannelUserChangeCallback_3_.length; ++var5) {
                var6 = this.field_176044_d.indexOf((Object)p_chatChannelUserChangeCallback_3_[var5]);
                if (var6 < 0) continue;
                this.field_176044_d.remove(var6);
            }
            for (var5 = 0; var5 < p_chatChannelUserChangeCallback_4_.length; ++var5) {
                var6 = this.field_176044_d.indexOf((Object)p_chatChannelUserChangeCallback_4_[var5]);
                if (var6 >= 0) {
                    this.field_176044_d.remove(var6);
                }
                this.field_176044_d.add(p_chatChannelUserChangeCallback_4_[var5]);
            }
            for (var5 = 0; var5 < p_chatChannelUserChangeCallback_2_.length; ++var5) {
                this.field_176044_d.add(p_chatChannelUserChangeCallback_2_[var5]);
            }
            try {
                if (ChatController.this.field_153003_a != null) {
                    ChatController.this.field_153003_a.func_176018_a(this.field_176048_a, p_chatChannelUserChangeCallback_2_, p_chatChannelUserChangeCallback_3_, p_chatChannelUserChangeCallback_4_);
                }
            }
            catch (Exception var7) {
                ChatController.this.func_152995_h(var7.toString());
            }
        }

        public void chatChannelRawMessageCallback(String p_chatChannelRawMessageCallback_1_, ChatRawMessage[] p_chatChannelRawMessageCallback_2_) {
            for (int var3 = 0; var3 < p_chatChannelRawMessageCallback_2_.length; ++var3) {
                this.field_176045_e.addLast(p_chatChannelRawMessageCallback_2_[var3]);
            }
            try {
                if (ChatController.this.field_153003_a != null) {
                    ChatController.this.field_153003_a.func_180605_a(this.field_176048_a, p_chatChannelRawMessageCallback_2_);
                }
            }
            catch (Exception var4) {
                ChatController.this.func_152995_h(var4.toString());
            }
            while (this.field_176045_e.size() > ChatController.this.field_153015_m) {
                this.field_176045_e.removeFirst();
            }
        }

        public void chatChannelTokenizedMessageCallback(String p_chatChannelTokenizedMessageCallback_1_, ChatTokenizedMessage[] p_chatChannelTokenizedMessageCallback_2_) {
            for (int var3 = 0; var3 < p_chatChannelTokenizedMessageCallback_2_.length; ++var3) {
                this.field_176042_f.addLast(p_chatChannelTokenizedMessageCallback_2_[var3]);
            }
            try {
                if (ChatController.this.field_153003_a != null) {
                    ChatController.this.field_153003_a.func_176025_a(this.field_176048_a, p_chatChannelTokenizedMessageCallback_2_);
                }
            }
            catch (Exception var4) {
                ChatController.this.func_152995_h(var4.toString());
            }
            while (this.field_176042_f.size() > ChatController.this.field_153015_m) {
                this.field_176042_f.removeFirst();
            }
        }

        public void chatClearCallback(String p_chatClearCallback_1_, String p_chatClearCallback_2_) {
            this.func_176032_a(p_chatClearCallback_2_);
        }

        public void chatBadgeDataDownloadCallback(String p_chatBadgeDataDownloadCallback_1_, ErrorCode p_chatBadgeDataDownloadCallback_2_) {
            if (ErrorCode.succeeded((ErrorCode)p_chatBadgeDataDownloadCallback_2_)) {
                this.func_176039_i();
            }
        }
    }

    public static interface ChatListener {
        public void func_176023_d(ErrorCode var1);

        public void func_176022_e(ErrorCode var1);

        public void func_176021_d();

        public void func_176024_e();

        public void func_176017_a(ChatState var1);

        public void func_176025_a(String var1, ChatTokenizedMessage[] var2);

        public void func_180605_a(String var1, ChatRawMessage[] var2);

        public void func_176018_a(String var1, ChatUserInfo[] var2, ChatUserInfo[] var3, ChatUserInfo[] var4);

        public void func_180606_a(String var1);

        public void func_180607_b(String var1);

        public void func_176019_a(String var1, String var2);

        public void func_176016_c(String var1);

        public void func_176020_d(String var1);
    }

    public static enum ChatState {
        Uninitialized("Uninitialized", 0),
        Initializing("Initializing", 1),
        Initialized("Initialized", 2),
        ShuttingDown("ShuttingDown", 3);

        private static final ChatState[] $VALUES;
        private static final String __OBFID = "CL_00001817";

        static {
            $VALUES = new ChatState[]{Uninitialized, Initializing, Initialized, ShuttingDown};
        }

        private ChatState(String stateName, int id) {
        }
    }

    public static enum EnumChannelState {
        Created("Created", 0),
        Connecting("Connecting", 1),
        Connected("Connected", 2),
        Disconnecting("Disconnecting", 3),
        Disconnected("Disconnected", 4);

        private static final EnumChannelState[] $VALUES;
        private static final String __OBFID = "CL_00002371";

        static {
            $VALUES = new EnumChannelState[]{Created, Connecting, Connected, Disconnecting, Disconnected};
        }

        private EnumChannelState(String p_i46062_1_, int p_i46062_2_) {
        }
    }

    public static enum EnumEmoticonMode {
        None("None", 0),
        Url("Url", 1),
        TextureAtlas("TextureAtlas", 2);

        private static final EnumEmoticonMode[] $VALUES;
        private static final String __OBFID = "CL_00002369";

        static {
            $VALUES = new EnumEmoticonMode[]{None, Url, TextureAtlas};
        }

        private EnumEmoticonMode(String p_i46060_1_, int p_i46060_2_) {
        }
    }
}

