package com.chaos.object.id.util;

import com.chaos.object.id.server.service.gen.ObjectIdGenerator;
import com.typesafe.config.Config;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

/**
 * Created by zcfrank1st on 8/29/16.
 */
public class ObjectIdThriftClient {

    private static String getObjectId () throws TException {
        Config conf = new ConfigLoader().getConf();

        TTransport transport = new TFramedTransport(new TSocket(conf.getString("thrift.server.host"), conf.getInt("thrift.server.port")), 15000);
        TProtocol protocol = new TCompactProtocol(transport);
        transport.open();
        ObjectIdGenerator.Client client = new ObjectIdGenerator.Client(protocol);
        String objectId = client.getObjectId();
        transport.close();

        return objectId;
    }
}
