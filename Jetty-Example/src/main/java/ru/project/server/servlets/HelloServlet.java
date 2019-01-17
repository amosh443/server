package ru.project.server.servlets;

import com.google.gson.Gson;
import com.sun.istack.internal.NotNull;
import ru.project.server.storage.DBHelper;
import ru.project.server.storage.ResultHandlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HelloServlet extends BaseServlet {

    public static final String HELLO_MESSAGE = "{\"message\": \"Привет, друг\"}";


    public HelloServlet(Gson gson) {
        super(gson);
    }

    @Override
    @NotNull
    String getPath() {
        return "/hello";
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        DBHelper helper = DBHelper.getInstance();
        try (Connection connection = helper.getConnection()) {
            helper.runQuery(connection, "SELECT * from ciustomers;", new ResultHandlet() {
                @Override
                public void handle(ResultSet rs) throws SQLException {
                    rs.next();
                    String restName = rs.getString("name");
                    try {
                        writeStringResponse(resp, restName);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (SQLException e) {

        }

    }
}
