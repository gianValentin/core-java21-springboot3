package com.app.core.security.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_token")
public class Token {
	  @Id
	  @GeneratedValue
	  public Long id;

	  @Column(unique = true)
	  public String token;

	  @Enumerated(EnumType.STRING)
	  @Builder.Default
	  public TokenType tokenType = TokenType.BEARER;

	  public boolean revoked;

	  public boolean expired;

	  @ManyToOne(fetch = FetchType.LAZY)
	  @JoinColumn(name = "user_id")
	  public SecurityUser user;
}
