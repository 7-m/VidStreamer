package com.mfd.viduploader;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@ControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {
	static Logger logger = Logger.getLogger(CustomGlobalExceptionHandler.class.getName());
	// Let Spring BasicErrorController handle the exception, we just override the status code
	@ExceptionHandler(InvalidDataException.class)
	public void springHandleNotFound(HttpServletResponse response, Exception ex) throws IOException {
		logger.log(Level.INFO,"Invalid data in request encountered", ex);
		response.sendError(HttpStatus.BAD_REQUEST.value());
	}

	@ExceptionHandler(UnauthorizedException.class)
	public void unauthHandler(HttpServletResponse response, Exception ex) throws IOException {
		logger.log(Level.INFO,"Unauthorized access occurred", ex);
		response.sendError(HttpStatus.UNAUTHORIZED.value());
	}
}