package jackson.object;

import java.math.BigDecimal;

public class InnerContract {

        @MetaDescriptor(values = {MetaInfo.TYPE})
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