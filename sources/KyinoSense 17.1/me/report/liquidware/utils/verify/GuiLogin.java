/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.audio.ISound
 *  net.minecraft.client.audio.PositionedSoundRecord
 *  net.minecraft.client.gui.GuiButton
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.util.EnumChatFormatting
 *  net.minecraft.util.ResourceLocation
 *  org.lwjgl.opengl.Display
 *  org.lwjgl.opengl.GL11
 *  org.lwjgl.opengl.GL20
 */
package me.report.liquidware.utils.verify;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import javax.swing.JOptionPane;
import me.report.liquidware.gui.GuiMainMenu;
import me.report.liquidware.utils.verify.HttpUtil;
import me.report.liquidware.utils.verify.HwidUtils;
import me.report.liquidware.utils.verify.HydraButton;
import me.report.liquidware.utils.verify.UIDField;
import me.report.liquidware.utils.verify.WbxMain;
import me.report.liquidware.utils.verify.WebUtils;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.special.AntiForge;
import net.ccbluex.liquidbounce.features.special.BungeeCordSpoof;
import net.ccbluex.liquidbounce.script.remapper.Remapper;
import net.ccbluex.liquidbounce.ui.client.clickgui.ClickGui;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.InventoryUtils;
import net.ccbluex.liquidbounce.utils.RotationUtils;
import net.ccbluex.liquidbounce.utils.SessionUtils;
import net.ccbluex.liquidbounce.utils.render.ColorUti;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import obfuscator.NativeMethod;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public class GuiLogin
extends GuiScreen {
    private String status = "Idle";
    private long ticks;
    private boolean launched = true;
    private final ResourceLocation loginbackground = new ResourceLocation("LiquidBounce".toLowerCase() + "/loginbackground.png");
    public static boolean login = false;
    private boolean darkTheme = false;
    private boolean falseError;
    private String token = null;
    private float fraction;
    public int alpha = 0;
    private int hwidy = 65;
    private long initTime = System.currentTimeMillis();
    private final Color blackish = new Color(20, 23, 26);
    private final Color black = new Color(40, 46, 51);
    private final Color blueish = new Color(0, 150, 135);
    private final Color blue = new Color(ColorUti.rainbow(36000000000L, 255, 0.5f).getRGB());
    public static String uid;
    private int debugFPS;
    private float hHeight = 540.0f;
    private float hWidth = 960.0f;
    private float errorBoxHeight = 0.0f;
    HydraButton button = new HydraButton(0, (int)this.hWidth - 70, (int)(this.hHeight + 5.0f), 140, 30, "Login");
    HydraButton hwid = new HydraButton(1, (int)this.hWidth - 70, (int)(this.hHeight - (float)this.hwidy), 140, 30, "Copy HWID");
    UIDField field;

    public GuiLogin() {
        this.initTime = System.currentTimeMillis();
    }

    @NativeMethod.Obfuscation(flags="+native,+tiger-black")
    public void func_73866_w_() {
        this.field_146292_n.add(this.button);
        this.field_146292_n.add(this.hwid);
        this.field = new UIDField(1, this.field_146297_k.field_71466_p, (int)this.hWidth - 70, (int)this.hHeight - 35, 140, 30, "idk");
        this.alpha = 100;
        this.darkTheme = true;
        super.func_73866_w_();
    }

    @NativeMethod.Obfuscation(flags="+native,+tiger-black")
    public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
        RenderUtils.drawImage(this.loginbackground, 0, 0, 960, 540);
        GlStateManager.func_179129_p();
        GL11.glBegin((int)7);
        GL11.glVertex2f((float)-1.0f, (float)-1.0f);
        GL11.glVertex2f((float)-1.0f, (float)1.0f);
        GL11.glVertex2f((float)1.0f, (float)1.0f);
        GL11.glVertex2f((float)1.0f, (float)-1.0f);
        GL11.glEnd();
        GL20.glUseProgram((int)0);
        if (mouseX <= 20 && mouseY <= 20 && this.alpha < 255) {
            ++this.alpha;
        }
        Color whiteish = new Color(-723464);
        Color white = Color.WHITE;
        Color shitGray = new Color(150, 150, 150);
        this.button.setColor(this.interpolateColor(this.button.hovered(mouseX, mouseY) ? this.blue.brighter() : this.blue, this.button.hovered(mouseX, mouseY) ? this.blueish.brighter() : this.blueish, this.fraction));
        this.field.setColor(this.interpolateColor(white, this.black, this.fraction));
        this.field.setTextColor(this.interpolateColor(shitGray, white, this.fraction));
        this.hwid.setColor(this.interpolateColor(this.hwid.hovered(mouseX, mouseY) ? this.blue.brighter() : this.blue, this.hwid.hovered(mouseX, mouseY) ? this.blueish.brighter() : this.blueish, this.fraction));
        ScaledResolution scaledResolution = new ScaledResolution(this.field_146297_k);
        this.button.updateCoordinates(this.hWidth - 70.0f, this.hHeight + 5.0f);
        this.hwid.updateCoordinates(this.hWidth - 70.0f, this.hHeight - (float)this.hwidy);
        this.field.updateCoordinates(this.hWidth - 70.0f, this.hHeight - 35.0f);
        int scaledWidthScaled = scaledResolution.func_78326_a();
        int scaledHeightScaled = scaledResolution.func_78328_b();
        this.hHeight += ((float)(scaledHeightScaled / 2) - this.hHeight) * 0.02f;
        this.hWidth = scaledWidthScaled / 2;
        RenderUtils.drawRect(0.0f, 0.0f, (float)scaledWidthScaled, (float)scaledHeightScaled, new Color(0, 0, 0, 150).getRGB());
        Color vis = new Color(this.interpolateColor(this.blue, this.blueish, this.fraction));
        Fonts.sbcnm.drawString("KyinoSense", this.hWidth - (float)(Fonts.sbcnm.func_78256_a("KyinoSense") / 2) + 9.0f, this.hHeight - 90.0f, this.interpolateColor(this.blue, this.blueish, this.fraction));
        Fonts.ICONFONT_50.drawString("B", this.hWidth - 95.0f, this.hHeight - 95.0f, this.interpolateColor(this.blue, this.blueish, this.fraction));
        this.button.func_146112_a(this.field_146297_k, mouseX, mouseY);
        this.hwid.func_146112_a(this.field_146297_k, mouseX, mouseY);
        if (this.status.startsWith("Idle") || this.status.startsWith("Initializing") || this.status.startsWith("Logging")) {
            Fonts.font40.drawString(this.status, this.hWidth - (float)(Fonts.font40.func_78256_a(this.status) / 2), this.hHeight + 45.0f, this.interpolateColor(new Color(150, 150, 150), white, this.fraction));
            this.errorBoxHeight = 0.0f;
        } else if (this.status.equals("Success")) {
            this.errorBoxHeight += (10.0f - this.errorBoxHeight) * 0.01f;
            RenderUtils.drawBorderedRect(this.hWidth - (float)(Fonts.font40.func_78256_a(this.status) / 2) - 10.0f, this.errorBoxHeight, this.hWidth + (float)(Fonts.font40.func_78256_a(this.status) / 2) + 10.0f, this.errorBoxHeight + 12.0f, 1.0f, new Color(170, 253, 126).getRGB(), this.interpolateColor(new Color(232, 255, 213), new Color(232, 255, 213).darker().darker(), this.fraction));
            Fonts.font40.func_175065_a(this.status, this.hWidth - (float)(Fonts.font40.func_78256_a(this.status) / 2), this.errorBoxHeight + 7.0f - (float)(Fonts.font40.getHeight() / 2), new Color(201, 255, 167).darker().getRGB(), true);
        } else {
            this.errorBoxHeight += (10.0f - this.errorBoxHeight) * 0.01f;
            RenderUtils.drawBorderedRect(this.hWidth - (float)(Fonts.font40.func_78256_a(this.status) / 2) - 10.0f, this.errorBoxHeight, this.hWidth + (float)(Fonts.font40.func_78256_a(this.status) / 2) + 10.0f, this.errorBoxHeight + 12.0f, 1.0f, -664863, this.interpolateColor(new Color(-465432), new Color(-465432).darker().darker(), this.fraction));
            Fonts.font40.func_175065_a(this.status, this.hWidth - (float)(Fonts.font40.func_78256_a(this.status) / 2), this.errorBoxHeight + 7.0f - (float)(Fonts.font40.getHeight() / 2), -1347963, true);
        }
        this.field.drawTextBox();
        Fonts.font40.drawString("Made With By: ReportTeam", this.hWidth - (float)(Fonts.font40.func_78256_a("Made With By: Report.") / 2), scaledHeightScaled - Fonts.SFUI35.getHeight() - 4, new Color(150, 150, 150).getRGB());
        if (this.falseError) {
            try {
                ScaledResolution sr = new ScaledResolution(this.field_146297_k);
                this.func_73864_a(sr.func_78326_a() / 2, sr.func_78328_b() / 2 + 20, 0);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            this.falseError = false;
        }
        super.func_73863_a(mouseX, mouseY, partialTicks);
    }

    @NativeMethod.Obfuscation(flags="+native,+tiger-black")
    protected void func_73869_a(char typedChar, int keyCode) throws IOException {
        if (this.field.isFocused() && keyCode >= 2 && keyCode <= 11 || keyCode == 14) {
            this.field.textboxKeyTyped(typedChar, keyCode);
        }
        if (keyCode == 64) {
            this.field_146297_k.func_147108_a((GuiScreen)this);
        }
    }

    @NativeMethod.Obfuscation(flags="+native,+tiger-black")
    protected void func_73864_a(int mouseX, int mouseY, int mouseButton) throws IOException {
        this.field.mouseClicked(mouseX, mouseY, mouseButton);
        super.func_73864_a(mouseX, mouseY, mouseButton);
    }

    @NativeMethod.Obfuscation(flags="+native,+tiger-black")
    protected void func_146284_a(GuiButton button) throws IOException {
        if (button.field_146127_k == 0) {
            button.field_146124_l = false;
            try {
                block18: {
                    this.status = "Logging in";
                    if (WebUtils.get("https://github.com/Reportsrc/dev/blob/main/testhwid.txt").contains(this.field.getText() + ":" + me.report.liquidware.utils.HwidUtils.getHWID()) && !this.field.getText().isEmpty()) {
                        this.status = "Success";
                        uid = this.field.getText();
                        GuiLogin.checkUser();
                        LiquidBounce.eventManager.registerListener(new RotationUtils());
                        LiquidBounce.eventManager.registerListener(new AntiForge());
                        LiquidBounce.eventManager.registerListener(new BungeeCordSpoof());
                        LiquidBounce.eventManager.registerListener(new InventoryUtils());
                        LiquidBounce.eventManager.registerListener(new SessionUtils());
                        try {
                            Remapper.INSTANCE.loadSrg();
                            LiquidBounce.scriptManager.enableScripts();
                        }
                        catch (Exception e) {
                            ClientUtils.getLogger().error("Failed to load scripts.", (Throwable)e);
                            e.printStackTrace();
                        }
                        LiquidBounce.commandManager.registerCommands();
                        LiquidBounce.clickGui = new ClickGui();
                        LiquidBounce.fileManager.loadConfig(LiquidBounce.fileManager.clickGuiConfig);
                        this.field_146297_k.func_147108_a((GuiScreen)new GuiMainMenu());
                        login = true;
                        try {
                            if (HttpUtil.webget("https://github.com/Reportsrc/dev/blob/main/testhwid.txt").contains(uid)) {
                                this.debugFPS = Minecraft.func_175610_ah();
                                Display.setTitle((String)("KyinoClient | B17.1 | BY: Report. | Rank:" + WbxMain.Rank + " | UID:" + uid));
                                WbxMain.Rank = "Dev";
                                break block18;
                            }
                            Display.setTitle((String)"KyinoClient 220520");
                            WbxMain.version = "Build 220520";
                        }
                        catch (Exception e) {}
                    } else if (WebUtils.get("https://github.com/Reportsrc/dev/blob/main/testhwid.txt").contains(HwidUtils.getHWID())) {
                        this.status = "User UID Error";
                        button.field_146124_l = true;
                    } else if (this.field.getText().isEmpty()) {
                        this.status = "User UID Empty";
                        button.field_146124_l = true;
                    } else {
                        this.status = "Your hwid error, please contact the administrator";
                        JOptionPane.showMessageDialog(null, "Failed", "Checker", 0);
                        JOptionPane.showInputDialog(null, "Ur hwid is unchecked", me.report.liquidware.utils.HwidUtils.getHWID());
                        button.field_146124_l = true;
                    }
                }
                this.field_146297_k.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a((ResourceLocation)new ResourceLocation("random.orb"), (float)1.0f));
            }
            catch (Throwable t) {
                t.printStackTrace();
                if (t.getMessage().contains("ConcurrentModificationException")) {
                    this.falseError = false;
                }
                this.status = t.getMessage();
                button.field_146124_l = true;
                this.field_146297_k.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a((ResourceLocation)new ResourceLocation("random.orb"), (float)-1.0f));
            }
        }
        if (button.field_146127_k == 1) {
            try {
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(me.report.liquidware.utils.HwidUtils.getHWID()), null);
            }
            catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        super.func_146284_a(button);
    }

    @NativeMethod.Obfuscation(flags="+native,+tiger-black")
    public static void checkUser() {
        try {
            if (HttpUtil.webget("https://gitee.com/Reportsrc/idinahuis/blob/master/staffuid.txt").contains(uid)) {
                WbxMain.Rank = "USER";
                System.out.print("Hello USER ");
                System.out.print("\n");
                return;
            }
        }
        catch (Exception exception) {
            System.out.println("Error");
            WbxMain.Rank = EnumChatFormatting.GRAY + "Rank:" + EnumChatFormatting.GREEN + "USER";
            return;
        }
        WbxMain.Rank = EnumChatFormatting.GRAY + "Rank:" + EnumChatFormatting.GREEN + "USER";
    }

    @NativeMethod.Obfuscation(flags="+native,+tiger-black")
    private int interpolateColor(Color color1, Color color2, float fraction) {
        int red = (int)((float)color1.getRed() + (float)(color2.getRed() - color1.getRed()) * fraction);
        int green = (int)((float)color1.getGreen() + (float)(color2.getGreen() - color1.getGreen()) * fraction);
        int blue = (int)((float)color1.getBlue() + (float)(color2.getBlue() - color1.getBlue()) * fraction);
        int alpha = (int)((float)color1.getAlpha() + (float)(color2.getAlpha() - color1.getAlpha()) * fraction);
        try {
            return new Color(red, green, blue, alpha).getRGB();
        }
        catch (Exception ex) {
            return -1;
        }
    }
}

