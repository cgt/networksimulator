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

        final Link switch_ = new Switch();
        switch_.onConnected(link);
        switch_.onFrame(expectedFrame);
    }

    private static class Switch implements Link {
        private FrameListener frameListener;

        @Override
        public void onConnected(FrameListener link) {
            this.frameListener = link;
        }

        @Override
        public void onFrame(Frame frame) {
            frameListener.onFrame(frame);
        }
    }
}
