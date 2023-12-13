package today.flux.event;

import com.darkmagician6.eventapi.events.Event;


/**
 * Created by John on 2017/03/28.
 */
public class ServerJoinEvent implements Event {
    private String ip;
    public ServerJoinEvent(String ip){
        this.ip = ip;
    }

    public String getIp() {
        return ip;
    }
}
