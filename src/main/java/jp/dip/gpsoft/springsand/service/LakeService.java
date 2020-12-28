package jp.dip.gpsoft.springsand.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import jp.dip.gpsoft.springsand.exception.NotFoundStatusException;
import jp.dip.gpsoft.springsand.model.Lake;
import jp.dip.gpsoft.springsand.repository.LakeRepository;

@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class LakeService {
	private static String[] prefectures;

	static {
		prefectures = new String[] {
				"北海道", "青森県", "岩手県", "宮城県", "秋田県",
				"山形県", "福島県", "茨城県", "栃木県", "群馬県",
				"埼玉県", "千葉県", "東京都", "神奈川県", "新潟県",
				"富山県", "石川県", "福井県", "山梨県", "長野県",
				"岐阜県", "静岡県", "愛知県", "三重県", "滋賀県",
				"京都府", "大阪府", "兵庫県", "奈良県", "和歌山県",
				"鳥取県", "島根県", "岡山県", "広島県", "山口県",
				"徳島県", "香川県", "愛媛県", "高知県", "福岡県",
				"佐賀県", "長崎県", "熊本県", "大分県", "宮崎県",
				"鹿児島県", "沖縄県"
		};
	}

	@Autowired
	private LakeRepository lakeRepository;

	public Lake lookupLake(Integer id) {
		Optional<Lake> maybeLake = lakeRepository.findById(id);
		return maybeLake.orElseThrow(NotFoundStatusException::new);
	}

	public List<Lake> findAllLakes() {
		return lakeRepository.findAll();
	}

	@Transactional(readOnly = false)
	public void saveLake(Lake lake) {
		lakeRepository.save(lake);
	}

	@Transactional(readOnly = false)
	public void deleteLake(Integer id) {
		lakeRepository.deleteById(id);
	}

	public static boolean isPref(String val) {
		return Arrays.asList(prefectures).contains(val);
	}

	public static String[] allPrefs() {
		return prefectures;
	}
}
