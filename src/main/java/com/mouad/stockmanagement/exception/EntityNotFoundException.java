package com.mouad.stockmanagement.exception;

import lombok.Getter;

// La classe "EntityNotFoundException" est une sous-classe de "RuntimeException" utilisée dans App pour signaler des erreurs lorsqu'une entité spécifique n'est pas trouvée dans l'application.
public class EntityNotFoundException extends RuntimeException {

    @Getter // L'annotation @Getter est utilisée ici pour générer automatiquement un getter pour le champ errorCode (getErrorCode()) en évitant de le créer manuellement
    private ErrorCodes errorCode;

    // NB: la classe hérite de "RuntimeException", qui possède une méthode "getMessage()" pour accéder au message de l'exception.
    // Cette méthode "getMessage()" est héritée par la classe "EntityNotFoundException", ce qui lui permet d'accéder au message passé lors de la création de l'instance de l'exception via le constructeur super(message)

    public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException(String message, Throwable reason) {
        super(message, reason);
    }

    public EntityNotFoundException(String message, Throwable reason, ErrorCodes errorCode) {
        super(message, reason);
        this.errorCode = errorCode;
    }

    public EntityNotFoundException(String message, ErrorCodes errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

}