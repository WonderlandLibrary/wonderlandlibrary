package club.cloverhook.account;

import java.util.ArrayList;

import club.cloverhook.Cloverhook;

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
