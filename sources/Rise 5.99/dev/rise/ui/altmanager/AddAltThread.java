package dev.rise.ui.altmanager;

import com.mojang.authlib.Agent;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import dev.rise.Rise;
import dev.rise.antipiracy.Validator;
import dev.rise.ui.alt.AltGUI;
import dev.rise.ui.altmanager.gui.AltManagerGUI;

import java.io.IOException;
import java.net.Proxy;

public final class AddAltThread extends Thread {
    private final String password;
    private final String username;

    public AddAltThread(final String username, final String password) {
        this.username = username;
        this.password = password;
    }

    private void checkAndAddAlt(final String username, final String password) throws IOException {
        final YggdrasilAuthenticationService service = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
        final YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication) service.createUserAuthentication(Agent.MINECRAFT);
        auth.setUsername(username);
        auth.setPassword(password);
        try {
            auth.logIn();

            final AltAccount alt = new AltAccount(username, password);
            alt.setUsername(auth.getSelectedProfile().getName());

            Rise.INSTANCE.getAltManagerGUI().altList.add(alt);
            Rise.INSTANCE.getAltGUI().altList.add(alt);
        } catch (final AuthenticationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        if (this.password.equals("")) {
            final AltManagerGUI altManager = Rise.INSTANCE.getAltManagerGUI();
            altManager.altList.add(new AltAccount(username, password));

            final AltGUI alt = Rise.INSTANCE.getAltGUI();
            alt.altList.add(new AltAccount(username, password));
            return;
        }
        try {
            this.checkAndAddAlt(this.username, this.password);
        } catch (final IOException e) {
            e.printStackTrace();
        }
        Validator.validate();
    }
}
