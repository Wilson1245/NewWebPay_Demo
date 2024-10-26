package com.demo.newwebpaydemo.service;

import com.demo.newwebpaydemo.newWebPay.NewWebUtil;
import com.demo.newwebpaydemo.newWebPay.bean.PayResponse;
import com.demo.newwebpaydemo.newWebPay.bean.PaymentRequest;
import com.demo.newwebpaydemo.newWebPay.bean.PaymentResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Random;

@Slf4j
@Service
public class PayService {

    @Value("${new-web-pay.key}")
    private String key;

    @Value("${new-web-pay.iv}")
    private String iv;

    @Value("${new-web-pay.mid}")
    private String mid;

    @Value("${new-web-pay.pay-url}")
    private String payUrl;

    @Value("${new-web-pay.query-trade-info-url}")
    private String queryTradeInfoUrl;

    @Value("${new-web-pay.close-url}")
    private String closeUrl;

    @Value("${new-web-pay.notify-url}")
    private String notifyUrl;

    public PayResponse pay() {
        PaymentRequest request = new PaymentRequest(mid, key, iv, getRandomString(15), 1000, "Test測試購買", notifyUrl);
        return PayResponse.of(request, payUrl);
    }

    public void notify(String tradeInfo) {
        String response = NewWebUtil.decryptAES(tradeInfo, key, iv);
        try {
            PaymentResponse.NotifyResponse notifyResponse = NewWebUtil.convertJson(response, PaymentResponse.NotifyResponse.class);
            log.info("notify->{}", notifyResponse.toString());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public PaymentResponse.QueryTradeInfoResponse queryTradeInfo(String merchantOrderNo, Integer amt) {
        try {
            return new PaymentRequest.QueryTradeInfo(mid, key, iv, merchantOrderNo, amt).request(queryTradeInfoUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public PaymentResponse.CloseTradeResponse closeTrade(String merchantOrderNo, Integer amt) {
        try {
            return new PaymentRequest.CloseTrade(mid, key, iv, merchantOrderNo, amt).request(closeUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getRandomString(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }
}
