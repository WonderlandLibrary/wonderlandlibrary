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
import net.ccbluex.liquidbounce.ui.client.altmanager.sub.GuiSessionLogin;
import net.ccbluex.liquidbounce.ui.client.altmanager.sub.altgenerator.GuiMCLeaks;
import net.ccbluex.liquidbounce.ui.client.altmanager.sub.altgenerator.GuiTheAltening;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.login.LoginUtils;
import net.ccbluex.liquidbounce.utils.login.MinecraftAccount;
import net.ccbluex.liquidbounce.utils.login.UserUtils;
import net.ccbluex.liquidbounce.utils.misc.HttpUtils;
import net.ccbluex.liquidbounce.utils.misc.MiscUtils;
import net.mcleaks.MCLeaks;

public class GuiAltManager
extends WrappedGuiScreen {
    public static final AltService altService = new AltService();
    private static final Map<String, Boolean> GENERATORS = new HashMap<String, Boolean>();
    private final IGuiScreen prevGui;
    public String status = "\u00a77Idle...";
    private IGuiButton loginButton;
    private IGuiButton randomButton;
    private GuiList altsList;
    private IGuiTextField searchField;

    public GuiAltManager(IGuiScreen prevGui) {
        this.prevGui = prevGui;
    }

    public static void loadGenerators() {
        try {
            JsonElement jsonElement = new JsonParser().parse(HttpUtils.get("https://cloud.liquidbounce.net/LiquidBounce/generators.json"));
            if (jsonElement.isJsonObject()) {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                jsonObject.entrySet().forEach(stringJsonElementEntry -> GENERATORS.put((String)stringJsonElementEntry.getKey(), ((JsonElement)stringJsonElementEntry.getValue()).getAsBoolean()));
            }
        }
        catch (Throwable throwable) {
            ClientUtils.getLogger().error("Failed to load enabled generators.", throwable);
        }
    }

    public static String login(MinecraftAccount minecraftAccount) {
        if (minecraftAccount == null) {
            return "";
        }
        if (altService.getCurrentService() != AltService.EnumAltService.MOJANG) {
            try {
                altService.switchService(AltService.EnumAltService.MOJANG);
            }
            catch (IllegalAccessException | NoSuchFieldException e) {
                ClientUtils.getLogger().error("Something went wrong while trying to switch alt service.", (Throwable)e);
            }
        }
        if (minecraftAccount.isCracked()) {
            LoginUtils.loginCracked(minecraftAccount.getName());
            MCLeaks.remove();
            return "\u00a7cYour name is now \u00a78" + minecraftAccount.getName() + "\u00a7c.";
        }
        LoginUtils.LoginResult result = LoginUtils.login(minecraftAccount.getName(), minecraftAccount.getPassword());
        if (result == LoginUtils.LoginResult.LOGGED) {
            MCLeaks.remove();
            String userName = mc.getSession().getUsername();
            minecraftAccount.setAccountName(userName);
            LiquidBounce.fileManager.saveConfig(LiquidBounce.fileManager.accountsConfig);
            return "\u00a7cYour name is now \u00a7f\u00a7l" + userName + "\u00a7c.";
        }
        if (result == LoginUtils.LoginResult.WRONG_PASSWORD) {
            return "\u00a7cWrong password.";
        }
        if (result == LoginUtils.LoginResult.NO_CONTACT) {
            return "\u00a7cCannot contact authentication server.";
        }
        if (result == LoginUtils.LoginResult.INVALID_ACCOUNT_DATA) {
            return "\u00a7cInvalid username or password.";
        }
        if (result == LoginUtils.LoginResult.MIGRATED) {
            return "\u00a7cAccount migrated.";
        }
        return "";
    }

    @Override
    public void initGui() {
        int textFieldWidth = Math.max(this.representedScreen.getWidth() / 8, 70);
        this.searchField = classProvider.createGuiTextField(2, Fonts.font40, this.representedScreen.getWidth() - textFieldWidth - 10, 10, textFieldWidth, 20);
        this.searchField.setMaxStringLength(Integer.MAX_VALUE);
        this.altsList = new GuiList(this.representedScreen);
        this.altsList.represented.registerScrollButtons(7, 8);
        int index = -1;
        for (int i = 0; i < LiquidBounce.fileManager.accountsConfig.getAccounts().size(); ++i) {
            MinecraftAccount minecraftAccount = LiquidBounce.fileManager.accountsConfig.getAccounts().get(i);
            if (minecraftAccount == null || (minecraftAccount.getPassword() != null && !minecraftAccount.getPassword().isEmpty() || minecraftAccount.getName() == null || !minecraftAccount.getName().equals(mc.getSession().getUsername())) && (minecraftAccount.getAccountName() == null || !minecraftAccount.getAccountName().equals(mc.getSession().getUsername()))) continue;
            index = i;
            break;
        }
        this.altsList.elementClicked(index, false, 0, 0);
        this.altsList.represented.scrollBy(index * this.altsList.represented.getSlotHeight());
        int j = 22;
        this.representedScreen.getButtonList().add(classProvider.createGuiButton(1, this.representedScreen.getWidth() - 80, j + 24, 70, 20, "Add"));
        this.representedScreen.getButtonList().add(classProvider.createGuiButton(2, this.representedScreen.getWidth() - 80, j + 48, 70, 20, "Remove"));
        this.representedScreen.getButtonList().add(classProvider.createGuiButton(7, this.representedScreen.getWidth() - 80, j + 72, 70, 20, "Import"));
        this.representedScreen.getButtonList().add(classProvider.createGuiButton(12, this.representedScreen.getWidth() - 80, j + 96, 70, 20, "Export"));
        this.representedScreen.getButtonList().add(classProvider.createGuiButton(8, this.representedScreen.getWidth() - 80, j + 120, 70, 20, "Copy"));
        this.representedScreen.getButtonList().add(classProvider.createGuiButton(0, this.representedScreen.getWidth() - 80, this.representedScreen.getHeight() - 65, 70, 20, "Back"));
        this.loginButton = classProvider.createGuiButton(3, 5, j + 24, 90, 20, "Login");
        this.representedScreen.getButtonList().add(this.loginButton);
        this.randomButton = classProvider.createGuiButton(4, 5, j + 48, 90, 20, "Random");
        this.representedScreen.getButtonList().add(this.randomButton);
        this.representedScreen.getButtonList().add(classProvider.createGuiButton(6, 5, j + 72, 90, 20, "Direct Login"));
        this.representedScreen.getButtonList().add(classProvider.createGuiButton(88, 5, j + 96, 90, 20, "Change Name"));
        if (GENERATORS.getOrDefault("mcleaks", true).booleanValue()) {
            this.representedScreen.getButtonList().add(classProvider.createGuiButton(5, 5, j + 120 + 5, 90, 20, "MCLeaks"));
        }
        if (GENERATORS.getOrDefault("thealtening", true).booleanValue()) {
            this.representedScreen.getButtonList().add(classProvider.createGuiButton(9, 5, j + 144 + 5, 90, 20, "TheAltening"));
        }
        this.representedScreen.getButtonList().add(classProvider.createGuiButton(10, 5, j + 168 + 5, 90, 20, "Session Login"));
        this.representedScreen.getButtonList().add(classProvider.createGuiButton(11, 5, j + 192 + 10, 90, 20, "Cape"));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.representedScreen.drawBackground(0);
        this.altsList.represented.drawScreen(mouseX, mouseY, partialTicks);
        Fonts.font40.drawCenteredString("AltManager", (float)this.representedScreen.getWidth() / 2.0f, 6.0f, 0xFFFFFF);
        Fonts.font35.drawCenteredString(this.searchField.getText().isEmpty() ? LiquidBounce.fileManager.accountsConfig.getAccounts().size() + " Alts" : this.altsList.accounts.size() + " Search Results", (float)this.representedScreen.getWidth() / 2.0f, 18.0f, 0xFFFFFF);
        Fonts.font35.drawCenteredString(this.status, (float)this.representedScreen.getWidth() / 2.0f, 32.0f, 0xFFFFFF);
        Fonts.font35.drawStringWithShadow("\u00a77User: \u00a7a" + (MCLeaks.isAltActive() ? MCLeaks.getSession().getUsername() : mc.getSession().getUsername()), 6, 6, 0xFFFFFF);
        Fonts.font35.drawStringWithShadow("\u00a77Type: \u00a7a" + (altService.getCurrentService() == AltService.EnumAltService.THEALTENING ? "TheAltening" : (MCLeaks.isAltActive() ? "MCLeaks" : (UserUtils.INSTANCE.isValidTokenOffline(mc.getSession().getToken()) ? "Premium" : "Cracked"))), 6, 15, 0xFFFFFF);
        this.searchField.drawTextBox();
        if (this.searchField.getText().isEmpty() && !this.searchField.isFocused()) {
            Fonts.font40.drawStringWithShadow("\u00a77Search...", this.searchField.getXPosition() + 4, 17, 0xFFFFFF);
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void actionPerformed(IGuiButton button) throws IOException {
        if (!button.getEnabled()) {
            return;
        }
        switch (button.getId()) {
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
                    LiquidBounce.fileManager.accountsConfig.removeAccount((MinecraftAccount)this.altsList.accounts.get(this.altsList.getSelectedSlot()));
                    LiquidBounce.fileManager.saveConfig(LiquidBounce.fileManager.accountsConfig);
                    this.status = "\u00a7aThe account has been removed.";
                    this.altsList.updateAccounts(this.searchField.getText());
                    break;
                }
                this.status = "\u00a7cSelect an account.";
                break;
            }
            case 3: {
                if (this.altsList.getSelectedSlot() != -1 && this.altsList.getSelectedSlot() < this.altsList.getSize()) {
                    this.loginButton.setEnabled(false);
                    this.randomButton.setEnabled(false);
                    Thread thread2 = new Thread(() -> {
                        MinecraftAccount minecraftAccount = (MinecraftAccount)this.altsList.accounts.get(this.altsList.getSelectedSlot());
                        this.status = "\u00a7aLogging in...";
                        this.status = GuiAltManager.login(minecraftAccount);
                        this.loginButton.setEnabled(true);
                        this.randomButton.setEnabled(true);
                    }, "AltLogin");
                    thread2.start();
                    break;
                }
                this.status = "\u00a7cSelect an account.";
                break;
            }
            case 4: {
                if (this.altsList.accounts.size() <= 0) {
                    this.status = "\u00a7cThe list is empty.";
                    return;
                }
                int randomInteger = new Random().nextInt(this.altsList.accounts.size());
                if (randomInteger < this.altsList.getSize()) {
                    this.altsList.selectedSlot = randomInteger;
                }
                this.loginButton.setEnabled(false);
                this.randomButton.setEnabled(false);
                Thread thread3 = new Thread(() -> {
                    MinecraftAccount minecraftAccount = (MinecraftAccount)this.altsList.accounts.get(randomInteger);
                    this.status = "\u00a7aLogging in...";
                    this.status = GuiAltManager.login(minecraftAccount);
                    this.loginButton.setEnabled(true);
                    this.randomButton.setEnabled(true);
                }, "AltLogin");
                thread3.start();
                break;
            }
            case 5: {
                mc.displayGuiScreen(classProvider.wrapGuiScreen(new GuiMCLeaks(this)));
                break;
            }
            case 6: {
                mc.displayGuiScreen(classProvider.wrapGuiScreen(new GuiDirectLogin(this)));
                break;
            }
            case 7: {
                String line;
                File file = MiscUtils.openFileChooser();
                if (file == null) {
                    return;
                }
                FileReader fileReader = new FileReader(file);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                while ((line = bufferedReader.readLine()) != null) {
                    String[] accountData = line.split(":", 2);
                    if (LiquidBounce.fileManager.accountsConfig.accountExists(accountData[0])) continue;
                    if (accountData.length > 1) {
                        LiquidBounce.fileManager.accountsConfig.addAccount(accountData[0], accountData[1]);
                        continue;
                    }
                    LiquidBounce.fileManager.accountsConfig.addAccount(accountData[0]);
                }
                fileReader.close();
                bufferedReader.close();
                this.altsList.updateAccounts(this.searchField.getText());
                LiquidBounce.fileManager.saveConfig(LiquidBounce.fileManager.accountsConfig);
                this.status = "\u00a7aThe accounts were imported successfully.";
                break;
            }
            case 8: {
                if (this.altsList.getSelectedSlot() != -1 && this.altsList.getSelectedSlot() < this.altsList.getSize()) {
                    MinecraftAccount minecraftAccount = (MinecraftAccount)this.altsList.accounts.get(this.altsList.getSelectedSlot());
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
            case 10: {
                mc.displayGuiScreen(classProvider.wrapGuiScreen(new GuiSessionLogin(this)));
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
                File selectedFile = MiscUtils.saveFileChooser();
                if (selectedFile == null || selectedFile.isDirectory()) {
                    return;
                }
                try {
                    if (!selectedFile.exists()) {
                        selectedFile.createNewFile();
                    }
                    FileWriter fileWriter = new FileWriter(selectedFile);
                    for (MinecraftAccount account : LiquidBounce.fileManager.accountsConfig.getAccounts()) {
                        if (account.isCracked()) {
                            fileWriter.write(account.getName() + "\r\n");
                            continue;
                        }
                        fileWriter.write(account.getName() + ":" + account.getPassword() + "\r\n");
                    }
                    fileWriter.flush();
                    fileWriter.close();
                    JOptionPane.showMessageDialog(null, "Exported successfully!", "AltManager", 1);
                    break;
                }
                catch (Exception e) {
                    e.printStackTrace();
                    MiscUtils.showErrorPopup("Error", "Exception class: " + e.getClass().getName() + "\nMessage: " + e.getMessage());
                }
            }
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {
        if (this.searchField.isFocused()) {
            this.searchField.textboxKeyTyped(typedChar, keyCode);
            this.altsList.updateAccounts(this.searchField.getText());
        }
        switch (keyCode) {
            case 1: {
                mc.displayGuiScreen(this.prevGui);
                return;
            }
            case 200: {
                int i = this.altsList.getSelectedSlot() - 1;
                if (i < 0) {
                    i = 0;
                }
                this.altsList.elementClicked(i, false, 0, 0);
                break;
            }
            case 208: {
                int i = this.altsList.getSelectedSlot() + 1;
                if (i >= this.altsList.getSize()) {
                    i = this.altsList.getSize() - 1;
                }
                this.altsList.elementClicked(i, false, 0, 0);
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
        this.representedScreen.superKeyTyped(typedChar, keyCode);
    }

    @Override
    public void handleMouseInput() throws IOException {
        this.representedScreen.superHandleMouseInput();
        this.altsList.represented.handleMouseInput();
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        this.searchField.mouseClicked(mouseX, mouseY, mouseButton);
        this.representedScreen.superMouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void updateScreen() {
        this.searchField.updateCursorCounter();
    }

    private class GuiList
    extends WrappedGuiSlot {
        private List<MinecraftAccount> accounts;
        private int selectedSlot;

        GuiList(IGuiScreen prevGui) {
            super(MinecraftInstance.mc, prevGui.getWidth(), prevGui.getHeight(), 40, prevGui.getHeight() - 40, 30);
            this.updateAccounts(null);
        }

        private void updateAccounts(String search) {
            if (search == null || search.isEmpty()) {
                this.accounts = LiquidBounce.fileManager.accountsConfig.getAccounts();
                return;
            }
            search = search.toLowerCase();
            this.accounts = new ArrayList<MinecraftAccount>();
            for (MinecraftAccount account : LiquidBounce.fileManager.accountsConfig.getAccounts()) {
                if ((account.getName() == null || !account.getName().toLowerCase().contains(search)) && (account.getAccountName() == null || !account.getAccountName().toLowerCase().contains(search))) continue;
                this.accounts.add(account);
            }
        }

        @Override
        public boolean isSelected(int id) {
            return this.selectedSlot == id;
        }

        int getSelectedSlot() {
            if (this.selectedSlot > this.accounts.size()) {
                this.selectedSlot = -1;
            }
            return this.selectedSlot;
        }

        public void setSelectedSlot(int selectedSlot) {
            this.selectedSlot = selectedSlot;
        }

        @Override
        public int getSize() {
            return this.accounts.size();
        }

        @Override
        public void elementClicked(int var1, boolean doubleClick, int var3, int var4) {
            this.selectedSlot = var1;
            if (doubleClick) {
                if (GuiAltManager.this.altsList.getSelectedSlot() != -1 && GuiAltManager.this.altsList.getSelectedSlot() < GuiAltManager.this.altsList.getSize() && GuiAltManager.this.loginButton.getEnabled()) {
                    GuiAltManager.this.loginButton.setEnabled(false);
                    GuiAltManager.this.randomButton.setEnabled(false);
                    new Thread(() -> {
                        MinecraftAccount minecraftAccount = this.accounts.get(GuiAltManager.this.altsList.getSelectedSlot());
                        GuiAltManager.this.status = "\u00a7aLogging in...";
                        GuiAltManager.this.status = "\u00a7c" + GuiAltManager.login(minecraftAccount);
                        GuiAltManager.this.loginButton.setEnabled(true);
                        GuiAltManager.this.randomButton.setEnabled(true);
                    }, "AltManagerLogin").start();
                } else {
                    GuiAltManager.this.status = "\u00a7cSelect an account.";
                }
            }
        }

        @Override
        public void drawSlot(int id, int x, int y, int var4, int var5, int var6) {
            MinecraftAccount minecraftAccount = this.accounts.get(id);
            Fonts.font40.drawCenteredString(minecraftAccount.getAccountName() == null ? minecraftAccount.getName() : minecraftAccount.getAccountName(), GuiAltManager.this.representedScreen.getWidth() / 2, y + 2, Color.WHITE.getRGB(), true);
            Fonts.font40.drawCenteredString(minecraftAccount.isCracked() ? "Cracked" : (minecraftAccount.getAccountName() == null ? "Premium" : minecraftAccount.getName()), GuiAltManager.this.representedScreen.getWidth() / 2, y + 15, minecraftAccount.isCracked() ? Color.GRAY.getRGB() : (minecraftAccount.getAccountName() == null ? Color.GREEN.getRGB() : Color.LIGHT_GRAY.getRGB()), true);
        }

        @Override
        public void drawBackground() {
        }
    }
}

