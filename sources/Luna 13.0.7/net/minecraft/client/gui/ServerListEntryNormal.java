package net.minecraft.client.gui;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerList;
import net.minecraft.client.network.OldServerPinger;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ServerListEntryNormal
  implements GuiListExtended.IGuiListEntry
{
  private static final Logger logger = ;
  private static final ThreadPoolExecutor field_148302_b = new ScheduledThreadPoolExecutor(5, new ThreadFactoryBuilder().setNameFormat("Server Pinger #%d").setDaemon(true).build());
  private static final ResourceLocation field_178015_c = new ResourceLocation("textures/misc/unknown_server.png");
  private static final ResourceLocation field_178014_d = new ResourceLocation("textures/gui/server_selection.png");
  private final GuiMultiplayer field_148303_c;
  private final Minecraft field_148300_d;
  private final ServerData field_148301_e;
  private final ResourceLocation field_148306_i;
  private String field_148299_g;
  private DynamicTexture field_148305_h;
  private long field_148298_f;
  private static final String __OBFID = "CL_00000817";
  
  protected ServerListEntryNormal(GuiMultiplayer p_i45048_1_, ServerData p_i45048_2_)
  {
    this.field_148303_c = p_i45048_1_;
    this.field_148301_e = p_i45048_2_;
    this.field_148300_d = Minecraft.getMinecraft();
    this.field_148306_i = new ResourceLocation("servers/" + p_i45048_2_.serverIP + "/icon");
    this.field_148305_h = ((DynamicTexture)this.field_148300_d.getTextureManager().getTexture(this.field_148306_i));
  }
  
  public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected)
  {
    if (!this.field_148301_e.field_78841_f)
    {
      this.field_148301_e.field_78841_f = true;
      this.field_148301_e.pingToServer = -2L;
      this.field_148301_e.serverMOTD = "";
      this.field_148301_e.populationInfo = "";
      field_148302_b.submit(new Runnable()
      {
        private static final String __OBFID = "CL_00000818";
        
        public void run()
        {
          try
          {
            ServerListEntryNormal.this.field_148303_c.getOldServerPinger().ping(ServerListEntryNormal.this.field_148301_e);
          }
          catch (UnknownHostException var2)
          {
            ServerListEntryNormal.this.field_148301_e.pingToServer = -1L;
            ServerListEntryNormal.this.field_148301_e.serverMOTD = (EnumChatFormatting.DARK_RED + "Can't resolve hostname");
          }
          catch (Exception var3)
          {
            ServerListEntryNormal.this.field_148301_e.pingToServer = -1L;
            ServerListEntryNormal.this.field_148301_e.serverMOTD = (EnumChatFormatting.DARK_RED + "Can't connect to server.");
          }
        }
      });
    }
    boolean var9 = this.field_148301_e.version > 47;
    boolean var10 = this.field_148301_e.version < 47;
    boolean var11 = (var9) || (var10);
    Minecraft.fontRendererObj.drawString(this.field_148301_e.serverName, x + 32 + 3, y + 1, 16777215);
    List var12 = Minecraft.fontRendererObj.listFormattedStringToWidth(this.field_148301_e.serverMOTD, listWidth - 32 - 2);
    for (int var13 = 0; var13 < Math.min(var12.size(), 2); var13++) {
      Minecraft.fontRendererObj.drawString((String)var12.get(var13), x + 32 + 3, y + 12 + Minecraft.fontRendererObj.FONT_HEIGHT * var13, 8421504);
    }
    String var23 = var11 ? EnumChatFormatting.DARK_RED + this.field_148301_e.gameVersion : this.field_148301_e.populationInfo;
    int var14 = Minecraft.fontRendererObj.getStringWidth(var23);
    Minecraft.fontRendererObj.drawString(var23, x + listWidth - var14 - 15 - 2, y + 1, 8421504);
    byte var15 = 0;
    String var17 = null;
    int var16;
    String var18;
    if (var11)
    {
      int var16 = 5;
      String var18 = var9 ? "Client out of date!" : "Server out of date!";
      var17 = this.field_148301_e.playerList;
    }
    else if ((this.field_148301_e.field_78841_f) && (this.field_148301_e.pingToServer != -2L))
    {
      int var16;
      int var16;
      if (this.field_148301_e.pingToServer < 0L)
      {
        var16 = 5;
      }
      else
      {
        int var16;
        if (this.field_148301_e.pingToServer < 150L)
        {
          var16 = 0;
        }
        else
        {
          int var16;
          if (this.field_148301_e.pingToServer < 300L)
          {
            var16 = 1;
          }
          else
          {
            int var16;
            if (this.field_148301_e.pingToServer < 600L)
            {
              var16 = 2;
            }
            else
            {
              int var16;
              if (this.field_148301_e.pingToServer < 1000L) {
                var16 = 3;
              } else {
                var16 = 4;
              }
            }
          }
        }
      }
      String var18;
      if (this.field_148301_e.pingToServer < 0L)
      {
        var18 = "(no connection)";
      }
      else
      {
        String var18 = this.field_148301_e.pingToServer + "ms";
        var17 = this.field_148301_e.playerList;
      }
    }
    else
    {
      var15 = 1;
      var16 = (int)(Minecraft.getSystemTime() / 100L + slotIndex * 2 & 0x7);
      if (var16 > 4) {
        var16 = 8 - var16;
      }
      var18 = "Pinging...";
    }
    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    this.field_148300_d.getTextureManager().bindTexture(Gui.icons);
    Gui.drawModalRectWithCustomSizedTexture(x + listWidth - 15, y, var15 * 10, 176 + var16 * 8, 10, 8, 256.0F, 256.0F);
    if ((this.field_148301_e.getBase64EncodedIconData() != null) && (!this.field_148301_e.getBase64EncodedIconData().equals(this.field_148299_g)))
    {
      this.field_148299_g = this.field_148301_e.getBase64EncodedIconData();
      prepareServerIcon();
      this.field_148303_c.getServerList().saveServerList();
    }
    if (this.field_148305_h != null) {
      func_178012_a(x, y, this.field_148306_i);
    } else {
      func_178012_a(x, y, field_178015_c);
    }
    int var19 = mouseX - x;
    int var20 = mouseY - y;
    if ((var19 >= listWidth - 15) && (var19 <= listWidth - 5) && (var20 >= 0) && (var20 <= 8)) {
      this.field_148303_c.func_146793_a(var18);
    } else if ((var19 >= listWidth - var14 - 15 - 2) && (var19 <= listWidth - 15 - 2) && (var20 >= 0) && (var20 <= 8)) {
      this.field_148303_c.func_146793_a(var17);
    }
    if ((this.field_148300_d.gameSettings.touchscreen) || (isSelected))
    {
      this.field_148300_d.getTextureManager().bindTexture(field_178014_d);
      Gui.drawRect(x, y, x + 32, y + 32, -1601138544);
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      int var21 = mouseX - x;
      int var22 = mouseY - y;
      if (func_178013_b()) {
        if ((var21 < 32) && (var21 > 16)) {
          Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0F, 32.0F, 32, 32, 256.0F, 256.0F);
        } else {
          Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0F, 0.0F, 32, 32, 256.0F, 256.0F);
        }
      }
      if (this.field_148303_c.func_175392_a(this, slotIndex)) {
        if ((var21 < 16) && (var22 < 16)) {
          Gui.drawModalRectWithCustomSizedTexture(x, y, 96.0F, 32.0F, 32, 32, 256.0F, 256.0F);
        } else {
          Gui.drawModalRectWithCustomSizedTexture(x, y, 96.0F, 0.0F, 32, 32, 256.0F, 256.0F);
        }
      }
      if (this.field_148303_c.func_175394_b(this, slotIndex)) {
        if ((var21 < 16) && (var22 > 16)) {
          Gui.drawModalRectWithCustomSizedTexture(x, y, 64.0F, 32.0F, 32, 32, 256.0F, 256.0F);
        } else {
          Gui.drawModalRectWithCustomSizedTexture(x, y, 64.0F, 0.0F, 32, 32, 256.0F, 256.0F);
        }
      }
    }
  }
  
  protected void func_178012_a(int p_178012_1_, int p_178012_2_, ResourceLocation p_178012_3_)
  {
    this.field_148300_d.getTextureManager().bindTexture(p_178012_3_);
    GlStateManager.enableBlend();
    Gui.drawModalRectWithCustomSizedTexture(p_178012_1_, p_178012_2_, 0.0F, 0.0F, 32, 32, 32.0F, 32.0F);
    GlStateManager.disableBlend();
  }
  
  private boolean func_178013_b()
  {
    return true;
  }
  
  /* Error */
  private void prepareServerIcon()
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 2	net/minecraft/client/gui/ServerListEntryNormal:field_148301_e	Lnet/minecraft/client/multiplayer/ServerData;
    //   4: invokevirtual 72	net/minecraft/client/multiplayer/ServerData:getBase64EncodedIconData	()Ljava/lang/String;
    //   7: ifnonnull +25 -> 32
    //   10: aload_0
    //   11: getfield 5	net/minecraft/client/gui/ServerListEntryNormal:field_148300_d	Lnet/minecraft/client/Minecraft;
    //   14: invokevirtual 16	net/minecraft/client/Minecraft:getTextureManager	()Lnet/minecraft/client/renderer/texture/TextureManager;
    //   17: aload_0
    //   18: getfield 15	net/minecraft/client/gui/ServerListEntryNormal:field_148306_i	Lnet/minecraft/util/ResourceLocation;
    //   21: invokevirtual 94	net/minecraft/client/renderer/texture/TextureManager:deleteTexture	(Lnet/minecraft/util/ResourceLocation;)V
    //   24: aload_0
    //   25: aconst_null
    //   26: putfield 19	net/minecraft/client/gui/ServerListEntryNormal:field_148305_h	Lnet/minecraft/client/renderer/texture/DynamicTexture;
    //   29: goto +266 -> 295
    //   32: aload_0
    //   33: getfield 2	net/minecraft/client/gui/ServerListEntryNormal:field_148301_e	Lnet/minecraft/client/multiplayer/ServerData;
    //   36: invokevirtual 72	net/minecraft/client/multiplayer/ServerData:getBase64EncodedIconData	()Ljava/lang/String;
    //   39: getstatic 95	com/google/common/base/Charsets:UTF_8	Ljava/nio/charset/Charset;
    //   42: invokestatic 96	io/netty/buffer/Unpooled:copiedBuffer	(Ljava/lang/CharSequence;Ljava/nio/charset/Charset;)Lio/netty/buffer/ByteBuf;
    //   45: astore_1
    //   46: aload_1
    //   47: invokestatic 97	io/netty/handler/codec/base64/Base64:decode	(Lio/netty/buffer/ByteBuf;)Lio/netty/buffer/ByteBuf;
    //   50: astore_2
    //   51: new 98	io/netty/buffer/ByteBufInputStream
    //   54: dup
    //   55: aload_2
    //   56: invokespecial 99	io/netty/buffer/ByteBufInputStream:<init>	(Lio/netty/buffer/ByteBuf;)V
    //   59: invokestatic 100	net/minecraft/client/renderer/texture/TextureUtil:func_177053_a	(Ljava/io/InputStream;)Ljava/awt/image/BufferedImage;
    //   62: astore_3
    //   63: aload_3
    //   64: invokevirtual 101	java/awt/image/BufferedImage:getWidth	()I
    //   67: bipush 64
    //   69: if_icmpne +7 -> 76
    //   72: iconst_1
    //   73: goto +4 -> 77
    //   76: iconst_0
    //   77: ldc 102
    //   79: iconst_0
    //   80: anewarray 103	java/lang/Object
    //   83: invokestatic 104	org/apache/commons/lang3/Validate:validState	(ZLjava/lang/String;[Ljava/lang/Object;)V
    //   86: aload_3
    //   87: invokevirtual 105	java/awt/image/BufferedImage:getHeight	()I
    //   90: bipush 64
    //   92: if_icmpne +7 -> 99
    //   95: iconst_1
    //   96: goto +4 -> 100
    //   99: iconst_0
    //   100: ldc 106
    //   102: iconst_0
    //   103: anewarray 103	java/lang/Object
    //   106: invokestatic 104	org/apache/commons/lang3/Validate:validState	(ZLjava/lang/String;[Ljava/lang/Object;)V
    //   109: aload_1
    //   110: invokevirtual 107	io/netty/buffer/ByteBuf:release	()Z
    //   113: pop
    //   114: aload_2
    //   115: invokevirtual 107	io/netty/buffer/ByteBuf:release	()Z
    //   118: pop
    //   119: goto +97 -> 216
    //   122: astore 4
    //   124: getstatic 109	net/minecraft/client/gui/ServerListEntryNormal:logger	Lorg/apache/logging/log4j/Logger;
    //   127: new 7	java/lang/StringBuilder
    //   130: dup
    //   131: invokespecial 8	java/lang/StringBuilder:<init>	()V
    //   134: ldc 110
    //   136: invokevirtual 10	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   139: aload_0
    //   140: getfield 2	net/minecraft/client/gui/ServerListEntryNormal:field_148301_e	Lnet/minecraft/client/multiplayer/ServerData;
    //   143: getfield 33	net/minecraft/client/multiplayer/ServerData:serverName	Ljava/lang/String;
    //   146: invokevirtual 10	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   149: ldc 111
    //   151: invokevirtual 10	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   154: aload_0
    //   155: getfield 2	net/minecraft/client/gui/ServerListEntryNormal:field_148301_e	Lnet/minecraft/client/multiplayer/ServerData;
    //   158: getfield 11	net/minecraft/client/multiplayer/ServerData:serverIP	Ljava/lang/String;
    //   161: invokevirtual 10	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   164: ldc 112
    //   166: invokevirtual 10	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   169: invokevirtual 13	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   172: aload 4
    //   174: invokeinterface 113 3 0
    //   179: aload_0
    //   180: getfield 2	net/minecraft/client/gui/ServerListEntryNormal:field_148301_e	Lnet/minecraft/client/multiplayer/ServerData;
    //   183: aconst_null
    //   184: invokevirtual 114	net/minecraft/client/multiplayer/ServerData:setBase64EncodedIconData	(Ljava/lang/String;)V
    //   187: aload_1
    //   188: invokevirtual 107	io/netty/buffer/ByteBuf:release	()Z
    //   191: pop
    //   192: aload_2
    //   193: invokevirtual 107	io/netty/buffer/ByteBuf:release	()Z
    //   196: pop
    //   197: goto +18 -> 215
    //   200: astore 5
    //   202: aload_1
    //   203: invokevirtual 107	io/netty/buffer/ByteBuf:release	()Z
    //   206: pop
    //   207: aload_2
    //   208: invokevirtual 107	io/netty/buffer/ByteBuf:release	()Z
    //   211: pop
    //   212: aload 5
    //   214: athrow
    //   215: return
    //   216: aload_0
    //   217: getfield 19	net/minecraft/client/gui/ServerListEntryNormal:field_148305_h	Lnet/minecraft/client/renderer/texture/DynamicTexture;
    //   220: ifnonnull +41 -> 261
    //   223: aload_0
    //   224: new 18	net/minecraft/client/renderer/texture/DynamicTexture
    //   227: dup
    //   228: aload_3
    //   229: invokevirtual 101	java/awt/image/BufferedImage:getWidth	()I
    //   232: aload_3
    //   233: invokevirtual 105	java/awt/image/BufferedImage:getHeight	()I
    //   236: invokespecial 115	net/minecraft/client/renderer/texture/DynamicTexture:<init>	(II)V
    //   239: putfield 19	net/minecraft/client/gui/ServerListEntryNormal:field_148305_h	Lnet/minecraft/client/renderer/texture/DynamicTexture;
    //   242: aload_0
    //   243: getfield 5	net/minecraft/client/gui/ServerListEntryNormal:field_148300_d	Lnet/minecraft/client/Minecraft;
    //   246: invokevirtual 16	net/minecraft/client/Minecraft:getTextureManager	()Lnet/minecraft/client/renderer/texture/TextureManager;
    //   249: aload_0
    //   250: getfield 15	net/minecraft/client/gui/ServerListEntryNormal:field_148306_i	Lnet/minecraft/util/ResourceLocation;
    //   253: aload_0
    //   254: getfield 19	net/minecraft/client/gui/ServerListEntryNormal:field_148305_h	Lnet/minecraft/client/renderer/texture/DynamicTexture;
    //   257: invokevirtual 116	net/minecraft/client/renderer/texture/TextureManager:loadTexture	(Lnet/minecraft/util/ResourceLocation;Lnet/minecraft/client/renderer/texture/ITextureObject;)Z
    //   260: pop
    //   261: aload_3
    //   262: iconst_0
    //   263: iconst_0
    //   264: aload_3
    //   265: invokevirtual 101	java/awt/image/BufferedImage:getWidth	()I
    //   268: aload_3
    //   269: invokevirtual 105	java/awt/image/BufferedImage:getHeight	()I
    //   272: aload_0
    //   273: getfield 19	net/minecraft/client/gui/ServerListEntryNormal:field_148305_h	Lnet/minecraft/client/renderer/texture/DynamicTexture;
    //   276: invokevirtual 117	net/minecraft/client/renderer/texture/DynamicTexture:getTextureData	()[I
    //   279: iconst_0
    //   280: aload_3
    //   281: invokevirtual 101	java/awt/image/BufferedImage:getWidth	()I
    //   284: invokevirtual 118	java/awt/image/BufferedImage:getRGB	(IIII[III)[I
    //   287: pop
    //   288: aload_0
    //   289: getfield 19	net/minecraft/client/gui/ServerListEntryNormal:field_148305_h	Lnet/minecraft/client/renderer/texture/DynamicTexture;
    //   292: invokevirtual 119	net/minecraft/client/renderer/texture/DynamicTexture:updateDynamicTexture	()V
    //   295: return
    // Line number table:
    //   Java source line #248	-> byte code offset #0
    //   Java source line #250	-> byte code offset #10
    //   Java source line #251	-> byte code offset #24
    //   Java source line #255	-> byte code offset #32
    //   Java source line #256	-> byte code offset #46
    //   Java source line #262	-> byte code offset #51
    //   Java source line #263	-> byte code offset #63
    //   Java source line #264	-> byte code offset #86
    //   Java source line #274	-> byte code offset #109
    //   Java source line #275	-> byte code offset #114
    //   Java source line #267	-> byte code offset #122
    //   Java source line #269	-> byte code offset #124
    //   Java source line #270	-> byte code offset #179
    //   Java source line #274	-> byte code offset #187
    //   Java source line #275	-> byte code offset #192
    //   Java source line #276	-> byte code offset #197
    //   Java source line #274	-> byte code offset #200
    //   Java source line #275	-> byte code offset #207
    //   Java source line #276	-> byte code offset #212
    //   Java source line #278	-> byte code offset #215
    //   Java source line #281	-> byte code offset #216
    //   Java source line #283	-> byte code offset #223
    //   Java source line #284	-> byte code offset #242
    //   Java source line #287	-> byte code offset #261
    //   Java source line #288	-> byte code offset #288
    //   Java source line #290	-> byte code offset #295
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	296	0	this	ServerListEntryNormal
    //   45	158	1	var2	io.netty.buffer.ByteBuf
    //   50	158	2	var3	io.netty.buffer.ByteBuf
    //   62	25	3	var1	java.awt.image.BufferedImage
    //   216	65	3	var1	java.awt.image.BufferedImage
    //   122	51	4	var8	Exception
    //   200	13	5	localObject	Object
    // Exception table:
    //   from	to	target	type
    //   51	109	122	java/lang/Exception
    //   51	109	200	finally
    //   122	187	200	finally
    //   200	202	200	finally
  }
  
  public boolean mousePressed(int p_148278_1_, int p_148278_2_, int p_148278_3_, int p_148278_4_, int p_148278_5_, int p_148278_6_)
  {
    if (p_148278_5_ <= 32)
    {
      if ((p_148278_5_ < 32) && (p_148278_5_ > 16) && (func_178013_b()))
      {
        this.field_148303_c.selectServer(p_148278_1_);
        this.field_148303_c.connectToSelected();
        return true;
      }
      if ((p_148278_5_ < 16) && (p_148278_6_ < 16) && (this.field_148303_c.func_175392_a(this, p_148278_1_)))
      {
        this.field_148303_c.func_175391_a(this, p_148278_1_, GuiScreen.isShiftKeyDown());
        return true;
      }
      if ((p_148278_5_ < 16) && (p_148278_6_ > 16) && (this.field_148303_c.func_175394_b(this, p_148278_1_)))
      {
        this.field_148303_c.func_175393_b(this, p_148278_1_, GuiScreen.isShiftKeyDown());
        return true;
      }
    }
    this.field_148303_c.selectServer(p_148278_1_);
    if (Minecraft.getSystemTime() - this.field_148298_f < 250L) {
      this.field_148303_c.connectToSelected();
    }
    this.field_148298_f = Minecraft.getSystemTime();
    return false;
  }
  
  public void setSelected(int p_178011_1_, int p_178011_2_, int p_178011_3_) {}
  
  public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY) {}
  
  public ServerData getServerData()
  {
    return this.field_148301_e;
  }
}
