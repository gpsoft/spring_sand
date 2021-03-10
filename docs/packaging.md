# Packaging

## ビルド

```sh
$ ./mvnw package spring-boot:repackage
```

## ローカルに起動

```sh
$ java -jar target/spring_sand-0.0.1-SNAPSHOT.jar
```

これで、いつも通り、8080番ポートで起動する。Eclipseのコンソールに出ていたログは、ターミナルに出力される。

## リアルに動かす作戦

開発時とは、以下の点が異なる。

- 80番ポートで起動
- ログは、少なめ
- ログは、ログファイル(`/var/tmp/spring_sand/log/spring.log`)へ出力
- DBは、H2ではなく、MySQLやPostgreSQLを使う

以上をプロパティファイルで切り替える。

- `application.properties` ...共通
- `application-dev.properties` ...開発時用
- `application-prod.properties` ...本番環境用
- `application-mysql.properties` ...MySQL用
- `application-postgres.properties` ...PostgreSQL用

## リアルに動かす準備

### ログファイル用のディレクトリ

```sh
$ mkdir -p /var/tmp/spring_sand/log
$ chmod -R 777 /var/tmp/spring_sand
```

`sudo`する必要があるかも。

### DDL(MySQL)

```sql
CREATE TABLE `rivers` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `source` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `mouse` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
CREATE TABLE `lakes` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `area` int(11) NOT NULL,
  `location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
CREATE TABLE `valleys` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
CREATE TABLE `roles` (
  `id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
CREATE TABLE `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `login_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `avatar` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
CREATE TABLE `user_role` (
  `user_id` int(11) NOT NULL,
  `role_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  PRIMARY KEY (`user_id`, `role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
```

### DDL(PostgreSQL)

```sql
CREATE TABLE rivers (
  id serial,
  name varchar(255) NOT NULL,
  source varchar(255) NOT NULL,
  mouse varchar(255) NOT NULL,
  PRIMARY KEY (id)
);
CREATE TABLE lakes (
  id serial,
  name varchar(255) NOT NULL,
  area integer NOT NULL,
  location varchar(255) NOT NULL,
  PRIMARY KEY (id)
);
CREATE TABLE valleys (
  id serial,
  name varchar(255) NOT NULL,
  PRIMARY KEY (id)
);
CREATE TABLE roles (
  id varchar(255) NOT NULL,
  name varchar(255) NOT NULL,
  PRIMARY KEY (id)
);
CREATE TABLE users (
  id serial,
  login_id varchar(255) NOT NULL,
  password varchar(255) NOT NULL,
  avatar text NOT NULL,
  PRIMARY KEY (id)
);
CREATE TABLE user_role (
  user_id integer NOT NULL,
  role_id varchar(255) NOT NULL,
  PRIMARY KEY (user_id, role_id)
);
```

## リアルに動かす

### ローカルで

```sh
$ sudo java -jar target/spring_sand-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod,mysql \
  --spring.datasource.url=jdbc:mysql://localhost:3306/spring_sand \
  --spring.datasource.username=root --spring.datasource.password=mysql
```

デフォルトでは、80番ポートを使用するので、`sudo`が必要。

ポート番号は環境変数`PORT`で変更可能。

```sh
$ PORT=8080 java -jar target/spring_sand-0.0.1-SNAPSHOT.jar ...以下同文
```

### Herokuへデプロイ

まず、Herokuへアプリ登録(一度だけ)。

```sh
$ heroku login
$ heroku apps:create --no-remote spring-sand
$ heroku addons:create heroku-postgresql:hobby-dev --app spring-sand
         # ↑これ間違ってるかも
$ heroku plugins:install java
```

DB接続のための情報を確認し、コンテナを起動して、`psql`で接続する。

```sh
$ heroku pg:info --app spring-sand
$ heroku pg:credentials:url --app spring-sand
$ heroku run bash --app spring-sand
# psql "postgres://lxaqh........."
```

デプロイして、ログ確認、ブラウザでアプリを開く。

```sh
$ heroku deploy:jar target/spring_sand-0.0.1-SNAPSHOT.jar --app spring-sand
$ heroku logs --tail --app spring-sand
$ heroku open --app spring-sand
```

GitリポジトリごとHerokuへ`push`することによりデプロイするスタイルもあるが、ここでは、ローカルでビルドした`jar`をデプロイするスタイルを採用している。

### Herokuの注意点

コンテナ側で自動定義される環境変数が大事。

- `PORT`が、httpサーバが使用すべきポート番号(80番じゃないよ)
- `JDBC_DATABASE_URL`が、`spring.datasource.url`の値
- `SPRING_DATASOURCE_USERNAME`が、`spring.datasource.username`の値
- `SPRING_DATASOURCE_PASSWORD`が、`spring.datasource.password`の値

これを踏まえて、`properties`ファイルを作成している。

また、デプロイ後のアプリの起動オプションは、`Procfile`に記述するのがHerokuの作法。

```
web: java -jar target/spring_sand-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod,postgres --logging.file.path=/app
```

