package ru.project.server.servlets;

import com.google.gson.Gson;
import com.sun.istack.internal.NotNull;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class BaseServlet extends HttpServlet {

    private static final String URL = "/api/v1";

    private final Gson gson;

    public BaseServlet(Gson gson) {
        this.gson = gson;
    }

    abstract String getPath();

    @NotNull
    public String getUrl() {
        return URL + getPath();
    }

    protected void writeStringResponse(HttpServletResponse resp, String response) throws IOException {
        resp.setContentType("application/json; charset=UTF-8");
        resp.getWriter().print(response);
    }

    protected <T> void writeResponse(HttpServletResponse resp, T object) throws IOException {
        String response = gson.toJson(object);
        writeStringResponse(resp, response);
    }

}
