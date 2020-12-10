package nl.hva.miw.internetbanking.repository;

import nl.hva.miw.internetbanking.model.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

// aangemaakt door Nina 09-12-2020

@Repository
public class JdbcAccountDAO implements AccountDAO {

    private Logger logger = LoggerFactory.getLogger(JdbcAccountDAO.class);

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcAccountDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        logger.warn("New JdbcAccountDAO.");
    }

    @Override
    public List<Account> getAccountsByCustomerId(long customerID) {
        final String sql = "SELECT customer.customerid, customer.username, account.accountid, account.iban, account.balance\n" +
                "FROM customer_has_account JOIN customer ON customer_has_account.customerID=customer.customerID JOIN account ON\n" +
                "account.accountID=customer_has_account.accountID WHERE customer.customerID = ?";
        return jdbcTemplate.query(sql, new AccountRowMapper(), customerID);
    }

    @Override
    public void saveAccount(Account account) {
    }
}
