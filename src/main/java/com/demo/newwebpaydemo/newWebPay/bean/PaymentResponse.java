package com.demo.newwebpaydemo.newWebPay.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
public class PaymentResponse {
    @JsonProperty("Status")
    private String status;
    @JsonProperty("Message")
    private String message;

    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class NotifyResponse extends PaymentResponse {
        @JsonProperty("Result")
        private Result result;

        @Data
        public static class Result {
            @JsonProperty("MerchantID")
            private String merchantID;

            @JsonProperty("Amt")
            private int amt;

            @JsonProperty("TradeNo")
            private String tradeNo;

            @JsonProperty("MerchantOrderNo")
            private String merchantOrderNo;

            @JsonProperty("RespondType")
            private String respondType;

            @JsonProperty("IP")
            private String ip;

            @JsonProperty("EscrowBank")
            private String escrowBank;

            @JsonProperty("PaymentType")
            private String paymentType;

            @JsonProperty("PayTime")
            private String payTime;

            @JsonProperty("PayerAccount5Code")
            private String payerAccount5Code;

            @JsonProperty("PayBankCode")
            private String payBankCode;
        }

        public String toString() {
            return "Status="+super.getStatus()+", Message="+super.getMessage()
                    +", MerchantID="+ getResult().merchantID+", Amt="+getResult().amt
                    +", TradeNo="+getResult().tradeNo+", MerchantOrderNo="+getResult().merchantOrderNo
                    +", RespondType="+getResult().respondType+", IP="+getResult().ip
                    +", EscrowBank="+getResult().escrowBank+", PaymentType="+getResult().paymentType
                    +", PayTime="+getResult().payTime+", PayerAccount5Code="+getResult().payerAccount5Code
                    +", PayBankCode="+getResult().payBankCode;
        }
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class CloseTradeResponse extends PaymentResponse {
        @JsonProperty("Result")
        private Result result;

        @Data
        public static class Result {
            @JsonProperty("MerchantID")
            private String merchantID;
            @JsonProperty("Amt")
            private int amt;
            @JsonProperty("TradeNo")
            private String tradeNo;
            @JsonProperty("MerchantOrderNo")
            private String merchantOrderNo;
        }
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class QueryTradeInfoResponse extends PaymentResponse {
        @JsonProperty("Result")
        private Result result;

        @Data
        // 嵌套的 Result 類
        public static class Result {
            @JsonProperty("MerchantID")
            private String merchantID;
            @JsonProperty("Amt")
            private int amt;
            @JsonProperty("TradeNo")
            private String tradeNo;
            @JsonProperty("MerchantOrderNo")
            private String merchantOrderNo;
            /**
             * 0=未付款
             * 1=付款成功
             * 2=付款失敗
             * 3=取消付款
             * 6=退款
             */
            @JsonProperty("TradeStatus")
            private String tradeStatus;
            /**
             * CREDIT=信用卡付款
             * VACC=銀行 ATM 轉帳付款
             * WEBATM=網路銀行轉帳付款
             * BARCODE=超商條碼繳費
             * CVS=超商代碼繳費
             * LINEPAY=LINE Pay 付款
             * ESUNWALLET=玉山 Wallet
             * TAIWANPAY=台灣 Pay
             * CVSCOM = 超商取貨付款
             * FULA=Fula 付啦
             */
            @JsonProperty("PaymentType")
            private String paymentType;
            @JsonProperty("CreateTime")
            private String createTime;
            @JsonProperty("PayTime")
            private String payTime;
            @JsonProperty("CheckCode")
            private String checkCode;
            /**
             * 預計撥款的時間
             * 回傳格式為：2014-06-25
             */
            @JsonProperty("FundTime")
            private String fundTime;
            /**
             * 付款資訊
             */
            @JsonProperty("PayInfo")
            private String payInfo;
            /**
             * 繳費有效期限
             * 格式為 Y-m-d H:i:s 例：2014-06-29 23:59:59
             */
            @JsonProperty("ExpireDate")
            private String expireDate;
            /**
             * 交易狀態
             * 0＝未付款
             * 1＝已付款
             * 2＝訂單失敗
             * 3＝訂單取消
             * 6＝已退款
             * 9＝付款中，待銀行確認
             */
            @JsonProperty("OrderStatus")
            private int orderStatus;
        }
    }
}
