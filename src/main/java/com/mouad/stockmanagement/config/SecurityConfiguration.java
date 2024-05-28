package com.mouad.stockmanagement.config;

import com.mouad.stockmanagement.auth.jwt;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import org.springframework.security.web.authentication.logout.LogoutHandler;

@Configuration // Indique que cette classe fournit une configuration pour l'application Spring qui doit être prise en charge lors du démarrage du serveur.
@RequiredArgsConstructor
@EnableWebSecurity // Indique à Spring d'activer la configuration de la sécurité Web pour l'application.
@EnableMethodSecurity // Active la sécurité au niveau de la méthode pour autoriser/désactiver l'accès aux méthodes basé sur des règles de sécurité.
public class SecurityConfiguration {

    private static final String[] WHITE_LIST_URL = {"/api/v1/auth/**",
            "/stockmanagement/v1/categories/all",
            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-ui.html"};
    private final jwt.JwtAuthenticationFilter jwtAuthFilter; // Une instance de la class "JwtAuthenticationFilter" pour verifier la validité de JWT provenant des requetes HTTP.
    private final AuthenticationProvider authenticationProvider; // Cet instance de "AuthenticationProvider" qui est initialisé dans "ApplicationConfig.java" et est utilisé pour verifier l'authentification de utilisateur courant avec la BD.
    private final LogoutHandler logoutHandler;

    // Cette méthode crée un Bean "SecurityFilterChain" qui configure les filtres de sécurité pour l'application.
    // NB: Cette méthode est appliquée à chaque requête entrante dans l'application.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable) // Désactive la protection contre les attaques CSRF (Cross-Site Request Forgery) dans la configuration HTTP.
            .authorizeHttpRequests(req -> req.requestMatchers(WHITE_LIST_URL) // Configure les autorisations pour les requêtes HTTP. Les URLs spécifiées dans WHITE_LIST_URL sont autorisées sans authentification, tandis que les autres requêtes nécessitent une authentification.
                .permitAll() // utilisée pour autoriser toutes les requêtes correspondant aux URLs spécifiées dans WHITE_LIST_URL sans nécessiter d'authentification.
                .anyRequest()
                .authenticated()
            )
            .sessionManagement(session -> session.sessionCreationPolicy(STATELESS)) // Configure la gestion des sessions pour être sans état (stateless), ce qui signifie que l'application n'utilise pas de session HTTP pour stocker l'état de l'utilisateur.
            .authenticationProvider(authenticationProvider) // spécifier comment l'authentification des utilisateurs doit être gérée.
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class) // ici on a configuré que le filtre "jwtAuthFilter" soit être exécuté avant le filtre "UsernamePasswordAuthenticationFilter" :
            .logout(logout -> // ici pour configurer deconnexion d'un utilisateur
                logout.logoutUrl("/api/v1/auth/logout")
                        // NB: "LogoutHandler" c'est une interface, mais puise que on a implementé cette interface par un service "LogoutService.java"
                        // NB: "LogoutHandler" est une interface, mais comme on a implémenté cette interface dans le service "LogoutService.java", alors ici, le service "LogoutService.java" est utilisé ici automatiquement.
                        //    C'est le même exemple pour le contrôleur "CategoryController.java", qui injecte le service "CategoryServiceImpl.java" le seul qui implémente l'interface "CategoryService.java".
                        //    Dans le contrôleur "CategoryController.java", on trouve : private final CategoryService categoryService;
                        .addLogoutHandler(logoutHandler)
                        .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext()));

                // *** Scénario d'une requête de l'utilisateur déjà connecté :
                // 1 - L'utilisateur envoie une requête à l'application avec un Token d'authentification valide inclus dans Header de la requête.
                // 2 - Le filtre "jwtAuthFilter" (qu'on a configuré pour gérer l'authentification basée sur le Token JWT dans le fichier: JwtAuthenticationFilter.java) est exécuté en premier, car il est configuré pour être exécuté avant le filtre "UsernamePasswordAuthenticationFilter".
                // 3 - Le filtre "jwtAuthFilter" extrait le jeton d'authentification de Header de la requête et valide sa légitimité.
                // 4 - Si le Token est valide et n'a pas expiré, le filtre "jwtAuthFilter" considère l'utilisateur comme authentifié et extrait les informations d'identification de l'utilisateur (comme son identifiant ou ses rôles) du Token JWT.
                // 5 - Puisque l'utilisateur est considéré comme déjà authentifié grâce au Token JWT, le processus d'authentification traditionnel avec un email d'utilisateur et un mot de passe n'est pas nécessaire.
                // 6 - dans le scénario où la requête provient d'un utilisateur déjà connecté, le filtre "UsernamePasswordAuthenticationFilter" n'est généralement pas exécuté.

                // *** Scénarion Lorsqu'un nouvel utilisateur se connecte à l'application :
                // 1 - L'utilisateur envoie ses identifiants (email d'utilisateur et mot de passe) au serveur.
                // 2 - La requête est interceptée par "UsernamePasswordAuthenticationFilter".
                // 3 - "UsernamePasswordAuthenticationFilter" délègue la tâche à Bean "AuthenticationProvider" qui est déja préconfiguré dans ApplicationConfig.java pour verifier les donnés dans la base de donnée.
                // 4 - si l'utilisateur authentifié avec succès, un Token d'authentification sera généré pour l'utilisateur.

        return http.build();
    }

}
