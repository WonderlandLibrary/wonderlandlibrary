package cn.hanabi.gui.cloudmusic.ui;

import cn.hanabi.Hanabi;
import cn.hanabi.gui.cloudmusic.api.CloudMusicAPI;
import cn.hanabi.utils.RenderUtil;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Iterator;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.FileUtils;
import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;

public class QRLoginScreen extends GuiScreen {
   public Thread loginProcessThread;
   public String state = "正在等待用户操作...";
   public ScaledResolution res;
   public GuiScreen lastScreen;

   public QRLoginScreen(GuiScreen prevScreen) {
      this.lastScreen = prevScreen;
   }

   public void initGui() {
      this.res = new ScaledResolution(this.mc);
      this.buttonList.add(new GuiButton(0, this.res.getScaledWidth() / 2 - 60, 210, 120, 20, "开始"));
      this.buttonList.add(new GuiButton(1, this.res.getScaledWidth() / 2 - 60, 240, 120, 20, "退出"));
      super.initGui();
   }

   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      this.drawDefaultBackground();
      String text = "使用二维码登录至网易云音乐";
      Hanabi.INSTANCE.fontManager.wqy16.drawString(text, (float)(this.res.getScaledWidth() / 2 - Hanabi.INSTANCE.fontManager.wqy16.getStringWidth(text) / 2), 30.0F, -1);
      Hanabi.INSTANCE.fontManager.wqy16.drawString(this.state, (float)(this.res.getScaledWidth() / 2 - Hanabi.INSTANCE.fontManager.wqy16.getStringWidth(this.state.replaceAll("§.", "")) / 2), 50.0F, -1);
      if (this.loginProcessThread != null) {
         RenderUtil.drawImage(new ResourceLocation("cloudMusicCache/qrcode"), this.res.getScaledWidth() / 2 - 64, 70, 128, 128, 1.0F);
      }

      super.drawScreen(mouseX, mouseY, partialTicks);
   }

   protected void actionPerformed(GuiButton button) throws IOException {
      switch (button.id) {
         case 0:
            this.loginProcessThread = new Thread(() -> {
               try {
                  ((GuiButton)this.buttonList.get(0)).enabled = false;
                  File fileDir = new File(this.mc.mcDataDir, ".cache/cookies.txt");
                  if (fileDir.exists()) {
                     fileDir.delete();
                  }

                  this.state = "正在创建Key...";
                  String key = CloudMusicAPI.INSTANCE.QRKey();
                  Hanabi.INSTANCE.println("Key=" + key);
                  this.state = "生成二维码...";
                  this.createQRImage(new File(this.mc.mcDataDir, ".cache/qrcode.png"), "https://music.163.com/login?codekey=" + key, 128, "png");
                  boolean needBreak = false;

                  while(!needBreak) {
                     if (!(this.mc.currentScreen instanceof QRLoginScreen)) {
                        needBreak = true;
                     }

                     Object[] result = CloudMusicAPI.INSTANCE.QRState(key);
                     int code = (Integer)result[0];
                     switch (code) {
                        case 800:
                           ((GuiButton)this.buttonList.get(0)).enabled = true;
                           this.state = "二维码已过期, 请重试";
                           needBreak = true;
                           break;
                        case 801:
                           this.state = "等待用户扫码";
                           break;
                        case 802:
                           this.state = "等待用户授权";
                           break;
                        case 803:
                           StringBuilder sb = new StringBuilder();
                           int size = 0;

                           Cookie c;
                           for(Iterator var8 = ((CookieStore)result[1]).getCookies().iterator(); var8.hasNext(); ++size) {
                              c = (Cookie)var8.next();
                              sb.append(c.getName()).append("=").append(c.getValue()).append(";");
                           }

                           CloudMusicAPI.INSTANCE.cookies = new String[size][2];

                           for(int i = 0; i < size; ++i) {
                              c = (Cookie)((CookieStore)result[1]).getCookies().get(i);
                              CloudMusicAPI.INSTANCE.cookies[i][0] = c.getName();
                              CloudMusicAPI.INSTANCE.cookies[i][1] = c.getValue();
                           }

                           FileUtils.writeStringToFile(fileDir, sb.substring(0, sb.toString().length() - 1));
                           this.state = "成功登入, Cookie已保存至 " + fileDir.getAbsolutePath() + ", 请妥善保管!";
                           needBreak = true;
                     }
                  }
               } catch (Exception var10) {
                  ((GuiButton)this.buttonList.get(0)).enabled = true;
                  this.state = "发生未知错误, 请重试!";
                  var10.printStackTrace();
               }

            });
            this.loginProcessThread.start();
            break;
         case 1:
            this.mc.displayGuiScreen(this.lastScreen);
      }

      super.actionPerformed(button);
   }

   public void updateScreen() {
      this.res = new ScaledResolution(this.mc);
      super.updateScreen();
   }

   public boolean doesGuiPauseGame() {
      return false;
   }

   public void createQRImage(File qrFile, String qrCodeText, int size, String fileType) throws WriterException, IOException {
      Hashtable hintMap = new Hashtable();
      hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
      QRCodeWriter qrCodeWriter = new QRCodeWriter();
      BitMatrix byteMatrix = qrCodeWriter.encode(qrCodeText, BarcodeFormat.QR_CODE, size, size, hintMap);
      int matrixWidth = byteMatrix.getWidth();
      BufferedImage image = new BufferedImage(matrixWidth, matrixWidth, 1);
      image.createGraphics();
      Graphics2D graphics = (Graphics2D)image.getGraphics();
      graphics.setColor(Color.WHITE);
      graphics.fillRect(0, 0, matrixWidth, matrixWidth);
      graphics.setColor(Color.BLACK);

      for(int i = 0; i < matrixWidth; ++i) {
         for(int j = 0; j < matrixWidth; ++j) {
            if (byteMatrix.get(i, j)) {
               graphics.fillRect(i, j, 1, 1);
            }
         }
      }

      ImageIO.write(image, fileType, qrFile);
      this.loadImage(qrFile);
   }

   public void loadImage(File file) {
      (new Thread(() -> {
         ResourceLocation rl = new ResourceLocation("cloudMusicCache/qrcode");
         IImageBuffer iib = new IImageBuffer() {
            final ImageBufferDownload ibd = new ImageBufferDownload();

            public BufferedImage parseUserSkin(BufferedImage image) {
               return image;
            }

            public void skinAvailable() {
            }
         };
         ThreadDownloadImageData textureArt = new ThreadDownloadImageData(file, (String)null, (ResourceLocation)null, iib);
         Minecraft.getMinecraft().getTextureManager().loadTexture(rl, textureArt);
      })).start();
   }
}
