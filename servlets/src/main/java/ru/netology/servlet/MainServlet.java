package ru.netology.servlet;

import ru.netology.controller.PostController;
import ru.netology.repository.PostRepository;
import ru.netology.service.PostService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MainServlet extends HttpServlet {
    private PostController controller;
    private String path = null;

    @Override
    public void init() {
        final var repository = new PostRepository();
        final var service = new PostService(repository);
        controller = new PostController(service);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        path = req.getRequestURI();
        ;
        if (path.equals("/api/posts")) {
            controller.all(resp);
            return;
        }
        if (path.matches("/api/posts/\\d+")) {
            controller.getById(countId(path), resp);
            return;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        path = req.getRequestURI();
        if (path.equals("/api/posts")) {
            controller.save(req.getReader(), resp);
            return;
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        path = req.getRequestURI();
        if (path.matches("/api/posts/\\d+")) {
            controller.removeById(countId(path), resp);
            return;
        }
    }

    private static long countId(String path) {
        return Long.parseLong(path.substring(path.lastIndexOf("/") + 1));
    }
}

