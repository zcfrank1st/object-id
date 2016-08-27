package com.chaos.object.id.util;

import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.JedisCluster;

/**
 * Created by zcfrank1st on 8/27/16.
 */
public class MachineIdProcessor {
    final private static String OBJECT_ID = "OBJECT_ID";

    // TODO init jedis cluster
    final private static JedisCluster jedisCluster = null;

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
