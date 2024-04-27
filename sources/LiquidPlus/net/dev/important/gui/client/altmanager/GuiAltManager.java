/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParser
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.FontRenderer
 *  net.minecraft.client.gui.GuiButton
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.gui.GuiSlot
 *  net.minecraft.client.gui.GuiTextField
 */
package net.dev.important.gui.client.altmanager;

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
import javax.swing.JOptionPane;
import net.dev.important.Client;
import net.dev.important.gui.client.altmanager.sub.GuiAdd;
import net.dev.important.gui.client.altmanager.sub.GuiChangeName;
import net.dev.important.gui.client.altmanager.sub.GuiDirectLogin;
import net.dev.important.gui.client.altmanager.sub.GuiSessionLogin;
import net.dev.important.gui.client.altmanager.sub.altgenerator.GuiTheAltening;
import net.dev.important.gui.font.Fonts;
import net.dev.important.utils.ClientUtils;
import net.dev.important.utils.login.LoginUtils;
import net.dev.important.utils.login.MinecraftAccount;
import net.dev.important.utils.login.UserUtils;
import net.dev.important.utils.misc.HttpUtils;
import net.dev.important.utils.misc.MiscUtils;
import net.dev.important.utils.misc.RandomUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.gui.GuiTextField;

public class GuiAltManager
extends GuiScreen {
    public static final AltService altService = new AltService();
    private static final Map<String, Boolean> GENERATORS = new HashMap<String, Boolean>();
    private final GuiScreen prevGui;
    public String status = "\u00a77Idle...";
    private GuiButton loginButton;
    private GuiButton randomButton;
    private GuiList altsList;
    private GuiTextField searchField;

    public GuiAltManager(GuiScreen prevGui) {
        this.prevGui = prevGui;
    }

    public static void loadGenerators() {
        try {
            JsonElement jsonElement = new JsonParser().parse(HttpUtils.get("https://wysi-foundation.github.io/LiquidCloud/LiquidBounce/generators.json"));
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
            return "\u00a7cYour name is now \u00a78" + minecraftAccount.getName() + "\u00a7c.";
        }
        LoginUtils.LoginResult result = LoginUtils.login(minecraftAccount.getName(), minecraftAccount.getPassword());
        if (result == LoginUtils.LoginResult.LOGGED) {
            String userName = Minecraft.func_71410_x().func_110432_I().func_111285_a();
            minecraftAccount.setAccountName(userName);
            Client.fileManager.saveConfig(Client.fileManager.accountsConfig);
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

    public void func_73866_w_() {
        int textFieldWidth = Math.max(this.field_146294_l / 8, 70);
        this.searchField = new GuiTextField(2, (FontRenderer)Fonts.font40, this.field_146294_l - textFieldWidth - 10, 10, textFieldWidth, 20);
        this.searchField.func_146203_f(Integer.MAX_VALUE);
        this.altsList = new GuiList(this);
        this.altsList.func_148134_d(7, 8);
        int index = -1;
        for (int i = 0; i < Client.fileManager.accountsConfig.getAccounts().size(); ++i) {
            MinecraftAccount minecraftAccount = Client.fileManager.accountsConfig.getAccounts().get(i);
            if (minecraftAccount == null || (minecraftAccount.getPassword() != null && !minecraftAccount.getPassword().isEmpty() || minecraftAccount.getName() == null || !minecraftAccount.getName().equals(this.field_146297_k.field_71449_j.func_111285_a())) && (minecraftAccount.getAccountName() == null || !minecraftAccount.getAccountName().equals(this.field_146297_k.field_71449_j.func_111285_a()))) continue;
            index = i;
            break;
        }
        this.altsList.func_148144_a(index, false, 0, 0);
        this.altsList.func_148145_f(index * this.altsList.field_148149_f);
        int j = 22;
        this.field_146292_n.add(new GuiButton(1, this.field_146294_l - 80, j + 24, 70, 20, "Add"));
        this.field_146292_n.add(new GuiButton(2, this.field_146294_l - 80, j + 48, 70, 20, "Remove"));
        this.field_146292_n.add(new GuiButton(7, this.field_146294_l - 80, j + 72, 70, 20, "Import"));
        this.field_146292_n.add(new GuiButton(12, this.field_146294_l - 80, j + 96, 70, 20, "Export"));
        this.field_146292_n.add(new GuiButton(8, this.field_146294_l - 80, j + 120, 70, 20, "Copy"));
        this.field_146292_n.add(new GuiButton(0, this.field_146294_l - 80, this.field_146295_m - 65, 70, 20, "Back"));
        this.loginButton = new GuiButton(3, 5, j + 24, 90, 20, "Login");
        this.field_146292_n.add(this.loginButton);
        this.randomButton = new GuiButton(4, 5, j + 48, 90, 20, "Cracked");
        this.field_146292_n.add(this.randomButton);
        this.field_146292_n.add(new GuiButton(6, 5, j + 72, 90, 20, "Direct Login"));
        this.field_146292_n.add(new GuiButton(88, 5, j + 96, 90, 20, "Change Name"));
        if (GENERATORS.getOrDefault("thealtening", true).booleanValue()) {
            this.field_146292_n.add(new GuiButton(9, 5, j + 144 + 5, 90, 20, "TheAltening"));
        }
        this.field_146292_n.add(new GuiButton(10, 5, j + 168 + 5, 90, 20, "Session Login"));
    }

    public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
        this.func_146278_c(0);
        this.altsList.func_148128_a(mouseX, mouseY, partialTicks);
        Fonts.font40.drawCenteredString("Alt Manager", (float)this.field_146294_l / 2.0f, 6.0f, 0xFFFFFF);
        Fonts.font35.drawCenteredString(this.searchField.func_146179_b().isEmpty() ? Client.fileManager.accountsConfig.getAccounts().size() + " Alts" : this.altsList.accounts.size() + " Search Results", (float)this.field_146294_l / 2.0f, 18.0f, 0xFFFFFF);
        Fonts.font35.drawCenteredString(this.status, (float)this.field_146294_l / 2.0f, 32.0f, 0xFFFFFF);
        Fonts.font35.func_175063_a("\u00a77User: \u00a7a" + this.field_146297_k.func_110432_I().func_111285_a(), 6.0f, 6.0f, 0xFFFFFF);
        Fonts.font35.func_175063_a("\u00a77Type: \u00a7a" + (altService.getCurrentService() == AltService.EnumAltService.THEALTENING ? "TheAltening" : (UserUtils.INSTANCE.isValidTokenOffline(this.field_146297_k.func_110432_I().func_148254_d()) ? "Premium" : "Cracked")), 6.0f, 15.0f, 0xFFFFFF);
        this.searchField.func_146194_f();
        if (this.searchField.func_146179_b().isEmpty() && !this.searchField.func_146206_l()) {
            this.func_73731_b(Fonts.font40, "\u00a77Search...", this.searchField.field_146209_f + 4, 17, 0xFFFFFF);
        }
        super.func_73863_a(mouseX, mouseY, partialTicks);
    }

    protected void func_146284_a(GuiButton button) throws IOException {
        if (!button.field_146124_l) {
            return;
        }
        switch (button.field_146127_k) {
            case 0: {
                this.field_146297_k.func_147108_a(this.prevGui);
                break;
            }
            case 1: {
                this.field_146297_k.func_147108_a((GuiScreen)new GuiAdd(this));
                break;
            }
            case 2: {
                if (this.altsList.getSelectedSlot() != -1 && this.altsList.getSelectedSlot() < this.altsList.func_148127_b()) {
                    Client.fileManager.accountsConfig.removeAccount((MinecraftAccount)this.altsList.accounts.get(this.altsList.getSelectedSlot()));
                    Client.fileManager.saveConfig(Client.fileManager.accountsConfig);
                    this.status = "\u00a7aThe account has been removed.";
                    this.altsList.updateAccounts(this.searchField.func_146179_b());
                    break;
                }
                this.status = "\u00a7cSelect an account.";
                break;
            }
            case 3: {
                if (this.altsList.getSelectedSlot() != -1 && this.altsList.getSelectedSlot() < this.altsList.func_148127_b()) {
                    this.randomButton.field_146124_l = false;
                    this.loginButton.field_146124_l = false;
                    Thread thread2 = new Thread(() -> {
                        MinecraftAccount minecraftAccount = (MinecraftAccount)this.altsList.accounts.get(this.altsList.getSelectedSlot());
                        this.status = "\u00a7aLogging in...";
                        this.status = GuiAltManager.login(minecraftAccount);
                        this.randomButton.field_146124_l = true;
                        this.loginButton.field_146124_l = true;
                    }, "AltLogin");
                    thread2.start();
                    break;
                }
                this.status = "\u00a7cSelect an account.";
                break;
            }
            case 4: {
                this.randomButton.field_146124_l = false;
                this.loginButton.field_146124_l = false;
                new Thread(() -> {
                    this.status = "\u00a76Trying to generate an account...";
                    LoginUtils.loginCracked(RandomUtils.randomString(RandomUtils.nextInt(5, 16)));
                    this.status = "\u00a7aDone!";
                    this.randomButton.field_146124_l = true;
                    this.loginButton.field_146124_l = true;
                }, "RandomCracked").start();
                break;
            }
            case 6: {
                this.field_146297_k.func_147108_a((GuiScreen)new GuiDirectLogin(this));
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
                    if (Client.fileManager.accountsConfig.accountExists(accountData[0])) continue;
                    if (accountData.length > 1) {
                        Client.fileManager.accountsConfig.addAccount(accountData[0], accountData[1]);
                        continue;
                    }
                    Client.fileManager.accountsConfig.addAccount(accountData[0]);
                }
                fileReader.close();
                bufferedReader.close();
                this.altsList.updateAccounts(this.searchField.func_146179_b());
                Client.fileManager.saveConfig(Client.fileManager.accountsConfig);
                this.status = "\u00a7aThe accounts were imported successfully.";
                break;
            }
            case 8: {
                if (this.altsList.getSelectedSlot() != -1 && this.altsList.getSelectedSlot() < this.altsList.func_148127_b()) {
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
                this.field_146297_k.func_147108_a((GuiScreen)new GuiChangeName(this));
                break;
            }
            case 9: {
                this.field_146297_k.func_147108_a((GuiScreen)new GuiTheAltening(this));
                break;
            }
            case 10: {
                this.field_146297_k.func_147108_a((GuiScreen)new GuiSessionLogin(this));
                break;
            }
            case 12: {
                if (Client.fileManager.accountsConfig.getAccounts().size() == 0) {
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
                    for (MinecraftAccount account : Client.fileManager.accountsConfig.getAccounts()) {
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

    protected void func_73869_a(char typedChar, int keyCode) throws IOException {
        if (this.searchField.func_146206_l()) {
            this.searchField.func_146201_a(typedChar, keyCode);
            this.altsList.updateAccounts(this.searchField.func_146179_b());
        }
        switch (keyCode) {
            case 1: {
                this.field_146297_k.func_147108_a(this.prevGui);
                return;
            }
            case 200: {
                int i = this.altsList.getSelectedSlot() - 1;
                if (i < 0) {
                    i = 0;
                }
                this.altsList.func_148144_a(i, false, 0, 0);
                break;
            }
            case 208: {
                int i = this.altsList.getSelectedSlot() + 1;
                if (i >= this.altsList.func_148127_b()) {
                    i = this.altsList.func_148127_b() - 1;
                }
                this.altsList.func_148144_a(i, false, 0, 0);
                break;
            }
            case 28: {
                this.altsList.func_148144_a(this.altsList.getSelectedSlot(), true, 0, 0);
                break;
            }
            case 209: {
                this.altsList.func_148145_f(this.field_146295_m - 100);
                break;
            }
            case 201: {
                this.altsList.func_148145_f(-this.field_146295_m + 100);
                return;
            }
        }
        super.func_73869_a(typedChar, keyCode);
    }

    public void func_146274_d() throws IOException {
        super.func_146274_d();
        this.altsList.func_178039_p();
    }

    protected void func_73864_a(int mouseX, int mouseY, int mouseButton) throws IOException {
        this.searchField.func_146192_a(mouseX, mouseY, mouseButton);
        super.func_73864_a(mouseX, mouseY, mouseButton);
    }

    public void func_73876_c() {
        this.searchField.func_146178_a();
    }

    private class GuiList
    extends GuiSlot {
        private List<MinecraftAccount> accounts;
        private int selectedSlot;

        GuiList(GuiScreen prevGui) {
            super(GuiAltManager.this.field_146297_k, prevGui.field_146294_l, prevGui.field_146295_m, 40, prevGui.field_146295_m - 40, 30);
            this.updateAccounts(null);
        }

        private void updateAccounts(String search) {
            if (search == null || search.isEmpty()) {
                this.accounts = Client.fileManager.accountsConfig.getAccounts();
                return;
            }
            search = search.toLowerCase();
            this.accounts = new ArrayList<MinecraftAccount>();
            for (MinecraftAccount account : Client.fileManager.accountsConfig.getAccounts()) {
                if ((account.getName() == null || !account.getName().toLowerCase().contains(search)) && (account.getAccountName() == null || !account.getAccountName().toLowerCase().contains(search))) continue;
                this.accounts.add(account);
            }
        }

        protected boolean func_148131_a(int id) {
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

        protected int func_148127_b() {
            return this.accounts.size();
        }

        protected void func_148144_a(int var1, boolean doubleClick, int var3, int var4) {
            this.selectedSlot = var1;
            if (doubleClick) {
                if (GuiAltManager.this.altsList.getSelectedSlot() != -1 && GuiAltManager.this.altsList.getSelectedSlot() < GuiAltManager.this.altsList.func_148127_b() && ((GuiAltManager)GuiAltManager.this).loginButton.field_146124_l) {
                    ((GuiAltManager)GuiAltManager.this).randomButton.field_146124_l = false;
                    ((GuiAltManager)GuiAltManager.this).loginButton.field_146124_l = false;
                    new Thread(() -> {
                        MinecraftAccount minecraftAccount = this.accounts.get(GuiAltManager.this.altsList.getSelectedSlot());
                        GuiAltManager.this.status = "\u00a7aLogging in...";
                        GuiAltManager.this.status = "\u00a7c" + GuiAltManager.login(minecraftAccount);
                        ((GuiAltManager)GuiAltManager.this).randomButton.field_146124_l = true;
                        ((GuiAltManager)GuiAltManager.this).loginButton.field_146124_l = true;
                    }, "AltManagerLogin").start();
                } else {
                    GuiAltManager.this.status = "\u00a7cSelect an account.";
                }
            }
        }

        protected void func_180791_a(int id, int x, int y, int var4, int var5, int var6) {
            MinecraftAccount minecraftAccount = this.accounts.get(id);
            Fonts.font40.drawCenteredString(minecraftAccount.getAccountName() == null ? minecraftAccount.getName() : minecraftAccount.getAccountName(), this.field_148155_a / 2, y + 2, Color.WHITE.getRGB(), true);
            Fonts.font40.drawCenteredString(minecraftAccount.isCracked() ? "Cracked" : (minecraftAccount.getAccountName() == null ? "Premium" : minecraftAccount.getName()), this.field_148155_a / 2, y + 15, minecraftAccount.isCracked() ? Color.GRAY.getRGB() : (minecraftAccount.getAccountName() == null ? Color.GREEN.getRGB() : Color.LIGHT_GRAY.getRGB()), true);
        }

        protected void func_148123_a() {
        }
    }
}

