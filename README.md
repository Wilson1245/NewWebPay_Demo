# 藍新金流 整合

## 需要修改

application.yml
```yml
new-web-pay:
  key: 'your-key'
  iv: 'your-iv'
  mid: 'your-mid'
  #  交易api
  pay-url: https://ccore.newebpay.com/MPG/mpg_gatewa
  #  單筆交易查詢
  query-trade-info-url: https://ccore.newebpay.com/API/QueryTradeInfo
  #  退款交易
  close-url: https://ccore.newebpay.com/API/CreditCard/Close
  #  回傳通知url
  notify-url: 'your-notify-url'
```

## 使用
直接引用 **newWebPay** package

## 測試
在目錄中有 **PayIndex.html**
1. /api/pay 拿到加密資料
2. 使用 **PayIndex.html** 送出資料跳到藍新

## 請退款
在 **PayRequest.CloseRequest** 中可以修改參數，目前是退款。

但應該是測試的關係，所以退款會出現店家信用卡停用的訊息。
