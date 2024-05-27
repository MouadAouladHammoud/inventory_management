package com.mouad.stockmanagement.config;

import com.mouad.stockmanagement.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.lang.NonNull;

import java.util.Arrays;

@Configuration

// @RequiredArgsConstructor: crée un constructeur pour cette classe en initialisant uniquement les champs de type 'final' et '@NonNull' (les champs peuvent être des variables ou des objets d'autres classes).
//   Ici, le constructeur est initialisé avec l'objet de type "final" UserRepository. (C'est la même chose si le champ est annoté avec @NonNull)
//   Si on n'utilise pas @RequiredArgsConstructor, alors il faut définir le constructeur manuellement pour injecter "userRepository" : public ApplicationConfig(UserRepository userRepository) {  this.userRepository = userRepository; }
@RequiredArgsConstructor
public class ApplicationConfig {

    private final UserRepository userRepository;
    // @NonNull
    // private UserRepository userRepository;


    // @Bean : Cette annotation peut être utilisée avec des méthodes dans une classe annotée avec @Configuration.
    //   il permet d'initialiser ces méthodes lors du démarrage de l'application et de créer des instances préconfigurées qui peuvent être utilisées lorsqu'elles sont appelées depuis d'autres classes de l'application."
    //   les méthodes annotées avec @Bean sont généralement définies dans des classes annotées avec @Configuration. Cela signifie que ces méthodes sont utilisées pour déclarer les beans dans le contexte de l'application et sont généralement appelées au démarrage de l'application pour créer et initialiser les instances de ces beans.
    Integer number = 0;
    @Bean
    public String test_() { // ici on peut utiliser une instance de la fonction test() dans n'import quel niveau de l'app. (voir le fichier AuthenticationService.java)
        this.number++;
        return "Hy !! " + this.number;
    }


    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {
            System.out.println("Let's inspect the beans provided by Spring Boot:");
            String[] beanNames = ctx.getBeanDefinitionNames();
            Arrays.sort(beanNames);
            for (String beanName : beanNames) {
                System.out.println(beanName); // afficher tous Beans dans le contexte de l'application
            }
        };
    }


    // NB: Notez bien que toutes les Beans ci-dessous doivent être initialisées obligatoirement pour la partie sécurité de l'application :

    // Cette méthode prend en paramètre une variable "username" qui récupère dans notre cas l'email de l'utilisateur (mais cela peut être le nom, l'ID ou l'email, selon lequel baseé sur l'identification des utilisateurs).
    //   Elle recherche ensuite dans le "UserRepository" l'utilisateur correspondant par son email.
    //   Si aucun utilisateur n'est trouvé, une exception "UsernameNotFoundException" est levée
    // @Bean
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
    // Une erreur  survenue si cette méthode n'est pas déclarée avec l'annotation @Bean :
    // Parameter 1 of constructor in com.mouad.stockmanagement.auth.jwt$JwtAuthenticationFilter required a bean of type 'org.springframework.security.core.userdetails.UserDetailsService' that could not be found.
    // Consider defining a bean of type 'org.springframework.security.core.userdetails.UserDetailsService' in your configuration.


    @Bean
    public PasswordEncoder passwordEncoder() { // preciser la methode d'encoder le mot de passe
        return new BCryptPasswordEncoder();
    }
    // Une erreur survenue si cette méthode n'est pas déclarée avec l'annotation @Bean :
    // Parameter 1 of constructor in com.mouad.stockmanagement.auth.AuthenticationService required a bean of type 'org.springframework.security.crypto.password.PasswordEncoder' that could not be found.
    // Consider defining a bean of type 'org.springframework.security.crypto.password.PasswordEncoder' in your configuration.


    // Cette méthode Bean est implémentée pour créer un objet "authProvider" de type "DaoAuthenticationProvider", en lui fournissant le Bean "UserDetailsService" pour rechercher l'utilisateur concerné et un type d'encodage (PasswordEncoder) pour encoder le mot de passe donné.
    //   L'objet "authProvider" utilise ici le Bean "UserDetailsService" pour récupérer les détails de l'utilisateur et vérifier le mot de passe. Notons que la récupération des détails de l'utilisateur concerné se fait en se basant sur son adresse email.
    //   ce provider d'authentification "authProvider" est implémenté dans la fonction "SecurityFilterChain" du fichier "SecurityConfiguration.java" afin qu'il soit exécuté pour chaque requête reçue.
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService()); // charger les détails de l'utilisateur à partir de la base de données en fonction de l'adresse e-mail fournie.
        authProvider.setPasswordEncoder(passwordEncoder()); // Une fois que les détails de l'utilisateur sont chargés, le "DaoAuthenticationProvider" compare le mot de passe fourni avec celui stocké en base de données.
        return authProvider;
    }
    // Une erreur  survenue si cette méthode n'est pas déclarée avec l'annotation @Bean :
    // Parameter 1 of constructor in com.mouad.stockmanagement.config.SecurityConfiguration required a bean of type 'org.springframework.security.authentication.AuthenticationProvider' that could not be found.
    // Consider defining a bean of type 'org.springframework.security.authentication.AuthenticationProvider' in your configuration.


    // À l'intérieur de cette méthode, elle appelle la méthode getAuthenticationManager() de "AuthenticationConfiguration" pour exécuter la fonction "authenticate()" avec l'email et le mot de passe d'un utilisateur afin de vérifier l'authentification de cet utilisateur. (voir le fichier : AuthenticationService.java).
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
    // Une erreur survenue si cette méthode n'est pas déclarée avec l'annotation @Bean :
    // Parameter 3 of constructor in com.mouad.stockmanagement.auth.AuthenticationService required a bean of type 'org.springframework.security.authentication.AuthenticationManager' that could not be found.
    // Consider defining a bean of type 'org.springframework.security.authentication.AuthenticationManager' in your configuration.

}
