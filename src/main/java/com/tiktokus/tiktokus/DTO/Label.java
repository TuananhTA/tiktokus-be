package com.tiktokus.tiktokus.DTO;

import com.tiktokus.tiktokus.Enum.OrderType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Label {
    private String labelUrl;
    private String tracking;
    private OrderType type;
}
