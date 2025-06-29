package com.main.userservice.config;

import com.main.userservice.model.Role;
import com.main.userservice.model.Roles;
import com.main.userservice.model.User;
import com.main.userservice.repository.RoleRepository;
import com.main.userservice.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

@Slf4j
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
                log.info("Creating default admin : admin@gmail.com");
                User admin = new User("admin", "admin@gmail.com", "admin");
                userRepository.save(admin);
                admin.setRoles(adminRoles);
                userRepository.save(admin);
            }
            if (!userRepository.existsByEmail("seller@gmail.com")) {
                log.info("Creating default seller : seller@gmail.com");
                User seller = new User("seller", "seller@gmail.com", "seller");
                userRepository.save(seller);
                seller.setRoles(sellerRoles);
                userRepository.save(seller);
            }
            if (!userRepository.existsByEmail("user@gmail.com")) {
                log.info("Creating default user : user@gmail.com");
                User user = new User("user", "user@gmail.com", "user");
                userRepository.save(user);
                user.setRoles(userRoles);
                userRepository.save(user);
            }
        };
    }
}
