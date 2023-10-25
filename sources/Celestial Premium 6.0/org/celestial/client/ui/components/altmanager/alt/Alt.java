/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.ui.components.altmanager.alt;

public final class Alt {
    private final String username;
    private String mask = "";
    private String password;
    private Status status;

    public Alt(String username, String password) {
        this(username, password, Status.Unchecked);
    }

    public Alt(String username, String password, Status status) {
        this(username, password, "", status);
    }

    public Alt(String username, String password, String mask, Status status) {
        this.username = username;
        this.password = password;
        this.mask = mask;
        this.status = status;
    }

    public Status getStatus() {
        return this.status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getMask() {
        return this.mask;
    }

    public void setMask(String mask) {
        this.mask = mask;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return this.username;
    }

    public static enum Status {
        Working("\u00a7aWorking"),
        Banned("\u00a7cBanned"),
        Unchecked("\u00a7eUnchecked"),
        NotWorking("\u00a74Not Working");

        private final String formatted;

        private Status(String string2) {
            this.formatted = string2;
        }

        public String toFormatted() {
            return this.formatted;
        }
    }
}

