package jp.dip.gpsoft.springsand.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.dip.gpsoft.springsand.model.Color;

public interface ColorRepository extends JpaRepository<Color, Integer> {
}
