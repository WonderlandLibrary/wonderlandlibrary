/*
 * Decompiled with CFR 0.150.
 */
package com.mojang.realmsclient.exception;

import com.mojang.realmsclient.client.RealmsError;
import net.minecraft.realms.RealmsScreen;

public class RealmsServiceException
extends Exception {
    public final int httpResultCode;
    public final String httpResponseContent;
    public final int errorCode;
    public final String errorMsg;

    public RealmsServiceException(int httpResultCode, String httpResponseText, RealmsError error) {
        super(httpResponseText);
        this.httpResultCode = httpResultCode;
        this.httpResponseContent = httpResponseText;
        this.errorCode = error.getErrorCode();
        this.errorMsg = error.getErrorMessage();
    }

    public RealmsServiceException(int httpResultCode, String httpResponseText, int errorCode, String errorMsg) {
        super(httpResponseText);
        this.httpResultCode = httpResultCode;
        this.httpResponseContent = httpResponseText;
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    @Override
    public String toString() {
        if (this.errorCode == -1) {
            return "Realms (" + this.httpResultCode + ") " + this.httpResponseContent;
        }
        String translationKey = "mco.errorMessage." + this.errorCode;
        String translated = RealmsScreen.getLocalizedString(translationKey);
        return (translated.equals(translationKey) ? this.errorMsg : translated) + " - " + this.errorCode;
    }
}

