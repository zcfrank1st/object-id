package com.chaos.object.id.util;

import org.apache.catalina.Host;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.util.HashSet;
import java.util.Set;

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
        if (jedisCluster != null) {
            return new MachineIdProcessor();
        }

        String clusterHostsPorts = new ConfigLoader().getConf().getString("redis.clusters");
        if (StringUtils.isEmpty(clusterHostsPorts)) {
            return null;
        }
        String[] hostsPorts = clusterHostsPorts.split(";");

        Set<HostAndPort> hostAndPortSets = new HashSet<>();

        for (String hostPort : hostsPorts) {
            String[] hostPortParts = hostPort.split(":");
            hostAndPortSets.add(new HostAndPort(hostPortParts[0], Integer.valueOf(hostPortParts[1])));
        }

        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setMaxTotal(200);
        poolConfig.setMaxIdle(20);
        poolConfig.setMinIdle(5);
        poolConfig.setMaxWaitMillis(2000);
        jedisCluster = new JedisCluster(hostAndPortSets, 2000, 10, poolConfig);

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
