package org.example.database.controller;

import io.javalin.apibuilder.CrudHandler;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import org.example.database.dao.UserDAO;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is the controller for User operations.
 */
public class UserController implements CrudHandler {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    @NotNull
    public static final UserDAO dao = new UserDAO();


    @Override
    public void create(@NotNull Context context) {

    }

    @Override
    public void delete(@NotNull Context context, @NotNull String s) {

    }

    @Override
    public void getAll(@NotNull Context context) {
        context.json(dao.getAllUsers());
        context.status(HttpStatus.OK);
    }

    @Override
    public void getOne(@NotNull Context context, @NotNull String s) {

    }

    @Override
    public void update(@NotNull Context context, @NotNull String s) {

    }
}
