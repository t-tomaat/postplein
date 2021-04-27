package nl.ruben.postplein.users;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
public class UserController {
	
	private final static Logger LOG = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	private UserRepository userRepository;
	

	@GetMapping("/users")
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}
	
	@GetMapping("/users/{id}")
	public User getUser(@PathVariable long id) {
		return userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException(id));
	}
	
	@PostMapping("/users")
	public ResponseEntity<Object> createUser(@Valid @RequestBody User user) {
		user.setIngevoerdOp(LocalDateTime.now());
		user.setGewijzigdOp(LocalDateTime.now());
		User savedUser = userRepository.save(user);
		LOG.info("Insert user {} {}", savedUser.getId(), user.getName());
		
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
			.path("/{id}")
			.buildAndExpand(savedUser.getId())
			.toUri();
		
		return ResponseEntity.created(location).build();
	}
	
	@Transactional
	@PutMapping("/users")
	public User updateUser(@Valid @RequestBody User user) {
		Long id = user.getId();
		
		if (id == null) {
			throw new MissingIdException();
		}
		
		User bestaandeUser = userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException(id));
		
		LOG.info("Update user {} set name = {}", id, user.getName());
		bestaandeUser.setName(user.getName());
		bestaandeUser.setGewijzigdOp(LocalDateTime.now());
		return bestaandeUser;
	}
	
	@DeleteMapping("/users/{id}")
	public User deleteUser(@PathVariable long id) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException(id));
		
		LOG.info("Delete user {} {}", id, user.getName());
		userRepository.delete(user);
		return user;
	}
	
}
