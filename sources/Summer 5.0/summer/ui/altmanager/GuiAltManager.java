package summer.ui.altmanager;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import net.minecraft.client.gui.*;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import summer.base.utilities.RenderUtils;

public class GuiAltManager extends GuiScreen
{
    private static Minecraft mc;
    private GuiButton login;
    private GuiButton remove;
    private GuiButton rename;
    private AltLoginThread loginThread;
    private int offset;
    public Alt selectedAlt;
    private String status;
    static {
        GuiAltManager.mc = Minecraft.getMinecraft();
    }
    
    public GuiAltManager() {
        this.selectedAlt = null;
        this.status = "";
      
    }
    
    public void actionPerformed(final GuiButton button) throws IOException {
        switch (button.id) {
            case 0: {
                if (this.loginThread == null) {
                    GuiAltManager.mc.displayGuiScreen(null);
                    break;
                }
                if (!this.loginThread.getStatus().equals("Logging in...") && !this.loginThread.getStatus().equals("Do not hit back! Logging in...")) {
                    GuiAltManager.mc.displayGuiScreen(null);
                    break;
                }
                this.loginThread.setStatus("Do not hit back! Logging in...");
                break;
            }
            case 1: {
                final String user = this.selectedAlt.getUsername();
                final String pass = this.selectedAlt.getPassword();
                (this.loginThread = new AltLoginThread(user, pass, true)).start();
                break;
            }
            case 2: {
                if (this.loginThread != null) {
                    this.loginThread = null;
                }
                AltManager.getAlts().remove(this.selectedAlt);
                this.status = "cRemoved.";
                this.selectedAlt = null;
            
                break;
            }
            case 3: {
                GuiAltManager.mc.displayGuiScreen(new GuiAddAlt(this));
                break;
            }
            case 4: {
                GuiAltManager.mc.displayGuiScreen(new GuiAltLogin(this));
                break;
            }
            case 5: {
                
                final List<Alt> alts = AltManager.alts;
                if (alts != null)
                {
                    final Random random = new Random();
                    
                    final Alt randomAlt = alts.get(random.nextInt(AltManager.alts.size()));
                    final String user2 = randomAlt.getUsername();
                    final String pass2 = randomAlt.getPassword();
                    (this.loginThread = new AltLoginThread(user2, pass2,  true)).start();
                }
                break;
            }
            case 6: {
                GuiAltManager.mc.displayGuiScreen(new GuiRenameAlt(this));
                break;
            }
            case 7: {
                
                final Alt lastAlt = AltManager.lastAlt;
                if (lastAlt != null) {
                    final String user3 = lastAlt.getUsername();
                    final String pass3 = lastAlt.getPassword();
                    (this.loginThread = new AltLoginThread(user3, pass3, true)).start();
                    break;
                }
                if (this.loginThread == null) {
                    this.status = "\247cThere is no last used alt!";
                    break;
                }
                this.loginThread.setStatus("\247cThere is no last used alt!");
                break;
            }
            case 8: {
            	GuiAltManager.mc.displayGuiScreen(new GuiTheAltening(this));
            }
        }
    }
    
    @Override
    public void drawScreen(final int par1, final int par2, final float par3) {
        this.drawDefaultBackground();
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
        this.drawDefaultBackground();
        Minecraft.fontRendererObj.drawStringWithShadow(GuiAltManager.mc.session.getUsername(), 10.0f, 10.0f, -7829368);
        final FontRenderer fontRendererObj = Minecraft.fontRendererObj;
        final StringBuilder sb = new StringBuilder("Account Manager - ");
        
        fontRendererObj.drawCenteredString(sb.append(AltManager.getAlts().size()).append(" alts").toString(), width / 2, 10, -1);
        Minecraft.fontRendererObj.drawCenteredString((this.loginThread == null) ? this.status : this.loginThread.getStatus(), width / 2, 20, -1);
        GL11.glPushMatrix();
        this.prepareScissorBox(0.0f, 33.0f, (float) width, (float)(height - 50));
        GL11.glEnable(3089);
        int y = 38;
        
        for (final Alt alt : AltManager.getAlts()) {
            if (this.isAltInArea(y)) {
                String name;
                if (alt.getMask().equals("")) {
                    name = alt.getUsername();
                }
                else {
                    name = alt.getMask();
                }
                String pass;
                if (alt.getPassword().equals("")) {
                    pass = "\247cCracked";
                }
                else {
                    pass = alt.getPassword().replaceAll(".", "*");
                }
                if (alt == this.selectedAlt) {
                    if (this.isMouseOverAlt(par1, par2, y - this.offset) && Mouse.isButtonDown(0)) {
                        RenderUtils.drawBorderedRect(52.0f, (float)(y - this.offset - 4), (float)(this.width - 52), (float)(y - this.offset + 20), 1.0f, -16777216, -2142943931);
                    }
                    else if (this.isMouseOverAlt(par1, par2, y - this.offset)) {
                        RenderUtils.drawBorderedRect(52.0f, (float)(y - this.offset - 4), (float)(this.width - 52), (float)(y - this.offset + 20), 1.0f, -16777216, -2142088622);
                    }
                    else {
                        RenderUtils.drawBorderedRect(52.0f, (float)(y - this.offset - 4), (float)(this.width - 52), (float)(y - this.offset + 20), 1.0f, -16777216, -2144259791);
                    }
                }
                else if (this.isMouseOverAlt(par1, par2, y - this.offset) && Mouse.isButtonDown(0)) {
                    RenderUtils.drawBorderedRect(52.0f, (float)(y - this.offset - 4), (float)(this.width - 52), (float)(y - this.offset + 20), 1.0f, -16777216, -2146101995);
                }
                else if (this.isMouseOverAlt(par1, par2, y - this.offset)) {
                    RenderUtils.drawBorderedRect(52.0f, (float)(y - this.offset - 4), (float)(this.width - 52), (float)(y - this.offset + 20), 1.0f, -16777216, -2145180893);
                }
                Minecraft.fontRendererObj.drawCenteredString(name, width / 2, y - this.offset, -1);
                Minecraft.fontRendererObj.drawCenteredString(pass, width / 2, y - this.offset + 10, 5592405);
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
    
    @Override
    public void initGui() {
        this.buttonList.add(new GuiButton(0, width / 2 + 4 + 76, height - 24, 75, 20, "Cancel"));
        this.buttonList.add(this.login = new GuiButton(1, width / 2 - 154, height - 48, 70, 20, "Login"));
        this.buttonList.add(this.remove = new GuiButton(2, width / 2 - 74, height - 24, 70, 20, "Remove"));
        this.buttonList.add(new GuiButton(3, width / 2 + 4 + 76, height - 48, 75, 20, "Add"));
        this.buttonList.add(new GuiButton(4, width / 2 - 74, height - 48, 70, 20, "Direct Login"));
        //this.buttonList.add(new GuiButton(5, this.width / 2 + 4, this.height - 48, 70, 20, "Random"));
        this.buttonList.add(this.rename = new GuiButton(6, width / 2 + 4, height - 24, 70, 20, "Edit"));
        this.buttonList.add(this.rename = new GuiButton(7, width / 2 - 154, height - 24, 70, 20, "Last Alt"));
        this.buttonList.add(new GuiButton(8, width / 2 + 4, height - 48, 70, 20, "The Altening"));
        this.login.enabled = false;
        this.remove.enabled = false;
        this.rename.enabled = false;
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
        int y = 38 - this.offset;
        
        for (final Alt alt : AltManager.getAlts()) {
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
        final int factor = new ScaledResolution(GuiAltManager.mc).getScaleFactor();
        GL11.glScissor((int)(x * factor), (int)((new ScaledResolution(GuiAltManager.mc).getScaledHeight() - y2) * factor), (int)((x2 - x) * factor), (int)((y2 - y) * factor));
    }
    
    public void renderBackground(final int par1, final int par2) {
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glDisable(3008);
        this.drawDefaultBackground();
        final Tessellator var3 = Tessellator.instance;
        var3.draw();
        GL11.glDepthMask(true);
        GL11.glEnable(2929);
        GL11.glEnable(3008);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    }
}
