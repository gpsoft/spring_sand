package jp.dip.gpsoft.springsand.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import jp.dip.gpsoft.springsand.exception.NotFoundStatusException;
import jp.dip.gpsoft.springsand.model.Valley;
import jp.dip.gpsoft.springsand.repository.ValleyRepository;

@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class ValleyService {
	@Autowired
	private ValleyRepository valleyRepository;

	public Valley lookupValley(Integer id) {
		Optional<Valley> maybeValley = valleyRepository.findById(id);
		return maybeValley.orElseThrow(NotFoundStatusException::new);
	}

	public Page<Valley> findValleys(String name, Pageable pageable) {
		if (name == null || name.isEmpty()) {
			return valleyRepository.findAll(pageable);
		}
		name = "%" + name + "%";
		List<Valley> list = valleyRepository.findByNameLike(name, pageable);
		long count = valleyRepository.countByNameLike(name);
		return new PageImpl<Valley>(list, pageable, count);

	}

	@Transactional(readOnly = false)
	public void saveValley(Valley valley) {
		valleyRepository.save(valley);
	}

	@Transactional(readOnly = false)
	public void deleteValley(Integer id) {
		valleyRepository.deleteById(id);
	}
}
