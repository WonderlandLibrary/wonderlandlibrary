package com.google.common.net;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.escape.Escaper;

@Beta
@GwtCompatible
public final class UrlEscapers {
  static final String URL_FORM_PARAMETER_OTHER_SAFE_CHARS = "-_.*";
  
  static final String URL_PATH_OTHER_SAFE_CHARS_LACKING_PLUS = "-._~!$'()*,;&=@:";
  
  public static Escaper urlFormParameterEscaper() {
    return URL_FORM_PARAMETER_ESCAPER;
  }
  
  private static final Escaper URL_FORM_PARAMETER_ESCAPER = (Escaper)new PercentEscaper("-_.*", true);
  
  public static Escaper urlPathSegmentEscaper() {
    return URL_PATH_SEGMENT_ESCAPER;
  }
  
  private static final Escaper URL_PATH_SEGMENT_ESCAPER = (Escaper)new PercentEscaper("-._~!$'()*,;&=@:+", false);
  
  public static Escaper urlFragmentEscaper() {
    return URL_FRAGMENT_ESCAPER;
  }
  
  private static final Escaper URL_FRAGMENT_ESCAPER = (Escaper)new PercentEscaper("-._~!$'()*,;&=@:+/?", false);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\net\UrlEscapers.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */