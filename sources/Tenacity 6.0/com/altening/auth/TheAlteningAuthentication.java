// 
// Decompiled by Procyon v0.5.36
// 

package com.altening.auth;

import com.altening.auth.service.AlteningServiceType;
import com.altening.auth.service.ServiceSwitcher;

public final class TheAlteningAuthentication
{
    private static TheAlteningAuthentication instance;
    private final ServiceSwitcher serviceSwitcher;
    private final SSLController sslController;
    private AlteningServiceType service;
    
    private TheAlteningAuthentication(final AlteningServiceType service) {
        this.serviceSwitcher = new ServiceSwitcher();
        this.sslController = new SSLController();
        this.updateService(service);
    }
    
    public static TheAlteningAuthentication mojang() {
        return withService(AlteningServiceType.MOJANG);
    }
    
    public static TheAlteningAuthentication altening() {
        return withService(AlteningServiceType.THEALTENING);
    }
    
    private static TheAlteningAuthentication withService(final AlteningServiceType service) {
        if (TheAlteningAuthentication.instance == null) {
            TheAlteningAuthentication.instance = new TheAlteningAuthentication(service);
        }
        else if (TheAlteningAuthentication.instance.getService() != service) {
            TheAlteningAuthentication.instance.updateService(service);
        }
        return TheAlteningAuthentication.instance;
    }
    
    public void updateService(final AlteningServiceType service) {
        if (service == null || this.service == service) {
            return;
        }
        switch (service) {
            case MOJANG: {
                this.sslController.enableCertificateValidation();
                break;
            }
            case THEALTENING: {
                this.sslController.disableCertificateValidation();
                break;
            }
        }
        this.service = this.serviceSwitcher.switchToService(service);
    }
    
    public AlteningServiceType getService() {
        return this.service;
    }
}
