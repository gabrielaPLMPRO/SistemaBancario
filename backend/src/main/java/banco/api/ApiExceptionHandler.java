package banco.api;

import banco.domain.RegraNegocioException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.http.converter.HttpMessageNotReadableException;

@RestControllerAdvice
public class ApiExceptionHandler {
    public record Erro(String codigo, String mensagem) { }
    @ExceptionHandler(RegraNegocioException.class)
    public ResponseEntity<Erro> negocio(RegraNegocioException ex) {
        int status=switch(ex.codigo()) {
            case "CLIENTE_NAO_ENCONTRADO","CONTA_NAO_ENCONTRADA" -> 404;
            case "CLIENTE_DUPLICADO","CONTA_DUPLICADA","CLIENTE_JA_POSSUI_CONTA" -> 409;
            default -> 422;
        };
        return ResponseEntity.status(status).body(new Erro(ex.codigo(),ex.getMessage()));
    }
    @ExceptionHandler({MethodArgumentNotValidException.class, HandlerMethodValidationException.class,
        ConstraintViolationException.class, HttpMessageNotReadableException.class,
        MethodArgumentTypeMismatchException.class, NumberFormatException.class})
    public ResponseEntity<Erro> entrada(Exception ex) {
        return ResponseEntity.badRequest().body(new Erro("ENTRADA_INVALIDA", "Confira os campos obrigatórios, tipos e formatos informados."));
    }
}
