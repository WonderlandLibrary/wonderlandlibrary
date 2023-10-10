// 
// Decompiled by Procyon v0.5.36
// 

package oshi.hardware;

import java.util.Properties;
import oshi.util.Util;
import oshi.util.FileUtil;
import java.util.regex.Matcher;
import oshi.util.ParseUtil;
import java.util.regex.Pattern;
import oshi.util.Memoizer;
import java.util.function.Supplier;
import oshi.annotation.concurrent.Immutable;
import java.util.List;
import oshi.annotation.concurrent.ThreadSafe;

@ThreadSafe
public interface CentralProcessor
{
    ProcessorIdentifier getProcessorIdentifier();
    
    long getMaxFreq();
    
    long[] getCurrentFreq();
    
    List<LogicalProcessor> getLogicalProcessors();
    
    List<PhysicalProcessor> getPhysicalProcessors();
    
    double getSystemCpuLoadBetweenTicks(final long[] p0);
    
    long[] getSystemCpuLoadTicks();
    
    double[] getSystemLoadAverage(final int p0);
    
    double[] getProcessorCpuLoadBetweenTicks(final long[][] p0);
    
    long[][] getProcessorCpuLoadTicks();
    
    int getLogicalProcessorCount();
    
    int getPhysicalProcessorCount();
    
    int getPhysicalPackageCount();
    
    long getContextSwitches();
    
    long getInterrupts();
    
    public enum TickType
    {
        USER(0), 
        NICE(1), 
        SYSTEM(2), 
        IDLE(3), 
        IOWAIT(4), 
        IRQ(5), 
        SOFTIRQ(6), 
        STEAL(7);
        
        private final int index;
        
        private TickType(final int value) {
            this.index = value;
        }
        
        public int getIndex() {
            return this.index;
        }
        
        private static /* synthetic */ TickType[] $values() {
            return new TickType[] { TickType.USER, TickType.NICE, TickType.SYSTEM, TickType.IDLE, TickType.IOWAIT, TickType.IRQ, TickType.SOFTIRQ, TickType.STEAL };
        }
        
        static {
            $VALUES = $values();
        }
    }
    
    @Immutable
    public static class LogicalProcessor
    {
        private final int processorNumber;
        private final int physicalProcessorNumber;
        private final int physicalPackageNumber;
        private final int numaNode;
        private final int processorGroup;
        
        public LogicalProcessor(final int processorNumber, final int physicalProcessorNumber, final int physicalPackageNumber) {
            this(processorNumber, physicalProcessorNumber, physicalPackageNumber, 0, 0);
        }
        
        public LogicalProcessor(final int processorNumber, final int physicalProcessorNumber, final int physicalPackageNumber, final int numaNode) {
            this(processorNumber, physicalProcessorNumber, physicalPackageNumber, numaNode, 0);
        }
        
        public LogicalProcessor(final int processorNumber, final int physicalProcessorNumber, final int physicalPackageNumber, final int numaNode, final int processorGroup) {
            this.processorNumber = processorNumber;
            this.physicalProcessorNumber = physicalProcessorNumber;
            this.physicalPackageNumber = physicalPackageNumber;
            this.numaNode = numaNode;
            this.processorGroup = processorGroup;
        }
        
        public int getProcessorNumber() {
            return this.processorNumber;
        }
        
        public int getPhysicalProcessorNumber() {
            return this.physicalProcessorNumber;
        }
        
        public int getPhysicalPackageNumber() {
            return this.physicalPackageNumber;
        }
        
        public int getNumaNode() {
            return this.numaNode;
        }
        
        public int getProcessorGroup() {
            return this.processorGroup;
        }
        
        @Override
        public String toString() {
            return "LogicalProcessor [processorNumber=" + this.processorNumber + ", coreNumber=" + this.physicalProcessorNumber + ", packageNumber=" + this.physicalPackageNumber + ", numaNode=" + this.numaNode + ", processorGroup=" + this.processorGroup + "]";
        }
    }
    
    @Immutable
    public static class PhysicalProcessor
    {
        private final int physicalPackageNumber;
        private final int physicalProcessorNumber;
        private final int efficiency;
        private final String idString;
        
        public PhysicalProcessor(final int physicalPackageNumber, final int physicalProcessorNumber) {
            this(physicalPackageNumber, physicalProcessorNumber, 0, "");
        }
        
        public PhysicalProcessor(final int physicalPackageNumber, final int physicalProcessorNumber, final int efficiency, final String idString) {
            this.physicalPackageNumber = physicalPackageNumber;
            this.physicalProcessorNumber = physicalProcessorNumber;
            this.efficiency = efficiency;
            this.idString = idString;
        }
        
        public int getPhysicalPackageNumber() {
            return this.physicalPackageNumber;
        }
        
        public int getPhysicalProcessorNumber() {
            return this.physicalProcessorNumber;
        }
        
        public int getEfficiency() {
            return this.efficiency;
        }
        
        public String getIdString() {
            return this.idString;
        }
        
        @Override
        public String toString() {
            return "PhysicalProcessor [package/core=" + this.physicalPackageNumber + "/" + this.physicalProcessorNumber + ", efficiency=" + this.efficiency + ", idString=" + this.idString + "]";
        }
    }
    
    @Immutable
    public static final class ProcessorIdentifier
    {
        private static final String OSHI_ARCHITECTURE_PROPERTIES = "oshi.architecture.properties";
        private final String cpuVendor;
        private final String cpuName;
        private final String cpuFamily;
        private final String cpuModel;
        private final String cpuStepping;
        private final String processorID;
        private final String cpuIdentifier;
        private final boolean cpu64bit;
        private final long cpuVendorFreq;
        private final Supplier<String> microArchictecture;
        
        public ProcessorIdentifier(final String cpuVendor, final String cpuName, final String cpuFamily, final String cpuModel, final String cpuStepping, final String processorID, final boolean cpu64bit) {
            this(cpuVendor, cpuName, cpuFamily, cpuModel, cpuStepping, processorID, cpu64bit, -1L);
        }
        
        public ProcessorIdentifier(final String cpuVendor, final String cpuName, final String cpuFamily, final String cpuModel, final String cpuStepping, final String processorID, final boolean cpu64bit, final long vendorFreq) {
            this.microArchictecture = Memoizer.memoize(this::queryMicroarchitecture);
            this.cpuVendor = cpuVendor;
            this.cpuName = cpuName;
            this.cpuFamily = cpuFamily;
            this.cpuModel = cpuModel;
            this.cpuStepping = cpuStepping;
            this.processorID = processorID;
            this.cpu64bit = cpu64bit;
            final StringBuilder sb = new StringBuilder();
            if (cpuVendor.contentEquals("GenuineIntel")) {
                sb.append(cpu64bit ? "Intel64" : "x86");
            }
            else {
                sb.append(cpuVendor);
            }
            sb.append(" Family ").append(cpuFamily);
            sb.append(" Model ").append(cpuModel);
            sb.append(" Stepping ").append(cpuStepping);
            this.cpuIdentifier = sb.toString();
            if (vendorFreq > 0L) {
                this.cpuVendorFreq = vendorFreq;
            }
            else {
                final Pattern pattern = Pattern.compile("@ (.*)$");
                final Matcher matcher = pattern.matcher(cpuName);
                if (matcher.find()) {
                    final String unit = matcher.group(1);
                    this.cpuVendorFreq = ParseUtil.parseHertz(unit);
                }
                else {
                    this.cpuVendorFreq = -1L;
                }
            }
        }
        
        public String getVendor() {
            return this.cpuVendor;
        }
        
        public String getName() {
            return this.cpuName;
        }
        
        public String getFamily() {
            return this.cpuFamily;
        }
        
        public String getModel() {
            return this.cpuModel;
        }
        
        public String getStepping() {
            return this.cpuStepping;
        }
        
        public String getProcessorID() {
            return this.processorID;
        }
        
        public String getIdentifier() {
            return this.cpuIdentifier;
        }
        
        public boolean isCpu64bit() {
            return this.cpu64bit;
        }
        
        public long getVendorFreq() {
            return this.cpuVendorFreq;
        }
        
        public String getMicroarchitecture() {
            return this.microArchictecture.get();
        }
        
        private String queryMicroarchitecture() {
            String arch = null;
            final Properties archProps = FileUtil.readPropertiesFromFilename("oshi.architecture.properties");
            final StringBuilder sb = new StringBuilder();
            final String ucVendor = this.cpuVendor.toUpperCase();
            if (ucVendor.contains("AMD")) {
                sb.append("amd.");
            }
            else if (ucVendor.contains("ARM")) {
                sb.append("arm.");
            }
            else if (ucVendor.contains("IBM")) {
                final int powerIdx = this.cpuName.indexOf("_POWER");
                if (powerIdx > 0) {
                    arch = this.cpuName.substring(powerIdx + 1);
                }
            }
            else if (ucVendor.contains("APPLE")) {
                sb.append("apple.");
            }
            if (Util.isBlank(arch) && !sb.toString().equals("arm.")) {
                sb.append(this.cpuFamily);
                arch = archProps.getProperty(sb.toString());
            }
            if (Util.isBlank(arch)) {
                sb.append('.').append(this.cpuModel);
                arch = archProps.getProperty(sb.toString());
            }
            if (Util.isBlank(arch)) {
                sb.append('.').append(this.cpuStepping);
                arch = archProps.getProperty(sb.toString());
            }
            return Util.isBlank(arch) ? "unknown" : arch;
        }
        
        @Override
        public String toString() {
            return this.getIdentifier();
        }
    }
}
