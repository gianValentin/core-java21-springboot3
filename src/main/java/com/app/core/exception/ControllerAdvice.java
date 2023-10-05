package com.app.core.exception;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.app.core.entity.dto.ArgumentNotValidDto;
import com.app.core.entity.dto.ErrorDto;
import com.app.core.utils.CustomCodeException;

@RestControllerAdvice
public class ControllerAdvice {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ArgumentNotValidDto> handleValidationExceptions(MethodArgumentNotValidException ex) {
		List<ErrorDto> erros = ex
				.getBindingResult().getAllErrors().stream()
				.map((error) -> ErrorDto.builder().code(error.getCode()).field(((FieldError) error).getField()).message(error.getDefaultMessage()).build())
				.sorted(Comparator.comparing(ErrorDto::getField))
				.collect(Collectors.toList());
		ArgumentNotValidDto argumentNotValidDto = ArgumentNotValidDto.builder().code(CustomCodeException.CODE_500)
				.errors(erros).build();
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(argumentNotValidDto);
	}

	@ExceptionHandler(value = RuntimeException.class)
	public ResponseEntity<ErrorDto> runtimeExceptionHandler(RuntimeException exception) {
		ErrorDto error = ErrorDto.builder().code(CustomCodeException.CODE_500).message(exception.getMessage()).build();
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}

	@ExceptionHandler(value = CJNotFoundException.class)
	public ResponseEntity<ErrorDto> notFoundExceptionHandler(CJNotFoundException exception) {
		ErrorDto error = ErrorDto.builder().code(exception.getCode()).message(exception.getMessage()).build();
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
	}
}
