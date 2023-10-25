/*
 * Decompiled with CFR 0.150.
 */
package com.mojang.realmsclient.gui.screens;

import com.mojang.realmsclient.client.FileDownload;
import com.mojang.realmsclient.dto.WorldDownload;
import com.mojang.realmsclient.gui.screens.RealmsLongConfirmationScreen;
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

public class RealmsDownloadLatestWorldScreen
extends RealmsScreen {
    private static final Logger LOGGER = LogManager.getLogger();
    private final RealmsScreen lastScreen;
    private final WorldDownload worldDownload;
    private RealmsButton cancelButton;
    private final String worldName;
    private final DownloadStatus downloadStatus;
    private volatile String errorMessage;
    private volatile String status;
    private volatile String progress;
    private volatile boolean cancelled;
    private volatile boolean showDots = true;
    private volatile boolean finished;
    private volatile boolean extracting;
    private Long previousWrittenBytes;
    private Long previousTimeSnapshot;
    private long bytesPersSecond;
    private int animTick;
    private static final String[] DOTS = new String[]{"", ".", ". .", ". . ."};
    private int dotIndex;
    private final int WARNING_ID = 100;
    private int confirmationId = -1;
    private boolean checked;
    private static final ReentrantLock downloadLock = new ReentrantLock();

    public RealmsDownloadLatestWorldScreen(RealmsScreen lastScreen, WorldDownload worldDownload, String worldName) {
        this.lastScreen = lastScreen;
        this.worldName = worldName;
        this.worldDownload = worldDownload;
        this.downloadStatus = new DownloadStatus();
    }

    public void setConfirmationId(int confirmationId) {
        this.confirmationId = confirmationId;
    }

    @Override
    public void init() {
        Keyboard.enableRepeatEvents(true);
        this.buttonsClear();
        this.cancelButton = RealmsDownloadLatestWorldScreen.newButton(0, this.width() / 2 - 100, this.height() - 42, 200, 20, RealmsDownloadLatestWorldScreen.getLocalizedString("gui.cancel"));
        this.buttonsAdd(this.cancelButton);
        this.checkDownloadSize();
    }

    private void checkDownloadSize() {
        if (this.finished) {
            return;
        }
        if (!this.checked && this.getContentLength(this.worldDownload.downloadLink) >= 0x140000000L) {
            String line1 = RealmsDownloadLatestWorldScreen.getLocalizedString("mco.download.confirmation.line1", RealmsDownloadLatestWorldScreen.humanReadableSize(0x140000000L));
            String line2 = RealmsDownloadLatestWorldScreen.getLocalizedString("mco.download.confirmation.line2");
            Realms.setScreen(new RealmsLongConfirmationScreen(this, RealmsLongConfirmationScreen.Type.Warning, line1, line2, false, 100));
        } else {
            this.downloadSave();
        }
    }

    @Override
    public void confirmResult(boolean result, int id) {
        this.checked = true;
        Realms.setScreen(this);
        this.downloadSave();
    }

    private long getContentLength(String downloadLink) {
        FileDownload fileDownload = new FileDownload();
        return fileDownload.contentLength(downloadLink);
    }

    @Override
    public void tick() {
        super.tick();
        ++this.animTick;
    }

    @Override
    public void buttonClicked(RealmsButton button) {
        if (!button.active()) {
            return;
        }
        if (button.id() == 0) {
            this.cancelled = true;
            this.backButtonClicked();
        }
    }

    @Override
    public void keyPressed(char ch, int eventKey) {
        if (eventKey == 1) {
            this.cancelled = true;
            this.backButtonClicked();
        }
    }

    private void backButtonClicked() {
        if (this.finished && this.confirmationId != -1 && this.errorMessage == null) {
            this.lastScreen.confirmResult(true, this.confirmationId);
        }
        Realms.setScreen(this.lastScreen);
    }

    @Override
    public void render(int xm, int ym, float a) {
        this.renderBackground();
        if (this.extracting && !this.finished) {
            this.status = RealmsDownloadLatestWorldScreen.getLocalizedString("mco.download.extracting");
        }
        this.drawCenteredString(RealmsDownloadLatestWorldScreen.getLocalizedString("mco.download.title"), this.width() / 2, 20, 0xFFFFFF);
        this.drawCenteredString(this.status, this.width() / 2, 50, 0xFFFFFF);
        if (this.showDots) {
            this.drawDots();
        }
        if (this.downloadStatus.bytesWritten != 0L && !this.cancelled) {
            this.drawProgressBar();
            this.drawDownloadSpeed();
        }
        if (this.errorMessage != null) {
            this.drawCenteredString(this.errorMessage, this.width() / 2, 110, 0xFF0000);
        }
        super.render(xm, ym, a);
    }

    private void drawDots() {
        int statusWidth = this.fontWidth(this.status);
        if (this.animTick % 10 == 0) {
            ++this.dotIndex;
        }
        this.drawString(DOTS[this.dotIndex % DOTS.length], this.width() / 2 + statusWidth / 2 + 5, 50, 0xFFFFFF);
    }

    private void drawProgressBar() {
        double percentage = this.downloadStatus.bytesWritten.doubleValue() / this.downloadStatus.totalBytes.doubleValue() * 100.0;
        this.progress = String.format("%.1f", percentage);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glDisable(3553);
        Tezzelator t = Tezzelator.instance;
        t.begin(7, RealmsDefaultVertexFormat.POSITION_COLOR);
        double base = this.width() / 2 - 100;
        double diff = 0.5;
        t.vertex(base - 0.5, 95.5, 0.0).color(217, 210, 210, 255).endVertex();
        t.vertex(base + 200.0 * percentage / 100.0 + 0.5, 95.5, 0.0).color(217, 210, 210, 255).endVertex();
        t.vertex(base + 200.0 * percentage / 100.0 + 0.5, 79.5, 0.0).color(217, 210, 210, 255).endVertex();
        t.vertex(base - 0.5, 79.5, 0.0).color(217, 210, 210, 255).endVertex();
        t.vertex(base, 95.0, 0.0).color(128, 128, 128, 255).endVertex();
        t.vertex(base + 200.0 * percentage / 100.0, 95.0, 0.0).color(128, 128, 128, 255).endVertex();
        t.vertex(base + 200.0 * percentage / 100.0, 80.0, 0.0).color(128, 128, 128, 255).endVertex();
        t.vertex(base, 80.0, 0.0).color(128, 128, 128, 255).endVertex();
        t.end();
        GL11.glEnable(3553);
        this.drawCenteredString(this.progress + " %", this.width() / 2, 84, 0xFFFFFF);
    }

    private void drawDownloadSpeed() {
        if (this.animTick % RealmsSharedConstants.TICKS_PER_SECOND == 0) {
            if (this.previousWrittenBytes != null) {
                long timeElapsed = System.currentTimeMillis() - this.previousTimeSnapshot;
                if (timeElapsed == 0L) {
                    timeElapsed = 1L;
                }
                this.bytesPersSecond = 1000L * (this.downloadStatus.bytesWritten - this.previousWrittenBytes) / timeElapsed;
                this.drawDownloadSpeed0(this.bytesPersSecond);
            }
            this.previousWrittenBytes = this.downloadStatus.bytesWritten;
            this.previousTimeSnapshot = System.currentTimeMillis();
        } else {
            this.drawDownloadSpeed0(this.bytesPersSecond);
        }
    }

    private void drawDownloadSpeed0(long bytesPersSecond) {
        if (bytesPersSecond > 0L) {
            int progressLength = this.fontWidth(this.progress);
            String stringPresentation = "(" + RealmsDownloadLatestWorldScreen.humanReadableSpeed(bytesPersSecond) + ")";
            this.drawString(stringPresentation, this.width() / 2 + progressLength / 2 + 15, 84, 0xFFFFFF);
        }
    }

    public static String humanReadableSpeed(long bytes) {
        int unit = 1024;
        if (bytes < 1024L) {
            return bytes + " B";
        }
        int exp = (int)(Math.log(bytes) / Math.log(1024.0));
        String pre = "KMGTPE".charAt(exp - 1) + "";
        return String.format("%.1f %sB/s", (double)bytes / Math.pow(1024.0, exp), pre);
    }

    public static String humanReadableSize(long bytes) {
        int unit = 1024;
        if (bytes < 1024L) {
            return bytes + " B";
        }
        int exp = (int)(Math.log(bytes) / Math.log(1024.0));
        String pre = "KMGTPE".charAt(exp - 1) + "";
        return String.format("%.0f %sB", (double)bytes / Math.pow(1024.0, exp), pre);
    }

    @Override
    public void mouseEvent() {
        super.mouseEvent();
    }

    private void downloadSave() {
        new Thread(){

            @Override
            public void run() {
                try {
                    if (!downloadLock.tryLock(1L, TimeUnit.SECONDS)) {
                        return;
                    }
                    RealmsDownloadLatestWorldScreen.this.status = RealmsScreen.getLocalizedString("mco.download.preparing");
                    if (RealmsDownloadLatestWorldScreen.this.cancelled) {
                        RealmsDownloadLatestWorldScreen.this.downloadCancelled();
                        return;
                    }
                    RealmsDownloadLatestWorldScreen.this.status = RealmsScreen.getLocalizedString("mco.download.downloading", RealmsDownloadLatestWorldScreen.this.worldName);
                    FileDownload fileDownload = new FileDownload();
                    fileDownload.contentLength(((RealmsDownloadLatestWorldScreen)RealmsDownloadLatestWorldScreen.this).worldDownload.downloadLink);
                    fileDownload.download(RealmsDownloadLatestWorldScreen.this.worldDownload, RealmsDownloadLatestWorldScreen.this.worldName, RealmsDownloadLatestWorldScreen.this.downloadStatus, RealmsDownloadLatestWorldScreen.this.getLevelStorageSource());
                    while (!fileDownload.isFinished()) {
                        if (fileDownload.isError()) {
                            fileDownload.cancel();
                            RealmsDownloadLatestWorldScreen.this.errorMessage = RealmsScreen.getLocalizedString("mco.download.failed");
                            RealmsDownloadLatestWorldScreen.this.cancelButton.msg(RealmsScreen.getLocalizedString("gui.done"));
                            return;
                        }
                        if (fileDownload.isExtracting()) {
                            RealmsDownloadLatestWorldScreen.this.extracting = true;
                        }
                        if (RealmsDownloadLatestWorldScreen.this.cancelled) {
                            fileDownload.cancel();
                            RealmsDownloadLatestWorldScreen.this.downloadCancelled();
                            return;
                        }
                        try {
                            Thread.sleep(500L);
                        }
                        catch (InterruptedException ignored) {
                            LOGGER.error("Failed to check Realms backup download status");
                        }
                    }
                    RealmsDownloadLatestWorldScreen.this.finished = true;
                    RealmsDownloadLatestWorldScreen.this.status = RealmsScreen.getLocalizedString("mco.download.done");
                    RealmsDownloadLatestWorldScreen.this.cancelButton.msg(RealmsScreen.getLocalizedString("gui.done"));
                }
                catch (InterruptedException ignored) {
                    LOGGER.error("Could not acquire upload lock");
                }
                catch (Exception e) {
                    RealmsDownloadLatestWorldScreen.this.errorMessage = RealmsScreen.getLocalizedString("mco.download.failed");
                    e.printStackTrace();
                }
                finally {
                    if (!downloadLock.isHeldByCurrentThread()) {
                        return;
                    }
                    downloadLock.unlock();
                    RealmsDownloadLatestWorldScreen.this.showDots = false;
                    RealmsDownloadLatestWorldScreen.this.finished = true;
                }
            }
        }.start();
    }

    private void downloadCancelled() {
        this.status = RealmsDownloadLatestWorldScreen.getLocalizedString("mco.download.cancelled");
    }

    public class DownloadStatus {
        public volatile Long bytesWritten = 0L;
        public volatile Long totalBytes = 0L;
    }
}

