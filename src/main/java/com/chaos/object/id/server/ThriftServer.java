package com.chaos.object.id.server;

import com.chaos.object.id.server.service.ObjectIdService;
import com.chaos.object.id.server.service.gen.ObjectIdGenerator;
import com.chaos.object.id.util.ConfigLoader;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.server.TNonblockingServer;
import org.apache.thrift.server.TServer;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TTransportException;

/**
 * Created by zcfrank1st on 8/27/16.
 */
public class ThriftServer {
    final private static int port = new ConfigLoader().getConf().getInt("thrift.server.port");

    public static void main(String[] args) throws TTransportException {
        TNonblockingServerSocket tnbSocketTransport = new TNonblockingServerSocket(port);
        TNonblockingServer.Args tnbArgs = new TNonblockingServer.Args(tnbSocketTransport);
        tnbArgs.processor(new ObjectIdGenerator.Processor<>(new ObjectIdService()));
        tnbArgs.transportFactory(new TFramedTransport.Factory());
        tnbArgs.protocolFactory(new TCompactProtocol.Factory());
        TServer server = new TNonblockingServer(tnbArgs);
        server.serve();
    }
}
