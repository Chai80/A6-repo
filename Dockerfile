# Compile our java files in this container
FROM openjdk:17-slim AS builder
COPY src /usr/src/cs6310/src
COPY lib /usr/src/cs6310/lib
WORKDIR /usr/src/cs6310
RUN find . -name "*.java" | xargs javac -cp lib/zip4j-2.7.0.jar -d ./target 
RUN jar cfe streaming_wars.jar Main -C ./target/ .

# Copy the jar and test scenarios into our final image
FROM openjdk:17-slim
WORKDIR /usr/src/cs6310
COPY test_scenarios ./
COPY ./lib/* ./lib/
COPY ./scripts/test_internal.sh ./
RUN mkdir db
RUN mkdir output
RUN mkdir test_results
COPY --from=builder /usr/src/cs6310/streaming_wars.jar ./streaming_wars.jar
CMD ["java", "-cp", "./lib/*:streaming_wars.jar", "Main", "commands_00.txt"]
