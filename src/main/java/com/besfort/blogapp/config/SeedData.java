package com.besfort.blogapp.config;

import com.besfort.blogapp.models.Account;
import com.besfort.blogapp.models.Authority;
import com.besfort.blogapp.models.Post;
import com.besfort.blogapp.repositories.AuthorityRepository;
import com.besfort.blogapp.services.AccountService;
import com.besfort.blogapp.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class SeedData implements CommandLineRunner {

    @Autowired
    private PostService postService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Override
    public void run(String... args) throws Exception {

        List<Post> posts = postService.getAll();

        if (posts.size() == 0) {

            Authority user = new Authority();
            user.setName("ROLE_USER");
            authorityRepository.save(user);

            Authority admin = new Authority();
            admin.setName("ROLE_ADMIN");
            authorityRepository.save(admin);

            Account account1 = Account
                    .builder()
                    .firstName("user_first")
                    .lastName("user_last")
                    .email("user.user@domain.com")
                    .password("password")
                    .build();

            Set<Authority> authorities1 = new HashSet<>();
            authorityRepository.findById("ROLE_USER").ifPresent(authorities1::add);
            account1.setAuthorities(authorities1);

            Account account2 = Account
                    .builder()
                    .firstName("admin_first")
                    .lastName("admin_last")
                    .email("admin.admin@domain.com")
                    .password("password")
                    .build();

            Set<Authority> authorities2 = new HashSet<>();
            authorityRepository.findById("ROLE_ADMIN").ifPresent(authorities2::add);
            account2.setAuthorities(authorities2);

            accountService.save(account1);
            accountService.save(account2);

            Post post1 = Post
                    .builder()
                    .title("Welcome to my blog application!")
                    .body("This is just a test post to make sure everything works as intended!")
                    .account(account1)
                    .build();

            Post post2 = Post
                    .builder()
                    .title("SEEU University")
                    .body("South East European University (SEEU) is private, public, not for profit higher education institution, consisting of faculties, centres and institutes as its integral part, specialized in socio-economic sciences.")
                    .account(account2)
                    .build();

            postService.save(post1);
            postService.save(post2);
        }
    }

}
