package oshi.hardware;

public interface HardwareAbstractionLayer {
   Processor[] getProcessors();

   Memory getMemory();
}
