package ru.dragomirov.servlets;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(name = "MainServlet", urlPatterns = "")
public class MainServlet extends HttpServlet {
    @Override
    public void init(){}

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            req.getRequestDispatcher("/main.html").forward(req, resp);
        } catch (Exception e) {
            e.getMessage();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String button = req.getParameter("exit");

            switch (button) {
                case "exit":
                    resp.sendRedirect("/login");
                    HttpSession exitSession = req.getSession(false);
                    exitSession.removeAttribute("user");
                    break;
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }
}
