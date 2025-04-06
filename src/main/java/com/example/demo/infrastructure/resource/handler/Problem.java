package com.example.demo.infrastructure.resource.handler;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.time.OffsetDateTime;
import java.util.List;

@JsonInclude(Include.NON_NULL)
public class Problem {
  private Integer status;
  private String type;
  private String title;
  private String detail;
  private String userMessage;
  private String code;
  private List<Object> objects;
  private final OffsetDateTime timestamp;

  public Problem() {
    this.timestamp = OffsetDateTime.now();
  }

  public Integer getStatus() {
    return status;
  }

  public String getType() {
    return type;
  }

  public String getTitle() {
    return title;
  }

  public String getDetail() {
    return detail;
  }

  public String getUserMessage() {
    return userMessage;
  }

  public String getCode() {
    return code;
  }

  public OffsetDateTime getTimestamp() {
    return timestamp;
  }

  public Problem withStatus(Integer status) {
    this.status = status;
    return this;
  }

  public Problem withType(String type) {
    this.type = type;
    return this;
  }

  public Problem withTitle(String title) {
    this.title = title;
    return this;
  }

  public Problem withDetail(String detail) {
    this.detail = detail;
    return this;
  }

  public Problem withUserMessage(String userMessage) {
    this.userMessage = userMessage;
    return this;
  }

  public Problem withCode(String code) {
    this.code = code;
    return this;
  }

  public Problem withObjects(List<Object> objects) {
    this.objects = objects;
    return this;
  }

  public static class Object {
    private final String name;
    private final String userMessage;

    public Object(String name, String userMessage) {
      this.name = name;
      this.userMessage = userMessage;
    }

    public String getName() {
      return name;
    }

    public String getUserMessage() {
      return userMessage;
    }
  }

  public List<Object> getObjects() {
    return objects;
  }
}
