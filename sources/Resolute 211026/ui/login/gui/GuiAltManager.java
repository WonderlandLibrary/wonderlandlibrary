// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.ui.login.gui;

import java.util.ArrayList;
import java.awt.Container;
import vip.Resolute.ui.login.gui.impl.AccountImport;
import javax.swing.JFrame;
import vip.Resolute.ui.login.gui.impl.GuiAltLogin;
import vip.Resolute.ui.login.gui.impl.GuiAddAlt;
import vip.Resolute.ui.MainMenu;
import vip.Resolute.util.font.MinecraftFontRenderer;
import vip.Resolute.Resolute;
import com.mojang.realmsclient.gui.ChatFormatting;
import vip.Resolute.util.font.FontUtil;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.gui.GuiButton;
import java.io.IOException;
import java.util.Random;
import vip.Resolute.ui.login.gui.thread.AltLoginThread;
import vip.Resolute.ui.shader.GLSLSandboxShader;
import vip.Resolute.ui.login.system.Account;
import vip.Resolute.ui.login.gui.components.GuiAccountList;
import net.minecraft.client.gui.GuiScreen;

public class GuiAltManager extends GuiScreen
{
    public static GuiAltManager INSTANCE;
    private GuiAccountList accountList;
    private Account selectAccount;
    public static Account currentAccount;
    private GLSLSandboxShader backgroundShader;
    public static AltLoginThread loginThread;
    private long initTime;
    private final Random random;
    
    public GuiAltManager() {
        this.selectAccount = null;
        this.initTime = System.currentTimeMillis();
        this.random = new Random();
        GuiAltManager.INSTANCE = this;
        try {
            this.backgroundShader = new GLSLSandboxShader("/mainMenuShader.frag");
        }
        catch (IOException e) {
            throw new IllegalStateException("Failed to load backgound shader", e);
        }
    }
    
    @Override
    public void initGui() {
        (this.accountList = new GuiAccountList(this)).registerScrollButtons(7, 8);
        this.accountList.elementClicked(-1, false, 0, 0);
        this.buttonList.add(new GuiButton(0, GuiAltManager.width / 2 + 158, GuiAltManager.height - 24, 100, 20, "Cancel"));
        this.buttonList.add(new GuiButton(1, GuiAltManager.width / 2 - 154, GuiAltManager.height - 48, 100, 20, "Login"));
        this.buttonList.add(new GuiButton(2, GuiAltManager.width / 2 - 50, GuiAltManager.height - 24, 100, 20, "Remove"));
        this.buttonList.add(new GuiButton(5, GuiAltManager.width / 2 + 4 + 50, GuiAltManager.height - 48, 100, 20, "Import Alts"));
        this.buttonList.add(new GuiButton(4, GuiAltManager.width / 2 - 50, GuiAltManager.height - 48, 100, 20, "Direct Login"));
        this.buttonList.add(new GuiButton(3, GuiAltManager.width / 2 - 154, GuiAltManager.height - 24, 100, 20, "Add Alt"));
        this.buttonList.add(new GuiButton(7, GuiAltManager.width / 2 + 54, GuiAltManager.height - 24, 100, 20, "Random Alt"));
        this.buttonList.add(new GuiButton(8, GuiAltManager.width / 2 - 258, GuiAltManager.height - 48, 100, 20, "Last Alt"));
        this.buttonList.add(new GuiButton(9, GuiAltManager.width / 2 + 158, GuiAltManager.height - 48, 100, 20, "Clear Alts"));
    }
    
    @Override
    public void drawScreen(final int p_drawScreen_1_, final int p_drawScreen_2_, final float p_drawScreen_3_) {
        GlStateManager.enableAlpha();
        GlStateManager.disableCull();
        final int i = 274;
        final int j = GuiAltManager.width / 2 - i / 2;
        final int k = 30;
        this.backgroundShader.useShader(GuiAltManager.width, GuiAltManager.height, (float)p_drawScreen_1_, (float)p_drawScreen_2_, (System.currentTimeMillis() - this.initTime) / 1000.0f);
        GL11.glBegin(7);
        GL11.glVertex2f(-1.0f, -1.0f);
        GL11.glVertex2f(-1.0f, 1.0f);
        GL11.glVertex2f(1.0f, 1.0f);
        GL11.glVertex2f(1.0f, -1.0f);
        GL11.glEnd();
        GL20.glUseProgram(0);
        this.accountList.drawScreen(p_drawScreen_1_, p_drawScreen_2_, p_drawScreen_3_);
        super.drawScreen(p_drawScreen_1_, p_drawScreen_2_, p_drawScreen_3_);
        String status = "Idle...";
        if (GuiAltManager.loginThread != null) {
            status = GuiAltManager.loginThread.getStatus();
        }
        final MinecraftFontRenderer fr = FontUtil.moon;
        fr.drawCenteredStringWithShadow(ChatFormatting.GREEN + this.mc.session.getUsername(), (float)(GuiAltManager.width / 23), 13.0f, -1);
        fr.drawCenteredStringWithShadow("Account Manager - " + Resolute.getAccountManager().getAccounts().size() + " alts", (float)(GuiAltManager.width / 2), 4.0f, -1);
        fr.drawCenteredStringWithShadow((GuiAltManager.loginThread == null) ? (ChatFormatting.GRAY + "Idle...") : GuiAltManager.loginThread.getStatus(), (float)(GuiAltManager.width / 2), 16.0f, -1);
    }
    
    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        this.accountList.handleMouseInput();
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        switch (button.id) {
            case 0: {
                if (GuiAltManager.loginThread == null || !GuiAltManager.loginThread.getStatus().contains("Logging in")) {
                    this.mc.displayGuiScreen(new MainMenu());
                    break;
                }
                break;
            }
            case 1: {
                if (this.accountList.selected == -1) {
                    return;
                }
                (GuiAltManager.loginThread = new AltLoginThread(this.accountList.getSelectedAccount().getEmail(), this.accountList.getSelectedAccount().getPassword())).start();
                break;
            }
            case 2: {
                this.accountList.removeSelected();
                break;
            }
            case 3: {
                if (GuiAltManager.loginThread != null) {
                    GuiAltManager.loginThread = null;
                }
                this.mc.displayGuiScreen(new GuiAddAlt(this));
                break;
            }
            case 4: {
                if (GuiAltManager.loginThread != null) {
                    GuiAltManager.loginThread = null;
                }
                this.mc.displayGuiScreen(new GuiAltLogin(this));
                break;
            }
            case 7: {
                final ArrayList<Account> registry = Resolute.getAccountManager().getAccounts();
                if (registry.isEmpty()) {
                    return;
                }
                final Random random = new Random();
                final Account randomAlt = registry.get(random.nextInt(Resolute.getAccountManager().getAccounts().size()));
                if (randomAlt.isBanned()) {
                    return;
                }
                this.login(GuiAltManager.currentAccount = randomAlt);
                break;
            }
            case 5: {
                final JFrame frame = new JFrame("Import");
                frame.setAlwaysOnTop(true);
                final AccountImport accountImport = new AccountImport();
                frame.setContentPane(accountImport);
                new Thread(() -> accountImport.openButton.doClick()).start();
                break;
            }
            case 8: {
                if (Resolute.getAccountManager().getLastAlt() == null) {
                    return;
                }
                (GuiAltManager.loginThread = new AltLoginThread(Resolute.getAccountManager().getLastAlt().getEmail(), Resolute.getAccountManager().getLastAlt().getPassword())).start();
                break;
            }
            case 9: {
                if (Resolute.getAccountManager().getAccounts().isEmpty()) {
                    return;
                }
                Resolute.getAccountManager().getAccounts().clear();
                break;
            }
        }
    }
    
    public void login(final Account account) {
        (GuiAltManager.loginThread = new AltLoginThread(account.getEmail(), account.getPassword())).start();
    }
}
