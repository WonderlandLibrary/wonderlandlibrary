package today.flux.gui.loginui;

import com.soterdev.SoterObfuscator;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import org.apache.commons.codec.digest.DigestUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import oshi.SystemInfo;
import oshi.hardware.Processor;
import today.flux.Flux;
import today.flux.gui.clickgui.classic.GuiRenderUtils;
import today.flux.gui.clickgui.classic.RenderUtil;
import today.flux.gui.fontRenderer.FontManager;
import today.flux.gui.fontRenderer.FontUtils;
import today.flux.irc.IRCClient;
import today.flux.module.ModuleManager;
import today.flux.utility.*;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.URL;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.Map;
import java.util.Objects;

public class GuiLogin extends GuiScreen {
    public static String[] account = null;

    public CustomTextField usernameField;
    public PasswordField passwordField;
    public ScaledResolution res;

    public boolean buttonEnabled = false, isLogging;

    public float hoverAlpha = 0;
    public float hoverHwidButtonAlpha = 0;
    public float enabledAlpha = 0;

    public URI registerAccount = URI.create("https://flux.today/forums/register.php");
    public URI resetPassword = URI.create("https://flux.today/forums/login.php?action=forget");

    public PopupDialog dialog = null;

    // 动画测试
    public boolean stage1 = true;
    public boolean stage2 = false;
    public float font_animation = 0;
    public float back_animation = 1;
    public TimeHelper timer = new TimeHelper();

    static boolean fontLoaded = false;
    public String content = "";

    static float timeDelta = -1000;

    @Override
    public void initGui() {
        timer.reset();
        res = new ScaledResolution(mc);
        this.usernameField = new CustomTextField(0, Flux.font, 0, 0, 100, 20);
        this.passwordField = new PasswordField(0, Flux.font, 0, 0, 100, 20);
        Keyboard.enableRepeatEvents(true);
        new Thread() {
            @Override
            public void run() {
                checkTime();
            }
        }.start();
        super.initGui();
    }

    public void checkTime() {
        try {
            if (timeDelta < -999) {
                long time = Long.parseLong(HttpUtil.performGetRequest(new URL("https://time.is/t/?zh.0.347.2464.0p.480.43d.1574683214663.1594044507830.")).substring(0, 13));
                float delta = (System.currentTimeMillis() - time) / 1000f;
                System.out.println("Sync time! " + delta);
                timeDelta = delta;
            }
        } catch (Throwable e) {
            this.dialog = new PopupDialog("Warning", "Failed to check system time!", 150, 55, true);
            e.printStackTrace();
        }

        if (Math.abs(timeDelta) > 10) {
            this.dialog = new PopupDialog("You may not be able to log in!", "The system time is not synchronized, please synchronize the time! (" + timeDelta + "s)", 320, 55, true);
        }
    }

    // @SoterObfuscator.Obfuscation(flags = "+native")
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        float wwidth = res.getScaledWidth();
        float wheight = res.getScaledHeight();
        RenderUtil.drawRect(0, 0, wwidth, wheight, 0xffffffff);
        RenderUtil.drawImage(new ResourceLocation("flux/loginbackground.png"), -150, 0, wwidth, wheight);

        drawGradientSideways(wwidth - 170, 0, wwidth - 150, wheight, RenderUtil.reAlpha(ColorUtils.BLACK.c, 0f), RenderUtil.reAlpha(ColorUtils.BLACK.c, 0.2f));

        FontManager.icon30.drawString("q", wwidth - 124, 27, 0xff4286f5);
        FontManager.font1.drawString(Flux.NAME.toUpperCase(), wwidth - 106, 25, 0xff4286f5);

        //USERNAME AND PASSWORD FIELD
        this.usernameField.xPosition = (int) (wwidth - 125);
        this.usernameField.yPosition = (int) (wheight / 2 - 50);

        this.passwordField.xPosition = (int) (wwidth - 125);
        this.passwordField.yPosition = (int) (wheight / 2 - 20);

        if (account != null) {
            this.usernameField.setText(account[0]);
            this.passwordField.setText(account[1]);
        }

        this.usernameField.drawTextBox();
        if (!usernameField.isFocused() && usernameField.getText().isEmpty()) {
            FontManager.font2.drawString("USERNAME", wwidth - 120, wheight / 2 - 45, 0xffdbdbdb);
        } else {
            RenderUtil.drawRect(wwidth - 120, wheight / 2 - 54, wwidth - 86, wheight / 2 - 48, 0xffffffff);
            FontManager.font3.drawString("USERNAME", wwidth - 118, wheight / 2 - 53.5f, this.usernameField.isFocused() ? 0xff7d7d7d : this.usernameField.getText().isEmpty() ? 0xffdbdbdb : 0xff7d7d7d);
        }

        this.passwordField.drawTextBox();
        if (!passwordField.isFocused() && passwordField.getText().isEmpty()) {
            FontManager.font2.drawString("PASSWORD", wwidth - 120, wheight / 2 - 15, 0xffdbdbdb);
        } else {
            RenderUtil.drawRect(wwidth - 120, wheight / 2 - 24, wwidth - 86.5f, wheight / 2 - 18, 0xffffffff);
            FontManager.font3.drawString("PASSWORD", wwidth - 118f, wheight / 2 - 23.5f, this.passwordField.isFocused() ? 0xff7d7d7d : this.passwordField.getText().isEmpty() ? 0xffdbdbdb : 0xff7d7d7d);
        }

        //FETCH HWID BUTTON
        GuiRenderUtils.drawRoundedRect(wwidth - 125, wheight - 115, 100, 15, 3, 0xff4286f5, 1, 0xff4286f5);

        // HWID BUTTON on hover alpha
        if (RenderUtil.isHovering(mouseX, mouseY, wwidth - 125, wheight - 115, wwidth - 25, wheight - 100) && this.dialog == null) {
            this.hoverHwidButtonAlpha = AnimationUtils.getAnimationState(this.hoverHwidButtonAlpha, 0.2f, 1);
        } else {
            this.hoverHwidButtonAlpha = AnimationUtils.getAnimationState(this.hoverHwidButtonAlpha, 0, 1);
        }

        FontManager.font2.drawString("GET HWID", wwidth - ((150 / 2f) + (FontManager.font2.getStringWidth("GET HWID") / 2)), wheight - 112f, ColorUtils.WHITE.c);

        if (this.hoverHwidButtonAlpha > 0) {
            if (Util.getOSType() == Util.EnumOS.WINDOWS)
                GuiRenderUtils.drawRoundedRect(wwidth - 125, wheight - 115, 100, 15, 3, RenderUtil.reAlpha(0xff000000, MathUtils.clampValue(this.hoverHwidButtonAlpha, 0, 1)), 1, RenderUtil.reAlpha(0xff000000, MathUtils.clampValue(this.hoverHwidButtonAlpha, 0, 1)));
        }

        //LOGIN BUTTON
        if (this.enabledAlpha < 1) {
            GuiRenderUtils.drawRoundedRect(wwidth - 125, wheight - 95, 100, 25, 3, 0xff7d7d7d, 1, 0xff7d7d7d);
        }

        buttonEnabled = !this.usernameField.getText().isEmpty() && !this.passwordField.getText().isEmpty() && !isLogging;
        if (buttonEnabled) {
            this.enabledAlpha = AnimationUtils.getAnimationState(this.enabledAlpha, 1, 5);
        } else {
            this.enabledAlpha = AnimationUtils.getAnimationState(this.enabledAlpha, 0, 5);
        }

        if (Util.getOSType() == Util.EnumOS.WINDOWS)
            GuiRenderUtils.drawRoundedRect(wwidth - 125, wheight - 95, 100, 25, 3, RenderUtil.reAlpha(0xff4286f5, MathUtils.clampValue(this.enabledAlpha, 0, 1)), 1, RenderUtil.reAlpha(0xff4286f5, MathUtils.clampValue(this.enabledAlpha, 0, 1)));

        // Change alpha if on Hover
        if (RenderUtil.isHovering(mouseX, mouseY, wwidth - 125, wheight - 95, wwidth - 25, wheight - 75) && this.dialog == null && !this.isLogging) {
            this.hoverAlpha = AnimationUtils.getAnimationState(this.hoverAlpha, 0.2f, 1);
        } else {
            this.hoverAlpha = AnimationUtils.getAnimationState(this.hoverAlpha, 0, 1);
        }

        FontManager.font2.drawString("LOGIN", wwidth - ((150 / 2f) + (FontManager.font2.getStringWidth("LOGIN") / 2)), wheight - 87f, ColorUtils.WHITE.c);

        // MAC OS support
        if (Util.getOSType() == Util.EnumOS.WINDOWS)
            GuiRenderUtils.drawRoundedRect(wwidth - 125, wheight - 95, 100, 25, 3, RenderUtil.reAlpha(0xff000000, MathUtils.clampValue(this.hoverAlpha, 0, 1)), 1, RenderUtil.reAlpha(0xff000000, MathUtils.clampValue(this.hoverAlpha, 0, 1)));

        FontManager.font4.drawString("q", wwidth - 60, wheight - 58, RenderUtil.reAlpha(0xff000000, 0.12f));

        FontManager.font5.drawString("No Account?", wwidth - 125, wheight - 42,
                RenderUtil.isHovering(mouseX, mouseY, wwidth - 125, wheight - 42, wwidth - 85, wheight - 35) && this.dialog == null ? new Color(0xff4286f5).darker().getRGB() : 0xff4286f5);
        FontManager.font5.drawString("Password Reset", wwidth - 125, wheight - 28,
                RenderUtil.isHovering(mouseX, mouseY, wwidth - 125, wheight - 28, wwidth - 78, wheight - 21) && this.dialog == null ? RenderUtil.reAlpha(0xff000000, 0.6f) : RenderUtil.reAlpha(0xff000000, 0.4f));

        if (this.dialog != null) {
            RenderUtil.drawRect(0, 0, wwidth, wheight, RenderUtil.reAlpha(ColorUtils.BLACK.c, 0.4f));
        }

        if (this.dialog != null) {
            if (this.dialog.destroy) {
                this.dialog = null;
                return;
            }

            this.dialog.render(mouseX, mouseY);
        }

        // 根据图层顺序,启动画面在这绘制
        if (!AnimationUtils.animationDone) {
            if (this.stage1) {
                content = "Please wait...";

                if (this.font_animation < 1) {
                    this.font_animation += 0.02f;
                }

                if (!fontLoaded && FontManager.wqy18 == FontManager.roboto15 && this.font_animation >= 1) {
                    FontManager.wqy18 = new FontUtils("wqy_microhei.ttf", Font.PLAIN, 18, 7, true);
                    FontManager.wqy15 = new FontUtils("wqy_microhei.ttf", Font.PLAIN, 15, 7, true);
                    fontLoaded = true;
                    timer.reset();
                }

                if (fontLoaded) {
                    content = "Welcome to Flux Client!";
                    if (timer.delay(3500)) {
                        this.stage1 = false;
                        this.stage2 = true;
                    }
                } else {
                    timer.reset();
                }
            }

            if (this.stage2) {
                this.font_animation = AnimationUtils.getAnimationState(this.font_animation, 0, 2f);
                this.back_animation = AnimationUtils.getAnimationState(this.back_animation, 0, 2f);
                if (this.font_animation == 0 && this.back_animation == 0) {
                    AnimationUtils.animationDone = true;
                }
            }

            RenderUtil.drawRect(0, 0, width, height, RenderUtil.reAlpha(ColorUtils.BLACK.c, this.back_animation));
            FontManager.robotoL40.drawCenteredStringWithAlpha(content, width / 2, height / 2 - 24,
                    ColorUtils.WHITE.c, this.font_animation > 1 ? 1 : this.font_animation);

            if (fontLoaded) {
                FontManager.robotoL18.drawCenteredStringWithAlpha("Ready for authentication", width / 2, height / 2,
                        ColorUtils.WHITE.c, this.font_animation > 1 ? 1 : this.font_animation);
            }
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (dialog == null && keyCode == 88) {
            IRCClient.isChina = !IRCClient.isChina;
            Display.setTitle(Flux.NAME + " b" + Flux.VERSION + " - " + (IRCClient.isChina ? "China" : "Global"));
            this.dialog = new PopupDialog("Server Switched", String.format("Successfully switched server to %s server!", (IRCClient.isChina ? "China" : "Global")), 200, 55, true);
        }

        //Tab切换输入框
        if (typedChar == '\t') {
            if (!this.usernameField.isFocused() && !this.passwordField.isFocused()) {
                this.usernameField.setFocused(true);
            } else {
                this.usernameField.setFocused(this.passwordField.isFocused());
                this.passwordField.setFocused(!this.usernameField.isFocused());
            }
        }

        if (typedChar == '\r' && buttonEnabled) {
            this.doLogin();
        }

        this.usernameField.textboxKeyTyped(typedChar, keyCode);
        this.passwordField.textboxKeyTyped(typedChar, keyCode);
        super.keyTyped(typedChar, keyCode);
    }

    @SoterObfuscator.Obfuscation(flags = "+native")
    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        float wwidth = res.getScaledWidth();
        float wheight = res.getScaledHeight();

        if (this.dialog != null) {
            this.dialog.doClick(mouseX, mouseY, mouseButton);
            return;
        }

        this.usernameField.mouseClicked(mouseX, mouseY, mouseButton);
        this.passwordField.mouseClicked(mouseX, mouseY, mouseButton);

        if (RenderUtil.isHovering(mouseX, mouseY, wwidth - 125, wheight - 95, wwidth - 25, wheight - 70) && mouseButton == 0 && buttonEnabled) {
            this.doLogin();
        }

        if (RenderUtil.isHovering(mouseX, mouseY, wwidth - 125, wheight - 42, wwidth - 85, wheight - 35) && mouseButton == 0) {
            try {
                Desktop desktop = Desktop.getDesktop();
                desktop.browse(registerAccount);
            } catch (Exception ex) {
                System.out.println("An error occurred while trying to open a url!");
            }

        }

        if (RenderUtil.isHovering(mouseX, mouseY, wwidth - 125, wheight - 28, wwidth - 78, wheight - 21) && mouseButton == 0) {
            try {
                Desktop desktop = Desktop.getDesktop();
                desktop.browse(resetPassword);
            } catch (Exception ex) {
                System.out.println("An error occurred while trying to open a url!");
            }
        }

        // GET HWID
        if (RenderUtil.isHovering(mouseX, mouseY, wwidth - 125, wheight - 115, wwidth - 25, wheight - 100) && mouseButton == 0) {
            try {
                Class clazz = Class.forName("java.lang.ProcessEnvironment");
                Field field = clazz.getDeclaredField("theUnmodifiableEnvironment");
                field.setAccessible(true);
                Map<String, String> map = (Map<String, String>) field.get(clazz);
                Processor[] processor = (new SystemInfo()).getHardware().getProcessors();
                String a = new Base58(14513).encode((map.get("PROCESSOR_IDENTIFIER") + map.get("LOGNAME") + map.get("USER")).getBytes());
                String b = new Base58(13132).encode((processor[0].getName() + processor.length + map.get("PROCESSOR_LEVEL") + a).getBytes());
                String c = new Base58(23241).encode((map.get("COMPUTERNAME") + System.getProperty("user.name") + b).getBytes());

                MessageDigest mdsha1 = MessageDigest.getInstance("SHA-1");
                byte[] sha1hash;
                mdsha1.update(Base64.getEncoder().encodeToString((a + b + c).getBytes()).getBytes("iso-8859-1"), 0, Base64.getEncoder().encodeToString((a + b + c).getBytes()).length());
                sha1hash = mdsha1.digest();
                final StringBuffer buf = new StringBuffer();
                for (int i = 0; i < sha1hash.length; ++i) {
                    int halfbyte = sha1hash[i] >>> 3 & 0xF;
                    int two_halfs = 0;
                    do {
                        if (halfbyte >= 0 && halfbyte <= 9) {
                            buf.append((char) (48 + halfbyte));
                        } else {
                            buf.append((char) (97 + (halfbyte - 10)));
                        }
                        halfbyte = (sha1hash[i] & 0xF);
                    } while (two_halfs++ < 1);
                }
                String hwid = buf.toString();

                MessageDigest md = MessageDigest.getInstance("MD5");
                md.update(hwid.getBytes());
                StringBuffer hexString = new StringBuffer();

                byte[] byteData = md.digest();

                for (byte aByteData : byteData) {
                    String hex = Integer.toHexString(0xff & aByteData);
                    if (hex.length() == 1) hexString.append('0');
                    hexString.append(hex);
                }
                String origin = new Base58(14514).encode((hwid + hexString).getBytes());
                StringBuffer buffer = new StringBuffer();
                for (int i = 16; i < 16 + 5 * 4; i += 5) {
                    buffer.append(origin, i, i + 5);
                    buffer.append('-');
                }
                buffer.deleteCharAt(buffer.length() - 1);

                if (!origin.equalsIgnoreCase(origin.toUpperCase()))
                    ModuleManager.killAuraMod = null;

                if (!origin.toUpperCase().toLowerCase().equals(origin.toLowerCase()))
                    ModuleManager.killAuraMod = null;

                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                String text = "imagine cracking flux";
                try {
                    String.class.getMethods()[76].getName();
                } catch (Throwable throwable) {
                    text = buffer.toString().toUpperCase();
                }
                Transferable trans = new StringSelection(text);
                clipboard.setContents(trans, null);

                if (this.dialog == null) {
                    this.dialog = new PopupDialog("Copying HWID to the clipboard", "Successfully copied your HWID to the clipboard", 200, 55, true);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @SoterObfuscator.Obfuscation(flags = "+native")
    public void doLogin() {
        if (checkUsername()) {
            isLogging = true;
            Flux.irc = new IRCClient(usernameField.getText(), passwordField.getText());
        } else {
            dialog = new PopupDialog("Error", "This client was not build by yourself.", 200, 55, true);
        }
    }

    @SoterObfuscator.Obfuscation(flags = "+native")
    public boolean checkUsername() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("FluxAntiLeak"))));
            String line = null;
            while ((line = in.readLine()) != null) {
                if (DigestUtils.md5Hex(usernameField.getText() + "FAL").equals(line)) {
                    return true;
                }
            }
        } catch (Throwable e) {

        }
        return false;
    }

    public void drawGradientSideways(float left, float top, float right, float bottom, int startColor, int endColor) {
        float f = (float) (startColor >> 24 & 255) / 255.0F;
        float f1 = (float) (startColor >> 16 & 255) / 255.0F;
        float f2 = (float) (startColor >> 8 & 255) / 255.0F;
        float f3 = (float) (startColor & 255) / 255.0F;
        float f4 = (float) (endColor >> 24 & 255) / 255.0F;
        float f5 = (float) (endColor >> 16 & 255) / 255.0F;
        float f6 = (float) (endColor >> 8 & 255) / 255.0F;
        float f7 = (float) (endColor & 255) / 255.0F;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(7425);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        worldrenderer.pos((double) right, (double) top, (double) this.zLevel).color(f5, f6, f7, f4).endVertex();
        worldrenderer.pos((double) left, (double) top, (double) this.zLevel).color(f1, f2, f3, f).endVertex();
        worldrenderer.pos((double) left, (double) bottom, (double) this.zLevel).color(f1, f2, f3, f).endVertex();
        worldrenderer.pos((double) right, (double) bottom, (double) this.zLevel).color(f5, f6, f7, f4).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    public void updateScreen() {
        res = new ScaledResolution(mc);
        this.usernameField.updateCursorCounter();
        this.passwordField.updateCursorCounter();
    }

}
