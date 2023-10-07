package cc.novoline.gui.screen.alt.login;

import net.minecraft.util.Session;

/**
 * @author xDelsy
 */
public interface AltLoginListener {

    void onLoginSuccess(AltType altType, Session session);

    void onLoginFailed();

}
