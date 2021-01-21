package nl.hva.miw.internetbanking.data.dao;

import lombok.extern.slf4j.Slf4j;
import nl.hva.miw.internetbanking.data.mapper.LegalPersonRowMapper;
import nl.hva.miw.internetbanking.model.LegalPerson;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class LegalPersonDAO implements DAO<LegalPerson, Long> {

    private final JdbcTemplate jdbcTemplate;

    public LegalPersonDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional
    public void create(LegalPerson legalPerson) {
        String sql = "INSERT INTO legalperson(companyID, companyName, kvkNumber, sector, " +
                "vatNumber, postalCode, homeNumber, street, residence, accountmanagerID) " +
                "VALUES" +
                "(?,?,?,?,?,?,?,?,?,?)";
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setLong(1, legalPerson.getCustomerID());
            ps.setString(2, legalPerson.getCompanyName());
            ps.setLong(3, legalPerson.getKvkNumber());
            ps.setString(4, legalPerson.getSector().name());
            ps.setString(5, legalPerson.getVatNumber());
            ps.setString(6, legalPerson.getPostalCode());
            ps.setString(7, legalPerson.getHomeNumber());
            ps.setString(8, legalPerson.getStreet());
            ps.setString(9, legalPerson.getResidence());
            ps.setLong(10, legalPerson.getAccountmanagerID());
            return ps;
        });
    }

    @Override
    public Optional<LegalPerson> read(Long id) {
        try {
            String sql = "SELECT * FROM legalperson WHERE companyID = ?";
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, new LegalPersonRowMapper(),
                    id));
        } catch (Exception e) {
            log.error("LegalPerson not found in database.");
        }
        return null;
    }

    @Override
    public void update(LegalPerson legalPerson) {
        String sql = "UPDATE legalperson SET companyID = ?, companyName = ?, kvkNumber = ?, " +
                "sector = ?, vatNumber = ?, postalCode = ?, homeNumber = ?, street = ?, " +
                "residence" +
                " = ?, accountmanagerID = ?";
        jdbcTemplate.update(sql, legalPerson.getCustomerID(), legalPerson.getCompanyName(),
                legalPerson.getKvkNumber(), legalPerson.getSector(), legalPerson.getVatNumber(),
                legalPerson.getPostalCode(), legalPerson.getHomeNumber(), legalPerson.getStreet(),
                legalPerson.getResidence(), legalPerson.getAccountmanagerID());
    }

    @Override
    public void delete(LegalPerson legalPerson) {
        jdbcTemplate.update("DELETE FROM legalperson WHERE companyID = ?",
                legalPerson.getCustomerID());
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM legalperson WHERE companyID = ?", id);
    }

    @Override
    public Optional<List<LegalPerson>> list() {
        String sql = "SELECT * FROM legalperson";
        return Optional.of(jdbcTemplate.query(sql, new LegalPersonRowMapper()));
    }

    public Optional<LegalPerson> readByKvkNumber(long kvkNumber) {
        String sql = "SELECT * FROM legalperson WHERE kvkNumber = ?";
        return Optional
                .ofNullable(
                        jdbcTemplate.queryForObject(sql, new LegalPersonRowMapper(), kvkNumber));
    }
}
