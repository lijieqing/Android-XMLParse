package lee.hua.xmlparse;

import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import lee.hua.xmlparse.xmlbean.Author;
import lee.hua.xmlparse.xmlbean.Publisher;
import lee.hua.xmlparse.xmlbean.User;
import lee.hua.xmlparse.api.XMLAPI;
import lee.hua.xmlparse.xmlbean.Book;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }
    @Test
    public void writeUser() throws IllegalAccessException, IOException, InvocationTargetException {
        User user = new User();
        user.setName("huaLPppppp");
        user.setAge(13);
        List<Book> b1 = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Book book = new Book();
            book.setName("bookFF" + i);
            book.setPage(87 + i * 3);
            book.setPrice((float) (37.5 + i));
            book.setPublishier(new Publisher("中国出版社"));
            b1.add(book);
        }
        user.setBooks(b1);
        Book book = new Book();
        book.setName("JPM");
        book.setPage(69);
        book.setPrice(69.96f);
        user.setVip(book);

        user.setAuthor(new Author("llxxs"));

        XMLAPI.writeObj2Xml(user, "/Users/lijie/Desktop/user.xml");
    }

    @Test
    public void readUser() throws IOException, ClassNotFoundException {
        XMLAPI.setXmlBeanScanPackage("lee.hua.xmlparse.xmlbean");
        User object = (User) XMLAPI.readXML(new FileInputStream("/Users/lijie/Desktop/user.xml"));
        System.out.println(object);
    }
}