package jackson.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import jackson.object.Contract;
import jackson.object.InnerContract;
import jackson.object.ObjectMapperFactory;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;

/**
 * Created by Povka on 2015.12.04.
 */
public class JacksonMaping {

    @Test
    public void testMapping() throws IOException {
        ObjectMapperFactory factory = new ObjectMapperFactory();
        ObjectMapper mapper = factory.createMapper();

        Contract contract = new Contract();
        contract.setAmount(new BigDecimal("200"));
        contract.setNumber(new BigDecimal("100"));
        InnerContract innerContract = new InnerContract();
        innerContract.setInnerAmount(new BigDecimal("200"));
        innerContract.setInnerNumber(new BigDecimal("100"));
        contract.setInnerContract(innerContract);

        StringWriter writer = new StringWriter();
        mapper.writeValue(writer, contract);
        System.out.println(writer.toString());
    }
}
