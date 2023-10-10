/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.rektskyapi.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AuthenticationResponse {
    @SerializedName(value="session")
    @Expose
    public String session;

    public AuthenticationResponse() {
    }

    public AuthenticationResponse(String session) {
        this.session = session;
    }
}

