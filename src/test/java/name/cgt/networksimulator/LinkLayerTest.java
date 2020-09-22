package name.cgt.networksimulator;

import org.jmock.Expectations;
import org.jmock.junit5.JUnit5Mockery;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

public class LinkLayerTest {
    @RegisterExtension
    final JUnit5Mockery context = new JUnit5Mockery();

    @Test
    public void send_empty_frame_to_link() {
        final var link = context.mock(Link.class);
        context.checking(new Expectations() {{
            oneOf(link).send(with(aNonNull(Frame.class)));
        }});

        final var networkAdapter = new NetworkAdapter(link);
        networkAdapter.send();
    }

    private static class Frame {
    }

    private static class NetworkAdapter {
        private final Link link;

        public NetworkAdapter(Link link) {
            this.link = link;
        }

        public void send() {
            link.send(new Frame());
        }
    }

    private interface Link {
        void send(Frame frame);
    }
}
