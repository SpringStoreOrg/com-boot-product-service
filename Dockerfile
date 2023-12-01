FROM eclipse-temurin:17-jdk-alpine
COPY ./target/*.jar product-service.jar 
ENTRYPOINT ["java","-jar","product-service.jar"]