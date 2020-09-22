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
            final var expectedFrame = new Frame(null, "A".getBytes());
            oneOf(link).send(with(equal(expectedFrame)));
        }});

        final var networkAdapter = new NetworkAdapter(null, link);
        networkAdapter.send("A".getBytes());
    }

    @Test
    public void sent_frames_contain_adapters_address() {
        final var networkAdapterAddress = new NetworkAdapterAddress();
        context.checking(new Expectations() {{
            final var expectedFrame = new Frame(networkAdapterAddress, null);
            oneOf(link).send(with(equal(expectedFrame)));
        }});

        final var networkAdapter = new NetworkAdapter(networkAdapterAddress, link);
        networkAdapter.send(null);
    }

    private static class Frame {
        private final NetworkAdapterAddress source;
        private final byte[] data;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Frame frame = (Frame) o;
            return Objects.equals(source, frame.source) &&
              Arrays.equals(data, frame.data);
        }

        @Override
        public int hashCode() {
            int result = Objects.hash(source);
            result = 31 * result + Arrays.hashCode(data);
            return result;
        }

        public Frame(NetworkAdapterAddress networkAdapterAddress, byte[] data) {
            source = networkAdapterAddress;
            this.data = data;
        }

        @Override
        public String toString() {
            return "Frame{" +
              "source=" + source +
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

        public void send(byte[] data) {
            link.send(new Frame(address, data));
        }
    }

    private interface Link {
        void send(Frame frame);
    }

    private static class NetworkAdapterAddress {
    }
}
