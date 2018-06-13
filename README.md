# AndroidScanText
基于[百度OCR](https://ai.baidu.com/)技术的文本扫描工具。

运行时需要将InitApplication中的 百度API Key 和 对应的Secret Key进行替换。具体查看[百度AI开发者平台文档](https://cloud.baidu.com/doc/OCR/OCR-Android-SDK.html#DEMO.E4.BD.BF.E7.94.A8.E8.AF.B4.E6.98.8E)。

    private void initOCRAccessToken() {
        OCR.getInstance(this).initAccessTokenWithAkSk(new OnResultListener<AccessToken>() {
            @Override
            public void onResult(AccessToken result) {
                String token = result.getAccessToken();
                GlobalConfig.hasGotOCRToken = true;
            }

            @Override
            public void onError(OCRError error) {
                error.printStackTrace();
            }
        }, getApplicationContext(),  "百度API KEY", "对应的Secret Key");
    }
