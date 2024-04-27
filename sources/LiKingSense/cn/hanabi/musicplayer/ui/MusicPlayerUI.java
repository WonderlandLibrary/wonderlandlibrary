/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javafx.embed.swing.JFXPanel
 *  javafx.scene.media.MediaPlayer$Status
 *  net.minecraft.client.gui.GuiScreen
 *  org.lwjgl.input.Keyboard
 *  org.lwjgl.input.Mouse
 */
package cn.hanabi.musicplayer.ui;

import cn.hanabi.musicplayer.MusicManager;
import cn.hanabi.musicplayer.api.CloudMusicAPI;
import cn.hanabi.musicplayer.impl.Track;
import cn.hanabi.musicplayer.ui.CustomTextField;
import cn.hanabi.musicplayer.ui.QRLoginScreen;
import cn.hanabi.musicplayer.ui.TrackSlot;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
import javafx.embed.swing.JFXPanel;
import javafx.scene.media.MediaPlayer;
import javax.swing.SwingUtilities;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class MusicPlayerUI
extends GuiScreen {
    public float x = 10.0f;
    public float y = 10.0f;
    public float x2 = 0.0f;
    public float y2 = 0.0f;
    public boolean drag = false;
    public CopyOnWriteArrayList<TrackSlot> slots = new CopyOnWriteArrayList();
    public float field_146294_l = 150.0f;
    public float field_146295_m = 250.0f;
    public boolean extended = false;
    public float sidebarAnimation = 0.0f;
    public float scrollY = 0.0f;
    public float scrollAni = 0.0f;
    public float minY = -100.0f;
    public CustomTextField textField = new CustomTextField("");

    public void func_73866_w_() {
        SwingUtilities.invokeLater(JFXPanel::new);
        Keyboard.enableRepeatEvents((boolean)true);
        super.func_73866_w_();
    }

    /*
     * Exception decompiling
     */
    public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Invalid stack depths @ lbl1163 : ALOAD_0 - null : trying to set 12 previously set to 16
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:207)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:1559)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:434)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    protected void func_73864_a(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (RenderUtils.isHovering(mouseX, mouseY, this.x + this.field_146294_l - 15.0f, this.y + 5.0f, this.x + this.field_146294_l - 5.0f, this.y + 15.0f) && mouseButton == 0) {
            boolean bl = this.extended = !this.extended;
        }
        if (mouseButton == 0) {
            if (RenderUtils.isHovering(mouseX, mouseY, this.x + this.field_146294_l / 2.0f - 12.0f, this.y + this.field_146295_m - 36.0f, this.x + this.field_146294_l / 2.0f + 12.0f, this.y + this.field_146295_m - 12.0f) && !MusicManager.INSTANCE.playlist.isEmpty()) {
                if (MusicManager.INSTANCE.currentTrack == null) {
                    try {
                        MusicManager.INSTANCE.play(MusicManager.INSTANCE.playlist.get(0));
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (MusicManager.INSTANCE.getMediaPlayer() != null) {
                    if (MusicManager.INSTANCE.getMediaPlayer().getStatus() == MediaPlayer.Status.PLAYING) {
                        MusicManager.INSTANCE.getMediaPlayer().pause();
                    } else {
                        MusicManager.INSTANCE.getMediaPlayer().play();
                    }
                }
            }
            if (RenderUtils.isHovering(mouseX, mouseY, this.x + 39.0f, this.y + this.field_146295_m - 32.0f, this.x + 55.0f, this.y + this.field_146295_m - 16.0f)) {
                MusicManager.INSTANCE.prev();
            }
            if (RenderUtils.isHovering(mouseX, mouseY, this.x + 96.0f, this.y + this.field_146295_m - 32.0f, this.x + 112.0f, this.y + this.field_146295_m - 16.0f)) {
                MusicManager.INSTANCE.next();
            }
            if (RenderUtils.isHovering(mouseX, mouseY, this.x + 10.0f, this.y + this.field_146295_m - 29.0f, this.x + 20.0f, this.y + this.field_146295_m - 19.0f)) {
                boolean bl = MusicManager.INSTANCE.lyric = !MusicManager.INSTANCE.lyric;
            }
            if (RenderUtils.isHovering(mouseX, mouseY, this.x + this.field_146294_l - 20.0f, this.y + this.field_146295_m - 29.0f, this.x + this.field_146294_l - 10.0f, this.y + this.field_146295_m - 19.0f)) {
                boolean bl = MusicManager.INSTANCE.repeat = !MusicManager.INSTANCE.repeat;
            }
            if (RenderUtils.isHovering(mouseX, mouseY, this.x + 5.0f, this.y + 5.0f, this.x + 15.0f, this.y + 15.0f)) {
                this.field_146297_k.func_147108_a((GuiScreen)new QRLoginScreen(this));
            }
        }
        if (this.extended && Math.ceil(this.sidebarAnimation) >= (double)(this.field_146294_l + 5.0f)) {
            float newX = this.x + this.sidebarAnimation;
            float newWidth = this.x + this.field_146294_l + this.sidebarAnimation;
            if (mouseButton == 0 && RenderUtils.isHovering(mouseX, mouseY, newWidth - 26.0f, this.y + 5.0f, newWidth - 7.0f, this.y + 17.0f) && !this.textField.textString.isEmpty() && MusicManager.INSTANCE.analyzeThread == null) {
                MusicManager.INSTANCE.analyzeThread = new Thread(() -> {
                    try {
                        this.slots.clear();
                        MusicManager.INSTANCE.playlist = (ArrayList)CloudMusicAPI.INSTANCE.getPlaylistDetail(this.textField.textString)[1];
                        for (Track t2 : MusicManager.INSTANCE.playlist) {
                            this.slots.add(new TrackSlot(t2));
                        }
                    }
                    catch (Exception ex) {
                        ClientUtils.displayChatMessage("\u89e3\u6790\u6b4c\u5355\u65f6\u53d1\u751f\u9519\u8bef!");
                        ex.printStackTrace();
                    }
                    MusicManager.INSTANCE.analyzeThread = null;
                });
                MusicManager.INSTANCE.analyzeThread.start();
            }
            if (RenderUtils.isHovering(mouseX, mouseY, newX + 5.0f, this.y + 20.0f, newWidth - 5.0f, this.y + this.field_146295_m - 4.0f)) {
                float startY = this.y + 21.0f + this.scrollAni;
                for (TrackSlot s : this.slots) {
                    if (startY > this.y && startY < this.y + this.field_146295_m - 4.0f) {
                        s.click(mouseX, mouseY, mouseButton);
                    }
                    startY += 22.0f;
                }
            }
            this.textField.mouseClicked(mouseX, mouseY, mouseButton);
        }
        super.func_73864_a(mouseX, mouseY, mouseButton);
    }

    protected void func_73869_a(char typedChar, int keyCode) throws IOException {
        if (this.extended) {
            this.textField.keyPressed(keyCode);
            this.textField.charTyped(typedChar);
        }
        super.func_73869_a(typedChar, keyCode);
    }

    public void dragWindow(int mouseX, int mouseY) {
        if (RenderUtils.isHovering(mouseX, mouseY, this.x + this.field_146294_l - 15.0f, this.y + 5.0f, this.x + this.field_146294_l - 5.0f, this.y + 15.0f)) {
            return;
        }
        if (!Mouse.isButtonDown((int)0) && this.drag) {
            this.drag = false;
        }
        if (this.drag) {
            this.x = (float)mouseX - this.x2;
            this.y = (float)mouseY - this.y2;
        } else if (RenderUtils.isHovering(mouseX, mouseY, this.x, this.y, this.x + this.field_146294_l, this.y + 20.0f) && Mouse.isButtonDown((int)0)) {
            this.drag = true;
            this.x2 = (float)mouseX - this.x;
            this.y2 = (float)mouseY - this.y;
        }
    }

    public void func_73876_c() {
        super.func_73876_c();
    }

    public void func_146281_b() {
        Keyboard.enableRepeatEvents((boolean)false);
        super.func_146281_b();
    }

    public boolean func_73868_f() {
        return false;
    }
}

