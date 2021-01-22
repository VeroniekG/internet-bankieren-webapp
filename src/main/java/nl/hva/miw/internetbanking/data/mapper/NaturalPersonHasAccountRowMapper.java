package nl.hva.miw.internetbanking.data.mapper;

import nl.hva.miw.internetbanking.data.dto.NaturalPersonHasAccountDTO;
import nl.hva.miw.internetbanking.model.Account;
import nl.hva.miw.internetbanking.model.NaturalPerson;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class NaturalPersonHasAccountRowMapper implements RowMapper<NaturalPersonHasAccountDTO> {

    @Override
    public NaturalPersonHasAccountDTO mapRow(ResultSet resultSet, int i) throws SQLException {
        NaturalPerson np = new NaturalPerson(resultSet.getString("firstName"),
                resultSet.getString("preposition"), resultSet.getString("surName"),
                resultSet.getString("dateOfBirth"), resultSet.getString("address"),
                resultSet.getString("email"), resultSet.getString("phone"));
        Account account = new Account(resultSet.getString("iban"), resultSet.getDouble("balance"));
        return new NaturalPersonHasAccountDTO(np, account);
    }
}
