package com.karol172.blog;

import com.karol172.blog.model.*;
import com.karol172.blog.properties.StorageProperties;
import com.karol172.blog.repository.*;
import com.karol172.blog.service.StorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class App {
    public static void main (String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Bean
    public CommandLineRunner demo (UserGroupRepository userGroupRepository,
                                   UserRepository userRepository,
                                   CategoryRepository categoryRepository,
                                   ArticleRepository repository,
                                   PermissionRepository permissionRepository,
                                   PasswordEncoder passwordEncoder,
                                   CommentRepository commentRepository,
                                   ActivationAccountRepository activationAccountRepository) {
        return (args) -> {
            permissionRepository.save(new Permission("Artykuły", null));
            permissionRepository.save(new Permission("Kategorie", null));
            permissionRepository.save(new Permission("Pliki", null));
            permissionRepository.save(new Permission("Komentarze", null));
            permissionRepository.save(new Permission("Grupy Użytkowników", null));
            permissionRepository.save(new Permission("Użytkownicy", null));
            permissionRepository.save(new Permission("Ustawienia", null));

            UserGroup admin = new UserGroup("Administratorzy", "This group contains users who are admin");
            userGroupRepository.save(admin);

            permissionRepository.findAll().forEach(p->admin.addPermission(p));
            userGroupRepository.save(admin);

            userGroupRepository.save(new UserGroup("Użytkownicy", ""));
            userGroupRepository.save(new UserGroup("Somsiady", null));

            User user = new User("root", passwordEncoder.encode("asd123"),
                    "karol.cymerys@gmail.com", "Jan",
                    "Kowal", null, true,
                    userGroupRepository.findFirstByName("Administratorzy"));
            User user2 = new User("ro2ot", passwordEncoder.encode("asd123"),
                    "karol.cymerys@gmail2.com", "Jan",
                    "Kowal", null, true,
                    userGroupRepository.findFirstByName("Somsiady"));

            categoryRepository.save(new Category("Drawing", ""));
            categoryRepository.save(new Category("2Drawing", ""));
            categoryRepository.save(new Category("Java", ""));

            userRepository.save(user);
            userRepository.save(user2);

            ActivationAccount activationAccount1 = new ActivationAccount(user, LocalDateTime.now(), "alamakota");
            activationAccount1.setDateActivation(LocalDateTime.now());

            ActivationAccount activationAccount2 = new ActivationAccount(user2, LocalDateTime.now(), "alamakota");
            activationAccount2.setDateActivation(LocalDateTime.now());


            activationAccountRepository.save(activationAccount1);
            activationAccountRepository.save(activationAccount2);


            Article article = new Article("Tytuł", "Treść treścią", null,
                    LocalDateTime.now(), LocalDateTime.now().minusYears(9),
                    true, true, categoryRepository.findByOrderByNameAsc().get(0));

            Article article2 = new Article("Tytuł123", "Treść treścią2", null,
                    LocalDateTime.now(), LocalDateTime.now().minusYears(8),
                    true, true, categoryRepository.findByOrderByNameAsc().get(0));

            Article article3 = new Article("Tytuł123", "Treść treścią2", null,
                    LocalDateTime.now(), LocalDateTime.now().minusYears(8),
                    true, true, categoryRepository.findByOrderByNameAsc().get(0));

            Article article4 = new Article("Tytuł123", "Treść treścią2", null,
                    LocalDateTime.now(), LocalDateTime.now().minusYears(8),
                    true, true, categoryRepository.findByOrderByNameAsc().get(0));

            Article article5 = new Article("Tytuł123", "Treść treścią2", null,
                    LocalDateTime.now(), LocalDateTime.now().minusYears(8),
                    true, true, categoryRepository.findByOrderByNameAsc().get(0));

            Article article6 = new Article("Tytuł123", "Treść treścią2", null,
                    LocalDateTime.now(), LocalDateTime.now().minusYears(8),
                    true, true, categoryRepository.findByOrderByNameAsc().get(0));

            Article article7 = new Article("Tytuł123", "Treść treścią2", null,
                    LocalDateTime.now(), LocalDateTime.now().minusYears(8),
                    true, true, categoryRepository.findByOrderByNameAsc().get(0));

            Article article8 = new Article("Tytuł123", "Treść treścią2", null,
                    LocalDateTime.now(), LocalDateTime.now().minusYears(8),
                    true, true, categoryRepository.findByOrderByNameAsc().get(0));

            Article article9 = new Article("Tytuł123", "Treść treścią2", null,
                    LocalDateTime.now(), LocalDateTime.now().minusYears(8),
                    true, true, categoryRepository.findByOrderByNameAsc().get(0));

            Article article10 = new Article("Tytuł123", "Treść treścią2", null,
                    LocalDateTime.now(), LocalDateTime.now().minusYears(8),
                    true, true, categoryRepository.findByOrderByNameAsc().get(0));

            Article article11 = new Article("Tytuł123", "Treść treścią2", null,
                    LocalDateTime.now(), LocalDateTime.now().minusYears(8),
                    true, true, categoryRepository.findByOrderByNameAsc().get(0));

            Article article12 = new Article("Tytuł123", "Treść treścią2", null,
                    LocalDateTime.now(), LocalDateTime.now().minusYears(8),
                    true, true, categoryRepository.findByOrderByNameAsc().get(0));

            repository.save(article);
            repository.save(article2);
            repository.save(article3);
            repository.save(article4);
            repository.save(article5);
            repository.save(article6);
            repository.save(article7);
            repository.save(article8);
            repository.save(article9);
            repository.save(article10);
            repository.save(article11);
            repository.save(article12);

            article.addAuthor(user);

            repository.save(article);

            article2.addAuthor(user2);
            repository.save(article2);

            commentRepository.save(new Comment("ale fajnie", LocalDateTime.now(), true, user2, article));
            commentRepository.save(new Comment("ale fajnie fajnie bardzo fajnie to", LocalDateTime.now(), false, user2, article));
            commentRepository.save(new Comment("ale fajnie fajnie bardzo fajnie toale fajnie fajnie bardzo fajnie ale fajnie fajnie bardzo fajnie toale fajnie fajnie bardzo fajnie ale fajnie fajnie bardzo fajnie toale fajnie fajnie bardzo fajnie ale fajnie fajnie bardzo", LocalDateTime.now(), true, user2, article));


        };
    }
    // Settings Managment

    // TODO: Setting's form : checking if user is still blocked
    // Injection and write t xml
    // TODO: CommandLine to .sql
}
