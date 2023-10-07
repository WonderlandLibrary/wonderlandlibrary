package cc.novoline.utils.thealtening;

import cc.novoline.utils.thealtening.service.AlteningServiceType;
import cc.novoline.utils.thealtening.service.ServiceSwitcher;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

public final class TheAlteningAuthentication {

    private static TheAlteningAuthentication instance;

    private final ServiceSwitcher serviceSwitcher = new ServiceSwitcher();
    private final SSLController sslController = new SSLController();
    private AlteningServiceType service;

    private TheAlteningAuthentication(@NonNull AlteningServiceType service) throws Throwable {
        updateService(service);
    }

    public void updateService(@NonNull AlteningServiceType service) {
        if (this.service == service) return;

        switch (service) {
            case MOJANG:
                sslController.enableCertificateValidation();
                break;

            case THEALTENING:
                sslController.disableCertificateValidation();
                break;
        }

        this.service = serviceSwitcher.switchToService(service);
    }

    public static TheAlteningAuthentication mojang() {
        return withService(AlteningServiceType.MOJANG);
    }

    public static TheAlteningAuthentication theAltening() {
        return withService(AlteningServiceType.THEALTENING);
    }

    private static TheAlteningAuthentication withService(@NonNull AlteningServiceType service) {
        if (instance == null) {
            try {
                instance = new TheAlteningAuthentication(service);
            } catch (NoSuchAlgorithmException | KeyManagementException e) {
                SSLController.log.warn(e);
            } catch (Throwable t) {
                SSLController.log.warn("Unexpected error occurred while executing...", t);
            }
        } else if (instance.getService() != service) {
            instance.updateService(service);
        }

        return instance;
    }

    @NonNull
    public AlteningServiceType getService() {
        return this.service;
    }

}
