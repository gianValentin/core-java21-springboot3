package com.app.core.security.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthDto {
	@JsonProperty("access_token")
	  private String accessToken;
	  @JsonProperty("refresh_token")
	  private String refreshToken;
}
