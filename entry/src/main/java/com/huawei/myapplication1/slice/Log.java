package com.huawei.myapplication1.slice;

import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
/**
 * @Author: DY
 * @Date: 2020/12/31
 */
//这个类没有实际用处，为了解决GLSurfaceProvider类中的log报错问题。
public class Log {
    public static void w(String TAG, String content) {
        HiLogLabel LABEL_LOG = new HiLogLabel(3, 0xD001100, TAG);
        HiLog.info(LABEL_LOG,content);
    }
    public static void e(String TAG, String content) {
        HiLogLabel LABEL_LOG = new HiLogLabel(3, 0xD001100, TAG);
        HiLog.info(LABEL_LOG,content);
    }
    public static void i(String TAG, String content) {
        HiLogLabel LABEL_LOG = new HiLogLabel(3, 0xD001100, TAG);
        HiLog.info(LABEL_LOG,content);
    }
    public static void v(String TAG, String content) {
        HiLogLabel LABEL_LOG = new HiLogLabel(3, 0xD001100, TAG);
        HiLog.info(LABEL_LOG,content);
    }
}
