package com.chat.interceptor;

import com.chat.utils.JSONResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

/**
 * @author ZhouHua
 * @date 2019/11/4
 */
@RestControllerAdvice
public class ExceptionInterceptor {

    @ExceptionHandler(value = ConstraintViolationException.class)
    public JSONResult handleConstraintViolationException(ConstraintViolationException ex) {
        StringBuilder errorMsg = new StringBuilder();
        Set<ConstraintViolation<?>> set = ex.getConstraintViolations();
        for (ConstraintViolation<?> constraintViolation : set) {
            errorMsg.append(constraintViolation.getMessage());
        }
        return JSONResult.errorMsg(errorMsg.toString());
    }

    @ExceptionHandler(value = RuntimeException.class)
    public JSONResult handleRuntimeException(RuntimeException ex) {
        ex.printStackTrace();
        return JSONResult.errorMsg(ex.getMessage());
    }

    /**
     *  校验错误拦截处理
     * @return 错误信息
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public JSONResult handleException(MethodArgumentNotValidException ex){
        StringBuilder errorMsg = new StringBuilder();
        BindingResult result = ex.getBindingResult();
        for (ObjectError objectError : result.getAllErrors()) {
            FieldError fieldError = (FieldError) objectError;
            errorMsg.append(fieldError.getDefaultMessage());
        }
        return JSONResult.errorMsg(errorMsg.toString());
    }
}
