package cc.novoline.gui.screen.alt.login;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;

/**
 * @author xDelsy
 */
public abstract class SessionUpdatingAltLoginListener implements AltLoginListener {

    @Override
    public void onLoginSuccess(AltType type, Session session) {
        updateMinecraftSession(session);
    }

    private void updateMinecraftSession(Session newSession) {
        Minecraft.getInstance().session = newSession;
    }

}
