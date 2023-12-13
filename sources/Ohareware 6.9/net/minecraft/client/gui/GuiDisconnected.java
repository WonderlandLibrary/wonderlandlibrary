package net.minecraft.client.gui;

import com.ohare.client.Client;
import com.ohare.client.gui.account.gui.GuiAltManager;
import com.ohare.client.gui.account.gui.impl.GuiAlteningLogin;
import com.ohare.client.gui.account.gui.thread.AccountLoginThread;
import com.ohare.client.gui.account.system.Account;
import com.ohare.client.utils.thealtening.TheAltening;
import com.ohare.client.utils.thealtening.domain.AlteningAlt;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.IChatComponent;

import java.io.IOException;
import java.util.*;

public class GuiDisconnected extends GuiScreen
{
    public static boolean niggaButton = false;
    public static ServerData serverData;
    private String reason;
    private IChatComponent message;
    private List multilineMessage;
    private final GuiScreen parentScreen;
    private int field_175353_i;
    private static final String __OBFID = "CL_00000693";

    private static ServerListEntryNormal lastServer;

    public GuiDisconnected(GuiScreen p_i45020_1_, String p_i45020_2_, IChatComponent p_i45020_3_)
    {
        this.parentScreen = p_i45020_1_;
        this.reason = I18n.format(p_i45020_2_, new Object[0]);
        this.message = p_i45020_3_;
    }

    /**
     * Fired when a key is typed (except F11 who toggle full screen). This is the equivalent of
     * KeyListener.keyTyped(KeyEvent e). Args : character (character on the key), keyCode (lwjgl Keyboard key code)
     */
    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {}

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    @Override
    public void initGui()
    {
        this.buttonList.clear();
        this.multilineMessage = this.fontRendererObj.listFormattedStringToWidth(this.message.getFormattedText(), this.width - 50);
        this.field_175353_i = this.multilineMessage.size() * this.fontRendererObj.FONT_HEIGHT;
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 2 + this.field_175353_i / 2 + this.fontRendererObj.FONT_HEIGHT, I18n.format("gui.toMenu", new Object[0])));
        this.buttonList.add(new GuiButton(1, this.width / 2 + 103, this.height / 2 + this.field_175353_i / 2 + this.fontRendererObj.FONT_HEIGHT,90, 20, "Go to AltManager"));
        this.buttonList.add(new GuiButton(2, this.width / 2 + 103, this.height / 2 + this.field_175353_i / 2 + 33,90, 20, "Set Banned"));
        this.buttonList.add(new GuiButton(4, this.width / 2 + 103, this.height / 2 + this.field_175353_i / 2 + 57,90, 20, "Remove Alt"));
        this.buttonList.add(new GuiButton(5, this.width / 2 - 100, this.height / 2 + this.field_175353_i / 2 + 33, "Relog with new Alt (Normal)"));
        this.buttonList.add(new GuiButton(6, this.width / 2 - 100, this.height / 2 + this.field_175353_i / 2 + 57, "Relog with new Alt (The Altening)"));
        this.buttonList.add(new GuiButton(7, this.width / 2 + 103, this.height / 2 + this.field_175353_i / 2 + 81,90, 20, "Relog"));
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException
    {
        if (button.id == 0)
        {
            this.mc.displayGuiScreen(this.parentScreen);
        }

        if(button.id == 1) {
            this.mc.displayGuiScreen(new GuiAltManager());
        }

        if(button.id == 2 && GuiAltManager.INSTANCE.currentAccount != null) {
            GuiAltManager.INSTANCE.currentAccount.setBanned(true);
        }
        if(button.id == 4) {
            if(GuiAltManager.INSTANCE.currentAccount != null) {
                if (GuiAltManager.INSTANCE.loginThread != null) {
                    GuiAltManager.INSTANCE.loginThread = null;
                }
                Client.INSTANCE.getAccountManager().getAccounts().remove(GuiAltManager.INSTANCE.currentAccount);
                Client.INSTANCE.getAccountManager().save();
            }
        }
        if(button.id == 5) {
            ArrayList<Account> registry = Client.INSTANCE.getAccountManager().getAccounts();
            Random random = new Random();
            Account randomAlt = registry.get(random.nextInt(Client.INSTANCE.getAccountManager().getAccounts().size()));
            String user2 = randomAlt.getEmail();
            String pass2 = randomAlt.getPassword();
            if(randomAlt.isBanned()) {
                return;
            }
            GuiAltManager.INSTANCE.currentAccount = randomAlt;
            try {
                (GuiAltManager.INSTANCE.loginThread = new AccountLoginThread(user2, pass2)).start();
                Client.INSTANCE.getAccountManager().save();
                if (serverData != null) mc.displayGuiScreen(new GuiConnecting(new GuiMainMenu(), mc, serverData));
            } catch (Exception e) {
            }
        }
        if (button.id == 6) {
            if (Client.INSTANCE.getAccountManager().getAlteningKey() == null) return;
            niggaButton = true;
            try {
                TheAltening theAltening = new TheAltening(Client.INSTANCE.getAccountManager().getAlteningKey());
                AlteningAlt account = theAltening.generateAccount(theAltening.getUser());
                if (!Objects.requireNonNull(account).getToken().isEmpty()) {
                    Client.INSTANCE.getAccountManager().setAlteningKey(Client.INSTANCE.getAccountManager().getAlteningKey());
                    Client.INSTANCE.getAccountManager().setLastAlteningAlt(Objects.requireNonNull(account).getToken());
                    GuiAlteningLogin.thread = new AccountLoginThread(Objects.requireNonNull(account).getToken().replaceAll(" ", ""), "nig");
                    GuiAlteningLogin.thread.start();
                    Client.INSTANCE.getAccountManager().save();
                }
                if (serverData != null) mc.displayGuiScreen(new GuiConnecting(new GuiMainMenu(), mc, serverData));
                } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (button.id == 7) {
            if (serverData != null) mc.displayGuiScreen(new GuiConnecting(new GuiMainMenu(), mc, serverData));
        }
    }

    /**
     * Draws the screen and all the components in it. Args : mouseX, mouseY, renderPartialTicks
     */
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, this.reason, this.width / 2, this.height / 2 - this.field_175353_i / 2 - this.fontRendererObj.FONT_HEIGHT * 2, 11184810);
        int var4 = this.height / 2 - this.field_175353_i / 2;

        if (this.multilineMessage != null)
        {
            for (Iterator var5 = this.multilineMessage.iterator(); var5.hasNext(); var4 += this.fontRendererObj.FONT_HEIGHT)
            {
                String var6 = (String)var5.next();

                this.drawCenteredString(this.fontRendererObj, var6, this.width / 2, var4, 16777215);
//                if(GuiAccountManager.currentAlt != null) {
//                    drawCenteredString(fontRendererObj,"Current Alt: " + mc.getSession().getUsername(), width /2, 20,-1);
//
//                }
            }
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

}
