package jp.dip.gpsoft.springsand;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import jp.dip.gpsoft.springsand.controller.ValleyController;
import jp.dip.gpsoft.springsand.model.CustomUserDetails;
import jp.dip.gpsoft.springsand.model.Role;
import jp.dip.gpsoft.springsand.model.User;
import jp.dip.gpsoft.springsand.model.Valley;
import jp.dip.gpsoft.springsand.repository.ColorRepository;
import jp.dip.gpsoft.springsand.repository.LakeRepository;
import jp.dip.gpsoft.springsand.repository.RiverRepository;
import jp.dip.gpsoft.springsand.repository.RoleRepository;
import jp.dip.gpsoft.springsand.repository.UserRepository;
import jp.dip.gpsoft.springsand.repository.ValleyRepository;
import jp.dip.gpsoft.springsand.service.ValleyService;

/**
 * テストサンプル4
 * 
 * - Webレイヤのみをロードしたテスト
 * - 他のテストに比べると、多少、起動が速いのかなぁ
 * - 「Webレイヤ」が具体的にどこを指すのか、いまいち不明
 * - おそらく、ApplicationとControllerだけなのかな
 * - あと、View(HTML生成の部分)もかな
 * - ServiceやRepositoryはロードされない
 * - ApplicationとControllerが、これらのDIを期待している場合は、ここでモック化する必要がある
 * - 特定のコントローラに絞ってテストするのが現実的
 */
@WebMvcTest(controllers = ValleyController.class)
public class WebLayerTests {
	@Autowired
	private MockMvc mockMvc;

	// 以下、SpringSandApplicationがDIしてもらってるbeanをモック化
	@MockBean
	private RiverRepository riverRepository;

	@MockBean
	private LakeRepository lakeRepository;

	@MockBean
	private ValleyRepository valleyRepository;

	@MockBean
	private UserRepository userRepository;

	@MockBean
	private RoleRepository roleRepository;

	@MockBean
	private ColorRepository colorRepository;

	@MockBean
	private BCryptPasswordEncoder pwEncoder;

	// ValleyControllerがDIしてもらってるbeanもモック化
	@MockBean
	private ValleyService valleyService;

	@Test
	public void indexOfValleysShouldBeRedirected() throws Exception {
		mockMvc.perform(get("/valleys"))
				// ログインせずにアクセスしたら、ログインページへリダイレクトされること。
				.andExpect(status().isFound()) // 302 response
				.andExpect(redirectedUrl("http://localhost/login"));
	}

	@Test
	@WithMockUser(username = "user", roles = "USER")
	public void forbidUserRole() throws Exception {
		mockMvc.perform(get("/valleys"))
				// USERロールでアクセスしたら、403 Forbiddenを返すこと。
				.andExpect(status().isForbidden()); // 403 response
	}

	@Test
	public void indexOfValleys() throws Exception {
		// Valley一覧のページでは、
		// ページ上部にアバターを表示するようになっているため
		// UserDetailsオブジェクトを仕込んでやる必要がある。
		CustomUserDetails ud = new CustomUserDetails(
				new User("admin", "", Role.ROLE_ADMIN));

		// ValleyControllerの中で
		// ValleyServiceのfindValleys()メソッドが呼ばれるので
		// モック化して、空っぽのPage<Valley>オブジェクトを返してやる。
		// 「空っぽのPage<Valley>オブジェクト」を作るためには、
		// Pageableオブジェクトが必要だが、それはfindValleys()の第2引数で来る。
		// この辺のモック化はちょっと複雑。
		doAnswer(new Answer<Page<Valley>>() {
			@Override
			public Page<Valley> answer(InvocationOnMock invocation) throws Throwable {
				Object[] args = invocation.getArguments();
				// ↑これが、findValleys()の引数リスト。
				return Page.empty((Pageable) args[1]);
			}
		}).when(valleyService).findValleys(any(), any());

		mockMvc.perform(get("/valleys").with(user(ud)))
				// ADMINロールでアクセスしたら、200 OKを返すこと。
				.andExpect(status().isOk())
				// Valley一覧は空っぽ。
				.andExpect(content().string(containsString("No valleys matched")));
	}
}
