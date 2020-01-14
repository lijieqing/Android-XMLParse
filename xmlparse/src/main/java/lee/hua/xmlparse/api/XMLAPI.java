package lee.hua.xmlparse.api;

import android.content.Context;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import lee.hua.xmlparse.xml.Globals;
import lee.hua.xmlparse.xml.XMLAttribute;
import lee.hua.xmlparse.xml.XMLBase;
import lee.hua.xmlparse.xml.XMLHasKids;
import lee.hua.xmlparse.xml.XmlGenerate;
import lee.hua.xmlparse.xml.XmlReader;

/**
 * Created by lijie on 2017/6/7.
 */
public class XMLAPI {
    private static Document document = null;

    //返回 xmlbean 包下的根目录

    /**
     * 将XML文件读取为XML标签对象
     *
     * @param inputStream the input stream of xml file
     * @return the object
     */
    public static Object readXML(InputStream inputStream) {
        Element rootElement = null;
        SAXReader reader = new SAXReader();
        try {
            document = reader.read(inputStream);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        //先 将root 节点解析出来
        if (document != null) rootElement = document.getRootElement();
        XMLBase root = new XMLHasKids(rootElement.getName());
        List<Attribute> attributes = rootElement.attributes();
        List<XMLAttribute> xmlAttributes = new ArrayList<>();
        for (Attribute attribute : attributes) {
            xmlAttributes.add(new XMLAttribute(attribute.getName(), attribute.getValue()));
        }
        root.setXMLAttributes(xmlAttributes);

        //利用递归 将子节点逐一解析 放入xmlbase 中
        List<Element> childelement = rootElement.elements();
        for (Element element : childelement) {
            XmlReader.XMLparse(element, root);
        }

        return root.transform();
    }

    /**
     * 将XML标签对象输出为XML文件
     *
     * @param object
     * @param path
     * @throws IOException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static void writeObj2Xml(Object object, String path) throws IOException, IllegalAccessException, InvocationTargetException {
        XmlGenerate.generate(object, path);
    }

    /**
     * 更改XML标签类的包路径
     *
     * @param packageName 包路径
     * @deprecated 此方法已过时，建议使用 setXmlBeanScanPackage方法
     */
    public static void changPackageName(String packageName) throws ClassNotFoundException, IOException {
        Globals.classParse(packageName);
    }

    /**
     * 设置XmlBean对象扫描包
     *
     * @param packageName 包路径
     * @throws IOException io
     * @throws ClassNotFoundException class not found
     * @deprecated 此方法已过时，建议使用 setXmlBeanScanPackage(Context context, String packageName)方法
     */
    public static void setXmlBeanScanPackage(String packageName) throws IOException, ClassNotFoundException {
        Globals.classParse(packageName);
    }

    /**
     * 设置XmlBean对象扫描包
     *
     * @param scanPackage 包路径
     */
    public static void setXmlBeanScanPackage(Context context, String scanPackage) {
        Globals.classParse(context, scanPackage);
    }
}
