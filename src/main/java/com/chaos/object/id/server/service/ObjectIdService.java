package com.chaos.object.id.server.service;

import com.chaos.object.id.core.ObjectId;
import com.chaos.object.id.server.service.gen.ObjectIdGenerator;
import org.apache.thrift.TException;

/**
 * Created by zcfrank1st on 8/27/16.
 */
public class ObjectIdService implements ObjectIdGenerator.Iface{
    final private static ObjectId objectId = new ObjectId();

    @Override
    public String getObjectId() throws TException {
        return Long.toHexString(objectId.getObjectId());
    }
}
