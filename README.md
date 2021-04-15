# JRediSearch Spring Demo

A Spring Boot App showing how to implement faceted full-text search and autocomplete with [RediSearch](https://redisearch.io) using the [JRediSearch](https://github.com/RediSearch/JRediSearch) client library.

**Prerequisites:**

- [Java 11](https://sdkman.io/jdks)
- [Maven 3.2+](https://sdkman.io/sdks#maven)
- [Docker](https://www.docker.com/products/docker-desktop)
- [Redis + Modules](https://hub.docker.com/r/redislabs/redismod) 6.0.1 or greater
- [RIOT File](https://developer.redislabs.com/riot/file/)

**NOTE:** If you're not on Mac or Windows, you may need to [install Docker Compose](https://docs.docker.com/compose/install/) as well.

- [Getting Started](#getting-started)
- [See Also](#see-also)
- [Help](#help)
- [License](#license)
- [Credit](#credit)

## Getting Started

### Clone the Repository w/ Submodules

To install this example application, run the following commands:

```bash
git clone git@github.com:redis-developer/jredisearch-spring-demo.git --recurse-submodule
```

### Import into your IDE

You can also import the code straight into your IDE:

- [Visual Studio Code](https://code.visualstudio.com/docs/languages/java)
- [Spring Tool Suite (STS)](https://spring.io/guides/gs/sts)
- [IntelliJ IDEA](https://spring.io/guides/gs/intellij-idea/)

### Start the Docker Compose application:

```bash
cd jredisearch-spring-demo/docker
docker-compose up
```

### Import the data into Redis using RIOT File

```bash
riot-file import-dump data/articles_authors.json
```

## Launch the Application

```bash
./mvnw clean spring-boot:run
```

## Usage

### Full-text search over articles

On your browser, Postman or cURL try:

```
http://localhost:8080/api/articles/search/?q=chicken
```

### Auto-complete search over author names

On your browser, Postman or cURL try:

```
http://localhost:8080/api/articles/authors/?q=ja
```

## See Also

Quick Tutorial on Redis' Powerful Modules:

- [RediSearch Tutorial](https://developer.redislabs.com/howtos/redisearch)

The following links on Redis and Java may also be helpful:

- [Java and Redis](https://developer.redislabs.com/develop/java/)

## Help

Please post any questions and comments on the [Redis Discord Server](https://discord.gg/redis), and remember to visit our [Redis Developer Page](https://developer.redislabs.com) for awesome tutorials, project and tips. You can also email me bsb@redislabs.com.

## License

[MIT Licence](http://www.opensource.org/licenses/mit-license.html)

## Credit

Created by [Brian Sam-Bodden](https://github.com/bsbodden) @ [Redis Labs](https://redislabs.com)
