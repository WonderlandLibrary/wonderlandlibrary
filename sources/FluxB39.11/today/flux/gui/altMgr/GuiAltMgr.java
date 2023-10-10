package today.flux.gui.altMgr;

import com.thealtening.auth.TheAlteningAuthentication;
import com.thealtening.auth.service.AlteningServiceType;
import me.yarukon.oauth.OAuthService;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Session;
import org.lwjgl.input.Keyboard;
import today.flux.gui.AbstractGuiScreen;
import today.flux.gui.altMgr.dialog.DialogWindow;
import today.flux.gui.altMgr.dialog.impl.AltInputDialog;
import today.flux.gui.altMgr.dialog.impl.ConfirmDialog;
import today.flux.gui.altMgr.dialog.impl.InfoDialog;
import today.flux.gui.altMgr.dialog.impl.InputDialog;
import today.flux.gui.altMgr.kingAlts.AltJson;
import today.flux.gui.altMgr.kingAlts.KingAlts;
import today.flux.gui.altMgr.kingAlts.ProfileJson;
import today.flux.gui.clickgui.classic.RenderUtil;
import today.flux.gui.fontRenderer.FontManager;
import today.flux.irc.IRCClient;
import today.flux.utility.ColorUtils;
import today.flux.utility.LoginUtils;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class GuiAltMgr extends AbstractGuiScreen {
    public GuiScreen parent;

    public AltScrollList list;
    public static CopyOnWriteArrayList<Alt> alts = new CopyOnWriteArrayList<>();
    public ArrayList<AltButton> cButtons = new ArrayList<>();
    public Alt selectedAlt = null;

    public String apiString = "King Alts: API Key not set";

    public static int premiumAlts = 0;
    public static int crackedAlts = 0;

    public DialogWindow popupDialog;

    public static OAuthService oAuthService = new OAuthService();

    public GuiAltMgr(GuiScreen parent) {
        super(2);
        this.parent = parent;
    }

    @Override
    public void doInit() {
        curWidth = mc.displayWidth / scale;
        curHeight = mc.displayHeight / scale;

        //alts.clear();
        this.cButtons.clear();

        cButtons.add(new AltButton(0, curWidth / 2 - 120, curHeight - 52, 57, 20, "Use"));
        cButtons.add(new AltButton(1, curWidth / 2 - (120 / 2f), curHeight - 52, 117, 20, "Direct Login"));
        cButtons.add(new AltButton(2, curWidth / 2 - 120, curHeight - 28, 57, 20, "Star"));
        cButtons.add(new AltButton(3, curWidth / 2 - 60, curHeight - 28, 57, 20, "Add"));
        cButtons.add(new AltButton(4, curWidth / 2, curHeight - 28, 57, 20, "Edit"));
        cButtons.add(new AltButton(5, curWidth / 2 + 60, curHeight - 52, 57, 20, "Delete"));
        cButtons.add(new AltButton(6, curWidth / 2 + 60, curHeight - 28, 57, 20, "Back"));

        cButtons.add(new AltButton(7, 5, 5, 110, 20, "Import Alts"));
        cButtons.add(new AltButton(8, curWidth - 85, 5, 80, 20, "Clear All"));

        cButtons.add(new AltButton(9, curWidth - 85 - 85, 5, 80, 20, "King Alts"));

        if (KingAlts.API_KEY.length() > 3) {
            cButtons.add(new AltButton(10, 5, 30, 110, 20, "Add Alt by King Alts"));
        }

        if (IRCClient.isChina) {
            cButtons.add(new AltButton(12, curWidth - 55, curHeight - 28, 50, 20, "Click me"));
        }

        cButtons.add(new AltButton(13, curWidth / 2 - 200, curHeight - 28, 77, 20, "Microsoft Login"));

        list = new AltScrollList(this, (alt) -> selectedAlt = alt, (alt) -> this.doClickButton(this.cButtons.get(0)));

        new Thread("King Alts") {
            @Override
            public void run() {
                if (KingAlts.API_KEY != null && KingAlts.API_KEY.length() > 3) {
                    apiString = "King Alts Profile Loading...";
                    ProfileJson json = KingAlts.getProfile();
                    if (json.getMessage() != null) {
                        apiString = "\247cERROR: " + json.getMessage();
                    } else {
                        apiString = "You have generated " + json.getGeneratedToday() + " alt" + (json.getGeneratedToday() > 1 ? "s" : "") + " today.";
                    }
                }
            }
        }.start();
    }

    @Override
    public void drawScr(int mouseX, int mouseY, float partialTicks) {

        if (popupDialog != null && popupDialog.destroy) {
            popupDialog = null;
        }

        this.drawDefaultBackground();
        RenderUtil.drawRect(0, 0, width, height, 0xdd36393f);

        FontManager.sans16.drawCenteredString("Current name: \247a" + Minecraft.getMinecraft().getSession().getUsername() + EnumChatFormatting.WHITE + " (" + (mc.getSession().getSessionType() == Session.Type.LEGACY ? "Cracked" : "Premium") + ")", curWidth / 2f, 5, 0xffffffff);
        FontManager.sans16.drawCenteredString("Premium: " + premiumAlts + ", Cracked: " + crackedAlts, curWidth / 2f, 15f, 0xffffffff);
        FontManager.sans16.drawCenteredString(apiString, curWidth / 2f, 25, 0xffffffff);

        if (IRCClient.isChina) {
            FontManager.wqy18.drawString("高质量黑卡", width - 50, this.height - 50, ColorUtils.WHITE.c);
            FontManager.wqy18.drawString("kw.baoziwl.com", width - 62, this.height - 40, ColorUtils.WHITE.c);
        }

        for (AltButton button : cButtons) {
            button.drawButton(mouseX, mouseY);
        }

        if (this.list != null) {
            this.list.doDraw(curWidth / 2f - 120, 40, 237, curHeight - 100, mouseX, mouseY);
        }

        if (popupDialog != null) {
            RenderUtil.drawRect(0, 0, curWidth, curHeight, 0x88000000);
            popupDialog.draw(mouseX, mouseY);
        }

        super.drawScr(mouseX, mouseY, partialTicks);
    }

    public void updateScreen() {
        cButtons.get(0).isEnabled = !alts.isEmpty() && selectedAlt != null;
        cButtons.get(2).isEnabled = !alts.isEmpty() && selectedAlt != null;
        cButtons.get(4).isEnabled = !alts.isEmpty() && selectedAlt != null;
        cButtons.get(5).isEnabled = !alts.isEmpty() && selectedAlt != null;

        if (cButtons.get(2).isEnabled) {
            if (selectedAlt != null) {
                cButtons.get(2).displayString = selectedAlt.isStarred() ? "Unstar" : "Star";
            }
        } else {
            cButtons.get(2).displayString = "Star";
        }

        if (selectedAlt != null) {
            if (!alts.contains(selectedAlt)) {
                selectedAlt = null;
            }
        }

        if (popupDialog != null) {
            popupDialog.updateScreen();
        }
    }

    @Override
    public void handleMouseInput() throws IOException {
        if (popupDialog == null) {
            list.handleMouseInput();
        }

        super.handleMouseInput();
    }

    @Override
    public void mouseClick(int mouseX, int mouseY, int mouseButton) {

        if (popupDialog == null) {
            for (AltButton button : this.cButtons) {
                if (button.isHovered() && button.isEnabled) {
                    button.playPressSound(this.mc.getSoundHandler());
                    this.doClickButton(button);
                }
            }

            list.onClick(mouseX, mouseY, mouseButton);
        } else {
            popupDialog.mouseClick(mouseX, mouseY, mouseButton);
        }

        super.mouseClick(mouseX, mouseY, mouseButton);
    }

    @Override
    public void mouseRelease(int mouseX, int mouseY, int state) {
        super.mouseRelease(mouseX, mouseY, state);
    }

    public void doClickButton(AltButton button) {
        switch (button.buttonID) {
            case 0:
                new Thread(() -> {
                    apiString = "Logging in...";
                    if (selectedAlt.getEmail().contains("@alt.com")) {
                        TheAlteningAuthentication.theAltening().updateService(AlteningServiceType.THEALTENING);
                        String reply = LoginUtils.login(selectedAlt.getEmail(), "THEALTENING");
                        apiString = "\247c" + reply;
                        if (reply == null) {
                            apiString = "\247aLogged as " + mc.getSession().getUsername();
                            selectedAlt.setChecked(mc.getSession().getUsername());
                        }
                        return;
                    }

                    if (selectedAlt.isCracked()) {// Cracked
                        LoginUtils.changeCrackedName(selectedAlt.getEmail());
                        apiString = "\247aLogged as " + mc.getSession().getUsername();
                    } else {// Premium
                        String reply = LoginUtils.login(selectedAlt.getEmail(), selectedAlt.getPassword());
                        apiString = "\247c" + reply;
                        if (reply == null) {
                            apiString = "\247aLogged as " + mc.getSession().getUsername();
                            selectedAlt.setChecked(mc.getSession().getUsername());
                        }
                    }
                }).start();
                break;

            case 1:
                popupDialog = new DirectLoginDialog();
                popupDialog.makeCenter(curWidth, curHeight);
                break;

            case 2:
                Alt alt = alts.get(list.getSelectedIndex());
                alt.setStarred(!alt.isStarred());
                GuiAltMgr.sortAlts();
                break;

            case 3:
                popupDialog = new AddAltDialog();
                popupDialog.makeCenter(curWidth, curHeight);
                break;

            case 4:
                popupDialog = new EditAltDialog(this.selectedAlt);
                popupDialog.makeCenter(curWidth, curHeight);
                break;

            case 5:
                popupDialog = new DeleteAltDialog(this.selectedAlt);
                popupDialog.makeCenter(curWidth, curHeight);
                break;

            case 6:
                mc.displayGuiScreen(parent);
                break;

            case 7:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        JFileChooser fileChooser = new JFileChooser() {
                            @Override
                            protected JDialog createDialog(Component parent) throws HeadlessException {
                                JDialog dialog = super.createDialog(parent);
                                dialog.setAlwaysOnTop(true);
                                return dialog;
                            }
                        };
                        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                        fileChooser.setAcceptAllFileFilterUsed(false);
                        fileChooser.addChoosableFileFilter(
                                new FileNameExtensionFilter("Username:Password format (TXT)", "txt"));
                        int action = fileChooser.showOpenDialog(null);
                        if (action == JFileChooser.APPROVE_OPTION)
                            try {
                                File file = fileChooser.getSelectedFile();
                                BufferedReader load = new BufferedReader(new FileReader(file));
                                for (String line = ""; (line = load.readLine()) != null; ) {
                                    String[] data = line.split(":");
                                    GuiAltMgr.alts.add(new Alt(data[0], data[1], (data.length == 3) ? data[2] : null));
                                }
                                load.close();
                                GuiAltMgr.sortAlts();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                    }
                }).start();
                break;

            case 8:
                popupDialog = new ClearAllDialog();
                popupDialog.makeCenter(curWidth, curHeight);
                break;

            case 9:
                popupDialog = new KingAltInputDialog();
                popupDialog.makeCenter(curWidth, curHeight);
                break;

            case 10:
                new Thread(() -> {
                    apiString = "Generating Alt...";
                    AltJson json = KingAlts.getAlt();
                    if (json.getMessage() != null) {
                        apiString = "\247cERROR: " + json.getMessage();
                    } else {
                        GuiAltMgr.alts.add(0, new Alt(json.getEmail(), json.getPassword(), null));
                        GuiAltMgr.sortAlts();

                        ProfileJson profile = KingAlts.getProfile();
                        if (profile.getMessage() != null) {
                            apiString = "\247cERROR: " + profile.getMessage();
                        } else {
                            apiString = "You have generated " + profile.getGeneratedToday() + " alt" + (profile.getGeneratedToday() > 1 ? "s" : "") + " today.";
                        }
                    }
                }).start();
                break;

            case 12:
                openUrl("https://kw.baoziwl.com");
                break;

            case 13:
                popupDialog = new MicrosoftLoginDialog();
                popupDialog.makeCenter(curWidth, curHeight);
                new Thread(() -> {
                    oAuthService.authWithNoRefreshToken();
                }).start();
                break;
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {

        if (popupDialog != null) {
            popupDialog.keyTyped(typedChar, keyCode);
        }

        if (keyCode == Keyboard.KEY_ESCAPE && popupDialog == null) {
            mc.displayGuiScreen(parent);
        }

        super.keyTyped(typedChar, keyCode);
    }

    public static void sortAlts() {
        CopyOnWriteArrayList<Alt> newAlts = new CopyOnWriteArrayList<>();

        premiumAlts = 0;
        crackedAlts = 0;
        for (Alt value : alts)
            if (value.isStarred())
                newAlts.add(value);

        for (Alt alt : alts)
            if (!alt.isCracked() && !alt.isStarred())
                newAlts.add(alt);

        for (Alt alt : alts)
            if (alt.isCracked() && !alt.isStarred())
                newAlts.add(alt);

        for (int i = 0; i < newAlts.size(); i++)
            for (int i2 = 0; i2 < newAlts.size(); i2++)
                if (i != i2
                        && newAlts.get(i).getEmail()
                        .equals(newAlts.get(i2).getEmail())
                        && newAlts.get(i).isCracked() == newAlts.get(i2)
                        .isCracked())
                    newAlts.remove(i2);

        for (Alt newAlt : newAlts)
            if (newAlt.isCracked())
                crackedAlts++;
            else
                premiumAlts++;

        alts = newAlts;
    }

    public void openUrl(String url) {
        try {
            Desktop desktop = Desktop.getDesktop();
            desktop.browse(new URI(url));
        } catch (Exception ex) {
            System.out.println("An error occurred while trying to open a url!");
        }
    }

    public class AddAltDialog extends AltInputDialog {
        public String displayText = null;

        public AddAltDialog() {
            super(0, 0, 200, 80, "Add an alt", Minecraft.getMinecraft().getSession().getUsername(), "Username / Email:Password");
        }

        @Override
        public void acceptAction() {
            new Thread(() -> {
                if (cef.getText().contains("@alt.com")) {
                    TheAlteningAuthentication.theAltening().updateService(AlteningServiceType.THEALTENING);
                    displayText = LoginUtils.login(cef.getText().split(":")[0], "Flux");
                    if (displayText == null) {
                        alts.add(0, new Alt(cef.getText().split(":")[0], "Flux", mc.getSession().getUsername()));
                        GuiAltMgr.sortAlts();

                        //Destroy the dialog window
                        destroy();
                    } else {
                        title = "[ERR TheAltening]" + displayText;
                    }

                    return;
                }

                if (cef.getText().split(":").length <= 1) {
                    // Cracked
                    alts.add(0, new Alt(cef.getText(), null, null));
                    displayText = null;
                } else {
                    // Premium
                    TheAlteningAuthentication.theAltening().updateService(AlteningServiceType.MOJANG);
                    String email = cef.getText().split(":")[0];
                    String password = cef.getText().split(":")[1];

                    displayText = LoginUtils.login(email, password);
                    if (displayText == null) {
                        alts.add(0, new Alt(email, password, mc.getSession().getUsername()));
                    } else {
                        title = "[ERR] " + displayText;
                    }
                }

                if (displayText == null) {
                    GuiAltMgr.sortAlts();
                    destroy();
                }
            }).start();
        }
    }

    public class EditAltDialog extends AltInputDialog {
        public Alt targetAlt;
        String displayText;

        public EditAltDialog(Alt targetAlt) {
            super(0, 0, 200, 80, "Edit this alt", targetAlt.isCracked() ? targetAlt.getNameOrEmail() : targetAlt.getEmail() + ":" + targetAlt.getPassword(), "Username / Email:Password");
            this.targetAlt = targetAlt;
            this.acceptButtonText = "Save";
        }

        @Override
        public void acceptAction() {
            // Save
            if (cef.getText().split(":").length <= 1) {
                // Cracked
                GuiAltMgr.alts.set(GuiAltMgr.alts.indexOf(targetAlt), new Alt(cef.getText(), null, null, targetAlt.isStarred()));
                displayText = null;
            } else {
                // Premium
                String email = cef.getText().split(":")[0];
                String password = cef.getText().split(":")[1];

                displayText = LoginUtils.login(email, password);
                if (displayText == null) {
                    GuiAltMgr.alts.set(GuiAltMgr.alts.indexOf(targetAlt), new Alt(email, password, mc.getSession().getUsername(), targetAlt.isStarred()));
                } else {
                    title = "[ERR] " + displayText;
                }
            }

            if (displayText == null) {
                GuiAltMgr.sortAlts();
                destroy();
            }
        }
    }

    public class DeleteAltDialog extends ConfirmDialog {
        public Alt targetAlt;

        public DeleteAltDialog(Alt targetAlt) {
            super(0, 0, 200, 60, "Are you sure to delete this alt?", "\"" + targetAlt.getNameOrEmail() + "\" will be lost forever!");
            float stringWid = FontManager.sans16_2.getStringWidth(message) + 20;
            width = stringWid < 140 ? 140 : stringWid;
            this.targetAlt = targetAlt;
        }

        @Override
        public void acceptAction() {
            alts.remove(targetAlt);
            GuiAltMgr.sortAlts();

            super.acceptAction();
        }
    }

    public class ClearAllDialog extends ConfirmDialog {
        public ClearAllDialog() {
            super(0, 0, 200, 60, "Are you sure you want to remove EVERY alt?", EnumChatFormatting.RED + "All alt" + EnumChatFormatting.WHITE + " will be lost forever!");
        }

        @Override
        public void acceptAction() {
            GuiAltMgr.alts.clear();
            GuiAltMgr.sortAlts();
            super.acceptAction();
        }
    }

    public class DirectLoginDialog extends AltInputDialog {

        String displayText;

        public DirectLoginDialog() {
            super(0, 0, 200, 80, "Direct login", Minecraft.getMinecraft().getSession().getUsername(), "Username / Email:Password");
        }

        @Override
        public void acceptAction() {
            new Thread(() -> {
                title = "Logging in...";
                if (cef.getText().contains("@alt.com")) {
                    TheAlteningAuthentication.theAltening().updateService(AlteningServiceType.THEALTENING);
                    displayText = LoginUtils.login(cef.getText().split(":")[0], "Flux");
                    if (displayText != null) {
                        title = "[ERR TheAltening] " + displayText;
                    } else {
                        destroy();
                    }
                    return;
                }

                if (cef.getText().split(":").length <= 1) {
                    LoginUtils.changeCrackedName(cef.getText());
                    displayText = null;
                } else {
                    TheAlteningAuthentication.mojang().updateService(AlteningServiceType.MOJANG);
                    String email = cef.getText().split(":")[0];
                    String password = cef.getText().split(":")[1];
                    displayText = LoginUtils.login(email, password);
                    if (displayText != null) {
                        title = "[ERR] " + displayText;
                    }
                }

                if (displayText == null)
                    destroy();

            }).start();
        }
    }

    public class KingAltInputDialog extends InputDialog {
        public KingAltInputDialog() {
            super(0, 0, 200, 80, "King Alts", KingAlts.API_KEY, "API Key");
            this.acceptButtonText = "Save API Key";
        }

        @Override
        public void acceptAction() {
            KingAlts.setApiKey(dtf.getText());
            super.acceptAction();
        }
    }

    public class MicrosoftLoginDialog extends InfoDialog {
        public MicrosoftLoginDialog() {
            super(0, 0, 200, 80, "Microsoft Login", "Unknown State!");
        }

        @Override
        public void draw(int mouseX, int mouseY) {
            super.draw(mouseX, mouseY);
            message = oAuthService.message;
            if(message.contains("Successfully logged in with account")) {
                this.buttonText = "OK";
            } else {
                this.buttonText = "Cancel";
            }
        }
    }
}
