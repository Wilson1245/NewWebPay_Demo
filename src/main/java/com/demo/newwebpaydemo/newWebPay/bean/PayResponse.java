package com.demo.newwebpaydemo.newWebPay.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PayResponse {
    @JsonProperty("Action")
    private String action;
    @JsonProperty("MerchantID")
    private String merchantID;
    @JsonProperty("Version")
    private String version;
    @JsonProperty("TradeInfo")
    private String tradeInfo;
    @JsonProperty("TradeSha")
    private String tradeSha;

    public static PayResponse of(PaymentRequest request, String action) {
        PayResponse response = new PayResponse();
        response.setAction(action);
        response.setMerchantID(request.getMerchantID());
        response.setVersion(request.getVersion());
        response.setTradeInfo(request.getTradeInfo());
        response.setTradeSha(request.getTradeSha());
        return response;
    }
}
