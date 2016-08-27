package com.chaos.object.id.util;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

/**
 * Created by zcfrank1st on 8/27/16.
 */
public class ConfigLoader {
    public Config getConf () {
        return ConfigFactory.load();
    }
}
