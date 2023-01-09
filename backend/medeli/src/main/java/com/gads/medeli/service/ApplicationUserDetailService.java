package com.gads.medeli.service;

import com.gads.medeli.entity.User;
import com.gads.medeli.repository.UserRepository;
import com.gads.medeli.security.jwt.JwtService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ApplicationUserDetailService implements UserDetailsService {
    private final static String USER_NOT_FOUND_MSG = "user with email %s not found";
    private final UserRepository repository;
    /**
     * Locates the user based on the email.
     * @param email the username identifying the user whose data is required.
     * @return a fully populated user record (never <code>null</code>)
     * @throws UsernameNotFoundException if the user could not be found or the user has no
     *                                   GrantedAuthority
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return  repository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                String.format(USER_NOT_FOUND_MSG, email)));
    }

    boolean isPasswordExist(String password) {
        return repository.findByPassword(password).isPresent();
    }

    boolean isEmailAndPasswordExist(String email, String password) {
        return repository.findByPasswordAndEmail(email, password).isPresent();
    }

    public int enableApplicationUser(String email) {
        loadUserByUsername(email);
        return repository.enableAppUser(email);
    }

    boolean isEmailExist(String email) {
        return repository.findByEmail(email).isPresent();
    }

    public String generateJwtTokenByEmail(String email){
        var user = findUserByEmail(email);
       return JwtService.builder().build().generateToken(user);
    }
  public  User findUserByEmail(String email) {
        return repository.findByEmail(email).orElseThrow(() ->
                new UsernameNotFoundException(
                        String.format(USER_NOT_FOUND_MSG, email)));
    }
}
