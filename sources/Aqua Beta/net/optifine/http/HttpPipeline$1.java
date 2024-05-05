package net.optifine.http;

import java.util.Map;
import net.optifine.http.HttpListener;
import net.optifine.http.HttpRequest;
import net.optifine.http.HttpResponse;

static final class HttpPipeline.1
implements HttpListener {
    final /* synthetic */ Map val$map;

    HttpPipeline.1(Map map) {
        this.val$map = map;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void finished(HttpRequest req, HttpResponse resp) {
        Map map = this.val$map;
        synchronized (map) {
            this.val$map.put((Object)"Response", (Object)resp);
            this.val$map.notifyAll();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void failed(HttpRequest req, Exception e) {
        Map map = this.val$map;
        synchronized (map) {
            this.val$map.put((Object)"Exception", (Object)e);
            this.val$map.notifyAll();
        }
    }
}
