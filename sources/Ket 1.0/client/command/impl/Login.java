package client.command.impl;

import client.Client;
import client.command.Command;
import client.util.ChatUtil;
import fr.litarvan.openauth.microsoft.MicrosoftAuthResult;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticationException;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticator;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Session;

public final class Login extends Command {

    public Login() {
        super("", "login", "l");
    }

    @Override
    public void execute(final String[] args) {
        switch (args.length) {
            case 1: {
                new Thread(() -> {
                    try {
                        final MicrosoftAuthResult result = new MicrosoftAuthenticator().loginWithWebview();
                        mc.setSession(new Session(result.getProfile().getName(), result.getProfile().getId(), result.getAccessToken(), "msa"));
                        ChatUtil.display(EnumChatFormatting.GREEN + "Logged in as " + EnumChatFormatting.YELLOW + result.getProfile().getName());
                    } catch (MicrosoftAuthenticationException e) {
                        if (Client.DEVELOPMENT_SWITCH) e.printStackTrace();
                    }
                }).start();
                break;
            }
            case 2: {
                if (args[1].contains(":")) {
                    new Thread(() -> {
                        try {
                            final MicrosoftAuthResult result = new MicrosoftAuthenticator().loginWithCredentials(args[1].split(":")[0], args[1].split(":")[1]);
                            mc.setSession(new Session(result.getProfile().getName(), result.getProfile().getId(), result.getAccessToken(), "msa"));
                            ChatUtil.display(EnumChatFormatting.GREEN + "Logged in as " + EnumChatFormatting.YELLOW + result.getProfile().getName());
                        } catch (MicrosoftAuthenticationException e) {
                            if (Client.DEVELOPMENT_SWITCH) e.printStackTrace();
                        }
                    }).start();
                }
                break;
            }
            case 3: {
                new Thread(() -> {
                    try {
                        final MicrosoftAuthResult result = new MicrosoftAuthenticator().loginWithCredentials(args[1], args[2]);
                        mc.setSession(new Session(result.getProfile().getName(), result.getProfile().getId(), result.getAccessToken(), "msa"));
                        ChatUtil.display(EnumChatFormatting.GREEN + "Logged in as " + EnumChatFormatting.YELLOW + result.getProfile().getName());
                    } catch (MicrosoftAuthenticationException e) {
                        if (Client.DEVELOPMENT_SWITCH) e.printStackTrace();
                    }
                }).start();
                break;
            }
            default: {
                error(String.format(".%s E-Mail:Password", args[0]));
                break;
            }
        }
    }

    private void login(String email, String password) throws MicrosoftAuthenticationException {
        MicrosoftAuthenticator authenticator = new MicrosoftAuthenticator();
        MicrosoftAuthResult result = authenticator.loginWithCredentials(email, password);
        mc.setSession(new Session(result.getProfile().getName(), result.getProfile().getId(), result.getAccessToken(), "msa"));
    }
}
