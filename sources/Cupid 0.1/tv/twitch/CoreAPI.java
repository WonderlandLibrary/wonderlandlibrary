package tv.twitch;

public abstract class CoreAPI {
  public abstract ErrorCode init(String paramString1, String paramString2);
  
  public abstract ErrorCode shutdown();
  
  public abstract ErrorCode setTraceLevel(MessageLevel paramMessageLevel);
  
  public abstract ErrorCode setTraceOutput(String paramString);
  
  public abstract String errorToString(ErrorCode paramErrorCode);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\tv\twitch\CoreAPI.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */