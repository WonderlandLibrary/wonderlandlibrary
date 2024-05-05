package net.minecraft.client.main;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

static final class Main.1
extends Authenticator {
    final /* synthetic */ String val$s1;
    final /* synthetic */ String val$s2;

    Main.1(String string, String string2) {
        this.val$s1 = string;
        this.val$s2 = string2;
    }

    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(this.val$s1, this.val$s2.toCharArray());
    }
}
