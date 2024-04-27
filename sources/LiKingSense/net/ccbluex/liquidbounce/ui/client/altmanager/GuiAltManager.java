/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParser
 *  com.thealtening.AltService
 *  com.thealtening.AltService$EnumAltService
 *  net.minecraft.client.gui.GuiScreen
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
import net.ccbluex.liquidbounce.utils.NotifyUtils;
import net.ccbluex.liquidbounce.utils.login.LoginUtils;
import net.ccbluex.liquidbounce.utils.login.MinecraftAccount;
import net.ccbluex.liquidbounce.utils.misc.HttpUtils;
import net.ccbluex.liquidbounce.utils.misc.MiscUtils;
import net.minecraft.client.gui.GuiScreen;
import ui.GuiMainMenu;

public class GuiAltManager
extends WrappedGuiScreen {
    public static final AltService altService = new AltService();
    private static final Map<String, Boolean> GENERATORS = new HashMap<String, Boolean>();
    public String status = "\u00a77Idle...";
    private IGuiButton loginButton;
    private IGuiButton randomButton;
    private GuiList altsList;
    private IGuiTextField searchField;

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
            return "\u00a7cYour name is now \u00a78" + minecraftAccount.getName() + "\u00a7c.";
        }
        LoginUtils.LoginResult result = LoginUtils.login(minecraftAccount.getName(), minecraftAccount.getPassword());
        if (result == LoginUtils.LoginResult.LOGGED) {
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

    /*
     * Exception decompiling
     */
    @Override
    public void initGui() {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl121 : INVOKEINTERFACE - null : Stack underflow
         *     at org.benf.cfr.reader.bytecode.analysis.stack.StackSim.getChange(StackSim.java:81)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:242)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:1559)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:434)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    /*
     * Exception decompiling
     */
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Invalid stack depths @ lbl97 : ALOAD_0 - null : trying to set 0 previously set to 3
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:207)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:1559)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:434)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    @Override
    public void actionPerformed(IGuiButton button) throws IOException {
        if (!button.getEnabled()) {
            return;
        }
        switch (button.getId()) {
            case 0: {
                mc2.func_147108_a((GuiScreen)new GuiMainMenu());
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
                    Thread thread = new Thread(() -> {
                        MinecraftAccount minecraftAccount = (MinecraftAccount)this.altsList.accounts.get(this.altsList.getSelectedSlot());
                        this.status = "\u00a7aLogging in...";
                        this.status = GuiAltManager.login(minecraftAccount);
                        this.loginButton.setEnabled(true);
                        this.randomButton.setEnabled(true);
                    }, "AltLogin");
                    thread.start();
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
                Thread thread = new Thread(() -> {
                    MinecraftAccount minecraftAccount = (MinecraftAccount)this.altsList.accounts.get(randomInteger);
                    this.status = "\u00a7aLogging in...";
                    this.status = GuiAltManager.login(minecraftAccount);
                    this.loginButton.setEnabled(true);
                    this.randomButton.setEnabled(true);
                }, "AltLogin");
                thread.start();
                break;
            }
            case 5: {
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
                    NotifyUtils.notice("AltManager", "Exported successfully!");
                    break;
                }
                catch (Exception e) {
                    e.printStackTrace();
                    MiscUtils.showErrorPopup("Error", "Exception class: " + e.getClass().getName() + "\nMessage: " + e.getMessage());
                }
            }
        }
    }

    /*
     * Exception decompiling
     */
    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl39 : INVOKEVIRTUAL - null : Stack underflow
         *     at org.benf.cfr.reader.bytecode.analysis.stack.StackSim.getChange(StackSim.java:81)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:242)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:1559)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:434)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
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
            Fonts.posterama40.drawCenteredString(minecraftAccount.getAccountName() == null ? minecraftAccount.getName() : minecraftAccount.getAccountName(), GuiAltManager.this.representedScreen.getWidth() / 2, y + 2, Color.WHITE.getRGB(), true);
            Fonts.posterama40.drawCenteredString(minecraftAccount.isCracked() ? "Cracked" : (minecraftAccount.getAccountName() == null ? "Premium" : minecraftAccount.getName()), GuiAltManager.this.representedScreen.getWidth() / 2, y + 15, minecraftAccount.isCracked() ? Color.GRAY.getRGB() : (minecraftAccount.getAccountName() == null ? Color.GREEN.getRGB() : Color.LIGHT_GRAY.getRGB()), true);
        }

        @Override
        public void drawBackground() {
        }
    }
}

