# Spring sand

## 概要

Springフレームワークの実験プロジェクト。

- [プレゼンスライド](https://gpsoft.github.io/slidarch/spring_sand.html)
- [Liveデモ(Herokuなので起きるのに1分弱かかるよ)](https://spring-sand.herokuapp.com/)

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
- `Build(Packaging)` ...パッケージングとデプロイ(Heroku)

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

`jar`ファイルをビルドして、ローカルに実行したり、Herokuへデプロイする。

[詳しくは、こちら](docs/packaging.md)
