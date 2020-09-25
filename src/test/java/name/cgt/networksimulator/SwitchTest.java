package name.cgt.networksimulator;

import org.jmock.Expectations;
import org.jmock.junit5.JUnit5Mockery;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

public class SwitchTest {
    @RegisterExtension
    final JUnit5Mockery context = new JUnit5Mockery();

    @Test
    public void forward_frames_to_link() {
        final var link = context.mock(Link.class);
        final var expectedFrame = new Frame(new NetworkAdapterAddress(), new NetworkAdapterAddress(), null);

        context.checking(new Expectations() {{
            oneOf(link).onFrame(with(equal(expectedFrame)));
        }});

        final Link switch_ = new Switch(link);
        switch_.onFrame(expectedFrame);
    }

    private static class Switch implements Link {
        private final Link link;

        public Switch(Link link) {
            this.link = link;
        }

        @Override
        public void onConnected(FrameListener frameListener) {
        }

        @Override
        public void onFrame(Frame frame) {
            link.onFrame(frame);
        }
    }
}
