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

### **Bean Naming in Spring**

In Spring, a **Bean name** is the unique identifier that allows the Spring container to differentiate between various beans. By default, Spring assigns a name to a bean based on certain conventions, but you can also explicitly define the name. When a class is annotated with `@Component`, `@Service`, `@Repository`, or similar stereotypes, Spring automatically assigns the **bean name** based on the class name:

- The name is the same as the class name but starts with a lowercase letter.

### Example:

```java
@Component
public class MyComponent {
    // Bean name: "myComponent"
}

```

### **Custom Bean Naming**

You can explicitly specify the name of a bean:

1. **Using `@Component`**:
    
    ```java
    @Component("customName")
    public class MyComponent {
        // Bean name: "customName"
    }
    
    ```
    
2. **Using `@Bean` in Configuration Classes**:
    
    ```java
    @Configuration
    public class AppConfig {
        @Bean(name = "customBean")
        public MyComponent myComponent() {
            return new MyComponent();
        }
    }
    
    ```
    
3. **Using Aliases with `@Bean`**:
Multiple aliases can be assigned to a single bean:
    
    ```java
    @Bean(name = {"primaryBean", "aliasBean"})
    public MyComponent myComponent() {
        return new MyComponent();
    }
    
    ```
    

### **Why is Bean Naming Important?**

1. **Avoiding Bean Name Conflicts**:
    - If two beans share the same name, Spring will throw an error unless explicitly configured (e.g., using `@Primary` or `@Qualifier`).
    - Explicit naming ensures no accidental overwrites when working in large applications.
2. **Improved Readability and Maintenance**:
    - Custom names can reflect the purpose of the bean, making the code easier to understand and maintain.
    - For example, naming a `PaymentService` bean as `"onlinePaymentService"` is more descriptive than just `"paymentService"`.
3. **Enabling Bean Selection**:
    - Bean names are used in annotations like `@Qualifier` to specify which bean to inject when multiple beans of the same type exist.

## Spring Component

In Spring, a **Component** is a class that is managed by the Spring IoC container. It is automatically detected and registered as a **Bean** when annotated with `@Component` and scanned via component scanning. Spring Components are used to define the main parts of your application, such as:

- Services (`@Service`)
- Data repositories (`@Repository`)
- Controllers (`@Controller`)

### **`@Component` Annotation**

Is a Spring annotation used to mark a class as a **Spring-managed Bean**. When a class is annotated with `@Component`, Spring will:

1. Detect it during classpath scanning.
2. Register it as a bean in the Spring IoC container.

### **`@Autowired` Annotation**

Is a Spring annotation used to **inject dependencies** automatically. It allows Spring to resolve and inject a bean into another bean. `@Autowired` on constructor is optional if there’s only one constructor.

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MyService {
    private final MyComponent myComponent;

    @Autowired
    public MyService(MyComponent myComponent) {
        this.myComponent = myComponent;
    }

    public void execute() {
        myComponent.doSomething();
    }
}
```

**How It Works:**

1. **Field Injection**: Injects the dependency directly into a field. **Not recommended** due to poor testability.
    
    ```java
    @Autowired
    private MyComponent myComponent;
    ```
    
2. **Constructor Injection** (Recommended): Dependencies are passed through the constructor, ensuring immutability and better testability.
    
    ```java
    @Autowired
    public MyService(MyComponent myComponent) {
        this.myComponent = myComponent;
    }
    ```
    
3. **Setter Injection**: Injects dependencies via a setter method.
    
    ```java
    @Autowired
    public void setMyComponent(MyComponent myComponent) {
        this.myComponent = myComponent;
    }
    ```
    

### When Do You Need Spring Components?

Use Spring components when you want the Spring container to manage the lifecycle and dependencies of an object. Common use cases include:

1. **Business Logic**:
Use `@Component` or `@Service` for classes that implement business logic.
    
    ```java
    @Service
    public class PaymentService { ... }
    ```
    
2. **Data Access**:
Use `@Repository` for database interaction classes.
    
    ```java
    @Repository
    public class AccountRepository { ... }
    ```
    
3. **Web Controllers**:
Use `@Controller` for classes handling HTTP requests.
    
    ```java
    @Controller
    public class HomeController { ... }
    ```
    
4. **Cross-Cutting Concerns**:
Use components for shared utilities like logging or validation.
5. **Dependency Injection**:
To wire dependencies automatically without manually creating instances.

### **Summary**

- Use `@Component` to mark a class as a Spring-managed bean.
- Use `@Autowired` to inject dependencies into beans automatically.
- Use Spring components whenever you want Spring to manage object creation, configuration, and dependency injection.

### Beans injection

**Bean injection** in Spring is the process of providing the dependencies a bean needs to perform its tasks. Spring framework provides four ways to inject beans, so the Spring can configure dependencies on different injection elements:

1. **Constructor injection**: the constructor parameter to receive dependencies during Bean construction.
2. **Field injection**: this field definition to receive dependency injected with the reflection access also called field injection
3. **Configuration Methods**: with one or many parameters receiving dependencies through method parameters also called method injection
4. **Setter injection**: the java setter method are specialized configuration method with only one parameter and a defined naming scheme called also setter injection.

### **1. Constructor Injection**

In **Constructor Injection**, dependencies are passed to a bean via its constructor. Spring uses the `@Autowired` annotation to resolve the dependencies.

### Advantages:

- Ensures immutability since dependencies are provided during object creation.
- Makes dependencies explicit and mandatory.

### Example:

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

// Dependency
@Component
public class Engine {
    public String start() {
        return "Engine started";
    }
}

// Dependent Bean
@Component
public class Car {
    private final Engine engine;

    @Autowired // Marks this constructor for dependency injection
    public Car(Engine engine) {
        this.engine = engine;
    }

    public void drive() {
        System.out.println(engine.start() + " - Car is driving");
    }
}

```

### **2. Field Injection**

In **Field Injection**, dependencies are directly injected into the fields using the `@Autowired` annotation.

### Advantages:

- Simplifies the code by avoiding boilerplate constructors or setters.

### Disadvantages:

- Makes testing harder since dependencies cannot be injected manually during tests.
- Hides dependencies, making them less explicit.

### Example:

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Vehicle {
    @Autowired // Dependency is injected directly into the field
    private Engine engine;

    public void startVehicle() {
        System.out.println(engine.start() + " - Vehicle is ready");
    }
}

```

**Usage**:

```java
var context = new AnnotationConfigApplicationContext("com.example");
Vehicle vehicle = context.getBean(Vehicle.class);
vehicle.startVehicle(); // Output: Engine started - Vehicle is ready

```

### **3. Setter Injection**

In **Setter Injection**, dependencies are provided via setter methods. Spring uses the `@Autowired` annotation to resolve and inject the dependency.

### Advantages:

- Provides optional dependencies (setters can be omitted if not needed).
- Allows changing dependencies post-construction.

### Example:

```java
@Component
public class Motorcycle {
    private Engine engine;

    @Autowired // Marks the setter for dependency injection
    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    public void ride() {
        System.out.println(engine.start() + " - Motorcycle is riding");
    }
}

```

**Usage**:

```java
var context = new AnnotationConfigApplicationContext("com.example");
Motorcycle motorcycle = context.getBean(Motorcycle.class);
motorcycle.ride(); // Output: Engine started - Motorcycle is riding

```

### **4. Configuration Methods**

In **Configuration Methods**, dependencies are manually defined in a `@Configuration` class, often when you need more control or need to customize the object creation.

### Advantages:

- Useful for defining third-party objects or beans with complex dependencies.
- Centralized configuration.

### Example:

```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public Engine engine() {
        return new Engine(); // Explicitly creates an Engine bean
    }

    @Bean
    public Truck truck(Engine engine) {
        return new Truck(engine); // Injects Engine bean
    }
}

@Component
public class Truck {
    private final Engine engine;

    public Truck(Engine engine) {
        this.engine = engine;
    }

    public void deliver() {
        System.out.println(engine.start() + " - Truck is delivering goods");
    }
}

```

**Usage**:

```java
var context = new AnnotationConfigApplicationContext(AppConfig.class);
Truck truck = context.getBean(Truck.class);
truck.deliver(); // Output: Engine started - Truck is delivering goods

```

### **When to Use Each Type?**

| Injection Type | Use When |
| --- | --- |
| **Constructor** | Dependencies are mandatory, should not change, or to ensure immutability. |
| **Field** | Simplicity is prioritized, but testing is not a concern. |
| **Setter** | Dependencies are optional, or you need to change dependencies post-construction. |
| **Configuration** | When dealing with complex object creation or third-party library beans. |

### **Bean Scoping in Spring**

**Bean scope** in Spring determines the lifecycle and visibility of a bean within the Spring container. It specifies how many instances of a bean will be created and how they are shared.

### **Types of Bean Scopes**

Spring provides several bean scopes. The most common ones are:

1. **Singleton** (Default)
2. **Prototype**
3. **Request** (Web-specific)
4. **Session** (Web-specific)
5. **Application** (Web-specific)
6. **Custom Scopes**

### **1. Singleton Scope**

- **Definition**: A single instance of the bean is created per Spring IoC container. This instance is shared across the application.
- **Use Case**: For stateless beans or shared resources like service classes.
- **Default Scope**: If no scope is explicitly defined, the bean is `singleton`.

### Example:

```java
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("singleton")
public class SingletonBean {
    public SingletonBean() {
        System.out.println("SingletonBean instance created");
    }
}
```

**Usage**:

```java
var context = new AnnotationConfigApplicationContext("com.example");
SingletonBean bean1 = context.getBean(SingletonBean.class);
SingletonBean bean2 = context.getBean(SingletonBean.class);
System.out.println(bean1 == bean2); // Output: true (Same instance)
```

### **2. Prototype Scope**

- **Definition**: A new instance of the bean is created each time it is requested from the container.
- **Use Case**: For stateful or short-lived beans, like objects that hold session-specific data.

### Example:

```java
@Component
@Scope("prototype")
public class PrototypeBean {
    public PrototypeBean() {
        System.out.println("PrototypeBean instance created");
    }
}
```

**Usage**:

```java
var context = new AnnotationConfigApplicationContext("com.example");
PrototypeBean bean1 = context.getBean(PrototypeBean.class);
PrototypeBean bean2 = context.getBean(PrototypeBean.class);
System.out.println(bean1 == bean2); // Output: false (Different instances)
```

### **3. Request Scope** (Web-Specific)

- **Definition**: A new bean instance is created for each HTTP request and is available only during that request.
- **Use Case**: For web applications where you need a bean tied to an HTTP request lifecycle.

### Example:

```java
@Component
@Scope("request")
public class RequestBean {
    public RequestBean() {
        System.out.println("RequestBean instance created");
    }
}
```

**Usage**:

- Available only in Spring web applications.
- A new instance is created for each HTTP request.

### **4. Session Scope** (Web-Specific)

- **Definition**: A new bean instance is created for each HTTP session and is available throughout the session.
- **Use Case**: For storing session-specific data in a web application.

### Example:

```java
@Component
@Scope("session")
public class SessionBean {
    public SessionBean() {
        System.out.println("SessionBean instance created");
    }
}
```

**Usage**:

- Requires a web environment with HTTP session support.
- A new instance is created for each user session.

### **5. Application Scope** (Web-Specific)

- **Definition**: A single instance of the bean is created for the lifecycle of the ServletContext.
- **Use Case**: For shared objects that need to be available across all sessions and requests in a web application.

### Example:

```java
@Component
@Scope("application")
public class ApplicationBean {
    public ApplicationBean() {
        System.out.println("ApplicationBean instance created");
    }
}
```

**Usage**:

- Available in web applications for shared resources.

### **6. Custom Scopes**

Spring also allows you to define custom scopes by implementing the `org.springframework.beans.factory.config.Scope` interface.

### Example:

You can create a scope for a specific use case, such as a database transaction scope or a thread scope.

### **How to Specify Bean Scope**

- **Using Annotations**:
    
    ```java
    @Scope("singleton") // Specify the scope
    @Component
    public class MyBean { ... }
    ```
    
- **Using XML Configuration**:
    
    ```xml
    <bean id="myBean" class="com.example.MyBean" scope="prototype" />
    ```
    

### **Comparison of Bean Scopes**

| Scope | Description | Lifetime | Shared Across Requests |
| --- | --- | --- | --- |
| **Singleton** | Default scope, one instance per container | Container lifecycle | Yes |
| **Prototype** | New instance for each request | Until no longer referenced | No |
| **Request** | New instance for each HTTP request (web apps) | Request lifecycle | No |
| **Session** | New instance per HTTP session (web apps) | Session lifecycle | No |
| **Application** | One instance per ServletContext (web apps) | Application lifecycle | Yes |

### **Best Practices for Bean Scopes**

1. **Use `singleton` scope** for stateless beans and services.
2. **Use `prototype` scope** for stateful beans or beans with a short lifecycle.
3. **Web scopes (`request`, `session`, `application`)** should only be used in web applications.
4. Avoid using `prototype` scope for beans injected into `singleton` beans, as it can cause unexpected behavior without proxies.

## Special Beans

### **Environment Bean**

The **Environment** bean in Spring provides a way to access and manage properties, profiles, and environment variables in an application. It is used to retrieve configuration values or to adapt application behavior based on the environment (e.g., development, production).

### **When to Use the Environment Bean?**

1. **Accessing Properties**: Retrieve values from `application.properties` or `application.yml`.
2. **Environment Variables**: Fetch system or environment variables.
3. **Profile Management**: Determine or set the active Spring profile.
4. **Best Practice**: Use `@Value` for simple property injection, and `Environment` for more dynamic or complex needs.

**Example**

`application.properties`

```
app.name=MySpringApp
```

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class AppConfig {
    private final Environment environment;

    @Autowired
    public AppConfig(Environment environment) {
        this.environment = environment;
    }

    public void printAppName() {
        String appName = environment.getProperty("app.name");
        System.out.println("Application Name: " + appName);
    }
}
```

```java
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MainApp {
    public static void main(String[] args) {
        var context = new AnnotationConfigApplicationContext("com.example");
        AppConfig appConfig = context.getBean(AppConfig.class);
        appConfig.printAppName(); // Output: Application Name: MySpringApp
    }
}
```

### **Profile Bean**

The **Profile Bean** is a mechanism to define and load specific beans based on the active application profile (e.g., `dev`, `test`, `prod`). It helps to customize configurations for different environments.

**When to Use?**

1. **Environment-Specific Beans**: Define beans for different environments, such as development, testing, or production.
2. **Simplify Configuration**: Activate only the beans relevant to the current profile, avoiding unnecessary resource initialization.

**Example**

`application.properties`:

```
spring.profiles.active=dev
```

```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

interface DataSource {
    String getConnectionDetails();
}

class DevDataSource implements DataSource {
    public String getConnectionDetails() {
        return "Connected to DEV database";
    }
}

class ProdDataSource implements DataSource {
    public String getConnectionDetails() {
        return "Connected to PROD database";
    }
}

@Configuration
public class AppConfig {

    @Bean
    @Profile("dev")
    public DataSource devDataSource() {
        return new DevDataSource();
    }

    @Bean
    @Profile("prod")
    public DataSource prodDataSource() {
        return new ProdDataSource();
    }
}

```

```java
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MainApp {
    public static void main(String[] args) {
        var context = new AnnotationConfigApplicationContext(AppConfig.class);
        DataSource dataSource = context.getBean(DataSource.class);
        System.out.println(dataSource.getConnectionDetails()); // Output: Connected to DEV database
    }
}

```

## @Value annotation

Is used in Spring to inject values into fields, method parameters, or constructor arguments. These values can come from property files, environment variables, or hard-coded expressions.

**When to Use @Value?**

1. **Property Injection**: Inject configuration values from `application.properties` or `application.yml`.
2. **Environment Variables**: Use system-level or environment-specific variables.
3. **Default Values**: Provide fallback values if a property is missing.

**Example**

`application.properties`:

```
app.name=MySpringApp
app.version=1.0
```

```java
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppConfig {

    @Value("${app.name}")
    private String appName;

    @Value("${app.version:1.0}")
    private String appVersion; // Default value is "1.0" if the property is missing

    public void printAppDetails() {
        System.out.println("App Name: " + appName);
        System.out.println("App Version: " + appVersion);
    }
}
```