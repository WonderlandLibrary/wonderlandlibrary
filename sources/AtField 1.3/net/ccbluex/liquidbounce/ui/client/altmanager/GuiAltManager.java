/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParser
 *  com.thealtening.AltService
 *  com.thealtening.AltService$EnumAltService
 */
package net.ccbluex.liquidbounce.ui.client.altmanager;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.thealtening.AltService;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.swing.JOptionPane;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IGuiButton;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IGuiScreen;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IGuiTextField;
import net.ccbluex.liquidbounce.api.util.WrappedGuiScreen;
import net.ccbluex.liquidbounce.api.util.WrappedGuiSlot;
import net.ccbluex.liquidbounce.ui.client.altmanager.sub.GuiAdd;
import net.ccbluex.liquidbounce.ui.client.altmanager.sub.GuiChangeName;
import net.ccbluex.liquidbounce.ui.client.altmanager.sub.GuiDirectLogin;
import net.ccbluex.liquidbounce.ui.client.altmanager.sub.GuiDonatorCape;
import net.ccbluex.liquidbounce.ui.client.altmanager.sub.altgenerator.GuiTheAltening;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.login.LoginUtils;
import net.ccbluex.liquidbounce.utils.login.MinecraftAccount;
import net.ccbluex.liquidbounce.utils.login.UserUtils;
import net.ccbluex.liquidbounce.utils.misc.HttpUtils;
import net.ccbluex.liquidbounce.utils.misc.MiscUtils;

public class GuiAltManager
extends WrappedGuiScreen {
    private final IGuiScreen prevGui;
    public String status = "\u00a77Idle...";
    private GuiList altsList;
    private IGuiButton loginButton;
    private IGuiTextField searchField;
    public static final AltService altService = new AltService();
    private static final Map GENERATORS = new HashMap();
    private IGuiButton randomButton;

    @Override
    public void initGui() {
        int n;
        int n2 = Math.max(this.representedScreen.getWidth() / 8, 70);
        this.searchField = classProvider.createGuiTextField(2, Fonts.roboto40, this.representedScreen.getWidth() - n2 - 10, 10, n2, 20);
        this.searchField.setMaxStringLength(Integer.MAX_VALUE);
        this.altsList = new GuiList(this, this.representedScreen);
        this.altsList.represented.registerScrollButtons(7, 8);
        int n3 = -1;
        for (n = 0; n < LiquidBounce.fileManager.accountsConfig.getAccounts().size(); ++n) {
            MinecraftAccount minecraftAccount = (MinecraftAccount)LiquidBounce.fileManager.accountsConfig.getAccounts().get(n);
            if (minecraftAccount == null || (minecraftAccount.getPassword() != null && !minecraftAccount.getPassword().isEmpty() || minecraftAccount.getName() == null || !minecraftAccount.getName().equals(mc.getSession().getUsername())) && (minecraftAccount.getAccountName() == null || !minecraftAccount.getAccountName().equals(mc.getSession().getUsername()))) continue;
            n3 = n;
            break;
        }
        this.altsList.elementClicked(n3, false, 0, 0);
        this.altsList.represented.scrollBy(n3 * this.altsList.represented.getSlotHeight());
        n = 22;
        this.representedScreen.getButtonList().add(classProvider.createGuiButton(1, this.representedScreen.getWidth() - 80, n + 24, 70, 20, "Add"));
        this.representedScreen.getButtonList().add(classProvider.createGuiButton(2, this.representedScreen.getWidth() - 80, n + 48, 70, 20, "Remove"));
        this.representedScreen.getButtonList().add(classProvider.createGuiButton(7, this.representedScreen.getWidth() - 80, n + 72, 70, 20, "Import"));
        this.representedScreen.getButtonList().add(classProvider.createGuiButton(12, this.representedScreen.getWidth() - 80, n + 96, 70, 20, "Export"));
        this.representedScreen.getButtonList().add(classProvider.createGuiButton(8, this.representedScreen.getWidth() - 80, n + 120, 70, 20, "Copy"));
        this.representedScreen.getButtonList().add(classProvider.createGuiButton(0, this.representedScreen.getWidth() - 80, this.representedScreen.getHeight() - 65, 70, 20, "Back"));
        this.loginButton = classProvider.createGuiButton(3, 5, n + 24, 90, 20, "Login");
        this.representedScreen.getButtonList().add(this.loginButton);
        this.randomButton = classProvider.createGuiButton(4, 5, n + 48, 90, 20, "Random");
        this.representedScreen.getButtonList().add(this.randomButton);
        this.representedScreen.getButtonList().add(classProvider.createGuiButton(6, 5, n + 72, 90, 20, "Direct Login"));
        this.representedScreen.getButtonList().add(classProvider.createGuiButton(88, 5, n + 96, 90, 20, "Change Name"));
        if (GENERATORS.getOrDefault("thealtening", true).booleanValue()) {
            this.representedScreen.getButtonList().add(classProvider.createGuiButton(9, 5, n + 144 + 5, 90, 20, "TheAltening"));
        }
        this.representedScreen.getButtonList().add(classProvider.createGuiButton(11, 5, n + 192 + 10, 90, 20, "Cape"));
    }

    @Override
    public void actionPerformed(IGuiButton iGuiButton) throws IOException {
        if (!iGuiButton.getEnabled()) {
            return;
        }
        switch (iGuiButton.getId()) {
            case 0: {
                mc.displayGuiScreen(this.prevGui);
                break;
            }
            case 1: {
                mc.displayGuiScreen(classProvider.wrapGuiScreen(new GuiAdd(this)));
                break;
            }
            case 2: {
                if (this.altsList.getSelectedSlot() != -1 && this.altsList.getSelectedSlot() < this.altsList.getSize()) {
                    LiquidBounce.fileManager.accountsConfig.removeAccount((MinecraftAccount)GuiList.access$000(this.altsList).get(this.altsList.getSelectedSlot()));
                    LiquidBounce.fileManager.saveConfig(LiquidBounce.fileManager.accountsConfig);
                    this.status = "\u00a7aThe account has been removed.";
                    GuiList.access$100(this.altsList, this.searchField.getText());
                    break;
                }
                this.status = "\u00a7cSelect an account.";
                break;
            }
            case 3: {
                if (this.altsList.getSelectedSlot() != -1 && this.altsList.getSelectedSlot() < this.altsList.getSize()) {
                    this.loginButton.setEnabled(false);
                    this.randomButton.setEnabled(false);
                    Thread thread = new Thread(this::lambda$actionPerformed$1, "AltLogin");
                    thread.start();
                    break;
                }
                this.status = "\u00a7cSelect an account.";
                break;
            }
            case 4: {
                if (GuiList.access$000(this.altsList).size() <= 0) {
                    this.status = "\u00a7cThe list is empty.";
                    return;
                }
                int n = new Random().nextInt(GuiList.access$000(this.altsList).size());
                if (n < this.altsList.getSize()) {
                    GuiList.access$202(this.altsList, n);
                }
                this.loginButton.setEnabled(false);
                this.randomButton.setEnabled(false);
                Thread thread = new Thread(() -> this.lambda$actionPerformed$2(n), "AltLogin");
                thread.start();
                break;
            }
            case 6: {
                mc.displayGuiScreen(classProvider.wrapGuiScreen(new GuiDirectLogin(this)));
                break;
            }
            case 7: {
                String string;
                File file = MiscUtils.openFileChooser();
                if (file == null) {
                    return;
                }
                FileReader fileReader = new FileReader(file);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                while ((string = bufferedReader.readLine()) != null) {
                    String[] stringArray = string.split(":", 2);
                    if (LiquidBounce.fileManager.accountsConfig.accountExists(stringArray[0])) continue;
                    if (stringArray.length > 1) {
                        LiquidBounce.fileManager.accountsConfig.addAccount(stringArray[0], stringArray[1]);
                        continue;
                    }
                    LiquidBounce.fileManager.accountsConfig.addAccount(stringArray[0]);
                }
                fileReader.close();
                bufferedReader.close();
                GuiList.access$100(this.altsList, this.searchField.getText());
                LiquidBounce.fileManager.saveConfig(LiquidBounce.fileManager.accountsConfig);
                this.status = "\u00a7aThe accounts were imported successfully.";
                break;
            }
            case 8: {
                if (this.altsList.getSelectedSlot() != -1 && this.altsList.getSelectedSlot() < this.altsList.getSize()) {
                    MinecraftAccount minecraftAccount = (MinecraftAccount)GuiList.access$000(this.altsList).get(this.altsList.getSelectedSlot());
                    if (minecraftAccount == null) break;
                    Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(minecraftAccount.getName() + ":" + minecraftAccount.getPassword()), null);
                    this.status = "\u00a7aCopied account into your clipboard.";
                    break;
                }
                this.status = "\u00a7cSelect an account.";
                break;
            }
            case 88: {
                mc.displayGuiScreen(classProvider.wrapGuiScreen(new GuiChangeName(this)));
                break;
            }
            case 9: {
                mc.displayGuiScreen(classProvider.wrapGuiScreen(new GuiTheAltening(this)));
                break;
            }
            case 11: {
                mc.displayGuiScreen(classProvider.wrapGuiScreen(new GuiDonatorCape(this)));
                break;
            }
            case 12: {
                if (LiquidBounce.fileManager.accountsConfig.getAccounts().size() == 0) {
                    this.status = "\u00a7cThe list is empty.";
                    return;
                }
                File file = MiscUtils.saveFileChooser();
                if (file == null || file.isDirectory()) {
                    return;
                }
                try {
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                    FileWriter fileWriter = new FileWriter(file);
                    for (MinecraftAccount minecraftAccount : LiquidBounce.fileManager.accountsConfig.getAccounts()) {
                        if (minecraftAccount.isCracked()) {
                            fileWriter.write(minecraftAccount.getName() + "\r\n");
                            continue;
                        }
                        fileWriter.write(minecraftAccount.getName() + ":" + minecraftAccount.getPassword() + "\r\n");
                    }
                    fileWriter.flush();
                    fileWriter.close();
                    JOptionPane.showMessageDialog(null, "Exported successfully!", "AltManager", 1);
                    break;
                }
                catch (Exception exception) {
                    exception.printStackTrace();
                    MiscUtils.showErrorPopup("Error", "Exception class: " + exception.getClass().getName() + "\nMessage: " + exception.getMessage());
                }
            }
        }
    }

    public static void loadGenerators() {
        try {
            JsonElement jsonElement = new JsonParser().parse(HttpUtils.get("https://cloud.liquidbounce.net/LiquidBounce/generators.json"));
            if (jsonElement.isJsonObject()) {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                jsonObject.entrySet().forEach(GuiAltManager::lambda$loadGenerators$0);
            }
        }
        catch (Throwable throwable) {
            ClientUtils.getLogger().error("Failed to load enabled generators.", throwable);
        }
    }

    static IGuiButton access$400(GuiAltManager guiAltManager) {
        return guiAltManager.loginButton;
    }

    static IGuiButton access$500(GuiAltManager guiAltManager) {
        return guiAltManager.randomButton;
    }

    static GuiList access$300(GuiAltManager guiAltManager) {
        return guiAltManager.altsList;
    }

    @Override
    public void handleMouseInput() throws IOException {
        this.representedScreen.superHandleMouseInput();
        this.altsList.represented.handleMouseInput();
    }

    @Override
    public void updateScreen() {
        this.searchField.updateCursorCounter();
    }

    private void lambda$actionPerformed$2(int n) {
        MinecraftAccount minecraftAccount = (MinecraftAccount)GuiList.access$000(this.altsList).get(n);
        this.status = "\u00a7aLogging in...";
        this.status = GuiAltManager.login(minecraftAccount);
        this.loginButton.setEnabled(true);
        this.randomButton.setEnabled(true);
    }

    public static String login(MinecraftAccount minecraftAccount) {
        if (minecraftAccount == null) {
            return "";
        }
        if (altService.getCurrentService() != AltService.EnumAltService.MOJANG) {
            try {
                altService.switchService(AltService.EnumAltService.MOJANG);
            }
            catch (IllegalAccessException | NoSuchFieldException reflectiveOperationException) {
                ClientUtils.getLogger().error("Something went wrong while trying to switch alt service.", (Throwable)reflectiveOperationException);
            }
        }
        if (minecraftAccount.isCracked()) {
            LoginUtils.loginCracked(minecraftAccount.getName());
            return "\u00a7cYour name is now \u00a78" + minecraftAccount.getName() + "\u00a7c.";
        }
        LoginUtils.LoginResult loginResult = LoginUtils.login(minecraftAccount.getName(), minecraftAccount.getPassword());
        if (loginResult == LoginUtils.LoginResult.LOGGED) {
            String string = mc.getSession().getUsername();
            minecraftAccount.setAccountName(string);
            LiquidBounce.fileManager.saveConfig(LiquidBounce.fileManager.accountsConfig);
            return "\u00a7cYour name is now \u00a7f\u00a7l" + string + "\u00a7c.";
        }
        if (loginResult == LoginUtils.LoginResult.WRONG_PASSWORD) {
            return "\u00a7cWrong password.";
        }
        if (loginResult == LoginUtils.LoginResult.NO_CONTACT) {
            return "\u00a7cCannot contact authentication server.";
        }
        if (loginResult == LoginUtils.LoginResult.INVALID_ACCOUNT_DATA) {
            return "\u00a7cInvalid username or password.";
        }
        if (loginResult == LoginUtils.LoginResult.MIGRATED) {
            return "\u00a7cAccount migrated.";
        }
        return "";
    }

    @Override
    public void drawScreen(int n, int n2, float f) {
        this.representedScreen.drawBackground(0);
        this.altsList.represented.drawScreen(n, n2, f);
        Fonts.roboto40.drawCenteredString("AltManager", (float)this.representedScreen.getWidth() / 2.0f, 6.0f, 0xFFFFFF);
        Fonts.roboto35.drawCenteredString(this.searchField.getText().isEmpty() ? LiquidBounce.fileManager.accountsConfig.getAccounts().size() + " Alts" : GuiList.access$000(this.altsList).size() + " Search Results", (float)this.representedScreen.getWidth() / 2.0f, 18.0f, 0xFFFFFF);
        Fonts.roboto35.drawCenteredString(this.status, (float)this.representedScreen.getWidth() / 2.0f, 32.0f, 0xFFFFFF);
        Fonts.roboto35.drawStringWithShadow("\u00a77User: \u00a7a" + mc.getSession().getUsername(), 6, 6, 0xFFFFFF);
        Fonts.roboto35.drawStringWithShadow("\u00a77Type: \u00a7a" + (altService.getCurrentService() == AltService.EnumAltService.THEALTENING ? "TheAltening" : (UserUtils.INSTANCE.isValidTokenOffline(mc.getSession().getToken()) ? "Premium" : "Cracked")), 6, 15, 0xFFFFFF);
        this.searchField.drawTextBox();
        if (this.searchField.getText().isEmpty() && !this.searchField.isFocused()) {
            Fonts.roboto40.drawStringWithShadow("\u00a77Search...", this.searchField.getXPosition() + 4, 17, 0xFFFFFF);
        }
        super.drawScreen(n, n2, f);
    }

    @Override
    public void keyTyped(char c, int n) throws IOException {
        if (this.searchField.isFocused()) {
            this.searchField.textboxKeyTyped(c, n);
            GuiList.access$100(this.altsList, this.searchField.getText());
        }
        switch (n) {
            case 1: {
                mc.displayGuiScreen(this.prevGui);
                return;
            }
            case 200: {
                int n2 = this.altsList.getSelectedSlot() - 1;
                if (n2 < 0) {
                    n2 = 0;
                }
                this.altsList.elementClicked(n2, false, 0, 0);
                break;
            }
            case 208: {
                int n3 = this.altsList.getSelectedSlot() + 1;
                if (n3 >= this.altsList.getSize()) {
                    n3 = this.altsList.getSize() - 1;
                }
                this.altsList.elementClicked(n3, false, 0, 0);
                break;
            }
            case 28: {
                this.altsList.elementClicked(this.altsList.getSelectedSlot(), true, 0, 0);
                break;
            }
            case 209: {
                this.altsList.represented.scrollBy(this.representedScreen.getHeight() - 100);
                break;
            }
            case 201: {
                this.altsList.represented.scrollBy(-this.representedScreen.getHeight() + 100);
                return;
            }
        }
        this.representedScreen.superKeyTyped(c, n);
    }

    public GuiAltManager(IGuiScreen iGuiScreen) {
        this.prevGui = iGuiScreen;
    }

    private void lambda$actionPerformed$1() {
        MinecraftAccount minecraftAccount = (MinecraftAccount)GuiList.access$000(this.altsList).get(this.altsList.getSelectedSlot());
        this.status = "\u00a7aLogging in...";
        this.status = GuiAltManager.login(minecraftAccount);
        this.loginButton.setEnabled(true);
        this.randomButton.setEnabled(true);
    }

    @Override
    public void mouseClicked(int n, int n2, int n3) throws IOException {
        this.searchField.mouseClicked(n, n2, n3);
        this.representedScreen.superMouseClicked(n, n2, n3);
    }

    private static void lambda$loadGenerators$0(Map.Entry entry) {
        GENERATORS.put(entry.getKey(), ((JsonElement)entry.getValue()).getAsBoolean());
    }

    private class GuiList
    extends WrappedGuiSlot {
        private int selectedSlot;
        final GuiAltManager this$0;
        private List accounts;

        static int access$202(GuiList guiList, int n) {
            guiList.selectedSlot = n;
            return guiList.selectedSlot;
        }

        @Override
        public int getSize() {
            return this.accounts.size();
        }

        static void access$100(GuiList guiList, String string) {
            guiList.updateAccounts(string);
        }

        GuiList(GuiAltManager guiAltManager, IGuiScreen iGuiScreen) {
            this.this$0 = guiAltManager;
            super(MinecraftInstance.mc, iGuiScreen.getWidth(), iGuiScreen.getHeight(), 40, iGuiScreen.getHeight() - 40, 30);
            this.updateAccounts(null);
        }

        @Override
        public void drawBackground() {
        }

        int getSelectedSlot() {
            if (this.selectedSlot > this.accounts.size()) {
                this.selectedSlot = -1;
            }
            return this.selectedSlot;
        }

        private void lambda$elementClicked$0() {
            MinecraftAccount minecraftAccount = (MinecraftAccount)this.accounts.get(GuiAltManager.access$300(this.this$0).getSelectedSlot());
            this.this$0.status = "\u00a7aLogging in...";
            this.this$0.status = "\u00a7c" + GuiAltManager.login(minecraftAccount);
            GuiAltManager.access$400(this.this$0).setEnabled(true);
            GuiAltManager.access$500(this.this$0).setEnabled(true);
        }

        @Override
        public void drawSlot(int n, int n2, int n3, int n4, int n5, int n6) {
            MinecraftAccount minecraftAccount = (MinecraftAccount)this.accounts.get(n);
            Fonts.roboto40.drawCenteredString(minecraftAccount.getAccountName() == null ? minecraftAccount.getName() : minecraftAccount.getAccountName(), this.this$0.representedScreen.getWidth() / 2, n3 + 2, Color.WHITE.getRGB(), true);
            Fonts.roboto40.drawCenteredString(minecraftAccount.isCracked() ? "Cracked" : (minecraftAccount.getAccountName() == null ? "Premium" : minecraftAccount.getName()), this.this$0.representedScreen.getWidth() / 2, n3 + 15, minecraftAccount.isCracked() ? Color.GRAY.getRGB() : (minecraftAccount.getAccountName() == null ? Color.GREEN.getRGB() : Color.LIGHT_GRAY.getRGB()), true);
        }

        static List access$000(GuiList guiList) {
            return guiList.accounts;
        }

        @Override
        public void elementClicked(int n, boolean bl, int n2, int n3) {
            this.selectedSlot = n;
            if (bl) {
                if (GuiAltManager.access$300(this.this$0).getSelectedSlot() != -1 && GuiAltManager.access$300(this.this$0).getSelectedSlot() < GuiAltManager.access$300(this.this$0).getSize() && GuiAltManager.access$400(this.this$0).getEnabled()) {
                    GuiAltManager.access$400(this.this$0).setEnabled(false);
                    GuiAltManager.access$500(this.this$0).setEnabled(false);
                    new Thread(this::lambda$elementClicked$0, "AltManagerLogin").start();
                } else {
                    this.this$0.status = "\u00a7cSelect an account.";
                }
            }
        }

        public void setSelectedSlot(int n) {
            this.selectedSlot = n;
        }

        private void updateAccounts(String string) {
            if (string == null || string.isEmpty()) {
                this.accounts = LiquidBounce.fileManager.accountsConfig.getAccounts();
                return;
            }
            string = string.toLowerCase();
            this.accounts = new ArrayList();
            for (MinecraftAccount minecraftAccount : LiquidBounce.fileManager.accountsConfig.getAccounts()) {
                if ((minecraftAccount.getName() == null || !minecraftAccount.getName().toLowerCase().contains(string)) && (minecraftAccount.getAccountName() == null || !minecraftAccount.getAccountName().toLowerCase().contains(string))) continue;
                this.accounts.add(minecraftAccount);
            }
        }

        @Override
        public boolean isSelected(int n) {
            return this.selectedSlot == n;
        }
    }
}

