package settlersofcatan.client;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class StateTest {
    private State create() {
        return new State();
    }
    
    State testState;

    @Before
    public void setUp() {
        testState = create();
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void test() {
        
    }
}
