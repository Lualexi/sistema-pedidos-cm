package com.gestionpedidos.modules.user.service.impl;

import com.gestionpedidos.modules.user.model.SystemSetting;
import com.gestionpedidos.modules.user.model.User;
import com.gestionpedidos.modules.user.model.UserModulePermission;
import com.gestionpedidos.modules.user.repository.SystemSettingRepository;
import com.gestionpedidos.modules.user.repository.UserModulePermissionRepository;
import com.gestionpedidos.modules.user.repository.UserRepository;
import com.gestionpedidos.modules.user.service.SettingsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SettingsServiceImpl implements SettingsService {

    private final SystemSettingRepository settingRepository;
    private final UserModulePermissionRepository permissionRepository;
    private final UserRepository userRepository;

    public SettingsServiceImpl(
            SystemSettingRepository settingRepository,
            UserModulePermissionRepository permissionRepository,
            UserRepository userRepository
    ) {
        this.settingRepository = settingRepository;
        this.permissionRepository = permissionRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<SystemSetting> getSettings(String moduleKey) {
        if (moduleKey != null && !moduleKey.isBlank()) {
            return settingRepository.findByModuleKey(moduleKey.trim().toUpperCase());
        }
        return settingRepository.findAll();
    }

    @Override
    @Transactional
    public SystemSetting saveSetting(SystemSetting setting) {
        if (setting.getModuleKey() == null || setting.getModuleKey().isBlank()
                || setting.getSettingKey() == null || setting.getSettingKey().isBlank()
                || setting.getSettingValue() == null || setting.getSettingValue().isBlank()) {
            throw new RuntimeException("moduleKey, settingKey y settingValue son obligatorios.");
        }
        setting.setModuleKey(setting.getModuleKey().trim().toUpperCase());
        setting.setSettingKey(setting.getSettingKey().trim().toUpperCase());
        setting.setSettingValue(setting.getSettingValue().trim());
        return settingRepository.save(setting);
    }

    @Override
    @Transactional
    public void deleteSetting(Long id) {
        if (!settingRepository.existsById(id)) {
            throw new RuntimeException("Configuración no encontrada con ID: " + id);
        }
        settingRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getUsers(boolean onlyInactive) {
        List<User> all = userRepository.findAll();
        if (!onlyInactive) {
            return all;
        }
        LocalDateTime threshold = LocalDateTime.now().minusMonths(1);
        return all.stream()
                .filter(u -> Boolean.FALSE.equals(u.getActive())
                        || u.getLastLoginAt() == null
                        || u.getLastLoginAt().isBefore(threshold))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserModulePermission> getUserPermissions(Long userId) {
        return permissionRepository.findByUserId(userId);
    }

    @Override
    @Transactional
    public UserModulePermission saveUserPermission(Long userId, UserModulePermission permission) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + userId));

        String module = permission.getModuleKey().trim().toUpperCase();
        UserModulePermission target = permissionRepository.findByUserIdAndModuleKey(userId, module)
                .orElseGet(() -> UserModulePermission.builder().user(user).moduleKey(module).build());

        target.setCanView(Boolean.TRUE.equals(permission.getCanView()));
        target.setCanCreate(Boolean.TRUE.equals(permission.getCanCreate()));
        target.setCanEdit(Boolean.TRUE.equals(permission.getCanEdit()));
        target.setCanDelete(Boolean.TRUE.equals(permission.getCanDelete()));

        return permissionRepository.save(target);
    }
}