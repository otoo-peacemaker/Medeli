package com.gads.medeli.model.request;

import lombok.*;

import javax.validation.constraints.Email;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
  private String firstname;
  private String lastname;
  private String email;
  private String password;
}
