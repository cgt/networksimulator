package name.cgt.networksimulator;

class NetworkAdapter {
    private final NetworkAdapterAddress address;
    private final Link link;
    private FrameListener listener;

    public NetworkAdapter(NetworkAdapterAddress address, Link link) {
        this.address = address;
        this.link = link;
    }

    public NetworkAdapter(NetworkAdapterAddress address, Link link, FrameListener listener) {
        this(address, link);
        this.listener = listener;
    }

    public void send(NetworkAdapterAddress destination, byte[] data) {
        link.send(new Frame(address, destination, data));
    }

    public void onFrame(Frame frame) {
        if (listener != null && frame.isFor(address)) {
            listener.onFrame(frame);
        }
    }
}
