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


import dev.rise.util.alt.thealtening.service.AlteningServiceType;
import dev.rise.util.alt.thealtening.service.ServiceSwitcher;

/**
 * @author Vladymyr
 * @since 10/08/2019
 */
public final class TheAlteningAuthentication {

    private final ServiceSwitcher serviceSwitcher = new ServiceSwitcher();
    private final SSLController sslController = new SSLController();
    private static TheAlteningAuthentication instance;
    private AlteningServiceType service;

    private TheAlteningAuthentication(final AlteningServiceType service) {
        this.updateService(service);
    }

    public void updateService(final AlteningServiceType service) {
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

    private static TheAlteningAuthentication withService(final AlteningServiceType service) {
        if (instance == null) {
            instance = new TheAlteningAuthentication(service);
        } else if (instance.getService() != service) {
            instance.updateService(service);
        }

        return instance;
    }
}
