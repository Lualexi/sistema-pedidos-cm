package com.gestionpedidos.modules.user.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
    name = "user_module_permissions",
    uniqueConstraints = @UniqueConstraint(
        name = "uq_user_module",
        columnNames = {"user_id", "module_key"}
    )
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserModulePermission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "module_key", nullable = false, length = 40)
    private String moduleKey;

    @Column(name = "can_view", nullable = false)
    @Builder.Default
    private Boolean canView = true;

    @Column(name = "can_create", nullable = false)
    @Builder.Default
    private Boolean canCreate = false;

    @Column(name = "can_edit", nullable = false)
    @Builder.Default
    private Boolean canEdit = false;

    @Column(name = "can_delete", nullable = false)
    @Builder.Default
    private Boolean canDelete = false;
}