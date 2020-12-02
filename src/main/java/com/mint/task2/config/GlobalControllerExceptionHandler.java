

package com.mint.task2.config;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.naming.AuthenticationException;
import javax.persistence.EntityNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.StringJoiner;
import java.util.concurrent.atomic.AtomicReference;

@Primary
@RestControllerAdvice
public class GlobalControllerExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalControllerExceptionHandler.class);

    public GlobalControllerExceptionHandler() {
    }


    @ExceptionHandler({ObjectOptimisticLockingFailureException.class})
    protected ResponseEntity<ApiResponseBase<?>> optimisticLockException(ObjectOptimisticLockingFailureException ex) {
        ex.printStackTrace();
        ApiResponseBase<?> apiResponseBase = new ApiResponseBase();
        apiResponseBase.setHasError(true);
        apiResponseBase.setErrorMessage("This record has been updated by another process");
        log.warn(apiResponseBase.getErrorMessage(), ex.fillInStackTrace());
        return new ResponseEntity(apiResponseBase, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({Exception.class, RuntimeException.class})
    protected ResponseEntity<ApiResponseBase<?>> unknownGeneralException(Exception ex) {
        ex.printStackTrace();
        if (ex.getClass().getSimpleName().equalsIgnoreCase("AccessDeniedException")) {
            return this.authException(new AuthenticationException(ex.getMessage()));
        } else if (ex instanceof UndeclaredThrowableException) {
            Throwable undeclaredThrowableException = ((UndeclaredThrowableException) ex).getUndeclaredThrowable();
            if (undeclaredThrowableException instanceof InvocationTargetException) {
                Throwable targetException = ((InvocationTargetException) undeclaredThrowableException).getTargetException();
                return this.mapException(targetException);
            }
        }
        else {
            ApiResponseBase<?> apiResponseBase = new ApiResponseBase();
            apiResponseBase.setHasError(true);
            apiResponseBase.setErrorMessage(this.getErrorMessage(ex));
            log.warn(apiResponseBase.getErrorMessage(), ex.fillInStackTrace());
            return new ResponseEntity(apiResponseBase, HttpStatus.BAD_REQUEST);
        }
        return null;
    }

    private ResponseEntity<ApiResponseBase<?>> mapException(Throwable e) {
         if (e instanceof EntityNotFoundException) {
            return this.entityNotFoundException((EntityNotFoundException)e);

        }
        return null;
    }

    @ExceptionHandler({EntityNotFoundException.class})
    protected ResponseEntity<ApiResponseBase<?>> entityNotFoundException(EntityNotFoundException ex) {
        ApiResponseBase<?> apiResponseBase = new ApiResponseBase();
        apiResponseBase.setHasError(true);
        apiResponseBase.setErrorMessage(this.getErrorMessage(ex));
        log.info(apiResponseBase.getErrorMessage(), ex.fillInStackTrace());
        return new ResponseEntity(apiResponseBase, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler({AuthenticationException.class})
    protected ResponseEntity<ApiResponseBase<?>> authException(AuthenticationException ex) {
        ApiResponseBase<?> apiResponseBase = new ApiResponseBase();
        apiResponseBase.setHasError(true);
        apiResponseBase.setErrorMessage(this.getErrorMessage(ex));
        log.info(apiResponseBase.getErrorMessage(), ex.fillInStackTrace());
        return new ResponseEntity(apiResponseBase, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({Throwable.class})
    protected ResponseEntity<ApiResponseBase<?>> throwable(Throwable e) {
        ApiResponseBase apiResponseBase;
        if (e instanceof InvocationTargetException) {
            InvocationTargetException ex = (InvocationTargetException)e;
            log.info("InvocationTargetException", ex.getCause());
            ex.printStackTrace();
            apiResponseBase = new ApiResponseBase();
            apiResponseBase.setHasError(true);
            apiResponseBase.setErrorMessage(ex.getCause().getMessage());
            return new ResponseEntity(apiResponseBase, HttpStatus.BAD_REQUEST);
        } else {
            log.info("Unknown Throwable", e);
            return null;
        }
    }


    private String getErrorMessage(Exception ex) {
        String unhandledErrMsg = "An internal error occurred while processing your request";
        String genericIconErrMsg = "Invalid request value entered";
        String errMsg = "";
        boolean isKnown = ex instanceof Exception;

        if (isKnown) {
            errMsg = StringUtils.hasText(ex.getMessage()) ? ex.getMessage() : genericIconErrMsg;
        } else {
            for(Throwable throwable = ex.getCause(); throwable != null; throwable = throwable.getCause()) {
                if (throwable instanceof JsonMappingException) {
                    errMsg = this.getJsonErrorMsg((JsonMappingException)throwable);
                    break;
                }

                if (throwable instanceof DateTimeParseException) {
                    errMsg = this.getDateErrorMsg(throwable);
                    break;
                }

                if (throwable instanceof InvalidFormatException) {
                    try {
                        Class<?> type = ((InvalidFormatException)throwable).getTargetType();
                        if (type instanceof Class && type.isEnum()) {
                            errMsg = this.getEnumErrorMsg(errMsg, (InvalidFormatException)throwable, type);
                        }
                        break;
                    } catch (Exception var9) {
                        ;
                    }
                }
            }

            errMsg = StringUtils.hasText(errMsg) ? errMsg : unhandledErrMsg;
        }

        return errMsg;
    }

    private String getEnumErrorMsg(String errMsg, InvalidFormatException throwable, Class<?> type) {
        Object value = throwable.getValue();
        List<Reference> path = throwable.getPath();
        AtomicReference<String> fieldName = new AtomicReference("");
        path.forEach((reference) -> {
            fieldName.set(reference.getFieldName());
        });
        Object[] enums = type.getEnumConstants();
        StringJoiner stringJoiner = new StringJoiner(",");
        Object[] var9 = enums;
        int var10 = enums.length;

        for(int var11 = 0; var11 < var10; ++var11) {
            Object enumName = var9[var11];
            stringJoiner.add(enumName.toString());
        }

        errMsg = String.format("Wrong value '%s' for '%s', expected values are [%s]", value, fieldName.get(), stringJoiner.toString());
        return errMsg;
    }

    private String getDateErrorMsg(Throwable throwable) {
        String errMsg = String.format("Wrong Date/Time format %s", throwable.getMessage().substring(throwable.getMessage().indexOf("'"), throwable.getMessage().lastIndexOf("'")));
        return errMsg;
    }

    private String getJsonErrorMsg(JsonMappingException jsonMappingException) {
        return String.format("Wrong input format: %s", jsonMappingException.getMessage().substring(0, jsonMappingException.getMessage().indexOf("):")));
    }
}
