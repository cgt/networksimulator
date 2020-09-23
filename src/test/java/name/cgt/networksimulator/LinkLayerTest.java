package name.cgt.networksimulator;

import org.jmock.Expectations;
import org.jmock.junit5.JUnit5Mockery;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.Arrays;
import java.util.Objects;

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

    private static class Frame {
        private final NetworkAdapterAddress source;
        private final NetworkAdapterAddress destination;
        private final byte[] data;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Frame frame = (Frame) o;
            return Objects.equals(source, frame.source) &&
              Objects.equals(destination, frame.destination) &&
              Arrays.equals(data, frame.data);
        }

        @Override
        public int hashCode() {
            int result = Objects.hash(source, destination);
            result = 31 * result + Arrays.hashCode(data);
            return result;
        }

        public Frame(NetworkAdapterAddress networkAdapterAddress, NetworkAdapterAddress destinationAddress, byte[] data) {
            source = networkAdapterAddress;
            destination = destinationAddress;
            this.data = data;
        }

        @Override
        public String toString() {
            return "Frame{" +
              "source=" + source +
              ", destination=" + destination +
              ", data=" + Arrays.toString(data) +
              '}';
        }
    }

    private static class NetworkAdapter {
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

    private interface Link {
        void send(Frame frame);
    }

    private static class NetworkAdapterAddress {
    }
}
