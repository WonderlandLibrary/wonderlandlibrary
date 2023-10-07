package dev.rise.ui.altmanager;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class AltAccount {
    private String email, password;
    private String username = "";

    private boolean isCracked;

    public AltAccount(final String email, final String pass) {
        this.email = email;
        password = pass;

        isCracked = pass.isEmpty();
    }
}
