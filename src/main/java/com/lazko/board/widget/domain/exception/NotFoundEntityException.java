package com.lazko.board.widget.domain.exception;


public class NotFoundEntityException extends RuntimeException {
    private final Long entityId;

    public NotFoundEntityException(Long entityId) {
        this.entityId = entityId;
    }

    public NotFoundEntityException(Long entityId, String message) {
        super(message);
        this.entityId = entityId;
    }

    public NotFoundEntityException(Long entityId, String message, Throwable cause) {
        super(message, cause);
        this.entityId = entityId;
    }

    public NotFoundEntityException(Long entityId, Throwable cause) {
        super(cause);
        this.entityId = entityId;
    }

    public NotFoundEntityException(Long entityId, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.entityId = entityId;
    }

    public Long getEntityId() {
        return entityId;
    }

}
