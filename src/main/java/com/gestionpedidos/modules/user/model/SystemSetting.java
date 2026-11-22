package com.gestionpedidos.modules.user.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
    name = "system_settings",
    uniqueConstraints = @UniqueConstraint(
        name = "uq_module_key_value",
        columnNames = {"module_key", "setting_key", "setting_value"}
    )
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SystemSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "module_key", nullable = false, length = 40)
    private String moduleKey;

    @Column(name = "setting_key", nullable = false, length = 80)
    private String settingKey;

    @Column(name = "setting_value", nullable = false, length = 160)
    private String settingValue;

    @Column(length = 200)
    private String description;
}