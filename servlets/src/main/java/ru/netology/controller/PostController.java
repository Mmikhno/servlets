package ru.netology.controller;

import com.google.gson.Gson;
import org.springframework.stereotype.Controller;
import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;
import ru.netology.service.PostService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Reader;

@Controller
public class PostController {
    public static final String APPLICATION_JSON = "application/json";
    public static final String NOT_FOUND_ERROR_TEMPLATE = "Post with id = %s not found";
    public static final String DEL_TEMPLATE = "Post with id = %s has been deleted";
    private final PostService service;
    final Gson gson = new Gson();

    public PostController(PostService service) {
        this.service = service;
    }

    public void all(HttpServletResponse response) throws IOException {
        response.setContentType(APPLICATION_JSON);
        final var data = service.all();
        response.getWriter().print(gson.toJson(data));
    }

    public void getById(long id, HttpServletResponse response) throws IOException {
        response.setContentType(APPLICATION_JSON);
        try {
            final var post = service.getById(id);
            response.getWriter().print(gson.toJson(post));
        } catch (NotFoundException e) {
            response.sendError(404, String.format(NOT_FOUND_ERROR_TEMPLATE, id));
        }
    }

    public void save(Reader body, HttpServletResponse response) throws IOException {
        response.setContentType(APPLICATION_JSON);
        final var post = gson.fromJson(body, Post.class);
        final var data = service.save(post);
        response.getWriter().print(gson.toJson(data));
    }

    public void removeById(long id, HttpServletResponse response) throws IOException {
        if (service.removeById(id)) {
            response.getWriter().print(response.getStatus());
            response.getWriter().print("\n" + String.format(DEL_TEMPLATE, id));
        } else {
            response.sendError(404, String.format(NOT_FOUND_ERROR_TEMPLATE, id));
        }
    }
}
