# Book Network API

## Spring Concepts

### Class decoration

- Apply Spring annotations on a class to configure its behavior and functionality in Spring Framework
- Annotations tells Spring how to manage and configure that class in IoC (inversion of control) container
- Some examples:
  - `@SpringBootApplication`: main class in Spring Boot application
  - `@RestController`: mark a Java class as a Rest Controller (combines `@Controller` and `@ResponseBody`) manages HTTP request and send data in JSON or XML format
  - `@GetMapping`: map GET HTTP request to specific controller methods. Spring Boot assign the URL in the annotation to a method in controller.
  - `@Bean`:  indicate that a method produces a bean to be instantiated, assembled, and managed by the Spring IoC. It is typically used in a `@Configuration` class.

## Dependencies

- [java-jwt: 4.4.0](https://github.com/auth0/java-jwt) → Dependency to generate tokens
