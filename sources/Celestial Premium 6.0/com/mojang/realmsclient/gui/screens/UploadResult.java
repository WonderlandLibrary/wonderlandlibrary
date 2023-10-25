/*
 * Decompiled with CFR 0.150.
 */
package com.mojang.realmsclient.gui.screens;

public class UploadResult {
    public final int statusCode;
    public final String errorMessage;

    public UploadResult(int statusCode, String errorMessage) {
        this.statusCode = statusCode;
        this.errorMessage = errorMessage;
    }

    public static class Builder {
        private int statusCode = -1;
        private String errorMessage = null;

        public Builder withStatusCode(int statusCode) {
            this.statusCode = statusCode;
            return this;
        }

        public Builder withErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
            return this;
        }

        public UploadResult build() {
            return new UploadResult(this.statusCode, this.errorMessage);
        }
    }
}

