/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.zxing.WriterException
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiButton
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.client.renderer.IImageBuffer
 *  net.minecraft.client.renderer.ImageBufferDownload
 *  net.minecraft.client.renderer.ThreadDownloadImageData
 *  net.minecraft.client.renderer.texture.ITextureObject
 *  net.minecraft.util.ResourceLocation
 *  org.apache.commons.io.FileUtils
 *  org.apache.http.client.CookieStore
 *  org.apache.http.cookie.Cookie
 */
package cn.hanabi.musicplayer.ui;

import cn.hanabi.musicplayer.api.CloudMusicAPI;
import com.google.zxing.WriterException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.FileUtils;
import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;

public class QRLoginScreen
extends GuiScreen {
    public Thread loginProcessThread;
    public String state = "\u6b63\u5728\u7b49\u5f85\u7528\u6237\u64cd\u4f5c...";
    public ScaledResolution res;
    public GuiScreen lastScreen;

    public QRLoginScreen(GuiScreen prevScreen) {
        this.lastScreen = prevScreen;
    }

    /*
     * Exception decompiling
     */
    public void func_73866_w_() {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl23 : INVOKEINTERFACE - null : Stack underflow
         *     at org.benf.cfr.reader.bytecode.analysis.stack.StackSim.getChange(StackSim.java:81)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:242)
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

    /*
     * Exception decompiling
     */
    public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Invalid stack depths @ lbl62 : ALOAD_0 - null : trying to set 6 previously set to 4
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

    protected void func_146284_a(GuiButton button) throws IOException {
        switch (button.field_146127_k) {
            case 0: {
                this.loginProcessThread = new Thread(() -> {
                    try {
                        ((GuiButton)this.field_146292_n.get((int)0)).field_146124_l = false;
                        File fileDir = new File(this.field_146297_k.field_71412_D, ".cache/cookies.txt");
                        if (fileDir.exists()) {
                            fileDir.delete();
                        }
                        this.state = "\u6b63\u5728\u521b\u5efaKey...";
                        String key = CloudMusicAPI.INSTANCE.QRKey();
                        System.out.println("Key=" + key);
                        this.state = "\u751f\u6210\u4e8c\u7ef4\u7801...";
                        this.createQRImage(new File(this.field_146297_k.field_71412_D, ".cache/qrcode.png"), "https://music.163.com/login?codekey=" + key, 128, "png");
                        boolean needBreak = false;
                        while (!needBreak) {
                            if (!(this.field_146297_k.field_71462_r instanceof QRLoginScreen)) {
                                needBreak = true;
                            }
                            Object[] result = CloudMusicAPI.INSTANCE.QRState(key);
                            int code = (Integer)result[0];
                            switch (code) {
                                case 800: {
                                    ((GuiButton)this.field_146292_n.get((int)0)).field_146124_l = true;
                                    this.state = "\u4e8c\u7ef4\u7801\u5df2\u8fc7\u671f, \u8bf7\u91cd\u8bd5";
                                    needBreak = true;
                                    break;
                                }
                                case 801: {
                                    this.state = "\u7b49\u5f85\u7528\u6237\u626b\u7801";
                                    break;
                                }
                                case 802: {
                                    this.state = "\u7b49\u5f85\u7528\u6237\u6388\u6743";
                                    break;
                                }
                                case 803: {
                                    StringBuilder sb = new StringBuilder();
                                    int size = 0;
                                    for (Cookie c : ((CookieStore)result[1]).getCookies()) {
                                        sb.append(c.getName()).append("=").append(c.getValue()).append(";");
                                        ++size;
                                    }
                                    CloudMusicAPI.INSTANCE.cookies = new String[size][2];
                                    for (int i = 0; i < size; ++i) {
                                        Cookie c;
                                        c = (Cookie)((CookieStore)result[1]).getCookies().get(i);
                                        CloudMusicAPI.INSTANCE.cookies[i][0] = c.getName();
                                        CloudMusicAPI.INSTANCE.cookies[i][1] = c.getValue();
                                    }
                                    FileUtils.writeStringToFile((File)fileDir, (String)sb.substring(0, sb.toString().length() - 1));
                                    this.state = "\u6210\u529f\u767b\u5165, Cookie\u5df2\u4fdd\u5b58\u81f3 " + fileDir.getAbsolutePath() + ", \u8bf7\u59a5\u5584\u4fdd\u7ba1!";
                                    needBreak = true;
                                }
                            }
                        }
                    }
                    catch (Exception ex) {
                        ((GuiButton)this.field_146292_n.get((int)0)).field_146124_l = true;
                        this.state = "\u53d1\u751f\u672a\u77e5\u9519\u8bef, \u8bf7\u91cd\u8bd5!";
                        ex.printStackTrace();
                    }
                });
                this.loginProcessThread.start();
                break;
            }
            case 1: {
                this.field_146297_k.func_147108_a(this.lastScreen);
            }
        }
        super.func_146284_a(button);
    }

    public void func_73876_c() {
        this.res = new ScaledResolution(this.field_146297_k);
        super.func_73876_c();
    }

    public boolean func_73868_f() {
        return false;
    }

    /*
     * Exception decompiling
     */
    public void createQRImage(File qrFile, String qrCodeText, int size, String fileType) throws WriterException, IOException {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl45 : INVOKEVIRTUAL - null : Stack underflow
         *     at org.benf.cfr.reader.bytecode.analysis.stack.StackSim.getChange(StackSim.java:81)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:242)
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

    public void loadImage(File file) {
        new Thread(() -> {
            ResourceLocation rl = new ResourceLocation("cloudMusicCache/qrcode");
            IImageBuffer iib = new IImageBuffer(){
                final ImageBufferDownload ibd = new ImageBufferDownload();

                public BufferedImage func_78432_a(BufferedImage image2) {
                    return image2;
                }

                public void func_152634_a() {
                }
            };
            ThreadDownloadImageData textureArt = new ThreadDownloadImageData(file, null, null, iib);
            Minecraft.func_71410_x().func_110434_K().func_110579_a(rl, (ITextureObject)textureArt);
        }).start();
    }
}

