package org.example.controller;

import io.javalin.apibuilder.CrudHandler;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import org.example.database.dao.UserDAO;
import org.example.database.entity.User;
import org.example.exception.CustomException;
import org.jdbi.v3.core.statement.UnableToExecuteStatementException;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.example.utils.EmailValidator;
import java.time.Instant;

/**
 * This is the controller for User operations.
 */
public class UserController implements CrudHandler {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    @NotNull
    public static final UserDAO dao = new UserDAO();


    @Override
    public void create(@NotNull Context context) {
        User newUser = getUserFromContext(context);
        if (null == newUser.getEmailAddress() || newUser.getEmailAddress().isEmpty()) {
            throw new CustomException(CustomException.ErrorCode.INVALID_INPUT, "email address required", null);
        }
        if (null == newUser.getUserName() || newUser.getUserName().isEmpty()) {
            throw new CustomException(CustomException.ErrorCode.INVALID_INPUT, "user name required", null);
        }

        try {
            newUser.setCreated(Instant.now());
            newUser.setActive(true);
            newUser.save(true);
            context.status(HttpStatus.OK);
        }
        catch (UnableToExecuteStatementException unableToExecuteStatementException){
            if (unableToExecuteStatementException.getMessage().contains("Unique index or primary key violation"))
                throw new CustomException(CustomException.ErrorCode.RECORD_ALREADY_EXITS);
        }
        catch (Exception e){
            throw new CustomException(CustomException.ErrorCode.INTERNAL_SERVER_ERROR, e);
        }

    }

    @Override
    public void delete(@NotNull Context context, @NotNull String userId) {
        try {
            if (!userId.isEmpty()) {
                dao.deleteById(Long.parseLong(userId));
                context.status(HttpStatus.OK);
            }
        }
        catch (NumberFormatException numberFormatException){
            throw new CustomException(CustomException.ErrorCode.NUMBER_FORMAT_EXCEPTION, userId, true);
        }
        catch (IllegalStateException illegalStateException){
            throw new CustomException(CustomException.ErrorCode.RECORD_NOT_FOUND, illegalStateException.getMessage(), true);
        }

    }

    @Override
    public void getAll(@NotNull Context context) {
        context.json(dao.getAllUsers());
        context.status(HttpStatus.OK);
    }

    @Override
    public void getOne(@NotNull Context context, @NotNull String userId) {
        try {
            if (!userId.isEmpty()) {
                context.json(dao.getById(Long.parseLong(userId)));
                context.status(HttpStatus.OK);
            }
        }
        catch (NumberFormatException numberFormatException){
            throw new CustomException(CustomException.ErrorCode.NUMBER_FORMAT_EXCEPTION, userId, true);
        }
        catch (IllegalStateException illegalStateException){
            throw new CustomException(CustomException.ErrorCode.RECORD_NOT_FOUND, illegalStateException.getMessage(), true);
        }
    }

    @Override
    public void update(@NotNull Context context, @NotNull String s) {
        User existingUser = getUserFromContext(context);
        if (null == existingUser.getEmailAddress() || existingUser.getEmailAddress().isEmpty()) {
            throw new CustomException(CustomException.ErrorCode.INVALID_INPUT, "email address required", null);
        }
        if (null == existingUser.getUserName() || existingUser.getUserName().isEmpty()) {
            throw new CustomException(CustomException.ErrorCode.INVALID_INPUT, "user name required", null);
        }
        try{
            existingUser.save(true);
        }
        catch (IllegalStateException illegalStateException){
            throw new CustomException(CustomException.ErrorCode.RECORD_NOT_FOUND, illegalStateException.getMessage(), true);
        }
    }

    public void findUserByEmail(@NotNull Context context, @NotNull String emailAddress) {

        if (!EmailValidator.isValidEmailAddress(emailAddress)){
            throw new CustomException(CustomException.ErrorCode.INVALID_EMAIL_ADDRESS);
        }
        try {
            User newUser = dao.findByEmailAddress(emailAddress);
            context.json(newUser);
            context.status(HttpStatus.OK);
        }
        catch (IllegalStateException illegalStateException){
            throw new CustomException(CustomException.ErrorCode.RECORD_NOT_FOUND, illegalStateException.getMessage(), true);
        }
    }

    /**
     * This method will throw an exception in case of any problems converting input JSON to user object.
     * @param context
     * @return
     */
    public User getUserFromContext(@NotNull Context context){
        try {
            return context.bodyAsClass(User.class);
        }
        catch (Exception exception){
            throw new CustomException(CustomException.ErrorCode.INTERNAL_SERVER_ERROR, exception.getMessage());
        }
    }
}
