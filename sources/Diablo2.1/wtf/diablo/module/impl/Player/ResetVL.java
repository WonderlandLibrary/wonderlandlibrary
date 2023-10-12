package wtf.diablo.module.impl.Player;

import lombok.Getter;
import lombok.Setter;
import wtf.diablo.module.Module;
import wtf.diablo.module.data.Category;
import wtf.diablo.module.data.ServerType;

@Getter@Setter
public class ResetVL extends Module {
    public ResetVL(){
        super("ResetVL","Resets VL", Category.PLAYER, ServerType.All);
    }


}
