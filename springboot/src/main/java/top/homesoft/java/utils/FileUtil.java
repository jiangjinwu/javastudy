package top.homesoft.java.utils;
//
//import info.monitorenter.cpdetector.io.*;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;


public class FileUtil {
    public static String resolveCode(String filePath) throws Exception {

//        CodepageDetectorProxy detector = CodepageDetectorProxy.getInstance();
        /*ParsingDetector可用于检查HTML、XML等文件或字符流的编码,
         * 构造方法中的参数用于指示是否显示探测过程的详细信息，为false不显示。
         */
//        detector.add(new ParsingDetector(false));
        /*JChardetFacade封装了由Mozilla组织提供的JChardet，它可以完成大多数文件的编码测定。
         * 所以，一般有了这个探测器就可满足大多数项目的要求，如果你还不放心，可以再多加几个探测器，
         * 比如下面的ASCIIDetector、UnicodeDetector等。
         */
//        detector.add(JChardetFacade.getInstance());
//        detector.add(ASCIIDetector.getInstance());
//        detector.add(UnicodeDetector.getInstance());
        Charset charset = null;
        File file = new File(filePath);
        try {
            //charset = detector.detectCodepage(file.toURI().toURL());
            InputStream is = new BufferedInputStream(new FileInputStream(filePath));
//            charset = detector.detectCodepage(is, 8);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        String charsetName = "GBK";
        if (charset != null) {
            if (charset.name().equals("US-ASCII")) {
                charsetName = "ISO_8859_1";
            } else if (charset.name().startsWith("UTF")) {
                charsetName = charset.name();// 例如:UTF-8,UTF-16BE.
            }
        }
        return charsetName;
    }
}
