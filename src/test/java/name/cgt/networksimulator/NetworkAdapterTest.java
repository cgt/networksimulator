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
    public void does_not_notify_listener_if_no_listener_registered() {
        final var aFrame = new Frame(null, null, null);
        final var networkAdapter = new NetworkAdapter(adapterAddress, null);
        networkAdapter.onFrame(aFrame);
    }

    @Test
    public void notifies_listener_of_received_frames_destined_for_adapter() {
        final var source = new NetworkAdapterAddress();
        final var aFrame = new Frame(source, adapterAddress, null);

        context.checking(new Expectations() {{
            oneOf(listener).onFrame(with(equal(aFrame)));
        }});

        final var networkAdapter = new NetworkAdapter(adapterAddress, null, listener);
        networkAdapter.onFrame(aFrame);
    }

    @Test
    public void does_not_notify_listener_of_frames_not_destined_for_adapter() {
        final var source = new NetworkAdapterAddress();
        final var someOtherAdapter = new NetworkAdapterAddress();
        final var aFrame = new Frame(source, someOtherAdapter, null);

        context.checking(new Expectations() {{
            never(listener);
        }});

        final var networkAdapter = new NetworkAdapter(adapterAddress, null, listener);
        networkAdapter.onFrame(aFrame);
    }

    @Test
    public void send_frame_from_one_adapter_to_another() {
        final var directLink = new Link() {
            NetworkAdapter destination;

            @Override
            public void send(Frame frame) {
                destination.onFrame(frame);
            }
        };
        final var sourceAddress = new NetworkAdapterAddress();
        final var sourceAdapter = new NetworkAdapter(sourceAddress, directLink);
        final var destinationAddress = new NetworkAdapterAddress();
        final var destinationListener = context.mock(FrameListener.class, "destinationListener");
        final var destinationAdapter = new NetworkAdapter(destinationAddress, directLink, destinationListener);
        directLink.destination = destinationAdapter;

        final var frameData = "payload".getBytes();

        context.checking(new Expectations() {{
            final var expectedFrame = new Frame(sourceAddress, destinationAddress, frameData);
            oneOf(destinationListener).onFrame(with(equal(expectedFrame)));
        }});

        sourceAdapter.send(destinationAddress, frameData);
    }
}
