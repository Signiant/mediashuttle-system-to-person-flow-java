# System-to-Person Automation API

Media Shuttle’s System-to-Person API allows you to generate single use links that allow specified users to upload or download content using a Media Shuttle portal.

## Getting Started

### Generate the code with OpenAPI Codegen

1. Follow these instructions to generate the code using OpenAPI Codegen:

- Install OpenAPI Codegen by referring to the documentation found at [OpenAPI Generator](https://github.com/OpenAPITools/openapi-generator)
- Once installed, execute the following command.

2. If you have the JAR file, execute the command below.
```
java -jar /path_to_open_api_generator_jar_file generate -i /path_to_yaml_file -g java -o ./output_path --global-property skipFormModel=false
```

### Note
1. If you encounter the following error while running the code generator.

```
Exception in thread "main" java.lang.RuntimeException: Could not generate model 'AzureBlobStorage'
at org.openapitools.codegen.DefaultGenerator.generateModels(DefaultGenerator.java:569)
at org.openapitools.codegen.DefaultGenerator.generate(DefaultGenerator.java:926)
```
- Please ensure that your system is equipped with the compatible Java 11 version for code generation.
- The OpenAPI Generator is actively maintained, and new versions are periodically released. It is recommended to visit the official OpenAPI Generator repository on [GitHub](https://github.com/OpenAPITools/openapi-generator) to check for the latest version and updated system requirements.

### Generate the code with Swagger Codegen

1. You can generate code using swagger-codegen by referring to the documentation available [here](https://swagger.io/docs/open-source-tools/swagger-codegen/)
2. To generate code, use the following command in the command prompt:

```cmd
swagger-codegen generate -i ./path_to_yaml_file -l java --output ./output_path --additional-properties=java8=true
```

### Note
Modify the import statements in the repository code and rearrange the parameter orders of a few methods.

- File Path - **com.workflows.service.SystemToPersonService**
- Methods - **addPackageFiles** and **createToken**

## Project Execution

1. Clone the code from the repository and import in the generated code folder.
2. Integrate the dependencies listed in the [Maven dependencies]() section into your project by following these steps:
   - Open your project's `pom.xml` file
   - Locate the root `<project>` element in the pom.xml file. It should be near the top of the file.
   - Inside the `<project>` element, add the `<parent>` element to include the Spring Boot parent dependency.
   - Locate the `<dependencies>` section. If it doesn't exist, you can add it under the `<dependencies>` tag. Add `<dependency>` inside the  `<dependencies>` tag.
3. Configure all the fields defined in the [Configuration]() section.
4. Initiate the project by executing the following command:
   ``` mvn spring-boot:run ```
   or by running the ```Main.java```

### Note

1. If you encounter the following error while running the command
   ```mvn spring-boot:run``` or by running the ```Main.java``` file:

```Unexpected value 'Share' , 'Send`, 'Submit```

Please replace the enum values at the following path:

**src/main/java/org/openapitools/client/model/Portal.java**

```java
SEND("Send"),
SHARE("Share"),
SUBMIT("Submit");
```

Add following code path to avoid `null` response field

**src/main/java/org/openapitools/client/model/PackageTokenResponse.java**

```java

@JsonInclude(JsonInclude.Include.NON_NULL)
private String destinationPath;

@JsonInclude(JsonInclude.Include.NON_NULL)
private Boolean isReusable;
```

## Configuration
Please add the following fields to the `application.yaml` file in your project:
```yaml
developer:
  key: #dev key
portal:
  url: #portal url
webhook:
  url: #webhook url
tokenUrl:
  expiryDay: # expiry days
base:
  path: #base path
user:
  mail: #usermail
destination:
  path:
transferfiles:
  files:
    -
      path: #filePath1
      isDirectory: #true or false
      size: #filesize
```
- `developer key`: Add your Media Shuttle API key. You can generate an API key from your IT Administration Console in the Developer menu.
- `portal url`: Add the URL of the Media Shuttle portal.
- `webhook url`: Add the URL of the webhook where events will be received.
- `tokenUrl expiryDay`: Add the number of days for the token URL expiry.
- `base path`: Add the Media Shuttle API base URL for API calls.
- `user mail`: Add the email address where you will receive notifications of file transfers.
- `destination path`: Add the destination path if you want to receive files at a different location.
- `transferfiles files`: Add the following details for each transfer file:
   - `path`: Add the file path.
   - `isDirectory`: Set to `true` or `false` based on whether it's a directory.
   - `size`: Add the file size.

### Maven dependencies
```xml
<parent>
   <groupId>org.springframework.boot</groupId>
   <artifactId>spring-boot-starter-parent</artifactId>
   <version>2.4.0</version>
</parent>

<dependency>
   <groupId>org.springframework.boot</groupId>
   <artifactId>spring-boot-starter-web</artifactId>
   <version>3.0.6</version>
</dependency>

<dependency>
   <groupId>org.springframework.boot</groupId>
   <artifactId>spring-boot-starter-test</artifactId>
   <version>3.0.6</version>
   <scope>test</scope>
</dependency>

<dependency>
   <groupId>com.fasterxml.jackson.core</groupId>
   <artifactId>jackson-databind</artifactId>
   <version>2.15.0</version>
</dependency>

<dependency>
   <groupId>com.fasterxml.jackson.core</groupId>
   <artifactId>jackson-core</artifactId>
   <version>2.15.0</version>
</dependency>

<dependency>
   <groupId>com.fasterxml.jackson.core</groupId>
   <artifactId>jackson-annotations</artifactId>
   <version>2.15.0</version>
</dependency>

```

