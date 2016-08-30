package com.chaos.object.id.server;

import com.chaos.object.id.core.ObjectId;
import com.chaos.object.id.util.ConfigLoader;
import fi.iki.elonen.NanoHTTPD;

import java.io.IOException;
import java.net.InetAddress;

/**
 * Created by zcfrank1st on 8/27/16.
 */
public class HttpServer extends NanoHTTPD {
    final private static int port = new ConfigLoader().getConf().getInt("http.server.port");

    public HttpServer(int port) throws IOException {
        super(port);
        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        System.out.println("\nRunning! http://" + InetAddress.getLocalHost().getHostAddress() + ":8080/ \n");
    }

    public static void main(String[] args) {
        try {
            new HttpServer(port);
        } catch (IOException ioe) {
            System.err.println("Couldn't start server:\n" + ioe);
        }
    }

    @Override
    public Response serve(IHTTPSession session) {
        return newFixedLengthResponse(Long.toHexString(new ObjectId().getObjectId()));
    }
}
