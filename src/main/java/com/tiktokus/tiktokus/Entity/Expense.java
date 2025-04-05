package com.tiktokus.tiktokus.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Expense extends Base {

    @Column
    private float expense;

    @Column
    private int step;

    @Column
    private float lableCosts;
}
