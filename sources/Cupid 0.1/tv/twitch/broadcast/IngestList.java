package tv.twitch.broadcast;

public class IngestList {
  protected IngestServer[] servers = null;
  
  protected IngestServer defaultServer = null;
  
  public IngestServer[] getServers() {
    return this.servers;
  }
  
  public IngestServer getDefaultServer() {
    return this.defaultServer;
  }
  
  public IngestList(IngestServer[] paramArrayOfIngestServer) {
    if (paramArrayOfIngestServer == null) {
      this.servers = new IngestServer[0];
    } else {
      this.servers = new IngestServer[paramArrayOfIngestServer.length];
      for (byte b = 0; b < paramArrayOfIngestServer.length; b++) {
        this.servers[b] = paramArrayOfIngestServer[b];
        if ((this.servers[b]).defaultServer)
          this.defaultServer = this.servers[b]; 
      } 
      if (this.defaultServer == null && this.servers.length > 0)
        this.defaultServer = this.servers[0]; 
    } 
  }
  
  public IngestServer getBestServer() {
    if (this.servers == null || this.servers.length == 0)
      return null; 
    IngestServer ingestServer = this.servers[0];
    for (byte b = 1; b < this.servers.length; b++) {
      if (ingestServer.bitrateKbps < (this.servers[b]).bitrateKbps)
        ingestServer = this.servers[b]; 
    } 
    return ingestServer;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\tv\twitch\broadcast\IngestList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */