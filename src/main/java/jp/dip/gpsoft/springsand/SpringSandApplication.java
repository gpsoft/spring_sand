package jp.dip.gpsoft.springsand;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import jp.dip.gpsoft.springsand.model.River;
import jp.dip.gpsoft.springsand.repository.RiverRepository;

@SpringBootApplication
public class SpringSandApplication {

	@Autowired
	private RiverRepository riverRepository;

	public static void main(String[] args) {
		SpringApplication.run(SpringSandApplication.class, args);
	}

	@Bean
	public CommandLineRunner runner() {
		return args -> {
			System.out.println("SpringSand application started!");

			// サンプルデータをINSERTしておく。
			River shinano = new River("信濃川", "長野", "新潟");
			riverRepository.save(shinano);
			River tone = new River("利根川", "群馬", "千葉");
			riverRepository.save(tone);
			River amazon = new River("アマゾン川", "アンデス山脈", "ブラジル");
			riverRepository.save(amazon);
		};
	}
}
