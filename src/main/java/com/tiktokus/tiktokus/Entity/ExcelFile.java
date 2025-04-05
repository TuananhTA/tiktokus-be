package com.tiktokus.tiktokus.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ExcelFile extends Base {

    @Lob
    private byte[] fileData;
}
