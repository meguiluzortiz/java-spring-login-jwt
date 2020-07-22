# Usage:
# make        # start the application
# make debug  # start the application in debug mode

.DEFAULT_GOAL := start
start:
	sh mvnw spring-boot:run

debug:
	sh mvnw spring-boot:run -Dspring-boot.run.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=5005"

clean-verify:
	sh mvnw clean verify

verify:
	sh mvnw -DskipTests verify

sonar-local:
	sh mvnw -Psonar,sonar-local sonar:sonar

sonar-remote:
	sh mvnw -Psonar,sonar-remote sonar:sonar
