package broker;

import org.junit.Test;

public class BrokerApplicationTest {

    @Test
    public void main() {
        BrokerApplication.main(new String[]{"--server.port=8066"});
    }
}