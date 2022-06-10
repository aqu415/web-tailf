package com.xx.log.util;

public class MixUtil {
    //获取文本编码
    private static final String FILE_ENCODE_TYPE = "file";
    //获取文件流编码
    private static final String IO_ENCODE_TYPE = "io";

    /**
     * 获取探测到的文件对象
     * <p>
     * 要判断文件编码格式的源文件的路径
     */
//    private static CodepageDetectorProxy getDetector() {
//        /*
//         * detector是探测器，它把探测任务交给具体的探测实现类的实例完成。
//         * cpDetector内置了一些常用的探测实现类，这些探测实现类的实例可以通过add方法 加进来，如ParsingDetector、
//         * JChardetFacade、ASCIIDetector、UnicodeDetector。
//         * detector按照“谁最先返回非空的探测结果，就以该结果为准”的原则返回探测到的
//         * 字符集编码。使用需要用到三个第三方JAR包：antlr.jar、chardet.jar和cpdetector.jar
//         * cpDetector是基于统计学原理的，不保证完全正确。
//         */
//        CodepageDetectorProxy detector = CodepageDetectorProxy.getInstance();
//
//        /*
//         * ParsingDetector可用于检查HTML、XML等文件或字符流的编码,构造方法中的参数用于
//         * 指示是否显示探测过程的详细信息，为false不显示。
//         */
//        detector.add(new ParsingDetector(false));
//        /*
//         * JChardetFacade封装了由Mozilla组织提供的JChardet，它可以完成大多数文件的编码
//         * 测定。所以，一般有了这个探测器就可满足大多数项目的要求，如果你还不放心，可以
//         * 再多加几个探测器，比如下面的ASCIIDetector、UnicodeDetector等。
//         */
//        detector.add(JChardetFacade.getInstance());// 用到antlr.jar、chardet.jar
//        // ASCIIDetector用于ASCII编码测定
//        detector.add(ASCIIDetector.getInstance());
//        // UnicodeDetector用于Unicode家族编码的测定
//        detector.add(UnicodeDetector.getInstance());
//
//        return detector;
//    }
//
//    /**
//     * 根据"encodeType"获取文本编码或文件流编码
//     */
//    public static String getFileOrIOEncode(String path, String encodeType) {
//        CodepageDetectorProxy detector = getDetector();
//        File file = new File(path);
//        Charset charset = null;
//        try {
//            switch (encodeType) {
//                case FILE_ENCODE_TYPE:
//                    charset = detector.detectCodepage(file.toURI().toURL());
//                    break;
//                case IO_ENCODE_TYPE:
//                    charset = detector.detectCodepage(new BufferedInputStream(new FileInputStream(file)), 128);//128表示读取128字节来判断文件流的编码,读得越多越精确,但是速度慢
//                    break;
//                default:
//                    charset = Charset.defaultCharset();
//                    break;
//            }
//
//        } catch (IOException e) {
//            //这里获取编码失败,使用系统默认的编码
//            charset = Charset.defaultCharset();
//            System.out.println(e.getMessage());
//        }
//        return charset.name();
//    }
}
