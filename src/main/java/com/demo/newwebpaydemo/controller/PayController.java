package com.demo.newwebpaydemo.controller;

import com.demo.newwebpaydemo.newWebPay.bean.PayResponse;
import com.demo.newwebpaydemo.newWebPay.bean.PaymentResponse;
import com.demo.newwebpaydemo.service.PayService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PayController {

    private final PayService payService;

    /**
     * 送出訂單
     * POST http://localhost:8080/api/pay
     */
    @PostMapping("/pay")
    public ResponseEntity<PayResponse> pay() {
        PayResponse response = payService.pay();
        return ResponseEntity.ok(response);
    }

    /**
     * 單一訂單查詢
     * POST http://localhost:8080/api/query_info?
     *     merchantOrderNo=[merchantOrderNo]&
     *     amt=[amt]&
     *     Content-Type: application/x-www-form-urlencoded
     */
    @PostMapping("/query_info")
    public ResponseEntity<PaymentResponse.QueryTradeInfoResponse> queryTradeInfo(@RequestParam String merchantOrderNo, @RequestParam Integer amt) {
        PaymentResponse.QueryTradeInfoResponse response = payService.queryTradeInfo(merchantOrderNo, amt);
        return ResponseEntity.ok(response);
    }

    /**
     * 請退款
     * POST http://localhost:8080/api/close_trade?
     *     merchantOrderNo=[merchantOrderNo]&
     *     amt=[amt]&
     *     Content-Type: application/x-www-form-urlencoded
     */
    @PostMapping("/close_trade")
    public ResponseEntity<PaymentResponse.CloseTradeResponse> closeTrade(@RequestParam String merchantOrderNo, @RequestParam Integer amt) {
        PaymentResponse.CloseTradeResponse response = payService.closeTrade(merchantOrderNo, amt);
        return ResponseEntity.ok(response);
    }

    /**
     *  接收藍新金流回傳資料
     */
    @PostMapping("/notify")
    public ResponseEntity<String> notify(@RequestParam(name = "Status", required = false) String status,
                                         @RequestParam(name = "MerchantID", required = false) String merchantId,
                                         @RequestParam(name = "TradeInfo", required = false) String tradeInfo,
                                         @RequestParam(name = "TradeSha", required = false) String tradeSha) {
        log.info("notify status->{}, message->{}, result->{}, tradeSha->{}", status, merchantId, tradeInfo, tradeSha);
        payService.notify(tradeInfo);
        return ResponseEntity.ok("OK");
    }
}
