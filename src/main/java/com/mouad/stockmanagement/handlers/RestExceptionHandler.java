package com.mouad.stockmanagement.handlers;

import com.mouad.stockmanagement.exception.EntityNotFoundException;
import com.mouad.stockmanagement.exception.InvalidEntityException;
import com.mouad.stockmanagement.exception.ErrorCodes;
import com.mouad.stockmanagement.exception.InvalidOperationException;

import java.util.Collections;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
// import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

// mise en œuvre d'un gestionnaire d'exceptions personnalisé (exeption de type "EntityNotFoundException", InvalidEntityException, ...)

@RestControllerAdvice // cette annotation utilisée pour déclarer que cette classe "RestExceptionHandler" comme un conseiller global pour les contrôleurs REST de l'application (afin de gérer les erreurs à tous les niveaux de l'application).
public class RestExceptionHandler extends ResponseEntityExceptionHandler { // "ResponseEntityExceptionHandler" fournit des méthodes pour gérer les exceptions au niveau du contrôleur

    // NB: Cette méthode ici est responsable de la gestion des exceptions de type "EntityNotFoundException" qui peut être levées dans n'importe quel contrôleur REST de l'application
    @ExceptionHandler(EntityNotFoundException.class) // cette annotation signifie que cette méthode ci-dessous gère les exceptions de type "EntityNotFoundException"
    public ResponseEntity<ErrorDto> handleException(EntityNotFoundException exception, WebRequest webRequest) {
        final HttpStatus notFound = HttpStatus.NOT_FOUND;
        final ErrorDto errorDto = ErrorDto.builder() // construire un objet "ErrorDto" contenant les détails de l'erreur, puis renvoie une réponse "ResponseEntity" avec le code d'état HTTP approprié (404 - NOT FOUND)
                .code(exception.getErrorCode())
                .httpCode(notFound.value())
                .message(exception.getMessage())
                .build();

        return new ResponseEntity<>(errorDto, notFound);
    }

    // NB: Cette méthode ici est responsable de la gestion des exceptions de type "InvalidEntityException" qui peut être levées dans n'importe quel contrôleur REST de l'application
    @ExceptionHandler(InvalidEntityException.class) // cette annotation signifie que cette méthode ci-dessous gère les exceptions de type "InvalidEntityException"
    public ResponseEntity<ErrorDto> handleException(InvalidEntityException exception, WebRequest webRequest) {
        final HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        final ErrorDto errorDto = ErrorDto.builder()
                .code(exception.getErrorCode())
                .httpCode(badRequest.value())
                .message(exception.getMessage())
                .errors(exception.getErrors())
                .build();

        return new ResponseEntity<>(errorDto, badRequest);
    }

    /*
    @ExceptionHandler(InvalidOperationException.class)
    public ResponseEntity<ErrorDto> handleException(InvalidOperationException exception, WebRequest webRequest) {
        final HttpStatus notFound = HttpStatus.BAD_REQUEST;
        final ErrorDto errorDto = ErrorDto.builder()
                .code(exception.getErrorCode())
                .httpCode(notFound.value())
                .message(exception.getMessage())
                .build();

        return new ResponseEntity<>(errorDto, notFound);
    }
    */

    /*
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorDto> handleException(BadCredentialsException exception, WebRequest webRequest) {
        final HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        final ErrorDto errorDto = ErrorDto.builder()
                .code(ErrorCodes.BAD_CREDENTIALS)
                .httpCode(badRequest.value())
                .message(exception.getMessage())
                .errors(Collections.singletonList("Login et / ou mot de passe incorrecte"))
                .build();

        return new ResponseEntity<>(errorDto, badRequest);
    }
    */

}
