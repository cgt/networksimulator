package name.cgt.networksimulator;

import org.jmock.Expectations;
import org.jmock.junit5.JUnit5Mockery;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

public class NetworkAdapterTest {
    @RegisterExtension
    final JUnit5Mockery context = new JUnit5Mockery();

    private final Link link = context.mock(Link.class);

    @Test
    public void sends_frame_to_link() {
        final var adapterAddress = new NetworkAdapterAddress();
        final var destinationAddress = new NetworkAdapterAddress();
        final var message = "message".getBytes();

        context.checking(new Expectations() {{
            oneOf(link).send(with(equal(
              new Frame(adapterAddress, destinationAddress, message)
            )));
        }});

        final var networkAdapter = new NetworkAdapter(adapterAddress, link);
        networkAdapter.send(destinationAddress, message);
    }
}
