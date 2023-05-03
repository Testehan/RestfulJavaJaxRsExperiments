package com.testehan.openliberty.chpt10;

import jakarta.xml.bind.annotation.*;

/*
    <customer id="42">
         <fullname>Bill Burke</fullname>
    </customer>

    If you want to use the JAXB on openliberty you need to enable the feature:
         <feature>xmlBinding-3.0</feature>
*/

@XmlRootElement(name="customer")
@XmlAccessorType(XmlAccessType.FIELD)
public class Customer {
    @XmlAttribute
    protected int id;
    @XmlElement
    protected String fullname;


    public Customer() {}

    public int getId() { return this.id; }
    public void setId(int id) { this.id = id; }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", fullname='" + fullname + '\'' +
                '}';
    }
}
