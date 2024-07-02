package ru.dragomirov.servlets;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "MainFormServlet", urlPatterns = "/my")
public class MainFormServlet extends BaseServlet {
    @Override
    public void init() {}

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp){}
}
