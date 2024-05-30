package ru.dragomirov.servlets;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "RequestJsonServlet", urlPatterns = "/search-by-name")
public class RequestJsonServlet extends HttpServlet {
    @Override
    public void init(){}

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) { }
}
