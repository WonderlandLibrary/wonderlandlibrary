// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.management.account;

import exhibition.management.SubFolder;
import exhibition.Client;
import java.io.File;
import exhibition.management.AbstractManager;

public class AccountManager<E extends Account> extends AbstractManager<Account>
{
    private Account current;
    
    public AccountManager(final Class<Account> clazz) {
        super(clazz, 0);
    }
    
    @Override
    public void setup() {
        final File accountDir = this.getAccountDir();
        if (accountDir.isDirectory()) {
            final File[] accountFiles = accountDir.listFiles();
            final int accounts = accountFiles.length;
            int i = 0;
            this.reset(accounts);
            for (final File accountFile : accountFiles) {
                final Account account = new Account(accountFile.getName().substring(0, accountFile.getName().indexOf(".")));
                account.load();
                this.array[i] = account;
                ++i;
            }
        }
    }
    
    public void reload() {
        for (final Account account : (Account[])this.array) {
            if (account != null) {
                final File accountFile = account.getFile();
                if (accountFile.exists()) {
                    account.load();
                }
            }
        }
    }
    
    public Account getCurrent() {
        if (this.current == null) {
            final Account account = new Account("ERROR", "ERROR");
            account.setPremium(false);
            this.current = account;
        }
        return this.current;
    }
    
    public void setCurrent(final Account account) {
        this.current = account;
    }
    
    @Override
    public void remove(final Account account) {
        final File accountFile = account.getFile();
        if (accountFile.exists()) {
            accountFile.delete();
        }
        super.remove(account);
    }
    
    public Account getAccount(final String username) {
        if (((Account[])this.array).length == 0) {
            return null;
        }
        for (final Account account : (Account[])this.array) {
            if (account != null && account.getDisplay().equals(username)) {
                return account;
            }
        }
        return null;
    }
    
    private File getAccountDir() {
        final File accountDir = new File(Client.getDataDir() + File.separator + SubFolder.Alt.getFolderName());
        if (!accountDir.exists()) {
            accountDir.mkdirs();
        }
        return accountDir;
    }
}
