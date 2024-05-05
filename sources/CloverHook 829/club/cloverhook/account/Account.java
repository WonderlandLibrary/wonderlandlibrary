package club.cloverhook.account;

import com.thealtening.AltService;

import club.cloverhook.Cloverhook;

/**
 * @author antja03
 */
public class Account {

    private String username;
    private String password;
    private String[] loggedBans;
    private AltService.EnumAltService service;

    public Account(String username, String password, AltService.EnumAltService service) {
        this.username = username;
        this.password = password;
        this.service = service;
    }

    public void attemptLogin() throws IllegalArgumentException {
        Cloverhook.instance.accountLoginService.attemptLogin(this);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String[] getLoggedBans() {
        return loggedBans;
    }

    public void setLoggedBans(String[] loggedBans) {
        this.loggedBans = loggedBans;
    }

    public AltService.EnumAltService getService() {
        return service;
    }

    public void setService(AltService.EnumAltService service) {
        this.service = service;
    }
}
