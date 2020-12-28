# Spring sand

## 概要

Springフレームワークの実験プロジェクト。

## 使い方

1. Eclispeのワークスペースフォルダの下で`git clone`
2. Eclipseのメニューから`File`→`Import`→`Existing Maven Projects`
3. Eclipseの`Package Explorer`でプロジェクトを右クリックして`Maven`→`Update project`
4. Eclipseの`Package Explorer`でプロジェクトを右クリックして`Run As`(または`Debug As`)→`Spring Boot App`
5. ブラウザで`http://localhost:8080/`を開く

## 実験内容

- `CRUD of Rivers` ...超シンプルなCRUDのサンプル
- `CRUD of Lakes` ...バリデーションチェックと`Form`クラスのサンプル
- `CRUD of Valleys` ...未定
- `Auth` ...未定
- `CRUD of Users` ...未定

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
