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
    public void notifies_link_when_connected() {
        final var networkAdapter = new NetworkAdapter(adapterAddress, null);

        context.checking(new Expectations() {{
            oneOf(link).onConnected(networkAdapter);
        }});

        networkAdapter.connect(link);
    }

    @Test
    public void sends_frame_to_link() {
        final var destinationAddress = new NetworkAdapterAddress();
        final var message = "message".getBytes();

        context.checking(new Expectations() {{
            allowing(link).onConnected(with(any(NetworkAdapter.class)));
            oneOf(link).onFrame(with(equal(
              new Frame(adapterAddress, destinationAddress, message)
            )));
        }});

        final var networkAdapter = new NetworkAdapter(adapterAddress);
        networkAdapter.connect(link);
        networkAdapter.send(destinationAddress, message);
    }

    @Test
    public void does_not_notify_listener_if_no_listener_registered() {
        final var aFrame = new Frame(null, null, null);
        final var networkAdapter = new NetworkAdapter(adapterAddress);
        networkAdapter.onFrame(aFrame);
    }

    @Test
    public void notifies_listener_of_received_frames_destined_for_adapter() {
        final var source = new NetworkAdapterAddress();
        final var aFrame = new Frame(source, adapterAddress, null);

        context.checking(new Expectations() {{
            oneOf(listener).onFrame(with(equal(aFrame)));
        }});

        final var networkAdapter = new NetworkAdapter(adapterAddress, listener);
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

        final var networkAdapter = new NetworkAdapter(adapterAddress, listener);
        networkAdapter.onFrame(aFrame);
    }

    @Test
    public void connect_two_adapters_directly() {
        final var a = new NetworkAdapter(new NetworkAdapterAddress());
        final var b = new NetworkAdapter(new NetworkAdapterAddress(), listener);

        context.checking(new Expectations() {{
            oneOf(listener).onFrame(new Frame(a.address(), b.address(), "hello".getBytes()));
        }});

        a.connect(b);
        a.send(b.address(), "hello".getBytes());
    }
}
