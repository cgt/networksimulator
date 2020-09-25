package name.cgt.networksimulator;

import org.jmock.Expectations;
import org.jmock.junit5.JUnit5Mockery;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.HashSet;
import java.util.Set;

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

    @Test
    public void forward_frames_to_all_links() {
        final var link1 = context.mock(Link.class, "link1");
        final var link2 = context.mock(Link.class, "link2");
        final var expectedFrame = new Frame(new NetworkAdapterAddress(), new NetworkAdapterAddress(), null);

        context.checking(new Expectations() {{
            oneOf(link1).onFrame(with(equal(expectedFrame)));
            oneOf(link2).onFrame(with(equal(expectedFrame)));
        }});

        final Link switch_ = new Switch();
        switch_.onConnected(link1);
        switch_.onConnected(link2);
        switch_.onFrame(expectedFrame);
    }

    private static class Switch implements Link {
        private final Set<FrameListener> links = new HashSet<>();

        @Override
        public void onConnected(FrameListener link) {
            links.add(link);
        }

        @Override
        public void onFrame(Frame frame) {
            for (final var link : links) {
                link.onFrame(frame);
            }
        }
    }
}
