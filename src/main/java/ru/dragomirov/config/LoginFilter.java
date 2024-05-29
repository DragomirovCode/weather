package ru.dragomirov.config;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ru.dragomirov.dao.HibernateSessionCrudDAO;
import ru.dragomirov.entities.Session;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

@WebFilter(value = {
        "/"
})
public class LoginFilter implements Filter {
    private HibernateSessionCrudDAO hibernateSessionCrudDAO = new HibernateSessionCrudDAO();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession(false);

        // Проверяем, есть ли куки с именем "uuid"
        Cookie[] cookies = request.getCookies();
        String uuid = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("uuid".equals(cookie.getName())) {
                    uuid = cookie.getValue();
                    break;
                }
            }
        }

        if (uuid != null) {
            Optional<Session> dbSession = hibernateSessionCrudDAO.findById(uuid);
            if (dbSession.isPresent() && dbSession.get().getExpiresAt().isAfter(LocalDateTime.now())) {
                // Сессия найдена и она еще не истекла, устанавливаем атрибут "user" в сессии сервера
                if (session == null) {
                    session = request.getSession(true);
                }
                session.setAttribute("user", dbSession.get().getUserId());
            }
        }


        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("/login");
        } else {
            filterChain.doFilter(request, response);
        }
    }
}
