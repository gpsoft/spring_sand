package jp.dip.gpsoft.springsand;

import static org.assertj.core.api.Assertions.assertThat;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

/**
 * テストサンプル2
 * 
 * - 適当なポートでサーバを起動
 * - GETリクエストを投げて、レスポンスHTMLの内容を検証
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class HttpRequestTests {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	private String url(String path) {
		return "http://localhost:" + port + path;
	}

	private String get(String path) {
		return restTemplate.getForObject(url(path), String.class);
	}

	@Test
	public void landingPage() throws Exception {
		Document doc = Jsoup.parse(get("/"));

		assertThat(doc.select(".user-info").text()).as("ログインボタン").isEqualTo("Login");
		assertThat(doc.select("h1").text()).as("見出し").isEqualTo("Menu");
		assertThat(doc.select("ul.menu li").size()).as("メニューアイテム数").isEqualTo(5);
		assertThat(doc.select("ul.menu").text()).as("メニュー")
				.contains("CRUD of Rivers")
				.contains("CRUD of Rivers(Colored)")
				.contains("CRUD of Lakes")
				.contains("CRUD of Valleys")
				.contains("CRUD of Users");
	}
}
