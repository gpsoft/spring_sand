package jp.dip.gpsoft.springsand.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private Environment env;

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Autowired
	private UserDetailsService userDetailsService;

	// @formatter:off
	// ■セキュリティの基本
	//   - 認証(authorization) ... 誰? ホントに山田さんか?
	//   - 認可(authentication) ... 山田さんに、ここ見せていいの?
	//
	// ■考えること
	//   - 認証方法
	//     - Basic認証
	//     - フォーム認証(ユーザ名/パスワード入力と、DBのユーザテーブル照合)
	//     - LDAP認証
	//     - その他
	//   - 認可方法
	//     - URLのパスに応じて
	//     - ユーザの権限(ロール)に応じて

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring()					// 無視(認証も認可も不要)
				.antMatchers("/css/**", "/js/**", "/fonts/**", "/img/**");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		if (Arrays.asList(env.getActiveProfiles()).contains("dev")) {
			// H2コンソール用にセキュリティを緩める
			http.authorizeRequests()
					.antMatchers("/h2-console/**").permitAll()
				.and().csrf()			// CSRF対策に関する設定
					.ignoringAntMatchers("/h2-console/**")
				.and().headers()		// レスポンスヘッダの設定
					.frameOptions()			// X-Frame-Optionsヘッダの設定
					.sameOrigin();
		}

		http.authorizeRequests()		// 認可に関する設定
				// URLパスのパターンと、認可条件のペアで指定
				.antMatchers("/", "/rivers/**").permitAll()		// 誰でもOK
				.antMatchers("/login", "/auth").permitAll()
				.antMatchers("/logout").authenticated()			// 認証済みなら誰でもOK
				.antMatchers("/lakes/**").authenticated()
				.antMatchers("/valleys/**").hasRole("ADMIN")	// ロールチェック
				.anyRequest().denyAll();						// 上記以外は、全部NG

		http.formLogin()				// フォーム認証に関する設定
										// デフォルトでは:
										//   - ログインページのURLは/login
										//   - /loginへ、usernameとpasswordをPOSTすると
										//     ログイン処理がおこなわれる
				.loginPage("/login")				// ログインページのURL
				.usernameParameter("userid")		// useridと
				.passwordParameter("passwd")		// passwdを
				.loginProcessingUrl("/auth")		// /authへPOSTするとログイン処理を実行
				.failureUrl("/login?error")			// ログイン失敗時の遷移先
				.defaultSuccessUrl("/");			// ログイン後のデフォルトの遷移先

		http.logout()					// ログアウトに冠する設定
				.logoutUrl("/logout")				// ログアウトURL(ここへPOSTすると自動ログアウト)
				.logoutSuccessUrl("/?logout");		// ログアウト後の遷移先
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		//if (Arrays.asList(env.getActiveProfiles()).contains("dev")) {
		//	auth.inMemoryAuthentication()	// DBを使わずに認証するための設定
		//			.withUser("test")
		//				.password(passwordEncoder().encode("test"))
		//				.roles("USER");
		//}
		// ■DBを使って認証するには
		// ◇必要なもの
		//   - UserDetailsインターフェイスをimplementsしたクラス
		//     - SpringのUserクラスでもOK
		//   - UserDetailsServiceをimplementsしたクラス
		auth.userDetailsService(userDetailsService)
				.passwordEncoder(passwordEncoder());
	}
	// @formatter:on
}
