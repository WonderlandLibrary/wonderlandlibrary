// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.stream;

import tv.twitch.chat.ChatUserInfo;
import tv.twitch.chat.ChatChannelInfo;
import tv.twitch.chat.ChatEvent;
import java.util.ListIterator;
import tv.twitch.chat.ChatTokenizedMessage;
import tv.twitch.chat.ChatRawMessage;
import com.google.common.collect.Lists;
import tv.twitch.chat.ChatBadgeData;
import java.util.LinkedList;
import java.util.List;
import tv.twitch.chat.IChatChannelListener;
import org.apache.logging.log4j.LogManager;
import tv.twitch.chat.ChatTokenizationOption;
import java.util.HashSet;
import tv.twitch.chat.ChatAPI;
import tv.twitch.chat.StandardChatAPI;
import tv.twitch.CoreAPI;
import tv.twitch.StandardCoreAPI;
import tv.twitch.ErrorCode;
import tv.twitch.chat.IChatAPIListener;
import tv.twitch.chat.ChatEmoticonData;
import java.util.HashMap;
import tv.twitch.AuthToken;
import tv.twitch.chat.Chat;
import tv.twitch.Core;
import org.apache.logging.log4j.Logger;

public class ChatController
{
    private static final Logger LOGGER;
    protected ChatListener field_153003_a;
    protected String field_153004_b;
    protected String field_153006_d;
    protected String field_153007_e;
    protected Core field_175992_e;
    protected Chat field_153008_f;
    protected ChatState field_153011_i;
    protected AuthToken field_153012_j;
    protected HashMap field_175998_i;
    protected int field_153015_m;
    protected EnumEmoticonMode field_175997_k;
    protected EnumEmoticonMode field_175995_l;
    protected ChatEmoticonData field_175996_m;
    protected int field_175993_n;
    protected int field_175994_o;
    protected IChatAPIListener field_175999_p;
    private static final String __OBFID = "CL_00001819";
    
    public void func_152990_a(final ChatListener p_152990_1_) {
        this.field_153003_a = p_152990_1_;
    }
    
    public void func_152994_a(final AuthToken p_152994_1_) {
        this.field_153012_j = p_152994_1_;
    }
    
    public void func_152984_a(final String p_152984_1_) {
        this.field_153006_d = p_152984_1_;
    }
    
    public void func_152998_c(final String p_152998_1_) {
        this.field_153004_b = p_152998_1_;
    }
    
    public ChatState func_153000_j() {
        return this.field_153011_i;
    }
    
    public boolean func_175990_d(final String p_175990_1_) {
        if (!this.field_175998_i.containsKey(p_175990_1_)) {
            return false;
        }
        final ChatChannelListener var2 = this.field_175998_i.get(p_175990_1_);
        return var2.func_176040_a() == EnumChannelState.Connected;
    }
    
    public EnumChannelState func_175989_e(final String p_175989_1_) {
        if (!this.field_175998_i.containsKey(p_175989_1_)) {
            return EnumChannelState.Disconnected;
        }
        final ChatChannelListener var2 = this.field_175998_i.get(p_175989_1_);
        return var2.func_176040_a();
    }
    
    public ChatController() {
        this.field_153003_a = null;
        this.field_153004_b = "";
        this.field_153006_d = "";
        this.field_153007_e = "";
        this.field_175992_e = null;
        this.field_153008_f = null;
        this.field_153011_i = ChatState.Uninitialized;
        this.field_153012_j = new AuthToken();
        this.field_175998_i = new HashMap();
        this.field_153015_m = 128;
        this.field_175997_k = EnumEmoticonMode.None;
        this.field_175995_l = EnumEmoticonMode.None;
        this.field_175996_m = null;
        this.field_175993_n = 500;
        this.field_175994_o = 2000;
        this.field_175999_p = (IChatAPIListener)new IChatAPIListener() {
            private static final String __OBFID = "CL_00002373";
            
            public void chatInitializationCallback(final ErrorCode p_chatInitializationCallback_1_) {
                if (ErrorCode.succeeded(p_chatInitializationCallback_1_)) {
                    ChatController.this.field_153008_f.setMessageFlushInterval(ChatController.this.field_175993_n);
                    ChatController.this.field_153008_f.setUserChangeEventInterval(ChatController.this.field_175994_o);
                    ChatController.this.func_153001_r();
                    ChatController.this.func_175985_a(ChatState.Initialized);
                }
                else {
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
            
            public void chatShutdownCallback(final ErrorCode p_chatShutdownCallback_1_) {
                if (ErrorCode.succeeded(p_chatShutdownCallback_1_)) {
                    final ErrorCode var2 = ChatController.this.field_175992_e.shutdown();
                    if (ErrorCode.failed(var2)) {
                        final String var3 = ErrorCode.getString(var2);
                        ChatController.this.func_152995_h(String.format("Error shutting down the Twitch sdk: %s", var3));
                    }
                    ChatController.this.func_175985_a(ChatState.Uninitialized);
                }
                else {
                    ChatController.this.func_175985_a(ChatState.Initialized);
                    ChatController.this.func_152995_h(String.format("Error shutting down Twith chat: %s", p_chatShutdownCallback_1_));
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
            
            public void chatEmoticonDataDownloadCallback(final ErrorCode p_chatEmoticonDataDownloadCallback_1_) {
                if (ErrorCode.succeeded(p_chatEmoticonDataDownloadCallback_1_)) {
                    ChatController.this.func_152988_s();
                }
            }
        };
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
        ErrorCode var1 = this.field_175992_e.initialize(this.field_153006_d, (String)null);
        if (ErrorCode.failed(var1)) {
            this.func_175985_a(ChatState.Uninitialized);
            final String var2 = ErrorCode.getString(var1);
            this.func_152995_h(String.format("Error initializing Twitch sdk: %s", var2));
            return false;
        }
        this.field_175995_l = this.field_175997_k;
        final HashSet var3 = new HashSet();
        switch (SwitchEnumEmoticonMode.field_175975_c[this.field_175997_k.ordinal()]) {
            case 1: {
                var3.add(ChatTokenizationOption.TTV_CHAT_TOKENIZATION_OPTION_NONE);
                break;
            }
            case 2: {
                var3.add(ChatTokenizationOption.TTV_CHAT_TOKENIZATION_OPTION_EMOTICON_URLS);
                break;
            }
            case 3: {
                var3.add(ChatTokenizationOption.TTV_CHAT_TOKENIZATION_OPTION_EMOTICON_TEXTURES);
                break;
            }
        }
        var1 = this.field_153008_f.initialize(var3, this.field_175999_p);
        if (ErrorCode.failed(var1)) {
            this.field_175992_e.shutdown();
            this.func_175985_a(ChatState.Uninitialized);
            final String var4 = ErrorCode.getString(var1);
            this.func_152995_h(String.format("Error initializing Twitch chat: %s", var4));
            return false;
        }
        this.func_175985_a(ChatState.Initialized);
        return true;
    }
    
    public boolean func_152986_d(final String p_152986_1_) {
        return this.func_175987_a(p_152986_1_, false);
    }
    
    protected boolean func_175987_a(final String p_175987_1_, final boolean p_175987_2_) {
        if (this.field_153011_i != ChatState.Initialized) {
            return false;
        }
        if (this.field_175998_i.containsKey(p_175987_1_)) {
            this.func_152995_h("Already in channel: " + p_175987_1_);
            return false;
        }
        if (p_175987_1_ != null && !p_175987_1_.equals("")) {
            final ChatChannelListener var3 = new ChatChannelListener(p_175987_1_);
            this.field_175998_i.put(p_175987_1_, var3);
            final boolean var4 = var3.func_176038_a(p_175987_2_);
            if (!var4) {
                this.field_175998_i.remove(p_175987_1_);
            }
            return var4;
        }
        return false;
    }
    
    public boolean func_175991_l(final String p_175991_1_) {
        if (this.field_153011_i != ChatState.Initialized) {
            return false;
        }
        if (!this.field_175998_i.containsKey(p_175991_1_)) {
            this.func_152995_h("Not in channel: " + p_175991_1_);
            return false;
        }
        final ChatChannelListener var2 = this.field_175998_i.get(p_175991_1_);
        return var2.func_176034_g();
    }
    
    public boolean func_152993_m() {
        if (this.field_153011_i != ChatState.Initialized) {
            return false;
        }
        final ErrorCode var1 = this.field_153008_f.shutdown();
        if (ErrorCode.failed(var1)) {
            final String var2 = ErrorCode.getString(var1);
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
                    catch (InterruptedException ex) {}
                }
            }
        }
    }
    
    public void func_152997_n() {
        if (this.field_153011_i != ChatState.Uninitialized) {
            final ErrorCode var1 = this.field_153008_f.flushEvents();
            if (ErrorCode.failed(var1)) {
                final String var2 = ErrorCode.getString(var1);
                this.func_152995_h(String.format("Error flushing chat events: %s", var2));
            }
        }
    }
    
    public boolean func_175986_a(final String p_175986_1_, final String p_175986_2_) {
        if (this.field_153011_i != ChatState.Initialized) {
            return false;
        }
        if (!this.field_175998_i.containsKey(p_175986_1_)) {
            this.func_152995_h("Not in channel: " + p_175986_1_);
            return false;
        }
        final ChatChannelListener var3 = this.field_175998_i.get(p_175986_1_);
        return var3.func_176037_b(p_175986_2_);
    }
    
    protected void func_175985_a(final ChatState p_175985_1_) {
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
        if (this.field_175995_l != EnumEmoticonMode.None && this.field_175996_m == null) {
            final ErrorCode var1 = this.field_153008_f.downloadEmoticonData();
            if (ErrorCode.failed(var1)) {
                final String var2 = ErrorCode.getString(var1);
                this.func_152995_h(String.format("Error trying to download emoticon data: %s", var2));
            }
        }
    }
    
    protected void func_152988_s() {
        if (this.field_175996_m == null) {
            this.field_175996_m = new ChatEmoticonData();
            final ErrorCode var1 = this.field_153008_f.getEmoticonData(this.field_175996_m);
            if (ErrorCode.succeeded(var1)) {
                try {
                    if (this.field_153003_a != null) {
                        this.field_153003_a.func_176021_d();
                    }
                }
                catch (Exception var2) {
                    this.func_152995_h(var2.toString());
                }
            }
            else {
                this.func_152995_h("Error preparing emoticon data: " + ErrorCode.getString(var1));
            }
        }
    }
    
    protected void func_152996_t() {
        if (this.field_175996_m != null) {
            final ErrorCode var1 = this.field_153008_f.clearEmoticonData();
            if (ErrorCode.succeeded(var1)) {
                this.field_175996_m = null;
                try {
                    if (this.field_153003_a != null) {
                        this.field_153003_a.func_176024_e();
                    }
                }
                catch (Exception var2) {
                    this.func_152995_h(var2.toString());
                }
            }
            else {
                this.func_152995_h("Error clearing emoticon data: " + ErrorCode.getString(var1));
            }
        }
    }
    
    protected void func_152995_h(final String p_152995_1_) {
        ChatController.LOGGER.error(TwitchStream.field_152949_a, "[Chat controller] {}", new Object[] { p_152995_1_ });
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
    
    public class ChatChannelListener implements IChatChannelListener
    {
        protected String field_176048_a;
        protected boolean field_176046_b;
        protected EnumChannelState field_176047_c;
        protected List field_176044_d;
        protected LinkedList field_176045_e;
        protected LinkedList field_176042_f;
        protected ChatBadgeData field_176043_g;
        private static final String __OBFID = "CL_00002370";
        
        public ChatChannelListener(final String p_i46061_2_) {
            this.field_176048_a = null;
            this.field_176046_b = false;
            this.field_176047_c = EnumChannelState.Created;
            this.field_176044_d = Lists.newArrayList();
            this.field_176045_e = new LinkedList();
            this.field_176042_f = new LinkedList();
            this.field_176043_g = null;
            this.field_176048_a = p_i46061_2_;
        }
        
        public EnumChannelState func_176040_a() {
            return this.field_176047_c;
        }
        
        public boolean func_176038_a(final boolean p_176038_1_) {
            this.field_176046_b = p_176038_1_;
            ErrorCode var2 = ErrorCode.TTV_EC_SUCCESS;
            if (p_176038_1_) {
                var2 = ChatController.this.field_153008_f.connectAnonymous(this.field_176048_a, (IChatChannelListener)this);
            }
            else {
                var2 = ChatController.this.field_153008_f.connect(this.field_176048_a, ChatController.this.field_153004_b, ChatController.this.field_153012_j.data, (IChatChannelListener)this);
            }
            if (ErrorCode.failed(var2)) {
                final String var3 = ErrorCode.getString(var2);
                ChatController.this.func_152995_h(String.format("Error connecting: %s", var3));
                this.func_176036_d(this.field_176048_a);
                return false;
            }
            this.func_176035_a(EnumChannelState.Connecting);
            this.func_176041_h();
            return true;
        }
        
        public boolean func_176034_g() {
            switch (SwitchEnumEmoticonMode.field_175976_a[this.field_176047_c.ordinal()]) {
                case 1:
                case 2: {
                    final ErrorCode var1 = ChatController.this.field_153008_f.disconnect(this.field_176048_a);
                    if (ErrorCode.failed(var1)) {
                        final String var2 = ErrorCode.getString(var1);
                        ChatController.this.func_152995_h(String.format("Error disconnecting: %s", var2));
                        return false;
                    }
                    this.func_176035_a(EnumChannelState.Disconnecting);
                    return true;
                }
                default: {
                    return false;
                }
            }
        }
        
        protected void func_176035_a(final EnumChannelState p_176035_1_) {
            if (p_176035_1_ != this.field_176047_c) {
                this.field_176047_c = p_176035_1_;
            }
        }
        
        public void func_176032_a(final String p_176032_1_) {
            if (ChatController.this.field_175995_l == EnumEmoticonMode.None) {
                this.field_176045_e.clear();
                this.field_176042_f.clear();
            }
            else {
                if (this.field_176045_e.size() > 0) {
                    final ListIterator var2 = this.field_176045_e.listIterator();
                    while (var2.hasNext()) {
                        final ChatRawMessage var3 = var2.next();
                        if (var3.userName.equals(p_176032_1_)) {
                            var2.remove();
                        }
                    }
                }
                if (this.field_176042_f.size() > 0) {
                    final ListIterator var2 = this.field_176042_f.listIterator();
                    while (var2.hasNext()) {
                        final ChatTokenizedMessage var4 = var2.next();
                        if (var4.displayName.equals(p_176032_1_)) {
                            var2.remove();
                        }
                    }
                }
            }
            try {
                if (ChatController.this.field_153003_a != null) {
                    ChatController.this.field_153003_a.func_176019_a(this.field_176048_a, p_176032_1_);
                }
            }
            catch (Exception var5) {
                ChatController.this.func_152995_h(var5.toString());
            }
        }
        
        public boolean func_176037_b(final String p_176037_1_) {
            if (this.field_176047_c != EnumChannelState.Connected) {
                return false;
            }
            final ErrorCode var2 = ChatController.this.field_153008_f.sendMessage(this.field_176048_a, p_176037_1_);
            if (ErrorCode.failed(var2)) {
                final String var3 = ErrorCode.getString(var2);
                ChatController.this.func_152995_h(String.format("Error sending chat message: %s", var3));
                return false;
            }
            return true;
        }
        
        protected void func_176041_h() {
            if (ChatController.this.field_175995_l != EnumEmoticonMode.None && this.field_176043_g == null) {
                final ErrorCode var1 = ChatController.this.field_153008_f.downloadBadgeData(this.field_176048_a);
                if (ErrorCode.failed(var1)) {
                    final String var2 = ErrorCode.getString(var1);
                    ChatController.this.func_152995_h(String.format("Error trying to download badge data: %s", var2));
                }
            }
        }
        
        protected void func_176039_i() {
            if (this.field_176043_g == null) {
                this.field_176043_g = new ChatBadgeData();
                final ErrorCode var1 = ChatController.this.field_153008_f.getBadgeData(this.field_176048_a, this.field_176043_g);
                if (ErrorCode.succeeded(var1)) {
                    try {
                        if (ChatController.this.field_153003_a != null) {
                            ChatController.this.field_153003_a.func_176016_c(this.field_176048_a);
                        }
                    }
                    catch (Exception var2) {
                        ChatController.this.func_152995_h(var2.toString());
                    }
                }
                else {
                    ChatController.this.func_152995_h("Error preparing badge data: " + ErrorCode.getString(var1));
                }
            }
        }
        
        protected void func_176033_j() {
            if (this.field_176043_g != null) {
                final ErrorCode var1 = ChatController.this.field_153008_f.clearBadgeData(this.field_176048_a);
                if (ErrorCode.succeeded(var1)) {
                    this.field_176043_g = null;
                    try {
                        if (ChatController.this.field_153003_a != null) {
                            ChatController.this.field_153003_a.func_176020_d(this.field_176048_a);
                        }
                    }
                    catch (Exception var2) {
                        ChatController.this.func_152995_h(var2.toString());
                    }
                }
                else {
                    ChatController.this.func_152995_h("Error releasing badge data: " + ErrorCode.getString(var1));
                }
            }
        }
        
        protected void func_176031_c(final String p_176031_1_) {
            try {
                if (ChatController.this.field_153003_a != null) {
                    ChatController.this.field_153003_a.func_180606_a(p_176031_1_);
                }
            }
            catch (Exception var3) {
                ChatController.this.func_152995_h(var3.toString());
            }
        }
        
        protected void func_176036_d(final String p_176036_1_) {
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
        
        public void chatStatusCallback(final String p_chatStatusCallback_1_, final ErrorCode p_chatStatusCallback_2_) {
            if (!ErrorCode.succeeded(p_chatStatusCallback_2_)) {
                ChatController.this.field_175998_i.remove(p_chatStatusCallback_1_);
                this.func_176030_k();
            }
        }
        
        public void chatChannelMembershipCallback(final String p_chatChannelMembershipCallback_1_, final ChatEvent p_chatChannelMembershipCallback_2_, final ChatChannelInfo p_chatChannelMembershipCallback_3_) {
            switch (SwitchEnumEmoticonMode.field_175974_b[p_chatChannelMembershipCallback_2_.ordinal()]) {
                case 1: {
                    this.func_176035_a(EnumChannelState.Connected);
                    this.func_176031_c(p_chatChannelMembershipCallback_1_);
                    break;
                }
                case 2: {
                    this.func_176030_k();
                    break;
                }
            }
        }
        
        public void chatChannelUserChangeCallback(final String p_chatChannelUserChangeCallback_1_, final ChatUserInfo[] p_chatChannelUserChangeCallback_2_, final ChatUserInfo[] p_chatChannelUserChangeCallback_3_, final ChatUserInfo[] p_chatChannelUserChangeCallback_4_) {
            for (int var5 = 0; var5 < p_chatChannelUserChangeCallback_3_.length; ++var5) {
                final int var6 = this.field_176044_d.indexOf(p_chatChannelUserChangeCallback_3_[var5]);
                if (var6 >= 0) {
                    this.field_176044_d.remove(var6);
                }
            }
            for (int var5 = 0; var5 < p_chatChannelUserChangeCallback_4_.length; ++var5) {
                final int var6 = this.field_176044_d.indexOf(p_chatChannelUserChangeCallback_4_[var5]);
                if (var6 >= 0) {
                    this.field_176044_d.remove(var6);
                }
                this.field_176044_d.add(p_chatChannelUserChangeCallback_4_[var5]);
            }
            for (int var5 = 0; var5 < p_chatChannelUserChangeCallback_2_.length; ++var5) {
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
        
        public void chatChannelRawMessageCallback(final String p_chatChannelRawMessageCallback_1_, final ChatRawMessage[] p_chatChannelRawMessageCallback_2_) {
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
        
        public void chatChannelTokenizedMessageCallback(final String p_chatChannelTokenizedMessageCallback_1_, final ChatTokenizedMessage[] p_chatChannelTokenizedMessageCallback_2_) {
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
        
        public void chatClearCallback(final String p_chatClearCallback_1_, final String p_chatClearCallback_2_) {
            this.func_176032_a(p_chatClearCallback_2_);
        }
        
        public void chatBadgeDataDownloadCallback(final String p_chatBadgeDataDownloadCallback_1_, final ErrorCode p_chatBadgeDataDownloadCallback_2_) {
            if (ErrorCode.succeeded(p_chatBadgeDataDownloadCallback_2_)) {
                this.func_176039_i();
            }
        }
    }
    
    public enum ChatState
    {
        Uninitialized("Uninitialized", 0), 
        Initializing("Initializing", 1), 
        Initialized("Initialized", 2), 
        ShuttingDown("ShuttingDown", 3);
        
        private static final ChatState[] $VALUES;
        private static final String __OBFID = "CL_00001817";
        
        private ChatState(final String stateName, final int id) {
        }
        
        static {
            $VALUES = new ChatState[] { ChatState.Uninitialized, ChatState.Initializing, ChatState.Initialized, ChatState.ShuttingDown };
        }
    }
    
    public enum EnumChannelState
    {
        Created("Created", 0), 
        Connecting("Connecting", 1), 
        Connected("Connected", 2), 
        Disconnecting("Disconnecting", 3), 
        Disconnected("Disconnected", 4);
        
        private static final EnumChannelState[] $VALUES;
        private static final String __OBFID = "CL_00002371";
        
        private EnumChannelState(final String p_i46062_1_, final int p_i46062_2_) {
        }
        
        static {
            $VALUES = new EnumChannelState[] { EnumChannelState.Created, EnumChannelState.Connecting, EnumChannelState.Connected, EnumChannelState.Disconnecting, EnumChannelState.Disconnected };
        }
    }
    
    public enum EnumEmoticonMode
    {
        None("None", 0), 
        Url("Url", 1), 
        TextureAtlas("TextureAtlas", 2);
        
        private static final EnumEmoticonMode[] $VALUES;
        private static final String __OBFID = "CL_00002369";
        
        private EnumEmoticonMode(final String p_i46060_1_, final int p_i46060_2_) {
        }
        
        static {
            $VALUES = new EnumEmoticonMode[] { EnumEmoticonMode.None, EnumEmoticonMode.Url, EnumEmoticonMode.TextureAtlas };
        }
    }
    
    static final class SwitchEnumEmoticonMode
    {
        static final int[] field_175976_a;
        static final int[] field_175974_b;
        static final int[] field_175975_c;
        private static final String __OBFID = "CL_00002372";
        
        static {
            field_175975_c = new int[EnumEmoticonMode.values().length];
            try {
                SwitchEnumEmoticonMode.field_175975_c[EnumEmoticonMode.None.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError) {}
            try {
                SwitchEnumEmoticonMode.field_175975_c[EnumEmoticonMode.Url.ordinal()] = 2;
            }
            catch (NoSuchFieldError noSuchFieldError2) {}
            try {
                SwitchEnumEmoticonMode.field_175975_c[EnumEmoticonMode.TextureAtlas.ordinal()] = 3;
            }
            catch (NoSuchFieldError noSuchFieldError3) {}
            field_175974_b = new int[ChatEvent.values().length];
            try {
                SwitchEnumEmoticonMode.field_175974_b[ChatEvent.TTV_CHAT_JOINED_CHANNEL.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError4) {}
            try {
                SwitchEnumEmoticonMode.field_175974_b[ChatEvent.TTV_CHAT_LEFT_CHANNEL.ordinal()] = 2;
            }
            catch (NoSuchFieldError noSuchFieldError5) {}
            field_175976_a = new int[EnumChannelState.values().length];
            try {
                SwitchEnumEmoticonMode.field_175976_a[EnumChannelState.Connected.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError6) {}
            try {
                SwitchEnumEmoticonMode.field_175976_a[EnumChannelState.Connecting.ordinal()] = 2;
            }
            catch (NoSuchFieldError noSuchFieldError7) {}
            try {
                SwitchEnumEmoticonMode.field_175976_a[EnumChannelState.Created.ordinal()] = 3;
            }
            catch (NoSuchFieldError noSuchFieldError8) {}
            try {
                SwitchEnumEmoticonMode.field_175976_a[EnumChannelState.Disconnected.ordinal()] = 4;
            }
            catch (NoSuchFieldError noSuchFieldError9) {}
            try {
                SwitchEnumEmoticonMode.field_175976_a[EnumChannelState.Disconnecting.ordinal()] = 5;
            }
            catch (NoSuchFieldError noSuchFieldError10) {}
        }
    }
    
    public interface ChatListener
    {
        void func_176023_d(final ErrorCode p0);
        
        void func_176022_e(final ErrorCode p0);
        
        void func_176021_d();
        
        void func_176024_e();
        
        void func_176017_a(final ChatState p0);
        
        void func_176025_a(final String p0, final ChatTokenizedMessage[] p1);
        
        void func_180605_a(final String p0, final ChatRawMessage[] p1);
        
        void func_176018_a(final String p0, final ChatUserInfo[] p1, final ChatUserInfo[] p2, final ChatUserInfo[] p3);
        
        void func_180606_a(final String p0);
        
        void func_180607_b(final String p0);
        
        void func_176019_a(final String p0, final String p1);
        
        void func_176016_c(final String p0);
        
        void func_176020_d(final String p0);
    }
}
