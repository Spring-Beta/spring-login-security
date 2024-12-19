package com.example.springloginsecurity;

import com.example.springloginsecurity.entity.Role;
import com.example.springloginsecurity.entity.User;
import com.example.springloginsecurity.repository.RoleRepository;
import com.example.springloginsecurity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
@EnableCaching
@RequiredArgsConstructor
public class SpringLoginSecurityApplication implements ApplicationRunner {

	private final RoleRepository roleRepository;
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(SpringLoginSecurityApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {

		Role admin = roleRepository.save(new Role().builder().name("ROLE_ADMIN").build());
		System.out.println(admin.getId());
		Role client = roleRepository.save(new Role().builder().name("ROLE_CLIENT").build());

		Set<Role> superRol  = new HashSet<Role>();
		superRol.add(admin);

		Set<Role> clientRol  = new HashSet<Role>();
		clientRol.add(client);

		userRepository.save(new User().builder().
				firstName("Admin")
				.email("admin@example.com")
				.password(passwordEncoder.encode("123"))
				.enabled(true).roles(superRol).build());
		userRepository.save(new User().builder().
				firstName("Client")
				.email("client@example.com")
				.password(passwordEncoder.encode("123"))
				.enabled(true).roles(clientRol).build());



	}
}
