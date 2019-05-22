package com.example.docsdemo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DocsdemoApplicationTests {

	@Test
	public void contextLoads() {
	}

	@Test
	public void givenOptional_whenMapWorksWithFilter_thenCorrect() {
		String password = "password     ";
		Optional<String> passOpt = Optional.ofNullable(password);
		boolean correctPassword = passOpt.filter(
				pass -> pass.equals("password")).isPresent();
		assertFalse(correctPassword);

		correctPassword = passOpt
				.map(String::trim)
				.filter(pass -> pass.equals("password"))
				.isPresent();
		assertTrue(correctPassword);
	}

}
