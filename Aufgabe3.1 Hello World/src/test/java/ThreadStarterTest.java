import org.junit.Test;

import java.io.ByteArrayOutputStream;

public class ThreadStarterTest{
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @Test(expected = IllegalArgumentException.class)
    public void testNoParams(){
        ThreadStarter.main(new String[]{});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testManyParams(){
        ThreadStarter.main(new String[]{"asdf", "xyz"});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWrongParams(){
        ThreadStarter.main(new String[]{"asdf"});
    }

}
