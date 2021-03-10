DBIMAGE ?= mysql:5.7.23
DBCONTAINER ?= mysql57
DBVOLUME ?= mysql57
DBPASSWD ?= mysql
DBPORTNO ?= 3306

PORT ?= 8080

DBHOST = $(shell docker inspect --format='{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' $(DBCONTAINER))

CMD_LIST := startdb attachdb stopdb jar prod deploy

all:
	@echo Usage:
	@echo make startdb
	@echo make attachdb
	@echo make stopdb
	@echo make jar
	@echo make prod
	@echo make deploy

.PHONY: $(CMD_LIST)
.SILENT: $(CMD_LIST)

# Start the DB container.
startdb:
	docker run --rm -it -d \
		--env MYSQL_ROOT_PASSWORD=$(DBPASSWD) \
		--env TZ=Asia/Tokyo \
		-v $(DBVOLUME):/var/lib/mysql \
		--name $(DBCONTAINER) \
		-p $(DBPORTNO):3306 \
		$(DBIMAGE) \
		mysqld --character-set-server=utf8mb4
	echo root password: $(DBPASSWD)
	echo server port: $(DBPORTNO)

# Attach to the running container.
attachdb:
	docker exec -it \
		$(DBCONTAINER) bash

# Stop the DB container.
stopdb:
	docker stop $(DBCONTAINER)

# Build jar
jar:
	./mvnw package spring-boot:repackage

# Run app with production profile
prod:
	PORT=$(PORT) java -jar target/spring_sand-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod,mysql --spring.datasource.url=jdbc:mysql://$(DBHOST):$(DBPORTNO)/spring_sand --spring.datasource.username=root --spring.datasource.password=mysql

# Deploy app on Heroku
deploy:
	heroku deploy:jar target/spring_sand-0.0.1-SNAPSHOT.jar --app spring-sand
	echo Opening a browser...
	heroku open --app spring-sand
	echo You can also run below commands:
	echo heroku run bash --app spring-sand
	echo heroku logs --tail --app spring-sand

%:
	@:

