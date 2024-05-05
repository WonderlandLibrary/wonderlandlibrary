package club.dortware.client.misc;

import co.gongzh.procbridge.Client;

import java.io.IOException;
import java.net.ServerSocket;

public class WebSocketThing {
    public static final Client CLIENT = new Client("0.0.0.0", 8000);
    public static void main(String... args) throws IOException {
        CLIENT.request("pos", String.join(" ", args));
    }
}