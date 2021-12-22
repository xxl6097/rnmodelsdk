package com.android.api.util;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;


import com.android.api.BuildConfig;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * -----------------------------------------------------------------
 * Copyright (C) 2014-2016, by het, Shenzhen, All rights reserved.
 * -----------------------------------------------------------------
 * <p>
 * <p>描述：Log工具，类似android.util.Log。 tag自动产生，格式:
 * TAG:className.methodName(Line:lineNumber),
 * customTagPrefix为空时只输出：className.methodName(Line:lineNumber)。</p>
 * 名称: Log工具 <br>
 * 作者: uuixa<br>
 * 版本: 1.0<br>
 * 日期: 2016/9/30 11:40<br>
 **/
public class Logc {
    public static final String LINE_BREAK = "\r\n";

    public static boolean isWriteAll = false;
    public static boolean isWriteInfo = false;
    public static boolean isWriteDebug = false;
    public static boolean isWriteError = false;
    public static boolean isWriteVerbose = false;
    public static boolean isWriteWarm = false;
    public static boolean isAndroid = true;
    public static String ROOT;// = Environment.getExternalStorageDirectory().getPath() + "/HeT/"; // SD卡中的根目录
    public static String TAG = "uuxia"; // 自定义Tag的前缀，可以是作者名

    public static boolean DEBUG = true/* && BuildConfig.DEBUG*/;
    // 容许打印日志的类型，默认是true，设置为false则不打印
    public static boolean allowD = DEBUG && true;
    public static boolean allowE = DEBUG && true;
    public static boolean allowI = DEBUG && true;
    public static boolean allowV = DEBUG && true;
    public static boolean allowW = DEBUG && true;
    //    private static final boolean isSaveLog = true; // 是否把保存日志到SD卡中
    private static String PATH_LOG_INFO;// = ROOT + "log/";

    public static void write(boolean on) {
        isWriteAll = on;
    }


    static {
//        android.util.Log
        String os = System.getProperty("os.name");
        boolean isdebug = BuildConfig.DEBUG;
        System.out.println("uu_clife current os System is " + os + ",isdebug:"+isdebug);
        if (os.toLowerCase().contains("win") || os.toLowerCase().contains("mac")) {
            isAndroid = false;
        } else {
            ROOT = Environment.getExternalStorageDirectory().getPath() + "/hetgateway/"; // SD卡中的根目录
//            File futureStudioIconFile = new File(context.getExternalFilesDir(fileDir) + File.separator + fileName);
            PATH_LOG_INFO = ROOT + "log/";
            isAndroid = true;
        }
    }

    public static void init(Context context) {
        PATH_LOG_INFO = Environment.getExternalStorageDirectory().getPath() + File.separator
                + context.getPackageName() + File.separator + "log/"; // 公共日志目录
    }


    private static void loge(String tag, String content, Throwable tr) {
        if (isAndroid) {
            if (content != null) {
                Log.e(tag, content, tr);
                if (isWriteAll||isWriteError) {
                    point(PATH_LOG_INFO, tag, content);
                }
            }
        } else {
            System.err.println(tag + "-" + content);
        }
    }

    private static void logd(String tag, String content) {
        if (isAndroid) {
            if (content != null) {
                Log.d(tag, content);
                if (isWriteAll||isWriteDebug) {
                    point(PATH_LOG_INFO, tag, content);
                }
            }
        } else {
            System.out.println(tag + "-" + content);
        }
    }

    private static void logw(String tag, String content) {
        if (isAndroid) {
            if (content != null) {
                Log.w(tag, content);
                if (isWriteAll||isWriteWarm) {
                    point(PATH_LOG_INFO, tag, content);
                }
            }
        } else {
            System.out.println(tag + "-" + content);
        }
    }

    private static void logi(String tag, String content) {
        if (isAndroid) {
            if (content != null) {
                Log.i(tag, content);
                if (isWriteAll||isWriteInfo) {
                    point(PATH_LOG_INFO, tag, content);
                }
            }
        } else {
            System.out.println(tag + "-" + content);
        }
    }

    private static void logv(String tag, String content) {
        if (isAndroid) {
            if (content != null) {
                Log.v(tag, content);
                if (isWriteAll||isWriteVerbose) {
                    point(PATH_LOG_INFO, tag, content);
                }
            }
        } else {
            System.out.println(tag + "-" + content);
        }
    }

    public static void v(String content) {
        if (isAndroid) {
            if (!allowV)
                return;
            StackTraceElement caller = getCallerStackTraceElement();
            String tag = generateTag(caller, null);

            logv(tag, content);
        } else {
            v(null, content, false);
        }
    }

    public static void v(String content, boolean isSaveLog) {
        if (isAndroid) {
            if (!allowV)
                return;
            StackTraceElement caller = getCallerStackTraceElement();
            String tag = generateTag(caller, null);

            logv(tag, content);
            if (isSaveLog && isAndroid) {
                point(PATH_LOG_INFO, tag, content);
            }
        } else {
            v(null, content, isSaveLog);
        }
    }

    public static void v(String uTag, String content) {
        if (isAndroid) {
            if (!allowV) {
                return;
            }
            StackTraceElement caller = getCallerStackTraceElement();
            String tag = generateTag(caller, uTag);

            logv(tag, content);
        } else {
            v(uTag, content, false);
        }
    }

    public static void v(String uTag, String content, boolean isSaveLog) {
        if (!allowV) {
            return;
        }
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller, uTag);

        logv(tag, content);

        if (isSaveLog && isAndroid || isWriteAll) {
            point(PATH_LOG_INFO, tag, content);
        }
    }

    public static void v(String content, Throwable tr) {
        if (!allowE)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller, null);
        Log.v(tag, content, tr);
        if (isWriteAll||isWriteVerbose) {
            point(PATH_LOG_INFO, tag, getThrowable(tr, content));
        }
    }

    public static void d(String content) {
        if (isAndroid) {
            if (!allowD)
                return;
            StackTraceElement caller = getCallerStackTraceElement();
            String tag = generateTag(caller, null);

            logd(tag, content);
        } else {
            d(null, content, false);
        }
    }

    public static void d(String content, boolean isSaveLog) {
        if (isAndroid) {
            if (!allowD)
                return;
            StackTraceElement caller = getCallerStackTraceElement();
            String tag = generateTag(caller, null);

            logd(tag, content);
            if (isSaveLog && isAndroid) {
                point(PATH_LOG_INFO, tag, content);
            }
        } else {
            d(null, content, isSaveLog);
        }
    }

    public static void d(String uTag, String content) {
        if (isAndroid) {
            if (!allowD) {
                return;
            }
            StackTraceElement caller = getCallerStackTraceElement();
            String tag = generateTag(caller, uTag);

            logd(tag, content);
        } else {
            d(uTag, content, false);
        }
    }

    public static void d(String uTag, String content, boolean isSaveLog) {
        if (!allowD) {
            return;
        }
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller, uTag);

        logd(tag, content);
        if (isSaveLog && isAndroid || isWriteAll) {
            point(PATH_LOG_INFO, tag, content);
        }
    }

    public static void d(String content, Throwable tr) {
        if (!allowE)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller, null);
        Log.d(tag, content, tr);
        if (isWriteAll||isWriteDebug) {
            point(PATH_LOG_INFO, tag, getThrowable(tr, content));
        }
    }

    public static void i(String content) {
        if (isAndroid) {
            if (!allowI)
                return;
            StackTraceElement caller = getCallerStackTraceElement();
            String tag = generateTag(caller, null);
            logi(tag, content);
        } else {
            i(null, content, false);
        }
    }

    public static void i(String content, Throwable tr) {
        if (!allowE)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller, null);
        Log.i(tag, content, tr);
        if (isWriteAll||isWriteInfo) {
            point(PATH_LOG_INFO, tag, getThrowable(tr, content));
        }
    }

    public static void i(String content, boolean isSaveLog) {
        i(null, content, isSaveLog);
    }

    public static void i(String uTag, String content) {
        if (isAndroid) {
            if (!allowI) {
                return;
            }
            StackTraceElement caller = getCallerStackTraceElement();
            String tag = generateTag(caller, uTag);

            logi(tag, content);
        } else {
            i(uTag, content, false);
        }
    }

    public static void i(String uTag, String content, boolean isSaveLog) {
        if (!allowI) {
            return;
        }
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller, uTag);

        logi(tag, content);
        if (isSaveLog && isAndroid || isWriteAll) {
            point(PATH_LOG_INFO, tag, content);
        }
    }

    public static void w(String content) {
        if (isAndroid) {
            if (!allowW)
                return;
            StackTraceElement caller = getCallerStackTraceElement();
            String tag = generateTag(caller, null);

            logw(tag, content);
        } else {
            w(null, content, false);
        }
    }

    public static void w(String content, boolean isSaveLog) {
        if (isAndroid) {
            if (!allowW)
                return;
            StackTraceElement caller = getCallerStackTraceElement();
            String tag = generateTag(caller, null);

            logw(tag, content);
            if (isSaveLog && isAndroid) {
                point(PATH_LOG_INFO, tag, content);
            }
        } else {
            w(null, content, isSaveLog);
        }
    }

    public static void w(String uTag, String content) {
        if (isAndroid) {
            if (!allowW) {
                return;
            }
            StackTraceElement caller = getCallerStackTraceElement();
            String tag = generateTag(caller, uTag);

            logw(tag, content);
        } else {
            w(null, content, false);
        }
    }

    public static void w(String uTag, String content, boolean isSaveLog) {
        if (!allowW) {
            return;
        }
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller, uTag);

        logw(tag, content);

        if (isSaveLog && isAndroid || isWriteAll) {
            point(PATH_LOG_INFO, tag, content);
        }
    }

    public static void w(String content, Throwable tr) {
        if (!allowE)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller, null);
        Log.w(tag, content, tr);
        if (isWriteAll||isWriteWarm) {
            point(PATH_LOG_INFO, tag, getThrowable(tr, content));
        }
    }

    public static void e(String content) {
        if (isAndroid) {
            if (!allowE)
                return;
            StackTraceElement caller = getCallerStackTraceElement();
            String tag = generateTag(caller, null);
            loge(tag, content, null);
        } else {
            w(null, content, false);
        }
    }

    public static void e(String content, boolean isSaveLog) {
        if (isAndroid) {
            if (!allowE)
                return;
            StackTraceElement caller = getCallerStackTraceElement();
            String tag = generateTag(caller, null);

            loge(tag, content, null);
            if (isSaveLog && isAndroid) {
                point(PATH_LOG_INFO, tag, content);
            }
        } else {
            w(null, content, isSaveLog);
        }
    }

    public static void e(String uTag, String content) {
        if (isAndroid) {
            if (!allowE) {
                return;
            }
            StackTraceElement caller = getCallerStackTraceElement();
            String tag = generateTag(caller, uTag);
            loge(tag, content, null);
        } else {
            w(uTag, content, false);
        }
    }

    public static void e(String uTag, String content, boolean isSaveLog) {
        if (!allowE) {
            return;
        }
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller, uTag);

        loge(tag, content, null);
        if (isSaveLog && isAndroid || isWriteAll) {
            point(PATH_LOG_INFO, tag, content);
        }
    }

    public static void e(String content, Throwable tr) {
        if (!allowE)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller, null);
        Log.e(tag, content, tr);
        if (isWriteAll||isWriteError) {
            point(PATH_LOG_INFO, tag, getThrowable(tr, content));
        }
    }

    public static void e(String uTag, String content, Throwable tr) {
        if (!allowE)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller, uTag);
        Log.e(tag, content, tr);
    }

    public static void e(String content, Throwable tr, boolean isSaveLog) {
        if (!allowE)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller, null);
        Log.e(tag, content, tr);
        if (isSaveLog && isAndroid || isWriteAll) {
            point(PATH_LOG_INFO, tag, getThrowable(tr, content));
        }
    }

    public static void e(Throwable tr, boolean isSaveLog) {
        if (!allowE)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller, null);

        String content = getThrowable(tr, null);
        loge(tag, content, null);
        if (isSaveLog && isAndroid || isWriteAll) {
            point(PATH_LOG_INFO, tag, content);
        }
    }

    public static void e(Throwable tr) {
        if (!allowE)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller, null);

        String content = getThrowable(tr, null);
        loge(tag, content, null);
    }

    public static void e(String uTag, String content, Throwable tr, boolean isSaveLog) {
        if (!allowE)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller, uTag);
        loge(tag, content, tr);

        String msg = getThrowable(tr, content);
        if (isSaveLog && isAndroid || isWriteAll) {
            point(PATH_LOG_INFO, tag, msg);
        }
    }


    private static StackTraceElement getCallerStackTraceElement() {
        return Thread.currentThread().getStackTrace()[4];
    }

    public static void point(String path, String tag, String msg) {
        if (isSDAva()) {
            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("",
                    Locale.SIMPLIFIED_CHINESE);
            dateFormat.applyPattern("yyyy");
            path = path + dateFormat.format(date) + "/";
            dateFormat.applyPattern("MM");
            path += dateFormat.format(date) + "/";
            dateFormat.applyPattern("dd");
            path += dateFormat.format(date) + ".log";
            dateFormat.applyPattern("[yyyy-MM-dd HH:mm:ss]");
            String time = dateFormat.format(date);


//            path += "aaa.log";
            File file = new File(path);
            if (!file.exists())
                createDipPath(path);
            BufferedWriter out = null;
            try {
                out = new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream(file, true)));
                out.write(time + " " + tag + " " + msg + "\r\n");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 根据文件路径 递归创建文件
     *
     * @param file
     */
    public static void createDipPath(String file) {
        String parentFile = file.substring(0, file.lastIndexOf("/"));
        File file1 = new File(file);
        File parent = new File(parentFile);
        if (!file1.exists()) {
            parent.mkdirs();
            try {
                file1.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static boolean isSDAva() {
        if (isAndroid && Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)
                || Environment.getExternalStorageDirectory().exists()) {
            return true;
        } else {
            return false;
        }
    }

    private static String generateTag(StackTraceElement caller, String uTag) {
        String tag = "(%s:%d).%s"; // 占位符
        String callerClazzName = caller.getFileName();
        tag = String.format(tag, callerClazzName, caller.getLineNumber(), caller.getMethodName()); // 替换
        String str = tag;
        if (uTag == null || uTag.equals("")) {
            str = TAG + ":" + tag;
        } else {
            str = TAG + "." + uTag + ":" + tag;
        }
        return str;
    }

    private static String getThrowable(Throwable throwable, String mag) {
        /* 打印异常 */
        StringBuffer sb = new StringBuffer();
        if (!TextUtils.isEmpty(mag)) {
            sb.append(mag);
        }
        if (throwable != null) {
            sb.append(LINE_BREAK);
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            throwable.printStackTrace(printWriter);
            sb.append(stringWriter.toString());
        }
        return sb.toString();
    }


}


