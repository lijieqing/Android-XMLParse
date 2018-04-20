package lee.hua.xmlparse.xmlbean;

import java.util.ArrayList;
import java.util.List;

import lee.hua.xmlparse.annotation.Ignore;
import lee.hua.xmlparse.annotation.XmlAttribute;
import lee.hua.xmlparse.annotation.XmlBean;
import lee.hua.xmlparse.annotation.XmlListNode;
import lee.hua.xmlparse.annotation.XmlSingleNode;

/**
 * Created by lijie on 2017/7/8.
 */
@XmlBean(name = "UserBean")
public class User {
    @XmlAttribute
    private String name;
    @XmlAttribute
    private Integer age;
    @XmlListNode(name = "Knowledge", nodeType = Book.class)
    private List<Book> books;
    @XmlSingleNode(name = "Writer", nodeType = Author.class)
    private Author author;
    @Ignore()
    private Book vip;

    public User() {
        books = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public Book getVip() {
        return vip;
    }

    public void setVip(Book vip) {
        this.vip = vip;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", books=" + books +
                ", author=" + author +
                ", vip=" + vip +
                '}';
    }
}
