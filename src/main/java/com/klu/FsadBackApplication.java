package com.klu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.EnableAsync;
import com.klu.model.Student;
import com.klu.repository.StudentRepository;

@SpringBootApplication
@EnableAsync
public class FsadBackApplication {

	public static void main(String[] args) {
		SpringApplication.run(FsadBackApplication.class, args);
	}

	@Bean
	public CommandLineRunner adminInitializer(StudentRepository repository) {
		return args -> {
			if (repository.findByEmail("admin@college.edu") == null) {
				Student admin = new Student();
				admin.setName("Admin");
				admin.setEmail("admin@college.edu");
				admin.setPassword("admin123");
				admin.setRole("ADMIN");
				// Provide other required dummy values
				admin.setRollNumber("ADMIN-001");
				admin.setDepartment("Management");
				admin.setCohort("Faculty");
				repository.save(admin);
				System.out.println("✅ Default Admin User created: admin@college.edu / admin123");
			}
		};
	}

}
