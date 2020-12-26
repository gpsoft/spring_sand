package jp.dip.gpsoft.springsand.service;

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
}
