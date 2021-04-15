package com.redislabs.edu.redisearch.demo;

import java.util.Collection;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.redisearch.SearchResult;
import io.redisearch.Suggestion;
import io.redisearch.client.Client;
import lombok.RequiredArgsConstructor;

@SpringBootApplication
public class RediSearchApplication {

  @Bean
  CommandLineRunner createSearchIndices(JedisConnectionFactory cf) {
    return args -> {
      // STEP 1. Create Search Index for Articles
    };
  }

  @Bean
  CommandLineRunner createAutoCompleteSuggestions(JedisConnectionFactory cf, RedisTemplate<String, String> template) {
    return args -> {
      // STEP 3. Create Auto Complete Suggestion for Author Names
    };
  }

  public static void main(String[] args) {
    SpringApplication.run(RediSearchApplication.class, args);
  }

}

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/articles")
class ArticlesController {

  private final JedisConnectionFactory cf;

  @GetMapping("/search")
  SearchResult search( //
      @RequestParam(name = "q") String query, //
      @RequestParam(defaultValue = "-1") Double minPrice, //
      @RequestParam(defaultValue = "-1") Double maxPrice) {
    try (var client = new Client("articles-idx", cf.getHostName(), cf.getPort())) {
      // STEP 2. Implement full-text search
      return null;
    }
  }

  @GetMapping("/authors")
  Collection<Suggestion> authorAutoComplete(@RequestParam(name = "q") String query) {
    try (var client = new Client("author-auto-complete", cf.getHostName(), cf.getPort())) {
      // STEP 4. Implement auto-complete for author Names
      return null;
    }
  }
}
