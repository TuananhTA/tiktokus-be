 # Sử dụng image OpenJDK để chạy ứng dụng
FROM openjdk:17-jdk-slim

# Thiết lập thư mục làm việc
WORKDIR /app

# Sao chép file JAR vào image
COPY target/tiktokUs-0.0.1-SNAPSHOT.jar app.jar

# Mở port 8080
EXPOSE 8081

# Chạy ứng dụng
ENTRYPOINT ["java", "-jar", "app.jar"]
