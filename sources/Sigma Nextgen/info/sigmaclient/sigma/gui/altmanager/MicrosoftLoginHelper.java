package info.sigmaclient.sigma.gui.altmanager;

import info.sigmaclient.sigma.config.alts.Alt;
import info.sigmaclient.sigma.config.alts.AltConfig;
import info.sigmaclient.sigma.gui.mainmenu.SigmaGuiMainMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;
import org.apache.commons.io.IOUtils;

public final class MicrosoftLoginHelper {
    private volatile MicrosoftLogin microsoftLogin;
    public boolean closed = false;
    public boolean failed = false;

    public MicrosoftLoginHelper() {
        final Thread thread = new Thread("MicrosoftLogin Thread") {
            @Override
            public void run() {
                try {
                    microsoftLogin = new MicrosoftLogin();

                    while (!closed) {
                        if (microsoftLogin.isLogged()) {
                            IOUtils.closeQuietly(microsoftLogin);

                            closed = true;

                            final Minecraft mc = Minecraft.getInstance();
                            mc.session = new Session(microsoftLogin.getUserName(), microsoftLogin.getUuid(), microsoftLogin.getAccessToken(), "Mojang");
//                            mc.displayGuiScreen(new SigmaGuiMainMenu());
                            Alt a = new Alt(mc.session.getUsername(), mc.session.getPlayerID(), mc.session.getToken());
                            AltConfig.Instance.alts.add(a);
                            JelloAltManager.select = a;
                            break;
                        }
                    }
                } catch (Throwable e) {
                    closed = true;
                    failed = true;

                    e.printStackTrace();

                    IOUtils.closeQuietly(microsoftLogin);
                }
            }
        };

        thread.setDaemon(true);
        thread.start();
    }

    public void close() {
        if (microsoftLogin != null && !closed) {
            microsoftLogin.close();
            closed = true;
        }
    }
}
