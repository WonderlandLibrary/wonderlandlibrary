package today.flux.gui.altMgr.kingAlts;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class AltJson {
    @SerializedName("email")
    String email;
    @SerializedName("password")
    String password;
    @SerializedName("message")
    String message;
}