package jackson.test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jackson.object.MetaTag;
import jackson.object.ObjectMapperFactory;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;

/**
 * Created by Povka on 2015.12.04.
 */
public class JacksonMaping {

    public static class Contract {

        @MetaTag
        private BigDecimal amount;

        private BigDecimal number;

        private InnerContract innerContract;

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }

        public BigDecimal getNumber() {
            return number;
        }

        public void setNumber(BigDecimal number) {
            this.number = number;
        }

        public InnerContract getInnerContract() {
            return innerContract;
        }

        public void setInnerContract(InnerContract innerContract) {
            this.innerContract = innerContract;
        }
    }

    public static class InnerContract {

        @MetaTag
        private BigDecimal innerAmount;

        private BigDecimal innerNumber;

        public BigDecimal getInnerAmount() {
            return innerAmount;
        }

        public void setInnerAmount(BigDecimal innerAmount) {
            this.innerAmount = innerAmount;
        }

        public BigDecimal getInnerNumber() {
            return innerNumber;
        }

        public void setInnerNumber(BigDecimal innerNumber) {
            this.innerNumber = innerNumber;
        }
    }


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
