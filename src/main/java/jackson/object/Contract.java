package jackson.object;

import java.math.BigDecimal;

public class Contract {

        @MetaDescriptor(values = {MetaInfo.TYPE})
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