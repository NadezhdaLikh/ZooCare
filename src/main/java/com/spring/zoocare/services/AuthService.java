package com.spring.zoocare.services;

import com.spring.zoocare.exceptions.CustomBackendException;
import com.spring.zoocare.models.database.entities.Employee;
import com.spring.zoocare.models.dtos.requests.AuthRequest;
import com.spring.zoocare.models.dtos.responses.AuthResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final EmployeeService employeeService;

    public AuthResponse authenticate(AuthRequest authRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authRequest.getEmail(),
                    authRequest.getPassword()));

            Employee employee = employeeService.findEmployeeByEmail(authRequest.getEmail());
            String jwtToken = jwtService.generateToken(Map.of("role", employee.getRole().toString()), employee);
            return AuthResponse.builder().token(jwtToken).build();
        } catch (BadCredentialsException badCredentialsException) {
            throw new CustomBackendException("Неверный адрес электронной почты или пароль.", HttpStatus.UNAUTHORIZED);
        } catch (NoSuchElementException noSuchElementException) {
            throw new CustomBackendException("Пользователь с данным адресом электронной почты не найден.", HttpStatus.NOT_FOUND);
        }
    }
}
