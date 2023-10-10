/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.Sys
 */
package com.wallhacks.losebypass.utils.auth;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.wallhacks.losebypass.LoseBypass;
import com.wallhacks.losebypass.utils.GuiUtil;
import com.wallhacks.losebypass.utils.MC;
import com.wallhacks.losebypass.utils.auth.AuthException;
import com.wallhacks.losebypass.utils.auth.MCToken;
import com.wallhacks.losebypass.utils.auth.MSToken;
import com.wallhacks.losebypass.utils.auth.Profile;
import com.wallhacks.losebypass.utils.auth.XBLToken;
import com.wallhacks.losebypass.utils.auth.XSTSToken;
import com.wallhacks.losebypass.utils.auth.account.MSAccount;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import net.minecraft.util.Session;
import org.lwjgl.Sys;

public class MSAuth
implements MC {
    private HttpServer srv;
    public MSAccount acc;
    public int state = 0;
    public boolean failed = false;

    public MSAuth() {
        final String done = "<html><body><h1>You can close this tab now</h1></body></html>";
        new Thread(() -> {
            try {
                this.srv = HttpServer.create(new InetSocketAddress(48375), 0);
                this.srv.createContext("/", new HttpHandler(){

                    @Override
                    public void handle(HttpExchange ex) throws IOException {
                        try {
                            ex.getResponseHeaders().add("Location", "http://localhost:48375/end");
                            ex.sendResponseHeaders(302, -1L);
                            new Thread(() -> MSAuth.this.auth(ex.getRequestURI().getQuery()), "MS Auth Thread").start();
                            return;
                        }
                        catch (Throwable t) {
                            LoseBypass.logger.warn("Unable to process request 'auth' on MS auth server", t);
                            MSAuth.this.stop();
                        }
                    }
                });
                this.srv.createContext("/end", new HttpHandler(){

                    @Override
                    public void handle(HttpExchange ex) throws IOException {
                        try {
                            byte[] b = done.getBytes(StandardCharsets.UTF_8);
                            ex.getResponseHeaders().put("Content-Type", Arrays.asList("text/html; charset=UTF-8"));
                            ex.sendResponseHeaders(200, b.length);
                            OutputStream os = ex.getResponseBody();
                            os.write(b);
                            os.flush();
                            os.close();
                            MSAuth.this.stop();
                            return;
                        }
                        catch (Throwable t) {
                            MSAuth.this.stop();
                        }
                    }
                });
                this.srv.start();
                Sys.openURL((String)"https://login.live.com/oauth20_authorize.srf?client_id=f187964d-b663-4d6f-8b35-71f146a1e5b7&response_type=code&scope=XboxLive.signin%20XboxLive.offline_access&redirect_uri=http://localhost:48375&prompt=consent&client_secret=hQH7Q~KLvutybVmaDO8YvJ2HrP_CATs_Lq-Wp");
                return;
            }
            catch (Throwable t) {
                LoseBypass.logger.warn("Unable to start MS auth server", t);
                this.stop();
            }
        }, "Auth").start();
    }

    public String getStatus() {
        switch (this.state) {
            case 0: {
                return "Open your browser";
            }
            case 1: {
                return "Getting msToken" + GuiUtil.getLoadingText(false);
            }
            case 2: {
                return "Getting xblToken" + GuiUtil.getLoadingText(false);
            }
            case 3: {
                return "Getting xstsToken" + GuiUtil.getLoadingText(false);
            }
            case 4: {
                return "Getting mcToken" + GuiUtil.getLoadingText(false);
            }
            case 5: {
                return "Getting minecraft profile" + GuiUtil.getLoadingText(false);
            }
        }
        return null;
    }

    private void auth(String code) {
        try {
            if (code == null) {
                throw new AuthException("query=null");
            }
            if (code.equals("error=access_denied&error_description=The user has denied access to the scope requested by the client application.")) {
                throw new AuthException("Access denied");
            }
            if (!code.startsWith("code=")) {
                throw new IllegalStateException("query=" + code);
            }
            ++this.state;
            MSToken msToken = new MSToken(code.substring(5));
            ++this.state;
            XBLToken xblToken = new XBLToken(msToken.access);
            ++this.state;
            XSTSToken xstsToken = new XSTSToken(xblToken.token);
            ++this.state;
            MCToken mcToken = new MCToken(xstsToken.userHash, xstsToken.token);
            ++this.state;
            Profile profile = new Profile(mcToken.token);
            Session session = new Session(profile.name, profile.uuid, mcToken.token, "mojang");
            this.acc = new MSAccount(session, msToken.refresh, profile.name, session.getProfile().getId().toString());
            return;
        }
        catch (AuthException t) {
            this.failed = true;
            LoseBypass.altManager.setStatus(t.getText());
        }
    }

    public boolean stop() {
        try {
            if (this.srv == null) return true;
            this.srv.stop(0);
            return true;
        }
        catch (Throwable t) {
            LoseBypass.logger.info("tried stopping ms auth but failed");
            return false;
        }
    }
}

