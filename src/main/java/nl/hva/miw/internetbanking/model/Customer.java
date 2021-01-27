package nl.hva.miw.internetbanking.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Customer implements Serializable {

    public static final String EMPTY_STRING = "";
    public static final long ZERO = 0L;
    private static final long serialVersionUID = 6793528094836886930L;

    private long customerID;
    private String userName;
    private String password;
    private List<Account> accounts;
    private CustomerType customerType;
    private String customerName;


    public Customer(String userName, String password, CustomerType customerType) {
        this.userName = userName;
        this.password = password;
        this.accounts = new ArrayList<>();
        this.customerType = customerType;
    }

    public Customer(long customerID, String userName, String password, CustomerType customerType) {
        this.customerID = customerID;
        this.userName = userName;
        this.password = password;
        this.customerType = customerType;
        this.accounts = new ArrayList<>();
    }

    public Customer(long id) {
        this.customerID = id;
    }
    
    public void addAccount(Account acc) {
        accounts.add(acc);
    }
    
    public void addCustomerName(String name) {
    
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj != null && obj.getClass() == getClass()) {
            return Objects.equals(customerID, ((Customer) obj).customerID) &&
                    Objects.equals(userName, ((Customer) obj).userName) &&
                    Objects.equals(password, ((Customer) obj).password) &&
                    Objects.equals(accounts, ((Customer) obj).accounts) &&
                    Objects.equals(customerType, ((Customer) obj).customerType) &&
                    Objects.equals(customerName, ((Customer) obj).customerName);
//            return customerID == ((Customer) obj).customerID &&
//                    userName == ((Customer) obj).userName &&
//                    password == ((Customer) obj).password &&
//                    accounts == ((Customer) obj).accounts &&
//                    customerType == ((Customer) obj).customerType &&
//                    customerName == ((Customer) obj).customerName;
        }
        return false;
    }
    
}
