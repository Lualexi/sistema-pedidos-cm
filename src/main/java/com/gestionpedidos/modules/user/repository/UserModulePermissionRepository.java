package com.gestionpedidos.modules.user.repository;

import com.gestionpedidos.modules.user.model.UserModulePermission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserModulePermissionRepository extends JpaRepository<UserModulePermission, Long> {
    List<UserModulePermission> findByUserId(Long userId);
    Optional<UserModulePermission> findByUserIdAndModuleKey(Long userId, String moduleKey);
}