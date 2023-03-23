package com.testehan.openliberty.chpt8;

import com.testehan.openliberty.chpt6.Address;
import jakarta.xml.bind.annotation.*;

/*
    <customer id="42">
         <fullname>Bill Burke</fullname>
         <address>
             <street>200 Marlborough Street</street>
             <city>Boston</city>
             <state>MA</state>
             <zip>02115</zip>
         </address>
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

    @XmlElement
    protected Address address;

    public Customer() {}

    public Customer(String fullname) {
        this.fullname = fullname;
    }

    public int getId() { return this.id; }
    public void setId(int id) { this.id = id; }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", fullname='" + fullname + '\'' +
                ", address=" + address +
                '}';
    }
}
