# HealthImprover

1. 此專案為 NTUT 電機系之專題

2. 研究動機：
	製作此專題的主旨為設計一套系統，能夠讓每個人擁有自己的健康管理員。在目前 App 普遍發展之下，每個人都能夠藉由 Google Play 商店來下載各式各樣的健康管理 App，但是大多數的健康管理 App 都是固定的，不會因為個人體質而有所改變而給予適當的預測，因此我們將設計一個能夠因個人體質而給予適當預測的健康管理 App。

3. 主要服務內容：
	一台 Weight Manage Server 處理所有雲端運算的動作，每一位使用者能夠利用 Google Server 提供的認證服務來註冊會員，並使用 Weight Manage Server 提供的資料庫服務建立線上資料庫，記錄自己每一天的身高、體重、熱量和體重目標等資料，然後藉由演算法來尋找個人生理參數，並用這些參數預測隔天的體重，或是算出以目前飲食與運動狀況，需要多少天能達到目標，甚至還能給出每日建議的攝取熱量。
    另外，本 App 還有記事、鬧鐘和拍照功能，讓使用者在記錄身高體重時，若有事情需要記錄能夠利用記事本記下來，又或者在飯後登記熱量時，設定鬧鐘提醒使用者幾點需要運動。本 App 還提供了提醒服務，在使用者忘記登記熱量 或是忘記吃飯時，會由 Weight Manage Server 提醒使用者進行登記，若是忘記吃飯也能達到提醒使用者的動作，長期使用甚至能改善使用者的生活規律。

4. 由於演算法部分無法公開，故此份檔案只有 App 部分。

5. App 圖標
![alt tag](https://github.com/AsaEwing/HealthImprover/blob/master/app/src/main/res/mipmap-xxxhdpi/ic_launcher.png)

6. 目前正開發第二版，主要方向為：
	一為，向下支援至4.4。
	二為，將程式碼簡潔化。
