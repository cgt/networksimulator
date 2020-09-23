package name.cgt.networksimulator;

import org.jmock.Expectations;
import org.jmock.junit5.JUnit5Mockery;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

public class NetworkAdapterTest {
    @RegisterExtension
    final JUnit5Mockery context = new JUnit5Mockery();

    private final NetworkAdapterAddress adapterAddress = new NetworkAdapterAddress();

    private final Link link = context.mock(Link.class);
    private final FrameListener listener = context.mock(FrameListener.class);

    @Test
    public void sends_frame_to_link() {
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

    @Test
    public void notifies_listener_of_received_frames() {
        context.checking(new Expectations() {{
            oneOf(listener).onFrame(with(equal(new Frame(null, null, null))));
        }});

        final var networkAdapter = new NetworkAdapter(adapterAddress, null, listener);
        networkAdapter.onFrame(new Frame(null, null, null));
    }
}
