package jp.dip.gpsoft.springsand;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import jp.dip.gpsoft.springsand.model.River;
import jp.dip.gpsoft.springsand.repository.RiverRepository;

/**
 * テストサンプル1
 * 
 * - @SpringBootTestを使用
 * - SpringSandApplicationクラスと同じパッケージに置いた方が良さそう
 * - いつも通り、アプリ全体がロードされるイメージかな?
 * - リポジトリ、サービス、モデルなどの単体テストに使えそう
 */
@SpringBootTest
class SpringSandApplicationTests {

	@Autowired
	private RiverRepository rr;

	@Test
	void contextLoads() {
		assertThat(rr).isNotNull();
	}

	@Test
	void someRiversPreset() {
		List<River> lis = rr.findAll();
		assertThat(lis.size()).isEqualTo(3);
	}

	@Test
	void riverIsNotNew() {
		List<River> lis = rr.findAll();
		assertThat(lis.get(0).isNew()).isFalse();
	}
}
