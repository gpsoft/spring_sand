package jp.dip.gpsoft.springsand.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

// @formatter:off
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private Environment env;
	
	@Autowired
	private UserDetailsService userDetailsService;

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
		// ひとまず、何でも許す
		web.ignoring().antMatchers("/**");
	}
}
// @formatter:on
