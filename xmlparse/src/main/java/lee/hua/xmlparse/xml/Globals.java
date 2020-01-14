package lee.hua.xmlparse.xml;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import dalvik.system.DexFile;
import lee.hua.xmlparse.annotation.XmlBean;

/**
 * Created by lijie on 2017/6/14.
 */
public class Globals {
    public static final String TAG = "xml-parse";

    private Globals() {
    }

    /**
     * XML标签类路径集合，key是XML标签名，value是XMLBean的class路径
     */
    private static Map<String, String> xmlNameClassPathMap = new HashMap<>();

    /**
     * 根据标签名获取对应的Class Name
     *
     * @param xmlBeanName 标签名
     * @return class name
     */
    public static String getBeanClass(String xmlBeanName) {
        if (xmlNameClassPathMap.containsKey(xmlBeanName)) {
            return xmlNameClassPathMap.get(xmlBeanName);
        }
        return "";
    }

    /**
     * @param packageName scan package name
     * @throws IOException            io exception
     * @throws ClassNotFoundException class not found exception
     * @deprecated 此方法在 Android 设备上稳定性差，
     * 请使用{@link lee.hua.xmlparse.xml.Globals} classParse(Context context, String packageName)
     */
    public static void classParse(String packageName) throws IOException, ClassNotFoundException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        //获取指定包下的包路径
        Enumeration<URL> urls = classLoader.getResources(packageName.replace(".", "/"));
        while (urls.hasMoreElements()) {
            URL url = urls.nextElement();
            System.out.println(url.getPath());
            File file = new File(url.getPath());
            searchClass(file, packageName, xmlNameClassPathMap);
        }

        System.out.println(xmlNameClassPathMap);
    }

    public static void classParse(Context context, String packageName) {
        //增加针对 Android 的 PackageCodePath 检索
        try {
            String packageCodePath = context.getPackageCodePath();
            DexFile df = new DexFile(packageCodePath);//通过DexFile查找当前的APK中可执行文件
            Enumeration<String> enumeration = df.entries();//获取df中的元素  这里包含了所有可执行的类名 该类名包含了包名+类名的方式
            while (enumeration.hasMoreElements()) {//遍历
                String className = enumeration.nextElement();
                if (className.contains(packageName)) {
                    String[] names = className.split("\\.");
                    String key = names[names.length - 1];
                    Log.d(TAG, "class :: " + className);
                    Log.d(TAG, "class Name :: " + key);
                    xmlNameClassPathMap.put(key, className);
                }
            }
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 递归搜索指定路径文件下的类文件
     *
     * @param file         指定文件
     * @param packageName  当前包名
     * @param nameClassMap XML标签名 与 类路径的map集合
     * @throws ClassNotFoundException
     */
    private static void searchClass(File file, String packageName, Map<String, String> nameClassMap) throws ClassNotFoundException {
        if (file.isDirectory()) {
            //是目录，遍历子文件
            File[] files = file.listFiles();
            StringBuilder sb;
            for (File f : files) {
                if (f.isDirectory()) {
                    //如果存在目录，变更类路径
                    sb = new StringBuilder(packageName).append(".").append(f.getName());
                } else {
                    //不是目录，使用原类路径
                    sb = new StringBuilder(packageName);
                }
                //再次进行搜索
                searchClass(f, sb.toString(), nameClassMap);
            }
        } else {
            //不是目录，获取文件名
            String fname = file.getName();
            StringBuilder sb;
            if (fname.endsWith(".class")) {
                //如果是class文件，去掉尾巴
                fname = fname.replace(".class", "");
                //组装包路径
                sb = new StringBuilder(packageName).append(".").append(fname);
                System.out.println("扫描类信息-" + sb.toString());
                Class<?> clazz = Class.forName(sb.toString());
                XmlBean xmlBean = clazz.getAnnotation(XmlBean.class);
                //通过反射获取XmlBean注解，如果存在，加入map中
                if (xmlBean != null) {
                    if (!"".equals(xmlBean.name().trim())) {
                        nameClassMap.put(xmlBean.name(), sb.toString());
                    } else {
                        nameClassMap.put(fname, sb.toString());
                    }
                }
            }
        }

    }

    public static void setClassPathMap(@NonNull String keyName,@NonNull String classPath){
        xmlNameClassPathMap.put(keyName,classPath);
    }
}
