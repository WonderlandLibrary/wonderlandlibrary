package intent.AquaDev.aqua.altloader;

import com.google.gson.JsonObject;
import intent.AquaDev.aqua.altloader.Api;
import intent.AquaDev.aqua.altloader.Callback;
import intent.AquaDev.aqua.altloader.RedeemResponse;
import javax.net.ssl.HttpsURLConnection;

/*
 * Exception performing whole class analysis ignored.
 */
static final class Api.2
implements Runnable {
    final /* synthetic */ String val$token;
    final /* synthetic */ Callback val$callback;

    Api.2(String string, Callback callback) {
        this.val$token = string;
        this.val$callback = callback;
    }

    public void run() {
        HttpsURLConnection connection = Api.preparePostRequest((String)"https://api.easymc.io/v1/token/redeem", (String)("{\"token\":\"" + this.val$token + "\",\"client\":\"mod-1.8.9\"}"));
        if (connection == null) {
            this.val$callback.done((Object)"Could not create Connection. Please try again later.");
            return;
        }
        Object o = Api.getResult((HttpsURLConnection)connection);
        if (o instanceof String) {
            this.val$callback.done(o);
            return;
        }
        JsonObject jsonObject = (JsonObject)o;
        RedeemResponse response = new RedeemResponse();
        response.session = jsonObject.get("session").getAsString();
        response.mcName = jsonObject.get("mcName").getAsString();
        response.uuid = jsonObject.get("uuid").getAsString();
        this.val$callback.done((Object)response);
    }
}
