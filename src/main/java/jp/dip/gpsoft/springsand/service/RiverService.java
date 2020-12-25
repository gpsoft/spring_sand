package jp.dip.gpsoft.springsand.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import jp.dip.gpsoft.springsand.model.River;
import jp.dip.gpsoft.springsand.repository.RiverRepository;

@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class RiverService {
	@Autowired
	private RiverRepository riverRepository;

	public River lookupRiver(Integer id) {
		return riverRepository.getOne(id);
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
