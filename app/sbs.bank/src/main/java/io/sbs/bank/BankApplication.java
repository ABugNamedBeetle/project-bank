package io.sbs.bank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BankApplication {
	// static {
		// }
		
	public static void main(String[] args) {
		System.setProperty("user.timezone", "Asia/Kolkata");
		
		SpringApplication.run(BankApplication.class, args);
	}

}
