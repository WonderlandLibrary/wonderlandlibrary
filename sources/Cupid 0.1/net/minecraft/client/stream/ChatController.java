package net.minecraft.client.stream;

import com.google.common.collect.Lists;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
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
  
  protected HashMap<String, ChatChannelListener> field_175998_i = new HashMap<>();
  
  protected int field_153015_m = 128;
  
  protected EnumEmoticonMode field_175997_k = EnumEmoticonMode.None;
  
  protected EnumEmoticonMode field_175995_l = EnumEmoticonMode.None;
  
  protected ChatEmoticonData field_175996_m = null;
  
  protected int field_175993_n = 500;
  
  protected int field_175994_o = 2000;
  
  protected IChatAPIListener field_175999_p = new IChatAPIListener() {
      public void chatInitializationCallback(ErrorCode p_chatInitializationCallback_1_) {
        if (ErrorCode.succeeded(p_chatInitializationCallback_1_)) {
          ChatController.this.field_153008_f.setMessageFlushInterval(ChatController.this.field_175993_n);
          ChatController.this.field_153008_f.setUserChangeEventInterval(ChatController.this.field_175994_o);
          ChatController.this.func_153001_r();
          ChatController.this.func_175985_a(ChatController.ChatState.Initialized);
        } else {
          ChatController.this.func_175985_a(ChatController.ChatState.Uninitialized);
        } 
        try {
          if (ChatController.this.field_153003_a != null)
            ChatController.this.field_153003_a.func_176023_d(p_chatInitializationCallback_1_); 
        } catch (Exception exception) {
          ChatController.this.func_152995_h(exception.toString());
        } 
      }
      
      public void chatShutdownCallback(ErrorCode p_chatShutdownCallback_1_) {
        if (ErrorCode.succeeded(p_chatShutdownCallback_1_)) {
          ErrorCode errorcode = ChatController.this.field_175992_e.shutdown();
          if (ErrorCode.failed(errorcode)) {
            String s = ErrorCode.getString(errorcode);
            ChatController.this.func_152995_h(String.format("Error shutting down the Twitch sdk: %s", new Object[] { s }));
          } 
          ChatController.this.func_175985_a(ChatController.ChatState.Uninitialized);
        } else {
          ChatController.this.func_175985_a(ChatController.ChatState.Initialized);
          ChatController.this.func_152995_h(String.format("Error shutting down Twith chat: %s", new Object[] { p_chatShutdownCallback_1_ }));
        } 
        try {
          if (ChatController.this.field_153003_a != null)
            ChatController.this.field_153003_a.func_176022_e(p_chatShutdownCallback_1_); 
        } catch (Exception exception) {
          ChatController.this.func_152995_h(exception.toString());
        } 
      }
      
      public void chatEmoticonDataDownloadCallback(ErrorCode p_chatEmoticonDataDownloadCallback_1_) {
        if (ErrorCode.succeeded(p_chatEmoticonDataDownloadCallback_1_))
          ChatController.this.func_152988_s(); 
      }
    };
  
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
    if (!this.field_175998_i.containsKey(p_175990_1_))
      return false; 
    ChatChannelListener chatcontroller$chatchannellistener = this.field_175998_i.get(p_175990_1_);
    return (chatcontroller$chatchannellistener.func_176040_a() == EnumChannelState.Connected);
  }
  
  public EnumChannelState func_175989_e(String p_175989_1_) {
    if (!this.field_175998_i.containsKey(p_175989_1_))
      return EnumChannelState.Disconnected; 
    ChatChannelListener chatcontroller$chatchannellistener = this.field_175998_i.get(p_175989_1_);
    return chatcontroller$chatchannellistener.func_176040_a();
  }
  
  public ChatController() {
    this.field_175992_e = Core.getInstance();
    if (this.field_175992_e == null)
      this.field_175992_e = new Core((CoreAPI)new StandardCoreAPI()); 
    this.field_153008_f = new Chat((ChatAPI)new StandardChatAPI());
  }
  
  public boolean func_175984_n() {
    if (this.field_153011_i != ChatState.Uninitialized)
      return false; 
    func_175985_a(ChatState.Initializing);
    ErrorCode errorcode = this.field_175992_e.initialize(this.field_153006_d, (String)null);
    if (ErrorCode.failed(errorcode)) {
      func_175985_a(ChatState.Uninitialized);
      String s1 = ErrorCode.getString(errorcode);
      func_152995_h(String.format("Error initializing Twitch sdk: %s", new Object[] { s1 }));
      return false;
    } 
    this.field_175995_l = this.field_175997_k;
    HashSet<ChatTokenizationOption> hashset = new HashSet<>();
    switch (this.field_175997_k) {
      case TTV_CHAT_JOINED_CHANNEL:
        hashset.add(ChatTokenizationOption.TTV_CHAT_TOKENIZATION_OPTION_NONE);
        break;
      case TTV_CHAT_LEFT_CHANNEL:
        hashset.add(ChatTokenizationOption.TTV_CHAT_TOKENIZATION_OPTION_EMOTICON_URLS);
        break;
      case null:
        hashset.add(ChatTokenizationOption.TTV_CHAT_TOKENIZATION_OPTION_EMOTICON_TEXTURES);
        break;
    } 
    errorcode = this.field_153008_f.initialize(hashset, this.field_175999_p);
    if (ErrorCode.failed(errorcode)) {
      this.field_175992_e.shutdown();
      func_175985_a(ChatState.Uninitialized);
      String s = ErrorCode.getString(errorcode);
      func_152995_h(String.format("Error initializing Twitch chat: %s", new Object[] { s }));
      return false;
    } 
    func_175985_a(ChatState.Initialized);
    return true;
  }
  
  public boolean func_152986_d(String p_152986_1_) {
    return func_175987_a(p_152986_1_, false);
  }
  
  protected boolean func_175987_a(String p_175987_1_, boolean p_175987_2_) {
    if (this.field_153011_i != ChatState.Initialized)
      return false; 
    if (this.field_175998_i.containsKey(p_175987_1_)) {
      func_152995_h("Already in channel: " + p_175987_1_);
      return false;
    } 
    if (p_175987_1_ != null && !p_175987_1_.equals("")) {
      ChatChannelListener chatcontroller$chatchannellistener = new ChatChannelListener(p_175987_1_);
      this.field_175998_i.put(p_175987_1_, chatcontroller$chatchannellistener);
      boolean flag = chatcontroller$chatchannellistener.func_176038_a(p_175987_2_);
      if (!flag)
        this.field_175998_i.remove(p_175987_1_); 
      return flag;
    } 
    return false;
  }
  
  public boolean func_175991_l(String p_175991_1_) {
    if (this.field_153011_i != ChatState.Initialized)
      return false; 
    if (!this.field_175998_i.containsKey(p_175991_1_)) {
      func_152995_h("Not in channel: " + p_175991_1_);
      return false;
    } 
    ChatChannelListener chatcontroller$chatchannellistener = this.field_175998_i.get(p_175991_1_);
    return chatcontroller$chatchannellistener.func_176034_g();
  }
  
  public boolean func_152993_m() {
    if (this.field_153011_i != ChatState.Initialized)
      return false; 
    ErrorCode errorcode = this.field_153008_f.shutdown();
    if (ErrorCode.failed(errorcode)) {
      String s = ErrorCode.getString(errorcode);
      func_152995_h(String.format("Error shutting down chat: %s", new Object[] { s }));
      return false;
    } 
    func_152996_t();
    func_175985_a(ChatState.ShuttingDown);
    return true;
  }
  
  public void func_175988_p() {
    if (func_153000_j() != ChatState.Uninitialized) {
      func_152993_m();
      if (func_153000_j() == ChatState.ShuttingDown)
        while (func_153000_j() != ChatState.Uninitialized) {
          try {
            Thread.sleep(200L);
            func_152997_n();
          } catch (InterruptedException interruptedException) {}
        }  
    } 
  }
  
  public void func_152997_n() {
    if (this.field_153011_i != ChatState.Uninitialized) {
      ErrorCode errorcode = this.field_153008_f.flushEvents();
      if (ErrorCode.failed(errorcode)) {
        String s = ErrorCode.getString(errorcode);
        func_152995_h(String.format("Error flushing chat events: %s", new Object[] { s }));
      } 
    } 
  }
  
  public boolean func_175986_a(String p_175986_1_, String p_175986_2_) {
    if (this.field_153011_i != ChatState.Initialized)
      return false; 
    if (!this.field_175998_i.containsKey(p_175986_1_)) {
      func_152995_h("Not in channel: " + p_175986_1_);
      return false;
    } 
    ChatChannelListener chatcontroller$chatchannellistener = this.field_175998_i.get(p_175986_1_);
    return chatcontroller$chatchannellistener.func_176037_b(p_175986_2_);
  }
  
  protected void func_175985_a(ChatState p_175985_1_) {
    if (p_175985_1_ != this.field_153011_i) {
      this.field_153011_i = p_175985_1_;
      try {
        if (this.field_153003_a != null)
          this.field_153003_a.func_176017_a(p_175985_1_); 
      } catch (Exception exception) {
        func_152995_h(exception.toString());
      } 
    } 
  }
  
  protected void func_153001_r() {
    if (this.field_175995_l != EnumEmoticonMode.None)
      if (this.field_175996_m == null) {
        ErrorCode errorcode = this.field_153008_f.downloadEmoticonData();
        if (ErrorCode.failed(errorcode)) {
          String s = ErrorCode.getString(errorcode);
          func_152995_h(String.format("Error trying to download emoticon data: %s", new Object[] { s }));
        } 
      }  
  }
  
  protected void func_152988_s() {
    if (this.field_175996_m == null) {
      this.field_175996_m = new ChatEmoticonData();
      ErrorCode errorcode = this.field_153008_f.getEmoticonData(this.field_175996_m);
      if (ErrorCode.succeeded(errorcode)) {
        try {
          if (this.field_153003_a != null)
            this.field_153003_a.func_176021_d(); 
        } catch (Exception exception) {
          func_152995_h(exception.toString());
        } 
      } else {
        func_152995_h("Error preparing emoticon data: " + ErrorCode.getString(errorcode));
      } 
    } 
  }
  
  protected void func_152996_t() {
    if (this.field_175996_m != null) {
      ErrorCode errorcode = this.field_153008_f.clearEmoticonData();
      if (ErrorCode.succeeded(errorcode)) {
        this.field_175996_m = null;
        try {
          if (this.field_153003_a != null)
            this.field_153003_a.func_176024_e(); 
        } catch (Exception exception) {
          func_152995_h(exception.toString());
        } 
      } else {
        func_152995_h("Error clearing emoticon data: " + ErrorCode.getString(errorcode));
      } 
    } 
  }
  
  protected void func_152995_h(String p_152995_1_) {
    LOGGER.error(TwitchStream.STREAM_MARKER, "[Chat controller] {}", new Object[] { p_152995_1_ });
  }
  
  public class ChatChannelListener implements IChatChannelListener {
    protected String field_176048_a = null;
    
    protected boolean field_176046_b = false;
    
    protected ChatController.EnumChannelState field_176047_c = ChatController.EnumChannelState.Created;
    
    protected List<ChatUserInfo> field_176044_d = Lists.newArrayList();
    
    protected LinkedList<ChatRawMessage> field_176045_e = new LinkedList<>();
    
    protected LinkedList<ChatTokenizedMessage> field_176042_f = new LinkedList<>();
    
    protected ChatBadgeData field_176043_g = null;
    
    public ChatChannelListener(String p_i46061_2_) {
      this.field_176048_a = p_i46061_2_;
    }
    
    public ChatController.EnumChannelState func_176040_a() {
      return this.field_176047_c;
    }
    
    public boolean func_176038_a(boolean p_176038_1_) {
      this.field_176046_b = p_176038_1_;
      ErrorCode errorcode = ErrorCode.TTV_EC_SUCCESS;
      if (p_176038_1_) {
        errorcode = ChatController.this.field_153008_f.connectAnonymous(this.field_176048_a, this);
      } else {
        errorcode = ChatController.this.field_153008_f.connect(this.field_176048_a, ChatController.this.field_153004_b, ChatController.this.field_153012_j.data, this);
      } 
      if (ErrorCode.failed(errorcode)) {
        String s = ErrorCode.getString(errorcode);
        ChatController.this.func_152995_h(String.format("Error connecting: %s", new Object[] { s }));
        func_176036_d(this.field_176048_a);
        return false;
      } 
      func_176035_a(ChatController.EnumChannelState.Connecting);
      func_176041_h();
      return true;
    }
    
    public boolean func_176034_g() {
      ErrorCode errorcode;
      switch (this.field_176047_c) {
        case TTV_CHAT_JOINED_CHANNEL:
        case TTV_CHAT_LEFT_CHANNEL:
          errorcode = ChatController.this.field_153008_f.disconnect(this.field_176048_a);
          if (ErrorCode.failed(errorcode)) {
            String s = ErrorCode.getString(errorcode);
            ChatController.this.func_152995_h(String.format("Error disconnecting: %s", new Object[] { s }));
            return false;
          } 
          func_176035_a(ChatController.EnumChannelState.Disconnecting);
          return true;
      } 
      return false;
    }
    
    protected void func_176035_a(ChatController.EnumChannelState p_176035_1_) {
      if (p_176035_1_ != this.field_176047_c)
        this.field_176047_c = p_176035_1_; 
    }
    
    public void func_176032_a(String p_176032_1_) {
      if (ChatController.this.field_175995_l == ChatController.EnumEmoticonMode.None) {
        this.field_176045_e.clear();
        this.field_176042_f.clear();
      } else {
        if (this.field_176045_e.size() > 0) {
          ListIterator<ChatRawMessage> listiterator = this.field_176045_e.listIterator();
          while (listiterator.hasNext()) {
            ChatRawMessage chatrawmessage = listiterator.next();
            if (chatrawmessage.userName.equals(p_176032_1_))
              listiterator.remove(); 
          } 
        } 
        if (this.field_176042_f.size() > 0) {
          ListIterator<ChatTokenizedMessage> listiterator1 = this.field_176042_f.listIterator();
          while (listiterator1.hasNext()) {
            ChatTokenizedMessage chattokenizedmessage = listiterator1.next();
            if (chattokenizedmessage.displayName.equals(p_176032_1_))
              listiterator1.remove(); 
          } 
        } 
      } 
      try {
        if (ChatController.this.field_153003_a != null)
          ChatController.this.field_153003_a.func_176019_a(this.field_176048_a, p_176032_1_); 
      } catch (Exception exception) {
        ChatController.this.func_152995_h(exception.toString());
      } 
    }
    
    public boolean func_176037_b(String p_176037_1_) {
      if (this.field_176047_c != ChatController.EnumChannelState.Connected)
        return false; 
      ErrorCode errorcode = ChatController.this.field_153008_f.sendMessage(this.field_176048_a, p_176037_1_);
      if (ErrorCode.failed(errorcode)) {
        String s = ErrorCode.getString(errorcode);
        ChatController.this.func_152995_h(String.format("Error sending chat message: %s", new Object[] { s }));
        return false;
      } 
      return true;
    }
    
    protected void func_176041_h() {
      if (ChatController.this.field_175995_l != ChatController.EnumEmoticonMode.None)
        if (this.field_176043_g == null) {
          ErrorCode errorcode = ChatController.this.field_153008_f.downloadBadgeData(this.field_176048_a);
          if (ErrorCode.failed(errorcode)) {
            String s = ErrorCode.getString(errorcode);
            ChatController.this.func_152995_h(String.format("Error trying to download badge data: %s", new Object[] { s }));
          } 
        }  
    }
    
    protected void func_176039_i() {
      if (this.field_176043_g == null) {
        this.field_176043_g = new ChatBadgeData();
        ErrorCode errorcode = ChatController.this.field_153008_f.getBadgeData(this.field_176048_a, this.field_176043_g);
        if (ErrorCode.succeeded(errorcode)) {
          try {
            if (ChatController.this.field_153003_a != null)
              ChatController.this.field_153003_a.func_176016_c(this.field_176048_a); 
          } catch (Exception exception) {
            ChatController.this.func_152995_h(exception.toString());
          } 
        } else {
          ChatController.this.func_152995_h("Error preparing badge data: " + ErrorCode.getString(errorcode));
        } 
      } 
    }
    
    protected void func_176033_j() {
      if (this.field_176043_g != null) {
        ErrorCode errorcode = ChatController.this.field_153008_f.clearBadgeData(this.field_176048_a);
        if (ErrorCode.succeeded(errorcode)) {
          this.field_176043_g = null;
          try {
            if (ChatController.this.field_153003_a != null)
              ChatController.this.field_153003_a.func_176020_d(this.field_176048_a); 
          } catch (Exception exception) {
            ChatController.this.func_152995_h(exception.toString());
          } 
        } else {
          ChatController.this.func_152995_h("Error releasing badge data: " + ErrorCode.getString(errorcode));
        } 
      } 
    }
    
    protected void func_176031_c(String p_176031_1_) {
      try {
        if (ChatController.this.field_153003_a != null)
          ChatController.this.field_153003_a.func_180606_a(p_176031_1_); 
      } catch (Exception exception) {
        ChatController.this.func_152995_h(exception.toString());
      } 
    }
    
    protected void func_176036_d(String p_176036_1_) {
      try {
        if (ChatController.this.field_153003_a != null)
          ChatController.this.field_153003_a.func_180607_b(p_176036_1_); 
      } catch (Exception exception) {
        ChatController.this.func_152995_h(exception.toString());
      } 
    }
    
    private void func_176030_k() {
      if (this.field_176047_c != ChatController.EnumChannelState.Disconnected) {
        func_176035_a(ChatController.EnumChannelState.Disconnected);
        func_176036_d(this.field_176048_a);
        func_176033_j();
      } 
    }
    
    public void chatStatusCallback(String p_chatStatusCallback_1_, ErrorCode p_chatStatusCallback_2_) {
      if (!ErrorCode.succeeded(p_chatStatusCallback_2_)) {
        ChatController.this.field_175998_i.remove(p_chatStatusCallback_1_);
        func_176030_k();
      } 
    }
    
    public void chatChannelMembershipCallback(String p_chatChannelMembershipCallback_1_, ChatEvent p_chatChannelMembershipCallback_2_, ChatChannelInfo p_chatChannelMembershipCallback_3_) {
      switch (p_chatChannelMembershipCallback_2_) {
        case TTV_CHAT_JOINED_CHANNEL:
          func_176035_a(ChatController.EnumChannelState.Connected);
          func_176031_c(p_chatChannelMembershipCallback_1_);
          break;
        case TTV_CHAT_LEFT_CHANNEL:
          func_176030_k();
          break;
      } 
    }
    
    public void chatChannelUserChangeCallback(String p_chatChannelUserChangeCallback_1_, ChatUserInfo[] p_chatChannelUserChangeCallback_2_, ChatUserInfo[] p_chatChannelUserChangeCallback_3_, ChatUserInfo[] p_chatChannelUserChangeCallback_4_) {
      for (int i = 0; i < p_chatChannelUserChangeCallback_3_.length; i++) {
        int j = this.field_176044_d.indexOf(p_chatChannelUserChangeCallback_3_[i]);
        if (j >= 0)
          this.field_176044_d.remove(j); 
      } 
      for (int k = 0; k < p_chatChannelUserChangeCallback_4_.length; k++) {
        int i1 = this.field_176044_d.indexOf(p_chatChannelUserChangeCallback_4_[k]);
        if (i1 >= 0)
          this.field_176044_d.remove(i1); 
        this.field_176044_d.add(p_chatChannelUserChangeCallback_4_[k]);
      } 
      for (int l = 0; l < p_chatChannelUserChangeCallback_2_.length; l++)
        this.field_176044_d.add(p_chatChannelUserChangeCallback_2_[l]); 
      try {
        if (ChatController.this.field_153003_a != null)
          ChatController.this.field_153003_a.func_176018_a(this.field_176048_a, p_chatChannelUserChangeCallback_2_, p_chatChannelUserChangeCallback_3_, p_chatChannelUserChangeCallback_4_); 
      } catch (Exception exception) {
        ChatController.this.func_152995_h(exception.toString());
      } 
    }
    
    public void chatChannelRawMessageCallback(String p_chatChannelRawMessageCallback_1_, ChatRawMessage[] p_chatChannelRawMessageCallback_2_) {
      for (int i = 0; i < p_chatChannelRawMessageCallback_2_.length; i++)
        this.field_176045_e.addLast(p_chatChannelRawMessageCallback_2_[i]); 
      try {
        if (ChatController.this.field_153003_a != null)
          ChatController.this.field_153003_a.func_180605_a(this.field_176048_a, p_chatChannelRawMessageCallback_2_); 
      } catch (Exception exception) {
        ChatController.this.func_152995_h(exception.toString());
      } 
      while (this.field_176045_e.size() > ChatController.this.field_153015_m)
        this.field_176045_e.removeFirst(); 
    }
    
    public void chatChannelTokenizedMessageCallback(String p_chatChannelTokenizedMessageCallback_1_, ChatTokenizedMessage[] p_chatChannelTokenizedMessageCallback_2_) {
      for (int i = 0; i < p_chatChannelTokenizedMessageCallback_2_.length; i++)
        this.field_176042_f.addLast(p_chatChannelTokenizedMessageCallback_2_[i]); 
      try {
        if (ChatController.this.field_153003_a != null)
          ChatController.this.field_153003_a.func_176025_a(this.field_176048_a, p_chatChannelTokenizedMessageCallback_2_); 
      } catch (Exception exception) {
        ChatController.this.func_152995_h(exception.toString());
      } 
      while (this.field_176042_f.size() > ChatController.this.field_153015_m)
        this.field_176042_f.removeFirst(); 
    }
    
    public void chatClearCallback(String p_chatClearCallback_1_, String p_chatClearCallback_2_) {
      func_176032_a(p_chatClearCallback_2_);
    }
    
    public void chatBadgeDataDownloadCallback(String p_chatBadgeDataDownloadCallback_1_, ErrorCode p_chatBadgeDataDownloadCallback_2_) {
      if (ErrorCode.succeeded(p_chatBadgeDataDownloadCallback_2_))
        func_176039_i(); 
    }
  }
  
  public enum ChatState {
    Uninitialized, Initializing, Initialized, ShuttingDown;
  }
  
  public enum EnumChannelState {
    Created, Connecting, Connected, Disconnecting, Disconnected;
  }
  
  public enum EnumEmoticonMode {
    None, Url, TextureAtlas;
  }
  
  public static interface ChatListener {
    void func_176023_d(ErrorCode param1ErrorCode);
    
    void func_176022_e(ErrorCode param1ErrorCode);
    
    void func_176021_d();
    
    void func_176024_e();
    
    void func_176017_a(ChatController.ChatState param1ChatState);
    
    void func_176025_a(String param1String, ChatTokenizedMessage[] param1ArrayOfChatTokenizedMessage);
    
    void func_180605_a(String param1String, ChatRawMessage[] param1ArrayOfChatRawMessage);
    
    void func_176018_a(String param1String, ChatUserInfo[] param1ArrayOfChatUserInfo1, ChatUserInfo[] param1ArrayOfChatUserInfo2, ChatUserInfo[] param1ArrayOfChatUserInfo3);
    
    void func_180606_a(String param1String);
    
    void func_180607_b(String param1String);
    
    void func_176019_a(String param1String1, String param1String2);
    
    void func_176016_c(String param1String);
    
    void func_176020_d(String param1String);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\client\stream\ChatController.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */