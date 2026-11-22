package com.gestionpedidos.modules.user.service;

import com.gestionpedidos.modules.user.model.SystemSetting;
import com.gestionpedidos.modules.user.model.User;
import com.gestionpedidos.modules.user.model.UserModulePermission;

import java.util.List;

public interface SettingsService {
    List<SystemSetting> getSettings(String moduleKey);
    SystemSetting saveSetting(SystemSetting setting);
    void deleteSetting(Long id);

    List<User> getUsers(boolean onlyInactive);
    List<UserModulePermission> getUserPermissions(Long userId);
    UserModulePermission saveUserPermission(Long userId, UserModulePermission permission);
}