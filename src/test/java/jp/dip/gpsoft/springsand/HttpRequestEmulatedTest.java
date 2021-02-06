package jp.dip.gpsoft.springsand;

import static org.hamcrest.Matchers.containsString;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * テストサンプル3
 * 
 * - サーバは起動せず、MockMvcにhttpをエミュレートしてもらう
 * - サーバを経由するより高速?
 */
@SpringBootTest
@AutoConfigureMockMvc
public class HttpRequestEmulatedTest {
	@Autowired
	private MockMvc mockMvc;

	@Test
	public void indexOfRivers() throws Exception {
		mockMvc.perform(get("/rivers"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("信濃川")));
	}

	@Test
	public void indexOfLakesShouldBeRedirected() throws Exception {
		mockMvc.perform(get("/lakes"))
				.andExpect(status().isFound()) // 302 response
				.andExpect(redirectedUrl("http://localhost/login"));
	}

	@Test
	public void login() throws Exception {
		mockMvc.perform(
				post("/login", new Object[0])
						.param("userid", "admin")
						.param("passwd", "admin")
						.with(SecurityMockMvcRequestPostProcessors.csrf()))
				//       ↑POSTのテストではCSRFトークンを付けてやらないと403エラーになる
				.andExpect(status().isFound()) // 302 response
				.andExpect(redirectedUrl("/"));
	}
}
