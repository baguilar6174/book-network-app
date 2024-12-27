# Spring Concepts

## Beans

Refers to an object that is managed by the framework. The Spring IoC (Inversion of Control) container creates these beans, manages their life cycle, and organizes their dependencies with other beans. It takes care of the instantiation, configuration, and wiring of objects, saving developers from a lot of manual work. Beans can be configured using XML, Java annotations, or Java code.

**Object life cycle:** means when & how it’s born, how it behaves throughout its life, and when & how it dies.

**Bean life cycle**: similar to object, when & how the bean is instantiated what action it performs until it lives and when & how it’s destroyed.

> When we run the app, first, the Spring container gets started, after that the container creates the instance of a bean as per the request and then, dependencies are injected, and finally, the bean is destroyed when the Spring container is closed.
> 

### Configure a Bean

Use `@Configuration` annotation

- `@Configuration` declares a class as 'full' configuration class.
    - Class must be `non-final` and `public`
- `@Bean` annotation declares bean configuration inside configuration class.
    - Method must be `non-final` & `non-private` (i.e. `public`, `protected` or `package private`)

### When use Beans

You need to use a **Bean** in your application whenever you want Spring to manage the lifecycle, configuration, and dependencies of an object. This is typically when:

1. **Dependency Injection is Needed**: When an object depends on other objects and you want Spring to handle the wiring automatically.
2. **Reusable Components**: For components like services, repositories, controllers, or any object that is shared across the application.
3. **Centralized Configuration**: When you need centralized control over object creation, initialization, and destruction.
4. **Cross-Cutting Concerns**: For aspects like transaction management, security, or logging, which Spring can integrate with beans seamlessly.
5. **Dynamic Behavior**: If you need to configure objects dynamically based on profiles, environments, or externalized configurations.

### Example

```java
@Configuration
public class AppConfig {

    @Bean
    public PaymentService paymentService() {
        return new PaymentServiceImpl(accountRepository());
    }

    @Bean
    public AccountRepository accountRepository() {
        return new JdbcAccountRepository(dataSource());
    }

    @Bean
    public DataSource dataSource() {
        return (...)
    }
}

```

This code demonstrates a **Java-based configuration** approach in Spring, where beans are explicitly defined using the `@Bean` annotation within a configuration class marked with `@Configuration`.

### Explanation:

### 1. **`@Configuration` Annotation**

- Marks the class as a configuration class where Spring looks for bean definitions.
- This class acts as a replacement for the traditional XML-based configuration.

### 2. **Defining Beans with `@Bean`**

Each method annotated with `@Bean` declares a bean to be managed by the Spring IoC container.

- **`paymentService` Method**:
    - Defines a bean of type `PaymentService`.
    - It returns an instance of `PaymentServiceImpl`, which depends on the `accountRepository` bean.
    - Spring ensures that the `accountRepository()` method is called to supply the dependency.

```java
@Bean
public PaymentService paymentService() {
    return new PaymentServiceImpl(accountRepository());
}

```

- **`accountRepository` Method**:
    - Defines a bean of type `AccountRepository`.
    - It returns an instance of `JdbcAccountRepository`, which depends on the `dataSource` bean.

```java
@Bean
public AccountRepository accountRepository() {
    return new JdbcAccountRepository(dataSource());
}

```

- **`dataSource` Method**:
    - Defines a bean of type `DataSource`.
    - The implementation is typically a database connection pool or similar resource.
    - The specifics depend on the code inside the method (`...`).

```java
@Bean
public DataSource dataSource() {
    return ( ... );
}

```

### 3. **Dependency Management**

Spring automatically manages dependencies:

- When the `paymentService` bean is requested, Spring ensures its dependency `accountRepository` is available.
- Similarly, `accountRepository` depends on `dataSource`, so Spring ensures the `dataSource` bean is created before `accountRepository`.

### 4. **Singleton Behavior**

By default, Spring beans are **singletons** within the container:

- The `accountRepository()` and `dataSource()` methods are only called once, and their return values are cached by Spring.
- Even though `accountRepository()` is called within `paymentService()`, Spring does not create a new `AccountRepository` each time. Instead, it reuses the same bean instance.

### Benefits of This Approach:

1. **Type Safety**: Beans are defined in Java code, ensuring type checking during compilation.
2. **Explicit Dependencies**: Each bean's dependencies are clearly visible in the code.
3. **Reusability**: Methods can be reused across the configuration.
4. **Flexibility**: Can dynamically configure beans using Java logic, which is difficult in XML-based configuration.
