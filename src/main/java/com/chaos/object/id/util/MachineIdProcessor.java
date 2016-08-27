package com.chaos.object.id.util;

import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.JedisCluster;

/**
 * Created by zcfrank1st on 8/27/16.
 */
public class MachineIdProcessor {
    final private static String OBJECT_ID = "OBJECT_ID";

    private static JedisCluster jedisCluster;

    private MachineIdProcessor () {
        throw new RuntimeException("cannot new object, use getInstance");
    }

    public static MachineIdProcessor createInstance() {
        String clusterHostsPorts = new ConfigLoader().getConf().getString("redis.clusters");
        if (StringUtils.isEmpty(clusterHostsPorts)) {
            return null;
        }

        // TODO init jedisCluster

        return new MachineIdProcessor();
    }

    public long applyForMachineId(String uniqueTag) {
        String oldId = getBindedMachineId(uniqueTag);
        if (!StringUtils.isEmpty(oldId)) {
            return Long.parseLong(oldId);
        }

        Long id = jedisCluster.incr(OBJECT_ID);
        bindMachineId(uniqueTag, id);
        return id;
    }

    private String getBindedMachineId(String uniqueTag) {
        return jedisCluster.get(uniqueTag);
    }

    private void bindMachineId(String key, Long id) {
        jedisCluster.set(key, id.toString());
    }
}
