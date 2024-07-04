package com.springboot.testing.SpringBoot_Testing;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class SpringBootTestingApplicationTests {

	@Test
	public void demoTestMethod() {
		assertTrue(true);
	}

}
