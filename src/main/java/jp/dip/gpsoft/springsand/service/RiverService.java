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

	public List<River> findAllRivers() {
		return riverRepository.findAll();
	}
}
