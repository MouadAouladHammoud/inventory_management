package com.mouad.stockmanagement.model;

import java.io.Serializable;
import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class AbstractEntity implements Serializable {

  @Id
  @GeneratedValue
  private Integer id;

  @CreatedDate
  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant creationDate;

  @LastModifiedDate
  @Column(name = "updated_at")
  private Instant lastModifiedDate;
}
