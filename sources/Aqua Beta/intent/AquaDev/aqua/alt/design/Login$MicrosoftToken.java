package intent.AquaDev.aqua.alt.design;

public static class Login.MicrosoftToken {
    public final String token;
    public final String refreshToken;

    public Login.MicrosoftToken(String token, String refreshToken) {
        this.token = token;
        this.refreshToken = refreshToken;
    }
}
