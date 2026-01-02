package com.example.auth_service.service;

import com.example.auth_service.api.dto.AuthRequest;
import com.example.auth_service.api.dto.RegisterRequest;
import com.example.auth_service.domain.enums.Role;
import com.example.auth_service.domain.model.User;
import com.example.auth_service.domain.repository.UserRepository;
import com.example.auth_service.kafka.producer.AuthEventProducer;
import com.example.auth_service.security.jwt.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

	@InjectMocks
	private AuthService authService;

	@Mock
	private UserRepository userRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	@Mock
	private JwtService jwtService;

	@Mock
	private AuthEventProducer authEventProducer;

	@Test
	void register_success() {
		RegisterRequest request = new RegisterRequest(
				"test@mail.com",
				"password"
		);

		when(userRepository.findByEmail("test@mail.com"))
				.thenReturn(Optional.empty());

		when(passwordEncoder.encode("password"))
				.thenReturn("hashed");

		when(jwtService.generateToken(anyString(), anyString(), anyString()))
				.thenReturn("jwt-token");

		when(userRepository.save(any(User.class)))
				.thenAnswer(invocation -> {
					User user = invocation.getArgument(0);
					user.setId(UUID.randomUUID()); // 🔥 ВАЖНО
					return user;
				});

		var response = authService.register(request);

		assertNotNull(response);
		assertEquals("jwt-token", response.getAccessToken());
		assertEquals("test@mail.com", response.getUser().getEmail());
		assertEquals(Role.USER, response.getUser().getRole());

		verify(authEventProducer)
				.sendRegistrationEvent("test@mail.com");
	}

	@Test
	void login_success() {
		User user = new User();
		user.setId(UUID.randomUUID()); // 🔥 ВАЖНО
		user.setEmail("test@mail.com");
		user.setPassword("hashed");
		user.setRole(Role.USER);

		when(userRepository.findByEmail("test@mail.com"))
				.thenReturn(Optional.of(user));

		when(passwordEncoder.matches("password", "hashed"))
				.thenReturn(true);

		when(jwtService.generateToken(anyString(), anyString(), anyString()))
				.thenReturn("jwt-token");

		AuthRequest request = new AuthRequest(
				"test@mail.com",
				"password"
		);

		var response = authService.login(request);

		assertNotNull(response);
		assertEquals("jwt-token", response.getAccessToken());
		assertEquals("test@mail.com", response.getUser().getEmail());
	}

	@Test
	void getUserByEmail_success() {
		User user = new User();
		user.setEmail("test@mail.com");

		when(userRepository.findByEmail("test@mail.com"))
				.thenReturn(Optional.of(user));

		User result = authService.getUserByEmail("test@mail.com");

		assertNotNull(result);
		assertEquals("test@mail.com", result.getEmail());
	}
}
