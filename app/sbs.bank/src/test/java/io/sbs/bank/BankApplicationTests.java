package io.sbs.bank;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BankApplicationTests {

	static {
		System.setProperty("user.timezone", "Asia/Kolkata");
	}

	@Test
	void contextLoads() {
	}

}
