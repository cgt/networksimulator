package name.cgt.networksimulator;

import org.jmock.Expectations;
import org.jmock.junit5.JUnit5Mockery;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

public class LinkLayerTest {
    @RegisterExtension
    final JUnit5Mockery context = new JUnit5Mockery();

    private final Link link = context.mock(Link.class);

    @Test
    public void send_bytes_to_link() {
        context.checking(new Expectations() {{
            final var expectedFrame = new Frame(null, null, "A".getBytes());
            oneOf(link).send(with(equal(expectedFrame)));
        }});

        final var networkAdapter = new NetworkAdapter(null, link);
        networkAdapter.send(null, "A".getBytes());
    }

    @Test
    public void sent_frames_contain_adapters_address() {
        final var networkAdapterAddress = new NetworkAdapterAddress();
        context.checking(new Expectations() {{
            final var expectedFrame = new Frame(networkAdapterAddress, null, null);
            oneOf(link).send(with(equal(expectedFrame)));
        }});

        final var networkAdapter = new NetworkAdapter(networkAdapterAddress, link);
        networkAdapter.send(null, null);
    }

    @Test
    public void sent_frames_contain_destination_address() {
        final var destinationAddress = new NetworkAdapterAddress();
        context.checking(new Expectations() {{
            final var expectedFrame = new Frame(null, destinationAddress, null);
            oneOf(link).send(with(equal(expectedFrame)));
        }});
        final var networkAdapter = new NetworkAdapter(null, link);
        networkAdapter.send(destinationAddress, null);
    }

    static class NetworkAdapter {
        private final NetworkAdapterAddress address;
        private final Link link;

        public NetworkAdapter(NetworkAdapterAddress address, Link link) {
            this.address = address;
            this.link = link;
        }

        public void send(NetworkAdapterAddress destination, byte[] data) {
            link.send(new Frame(address, destination, data));
        }
    }
}
