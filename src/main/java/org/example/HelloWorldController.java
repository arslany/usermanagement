package org.example;

import io.javalin.apibuilder.CrudHandler;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import org.jetbrains.annotations.NotNull;

/**
 * The main purpose of this controller is to demonstrate the use of CrudHandler
 * and implement the default end points.
 */
public class HelloWorldController implements CrudHandler {

    /**
     * post: http://localhost:7000/hello
     * body :
     * {
     *     "first": " first param value",
     *     "second": "second param value"
     * }
     */
    @Override
    public void create(@NotNull Context context) {
        String body = context.body();
        context.result("Create service called with body: " + body);
    }

    @Override
    public void delete(@NotNull Context context, @NotNull String s) {
        context.result("Delete service called with parameter: " + s);
        context.status(HttpStatus.OK);

    }

    @Override
    public void getAll(@NotNull Context context) {
        context.json("getAll service called");
        context.status(HttpStatus.OK);
    }

    @Override
    public void getOne(@NotNull Context context, @NotNull String s) {
        context.json("getOne service called");
        context.status(HttpStatus.OK);
    }

    @Override
    public void update(@NotNull Context context, @NotNull String s) {
        context.json("update service called");
        context.status(HttpStatus.OK);
    }
}
