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

import static com.mouad.stockmanagement.permission.Permission.ADMIN_CREATE;
import static com.mouad.stockmanagement.permission.Permission.ADMIN_DELETE;
import static com.mouad.stockmanagement.permission.Permission.ADMIN_READ;
import static com.mouad.stockmanagement.permission.Permission.ADMIN_UPDATE;
import static com.mouad.stockmanagement.permission.Permission.MANAGER_CREATE;
import static com.mouad.stockmanagement.permission.Permission.MANAGER_DELETE;
import static com.mouad.stockmanagement.permission.Permission.MANAGER_READ;
import static com.mouad.stockmanagement.permission.Permission.MANAGER_UPDATE;

import static com.mouad.stockmanagement.model.UserRole.ADMIN;
import static com.mouad.stockmanagement.model.UserRole.MANAGER;
import static com.mouad.stockmanagement.model.UserRole.USER;

import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration // Indique que cette classe fournit une configuration pour l'application Spring qui doit être prise en charge lors du démarrage du serveur.
@RequiredArgsConstructor
@EnableWebSecurity // Indique à Spring d'activer la configuration de la sécurité Web pour l'application.
@EnableMethodSecurity // Active la sécurité au niveau de la méthode pour autoriser/désactiver l'accès aux méthodes basé sur des règles de sécurité.
public class SecurityConfiguration {

    // *** la methode "SecurityFilterChain" s'execute à chaque requête entrante dans l'application.
    // *** Voici ce qui se passe pour chaque requête entrante d'un utilisateur connecté :
    // 1 - Spring Security utilise des filtres pour intercepter les requêtes HTTP, un filtre "JwtAuthenticationFilter" extrait le token de la requête et le valide.
    // 2 - Si le token est valide, Spring Security utilise un service de "UserDetailsService" pour charger les détails de l'utilisateur à partir de la base de données. (voir le Component: jwt.JwtAuthenticationFilter qui implemente automatiquement la fonction: doFilterInternal())
    // 3 - Une fois que l'utilisateur est chargé (grace à Bean "UserDetailsService"), un objet "User" est créé. Ce "User" implémente l'interface "UserDetails", qui nécessite l'implémentation de certaines méthodes, y compris getAuthorities(). (voir la fonction getAuthorities() dans la classe User.java)
    // 4 - La méthode getAuthorities() de la classe "User" est appelée pour récupérer les autorités (permissions) de l'utilisateur sous format de list
    // 5 - La méthode getAuthorities() de UserRole convertit la list des permissions définies pour le rôle en objets "SimpleGrantedAuthority" (par exp le role de Manager: [management:create, management:read, management:delete, management:update, ROLE_MANAGER])
    // 6 - Spring Security utilise les autorités (permissions) retournées pour créer un objet "Authentication" (authenticationProvider) représentant l'utilisateur authentifié. Ce processus inclut les permissions et le rôle de l'utilisateur.
    // 7 - Après cela, les permissions sont maintenant utilisées ici (dans la fonction SecurityFilterChain) pour vérifier si le endpoint demandé est accessible pour l'utilisateur actuel.

    private static final String[] WHITE_LIST_URL = {"/api/v1/auth/**",
            "/stockmanagement/v1/categories/all", // endpoint pulic tout le monde peuvent y acceder
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
    // NB: Cette méthode est appliquée à chaque requête HTTP entrante dans l'application.
    //     Ce filtre de sécurité analyse la requête et applique les configurations de sécurité appropriées (authentification, autorisation, etc.) avant que la requête n'atteigne les contrôleurs de l'application.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable) // Désactive la protection contre les attaques CSRF (Cross-Site Request Forgery) dans la configuration HTTP.
            .authorizeHttpRequests(req ->
                req.requestMatchers(WHITE_LIST_URL) // Configure les autorisations pour les requêtes HTTP. Les URLs spécifiées dans WHITE_LIST_URL sont autorisées sans authentification, tandis que les autres requêtes nécessitent une authentification.
                    .permitAll() // utilisée pour autoriser toutes les requêtes correspondant aux URLs spécifiées dans WHITE_LIST_URL sans nécessiter d'authentification.

                // spécifier les endpoints auxquels le manager et l'administrateur peuvent accéder : (Même si l'utilisateur avait le rôle de "USER" et était identifié, il ne pouvait pas y accéder.)
                .requestMatchers("/stockmanagement/v1/categories/**").hasAnyRole(ADMIN.name(), MANAGER.name())
                    .requestMatchers(GET, "/stockmanagement/v1/categories/**").hasAnyAuthority(ADMIN_READ.name(), MANAGER_READ.name())
                    .requestMatchers(POST, "/stockmanagement/v1/categories/**").hasAnyAuthority(ADMIN_CREATE.name(), MANAGER_CREATE.name())
                    .requestMatchers(PUT, "/stockmanagement/v1/categories/**").hasAnyAuthority(ADMIN_UPDATE.name(), MANAGER_UPDATE.name())
                    .requestMatchers(DELETE, "/stockmanagement/v1/categories/**").hasAnyAuthority(ADMIN_DELETE.name(), MANAGER_DELETE.name())

                // spécifier les endpoints auxquels seul l'administrateur peut accéder :
                .requestMatchers("/stockmanagement/v1/company/**").hasRole(ADMIN.name()) // La règle globale ici s'applique à toutes les requêtes HTTP (GET, POST, PUT, DELETE, etc.) sur ce chemin. Elle exige que l'utilisateur ait le rôle ADMIN. (le nom du rôle tel que défini dans l'enum UserRole.)
                    .requestMatchers(GET, "/stockmanagement/v1/company/**").hasAuthority(ADMIN_READ.name()) // spécifier le règle particulière pour les requêtes GET. Par conséquent, si n'existe pas, Spring Security appliquera les règles globales disponibles pour le chemin.
                    .requestMatchers(POST, "/stockmanagement/v1/company/**").hasAuthority(ADMIN_CREATE.name())
                    .requestMatchers(PUT, "/stockmanagement/v1/company/**").hasAuthority(ADMIN_UPDATE.name())
                    .requestMatchers(DELETE, "/stockmanagement/v1/company/**").hasAuthority(ADMIN_DELETE.name())

                // Pour les autres endpoints, tous les niveaux d'utilisateurs, qu'il s'agisse de USER, ADMIN ou MANAGER, peuvent y accéder dès lors qu'ils sont identifiés et ont un token.

                .anyRequest()
                .authenticated()
            )

            // NB: Ce mode indique que l'application est "stateless" (sans état). Autrement dit, chaque requête HTTP est indépendante et ne dépend pas d'une session serveur stockée.
            //     Les API RESTful sont conçues pour être sans état (stateless). Cela signifie que chaque requête doit contenir toutes les informations nécessaires pour être traitée, sans dépendre de l'état du serveur (comme une session HTTP)
            //     il s'agit lorsque vous utilisez des Tokens pour l'authentification, il n'est pas nécessaire de stocker l'état d'un utilisateur sur le serveur. Au lieu de cela, chaque requête du client contient le token qui est utilisé pour vérifier l'identité de l'utilisateur.
            .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))

            // NB: "authenticationProvider": Cette option dit essentiellement à Spring Security de ne pas utiliser le fournisseur d'authentification par défaut, mais plutôt celui que tu as configuré avec tes propres règles (UserDetailsService, PasswordEncoder, etc.).
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
