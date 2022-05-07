package com.tangshi.common.vo;

import com.tangshi.conferencesubscribe.domain.OrderMsg;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DateMsgResult {
    private String date;
    private List<OrderMsg> orderList;
}
