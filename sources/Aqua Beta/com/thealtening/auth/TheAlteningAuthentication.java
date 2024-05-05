package com.thealtening.auth;

import com.thealtening.auth.service.AlteningServiceType;
import com.thealtening.auth.service.ServiceSwitcher;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

/**
 * @author Vladymyr
 * @since 10/08/2019
 */
public final class TheAlteningAuthentication {

    private final ServiceSwitcher serviceSwitcher = new ServiceSwitcher();
    private final SSLController sslController = new SSLController();
    private static TheAlteningAuthentication instance;
    private AlteningServiceType service;

    private TheAlteningAuthentication(AlteningServiceType service) {
        this.updateService(service);
    }

    public void updateService(AlteningServiceType service) {
        if (service == null || this.service == service) {
            return;
        }

        switch (service) {
            case MOJANG:
                this.sslController.enableCertificateValidation();
                break;

            case THEALTENING:
                this.sslController.disableCertificateValidation();
                break;
        }

        this.service = this.serviceSwitcher.switchToService(service);
    }

    public AlteningServiceType getService() {
        return service;
    }

    public static TheAlteningAuthentication mojang() {
        return withService(AlteningServiceType.MOJANG);
    }

    public static TheAlteningAuthentication theAltening() {
        return withService(AlteningServiceType.THEALTENING);
    }

    private static TheAlteningAuthentication withService(AlteningServiceType service) {
        if (instance == null) {
            instance = new TheAlteningAuthentication(service);
        } else if (instance.getService() != service) {
            instance.updateService(service);
        }

        return instance;
    }
}