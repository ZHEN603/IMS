# 使用Amazon Corretto 17作为基础镜像
FROM amazoncorretto:17

# 工作目录设置为/app
WORKDIR /app

# 将构建生成的jar文件复制到工作目录下
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

# 暴露应用运行的端口，根据你的应用实际情况调整
EXPOSE 9002
ENV SPRING_PROFILES_ACTIVE=prod
# 使用Corretto 17运行jar包
ENTRYPOINT ["java", "-jar", "app.jar"]