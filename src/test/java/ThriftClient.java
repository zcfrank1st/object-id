import com.chaos.object.id.server.service.gen.ObjectIdGenerator;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

/**
 * Created by zcfrank1st on 8/27/16.
 */
public class ThriftClient {
    public static void main(String[] args) throws TException {
        TTransport transport = new TFramedTransport(new TSocket("localhost", 39987), 15000);
        TProtocol protocol = new TCompactProtocol(transport);
        transport.open();
        ObjectIdGenerator.Client client = new ObjectIdGenerator.Client(protocol);

        System.out.println(client.getObjectId());
        transport.close();
    }
}
