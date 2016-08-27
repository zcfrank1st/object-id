package com.chaos.object.id.core;

import com.chaos.object.id.util.ConfigLoader;
import com.google.common.base.Preconditions;
import com.typesafe.config.Config;

import java.net.SocketException;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by zcfrank1st on 8/24/16.
 */
public class ObjectId {
    private static AtomicLong num = new AtomicLong(0);

    private static long businessBits = 4L;
    private static long serialBits = 10L;
    private static long machineBits = 8L;

    private static long maxBusinessId = ~(-1 << businessBits);
    private static long maxSerialNumber = ~(-1 << serialBits);
    private static long maxMachineId = ~(-1 << machineBits);

    private static long machineId;
    private static long businessId;

    static {
        Config conf = new ConfigLoader().getConf();
        machineId = conf.getLong("machine.id"); // 8 bits
        businessId = conf.getLong("business.id"); // 4 bits
        Preconditions.checkArgument(machineId <= maxMachineId, "exceed max machineId, max machineId [" + maxMachineId + "]");
        Preconditions.checkArgument(businessId <= maxBusinessId, "exceed max businessId, max businessId [" + maxBusinessId + "]");
    }

    private long getTimestamp () { // 42 bits
        return System.currentTimeMillis();
    }

    private long getSerialNumber () { //  10 bits
        long c = num.getAndIncrement();
        if (c != maxSerialNumber) {
            return c;
        }
        num.set(0);
        return maxSerialNumber;
    }

    public long getObjectId () {
        return getTimestamp() << (machineBits + businessBits + serialBits) |
                machineId << (businessBits + serialBits) |
                businessId << serialBits |
                getSerialNumber();
    }
}
