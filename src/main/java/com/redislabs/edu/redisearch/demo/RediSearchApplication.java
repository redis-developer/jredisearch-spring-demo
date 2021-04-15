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

import io.redisearch.Query;
import io.redisearch.Schema;
import io.redisearch.SearchResult;
import io.redisearch.Suggestion;
import io.redisearch.client.Client;
import io.redisearch.client.IndexDefinition;
import io.redisearch.client.SuggestionOptions;
import lombok.RequiredArgsConstructor;
import redis.clients.jedis.exceptions.JedisDataException;

@SpringBootApplication
public class RediSearchApplication {

  @Bean
  CommandLineRunner createSearchIndices(JedisConnectionFactory cf) {
    return args -> {
      var searchIndex = "articles-idx";

      try (var client = new Client(searchIndex, cf.getHostName(), cf.getPort())) {
        var sc = new Schema() //
            .addSortableTextField("title", 1.0) //
            .addSortableNumericField("price");
        var def = new IndexDefinition().setPrefixes("com.redislabs.edu.redisearch.demo.Article");
        client.createIndex(sc, Client.IndexOptions.defaultOptions().setDefinition(def));
      } catch (JedisDataException jde) {
        // ignore - index already exists
      }
    };
  }

  @Bean
  CommandLineRunner createAutoCompleteSuggestions(JedisConnectionFactory cf, RedisTemplate<String, String> template) {
    return args -> {
      var autoCompleteKey = "author-auto-complete";
      if (!template.hasKey(autoCompleteKey)) {
        var authorKeySet = "com.redislabs.edu.redisearch.demo.Author";
        var authorHashPrefix = "com.redislabs.edu.redisearch.demo.Author:";

        try (var client = new Client(autoCompleteKey, cf.getHostName(), cf.getPort())) {
          var authorIds = template.opsForSet().members(authorKeySet);
          authorIds.forEach(authorId -> {
            var authorHashKey = authorHashPrefix + authorId;
            String authorName = template.opsForHash().get(authorHashKey, "name").toString();
            var suggestion = Suggestion.builder().str(authorName).score(1).build();
            client.addSuggestion(suggestion, true);
          });
        }
      }
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
      var q = new Query(query);
      if (minPrice != -1 && maxPrice != -1) {
        q.addFilter(new Query.NumericFilter("price", minPrice, maxPrice));
      }
      q.returnFields("title", "price");
      return client.search(q);
    }
  }

  @GetMapping("/authors")
  Collection<Suggestion> authorAutoComplete(@RequestParam(name = "q") String query) {
    try (var client = new Client("author-auto-complete", cf.getHostName(), cf.getPort())) {
      return client.getSuggestion(query, SuggestionOptions.builder().build());
    }
  }
}
