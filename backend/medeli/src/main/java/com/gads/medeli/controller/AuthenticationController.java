package com.gads.medeli.controller;


import com.gads.medeli.model.response.AuthenticationResponse;
import com.gads.medeli.model.request.LoginRequest;
import com.gads.medeli.model.request.RegisterRequest;
import com.gads.medeli.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class AuthenticationController {

  @Autowired
  private final AuthenticationService service;

  @PostMapping("/registration")
  public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
    return ResponseEntity.ok(service.register(request));
  }

  @PostMapping("/login")
  public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody LoginRequest request
  ) {
    return ResponseEntity.ok(service.login(request));
  }

}
