package com.mouad.stockmanagement.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mouad.stockmanagement.model.UserRole;
import com.mouad.stockmanagement.model.User;
import com.mouad.stockmanagement.repository.UserRepository;

import com.mouad.stockmanagement.token.Token;
import com.mouad.stockmanagement.token.TokenRepository;
import com.mouad.stockmanagement.token.TokenType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;

import org.springframework.http.HttpHeaders;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final jwt.JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final TokenRepository tokenRepository;

    // Dans cet exemple, nous injectons par défaut ces Beans de type String (test et test3) qui sont déjà prédéfinis dans "ApplicationConfig.java" dans la fonction "test_()".
    // Spring Boot prend en considération le type de méthode déclarée, qui dans cet exemple est de type "String" (dans le fichier ApplicationConfig.java: @Bean public String test_() { ... }).
    //   Alors, chaque injection par défaut d'une variable de type String va utiliser par défaut la fonction test_() pour l'implémentation de la construction
    private final String test;
    private final String test3;


    // *** Inscription d'un nouvel utilisateur ***
    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
            .firstName(request.getFirstname())
            .lastName(request.getLastname())
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .userRole(request.getUserRole())
            .build();
        var userSaved = repository.save(user);

        var jwtToken = jwtService.generateToken(user, userSaved.getId()); // appeller une méthode sur le "jwtService" pour générer un Token JWT pour l'utilisateur trouvé.
        var refreshToken = jwtService.generateRefreshToken(user, userSaved.getId());

        saveTokenForUser(userSaved, jwtToken); // Enregistrer le token de l'utilisateur dans la base de données

        return AuthenticationResponse.builder() // construire et retourne une réponse contenant le JWT pour l'envoyer au client.
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }


    // *** Connexion de l'utilisateur ***
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        System.out.println(test); // Output: Hy !! 1

        String test2 = "test2";
        System.out.println(test2); // Output: test2

        // *** Spring Boot implémente un Bean "AuthenticationProvider" par défaut lorsque la méthode "authenticate" de "authenticationManager" est utilisée, en utilisant un Bean de "UserDetailsService" et de "PasswordEncoder" qui sont déjà prédéfinis dans le contexte de l'application (voir le fichier: ApplicationConfig.java).
        // 1 - Lorsqu'un utilisateur tente de se connecter, ses informations (email et mot de passe) de connexion sont encapsulées dans un "UsernamePasswordAuthenticationToken".
        // 2 - Authentification via "AuthenticationManager"
        // 3 - Puisque nous avons défini un Bean "UserDetailsService" et un Bean "PasswordEncoder", Spring Boot configure automatiquement un "DaoAuthenticationProvider" qui utilise ces Beans pour effectuer l'authentification.
        // 4 - "DaoAuthenticationProvider" appelle le "UserDetailsService" pour charger les détails de l'utilisateur en utilisant l'adresse email.
        // 5 - Ensuite, il utilise le "PasswordEncoder" pour comparer le mot de passe fourni avec le mot de passe stocké dans la base de données.
        // 6 - Si les mots de passe correspondent, l'utilisateur est authentifié. Sinon, une exception est levée.
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
            )
        );

        var user = repository.findByEmail(request.getEmail()).orElseThrow();

        var jwtToken = jwtService.generateToken(user, user.getId()); // appeller une méthode sur le "jwtService" pour générer un Token JWT pour l'utilisateur trouvé.
        var refreshToken = jwtService.generateRefreshToken(user, user.getId());

        revokeAllUserTokens(user); // Invalider les autres tokens qui sont liés à l'utilisateur concerné.
        saveTokenForUser(user, jwtToken); // Enregistrer un nouveau token pour l'utilisateur concerné.

        System.out.println(test3); // Output: Hy !! 1
        return AuthenticationResponse.builder() // construire et retourne une réponse contenant le JWT pour l'envoyer au client.
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void saveTokenForUser(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .revoked(false)
                .expired(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }

        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);

        if (userEmail != null) {
            var user = this.repository.findByEmail(userEmail).orElseThrow();

            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user, user.getId());

                revokeAllUserTokens(user);
                saveTokenForUser(user, accessToken);

                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();

                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }
}
