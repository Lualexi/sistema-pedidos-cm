package com.gestionpedidos.modules.user.repository;

import com.gestionpedidos.modules.user.model.SystemSetting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SystemSettingRepository extends JpaRepository<SystemSetting, Long> {
    List<SystemSetting> findByModuleKey(String moduleKey);
}