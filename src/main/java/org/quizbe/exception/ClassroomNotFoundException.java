package org.quizbe.exception;

public class ClassroomNotFoundException extends RuntimeException {

  public ClassroomNotFoundException() {
  }

  public ClassroomNotFoundException(String message) {
    super(message);
  }

  public ClassroomNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

  public ClassroomNotFoundException(Throwable cause) {
    super(cause);
  }

  public ClassroomNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
