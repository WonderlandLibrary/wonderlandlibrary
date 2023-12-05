package rip.helium.account;

import java.util.ArrayList;

/**
 * @author antja03
 */
public class AccountManager {

    private final ArrayList<Account> ACCOUNT_REGISTRY;

    public AccountManager() {
        ACCOUNT_REGISTRY = new ArrayList<>();
    }

    public ArrayList<Account> getAccountRegistry() {
        return ACCOUNT_REGISTRY;
    }
}
