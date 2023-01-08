package com.gads.medeli.model.request;

import com.gads.medeli.security.validation.ValidEmail;
import lombok.*;

import javax.validation.constraints.Email;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
  private String firstname;
  private String lastname;
  @ValidEmail
  private String email;
  private String password;
}
