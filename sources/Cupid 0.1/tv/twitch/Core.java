package tv.twitch;

public class Core {
  private static Core s_Instance = null;
  
  public static Core getInstance() {
    return s_Instance;
  }
  
  private CoreAPI m_CoreAPI = null;
  
  private String m_ClientId = null;
  
  private int m_NumInitializations = 0;
  
  public Core(CoreAPI paramCoreAPI) {
    this.m_CoreAPI = paramCoreAPI;
    if (s_Instance == null)
      s_Instance = this; 
  }
  
  public boolean getIsInitialized() {
    return (this.m_NumInitializations > 0);
  }
  
  public ErrorCode initialize(String paramString1, String paramString2) {
    if (this.m_NumInitializations == 0) {
      this.m_ClientId = paramString1;
    } else if (paramString1 != this.m_ClientId) {
      return ErrorCode.TTV_EC_INVALID_CLIENTID;
    } 
    this.m_NumInitializations++;
    if (this.m_NumInitializations > 1)
      return ErrorCode.TTV_EC_SUCCESS; 
    ErrorCode errorCode = this.m_CoreAPI.init(paramString1, paramString2);
    if (ErrorCode.failed(errorCode)) {
      this.m_NumInitializations--;
      this.m_ClientId = null;
    } 
    return errorCode;
  }
  
  public ErrorCode shutdown() {
    if (this.m_NumInitializations == 0)
      return ErrorCode.TTV_EC_NOT_INITIALIZED; 
    this.m_NumInitializations--;
    ErrorCode errorCode = ErrorCode.TTV_EC_SUCCESS;
    if (this.m_NumInitializations == 0) {
      errorCode = this.m_CoreAPI.shutdown();
      if (ErrorCode.failed(errorCode)) {
        this.m_NumInitializations++;
      } else if (s_Instance == this) {
        s_Instance = null;
      } 
    } 
    return errorCode;
  }
  
  public ErrorCode setTraceLevel(MessageLevel paramMessageLevel) {
    return this.m_CoreAPI.setTraceLevel(paramMessageLevel);
  }
  
  public String errorToString(ErrorCode paramErrorCode) {
    return this.m_CoreAPI.errorToString(paramErrorCode);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\tv\twitch\Core.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */