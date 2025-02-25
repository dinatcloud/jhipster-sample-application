package co.mz.insurance.mk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class SimpleTodoApplication {

  public static void main(String[] args) {
    new SpringApplication(SimpleTodoApplication.class).run(args);
  }
}
