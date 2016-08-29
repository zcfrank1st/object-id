package com.chaos.object.id.core;

import com.chaos.object.id.util.ConfigLoader;
import com.chaos.object.id.util.MachineIdProcessor;
import com.google.common.base.Preconditions;
import com.typesafe.config.Config;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by zcfrank1st on 8/24/16.
 */
public class ObjectId {
    final private static ReentrantLock lock = new ReentrantLock();

    final private static long businessBits = 4L;
    final private static long serialBits = 10L;
    final private static long machineBits = 8L;

    final private static long maxBusinessId = ~(-1 << businessBits);
    final private static long maxSerialNumber = ~(-1 << serialBits);
    final private static long maxMachineId = ~(-1 << machineBits);

    final private static long machineId;
    final private static long businessId;

    final private static MachineIdProcessor machineIdProcessor = MachineIdProcessor.createInstance();

    static {
        Config conf = new ConfigLoader().getConf();

        if (null == machineIdProcessor) {
            machineId = conf.getLong("machine.id"); // 8 bits
        } else {
            // TODO unique key /etc/machine-id
            machineId = machineIdProcessor.applyForMachineId();
        }
        Preconditions.checkArgument(machineId <= maxMachineId, "exceed max machineId, max machineId [" + maxMachineId + "]");

        businessId = conf.getLong("business.id"); // 4 bits
        Preconditions.checkArgument(businessId <= maxBusinessId, "exceed max businessId, max businessId [" + maxBusinessId + "]");
    }

    private static AtomicLong num = new AtomicLong(0);

    private long getTimestamp () { // 42 bits
        return System.currentTimeMillis();
    }

    private long getSerialNumber () { //  10 bits
        lock.lock();
        try {
            long c = num.getAndIncrement();
            if (c != maxSerialNumber) {
                return c;
            }
            num.set(0);
            return maxSerialNumber;
        } finally {
            lock.unlock();
        }
    }

    public long getObjectId () {
        return getTimestamp() << (machineBits + businessBits + serialBits) |
                machineId << (businessBits + serialBits) |
                businessId << serialBits |
                getSerialNumber();
    }
}
