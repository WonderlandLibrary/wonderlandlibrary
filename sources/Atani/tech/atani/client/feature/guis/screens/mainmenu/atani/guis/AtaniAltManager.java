package tech.atani.client.feature.guis.screens.mainmenu.atani.guis;

import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import net.minecraft.client.gui.*;
import net.minecraft.client.resources.I18n;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import tech.atani.client.feature.font.storage.FontStorage;
import tech.atani.client.feature.guis.screens.mainmenu.atani.button.AtaniButton;
import tech.atani.client.feature.guis.screens.mainmenu.atani.AtaniMainMenu;
import tech.atani.client.utility.discord.DiscordRP;
import tech.atani.client.utility.render.RenderUtil;
import tech.atani.client.utility.render.shader.render.ingame.RenderableShaders;
import tech.atani.client.utility.render.shader.shaders.RoundedShader;
import tech.atani.client.feature.account.Account;
import tech.atani.client.feature.account.storage.AccountStorage;

public class AtaniAltManager extends GuiScreen
{
    private static Minecraft mc;
    private GuiButton login;
    private GuiButton remove;
    private GuiButton rename;
    private AltLoginThread loginThread;
    private int offset;
    public Account selectedAlt;
    private String status;
    static {
        AtaniAltManager.mc = Minecraft.getMinecraft();
    }
    
    public AtaniAltManager() {
        this.selectedAlt = null;
        this.status = "";
      
    }

    @Override
    public void initGui() {
        this.buttonList.clear();
        this.buttonList.add(this.login = new AtaniButton(1, this.width / 2 - 154, this.height - 52 - 30 - 5, 150, 20, "Login"));
        this.buttonList.add(new AtaniButton(3, this.width / 2 + 4, this.height - 52 - 30 - 5, 150, 20, "Add"));
        this.buttonList.add(this.rename = new AtaniButton(6, this.width / 2 - 154, this.height - 28 - 30, 71, 20, "Rename"));
        this.buttonList.add(this.remove = new AtaniButton(2, this.width / 2 - 76 + 1, this.height - 28 - 30, 71, 20, "Remove"));
        this.buttonList.add(new AtaniButton(4, this.width / 2 + 4, this.height - 28 - 30, 71, 20, "Direct"));
        this.buttonList.add(new AtaniButton(0, this.width / 2 + 82 + 1, this.height - 28 - 30, 71, 20, I18n.format("gui.cancel", new Object[0])));
        this.login.enabled = false;
        this.remove.enabled = false;
        this.rename.enabled = false;
    }
    
    public void actionPerformed(final GuiButton button) throws IOException {
        switch (button.id) {
            case 0: {
                if (this.loginThread == null) {
                    AtaniAltManager.mc.displayGuiScreen(null);
                    break;
                }
                if (!this.loginThread.getStatus().equals("Logging in...") && !this.loginThread.getStatus().equals("Do not hit back! Logging in...")) {
                    AtaniAltManager.mc.displayGuiScreen(null);
                    break;
                }
                this.loginThread.setStatus("Do not hit back! Logging in...");
                break;
            }
            case 1: {
                final String user = this.selectedAlt.getName();
                final String pass = this.selectedAlt.getPassword();
                (this.loginThread = new AltLoginThread(user, pass, true)).start();
                break;
            }
            case 2: {
                if (this.loginThread != null) {
                    this.loginThread = null;
                }
                AccountStorage.getInstance().getList().remove(this.selectedAlt);
                this.status = "§cRemoved.";
                this.selectedAlt = null;
            
                break;
            }
            case 3: {
                AtaniAltManager.mc.displayGuiScreen(new AtaniAddAlt(this));
                break;
            }
            case 4: {
                AtaniAltManager.mc.displayGuiScreen(new AtaniAltLogin(this));
                break;
            }
            case 5: {
                
                final List<Account> alts = AccountStorage.getInstance().getList();
                if (alts != null)
                {
                    final Random random = new Random();
                    
                    final Account randomAlt = alts.get(random.nextInt(AccountStorage.getInstance().getList().size()));
                    final String user2 = randomAlt.getName();
                    final String pass2 = randomAlt.getPassword();
                    (this.loginThread = new AltLoginThread(user2, pass2,  true)).start();
                }
                break;
            }
            case 6: {
                AtaniAltManager.mc.displayGuiScreen(new AtaniRenameAlt(this));
                break;
            }
        }
    }
    
    @Override
    public void drawScreen(final int par1, final int par2, final float par3) {
        DiscordRP.update("Alt Menu", "Selecting an alt");

        if (Mouse.hasWheel()) {
            final int wheel = Mouse.getDWheel();
            if (wheel < 0) {
                this.offset += 26;
                if (this.offset < 0) {
                    this.offset = 0;
                }
            }
            else if (wheel > 0) {
                this.offset -= 26;
                if (this.offset < 0) {
                    this.offset = 0;
                }
            }
        }

        RenderUtil.drawRect(0, 0, this.width, this.height, new Color(16, 16, 16).getRGB());
        AtaniMainMenu.shaderBackground.render();

        int top = 32 + 20;
        int k1 = this.width / 2 - 220 / 2 + 1;
        int y1 = top + 4 ;

        RenderableShaders.render(() -> {
            RoundedShader.drawRound(k1 - 60, y1 - 30, 220 + 60 * 2, height - 50, 10, new Color(0, 0, 0, 130));
        });

        FontStorage.getInstance().findFont("Roboto", 19).drawStringWithShadow(AtaniAltManager.mc.session.getUsername(), 10.0f, 10.0f, -7829368);
        final FontRenderer fontRendererObj = FontStorage.getInstance().findFont("Roboto", 19);
        final StringBuilder sb = new StringBuilder("Account Manager - ");
        
        fontRendererObj.drawCenteredString(status == "" && loginThread == null ? sb.append(AccountStorage.getInstance().getList().size()).append(" alts").toString() : (this.loginThread == null) ? this.status : this.loginThread.getStatus(), width / 2, 20 + 20, -1);
        GL11.glPushMatrix();
        this.prepareScissorBox(0.0f, 33.0f + 20, (float) width, (float)(height - 50));
        GL11.glEnable(3089);
        int y = 38 + 20;
        
        for (final Account alt : AccountStorage.getInstance().getList()) {
            if (this.isAltInArea(y)) {
                String name = alt.getName();
                String pass = alt.getPassword();
                if (alt.getPassword().equals("")) {
                    pass = "\247cCracked";
                }
                else {
                    pass = alt.getPassword().replaceAll(".", "*");
                }
                if(alt == selectedAlt) {
                    float halfWidthName = FontStorage.getInstance().findFont("Roboto", 19).getStringWidthInt(name);
                    float halfWidthPass = FontStorage.getInstance().findFont("Roboto", 19).getStringWidthInt(pass);
                    float halfWidth = (Math.max(halfWidthName, halfWidthPass) + 10) / 2;
                    RoundedShader.drawRound(this.width / 2 - halfWidth, (float)(y - this.offset - 4),  halfWidth * 2, 25, 10, new Color(0, 0, 0, 130));
                }
                FontStorage.getInstance().findFont("Roboto", 19).drawCenteredString(name, width / 2, y - this.offset, -1);
                FontStorage.getInstance().findFont("Roboto", 19).drawCenteredString(pass, width / 2, y - this.offset + 10, 5592405);
                y += 26;
            }
        }
        GL11.glDisable(3089);
        GL11.glPopMatrix();
        super.drawScreen(par1, par2, par3);
        if (this.selectedAlt == null) {
            this.login.enabled = false;
            this.remove.enabled = false;
            this.rename.enabled = false;
        }
        else {
            this.login.enabled = true;
            this.remove.enabled = true;
            this.rename.enabled = true;
        }
        if (Keyboard.isKeyDown(200)) {
            this.offset -= 26;
            if (this.offset < 0) {
                this.offset = 0;
            }
        }
        else if (Keyboard.isKeyDown(208)) {
            this.offset += 26;
            if (this.offset < 0) {
                this.offset = 0;
            }
        }
    }
    
    private boolean isAltInArea(final int y) {
        return y - this.offset <= height - 50;
    }
    
    private boolean isMouseOverAlt(final int x, final int y, final int y1) {
        return x >= 52 && y >= y1 - 4 && x <= width - 52 && y <= y1 + 20 && y >= 33 && x <= width && y <= height - 50;
    }
    
    @Override
    protected void mouseClicked(final int par1, final int par2, final int par3) throws IOException {
        if (this.offset < 0) {
            this.offset = 0;
        }
        int y = 38 - this.offset + 20;
        
        for (final Account alt : AccountStorage.getInstance().getList()) {
            if (this.isMouseOverAlt(par1, par2, y)) {
                if (alt == this.selectedAlt) {
                    this.actionPerformed(this.buttonList.get(1));
                    return;
                }
                this.selectedAlt = alt;
            }
            y += 26;
        }
        try {
            super.mouseClicked(par1, par2, par3);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void prepareScissorBox(final float x, final float y, final float x2, final float y2) {
        final int factor = new ScaledResolution(AtaniAltManager.mc).getScaleFactor();
        GL11.glScissor((int)(x * factor), (int)((new ScaledResolution(AtaniAltManager.mc).getScaledHeight() - y2) * factor), (int)((x2 - x) * factor), (int)((y2 - y) * factor));
    }
    
    public void renderBackground(final int par1, final int par2) {
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glDisable(3008);
        this.drawDefaultBackground();
        final Tessellator var3 = Tessellator.getInstance();
        var3.draw();
        GL11.glDepthMask(true);
        GL11.glEnable(2929);
        GL11.glEnable(3008);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    }
}
