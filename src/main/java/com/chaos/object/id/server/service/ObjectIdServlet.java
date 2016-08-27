package com.chaos.object.id.server.service;

import com.chaos.object.id.core.ObjectId;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by zcfrank1st on 8/27/16.
 */
@WebServlet(
        name = "Object id servlet",
        urlPatterns = {"/object/id"}
)
public class ObjectIdServlet extends HttpServlet {
    private final static ObjectId objectId = new ObjectId();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        ServletOutputStream out = resp.getOutputStream();
        out.write(Long.toHexString(objectId.getObjectId()).getBytes());
        out.flush();
        out.close();
    }
}
