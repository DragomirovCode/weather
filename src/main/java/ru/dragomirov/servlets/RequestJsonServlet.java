package ru.dragomirov.servlets;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;

@WebServlet(name = "RequestJsonServlet", urlPatterns = "/search-by-name")
public class RequestJsonServlet extends HttpServlet {
}
