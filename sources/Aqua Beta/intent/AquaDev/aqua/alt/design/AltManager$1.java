package intent.AquaDev.aqua.alt.design;

import intent.AquaDev.aqua.alt.design.AltManager;
import intent.AquaDev.aqua.altloader.Callback;
import intent.AquaDev.aqua.altloader.RedeemResponse;

/*
 * Exception performing whole class analysis ignored.
 */
class AltManager.1
implements Callback<Object> {
    AltManager.1() {
    }

    public void done(Object o) {
        RedeemResponse response;
        if (o instanceof String) {
            return;
        }
        if (AltManager.access$000().savedSession == null) {
            AltManager.access$000().savedSession = AltManager.access$100((AltManager)AltManager.this).getSession();
        }
        AltManager.access$000().easyMCSession = response = (RedeemResponse)o;
        AltManager.access$000().setEasyMCSession(response);
    }
}
