package edu.pec.dromeas.payload;

import lombok.Data;

@Data
public class SystemStat
{
    private long maxMemory;
    private long freeMemory;
    private long totalMemory;
    private long processors;
}
