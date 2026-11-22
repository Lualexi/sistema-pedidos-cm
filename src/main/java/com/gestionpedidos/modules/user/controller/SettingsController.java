package com.gestionpedidos.modules.user.controller;

import com.gestionpedidos.modules.user.model.SystemSetting;
import com.gestionpedidos.modules.user.model.User;
import com.gestionpedidos.modules.user.model.UserModulePermission;
import com.gestionpedidos.modules.user.repository.UserRepository;
import com.gestionpedidos.modules.user.service.SettingsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/settings")
public class SettingsController {

    private final SettingsService settingsService;
    private final UserRepository userRepository;

    public SettingsController(SettingsService settingsService, UserRepository userRepository) {
        this.settingsService = settingsService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<List<SystemSetting>> getSettings(@RequestParam(required = false) String moduleKey) {
        return ResponseEntity.ok(settingsService.getSettings(moduleKey));
    }

    @PostMapping
    public ResponseEntity<SystemSetting> createSetting(@RequestBody SystemSetting setting) {
        return ResponseEntity.status(HttpStatus.CREATED).body(settingsService.saveSetting(setting));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSetting(@PathVariable Long id) {
        settingsService.deleteSetting(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers(@RequestParam(defaultValue = "false") boolean inactiveOnly) {
        return ResponseEntity.ok(settingsService.getUsers(inactiveOnly));
    }

    @GetMapping("/users/{userId}/permissions")
    public ResponseEntity<List<UserModulePermission>> getUserPermissions(@PathVariable Long userId) {
        return ResponseEntity.ok(settingsService.getUserPermissions(userId));
    }

    @PutMapping("/users/{userId}/permissions")
    public ResponseEntity<UserModulePermission> saveUserPermission(
            @PathVariable Long userId,
            @RequestBody UserModulePermission permission
    ) {
        return ResponseEntity.ok(settingsService.saveUserPermission(userId, permission));
    }

    @GetMapping("/my-permissions")
    public ResponseEntity<List<UserModulePermission>> getMyPermissions(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return userRepository.findByUsername(authentication.getName())
                .map(u -> ResponseEntity.ok(settingsService.getUserPermissions(u.getId())))
                .orElseGet(() -> ResponseEntity.ok(List.of()));
    }
}