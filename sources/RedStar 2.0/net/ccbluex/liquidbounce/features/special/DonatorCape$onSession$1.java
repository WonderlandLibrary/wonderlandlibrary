package net.ccbluex.liquidbounce.features.special;

import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import net.ccbluex.liquidbounce.ui.client.altmanager.sub.GuiDonatorCape;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.json.JSONObject;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=3, d1={"\u0000\b\n\u0000\n\n\u0000\u00000H\n¢\b"}, d2={"<anonymous>", "", "invoke"})
final class DonatorCape$onSession$1
extends Lambda
implements Function0<Unit> {
    public static final DonatorCape$onSession$1 INSTANCE = new /* invalid duplicate definition of identical inner class */;

    @Override
    public final void invoke() {
        CloseableHttpResponse response;
        String uuid = MinecraftInstance.mc.getSession().getPlayerId();
        String username = MinecraftInstance.mc.getSession().getUsername();
        CloseableHttpClient httpClient = HttpClients.createDefault();
        BasicHeader[] headers = new BasicHeader[]{new BasicHeader("Content-Type", "application/json"), new BasicHeader("Authorization", GuiDonatorCape.Companion.getTransferCode())};
        HttpPatch request = new HttpPatch("http://capes.liquidbounce.net/api/v1/cape/self");
        request.setHeaders((Header[])headers);
        JSONObject body = new JSONObject();
        body.put("uuid", (Object)uuid);
        request.setEntity((HttpEntity)new StringEntity(body.toString()));
        CloseableHttpResponse closeableHttpResponse = response = httpClient.execute((HttpUriRequest)request);
        Intrinsics.checkExpressionValueIsNotNull(closeableHttpResponse, "response");
        StatusLine statusLine = closeableHttpResponse.getStatusLine();
        Intrinsics.checkExpressionValueIsNotNull(statusLine, "response.statusLine");
        int statusCode = statusLine.getStatusCode();
        ClientUtils.getLogger().info(statusCode == 204 ? "[Donator Cape] Successfully transferred cape to " + uuid + " (" + username + ')' : "[Donator Cape] Failed to transfer cape (" + statusCode + ')');
    }

    DonatorCape$onSession$1() {
    }
}
