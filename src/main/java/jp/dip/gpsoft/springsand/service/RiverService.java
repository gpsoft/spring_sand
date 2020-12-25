package jp.dip.gpsoft.springsand.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import jp.dip.gpsoft.springsand.exception.NotFoundStatusException;
import jp.dip.gpsoft.springsand.model.River;
import jp.dip.gpsoft.springsand.repository.RiverRepository;

@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class RiverService {
	@Autowired
	private RiverRepository riverRepository;

	public River lookupRiver(Integer id) {
		Optional<River> maybeRiver = riverRepository.findById(id);
		return maybeRiver.orElseThrow(NotFoundStatusException::new);
	}

	public List<River> findAllRivers() {
		return riverRepository.findAll();
	}

	@Transactional(readOnly = false)
	public void saveRiver(River river) {
		riverRepository.save(river);
	}

	@Transactional(readOnly = false)
	public void deleteRiver(Integer id) {
		riverRepository.deleteById(id);
	}
}
