package name.cgt.networksimulator;

class NetworkAdapter {
    private final NetworkAdapterAddress address;
    private final Link link;

    public NetworkAdapter(NetworkAdapterAddress address, Link link) {
        this.address = address;
        this.link = link;
    }

    public void send(NetworkAdapterAddress destination, byte[] data) {
        link.send(new Frame(address, destination, data));
    }
}
