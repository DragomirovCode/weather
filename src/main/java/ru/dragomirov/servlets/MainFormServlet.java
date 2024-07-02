package ru.dragomirov.servlets;

import jakarta.servlet.annotation.WebServlet;

@WebServlet(name = "MainFormServlet", urlPatterns = "/my")
public class MainFormServlet extends BaseServlet {
    @Override
    public void init() {}
}
