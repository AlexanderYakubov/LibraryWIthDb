package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@SpringBootApplication
public class DemoApplication {

	// MVC - model view controller pattern
	// POST localhost:8080/users

	// localhost:8080/users/1
	// localhost:8080/users/1/posts
	// localhost:8080/users/1/posts/1/comments
	// localhost:8080/users/1/posts/1/comments/1
	// localhost:8080/users/1/posts/1/comments/1/likes


	@RestController
	@RequestMapping("/users")
	static class UsersController {

		//
		final UserService userService;

		UsersController(UserService userService) {
			this.userService = userService;
		}

		//endpoint to get user
		@GetMapping("/{id}")
		UserDTO getUser(@PathVariable Long id) {
			return userService.findUserById(id);
		}

		//endpoint for creating user
		@PostMapping
		UserDTO createUser(@RequestBody UserDTO userDTO) {
			return userService.createUser(userDTO);
		}
	}


	// spring data jpa

	// controller = own API communication
	// service = business logic
	// repository = database communication
	// client = external API communication

//	@Entity
//	// ORM - object relational mapping -
//	// mapping between java objects and database tables
//	class User {
//
//		Long Id;
//
//		@Column(name = "age_user")
//		Long age;
//
//		List<Post> posts = new ArrayList<>();
//		// sql - structured query language
//		// реляционные базы данных
//
//		// dcl - data creation language
//		// dml  - data manipulation language
//
//
//		// data creation language
//		// create table user (
//		// 	age int,
//		// 	name varchar(255),
//		// 	email varchar(255)
//		// );
//
//

		// data manipulation language
		// crud - create read update delete


		// postgresql
		// mysql

		// nosql db
		// mongodb {
//			user: {
//				age: 12,
//				name: "Zhambyl",
//				email: "sdsa@gmai.com",
//				posts: [
//					{
//						title: "sdsad",
//						content: "sdsad"
//					}
//				]
		// }
		// redis
		// cassandra
		// neo4j

		// join
		// select age,name,email from user where id = 1;
		// insert into user (age,name,email) values (12,"Zhambyl","e.zhambul@gmail");
		// update user where id = 1 set name = "Zhambyl"
		// delete from user where id = 1;


		// table user
		// age | name | email | profile_id   | post_id
		// 12 | Zhambyl | e.zhambul@gmail.com | 1
		// 18 | Sasha | sahsa@gmail


		// table user_post
		// user_id | post_id
		// 1	   | 1


		// one-to-one relationship
		// one-to-many relationship
		// many-to-many relationship

		// table profile
		// table post
		// user_id | title | content
		// 1	   | Sasha | sahsa@gmail



		// table post
		// user_id | title | content
		// 1	   | Sasha | sahsa@gmail


		// age | name | email
		// age | name | email
		// age | name | email
		// age | name | email
		// age | name | email
		// age | name | email

//	}
//
//	class Post {
//
//		Long Id;
//	}


	@Component
	static class IntagramClient {

		private final RestTemplate restTemplate;

		IntagramClient(RestTemplate restTemplate) {
			this.restTemplate = restTemplate;
		}

		void banUser(String id) {
			restTemplate.delete("http://instagram.com/user/" + id);
			// make request to instagram
		}

		void deleteUser(String id) {
			// make request to instagram
		}
	}


	@Component
	public static class UserService {

		private final IntagramClient intagramClient;
		private final UserRepository userRepository;

		UserService(IntagramClient intagramClient, UserRepository userRepository) {
			this.intagramClient = intagramClient;
			this.userRepository = userRepository;
		}

		UserDTO findUserById(Long id) {
			//
			Optional<User> byId = userRepository.findById(id);
			if (byId.isPresent()) {
				User user = byId.get();
				return new UserDTO(user.getName(), user.getEmail(), user.getId().doubleValue());
			}
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}

		void banUser(Double id) {
			// ban user
		}

		void deleteUser(Long id) {
			// generate sql
			// delete from user where id = 1;
			userRepository.deleteById(id);
		}


		public UserDTO createUser(UserDTO userDTO) {
			User user = new User();
			user.setName(userDTO.name());
			user.setEmail(userDTO.email());
			User savedUser = userRepository.save(user);
			return new UserDTO(savedUser.getName(), savedUser.getEmail(), savedUser.getId().doubleValue());
		}
	}


	//rest template configuration class
	@Configuration
	static class RestTemplateConfiguration {

		@Bean
		RestTemplate restTemplate() {
			return new RestTemplate();
		}
	}

	//record class for DTO User
	static record UserDTO(String name, String email, Double id) {
	}

	//record class for DTO Post
	static record PostDTO(String title, String content, Double id) {
	}

	//hello world rest controller
//	@RestController
//	static class HelloWorldController {
//
//		// autowire rest template
//		final RestTemplate restTemplate;
//		final ObjectMapper objectMapper;
//
//		public HelloWorldController(RestTemplate restTemplate, ObjectMapper objectMapper) {
//			this.restTemplate = restTemplate;
//			this.objectMapper = objectMapper;
//		}
//
//		@GetMapping("/hello")
//		String hello() {
//			return "Hello world";
//		}
//
//
//		// post request to another service which whill use rest template to make a request
//		@PostMapping("/hello")
//		String helloPost() {
//			return restTemplate.postForObject("http://localhost:8080/hello", null, String.class);
//		}
//	}
//	// bean
	@Component
	static class Mouse {

 	}

	// bean
	@Component
	static class Button {

	}

	// bean
	@Component
	static class Keyboard {

		final List<Button> buttons;

		Keyboard(List<Button> buttons) {
			this.buttons = buttons;
		}
	}

	enum Color {
		RED, GREEN, BLUE
	}
	@Configuration
	class ComputerConfiguration {

		@Bean
		Computer createComputer(Mouse mouse, Keyboard keyboard) {
			return new Computer(mouse, keyboard, Color.RED);
		}

	}


	//
	interface DBCreator {
		void create();
	}


	interface Agreement {

		void sign();
	}

	@Component
	@Primary
	class AgreemmentToSellAHouse implements Agreement {

		@Override
		public void sign() {
			System.out.println("House sold");
		}
	}

	@Component
	class AgreemmentToSellAPhone implements Agreement {

		@Override
		public void sign() {
			System.out.println("Phone sold");
		}
	}

	@Component
	class Client {
		final Agreement agreement;

		@Autowired
		public Client(Agreement agreement) {
			this.agreement = agreement;
		}

		void signAgreement() {
			agreement.sign();
		}
	}


	class Manager {

		void run() {
			Client client = new Client(new AgreemmentToSellAHouse());
			client.signAgreement();
		}
	}

	@Component
	class COMPLICATEDDbCreator implements DBCreator {
		@Override
		public void create() {
			System.out.println("Complicated DB created");
		}
	}

	static class Computer {

		final Mouse mouse;
		final Keyboard keyboard;
		private final Color color;

		// reflection
		Computer(Mouse mouse, Keyboard keyboard, Color color) {
			this.mouse = mouse;
			this.keyboard = keyboard;
			this.color = color;
		}

	}

	@Component
	@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
	static class Programmer {
		final Computer computer;

		// inversion of control
		public Programmer(Computer computer) {
			this.computer = computer;
		}

		void program() {
			System.out.println("I'm programming on " + computer.color + " computer");
		}
	}


	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}
