package com.example.demo;

import org.apache.tomcat.jni.Library;
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

import java.sql.Array;
import java.util.Date;
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
	@RequestMapping("/library")
	static class UsersController {

		final BookService bookService;

		UsersController(BookService bookService) {
			this.bookService = bookService;
		}

		//endpoint to get user
		@GetMapping("/books")
		List<Book> getAllBooks() {
			return bookService.getAllBooks();
		}

		@GetMapping("/logs")
		List<Log> getAllLogs() {
			return bookService.getAllLogs();
		}

		//endpoint for creating user
		@PostMapping("/books")
		BookDTO createBook(@RequestBody BookDTO bookDTO) {
			return bookService.createBook(bookDTO.author, bookDTO.name);
		}
		@PostMapping("/logs/take")
		LogDTO createLog(@RequestBody LogDTOCreate logDTO) {
			return bookService.takeBook(logDTO.book_id, logDTO.client_name);
		}

		@PostMapping("/logs/return")
		LogDTO createLog(@RequestBody LogDTOUpdate logDTO) {
			return bookService.returnBook(logDTO.log_id);
		}
	}

//--------------------


//	@Component
//	static class LibraryClient{
//		private final RestTemplate restTemplate;
//
//		LibraryClient(RestTemplate restTemplate) {
//			this.restTemplate = restTemplate;
//		}
//
//		void createBook(String authorName, String bookName){
//			restTemplate.postForObject();
//		}
//		void takeBook()
//	}

	@Component
	public static class BookService {
		private final UserRepository userRepository;
		private final LogRepository logRepository;
		BookService(UserRepository userRepository, LogRepository logRepository){
			this.userRepository = userRepository;
			this.logRepository = logRepository;
		}
		public BookDTO createBook(String author, String name){
			Book book = new Book();
			book.setAuthor(author);
			book.setName(name);
			Book savedBook = userRepository.save(book);
			return new BookDTO(savedBook.getAuthor(), savedBook.getName(), savedBook.getId());
		}

		public LogDTO takeBook(Long book_id, String client_name){
			Log log = new Log();
			log.setBook(userRepository.findBookById(book_id));
			log.setClient_name(client_name);
			log.setTaken(new Date().getTime());
			Log savedLog = logRepository.save(log);
			return new LogDTO(savedLog.getTaken(), -1L, savedLog.getBook(), savedLog.getClient_name(), savedLog.getId());
		}

		public  LogDTO returnBook(Long log_id){
			Log existingLog = logRepository.findById(log_id)
					.orElseThrow(() -> new IllegalArgumentException("Log not found"));

			// Update the properties of the existing log with the values from the updated log
			existingLog.setReturned(new Date().getTime());
			logRepository.save(existingLog);
			return new LogDTO(existingLog.getTaken(), existingLog.getReturned(), existingLog.getBook(), existingLog.getClient_name(), existingLog.getId());

//			Optional<Log> logBox = logRepository.findById(log_id);
//			if(logBox.isPresent()) {
//				Log log = logBox.get();
//				log.setReturned(new Date().getTime());
//				Log savedLog = logRepository.updateLogById(log.getId(), log);
//				return new LogDTO(savedLog.getTaken(), savedLog.getReturned(), savedLog.getBook(), savedLog.getClient_name(), savedLog.getId());
//			}
//			return new LogDTO(0L, 0L, new Book(), "error", 0L);
		}

		public List<Book> getAllBooks(){
			return userRepository.findAll();
		}
		public List<Log> getAllLogs(){
			return logRepository.findAll();
		}
	}

//	@Component
//	public static class BookLogService {
//		private final UserRepository userRepository;
//		BookLogService(UserRepository userRepository){
//			this.userRepository = userRepository;
//		}
//
//	}

	//-------------
//	@Component
//	public static class UserService {
//
//		private final IntagramClient intagramClient;
//		private final UserRepository userRepository;
//
//		UserService(IntagramClient intagramClient, UserRepository userRepository) {
//			this.intagramClient = intagramClient;
//			this.userRepository = userRepository;
//		}
//
//		UserDTO findUserById(Long id) {
//			//
//			Optional<User> byId = userRepository.findById(id);
//			if (byId.isPresent()) {
//				User user = byId.get();
//				return new UserDTO(user.getName(), user.getEmail(), user.getId().doubleValue());
//			}
//			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
//		}
//
//		void banUser(Double id) {
//			// ban user
//		}
//
//		void deleteUser(Long id) {
//			// generate sql
//			// delete from user where id = 1;
//			userRepository.deleteById(id);
//		}
//
//
//		public UserDTO createUser(UserDTO userDTO) {
//			User user = new User();
//			user.setName(userDTO.name());
//			user.setEmail(userDTO.email());
//			User savedUser = userRepository.save(user);
//			return new UserDTO(savedUser.getName(), savedUser.getEmail(), savedUser.getId().doubleValue());
//		}
//	}
//
//
//	@Component
//	static class IntagramClient {
//
//		private final RestTemplate restTemplate;
//
//		IntagramClient(RestTemplate restTemplate) {
//			this.restTemplate = restTemplate;
//		}
//
//		void banUser(String id) {
//			restTemplate.delete("http://instagram.com/user/" + id);
//			// make request to instagram
//		}
//
//		void deleteUser(String id) {
//			// make request to instagram
//		}
//	}

	//rest template configuration class
	@Configuration
	static class RestTemplateConfiguration {

		@Bean
		RestTemplate restTemplate() {
			return new RestTemplate();
		}
	}

//	//record class for DTO User
//	static record UserDTO(String name, String email, Double id) {
//	}
//
//	//record class for DTO Post
//	static record PostDTO(String title, String content, Double id) {
//	}

	static record BookDTO(String author, String name, Long id){
	}

	static record LogDTO(Long taken, Long returned, Book book, String client_name, Long id){
	}

	static record LogDTOCreate(Long book_id, String client_name){
	}
	static  record LogDTOUpdate(Long log_id){
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


//	class Manager {
//
//		void run() {
//			Client client = new Client(new AgreemmentToSellAHouse());
//			client.signAgreement();
//		}
//	}

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
