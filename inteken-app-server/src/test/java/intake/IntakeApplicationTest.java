package intake;

import org.junit.Test;

public class IntakeApplicationTest {

    @Test
    public void main() {
        IntakeAppApplication.main(new String[]{"--server.port=8066"});
    }
}