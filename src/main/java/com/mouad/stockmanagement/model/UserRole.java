package com.mouad.stockmanagement.model;

import com.mouad.stockmanagement.permission.Permission;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.mouad.stockmanagement.permission.Permission.ADMIN_CREATE;
import static com.mouad.stockmanagement.permission.Permission.ADMIN_DELETE;
import static com.mouad.stockmanagement.permission.Permission.ADMIN_READ;
import static com.mouad.stockmanagement.permission.Permission.ADMIN_UPDATE;
import static com.mouad.stockmanagement.permission.Permission.MANAGER_CREATE;
import static com.mouad.stockmanagement.permission.Permission.MANAGER_DELETE;
import static com.mouad.stockmanagement.permission.Permission.MANAGER_READ;
import static com.mouad.stockmanagement.permission.Permission.MANAGER_UPDATE;

@RequiredArgsConstructor
public enum UserRole {

    USER(Collections.emptySet()), // n'a aucune permission spécifique
    ADMIN(
        Set.of(
            ADMIN_READ,
            ADMIN_UPDATE,
            ADMIN_DELETE,
            ADMIN_CREATE,
            MANAGER_READ,
            MANAGER_UPDATE,
            MANAGER_DELETE,
            MANAGER_CREATE
        )
    ),
    MANAGER(
        Set.of(
            MANAGER_READ,
            MANAGER_UPDATE,
            MANAGER_DELETE,
            MANAGER_CREATE
        )
    )
    ;

    @Getter
    private final Set<Permission> permissions;

    // Cette méthode retourne une liste d'objets "SimpleGrantedAuthority" représentant les autorités (permissions) de Spring Security pour le rôle actuel d'utilisateur déteminé par le token qui vient avec le réquete executé.
    public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities = getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name())); // this.name() => il concerne le valeur de "userRole" de "User" qui est initialisé lors de la construction

        // NB:
        System.out.println("getPermissions()" + getPermissions());
        System.out.println("authorities" + authorities);
        // User :
        // getPermissions() => []
        // authorities => [ROLE_USER]

        // Manager :
        // getPermissions() => [MANAGER_CREATE, MANAGER_READ, MANAGER_DELETE, MANAGER_UPDATE]
        // authorities => [management:create, management:read, management:delete, management:update, ROLE_MANAGER]

        // Admin :
        // getPermissions() => [MANAGER_UPDATE, ADMIN_DELETE, ADMIN_READ, ADMIN_CREATE, ADMIN_UPDATE, MANAGER_CREATE, MANAGER_READ, MANAGER_DELETE]
        // authorities => [management:update, admin:delete, admin:read, admin:create, admin:update, management:create, management:read, management:delete, ROLE_ADMIN]
        return authorities;
    }
}

