package ru.project.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import ru.project.server.servlets.BaseServlet;
import ru.project.server.servlets.HelloServlet;

public class Main {

    public static void main(String[] args) {

        int port = args.length > 0 ? Integer.parseInt(args[0]) : 8080;

        Server server = new Server(port);
        ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);

        Gson gson = new GsonBuilder().setLenient()
                                     .create();

        final BaseServlet hello = new HelloServlet(gson);
        contextHandler.addServlet(new ServletHolder(hello), hello.getUrl());

        HandlerList handlerList = new HandlerList();
        handlerList.setHandlers(new Handler[]{contextHandler});
        server.setHandler(handlerList);


        try {
            server.start();
            server.join();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
