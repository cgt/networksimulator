package name.cgt.networksimulator;

import org.jmock.Expectations;
import org.jmock.junit5.JUnit5Mockery;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.Arrays;

public class LinkLayerTest {
    @RegisterExtension
    final JUnit5Mockery context = new JUnit5Mockery();

    private final Link link = context.mock(Link.class);

    @Test
    public void send_bytes_to_link() {
        context.checking(new Expectations() {{
            final var expectedFrame = new Frame("A".getBytes());
            oneOf(link).send(with(equal(expectedFrame)));
        }});

        final var networkAdapter = new NetworkAdapter(link);
        networkAdapter.send("A".getBytes());
    }

    private static class Frame {
        private final byte[] data;

        public Frame(byte[] data) {
            this.data = data;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Frame frame = (Frame) o;
            return Arrays.equals(data, frame.data);
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(data);
        }

        @Override
        public String toString() {
            return "Frame{" +
              "data=" + Arrays.toString(data) +
              '}';
        }
    }

    private static class NetworkAdapter {
        private final Link link;

        public NetworkAdapter(Link link) {
            this.link = link;
        }

        public void send(byte[] data) {
            link.send(new Frame(data));
        }
    }

    private interface Link {
        void send(Frame frame);
    }
}
