/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.rektskyapi.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

public class TokenListResponse {
    @Expose
    @SerializedName(value="tokens")
    public List<String> tokens = new ArrayList<String>();

    public TokenListResponse(List<String> tokens) {
        this.tokens = tokens;
    }

    public TokenListResponse() {
    }
}

