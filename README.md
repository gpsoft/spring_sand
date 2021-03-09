# Spring sand

## 概要

Springフレームワークの実験プロジェクト。

[プレゼンスライド](https://gpsoft.github.io/slidarch/spring_sand.html)もあるよ。

## 使い方

1. Eclispeのワークスペースフォルダの下で`git clone`
2. Eclipseのメニューから`File`→`Import`→`Existing Maven Projects`
3. Eclipseの`Package Explorer`でプロジェクトを右クリックして`Maven`→`Update project`
4. Eclipseの`Package Explorer`でプロジェクトを右クリックして`Run As`(または`Debug As`)→`Spring Boot App`
5. ブラウザで`http://localhost:8080/`を開く

## 実験内容

- `CRUD of Rivers` ...超シンプルなCRUDのサンプル
- `CRUD of Lakes` ...バリデーションチェックと`Form`クラスのサンプル
- `CRUD of Valleys` ...検索、ページャ、ソートのサンプル
- `Auth` ...AuthenticationとAuthorization
- `CRUD of Users` ...ファイルアップロード、Many-to-many関連など
- `Layout` ...全ページに共通レイアウトを適用
- `Test` ...テストの自動化
- `Build(Packaging)` ...パッケージング

## Rivers

| HTTPメソッド|URL |Controllerメソッド|テンプレート |
| :---------- | :- | :--------------- | :---------- |
| GET|`/rivers`|`index()`|`river/index.html` |
| GET|`/rivers/1`|`show(id)`|`river/details.html` |
| GET|`/rivers/new`|`newRiver()`|`river/form.html` |
| GET|`/rivers/1/edit`|`editRiver(id)`|`river/form.html` |
| POST|`/rivers`|`create()`|`/rivers`へリダイレクト |
| POST|`/rivers/1`|`update(id)`|`/rivers`へリダイレクト |
| POST|`/rivers/1/delete`|`destroy(id)`|`/rivers`へリダイレクト |

CRUDの基本。あえて、`PUT`や`DELETE`メソッドは使わない。

## Lakes

| HTTPメソッド|URL |Controllerメソッド|テンプレート |
| :---------- | :- | :--------------- | :---------- |
| GET|`/lakes`|`index()`|`lake/index.html` |
| GET|`/lakes/1`|`show(id)`|`lake/details.html` |
| GET|`/lakes/new`|`newLake()`|`lake/form.html` |
| GET|`/lakes/1/edit`|`editLake(id)`|`lake/form.html` |
| POST|`/lakes`|`create()`|成功時は`/lakes`へリダイレクト。失敗時は`lake/form.html` |
| POST|`/lakes/1`|`update(id)`|成功時は`/lakes`へリダイレクト。失敗時は`lake/form.html` |
| POST|`/lakes/1/delete`|`destroy(id)`|`/lakes`へリダイレクト |

Riversとの大きな違いは2つ。

- まぁまぁ真面目にバリデーションチェックする
- ControllerとViewの間のデータ交換用に、`Lake`クラスではなく`LakeForm`クラスを使う

`LakeForm`クラスを導入する理由は、入力画面の入力項目とモデルのフィールドが1:1に対応しないから。

## Valleys

構成はRiversと同じ。一覧ページ(`/valleys`へのGETリクエスト)に、以下の機能を追加した。

- 検索
- ページング
- ソート

クエリ文字列パラメータは以下の通り。

- `q` ...検索キーワード(部分一致)
- `page` ...ページ番号(0オリジン)
- `sort` ...ソートキー(`id`か`name`)と方向(`asc`か`desc`)

`sort`パラメータは複数指定可能(`/valleys?sort=id,asc&sort=name,desc`など)。

また、`@Query`アノテーションを利用して任意の`SELECT`文を実行するサンプルにもなっている。ポイントは以下の通り。

- 検索キーワードの有無によらず、1つの`SELECT`文で済むように、`WHERE`句を細工した
- `countQuery`要素を指定することにより、ページングに使えるようにした

## Auth

### Authentication(認証)

- admin/adminでログイン(ADMINロール)
- user/userでログイン(USERロール)
- Remember me機能付き
- `AuthController`

| HTTPメソッド|URL |Controllerメソッド|テンプレート |
| :---------- | :- | :--------------- | :---------- |
| GET|`/login`|`loginForm()`|`auth/form.html` |
| POST|`/login`| Spring Security |ログイン失敗なら、`/loginError`へForward |
| - |`/loginError`| `loginError()` |`auth/form.html` |

### Authorization(認可)

- Riversは誰でも可
- Lakesは、ログイン必須
- ValleysとUsersは、ADMINロールが必要

## Users

- アバター画像のアップロード
- チェックボックスで、ロールを複数選択
- バリデーションチェックのグループ分け(新規登録のときだけ特定のバリデーションチェックを行う、とか)
- ロールはマスターテーブルで管理し、ユーザテーブルとMany-to-manyで関連付けることにした

## Test

- `SpringSandApplicationTests` ...基本。全レイヤを結合
- `HttpRequestTests` ...httpリクエスト・レスポンスレベルのテスト
- `HttpRequestEmulatedTest` ...httpサーバ無し。エミュレートされたリクエストでテスト
- `WebLayerTests` ...Webレイヤのみをテスト

実行方法は…。

- Eclipseの`Package Explorer`でプロジェクトを右クリックして`Run As`(または`Debug As`)→`JUnit Test`
- あるいは、特定のテストクラスを右クリック

## Packaging

### ビルド

```sh
$ ./mvnw package spring-boot:repackage
```

### 起動

```sh
$ java -jar target/spring_sand-0.0.1-SNAPSHOT.jar
```

いつも通り、8080番ポートで起動する。Eclipseのコンソールに出ていたログは、ターミナルに出力される。

### 本番環境

開発時とは、以下の点が異なる。

- 80番ポートで起動
- ログは、少なめ
- ログは、ログファイル(`/var/tmp/spring_sand/log/spring.log`)へ出力
- DBは、H2ではなく、MySQLを使う

以上をプロパティファイルで切り替える。

- `application.properties` ...共通
- `application-dev.properties` ...開発時用
- `application-prod.properties` ...本番環境用

ログファイル用のディレクトリを作成(Linux用)。

```sh
$ sudo mkdir -p /var/tmp/spring_sand/log
$ sudo chmod -R 777 /var/tmp/spring_sand
```

起動時に、アクティブプロファイルを指定。

```sh
$ sudo java -jar target/spring_sand-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```

Linuxで80番ポートを使う場合は、`sudo`が必要(Windowsでは不要?)。

本番DBのパスワードをファイルに書くのは避けたい、という場合は、コマンドラインで指定しても良い。

```sh
$ sudo java -jar target/spring_sand-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod --spring.datasource.password=StrongPassword123
```

### DDL

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
