package name.cgt.networksimulator;

class NetworkAdapter implements FrameListener, Link {
    private final NetworkAdapterAddress address;
    private Link link;
    private FrameListener listener;

    public NetworkAdapter(NetworkAdapterAddress address) {
        this.address = address;
    }

    public NetworkAdapter(NetworkAdapterAddress address, FrameListener listener) {
        this(address);
        this.listener = listener;
    }

    public void send(NetworkAdapterAddress destination, byte[] data) {
        link.onFrame(new Frame(address, destination, data));
    }

    public void onFrame(Frame frame) {
        if (listener != null && frame.isFor(address)) {
            listener.onFrame(frame);
        }
    }

    public void connect(Link link) {
        this.link = link;
        link.onConnected(this);
    }

    @Override
    public void onConnected(FrameListener frameListener) {
    }
}
