/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.rektskyapi.requests;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GenerateTokensRequest {
    @Expose
    @SerializedName(value="amount")
    public Integer amount = 1;

    public GenerateTokensRequest(Integer amount) {
        this.amount = amount;
    }

    public GenerateTokensRequest() {
    }
}

