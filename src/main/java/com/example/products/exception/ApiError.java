package com.example.products.exception;

import java.time.Instant;
import java.util.List;

public class ApiError {
  private String error;
  private String message;
  private Integer status;
  private String path;
  private Instant timestamp;
  private List<ValidationError> errors;

  public ApiError() {
    this.timestamp = Instant.now();
  }

  public ApiError(String error, String message, Integer status, String path, List<ValidationError> errors) {
    this.error = error;
    this.message = message;
    this.status = status;
    this.path = path;
    this.errors = errors;
    this.timestamp = Instant.now();
  }

  public String getError() {
    return error;
  }

  public void setError(String error) {
    this.error = error;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public Instant getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Instant timestamp) {
    this.timestamp = timestamp;
  }

  public List<ValidationError> getErrors() {
    return errors;
  }

  public void setErrors(List<ValidationError> errors) {
    this.errors = errors;
  }

  public static class ValidationError {
    private String field;
    private String message;
    
    public ValidationError(String field, String message) {
      this.field = field;
      this.message = message;
    }

    public String getField() {
      return field;
    }

    public void setField(String field) {
      this.field = field;
    }

    public String getMessage() {
      return message;
    }

    public void setMessage(String message) {
      this.message = message;
    }
  }
}