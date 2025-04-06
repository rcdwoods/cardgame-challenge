package com.example.demo.infrastructure.resource.handler;

import com.example.demo.domain.exception.BusinessException;
import com.example.demo.domain.exception.EntityNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import java.time.format.DateTimeParseException;
import java.util.Objects;

@ControllerAdvice
public class ResourceExceptionHandler extends ResponseEntityExceptionHandler {
	public static final String GENERIC_MESSAGE_ERROR = "An unexpected error occur, please contact the system's administrator.";

	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<Object> handleBusinessException(BusinessException ex, WebRequest request) {

		HttpStatus status = HttpStatus.BAD_REQUEST;
		String detail = ex.getMessage();

		Problem problem = createProblem(status, detail)
			.withUserMessage(detail)
			.withCode(ex.getCode());

		return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
	}

	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException ex,
	                                                            WebRequest request) {

		HttpStatus status = HttpStatus.NOT_FOUND;
		String detail = ex.getMessage();

		Problem problem = createProblem(status, detail).
			withUserMessage(detail).
			withCode(ex.getCode());

		return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
	}

	@Override
	protected ResponseEntity<Object> handleExceptionInternal(
		Exception ex,
		Object body,
		HttpHeaders headers,
		HttpStatusCode status,
		WebRequest request
	) {
		if (Objects.isNull(body)) {
			body = new Problem()
				.withTitle(HttpStatus.resolve(status.value()).getReasonPhrase())
				.withStatus(status.value())
				.withUserMessage(GENERIC_MESSAGE_ERROR);
		} else if (body instanceof String) {
			body = new Problem()
				.withTitle((String) body)
				.withStatus(status.value())
				.withUserMessage(GENERIC_MESSAGE_ERROR);
		}

		return super.handleExceptionInternal(ex, body, headers, status, request);
	}

	private Problem createProblem(HttpStatus status, String detail) {
		return new Problem()
			.withStatus(status.value())
			.withDetail(detail);
	}
}
