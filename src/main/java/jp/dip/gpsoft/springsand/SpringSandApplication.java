package jp.dip.gpsoft.springsand;

import java.util.Arrays;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;

import jp.dip.gpsoft.springsand.model.Color;
import jp.dip.gpsoft.springsand.model.Lake;
import jp.dip.gpsoft.springsand.model.River;
import jp.dip.gpsoft.springsand.model.Role;
import jp.dip.gpsoft.springsand.model.User;
import jp.dip.gpsoft.springsand.model.Valley;
import jp.dip.gpsoft.springsand.repository.ColorRepository;
import jp.dip.gpsoft.springsand.repository.LakeRepository;
import jp.dip.gpsoft.springsand.repository.RiverRepository;
import jp.dip.gpsoft.springsand.repository.RoleRepository;
import jp.dip.gpsoft.springsand.repository.UserRepository;
import jp.dip.gpsoft.springsand.repository.ValleyRepository;

@SpringBootApplication
public class SpringSandApplication {
	@Autowired
	private Environment env;

	@Autowired
	private RiverRepository riverRepository;

	@Autowired
	private LakeRepository lakeRepository;

	@Autowired
	private ValleyRepository valleyRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private ColorRepository colorRepository;
	
	@Autowired
	private PasswordEncoder pwEncoder;

	public static void main(String[] args) {
		SpringApplication.run(SpringSandApplication.class, args);
	}

	@Bean
	public CommandLineRunner runner() {
		return args -> {
			System.out.println("SpringSand application started!");

			if (userRepository.count() <= 0) {	// DB is empty?
				System.out.println("Populating DB with minimal data.");
				roleRepository.save(new Role(Role.ROLE_USER, "一般ユーザ"));
				roleRepository.save(new Role(Role.ROLE_ADMIN, "管理者"));
				userRepository.save(
						new User("admin", pwEncoder.encode("admin"), Role.ROLE_ADMIN));
				userRepository.save(
						new User("user", pwEncoder.encode("user"), Role.ROLE_USER));
				colorRepository.save(new Color("赤"));
				colorRepository.save(new Color("緑"));
				colorRepository.save(new Color("青"));
			}
			if (Arrays.asList(env.getActiveProfiles()).contains("dev")) {
				// サンプルデータをINSERTしておく。
				River shinano = new River("信濃川", "長野", "新潟");
				riverRepository.save(shinano);
				River tone = new River("利根川", "群馬", "千葉");
				riverRepository.save(tone);
				River amazon = new River("アマゾン川", "アンデス山脈", "ブラジル");
				riverRepository.save(amazon);
				Lake biwa = new Lake("琵琶湖", "滋賀県", 670);
				lakeRepository.save(biwa);
				Lake shinji = new Lake("宍道湖", "島根県", 79);
				lakeRepository.save(shinji);
				Lake hamana = new Lake("浜名湖", "静岡県", 65);
				lakeRepository.save(hamana);
				IntStream.rangeClosed(1, 100).forEach(n -> {
					valleyRepository.save(new Valley("谷" + String.format("%02d", n)));
				});
				valleyRepository.save(new Valley("耶馬渓"));
				valleyRepository.save(new Valley("帝釈峡"));
				valleyRepository.save(new Valley("仙酔峡"));
				valleyRepository.save(new Valley("Yosemite & Kalalau"));
			}
		};
	}
}
