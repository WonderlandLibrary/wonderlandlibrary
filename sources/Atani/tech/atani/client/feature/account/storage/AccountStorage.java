package tech.atani.client.feature.account.storage;

import de.florianmichael.rclasses.storage.Storage;
import tech.atani.client.feature.account.Account;

public class AccountStorage extends Storage<Account> {

    private static AccountStorage instance;

    @Override
    public void init() {
        // Accounts are added with files
    }

    public static AccountStorage getInstance() {
        return instance;
    }

    public static void setInstance(AccountStorage instance) {
        AccountStorage.instance = instance;
    }
}
