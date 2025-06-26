package com.main.userservice.config;

import com.main.userservice.model.Role;
import com.main.userservice.model.Roles;
import com.main.userservice.model.User;
import com.main.userservice.repository.RoleRepository;
import com.main.userservice.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

@Configuration
public class AppConfig {
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
    @Bean
    public CommandLineRunner commandLineRunner(RoleRepository roleRepository, UserRepository userRepository) {
        return args -> {
            Role roleAdmin = roleRepository.findByRoleName(Roles.ROLE_ADMIN)
                    .orElseGet(() -> {
                        Role newUserRole = new Role(Roles.ROLE_ADMIN);
                        return roleRepository.save(newUserRole);
                    });
            Role roleSeller = roleRepository.findByRoleName(Roles.ROLE_SELLER)
                    .orElseGet(() -> {
                        Role newUserRole = new Role(Roles.ROLE_SELLER);
                        return roleRepository.save(newUserRole);
                    });
            Role roleUser = roleRepository.findByRoleName(Roles.ROLE_USER)
                    .orElseGet(() -> {
                        Role newUserRole = new Role(Roles.ROLE_USER);
                        return roleRepository.save(newUserRole);
                    });

            Set<Role> adminRoles = Set.of(roleAdmin, roleSeller, roleUser);
            Set<Role> sellerRoles = Set.of(roleSeller, roleUser);
            Set<Role> userRoles = Set.of(roleUser);

            if (!userRepository.existsByEmail("admin@gmail.com")) {
                User admin = new User("admin", "admin@gmail.com", "admin");
                userRepository.save(admin);
            }
            if (!userRepository.existsByEmail("seller@gmail.com")) {
                User admin = new User("seller", "seller@gmail.com", "seller");
                userRepository.save(admin);
            }
            if (!userRepository.existsByEmail("user@gmail.com")) {
                User admin = new User("user", "user@gmail.com", "user");
                userRepository.save(admin);
            }

            userRepository.findByEmail("admin@gmail.com").ifPresent(admin -> {
                admin.setRoles(adminRoles);
                userRepository.save(admin);
            });
            userRepository.findByEmail("seller@gmail.com").ifPresent(manager -> {
                manager.setRoles(sellerRoles);
                userRepository.save(manager);
            });
            userRepository.findByEmail("user@gmail.com").ifPresent(user -> {
                user.setRoles(userRoles);
                userRepository.save(user);
            });
        };
    }
}
