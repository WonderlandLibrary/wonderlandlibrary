package tv.twitch.broadcast;

public class VideoParams {
   public int outputWidth;
   public int outputHeight;
   public PixelFormat pixelFormat;
   public int maxKbps;
   public int targetFps;
   public EncodingCpuUsage encodingCpuUsage;
   public boolean disableAdaptiveBitrate;
   public boolean verticalFlip;

   public VideoParams() {
      this.pixelFormat = PixelFormat.TTV_PF_BGRA;
      this.encodingCpuUsage = EncodingCpuUsage.TTV_ECU_HIGH;
      this.disableAdaptiveBitrate = false;
      this.verticalFlip = false;
   }

   public VideoParams clone() {
      VideoParams var1 = new VideoParams();
      var1.outputWidth = this.outputWidth;
      var1.outputHeight = this.outputHeight;
      var1.pixelFormat = this.pixelFormat;
      var1.maxKbps = this.maxKbps;
      var1.targetFps = this.targetFps;
      var1.encodingCpuUsage = this.encodingCpuUsage;
      var1.disableAdaptiveBitrate = this.disableAdaptiveBitrate;
      var1.verticalFlip = this.verticalFlip;
      return var1;
   }

   public Object clone() throws CloneNotSupportedException {
      return this.clone();
   }
}
