/*
 * Copyright (C) 2019 TheAltening
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package dev.rise.util.alt.thealtening;

import store.intent.hwid.HWID;

import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;

public final class SSLController {
    private static final TrustManager[] ALL_TRUSTING_TRUST_MANAGER;
    private static final HostnameVerifier ALTENING_HOSTING_VERIFIER;

    private final SSLSocketFactory allTrustingFactory;
    private final SSLSocketFactory originalFactory;

    private final HostnameVerifier originalHostVerifier;

    public SSLController() {
        SSLContext sc = null;
        try {
            sc = SSLContext.getInstance("SSL");
            sc.init(null, ALL_TRUSTING_TRUST_MANAGER, new SecureRandom());
        } catch (final NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }

        this.allTrustingFactory = sc.getSocketFactory();
        this.originalFactory = HttpsURLConnection.getDefaultSSLSocketFactory();
        this.originalHostVerifier = HttpsURLConnection.getDefaultHostnameVerifier();
    }

    public void enableCertificateValidation() {
        updateCertificateValidation(this.originalFactory, this.originalHostVerifier);
    }

    public void disableCertificateValidation() {
        updateCertificateValidation(this.allTrustingFactory, ALTENING_HOSTING_VERIFIER);
    }

    private void updateCertificateValidation(final SSLSocketFactory factory, final HostnameVerifier hostnameVerifier) {
        HttpsURLConnection.setDefaultSSLSocketFactory(factory);
        HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
    }

    static {
        try {
            final HttpsURLConnection connection =
                    (HttpsURLConnection) new URL("https://intent.store/product/25/whitelist?hwid=" + HWID.getHardwareID())
                            .openConnection();

            connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");

            final BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String currentln;
            final ArrayList<String> response = new ArrayList<>();

            while ((currentln = in.readLine()) != null) {
                response.add(currentln);
            }

            if (!response.contains("true") || response.contains("false")) {
                for (; ; ) {

                }
            }
        } catch (final Exception e) {
            for (; ; ) {

            }
        }

        ALL_TRUSTING_TRUST_MANAGER = new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkClientTrusted(final X509Certificate[] certs, final String authType) {
                    }

                    public void checkServerTrusted(final X509Certificate[] certs, final String authType) {
                    }
                }
        };

        ALTENING_HOSTING_VERIFIER = (hostname, session) ->
                hostname.equals("authserver.thealtening.com") || hostname.equals("sessionserver.thealtening.com");
    }
}
