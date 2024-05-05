/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 */
package com.wallhacks.losebypass.manager;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.wallhacks.losebypass.LoseBypass;
import com.wallhacks.losebypass.gui.ClickGui;
import com.wallhacks.losebypass.gui.components.AltAccountComponent;
import com.wallhacks.losebypass.utils.EncryptionUtil;
import com.wallhacks.losebypass.utils.FakeWorld;
import com.wallhacks.losebypass.utils.FileUtil;
import com.wallhacks.losebypass.utils.GuiUtil;
import com.wallhacks.losebypass.utils.MC;
import com.wallhacks.losebypass.utils.MathUtil;
import com.wallhacks.losebypass.utils.RandomString;
import com.wallhacks.losebypass.utils.SessionUtils;
import com.wallhacks.losebypass.utils.Timer;
import com.wallhacks.losebypass.utils.auth.AuthException;
import com.wallhacks.losebypass.utils.auth.MSAuth;
import com.wallhacks.losebypass.utils.auth.Profile;
import com.wallhacks.losebypass.utils.auth.account.Account;
import com.wallhacks.losebypass.utils.auth.account.AccountType;
import com.wallhacks.losebypass.utils.auth.account.MSAccount;
import com.wallhacks.losebypass.utils.auth.account.TokenAccount;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Session;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.storage.WorldInfo;

public class AltManager
implements MC {
    public static FakeWorld fakeWorld;
    CopyOnWriteArrayList<Account> accounts;
    EntityOtherPlayerMP player;
    String key = new RandomString(50).nextString();
    double state = 0.0;
    Timer timout = new Timer().reset();
    boolean click = false;
    String status = "";
    Timer statusTimout = new Timer().reset();
    double statusState = 0.0;
    MSAuth msAuth;
    Profile tokenProfile;
    CopyOnWriteArrayList<AltAccountComponent> components = new CopyOnWriteArrayList();

    public AltManager() {
        WorldInfo info = new WorldInfo();
        info.setSpawn(new BlockPos(0, 0, 0));
        WorldProvider provider = new WorldProvider(){

            @Override
            public String getDimensionName() {
                return null;
            }

            @Override
            public String getInternalNameSuffix() {
                return null;
            }
        };
        fakeWorld = new FakeWorld(info, provider);
        provider.registerWorld(fakeWorld);
        NetworkPlayerInfo playerInfo = new NetworkPlayerInfo(mc.getSession().getProfile());
        this.player = new EntityOtherPlayerMP(fakeWorld, playerInfo.getGameProfile());
        this.player.playerInfo = playerInfo;
        this.player.playerInfo.setGameType(WorldSettings.GameType.CREATIVE);
        this.accounts = new CopyOnWriteArrayList();
        this.loadAlts();
        this.setList();
    }

    public void render(int mouseX, int mouseY, float deltaTime) {
        int height = new ScaledResolution(mc).getScaledHeight();
        int width = new ScaledResolution(mc).getScaledWidth();
        if (!this.player.getGameProfile().equals((Object)mc.getSession().getProfile())) {
            this.refreshSkin();
        }
        if ((double)mouseX > (double)width - (10.0 + 165.0 * this.state)) {
            this.state += (double)deltaTime * 0.01;
            this.timout.reset();
        } else if (this.timout.passedMs(1000L)) {
            this.state -= (double)deltaTime * 0.01;
        }
        this.statusState = !this.status.equals("") && !this.statusTimout.passedS(2.0) ? (this.statusState += (double)deltaTime * 0.01) : (this.statusState -= (double)deltaTime * 0.01);
        this.state = MathUtil.clamp(this.state, 0.0, 1.0);
        this.statusState = MathUtil.clamp(this.statusState, 0.0, 1.0);
        float offset = (int)(165.0 * this.state);
        float offsetStatus = (int)(15.0 * this.statusState);
        GuiUtil.drawRect((float)width - offset, 0.0f, width, height, ClickGui.background());
        int currentY = 27;
        for (AltAccountComponent alt : this.components) {
            GuiUtil.rounded((int)((float)width - offset + 5.0f), currentY, (int)((float)width - offset + 160.0f), currentY + 45, ClickGui.background4(), 3);
            GuiUtil.drawEntityOnScreen((int)((float)width - offset + 20.0f), currentY + 40, 19, mouseX, mouseY, alt.getPlayer());
            LoseBypass.fontManager.drawString(alt.getAccount().getName(), (int)((float)width - offset + 36.0f), currentY + 5, -1);
            LoseBypass.fontManager.drawString(alt.getAccount().getStatus(), (int)((float)width - offset + 36.0f), currentY + 15, -1);
            boolean log = GuiUtil.button((int)((float)width - offset + 125.0f), currentY + 5, (int)((float)width - offset + 155.0f), currentY + 20, mouseX, mouseY, "Login", alt.getAccount().loading);
            boolean del = GuiUtil.button((int)((float)width - offset + 125.0f), currentY + 25, (int)((float)width - offset + 155.0f), currentY + 40, mouseX, mouseY, "Delete", alt.getAccount().loading);
            if (this.click) {
                if (del) {
                    this.components.remove(alt);
                    this.removeAlt(alt.getAccount());
                } else if (log) {
                    alt.getAccount().login();
                }
            }
            currentY += 50;
        }
        LoseBypass.fontManager.getThickFont().drawString("Alt-Manager", (float)width - offset + 60.0f, 7.0f, -1);
        GuiUtil.drawRect((float)width - offset, 20.0f, width, 22.0f, ClickGui.mainColor());
        GuiUtil.drawRect((float)width - offset, (float)height - (63.0f + offsetStatus), width, height - 60, ClickGui.mainColor());
        GuiUtil.drawRect((float)width - offset, (float)height - (61.0f + offsetStatus), width, height - 60, ClickGui.background3());
        LoseBypass.fontManager.drawString(this.status, (int)((float)width - offset + 5.0f), (int)((float)height - (57.0f + offsetStatus)), -1);
        GuiUtil.drawRect((float)width - offset, height - 61, width, height, ClickGui.background2());
        GuiUtil.drawEntityOnScreen((int)((float)width - offset + 20.0f), height - 5, 27, mouseX, mouseY, this.player);
        String name = mc.getSession().getProfile().getName();
        boolean cracked = mc.getSession().getToken().equals("FML") || mc.getSession().getToken().equals("");
        LoseBypass.fontManager.drawString(name, (int)((float)width - offset + 40.0f), height - 55, -1);
        LoseBypass.fontManager.drawString(cracked ? ChatFormatting.RED + "Cracked" : ChatFormatting.GREEN + "Premium", (int)((float)width - offset + 40.0f), height - 45, -1);
        if (this.msAuth != null) {
            this.statusTimout.reset();
            if (this.msAuth.acc != null) {
                this.addAlt(this.msAuth.acc);
                this.status = "Logged in to " + this.msAuth.acc.getName();
                this.msAuth.acc.login();
                this.components.add(new AltAccountComponent(this.msAuth.acc));
                this.msAuth = null;
            } else if (this.msAuth.failed) {
                this.msAuth.stop();
                this.msAuth = null;
            } else {
                this.status = this.msAuth.getStatus();
            }
        }
        if (this.tokenProfile != null) {
            this.statusTimout.reset();
            if (this.tokenProfile.uuid != null && this.tokenProfile.name != null) {
                TokenAccount acc = new TokenAccount(this.tokenProfile.uuid, this.tokenProfile.token, this.tokenProfile.name);
                this.accounts.add(acc);
                this.components.add(new AltAccountComponent(acc));
                this.status = "Logged in to " + this.tokenProfile.name;
                this.tokenProfile = null;
            } else {
                this.status = "Validating token" + GuiUtil.getLoadingText(false);
            }
        }
        boolean msButton = GuiUtil.button((int)((float)width - offset + 110.0f), height - 38, (int)((float)width - offset + 160.0f), height - 23, mouseX, mouseY, this.msAuth == null ? "Add Alt" : "Cancel", this.tokenProfile != null);
        boolean addToken = GuiUtil.button((int)((float)width - offset + 103.0f), height - 18, (int)((float)width - offset + 160.0f), height - 3, mouseX, mouseY, "Paste Token", this.tokenProfile != null);
        boolean addMojang = GuiUtil.button((int)((float)width - offset + 110.0f), height - 58, (int)((float)width - offset + 160.0f), height - 43, mouseX, mouseY, "Add Cracked", this.tokenProfile != null);
        boolean copyToken = GuiUtil.button((int)((float)width - offset + 40.0f), height - 18, (int)((float)width - offset + 97.0f), height - 3, mouseX, mouseY, "Copy Token", cracked);
        if (this.click) {
            DataFlavor flavor;
            Clipboard clipboard;
            if (msButton) {
                if (this.msAuth == null) {
                    this.msAuth = new MSAuth();
                } else if (this.msAuth.stop()) {
                    this.msAuth = null;
                    this.status = "Cancelled!";
                } else {
                    this.status = "failed to stop server";
                }
            } else if (copyToken) {
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(mc.getSession().getToken()), null);
                this.status = "Copied token to clipboard!";
                this.statusTimout.reset();
            } else if (addToken && (clipboard = Toolkit.getDefaultToolkit().getSystemClipboard()).isDataFlavorAvailable(flavor = DataFlavor.stringFlavor)) {
                try {
                    String token = (String)clipboard.getData(flavor);
                    new Thread(() -> {
                        try {
                            this.tokenProfile = new Profile(token);
                            return;
                        }
                        catch (AuthException e) {
                            this.tokenProfile = null;
                            this.status = e.getText();
                            this.timout.reset();
                        }
                    }).start();
                }
                catch (Exception e) {
                    this.status = "Cant read token";
                    this.statusTimout.reset();
                }
            }
        }
        this.click = false;
    }

    String getAltFile(String name) {
        String base = LoseBypass.ParentPath.getAbsolutePath() + "" + System.getProperty("file.separator") + "alts" + System.getProperty("file.separator") + "";
        return base + name + ".acc";
    }

    public void setList() {
        Iterator<Account> iterator = this.getAlts().iterator();
        while (iterator.hasNext()) {
            Account a = iterator.next();
            this.components.add(new AltAccountComponent(a));
        }
    }

    public void setStatus(String status) {
        this.statusTimout.reset();
        this.status = status;
    }

    public void loadAlts() {
        File authKey = new File(LoseBypass.ParentPath.getAbsolutePath() + "" + System.getProperty("file.separator") + "alts" + System.getProperty("file.separator") + "auth.key");
        if (!authKey.exists()) return;
        this.key = FileUtil.read(authKey.getAbsolutePath());
        String[] stringArray = FileUtil.listFilesForFolder(LoseBypass.ParentPath.getAbsolutePath() + "" + System.getProperty("file.separator") + "alts", ".acc");
        int n = stringArray.length;
        int n2 = 0;
        while (n2 < n) {
            String file = stringArray[n2];
            try {
                FileInputStream fi_stream = new FileInputStream(LoseBypass.ParentPath.getAbsolutePath() + "" + System.getProperty("file.separator") + "alts" + System.getProperty("file.separator") + "" + file);
                DataInputStream di_stream = new DataInputStream(fi_stream);
                BufferedReader br = new BufferedReader(new InputStreamReader(di_stream));
                String backupName = file.substring(0, file.length() - 4);
                switch (br.readLine()) {
                    case "MICROSOFT": {
                        String uuid = br.readLine();
                        String refresh = br.readLine();
                        refresh = EncryptionUtil.decrypt(refresh, this.key);
                        uuid = EncryptionUtil.decrypt(uuid, this.key);
                        Account acc = new MSAccount(null, refresh, backupName, uuid);
                        ((MSAccount)acc).refresh();
                        this.addAlt(acc);
                        break;
                    }
                    case "TOKEN": {
                        String uuid = br.readLine();
                        String token = br.readLine();
                        token = EncryptionUtil.decrypt(token, this.key);
                        uuid = EncryptionUtil.decrypt(uuid, this.key);
                        Account acc = new TokenAccount(token, uuid, backupName);
                        this.addAlt(acc);
                        break;
                    }
                    case "CRACKED": {
                        this.addAlt(new Account(new Session(br.readLine(), "", "", "Mojang"), AccountType.CRACKED, ""));
                        break;
                    }
                }
            }
            catch (Exception fucked) {
                fucked.printStackTrace();
            }
            ++n2;
        }
    }

    public void mouseClick() {
        this.click = true;
    }

    private void refreshSkin() {
        NetworkPlayerInfo playerInfo = new NetworkPlayerInfo(mc.getSession().getProfile());
        SessionUtils.setSkin(playerInfo, playerInfo.getGameProfile().getId());
        this.player = new EntityOtherPlayerMP(fakeWorld, playerInfo.getGameProfile());
        this.player.playerInfo = playerInfo;
    }

    public void removeAlt(Account account) {
        this.accounts.remove(account);
        FileUtil.deleteFile(this.getAltFile(account.getName()));
    }

    public void addAlt(Account account) {
        this.accounts.add(account);
    }

    public CopyOnWriteArrayList<Account> getAlts() {
        return this.accounts;
    }

    public void saveAlts() {
        FileUtil.deleteDirectory(LoseBypass.ParentPath.getAbsolutePath() + "" + System.getProperty("file.separator") + "alts");
        String key = new RandomString(50).nextString();
        FileUtil.write(LoseBypass.ParentPath.getAbsolutePath() + "" + System.getProperty("file.separator") + "alts" + System.getProperty("file.separator") + "auth.key", key);
        Iterator<Account> iterator = LoseBypass.altManager.accounts.iterator();
        while (iterator.hasNext()) {
            Account account = iterator.next();
            try {
                ArrayList<String> lines = new ArrayList<String>();
                switch (2.$SwitchMap$com$wallhacks$losebypass$utils$auth$account$AccountType[account.accountType.ordinal()]) {
                    case 1: {
                        lines.add("MICROSOFT");
                        lines.add(EncryptionUtil.encrypt(account.getUUID(), key));
                        lines.add(EncryptionUtil.encrypt(((MSAccount)account).getRefreshToken(), key));
                        break;
                    }
                    case 2: {
                        lines.add("CRACKED");
                        lines.add(account.getName());
                        break;
                    }
                    case 3: {
                        lines.add("TOKEN");
                        lines.add(EncryptionUtil.encrypt(account.getUUID(), key));
                        lines.add(EncryptionUtil.encrypt(((TokenAccount)account).getToken(), key));
                        break;
                    }
                }
                String content = lines.stream().map(e -> e + "\n").collect(Collectors.joining());
                FileUtil.write(this.getAltFile(account.getName()), content);
            }
            catch (Exception exception) {}
        }
    }
}

