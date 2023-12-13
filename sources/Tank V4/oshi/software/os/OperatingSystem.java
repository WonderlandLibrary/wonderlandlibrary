package oshi.software.os;

public interface OperatingSystem {
   String getFamily();

   String getManufacturer();

   OperatingSystemVersion getVersion();
}
