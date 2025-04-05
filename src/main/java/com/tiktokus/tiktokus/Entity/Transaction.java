package com.tiktokus.tiktokus.Entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tiktokus.tiktokus.Enum.TransactionType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Transaction extends Base {

    @Column
    private String description;

    @Column
    private float amount;

    @Column
    private float prevBalance;

    @Column
    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) // Tên cột khóa ngoại
    private User user;
}
