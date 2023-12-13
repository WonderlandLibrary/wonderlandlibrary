package today.flux.event;

import com.darkmagician6.eventapi.events.Event;
import lombok.Getter;
import net.minecraft.entity.EntityLivingBase;

/**
 * Created by John on 2017/06/24.
 */
public class LivingUpdateEvent implements Event {
    public LivingUpdateEvent(EntityLivingBase entity){
        this.entity = entity;
    }

    @Getter
    private EntityLivingBase entity;
}
