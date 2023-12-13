/*
 * Decompiled with CFR 0_122.
 */
package me.arithmo.management.account;

import java.io.File;
import me.arithmo.Client;
import me.arithmo.management.AbstractManager;
import me.arithmo.management.Saveable;
import me.arithmo.management.SubFolder;
import me.arithmo.management.account.Account;

public class AccountManager<E extends Account>
extends AbstractManager<Account> {
    private Account current;

    public AccountManager(Class<Account> clazz) {
        super(clazz, 0);
    }

    @Override
    public void setup() {
        File accountDir = this.getAccountDir();
        if (accountDir.isDirectory()) {
            File[] accountFiles = accountDir.listFiles();
            int accounts = accountFiles.length;
            int i = 0;
            this.reset(accounts);
            for (File accountFile : accountFiles) {
                Account account = new Account(accountFile.getName().substring(0, accountFile.getName().indexOf(".")));
                account.load();
                ((Account[])this.array)[i] = account;
                ++i;
            }
        }
    }

    public void reload() {
        for (Account account : (Account[])this.array) {
            File accountFile;
            if (account == null || !(accountFile = account.getFile()).exists()) continue;
            account.load();
        }
    }

    public Account getCurrent() {
        if (this.current == null) {
            Account account = new Account("ERROR", "ERROR");
            account.setPremium(false);
            this.current = account;
        }
        return this.current;
    }

    public void setCurrent(Account account) {
        this.current = account;
    }

    @Override
    public void remove(Account account) {
        File accountFile = account.getFile();
        if (accountFile.exists()) {
            accountFile.delete();
        }
        super.remove(account);
    }

    public Account getAccount(String username) {
        if (((Account[])this.array).length == 0) {
            return null;
        }
        for (Account account : (Account[])this.array) {
            if (account == null || !account.getDisplay().equals(username)) continue;
            return account;
        }
        return null;
    }

    private File getAccountDir() {
        File accountDir = new File(Client.getDataDir() + File.separator + SubFolder.Alt.getFolderName());
        if (!accountDir.exists()) {
            accountDir.mkdirs();
        }
        return accountDir;
    }
}

