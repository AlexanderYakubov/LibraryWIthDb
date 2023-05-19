package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.context.annotation.Scope;

import java.util.List;

@SpringBootApplication
public class DemoApplication {


	// bean
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
		ApplicationContext applicationContext = SpringApplication.run(DemoApplication.class, args);

		Programmer programmer = applicationContext.getBean(Programmer.class);
		programmer.program();
		Client client = applicationContext.getBean(Client.class);
		client.signAgreement();
	}

}
