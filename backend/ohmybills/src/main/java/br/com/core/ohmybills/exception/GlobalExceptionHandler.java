package br.com.core.ohmybills.exception;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // 400 - Erros de validação em payload (Bean Validation em @RequestBody)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ProblemDetail handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpServletRequest req) {
        var pd = base(HttpStatus.BAD_REQUEST, "Requisição inválida", "Erros de validação no corpo da requisição.", req);
        List<Map<String, Object>> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> Map.of(
                        "field", fe.getField(),
                        "message", Optional.ofNullable(fe.getDefaultMessage()).orElse("valor inválido"),
                        "rejected", fe.getRejectedValue()))
                .collect(Collectors.toList());
        pd.setProperty("errors", errors);
        log.warn("400 Validation error on {} -> {}", req.getRequestURI(), errors);
        return pd;
    }

    // 400 - Erros de validação em parâmetros (@RequestParam/@PathVariable)
    @ExceptionHandler(ConstraintViolationException.class)
    ProblemDetail handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest req) {
        var pd = base(HttpStatus.BAD_REQUEST, "Requisição inválida", "Erros de validação em parâmetros.", req);
        List<Map<String, Object>> errors = ex.getConstraintViolations().stream()
                .map(v -> Map.of(
                        "param", v.getPropertyPath().toString(),
                        "message", v.getMessage(),
                        "rejected", v.getInvalidValue()))
                .collect(Collectors.toList());
        pd.setProperty("errors", errors);
        log.warn("400 Constraint violation on {} -> {}", req.getRequestURI(), errors);
        return pd;
    }

    // 400 - JSON malformado ou conversão falhou
    @ExceptionHandler(HttpMessageNotReadableException.class)
    ProblemDetail handleNotReadable(HttpMessageNotReadableException ex, HttpServletRequest req) {
        log.warn("400 Not readable on {}: {}", req.getRequestURI(), ex.getMostSpecificCause().getMessage());
        return base(HttpStatus.BAD_REQUEST, "Requisição inválida", "Formato do corpo inválido ou não legível.", req);
    }

    // 400 - Parâmetro obrigatório ausente
    @ExceptionHandler(MissingServletRequestParameterException.class)
    ProblemDetail handleMissingParam(MissingServletRequestParameterException ex, HttpServletRequest req) {
        var detail = "Parâmetro obrigatório ausente: " + ex.getParameterName();
        log.warn("400 Missing parameter on {}: {}", req.getRequestURI(), detail);
        return base(HttpStatus.BAD_REQUEST, "Requisição inválida", detail, req);
    }

    // 400 - Tipo de parâmetro incompatível
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    ProblemDetail handleTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest req) {
        var detail = "Parâmetro '" + ex.getName() + "' com tipo inválido.";
        log.warn("400 Type mismatch on {}: {}", req.getRequestURI(), detail);
        return base(HttpStatus.BAD_REQUEST, "Requisição inválida", detail, req);
    }

    // 404 - Rota não encontrada (necessita propriedade para lançar NoHandlerFoundException)
    @ExceptionHandler(NoHandlerFoundException.class)
    ProblemDetail handleNoHandler(NoHandlerFoundException ex, HttpServletRequest req) {
        log.warn("404 Not found: {} {}", ex.getHttpMethod(), ex.getRequestURL());
        return base(HttpStatus.NOT_FOUND, "Recurso não encontrado", "A rota solicitada não foi encontrada.", req);
    }

    // 404 - Rota não encontrada (necessita propriedade para lançar NoHandlerFoundException)
    @ExceptionHandler(EntityNotFoundException.class)
    ProblemDetail handleEntityNotFound(EntityNotFoundException ex, HttpServletRequest req) {
        log.warn("404 Entity not found on {}: {}", req.getRequestURI(), rootMsg(ex));
        return base(HttpStatus.NOT_FOUND, "Recurso não encontrado", "Entidade solicitada não foi encontrada.", req);
    }

    // 405 - Método não suportado
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    ProblemDetail handleMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpServletRequest req) {
        log.warn("405 Method not supported on {}: {}", req.getRequestURI(), ex.getMethod());
        var pd = base(HttpStatus.METHOD_NOT_ALLOWED, "Método não permitido", "Método HTTP não suportado.", req);
        pd.setProperty("allowedMethods", ex.getSupportedHttpMethods());
        return pd;
    }

    // 415 - Content-Type não suportado
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    ProblemDetail handleMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpServletRequest req) {
        log.warn("415 Media type not supported on {}: {}", req.getRequestURI(), ex.getContentType());
        var pd = base(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Mídia não suportada", "Tipo de conteúdo não suportado.", req);
        pd.setProperty("supportedMediaTypes", ex.getSupportedMediaTypes());
        return pd;
    }

    // 409 - Violações de integridade (chave única, FK, etc.)
    @ExceptionHandler(DataIntegrityViolationException.class)
    ProblemDetail handleDataIntegrity(DataIntegrityViolationException ex, HttpServletRequest req) {
        log.warn("409 Data integrity on {}: {}", req.getRequestURI(), rootMsg(ex));
        return base(HttpStatus.CONFLICT, "Conflito de dados", "Operação viola regras de integridade.", req);
    }

    // Propaga códigos e razões definidos via ResponseStatusException
    @ExceptionHandler(ResponseStatusException.class)
    ProblemDetail handleResponseStatus(ResponseStatusException ex, HttpServletRequest req) {
        var pd = base(HttpStatus.valueOf(ex.getStatusCode().value()), null, ex.getReason(), req);
        logAt(HttpStatus.valueOf(pd.getStatus()), req.getRequestURI(), ex.getReason());
        return pd;
    }

    // Fallback 500 - erro inesperado
    @ExceptionHandler(Exception.class)
    ProblemDetail handleUnexpected(Exception ex, HttpServletRequest req) {
        log.error("500 Unexpected error on {}:", req.getRequestURI(), ex);
        return base(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno", "Ocorreu um erro não esperado.", req);
    }

    // Utilitário para criar ProblemDetail com metadados consistentes
    private ProblemDetail base(HttpStatus status, String title, String detail, HttpServletRequest req) {
        ProblemDetail pd = ProblemDetail.forStatus(status);
        if (title != null) pd.setTitle(title);
        if (detail != null) pd.setDetail(detail);
        pd.setProperty("path", req.getRequestURI());
        pd.setProperty("timestamp", Instant.now().toString());
        return pd;
    }

    private static String rootMsg(Throwable t) {
        Throwable r = t;
        while (r.getCause() != null) r = r.getCause();
        return Optional.ofNullable(r.getMessage()).orElse(t.getMessage());
    }

    private void logAt(HttpStatus status, Object... args) {
        if (status.is5xxServerError()) {
            log.error("ResponseStatus on {}: {}", args);
        } else if (status.is4xxClientError()) {
            log.warn("ResponseStatus on {}: {}", args);
        } else {
            log.info("ResponseStatus on {}: {}", args);
        }
    }
}