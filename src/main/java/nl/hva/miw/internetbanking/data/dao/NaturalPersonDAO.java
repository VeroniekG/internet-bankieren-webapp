package nl.hva.miw.internetbanking.data.dao;

import nl.hva.miw.internetbanking.data.mapper.NaturalPersonRowMapper;
import nl.hva.miw.internetbanking.model.NaturalPerson;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@Repository
public class NaturalPersonDAO implements DAO<NaturalPerson, Long> {

    private final JdbcTemplate jdbcTemplate;

    public NaturalPersonDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional
    public void create(NaturalPerson naturalPerson) {
        String sql = "INSERT INTO naturalperson(customerID, initials, firstName, preposition, " +
                "surName, dateOfBirth, socialSecurityNumber, email, phone, postalCode, " +
                "homeNumber, street, residence) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setLong(1, naturalPerson.getCustomerID());
            ps.setString(2, naturalPerson.getInitials());
            ps.setString(3, naturalPerson.getFirstName());
            ps.setString(4, naturalPerson.getPreposition());
            ps.setString(5, naturalPerson.getSurName());
            ps.setString(6, naturalPerson.getDateOfBirth());
            ps.setString(7, naturalPerson.getSocialSecurityNumber());
            ps.setString(8, naturalPerson.getEmail());
            ps.setString(9, naturalPerson.getPhone());
            ps.setString(10, naturalPerson.getPostalCode());
            ps.setString(11, naturalPerson.getHomeNumber());
            ps.setString(12, naturalPerson.getStreet());
            ps.setString(13, naturalPerson.getResidence());
            return ps;
        });
    }

    @Override
    public Optional<NaturalPerson> read(Long id) {
        String sql = "SELECT * FROM naturalperson WHERE customerID = ?";
        return Optional.ofNullable(jdbcTemplate.queryForObject(sql, new NaturalPersonRowMapper(),
                id));
    }

    @Override
    public void update(NaturalPerson naturalPerson) {
        String sql = "UPDATE naturalperson SET initials = ?, firstName = ?, preposition = ?" +
                ", surName = ?, dateOfBirth = ?, socialSecurityNumber = ?, email = ?" +
                ", phone = ?, postalCode = ?, homeNumber = ?, street = ?, residence = ? " +
                "WHERE customerID = ?";
        jdbcTemplate.update(sql, naturalPerson.getInitials(), naturalPerson.getFirstName(),
                naturalPerson.getPreposition(), naturalPerson.getSurName(),
                naturalPerson.getDateOfBirth(),
                naturalPerson.getSocialSecurityNumber(), naturalPerson.getEmail(),
                naturalPerson.getPhone(),
                naturalPerson.getPostalCode(), naturalPerson.getHomeNumber(),
                naturalPerson.getStreet(),
                naturalPerson.getResidence(), naturalPerson.getCustomerID());
    }

    @Override
    public void delete(NaturalPerson naturalPerson) {
        jdbcTemplate.update("DELETE FROM naturalperson WHERE customerID = ?",
                naturalPerson.getCustomerID());
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM naturalperson WHERE customerID = ?", id);
    }

    @Override
    public Optional<List<NaturalPerson>> list() {
        String sql = "SELECT * FROM naturalperson";
        return Optional.of(jdbcTemplate.query(sql, new NaturalPersonRowMapper()));
    }

    public Optional<NaturalPerson> read(String socialSecurityNumber) {
        String sql = "SELECT * FROM naturalperson WHERE socialSecurityNumber = ?";
        return Optional.ofNullable(jdbcTemplate.queryForObject(sql, new NaturalPersonRowMapper(),
                socialSecurityNumber));
    }

    public boolean existsBySocialSecurityNumber(String socialSecurityNumber) {
        final String sql = "SELECT * FROM naturalperson WHERE socialSecurityNumber = ?";
        return jdbcTemplate.queryForObject(sql, new NaturalPersonRowMapper(),
                socialSecurityNumber) != null;
    }
}
