package io.digiwise.ldapihm;

import io.digiwise.ldapihm.model.Authority;
import io.digiwise.ldapihm.model.User;
import io.digiwise.ldapihm.repository.AuthorityRepository;
import io.digiwise.ldapihm.repository.UserRepository;
import org.slf4j.Logger;
import io.digiwise.ldapihm.storage.StorageProperties;
import io.digiwise.ldapihm.storage.StorageService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
/*extends SpringBootServletInitializer*/
@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class LdapIhmApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(LdapIhmApplication.class, args);
	}

	@Bean
	CommandLineRunner init(StorageService storageService) {
		return (args) -> {
			storageService.deleteAll();
			storageService.init();
		};
	}

	// To be deleted

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AuthorityRepository authorityRepository;


	@Override
	public void run(String... args) throws Exception {
//		logger.info("Inserting -> {}", userRepository.insert(new User("hajar", "hajar123", true)));
//		logger.info("Inserting -> {}", authorityRepository.insert(new Authority("hajar", "ROLE_ADMIN")));
	}
}
