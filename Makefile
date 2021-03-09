MYSQLIMAGE ?= mysql:5.7.23
MYSQLCONTAINER ?= mysql57
MYSQLVOLUME ?= mysql57
MYSQLPASSWD ?= mysql
MYSQLPORTNO ?= 3306

HTTPDPORTNO ?= 8080

MYSQLHOST = $(shell docker inspect --format='{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' $(MYSQLCONTAINER))

CMD_LIST := startmysql attachmysql stopmysql

all:
	@echo Usage:
	@echo make startmysql
	@echo make attachmysql
	@echo make stopmysql
	@echo make prod

.PHONY: $(CMD_LIST)
.SILENT: $(CMD_LIST)

# Start the DB container.
startmysql:
	docker run --rm -it -d \
		--env MYSQL_ROOT_PASSWORD=$(MYSQLPASSWD) \
		--env TZ=Asia/Tokyo \
		-v $(MYSQLVOLUME):/var/lib/mysql \
		--name $(MYSQLCONTAINER) \
		-p $(MYSQLPORTNO):3306 \
		$(MYSQLIMAGE)
	echo root password: $(MYSQLPASSWD)
	echo server port: $(MYSQLPORTNO)

# Attach to the running container.
attachmysql:
	docker exec -it \
		$(MYSQLCONTAINER) bash

# Stop the MYSQL container.
stopmysql:
	docker stop $(MYSQLCONTAINER)

# Run app with production profile
prod:
	java -jar target/spring_sand-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod --spring.datasource.url=jdbc:mysql://$(MYSQLHOST):$(MYSQLPORTNO)/spring_sand --server.port=$(HTTPDPORTNO)

%:
	@:

