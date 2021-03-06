package nl.hva.miw.internetbanking.data.dao;

import nl.hva.miw.internetbanking.data.mapper.AccountRowMapper;
import nl.hva.miw.internetbanking.data.mapper.IbanRowMapper;
import nl.hva.miw.internetbanking.model.Account;
import nl.hva.miw.internetbanking.model.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Repository
public class AccountDAO implements DAO<Account, Long> {

    private final JdbcTemplate jdbcTemplate;
    Logger logger = LoggerFactory.getLogger(AccountDAO.class);

    public AccountDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional
    public void create(Account account) {
        String sql = "INSERT INTO account(iban, balance) VALUES (?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"accountID"});
            ps.setString(1, account.getIban());
            ps.setDouble(2,account.getBalance());
            return ps;
        }, keyHolder);
        long id = Objects.requireNonNull(keyHolder.getKey().longValue());
        account.setAccountID(id);
    }

    public void saveAccountToCustomer(Account account, Customer customer) {
        String sql = "INSERT INTO customer_has_account(customerID, accountID) VALUES " +
                "(?,?)";
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, customer.getCustomerID());
            preparedStatement.setLong(2, account.getAccountID());
            return preparedStatement;
        });
    }

    @Override
    public Optional<Account> read(Long accountID) {
        try {
            String sql = "SELECT * FROM account WHERE accountID = ?";
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, new AccountRowMapper(), accountID));
        } catch (EmptyResultDataAccessException e) {
            logger.info("No record found for account " + accountID, e);
            return Optional.empty();
        }
    }

    @Override
    public void update(Account account) {
        String sql = "UPDATE account SET iban = ?, balance = ?" +
                     "WHERE accountID = ?";
        jdbcTemplate.update(sql,
                            account.getIban(),
                            account.getBalance(),
                            account.getAccountID()
                           );
    }

    public Optional<Account> read(String iban) {
        try {
            String sql = "SELECT * FROM account WHERE iban = ?";
            return Optional
                    .ofNullable(jdbcTemplate.queryForObject(sql, new AccountRowMapper(), iban));
        } catch (EmptyResultDataAccessException e) {
            logger.info("No record found for account " + iban, e);
            return Optional.empty();
        }
    }

    @Override
    public void delete(Account account) {

    }

    @Override
    public void deleteById(Long accountID) {

    }

    @Override
    public Optional<List<Account>> list() {
        return Optional.empty();
    }

    public boolean existsByIban(String iban) {
        final String sql = "SELECT iban FROM account WHERE iban = ?";
        return jdbcTemplate.queryForObject(sql, new IbanRowMapper(), iban) != null;
    }



    public List<Account> getAccountsForCustomer(Customer customer) {
        List<Account> accounts = getAccountsByCustomerId(customer.getCustomerID());
        for (Account acc : accounts) {
            acc.addAccountHolder(customer);
            customer.addAccount(acc);
        }
        return accounts;
    }

    public List<Account> getAccountsByCustomerId(long customerID) {
        final String sql = "SELECT customer.customerID, customer.userName, account.accountID, " +
                           "account.iban, account.balance\n" +
                           "FROM customer_has_account JOIN customer ON customer_has_account" +
                           ".customerID=customer.customerID JOIN account ON\n" +
                           "account.accountID=customer_has_account.accountID WHERE customer.customerID = ?";
        return jdbcTemplate.query(sql, new AccountRowMapper(), customerID);
    }

}
