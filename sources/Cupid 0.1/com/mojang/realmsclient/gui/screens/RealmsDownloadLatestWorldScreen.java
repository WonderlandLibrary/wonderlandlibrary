package com.mojang.realmsclient.gui.screens;

import com.mojang.realmsclient.client.FileDownload;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import net.minecraft.realms.Realms;
import net.minecraft.realms.RealmsButton;
import net.minecraft.realms.RealmsDefaultVertexFormat;
import net.minecraft.realms.RealmsScreen;
import net.minecraft.realms.RealmsSharedConstants;
import net.minecraft.realms.Tezzelator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class RealmsDownloadLatestWorldScreen extends RealmsScreen {
  private static final Logger LOGGER = LogManager.getLogger();
  
  private final RealmsScreen lastScreen;
  
  private final String downloadLink;
  
  private RealmsButton cancelButton;
  
  private final String worldName;
  
  private final DownloadStatus downloadStatus;
  
  private volatile String errorMessage = null;
  
  private volatile String status = null;
  
  private volatile String progress = null;
  
  private volatile boolean cancelled = false;
  
  private volatile boolean showDots = true;
  
  private volatile boolean finished = false;
  
  private volatile boolean extracting = false;
  
  private Long previousWrittenBytes = null;
  
  private Long previousTimeSnapshot = null;
  
  private long bytesPersSecond = 0L;
  
  private int animTick = 0;
  
  private static final String[] DOTS = new String[] { "", ".", ". .", ". . ." };
  
  private int dotIndex = 0;
  
  private final int WARNING_ID = 100;
  
  private boolean checked = false;
  
  private static final ReentrantLock downloadLock = new ReentrantLock();
  
  public RealmsDownloadLatestWorldScreen(RealmsScreen lastScreen, String downloadLink, String worldName) {
    this.lastScreen = lastScreen;
    this.worldName = worldName;
    this.downloadLink = downloadLink;
    this.downloadStatus = new DownloadStatus();
  }
  
  public void init() {
    Keyboard.enableRepeatEvents(true);
    buttonsClear();
    buttonsAdd(this.cancelButton = newButton(0, width() / 2 - 100, height() - 42, 200, 20, getLocalizedString("gui.cancel")));
    checkDownloadSize();
  }
  
  private void checkDownloadSize() {
    if (this.finished)
      return; 
    if (!this.checked && getContentLength(this.downloadLink) >= 1048576000L) {
      String line1 = getLocalizedString("mco.download.confirmation.line1", new Object[] { humanReadableSize(1048576000L) });
      String line2 = getLocalizedString("mco.download.confirmation.line2");
      Realms.setScreen(new RealmsLongConfirmationScreen(this, RealmsLongConfirmationScreen.Type.Warning, line1, line2, false, 100));
    } else {
      downloadSave();
    } 
  }
  
  public void confirmResult(boolean result, int id) {
    this.checked = true;
    Realms.setScreen(this);
    downloadSave();
  }
  
  private long getContentLength(String downloadLink) {
    FileDownload fileDownload = new FileDownload();
    return fileDownload.contentLength(downloadLink);
  }
  
  public void tick() {
    super.tick();
    this.animTick++;
  }
  
  public void buttonClicked(RealmsButton button) {
    if (!button.active())
      return; 
    if (button.id() == 0) {
      this.cancelled = true;
      backButtonClicked();
    } 
  }
  
  public void keyPressed(char ch, int eventKey) {
    if (eventKey == 1) {
      this.cancelled = true;
      backButtonClicked();
    } 
  }
  
  private void backButtonClicked() {
    Realms.setScreen(this.lastScreen);
  }
  
  public void render(int xm, int ym, float a) {
    renderBackground();
    if (this.extracting && !this.finished)
      this.status = getLocalizedString("mco.download.extracting"); 
    drawCenteredString(getLocalizedString("mco.download.title"), width() / 2, 20, 16777215);
    drawCenteredString(this.status, width() / 2, 50, 16777215);
    if (this.showDots)
      drawDots(); 
    if (this.downloadStatus.bytesWritten.longValue() != 0L && !this.cancelled) {
      drawProgressBar();
      drawDownloadSpeed();
    } 
    if (this.errorMessage != null)
      drawCenteredString(this.errorMessage, width() / 2, 110, 16711680); 
    super.render(xm, ym, a);
  }
  
  private void drawDots() {
    int statusWidth = fontWidth(this.status);
    if (this.animTick % 10 == 0)
      this.dotIndex++; 
    drawString(DOTS[this.dotIndex % DOTS.length], width() / 2 + statusWidth / 2 + 5, 50, 16777215);
  }
  
  private void drawProgressBar() {
    double percentage = this.downloadStatus.bytesWritten.doubleValue() / this.downloadStatus.totalBytes.doubleValue() * 100.0D;
    this.progress = String.format("%.1f", new Object[] { Double.valueOf(percentage) });
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    GL11.glDisable(3553);
    Tezzelator t = Tezzelator.instance;
    t.begin(7, RealmsDefaultVertexFormat.POSITION_COLOR);
    double base = (width() / 2 - 100);
    double diff = 0.5D;
    t.vertex(base - 0.5D, 95.5D, 0.0D).color(217, 210, 210, 255).endVertex();
    t.vertex(base + 200.0D * percentage / 100.0D + 0.5D, 95.5D, 0.0D).color(217, 210, 210, 255).endVertex();
    t.vertex(base + 200.0D * percentage / 100.0D + 0.5D, 79.5D, 0.0D).color(217, 210, 210, 255).endVertex();
    t.vertex(base - 0.5D, 79.5D, 0.0D).color(217, 210, 210, 255).endVertex();
    t.vertex(base, 95.0D, 0.0D).color(128, 128, 128, 255).endVertex();
    t.vertex(base + 200.0D * percentage / 100.0D, 95.0D, 0.0D).color(128, 128, 128, 255).endVertex();
    t.vertex(base + 200.0D * percentage / 100.0D, 80.0D, 0.0D).color(128, 128, 128, 255).endVertex();
    t.vertex(base, 80.0D, 0.0D).color(128, 128, 128, 255).endVertex();
    t.end();
    GL11.glEnable(3553);
    drawCenteredString(this.progress + " %", width() / 2, 84, 16777215);
  }
  
  private void drawDownloadSpeed() {
    if (this.animTick % RealmsSharedConstants.TICKS_PER_SECOND == 0) {
      if (this.previousWrittenBytes != null) {
        long timeElapsed = System.currentTimeMillis() - this.previousTimeSnapshot.longValue();
        if (timeElapsed == 0L)
          timeElapsed = 1L; 
        this.bytesPersSecond = 1000L * (this.downloadStatus.bytesWritten.longValue() - this.previousWrittenBytes.longValue()) / timeElapsed;
        drawDownloadSpeed0(this.bytesPersSecond);
      } 
      this.previousWrittenBytes = this.downloadStatus.bytesWritten;
      this.previousTimeSnapshot = Long.valueOf(System.currentTimeMillis());
    } else {
      drawDownloadSpeed0(this.bytesPersSecond);
    } 
  }
  
  private void drawDownloadSpeed0(long bytesPersSecond) {
    if (bytesPersSecond > 0L) {
      int progressLength = fontWidth(this.progress);
      String stringPresentation = "(" + humanReadableSpeed(bytesPersSecond) + ")";
      drawString(stringPresentation, width() / 2 + progressLength / 2 + 15, 84, 16777215);
    } 
  }
  
  public static String humanReadableSpeed(long bytes) {
    int unit = 1024;
    if (bytes < unit)
      return bytes + " B"; 
    int exp = (int)(Math.log(bytes) / Math.log(unit));
    String pre = "KMGTPE".charAt(exp - 1) + "";
    return String.format("%.1f %sB/s", new Object[] { Double.valueOf(bytes / Math.pow(unit, exp)), pre });
  }
  
  public static String humanReadableSize(long bytes) {
    int unit = 1024;
    if (bytes < unit)
      return bytes + " B"; 
    int exp = (int)(Math.log(bytes) / Math.log(unit));
    String pre = "KMGTPE".charAt(exp - 1) + "";
    return String.format("%.0f %sB", new Object[] { Double.valueOf(bytes / Math.pow(unit, exp)), pre });
  }
  
  public void mouseEvent() {
    super.mouseEvent();
  }
  
  private void downloadSave() {
    (new Thread() {
        public void run() {
          try {
            if (!RealmsDownloadLatestWorldScreen.downloadLock.tryLock(1L, TimeUnit.SECONDS))
              return; 
            RealmsDownloadLatestWorldScreen.this.status = RealmsScreen.getLocalizedString("mco.download.preparing");
            if (RealmsDownloadLatestWorldScreen.this.cancelled) {
              RealmsDownloadLatestWorldScreen.this.downloadCancelled();
              return;
            } 
            RealmsDownloadLatestWorldScreen.this.status = RealmsScreen.getLocalizedString("mco.download.downloading", new Object[] { RealmsDownloadLatestWorldScreen.access$400(this.this$0) });
            FileDownload fileDownload = new FileDownload();
            fileDownload.contentLength(RealmsDownloadLatestWorldScreen.this.downloadLink);
            fileDownload.download(RealmsDownloadLatestWorldScreen.this.downloadLink, RealmsDownloadLatestWorldScreen.this.worldName, RealmsDownloadLatestWorldScreen.this.downloadStatus, RealmsDownloadLatestWorldScreen.this.getLevelStorageSource());
            while (!fileDownload.isFinished()) {
              if (fileDownload.isError()) {
                fileDownload.cancel();
                RealmsDownloadLatestWorldScreen.this.errorMessage = RealmsScreen.getLocalizedString("mco.download.failed");
                RealmsDownloadLatestWorldScreen.this.cancelButton.msg(RealmsScreen.getLocalizedString("gui.done"));
                return;
              } 
              if (fileDownload.isExtracting())
                RealmsDownloadLatestWorldScreen.this.extracting = true; 
              if (RealmsDownloadLatestWorldScreen.this.cancelled) {
                fileDownload.cancel();
                RealmsDownloadLatestWorldScreen.this.downloadCancelled();
                return;
              } 
              try {
                Thread.sleep(500L);
              } catch (InterruptedException e) {
                RealmsDownloadLatestWorldScreen.LOGGER.error("Failed to check Realms backup download status");
              } 
            } 
            RealmsDownloadLatestWorldScreen.this.finished = true;
            RealmsDownloadLatestWorldScreen.this.status = RealmsScreen.getLocalizedString("mco.download.done");
            RealmsDownloadLatestWorldScreen.this.cancelButton.msg(RealmsScreen.getLocalizedString("gui.done"));
          } catch (InterruptedException e) {
            RealmsDownloadLatestWorldScreen.LOGGER.error("Could not acquire upload lock");
          } catch (Exception e) {
            RealmsDownloadLatestWorldScreen.this.errorMessage = RealmsScreen.getLocalizedString("mco.download.failed");
            e.printStackTrace();
          } finally {
            if (!RealmsDownloadLatestWorldScreen.downloadLock.isHeldByCurrentThread())
              return; 
            RealmsDownloadLatestWorldScreen.downloadLock.unlock();
            RealmsDownloadLatestWorldScreen.this.showDots = false;
            RealmsDownloadLatestWorldScreen.this.buttonsRemove(RealmsDownloadLatestWorldScreen.this.cancelButton);
            RealmsDownloadLatestWorldScreen.this.finished = true;
          } 
        }
      }).start();
  }
  
  private void downloadCancelled() {
    this.status = getLocalizedString("mco.download.cancelled");
  }
  
  public class DownloadStatus {
    public volatile Long bytesWritten = Long.valueOf(0L);
    
    public volatile Long totalBytes = Long.valueOf(0L);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\mojang\realmsclient\gui\screens\RealmsDownloadLatestWorldScreen.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */