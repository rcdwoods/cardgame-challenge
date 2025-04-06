package com.example.demo.infrastructure.resource.handler;

import com.example.demo.domain.exception.BusinessException;
import com.example.demo.domain.exception.EntityNotFoundException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import java.util.List;
import java.util.Objects;

@ControllerAdvice
public class ResourceExceptionHandler extends ResponseEntityExceptionHandler {
	public static final String GENERIC_MESSAGE_ERROR = "An unexpected error occur, please contact the system's administrator.";

	private final MessageSource messageSource;

	public ResourceExceptionHandler(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

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
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		BindingResult bindingResult = ex.getBindingResult();

		return handleValidationInternal(ex, bindingResult, headers, status, request);
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

	private Problem createProblem(HttpStatusCode status, String detail) {
		return new Problem()
			.withStatus(status.value())
			.withDetail(detail);
	}

	private ResponseEntity<Object> handleValidationInternal(Exception ex, BindingResult bindingResult, HttpHeaders headers,
																													HttpStatusCode status, WebRequest request) {
		String detail = "One or more fields are invalid. Fill rightly and try again.";

		List<Problem.Object> problemObjects = bindingResult.getAllErrors().stream()
			.map(objectError -> {

				String message = messageSource.getMessage(objectError, LocaleContextHolder.getLocale());

				String name = objectError.getObjectName();

				if (objectError instanceof FieldError) {
					name =((FieldError) objectError).getField();
				}

				return new Problem.Object(name, message);
			}).toList();

		Problem problem = createProblem(status, detail)
			.withUserMessage(detail)
			.withObjects(problemObjects)
			.withCode("INVALID_DATA");

		return handleExceptionInternal(ex, problem, headers, status, request);
	}
}
