# Packaging

## ビルド

```sh
$ ./mvnw package spring-boot:repackage
```

これで、`target`ディレクトリの下に`jar`ファイルができる。テストをスキップしたい場合は、`-DskipTests`オプションを付ければ良い。

## ローカルに起動

Eclipseを使わずに、ローカルに起動する。

```sh
$ java -jar target/spring_sand-0.0.1-SNAPSHOT.jar
```

ビルドした`jar`ファイルには、Tomcatが内蔵されている。これで、いつも通り、8080番ポートでWebサーバが動き出す。Eclipseのコンソールに出ていたログは、ターミナルに出力される。

## 本番っぽくリアルに動かす作戦

開発時とは、以下の点が異なる。

- 80番ポートで起動
- ログは、少なめにする
- ログは、ログファイル(`/var/tmp/spring_sand/log/spring.log`)へ出力
- DBは、H2ではなく、MySQLやPostgreSQLを使う

以上をプロパティファイルで切り替える。

- `application.properties` ...共通
- `application-dev.properties` ...開発時用
- `application-prod.properties` ...本番環境用
- `application-mysql.properties` ...MySQL用
- `application-postgres.properties` ...PostgreSQL用

プロファイルを細かく分割しておけば、その中から任意のプロファイルを組み合わせて、いろんな構成でアプリを起動することができる。

## リアルに動かす準備

### ログファイル用のディレクトリ

```sh
$ mkdir -p /var/tmp/spring_sand/log
$ chmod -R 777 /var/tmp/spring_sand
```

`sudo`する必要があるかも。

### テーブルを作る(MySQL)

```sql
CREATE TABLE `rivers` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `source` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `mouse` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `color_id` int(11),
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
CREATE TABLE `colors` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
```

### テーブルを作る(PostgreSQL)

```sql
CREATE TABLE rivers (
  id serial,
  name varchar(255) NOT NULL,
  source varchar(255) NOT NULL,
  mouse varchar(255) NOT NULL,
  color_id integer,
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
CREATE TABLE colors (
  id serial,
  name varchar(255) NOT NULL,
  PRIMARY KEY (id)
);
```

## リアルに動かす(自前のサーバで動かす場合)

```sh
$ sudo java -jar target/spring_sand-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod,mysql \
  --spring.datasource.url=jdbc:mysql://localhost:3306/spring_sand \
  --spring.datasource.username=root --spring.datasource.password=mysql
```

`prod`と`mysql`プロファイルをアクティブにしている。また、80番ポートを使用するので、Linuxでは`sudo`が必要。ポート番号は環境変数`PORT`で変更可能。

```sh
$ PORT=8080 java -jar target/spring_sand-0.0.1-SNAPSHOT.jar ...以下同文
```

上記のコマンドは、同じマシン上でMySQLが動いている想定。

## リアルに動かす(Herokuへデプロイする場合)

### 前提

- Herokuにサインアップ済み
- Heroku CLIをインストールし、`heroku`コマンドが使える状態

JavaプロジェクトをHerokuへデプロイする方法は、2つある。

- Gitリポジトリを丸ごとHerokuへプッシュする
- `jar`ファイルのみをHerokuへアップロードする

ここで紹介するのは、後者の方法。大まかな流れは以下の通り。

1. Javaプロジェクト用のHerokuプラグインをインストールする
1. Herokuアプリを新規作成する
1. PostgresのDBを作り、Herokuアプリと紐付ける
1. Postgresへ接続するためのプロファイル(`properties`ファイル)を作る
1. アプリ起動スクリプト`Procfile`ファイルを作る
1. `jar`ファイルをビルドする
1. デプロイする

### 準備

Herokuへログイン。

```sh
$ heroku login
```

Javaプロジェクト用のHerokuプラグインをインストール。

```sh
$ heroku plugins:install java
```

↑これにより、`heroku deploy:jar`コマンドが使えるようになる(あとで使う)。

Herokuアプリを新規作成。

```sh
$ heroku apps:create --no-remote spring-sand
```

↑ここでは、`spring-sand`というアプリ名を指定した。以降、`heroku`コマンドを使うときは、アプリを明示するために`--app spring-sand`オプションを付ける。

PostgresのDBを作り、Herokuアプリと紐付ける。

```sh
$ heroku addons:create heroku-postgresql:hobby-dev --app spring-sand
```

↑これにより、HerokuのPostgresサーバ上に、`spring-sand`用のアカウントやDBが作成される。Postgresサーバは、アプリとは別のサーバで動いている、という点に注意。

DB接続のための情報を確認する。

```sh
$ heroku pg:info --app spring-sand
$ heroku pg:credentials:url --app spring-sand
```

↑ここで最後に表示される`Connection URL`の値をコピっておく。

コンテナを起動して、Postgresのクライアントツール`psql`でDBへ接続する。

```sh
$ heroku run bash --app spring-sand
# psql "postgres://lxaqh........."
```

↑`psql`の引数には、コピっておいたURLを指定。これでDBへ接続できるので、必要なテーブルを作る。

よく使うPostgresのコマンドは以下の通り。

```
\l      ...DB一覧を表示
\dt     ...テーブル一覧を表示
\d hoge ...hogeテーブルの詳細を表示
```

テーブルを作成したら、`exit`して`psql`を終了し、更にもう一度`exit`してコンテナも終了する。

以上で、Heroku側の準備は完了。

### デプロイ

プロファイル(`properties`ファイル)にPostgresへ接続するための情報を記述しておく。と言っても、Herokuが環境変数に設定しておいてくれるので、それを参照するようにしておけば良い。

```properties:application-postgres.properties
spring.datasource.driver-class-name=org.postgresql.Driver
spring.datasource.url=${JDBC_DATABASE_URL}
spring.datasource.username=${SPRING_DATASOURCE_USERNAME}
spring.datasource.password=${SPRING_DATASOURCE_PASSWORD}
```

デプロイ後にアプリが起動されるよう、プロジェクトのトップディレクトリに`Procfile`ファイルを用意しておく(Herokuは、この内容に従ってアプリを起動してくれる)。

```procfile:Procfile
web: java -jar target/spring_sand-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod,postgres --logging.file.path=/app
```

↑ここでは、`prod`と`postgres`プロファイルをアクティブにしている。また、ログファイルの出力場所は、`/var/tmp/spring_sand/log/`ではなく、`/app`に変えた。

デプロイして、ブラウザを開き、ログを確認する。

```sh
$ heroku deploy:jar target/spring_sand-0.0.1-SNAPSHOT.jar --app spring-sand
$ heroku open --app spring-sand
$ heroku logs --tail --app spring-sand
```

現状、HerokuのJavaのデフォルトバージョンはJDK8のようだ。バージョンを明示したい場合は、`heroku deploy:jar`コマンドに`--jdk`オプションを付ければ良い。

```sh
$ heroku deploy:jar target/spring_sand-0.0.1-SNAPSHOT.jar --jdk 11 --app spring-sand
```

### Herokuの注意点

Herokuコンテナ側で自動定義される環境変数が大事。

- `PORT`が、httpサーバが使用すべきポート番号(80番じゃないよ)
- `JDBC_DATABASE_URL`が、`spring.datasource.url`の値
- `SPRING_DATASOURCE_USERNAME`が、`spring.datasource.username`の値
- `SPRING_DATASOURCE_PASSWORD`が、`spring.datasource.password`の値

これを踏まえて、`properties`ファイルを作成している。

トラブルシューティングはログ頼みなので、最初のうちは、ログをコンソールに出力した方がいいかも(そうすれば、`heroku logs`コマンドで参照できる)。
