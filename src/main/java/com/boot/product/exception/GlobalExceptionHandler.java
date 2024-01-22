
package com.boot.product.exception;

import lombok.Data;
import lombok.NoArgsConstructor;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
@Log4j2
public class GlobalExceptionHandler {

	@Data
	@NoArgsConstructor
	public class ApiError {

		private int status;
		private String message;
		private String error;

		public ApiError(HttpStatus httpStatus, String message) {
			if (httpStatus != null) {
				this.status = httpStatus.value();
				this.error = httpStatus.getReasonPhrase();
			}
			this.message = message;
		}
	}

	private ResponseEntity<ApiError> createResponseEntity(HttpStatus httpStatus,
		Exception e)
	{
		return new ResponseEntity<>(new ApiError(httpStatus, e.getMessage()),
			httpStatus);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> methodArgumentNotValidException(MethodArgumentNotValidException exception) {
		log.error(exception.getMessage(), exception);
		ErrorResponse error = new ErrorResponse();
		exception.getBindingResult().getFieldErrors().stream().forEach(item -> {
			error.messages.add(new ErrorMessage(item.getField(), item.getDefaultMessage()));
		});
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(EntityNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<ApiError> handleNotFounds(
		EntityNotFoundException entityNotFoundException)
	{
		return createResponseEntity(HttpStatus.NOT_FOUND, entityNotFoundException);
	}

	@ExceptionHandler(InvalidInputDataException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<ApiError> invalidInputData(
		InvalidInputDataException invalidInputDataException)
	{
		return createResponseEntity(HttpStatus.BAD_REQUEST,
			invalidInputDataException);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<ApiError> constraintViolationException(
			ConstraintViolationException constraintViolationException)
	{
		return createResponseEntity(HttpStatus.BAD_REQUEST,
				constraintViolationException);
	}

	@ExceptionHandler(DuplicateEntryException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	public ResponseEntity<ApiError> duplicateEntry(
			DuplicateEntryException duplicateEntryException)
	{
		return createResponseEntity(HttpStatus.CONFLICT,
				duplicateEntryException);
	}
	
	@ExceptionHandler(UnableToModifyDataException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<ApiError> unableToModifyEntry(
			UnableToModifyDataException unableToModifyDataException)
	{
		return createResponseEntity(HttpStatus.BAD_REQUEST,
				unableToModifyDataException);
	}

	class ErrorResponse {
		public List<ErrorMessage> messages = new ArrayList<>();
	}

	class ErrorMessage {
		public String fieldKey;
		public String message;

		public ErrorMessage(String fieldKey, String message) {
			this.fieldKey = fieldKey;
			this.message = message;
		}
	}

}
