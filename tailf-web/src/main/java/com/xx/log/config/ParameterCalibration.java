package com.xx.log.config;

import com.xx.log.common.pojo.vo.TestVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ParameterCalibration {

    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public TestVO handleMethodArgumentNotValidException(Exception exception) {
        StringBuilder errorInfo = new StringBuilder();
        BindingResult bindingResult = null;
        if (exception instanceof MethodArgumentNotValidException) {
            bindingResult = ((MethodArgumentNotValidException) exception).getBindingResult();
        }
        if (exception instanceof BindException) {
            bindingResult = ((BindException) exception).getBindingResult();
        }
        for (int i = 0; i < bindingResult.getFieldErrors().size(); i++) {
            if (i > 0) {
                errorInfo.append(",");
            }
            FieldError fieldError = bindingResult.getFieldErrors().get(i);
            errorInfo.append(fieldError.getField()).append(" :").append(fieldError.getDefaultMessage());
        }
        log.error(errorInfo.toString());
        //这里返回自己的Result的结果类。
        return TestVO.builder().msg(errorInfo.toString()).code(-1).build();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public TestVO handleDefaultException(Exception exception) {
        log.error(exception.toString());
        //这里返回自己的Result的结果类。
        log.error("Global Exception", exception);
        return TestVO.builder().msg("服务器错误").code(-1).build();
    }
}
